package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public BookingDto create(BookingDto bookingDto, Long userId) {
        if (bookingDto.getItemId() == null || bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException();
        }
        if (userId == null) {
            throw new ValidationException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(bookingDto.getEnd())) {
            throw new ValidationException();
        }
        if (currentTime.isAfter(bookingDto.getStart())) {
            throw new ValidationException();
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException();
        }

        ItemDto item = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!item.getAvailable()) {
            throw new ValidationException();
        }
        if (userId.equals(item.getOwner())) {
            throw new NotFoundException();
        }

        bookingDto.setBookerId(userId);
        bookingDto.setStatus("WAITING");
        return bookingRepository.save(bookingDto);
    }

    public Booking getBookingById(Long bookingId, Long userId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        BookingDto bookingDto = bookingRepository.findBookingDtoById(bookingId).get();
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }

        ItemDto itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!Objects.equals(itemDto.getOwner(), userId) && !Objects.equals(bookingDto.getBookerId(), userId)) {
            throw new NotFoundException();
        }

        return mapper(bookingDto);
    }

    public List<Booking> getBookings(String state, Long userId) {
        return bookingRepository.findBookingDtoByBookerId(userId)
                .stream()
                .peek(this::stateMaker)
                .filter((bookingDto -> state.equals("ALL") || bookingDto.getState().equals(state)))
                .map(this::mapper)
                .collect(Collectors.toList());
    }

    public List<Booking> getOwnerBookings(String state, Long userId) {
        List<BookingDto> bookingDtoList = bookingRepository.findBookingDtoByOwnerId(userId)
                .stream()
                .peek(this::stateMaker)
                .filter((bookingDto -> state.equals("ALL") || bookingDto.getState().equals(state)))
                .collect(Collectors.toList());

        if (bookingDtoList.isEmpty()) {
            throw new NotFoundException();
        } else {
            return bookingDtoList
                    .stream()
                    .map(this::mapper)
                    .collect(Collectors.toList());
        }
    }

    public Booking approve(String approved, Long bookingId, Long userId) {
        if (bookingId == null || userId == null) {
            throw new ValidationException();
        }
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        BookingDto bookingDto = bookingRepository.findBookingDtoById(bookingId).get();
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }
        if (Objects.equals(bookingDto.getBookerId(), userId)) {
            throw new NotFoundException();
        }
        if (!"WAITING".equalsIgnoreCase(bookingDto.getStatus())) {
            throw new ValidationException();
        }

        ItemDto itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!Objects.equals(itemDto.getOwner(), userId)) {
            throw new ValidationException();
        }

        if ("TRUE".equalsIgnoreCase(approved)) {
            bookingDto.setStatus("APPROVED");
            return mapper(bookingRepository.save(bookingDto));
        }
        if ("FALSE".equalsIgnoreCase(approved)) {
            bookingDto.setStatus("REJECTED");
            return mapper(bookingRepository.save(bookingDto));
        } else {
            throw new ValidationException();
        }
    }

    private Booking mapper(BookingDto bookingDto) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStatus(bookingDto.getStatus());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        User booker = new User();
        booker.setId(bookingDto.getBookerId());
        booking.setBooker(booker);

        Item item = new Item();
        ItemDto itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        item.setId(bookingDto.getItemId());
        item.setName(itemDto.getName());
        booking.setItem(item);

        return booking;
    }

    private void stateMaker(BookingDto bookingDto) {
        if ("WAITING".equals(bookingDto.getStatus())) {
            bookingDto.setState("WAITING");
        } else if ("REJECTED".equals(bookingDto.getStatus())) {
            bookingDto.setState("REJECTED");
        } else if (bookingDto.getStart().isAfter(LocalDateTime.now())) {
            bookingDto.setState("FUTURE");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            bookingDto.setState("PAST");
        } else {
            bookingDto.setState("CURRENT");
        }
    }
}
