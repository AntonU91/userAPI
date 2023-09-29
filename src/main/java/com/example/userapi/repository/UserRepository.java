package com.example.userapi.repository;

import com.example.userapi.model.User;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("FROM User u WHERE u.birthDate BETWEEN :from and :to")
    Page<User> findAllByBirthDateRange(
            @Param("from") LocalDate from, @Param("to") LocalDate to, Pageable pageable);
}
