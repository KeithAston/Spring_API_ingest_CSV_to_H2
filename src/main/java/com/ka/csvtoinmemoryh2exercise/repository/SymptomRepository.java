package com.ka.csvtoinmemoryh2exercise.repository;

import com.ka.csvtoinmemoryh2exercise.models.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<Symptom, String> {
}
