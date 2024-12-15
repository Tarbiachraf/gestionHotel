package com.system.hotel.booking.serviceimplementation;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.system.hotel.booking.entity.Booking;
import com.system.hotel.booking.entity.HotelRoomType;
import com.system.hotel.booking.entity.Payment;
import com.system.hotel.booking.entity.Room;
import com.system.hotel.booking.repository.BookingRepository;
import com.system.hotel.booking.repository.HotelRoomTypeRepository;
import com.system.hotel.booking.repository.PaymentRepository;
import com.system.hotel.booking.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

class PaymentServiceImplementationTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private HotelRoomTypeRepository hotelRoomTypeRepository;

    @InjectMocks
    private PaymentServiceImplementation paymentService;

    private Payment payment;
    private Booking booking;
    private Room room;
    private HotelRoomType hotelRoomType;
    private Long bookingId = 1L;
    private Long roomId = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialisation des objets nécessaires
        payment = new Payment();
        booking = new Booking();
        booking.setId(bookingId);
        booking.setPaymentStatus(false);  // Simule un état avant paiement

        room = new Room();
        room.setId(roomId);
        hotelRoomType = new HotelRoomType();
        hotelRoomType.setId(1L);
        hotelRoomType.setBookedCount(0);
        room.setHotelRoomType(hotelRoomType);

        payment.setBooking(booking);
    }

    @Test
    void testCreatePayment_ShouldCreatePaymentAndUpdateBookingAndRoomType() {
        // Arrangements
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(hotelRoomTypeRepository.findById(hotelRoomType.getId())).
                thenReturn(Optional.of(hotelRoomType));

        // Act
        Payment createdPayment = paymentService.createPayment(payment
                , bookingId, List.of(roomId));

        // Assert
        assertNotNull(createdPayment);
        assertEquals(1, hotelRoomType.getBookedCount());
        // Le compteur de réservations dans le type de chambre devrait être incrémenté
        verify(roomRepository, times(1)).findById(roomId);
        verify(bookingRepository, times(1)).save(booking);
        verify(paymentRepository, times(1)).save(payment);
        verify(hotelRoomTypeRepository, times(1)).save(hotelRoomType);
    }

    @Test
    void testCreatePayment_ShouldThrowException_WhenRoomNotFound() {
        // Arrangements
        when(roomRepository.findById(roomId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            paymentService.createPayment(payment, bookingId, List.of(roomId));
        });

        verify(roomRepository, times(1)).findById(roomId);
        verify(bookingRepository, never()).save(any());
        verify(paymentRepository, never()).save(any());
        verify(hotelRoomTypeRepository, never()).save(any());
    }
    @Test
    void testCreatePayment_ShouldThrowException_WhenBookingNotFound() {
        // Arrangements
        Long roomId = 1L;
        Long bookingId = 1L;
        // Création d'un objet Room avec un HotelRoomType valide
        Room room = new Room();
        room.setId(roomId);
        HotelRoomType hotelRoomType = new HotelRoomType();
        hotelRoomType.setId(100L); // Id valide pour le type de chambre
        room.setHotelRoomType(hotelRoomType); // Assurez-vous que le hotelRoomType est initialisé
        // Mock des repos
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room)); // Mock du roomRepository
        when(hotelRoomTypeRepository.findById(100L)).thenReturn(Optional.of(hotelRoomType)); // Mock du hotelRoomTypeRepository avec un id valide
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty()); // La réservation n'existe pas
        Payment payment = new Payment();
        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            paymentService.createPayment(payment, bookingId, List.of(roomId));
        });// Vérifications
        verify(roomRepository, times(1)).findById(roomId);
        verify(hotelRoomTypeRepository, times(1)).findById(100L); // Vérifier que findById est appelé avec le bon id
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testCreatePayment_ShouldThrowException_WhenHotelRoomTypeNotFound() {
        // Arrangements
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room)); // Mock de roomRepository
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking)); // Mock de bookingRepository
        when(hotelRoomTypeRepository.findById(hotelRoomType.getId())).
                thenReturn(Optional.empty()); // Mock de hotelRoomTypeRepository (vide)

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            paymentService.createPayment(payment, bookingId, List.of(roomId));
        });

        // Vérifications
        verify(roomRepository, times(1)).findById(roomId);
        verify(bookingRepository, times(0)).findById(bookingId);  // On vérifie que l'appel n'est pas fait
        verify(paymentRepository, never()).save(any());
        verify(hotelRoomTypeRepository, times(1)).
                findById(hotelRoomType.getId());
        // Vérifier que le repository de hotelRoomType a bien été invoqué
    }


}
