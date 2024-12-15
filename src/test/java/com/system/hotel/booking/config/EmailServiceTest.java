package com.system.hotel.booking.config;

import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@Profile("test")
@Configuration
public class EmailServiceTest {
    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        // Création du mock pour JavaMailSender
        JavaMailSender javaMailSender = Mockito.mock(JavaMailSender.class);

        // Mock du comportement de createMimeMessage
        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));

        // Mock du comportement de send (optionnel si vous voulez vérifier l'appel)
        Mockito.doNothing().when(javaMailSender).send(ArgumentMatchers.any(MimeMessage.class));

        return javaMailSender;
    }
}