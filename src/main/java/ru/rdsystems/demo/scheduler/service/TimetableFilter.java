package ru.rdsystems.demo.scheduler.service;

import lombok.*;
import org.springframework.stereotype.Service;

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
}
