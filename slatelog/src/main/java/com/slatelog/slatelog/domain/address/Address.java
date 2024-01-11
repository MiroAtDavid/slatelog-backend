package com.slatelog.slatelog.domain.address;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;

public record Address (

    String street,
    String city,
    String state,
    String zipCode ) {

    public Address {
        hasMaxText(street,255,"Street");
        hasMaxText(city,255,"City");
        hasMaxText(state,255,"State");
        hasMaxText(zipCode,255,"Zip Code");
    }
}
