package com.example.repository;

import java.util.Optional;

import com.example.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

  Optional<EmployeeEntity> findFirstByNameContainingIgnoreCase(String namePart);
}
