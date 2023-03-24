package com.quintor.api.mongoConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.SchemaValidator;
import com.quintor.api.validators.Validatable;
import com.quintor.api.validators.XMLSchemaValidator;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@RestController
@RequestMapping("api/mt940")
public class Mt940Controller {
    private final Mt940Service mt940Service;

    public Mt940Controller(Mt940Service mt940Service) {
        this.mt940Service = mt940Service;
    }

    public Mt940Service getMt940Service() {
        return this.mt940Service;
    }

    /**
     * This method can called when a file should be uploaded to the MongoDB. It inserts the file with a userId.
     *
     * @param file   The file which should be uploaded.
     * @param userId The user id of the user who uploads the file.
     * @return A string with succes when the file has been uploaded. A string with no_file when there is no file. And a string with wrong_user_id when the user id is invalid.
     * @throws IOException
     */
    @PostMapping("/insert")
    public String insertMt940(@RequestParam("file") File file, @RequestParam("userId") int userId) throws IOException {
        MultipartFile multipartFile = convertFileToMultiPartFile(file);
        if (multipartFile == null || multipartFile.isEmpty()) {
            return "no_file";
        }
        if (userId <= 0) {
            return "wrong_user_id";
        }
        String document = new String(multipartFile.getBytes(), StandardCharsets.UTF_8);
        mt940Service.insertMt940(new Mt940(document, userId));
        return "success";
    }

    /**
     * This method converts a File object to a MultiPartFile object.
     *
     * @param file The file which should be converted.
     * @return a MultiPartFile object.
     * @throws IOException
     */
    private MultipartFile convertFileToMultiPartFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        InputStream stream = new FileInputStream(file);
        return new MockMultipartFile("file", file.getName(), MediaType.TEXT_HTML_VALUE, stream);
    }
//
    @PostMapping("/MT940toJSONValidation")
    public String MT940toJSONValidation(@RequestParam("file") MultipartFile file) {
        SchemaValidator schemaValidator = new JSONSchemaValidator();
        return schemaValidator.validateFormat(file);
    }

    @PostMapping("/MT940toXMLValidation")
    public String MT940toXMLValidation(@RequestParam("file") MultipartFile file) {
        SchemaValidator schemaValidator = new XMLSchemaValidator();
        return schemaValidator.validateFormat(file);
    }

//    private String postRequestToParser(MultipartFile file, Validatable validator) throws IOException {
//        String url = "http:localhost:8080/MT940toJSON";
//        URL api = new URL(url);
//        HttpURLConnection httpURLConnection = (HttpURLConnection) api.openConnection();
//        httpURLConnection.setRequestMethod("POST");
//        httpURLConnection.setRequestProperty("Accept", "application/json");
//
//        httpURLConnection.setDoOutput(true);
//        OutputStream os = httpURLConnection.getOutputStream();
//        String params = "file=" + file;
//        os.write(params.getBytes());
//        os.flush();
//        os.close();
//
//        int responseCode = httpURLConnection.getResponseCode();
//        if (responseCode == HttpURLConnection.HTTP_OK) {
//            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//
//            return validator.validateFormat(response.toString());
//        }
//        return String.valueOf(responseCode);
//    }
}
