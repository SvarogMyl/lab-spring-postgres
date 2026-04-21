package com.example.demo;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }

    @Test
    void testFullFlowWithSecurity() throws Exception {
        // 1. Verificar que sin token recibimos Forbidden (403)
        mockMvc.perform(get("/items"))
                .andExpect(status().isForbidden());

        // 2. Hacer Login para obtener el Token
        Map<String, String> credentials = Map.of("username", "admin", "password", "admin123");
        MvcResult loginResult = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseBody).get("token").asText();

        // 3. Crear un Item usando el Token
        Item newItem = new Item();
        newItem.setNombre("Item de Test");
        newItem.setDescripcion("Probando con Testcontainers");

        mockMvc.perform(post("/items")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
                .andExpect(status().isCreated());

        // 4. Verificar en la base de datos real del contenedor
        assertThat(itemRepository.findAll()).hasSize(1);
        assertThat(itemRepository.findAll().get(0).getNombre()).isEqualTo("Item de Test");
    }
}
