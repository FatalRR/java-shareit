package ru.practicum.shareit.item.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    List<Item> findByUserId(Integer id);

    Optional<Item> findById(Integer id);

    void deleteByUserIdAndId(Integer userId, Integer itemId);

    List<Item> findItemByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String firstQuery, String secondQuery);
}