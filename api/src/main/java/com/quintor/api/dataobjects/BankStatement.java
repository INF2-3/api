package com.quintor.api.dataobjects;

import java.time.LocalDate;

public class BankStatement {
    private int id;
    private String transactionReferenceNumber;
    private String accountNumber;
    private String statementNumber;
    private FileDescription fileDescription;
    private int lastUpdatedUser;
    private LocalDate uploadDate;
    private String fileCol;

}
