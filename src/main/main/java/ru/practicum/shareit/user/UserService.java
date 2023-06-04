package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getAll();

    UserDto saveUser(UserDto userDto) throws AlreadyExistException;

    UserDto update(Integer userId, UserDto userDto) throws ValidationException;

    UserDto getById(Integer userId);

    void removeById(Integer userId);
}