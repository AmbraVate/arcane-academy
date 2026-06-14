package com.ambravate.arcane.academy.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface ProcessedStripeEventRepository extends JpaRepository<ProcessedStripeEvent, String> {
}
