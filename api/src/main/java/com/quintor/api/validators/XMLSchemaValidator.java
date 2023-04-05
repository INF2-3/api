package com.quintor.api.validators;

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

    /**
     * This method gets the input from the parser in xml format and validates that xml against an xml schema
     *
     * @param input StringBuffer input that contains the xml
     * @return returns "Validated" if no errors occur, otherwise throw exception
     */
    @Override
    public String compareToSchema(String input, String schemaName) throws SAXException, IOException {
        InputStream schemaStream = XMLSchemaValidator.class.getClassLoader().getResourceAsStream("schemas/xml/" + schemaName + ".xsd");
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new StreamSource(schemaStream));
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(new ByteArrayInputStream(input.toString().getBytes(StandardCharsets.UTF_8))));
        return input;
    }
}
