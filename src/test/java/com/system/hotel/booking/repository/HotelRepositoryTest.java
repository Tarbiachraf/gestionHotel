package com.system.hotel.booking.repository;

import com.system.hotel.booking.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class HotelRepositoryTest {

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void setUp() {
        // Ajouter des hôtels pour les tests
        hotelRepository.save(new Hotel(null, "Hotel A", "emailA@example.com", "1234567890", "France", "Paris", "Address A", "imageA.jpg", null, null));
        hotelRepository.save(new Hotel(null, "Hotel B", "emailB@example.com", "1234567890", "USA", "New York", "Address B", "imageB.jpg", null, null));
        hotelRepository.save(new Hotel(null, "Hotel C", "emailC@example.com", "1234567890", "Italy", "Rome", "Address C", "imageC.jpg", null, null));
    }

    @Test
    void testFindHotelsByPage() {
        // Créer un Pageable pour récupérer les deux premiers hôtels
        Pageable pageable = PageRequest.of(0, 2); // Page 0, taille 2

        // Appeler la méthode
        List<Hotel> hotels = hotelRepository.findHotelsByPage(pageable);

        // Vérifier les résultats
        assertNotNull(hotels);
        assertEquals(2, hotels.size());

        // Vérifier l'ordre des hôtels
        assertEquals("Hotel C", hotels.get(0).getName()); // Hôtel avec ID le plus élevé
        assertEquals("Hotel B", hotels.get(1).getName());
    }
}