package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.scheduler.model.api.TimetableCreateField;
import ru.rdsystems.demo.scheduler.model.entity.*;
import ru.rdsystems.demo.scheduler.service.implementations.*;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/create")
public class CreateController {

    private final EmployeeServiceImpl employeeService;
    private final ScheduleServiceImpl scheduleService;
    private final TemplateServiceImpl templateService;
    private final SlotServiceImpl slotService;
    private final TimetableServiceImpl timetableService;
    DateTimeFormatter parserTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    @PostMapping("/createEmployee")
    public ResponseEntity<Map<String, Object>>  createEmployee(
            @RequestParam("name") String name,
            @RequestParam("position") String position
    ){
        List<String> errorList = new ArrayList<>();
        Optional<EmployeeEntity> optEmployee = employeeService.createEmployee(name, position, errorList);
        return getResponseWithId(optEmployee, EmployeeEntity::getId, errorList);
    }

    @PostMapping("/createSchedule")
    public ResponseEntity<Map<String, Object>>  createSchedule(
            @RequestParam("name") String name
    ){
        List<String> errorList = new ArrayList<>();
        Optional<ScheduleEntity> optSchedule = scheduleService.createSchedule(name, errorList);
        return getResponseWithId(optSchedule, ScheduleEntity::getId, errorList);
    }

    @PostMapping("/createTemplate")
    public ResponseEntity<Map<String, Object>> createTemplate(){
        TemplateEntity template = templateService.createTemplate();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", template.getId()));
    }

    @PostMapping("/createSlot")
    public ResponseEntity<Map<String, Object>> createSlot(
            @RequestBody Map<String, String> bodyData
    ){
        List<String> errorList = new ArrayList<>();
        Optional<SlotEntity> slot = slotService.createSlot(
                bodyData.get("templateId"), LocalTime.parse(bodyData.get("begtime"), parserTime),
                LocalTime.parse(bodyData.get("endtime"), parserTime), errorList
        );
        return getResponseWithId(slot, SlotEntity::getId, errorList);
    }

    @PostMapping("/createTimetable")
    public ResponseEntity<Map<String, Object>> createTimetable(
            @RequestBody Map<String, String> bodyData,
            @RequestHeader("x-current-user") String currentUser
    ){
        ResponseEntity<Map<String, Object>> response;
        try{
            List<String> errorList = new ArrayList<>();
            String executorName = null;
            if (bodyData.containsKey("executor"))
                executorName = bodyData.get("executor");
            Optional<TimetableEntity>  timetable = timetableService.createTimetable(
                    new TimetableCreateField(
                            bodyData.get("scheduleName"), bodyData.get("slotType"),
                            LocalTime.parse(bodyData.get("begtime"), parserTime),
                            LocalTime.parse(bodyData.get("endtime"), parserTime),
                            currentUser, executorName, errorList));
            response = getResponseWithId(timetable, TimetableEntity::getId, errorList);
        } catch (DateTimeException dtEx){
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("errors", "Ошибка конвертации параметра: " + dtEx.getMessage()));
        }
        return response;
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