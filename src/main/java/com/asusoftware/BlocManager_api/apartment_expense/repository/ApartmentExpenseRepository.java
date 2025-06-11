package com.asusoftware.BlocManager_api.apartment_expense.repository;

import com.asusoftware.BlocManager_api.apartment_expense.model.ApartmentExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApartmentExpenseRepository extends JpaRepository<ApartmentExpense, UUID> {
    List<ApartmentExpense> findByApartmentId(UUID apartmentId);
    List<ApartmentExpense> findByExpenseId(UUID expenseId);
}
