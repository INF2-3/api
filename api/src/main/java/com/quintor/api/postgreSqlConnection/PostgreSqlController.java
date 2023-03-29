package com.quintor.api.postgreSqlConnection;

import com.quintor.api.mt940.Json;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/insert")
    public String insert(@RequestParam("file") File file, @RequestParam("userId") int userId) throws IOException, SQLException {

        System.out.println("in endpoint");
        //remove when using postman to test
        //MultipartFile multipartFile = convertFileToMultiPartFile(file);
        if (file == null) {
            return "no_file";
        }
        if (userId <= 0) {
            return "wrong_user_id";
        }
        JSONObject json = parser(file);
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password)){
             // Step 2:Create a statement using connection object
            //PreparedStatement preparedStatement = connection.prepareStatement(SQL);
             //insertInPostgres(json);

            //System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
          //  preparedStatement.executeUpdate();
        }catch (SQLException e) {

            // print SQL exception information
            return e.toString();
        }


        return "done";
    }

    private JSONObject parser(File file) throws IOException {
        String url = "http://localhost:9091" + "/MT940toJSON";
        URL api = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();

        //System.out.println(response);
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

        JSONObject test;
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer sb = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }
            String response2 = sb.toString();
            System.out.println(response2);
            test = getResponse(httpURLConnection);
            in.close();
            return test;
            // print result
        } else {
            System.out.println("POST request not worked");
        }


        return null;
    }

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


//    public String getResponse(HttpURLConnection connection) throws IOException {
//        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        return response.toString();
//    }
    private void insertInPostgres(JSONObject json) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        //get tag for the table

        //date, currency, amount, dCMark
        //multiple forwardAvailable balances!
        //json.get("forwardAvailableBalance");

        // name, date, description, currency, amount, dCMark
        //json.get("closingAvailableBalance");
        // transactionType, identificationCode, amount, entryDate, informationToAccountOwner{ endToEndReference, ...... }, supplementaryDetails, name, debitCreditMark, valueDate, referenceForTheAccountOwner, referenceOfTheAccountServicingInstitution
        JSONObject transactions = (JSONObject) json.get("transactions");

        //Loop with forEach transaction in transactions to get all te rows
        //transactions.get("amount");


        //informationToAccountOwner{ endToEndReference, ...... }
       // JSONObject accountOwnerInfo = (JSONObject) transactions.get("informationToAccountOwner");
        //paymentInformationId, ultimateCreditor, endToEndReference, ultimateDebitor, counterPartyId, mandateReference, charges, accountOwnerInformationInOneLine, remittanceInformation, exchangeRate, returnReason, purposeCode, clientReference, instructionId, creditorId


        String sqlTransaction = "CALL Insert_Transaction(?::date, ?::int, ?::char, ?::money, ?::varchar, ?::varchar, ?::varchar, ?::varchar, ?::int, ?::varchar, ?::int, ?::int)";
        System.out.println("in insertPostgress");
        //get tag for the column
        try{
            //String sql = "INSERT INTO transaction VALUES (?, ?::JSON)";
            PreparedStatement ps = connection.prepareStatement(sqlTransaction);
            ps.setDate(1, (Date) transactions.get("valueDate"));
            ps.setInt(2, (Integer) transactions.get("entryDate"));
            ps.setString(3, (String) transactions.get("debitCreditMark"));
            ps.setDouble(4, (Double) transactions.get("amount"));
            ps.setString(5, (String) transactions.get("identificationCode"));
            ps.setString(6, (String) transactions.get("referenceForTheAccountOwner"));
            ps.setString(7, (String) transactions.get("referenceOfTheAccountServicingInstitution"));
            ps.setString(8, (String) transactions.get("supplementaryDetails"));
            ps.setInt(9,2);                       //original_description_ID
            ps.setString(10, "This is a description");                  //description
            ps.setInt(11, 1);                     //fileID
            ps.setInt(12, 1); //catagoryID
            ps.executeUpdate();
            connection.commit();
            System.out.println(sqlTransaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
//    private <PGobject> getFromDbToJson(){
//        try{
//            String sql = "";
//            Connection connection = DriverManager.getConnection(url, user, password);
//            Statement stmnt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
//            ResultSet rs = stmnt.executeQuery(sql);
//
//            JSONPObject json;
//            JSONPObject json2;
//
//            while(rs.next()){
//
//            }
//
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }


    private MultipartFile convertFileToMultiPartFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        InputStream stream = new FileInputStream(file);
        return new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
    }

}
