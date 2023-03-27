package com.quintor.api.validators;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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
        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();
        httpURLConnection.setRequestMethod("POST");
        String boundary = UUID.randomUUID().toString();
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        httpURLConnection.setDoOutput(true);
        OutputStream os = httpURLConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

        // Add the file parameter as a binary attachment
        writer.write("--" + boundary + "\r\n");
        writer.write("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n");
        writer.write("Content-Type: " + file.getContentType() + "\r\n");
        writer.write("\r\n");
        writer.flush();


        InputStream is = file.getInputStream();
        byte[] buffer = new byte[8192];
        int bytesRead;
        while ((bytesRead = is.read(buffer, 0, buffer.length)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();

        writer.write("\r\n--" + boundary + "--\r\n");
        writer.flush();
        os.close();

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
