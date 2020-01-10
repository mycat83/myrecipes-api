package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.MaterialEntity;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Material;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.repository.MaterialRepository;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BaseInfoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void When_재료_리스트_조회_When_정상_리턴() throws Exception {

        // Given
        this.materialRepository.deleteAll();
        UnitEntity unitentity = saveUnit();
        MaterialEntity materialEntity = saveMaterial(unitentity);

        // When
        final ResultActions actions = this.mockMvc.perform(get("/materials")
                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaTypes.HAL_JSON)
                );

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].name").value(materialEntity.getName()))
                .andExpect(jsonPath("$[0].unitName").value(materialEntity.getUnitEntity().getName()));
    }

    @Test
    public void When_재료_저장_When_정상_리턴() throws Exception {

        // Given
        saveUnit();
        Material material = Material.builder()
                .name("재료")
                .unitName("kg")
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/materials")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(material)));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(material.getName()))
                .andExpect(jsonPath("unitName").value(material.getUnitName()));
    }

    @Test
    public void When_단위_저장_When_정상_리턴() throws Exception {

        //given
        Unit unit = Unit.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();

        //when
        final ResultActions actions = this.mockMvc.perform(post("/units")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(unit)));

        //then
        actions.andDo(print())
                .andExpect(status().isOk())
//                .andExpect(status().isCreated())
//                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("name").value(unit.getName()))
                .andExpect(jsonPath("exchangeUnitName").value(unit.getExchangeUnitName()))
                .andExpect(jsonPath("exchangeQuantity").value(unit.getExchangeQuantity()));
    }

    private UnitEntity saveUnit() {

        UnitEntity unitEntity = UnitEntity.builder()
                .name("kg")
                .registerUserId(1001)
                .modifyUserId(1001)
                .build();
        this.unitRepository.save(unitEntity);
        return unitEntity;
    }

    private MaterialEntity saveMaterial(UnitEntity unitEntity) {

        MaterialEntity materialEntity = MaterialEntity.builder()
                .name("재료")
                .registerUserId(1001)
                .modifyUserId(1001)
                .unitEntity(unitEntity)
                .build();
        this.materialRepository.save(materialEntity);
        return materialEntity;
    }
}