package com.quintor.api.postgreSqlConnection;

import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.SchemaValidator;
import com.quintor.api.validators.XMLSchemaValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@RequestMapping("api/postgres")
public class PostgreSqlController {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "root";
    private final String password = "changeme";
    /*
     * Function inserts MT940 JSON from parser into POSTGRESQL
     * It calls the function insertInPostgres(json) if json is not null
     * */
    @PostMapping("/insert")
    public String insert(@RequestParam("file") File file, @RequestParam("userId") int userId, String mode) throws IOException, ParserConfigurationException, SAXException {

        if (file == null) {
            return "no_file";
        }
        if (userId <= 0) {
            return "wrong_user_id";
        }
        if(Objects.equals(mode, "JSON")){
            String jsonString = parserJSON(file);
            JSONObject json = new JSONObject(jsonString);


            try (Connection connection = DriverManager.getConnection(url, user, password)){ //Establishing a Connection
                //Insert into db
                insertInPostgresJSON(json);
            }catch (SQLException e) {
                // print SQL exception information
                return e.toString();
            }
        }

        if(Objects.equals(mode, "XML")){
            String xmlString = parserXML(file);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xml = documentBuilder.parse(new InputSource(new StringReader(xmlString)));


            try (Connection connection = DriverManager.getConnection(url, user, password)){ //Establishing a Connection
                //Insert into db
                insertInPostgresXML(xml);
            }catch (SQLException e) {
                // print SQL exception information
                return e.toString();
            }
        }
        return "done";
    }


    /*
    * sets up connection with parser
    * and returns String gotten from parser wht getResponse()
    */
    private String parserJSON(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
        SchemaValidator schemaValidator = new JSONSchemaValidator();
        String result = schemaValidator.validateFormat(multipartFile, "bankStatementSchema");
        return result;
    }

    /*
     * sets up connection with parser
     * and returns String gotten from parser wht getResponse()
     */
    private String parserXML(File file) throws IOException {
        InputStream stream = new FileInputStream(file);
        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
        SchemaValidator schemaValidator = new XMLSchemaValidator();
        String result = schemaValidator.validateFormat(multipartFile, "bankStatementSchema");
        return result;
    }


    /*
     * gets tags from the json and inserts it into postgresql
     * with stored procedures. does table after table
     */
    private void insertInPostgresJSON(JSONObject json) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        //get tag for the table
        JSONObject tags = (JSONObject) json.get("tags");

        insertIntoFileDescriptionJSON(connection, tags);
        insertIntoFileJSON(connection, tags);
        insertIntoBalanceJSON(connection, tags);
        insertIntoTransactionJSON(connection, tags);
    }

    private void insertInPostgresXML(Document xml) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        //get tags values
        NodeList nodeList = xml.getElementsByTagName("tags");
        Element tags = (Element) nodeList.item(0);

        insertIntoFileDescriptionXML(connection, tags);
        insertIntoFileXML(connection, tags);
        insertIntoBalanceXML(connection, tags);
        insertIntoTransactionXML(connection, tags);

    }

    private void insertIntoFileDescriptionXML(Connection connection, Element tags){
        String sqlFileDescription = "CALL insert_file_description(?::int, ?::int, ?::numeric, ?::numeric);";
        NodeList tag = tags.getElementsByTagName("generalInformationToAccountOwner");
        Element fileDescription = (Element) tag.item(0);
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileDescription);
            ps.setString(1, fileDescription.getElementsByTagName("numberOfDebitEntries").item(0).getTextContent());
            ps.setString(2, fileDescription.getElementsByTagName("numberOfCreditEntries").item(0).getTextContent());
            ps.setString(3, fileDescription.getElementsByTagName("debitEntriesTotalAmount").item(0).getTextContent());
            ps.setString(4, fileDescription.getElementsByTagName("creditEntriesTotalAmount").item(0).getTextContent());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void insertIntoFileXML(Connection connection, Element tags){
        LocalDate currentDate = LocalDate.from(getCurrentDate());
        String sqlFile = "CALL insert_file(?::varchar, ?::varchar, ?::int, ?::int, ?::int, ?::date)";

        NodeList accountIdentificationTag = tags.getElementsByTagName("accountIdentification");
        Element accountIdentification = (Element) accountIdentificationTag.item(0);
        NodeList transactionReferenceNumberTag = tags.getElementsByTagName("transactionReferenceNumber");
        Element transactionReferenceNumber = (Element) transactionReferenceNumberTag.item(0);
        NodeList statementNumberTag = tags.getElementsByTagName("statementNumber");
        Element statementNumber = (Element) statementNumberTag.item(0);

        int fileDescriptionId = getFileDescriptionId(connection);

        try{
            PreparedStatement ps = connection.prepareStatement(sqlFile);
            ps.setString(1, transactionReferenceNumber.getElementsByTagName("referenceNumber").item(0).getTextContent());
            ps.setString(2, accountIdentification.getElementsByTagName("accountNumber").item(0).getTextContent());
            ps.setString(3, statementNumber.getElementsByTagName("statementNumber").item(0).getTextContent());
            ps.setInt(4, fileDescriptionId);
            ps.setInt(5, 1);
            ps.setDate(6, Date.valueOf(currentDate)); //now date
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void insertIntoBalanceXML(Connection connection, Element tags) throws SQLException {
        int fileId = getFileId(connection);
        //balance
        String sqlBalance = "CALL Insert_balance( ?::char, ?::date, ?::varchar, ?::numeric, ?::varchar,  ?::int);";
        NodeList closingAvailableBalanceTag = tags.getElementsByTagName("closingAvailableBalance");
        Element closingAvailableBalance = (Element) closingAvailableBalanceTag.item(0);
        try{
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            ps.setString(1, closingAvailableBalance.getElementsByTagName("dCMark").item(0).getTextContent());
            ps.setString(2, closingAvailableBalance.getElementsByTagName("date").item(0).getTextContent());
            ps.setString(3, closingAvailableBalance.getElementsByTagName("currency").item(0).getTextContent());
            ps.setString(4, closingAvailableBalance.getElementsByTagName("amount").item(0).getTextContent());
            ps.setString(5, "closingAvailableBalance"); //type
            ps.setInt(6, fileId); //File_id
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        NodeList openingBalanceTag = tags.getElementsByTagName("openingBalance");
        Element openingBalance = (Element) openingBalanceTag.item(0);

        try {
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            ps.setString(1, openingBalance.getElementsByTagName("dCMark").item(0).getTextContent());
            ps.setString(2, openingBalance.getElementsByTagName("date").item(0).getTextContent());
            ps.setString(3, openingBalance.getElementsByTagName("currency").item(0).getTextContent());
            ps.setString(4, openingBalance.getElementsByTagName("amount").item(0).getTextContent());
            ps.setString(5, "openingBalance"); //type
            ps.setInt(6, fileId); //File_id
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        NodeList forwardAvailableBalancesTag = tags.getElementsByTagName("forwardAvailableBalance");

        for(int n = 0; n <forwardAvailableBalancesTag.getLength(); n++){
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            Element forwardAvailableBalance = (Element) forwardAvailableBalancesTag.item(n);
            try{
                ps.setString(1, forwardAvailableBalance.getElementsByTagName("dCMark").item(0).getTextContent());
                ps.setString(2, forwardAvailableBalance.getElementsByTagName("date").item(0).getTextContent());
                ps.setString(3, forwardAvailableBalance.getElementsByTagName("currency").item(0).getTextContent());
                ps.setString(4, forwardAvailableBalance.getElementsByTagName("amount").item(0).getTextContent());
                ps.setString(5, "forwardAvailableBalance"); //type
                ps.setInt(6, fileId); //File_id
                ps.executeUpdate();
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }
    private void insertIntoTransactionXML(Connection connection, Element tags){
        int fileId = getFileId(connection);

        //transactions
        NodeList transactions = tags.getElementsByTagName("transaction");
        for(int n = 0; n < transactions.getLength(); n++) {
            Element transaction = (Element) transactions.item(0);
            NodeList descriptionTag = transaction.getElementsByTagName("informationToAccountOwner");
            Element description = (Element) descriptionTag.item(0);
            try {
                //insert description
                String sqlDescription = "CALL Insert_description( ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar);";
                PreparedStatement psd = connection.prepareStatement(sqlDescription, Statement.RETURN_GENERATED_KEYS);
                psd.setString(1, description.getElementsByTagName("returnReason").item(0).getTextContent()); //return_reason
                psd.setString(2, description.getElementsByTagName("clientReference").item(0).getTextContent()); //client_reference
                psd.setString(3, description.getElementsByTagName("endToEndReference").item(0).getTextContent()); //end_to_end_reference
                psd.setString(4, description.getElementsByTagName("paymentInformationId").item(0).getTextContent()); //payment_information_id
                psd.setString(5, description.getElementsByTagName("instructionId").item(0).getTextContent()); //instruction_id
                psd.setString(6, description.getElementsByTagName("mandateReference").item(0).getTextContent()); //mandate_reference
                psd.setString(7, description.getElementsByTagName("creditorId").item(0).getTextContent()); //creditor_id
                psd.setString(8, description.getElementsByTagName("counterPartyId").item(0).getTextContent()); //counterparty_id
                psd.setString(9, description.getElementsByTagName("remittanceInformation").item(0).getTextContent()); //remittance_information
                psd.setString(10, description.getElementsByTagName("purposeCode").item(0).getTextContent()); //purpose_code
                psd.setString(11, description.getElementsByTagName("ultimateCreditor").item(0).getTextContent()); //ultimate_creditor
                psd.setString(12, description.getElementsByTagName("ultimateDebitor").item(0).getTextContent()); //ultimate_debtor
                psd.setString(13, description.getElementsByTagName("exchangeRate").item(0).getTextContent()); //exchange_rate
                psd.setString(14, description.getElementsByTagName("charges").item(0).getTextContent()); //charges
                psd.executeUpdate();

                int originalDescriptionId = getOriginalDescriptionId(connection);
                //insert transaction
                String sqlTransaction = "CALL Insert_Transaction(?::date, ?::varchar, ?::char, ?::numeric, ?::varchar, ?::int, ?::int, ?::int, ?::varchar, ?::varchar, ?::varchar, ?::varchar)";
                PreparedStatement ps = connection.prepareStatement(sqlTransaction);
                ps.setString(1, transaction.getElementsByTagName("valueDate").item(0).getTextContent());
                ps.setString(2, transaction.getElementsByTagName("entryDate").item(0).getTextContent());
                ps.setString(3, transaction.getElementsByTagName("debitCreditMark").item(0).getTextContent());
                ps.setString(4, transaction.getElementsByTagName("amount").item(0).getTextContent());
                ps.setString(5, transaction.getElementsByTagName("identificationCode").item(0).getTextContent());
                ps.setInt(6, 	originalDescriptionId);                       //original_description_ID from table description
                ps.setInt(7, fileId);
                ps.setInt(8, 1);
                if ( transaction.getElementsByTagName("referenceForTheAccountOwner").getLength() > 0 ) {
                    ps.setString(9, transaction.getElementsByTagName("referenceForTheAccountOwner").item(0).getTextContent());
                }
                if (transaction.getElementsByTagName("referenceOfTheAccountServicingInstitution").getLength() > 0) {
                    ps.setString(10, transaction.getElementsByTagName("referenceOfTheAccountServicingInstitution").item(0).getTextContent());
                }
                if(transaction.getElementsByTagName("supplementaryDetails").getLength() > 0) {
                    ps.setString(11, transaction.getElementsByTagName("supplementaryDetails").item(0).getTextContent());
                } else {
                    ps.setString(11, "");
                }
                ps.setString(12, "");                  //description
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private LocalDateTime getCurrentDate(){
        return LocalDateTime.now();
    }

    private int getFileDescriptionId(Connection connection){
        //get FILE_DESCRIPTION_ID
        String sqlFileDescriptionId = "SELECT f_d_id FROM file_description ORDER BY f_d_id DESC LIMIT 1";
        int fileDescriptionId = 0;
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileDescriptionId);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                fileDescriptionId = Integer.parseInt(rs.getString("f_d_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fileDescriptionId;
    }

    private int getOriginalDescriptionId(Connection connection){
        //get FILE_DESCRIPTION_ID
        String sqlFileDescriptionId = "SELECT d_id FROM description ORDER BY d_id DESC LIMIT 1";
        int descriptionId = 0;
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileDescriptionId);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                descriptionId = Integer.parseInt(rs.getString("d_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return descriptionId;
    }

    private int getFileId(Connection connection){
        //get FILE_ID
        String sqlFileId = "SELECT f_id FROM file ORDER BY f_id DESC LIMIT 1";
        int fileId = 0;
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileId);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                fileId = Integer.parseInt(rs.getString("f_id"));
//                System.out.println(fileId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fileId;
    }
    private void insertIntoFileDescriptionJSON(Connection connection, JSONObject tags){
        //file_description
        String sqlFileDescription = "CALL insert_file_description(?::int, ?::int, ?::numeric, ?::numeric);";
        JSONObject fileDescription = (JSONObject) tags.get("generalInformationToAccountOwner");
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileDescription);
            ps.setString(1, (String) fileDescription.get("numberOfDebitEntries"));
            ps.setString(2, (String) fileDescription.get("numberOfCreditEntries"));
            ps.setString(3, (String) fileDescription.get("debitEntriesTotalAmount"));
            ps.setString(4, (String) fileDescription.get("creditEntriesTotalAmount"));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertIntoFileJSON(Connection connection, JSONObject tags){
        String sqlFile = "CALL insert_file(?::varchar, ?::varchar, ?::int, ?::int, ?::int, ?::date)";
        JSONObject accountIdentification = (JSONObject) tags.get("accountIdentification");
        JSONObject transactionReferenceNumber = (JSONObject) tags.get("transactionReferenceNumber");
        JSONObject statementNumber = (JSONObject) tags.get("statementNumber");

        int fileDescriptionId = getFileDescriptionId(connection);

        try{
            PreparedStatement ps = connection.prepareStatement(sqlFile);
            ps.setString(1, (String) transactionReferenceNumber.get("referenceNumber"));
            ps.setString(2, (String) accountIdentification.get("accountNumber"));
            ps.setString(3, (String) statementNumber.get("statementNumber"));
            ps.setInt(4, fileDescriptionId);
            ps.setInt(5, 1);
            ps.setString(6, "2300-10-23");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private void insertIntoBalanceJSON(Connection connection, JSONObject tags) throws SQLException {
        int fileId = getFileId(connection);
        //balance
        String sqlBalance = "CALL Insert_balance( ?::char, ?::date, ?::varchar, ?::numeric, ?::varchar,  ?::int);";
        JSONObject closingAvailableBalance = (JSONObject) tags.get("closingAvailableBalance");
        try{
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            ps.setString(1, (String) closingAvailableBalance.get("dCMark"));
            ps.setString(2, (String) closingAvailableBalance.get("date"));
            ps.setString(3, (String) closingAvailableBalance.get("currency"));
            ps.setString(4, (String) closingAvailableBalance.get("amount"));
            ps.setString(5, "closingAvailableBalance"); //type
            ps.setInt(6, fileId); //File_id
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        JSONObject openingBalance = (JSONObject) tags.get("openingBalance");
        try {
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            ps.setString(1, (String) openingBalance.get("dCMark"));
            ps.setString(2, (String) openingBalance.get("date"));
            ps.setString(3, (String) openingBalance.get("currency"));
            ps.setString(4, (String) openingBalance.get("amount"));
            ps.setString(5, "openingBalance"); //type
            ps.setInt(6, fileId); //File_id
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        JSONArray forwardAvailableBalances = (JSONArray) tags.get("forwardAvailableBalance");


        for(int n = 0; n <forwardAvailableBalances.length(); n++){
            PreparedStatement ps = connection.prepareStatement(sqlBalance);
            JSONObject forwardAvailableBalance = (JSONObject) forwardAvailableBalances.get(n);
            try{
                ps.setString(1, (String) forwardAvailableBalance.get("dCMark"));
                ps.setString(2, (String) forwardAvailableBalance.get("date"));
                ps.setString(3, (String) forwardAvailableBalance.get("currency"));
                ps.setString(4, (String) forwardAvailableBalance.get("amount"));
                ps.setString(5, "forwardAvailableBalance"); //type
                ps.setInt(6, fileId); //File_id
                ps.executeUpdate();
            }catch (SQLException e){
                throw new RuntimeException(e);
            }
        }
    }

    private void insertIntoTransactionJSON(Connection connection, JSONObject tags){
        int fileId = getFileId(connection);


        //transactions
        JSONArray transactions = (JSONArray) tags.get("transactions");
        for(int n = 0; n < transactions.length(); n++) {
            //get descriptionID!!!
            try {
                //insert description
                JSONObject transaction = (JSONObject) transactions.get(n);
                JSONObject description = (JSONObject) transaction.get("informationToAccountOwner");
                String sqlDescription = "CALL Insert_description( ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar);";
                PreparedStatement psd = connection.prepareStatement(sqlDescription, Statement.RETURN_GENERATED_KEYS);
                psd.setString(1, (String) description.get("returnReason")); //return_reason
                psd.setString(2, (String) description.get("clientReference")); //client_reference
                psd.setString(3, (String) description.get("endToEndReference")); //end_to_end_reference
                psd.setString(4, (String) description.get("paymentInformationId")); //payment_information_id
                psd.setString(5, (String) description.get("instructionId")); //instruction_id
                psd.setString(6, (String) description.get("mandateReference")); //mandate_reference
                psd.setString(7, (String) description.get("creditorId")); //creditor_id
                psd.setString(8, (String) description.get("counterPartyId")); //counterparty_id
                psd.setString(9, (String) description.get("remittanceInformation")); //remittance_information
                psd.setString(10, (String) description.get("purposeCode")); //purpose_code
                psd.setString(11, (String) description.get("ultimateCreditor")); //ultimate_creditor
                psd.setString(12, (String) description.get("ultimateDebitor")); //ultimate_debtor
                psd.setString(13, (String) description.get("exchangeRate")); //exchange_rate
                psd.setString(14, (String) description.get("charges")); //charges
                psd.executeUpdate();

                int originalDescriptionId = getOriginalDescriptionId(connection);
                System.out.println(originalDescriptionId);
                //insert transaction
                String sqlTransaction = "CALL Insert_Transaction(?::date, ?::varchar, ?::char, ?::numeric, ?::varchar, ?::int, ?::int, ?::int, ?::varchar, ?::varchar, ?::varchar, ?::varchar)";
                PreparedStatement ps = connection.prepareStatement(sqlTransaction);
                ps.setString(1, (String) transaction.get("valueDate"));
                ps.setString(2, (String) transaction.get("entryDate"));
                ps.setString(3, (String) transaction.get("debitCreditMark"));
                ps.setString(4, (String) transaction.get("amount"));
                ps.setString(5, (String) transaction.get("identificationCode"));
                ps.setInt(6, 	originalDescriptionId);                       //original_description_ID from table description
                ps.setInt(7, fileId);
                ps.setInt(8, 1);
                if (transaction.has("referenceForTheAccountOwner")) {
                    ps.setString(9, (String) transaction.get("referenceForTheAccountOwner"));
                }
                if (transaction.has("referenceOfTheAccountServicingInstitution")) {
                    ps.setString(10, (String) transaction.get("referenceOfTheAccountServicingInstitution"));
                }
                if(transaction.has("supplementaryDetails")) {
                    ps.setString(11, (String) transaction.get("supplementaryDetails"));
                } else {
                    ps.setString(11, "");
                }
                ps.setString(12, "");                  //description
                ps.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }


}
