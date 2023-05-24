package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), userService.getAll().size());
        return userService.getAll();
    }

    @PostMapping
    public User save(@RequestBody @Valid User user) {
        log.info(String.valueOf(LogMessages.TRY_ADD), user);
        try {
            return userService.saveUser(user);
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(ExceptionMessages.USER_EMAIL_EXIST);
        }
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody User user, @PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_UPDATE), user);
        return userService.update(userId, user);
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_REMOVE), userId);
        userService.removeById(userId);
    }
}