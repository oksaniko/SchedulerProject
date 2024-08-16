package ru.rdsystems.demo.scheduler.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.scheduler.model.api.Utils;
import ru.rdsystems.demo.scheduler.repository.*;
import ru.rdsystems.demo.scheduler.service.ScheduleService;
import ru.rdsystems.demo.scheduler.service.TimetableFilter;
import ru.rdsystems.demo.scheduler.service.TimetableService;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class InfoController {

    private final EmployeeRepository employeeRepo;
    private final ScheduleRepository scheduleRepo;
    private final TemplateRepository templateRepo;
    private final SlotRepository slotRepo;
    private final TimetableRepository timetableRepo;
    private final TimetableService timetableService;
    private final ScheduleService scheduleService;
    private final ObjectMapper mapper;
    private final Utils utils;

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

    private Sort getSortByMap(Map<String, String> sortMap){
        Sort resultSort = null;
        if (sortMap != null && !sortMap.isEmpty()) {
            try{
                Sort.Direction direction = Sort.Direction.valueOf(sortMap.get("direction").toUpperCase());
                StringBuilder field = new StringBuilder(sortMap.get("field"));
                if(field.toString().equals("beginTime") || field.toString().equals("endTime"))
                    field.insert(0, "slot.");
                resultSort = Sort.by(direction, field.toString());
            } catch (IllegalArgumentException ia){
                throw new IllegalArgumentException("Направление сортировки " + sortMap.get("direction") + " не определено");
            }
        }
        return resultSort;
    }

    @PostMapping("/getTimetableForFilters")
    public ResponseEntity<Map<String, Object>> getTimetableForFilters(
            @RequestBody Map<String, Object> bodyData
    ){
        Map<String, Object> json = new HashMap<>();
        HttpStatus responseStatus = HttpStatus.OK;
        try{
            Map<String, Object> filterMap = mapper.convertValue(bodyData.get("filter"), Map.class);
            TimetableFilter filter = new TimetableFilter();
            if(filterMap != null && !filterMap.isEmpty()){
                DateTimeFormatter parserTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);
                for (String key : filterMap.keySet()) {
                    if (key.equals("id"))
                        filter.setId(filterMap.get("id").toString());
                    if (key.equals("slotId"))
                        filter.setSlotId(filterMap.get("slotId").toString());
                    if (key.equals("scheduleId"))
                        filter.setScheduleId(filterMap.get("scheduleId").toString());
                    if (key.equals("slotType"))
                        filter.setSlotType(filterMap.get("slotType").toString());
                    if (key.equals("administratorId"))
                        filter.setAdministratorId(filterMap.get("administratorId").toString());
                    if (key.equals("executorId"))
                        filter.setExecutorId(filterMap.get("executorId").toString());
                    if (key.equals("beginTime"))
                        filter.setBeginTime(LocalTime.parse(filterMap.get("beginTime").toString(), parserTime));
                    if (key.equals("endTime"))
                        filter.setEndTime(LocalTime.parse(filterMap.get("endTime").toString(), parserTime));
                }
            }
            Map<String, String> sortMap = mapper.convertValue(bodyData.get("sort"), Map.class);
            Integer page = null, size = null;
            if(bodyData.containsKey("page"))
                page = Integer.valueOf(bodyData.get("page").toString());
            if(bodyData.containsKey("size"))
                size = Integer.valueOf(bodyData.get("size").toString());
            json = timetableService.getTimetablesForFilters(filter, getSortByMap(sortMap), page, size);
        } catch (Exception e){
            json = Map.of("error", e.getMessage());
            responseStatus = HttpStatus.NOT_FOUND;
        }
        return ResponseEntity.status(responseStatus)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
    }

}