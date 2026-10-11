package com.example.pokemonm2.controler;

import com.example.pokemonm2.dto.DresseurDto;
import com.example.pokemonm2.services.IDresseurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DresseurController.class)
class DresseurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IDresseurService service;

    @Test
    void should_return_200_and_dresseur_dto_when_dresseur_exists() throws Exception {
        // Arrange
        Long id = 1L;
        DresseurDto dto = new DresseurDto();
        dto.setNomDresseur("Sacha");
        dto.setNombrePokemons(3);
        dto.setNiveauDresseur(131);
        when(service.exist(id)).thenReturn(true);
        when(service.get(id)).thenReturn(dto);

        // Act
        ResultActions result = mockMvc.perform(get("/dresseurs/" + id));

        // Assert
        result.andExpect(status().is(200))
                .andExpect(jsonPath("$.nomDresseur").value("Sacha"))
                .andExpect(jsonPath("$.nombrePokemons").value(3))
                .andExpect(jsonPath("$.niveauDresseur").value(131));
    }

    @Test
    void should_return_201_and_message_when_dresseur_does_not_exist() throws Exception {
        // Arrange
        Long id = 43L;
        String message = "Le dresseur n'existe pas";
        when(service.exist(id)).thenReturn(false);

        // Act
        ResultActions result = mockMvc.perform(get("/dresseurs/" + id));

        // Assert
        result.andExpect(status().is(201))
                .andExpect(content().string(message));
        verify(service, never()).get(id);
    }
}
