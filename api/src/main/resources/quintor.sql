-- Adminer 4.8.1 PostgreSQL 15.1 dump

DROP TABLE IF EXISTS "balance";
DROP SEQUENCE IF EXISTS balance_id_seq;
CREATE SEQUENCE balance_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."balance" (
    "id" integer DEFAULT nextval('balance_id_seq') NOT NULL,
    "debit/credit" character(1) NOT NULL,
    "date" date NOT NULL,
    "currency" character varying(3) NOT NULL,
    "amount" money NOT NULL,
    "type" character varying(25) NOT NULL,
    "fileId" integer NOT NULL,
    CONSTRAINT "balance_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

CREATE INDEX "balance_fileId" ON "public"."balance" USING btree ("fileId");


DROP TABLE IF EXISTS "category";
DROP SEQUENCE IF EXISTS category_id_seq;
CREATE SEQUENCE category_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."category" (
    "id" integer DEFAULT nextval('category_id_seq') NOT NULL,
    "name" character varying(255) NOT NULL,
    CONSTRAINT "category_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "description";
CREATE TABLE "public"."description" (
    "id" integer NOT NULL,
    "returnReason" character varying(255),
    "clientReference" character varying(255),
    "endToEndReference" character varying(255),
    "paymentInformionId" character varying(255),
    "instructionId" character varying(255),
    "mandateReference" character varying(255),
    "creditorId" character varying(255),
    "counterpartyId" character varying(255),
    "remittanceInformation" character varying(255),
    "purposeCode" character varying(255),
    "ultimateCreditor" character varying(255),
    "ultimateDebtor" character varying(255),
    "exchangeRate" character varying(255),
    "charges" character varying(255),
    CONSTRAINT "description_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "file";
DROP SEQUENCE IF EXISTS file_id_seq;
CREATE SEQUENCE file_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."file" (
    "id" integer DEFAULT nextval('file_id_seq') NOT NULL,
    "transactionReferenceNumber" character varying(16) NOT NULL,
    "accountNumber" character varying(35) NOT NULL,
    "statementNumber" integer NOT NULL,
    "fileDescriptionId" integer NOT NULL,
    "lastUpdatedUser" integer NOT NULL,
    "uploadDate" date NOT NULL,
    "filecol" character varying(45) NOT NULL,
    CONSTRAINT "file_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

CREATE INDEX "file_fileDescriptionId" ON "public"."file" USING btree ("fileDescriptionId");

CREATE INDEX "file_lastUpdatedUser" ON "public"."file" USING btree ("lastUpdatedUser");


DROP TABLE IF EXISTS "fileDescription";
DROP SEQUENCE IF EXISTS "fileDescription_id_seq";
CREATE SEQUENCE "fileDescription_id_seq" INCREMENT 1  MINVALUE 1 MAXVALUE 2147483647  CACHE 1;

CREATE TABLE "public"."fileDescription" (
    "id" integer DEFAULT nextval('"fileDescription_id_seq"') NOT NULL,
    "numberOfDebitEntries" integer NOT NULL,
    "numberOfCreditEntries" integer NOT NULL,
    "amountOfDebitEntries" money NOT NULL,
    "amountOfCreditEntries" money NOT NULL,
    CONSTRAINT "fileDescription_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "role";
DROP SEQUENCE IF EXISTS role_id_seq;
CREATE SEQUENCE role_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."role" (
    "id" integer DEFAULT nextval('role_id_seq') NOT NULL,
    "roleName" character varying(255),
    CONSTRAINT "role_pkey" PRIMARY KEY ("id")
) WITH (oids = false);


DROP TABLE IF EXISTS "transaction";
DROP SEQUENCE IF EXISTS transaction_id_seq;
CREATE SEQUENCE transaction_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."transaction" (
    "id" integer DEFAULT nextval('transaction_id_seq') NOT NULL,
    "valueDate" date NOT NULL,
    "entryDate" integer NOT NULL,
    "debit/credit" character(1) NOT NULL,
    "amount" money NOT NULL,
    "transactionCode" character varying(4) NOT NULL,
    "referenceOwner" character varying(16),
    "institutionReference" character varying(16),
    "supplementaryDetails" character varying(34),
    "originalDescriptionId" integer NOT NULL,
    "description" character varying(500),
    "fileId" integer NOT NULL,
    "categoryId" integer NOT NULL,
    CONSTRAINT "transaction_pkey" PRIMARY KEY ("id")
) WITH (oids = false);

CREATE INDEX "transaction_categoryId" ON "public"."transaction" USING btree ("categoryId");

CREATE INDEX "transaction_fileId" ON "public"."transaction" USING btree ("fileId");

CREATE INDEX "transaction_originalDescriptionId" ON "public"."transaction" USING btree ("originalDescriptionId");


DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."user" (
    "id" integer DEFAULT nextval('user_id_seq') NOT NULL,
    "email" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "roleId" integer NOT NULL,
    "username" character varying(255) NOT NULL,
    CONSTRAINT "user_email" UNIQUE ("email"),
    CONSTRAINT "user_id" UNIQUE ("id"),
    CONSTRAINT "user_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "user_username" UNIQUE ("username")
) WITH (oids = false);


ALTER TABLE ONLY "public"."balance" ADD CONSTRAINT "balance_fileId_fkey" FOREIGN KEY ("fileId") REFERENCES file(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_fileDescriptionId_fkey" FOREIGN KEY ("fileDescriptionId") REFERENCES "fileDescription"(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_lastUpdatedUser_fkey" FOREIGN KEY ("lastUpdatedUser") REFERENCES "user"(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_categoryId_fkey" FOREIGN KEY ("categoryId") REFERENCES category(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_fileId_fkey" FOREIGN KEY ("fileId") REFERENCES file(id) NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_originalDescriptionId_fkey" FOREIGN KEY ("originalDescriptionId") REFERENCES description(id) NOT DEFERRABLE;

ALTER TABLE ONLY "public"."user" ADD CONSTRAINT "user_id_fkey" FOREIGN KEY (id) REFERENCES role(id) NOT DEFERRABLE;

-- 2023-03-22 13:05:36.02967+00
