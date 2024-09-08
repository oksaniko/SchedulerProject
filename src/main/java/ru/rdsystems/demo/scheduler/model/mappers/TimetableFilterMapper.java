package ru.rdsystems.demo.scheduler.model.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Sort;
import ru.rdsystems.demo.scheduler.model.api.TimetableFilterAndSorting;
import ru.rdsystems.demo.scheduler.model.api.TimetableSort;
import ru.rdsystems.demo.scheduler.schedulerApi.GetTimetableForFiltersRequest;
import ru.rdsystems.demo.scheduler.schedulerApi.SortTimetable;

@Mapper(componentModel = "spring")
public interface TimetableFilterMapper {

	TimetableFilterAndSorting map(GetTimetableForFiltersRequest filterAndSortingRequest);

	@Mapping(target = "direction", expression = "java( getDirectionFromRequest(sortRequest) )")
	TimetableSort map(SortTimetable sortRequest);

	default Sort.Direction getDirectionFromRequest(SortTimetable sortRequest){
		return sortRequest != null
				? Sort.Direction.valueOf(sortRequest.getDirection())
				: Sort.Direction.ASC;
	}

}
