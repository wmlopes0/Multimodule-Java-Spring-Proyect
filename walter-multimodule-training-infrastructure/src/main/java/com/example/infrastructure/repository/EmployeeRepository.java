package com.example.infrastructure.repository;

import java.util.Optional;

import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeEntity, Long> {

  Optional<EmployeeEntity> findFirstByNameContainingIgnoreCase(String namePart);

  Optional<EmployeeEntity> findFirstByOrderByNumberDesc();

  default Long findMaxId() {
    return findFirstByOrderByNumberDesc().map(EmployeeEntity::getNumber).orElse(null);
  }
}
