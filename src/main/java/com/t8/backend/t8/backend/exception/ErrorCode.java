package com.t8.backend.t8.backend.exception;

public enum ErrorCode {
    MEMBER_NOT_FOUND("Member not found"),
    RESTAURANT_NOT_FOUND("Restaurant not found"),
    RESERVATION_NOT_FOUND("Reservation not found");

    private final String message;
    ErrorCode(String message) { this.message = message; }
    public String getMessage() { return message; }
}