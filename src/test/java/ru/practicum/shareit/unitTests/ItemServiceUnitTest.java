package ru.practicum.shareit.unitTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceUnitTest {
    @MockBean
    private final UserRepository userRepository;
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final CommentRepository commentRepository;
    @MockBean
    private final BookingRepository bookingRepository;
    private final ItemService itemService;

    @Test
    void createItemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.save(item)).thenReturn(item);

        Assertions.assertEquals(item.getName(), itemService.createItem(item, 1L).getName());
    }

    @Test
    void createItemTestShouldThrowNotFoundException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.createItem(item, 1L));
    }

    @Test
    void createItemTestShouldThrowValidationException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("");
        item.setDescription("description");
        item.setAvailable(true);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> itemService.createItem(item, 1L));
    }

    @Test
    void getItemByIdTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertEquals(item.getName(), itemService.getItemById(1L, 1L).getName());
    }

    @Test
    void getItemByIdTestShouldThrowNotFoundException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L, 1L));
    }

    @Test
    void updateItemTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.save(item)).thenReturn(item);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertEquals(item.getName(), itemService.updateItemById(item, 1L, 1L).getName());
    }

    @Test
    void updateItemTestShouldThrowNotFoundException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);
        Mockito.when(itemRepository.save(item)).thenReturn(item);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItemById(item, 1L, 1L));
    }

    @Test
    void updateItemTestShouldThrowValidationException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("");
        item.setAvailable(true);
        item.setOwner(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.save(item)).thenReturn(item);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(ValidationException.class, () -> itemService.updateItemById(item, 1L, 1L));
    }

    @Test
    void getItemsTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.findItemDtoByOwner(1L)).thenReturn(items);

        Assertions.assertEquals(1, itemService.getItems(1L, null, null).size());
        Assertions.assertEquals(item.getName(), itemService
                .getItems(1L, null, null).get(0).getName());
    }

    @Test
    void getItemsTestShouldThrowNotFoundException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);
        Mockito.when(itemRepository.findItemDtoByOwner(1L)).thenReturn(items);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItems(1L, null, null));
    }

    @Test
    void getItemsTestShouldThrowValidationException() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        List<Item> items = List.of(item);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.findItemDtoByOwner(1L)).thenReturn(items);

        Assertions.assertThrows(ValidationException.class, () -> itemService.getItems(1L, 0, null));
    }

    @Test
    void searchTest() {
        Item item = new Item();
        item.setId(1L);
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        List<Item> items = List.of(item);

        Mockito.when(itemRepository.search("text")).thenReturn(items);

        Assertions.assertEquals(1, itemService.searchItems("text", null, null).size());
        Assertions.assertEquals(item.getName(),
                itemService.searchItems("text", null, null).get(0).getName());
    }

    @Test
    void searchTestShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class,
                () -> itemService.searchItems("text", null, 1));
    }

    @Test
    void addCommentTest() {
        Booking booking = new Booking();
        booking.setEnd(LocalDateTime.now().minusHours(1));
        booking.setStart(LocalDateTime.now().minusDays(1));
        booking.setItemId(1L);
        booking.setBookerId(1L);

        List<Booking> bookings = List.of(booking);

        User user = new User();
        user.setEmail("user@mail.ru");
        user.setName("user");
        user.setId(1L);

        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItemId(1L);
        comment.setAuthorId(1L);
        comment.setCreated(LocalDateTime.now());

        CommentDto commentDto = new CommentDto();
        commentDto.setCreated(LocalDateTime.now());
        commentDto.setId(1L);
        commentDto.setText("text");
        commentDto.setAuthorName("user");

        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito
                .when(bookingRepository.findBookingDtoByBookerIdAndItemIdOrderByEndAsc(1L, 1L))
                .thenReturn(bookings);
        Mockito.when(userRepository.findUserDtoById(1L)).thenReturn(Optional.of(user));
        Mockito.when(commentRepository.save(comment)).thenReturn(comment);

        Assertions.assertEquals(user.getName(), itemService.addComment(1L, 1L, comment).getAuthorName());
    }

    @Test
    void addCommentTestShouldThrowValidationException() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("");
        comment.setItemId(1L);
        comment.setAuthorId(1L);
        comment.setCreated(LocalDateTime.now());

        Assertions.assertThrows(ValidationException.class, () -> itemService.addComment(1L, 1L, comment));
    }

    @Test
    void addCommentTestShouldThrowNotFoundException() {
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setText("text");
        comment.setItemId(1L);
        comment.setAuthorId(1L);
        comment.setCreated(LocalDateTime.now());

        Mockito.when(itemRepository.existsById(1L)).thenReturn(false);
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> itemService.addComment(1L, 1L, comment));
    }
}
