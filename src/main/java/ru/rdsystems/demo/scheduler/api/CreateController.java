package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.scheduler.model.api.TimetableCreateField;
import ru.rdsystems.demo.scheduler.model.entity.*;
import ru.rdsystems.demo.scheduler.model.mappers.*;
import ru.rdsystems.demo.scheduler.schedulerApi.*;
import ru.rdsystems.demo.scheduler.service.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CreateController implements CreateApi {

    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final TemplateService templateService;
    private final SlotService slotService;
    private final TimetableService timetableService;
    DateTimeFormatter parserTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    private final EmployeeMapper employeeMapper;
    private final TemplateMapper templateMapper;
    private final ScheduleMapper scheduleMapper;
    private final SlotMapper slotMapper;
    private final TimetableMapper timetableMapper;

    @Override
    public ResponseEntity<Employee> createEmployee(@NotNull @Valid String name, @NotNull @Valid String position) {
        List<String> errorList = new ArrayList<>();
        Optional<EmployeeEntity> optEmployee = employeeService.createEmployee(name, position, errorList);
        return optEmployee.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(employeeMapper.map(optEmployee.get()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createSchedule(@NotNull @Valid String name) {
        List<String> errorList = new ArrayList<>();
        Optional<ScheduleEntity> optSchedule = scheduleService.createSchedule(name, errorList);
        return optSchedule.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(scheduleMapper.map(optSchedule.get()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createTemplate() {
        TemplateEntity template = templateService.createTemplate();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(templateMapper.map(template));
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createSlot(@Valid CreateSlotRequest createSlotRequest) {
        List<String> errorList = new ArrayList<>();
        Optional<SlotEntity> slot = slotService.createSlot(
                createSlotRequest.getTemplateId(),
                LocalTime.parse(createSlotRequest.getBegtime(), parserTime),
                LocalTime.parse(createSlotRequest.getEndtime(), parserTime),
                errorList
        );
        return slot.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(slotMapper.map(slot.get()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createTimetable(@NotNull String xCurrentUser, @Valid CreateTimetableRequest createTimetableRequest) {
        List<String> errorList = new ArrayList<>();
        Optional<TimetableEntity> timetable = Optional.empty();
        try{
            timetable = timetableService.createTimetable(
                    new TimetableCreateField(
                            createTimetableRequest.getScheduleName(),
                            createTimetableRequest.getSlotType(),
                            LocalTime.parse(createTimetableRequest.getBegtime(), parserTime),
                            LocalTime.parse(createTimetableRequest.getEndtime(), parserTime),
                            xCurrentUser, createTimetableRequest.getExecutor(), errorList));
        } catch (DateTimeException dtEx){
            System.out.println("Ошибка конвертации параметра: " + dtEx.getMessage());
        }
        return timetable.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(timetableMapper.map(timetable.get()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(null);
    }

    private static <T> ResponseEntity<Map<String, Object>> getResponseWithId(
            Optional<T> optional,
            Function<T, String> idGetter,
            List<String> errorList) {
        return optional.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", idGetter.apply(optional.get())))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("errors", errorList));
    }

}