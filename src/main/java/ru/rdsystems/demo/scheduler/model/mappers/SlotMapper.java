package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rdsystems.demo.scheduler.model.entity.SlotEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.CreateSchedule200Response;

@Mapper(componentModel = "spring")
public interface SlotMapper {

	@Mapping(target = "id", expression = " java( entity.getId() )")
	CreateSchedule200Response map(SlotEntity entity);

}
