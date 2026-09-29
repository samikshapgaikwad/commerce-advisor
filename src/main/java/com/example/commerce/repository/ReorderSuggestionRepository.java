package com.example.commerce.repository;

import com.example.commerce.model.ReorderSuggestion;
import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReorderSuggestionRepository extends JpaRepository<ReorderSuggestion, Long> {
    List<ReorderSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    List<ReorderSuggestion> findByProductAndStatusOrderByCreatedAtDesc(Product product, SuggestionStatus status);
    Optional<ReorderSuggestion> findFirstByProductAndTriggerAndStatusOrderByCreatedAtDesc(
            Product product, BusinessTrigger trigger, SuggestionStatus status);
}