package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import ru.rdsystems.demo.scheduler.model.mappers.ScheduleMapper;
import ru.rdsystems.demo.scheduler.model.mappers.TimetableFilterMapper;
import ru.rdsystems.demo.scheduler.model.mappers.TimetableInfoMapper;
import ru.rdsystems.demo.scheduler.repository.*;
import ru.rdsystems.demo.scheduler.schedulerApi.*;
import ru.rdsystems.demo.scheduler.service.ScheduleService;
import ru.rdsystems.demo.scheduler.service.TimetableService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfoController implements GetScheduleApi, GetTimetableForFiltersApi {

    private final EmployeeRepository employeeRepo;
    private final ScheduleRepository scheduleRepo;
    private final TemplateRepository templateRepo;
    private final SlotRepository slotRepo;
    private final TimetableRepository timetableRepo;
    private final TimetableService timetableService;
    private final ScheduleService scheduleService;

    private final ScheduleMapper scheduleMapper;
    private final TimetableFilterMapper filterMapper;
    private final TimetableInfoMapper timetableMapper;

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

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return GetScheduleApi.super.getRequest();
    }

    @Override
    public ResponseEntity<GetSchedule200Response> getSchedule(@Valid String id, @Valid String name) {
        ResponseEntity<GetSchedule200Response> response = null;
        if(id == null && name == null) {
            System.out.println("Хотя бы один параметр должен быть указан");
        } else {
            response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(scheduleMapper.map(scheduleService.getScheduleInfo(id, name).get(0)));
        }
        return response;
    }

    @Override
    public ResponseEntity<List<GetTimetableForFilters200ResponseInner>> getTimetableForFilters(@Valid GetTimetableForFiltersRequest getTimetableForFiltersRequest) {
        ResponseEntity<List<GetTimetableForFilters200ResponseInner>> response;
        try{
            response = ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(timetableMapper.map(
                            timetableService.getTimetablesForFilters(filterMapper.map(getTimetableForFiltersRequest))
                                    .getContent()
                    ));
        } catch (Exception e){
            System.out.println(e.getMessage());
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON)
                    .body(null);
        }
        return response;
    }

}