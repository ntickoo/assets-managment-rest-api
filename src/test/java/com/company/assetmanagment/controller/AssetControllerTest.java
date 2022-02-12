package com.company.assetmanagment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import com.company.assetmanagment.data.dto.MapStructMapper;
import com.company.assetmanagment.data.dto.MapStructMapperImpl;
import com.company.assetmanagment.service.AssetService;

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


	@BeforeEach
	public void setUp()
	{
		Asset asst1 = Asset.builder().name("TEST1").description("TEST DESCRIPTION1").type("TESTTYPE")
				.id(Long.valueOf(1)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst2 = Asset.builder().name("TEST2").description("TEST DESCRIPTION2").type("TESTTYPE")
				.id(Long.valueOf(2)).createdOn(new Date()).updatedOn(new Date()).build();

		Asset asst3 = Asset.builder().name("TEST3").description("TEST DESCRIPTION3").type("TESTTYPE")
				.id(Long.valueOf(3)).createdOn(new Date()).updatedOn(new Date()).build();

		List<Asset> manyAssets = Stream.of(asst3, asst2, asst1).collect(Collectors.toList());

		Mockito.when(assetService.getById(any(Long.class))).thenReturn(asst1);

		Mockito.when(assetService.create(any(Asset.class))).thenReturn(asst2);

		Mockito.when(assetService.update(anyLong(), any(Asset.class))).thenReturn(asst3);

		doNothing().when(assetService).delete(any());

		Mockito.when(assetService.findAllAndSortBy(any(Sort.class))).thenReturn(manyAssets);

	}

	@Test
	public void shouldReturnDefaultMessage() throws Exception
	{
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	public void shouldReturnData() throws Exception
	{
		this.mockMvc.perform(get(API_PATH)).andDo(print()).andExpect(status().isOk());
	}
}
