package ru.rdsystems.demo.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.scheduler.model.entity.SlotEntity;

import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface SlotRepository extends JpaRepository<SlotEntity, String> {

	Optional<SlotEntity> findFirstByBeginTimeAndEndTime(LocalTime begTime, LocalTime endTime);

}
