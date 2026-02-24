package com.pismo.transactionroutine.models;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "operation_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "operation_type_id")
    private Long operationTypeId;

    @Column(name = "operation_type", unique = true)
    private String operationType;
}