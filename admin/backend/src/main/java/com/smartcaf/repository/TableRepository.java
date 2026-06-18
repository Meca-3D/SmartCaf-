package com.smartcaf.repository;

import com.smartcaf.model.CafeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<CafeTable, Long> {
    Optional<CafeTable> findByTableNumber(Integer tableNumber);
}
