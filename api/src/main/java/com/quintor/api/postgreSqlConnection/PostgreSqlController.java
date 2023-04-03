package com.quintor.api.postgreSqlConnection;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

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
    public String insert(@RequestParam("file") File file, @RequestParam("userId") int userId) throws IOException, ParseException {

        if (file == null) {
            return "no_file";
        }
        if (userId <= 0) {
            return "wrong_user_id";
        }
        String jsonString = parser(file);

        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password)){
             // Step 2:Create a statement using connection object
            assert json != null;
            insertInPostgres(json);

            // Step 3: Execute the query or update query
        }catch (SQLException e) {

            // print SQL exception information
            return e.toString();
        }


        return "done";
    }

    /*
    * sets up connection with parser
    * and returns String gotten from parser wht getResponse()
    */
    private String parser(File file) throws IOException {
        String url = "http://localhost:9091" + "/MT940toJSON";
        URL api = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();

        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        String params = "file=" + file;
        os.write(params.getBytes());
        os.flush();
        os.close();

        int responseCode = httpURLConnection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            String response2 = sb.toString();
            in.close();
            return response2;
        } else {
            System.out.println("POST request not worked");
        }


        return null;
    }

    /*
    * gets response from endpoint in bufferedReader and into string
    */
    private JSONObject getResponse(HttpURLConnection httpURLConnection) throws IOException {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        if (100 <= httpURLConnection.getResponseCode() && httpURLConnection.getResponseCode() <= 399) {
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            JSONObject json = new JSONObject();
            json.writeJSONString(sb);
            return json;
        } else {
            br = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            String line;
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            JSONObject json = new JSONObject();
            json.writeJSONString(sb);
            return json;
        }
    }

    /*
    * gets tags from the json and inserts it into postgresql
    * with stored procedures. does table after table
    */
    private void insertInPostgres(JSONObject json) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        //get tag for the table
        JSONObject tags = (JSONObject) json.get("tags");

        //file_description
        String sqlFileDescription = "CALL insert_file_description(?::int, ?::int, ?::money, ?::money);";
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

        //file
        String sqlFile = "CALL insert_file(?::varchar, ?::varchar, ?::int, ?::int, ?::int, ?::date, ?::varchar)";
        JSONObject accountIdentification = (JSONObject) tags.get("accountIdentification");
        JSONObject transactionReferenceNumber = (JSONObject) tags.get("transactionReferenceNumber");
        JSONObject statementNumber = (JSONObject) tags.get("statementNumber");

        //get FILE_DESCRIPTION_ID
        String sqlFileDescriptionId = "SELECT id FROM file_description ORDER BY id DESC LIMIT 1";
        int fileDescriptionId = 0;
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileDescriptionId);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                fileDescriptionId = Integer.parseInt(rs.getString("id"));
                System.out.println(fileDescriptionId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try{
            PreparedStatement ps = connection.prepareStatement(sqlFile);
            ps.setString(1, (String) transactionReferenceNumber.get("referenceNumber"));
            ps.setString(2, (String) accountIdentification.get("accountNumber"));
            ps.setString(3, (String) statementNumber.get("statementNumber"));
            ps.setInt(4, fileDescriptionId);
            ps.setInt(5, 1);
            ps.setString(6, "2300-10-23");
            ps.setString(7, "What?");
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //get FILE_ID
        String sqlFileId = "SELECT id FROM file ORDER BY id DESC LIMIT 1";
        int fileId = 0;
        try{
            PreparedStatement ps = connection.prepareStatement(sqlFileId);
            ResultSet rs = ps.executeQuery();
            while ( rs.next() ) {
                fileId = Integer.parseInt(rs.getString("id"));
                System.out.println(fileId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        //balance
        String sqlBalance = "CALL Insert_balance( ?::char, ?::date, ?::varchar, ?::money, ?::varchar,  ?::int);";
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


        for(int n = 0; n <forwardAvailableBalances.size(); n++){
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


        //transactions
        JSONArray transactions = (JSONArray) tags.get("transactions");
        for(int n = 0; n < transactions.size(); n++) {
            //get descriptionID!!!
            JSONObject transaction = (JSONObject) transactions.get(n);
            JSONObject description = (JSONObject) transaction.get("informationToAccountOwner");
            try{
                String sqlTransaction = "CALL Insert_Transaction(?::date, ?::int, ?::char, ?::money, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::int, ?::varchar, ?::int, ?::int)";
                PreparedStatement ps = connection.prepareStatement(sqlTransaction);
                ps.setString(1, (String) transaction.get("valueDate"));
                ps.setString(2, (String) transaction.get("entryDate"));
                ps.setString(3, (String) transaction.get("debitCreditMark"));
                ps.setString(4, (String) transaction.get("amount"));
                ps.setString(5, (String) transaction.get("identificationCode"));
                ps.setString(6, (String) transaction.get("referenceForTheAccountOwner"));
                ps.setString(7, (String) transaction.get("referenceOfTheAccountServicingInstitution"));
                ps.setString(8, (String) transaction.get("supplementaryDetails"));
                ps.setInt(9,2);                       //original_description_ID
                ps.setString(10, "This is a description");                  //description
                ps.setInt(11, fileId); //catagoryID
                ps.setInt(12, 1); //catagoryID
                ps.executeUpdate();

                //Description transaction ID as foreinkey might need to be added
                String sqlDescription = "CALL Insert_description( ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::varchar);";
                PreparedStatement psd = connection.prepareStatement(sqlDescription);
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

            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        }
    }
}
