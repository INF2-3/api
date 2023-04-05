package com.quintor.api.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class JSONSchemaValidator extends SchemaValidator {

    public JSONSchemaValidator() {
        super("json");
    }

    /**
     * This method retrieves the needed json schema and compares the StringBuffer input to the retrieved schema
     *
     * @param input StringBuffer containing json that needs to be validated
     * @return returns "Validated" if there were no errors, otherwise returns the error
     */
    @Override
    public String compareToSchema(String input, String schemaName) throws IOException {
        InputStream schemaStream = JSONSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/json/" + schemaName + ".json");
        JsonSchema jsonSchema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(schemaStream);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(input.toString());
        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        String errorsCombined = "";
        for (ValidationMessage error : errors) {
            errorsCombined += error.toString() + "\n";
        }

        if (errorsCombined.equals("")) {
            return input;
        }
        throw new IOException(errorsCombined);
    }
}
