package ru.rdsystems.demo.scheduler.model.api;

import java.time.LocalTime;
import java.util.List;

public record TimetableCreateField(String scheduleName, String slotTypeStr, LocalTime slotBegTime,
								   LocalTime slotEndTime, String adminId, String executorName, List<String> errorList) {
}