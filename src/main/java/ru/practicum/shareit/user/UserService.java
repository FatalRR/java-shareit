package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User save(User user);

    User update(Integer userId, User user);

    User getById(Integer id);

    void removeById(Integer id);
}