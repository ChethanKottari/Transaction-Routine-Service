package com.pismo.transactionroutine.models;

import jakarta.persistence.*;
import lombok.*;

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

}
