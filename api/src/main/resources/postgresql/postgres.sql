-- Adminer 4.8.1 PostgreSQL 15.1 dump

DROP TABLE IF EXISTS "balance";
DROP SEQUENCE IF EXISTS balance_id_seq;
CREATE SEQUENCE balance_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."balance" (
    "b_id" integer DEFAULT nextval('balance_id_seq') NOT NULL,
    "b_debit_credit" character(1) NOT NULL,
    "b_date" date NOT NULL,
    "b_currency" character varying(3) NOT NULL,
    "b_amount" numeric NOT NULL,
    "b_type" character varying(25) NOT NULL,
    "b_file_id" integer NOT NULL,
    CONSTRAINT "balance_pkey" PRIMARY KEY ("b_id")
) WITH (oids = false);

INSERT INTO "balance" ("b_id", "b_debit_credit", "b_date", "b_currency", "b_amount", "b_type", "b_file_id") VALUES
(1,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	2),
(2,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	2),
(3,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	2),
(4,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	2),
(5,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	3),
(6,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	3),
(7,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	3),
(8,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	3),
(9,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	4),
(10,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	4),
(11,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	4),
(12,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	4),
(13,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	5),
(14,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	5),
(15,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	5),
(16,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	5),
(17,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	6),
(18,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	6),
(19,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	6),
(20,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	6),
(21,	'C',	'2014-02-20',	'EUR',	564.35,	'closingAvailableBalance',	7),
(22,	'C',	'2014-02-19',	'EUR',	662.23,	'openingBalance',	7),
(23,	'C',	'2014-02-21',	'EUR',	564.35,	'forwardAvailableBalance',	7),
(24,	'C',	'2014-02-24',	'EUR',	564.35,	'forwardAvailableBalance',	7);

DROP VIEW IF EXISTS "bankstatementsview";
CREATE TABLE "bankstatementsview" ("f_id" integer, "f_transaction_reference_number" character varying(16), "f_account_number" character varying(35), "f_statement_number" integer, "f_file_description_id" integer, "f_last_updated_user" integer, "f_upload_date" date, "f_d_id" integer, "f_d_number_of_debit_entries" integer, "f_d_number_of_credit_entries" integer, "f_d_amount_of_debit_entries" numeric, "f_d_amount_of_credit_entries" numeric, "u_id" integer, "u_email" character varying(255), "u_role_id" integer, "u_username" character varying(255), "b_id" integer, "b_debit_credit" character(1), "b_date" date, "b_currency" character varying(3), "b_amount" numeric, "b_type" character varying(25), "b_file_id" integer);


DROP TABLE IF EXISTS "category";
DROP SEQUENCE IF EXISTS category_id_seq;
CREATE SEQUENCE category_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."category" (
    "c_id" integer DEFAULT nextval('category_id_seq') NOT NULL,
    "c_name" character varying(255) NOT NULL,
    CONSTRAINT "category_pkey" PRIMARY KEY ("c_id")
) WITH (oids = false);

INSERT INTO "category" ("c_id", "c_name") VALUES
(1,	'leden');

DROP TABLE IF EXISTS "description";
DROP SEQUENCE IF EXISTS description_id_seq;
CREATE SEQUENCE description_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."description" (
    "d_id" integer DEFAULT nextval('description_id_seq') NOT NULL,
    "d_return_reason" character varying(255),
    "d_client_reference" character varying(255),
    "d_end_to_end_reference" character varying(255),
    "d_payment_information_id" character varying(255),
    "d_instruction_id" character varying(255),
    "d_mandate_reference" character varying(255),
    "d_creditor_id" character varying(255),
    "d_counterparty_id" character varying(255),
    "d_remittance_information" character varying(255),
    "d_purpose_code" character varying(255),
    "d_ultimate_creditor" character varying(255),
    "d_ultimate_debtor" character varying(255),
    "d_exchange_rate" character varying(255),
    "d_charges" character varying(255),
    CONSTRAINT "description_pkey" PRIMARY KEY ("d_id")
) WITH (oids = false);

INSERT INTO "description" ("d_id", "d_return_reason", "d_client_reference", "d_end_to_end_reference", "d_payment_information_id", "d_instruction_id", "d_mandate_reference", "d_creditor_id", "d_counterparty_id", "d_remittance_information", "d_purpose_code", "d_ultimate_creditor", "d_ultimate_debtor", "d_exchange_rate", "d_charges") VALUES
(1,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(2,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(3,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(4,	'',	'',	'',	'M000000003333333',	'',	'',	'',	'',	'USTD//TOTAAL 1 VZ',	'',	'',	'',	'',	''),
(5,	'MS03',	'',	'20120123456789',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'USTD//Factuurnr 123456 Klantnr 00123',	'',	'',	'',	'',	''),
(6,	'',	'',	'EV123REP123412T1234',	'',	'',	'MND-EV01',	'NL32ZZZ999999991234',	'NL32INGB0000012345/INGBNL2A/ING Bank N.V. inzake WeB',	'USTD//EV123REP123412T1234',	'',	'',	'',	'',	''),
(7,	'',	'',	'',	'M000000001111111',	'',	'',	'NL32ZZZ999999991234',	'',	'USTD//TOTAAL       1 POSTEN',	'',	'',	'',	'',	''),
(8,	'MS03',	'',	'20120501P0123478',	'',	'',	'MND-120123',	'NL32ZZZ999999991234',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'USTD//CONTRIBUTIE FEB 2014',	'',	'',	'',	'',	''),
(9,	'',	'',	'15814016000676480',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'STRD/CUR/9001123412341234',	'',	'',	'',	'',	''),
(10,	'',	'',	'15614016000384600',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/INGBANK NV',	'STRD/CUR/1070123412341234',	'',	'',	'',	'',	''),
(11,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(12,	'',	'',	'',	'M000000003333333',	'',	'',	'',	'',	'USTD//TOTAAL 1 VZ',	'',	'',	'',	'',	''),
(13,	'MS03',	'',	'20120123456789',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'USTD//Factuurnr 123456 Klantnr 00123',	'',	'',	'',	'',	''),
(14,	'',	'',	'EV123REP123412T1234',	'',	'',	'MND-EV01',	'NL32ZZZ999999991234',	'NL32INGB0000012345/INGBNL2A/ING Bank N.V. inzake WeB',	'USTD//EV123REP123412T1234',	'',	'',	'',	'',	''),
(15,	'',	'',	'',	'M000000001111111',	'',	'',	'NL32ZZZ999999991234',	'',	'USTD//TOTAAL       1 POSTEN',	'',	'',	'',	'',	''),
(16,	'MS03',	'',	'20120501P0123478',	'',	'',	'MND-120123',	'NL32ZZZ999999991234',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'USTD//CONTRIBUTIE FEB 2014',	'',	'',	'',	'',	''),
(17,	'',	'',	'15814016000676480',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/J.Janssen',	'STRD/CUR/9001123412341234',	'',	'',	'',	'',	''),
(18,	'',	'',	'15614016000384600',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/INGBANK NV',	'STRD/CUR/1070123412341234',	'',	'',	'',	'',	''),
(19,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(20,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(21,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(22,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(23,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(24,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(25,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(26,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(27,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(28,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(29,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(30,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(31,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(32,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(33,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	''),
(34,	'',	'',	'EV12341REP1231456T1234',	'',	'',	'',	'',	'NL32INGB0000012345/INGBNL2A/ING BANK NV INZAKE WEB',	'USTD//EV10001REP1000000T1000',	'',	'',	'',	'',	'');

DROP TABLE IF EXISTS "file";
DROP SEQUENCE IF EXISTS file_id_seq;
CREATE SEQUENCE file_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."file" (
    "f_id" integer DEFAULT nextval('file_id_seq') NOT NULL,
    "f_transaction_reference_number" character varying(16) NOT NULL,
    "f_account_number" character varying(35) NOT NULL,
    "f_statement_number" integer NOT NULL,
    "f_file_description_id" integer NOT NULL,
    "f_last_updated_user" integer NOT NULL,
    "f_upload_date" date NOT NULL,
    CONSTRAINT "file_pkey" PRIMARY KEY ("f_id")
) WITH (oids = false);

INSERT INTO "file" ("f_id", "f_transaction_reference_number", "f_account_number", "f_statement_number", "f_file_description_id", "f_last_updated_user", "f_upload_date") VALUES
(2,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	2,	1,	'2300-10-23'),
(3,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	3,	1,	'2300-10-23'),
(4,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	4,	1,	'2300-10-23'),
(5,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	5,	1,	'2300-10-23'),
(6,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	6,	1,	'2023-04-07'),
(7,	'P140220000000001',	'NL69INGB0123456789EUR',	0,	7,	1,	'2023-04-07');

DROP TABLE IF EXISTS "file_description";
DROP SEQUENCE IF EXISTS file_description_id_seq;
CREATE SEQUENCE file_description_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."file_description" (
    "f_d_id" integer DEFAULT nextval('file_description_id_seq') NOT NULL,
    "f_d_number_of_debit_entries" integer NOT NULL,
    "f_d_number_of_credit_entries" integer NOT NULL,
    "f_d_amount_of_debit_entries" numeric NOT NULL,
    "f_d_amount_of_credit_entries" numeric NOT NULL,
    CONSTRAINT "file_description_pkey" PRIMARY KEY ("f_d_id")
) WITH (oids = false);

INSERT INTO "file_description" ("f_d_id", "f_d_number_of_debit_entries", "f_d_number_of_credit_entries", "f_d_amount_of_debit_entries", "f_d_amount_of_credit_entries") VALUES
(1,	4,	4,	134.46,	36.58),
(2,	4,	4,	134.46,	36.58),
(3,	4,	4,	134.46,	36.58),
(4,	4,	4,	134.46,	36.58),
(5,	4,	4,	134.46,	36.58),
(6,	4,	4,	134.46,	36.58),
(7,	4,	4,	134.46,	36.58);

DROP TABLE IF EXISTS "role";
DROP SEQUENCE IF EXISTS role_id_seq;
CREATE SEQUENCE role_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."role" (
    "r_id" integer DEFAULT nextval('role_id_seq') NOT NULL,
    "r_name" character varying(255) NOT NULL,
    CONSTRAINT "role_pkey" PRIMARY KEY ("r_id")
) WITH (oids = false);

INSERT INTO "role" ("r_id", "r_name") VALUES
(1,	'penningmeester');

DROP TABLE IF EXISTS "transaction";
DROP SEQUENCE IF EXISTS transaction_id_seq;
CREATE SEQUENCE transaction_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."transaction" (
    "t_id" integer DEFAULT nextval('transaction_id_seq') NOT NULL,
    "t_value_date" date NOT NULL,
    "t_entry_date" character varying(4) NOT NULL,
    "t_debit_credit" character(1) NOT NULL,
    "t_amount" numeric NOT NULL,
    "t_transaction_code" character varying(4) NOT NULL,
    "t_reference_owner" character varying(16),
    "t_institution_reference" character varying(16),
    "t_supplementary_details" character varying(34),
    "t_original_description_id" integer NOT NULL,
    "t_description" character varying(500),
    "t_file_id" integer NOT NULL,
    "t_category_id" integer,
    CONSTRAINT "transaction_pkey" PRIMARY KEY ("t_id")
) WITH (oids = false);

INSERT INTO "transaction" ("t_id", "t_value_date", "t_entry_date", "t_debit_credit", "t_amount", "t_transaction_code", "t_reference_owner", "t_institution_reference", "t_supplementary_details", "t_original_description_id", "t_description", "t_file_id", "t_category_id") VALUES
(27,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	27,	'',	7,	1),
(28,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	28,	'',	7,	1),
(29,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	29,	'',	7,	1),
(30,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	30,	'',	7,	1),
(31,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	31,	'',	7,	1),
(32,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	32,	'',	7,	1),
(33,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	33,	'',	7,	1),
(34,	'2014-02-20',	'0220',	'C',	1.56,	'TRF',	'EREF',	'00000000001005',	'/TRCD/00100/',	34,	'',	7,	1);

DROP VIEW IF EXISTS "transactionsview";
CREATE TABLE "transactionsview" ("t_id" integer, "t_value_date" date, "t_entry_date" character varying(4), "t_debit_credit" character(1), "t_amount" numeric, "t_transaction_code" character varying(4), "t_reference_owner" character varying(16), "t_institution_reference" character varying(16), "t_supplementary_details" character varying(34), "t_original_description_id" integer, "t_description" character varying(500), "t_file_id" integer, "t_category_id" integer, "d_id" integer, "d_return_reason" character varying(255), "d_client_reference" character varying(255), "d_end_to_end_reference" character varying(255), "d_payment_information_id" character varying(255), "d_instruction_id" character varying(255), "d_mandate_reference" character varying(255), "d_creditor_id" character varying(255), "d_counterparty_id" character varying(255), "d_remittance_information" character varying(255), "d_purpose_code" character varying(255), "d_ultimate_creditor" character varying(255), "d_ultimate_debtor" character varying(255), "d_exchange_rate" character varying(255), "d_charges" character varying(255), "c_id" integer, "c_name" character varying(255));


DROP TABLE IF EXISTS "user";
DROP SEQUENCE IF EXISTS user_id_seq;
CREATE SEQUENCE user_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1;

CREATE TABLE "public"."user" (
    "u_id" integer DEFAULT nextval('user_id_seq') NOT NULL,
    "u_email" character varying(255) NOT NULL,
    "u_password" character varying(255) NOT NULL,
    "u_role_id" integer NOT NULL,
    "u_username" character varying(255) NOT NULL,
    CONSTRAINT "user_pkey" PRIMARY KEY ("u_id")
) WITH (oids = false);

INSERT INTO "user" ("u_id", "u_email", "u_password", "u_role_id", "u_username") VALUES
(1,	'test@gmail.com',	'changeme',	1,	'testUser');

ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_file_description_id_fkey" FOREIGN KEY (f_file_description_id) REFERENCES file_description(f_d_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."file" ADD CONSTRAINT "file_last_updated_user_fkey" FOREIGN KEY (f_last_updated_user) REFERENCES "user"(u_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_category_id_fkey" FOREIGN KEY (t_category_id) REFERENCES category(c_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_file_id_fkey" FOREIGN KEY (t_file_id) REFERENCES file(f_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;
ALTER TABLE ONLY "public"."transaction" ADD CONSTRAINT "transaction_original_description_id_fkey" FOREIGN KEY (t_original_description_id) REFERENCES description(d_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

ALTER TABLE ONLY "public"."user" ADD CONSTRAINT "user_role_id_fkey" FOREIGN KEY (u_role_id) REFERENCES role(r_id) ON UPDATE CASCADE ON DELETE CASCADE NOT DEFERRABLE;

DROP TABLE IF EXISTS "bankstatementsview";
CREATE VIEW "bankstatementsview" AS SELECT file.f_id,
    file.f_transaction_reference_number,
    file.f_account_number,
    file.f_statement_number,
    file.f_file_description_id,
    file.f_last_updated_user,
    file.f_upload_date,
    file_description.f_d_id,
    file_description.f_d_number_of_debit_entries,
    file_description.f_d_number_of_credit_entries,
    file_description.f_d_amount_of_debit_entries,
    file_description.f_d_amount_of_credit_entries,
    "user".u_id,
    "user".u_email,
    "user".u_role_id,
    "user".u_username,
    balance.b_id,
    balance.b_debit_credit,
    balance.b_date,
    balance.b_currency,
    balance.b_amount,
    balance.b_type,
    balance.b_file_id
   FROM (((file
     JOIN file_description ON ((file.f_file_description_id = file_description.f_d_id)))
     JOIN "user" ON ((file.f_last_updated_user = "user".u_id)))
     JOIN balance ON ((file.f_id = balance.b_file_id)))
  WHERE ((balance.b_type)::text = 'closingBalance'::text);

DROP TABLE IF EXISTS "transactionsview";
CREATE VIEW "transactionsview" AS SELECT transaction.t_id,
    transaction.t_value_date,
    transaction.t_entry_date,
    transaction.t_debit_credit,
    transaction.t_amount,
    transaction.t_transaction_code,
    transaction.t_reference_owner,
    transaction.t_institution_reference,
    transaction.t_supplementary_details,
    transaction.t_original_description_id,
    transaction.t_description,
    transaction.t_file_id,
    transaction.t_category_id,
    description.d_id,
    description.d_return_reason,
    description.d_client_reference,
    description.d_end_to_end_reference,
    description.d_payment_information_id,
    description.d_instruction_id,
    description.d_mandate_reference,
    description.d_creditor_id,
    description.d_counterparty_id,
    description.d_remittance_information,
    description.d_purpose_code,
    description.d_ultimate_creditor,
    description.d_ultimate_debtor,
    description.d_exchange_rate,
    description.d_charges,
    category.c_id,
    category.c_name
   FROM ((transaction
     JOIN description ON ((transaction.t_original_description_id = description.d_id)))
     LEFT JOIN category ON ((transaction.t_category_id = category.c_id)));

-- 2023-04-07 08:15:37.776304+00
