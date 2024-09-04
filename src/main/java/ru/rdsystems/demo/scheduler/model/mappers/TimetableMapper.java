package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.CreateSchedule200Response;

@Mapper(componentModel = "spring")
public interface TimetableMapper {

	CreateSchedule200Response map(TimetableEntity entity);

}
