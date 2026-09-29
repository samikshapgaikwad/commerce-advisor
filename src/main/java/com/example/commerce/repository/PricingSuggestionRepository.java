package com.example.commerce.repository;

import com.example.commerce.model.PricingSuggestion;
import com.example.commerce.model.Product;
import com.example.commerce.model.enums.BusinessTrigger;
import com.example.commerce.model.enums.SuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PricingSuggestionRepository extends JpaRepository<PricingSuggestion, Long> {
    List<PricingSuggestion> findByStatusOrderByCreatedAtDesc(SuggestionStatus status);
    List<PricingSuggestion> findByProductAndStatusOrderByCreatedAtDesc(Product product, SuggestionStatus status);
    Optional<PricingSuggestion> findFirstByProductAndTriggerAndStatusOrderByCreatedAtDesc(
            Product product, BusinessTrigger trigger, SuggestionStatus status);
}