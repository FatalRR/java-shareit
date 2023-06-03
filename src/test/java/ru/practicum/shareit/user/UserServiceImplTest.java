package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getAllTest() {
        UserDto expectedUser1 = new UserDto();
        UserDto expectedUser2 = new UserDto();

        List<UserDto> usersList = List.of(expectedUser1, expectedUser2);

        when(userRepository.findAll())
                .thenReturn(UserMapper.toEntityList(usersList));

        List<UserDto> actualUserList = userService.getAll();

        assertEquals(usersList, actualUserList);
    }

    @Test
    void saveUserTest() {
        UserDto userToSave = new UserDto();

        when(userRepository.save(UserMapper.toEntity(userToSave)))
                .thenReturn(UserMapper.toEntity(userToSave));

        UserDto actualUser = userService.saveUser(userToSave);

        assertEquals(userToSave, actualUser);
    }

    @Test
    void updateTest() {
        User userToUpdate = new User();
        userToUpdate.setId(1);
        userToUpdate.setName("Sergey");
        userToUpdate.setEmail("test@mail.ru");

        User userInDB = new User();
        userInDB.setId(1);
        userInDB.setName("ser");
        userInDB.setEmail("test2@mail.ru");

        when(userRepository.findById(1))
                .thenReturn(Optional.of(userInDB));
        when(userRepository.save(userToUpdate))
                .thenReturn(userToUpdate);

        UserDto actualUser = userService.update(userToUpdate.getId(), UserMapper.toDto(userToUpdate));

        assertEquals(UserMapper.toDto(userToUpdate), actualUser);
        verify(userRepository).findById(1);
        verify(userRepository).save(userToUpdate);
    }

    @Test
    void updateShouldThrowExceptionTest() {
        UserDto userToUpdate = new UserDto();
        userToUpdate.setId(1);
        userToUpdate.setName("UserToUpdate");
        userToUpdate.setEmail("test@mail.ru");
        when(userRepository.findById(userToUpdate.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getById(userToUpdate.getId()));

        verify(userRepository).findById(1);
        verify(userRepository, never()).save(UserMapper.toEntity(userToUpdate));
    }

    @Test
    void getByIdTest() {
        Integer userId = 1;
        User expectedUser = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        UserDto actualUser = userService.getById(userId);

        assertEquals(UserMapper.toDto(expectedUser), actualUser);
    }

    @Test
    void getByIdShouldThrowExceptionTest() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getById(userId));
    }

    @Test
    void removeByIdTest() {
        UserDto userToDelete = new UserDto();
        userToDelete.setId(1);

        userService.removeById(userToDelete.getId());

        verify(userRepository).deleteById(1);
    }
}