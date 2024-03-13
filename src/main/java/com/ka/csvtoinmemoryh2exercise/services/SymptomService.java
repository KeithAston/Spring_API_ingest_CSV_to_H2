package com.ka.csvtoinmemoryh2exercise.services;

import com.ka.csvtoinmemoryh2exercise.exceptions.InvalidFileTypeException;
import com.ka.csvtoinmemoryh2exercise.exceptions.ResourceNotFoundException;
import com.ka.csvtoinmemoryh2exercise.integrators.SymptomRepositoryIntegrator;
import com.ka.csvtoinmemoryh2exercise.models.Symptom;
import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.csv.CSVParser;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@CommonsLog
public class SymptomService {

    @Autowired
    private SymptomRepositoryIntegrator symptomRepositoryIntegrator;

    public String uploadCsvData(MultipartFile file) throws Exception {
        checkFormat(file);
        List<Symptom> csvDataList = convertFileToCsvObjects(file.getInputStream());

        return symptomRepositoryIntegrator.saveSymptoms(csvDataList);
    }

    public List<Symptom> findAllSymptoms(){
        return symptomRepositoryIntegrator.findAllSymptoms();
    }

    public String deleteAllSymptoms(){
        symptomRepositoryIntegrator.deleteAllSymptoms();
        return "Successfully deleted all records";
    }

    public Symptom findByCode(String code) throws ResourceNotFoundException {
        Optional<Symptom> symptom = symptomRepositoryIntegrator.fetchByCode(code);
        if(symptom.isPresent()){
            return symptom.get();
        } else {
            throw new ResourceNotFoundException("Symptom with code " + code + " was not found");
        }
    }

    private List<Symptom> convertFileToCsvObjects(InputStream is) throws IOException, ParseException {
        BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        CSVParser csvParser = new CSVParser(fileReader,
                CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());

        List<Symptom> csvDataList = new ArrayList<Symptom>();
        Iterable<CSVRecord> csvRecords = csvParser.getRecords();
        SimpleDateFormat DateFor = new SimpleDateFormat("dd-MM-yyyy");

        for (CSVRecord csvRecord : csvRecords) {
            Symptom symptom = new Symptom();
            symptom.setCode(csvRecord.get("code"));
            symptom.setCodeListCode(csvRecord.get("codeListCode"));
            symptom.setSource(csvRecord.get("source"));
            symptom.setDisplayValue(csvRecord.get("displayValue"));
            symptom.setLongDescription(csvRecord.get("longDescription"));

            if (!csvRecord.get("toDate").isEmpty()) {
                Date fromDate = DateFor.parse(csvRecord.get("fromDate"));
                symptom.setFromDate(fromDate);
            }
            if (!csvRecord.get("toDate").isEmpty()){
                Date toDate = DateFor.parse(csvRecord.get("toDate"));
                symptom.setToDate(toDate);
            }
            if (!csvRecord.get("sortingPriority").isEmpty()){
                symptom.setSortingPriority(Integer.parseInt(csvRecord.get("sortingPriority")));
            }

            csvDataList.add(symptom);
        }
        return csvDataList;

    }
    private void checkFormat(MultipartFile file) throws Exception {
        if(!file.getContentType().equals("text/csv")){
            throw new InvalidFileTypeException("Illegal file type");
        }
    }

}
