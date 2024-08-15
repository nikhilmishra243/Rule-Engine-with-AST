package com.zeotap.assignment1.repository;

import com.zeotap.assignment1.entity.Rule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RuleRepository extends JpaRepository<Rule, Long> {
}
