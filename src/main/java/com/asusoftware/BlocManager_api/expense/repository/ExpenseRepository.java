package com.asusoftware.BlocManager_api.expense.repository;

import com.asusoftware.BlocManager_api.expense.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByBlockId(UUID blockId);
    List<Expense> findByBlockIdAndMonth(UUID blockId, LocalDate month);
    List<Expense> findAllByBlockId(UUID blockId);
}
