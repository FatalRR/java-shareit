package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    UserService userService;

    @InjectMocks
    UserController userController;

    @Test
    void getAllTest() {
        List<UserDto> users = List.of(new UserDto());
        when(userService.getAll()).thenReturn(users);

        List<UserDto> response = userController.getAll();

        assertEquals(users, response);
    }

    @Test
    void saveTest() {
        UserDto user = new UserDto();
        when(userService.saveUser(user)).thenReturn(user);

        UserDto response = userController.save(user);

        assertEquals(user, response);
    }

    @Test
    void updateTest() {
        UserDto user = new UserDto();
        user.setId(1);
        when(userService.update(1, user)).thenReturn(user);
        UserDto response = userController.update(user, 1);

        assertEquals(user, response);
    }

    @Test
    void getByIdTest() {
        UserDto user = new UserDto();
        user.setId(1);
        when(userService.getById(1)).thenReturn(user);
        UserDto response = userController.getById(1);

        assertEquals(user, response);
    }

    @Test
    void removeByIdTest() {
        userController.removeById(1);

        verify(userService).removeById(anyInt());
    }
}