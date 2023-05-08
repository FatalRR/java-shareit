package ru.practicum.shareit.user;

import java.util.List;

public interface UserRepository {
    List<User> getAll();

    User save(User user);

    User update(User user);

    User getById(Integer id);

    void removeById(Integer id);
}