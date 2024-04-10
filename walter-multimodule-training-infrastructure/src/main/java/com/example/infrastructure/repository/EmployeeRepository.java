package com.example.infrastructure.repository;

import java.util.List;
import java.util.Optional;

import com.example.infrastructure.entity.EmployeeEntity;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends MongoRepository<EmployeeEntity, Long> {

  Optional<EmployeeEntity> findFirstByNameContainingIgnoreCase(String namePart);

  @Query(value = "{}", fields = "{number : 1}", sort = "{number : -1}")
  List<EmployeeEntity> findTopByOrderByNumberDesc(Pageable pageable);

  default Long findMaxId() {
    List<EmployeeEntity> entities = findTopByOrderByNumberDesc(PageRequest.of(0, 1));
    return entities.isEmpty() ? null : entities.get(0).getNumber();
  }
}
