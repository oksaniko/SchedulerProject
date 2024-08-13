package ru.rdsystems.demo.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.rdsystems.demo.scheduler.model.entity.ScheduleEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {

	Optional<ScheduleEntity> findFirstByName(String name);

	@Query(value = "select s.schedule_name schedule, st.begin_time, st.end_time, t.slot_type slot, " +
			"tt.id template, eAdm.employee_name admin, eExec.employee_name executor " +
			"from schedules s " +
			"join timetables t on t.schedule_id = s.id " +
			"join slots st on st.id = t.slot_id " +
			"join templates tt on tt.id = st.schedule_template_id " +
			"join employees eAdm on eAdm.id = t.administrator_id " +
			"left join employees eExec on eExec.id = t.executor_id " +
			"where ((s.id = :id and :id is not null) or (s.schedule_name = :name and :name is not null)) " +
			"order by st.begin_time, st.end_time",
			nativeQuery = true)
	List<Map<String, Object>> getScheduleInfo(String id, String name);
}
