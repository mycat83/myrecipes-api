package link.myrecipes.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import link.myrecipes.api.domain.UnitEntity;
import link.myrecipes.api.dto.Unit;
import link.myrecipes.api.repository.UnitRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UnitControllerTest extends ControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UnitRepository unitRepository;

    @Test
    public void When_단위_조회_When_정상_리턴() throws Exception {

        // Given
        UnitEntity unitEntity = saveUnit();

        // When
        final ResultActions actions = this.mockMvc.perform(get("/units/{name}", unitEntity.getName())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON));

        // Then
        actions.andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("name").value(unitEntity.getName()))
                .andExpect(jsonPath("exchangeUnitName").value(unitEntity.getExchangeUnitName()))
                .andExpect(jsonPath("exchangeQuantity").value(unitEntity.getExchangeQuantity()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.insert-unit").exists())
                .andExpect(jsonPath("_links.profile").exists());
    }

    @Test
    public void When_단위_저장_When_정상_리턴() throws Exception {

        // Given
        Unit unit = Unit.builder()
                .name("kg")
                .exchangeUnitName("g")
                .exchangeQuantity(1000D)
                .build();

        // When
        final ResultActions actions = this.mockMvc.perform(post("/units")
                .param("userId", "1001")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
                .content(this.objectMapper.writeValueAsString(unit)));

        // Then
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
}