package com.quintor.api.toXml;

import com.quintor.api.dataobjects.Transaction;
import com.quintor.api.dataobjects.TransactionList;
import com.thoughtworks.xstream.XStream;

import java.util.List;

public class ToXML {

    public static String transactionsToXML(List<Transaction> allTransactions) {
        XStream xStream = new XStream();
        xStream.alias("transaction", Transaction.class);
        xStream.alias("transactions", TransactionList.class);
        xStream.addImplicitCollection(TransactionList.class, "list");
        String xml = xStream.toXML(allTransactions);
        System.out.println(xml);
        return xml;
    }
}
