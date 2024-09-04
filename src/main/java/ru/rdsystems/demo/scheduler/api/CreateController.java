package ru.rdsystems.demo.scheduler.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.rdsystems.demo.scheduler.model.api.TimetableCreateField;
import ru.rdsystems.demo.scheduler.model.entity.*;
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
@RequestMapping("/api/create")
public class CreateController implements CreateApi {

    private final EmployeeService employeeService;
    private final ScheduleService scheduleService;
    private final TemplateService templateService;
    private final SlotService slotService;
    private final TimetableService timetableService;
    DateTimeFormatter parserTime = DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH);

    @Override
    public ResponseEntity<Employee> createEmployee(@NotNull @Valid String name, @NotNull @Valid String position) {
        return CreateApi.super.createEmployee(name, position);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createSchedule(@NotNull @Valid String name) {
        return CreateApi.super.createSchedule(name);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createTemplate() {
        return CreateApi.super.createTemplate();
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createSlot(@Valid CreateSlotRequest createSlotRequest) {
        return CreateApi.super.createSlot(createSlotRequest);
    }

    @Override
    public ResponseEntity<CreateSchedule200Response> createTimetable(@NotNull String xCurrentUser, @Valid CreateTimetableRequest createTimetableRequest) {
        return CreateApi.super.createTimetable(xCurrentUser, createTimetableRequest);
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