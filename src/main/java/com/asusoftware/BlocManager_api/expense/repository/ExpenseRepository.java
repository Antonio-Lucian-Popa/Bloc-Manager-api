package com.asusoftware.BlocManager_api.expense.repository;

import com.asusoftware.BlocManager_api.expense.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, UUID> {
    List<Expense> findByBlockId(UUID blockId);
    List<Expense> findByBlockIdAndDueDate(UUID blockId, LocalDate dueDate);
    List<Expense> findAllByBlockId(UUID blockId);

    @Query("SELECT e FROM Expense e WHERE e.blockId IN :blockIds")
    List<Expense> findAllByBlockIds(@Param("blockIds") List<UUID> blockIds);

    @Query("""
    SELECT e FROM Expense e
    WHERE e.blockId IN :blockIds
    AND (:search IS NULL OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%')))
""")
    Page<Expense> findByBlockIdsWithSearch(
            @Param("blockIds") List<UUID> blockIds,
            @Param("search") String search,
            Pageable pageable
    );
}
