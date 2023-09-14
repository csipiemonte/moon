--
--  SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
--
--  SPDX-License-Identifier: EUPL-1.2


SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;
SET search_path = moon, pg_catalog;

CREATE TABLE IF NOT EXISTS moon_io_d_tipocodiceistanza (
	id_tipo_codice_istanza SERIAL PRIMARY KEY,
	desc_codice VARCHAR (100) NOT NULL,
	descrizione_tipo  VARCHAR (200)
);

insert INTO moon_io_d_tipocodiceistanza (desc_codice, descrizione_tipo) values ( 'codice_modulo.versione.anno.prog', 'codice del modulo_versione_anno di creazione istanza_progressivo assoluto');
insert INTO moon_io_d_tipocodiceistanza (desc_codice, descrizione_tipo) values ( 'codice_modulo.versione.anno.prog_annuale', 'codice del modulo_versione_anno di creazione istanza_progressivo annuale');
insert INTO moon_io_d_tipocodiceistanza (desc_codice, descrizione_tipo) values ( 'codice_modulo.versione.str8', 'codice del modulo_versione_stringa alfanumerica di 8 caratteri');


CREATE TABLE IF NOT EXISTS moon_io_d_modulo (
	id_modulo SERIAL PRIMARY KEY,
	codice_modulo  VARCHAR (30) UNIQUE NOT NULL,
	oggetto_modulo VARCHAR (50) NOT NULL,
	descrizione_modulo TEXT,
	id_tipo_codice_istanza INT NOT NULL DEFAULT '1',
	flag_is_riservato CHAR(1) NOT NULL DEFAULT 'N',
	flag_protocollo_integrato CHAR(1) NOT NULL DEFAULT 'N',
	data_ins TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_upd TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_tipo_codice_istanza) REFERENCES moon_io_d_tipocodiceistanza (id_tipo_codice_istanza)
);

CREATE TABLE IF NOT EXISTS moon_io_d_versione_modulo (
	id_versione_modulo SERIAL PRIMARY KEY,
	id_modulo INT NOT NULL,
	versione_modulo VARCHAR(30) NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_io_d_statomodulo (
	id_stato INT PRIMARY KEY,
	codice_stato VARCHAR (5) UNIQUE NOT NULL,
	descrizione_stato VARCHAR (50),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_io_r_cronologia_statomodulo (
	id_cron SERIAL PRIMARY KEY,
	id_versione_modulo INT NOT NULL, 
	id_stato INT NOT NULL,
	data_inizio_validita TIMESTAMP,
	data_fine_validita TIMESTAMP,
	FOREIGN KEY (id_versione_modulo) REFERENCES moon_io_d_versione_modulo (id_versione_modulo),
	FOREIGN KEY (id_stato) REFERENCES moon_io_d_statomodulo (id_stato)
);

CREATE TABLE IF NOT EXISTS moon_io_d_modulostruttura (
	id_modulostruttura SERIAL PRIMARY KEY,
	id_modulo INT NOT NULL,
	id_versione_modulo INT NOT NULL,
	struttura TEXT,
	tipo_struttura VARCHAR (3),
	UNIQUE (id_modulo, id_versione_modulo),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_versione_modulo) REFERENCES moon_io_d_versione_modulo (id_versione_modulo)
);

COMMENT ON COLUMN moon_io_d_modulostruttura.tipo_struttura IS 'for=form, wiz=wizard ';


CREATE TABLE IF NOT EXISTS moon_io_d_stepcompilazione (
	id_stepcompilazione SERIAL PRIMARY KEY,
	id_modulostruttura INT UNIQUE NOT NULL,
	label_step_compilazione VARCHAR (50),
	ordine INT,
	FOREIGN KEY (id_modulostruttura) REFERENCES moon_io_d_modulostruttura (id_modulostruttura)
);

CREATE TABLE IF NOT EXISTS moon_io_d_quadro (
	id_quadro SERIAL PRIMARY KEY,
	nome_quadro VARCHAR (30) NOT NULL,
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

COMMENT ON COLUMN moon_io_d_quadro.fl_attivo IS 'S=attivo, N=non attivo ';

CREATE TABLE IF NOT EXISTS moon_io_r_modulo_quadri (
	id_modulo INT NOT NULL,
	id_quadro INT NOT NULL,
	ordine SMALLINT,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_quadro) REFERENCES moon_io_d_quadro (id_quadro),
	PRIMARY KEY (id_modulo,id_quadro)
);

CREATE TABLE IF NOT EXISTS moon_io_d_elemento (
	id_elemento SERIAL PRIMARY KEY,
	nome_elemento VARCHAR (30) NOT NULL,
	label VARCHAR (200),
	id_tipo_elemento INTEGER NOT NULL,
	id_gruppo_dati INTEGER DEFAULT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_io_r_quadro_elementi (
	id_quadro INT NOT NULL,
	id_elemento INT NOT NULL,
	progressivo INT NOT NULL DEFAULT 1,
	obbligatorio CHAR(1) NOT NULL DEFAULT 'N',
	condizionato CHAR(1) NOT NULL DEFAULT 'N',
	FOREIGN KEY (id_elemento) REFERENCES moon_io_d_elemento (id_elemento),
	FOREIGN KEY (id_quadro) REFERENCES moon_io_d_quadro (id_quadro),
	PRIMARY KEY (id_quadro,id_elemento,progressivo)
);
		
CREATE TABLE IF NOT EXISTS moon_io_d_moduloattributi (
	id_attributo SERIAL PRIMARY KEY,
	id_modulo INT NOT NULL,
	nome_attributo VARCHAR (255),
	valore VARCHAR (2000),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	UNIQUE (id_modulo, nome_attributo)
);

CREATE TABLE IF NOT EXISTS moon_io_d_moduloprogressivo (
	id_modulo INT NOT NULL,
	id_tipo_codice_istanza INT NOT NULL DEFAULT '1',
	progressivo BIGINT NOT NULL,
	anno_riferimento SMALLINT, 
	lunghezza SMALLINT NOT NULL DEFAULT 8,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	UNIQUE (id_modulo, anno_riferimento),
	FOREIGN KEY (id_tipo_codice_istanza) REFERENCES moon_io_d_tipocodiceistanza (id_tipo_codice_istanza)
);


-- gestione workflow

CREATE TABLE IF NOT EXISTS moon_wf_d_stato (
	id_stato_wf SERIAL PRIMARY KEY,
	codice_stato_wf VARCHAR (50) UNIQUE NOT NULL,
	nome_stato_wf VARCHAR (50) NOT NULL,
	desc_stato_wf VARCHAR (400),
	id_tab_fo INT NOT NULL DEFAULT 2
);

CREATE TABLE IF NOT EXISTS moon_wf_d_azione (
	id_azione SERIAL PRIMARY KEY,
	codice_azione VARCHAR (50) UNIQUE NOT NULL,
	nome_azione VARCHAR (30) NOT NULL,
	desc_azione VARCHAR (100)
);

CREATE TABLE IF NOT EXISTS moon_wf_d_processo (
	id_processo SERIAL PRIMARY KEY,
	codice_processo VARCHAR(30) NOT NULL,
	nome_processo VARCHAR(50),
	descrizione_processo VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_wf_d_workflow (
	id_workflow SERIAL PRIMARY KEY,
	id_processo INT NOT NULL,
	id_stato_wf_partenza INT NOT NULL,
	id_stato_wf_arrivo INT NOT NULL,
	campo_condizione VARCHAR(50) DEFAULT NULL,
	valore_condizione VARCHAR(50) DEFAULT NULL,
	id_azione INT DEFAULT NULL,
	email_destinatario text DEFAULT NULL,
	id_utente_destinatario INT DEFAULT NULL,
	id_tipo_utente_destinatario INT DEFAULT NULL,
	id_gruppo_utenti_destinatari INT DEFAULT NULL,
	flag_archiviabile CHAR(1) NOT NULL DEFAULT 'N',
	flag_annullabile CHAR(1) NOT NULL DEFAULT 'N',
	id_datiazione INT,
	lag_stato_istanza CHAR(1) NOT NULL DEFAULT 'N',
	flag_api CHAR(1) NOT NULL DEFAULT 'N',
	flag_automatico CHAR(1) NOT NULL DEFAULT 'N',
	FOREIGN KEY (id_processo) REFERENCES moon_wf_d_processo (id_processo),
	FOREIGN KEY (id_azione) REFERENCES moon_wf_d_azione (id_azione),
	FOREIGN KEY (id_stato_wf_partenza) REFERENCES moon_wf_d_stato (id_stato_wf),
	FOREIGN KEY (id_stato_wf_arrivo) REFERENCES moon_wf_d_stato (id_stato_wf)
);


-- il destinatario di uno stato è l'utente che accedendo al sistema potrà gestire il modello in quel punto del processo

CREATE TABLE IF NOT EXISTS moon_wf_d_referentestato (
	id_referentestato SERIAL PRIMARY KEY,
	id_modulo INT NOT NULL,
	id_stato_wf INT NOT NULL,
	id_utente INT NOT NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

-- il referenteStato è l'utente che può agire su uno stato anche se non corrisponde al destinatario di quello stato

CREATE TABLE IF NOT EXISTS moon_wf_r_modulo_processo (
	id_modulo INT NOT NULL,
	id_processo INT NOT NULL,
	ordine INT NOT NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_processo) REFERENCES moon_wf_d_processo (id_processo)
);
alter table moon_wf_r_modulo_processo add PRIMARY KEY (id_modulo, id_processo, ordine);													   

-- nel momento in cui viene attivata un'azione, all'interno di un processo e per un modulo specifico,
-- può anche essere attivato un form di dati definito in questa tabella 
CREATE TABLE IF NOT EXISTS moon_wf_d_dati_azione (
	id_datiazione SERIAL PRIMARY KEY,
	codice_datiazione  VARCHAR (50) NOT NULL,
	versione_datiazione  VARCHAR (30) NOT NULL,
	struttura TEXT,
	descrizione_datiazione TEXT,
	data_ins TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	data_upd TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	UNIQUE (codice_datiazione,versione_datiazione)
);


-- ------------------------------------------------------------------------------------------
-- gestione front-end

CREATE TABLE IF NOT EXISTS moon_fo_c_regione (
	codice_regione VARCHAR(2) PRIMARY KEY,
	nome_regione VARCHAR(50),
	flag_attivo CHAR(1) DEFAULT 'S'	
);

CREATE TABLE IF NOT EXISTS moon_fo_c_provincia (
	codice_provincia VARCHAR(3) PRIMARY KEY,
	sigla_provincia VARCHAR(2),
	nome_provincia VARCHAR(50),
	codice_regione VARCHAR(2),
	flag_attivo CHAR(1) DEFAULT 'S'
);

CREATE TABLE IF NOT EXISTS moon_fo_c_comune (
	codice_comune VARCHAR(6) PRIMARY KEY,
	nome_comune VARCHAR(100),
	progressivo_comune VARCHAR(3),
	codice_provincia VARCHAR(3),
	flag_attivo CHAR(1) DEFAULT 'S'
);

CREATE TABLE IF NOT EXISTS moon_fo_c_comune_rp (
	codice_comune VARCHAR(6) PRIMARY KEY,
	nome_comune VARCHAR(100),
	codice_catastale VARCHAR(4),
	codice_asl VARCHAR(6),
	desc_asl VARCHAR(100),
	email_asl VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS moon_ext_anpr_c_stato_estero (
id_stato_estero         INTEGER PRIMARY KEY,
denominazione           VARCHAR(200),
denominazione_istat     VARCHAR(200),
denominazione_istat_en  VARCHAR(200),
dt_inizio_validita      DATE,
dt_fine_validita        DATE,
cod_iso_3166_1_alpha3   VARCHAR(3),
cod_mae                 VARCHAR(3),
cod_min                 VARCHAR(3),
cod_at                  VARCHAR(4),
cod_istat               VARCHAR(3),
cittadinanza            VARCHAR(1),
nascita                 VARCHAR(1),
residenza               VARCHAR(1),
fonte                   VARCHAR(5),
tipo                    VARCHAR(20),
cod_iso_sovrano         VARCHAR(3),
motivo                  VARCHAR(20),
dt_ultimo_aggiornamento DATE         
);

	
CREATE TABLE IF NOT EXISTS moon_fo_d_categoria (
	id_categoria SERIAL PRIMARY KEY,
	nome_categoria VARCHAR(50),
	id_categoria_padre INTEGER DEFAULT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_fo_r_categoria_modulo (
	id_categoria INTEGER NOT NULL,
	id_modulo INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	PRIMARY KEY (id_categoria,id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_fo_d_tipoente (
	id_tipo_ente SERIAL PRIMARY KEY,
	codice_tipo_ente VARCHAR(10) NOT NULL,
	nome_tipo_ente VARCHAR(50),
	descrizione_tipo_ente VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_fo_d_ente (
	id_ente SERIAL PRIMARY KEY,
	id_ente_padre INT DEFAULT NULL,
	codice_ente VARCHAR(10) NOT NULL,
	nome_ente VARCHAR(50),
	descrizione_ente VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	id_tipo_ente INTEGER DEFAULT NULL,
	logo VARCHAR(200),
	indirizzo VARCHAR(400),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	codice_ipa VARCHAR(50),
	FOREIGN KEY (id_tipo_ente) REFERENCES moon_fo_d_tipoente (id_tipo_ente)
);
-- in logo viene salvato un url con il link alla risorsa
ALTER TABLE moon_fo_d_ente ADD FOREIGN KEY (id_ente_padre) REFERENCES moon_fo_d_ente (id_ente);

COMMENT ON COLUMN moon_fo_d_ente.codice_ipa is 'Codice IPA - Indice delle Pubbliche Amministrazioni del CSI Piemonte - usato per integrazione con COSMO';

CREATE TABLE IF NOT EXISTS moon_fo_d_area (
	id_area SERIAL PRIMARY KEY,
	id_ente INT NOT NULL,
	codice_area VARCHAR(10) UNIQUE NOT NULL,
	nome_area VARCHAR(50),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	email character varying(255),
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente)
);
CREATE UNIQUE INDEX ak_moon_fo_d_area ON moon_fo_d_area (id_ente, codice_area);
-- un'area è un'entità astratta che appartiene ad un ente e che raggruppa utenti e moduli con caratteristiche omogenee

CREATE TABLE IF NOT EXISTS moon_fo_r_area_modulo (
	id_area INT NOT NULL,
	id_modulo INT NOT NULL,
	id_ente INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	PRIMARY KEY (id_area,id_modulo)
);

CREATE UNIQUE INDEX ak_moon_fo_r_area_modulo ON moon_fo_r_area_modulo (id_modulo, id_ente);


CREATE TABLE IF NOT EXISTS moon_fo_d_portale (
	id_portale SERIAL PRIMARY KEY,
	codice_portale VARCHAR(30) NOT NULL,
	nome_portale VARCHAR(100),
	descrizione_portale VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);


CREATE TABLE IF NOT EXISTS moon_fo_r_portale_modulo (
	id_portale INT NOT NULL,
	id_modulo INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_portale) REFERENCES moon_fo_d_portale (id_portale),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	PRIMARY KEY (id_portale,id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_fo_d_importanza (
	id_importanza INT NOT NULL PRIMARY KEY,
	desc_importanza VARCHAR (50),
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP	
);

insert INTO moon_fo_d_importanza (id_importanza, desc_importanza) values ( 0, 'non importante');
insert INTO moon_fo_d_importanza (id_importanza, desc_importanza) values ( 1, 'in evidenza');
insert INTO moon_fo_d_importanza (id_importanza, desc_importanza) values ( 2, 'altro');


CREATE TABLE IF NOT EXISTS moon_fo_t_istanza (
	id_istanza BIGSERIAL PRIMARY KEY,
	codice_istanza VARCHAR (50) not null,
	id_modulo INT NOT NULL,
	id_versione_modulo INT NOT NULL DEFAULT 0,
	id_ente INT NOT NULL DEFAULT 0,
	codice_fiscale_dichiarante VARCHAR (16) NOT NULL,
	cognome_dichiarante VARCHAR (150) NULL,
	nome_dichiarante VARCHAR (150) NULL,
	id_stato_wf INT NOT NULL,
	data_creazione TIMESTAMP,
	attore_ins VARCHAR (16),
	attore_upd VARCHAR (16), 
	fl_eliminata CHAR(1) NOT NULL DEFAULT 'N',
	fl_archiviata CHAR(1) NOT NULL DEFAULT 'N',
    fl_test CHAR(1) NOT NULL DEFAULT 'N', 
    importanza INT NOT NULL DEFAULT 0,
	numero_protocollo VARCHAR (20),
	data_protocollo DATE,
	current_step INT NOT NULL DEFAULT 0,
	hash_univocita character varying(64) NULL,
	grouppo_operatore_fo VARCHAR (20) NULL,
	dati_aggiuntivi VARCHAR (1024) NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_versione_modulo) REFERENCES moon_io_d_versione_modulo(id_versione_modulo),
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_stato_wf) REFERENCES moon_wf_d_stato (id_stato_wf),
	FOREIGN KEY (importanza) REFERENCES moon_fo_d_importanza (id_importanza)
);


CREATE UNIQUE INDEX CONCURRENTLY istanza_codice_istanza 
ON moon_fo_t_istanza (codice_istanza);

ALTER TABLE moon_fo_t_istanza 
ADD CONSTRAINT unique_codice_istanza 
UNIQUE USING INDEX istanza_codice_istanza;


CREATE TABLE IF NOT EXISTS moon_fo_t_cronologia_stati (
	id_cronologia_stati BIGSERIAL PRIMARY KEY,
	id_istanza BIGINT,
	id_stato_wf INT NOT NULL,
	id_azione_svolta INT NOT NULL,
	data_inizio TIMESTAMP,
	data_fine TIMESTAMP,
	attore_ins VARCHAR (16),
	attore_upd VARCHAR (16), 
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_stato_wf) REFERENCES moon_wf_d_stato (id_stato_wf),
	FOREIGN KEY (id_azione_svolta) REFERENCES moon_wf_d_azione (id_azione)
);


CREATE TABLE IF NOT EXISTS moon_fo_d_tipo_modifica_dati (
	id_tipo_modifica SERIAL PRIMARY KEY,
	cod_tipo_modifica_dati VARCHAR(3) NOT NULL,
	descrizione_tipo_modifica_dati VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

COMMENT ON COLUMN moon_fo_d_tipo_modifica_dati.cod_tipo_modifica_dati IS 'INI= iniziali, ADD=aggiunti, UPD=sostituiti, NON=nessuna modifica '; 


insert INTO moon_fo_d_tipo_modifica_dati (id_tipo_modifica,cod_tipo_modifica_dati,descrizione_tipo_modifica_dati) values ( 1,'INI', 'inserimento iniziale');
insert INTO moon_fo_d_tipo_modifica_dati (id_tipo_modifica,cod_tipo_modifica_dati,descrizione_tipo_modifica_dati) values ( 2,'NON', 'nessuna modifica ai dati esistenti');
insert INTO moon_fo_d_tipo_modifica_dati (id_tipo_modifica,cod_tipo_modifica_dati,descrizione_tipo_modifica_dati) values ( 3,'UPD', 'modifica di dati esistenti');
insert INTO moon_fo_d_tipo_modifica_dati (id_tipo_modifica,cod_tipo_modifica_dati,descrizione_tipo_modifica_dati) values ( 4,'ADD', 'aggiunta di nuovi elementi');
insert INTO moon_fo_d_tipo_modifica_dati (id_tipo_modifica,cod_tipo_modifica_dati,descrizione_tipo_modifica_dati) values ( 5,'AUP', 'aggiunta di nuovi elementi e modifica di quelli esistenti');


CREATE TABLE IF NOT EXISTS moon_fo_t_dati_istanza (
	id_dati_istanza BIGSERIAL PRIMARY KEY,
	id_cronologia_stati BIGINT,
	id_istanza BIGINT,
	dati_istanza text,
	id_tipo_modifica INT NOT NULL DEFAULT 1,
	id_stepcompilazione INT DEFAULT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_cronologia_stati) REFERENCES moon_fo_t_cronologia_stati (id_cronologia_stati),
	FOREIGN KEY (id_tipo_modifica) REFERENCES moon_fo_d_tipo_modifica_dati (id_tipo_modifica)
);

CREATE TABLE moon_fo_t_allegati_istanza
(
	id_allegato SERIAL PRIMARY KEY,
	formio_key character varying(255) NOT NULL,
	formio_name_file character varying(255) NOT NULL,
	formio_dir character varying(255) NOT NULL,
	codice_file character varying(50) NULL,
	hash_file character varying(64) NOT NULL,
	nome_file character varying(255) NOT NULL,
	lunghezza BIGINT NOT NULL,
	contenuto bytea NULL,
	content_type character varying(255) NOT NULL,
	media_type character varying(255) NOT NULL,
	sub_media_type character varying(255),
	ip_address character varying(32),
	estensione character varying(10),
	uuid_index character varying(36),
	fl_eliminato CHAR(1) default 'N',
	data_creazione timestamp without time zone DEFAULT now(),
	id_istanza BIGINT NULL, -- valorizzato al submit
	key character varying(255),
	full_key character varying(255),
	label character varying(255),
	fl_firmato CHAR(1) default 'N',
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza)
);


CREATE TABLE moon_fo_t_repository_file
(
    id_file SERIAL PRIMARY KEY,
    nome_file character varying(255) NOT NULL,
    contenuto bytea NULL,
    fl_eliminato character(1) DEFAULT 'N'::bpchar,
    data_creazione timestamp without time zone DEFAULT now(),
    id_istanza bigint,
    formio_key character varying(255),
    formio_name_file character varying(255),
    hash_file character varying(64),
    content_type character varying(255),
    id_tipologia integer,
    codice_file character varying(50),
    id_storico_workflow INT,
	fl_firmato CHAR(1) default 'N',
	tipologia_fruitore character varying(128),
	ref_url varchar(128) NULL,
	descrizione varchar(255) NULL,
	lunghezza int8 NULL,
	numero_protocollo varchar(50) NULL,
	data_protocollo timestamp NULL,
	uuid_index varchar(36) NULL,
	key character varying(255),
	full_key character varying(255),
	label character varying(255),
	id_tipologia_mydocs INT NULL,
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_storico_workflow) REFERENCES moon_wf_t_storico_workflow (id_storico_workflow)
);


-- ogni volta che viene compiuta una azione, viene creato un record nuovo con data_fine null
-- e viene assegnata una data_fine ai record precedenti che hanno data_fine null 
CREATE TABLE IF NOT EXISTS moon_wf_t_storico_workflow (
	id_storico_workflow SERIAL PRIMARY KEY,
	id_istanza INT NOT NULL,
	id_processo INT NOT NULL,
	id_stato_wf_partenza INT NOT NULL,
	id_stato_wf_arrivo INT NOT NULL,
	id_azione INT DEFAULT NULL,
	nome_stato_wf_partenza VARCHAR (50) NOT NULL,
	nome_stato_wf_arrivo VARCHAR (50) NOT NULL,
	nome_azione VARCHAR (50) NOT NULL,
	dati_azione text,
	desc_destinatario VARCHAR(255) DEFAULT NULL,
	data_inizio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_fine TIMESTAMP,
    id_file_rendering INT default null,
    attore_upd VARCHAR (16) NOT NULL DEFAULT '',
   	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_processo) REFERENCES moon_wf_d_processo (id_processo),
	FOREIGN KEY (id_azione) REFERENCES moon_wf_d_azione (id_azione),
	FOREIGN KEY (id_stato_wf_partenza) REFERENCES moon_wf_d_stato (id_stato_wf),
	FOREIGN KEY (id_stato_wf_arrivo) REFERENCES moon_wf_d_stato (id_stato_wf),
	FOREIGN KEY (id_file_rendering) REFERENCES moon_fo_t_repository_file (id_file)
);


CREATE TABLE IF NOT EXISTS moon_fo_d_tipo_utente (
	id_tipo_utente SERIAL PRIMARY KEY,
	codice_tipo_utente VARCHAR(3),
	desc_tipo_utente VARCHAR(30),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

COMMENT ON COLUMN moon_fo_d_tipo_utente.codice_tipo_utente IS 'ADM=utente admin, PA=utente di backoffice, RUP=utente rupar di frontoffice, CIT=utente internet di frontoffice';


CREATE TABLE IF NOT EXISTS moon_fo_t_utente (
	id_utente SERIAL PRIMARY KEY,
	identificativo_utente VARCHAR(36) NOT NULL,
	nome VARCHAR(50) NOT NULL DEFAULT '',
	cognome VARCHAR(50) NOT NULL DEFAULT '',
	username VARCHAR (25),
	password VARCHAR (64),
	email VARCHAR(200),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	id_tipo_utente INT NOT NULL,
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_upd TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	attore_upd VARCHAR (16),
	id_gruppo_rif INT NOT NULL DEFAULT '0',
	id_provider_esterno int4 NULL,
	identificativo_utente_esterno varchar(36) NULL,
	FOREIGN KEY (id_tipo_utente) REFERENCES  moon_fo_d_tipo_utente (id_tipo_utente)
); 

CREATE UNIQUE INDEX CONCURRENTLY idx_moon_fo_t_utente_identificativo_utente
ON moon_fo_t_utente (identificativo_utente);

ALTER TABLE moon_fo_t_utente ADD CONSTRAINT unique_identificativo_utente 
UNIQUE USING INDEX moon_fo_t_utente_identificativo_utente;

ALTER TABLE moon_fo_t_utente ADD CONSTRAINT unique_provider_id_utente_esterno 
UNIQUE (id_provider_esterno, identificativo_utente_esterno);

CREATE TABLE IF NOT EXISTS moon_fo_d_gruppo_utenti (
	id_gruppo_utenti SERIAL PRIMARY KEY,
	codice_gruppo_utenti VARCHAR(10) UNIQUE NOT NULL,
	nome_gruppo_utenti VARCHAR(50),
	descrizione_gruppo_utenti VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_fo_r_utente_modulo (
	id_utente INT NOT NULL,
	id_modulo INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_utente) REFERENCES moon_fo_t_utente (id_utente),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);
alter table moon_fo_r_utente_modulo add PRIMARY KEY (id_utente, id_modulo);

CREATE TABLE IF NOT EXISTS moon_fo_d_funzione (
	id_funzione SERIAL PRIMARY KEY,
	codice_funzione VARCHAR(50) UNIQUE NOT NULL,
	nome_funzione VARCHAR(50),
	descrizione_funzione VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

/*
CREATE TABLE IF NOT EXISTS moon_fo_d_ruolo (
	id_ruolo SERIAL PRIMARY KEY,
	codice_ruolo VARCHAR(10) UNIQUE NOT NULL,
	nome_ruolo VARCHAR(50),
	descrizione_ruolo VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);
*/
/*
CREATE TABLE IF NOT EXISTS moon_fo_d_ruolo (
	id_ruolo serial4 NOT NULL,
	codice_ruolo varchar(10) NOT NULL,
	nome_ruolo varchar(50) NULL,
	descrizione_ruolo varchar(400) NULL,
	fl_attivo bpchar(1) NOT NULL DEFAULT 'S'::bpchar,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
    id_ente int4,
	CONSTRAINT moon_fo_d_ruolo_codice_ruolo_key UNIQUE (codice_ruolo),
	CONSTRAINT moon_fo_d_ruolo_pkey PRIMARY KEY (id_ruolo),
    FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente)
);
*/

CREATE TABLE IF NOT EXISTS moon_fo_d_ruolo (
	id_ruolo serial4 NOT NULL,
	codice_ruolo varchar(10) NOT NULL,
	nome_ruolo varchar(50) NULL,
	descrizione_ruolo varchar(400) NULL,
	fl_attivo bpchar(1) NOT NULL DEFAULT 'S'::bpchar,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
    id_ente int4,
	CONSTRAINT moon_fo_d_ruolo_codice_ruolo_id_ente_key UNIQUE (codice_ruolo, id_ente),
	CONSTRAINT moon_fo_d_ruolo_pkey PRIMARY KEY (id_ruolo),
    FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente)
);

CREATE TABLE IF NOT EXISTS moon_fo_r_ruolo_funzioni (
	id_ruolo INT NOT NULL,
	id_funzione INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_ruolo) REFERENCES moon_fo_d_ruolo (id_ruolo),
	FOREIGN KEY (id_funzione) REFERENCES moon_fo_d_funzione (id_funzione),
	PRIMARY KEY (id_ruolo,id_funzione)
);

CREATE TABLE IF NOT EXISTS moon_fo_r_utente_area_ruolo (
	id_utente INT NOT NULL,
	id_area INT NOT NULL,
	id_ruolo INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_ruolo) REFERENCES moon_fo_d_ruolo (id_ruolo),
	FOREIGN KEY (id_utente) REFERENCES moon_fo_t_utente (id_utente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	PRIMARY KEY (id_ruolo,id_utente,id_area)
);


-- elenco degli utenti che possono compilare un dato modulo, se questo è indicato come riservato
CREATE TABLE IF NOT EXISTS moon_fo_d_utente_modulo_riservato (
	id_modulo INT NOT NULL,
	cf_utente VARCHAR (16) NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	PRIMARY KEY (cf_utente,id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_fo_r_utente_gruppo (
	id_utente INT NOT NULL,
	id_gruppo_utenti INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_utente) REFERENCES moon_fo_t_utente (id_utente),
	FOREIGN KEY (id_gruppo_utenti) REFERENCES moon_fo_d_gruppo_utenti (id_gruppo_utenti)
);


CREATE TABLE IF NOT EXISTS moon_fo_t_notifica (
	id_notifica BIGSERIAL PRIMARY KEY,
	id_istanza BIGINT NOT NULL,
	cf_destinatario varchar(16) NOT NULL,
	email_destinatario varchar(200) DEFAULT NULL,
	oggetto_notifica varchar(100) DEFAULT NULL,
	testo_notifica text,
	data_invio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	esito_invio char(1),
	flag_letta CHAR(1) NOT NULL DEFAULT 'N',
	flag_archiviata CHAR(1) NOT NULL DEFAULT 'N'
);

CREATE TABLE moon_fo_r_allegato_notifica
(	
	id_allegato_notifica SERIAL PRIMARY KEY,
	id_file int not null,
	id_notifica int not null,
	FOREIGN KEY (id_file) REFERENCES moon_fo_t_repository_file (id_file),
	FOREIGN KEY (id_notifica) REFERENCES moon_fo_t_notifica (id_notifica)
);



CREATE TABLE IF NOT EXISTS moon_fo_t_messaggi (
	id_messaggio SERIAL NOT NULL,
	id_istanza BIGINT,
	desc_mittente varchar(200) DEFAULT NULL,
	codice_fiscale_mittente varchar(16) DEFAULT NULL,
	email_mittente varchar(200) DEFAULT NULL,
	desc_destinatario varchar(200) DEFAULT NULL,
	codice_fiscale_destinatario varchar(16) DEFAULT NULL,
	email_destinatario varchar(200) DEFAULT NULL,
	oggetto_messaggio varchar(100),
	testo_messaggio text,
	data_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	data_chiusura timestamp DEFAULT NULL,
	flag_letto CHAR(1) NOT NULL DEFAULT 'N',
	flag_archiviato CHAR(1) NOT NULL DEFAULT 'N' 
);

CREATE TABLE IF NOT EXISTS moon_fo_t_thread (
	id_thread SERIAL NOT NULL,
	desc_thread varchar(100) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS moon_fo_r_messaggi_thread (
	id_messaggio BIGINT NOT NULL,
	id_thread BIGINT NOT NULL
);


CREATE TABLE IF NOT EXISTS moon_fo_t_log_email (
	id_log_email BIGSERIAL PRIMARY KEY,
	data_log_email TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	id_tipologia INT NULL, -- 1 MOOnFO-PostSaveIstanzaTaskManager.SendEmailDichiaranteIstanzaTask
	id_ente INT NULL,
	id_modulo INT NULL,
	id_istanza INT NULL,
	email_destinatario VARCHAR(255) NOT NULL,
	tipo_email VARCHAR(30) NOT NULL, -- text, text-attach, html, html-attach
	esito VARCHAR(30) NOT NULL,
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza)
);

CREATE TABLE IF NOT EXISTS moon_fo_t_log_giornaliero (
	giorno DATE NOT NULL,
	num_accessi INT DEFAULT NULL,
	count_creati INT DEFAULT NULL,
	count_avviati INT DEFAULT NULL,
	count_eliminati INT DEFAULT NULL,
	codice_modulo VARCHAR (30) DEFAULT NULL
);

CREATE TABLE IF NOT EXISTS moon_fo_t_log_incrementale (
	id_modulo INT NOT NULL,
	count_creati INT DEFAULT NULL,
	count_avviati INT DEFAULT NULL,
	count_eliminati INT DEFAULT NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);


CREATE TABLE moon_fo_t_log_operazione (
  id_traccia BIGSERIAL,
  data_ora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  ip_address VARCHAR(20) DEFAULT NULL,
  utente VARCHAR(16) NOT NULL,
  codice_app VARCHAR(2) NOT NULL,
  operazione VARCHAR(100) NOT NULL,
  dettaglio VARCHAR(500),
  codice_ruolo VARCHAR(100) DEFAULT NULL,
  PRIMARY KEY (id_traccia)
) ;
COMMENT ON COLUMN moon_fo_t_log_operazione.codice_app IS 'fo=frontoffice, bo=backoffice, ad=admin, br=builder';


-- ------------------------------------------------------------------------------------------
-- gestione fruitore applicativi

CREATE TABLE IF NOT EXISTS moon_wf_d_fruitore (
	id_fruitore SERIAL PRIMARY KEY,
	desc_fruitore VARCHAR (100) NOT NULL
);

CREATE TABLE IF NOT EXISTS moon_wf_r_fruitore_modulo (
	id_fruitore INT NOT NULL,
	id_modulo INT NOT NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_fruitore) REFERENCES moon_wf_d_fruitore (id_fruitore),
	PRIMARY KEY (id_fruitore, id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_wf_r_fruitore_ente (
	id_fruitore INT NOT NULL,
	id_ente INT NOT NULL,
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_fruitore) REFERENCES moon_wf_d_fruitore (id_fruitore),
	PRIMARY KEY (id_fruitore, id_ente)
);

CREATE TABLE IF NOT EXISTS moon_wf_t_fruitore_dati_azione (
	id_fruitore_dati_azione SERIAL PRIMARY KEY,
	id_storico_workflow BIGINT NOT NULL,
	id_istanza BIGINT NOT NULL,
	codice VARCHAR (30),
	descrizione VARCHAR (255),
	identificativo VARCHAR (50),
	data TIMESTAMP,
	numero_protocollo VARCHAR (50),
	data_protocollo TIMESTAMP,
	dati_azione text,
	post_azioni text,
	allegati_azione text,
	FOREIGN KEY (id_storico_workflow) REFERENCES  moon_wf_t_storico_workflow (id_storico_workflow),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza)
);

-- ------------------------------------------------------------------------------------------
-- gestione protocollo

CREATE TABLE IF NOT EXISTS moon_pr_d_parametri (
	id_parametro SERIAL PRIMARY KEY,
	id_ente INT NOT NULL,
	id_area INT,
	id_modulo INT,
	nome_attributo VARCHAR (255),
	valore VARCHAR (2000),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area)
);

CREATE UNIQUE INDEX ak4_moon_pr_d_parametri ON moon_pr_d_parametri (id_ente, id_area, id_modulo, nome_attributo)
WHERE id_area IS NOT NULL 
AND id_modulo IS NOT NULL;

CREATE UNIQUE INDEX ak3_moon_pr_d_parametri ON moon_pr_d_parametri (id_ente, id_area, nome_attributo)
WHERE id_area IS NOT NULL 
AND id_modulo IS NULL;

CREATE UNIQUE INDEX ak2_moon_pr_d_parametri ON moon_pr_d_parametri (id_ente, nome_attributo)
WHERE id_area IS NULL 
AND id_modulo IS NULL;


CREATE TABLE IF NOT EXISTS moon_pr_t_richiesta (
	id_richiesta BIGSERIAL PRIMARY KEY,
	data_richiesta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	codice_richiesta VARCHAR(100) NOT NULL, -- simile uuid ma piu parlante es. codice_istanza + sufisso per allegati
	uuid_richiesta VARCHAR(100) NOT NULL,
	stato VARCHAR(30) NOT NULL, -- TX, OK, KO
	tipo_ing_usc INT NOT NULL, -- 1-IN 2-OUT
	tipo_doc INT NOT NULL, -- 1-ISTANZA 2-ALLEGATO
	id_istanza BIGINT,
	id_allegato_istanza BIGINT,
	id_file BIGINT,
	id_modulo INT,
	id_area INT,
	id_ente INT NOT NULL,
	id_protocollatore INT NULL, -- 1-STARDAS
	uuid_protocollatore VARCHAR(100) NULL,
	note VARCHAR(2000) NULL, -- result_code smistamento intermedie
	codice_esito VARCHAR(100) NULL, -- esito finale
	desc_esito VARCHAR(255) NULL, -- esito finale
	data_upd TIMESTAMP NULL,
	id_storico_workflow BIGINT default NULL,
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_allegato_istanza) REFERENCES moon_fo_t_allegati_istanza (id_allegato),
	FOREIGN KEY (id_file) REFERENCES moon_fo_t_repository_file (id_file),
	FOREIGN KEY (id_storico_workflow) REFERENCES moon_wf_t_storico_workflow (id_storico_workflow)
);

CREATE TABLE IF NOT EXISTS moon_pr_d_metadato (
	id_metadato SERIAL PRIMARY KEY,
	nome_metadato VARCHAR (255) NOT NULL,
	default_value VARCHAR (255),
	ordine INT
);
CREATE UNIQUE INDEX CONCURRENTLY idx_moon_pr_d_metadato_nome_metadato ON moon_pr_d_metadato (nome_metadato);
ALTER TABLE moon_pr_d_metadato ADD CONSTRAINT ak_moon_pr_d_metadato UNIQUE USING INDEX idx_moon_pr_d_metadato_nome_metadato;


INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('DESCRIZIONE_VOL_SERIE_FASCICOLI', '2022', 1);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('CODICE_FASCICOLO', NULL, 2);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('OGGETTO_DOCUMENTO', NULL, 3);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('PAROLE_CHIAVE_DOCUMENTO', '@@CODICE_ISTANZA@@ @@NOME@@ @@COGNOME@@', 4);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('AUTORE_FISICO_DOCUMENTO', '@@NOME@@ @@COGNOME@@', 5);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('MITTENTE_DENOMINAZIONE', '@@NOME@@ @@COGNOME@@', 6);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('DATA_CRONICA_DOCUMENTO', '@@MODIFIED@@', 7);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('ANNOTAZIONE_FORMALE_1', 'Istanza MOOn@@CODICE_ISTANZA@@', 8);
INSERT INTO moon.moon_pr_d_metadato (nome_metadato, default_value, ordine)
VALUES('ANNOTAZIONE_FORMALE_2', 'Istanza MOOn@@CODICE_ISTANZA@@', 9);


CREATE TABLE IF NOT EXISTS moon_ml_d_logon_mode (
	id_logon_mode INT PRIMARY KEY,
    codice_logon_mode VARCHAR (30) NOT NULL,
	desc_logon_mode VARCHAR (100)
);

CREATE TABLE IF NOT EXISTS moon_ml_r_portale_modulo_logon_mode (
	id_portale INT NOT NULL,
	id_modulo INT NOT NULL,
	id_logon_mode INT NOT NULL,
    filtro  VARCHAR (250),
    PRIMARY KEY (id_modulo,id_portale),
    FOREIGN KEY (id_portale) REFERENCES moon_fo_d_portale (id_portale),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
    FOREIGN KEY (id_logon_mode) REFERENCES moon_ml_d_logon_mode (id_logon_mode)
);


-- ------------------------------------------------------------------------------------------
-- gestione audit

CREATE TABLE IF NOT EXISTS CSI_LOG_AUDIT (
	id_audit SERIAL PRIMARY KEY,
	data_ora TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
	id_app VARCHAR (100) NOT NULL,
	ip_address VARCHAR (40) ,
	utente VARCHAR (100) NOT NULL,
	operazione VARCHAR (50) NOT NULL,
	ogg_oper VARCHAR (500) NOT NULL,
	key_oper VARCHAR (500)
);


-- ------------------------------------------------------------------------------------------
-- gestione forme giuridiche

CREATE TABLE moon_c_forme_giuridiche
(
   id_forma_giuridica     SERIAL PRIMARY KEY,
   cod_forma_giuridica    varchar(8)     NOT NULL,
   descr_forma_giuridica  varchar(200)   NOT NULL,
   flag_pubblico_privato  numeric(1)     NOT NULL,
   cod_natura_giuridica   char(2),
   CONSTRAINT moon_c_forme_giuridiche_flag_pubblico_privato CHECK (flag_pubblico_privato = ANY (ARRAY[(1)::numeric, (2)::numeric]))
);

ALTER TABLE moon_c_forme_giuridiche
   ADD CONSTRAINT ak_moon_c_forme_giuridiche UNIQUE (cod_forma_giuridica);

CREATE UNIQUE INDEX idx_moon_c_forme_giuridiche_cod_natura ON moon_c_forme_giuridiche USING btree (cod_natura_giuridica);


-- ------------------------------------------------------------------------------------------
-- gestione integrazione COSMO

CREATE TABLE IF NOT EXISTS moon_cs_t_log_pratica (
	id_log_pratica BIGSERIAL PRIMARY KEY,
	id_pratica VARCHAR(255) NOT NULL,
	id_istanza BIGINT,
	idx INT,
	id_modulo BIGINT,
	data_ins TIMESTAMP NOT NULL,
	data_avvio TIMESTAMP NULL,
	data_upd TIMESTAMP NULL,
	crea_richiesta TEXT NOT NULL,
	crea_risposta TEXT NULL,
	crea_documento_richiesta TEXT NULL,
	crea_documento_risposta TEXT NULL,
	avvia_processo_richiesta TEXT NULL,
	avvia_processo_risposta TEXT NULL,
	errore TEXT NULL,
	pratica TEXT NULL,
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

-- moon_cs_t_pratica_ak_id_istanza_idx
CREATE UNIQUE INDEX CONCURRENTLY idx_moon_cs_t_log_pratica_uniqe_id_pratica_idx ON moon_cs_t_log_pratica (id_pratica);
ALTER TABLE moon_cs_t_log_pratica ADD CONSTRAINT moon_cs_t_log_pratica_ak_id_pratica 
UNIQUE USING INDEX moon_cs_t_log_pratica_uniqe_id_pratica_idx;

-- moon_cs_t_pratica_ak_id_istanza_idx
CREATE UNIQUE INDEX CONCURRENTLY idx_moon_cs_t_log_pratica_uniqe_idx_id_istanza_idx ON moon_cs_t_log_pratica (id_istanza, idx);
ALTER TABLE moon_cs_t_log_pratica ADD CONSTRAINT moon_cs_t_log_pratica_ak_id_istanza_idx 
UNIQUE USING INDEX moon_cs_t_log_pratica_uniqe_idx_id_istanza_idx;

CREATE TABLE IF NOT EXISTS moon_cs_t_log_servizio (
	id_log_servizio BIGSERIAL PRIMARY KEY,
	id_log_pratica BIGINT,
	id_pratica VARCHAR(255),
	id_istanza BIGINT,
	id_modulo BIGINT,
	servizio VARCHAR(30) NOT NULL,
	data_ins TIMESTAMP NOT NULL,
	data_upd TIMESTAMP NULL,
	richiesta TEXT NULL,
	risposta TEXT NULL,
	errore TEXT NULL,
	FOREIGN KEY (id_log_pratica) REFERENCES moon_cs_t_log_pratica (id_log_pratica),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

-- ------------------------------------------------------------------------------------------
-- gestione anagrafica ATECO

CREATE TABLE IF NOT EXISTS moon_fo_c_ateco (
    id_ateco SERIAL PRIMARY KEY,
	sezione VARCHAR(1),
	divisione VARCHAR(2),
	codice VARCHAR(8),
	descrizione VARCHAR(255),
	data_inizio DATE,
	data_fine DATE
);


-- ------------------------------------------------------------------------------------------
-- crea tabella logout per FO

CREATE TABLE IF NOT EXISTS moon_fo_d_logout (
	id_logout SERIAL PRIMARY KEY,
	id_portale INT NOT NULL,
	liv_auth INT NULL,
	url VARCHAR (255),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_portale) REFERENCES moon_fo_d_portale (id_portale),
	UNIQUE (id_portale, liv_auth)
);


-- ------------------------------------------------------------------------------------------
-- Per inserire tag sulle istanze (Selector di istanze multiuso)

CREATE TABLE IF NOT EXISTS moon_fo_d_tag (
	id_tag BIGSERIAL PRIMARY KEY,
	codice_tag VARCHAR(50) UNIQUE NOT NULL,
	descrizione_tag VARCHAR(255),
	data_ins TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_fo_r_tag_istanza (
	id_tag BIGINT NOT NULL,
	id_istanza BIGINT NOT NULL,
	esito VARCHAR (32),
	data_ins TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	UNIQUE (id_tag, id_istanza),
	FOREIGN KEY (id_tag) REFERENCES moon_fo_d_tag (id_tag),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza)
);

CREATE INDEX idx_moon_fo_r_tag_istanza_id_tag ON moon_fo_r_tag_istanza USING btree (id_tag);
CREATE INDEX idx_moon_fo_r_tag_istanza_id_istanza ON moon_fo_r_tag_istanza USING btree (id_istanza);


-- ------------------------------------------------------------------------------------------
-- Gestione FAQ

CREATE TABLE IF NOT EXISTS moon_fo_t_faq (
	id_faq SERIAL PRIMARY KEY,
	titolo VARCHAR(500),
	contenuto text,
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

CREATE TABLE IF NOT EXISTS moon_r_faq_modulo (
	id_faq INT NOT NULL,
	id_modulo INT NOT NULL,
	ordine SMALLINT,
	FOREIGN KEY (id_faq) REFERENCES moon_fo_t_faq (id_faq),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	PRIMARY KEY (id_faq,id_modulo)
);


-- ------------------------------------------------------------------------------------------
-- Gestione Supporto

CREATE TABLE IF NOT EXISTS moon_fo_t_richiesta_supporto (
	id_richiesta_supporto BIGSERIAL PRIMARY KEY,
	id_istanza INT NOT NULL,
	id_modulo INT NOT NULL,
	flag_in_attesa_di_risposta CHAR(1) NOT NULL DEFAULT 'S',
	desc_mittente varchar(200) DEFAULT NULL,
	email_mittente varchar(200) DEFAULT NULL,
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL,
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

CREATE TABLE IF NOT EXISTS moon_fo_t_messaggio_supporto (
	id_messaggio_supporto BIGSERIAL PRIMARY KEY,
	id_richiesta_supporto INT NOT NULL,
	contenuto text,
	provenienza CHAR(2) NOT NULL DEFAULT 'FO', -- or id_tipo_utente : 2-PA 3-RUP 4-CIT  OR flag_is_risposta CHAR(1) ?? forse suffisciente il flag FO/BO (meglio tipo provenianza)
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL,
	FOREIGN KEY (id_richiesta_supporto) REFERENCES moon_fo_t_richiesta_supporto (id_richiesta_supporto)
);

CREATE TABLE IF NOT EXISTS moon_fo_t_allegato_messaggio_supporto (
	id_allegato_messaggio_supporto SERIAL PRIMARY KEY,
	id_messaggio_supporto INT NOT NULL,
	nome character varying(255) NOT NULL,
	dimensione BIGINT NOT NULL,
	contenuto bytea NOT NULL,
	content_type character varying(255) NOT NULL,
	FOREIGN KEY (id_messaggio_supporto) REFERENCES moon_fo_t_messaggio_supporto (id_messaggio_supporto)
);


-- ------------------------------------------------------------------------------------------
-- 

CREATE TABLE IF NOT EXISTS moon_fo_r_portale_ente (
	id_portale INT NOT NULL,
	id_ente INT NOT NULL,
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_portale) REFERENCES moon_fo_d_portale (id_portale),
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	PRIMARY KEY (id_portale,id_ente)
);


-- ------------------------------------------------------------------------------------------
-- Per persistenza del PDF

CREATE TABLE moon_fo_t_pdf_istanza
(
	id_pdf_istanza BIGSERIAL PRIMARY KEY,
	id_istanza BIGINT NOT NULL,
	id_modulo BIGINT NOT NULL,
	hash_pdf character varying(64) NOT NULL,
	contenuto_pdf bytea NULL,
	resoconto text,
	data_ins TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	data_upd TIMESTAMP NULL,
	attore_upd VARCHAR (16) NULL,
	uuid_index varchar(36),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

ALTER TABLE moon_fo_t_pdf_istanza
   ADD CONSTRAINT ak_moon_fo_t_pdf_istanza_id_istanza UNIQUE (id_istanza);
CREATE UNIQUE INDEX idx_moon_fo_t_pdf_istanza_id_istanza ON moon_fo_t_pdf_istanza USING btree (id_istanza);


-- ------------------------------------------------------------------------------------------
-- Ticketing System (NextCRM)
-- 

CREATE TABLE IF NOT EXISTS moon_ts_t_richiesta (
	id_richiesta BIGSERIAL PRIMARY KEY,
	data_richiesta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	codice_richiesta VARCHAR(100) NOT NULL, -- simile uuid generato da moon ma piu parlante es. CODICE_ISTANZA + qualcosa
	uuid_richiesta VARCHAR(100) NOT NULL,
	stato VARCHAR(30) NOT NULL, -- TX, OK, KO
	tipo_doc INT NOT NULL, -- 1-ISTANZA (2-ALLEGATO)
	id_istanza BIGINT,
	id_allegato_istanza BIGINT,
	id_file BIGINT,
	id_modulo INT,
	id_area INT,
	id_ente INT NOT NULL,
	id_ticketing_system INT NULL, -- 1-NEXTCRM
	uuid_ticketing_system VARCHAR(100) NULL,
	note VARCHAR(2000) NULL, -- 
	codice_esito VARCHAR(100) NULL, -- esito finale (201)
	desc_esito VARCHAR(255) NULL, -- esito finale
	data_upd TIMESTAMP NULL,
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_allegato_istanza) REFERENCES moon_fo_t_allegati_istanza (id_allegato),
	FOREIGN KEY (id_file) REFERENCES moon_fo_t_repository_file (id_file)
);


-- ------------------------------------------------------------------------------------------
-- Add DecodificaTipoLogEmail
-- 

CREATE TABLE IF NOT EXISTS moon_fo_d_tipo_email (
	id_tipo_email SERIAL PRIMARY KEY,
	cod_tipo_email VARCHAR(32) NOT NULL,
	descrizione_tipo_email VARCHAR(400),
	fl_attivo CHAR(1) NOT NULL DEFAULT 'S',
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN'
);

insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 1, 'FO_INVIO_ISTANZA', 'Conferma di invio istanza da MOOn FrontOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 2, 'BO_RICHIESTA_INTEGRAZIONE', 'Richiesta di integrazione da MOOn BackOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 3, 'FO_RISPOSTA_INTEGRAZIONE', 'Conferma di invio della risposta di integrazione da MOOn FrontOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 4, 'BO_RESPINGI_EMAIL', 'Notifica di rispingimento da MOOn BackOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 5, 'BO_INVIA_COMUNICAZIONE', 'Invia communicazione da MOOn BackOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 6, 'BO_INVIO_ISTANZA', 'Conferma di invio istanza da MOOn BackOffice');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 7, 'API_ALLEGATO_AZIONE_CAMBIO_STATO', 'Allegato inviato cambio stato via API');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 8, 'API_ALLEGATO_NOTIFICA', 'Allegato notifica via API');
insert INTO moon_fo_d_tipo_email (id_tipo_email,cod_tipo_email,descrizione_tipo_email) values ( 9, 'SRV_PRT_IN_CALLBACK', 'Callback di Protocollazione in ingresso da moonsrv');


-- ------------------------------------------------------------------------------------------
-- gestione pagamenti (EPAY)

-- Traciamento Ricevuta Telematica (contiene esiti positivi e negativi)
CREATE TABLE IF NOT EXISTS moon_ep_t_ricevuta_telem_testa (
	id_ricevuta_telem_testa BIGSERIAL PRIMARY KEY,
	id_messaggio VARCHAR(35) NULL,
	cf_ente_creditore VARCHAR(35) NULL,
	codice_versamento VARCHAR(4) NULL,
	numero_rt SMALLINT NULL,
	data_ins TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS moon_ep_t_ricevuta_telematica (
	id_ricevuta_telematica BIGSERIAL PRIMARY KEY,
	id_ricevuta_telem_testa BIGINT NOT NULL,
	id VARCHAR(50) NULL,
	xml text NULL,
	data_ins TIMESTAMP NOT NULL,
	FOREIGN KEY (id_ricevuta_telem_testa) REFERENCES moon_ep_t_ricevuta_telem_testa (id_ricevuta_telem_testa)
);

-- Traciamento Notifiche Pagamento (contiene solo esiti positivi)
CREATE TABLE IF NOT EXISTS moon_ep_t_notifica_pagam_testa (
	id_notifica_pagam_testa BIGSERIAL PRIMARY KEY,
	id_messaggio VARCHAR(35) NULL,
	cf_ente_creditore VARCHAR(35) NULL,
	codice_versamento VARCHAR(4) NULL,
	pagamenti_spontanei CHAR(1) NOT NULL DEFAULT 'N',
	numero_pagamenti SMALLINT NULL,
	importo_totale_pagamenti DECIMAL(10,2) NULL,
	data_ins TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS moon_ep_t_notifica_pagamento (
	id_notifica_pagamento BIGSERIAL PRIMARY KEY,
	id_notifica_pagam_testa BIGINT NOT NULL,
	id_posizione_debitoria VARCHAR(50) NULL,
	anno_di_riferimento SMALLINT NULL,
	iuv VARCHAR(35) NULL,
	importo_pagato DECIMAL(10,2) NULL,
	data_scadenza DATE NULL,
	desc_causale_versamento VARCHAR(140) NULL,
	data_esito_pagamento DATE NULL,
	soggetto_debitore TEXT NULL,
	soggetto_versante TEXT NULL,
	dati_transazione_psp TEXT NULL,
	dati_specifici_riscossione TEXT NULL,
	note TEXT NULL,
	codice_avviso VARCHAR(35) NULL,
	data_ins TIMESTAMP NOT NULL,
	FOREIGN KEY (id_notifica_pagam_testa) REFERENCES moon_ep_t_notifica_pagam_testa (id_notifica_pagam_testa)
);

-- Richieste di Pagamento (creazione di IUV / CodiceAvviso)
CREATE TABLE IF NOT EXISTS moon_ep_t_richiesta (
	id_richiesta BIGSERIAL PRIMARY KEY,
	id_istanza BIGINT NOT NULL,
	id_modulo BIGINT,
	id_tipologia_epay INT NOT NULL,
	id_storico_workflow BIGINT NULL,
	id_epay VARCHAR(255) NOT NULL,
	iuv VARCHAR(255) NULL,
	codice_avviso VARCHAR(255) NULL,
	data_ins TIMESTAMP NOT NULL,
	data_del TIMESTAMP NULL,
	attore_ins VARCHAR(16) NULL,
	attore_del VARCHAR(16) NULL,
	richiesta text NOT NULL,
	risposta text NULL,
	codice_esito VARCHAR(30) NULL,
	desc_esito VARCHAR(255) NULL,
	id_ricevuta_telematica_positiva BIGINT NULL,
	data_ricevuta_telematica_positiva TIMESTAMP NULL,
	id_notifica_pagamento BIGINT NULL,
	data_notifica_pagamento TIMESTAMP NULL,
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_ricevuta_telematica_positiva) REFERENCES moon_ep_t_ricevuta_telematica (id_ricevuta_telematica),
	FOREIGN KEY (id_notifica_pagamento) REFERENCES moon_ep_t_notifica_pagamento (id_notifica_pagamento)
);

-- moon_ep_t_richiesta_ak_id_epay
CREATE UNIQUE INDEX CONCURRENTLY idx_moon_ep_t_richiesta_unique_id_epay_idx ON moon_ep_t_richiesta (id_epay);
ALTER TABLE moon_ep_t_richiesta ADD CONSTRAINT moon_ep_t_richiesta_ak_id_epay 
UNIQUE USING INDEX moon_ep_t_richiesta_unique_id_epay_idx;

CREATE UNIQUE INDEX moon_ep_t_richiesta_unique_codice_avviso_idx ON moon_ep_t_richiesta USING btree (codice_avviso);
ALTER TABLE moon_ep_t_richiesta ADD CONSTRAINT moon_ep_t_richiesta_ak_codice_avviso 
UNIQUE USING INDEX moon_ep_t_richiesta_unique_codice_avviso_idx;

-- tabella componenti pagamento
CREATE TABLE IF NOT EXISTS moon_ep_t_componente_pagamento (
	id_componente_pagamento SERIAL PRIMARY KEY,
	id_modulo INT NOT NULL,
	anno_accertamento VARCHAR (30) NOT NULL,
	numero_accertamento VARCHAR (20) NOT NULL,
	causale VARCHAR(140) NULL,
	importo DECIMAL(10,2) NOT NULL,
	dati_specifici_riscossione TEXT NULL,
	codice_tipo_versamento VARCHAR (20) NULL,
	ordine INT NOT NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);       


-- 
-- Add LogErrori
-- 

CREATE TABLE IF NOT EXISTS moon_l_errori (
	uuid VARCHAR(36) PRIMARY KEY,
	id_componente INT NOT NULL,
	inet_adress VARCHAR(255) NULL,
	data_ins TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_ins VARCHAR(36) NULL,
	codice VARCHAR(50) NULL,
	message VARCHAR(255) NULL,
	class_name VARCHAR(50) NULL,
	method_name VARCHAR(50) NULL,
	id_istanza BIGINT,
	id_modulo BIGINT,
	ex_class_name VARCHAR(255) NULL,
	ex_message text NULL,
	ex_cause text NULL,
	ex_trace text NULL,
	info text NULL,
	elapsed_time_ms INT,
	note VARCHAR(255) NULL,
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza)
);


-- 
-- Added INDEX
-- 3 commented unused biggest

CREATE INDEX CONCURRENTLY idx_moon_fo_t_dati_istanza_id_istanza ON moon_fo_t_dati_istanza USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_dati_istanza_id_cronologia_stati ON moon_fo_t_dati_istanza USING btree (id_cronologia_stati);
-- CREATE INDEX CONCURRENTLY idx_moon_fo_t_dati_istanza_id_tipo_modifica ON moon_fo_t_dati_istanza USING btree (id_tipo_modifica);

DROP INDEX idx_moon_fo_t_dati_istanza_id_tipo_modifica;
DROP INDEX idx_moon_fo_t_cronologia_stati_id_azione_svolta;
DROP INDEX moon_wf_t_storico_workflow_id_file_rendering;

-- CREATE INDEX CONCURRENTLY idx_moon_fo_t_cronologia_stati_id_azione_svolta ON moon_fo_t_cronologia_stati USING btree (id_azione_svolta);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_cronologia_stati_id_istanza ON moon_fo_t_cronologia_stati USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_cronologia_stati_id_stato_wf ON moon_fo_t_cronologia_stati USING btree (id_stato_wf);

CREATE INDEX CONCURRENTLY idx_moon_wf_t_storico_workflow_id_azione ON moon_wf_t_storico_workflow USING btree (id_azione);
-- CREATE INDEX CONCURRENTLY idx_moon_wf_t_storico_workflow_id_file_rendering ON moon_wf_t_storico_workflow USING btree (id_file_rendering);
CREATE INDEX CONCURRENTLY idx_moon_wf_t_storico_workflow_id_processo ON moon_wf_t_storico_workflow USING btree (id_processo);
CREATE INDEX CONCURRENTLY idx_moon_wf_t_storico_workflow_id_stato_wf_arrivo ON moon_wf_t_storico_workflow USING btree (id_stato_wf_arrivo);
CREATE INDEX CONCURRENTLY idx_moon_wf_t_storico_workflow_id_stato_wf_partenza ON moon_wf_t_storico_workflow USING btree (id_stato_wf_partenza);

CREATE INDEX CONCURRENTLY idx_moon_fo_t_istanza_id_ente ON moon_fo_t_istanza USING btree (id_ente);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_istanza_id_modulo ON moon_fo_t_istanza USING btree (id_modulo);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_istanza_id_stato_wf ON moon_fo_t_istanza USING btree (id_stato_wf);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_istanza_id_modulo_id_versione_modulo ON moon_fo_t_istanza USING btree (id_modulo,id_versione_modulo);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_istanza_importanza ON moon_fo_t_istanza USING btree (importanza);

CREATE INDEX CONCURRENTLY idx_moon_fo_t_allegati_istanza_id_istanza ON moon_fo_t_allegati_istanza USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_allegati_istanza_formio_name_file ON moon_fo_t_allegati_istanza (formio_name_file);

-- OPTIONAL SOLO SU COTO-01
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_allegato_istanza ON moon_prar_t_richiesta USING btree (id_allegato_istanza);
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_area ON moon_prar_t_richiesta USING btree (id_area);
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_ente ON moon_prar_t_richiesta USING btree (id_ente);
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_file ON moon_prar_t_richiesta USING btree (id_file);
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_istanza ON moon_prar_t_richiesta USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_prar_t_richiesta_id_modulo ON moon_prar_t_richiesta USING btree (id_modulo);

CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_allegato_istanza ON moon_pr_t_richiesta USING btree (id_allegato_istanza);
CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_area ON moon_pr_t_richiesta USING btree (id_area);
CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_ente ON moon_pr_t_richiesta USING btree (id_ente);
CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_file ON moon_pr_t_richiesta USING btree (id_file);
CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_istanza ON moon_pr_t_richiesta USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_id_modulo ON moon_pr_t_richiesta USING btree (id_modulo);

CREATE INDEX CONCURRENTLY idx_moon_fo_t_log_email_id_ente ON moon_fo_t_log_email USING btree (id_ente);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_log_email_id_istanza ON moon_fo_t_log_email USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_log_email_id_modulo ON moon_fo_t_log_email USING btree (id_modulo);

CREATE INDEX CONCURRENTLY idx_moon_fo_t_repository_file_id_istanza ON moon_fo_t_repository_file USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_fo_t_repository_file_id_storico_workflow ON moon_fo_t_repository_file USING btree (id_storico_workflow);

CREATE INDEX CONCURRENTLY idx_moon_wf_t_fruitore_dati_azione_id_istanza ON moon_wf_t_fruitore_dati_azione USING btree (id_istanza);
CREATE INDEX CONCURRENTLY idx_moon_wf_t_fruitore_dati_azione_id_storico_workflow ON moon_wf_t_fruitore_dati_azione USING btree (id_storico_workflow);

CREATE INDEX CONCURRENTLY idx_moon_fo_t_pdf_istanza_id_modulo ON moon_fo_t_pdf_istanza USING btree (id_modulo);

CREATE INDEX CONCURRENTLY idx_moon_pr_t_richiesta_uuid_protocollatore ON moon_pr_t_richiesta USING btree (uuid_protocollatore);


CREATE TABLE moon_io_d_modulo_class (
	id bigserial NOT NULL,
	id_modulo INT NOT NULL,
	contenuto bytea NOT NULL,
	nome_class text NOT NULL,
	tipologia INT NULL,
	data_ora timestamp NOT NULL DEFAULT now(),
	CONSTRAINT moon_io_d_modulo_class_pkey PRIMARY KEY (id),
	CONSTRAINT moon_io_d_modulo_class_id_modulo_fkey FOREIGN KEY (id_modulo) REFERENCES moon.moon_io_d_modulo(id_modulo)
);


CREATE TABLE moon.moon_wf_t_fruitore_attributo (
	id_attributo serial NOT NULL,
	id_fruitore int4 NOT NULL,
	nome_attributo varchar(255) NULL,
	valore varchar(2000) NULL,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
	CONSTRAINT moon_wf_t_fruitore_attributo_pkey PRIMARY KEY (id_attributo),
	CONSTRAINT moon_wf_t_fruitore_attributo_id_fruitore_nome_attributo_key UNIQUE (id_fruitore, nome_attributo),
	FOREIGN KEY (id_fruitore) REFERENCES moon.moon_wf_d_fruitore(id_fruitore)
);



-- 
-- Archiviazione STARDAS (senza Protocollo) (Usato su COTO TRIBUTI)
-- 
CREATE TABLE moon.moon_prar_t_richiesta (
	id_richiesta bigserial NOT NULL,
	data_richiesta timestamp NOT NULL DEFAULT now(),
	codice_richiesta varchar(100) NOT NULL,
	uuid_richiesta varchar(100) NOT NULL,
	stato varchar(30) NOT NULL,
	tipo_ing_usc int4 NOT NULL,
	tipo_doc int4 NOT NULL,
	id_istanza int8 NULL,
	id_allegato_istanza int8 NULL,
	id_file int8 NULL,
	id_modulo int4 NULL,
	id_area int4 NULL,
	id_ente int4 NOT NULL,
	id_protocollatore int4 NULL,
	uuid_protocollatore varchar(100) NULL,
	note varchar(2000) NULL,
	codice_esito varchar(100) NULL,
	desc_esito varchar(255) NULL,
	data_upd timestamp NULL,
	CONSTRAINT moon_prar_t_richiesta_pkey PRIMARY KEY (id_richiesta)
);
CREATE INDEX idx_moon_prar_t_richiesta_id_allegato_istanza ON moon_prar_t_richiesta USING btree (id_allegato_istanza);
CREATE INDEX idx_moon_prar_t_richiesta_id_area ON moon_prar_t_richiesta USING btree (id_area);
CREATE INDEX idx_moon_prar_t_richiesta_id_ente ON moon_prar_t_richiesta USING btree (id_ente);
CREATE INDEX idx_moon_prar_t_richiesta_id_file ON moon_prar_t_richiesta USING btree (id_file);
CREATE INDEX idx_moon_prar_t_richiesta_id_istanza ON moon_prar_t_richiesta USING btree (id_istanza);
CREATE INDEX idx_moon_prar_t_richiesta_id_modulo ON moon_prar_t_richiesta USING btree (id_modulo);

-- moon.moon_prar_t_richiesta foreign keys
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_allegato_istanza_fkey FOREIGN KEY (id_allegato_istanza) REFERENCES moon.moon_fo_t_allegati_istanza(id_allegato);
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_area_fkey FOREIGN KEY (id_area) REFERENCES moon.moon_fo_d_area(id_area);
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_ente_fkey FOREIGN KEY (id_ente) REFERENCES moon.moon_fo_d_ente(id_ente);
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_file_fkey FOREIGN KEY (id_file) REFERENCES moon.moon_fo_t_repository_file(id_file);
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_istanza_fkey FOREIGN KEY (id_istanza) REFERENCES moon.moon_fo_t_istanza(id_istanza);
ALTER TABLE moon.moon_prar_t_richiesta ADD CONSTRAINT moon_prar_t_richiesta_id_modulo_fkey FOREIGN KEY (id_modulo) REFERENCES moon.moon_io_d_modulo(id_modulo);


CREATE TABLE moon.moon_prar_d_parametri (
	id_parametro serial4 NOT NULL,
	id_ente int4 NOT NULL,
	id_area int4 NULL,
	id_modulo int4 NULL,
	nome_attributo varchar(255) NULL,
	valore varchar(2000) NULL,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
	CONSTRAINT moon_prar_d_parametri_pkey PRIMARY KEY (id_parametro)
);
CREATE UNIQUE INDEX ak2_moon_prar_d_parametri ON moon_prar_d_parametri USING btree (id_ente, nome_attributo) WHERE ((id_area IS NULL) AND (id_modulo IS NULL));
CREATE UNIQUE INDEX ak3_moon_prar_d_parametri ON moon_prar_d_parametri USING btree (id_ente, id_area, nome_attributo) WHERE ((id_area IS NOT NULL) AND (id_modulo IS NULL));
CREATE UNIQUE INDEX ak4_moon_prar_d_parametri ON moon_prar_d_parametri USING btree (id_ente, id_area, id_modulo, nome_attributo) WHERE ((id_area IS NOT NULL) AND (id_modulo IS NOT NULL));

-- moon.moon_prar_d_parametri foreign keys
ALTER TABLE moon.moon_prar_d_parametri ADD CONSTRAINT moon_prar_d_parametri_id_area_fkey FOREIGN KEY (id_area) REFERENCES moon.moon_fo_d_area(id_area);
ALTER TABLE moon.moon_prar_d_parametri ADD CONSTRAINT moon_prar_d_parametri_id_ente_fkey FOREIGN KEY (id_ente) REFERENCES moon.moon_fo_d_ente(id_ente);



-- 
-- DoQui INDEX Archiviazione Documenti (PDF Istanza & Allegati)
--
CREATE TABLE moon.moon_idx_t_richiesta (
    id_richiesta bigserial NOT NULL,
    data_inizio timestamp NOT NULL,
    data_fine timestamp,
    id_modulo int4 NOT NULL,
    data_filter timestamp,
    istanze_proc int8, 
    CONSTRAINT moon_idx_t_richiesta_pkey PRIMARY KEY (id_richiesta),
    CONSTRAINT moon_idx_t_richiesta_id_modulo_fkey FOREIGN KEY (id_modulo) REFERENCES moon.moon_io_d_modulo(id_modulo)
);

CREATE TABLE moon.moon_idx_t_richiesta_dettaglio (
    id_richiesta bigserial not null,
    tipo_file smallint NOT NULL,
    id_istanza int8 NOT NULL,
    id_allegato int8  null,
    id_file int8  null,
    stato varchar(3) not NULL,
    desc_stato varchar(255) NULL,
    CONSTRAINT moon_idx_t_richiesta_dettaglio_id_richiesta_fkey FOREIGN KEY (id_richiesta) REFERENCES moon.moon_idx_t_richiesta(id_richiesta),
    CONSTRAINT moon_idx_r_richiesta_dettaglio_id_istanza_fkey FOREIGN KEY (id_istanza) REFERENCES moon.moon_fo_t_istanza(id_istanza),
    CONSTRAINT moon_idx_r_richiesta_dettaglio_id_allegato_fkey FOREIGN KEY (id_allegato) REFERENCES moon.moon_fo_t_allegati_istanza(id_allegato),
    CONSTRAINT moon_idx_r_richiesta_dettaglio_id_file_fkey FOREIGN KEY (id_file) REFERENCES moon.moon_fo_t_repository_file(id_file)
);

---
--- Custom components form.io in json format
---
CREATE TABLE IF NOT EXISTS moon.moon_bo_t_formio_custom_components (
	id_component varchar(50) NOT NULL,
	json_component text NOT NULL,
	CONSTRAINT moon_bo_t_formio_custom_components_pkey PRIMARY KEY (id_component)
);

--moon_fo_r_utente_funzione 
CREATE TABLE IF NOT EXISTS moon_fo_r_utente_funzione (
	id_utente int4 NOT NULL,
	id_funzione int4 NOT NULL,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
	CONSTRAINT moon_fo_r_utente_funzione_pkey PRIMARY KEY (id_utente, id_funzione),
	FOREIGN KEY (id_funzione) REFERENCES moon_fo_d_funzione (id_funzione),
	FOREIGN KEY (id_utente) REFERENCES moon_fo_t_utente (id_utente)
);

--moon_fo_r_utente_ruolo
CREATE TABLE IF NOT EXISTS moon_fo_r_utente_ruolo (
	id_utente int4 NOT NULL,
	id_ruolo int4 NOT NULL,
	data_upd timestamp NULL DEFAULT now(),
	attore_upd varchar(16) NOT NULL DEFAULT 'ADMIN'::character varying,
	CONSTRAINT moon_fo_r_utente_ruolo_pkey PRIMARY KEY (id_utente, id_ruolo),
	FOREIGN KEY (id_utente) REFERENCES moon_fo_t_utente (id_utente),
	FOREIGN KEY (id_ruolo) REFERENCES moon_fo_d_ruolo (id_ruolo)
);


-- 
-- MyDocs (DOCME)
-- 
CREATE TABLE IF NOT EXISTS moon_md_d_parametri (
	id_parametro SERIAL PRIMARY KEY,
	id_ente INT NOT NULL,
	id_area INT,
	id_modulo INT,
	nome_attributo VARCHAR (255),
	valore VARCHAR (2000),
	data_upd TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	attore_upd VARCHAR (16) NOT NULL DEFAULT 'ADMIN',
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo)
);

CREATE UNIQUE INDEX ak4_moon_md_d_parametri ON moon_md_d_parametri (id_ente, id_area, id_modulo, nome_attributo)
WHERE id_area IS NOT NULL 
AND id_modulo IS NOT NULL;

CREATE UNIQUE INDEX ak3_moon_md_d_parametri ON moon_md_d_parametri (id_ente, id_area, nome_attributo)
WHERE id_area IS NOT NULL 
AND id_modulo IS NULL;

CREATE UNIQUE INDEX ak2_moon_md_d_parametri ON moon_md_d_parametri (id_ente, nome_attributo)
WHERE id_area IS NULL 
AND id_modulo IS NULL;


CREATE TABLE IF NOT EXISTS moon_md_t_richiesta (
	id_richiesta BIGSERIAL PRIMARY KEY,
	data_richiesta TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	stato VARCHAR(30) NOT NULL, -- TX, OK, KO
	tipo_doc INT NOT NULL, -- 1-ISTANZA 2-DOC_PA
	id_istanza BIGINT,
	id_allegato_istanza BIGINT,
	id_file BIGINT,
	id_modulo INT,
	id_area INT,
	id_ente INT NOT NULL,
	id_storico_workflow BIGINT,
	id_ambito_mydocs INT NOT NULL,
	id_tipologia_mydocs INT NOT NULL,
	uuid_mydocs VARCHAR(100) NULL,
	note VARCHAR(2000) NULL, -- result_code smistamento intermedie
	codice_esito VARCHAR(100) NULL, -- esito finale
	desc_esito VARCHAR(255) NULL, -- esito finale
	data_upd TIMESTAMP NULL,
	FOREIGN KEY (id_ente) REFERENCES moon_fo_d_ente (id_ente),
	FOREIGN KEY (id_area) REFERENCES moon_fo_d_area (id_area),
	FOREIGN KEY (id_modulo) REFERENCES moon_io_d_modulo (id_modulo),
	FOREIGN KEY (id_istanza) REFERENCES moon_fo_t_istanza (id_istanza),
	FOREIGN KEY (id_allegato_istanza) REFERENCES moon_fo_t_allegati_istanza (id_allegato),
	FOREIGN KEY (id_file) REFERENCES moon_fo_t_repository_file (id_file),
	FOREIGN KEY (id_storico_workflow) REFERENCES moon_wf_t_storico_workflow (id_storico_workflow)
);






