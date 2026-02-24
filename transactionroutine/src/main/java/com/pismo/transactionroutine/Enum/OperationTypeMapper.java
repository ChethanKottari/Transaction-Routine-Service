package com.pismo.transactionroutine.Enum;

public enum OperationTypeMapper {

    PURCHASE(1),
    WITHDRAWAL(2),
    INSTALLMENT(3),
    CREDIT_VOUCHER(4);

    private final int id;

    OperationTypeMapper(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static OperationTypeMapper fromString(String type) {
        try {
            return OperationTypeMapper.valueOf(type.toUpperCase());
        } catch (Exception ex) {
            throw new RuntimeException("Invalid operation type");
        }
    }
}