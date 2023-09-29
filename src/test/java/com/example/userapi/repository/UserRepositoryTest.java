package com.example.userapi.repository;

import com.example.userapi.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @Sql(scripts = {"classpath:db/before/insert-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:db/after/delete-users-table-users.sql"},
         executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void whenGetAllUsersBySpecifiedBirthDateRange_ValidBirthDateRange_ReturnAllUsersInBirthDateRange() {
        LocalDate from = LocalDate.of(1990, 1, 1);
        LocalDate to = LocalDate.of(1996, 1, 1);
        List<User> userListBeforeFiltering = userRepository.findAll();
        List<User> expected = userListBeforeFiltering.stream()
                .filter(user -> user.getBirthDate().isAfter(from) && user.getBirthDate()
                        .isBefore(to)).toList();

        Page<User> userPage = userRepository.findAllByBirthDateRange(from, to, Pageable.unpaged());
        List<User> actual = userPage.stream().toList();
        Assertions.assertEquals(2, actual.size());
        Assertions.assertEquals(expected, actual);
    }

}