package ru.practicum.shareit.requests.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.requests.model.ItemRequest;

import java.util.List;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    ItemRequest findItemRequestById(Long id);

    List<ItemRequest> getItemRequestByRequesterIdOrderByCreatedAsc(Long userId);

    @Query("select i from ItemRequest i where i.requesterId <> ?1 order by i.created asc")
    Page<ItemRequest> findAllItemRequest(Long userId, Pageable page);

}
