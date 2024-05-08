package com.slatelog.slatelog.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class IcsCalendarService {
    public static String generateICSFile(String title, List<Instant> instantList,
                                       String description, String location, String organizer, String[] attendees,
                                       String filePath) {
        // Format date-time according to iCalendar format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");

        // Construct iCalendar data
        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n");
        icsContent.append("VERSION:2.0\n");

        // Iterate over each Instant in the list
        for (Instant startTime : instantList) {
            // Calculate end time by adding one hour
            Instant endTime = startTime.plus(Duration.ofHours(1));

            // Convert Instant to ZonedDateTime with UTC time zone
            ZonedDateTime startDateTime = ZonedDateTime.ofInstant(startTime, ZoneId.of("UTC"));
            ZonedDateTime endDateTime = ZonedDateTime.ofInstant(endTime, ZoneId.of("UTC"));

            // Format start and end times
            String formattedStart = startDateTime.format(formatter);
            String formattedEnd = endDateTime.format(formatter);

            // Append event details to the icsContent StringBuilder
            icsContent.append("BEGIN:VEVENT\n");
            icsContent.append("DTSTART:" + formattedStart + "\n");
            icsContent.append("DTEND:" + formattedEnd + "\n");
            icsContent.append("SUMMARY:" + title + "\n");
            icsContent.append("DESCRIPTION:" + description + "\n");
            icsContent.append("LOCATION:" + location + "\n");
            icsContent.append("ORGANIZER:" + organizer + "\n");
            for (String attendee : attendees) {
                icsContent.append("ATTENDEE:" + attendee + "\n");
            }
            icsContent.append("END:VEVENT\n");
        }

        icsContent.append("END:VCALENDAR\n");

        // Write iCalendar data to a file
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(icsContent.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return icsContent.toString();
    }

}



