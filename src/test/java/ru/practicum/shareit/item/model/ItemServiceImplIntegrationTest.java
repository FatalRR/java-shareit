package ru.practicum.shareit.item.model;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemServiceImplIntegrationTest {
    private final ItemService service;
    private final UserService userService;

    @Test
    void getItemsTest() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setName("Sergey");
        userService.saveUser(UserMapper.toDto(user));
        UserDto userFromBase = userService.getAll().get(0);
        ItemDto itemDto = new ItemDto();
        itemDto.setName("test");
        itemDto.setAvailable(true);
        service.save(userFromBase.getId(), itemDto);
        List<ItemWithBooking> itemFromBase = (List<ItemWithBooking>) service.getItems(userFromBase.getId(), 1, 10);
        assertThat(itemFromBase, notNullValue());
        assertThat(itemFromBase.get(0).getName(), equalTo(itemDto.getName()));
    }
}