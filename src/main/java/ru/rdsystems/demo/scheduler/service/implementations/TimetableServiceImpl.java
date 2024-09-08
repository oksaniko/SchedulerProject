package ru.rdsystems.demo.scheduler.service.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.api.*;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;
import ru.rdsystems.demo.scheduler.repository.TimetableRepository;
import ru.rdsystems.demo.scheduler.service.TimetableService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {
	private final TimetableRepository repository;
	private final SlotServiceImpl slotService;
	private final ScheduleServiceImpl scheduleService;
	private final EmployeeServiceImpl employeeService;

	private TimetableEntity.SlotType getSlotTypeByStr(String typeStr){
		TimetableEntity.SlotType slotType;
		try {
			slotType = TimetableEntity.SlotType.valueOf(typeStr);
		} catch (IllegalArgumentException ia){
			throw new IllegalArgumentException("Тип слота " + typeStr + " не определен в справочнике");
		}
		return slotType;
	}

	@Override
	@Transactional
	public Optional<TimetableEntity> createTimetable(TimetableCreateField timetableCreateField){
		TimetableEntity timetable = null;
		try {
			ScheduleEntity schedule = scheduleService.getByName(timetableCreateField.scheduleName());
			EmployeeEntity admin = employeeService.getAdminById(timetableCreateField.adminId())
					.orElseThrow(() -> new EntityNotFoundException("Администратор (id = " + timetableCreateField.adminId() + ") не найден"));
			EmployeeEntity executor = employeeService.getByName(timetableCreateField.executorName());
			if(!repository.findIntersectTime(
					(executor.equals(admin) ? admin : executor).getId(),
					schedule.getId(), timetableCreateField.slotBegTime(), timetableCreateField.slotEndTime()).isEmpty()){
				throw new IllegalArgumentException("В расписании сотрудника обнаружены пересекающиеся периоды");
			}
			timetable = new TimetableEntity(
					UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
					slotService.getByBegEndTime(timetableCreateField.slotBegTime(), timetableCreateField.slotEndTime()),
					schedule, getSlotTypeByStr(timetableCreateField.slotTypeStr()), admin,
					executor.equals(admin) ? null : executor
			);
			repository.save(timetable);
		}catch (Exception er){
			timetableCreateField.errorList().add(er.getMessage());
		}
		return Optional.ofNullable(timetable);
	}

	@Override
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
			if(filter.getBeginTime() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("slot").get("beginTime"),
						filter.getBeginTime()));
			if(filter.getEndTime() != null)
				predicate.add((root, query, criteriaBuilder) -> criteriaBuilder.lessThan(root.get("slot").get("endTime"),
						filter.getEndTime()));
			resultSpecification = Specification.allOf(predicate);
		}
		return resultSpecification;
	}

	private String getFieldSort(TimetableSortField sortField){
		switch (sortField){
			case id:
				return "id";
			case slotId:
				return "slot.id";
			case scheduleId:
				return "schedule.id";
			case slotType:
				return "slotType";
			case administratorId:
				return "administrator.id";
			case executorId:
				return "executor.id";
			case beginTime:
				return "slot.beginTime";
			case endTime:
				return "slot.endTime";
		}
		return null;
	}

	private Sort getSortByJson(TimetableSort sort){
		Sort resultSort;
		try{
			resultSort = Sort.by(sort.getDirection(), getFieldSort(sort.getField()));
		} catch (IllegalArgumentException ia){
			throw new IllegalArgumentException("Направление сортировки " + sort.getDirection() + " не определено");
		}
		return resultSort;
	}

	@Override
	public Map<String, Object> getTimetablesForFilters(TimetableFilterAndSorting filterAndSorting)	{
		Map<String, Object> resultSet;
		Sort sort = getSortByJson(filterAndSorting.getSort());
		Integer page = filterAndSorting.getPage() == null ? 0 : filterAndSorting.getPage();
		Integer size = filterAndSorting.getSize() == null ? 5 : filterAndSorting.getSize();
		Specification<TimetableEntity> specification = getSpecification(filterAndSorting.getFilter());
		if(sort != null)
			resultSet = Map.of("timetables", repository.findAll(
				specification, PageRequest.of(page, size, sort)));
		else
			resultSet = Map.of("timetables", repository.findAll(
				specification, PageRequest.of(page, size)));
		return resultSet;
	}

}
