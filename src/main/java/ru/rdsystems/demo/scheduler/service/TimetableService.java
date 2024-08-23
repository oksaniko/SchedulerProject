package ru.rdsystems.demo.scheduler.service;

import ru.rdsystems.demo.scheduler.model.api.TimetableCreateField;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;

import java.util.Map;
import java.util.Optional;

public interface TimetableService {

	Optional<TimetableEntity> createTimetable(TimetableCreateField timetableCreateField);

	TimetableEntity getById(String id);

	Map<String, Object> getTimetablesForFilters(Map<String, Object> filterMap,
												Map<String, String> sortMap, Integer page, Integer size);
}
