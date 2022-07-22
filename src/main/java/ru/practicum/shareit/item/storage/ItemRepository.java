package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<ItemDto, Long> {
    @Query("select i from ItemDto i " +
            "where i.owner = ?1")
    List<ItemDto> findAllByOwner(Long ownerId);

    @Query("select i from ItemDto i " +
            "where i.id = ?1")
    Optional<ItemDto> findItemDtoById(Long id);

    @Query(" select i from ItemDto i " +
            "where (upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%'))) and i.available = true")
    List<ItemDto> search(String text);
}
