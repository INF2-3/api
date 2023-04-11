package com.quintor.api.postgreSqlConnection;

import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.SchemaValidator;
import com.quintor.api.validators.XMLSchemaValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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
    private static final String url = System.getenv("POSTGRESQL_URL");
    private static final String user = System.getenv("POSTGRESQL_USER");
    private static final String password = System.getenv("POSTGRESQL_PASSWORD");

    /*
     * Function inserts MT940 JSON from parser into POSTGRESQL
     * It calls the function insertInPostgres(json) if json is not null
     * */
    @PostMapping("/insert")
    public ResponseEntity<Boolean> insert(@RequestParam("file") File file, @RequestParam("userId") int userId, @RequestParam("mode") String mode) throws IOException, ParserConfigurationException, SAXException {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            if (file == null || userId <= 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
            }

            if (mode.equals("JSON")) {
                String jsonString = parserJSON(file);
                JSONObject json = new JSONObject(jsonString);

                insertInPostgresJSON(json);
            }

            if (mode.equals("XML")) {
                String xmlString = parserXML(file);
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                InputSource inputSource = new InputSource(new StringReader(xmlString));
                Document xml = documentBuilder.parse(inputSource);

                insertInPostgresXML(xml);
            }
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<Boolean> update(@RequestParam("transactionId") int transactionId, @RequestParam("description") String description, @RequestParam("category") String category, @RequestParam("mode") String mode) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String updateSQL = "UPDATE transaction SET t_description = ?, t_category_id = ? WHERE t_id = ?";
            PreparedStatement stmt = connection.prepareStatement(updateSQL);

            if(description.equals("null")) {
                stmt.setString(1, "");
            } else {
                stmt.setString(1, description);
            }
            if (category.equals("")) {
                stmt.setNull(2, Types.NULL);
            } else {
                stmt.setInt(2, getCategoryId(connection, category));
            }
            stmt.setInt(3, transactionId);
            stmt.executeUpdate();
            return ResponseEntity.status(HttpStatus.OK).body(true);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    private int getCategoryId(Connection connection, String category) throws SQLException {
        String getCategory = "SELECT c_id FROM category WHERE c_name = ?";
        PreparedStatement getCatStmt = connection.prepareStatement(getCategory);
        getCatStmt.setString(1, category);
        ResultSet resultSet = getCatStmt.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("c_id");
        } else {
            String createCategory = "INSERT INTO category (c_name) VALUES (?) RETURNING c_id";
            PreparedStatement createCatStmt = connection.prepareStatement(createCategory, Statement.RETURN_GENERATED_KEYS);
            createCatStmt.setString(1, category);
            createCatStmt.executeUpdate();
            ResultSet rs = createCatStmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException();
        }
    }

    private boolean doesFileExistInDatabase(Connection connection, String referenceNumber) throws SQLException {
        String sqlSelectFile = "SELECT * FROM file WHERE f_transaction_reference_number = ?";
        PreparedStatement selectFiles = connection.prepareStatement(sqlSelectFile);
        selectFiles.setString(1, referenceNumber);
        ResultSet resultSet = selectFiles.executeQuery();
        if (resultSet.next()) {
            return true;
        }
        return false;
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
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            JSONObject tags = (JSONObject) json.get("tags");

            insertIntoFileDescriptionJSON(connection, tags);
            insertIntoFileJSON(connection, tags);
            insertIntoBalanceJSON(connection, tags);
            insertIntoTransactionJSON(connection, tags);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    private void insertInPostgresXML(Document xml) throws SQLException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);

            insertIntoFileDescriptionXML(connection, xml);
            insertIntoFileXML(connection, xml);
            insertIntoBalanceXML(connection, xml);
            insertIntoTransactionXML(connection, xml);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw new SQLException();
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
    }

    private void insertIntoFileDescriptionXML(Connection connection, Document xml) {
        String sqlFileDescription = "CALL insert_file_description(?::int, ?::int, ?::numeric, ?::numeric);";

        try {
            PreparedStatement ps = connection.prepareStatement(sqlFileDescription);
            ps.setString(1, xml.getElementsByTagName("numberOfDebitEntries").item(0).getTextContent());
            ps.setString(2, xml.getElementsByTagName("numberOfCreditEntries").item(0).getTextContent());
            ps.setString(3, xml.getElementsByTagName("debitEntriesTotalAmount").item(0).getTextContent());
            ps.setString(4, xml.getElementsByTagName("creditEntriesTotalAmount").item(0).getTextContent());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void insertIntoFileXML(Connection connection, Document xml) throws SQLException {
        LocalDate currentDate = LocalDate.from(getCurrentDate());
        String sqlFile = "CALL insert_file(?::varchar, ?::varchar, ?::int, ?::int, ?::int, ?::date)";

        if (doesFileExistInDatabase(connection, xml.getElementsByTagName("referenceNumber").item(0).getTextContent())) {
            throw new SQLException();
        }

        int fileDescriptionId = getFileDescriptionId(connection);

        try {
            PreparedStatement ps = connection.prepareStatement(sqlFile);
            ps.setString(1, xml.getElementsByTagName("referenceNumber").item(0).getTextContent());
            ps.setString(2, xml.getElementsByTagName("accountNumber").item(0).getTextContent());
            ps.setString(3, xml.getElementsByTagName("statementNumber").item(1).getTextContent());
            ps.setInt(4, fileDescriptionId);
            ps.setInt(5, 1);
            ps.setDate(6, Date.valueOf(currentDate)); //now date
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareXMLBalanceInsert(Connection connection, Element balance, PreparedStatement ps) {
        try {
            int fileId = getFileId(connection);
            ps.setString(1, balance.getElementsByTagName("dCMark").item(0).getTextContent());
            ps.setString(2, balance.getElementsByTagName("date").item(0).getTextContent());
            ps.setString(3, balance.getElementsByTagName("currency").item(0).getTextContent());
            ps.setString(4, balance.getElementsByTagName("amount").item(0).getTextContent());
            ps.setString(5, balance.getNodeName()); //type
            ps.setInt(6, fileId); //File_id
            ps.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertIntoBalanceXML(Connection connection, Document xml) throws SQLException {
        //balance
        String sqlBalance = "CALL Insert_balance( ?::char, ?::date, ?::varchar, ?::numeric, ?::varchar,  ?::int);";

        // add closingAvailableBalance to prepared statement batch
        NodeList closingAvailableBalanceTag = xml.getElementsByTagName("closingAvailableBalance");
        Element closingAvailableBalance = (Element) closingAvailableBalanceTag.item(0);
        PreparedStatement ps = connection.prepareStatement(sqlBalance);
        prepareXMLBalanceInsert(connection, closingAvailableBalance, ps);


        // add closingBalance to prepared statement batch
        NodeList closingBalanceTag = xml.getElementsByTagName("closingBalance");
        Element closingBalance = (Element) closingBalanceTag.item(0);
        prepareXMLBalanceInsert(connection, closingBalance, ps);

        // add openingBalance to prepared statement batch
        NodeList openingBalanceTag = xml.getElementsByTagName("openingBalance");
        Element openingBalance = (Element) openingBalanceTag.item(0);
        prepareXMLBalanceInsert(connection, openingBalance, ps);


        // add all forward available balances to prepared statement batch
        NodeList forwardAvailableBalancesTag = xml.getElementsByTagName("forwardAvailableBalance");
        for (int n = 0; n < forwardAvailableBalancesTag.getLength(); n++) {
            Element forwardAvailableBalance = (Element) forwardAvailableBalancesTag.item(n);
            prepareXMLBalanceInsert(connection, forwardAvailableBalance, ps);
        }
        ps.executeBatch();
    }

    private void insertIntoTransactionXML(Connection connection, Document xml) {
        int fileId = getFileId(connection);

        //transactions
        NodeList transactions = xml.getElementsByTagName("transaction");
        for (int n = 0; n < transactions.getLength(); n++) {
            Element transaction = (Element) transactions.item(n);
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
                ps.setInt(6, originalDescriptionId);                       //original_description_ID from table description
                ps.setInt(7, fileId);
                ps.setNull(8, Types.INTEGER);
                if (transaction.getElementsByTagName("referenceForTheAccountOwner").getLength() > 0) {
                    ps.setString(9, transaction.getElementsByTagName("referenceForTheAccountOwner").item(0).getTextContent());
                }
                if (transaction.getElementsByTagName("referenceOfTheAccountServicingInstitution").getLength() > 0) {
                    ps.setString(10, transaction.getElementsByTagName("referenceOfTheAccountServicingInstitution").item(0).getTextContent());
                }
                if (transaction.getElementsByTagName("supplementaryDetails").getLength() > 0) {
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

    private LocalDateTime getCurrentDate() {
        return LocalDateTime.now();
    }

    private int getFileDescriptionId(Connection connection) {
        //get FILE_DESCRIPTION_ID
        String sqlFileDescriptionId = "SELECT f_d_id FROM file_description ORDER BY f_d_id DESC LIMIT 1";
        int fileDescriptionId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sqlFileDescriptionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fileDescriptionId = Integer.parseInt(rs.getString("f_d_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fileDescriptionId;
    }

    private int getOriginalDescriptionId(Connection connection) {
        //get FILE_DESCRIPTION_ID
        String sqlFileDescriptionId = "SELECT d_id FROM description ORDER BY d_id DESC LIMIT 1";
        int descriptionId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sqlFileDescriptionId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                descriptionId = Integer.parseInt(rs.getString("d_id"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return descriptionId;
    }

    private int getFileId(Connection connection) {
        //get FILE_ID
        String sqlFileId = "SELECT f_id FROM file ORDER BY f_id DESC LIMIT 1";
        int fileId = 0;
        try {
            PreparedStatement ps = connection.prepareStatement(sqlFileId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fileId = Integer.parseInt(rs.getString("f_id"));
//                System.out.println(fileId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return fileId;
    }

    private void insertIntoFileDescriptionJSON(Connection connection, JSONObject tags) {
        //file_description
        String sqlFileDescription = "CALL insert_file_description(?::int, ?::int, ?::numeric, ?::numeric);";
        JSONObject fileDescription = (JSONObject) tags.get("generalInformationToAccountOwner");
        try {
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

    private void insertIntoFileJSON(Connection connection, JSONObject tags) throws SQLException {

        LocalDate currentDate = LocalDate.from(getCurrentDate());
        String sqlFile = "CALL insert_file(?::varchar, ?::varchar, ?::int, ?::int, ?::int, ?::date)";
        JSONObject accountIdentification = (JSONObject) tags.get("accountIdentification");
        JSONObject transactionReferenceNumber = (JSONObject) tags.get("transactionReferenceNumber");
        JSONObject statementNumber = (JSONObject) tags.get("statementNumber");

        if(doesFileExistInDatabase(connection, (String) transactionReferenceNumber.get("referenceNumber"))) {
            throw new SQLException();
        }

        int fileDescriptionId = getFileDescriptionId(connection);

        try {
            PreparedStatement ps = connection.prepareStatement(sqlFile);
            ps.setString(1, (String) transactionReferenceNumber.get("referenceNumber"));
            ps.setString(2, (String) accountIdentification.get("accountNumber"));
            ps.setString(3, (String) statementNumber.get("statementNumber"));
            ps.setInt(4, fileDescriptionId);
            ps.setInt(5, 1);
            ps.setString(6, String.valueOf(currentDate));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void prepareJSONBalanceInsert(Connection connection, JSONObject balance, String balanceName, PreparedStatement ps) {
        try {
            int fileId = getFileId(connection);
            ps.setString(1, (String) balance.get("dCMark"));
            ps.setString(2, (String) balance.get("date"));
            ps.setString(3, (String) balance.get("currency"));
            ps.setString(4, (String) balance.get("amount"));
            ps.setString(5, balanceName); //type
            ps.setInt(6, fileId); //File_id
            ps.addBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertIntoBalanceJSON(Connection connection, JSONObject tags) throws SQLException {
        //balance
        String sqlBalance = "CALL Insert_balance( ?::char, ?::date, ?::varchar, ?::numeric, ?::varchar,  ?::int);";
        PreparedStatement ps = connection.prepareStatement(sqlBalance);

        JSONObject closingAvailableBalance = (JSONObject) tags.get("closingAvailableBalance");
        prepareJSONBalanceInsert(connection, closingAvailableBalance, "closingAvailableBalance", ps);

        JSONObject closingBalance = (JSONObject) tags.get("closingBalance");
        prepareJSONBalanceInsert(connection, closingBalance, "closingBalance", ps);

        JSONObject openingBalance = (JSONObject) tags.get("openingBalance");
        prepareJSONBalanceInsert(connection, openingBalance, "openingBalance", ps);


        JSONArray forwardAvailableBalances = (JSONArray) tags.get("forwardAvailableBalance");
        for (int n = 0; n < forwardAvailableBalances.length(); n++) {
            JSONObject forwardAvailableBalance = (JSONObject) forwardAvailableBalances.get(n);
            prepareJSONBalanceInsert(connection, forwardAvailableBalance, "forwardAvailableBalance", ps);
        }
        ps.executeBatch();
    }

    private void insertIntoTransactionJSON(Connection connection, JSONObject tags) {
        int fileId = getFileId(connection);


        //transactions
        JSONArray transactions = (JSONArray) tags.get("transactions");
        for (int n = 0; n < transactions.length(); n++) {
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
                //insert transaction
                String sqlTransaction = "CALL Insert_Transaction(?::date, ?::varchar, ?::char, ?::numeric, ?::varchar, ?::int, ?::int, ?::int, ?::varchar, ?::varchar, ?::varchar, ?::varchar)";
                PreparedStatement ps = connection.prepareStatement(sqlTransaction);
                ps.setString(1, (String) transaction.get("valueDate"));
                ps.setString(2, (String) transaction.get("entryDate"));
                ps.setString(3, (String) transaction.get("debitCreditMark"));
                ps.setString(4, (String) transaction.get("amount"));
                ps.setString(5, (String) transaction.get("identificationCode"));
                ps.setInt(6, originalDescriptionId);                       //original_description_ID from table description
                ps.setInt(7, fileId);
                ps.setNull(8, Types.INTEGER);
                if (transaction.has("referenceForTheAccountOwner")) {
                    ps.setString(9, (String) transaction.get("referenceForTheAccountOwner"));
                }
                if (transaction.has("referenceOfTheAccountServicingInstitution")) {
                    ps.setString(10, (String) transaction.get("referenceOfTheAccountServicingInstitution"));
                }
                if (transaction.has("supplementaryDetails")) {
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
