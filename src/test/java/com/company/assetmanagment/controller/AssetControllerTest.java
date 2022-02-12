package com.company.assetmanagment.controller;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.data.dto.AssetIncomingDto;
import com.company.assetmanagment.data.dto.MapStructMapper;
import com.company.assetmanagment.data.dto.MapStructMapperImpl;
import com.company.assetmanagment.exception.ResourceNotFoundException;
import com.company.assetmanagment.service.AssetService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AssetController.class)
@ActiveProfiles("test")
class AssetControllerTest
{

	@TestConfiguration
	public static class MapStructMapperConfiguration
	{
		@Bean
		public MapStructMapper getMapStructMapper()
		{
			return new MapStructMapperImpl();
		}
	}

	@MockBean
	private AssetService assetService;

	
	private final String API_PATH = "/api/v1/assets";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setUp()
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst2 = Asset.builder().name("TEST2").description("TEST DESCRIPTION2").type("TESTTYPE").id(Long.valueOf(2)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst3 = Asset.builder().name("TEST3").description("TEST DESCRIPTION3").type("TESTTYPE").id(Long.valueOf(3)).createdOn(new Date()).updatedOn(new Date()).build();

		List<Asset> manyAssets = Stream.of(asst3, asst2, asst1).collect(Collectors.toList());

		
		Mockito.when(assetService.getById( not(eq(Long.valueOf(100))) )   ).thenReturn(asst1);
		Mockito.when(assetService.getById( Long.valueOf(100))	   		  ).thenThrow(new ResourceNotFoundException("No asset found with the id."));
		
		
		Mockito.when(assetService.create(any(Asset.class))).thenReturn(asst2);

		Mockito.when(assetService.update(not(eq(Long.valueOf(100))), any(Asset.class)) ).thenReturn(asst3);
		Mockito.when(assetService.update(    eq(Long.valueOf(100)) , any(Asset.class)) ).thenThrow(new ResourceNotFoundException("No asset found with the id."));
		
		doNothing().when(assetService).delete(any());

		Mockito.when(assetService.findAllAndSortBy(any(Sort.class))).thenReturn(manyAssets);

	}

	@Test
	public void SmokeTest_GetCall_ShouldPass() throws Exception
	{
		this.mockMvc.perform(get(API_PATH)).andDo(print()).andExpect(status().isOk());
	}
	
	
	@Test
	public void GetApiTest_ByIdExists_ShouldPass() throws Exception
	{
		this.mockMvc.perform(get(API_PATH+"/1")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(1));
	}
	
	
	@Test
	public void GetApiTest_ByIdNotExist_ShouldPass() throws Exception
	{
		this.mockMvc.perform(get(API_PATH+"/100")).andDo(print())
					.andExpect(status().isNotFound())
					.andExpect(jsonPath("$.status").value("NOT_FOUND"));
	}
	
	@Test
	public void GetAllApiTest_AnySort_ShouldPass() throws Exception
	{
		this.mockMvc.perform(get(API_PATH+"?sortDirection=ASC&sortColumn=type")).andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$[0].id").value(3));
	}
	
	
	@Test
	public void PostApiTest_ValidRequest_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(post(API_PATH).contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void PostApiTest_BadRequest_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("t");
		dto.setDescription("t");
		dto.setType("t");
		
		this.mockMvc.perform(post(API_PATH)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(dto))).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value("BAD_REQUEST"));
	}
	
	@Test
	public void PutApiTest_ValidRequest_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(put(API_PATH+"/1").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").exists());
	}
	
	@Test
	public void PutApiTest_AssetNotExist_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(put(API_PATH+"/100").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isNotFound());
	}
	
	@Test
	public void DeleteApiTest_Any_ShouldPass() throws Exception
	{		
		this.mockMvc.perform(delete(API_PATH+"/100").content(""))
					.andExpect(status().is(204));
	}
	
	@Test
	public void PatchApiTest_AssetExist_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(put(API_PATH+"/1").contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isOk());
	}
	
}
