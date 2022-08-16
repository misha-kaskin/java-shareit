package ru.practicum.shareit.jpaTest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingJpaTest {
    private final TestEntityManager entityManager;
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Test
    void bookingJpaTest() {
        User user = new User();
        user.setId(null);
        user.setEmail("user@mail.ru");
        user.setName("user");

        userRepository.save(user);

        User otherUser = new User();
        otherUser.setId(null);
        otherUser.setEmail("otherUser@mail.ru");
        otherUser.setName("otherUser");

        userRepository.save(otherUser);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setDescription("description");
        item.setId(null);
        item.setOwner(1L);

        itemRepository.save(item);

        Booking booking = new Booking();
        booking.setStatus("WAITING");
        booking.setItemId(1L);
        booking.setBookerId(2L);
        booking.setStart(LocalDateTime.now().plusHours(1L));
        booking.setEnd(LocalDateTime.now().plusDays(1L));

        Booking lastBooking = new Booking();
        lastBooking.setStatus("WAITING");
        lastBooking.setItemId(1L);
        lastBooking.setBookerId(2L);
        lastBooking.setStart(LocalDateTime.now().minusDays(1L));
        lastBooking.setEnd(LocalDateTime.now().minusHours(1L));

        Assertions.assertNull(booking.getId());
        Assertions.assertNull(lastBooking.getId());

        bookingRepository.save(booking);
        bookingRepository.save(lastBooking);

        Assertions.assertNotNull(booking.getId());
        Assertions.assertNotNull(lastBooking.getId());

        Assertions.assertEquals(2, bookingRepository.findBookingDtoByOwnerId(1L).size());
        Assertions.assertEquals(1,
                bookingRepository.getNextBooking(1L, LocalDateTime.now()).size());
        Assertions.assertEquals(1L,
                bookingRepository.getNextBooking(1L, LocalDateTime.now()).get(0).getId());
        Assertions.assertEquals(2L,
                bookingRepository.getLastBooking(1L, LocalDateTime.now()).get(0).getId());
    }
}
