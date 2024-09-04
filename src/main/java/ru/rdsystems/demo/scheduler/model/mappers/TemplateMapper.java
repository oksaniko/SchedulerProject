package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import ru.rdsystems.demo.scheduler.model.entity.TemplateEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.CreateSchedule200Response;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

	CreateSchedule200Response map(TemplateEntity entity);

}
