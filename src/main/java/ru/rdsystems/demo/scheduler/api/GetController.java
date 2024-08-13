package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.scheduler.repository.*;
import ru.rdsystems.demo.scheduler.service.ScheduleService;

import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class GetController {

    private final EmployeeRepository employeeRepo;
    private final ScheduleRepository scheduleRepo;
    private final TemplateRepository templateRepo;
    private final SlotRepository slotRepo;
    private final TimetableRepository timetableRepo;
    private final ScheduleService scheduleService;

    @GetMapping("/getDBInfo")
    public ResponseEntity<List<Object>> getDBInfo(){
        List<Object> json = new ArrayList<>();
        json.add(Map.of("users", employeeRepo.findAll()));
        json.add(Map.of("schedules", scheduleRepo.findAll()));
        json.add(Map.of("templates", templateRepo.findAll()));
        json.add(Map.of("slots", slotRepo.findAll()));
        json.add(Map.of("timetables", timetableRepo.findAll()));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

    @GetMapping("/getSchedule")
    public ResponseEntity<List<Map<String, Object>>> getSchedule(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "name", required = false) String name
    ){
        ResponseEntity<List<Map<String, Object>>> response;
        if(id == null && name == null) {
            List<Map<String, Object>> errorList = new ArrayList<>();
            errorList.add(Map.of("error", "Хотя бы один параметр должен быть указан"));
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(errorList);
        }
        else
            response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(scheduleService.getScheduleInfo(id, name));
        return response;
    }

}