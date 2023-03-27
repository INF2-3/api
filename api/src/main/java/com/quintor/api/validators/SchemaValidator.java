package com.quintor.api.validators;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class SchemaValidator {
    private String validatorType;

    public SchemaValidator(String validatorType) {
        this.validatorType = validatorType;
    }

    public abstract String validateFormat(MultipartFile file);

    public StringBuffer requestInputFromParser(MultipartFile file) throws IOException {
        String url = "http://localhost:8080/MT940to" + validatorType.toUpperCase();
        URL api = new URL(url);

        // Setup the connection
        String boundary = UUID.randomUUID().toString();
        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        // Setup the request body
        bufferedWriter.write("--" + boundary + "\r\n");
        bufferedWriter.write("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n");
        bufferedWriter.write("Content-Type: " + file.getContentType() + "\r\n");
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

        file.getInputStream().transferTo(outputStream);
        outputStream.flush();

        bufferedWriter.write("\r\n--" + boundary + "--\r\n");
        bufferedWriter.flush();
        outputStream.close();

        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response;
        }
        return null;
    }
}
