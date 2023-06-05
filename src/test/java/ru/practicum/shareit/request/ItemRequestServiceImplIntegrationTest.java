package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplIntegrationTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemRequestService service;

    @Test
    void getAllRequestTest() {
        User user = new User();
        user.setEmail("test@mail.ru");
        user.setName("Sergey");
        userService.saveUser(UserMapper.toDto(user));
        UserDto userFromBase = userService.getAll().get(0);
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("description");
        service.addNewItemRequest(userFromBase.getId(), itemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery("Select ir from ItemRequest ir where ir.userId = :userId", ItemRequest.class);
        ItemRequest itemRequestFromBase = query.setParameter("userId", userFromBase.getId()).getSingleResult();

        assertThat(itemRequestFromBase.getDescription(), equalTo(itemRequestDto.getDescription()));
    }
}