package com.ambravate.arcane.academy.common.repository;

import com.ambravate.arcane.academy.common.domain.UserRabbitHoleTerm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRabbitHoleTermRepository extends JpaRepository<UserRabbitHoleTerm, String> {
    List<UserRabbitHoleTerm> findByUserIdOrderBySavedAtDesc(String userId);
    Optional<UserRabbitHoleTerm> findByUserIdAndTerm(String userId, String term);
    void deleteByUserIdAndTerm(String userId, String term);
}
