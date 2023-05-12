package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;

import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> userEmails = new HashSet<>();
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
        if (userEmails.contains(user.getEmail())) {
            throw new AlreadyExistException(ExceptionMessages.USER_EMAIL_EXIST);
        }

        user.setId(generateId());
        users.put(user.getId(), user);
        userEmails.add(user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }

        for (User userCheck : users.values()) {
            if (userCheck.getEmail().equals(user.getEmail()) && !user.getId().equals(userCheck.getId())) {
                throw new AlreadyExistException(ExceptionMessages.USER_EMAIL_EXIST);
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
                userEmails.remove(userTemp.getEmail());
                userEmails.add(user.getEmail());
            }
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        return user;
    }

    @Override
    public void removeById(Integer id) {
        User user = users.remove(id);
        if (user == null) {
            throw new NotFoundException(ExceptionMessages.USER_NOT_FOUND);
        }
        userEmails.remove(user.getEmail());
    }
}