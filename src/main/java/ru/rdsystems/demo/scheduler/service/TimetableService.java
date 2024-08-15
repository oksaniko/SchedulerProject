package ru.rdsystems.demo.scheduler.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;
import ru.rdsystems.demo.scheduler.repository.TimetableRepository;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableService {
	private final TimetableRepository repository;
	private final SlotService slotService;
	private final ScheduleService scheduleService;
	private final EmployeeService employeeService;

	private TimetableEntity.SlotType getSlotTypeByStr(String typeStr){
		TimetableEntity.SlotType slotType;
		try {
			slotType = TimetableEntity.SlotType.valueOf(typeStr);
		} catch (IllegalArgumentException ia){
			throw new IllegalArgumentException("Тип слота " + typeStr + " не определен в справочнике");
		}
		return slotType;
	}

	@Transactional
	public Optional<TimetableEntity> createTimetable(String scheduleName, String slotTypeStr,
													 LocalTime slotBegTime, LocalTime slotEndTime, String adminId,
													 String executorName, List<String> errorList){
		TimetableEntity timetable = null;
		try {
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
					schedule, getSlotTypeByStr(slotTypeStr), admin,
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
				.orElseThrow(() -> new EntityNotFoundException("Расписание (id = " + id + ") не найдено"));
	}

	private Specification<TimetableEntity> getSpecification(TimetableFilter filter){
		Specification<TimetableEntity> resultSpecification = Specification.allOf();
		List<Specification<TimetableEntity>> predicate = new ArrayList<>();
		if(filter != null) {
			if(filter.getId() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), filter.getId()));
			if(filter.getSlotId() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("slot"),
						slotService.getById(filter.getSlotId())));
			if(filter.getScheduleId() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("schedule"),
						scheduleService.getById(filter.getScheduleId())));
			if(filter.getSlotType() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("slotType"),
						getSlotTypeByStr(filter.getSlotType())));
			if(filter.getAdministratorId() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("administrator"),
						employeeService.getById(filter.getAdministratorId())));
			if(filter.getExecutorId() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("executor"),
						employeeService.getById(filter.getExecutorId())));
			resultSpecification = Specification.allOf(predicate);
		}
		return resultSpecification;
	}

	public Map<String, Object> getTimetablesForFilters(TimetableFilter filter,
													   Sort sort, Integer page, Integer size)	{
		Map<String, Object> resultSet;
		if(page != null && size != null){
			if(sort != null)
				resultSet = Map.of("timetables", repository.findAll(
						getSpecification(filter), PageRequest.of(page, size, sort)));
			else
				resultSet = Map.of("timetables", repository.findAll(
						getSpecification(filter), PageRequest.of(page, size)));
		} else if(sort != null)
			resultSet = Map.of("timetables", repository.findAll(getSpecification(filter), sort));
		else
			resultSet = Map.of("timetables", repository.findAll(getSpecification(filter)));
		return resultSet;
	}

}
