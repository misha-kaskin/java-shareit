package ru.practicum.shareit.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @Test
    void createBookingTest() throws Exception {
        Booking booking = new Booking();
        booking.setStatus("WAITING");
        booking.setId(1L);
        booking.setItemId(1L);
        booking.setBookerId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1L));

        Mockito.when(bookingService.create(any(Booking.class), anyLong())).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking.getStatus())))
                .andExpect(jsonPath("$.itemId", is(booking.getItemId()), Long.class));
    }

    @Test
    void createBookingTestShouldReturn400() throws Exception {
        Booking booking = new Booking();
        booking.setStatus("WAITING");
        booking.setId(1L);
        booking.setItemId(1L);
        booking.setBookerId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1L));

        Mockito
                .when(bookingService.create(any(), anyLong()))
                .thenThrow(ValidationException.class);

        mvc.perform(post("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void createBookingTestShouldReturn404() throws Exception {
        Booking booking = new Booking();
        booking.setStatus("WAITING");
        booking.setId(1L);
        booking.setItemId(1L);
        booking.setBookerId(1L);
        booking.setStart(LocalDateTime.now());
        booking.setEnd(LocalDateTime.now().plusDays(1L));

        Mockito
                .when(bookingService.create(any(Booking.class), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        Mockito.when(bookingService.getBookingById(anyLong(), anyLong())).thenReturn(bookingDto);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())));
    }

    @Test
    void getBookingByIdTestShouldReturnNotFoundException() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        Mockito.when(bookingService.getBookingById(anyLong(), anyLong())).thenThrow(NotFoundException.class);

        mvc.perform(get("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void getBookingsTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(bookingDtoList);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())));
    }

    @Test
    void getBookingsTestShouldReturn400() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenThrow(ValidationException.class);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void getBookingsTestShouldReturn404() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/bookings")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void getOwnerBookingsTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getOwnerBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenReturn(bookingDtoList);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingDto.getStatus())));
    }

    @Test
    void getOwnerBookingsTestShouldReturn400() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getOwnerBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenThrow(ValidationException.class);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "null")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void getOwnerBookingsTestShouldReturn404() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        List<BookingDto> bookingDtoList = List.of(bookingDto);

        Mockito
                .when(bookingService.getOwnerBookings(anyString(), anyLong(), anyInt(), anyInt()))
                .thenThrow(NotFoundException.class);

        mvc.perform(get("/bookings/owner")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }

    @Test
    void approveTest() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        Mockito.when(bookingService.approve(anyString(), anyLong(), anyLong())).thenReturn(bookingDto);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "TRUE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus())));
    }

    @Test
    void approveTestShouldReturn400() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        Mockito
                .when(bookingService.approve(anyString(), anyLong(), anyLong()))
                .thenThrow(ValidationException.class);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "TRUE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void approveTestShouldReturn404() throws Exception {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(1L);
        bookingDto.setStart(LocalDateTime.now());
        bookingDto.setEnd(LocalDateTime.now().plusDays(1L));
        bookingDto.setStatus("WAITING");
        bookingDto.setItem(new ItemDto());
        bookingDto.setBooker(new UserDto());

        Mockito
                .when(bookingService.approve(anyString(), anyLong(), anyLong()))
                .thenThrow(NotFoundException.class);

        mvc.perform(patch("/bookings/1")
                        .content(mapper.writeValueAsString(bookingDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "TRUE")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404));
    }
}
