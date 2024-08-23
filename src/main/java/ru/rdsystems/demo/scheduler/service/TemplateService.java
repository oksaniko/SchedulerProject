package ru.rdsystems.demo.scheduler.service;

import ru.rdsystems.demo.scheduler.model.entity.TemplateEntity;

public interface TemplateService {

	TemplateEntity createTemplate();

	TemplateEntity getById(String id);
}
