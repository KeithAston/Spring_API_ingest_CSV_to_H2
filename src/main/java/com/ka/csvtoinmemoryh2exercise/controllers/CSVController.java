package com.ka.csvtoinmemoryh2exercise.controllers;

import com.ka.csvtoinmemoryh2exercise.exceptions.ResourceNotFoundException;
import com.ka.csvtoinmemoryh2exercise.models.Symptom;
import com.ka.csvtoinmemoryh2exercise.services.SymptomService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class CSVController {

    private final SymptomService symptomService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvData(@RequestBody MultipartFile file) throws Exception {
        return new ResponseEntity<>(symptomService.uploadCsvData(file), HttpStatus.OK);
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<Symptom>> findAllSymptoms(){
        return new ResponseEntity<>(symptomService.findAllSymptoms(), HttpStatus.OK);
    }

    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> deleteAllSymptoms(){
        return new ResponseEntity<>(symptomService.deleteAllSymptoms(), HttpStatus.OK);
    }

    @GetMapping("find/{code}")
    public ResponseEntity<Symptom> findSymptomByCode(@PathVariable String code) throws ResourceNotFoundException {
        return new ResponseEntity<>(symptomService.findByCode(code), HttpStatus.OK);
    }

}
