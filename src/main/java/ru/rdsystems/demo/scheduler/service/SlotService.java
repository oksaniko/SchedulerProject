package ru.rdsystems.demo.scheduler.service;

import ru.rdsystems.demo.scheduler.model.entity.SlotEntity;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SlotService {

	Optional<SlotEntity> createSlot(String templateId, LocalTime begtime, LocalTime endtime, List<String> errorList);

	SlotEntity getById(String id);

	SlotEntity getByBegEndTime(LocalTime begtime, LocalTime endtime);
}
