package com.asusoftware.BlocManager_api.meter_reading.repository;

import com.asusoftware.BlocManager_api.meter_reading.model.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeterReadingRepository extends JpaRepository<MeterReading, UUID> {
    List<MeterReading> findByApartmentId(UUID apartmentId);
    List<MeterReading> findAllByApartmentId(UUID apartmentId);

}
