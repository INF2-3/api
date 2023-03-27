package com.quintor.api.postgreSqlConnection;

import net.minidev.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        JSONObject json;
        //remove when using postman to test
        MultipartFile multipartFile = convertFileToMultiPartFile(file);
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "no_file";
        }
        if (userId <= 0) {
            return "wrong_user_id";
        }
        json = parser(multipartFile);
        String SQL = "INSERT INTO test(naam) VALUES (?)";
        // Step 1: Establishing a Connection
        try (Connection connection = DriverManager.getConnection(url, user, password)){
             // Step 2:Create a statement using connection object
            PreparedStatement preparedStatement = connection.prepareStatement(SQL);
            preparedStatement.setString(1, "afschrift");
             insertInPostgres(json);

            System.out.println(preparedStatement);
            // Step 3: Execute the query or update query
            preparedStatement.executeUpdate();
        }catch (SQLException e) {

            // print SQL exception information
            return e.toString();
        }


        return SQL;
    }

    private JSONObject parser(MultipartFile file) throws IOException {
        String url = "http://localhost:9091" + "/api/mt940/insert";
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

        return getResponse(httpURLConnection);
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
        }
        return null;
    }

    private void insertInPostgres(JSONObject json) throws SQLException {
        Connection connection = DriverManager.getConnection(url, user, password);
        //get tag for the table
        json.get("forwardAvailableBalance");
        json.get("closingAvailableBalance");
        json.get("transactions");

        //get tag for the column
        try{
            String sql = "INSERT INTO transaction VALUES (?, ?::JSON)";
            PreparedStatement ps = connection.prepareStatement(sql);

            for (int i=0; i<4; i++) {
                ps.setInt (1, i+1);
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

}
