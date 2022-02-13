package com.company.assetmanagment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.exception.ResourceNotFoundException;
import com.company.assetmanagment.repository.AssetRepository;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AssetServiceTest
{

	@Mock
	private AssetRepository assetRepository;

	
	@BeforeEach
	public void setUp()
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst2 = Asset.builder().name("TEST2").description("TEST DESCRIPTION2").type("TESTTYPE").id(Long.valueOf(2)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst3 = Asset.builder().name("TEST3").description("TEST DESCRIPTION3").type("TESTTYPE").id(Long.valueOf(3)).createdOn(new Date()).updatedOn(new Date()).build();

		List<Asset> manyAssets = Stream.of(asst3, asst2, asst1).collect(Collectors.toList());

		
		Mockito.when(assetRepository.findById( not(eq(Long.valueOf(100))) )   ).thenReturn(Optional.ofNullable(asst1));
		Mockito.when(assetRepository.findById( Long.valueOf(100))	   		  ).thenReturn(Optional.ofNullable(null));
		
		
		Mockito.when(assetRepository.save(any(Asset.class))).thenReturn(asst2);

		doNothing().when(assetRepository).delete(any());

		Mockito.when(assetRepository.findAll(any(Sort.class))).thenReturn(manyAssets);

	}
	
	
	@Test
	void Find_GetByExistingId_ShouldPass()
	{
		
		AssetService assetService = new AssetService(assetRepository);
		
		assertNotNull(assetService.getById(1L));
		
		assertThrows(
		           ResourceNotFoundException.class,
		           () -> assetService.getById(100L),
		           "Should Throw exception for Resource not found."
		    );
	}

	
	@Test
	void Find_GetAllSorted_ShouldPass()
	{
		
		AssetService assetService = new AssetService(assetRepository);
		
		Sort sort = Sort.by(Direction.DESC, "name");
		
		assertNotNull(assetService.findAllAndSortBy(sort));
		
		assertThat(assetService.findAllAndSortBy(sort)).hasSize(3).first().matches( p -> p.getName().equals("TEST3"));
		
	}
	
	
	@Test
	void Delete_ExistingId_ShouldPass()
	{
		
		AssetService assetService = new AssetService(assetRepository);
				
		assertDoesNotThrow( () -> assetService.delete(1L));
		assertThrows(
		           ResourceNotFoundException.class,
		           () -> assetService.getById(100L),
		           "Should Throw exception for Resource not found."
		    );		
	}
	
	@Test
	void Delete_NotExistingId_ShouldPass()
	{
		
		AssetService assetService = new AssetService(assetRepository);
				
		assertThrows(
		           ResourceNotFoundException.class,
		           () -> assetService.getById(100L),
		           "Should Throw exception for Resource not found."
		    );		
	}	
	
	
	@Test
	void Save_Asset_ShouldPass()
	{
		AssetService assetService = new AssetService(assetRepository);
		
		assertDoesNotThrow(() -> assetService.create(new Asset())		    );		
	}	
	
}
