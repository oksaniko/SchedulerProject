package ru.rdsystems.demo.scheduler.model.api;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import ru.rdsystems.demo.scheduler.model.entity.EmployeeEntity;
import ru.rdsystems.demo.scheduler.model.entity.TemplateEntity;
import ru.rdsystems.demo.scheduler.service.implementations.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class Utils {

	private final EmployeeServiceImpl emplService;
	private final ScheduleServiceImpl scheduleService;
	private final TemplateServiceImpl templService;
	private final SlotServiceImpl slotService;
	private final TimetableServiceImpl timetableService;

	@EventListener(ApplicationReadyEvent.class)
	public void runAfterStartup(){
		List<String> errorList = new ArrayList<>();

		EmployeeEntity admin = emplService.createEmployee("i.ivanov", "manager", errorList).get();
		System.out.println("admin_id = " + admin.getId());
		emplService.createEmployee("p.petrov", "EMPLOYEE", errorList);
		emplService.createEmployee("s.sidorov", "employee", errorList);

		scheduleService.createSchedule("Полный рабочий день", errorList);
		scheduleService.createSchedule("Сокращенный рабочий день", errorList);

		TemplateEntity fullDayTemplate = templService.createTemplate();
		slotService.createSlot(fullDayTemplate.getId(),
				LocalTime.of(8, 0), LocalTime.of(12, 0), errorList);
		slotService.createSlot(fullDayTemplate.getId(),
				LocalTime.of(13, 0), LocalTime.of(17, 0), errorList);

		fullDayTemplate = templService.createTemplate();
		slotService.createSlot(fullDayTemplate.getId(),
				LocalTime.of(9, 0), LocalTime.of(13, 0), errorList);
		slotService.createSlot(fullDayTemplate.getId(),
				LocalTime.of(14, 0), LocalTime.of(18, 0), errorList);

		TemplateEntity shortDayTemplate = templService.createTemplate();
		slotService.createSlot(shortDayTemplate.getId(),
				LocalTime.of(8, 0), LocalTime.of(12, 0), errorList);
		slotService.createSlot(shortDayTemplate.getId(),
				LocalTime.of(13, 0), LocalTime.of(16, 0), errorList);

		shortDayTemplate = templService.createTemplate();
		slotService.createSlot(shortDayTemplate.getId(),
				LocalTime.of(9, 0), LocalTime.of(13, 0), errorList);
		slotService.createSlot(shortDayTemplate.getId(),
				LocalTime.of(14, 0), LocalTime.of(17, 0), errorList);

		TemplateEntity dinner = templService.createTemplate();
		slotService.createSlot(dinner.getId(),
				LocalTime.of(12, 0), LocalTime.of(13, 0), errorList);
		slotService.createSlot(dinner.getId(),
				LocalTime.of(13, 0), LocalTime.of(14, 0), errorList);

		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "LOCAL", LocalTime.of(9, 0), LocalTime.of(13, 0), admin.getId(), "i.ivanov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "LOCAL", LocalTime.of(14, 0), LocalTime.of(18, 0), admin.getId(), "i.ivanov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "UNDEFINED", LocalTime.of(13, 0), LocalTime.of(14, 0), admin.getId(), "i.ivanov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Сокращенный рабочий день", "FROM_HOME", LocalTime.of(9, 0), LocalTime.of(13, 0), admin.getId(), "i.ivanov", errorList));
		/*timetableService.createTimetable(
				"Сокращенный рабочий день", "FROM_HOME",
				LocalTime.of(14, 0), LocalTime.of(17, 0), admin.getId(), "i.ivanov", errorList);
		timetableService.createTimetable(
				"Сокращенный рабочий день", "UNDEFINED",
				LocalTime.of(13, 0), LocalTime.of(14, 0), admin.getId(), "i.ivanov", errorList);*/

		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "FROM_HOME", LocalTime.of(8, 0), LocalTime.of(12, 0), admin.getId(), "p.petrov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "UNDEFINED", LocalTime.of(12, 0), LocalTime.of(13, 0), admin.getId(), "p.petrov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Полный рабочий день", "FROM_HOME", LocalTime.of(13, 0), LocalTime.of(17, 0), admin.getId(), "p.petrov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Сокращенный рабочий день", "FROM_HOME", LocalTime.of(8, 0), LocalTime.of(12, 0), admin.getId(), "p.petrov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Сокращенный рабочий день", "UNDEFINED", LocalTime.of(12, 0), LocalTime.of(13, 0), admin.getId(), "p.petrov", errorList));
		timetableService.createTimetable(
				new TimetableCreateField("Сокращенный рабочий день", "FROM_HOME", LocalTime.of(13, 0), LocalTime.of(17, 0), admin.getId(), "p.petrov", errorList));

		/*timetableService.createTimetable(
				"Полный рабочий день", "LOCAL",
				LocalTime.of(8, 0), LocalTime.of(12, 0), admin.getId(), "s.sidorov", errorList);
		timetableService.createTimetable(
				"Полный рабочий день", "UNDEFINED",
				LocalTime.of(12, 0), LocalTime.of(13, 0), admin.getId(), "s.sidorov", errorList);
		timetableService.createTimetable(
				"Полный рабочий день", "LOCAL",
				LocalTime.of(13, 0), LocalTime.of(17, 0), admin.getId(), "s.sidorov", errorList);
		timetableService.createTimetable(
				"Сокращенный рабочий день", "LOCAL",
				LocalTime.of(9, 0), LocalTime.of(13, 0), admin.getId(), "s.sidorov", errorList);
		timetableService.createTimetable(
				"Сокращенный рабочий день", "UNDEFINED",
				LocalTime.of(13, 0), LocalTime.of(14, 0), admin.getId(), "s.sidorov", errorList);
		timetableService.createTimetable(
				"Сокращенный рабочий день", "LOCAL",
				LocalTime.of(14, 0), LocalTime.of(17, 0), admin.getId(), "s.sidorov", errorList);*/
		System.out.println(errorList);
	}

}
