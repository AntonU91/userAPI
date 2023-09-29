package com.example.userapi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.userapi.dto.UserRequestDto;
import com.example.userapi.dto.UserResponseDto;
import com.example.userapi.dto.UserUpdateDto;
import com.example.userapi.mapper.UserMapper;
import com.example.userapi.model.User;
import com.example.userapi.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {
    private static final Long USER_ID = 1L;
    private static final int NON_EXISTED_ID = 100;
    private static MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                          .build();
    }

    @Test
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createUser_ValidUserRequestDto_Created() throws Exception {
        UserRequestDto validUserRequestDto = new UserRequestDto();
        validUserRequestDto.setEmail("test@user.net");
        validUserRequestDto.setBirthDate(LocalDate.of(1999, 2, 25));
        validUserRequestDto.setFirstName("Test name");
        validUserRequestDto.setLastName("Test surname");
        String requestBody = objectMapper.writeValueAsString(validUserRequestDto);
        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated());
    }

    @Test
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createUser_InvalidUserRequestDto_BadRequest() throws Exception {
        UserRequestDto invalidUserRequestDto = new UserRequestDto();
        invalidUserRequestDto.setBirthDate(LocalDate.of(1999, 2, 25));
        invalidUserRequestDto.setFirstName("Test name");
        invalidUserRequestDto.setLastName("Test surname");
        String requestBody = objectMapper.writeValueAsString(invalidUserRequestDto);
        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateAllUserProperties_ValidUserRequestDto_Ok() throws Exception {
        UserRequestDto validUserRequestDto = new UserRequestDto();
        validUserRequestDto.setEmail("test@user.net");
        validUserRequestDto.setBirthDate(LocalDate.of(1999, 2, 25));
        validUserRequestDto.setFirstName("Updated name");
        validUserRequestDto.setLastName("Updated surname");

        String requestBody = objectMapper.writeValueAsString(validUserRequestDto);
        mockMvc.perform(put("/api/users/{userId}", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk());
        User actual = userRepository.findById(1L).get();
        User expected = userMapper.toUser(validUserRequestDto);
        expected.setId(actual.getId());
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateSomeUserProperties_ValidUserUpdateDto_Ok() throws Exception {
        UserUpdateDto validUpdateDto = new UserUpdateDto();
        LocalDate expected = LocalDate.of(2001, 1, 2);
        validUpdateDto.setBirthDate(expected);

        String requestBody = objectMapper.writeValueAsString(validUpdateDto);
        mockMvc.perform(patch("/api/users/{userId}", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk());
        User actual = userRepository.findById(1L).get();
        Assertions.assertEquals(expected, actual.getBirthDate());
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void updateSomeUserProperties_InvalidUserUpdateDto_BadRequest() throws Exception {
        UserUpdateDto invalidUpdateDto = new UserUpdateDto();
        LocalDate invalidDate = LocalDate.of(2222, 1, 2);
        invalidUpdateDto.setBirthDate(invalidDate);

        String requestBody = objectMapper.writeValueAsString(invalidUpdateDto);
        mockMvc.perform(patch("/api/users/{userId}", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAllUsersBySpecifiedBirthData_ValidBirthDateRange_Ok() throws Exception {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(1996, 1, 1);

        MvcResult mvcResult =
                mockMvc.perform(MockMvcRequestBuilders.get("/api/users?from={from}&to={to}", from, to)
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn();
        List<UserResponseDto> userList =
                objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<List<UserResponseDto>>() {
                });
        Assertions.assertEquals(2, userList.size());
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUserById_ExistedId_Ok() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", USER_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteUserById_NonExistedId_NotFound() throws Exception {
        mockMvc.perform(delete("/api/users/{userId}", NON_EXISTED_ID)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}