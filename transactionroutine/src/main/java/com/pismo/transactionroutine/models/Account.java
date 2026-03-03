package com.pismo.transactionroutine.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNT")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(name = "ACCOUNT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(name = "DOCUMENT_ID")
    private String documentId;

    @Column(name= "CREDIT_LIMIT")
    private BigDecimal creditLimit;

}
