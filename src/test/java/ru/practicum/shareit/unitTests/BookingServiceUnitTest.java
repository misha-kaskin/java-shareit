package ru.practicum.shareit.unitTests;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceUnitTest {
    @MockBean
    private final BookingRepository bookingRepository;
    @MockBean
    private final ItemRepository itemRepository;
    @MockBean
    private final UserRepository userRepository;
    private final BookingService bookingService;

    @Test
    void bookingCreateTest() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(booking)).thenReturn(booking);

        Assertions.assertEquals(booking.getId(), bookingService.create(booking, 1L).getId());
    }

    @Test
    void createBookingTestShouldThrowNotFoundException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.create(booking, 1L));
    }

    @Test
    void createBookingTestShouldThrowValidationException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(null);
        booking.setItemId(null);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        Assertions.assertThrows(ValidationException.class, () -> bookingService.create(booking, 1L));
    }

    @Test
    void getBookingByIdTest() {
        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        UserDto userDto = new UserDto();
        userDto.setEmail("user@mail.ru");
        userDto.setName("user");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.findBookingDtoById(1L)).thenReturn(Optional.of(booking));

        Assertions.assertEquals(bookingDto.getId(), bookingService.getBookingById(1L, 1L).getId());
    }

    @Test
    void getBookingByIdTestShouldThrowNotFoundException() {
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingsTest() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        List<Booking> bookings = List.of(booking);

        UserDto userDto = new UserDto();
        userDto.setEmail("user@mail.ru");
        userDto.setName("user");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(bookingRepository
                .findBookingDtoByBookerIdOrderByStartDesc(1L))
                .thenReturn(bookings);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertEquals(1,
                bookingService.getBookings("ALL", 1L, null, null).size());
        Assertions.assertEquals(1L,
                bookingService.getBookings("ALL", 1L, null, null).get(0).getId());
    }

    @Test
    void getBookingsTestShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.getBookings("ALL", 1L, null, 1));
    }

    @Test
    void getBookingsTestShouldThrowNotFoundException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        List<Booking> bookings = List.of();

        UserDto userDto = new UserDto();
        userDto.setEmail("user@mail.ru");
        userDto.setName("user");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(bookingRepository
                        .findBookingDtoByBookerIdOrderByStartDesc(1L))
                .thenReturn(bookings);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getBookings("ALL", 1L, null, null));
    }

    @Test
    void approveTest() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(2L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));
        booking.setStatus("WAITING");

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(1L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(itemRepository.existsById(1L)).thenReturn(true);
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(true);
        Mockito.when(bookingRepository.findBookingDtoById(1L)).thenReturn(Optional.of(booking));
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));
        Mockito.when(bookingRepository.save(booking)).thenReturn(booking);

        Assertions.assertEquals(1L, bookingService.approve("TRUE", 1L, 1L).getId());
    }

    @Test
    void approveTestShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.approve("TRUE", null, null));
    }

    @Test
    void approveTestShouldThrowNotFoundException() {
        Mockito.when(bookingRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.approve("TRUE", 1L, 1L));
    }

    @Test
    void getOwnerBookingsTest() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        List<Booking> bookings = List.of(booking);

        UserDto userDto = new UserDto();
        userDto.setEmail("user@mail.ru");
        userDto.setName("user");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(bookingRepository
                        .findBookingDtoByOwnerId(1L))
                .thenReturn(bookings);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertEquals(1,
                bookingService.getOwnerBookings("ALL", 1L, null, null).size());
        Assertions.assertEquals(1L,
                bookingService.getOwnerBookings("ALL", 1L, null, null).get(0).getId());
    }

    @Test
    void getOwnerBookingsTestShouldThrowValidationException() {
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.getOwnerBookings("ALL", 1L, null, 1));
    }

    @Test
    void getOwnerBookingsTestShouldThrowNotFoundException() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setBookerId(1L);
        booking.setItemId(1L);
        booking.setStart(LocalDateTime.now().plusHours(1));
        booking.setEnd(LocalDateTime.now().plusDays(1));

        List<Booking> bookings = List.of();

        UserDto userDto = new UserDto();
        userDto.setEmail("user@mail.ru");
        userDto.setName("user");
        userDto.setId(1L);

        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setDescription("description");
        itemDto.setName("name");
        itemDto.setAvailable(true);
        itemDto.setOwner(userDto);

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setBooker(userDto);
        bookingDto.setItem(itemDto);
        bookingDto.setStart(LocalDateTime.now().plusHours(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(1));

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Item item = new Item();
        item.setName("name");
        item.setAvailable(true);
        item.setId(1L);
        item.setOwner(2L);
        item.setDescription("description");
        item.setRequestId(1L);

        Mockito.when(bookingRepository
                        .findBookingDtoByOwnerId(1L))
                .thenReturn(bookings);
        Mockito.when(itemRepository.findItemDtoById(1L)).thenReturn(Optional.of(item));

        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getOwnerBookings("ALL", 1L, null, null));
    }
}
