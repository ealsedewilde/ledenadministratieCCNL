;             
CREATE USER IF NOT EXISTS "SA" PASSWORD '' ADMIN;             
DROP TABLE IF EXISTS "PUBLIC"."EXTERNAL_RELATION" CASCADE;    
DROP TABLE IF EXISTS "PUBLIC"."DOCUMENT" CASCADE;             
DROP TABLE IF EXISTS "PUBLIC"."DOCUMENT_TEMPLATE" CASCADE;    
DROP TABLE IF EXISTS "PUBLIC"."INTERNAL_RELATION" CASCADE;    
DROP TABLE IF EXISTS "PUBLIC"."MEMBER" CASCADE;               
DROP TABLE IF EXISTS "PUBLIC"."PAYMENT_FILE" CASCADE;         
DROP TABLE IF EXISTS "PUBLIC"."SETTING" CASCADE;              
DROP TABLE IF EXISTS "PUBLIC"."DIRECT_DEBIT_CONFIG" CASCADE;  
DROP TABLE IF EXISTS "PUBLIC"."ARCHIVED_MEMBER" CASCADE;      
DROP SEQUENCE IF EXISTS "PUBLIC"."HIBERNATE_SEQUENCE";        
CREATE SEQUENCE "PUBLIC"."HIBERNATE_SEQUENCE" START WITH 172; 
CREATE CACHED TABLE "PUBLIC"."EXTERNAL_RELATION"(
    "RELATION_TYPE" VARCHAR(31) NOT NULL,
    "RELATION_NUMBER" INTEGER NOT NULL,
    "ADDRESS" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER_APPENDIX" VARCHAR(255),
    "CITY" VARCHAR(255) NOT NULL,
    "COUNTRY" VARCHAR(255),
    "ADDRESS_INVALID" BOOLEAN NOT NULL,
    "POSTAL_CODE" VARCHAR(255),
    "CONTACT_NAME" VARCHAR(255) NOT NULL,
    "CONTACT_NAME_PREFIX" VARCHAR(255),
    "EMAIL" VARCHAR(255),
    "MODIFICATION_DATE" DATE,
    "RELATION_INFO" CLOB,
    "RELATION_NAME" VARCHAR(255),
    "RELATION_SINCE" DATE,
    "TELEPHONE_NUMBER" VARCHAR(255)
);              
ALTER TABLE "PUBLIC"."EXTERNAL_RELATION" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_C9" PRIMARY KEY("RELATION_NUMBER");              
-- 29 +/- SELECT COUNT(*) FROM PUBLIC.EXTERNAL_RELATION;      
CREATE CACHED TABLE "PUBLIC"."DOCUMENT"(
    "ID" INTEGER NOT NULL,
    "CREATION_DATE" DATE NOT NULL,
    "DESCRIPTION" VARCHAR(255),
    "DOCUMENT_NAME" VARCHAR(255) NOT NULL,
    "DOCUMENT_TYPE" VARCHAR(255) NOT NULL,
	"PDF" BLOB NOT NULL,
    "OWNER_ID" INTEGER
);             
ALTER TABLE "PUBLIC"."DOCUMENT" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6" PRIMARY KEY("ID");     
-- 20 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT;               
CREATE CACHED TABLE "PUBLIC"."DOCUMENT_TEMPLATE"(
    "DOCUMENT_TEMPLATE_TYPE" VARCHAR(255) NOT NULL,
    "NAME" VARCHAR(255) NOT NULL,
    "INCLUDE_SEPA_FORM" BOOLEAN NOT NULL,
    "MODIFICATION_DATE" DATE NOT NULL,
    "TEMPLATE" CLOB NOT NULL
);
ALTER TABLE "PUBLIC"."DOCUMENT_TEMPLATE" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_C" PRIMARY KEY("DOCUMENT_TEMPLATE_TYPE", "NAME");
-- 3 +/- SELECT COUNT(*) FROM PUBLIC.DOCUMENT_TEMPLATE;       
CREATE CACHED TABLE "PUBLIC"."INTERNAL_RELATION"(
    "RELATION_NUMBER" INTEGER NOT NULL,
    "ADDRESS" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER_APPENDIX" VARCHAR(255),
    "CITY" VARCHAR(255) NOT NULL,
    "COUNTRY" VARCHAR(255),
    "ADDRESS_INVALID" BOOLEAN NOT NULL,
    "POSTAL_CODE" VARCHAR(255),
    "CONTACT_NAME" VARCHAR(255),
    "MODIFICATION_DATE" DATE,
    "TELEPHONE_NUMBER" VARCHAR(255),
    "TITLE" VARCHAR(255) NOT NULL
);            
ALTER TABLE "PUBLIC"."INTERNAL_RELATION" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_3" PRIMARY KEY("RELATION_NUMBER");               
-- 8 +/- SELECT COUNT(*) FROM PUBLIC.INTERNAL_RELATION;       
CREATE CACHED TABLE "PUBLIC"."MEMBER"(
    "MEMBER_NUMBER" INTEGER NOT NULL,
    "ADDRESS" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER_APPENDIX" VARCHAR(255),
    "CITY" VARCHAR(255) NOT NULL,
    "COUNTRY" VARCHAR(255),
    "ADDRESS_INVALID" BOOLEAN NOT NULL,
    "POSTAL_CODE" VARCHAR(255),
    "CURRENT_YEAR_PAID" BOOLEAN NOT NULL,
    "EMAIL" VARCHAR(255),
    "IBAN_NUMBER" VARCHAR(255),
    "INITIALS" VARCHAR(255) NOT NULL,
    "LAST_NAME" VARCHAR(255) NOT NULL,
    "LAST_NAME_PREFIX" VARCHAR(255),
    "MEMBER_INFO" CLOB,
    "MEMBER_SINCE" DATE,
    "MEMBER_STATUS" VARCHAR(255),
    "MEMBERCARD_ISSUED" BOOLEAN NOT NULL,
    "MODIFICATION_DATE" DATE,
    "NO_MAGAZINE" BOOLEAN NOT NULL,
    "PAYMENT_DATE" DATE,
    "PAYMENT_INFO" CLOB,
    "PAYMENT_METHOD" VARCHAR(255),
    "TELEPHONE_NUMBER" VARCHAR(255)
);     
ALTER TABLE "PUBLIC"."MEMBER" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_87" PRIMARY KEY("MEMBER_NUMBER");           
-- 676 +/- SELECT COUNT(*) FROM PUBLIC.MEMBER;
CREATE CACHED TABLE "PUBLIC"."PAYMENT_FILE"(
    "FILE_NAME" VARCHAR(255) NOT NULL,
    "XML" CLOB
);      
ALTER TABLE "PUBLIC"."PAYMENT_FILE" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_6E" PRIMARY KEY("FILE_NAME");         
-- 0 +/- SELECT COUNT(*) FROM PUBLIC.PAYMENT_FILE;            
CREATE CACHED TABLE "PUBLIC"."SETTING"(
    "SETTINGS_KEY" VARCHAR(255) NOT NULL,
    "DESCRIPTION" VARCHAR(255),
    "SETTINGS_GROUP" VARCHAR(255),
    "SETTINGS_VALUE" VARCHAR(255) NOT NULL,
    "ID" VARCHAR(255) NOT NULL
);      
-- 13 +/- SELECT COUNT(*) FROM PUBLIC.SETTING;
CREATE CACHED TABLE "PUBLIC"."DIRECT_DEBIT_CONFIG"(
    "ID" INTEGER NOT NULL,
    "AUTH_DESCRIPTION" VARCHAR(255),
    "AUTH_VALUE" VARCHAR(255),
    "AUTH_TYPE_DESCRIPTION" VARCHAR(255),
    "AUTH_TYPE_VALUE" VARCHAR(255),
    "IBAN_NAME_DESCRIPTION" VARCHAR(255),
    "IBAN_NAME_VALUE" VARCHAR(255),
    "DD_AMOUNT_DESCRIPTION" VARCHAR(255),
    "DD_AMOUNT_VALUE" DECIMAL(19, 2),
    "DD_DATE_DESCRIPTION" VARCHAR(255),
    "DD_DATE_VALUE" DATE,
    "DD_REASON_DESCRIPTION" VARCHAR(255),
    "DD_REASON_VALUE" VARCHAR(255),
    "DD_DIR_DESCRIPTION" VARCHAR(255),
    "DD_DIR_VALUE" VARCHAR(255),
    "DD_ID_DESCRIPTION" VARCHAR(255),
    "DD_ID_VALUE" VARCHAR(255),
    "IBAN_NUMBER_DESCRIPTION" VARCHAR(255),
    "IBAN_NUMBER_VALUE" VARCHAR(255),
    "MSG_ID_DESCRIPTION" VARCHAR(255),
    "MSSG_ID_VALUE" VARCHAR(255),
    "TESTRUN_DESCRIPTION" VARCHAR(255),
    "TESTRUN_VALUE" BOOLEAN
);        
ALTER TABLE "PUBLIC"."DIRECT_DEBIT_CONFIG" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_1" PRIMARY KEY("ID");          
-- 1 +/- SELECT COUNT(*) FROM PUBLIC.DIRECT_DEBIT_CONFIG;     
CREATE CACHED TABLE "PUBLIC"."ARCHIVED_MEMBER"(
    "ARCHIVE_YEAR" INTEGER NOT NULL,
    "MEMBER_NUMBER" INTEGER NOT NULL,
    "ADDRESS" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER" VARCHAR(255) NOT NULL,
    "ADDRESS_NUMBER_APPENDIX" VARCHAR(255),
    "CITY" VARCHAR(255) NOT NULL,
    "COUNTRY" VARCHAR(255),
    "POSTAL_CODE" VARCHAR(255),
    "CURRENT_YEAR_PAID" BOOLEAN NOT NULL,
    "EMAIL" VARCHAR(255),
    "IBAN_NUMBER" VARCHAR(255),
    "INITIALS" VARCHAR(255) NOT NULL,
    "LAST_NAME" VARCHAR(255) NOT NULL,
    "LAST_NAME_PREFIX" VARCHAR(255),
    "MEMBER_INFO" CLOB,
    "MEMBER_SINCE" DATE,
    "MEMBER_STATUS" VARCHAR(255),
    "MEMBERCARD_ISSUED" BOOLEAN NOT NULL,
    "MODIFICATION_DATE" DATE,
    "NO_MAGAZINE" BOOLEAN NOT NULL,
    "PAYMENT_DATE" DATE,
    "PAYMENT_INFO" CLOB,
    "PAYMENT_METHOD" VARCHAR(255),
    "TELEPHONE_NUMBER" VARCHAR(255),
    "ADDRESS_INVALID" BOOLEAN NOT NULL
);      
ALTER TABLE "PUBLIC"."ARCHIVED_MEMBER" ADD CONSTRAINT "PUBLIC"."CONSTRAINT_F" PRIMARY KEY("ARCHIVE_YEAR", "MEMBER_NUMBER");   
-- 48 +/- SELECT COUNT(*) FROM PUBLIC.ARCHIVED_MEMBER;        
ALTER TABLE "PUBLIC"."INTERNAL_RELATION" ADD CONSTRAINT "PUBLIC"."UK_OAQ592PQXQ05ROQHES5TU5YND" UNIQUE("TITLE");              
ALTER TABLE "PUBLIC"."DOCUMENT" ADD CONSTRAINT "PUBLIC"."FK7UH6E2LXBP5R6ECH54JE7IXL1" FOREIGN KEY("OWNER_ID") REFERENCES "PUBLIC"."MEMBER"("MEMBER_NUMBER") NOCHECK;          

INSERT INTO "PUBLIC"."SETTING" VALUES('from', 'Afzender van mails', 'ccnl.mail', 'ledenadministratie@citroenclubnederland.nl', 'ccnl.mail.from');           
INSERT INTO "PUBLIC"."SETTING" VALUES('subject', '', 'ccnl.mail', STRINGDECODE('Opzegging lidmaatschap Citro\u00ebn Club Nederland'), 'ccnl.mail.subject'); 
INSERT INTO "PUBLIC"."SETTING" VALUES('host', '', 'spring.mail', 'server60.hosting2go.nl', 'spring.mail.host');               
INSERT INTO "PUBLIC"."SETTING" VALUES('port', '', 'spring.mail', '587', 'spring.mail.port');  
INSERT INTO "PUBLIC"."SETTING" VALUES('username', '', 'spring.mail', 'ledenadministratie@citroenclubnederland.nl', 'spring.mail.username');   
INSERT INTO "PUBLIC"."SETTING" VALUES('password', '', 'spring.mail', '**********', 'spring.mail.password');  
INSERT INTO "PUBLIC"."SETTING" VALUES('mail.smtp.starttls.enable', '', 'spring.mail.properties', 'true', 'spring.mail.properties.mail.smtp.starttls.enable'); 
INSERT INTO "PUBLIC"."SETTING" VALUES('mail.smtp.auth', '', 'spring.mail.properties', 'true', 'spring.mail.properties.mail.smtp.auth');       
INSERT INTO "PUBLIC"."SETTING" VALUES('number', 'Volgnummer clubblad', 'ccnl.magazine', '366', 'ccnl.magazine.number');       
INSERT INTO "PUBLIC"."SETTING" VALUES('db', 'Database Backup-/Restore directory', 'ccnl.directory', 'C:/temp', 'ccnl.directory.db');          
INSERT INTO "PUBLIC"."SETTING" VALUES('sepa', 'Basis directory voor SEPA formulieren', 'ccnl.directory', 'C:/Users/SKIKK/OneDrive/prive/CCNL_prive/ledenadministratie', 'ccnl.directory.sepa');               
INSERT INTO "PUBLIC"."SETTING" VALUES('excel', 'Directory voor Excel import en export', 'ccnl.directory', 'C:/temp', 'ccnl.directory.excel'); 
INSERT INTO "PUBLIC"."SETTING" VALUES('magazine', 'Directory voor adresbestanden van het clubblad', 'ccnl.directory', 'C:/temp', 'ccnl.directory.magazine');  
