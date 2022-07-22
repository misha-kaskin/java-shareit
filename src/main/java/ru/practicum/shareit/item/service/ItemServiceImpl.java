package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public ItemDto createItem(ItemDto item, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!StringUtils.hasText(item.getName())) {
            throw new ValidationException();
        }
        if (!StringUtils.hasText(item.getDescription())) {
            throw new ValidationException();
        }
        if (item.getAvailable() == null) {
            throw new ValidationException();
        }
        item.setOwner(userId);
        return itemRepository.save(item);
    }

    public ItemDto getItemById(Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        Optional<ItemDto> item = itemRepository.findItemDtoById(id);
        if (item.isPresent()) {
            ItemDto itemDto = item.get();

            if (itemDto.getOwner().equals(userId)) {
                addBooking(itemDto);
            }
            addComments(itemDto);

            return itemDto;
        } else {
            throw new NotFoundException();
        }
    }

    private void addBooking(ItemDto itemDto) {
        List<BookingDto> nextBookings = bookingRepository.getNextBooking(itemDto.getId(), LocalDateTime.now());
        if (!nextBookings.isEmpty()) {
            itemDto.setNextBooking(nextBookings.get(0));
        }

        List<BookingDto> lastBookings = bookingRepository.getLastBooking(itemDto.getId(), LocalDateTime.now());
        if (!lastBookings.isEmpty()) {
            itemDto.setLastBooking(lastBookings.get(0));
        }
    }

    private void addComments(ItemDto itemDto) {
        List<Comment> comments = commentRepository.getCommentDtoByItemId(itemDto.getId())
                .stream()
                .map(this::mapper)
                .collect(Collectors.toList());
        itemDto.setComments(comments);
    }

    public ItemDto updateItemById(ItemDto item, Long id, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!userId.equals(getItemById(id, userId).getOwner())) {
            throw new NotFoundException();
        }
        if (item.getName() != null && item.getName().isEmpty()) {
            throw new ValidationException();
        }
        if (item.getDescription() != null && item.getDescription().isEmpty()) {
            throw new ValidationException();
        }
        ItemDto lastItem = getItemById(id, userId);
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

    public List<ItemDto> getItems(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        return itemRepository.findAllByOwner(userId).stream()
                .peek(this::addBooking)
                .peek(this::addComments)
                .collect(Collectors.toList());
    }

    public List<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text);
    }

    @Override
    public Comment addComment(Long userId, Long itemId, CommentDto commentDto) {
        if (!StringUtils.hasText(commentDto.getText())) {
            throw new ValidationException();
        }
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException();
        }
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException();
        }

        List<BookingDto> bookingList = bookingRepository.findBookingDtoByBookerIdAndItemId(userId, itemId);
        if (bookingList.isEmpty()) {
            throw new ValidationException();
        }
        if (bookingList.get(0).getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException();
        }

        commentDto.setItemId(itemId);
        commentDto.setAuthorId(userId);
        commentDto.setCreated(LocalDateTime.now());
        return mapper(commentRepository.save(commentDto));
    }

    private Comment mapper(CommentDto commentDto) {
        Comment comment = new Comment();

        comment.setText(commentDto.getText());
        comment.setId(commentDto.getId());
        comment.setCreated(commentDto.getCreated());
        comment.setAuthorName(userRepository.getUserDtoById(commentDto.getAuthorId()).get().getName());

        return comment;
    }
}
