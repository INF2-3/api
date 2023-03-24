package com.quintor.api.validators;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class XMLSchemaValidator implements Validatable {

    @Override
    public String validateFormat(String input) {
        try {
            InputStream schemaStream = XMLSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/schema.xml");
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(schemaStream));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File("api/src/main/resources/schemas/test.xml")));

        } catch (SAXException | IOException e) {
            return e.getMessage();
        }
        return "Success";
    }
}
