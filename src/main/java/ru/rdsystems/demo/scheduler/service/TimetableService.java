package ru.rdsystems.demo.scheduler.service;

import org.springframework.data.domain.Page;
import ru.rdsystems.demo.scheduler.model.api.TimetableCreateField;
import ru.rdsystems.demo.scheduler.model.api.TimetableFilterAndSorting;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;

import java.util.Map;
import java.util.Optional;

public interface TimetableService {

	Optional<TimetableEntity> createTimetable(TimetableCreateField timetableCreateField);

	TimetableEntity getById(String id);

	Page<TimetableEntity> getTimetablesForFilters(TimetableFilterAndSorting filterAndSorting);
}
