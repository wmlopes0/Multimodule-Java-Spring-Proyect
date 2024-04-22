package com.example.infrastructure.repository;

import java.util.Optional;

import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeEntity, String> {

  Optional<EmployeeEntity> findFirstByNameContainingIgnoreCase(String namePart);
}
