package ru.rdsystems.demo.scheduler.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class TimetableSort {
	private Sort.Direction direction;
	private TimetableSortField field;
}
