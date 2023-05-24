package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.AlreadyExistException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.messages.ExceptionMessages;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public User saveUser(User user) throws AlreadyExistException {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(Integer userId, User user) {
        user.setId(userId);
        Optional<User> userTemp = userRepository.findById(userId);
        if (user.getName() == null) {
            user.setName(userTemp.get().getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userTemp.get().getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }

    @Transactional
    @Override
    public void removeById(Integer id) {
        userRepository.deleteById(id);
    }
}