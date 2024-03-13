package com.ka.csvtoinmemoryh2exercise.integrators;

import com.ka.csvtoinmemoryh2exercise.models.Symptom;
import com.ka.csvtoinmemoryh2exercise.repository.SymptomRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class SymptomRepositoryIntegrator {

    @Autowired
    private SymptomRepository symptomRepository;

    @Transactional
    public String saveSymptoms(List<Symptom> symptomList) {
        symptomRepository.saveAll(symptomList);
        return "Success";
    }

    @Transactional
    public List<Symptom> findAllSymptoms(){
        return symptomRepository.findAll();
    }

    @Transactional
    public void deleteAllSymptoms(){
        symptomRepository.deleteAll();
    }

    @Transactional
    public Optional<Symptom> fetchByCode(String code){
        return symptomRepository.findById(code);
    }

}
