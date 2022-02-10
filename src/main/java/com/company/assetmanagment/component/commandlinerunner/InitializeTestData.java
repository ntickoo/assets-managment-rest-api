package com.company.assetmanagment.component.commandlinerunner;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.repository.AssetRepository;

import lombok.extern.log4j.Log4j2;

@Profile("dev")
@Component
@Log4j2
public class InitializeTestData implements CommandLineRunner
{
	
	@Autowired
	private final AssetRepository assetRepository;
	
	public InitializeTestData(AssetRepository assetRepository)
	{
		this.assetRepository = assetRepository;
	}
	
	@Override
	public void run(String... args) throws Exception
	{
		List<Asset> assets = new ArrayList<>();
		
		for(int i = 1; i < 100; i++)
		{
			Asset asst = Asset.builder()
								.name		(RandomStringUtils.randomAlphabetic(6))
								.description(RandomStringUtils.randomAlphabetic(10))
								.type( new Integer(i % 5).toString())
								.build();
			
			assets.add(asst);
		}
		
		this.assetRepository.saveAll(assets);
		log.info("Inserted {} test records in db", assets.size());
	}
}
