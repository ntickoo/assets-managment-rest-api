package com.company.assetmanagment.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.exception.ResourceNotFoundException;
import com.company.assetmanagment.repository.AssetRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AssetService
{
	private final AssetRepository assetRepository;
	
	@Autowired
	public AssetService(AssetRepository assetRepository)
	{
		this.assetRepository = assetRepository;
	}
	
	public Asset getById(Long id)
	{
		log.info("getById {}", id);
		Assert.notNull(id, "Asset id mustn't be null");
		
		return this.assetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No Asset found with id {%s}", id)));
	}
	
	public List<Asset> findAllAndSortBy(Sort sort)
	{
		log.info("findAllAndSortBy {}", sort);
		
		return Streamable.of(this.assetRepository.findAll(sort)).toList();
	}
	
	public Asset create(Asset asset)
	{
		return this.assetRepository.save(asset);
	}
	
	public void delete(Long id)
	{
		Asset asst = getById(id);
		this.assetRepository.delete(asst);
	}
	
	public Asset update(Long id, Asset newAssetValues)
	{
		Asset item = this.assetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("No Asset found with id {%s}", id)));
		
		item.setName			(newAssetValues.getName			());
		item.setDescription		(newAssetValues.getDescription	());
		item.setType			(newAssetValues.getType			());
		
		return this.assetRepository.save(item);
	}
}
