package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rdsystems.demo.scheduler.model.entity.*;
import ru.rdsystems.demo.scheduler.schedulerApi.*;

import java.time.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TimetableInfoMapper {

	GetTimetableForFilters200ResponseInner map(TimetableEntity entity);

	List<GetTimetableForFilters200ResponseInner> map(List<TimetableEntity> list);

	@Mapping(target = "timetableForAdmin", ignore = true)
	@Mapping(target = "timetablesForExecute", ignore = true)
	Employee map(EmployeeEntity entity);

	@Mapping(target = "timetables", ignore = true)
	Schedule map(ScheduleEntity entity);

	Slot map(SlotEntity entity);

	@Mapping(target = "slots", ignore = true)
	Template map(TemplateEntity entity);

	default OffsetDateTime map(LocalTime value){
		LocalDate localDate = LocalDate.of(1900, 1, 1);
		return value.atDate(localDate).atOffset(ZoneOffset.UTC);
	}

	default OffsetDateTime map(ZonedDateTime value){
		return value.toOffsetDateTime();
	}

}
