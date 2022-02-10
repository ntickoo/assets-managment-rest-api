package com.company.assetmanagment.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface RepositoryBase<T, ID> extends PagingAndSortingRepository<T, ID>
{

}
