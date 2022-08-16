package ru.practicum.shareit.integrationTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceIntegrationTest {
    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void bookingServiceTest() {
        Item item = new Item();
        item.setName("name");
        item.setDescription("description");
        item.setAvailable(true);
        item.setOwner(1L);

        User user = new User();
        user.setName("user");
        user.setEmail("user@mail.ru");
        user.setId(null);

        User otherUser = new User();
        otherUser.setName("otherUser");
        otherUser.setEmail("otherUser@mail.ru");
        otherUser.setId(null);

        userService.createUser(user);
        userService.createUser(otherUser);
        itemService.createItem(item, 1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStatus("WAITING");
        booking.setStart(LocalDateTime.now().plusHours(1L));
        booking.setEnd(LocalDateTime.now().plusDays(1L));

        Assertions.assertEquals(1L, bookingService.create(booking, 2L).getId());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(booking, 1L));
        Assertions.assertEquals(1,
                bookingService.getBookings("FUTURE", 2L, null, null).size());
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.getBookings("UNKNOWN", 2L, null, null));
    }
}
