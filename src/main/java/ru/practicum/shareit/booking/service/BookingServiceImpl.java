package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
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

    public Booking create(Booking bookingDto, Long userId) {
        if (bookingDto.getItemId() == null
                || bookingDto.getStart() == null
                || bookingDto.getEnd() == null) {
            throw new ValidationException("Пустая строка");
        }
        if (userId == null) {
            throw new ValidationException("Пустой userId");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isAfter(bookingDto.getEnd())) {
            throw new ValidationException("Бронирование в прошлом");
        }
        if (currentTime.isAfter(bookingDto.getStart())) {
            throw new ValidationException("Бронирование в прошлом");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Бронирование в прошлом");
        }

        Item item = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!item.getAvailable()) {
            throw new ValidationException("Бронирование недоступно");
        }
        if (userId.equals(item.getOwner())) {
            throw new NotFoundException();
        }

        bookingDto.setBookerId(userId);
        bookingDto.setStatus("WAITING");
        return bookingRepository.save(bookingDto);
    }

    public BookingDto getBookingById(Long bookingId, Long userId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        Booking bookingDto = bookingRepository.findBookingDtoById(bookingId).get();
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }

        Item itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!Objects.equals(itemDto.getOwner(), userId) && !Objects.equals(bookingDto.getBookerId(), userId)) {
            throw new NotFoundException();
        }

        return convert(bookingDto);
    }

    public List<BookingDto> getBookings(String state, Long userId, Integer from, Integer size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(new StringBuilder()
                    .append("Unknown state: ").append(state)
                    .toString());
        }

        if (!((Objects.nonNull(from) && Objects.nonNull(size))
                || (Objects.isNull(from) && Objects.isNull(size)))) {
            throw new ValidationException("Некорректные параметры пагинации");
        }
        if (Objects.nonNull(from)) {
            if (from < 0) {
                throw new ValidationException("Некорректные параметры пагинации");
            }
        }
        if (Objects.nonNull(size)) {
            if (size < 1) {
                throw new ValidationException("Некорректные параметры пагинации");
            }
        }

        List<Booking> bookingList;

        if (Objects.isNull(from) && Objects.isNull(size)) {
            bookingList = bookingRepository.findBookingDtoByBookerIdOrderByStartDesc(userId);
        } else {
            bookingList = bookingRepository
                    .findBookingDtoByBookerIdOrderByStartDesc(userId, PageRequest.of(from, size))
                    .toList();
        }

        List<BookingDto> bookingDtoList = bookingList
                .stream()
                .peek(this::stateMaker)
                .filter(bookingDto -> state.equals("ALL")
                        || bookingDto.getState().equals(state)
                        || bookingDto.getStatus().equals(state))
                .map(this::convert)
                .collect(Collectors.toList());
        if (bookingDtoList.isEmpty()) {
            throw new NotFoundException();
        } else {
            return bookingDtoList;
        }
    }

    public List<BookingDto> getOwnerBookings(String state, Long userId, Integer from, Integer size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(new StringBuilder()
                    .append("Unknown state: ").append(state)
                    .toString());
        }

        if (!((Objects.nonNull(from) && Objects.nonNull(size))
                || (Objects.isNull(from) && Objects.isNull(size)))) {
            throw new ValidationException("Некорректные параметры пагинации");
        }
        if (Objects.nonNull(from)) {
            if (from < 0) {
                throw new ValidationException("Некорректные параметры пагинации");
            }
        }
        if (Objects.nonNull(size)) {
            if (size < 1) {
                throw new ValidationException("Некорректные параметры пагинации");
            }
        }

        List<Booking> bookingDtoList;

        if (Objects.isNull(size) && Objects.isNull(from)) {
            bookingDtoList = bookingRepository.findBookingDtoByOwnerId(userId);
        } else {
            bookingDtoList = bookingRepository
                    .findBookingDtoByOwnerId(userId, PageRequest.of(from, size))
                    .toList();
        }

       bookingDtoList = bookingRepository.findBookingDtoByOwnerId(userId)
                .stream()
                .peek(this::stateMaker)
                .filter(bookingDto -> state.equals("ALL")
                        || bookingDto.getState().equals(state)
                        || bookingDto.getStatus().equals(state))
                .collect(Collectors.toList());

        if (bookingDtoList.isEmpty()) {
            throw new NotFoundException();
        } else {
            return bookingDtoList
                    .stream()
                    .map(this::convert)
                    .collect(Collectors.toList());
        }
    }

    public BookingDto approve(String approved, Long bookingId, Long userId) {
        if (bookingId == null || userId == null) {
            throw new ValidationException("Пустые поля");
        }
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }

        Booking bookingDto = bookingRepository.findBookingDtoById(bookingId).get();
        if (!itemRepository.existsById(bookingDto.getItemId())) {
            throw new NotFoundException();
        }
        if (Objects.equals(bookingDto.getBookerId(), userId)) {
            throw new NotFoundException();
        }
        if (!"WAITING".equalsIgnoreCase(bookingDto.getStatus())) {
            throw new ValidationException("Заказ одобрен/отклонен");
        }

        Item itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        if (!Objects.equals(itemDto.getOwner(), userId)) {
            throw new ValidationException("");
        }

        if ("TRUE".equalsIgnoreCase(approved)) {
            bookingDto.setStatus("APPROVED");
            return convert(bookingRepository.save(bookingDto));
        }
        if ("FALSE".equalsIgnoreCase(approved)) {
            bookingDto.setStatus("REJECTED");
            return convert(bookingRepository.save(bookingDto));
        } else {
            throw new ValidationException("Неправильный статус");
        }
    }

    private BookingDto convert(Booking bookingDto) {
        BookingDto booking = new BookingDto();
        booking.setId(bookingDto.getId());
        booking.setStatus(bookingDto.getStatus());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());

        UserDto booker = new UserDto();
        booker.setId(bookingDto.getBookerId());
        booking.setBooker(booker);

        ItemDto item = new ItemDto();
        Item itemDto = itemRepository.findItemDtoById(bookingDto.getItemId()).get();
        item.setId(bookingDto.getItemId());
        item.setName(itemDto.getName());
        booking.setItem(item);

        return booking;
    }

    private void stateMaker(Booking bookingDto) {
        if (bookingDto.getStart().isAfter(LocalDateTime.now())) {
            bookingDto.setState("FUTURE");
        } else if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            bookingDto.setState("PAST");
        } else {
            bookingDto.setState("CURRENT");
        }
    }
}
