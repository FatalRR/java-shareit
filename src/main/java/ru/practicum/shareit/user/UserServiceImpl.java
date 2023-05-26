package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getAll() {
        return UserMapper.toDtoList(userRepository.findAll());
    }

    @Transactional
    @Override
    public UserDto saveUser(UserDto userDto) throws AlreadyExistException {
        return UserMapper.toDto(userRepository.save(UserMapper.toEntity(userDto)));
    }

    @Transactional
    @Override
    public UserDto update(Integer userId, UserDto userDto) {
        User user = UserMapper.toEntity(userDto);
        user.setId(userId);
        Optional<User> userTemp = userRepository.findById(userId);
        if (user.getName() == null) {
            user.setName(userTemp.get().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userTemp.get().getEmail());
        }
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Integer id) {
        return UserMapper.toDto(userRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessages.USER_NOT_FOUND)));
    }

    @Transactional
    @Override
    public void removeById(Integer id) {
        userRepository.deleteById(id);
    }
}