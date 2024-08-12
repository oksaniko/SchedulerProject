package ru.rdsystems.demo.scheduler.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.repository.EmployeeRepository;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;

    @Transactional
    public Optional<EmployeeEntity> createEmployee(String name, String positionCode, List<String> errorList){
        EmployeeEntity employee = null;
        try {
            EmployeeEntity.EmployeePosition position = EmployeeEntity.EmployeePosition.valueOf(positionCode.toUpperCase());
            employee = new EmployeeEntity(
                    UUID.randomUUID().toString().replace("-","").toLowerCase(Locale.ROOT),
                    name,
                    EmployeeEntity.EmployeeStatus.WORKING,
                    position,
                    List.of(),
                    List.of()
            );
            repository.save(employee);
        }
        catch (IllegalArgumentException ex){
            errorList.add("Должность " + positionCode + " отсутствует в справочнике");
        }
        return Optional.ofNullable(employee);
    }

    public EmployeeEntity getById(String id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
    }

    public EmployeeEntity getByName(String name){
        return repository.findFirstByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с именем " + name + " не найден"));
    }

    public Optional<EmployeeEntity> getAdminById(String id){
        EmployeeEntity someEmpl = getById(id);
        return (someEmpl.getPosition().equals(EmployeeEntity.EmployeePosition.MANAGER) ? Optional.of(someEmpl) : Optional.empty());
    }

}
