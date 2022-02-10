package com.company.assetmanagment.data.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetResponseDto
{
	private Long 	id;
	
	private String 	name;
	
	private String 	description;
	
	private String 	type;
	
	private Date 	createdOn;
}
