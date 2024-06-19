package com.slatelog.slatelog.persistance;


import com.slatelog.slatelog.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-lookup-strategies

// Annotations used?
// --------------------------------------------------------------------------------------------
// @Repository to tell Spring that this is a repository

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    User findUserById(String userId);
    boolean existsByEmail(String email);
    //    User findByEmail(String email);
}