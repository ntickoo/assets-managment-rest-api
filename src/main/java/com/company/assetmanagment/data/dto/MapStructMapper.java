package com.company.assetmanagment.data.dto;

import org.mapstruct.Mapper;

import com.company.assetmanagment.data.domain.model.Asset;

@Mapper(componentModel = "spring")
public interface MapStructMapper
{
	AssetResponseDto assetToAssetResponseDto(Asset asset);
	
	Asset assetIncomdingDtoToAsset(AssetIncomingDto assetIncomingDto);
	
	Asset assetResponseDtoToAsset(AssetResponseDto assetDTO);
}