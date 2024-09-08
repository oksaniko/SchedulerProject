package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.Employee;

import java.time.*;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	Employee map(EmployeeEntity employee);

	default OffsetDateTime map(LocalTime value){
		LocalDate localDate = LocalDate.of(1900, 1, 1);
		return value.atDate(localDate).atOffset(ZoneOffset.UTC);
	}

	default OffsetDateTime map(ZonedDateTime value){
		return value.toOffsetDateTime();
	}

}
