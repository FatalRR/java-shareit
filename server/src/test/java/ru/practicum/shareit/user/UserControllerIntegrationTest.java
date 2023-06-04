package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.ItemService;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerIntegrationTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private ItemService itemService;

    @MockBean
    private ItemRequestService itemRequestService;

    @SneakyThrows
    @Test
    void getAllTest() {
        List<UserDto> users = List.of(new UserDto());
        when(userService.getAll()).thenReturn(users);

        String response = mockMvc.perform(get("/users"))

                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(users), response);
    }

    @SneakyThrows
    @Test
    void saveTest() {
        UserDto user = new UserDto();
        user.setEmail("i@i.ru");
        user.setName("i");
        when(userService.saveUser(any(UserDto.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

/*    @SneakyThrows
    @Test
    void saveTestShouldThrowExceptionEmail() {
        UserDto user = new UserDto();
        user.setEmail("i@i.ru");
        user.setName(null);
        when(userService.saveUser(any(UserDto.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, never()).saveUser(any(UserDto.class));

    }*/

/*    @SneakyThrows
    @Test
    void saveTestShouldThrowExceptionName() {
        UserDto user = new UserDto();
        user.setEmail(null);
        user.setName("i");
        when(userService.saveUser(any(UserDto.class))).thenReturn(user);

        String response = mockMvc.perform(post("/users")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService, never()).saveUser(any(UserDto.class));

    }*/

    @SneakyThrows
    @Test
    void updateTest() {
        UserDto user = new UserDto();
        user.setId(1);
        when(userService.update(anyInt(), any(UserDto.class))).thenReturn(user);
        String response = mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

    @Test
    void updateTestShouldThrowException() throws Exception {
        UserDto user = new UserDto();
        user.setId(1);
        doThrow(ValidationException.class).when(userService).update(anyInt(), any(UserDto.class));

        mockMvc.perform(patch("/users/{userId}", 1)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @SneakyThrows
    @Test
    void getByIdTest() {
        UserDto user = new UserDto();
        user.setId(1);
        user.setEmail("i@i.ru");
        user.setName("i");
        when(userService.getById(1)).thenReturn(user);
        String response = mockMvc.perform(get("/users/{userId}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user), response);
    }

    @SneakyThrows
    @Test
    void removeByIdTest() {
        mockMvc.perform(delete("/users/{userId}", 1))
                .andExpect(status().isOk());

        verify(userService).removeById(1);
    }
}