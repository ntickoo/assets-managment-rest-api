package com.company.assetmanagment.data.request;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class RequestParams implements SortableRequestParams
{
	private String sortDirection;

	private String sortColumn;

	@Hidden
	@Override
	public Sort getSortCriteria()
	{
		final Sort.Direction sortDirection = EnumUtils.isValidEnum(Sort.Direction.class, this.sortDirection) 
														? EnumUtils.getEnum(Sort.Direction.class, this.sortDirection) 
														: Sort.Direction.DESC;
		
		final String sortColumn = 	StringUtils.isAllEmpty(this.sortColumn) 
									? "id"
									: this.sortColumn.trim();
		
		return Sort.by(sortDirection,sortColumn);
	}
}
