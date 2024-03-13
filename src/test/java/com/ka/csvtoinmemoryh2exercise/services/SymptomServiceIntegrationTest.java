package com.ka.csvtoinmemoryh2exercise.services;

import com.ka.csvtoinmemoryh2exercise.exceptions.InvalidFileTypeException;
import com.ka.csvtoinmemoryh2exercise.exceptions.ResourceNotFoundException;
import com.ka.csvtoinmemoryh2exercise.integrators.SymptomRepositoryIntegrator;
import com.ka.csvtoinmemoryh2exercise.repository.SymptomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@SqlGroup({
        @Sql(value = "classpath:empty/reset.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:init/data.sql", executionPhase = BEFORE_TEST_METHOD)
})
public class SymptomServiceIntegrationTest {

    private SymptomService symptomService;

    @Autowired
    private SymptomRepository symptomRepository;

    private MultipartFile multipartFile;
    private File file;

    @BeforeEach
    public void setup() throws IOException {
        symptomService = new SymptomService(new SymptomRepositoryIntegrator(symptomRepository));

        var resourceLoader = new DefaultResourceLoader();
        var csv = resourceLoader.getResource("classpath:exercise.csv");
        file = csv.getFile();
        multipartFile = new MockMultipartFile("exercise.csv",
                "exercise.csv","text/csv", new FileInputStream(file));
    }

    //upload csv tests

    @Test
    public void should_upload_csv_file() throws Exception {
        var resourceLoader = new DefaultResourceLoader();
        var csv = resourceLoader.getResource("classpath:exercise.csv");
        File file = csv.getFile();
        MultipartFile multipartFile = new MockMultipartFile("exercise.csv",
                "exercise.csv","text/csv", new FileInputStream(file));

        symptomRepository.deleteAll();
        symptomService.uploadCsvData(multipartFile);

        //check local csv file is uploaded successfully to test h2 instance
        assertThat(symptomRepository.findAll()).isNotEmpty();
        assertThat(symptomRepository.findById("271636001").get().getDisplayValue()).isEqualTo("Polsslag regelmatig");

    }

    @Test
    public void should_throw_exception_to_wrong_file_type() throws IOException {
        multipartFile = new MockMultipartFile("exercise.csv",
                "exercise.csv","aplication/json", new FileInputStream(file));

        assertThrows(InvalidFileTypeException.class, () -> symptomService.uploadCsvData(multipartFile));
    }

    //find by code tests

    @Test
    public void should_find_by_code() throws ResourceNotFoundException {
        var response = symptomService.findByCode("1234");
        assertThat(response).isEqualTo(symptomRepository.getReferenceById("1234"));
    }

    @Test
    public void should_throw_exception_if_not_found() {
        assertThrows(ResourceNotFoundException.class, () -> symptomService.findByCode("blah"));
    }

    //delete all symptoms tests

    @Test
    public void should_delete_all_symptoms(){
        assertThat(symptomRepository.findAll().size()).isEqualTo(3);
        symptomService.deleteAllSymptoms();
        assertThat(symptomRepository.findAll().size()).isEqualTo(0);
    }

    //find all symptoms tests

    @Test
    public void should_find_all_symptoms(){
        var response = symptomService.findAllSymptoms();
        assertThat(response.size()).isEqualTo(symptomRepository.findAll().size());

        symptomRepository.deleteAll();

        response = symptomService.findAllSymptoms();
        assertThat(response.size()).isEqualTo(symptomRepository.findAll().size());
    }

}
