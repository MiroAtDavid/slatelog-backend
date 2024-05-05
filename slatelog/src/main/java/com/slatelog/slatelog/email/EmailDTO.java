package com.slatelog.slatelog.email;

// Define a record called EmailDTO to represent an email data transfer object (DTO)
public record EmailDTO (String recipient, String subject, String body){ }
