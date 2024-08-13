package ru.rdsystems.demo.scheduler.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;
import ru.rdsystems.demo.scheduler.repository.ScheduleRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final ScheduleRepository repository;

	@Transactional
	public Optional<ScheduleEntity> createSchedule(String name, List<String> errorList){
		ScheduleEntity schedule = null;
		try{
			schedule = new ScheduleEntity(
					UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
					name,
					ZonedDateTime.now(ZoneId.of("Europe/Moscow")),
					List.of()
			);
			repository.save(schedule);
		} catch (Exception e){
			errorList.add(e.getMessage());
		}
		return Optional.ofNullable(schedule);
	}

	public ScheduleEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Расписание (id = " + id + ") не найдено"));
	}

	public ScheduleEntity getByName(String name){
		return repository.findFirstByName(name)
				.orElseThrow(() -> new EntityNotFoundException("Расписание с именем " + name + " не найдено"));
	}

	public List<Map<String, Object>> getScheduleInfo(String id, String name){
		return repository.getScheduleInfo(id, name);
	}
}
