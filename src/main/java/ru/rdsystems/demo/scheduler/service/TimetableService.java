package ru.rdsystems.demo.scheduler.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;
import ru.rdsystems.demo.scheduler.repository.TimetableRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimetableService {
	private final TimetableRepository repository;
	private final SlotService slotService;
	private final ScheduleService scheduleService;
	private final EmployeeService employeeService;

	@Transactional
	public Optional<TimetableEntity> createTimetable(String scheduleName, String slotTypeStr,
													 LocalTime slotBegTime, LocalTime slotEndTime, String adminId,
													 String executorName, List<String> errorList){
		TimetableEntity timetable = null;
		TimetableEntity.SlotType slotType;
		try {
			try {
				slotType = TimetableEntity.SlotType.valueOf(slotTypeStr);
			} catch (IllegalArgumentException ia){
				throw new IllegalArgumentException("Тип слота " + slotTypeStr + " не определен в справочнике");
			}
			ScheduleEntity schedule = scheduleService.getByName(scheduleName);
			EmployeeEntity admin = employeeService.getAdminById(adminId)
					.orElseThrow(() -> new EntityNotFoundException("Администратор (id = " + adminId + ") не найден"));
			EmployeeEntity executor = employeeService.getByName(executorName);
			if(!repository.findIntersectTime(
					(executor.equals(admin) ? admin : executor).getId(),
					schedule.getId(), slotBegTime, slotEndTime).isEmpty()){
				throw new RuntimeException("В расписании сотрудника обнаружены пересекающиеся периоды");
			}
			timetable = new TimetableEntity(
					UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
					slotService.getByBegEndTime(slotBegTime, slotEndTime),
					schedule, slotType, admin,
					executor.equals(admin) ? null : executor
			);
			repository.save(timetable);
		}catch (Exception er){
			errorList.add(er.getMessage());
		}
		return Optional.ofNullable(timetable);
	}

	public TimetableEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Период (id = " + id + ") не найден"));
	}

}
