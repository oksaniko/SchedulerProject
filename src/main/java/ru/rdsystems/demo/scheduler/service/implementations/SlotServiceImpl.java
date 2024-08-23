package ru.rdsystems.demo.scheduler.service.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.SlotEntity;
import ru.rdsystems.demo.scheduler.repository.SlotRepository;
import ru.rdsystems.demo.scheduler.service.SlotService;
import ru.rdsystems.demo.scheduler.service.TemplateService;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService {
	private final SlotRepository repository;
	private final TemplateServiceImpl templateService;

	@Override
	@Transactional
	public Optional<SlotEntity> createSlot(String templateId, LocalTime begtime, LocalTime endtime, List<String> errorList){
		SlotEntity slot = null;
		if(begtime.isBefore(endtime)){
			try{
				slot = new SlotEntity(
						UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
						begtime, endtime, templateService.getById(templateId));
				repository.save(slot);
			} catch (EntityNotFoundException e){
				errorList.add(e.getMessage());
			}
		} else
			errorList.add("Нижняя граница периода не должна превышать верхнюю");
		return Optional.ofNullable(slot);
	}

	@Override
	public SlotEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Слот (id = " + id + ") не найден"));
	}

	@Override
	public SlotEntity getByBegEndTime(LocalTime begtime, LocalTime endtime){
		return repository.findFirstByBeginTimeAndEndTime(begtime, endtime)
				.orElseThrow(() -> new EntityNotFoundException(
						"Слот [" + begtime.truncatedTo(ChronoUnit.SECONDS) + " - " + endtime.truncatedTo(ChronoUnit.SECONDS)+ "] не найден"
				));
	}
}
