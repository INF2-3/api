package com.quintor.api.postgreSqlConnection;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.quintor.api.mt940.Mt940Service;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

@RestController
@RequestMapping("api/mt940/postgres")
public class PostgreSqlController {
    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "root";
    private final String password = "changeme";

    public PostgreSqlController () {

    }

    @PostMapping("/insert")
    public String i (@RequestParam("userId") int userId, @RequestParam("file") File file) throws IOException, SQLException {
            //remove when using postman to test
        MultipartFile multipartFile = convertFileToMultiPartFile(file);
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "no_file";
        }
        if (1 <= 0) {
            return "wrong_user_id";
        }
        String SQL = "INSERT INTO test(naam) VALUES (?)";
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password)){


             // Step 2:Create a statement using connection object
//             PreparedStatement preparedStatement = connection.prepareStatement(SQL)) {
//            preparedStatement.setString(1, "afschrift");
             insertInto();

//            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
//            preparedStatement.executeUpdate();
//        }
//        catch (SQLException e) {

            // print SQL exception information
//            return e.toString();
            return "yes";
        }


//        return "help";
    }


    private void insertInto() throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);

        String[] json = new String[]{"{\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"TRF\",\n" +
             5   "    \"amount\": \"1,56\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"EV12341REP1231456T1234\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/EREF/EV12341REP1231456T1234//CNTP/NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB///REMI/USTD//EV10001REP1000000T1000/\",\n" +
                "     \"remittanceInformation\": \"USTD//EV10001REP1000000T1000\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"\"\n" +
                "    }"};


        try{
            String sql = "INSERT INTO transaction VALUES (?, ?::JSON)";
            PreparedStatement ps = connection.prepareStatement(sql);

            for (int i=0; i<4; i++) {
                ps.setInt (1, i+1);
                ps.setObject (2, json[i]);
                ps.executeUpdate();
            }
            connection.commit();
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

    private boolean getResponse(HttpURLConnection httpURLConnection) throws IOException {
        if (httpURLConnection == null) {
            return false;
        }
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response);
            if (response.toString().equals("success")) {
                return true;
            }
            return false;
        }
        return false;
    }
}
