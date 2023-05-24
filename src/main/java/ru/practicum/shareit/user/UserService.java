package ru.practicum.shareit.user;

import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User saveUser(User user) throws AlreadyExistException;

    User update(Integer userId, User user) throws ValidationException;

    User getById(Integer userId);

    void removeById(Integer userId);
}