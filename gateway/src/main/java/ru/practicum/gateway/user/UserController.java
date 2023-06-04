package ru.practicum.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.exception.AlreadyExistException;
import ru.practicum.gateway.messages.ExceptionMessages;
import ru.practicum.gateway.user.dto.UserDto;
import ru.practicum.gateway.messages.LogMessages;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> save(@Valid @RequestBody UserDto userDto) {
        log.debug(String.valueOf(LogMessages.TRY_ADD), userDto);
        try {
            return userClient.saveUser(userDto);
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(ExceptionMessages.USER_EMAIL_EXIST);
        }
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Integer userId,
                                         @RequestBody UserDto userDto) {
        log.debug(String.valueOf(LogMessages.TRY_UPDATE), userId);
        return userClient.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> removeById(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_REMOVE), userId);
        return userClient.removeById(userId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Integer userId) {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userClient.getById(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.debug(String.valueOf(LogMessages.TRY_GET_OBJECT));
        return userClient.getAll();
    }
}