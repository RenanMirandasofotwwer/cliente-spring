package com.example.clientapi;

import com.example.clientapi.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientRepository repository;

    @BeforeEach
    void cleanDatabase() {
        repository.deleteAll();
    }

    @Test
    void shouldCreateListFindUpdateAndDeleteClient() throws Exception {
        String clientJson = """
                {
                  "name": "Ana Beatriz Lima",
                  "cpf": "123.456.789-01",
                  "income": 6400.50,
                  "birthDate": "1992-07-14T00:00:00Z",
                  "children": 2
                }
                """;

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ana Beatriz Lima"));

        mockMvc.perform(get("/clients?page=0&size=5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.totalElements").value(1));

        Long clientId = repository.findAll().get(0).getId();
        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpf").value("123.456.789-01"));

        String updatedJson = """
                {
                  "name": "Ana Beatriz Lima Souza",
                  "cpf": "123.456.789-01",
                  "income": 7000.00,
                  "birthDate": "1992-07-14T00:00:00Z",
                  "children": 2
                }
                """;
        mockMvc.perform(put("/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ana Beatriz Lima Souza"))
                .andExpect(jsonPath("$.income").value(7000.0));

        mockMvc.perform(delete("/clients/{id}", clientId))
                .andExpect(status().isNoContent());
        mockMvc.perform(get("/clients/{id}", clientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectInvalidClient() throws Exception {
        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "cpf": "111",
                                  "income": -1,
                                  "birthDate": "1992-07-14T00:00:00Z",
                                  "children": -2
                                }
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectDuplicateCpf() throws Exception {
        String clientJson = """
                {
                  "name": "Ana Beatriz Lima",
                  "cpf": "123.456.789-01",
                  "income": 6400.50,
                  "birthDate": "1992-07-14T00:00:00Z",
                  "children": 2
                }
                """;

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isConflict());
    }
}
