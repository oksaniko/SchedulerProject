package ru.rdsystems.demo.scheduler.service.implementations;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.TemplateEntity;
import ru.rdsystems.demo.scheduler.repository.TemplateRepository;
import ru.rdsystems.demo.scheduler.service.TemplateService;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

	private final TemplateRepository repository;

	@Override
	@Transactional
	public TemplateEntity createTemplate(){
		TemplateEntity template = new TemplateEntity(
				UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
				ZonedDateTime.now(ZoneId.of("Europe/Moscow")),
				List.of()
		);
		repository.save(template);
		return template;
	}

	@Override
	public TemplateEntity getById(String id){
		return repository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Шаблон расписания (id = " + id + ") не найден"));
	}

}
