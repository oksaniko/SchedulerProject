package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.rdsystems.demo.scheduler.model.entity.*;
import ru.rdsystems.demo.scheduler.service.*;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/create")
public class CreateController {

    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final TemplateService templateService;
    private final SlotService slotService;
    private final TimetableService timetableService;
    DateTimeFormatter parserTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    @PostMapping("/createEmployee")
    public ResponseEntity<Map<String, Object>>  createEmployee(
            @RequestParam("name") String name,
            @RequestParam("position") String position
    ){
        List<String> errorList = new ArrayList<>();
        Optional<EmployeeEntity> optEmployee = employeeService.createEmployee(name, position, errorList);
        return optEmployee.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", optEmployee.get().getId()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("errors", errorList));
    }

    @PostMapping("/createSchedule")
    public ResponseEntity<Map<String, Object>>  createSchedule(
            @RequestParam("name") String name
    ){
        List<String> errorList = new ArrayList<>();
        Optional<ScheduleEntity> optSchedule = scheduleService.createSchedule(name, errorList);
        return optSchedule.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", optSchedule.get().getId()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("errors", errorList));
    }

    @PostMapping("/createTemplate")
    public ResponseEntity<Map<String, Object>> createTemplate(){
        TemplateEntity template = templateService.createTemplate();
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", template.getId()));
    }

    @PostMapping("/createSlot")
    public ResponseEntity<Map<String, Object>> createSlot(
            @RequestParam("begtime") LocalTime begTime,
            @RequestParam("endtime") LocalTime endTime,
            @RequestParam("templateId") String templateId
    ){
        List<String> errorList = new ArrayList<>();
        Optional<SlotEntity> slot = slotService.createSlot(
                templateId, begTime, endTime, errorList
        );
        return slot.isPresent()
                ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("id", slot.get().getId()))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("errors", errorList));
    }

    @PostMapping("/createTimetable")
    public ResponseEntity<Map<String, Object>> createTimetable(
            @RequestParam("scheduleName") String scheduleName,
            @RequestParam("slotType") String slotType,
            @RequestParam("begtime") String begTime,
            @RequestParam("endtime") String endTime,
            @RequestParam("executor") String executorName,
            @RequestHeader("x-current-user") String currentUser
    ){
        List<String> errorList = new ArrayList<>();
        ResponseEntity<Map<String, Object>> response;
        try{
            Optional<TimetableEntity>  timetable = timetableService.createTimetable(scheduleName, slotType,
                    LocalTime.parse(begTime, parserTime), LocalTime.parse(endTime, parserTime),
                    currentUser, executorName, errorList);
            response = timetable.isPresent()
                    ? ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("id", timetable.get().getId()))
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("errors", errorList));
        } catch (DateTimeException dtEx){
            errorList.add("Ошибка конвертации параметра: " + dtEx.getMessage());
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("errors", errorList));
        }
        return response;
    }

}