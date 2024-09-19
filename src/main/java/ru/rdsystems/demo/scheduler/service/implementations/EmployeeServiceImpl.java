package ru.rdsystems.demo.scheduler.service.implementations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rdsystems.demo.scheduler.kafka.KafkaProducer;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.repository.EmployeeRepository;
import ru.rdsystems.demo.scheduler.service.EmployeeService;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository repository;
    private final KafkaProducer kafkaProducer;

    @Value("${kafka.topic.employee}")
    private String topicEmployee;

    @Override
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

            ObjectMapper objectMapper = new ObjectMapper();
            String message = objectMapper.writeValueAsString(employee);
            System.out.println("to kafka:" + message);
            kafkaProducer.sendMessage(topicEmployee, message);
        }
        catch (IllegalArgumentException ex){
            errorList.add("Должность " + positionCode + " отсутствует в справочнике");
        } catch (JsonProcessingException e) {
            errorList.add("Ошибка упаковки в json " + e.getMessage());
			throw new RuntimeException(e);
		}
		return Optional.ofNullable(employee);
    }

    @Override
    public EmployeeEntity getById(String id){
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник (id = " + id + ") не найден"));
    }

    @Override
    public EmployeeEntity getByName(String name){
        return repository.findFirstByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Сотрудник с именем " + name + " не найден"));
    }

    @Override
    public Optional<EmployeeEntity> getAdminById(String id){
        EmployeeEntity someEmpl = getById(id);
        return (someEmpl.getPosition().equals(EmployeeEntity.EmployeePosition.MANAGER) ? Optional.of(someEmpl) : Optional.empty());
    }

}
