package ru.rdsystems.demo.scheduler.service;

import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ScheduleService {

	Optional<ScheduleEntity> createSchedule(String name, List<String> errorList);

	ScheduleEntity getById(String id);

	ScheduleEntity getByName(String name);

	List<Map<String, Object>> getScheduleInfo(String id, String name);
}
