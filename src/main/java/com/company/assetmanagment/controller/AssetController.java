package com.company.assetmanagment.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.data.dto.AssetIncomingDto;
import com.company.assetmanagment.data.dto.AssetResponseDto;
import com.company.assetmanagment.data.dto.MapStructMapper;
import com.company.assetmanagment.data.request.GetAssetsCriteria;
import com.company.assetmanagment.exception.BusinessException;
import com.company.assetmanagment.service.AssetService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping(path = {"/api/v1/assets"}, produces = APPLICATION_JSON_VALUE)
@Tag(name = "Asset", description = "API Endpoints for managing Assets.")
public class AssetController
{
	private static final String ID = "id";
	
	private final AssetService 		assetService;
	private final MapStructMapper 	dtoMapper;
	
	@Autowired
	public AssetController(AssetService assetService, MapStructMapper dtoMapper)
	{
		this.assetService 	= assetService;
		this.dtoMapper 		= dtoMapper;
	}
	
	
	
	@Operation
	(
	    summary = "Get an Asset by id",description = "Get an Asset by id",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "200",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetResponseDto.class))
	        ),
	        @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@GetMapping(path = "/{id}")
	public ResponseEntity<AssetResponseDto> getAsset(@PathVariable(value = ID) Long id)
	{
		log.info("getAsset for path param {}", id);
		Asset result = this.assetService.getById(id);
		
		return  ResponseEntity.ok(dtoMapper.assetToAssetResponseDto(result));
	}
	
	@Operation
	(
	    summary = "Get all assets",description = "Get all assets. Supports Sorting. Default sort by id.",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "200",
	            content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AssetResponseDto.class)))
	        ),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@GetMapping
	public ResponseEntity<List<AssetResponseDto>> getAssets(@Valid GetAssetsCriteria assetCriteria)
	{
		log.info("get All Assets {}", assetCriteria);
				
		List<AssetResponseDto>	responseDto =	this.assetService.findAllAndSortBy(assetCriteria.getSortCriteria())
														.stream()
														.map(f -> dtoMapper.assetToAssetResponseDto(f))
														.collect(Collectors.toList());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(
						responseDto
					  );
	}
	
	@Operation
	(
	    summary = "Create an asset",description = "Create an asset",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "201",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetResponseDto.class))
	        ),
	        @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<AssetResponseDto> createAsset(@Valid @RequestBody AssetIncomingDto orderIncomingDto)
	{
		log.info("createAsset {}", orderIncomingDto);
		Asset savedAsset = this.assetService.create(dtoMapper.assetIncomdingDtoToAsset(orderIncomingDto));
		
		return ResponseEntity
				.status(HttpStatus.CREATED)
				.body( 
						this.dtoMapper.assetToAssetResponseDto(savedAsset)
					  );
	}
	
	
	@Operation
	(
	    summary = "Replace an asset with new details.",description = "Replace an asset with new details.",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "200",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetResponseDto.class))
	        ),
	        @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<AssetResponseDto> updateAsset(@PathVariable(value = ID) Long id, @Valid @RequestBody AssetIncomingDto orderIncomingDto)
	{
		Asset asst = dtoMapper.assetIncomdingDtoToAsset(orderIncomingDto);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body( 
						this.dtoMapper.assetToAssetResponseDto(this.assetService.update(id, asst))
					  );
	}
	
	@Operation
	(
	    summary = "Update an attribute of an asset.",description = "Update an attribute of an asset.",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "200",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetResponseDto.class))
	        ),
	        @ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity<AssetResponseDto> partiallyUpdateAsset(@PathVariable(value = ID) Long id, @RequestBody Map<String, Object> fields)
	{
		Asset asst = this.assetService.getById(id);

		for (Entry<String, Object> entry : fields.entrySet()) 
		{
			Field field = ReflectionUtils.findField(Asset.class, entry.getKey());
	        if(field != null)
	        {
		        field.setAccessible(true);
		        ReflectionUtils.setField(field, asst, entry.getValue());
	        }
	        else
	        {
	        	throw new BusinessException(String.format("Could not find any field with name %s to update.", entry.getKey()));
	        }
		}

		return ResponseEntity
					.status(HttpStatus.OK)
					.body( 
							this.dtoMapper.assetToAssetResponseDto(this.assetService.update(id, asst))
						  );
	}
	
	
	@Operation
	(
	    summary = "Delete an asset.",description = "Delete an asset.",tags = { "Asset" },
	    responses = {
	        @ApiResponse(
	            description = "Success",
	            responseCode = "204",
	            content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssetResponseDto.class))
	        ),
	        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
	        @ApiResponse(description = "Internal error", responseCode = "500", content = @Content)
	    }
    )
	@DeleteMapping(path = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = ID) Long id)
	{
		this.assetService.delete(id);
		
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
