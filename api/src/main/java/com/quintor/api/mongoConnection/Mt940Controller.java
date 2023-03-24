package com.quintor.api.mongoConnection;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import com.quintor.api.validators.JSONSchemaValidator;
import com.quintor.api.validators.XMLSchemaValidator;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
        JSONSchemaValidator validator = new JSONSchemaValidator();
        return validator.validateFormat("{\n" +
                " \"header\": {\n" +
                "  \"logicalTerminal\": \"INGBNL2ABXXX\",\n" +
                "  \"receiverAddress\": \"INGBNL2AXXXN\",\n" +
                "  \"sequenceNumber\": \"000000\",\n" +
                "  \"mtId\": \"fin.940\",\n" +
                "  \"sessionNumber\": \"0000\",\n" +
                "  \"messageType\": \"940\",\n" +
                "  \"applicationId\": \"F\",\n" +
                "  \"serviceId\": \"01\"\n" +
                " },\n" +
                " \"tags\": {\n" +
                "  \"forwardAvailableBalance\": [\n" +
                "   {\n" +
                "    \"date\": \"140221\",\n" +
                "    \"currency\": \"EUR\",\n" +
                "    \"amount\": \"564,35\",\n" +
                "    \"dCMark\": \"C\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"date\": \"140224\",\n" +
                "    \"currency\": \"EUR\",\n" +
                "    \"amount\": \"564,35\",\n" +
                "    \"dCMark\": \"C\"\n" +
                "   }\n" +
                "  ],\n" +
                "  \"closingAvailableBalance\": {\n" +
                "   \"name\": \"64\",\n" +
                "   \"date\": \"140220\",\n" +
                "   \"description\": \"This tag specifies for the closing available balance, whether it is a debit or credit\\nbalance, the date, the currency and the amount of the balance\",\n" +
                "   \"currency\": \"EUR\",\n" +
                "   \"amount\": \"564,35\",\n" +
                "   \"dCMark\": \"C\"\n" +
                "  },\n" +
                "  \"generalInformationToAccountOwner\": {\n" +
                "   \"debitEntriesTotalAmount\": \"134,46\",\n" +
                "   \"numberOfDebitEntries\": \"4\",\n" +
                "   \"creditEntriesTotalAmount\": \"36,58\",\n" +
                "   \"numberOfCreditEntries\": \"4\"\n" +
                "  },\n" +
                "  \"accountIdentification\": {\n" +
                "   \"name\": \"25\",\n" +
                "   \"accountNumber\": \"NL69INGB0123456789EUR\",\n" +
                "   \"description\": \"This tag identifies the account for which the statement is sent\"\n" +
                "  },\n" +
                "  \"transactionReferenceNumber\": {\n" +
                "   \"name\": \"20\",\n" +
                "   \"referenceNumber\": \"P140220000000001\",\n" +
                "   \"description\": \"This tag specifies the reference assigned by the Sender to unambiguously identify\\nthe message.\"\n" +
                "  },\n" +
                "  \"closingBalance\": {\n" +
                "   \"name\": \"62F\",\n" +
                "   \"date\": \"140220\",\n" +
                "   \"description\": \"This tag specifies for the closing balance, whether it is a debit or credit balance, the\\ndate, the currency and the amount of the balance.\",\n" +
                "   \"currency\": \"EUR\",\n" +
                "   \"amount\": \"564,35\",\n" +
                "   \"dCMark\": \"C\"\n" +
                "  },\n" +
                "  \"transactions\": [\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"TRF\",\n" +
                "    \"amount\": \"1,56\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"EV12341REP1231456T1234\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/EREF/EV12341REP1231456T1234//CNTP/NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB///REMI/USTD//EV10001REP1000000T1000/\",\n" +
                "     \"remittanceInformation\": \"USTD//EV10001REP1000000T1000\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/00100/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"C\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"EREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001005\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"TRF\",\n" +
                "    \"amount\": \"1,57\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"M000000003333333\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/PREF/M000000003333333//REMI/USTD//TOTAAL 1 VZ/\",\n" +
                "     \"remittanceInformation\": \"USTD//TOTAAL 1 VZ\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/00200/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"D\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"PREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001006\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"RTI\",\n" +
                "    \"amount\": \"1,57\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"20120123456789\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/J.Janssen\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/RTRN/MS03//EREF/20120123456789//CNTP/NL32INGB0000012345/INGBNL2A/J.Janssen///REMI/USTD//Factuurnr 123456 Klantnr 00123/\",\n" +
                "     \"remittanceInformation\": \"USTD//Factuurnr 123456 Klantnr 00123\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"MS03\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/00190/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"C\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"EREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001007\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"DDT\",\n" +
                "    \"amount\": \"1,14\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"EV123REP123412T1234\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/ING Bank N.V. inzake WeB\",\n" +
                "     \"mandateReference\": \"MND-EV01\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/EREF/EV123REP123412T1234//MARF/MND-EV01//CSID/NL32ZZZ999999991234//CNTP/NL32INGB0000012345/INGBNL2A/ING Bank N.V. inzake WeB///REMI/USTD//EV123REP123412T1234/\",\n" +
                "     \"remittanceInformation\": \"USTD//EV123REP123412T1234\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"NL32ZZZ999999991234\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/01016/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"D\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"EREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001009\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"DDT\",\n" +
                "    \"amount\": \"1,45\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"M000000001111111\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/PREF/M000000001111111//CSID/NL32ZZZ999999991234//REMI/USTD//TOTAAL       1 POSTEN/\",\n" +
                "     \"remittanceInformation\": \"USTD//TOTAAL       1 POSTEN\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"NL32ZZZ999999991234\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/01000/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"C\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"PREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001008\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"RTI\",\n" +
                "    \"amount\": \"12,75\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"20120501P0123478\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/J.Janssen\",\n" +
                "     \"mandateReference\": \"MND-120123\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/RTRN/MS03//EREF/20120501P0123478//MARF/MND-120123//CSID/NL32ZZZ999999991234//CNTP/NL32INGB0000012345/INGBNL2A/J.Janssen///REMI/USTD//CONTRIBUTIE FEB 2014/\",\n" +
                "     \"remittanceInformation\": \"USTD//CONTRIBUTIE FEB 2014\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"MS03\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"NL32ZZZ999999991234\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/01315/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"D\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"EREF\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001010\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"TRF\",\n" +
                "    \"amount\": \"32,00\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {\n" +
                "     \"paymentInformationId\": \"\",\n" +
                "     \"ultimateCreditor\": \"\",\n" +
                "     \"endToEndReference\": \"15814016000676480\",\n" +
                "     \"ultimateDebitor\": \"\",\n" +
                "     \"counterPartyId\": \"NL32INGB0000012345/INGBNL2A/J.Janssen\",\n" +
                "     \"mandateReference\": \"\",\n" +
                "     \"charges\": \"\",\n" +
                "     \"accountOwnerInformationInOneLine\": \"/EREF/15814016000676480//CNTP/NL32INGB0000012345/INGBNL2A/J.Janssen///REMI/STRD/CUR/9001123412341234/\",\n" +
                "     \"remittanceInformation\": \"STRD/CUR/9001123412341234\",\n" +
                "     \"exchangeRate\": \"\",\n" +
                "     \"returnReason\": \"\",\n" +
                "     \"purposeCode\": \"\",\n" +
                "     \"clientReference\": \"\",\n" +
                "     \"instructionId\": \"\",\n" +
                "     \"creditorId\": \"\"\n" +
                "    },\n" +
                "    \"supplementaryDetails\": \"/TRCD/00108/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"C\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"9001123412341234\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001011\"\n" +
                "   },\n" +
                "   {\n" +
                "    \"transactionType\": \"N\",\n" +
                "    \"identificationCode\": \"TRF\",\n" +
                "    \"amount\": \"119,00\",\n" +
                "    \"entryDate\": \"0220\",\n" +
                "    \"informationToAccountOwner\": {},\n" +
                "    \"supplementaryDetails\": \"/TRCD/00108/\",\n" +
                "    \"name\": \"61\",\n" +
                "    \"debitCreditMark\": \"D\",\n" +
                "    \"valueDate\": \"140220\",\n" +
                "    \"referenceForTheAccountOwner\": \"1070123412341234\",\n" +
                "    \"referenceOfTheAccountServicingInstitution\": \"00000000001012\"\n" +
                "   }\n" +
                "  ],\n" +
                "  \"statementNumber\": {\n" +
                "   \"name\": \"28C\",\n" +
                "   \"statementNumber\": \"00000\",\n" +
                "   \"description\": \"This tag contains the sequential number of the statement\"\n" +
                "  },\n" +
                "  \"openingBalance\": {\n" +
                "   \"name\": \"60F\",\n" +
                "   \"date\": \"140219\",\n" +
                "   \"description\": \"This tag specifies, for the opening balance, whether it is a debit or credit balance,\\nthe date, the currency and the amount of the balance.\",\n" +
                "   \"currency\": \"EUR\",\n" +
                "   \"amount\": \"662,23\",\n" +
                "   \"dCMark\": \"C\"\n" +
                "  }\n" +
                " }\n" +
                "}");
    }

    @PostMapping("/MT940toXMLValidation")
    public String MT940toXMLValidation(@RequestParam("file") MultipartFile file) {
        XMLSchemaValidator schemaValidator = new XMLSchemaValidator();
        return schemaValidator.validateFormat("t");
    }
}
