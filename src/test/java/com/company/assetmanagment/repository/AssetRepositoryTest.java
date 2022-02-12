package com.company.assetmanagment.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.LongStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;

import com.company.assetmanagment.data.domain.model.Asset;

@DataJpaTest
@ActiveProfiles("test")
class AssetRepositoryTest
{
	@Autowired
	private AssetRepository assetRepository;

	// UnitOfWork_StateUnderTest_ExpectedBehavior
	
	@Test
	public void Save_NewAsset_Pass()
	{
		Asset asset = Asset.builder().name("Temperature Sensor").description("Temperature sensor for machines").type("Sensor").build();
		
		assetRepository.save(asset);
		
		assertThat(assetRepository.findAll()).hasSize(1).extracting(Asset::getName).containsOnly("Temperature Sensor");
	}

	@Test
	public void Save_ManyNewAssets_Pass()
	{
		Collection<Asset> assets = getTestAssets(10);
		
		assetRepository.saveAll(assets);
		
		assertThat(assetRepository.findAll()).hasSize(10).allMatch(p -> p.getType().equals("Sensor"));
	}

	@Test
	public void JpaTest_SaveDelete_Pass()
	{
		Collection<Asset> assets = getTestAssets(10);
		
		assetRepository.saveAll(assets);
		
		assertThat(assetRepository.findAll()).hasSize(10).allMatch(p -> p.getType().equals("Sensor"));
		
		LongStream.range(1, 6).forEach(idx -> assetRepository.deleteById(idx));
		
		assertThat(assetRepository.findAll()).hasSize(5).allMatch(p -> p.getType().equals("Sensor"));
	}
	
	@Test
	public void JpaTest_SortAsset_Pass()
	{
		Collection<Asset> assets =  getTestAssets(9);
		
		assetRepository.saveAll(assets);
		
		Sort sortCriteria = Sort.by(Direction.ASC, "name");
		
		assertThat(assetRepository.findAll(sortCriteria)).first().matches( p -> p.getName().endsWith("1"));
		
		Sort sortCriteria2 = Sort.by(Direction.DESC, "name");
		
		assertThat(assetRepository.findAll(sortCriteria2)).first().matches( p -> p.getName().endsWith("9"));
		
	}
	
	private Collection<Asset> getTestAssets(int size)
	{
		Collection<Asset> assets =  new ArrayList<>();

		for(int i = 1; i <= size ; i++)
		{
			assets.add(Asset.builder().name("Temperature Sensor" + i).description("Temperature sensor for machines"+i).type("Sensor").build());
		}
		return assets;
	}
	
}
