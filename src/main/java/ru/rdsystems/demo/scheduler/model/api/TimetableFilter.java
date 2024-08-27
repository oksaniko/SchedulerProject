package ru.rdsystems.demo.scheduler.model.api;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
@NoArgsConstructor
@Getter
@Setter
public class TimetableFilter {
	private String id;
	private String slotId;
	private String scheduleId;
	private String slotType;
	private String administratorId;
	private String executorId;
	private LocalTime beginTime;
	private LocalTime endTime;
}
