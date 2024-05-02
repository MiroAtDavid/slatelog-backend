package com.slatelog.slatelog.presentation.commands;

public class Commands {

    // --- Registration ---
    //  whiten input = email.trim().toLowerCase();
    public record UserRegistrationCommand(
            String email, String password, String firstName, String lastName) {
        public String email() {
            return email.trim().toLowerCase();
        }
    }
    ;

    // --- Verification ---
    public record UserVerificationCommand(String userId, String tokenId) {}
    ;
    // Coop Edren Datenübertragung
    // title, address, deadlineDate, daedlineTime, description, emails, dateTimes
    public record CreateEventCommand (
            String title,
            String address,
            String deadlineDate,
            String deadlineTime,
            String description,
            String emails,
            String dateTimes
    ){};


    // Coop Edren Datenübertragung
    // event id
    public record UpdateEventCommand (
            String eventId,
            String title,
            String address,
            String deadlineDate,
            String deadlineTime,
            String description,
            String emails,
            String dateTimes
    ){};



}