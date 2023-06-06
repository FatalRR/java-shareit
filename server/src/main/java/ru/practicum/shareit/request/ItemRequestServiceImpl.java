package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemMapper;
import ru.practicum.shareit.item.model.ItemRepository;
import ru.practicum.shareit.messages.ExceptionMessages;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequestDto addNewItemRequest(Integer userId, ItemRequestDto itemRequestDto) {
        validateUserId(userId);
        ItemRequest request = ItemRequestMapper.toEntity(userId, itemRequestDto, null);
        return ItemRequestMapper.toDto(itemRequestRepository.save(request));
    }

    @Override
    public Collection<ItemRequestDto> getRequests(Integer userId) {
        validateUserId(userId);
        List<ItemRequest> itemRequests = itemRequestRepository.findByUserId(userId);
        return mapToRequestWithItems(itemRequests);
    }

    public List<ItemRequestDto> mapToRequestWithItems(Iterable<ItemRequest> itemRequests) {
        List<Integer> requestIds = StreamSupport.stream(itemRequests.spliterator(), false)
                .map(ItemRequest::getId)
                .collect(Collectors.toList());

        List<ItemRequestDto> itemRequestWithItems = StreamSupport.stream(itemRequests.spliterator(), false)
                .map(ItemRequestMapper::toDto)
                .collect(Collectors.toList());

        List<Item> items = itemRepository.findByRequestIdIn(requestIds);
        Map<Integer, List<Item>> itemsByRequestId = items.stream()
                .collect(Collectors.groupingBy(Item::getRequestId));

        itemRequestWithItems.forEach(requestDto -> {
            List<Item> requestItems = itemsByRequestId.getOrDefault(requestDto.getId(), Collections.emptyList());
            requestDto.setItems(ItemMapper.mapToItemDto(requestItems));
        });

        return itemRequestWithItems;
    }

    @Override
    public List<ItemRequestDto> getAllRequest(Integer userId, Integer from, Integer size) {
        if (from < 0) {
            throw new ValidationException(ExceptionMessages.FROM_IS_NEGATIVE);
        }

        Sort sortByDate = Sort.by(Sort.Direction.ASC, "created");
        int pageIndex = from / size;

        Pageable page = PageRequest.of(pageIndex, size, sortByDate);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAll(page);
        List<ItemRequest> itemRequestList = itemRequestPage.toList();
        for (ItemRequest itemRequest : itemRequestList) {
            if (Objects.equals(itemRequest.getUserId(), userId)) {
                if (itemRequestList.size() == 1) {
                    return new ArrayList<>();
                }
                itemRequestList.remove(itemRequest);
            }
        }
        return mapToRequestWithItems(itemRequestPage.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Integer userId, Integer requestId) {
        validateUserId(userId);
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(()
                -> new NotFoundException(String.format(ExceptionMessages.REQUEST_NOT_FOUND, requestId)));
        List<Item> items = itemRepository.findByRequestId(requestId).orElse(null);
        itemRequest.setItems(items);
        return ItemRequestMapper.toDto(itemRequest);
    }

    private void validateUserId(Integer userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException(ExceptionMessages.USER_NOT_FOUND));
    }
}