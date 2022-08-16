package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Autowired
    public ItemServiceImpl(UserRepository userRepository, ItemRepository itemRepository,
                           CommentRepository commentRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
        this.commentRepository = commentRepository;
        this.bookingRepository = bookingRepository;
    }

    public Item createItem(Item item, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!StringUtils.hasText(item.getName())) {
            throw new ValidationException("Пустая строка");
        }
        if (!StringUtils.hasText(item.getDescription())) {
            throw new ValidationException("Пустая строка");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("Пустая строка");
        }
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    public Item getItemById(Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        Optional<Item> item = itemRepository.findItemDtoById(id);
        item.ifPresentOrElse((itemDto) -> {
            if (itemDto.getOwner().equals(userId)) {
                addBooking(itemDto);
            }
            addComments(itemDto);
        }, () -> {
            throw new NotFoundException();
        });
        return item.get();
    }

    private void addBooking(Item itemDto) {
        List<Booking> nextBookings = bookingRepository.getNextBooking(itemDto.getId(), LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            itemDto.setNextBooking(nextBookings.get(0));
        }

        List<Booking> lastBookings = bookingRepository.getLastBooking(itemDto.getId(), LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            itemDto.setLastBooking(lastBookings.get(0));
        }
    }

    private void addComments(Item itemDto) {
        List<CommentDto> comments = commentRepository.getCommentDtoByItemId(itemDto.getId())
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
        itemDto.setComments(comments);
    }

    public Item updateItemById(Item item, Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!userId.equals(getItemById(id, userId).getOwner())) {
            throw new NotFoundException();
        }
        if (item.getName() != null && item.getName().isEmpty()) {
            throw new ValidationException("Пустая строка");
        }
        if (item.getDescription() != null && item.getDescription().isEmpty()) {
            throw new ValidationException("Пустая строка");
        }
        Item lastItem = getItemById(id, userId);
        if (item.getName() != null) {
            lastItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            lastItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            lastItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(lastItem);
    }

    public List<Item> getItems(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
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
        if (Objects.isNull(size) && Objects.isNull(from)) {
            return itemRepository.findItemDtoByOwner(userId).stream()
                    .peek(this::addBooking)
                    .peek(this::addComments)
                    .collect(Collectors.toList());
        } else {
            return itemRepository.findItemDtoByOwner(userId, PageRequest.of(from, size))
                    .stream()
                    .peek(this::addBooking)
                    .peek(this::addComments)
                    .collect(Collectors.toList());
        }
    }

    public List<Item> searchItems(String text, Integer from, Integer size) {
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
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        if (Objects.isNull(size) && Objects.isNull(from)) {
            return itemRepository.search(text);
        } else {
            return itemRepository.search(text, PageRequest.of(from, size)).toList();
        }
    }

    @Override
    public CommentDto addComment(Long userId, Long itemId, Comment commentDto) {
        if (!StringUtils.hasText(commentDto.getText())) {
            throw new ValidationException("Пустая строка");
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException();
        }

        List<Booking> bookingList = bookingRepository.findBookingDtoByBookerIdAndItemIdOrderByEndAsc(userId, itemId);
        if (bookingList.isEmpty()) {
            throw new ValidationException("Пустой список бронирований");
        }
        if (bookingList.get(0).getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("У пользователя нету завершенных бронирований данной вещи");
        }

        commentDto.setItemId(itemId);
        commentDto.setAuthorId(userId);
        commentDto.setCreated(LocalDateTime.now());
        return convert(commentRepository.save(commentDto));
    }

    private CommentDto convert(Comment commentDto) {
        CommentDto comment = new CommentDto();

        comment.setText(commentDto.getText());
        comment.setId(commentDto.getId());
        comment.setCreated(commentDto.getCreated());
        comment.setAuthorName(userRepository.findUserDtoById(commentDto.getAuthorId()).get().getName());

        return comment;
    }
}
