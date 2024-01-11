package com.slatelog.slatelog.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.slatelog.slatelog.domain.BaseEntity;
import com.slatelog.slatelog.security.PasswordService.EncodedPassword;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import static com.slatelog.slatelog.foundation.AssertUtil.isValidEmail;
import static com.slatelog.slatelog.foundation.EntityUtil.generateUUIDv4;

@Getter
@Setter
@Document(collection = "user")
public class User extends BaseEntity<String> {

    @Indexed(unique = true)
    private String email;
    private String password;
    private Role role;
    private Profile profile;
    private Social social;
    private Account account;


    @PersistenceCreator
    @JsonCreator
    protected User(String id) {
        super(id);
    }

    public User(String email, EncodedPassword password, Role role, Profile profile) {
        super(generateUUIDv4());

        this.email = isValidEmail(email, "email");
        this.password = password.getEncodedPassword();
        this.role = role;
        this.profile = profile;
        this.social = new Social();
        this.account = new Account();
    }

}
