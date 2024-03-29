package com.quintor.api.toXml;

import com.quintor.api.dataobjects.BankStatement;
import com.quintor.api.dataobjects.BankStatementList;
import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.dataobjects.TransactionList;
import com.thoughtworks.xstream.XStream;

import java.util.List;

public class ToXML {
    /**
     * Puts the list of transaction in an XML string.
     *
     * @param allTransactions the list of transaction you want to change to XML
     * @return a String of XML
     */

    public static String transactionsToXML(List<Transaction> allTransactions) {
        XStream xStream = new XStream();
        xStream.alias("transaction", Transaction.class);
        xStream.alias("transactions", TransactionList.class);
        xStream.addImplicitCollection(TransactionList.class, "list");
        return xStream.toXML(allTransactions);
    }

    /**
     * Puts the list of BankStatements in an XML String.
     *
     * @param allBankStatements the list of BankStatements you want to change to XML.
     * @return a String of XML.
     */
    public static String bankStatementsToXML(List<BankStatement> allBankStatements) {
        XStream xStream = new XStream();
        xStream.alias("bankStatement", BankStatement.class);
        xStream.alias("bankStatements", BankStatementList.class);
        xStream.addImplicitCollection(BankStatementList.class, "list");
        return xStream.toXML(allBankStatements);
    }
}
