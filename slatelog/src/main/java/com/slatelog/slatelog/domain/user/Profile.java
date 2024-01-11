package com.slatelog.slatelog.domain.user;

import com.slatelog.slatelog.domain.address.Address;
import jakarta.annotation.Nullable;
import lombok.Getter;

import static com.slatelog.slatelog.foundation.AssertUtil.hasMaxText;

/**
 * Profile of a user.
 *
 * <p>Here we can place base profile information of a user.
 */

// This class in inlined in User.
@Getter
public class Profile {
    private String firstName;
    private String lastName;
    private @Nullable Address address;

    // private Media avatar;

    public Profile(String firstName, String lastName /*Media avatar*/) {
        this.firstName = hasMaxText(firstName, 255, "firstName");
        this.lastName = hasMaxText(lastName, 255, "lastName");
        this.address =  address;

        // That means the user has to upload an avatar if not we can make it nullable.
        // this.avatar = isNotNull(avatar, "avatar");
    }
}