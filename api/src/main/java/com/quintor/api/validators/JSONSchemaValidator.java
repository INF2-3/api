package com.quintor.api.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.InputStream;
import java.util.Set;

public class JSONSchemaValidator implements Validatable{
    @Override
    public String validateFormat(String input) {
        InputStream schemaStream = JSONSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/schema.json");
        JsonSchema jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaStream);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(input);
            Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
            String errorsCombined = "";
            for (ValidationMessage error : errors) {
                errorsCombined += error.toString() + "\n";
            }

            return errorsCombined;
        } catch (JsonProcessingException e) {
            return "Error";
        }
    }
}
