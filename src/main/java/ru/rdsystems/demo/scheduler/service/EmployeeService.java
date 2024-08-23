package ru.rdsystems.demo.scheduler.service;

import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {

	Optional<EmployeeEntity> createEmployee(String name, String positionCode, List<String> errorList);

	EmployeeEntity getById(String id);

	EmployeeEntity getByName(String name);

	Optional<EmployeeEntity> getAdminById(String id);
}
