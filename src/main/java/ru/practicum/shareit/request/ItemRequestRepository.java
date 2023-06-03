package ru.practicum.shareit.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    Optional<ItemRequest> findById(Integer userId);

    List<ItemRequest> findByUserId(Integer userId);
}