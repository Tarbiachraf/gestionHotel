package com.system.hotel.booking.common;

import com.sun.istack.NotNull;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class ReservationRequest {
    @NotNull
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères.")
    private String nom;

    @NotNull
    @Future(message = "la date doit être dans le future")
    private LocalDate dateReservation;


}