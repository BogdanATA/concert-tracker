package com.pluralsight.concerttracker.service;

public class ConcertNotFoundException extends RuntimeException {
    public ConcertNotFoundException(String message) {
        super(message);
    }
}
