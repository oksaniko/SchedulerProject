package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;
import ru.rdsystems.demo.scheduler.schedulerApi.CreateSchedule200Response;
import ru.rdsystems.demo.scheduler.schedulerApi.GetSchedule200Response;

import java.util.Map;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

	CreateSchedule200Response map(ScheduleEntity entity);

	@Mapping(target = "schedule", expression = " java( scheduleInfo.get(\"schedule\").toString() )")
	@Mapping(target = "slot", expression = " java( scheduleInfo.get(\"slot\").toString() )")
	@Mapping(target = "beginTime", expression = " java( scheduleInfo.get(\"begin_time\").toString() )")
	@Mapping(target = "endTime", expression = " java( scheduleInfo.get(\"end_time\").toString() )")
	@Mapping(target = "template", expression = " java( scheduleInfo.get(\"template\").toString() )")
	@Mapping(target = "executor", expression = " java( scheduleInfo.get(\"executor\").toString() )")
	@Mapping(target = "admin", expression = " java( scheduleInfo.get(\"admin\").toString() )")
	GetSchedule200Response map(Map<String, Object> scheduleInfo);

}
