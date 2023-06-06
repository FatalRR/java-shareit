package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.messages.LogMessages;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAll() {
        log.info(String.valueOf(LogMessages.COUNT), userService.getAll().size());
        return userService.getAll();
    }

    @PostMapping
    public UserDto save(@RequestBody UserDto userDto) {
        log.info(String.valueOf(LogMessages.TRY_ADD), userDto);
        try {
            return userService.saveUser(userDto);
        } catch (AlreadyExistException e) {
            throw new AlreadyExistException(ExceptionMessages.USER_EMAIL_EXIST);
        }
    }

    @PatchMapping("/{userId}")
    public UserDto update(@RequestBody UserDto userDto,
                          @PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_UPDATE), userDto);
        return userService.update(userId, userDto);
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_GET_OBJECT), userId);
        return userService.getById(userId);
    }

    @DeleteMapping("/{userId}")
    public void removeById(@PathVariable Integer userId) {
        log.info(String.valueOf(LogMessages.TRY_REMOVE), userId);
        userService.removeById(userId);
    }
}