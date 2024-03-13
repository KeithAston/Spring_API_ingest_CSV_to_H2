package com.ka.csvtoinmemoryh2exercise.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name="DATA")
@Data
@NoArgsConstructor
public class Symptom {

    @Column(name = "SOURCE")
    private String source;

    @Column(name = "CODELISTCODE")
    private String codeListCode;

    @Id
    @Column(name = "CODE", nullable = false)
    private String code;

    @Column(name = "DISPLAYVALUE")
    private String displayValue;

    @Column(name = "LONGDESCRIPTION")
    private String longDescription;

    @Column(name = "FROMDATE")
    private Date fromDate;

    @Column(name = "TODATE")
    private Date toDate;

    @Column(name = "SORTINGPRIORITY")
    private int sortingPriority;


}
