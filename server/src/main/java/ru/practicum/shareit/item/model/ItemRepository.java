package ru.practicum.shareit.item.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Page<Item> findByUserId(Integer id, Pageable page);

    Optional<Item> findById(Integer id);

    void deleteByUserIdAndId(Integer userId, Integer itemId);

    List<Item> findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String firstQuery, String secondQuery, Pageable page);

    Optional<List<Item>> findByRequestId(Integer requestId);

    List<Item> findByRequestIdIn(Iterable<Integer> itemRequests);
}