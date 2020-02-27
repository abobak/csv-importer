package com.csv.task.backend.repository;

import com.csv.task.backend.model.LoyaltyPointsEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LoyaltyPointsEntryRepository extends JpaRepository<LoyaltyPointsEntry, UUID> {
}
