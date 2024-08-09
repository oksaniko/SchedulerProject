package ru.rdsystems.demo.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.scheduler.model.entity.TemplateEntity;

@Repository
public interface TemplateRepository extends JpaRepository<TemplateEntity, String> {
}
