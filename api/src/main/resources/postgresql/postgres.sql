-- Adminer 4.8.1 PostgreSQL 15.1 dump

DROP TABLE IF EXISTS "balance";
DROP SEQUENCE IF EXISTS balance_id_seq;
CREATE SEQUENCE balance_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."balance" (
    "b_id" integer DEFAULT nextval('balance_id_seq') NOT NULL,
    "debit_credit" character(1) NOT NULL,
    "date" date NOT NULL,
    "currency" character varying(3) NOT NULL,
    "amount" money NOT NULL,
    "type" character varying(25) NOT NULL,
    "file_id" integer NOT NULL,
    CONSTRAINT "balance_pkey" PRIMARY KEY ("b_id")
) WITH (oids = false);


DROP TABLE IF EXISTS "category";
DROP SEQUENCE IF EXISTS category_id_seq;
CREATE SEQUENCE category_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."category" (
    "c_id" integer DEFAULT nextval('category_id_seq') NOT NULL,
    "name" character varying(255) NOT NULL,
    CONSTRAINT "category_pkey" PRIMARY KEY ("c_id")
) WITH (oids = false);

INSERT INTO "category" ("c_id", "name") VALUES
(1,	'Leden');

DROP TABLE IF EXISTS "description";
DROP SEQUENCE IF EXISTS description_id_seq;
CREATE SEQUENCE description_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."description" (
    "d_id" integer DEFAULT nextval('description_id_seq') NOT NULL,
    "return_reason" character varying(255),
    "client_reference" character varying(255),
    "end_to_end_reference" character varying(255),
    "payment_information_id" character varying(255),
    "instruction_id" character varying(255),
    "mandate_reference" character varying(255),
    "creditor_id" character varying(255),
    "counterparty_id" character varying(255),
    "remittance_information" character varying(255),
    "purpose_code" character varying(255),
    "ultimate_creditor" character varying(255),
    "ultimate_debtor" character varying(255),
    "exchange_rate" character varying(255),
    "charges" character varying(255),
    CONSTRAINT "description_pkey" PRIMARY KEY ("d_id")
) WITH (oids = false);

INSERT INTO "description" ("d_id", "return_reason", "client_reference", "end_to_end_reference", "payment_information_id", "instruction_id", "mandate_reference", "creditor_id", "counterparty_id", "remittance_information", "purpose_code", "ultimate_creditor", "ultimate_debtor", "exchange_rate", "charges") VALUES
(1,	NULL,	'1',	NULL,	'1',	NULL,	'1',	'1',	'1',	NULL,	'23',	NULL,	'43t5',	'643',	NULL),
(0,	'een reden',	'een reference',	'nog een reference',	'payment information id',	'instrcuties',	'nog een reference dit keer mandate',	'id van creditor',	'id van counterparyt',	'informatie over remittance',	'code met purpose',	'de ultimate creditor',	'de ultimate debtor',	'de exvhange rate',	'de charges');

DROP TABLE IF EXISTS "file";
DROP SEQUENCE IF EXISTS file_id_seq;
CREATE SEQUENCE file_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."file" (
    "f_id" integer DEFAULT nextval('file_id_seq') NOT NULL,
    "transaction_reference_number" character varying(16) NOT NULL,
    "account_number" character varying(35) NOT NULL,
    "statement_number" integer NOT NULL,
    "file_description_id" integer NOT NULL,
    "last_updated_user" integer NOT NULL,
    "upload_date" date NOT NULL,
    "filecol" character varying(45) NOT NULL,
    CONSTRAINT "file_pkey" PRIMARY KEY ("f_id")
) WITH (oids = false);

INSERT INTO "file" ("f_id", "transaction_reference_number", "account_number", "statement_number", "file_description_id", "last_updated_user", "upload_date", "filecol") VALUES
(1,	'1',	'1',	1,	1,	1,	'2023-03-27',	'1');

DROP TABLE IF EXISTS "file_description";
DROP SEQUENCE IF EXISTS file_description_id_seq;
CREATE SEQUENCE file_description_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."file_description" (
    "f_d_id" integer DEFAULT nextval('file_description_id_seq') NOT NULL,
    "number_of_debit_entries" integer NOT NULL,
    "number_of_credit_entries" integer NOT NULL,
    "amount_of_debit_entries" money NOT NULL,
    "amount_of_credit_entries" money NOT NULL,
    CONSTRAINT "file_description_pkey" PRIMARY KEY ("f_d_id")
) WITH (oids = false);

INSERT INTO "file_description" ("f_d_id", "number_of_debit_entries", "number_of_credit_entries", "amount_of_debit_entries", "amount_of_credit_entries") VALUES
(1,	1,	1,	'$1.00',	'$1.00');

DROP TABLE IF EXISTS "role";
DROP SEQUENCE IF EXISTS role_id_seq;
CREATE SEQUENCE role_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."role" (
    "r_id" integer DEFAULT nextval('role_id_seq') NOT NULL,
    "name" character varying(255) NOT NULL,
    CONSTRAINT "role_pkey" PRIMARY KEY ("r_id")
) WITH (oids = false);

INSERT INTO "role" ("r_id", "name") VALUES
(1,	'Penningmeester');

DROP TABLE IF EXISTS "transaction";
DROP SEQUENCE IF EXISTS transaction_id_seq;
CREATE SEQUENCE transaction_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."transaction" (
    "t_id" integer DEFAULT nextval('transaction_id_seq') NOT NULL,
    "value_date" date NOT NULL,
    "entry_date" character varying(4) NOT NULL,
    "debit_credit" character(1) NOT NULL,
    "amount" money NOT NULL,
    "transaction_code" character varying(4) NOT NULL,
    "reference_owner" character varying(16),
    "institution_reference" character varying(16),
    "supplementary_details" character varying(34),
    "original_description_id" integer NOT NULL,
    "description" character varying(500),
    "file_id" integer NOT NULL,
    "category_id" integer,
    CONSTRAINT "transaction_pkey" PRIMARY KEY ("t_id")
) WITH (oids = false);

INSERT INTO "transaction" ("t_id", "value_date", "entry_date", "debit_credit", "amount", "transaction_code", "reference_owner", "institution_reference", "supplementary_details", "original_description_id", "description", "file_id", "category_id") VALUES
(1,	'2020-05-22',	'0522',	'c',	'$254.00',	'fds',	'hgfd',	'gfd',	'gfd',	1,	'Dit gaat een heel verhaal zijn, om te testen of de beschrijving laten zien op de frontend het doet.',	1,	1),
(2,	'2023-03-27',	'2703',	'd',	'$123.00',	'259',	'gafd',	'gds',	'hbgfds',	1,	'gfdshbvfdfrghnbv',	1,	1),
(0,	'2023-03-30',	'8274',	'd',	'$541.00',	'5',	'een referecne',	'34',	'details',	0,	'Dit is een hele goede beschirivjing',	1,	1);

DROP VIEW IF EXISTS "transactionsview";
CREATE TABLE "transactionsview" ("t_id" integer, "value_date" date, "entry_date" character varying(4), "debit_credit" character(1), "amount" money, "transaction_code" character varying(4), "reference_owner" character varying(16), "institution_reference" character varying(16), "supplementary_details" character varying(34), "original_description_id" integer, "description" character varying(500), "file_id" integer, "category_id" integer, "c_id" integer, "name" character varying(255), "d_id" integer, "return_reason" character varying(255), "client_reference" character varying(255), "end_to_end_reference" character varying(255), "payment_information_id" character varying(255), "instruction_id" character varying(255), "mandate_reference" character varying(255), "creditor_id" character varying(255), "counterparty_id" character varying(255), "remittance_information" character varying(255), "purpose_code" character varying(255), "ultimate_creditor" character varying(255), "ultimate_debtor" character varying(255), "exchange_rate" character varying(255), "charges" character varying(255));


DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."user" (
    "u_id" integer DEFAULT nextval('user_id_seq') NOT NULL,
    "email" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "role_id" integer NOT NULL,
    "username" character varying(255) NOT NULL,
    CONSTRAINT "user_pkey" PRIMARY KEY ("u_id")
) WITH (oids = false);

INSERT INTO "user" ("u_id", "email", "password", "role_id", "username") VALUES
(1,	'test@test.com',	'Test123',	1,	'test');

ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_file_description_id_fkey" FOREIGN KEY (file_description_id) REFERENCES file_description(f_d_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_last_updated_user_fkey" FOREIGN KEY (last_updated_user) REFERENCES "user"(u_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_category_id_fkey" FOREIGN KEY (category_id) REFERENCES category(c_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_file_id_fkey" FOREIGN KEY (file_id) REFERENCES file(f_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_original_description_id_fkey" FOREIGN KEY (original_description_id) REFERENCES description(d_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

ALTER TABLE ONLY "public"."user" ADD CONSTRAINT "user_role_id_fkey" FOREIGN KEY (role_id) REFERENCES role(r_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

DROP TABLE IF EXISTS "transactionsview";
CREATE VIEW "transactionsview" AS SELECT transaction.t_id,
    transaction.value_date,
    transaction.entry_date,
    transaction.debit_credit,
    transaction.amount,
    transaction.transaction_code,
    transaction.reference_owner,
    transaction.institution_reference,
    transaction.supplementary_details,
    transaction.original_description_id,
    transaction.description,
    transaction.file_id,
    transaction.category_id,
    category.c_id,
    category.name,
    description.d_id,
    description.return_reason,
    description.client_reference,
    description.end_to_end_reference,
    description.payment_information_id,
    description.instruction_id,
    description.mandate_reference,
    description.creditor_id,
    description.counterparty_id,
    description.remittance_information,
    description.purpose_code,
    description.ultimate_creditor,
    description.ultimate_debtor,
    description.exchange_rate,
    description.charges
   FROM ((transaction
     JOIN category ON ((transaction.category_id = category.c_id)))
     JOIN description ON ((transaction.original_description_id = description.d_id)));

-- 2023-04-04 13:15:20.913557+00
