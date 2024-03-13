package com.ka.csvtoinmemoryh2exercise.services;

import com.ka.csvtoinmemoryh2exercise.exceptions.InvalidFileTypeException;
import com.ka.csvtoinmemoryh2exercise.integrators.SymptomRepositoryIntegrator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class SymptomServiceTest {

    @InjectMocks
    private SymptomService symptomService;

    @Mock
    private SymptomRepositoryIntegrator repositoryIntegrator;

    private MultipartFile multipartFile;
    private File file;

    @BeforeEach
    public void setup() throws IOException {
        var resourceLoader = new DefaultResourceLoader();
        var csv = resourceLoader.getResource("classpath:exercise.csv");
        file = csv.getFile();
        multipartFile = new MockMultipartFile("exercise.csv",
                "exercise.csv","text/csv", new FileInputStream(file));
    }

    // upload csv tests
    @Test
    public void should_send_correct_param_to_integrator() throws Exception {
        when(repositoryIntegrator.saveSymptoms(any())).thenReturn("Successfully saved");
        assertThat(symptomService.uploadCsvData(multipartFile)).isEqualTo("Successfully saved");
    }

    @Test
    public void should_throw_exception_if_filetype_wrong() throws IOException {
        multipartFile = new MockMultipartFile("exercise.csv",
                "exercise.csv","aplication/json", new FileInputStream(file));
        assertThrows(InvalidFileTypeException.class, () -> symptomService.uploadCsvData(multipartFile));
    }



}
