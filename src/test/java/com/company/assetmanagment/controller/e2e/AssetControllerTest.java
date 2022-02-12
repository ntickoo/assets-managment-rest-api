package com.company.assetmanagment.controller.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.company.assetmanagment.data.domain.model.Asset;
import com.company.assetmanagment.data.dto.AssetIncomingDto;
import com.company.assetmanagment.repository.AssetRepository;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class AssetControllerTest
{
	private final String API_PATH = "/api/v1/assets";
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private AssetRepository assetRepository;
	
	@Test
	public void PostApiE2ETest_CreateAsset_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(post(API_PATH).contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").exists());
		
		assertThat(assetRepository.findAll()).hasSize(1);
	}
	
	@Test
	public void PostApiE2ETest_InvalidAsset_ShouldPass() throws Exception
	{
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("t");
		dto.setDescription("t");
		dto.setType("t");
		
		this.mockMvc.perform(post(API_PATH).contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isBadRequest());
		
		assertThat(assetRepository.findAll()).hasSize(0);
	}
	
	@Test
	public void PostApiE2ETest_InvalidAssetEmptyBody_ShouldPass() throws Exception
	{
		this.mockMvc.perform(post(API_PATH).contentType("application/json").content(""))
					.andExpect(status().isBadRequest());
		
		assertThat(assetRepository.findAll()).hasSize(0);
	}
	
	@Test
	public void GetAllApiE2ETest_SortByDescription_ShouldPass() throws Exception
	{
		for(int i = 100; i <= 105 ; i++)
		{
			AssetIncomingDto dto = new AssetIncomingDto();
			dto.setName(Integer.toString(i));
			dto.setDescription(Integer.toString(i));
			dto.setType("t");
			
			this.mockMvc.perform(post(API_PATH).contentType("application/json").content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").exists());
		}

		assertThat(assetRepository.findAll()).hasSize(6);
	
		this.mockMvc.perform(get(API_PATH+"?sortDirection=DESC&sortColumn=description")).andDo(print())
		.andExpect(status().isOk())
		.andExpect(jsonPath("$[0].name").value(105));
		
	}
	
	@Test
	public void PutE2ETest_AssetNotExist_ShouldPass() throws Exception
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();
		asst1 = this.assetRepository.save(asst1);
		
		AssetIncomingDto dto = new AssetIncomingDto();
		dto.setName("ttt");
		dto.setDescription("tttttt");
		dto.setType("t");
		
		this.mockMvc.perform(put(API_PATH + "/" + asst1.getId()).contentType("application/json").content(objectMapper.writeValueAsString(dto)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.name").value("ttt"));
		
		Asset a = this.assetRepository.findById(asst1.getId()).get();
		assertEquals(a.getDescription(), "tttttt");
	}
	
	@Test
	public void DeleteE2ETest_Any_ShouldPass() throws Exception
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();
		asst1 = this.assetRepository.save(asst1);
		this.mockMvc.perform(delete(API_PATH+ "/" + asst1.getId()).content(""))
					.andExpect(status().is(204));
		
		this.mockMvc.perform(delete(API_PATH+ "/" + asst1.getId()).content(""))
		.andExpect(status().isNotFound());
	}
	
	@Test
	public void PatchApiTest_BadFieldToPatch_ShouldPass() throws Exception
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();
		asst1 = this.assetRepository.save(asst1);
		
		
		this.mockMvc.perform(patch(API_PATH+ "/"+asst1.getId()).contentType("application/json").content("{\"nnnnn\": \"desc patch\" }"))
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void PatchApiTest_ValidField_ShouldPass() throws Exception
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE").id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();
		asst1 = this.assetRepository.save(asst1);
		
		
		this.mockMvc.perform(patch(API_PATH+ "/"+asst1.getId()).contentType("application/json").content("{\"name\": \"newname\" }"))
					.andExpect(status().isOk());
		
		Asset a = this.assetRepository.findById(asst1.getId()).get();
		assertEquals(a.getName(), "newname");
	}
	
	
}
