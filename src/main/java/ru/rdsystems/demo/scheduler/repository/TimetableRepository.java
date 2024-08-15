package ru.rdsystems.demo.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.scheduler.model.entity.TimetableEntity;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface TimetableRepository extends JpaRepository<TimetableEntity, String>, JpaSpecificationExecutor<TimetableEntity> {

	@Query(value = "select t.* from timetables t join slots s on s.id = t.slot_id " +
			"where coalesce(t.executor_id, t.administrator_id) = :emplId " +
			"and t.schedule_id = :scheduleId " +
			"and s.begin_time < :endTime and s.end_time > :begTime",
			nativeQuery = true)
	List<TimetableEntity> findIntersectTime(String emplId, String scheduleId, LocalTime begTime, LocalTime endTime);

}
