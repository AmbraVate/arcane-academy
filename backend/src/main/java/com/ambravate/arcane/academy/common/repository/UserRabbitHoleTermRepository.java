package com.ambravate.polymath.academy.repository;

import com.ambravate.polymath.academy.model.UserRabbitHoleTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRabbitHoleTermRepository extends JpaRepository<UserRabbitHoleTerm, String> {
    List<UserRabbitHoleTerm> findByUserIdOrderBySavedAtDesc(String userId);
    Optional<UserRabbitHoleTerm> findByUserIdAndTerm(String userId, String term);
    void deleteByUserIdAndTerm(String userId, String term);
}
