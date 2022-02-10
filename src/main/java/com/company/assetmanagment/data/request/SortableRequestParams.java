package com.company.assetmanagment.data.request;

import org.springframework.data.domain.Sort;

public interface SortableRequestParams
{
	Sort getSortCriteria();
}
