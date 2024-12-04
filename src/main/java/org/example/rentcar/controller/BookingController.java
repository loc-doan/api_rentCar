package org.example.rentcar.controller;

import lombok.RequiredArgsConstructor;
import org.example.rentcar.dto.BookingDto;
import org.example.rentcar.event.BookingApprovedEvent;
import org.example.rentcar.event.BookingCompleteEvent;
import org.example.rentcar.event.BookingDeclineEvent;
import org.example.rentcar.model.Booking;
import org.example.rentcar.request.BookingRequest;
import org.example.rentcar.request.BookingUpdateRequest;
import org.example.rentcar.response.APIResponse;
import org.example.rentcar.service.booking.BookingService;
import org.example.rentcar.utils.FeedBackMessage;
import org.example.rentcar.utils.UrlMapping;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(UrlMapping.BOOKING)
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final ApplicationEventPublisher applicationEventPublisher;
    @PostMapping(UrlMapping.ADD)
    public ResponseEntity<APIResponse> bookCar(@RequestBody BookingRequest bookingRequest,
                                               @RequestParam long carId,
                                               @RequestParam long customerId) {
        Booking book = bookingService.bookCar(bookingRequest, carId, customerId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, book.getId()));
    }

    @GetMapping(UrlMapping.GET_ALL)
    public ResponseEntity<APIResponse> getBookings() {
        List<Booking> bookingList = bookingService.getAllBookings();
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, bookingList));
    }

    @GetMapping(UrlMapping.GET_BY_ID)
    public ResponseEntity<APIResponse> getBooking(@PathVariable long id) {
        BookingDto booking = bookingService.getBookingDtoById(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking));
    }

    @PutMapping(UrlMapping.UPDATE_BY_ID)
    public ResponseEntity<APIResponse> updateBooking(@PathVariable long id,
                                                     @RequestBody BookingUpdateRequest bookingUpdateRequest) {
        Booking booking = bookingService.updateBooking(bookingUpdateRequest,id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking.getId()));
    }

    @DeleteMapping(UrlMapping.DELETE_BY_ID)
    public ResponseEntity<APIResponse> deleteBooking(@PathVariable long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.DELETE_SUCCESS,null));
    }

    @PutMapping(UrlMapping.BOOKING_APPROVED)
    public ResponseEntity<APIResponse> approvedBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.approveBooking(bookingId);
        applicationEventPublisher.publishEvent(new BookingApprovedEvent(booking));
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking.getId()));
    }

    @PutMapping(UrlMapping.BOOKING_REJECTED)
    public ResponseEntity<APIResponse> declineBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.declineBooking(bookingId);
        applicationEventPublisher.publishEvent(new BookingDeclineEvent(booking));
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking.getId()));
    }

    @PutMapping(UrlMapping.BOOKING_COMPLETED)
    public ResponseEntity<APIResponse> completeBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.completeBooking(bookingId);
        applicationEventPublisher.publishEvent(new BookingCompleteEvent(booking));
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking.getId()));
    }

    @PutMapping(UrlMapping.BOOKING_CANCELED)
    public ResponseEntity<APIResponse> cancelBooking(@PathVariable long bookingId) {
        Booking booking = bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok(new APIResponse(FeedBackMessage.SUCCESS, booking.getId()));
    }
}
