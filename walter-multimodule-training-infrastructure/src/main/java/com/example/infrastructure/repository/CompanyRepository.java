package com.example.infrastructure.repository;

import com.example.infrastructure.entity.CompanyEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends MongoRepository<CompanyEntity, String> {

}
