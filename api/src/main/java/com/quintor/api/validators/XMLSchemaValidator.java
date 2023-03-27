package com.quintor.api.validators;

import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class XMLSchemaValidator extends SchemaValidator {

    public XMLSchemaValidator() {
        super("xml");
    }

    @Override
    public String validateFormat(MultipartFile file) {
        try {
            StringBuffer input = requestInputFromParser(file);
            InputStream schemaStream = XMLSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/schema.xml");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(schemaStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new ByteArrayInputStream(input.toString().getBytes(StandardCharsets.UTF_8))));

        } catch (SAXException | IOException e) {
            return e.getMessage();
        }
        return "Validated";
    }
}
