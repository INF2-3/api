package com.quintor.api.validators;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public abstract class SchemaValidator {
    private final String validatorType;

    public SchemaValidator(String validatorType) {
        this.validatorType = validatorType;
    }

    public abstract String compareToSchema(StringBuffer input, String schemaName) throws SAXException, IOException;

    /**
     * First stores the output from the parser in a StringBuffer and calls the compareToSchema method to validate it
     *
     * @param file MT940 file
     * @return returns "Validated" or an error message
     */
    public String validateFormat(MultipartFile file, String schemaName) {
        try {
            StringBuffer input = requestInputFromParser(file);
            return compareToSchema(input, schemaName);
        } catch (SAXException | IOException e) {
            return e.getMessage();
        }
    }

    /**
     * This method makes a request to the MT940to{fileType} endpoint from the parser to get the parsed input
     * and writes it to a stringbuffer.
     *
     * @param file MT940 file
     * @return StringBuffer containing xml or json
     */
    public StringBuffer requestInputFromParser(MultipartFile file) throws IOException {
        String url = System.getenv("URL") + "/MT940to" + validatorType.toUpperCase();
        URL api = new URL(url);

        // Set up the connection
        String boundary = UUID.randomUUID().toString();
        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        httpURLConnection.setDoOutput(true);
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        // Set up the request body
        bufferedWriter.write("--" + boundary + "\r\n");
        bufferedWriter.write("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getOriginalFilename() + "\"\r\n");
        bufferedWriter.write("Content-Type: " + file.getContentType() + "\r\n");
        bufferedWriter.write("\r\n");
        bufferedWriter.flush();

        // Transfer input stream to upstream
        file.getInputStream().transferTo(outputStream);
        outputStream.flush();

        bufferedWriter.write("\r\n--" + boundary + "--\r\n");
        bufferedWriter.flush();
        outputStream.close();

        // Get the request response, if response is OK, write result to StringBuffer
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
