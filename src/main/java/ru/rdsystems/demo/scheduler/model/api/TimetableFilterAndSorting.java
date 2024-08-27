package ru.rdsystems.demo.scheduler.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
@AllArgsConstructor
public class TimetableFilterAndSorting {
	private TimetableFilter filter;
	private TimetableSort sort;
	private Integer page;
	private Integer size;
}
