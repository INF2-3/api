package com.quintor.api.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class JSONSchemaValidator extends SchemaValidator {

    public JSONSchemaValidator() {
        super("json");
    }

    @Override
    public String validateFormat(MultipartFile file) {
        InputStream schemaStream = JSONSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/schema.json");
        JsonSchema jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaStream);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            StringBuffer input = requestInputFromParser(file);
            JsonNode jsonNode = objectMapper.readTree(input.toString());
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
            String errorsCombined = "";
            for (ValidationMessage error : errors) {
                errorsCombined += error.toString() + "\n";
            }

            if (errorsCombined.equals("")) {
                return "Validated";
            }
            return errorsCombined;
        } catch (IOException e) {
            return e.getMessage();
        }
    }
}
