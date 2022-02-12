package com.company.assetmanagment.data.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetIncomingDto
{
	@NotEmpty
	@Size(min = 3, message = "Asset name should be atleast 3 characters.")
	private String name;

	@NotEmpty
	@Size(min = 3, message = "Asset description should be atleast 3 characters.")
	private String description;
	
	@NotEmpty
	private String type;
}
