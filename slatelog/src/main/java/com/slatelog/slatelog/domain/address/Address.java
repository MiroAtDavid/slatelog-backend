package com.slatelog.slatelog.domain.address;



import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;


// Defining a record for representing an address
public record Address (

        String street, // Street component of the address
        String city, // City component of the address
        String state, // State component of the address
        String zipCode // Zip code component of the address
) {

    // Constructor for Address record
    public Address {
        // Checking if the length of street does not exceed 255 characters
        hasMaxText(street, 255, "Street");
        // Checking if the length of city does not exceed 255 characters
        hasMaxText(city, 255, "City");
        // Checking if the length of state does not exceed 255 characters
        hasMaxText(state, 255, "State");
        // Checking if the length of zip code does not exceed 255 characters
        hasMaxText(zipCode, 255, "Zip Code");
    }
}
