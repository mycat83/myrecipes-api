package link.myrecipes.api.controller;

import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import link.myrecipes.api.service.BaseInfoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.file.Files;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseInfoControllerTest {
    private Material material;
    private Unit unit;

    @Value("classpath:/json/material.json")
    private Resource materialResource;

    @Value("classpath:/json/unit.json")
    private Resource unitResource;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BaseInfoServiceImpl baseInfoService;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Before
    public void setUp() {
        this.material = Material.builder()
                .id(10)
                .name("재료")
                .unitName("kg")
                .build();

        this.unit = Unit.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();
    }

    @Test
    public void When_재료_리스트_조회_When_정상_리턴() throws Exception {
        //given
        UnitEntity unitentity = UnitEntity.builder()
                .name("kg")
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        this.unitRepository.save(unitentity);

        MaterialEntity materialEntity = MaterialEntity.builder()
                .name("식용유")
                .registerUserId(1002)
                .modifyUserId(1002)
                .unitEntity(unitentity)
                .build();
        this.materialRepository.save(materialEntity);

        //when
        final ResultActions actions = this.mockMvc.perform(get("/materials")
                .contentType(MediaType.APPLICATION_JSON));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(materialEntity.getName()))
                .andExpect(jsonPath("$[0].unitName").value(materialEntity.getUnitEntity().getName()));
    }

    @Test
    public void When_재료_저장_When_정상_리턴() throws Exception {
        //given
        String materialJson = new String(Files.readAllBytes(materialResource.getFile().toPath()));
        given(this.baseInfoService.createMaterial(any(Material.class), any(Integer.class))).willReturn(this.material);

        //when
        final ResultActions actions = this.mockMvc.perform(post("/materials?userId=10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(materialJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"id\":" + this.material.getId())))
                .andExpect(content().string(containsString("\"name\":\"" + this.material.getName() + "\"")))
                .andExpect(content().string(containsString("\"unitName\":\"" + this.material.getUnitName() + "\"")));
    }

    @Test
    public void When_단위_저장_When_정상_리턴() throws Exception {
        //given
        String unitJson = new String(Files.readAllBytes(unitResource.getFile().toPath()));
        given(this.baseInfoService.createUnit(any(Unit.class), any(Integer.class))).willReturn(this.unit);

        //when
        final ResultActions actions = this.mockMvc.perform(post("/units?userId=10001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(unitJson));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().string(containsString("\"name\":\"" + this.unit.getName() + "\"")))
                .andExpect(content().string(containsString("\"exchangeUnitName\":\"" + this.unit.getExchangeUnitName() + "\"")))
                .andExpect(content().string(containsString("\"exchangeQuantity\":" + this.unit.getExchangeQuantity())));
    }
}