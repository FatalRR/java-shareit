package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    private Integer generateId() {
        return id++;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User save(User user) {
        for (User userCheck : users.values()) {
            if (userCheck.getEmail().equals(user.getEmail())) {
                throw new AlreadyExistException(String.valueOf(ExceptionMessages.USER_EMAIL_EXIST));
            }
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(String.valueOf(ExceptionMessages.USER_NOT_FOUND));
        }

        for (User userCheck : users.values()) {
            if (userCheck.getEmail().equals(user.getEmail()) && !user.getId().equals(userCheck.getId())) {
                throw new AlreadyExistException(String.valueOf(ExceptionMessages.USER_EMAIL_EXIST));
            }
        }

        for (User userTemp : users.values()) {
            if (userTemp.getId().equals(user.getId())) {
                if (user.getName() == null) {
                    user.setName(userTemp.getName());
                }
                if (user.getEmail() == null) {
                    user.setEmail(userTemp.getEmail());
                }
                users.replace(user.getId(), user);
            }
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException(String.valueOf(ExceptionMessages.USER_NOT_FOUND));
        }
    }

    @Override
    public void removeById(Integer id) {
        if (users.containsKey(id)) {
            users.remove(id);
        } else {
            throw new NotFoundException(String.valueOf(ExceptionMessages.USER_NOT_FOUND));
        }
    }
}