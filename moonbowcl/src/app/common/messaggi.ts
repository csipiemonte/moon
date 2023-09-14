/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Messaggi {
  public static msgSubmitForm = 'Modulo inviato o salvato  correttamente';
  public static msgErrForm = 'Errore generico sul Form';
  public static msgModified = 'Istanza modificata';
  
  public static messaggioConfermaInvio = "Confermi di voler inviare l'istanza all'ufficio competente?";
  public static messaggioConfermaEliminazione = 'Confermi di voler eliminare questa istanza?';
  public static messaggioUscita = 'Non hai effettuato nessun salvataggio, sei sicuro di voler uscire senza salvare?';
  public static messaggioConfermaRiportaInBozza = 'Confermi di voler riportare questa istanza allo stato bozza?';
  public static messaggioConfermaCompleta = "Confermi di voler salvare, uscire dalla compilazione e proseguire al passo successivo?";

  public static messaggioEsitoCompletamentoSiDescr1 = "L'istanza è stata salvata correttamente, ma NON è ancora stata INVIATA.";
  public static messaggioEsitoCompletamentoSiDescr2 = "Per inviarla all'ufficio competente DEVI premere sul pulsante \"Invia istanza\"";
  public static messaggioEsitoCompletamentoNoDescr1 = "L'istanza è stata salvata, ma NON è ancora stata INVIATA.";
  public static messaggioEsitoCompletamentoNoDescr2 = "Per inviarla all'ufficio competente DEVI premere sul pulsante \"Invia istanza\"";

  public static messaggioDatiNonCompleti = "Attenzione! I dati presenti nell'istanza non risultano completi. Si consiglia di riportare in bozza l'istanza e verificare. Se il problema si ripresenta, potrebbe essere dovuto al browser utilizzato. Provare a cambiare il browser in uso.";

  public static messaggioInformativaRPCCSR = "Dichiaro che i dati disponibili saranno trattati secondo quanto previsto dal d.lgs.196/2003 “Codice in materia di protezione dei dati personali” e dal “Regolamento UE 2016/679 (GDPR)";

  public static msgErroreAzione1 = "L'azione ";
  public static msgErroreAzione2 = " non è andata a buon fine";
  public static msgAzioneKO = 'Azione fallita';
  public static msgAzioneOK = 'Azione completata';

  public static OK = "Salvataggio effettuato con successo";
}

export class ErrorRest {
  constructor(public type: TypeErrorRest, public message?: string, public codice?: string, ) { }
}

export enum TypeErrorRest {
  TIMEOUT,
  SCONOSCIUTO,
  OK,
  UNAUTHORIZED
}

export enum ServiziError {
  GENERIC = 'Attenzione! Si è verificato un errore di comunicazione con il server! Contattare l\'assistenza.',
  TIMEOUT = 'Attenzione! La rete da cui si è collegati risulta essere troppo lenta',
  UNAUTHORIZED = 'Attenzione! L\'utente non risulta autorizzato ad accedere alla risorsa richiesta',
  SERVICE_NOT_FOUND = 'Servizio non trovato',
  USER_NOT_QUALIFIED = 'Utente non abilitato al servizio'
}


export enum MsgCercaIstanza {
  MODULO_OBBLIGATORIO = 'È obbligatorio selezionare un modulo.',
  COMUNE_OBBLIGATORIO = 'È obbligatorio selezionare un comune',
  ENTE_OBBLIGATORIO = 'È obbligatorio selezionare un ente',
  ERRORE_INTERVALLO_DATE = 'Errore intervallo date',
  ERRORE_UTENTE_NON_ABILITATO = 'Utente non abilitato',
  ERRORE_STATI_MODULO = 'Selezionare una tipologia di modulo',
  ISTANZE_NON_PRESENTI = 'Non è presente alcuna istanza corrispendente al filtro impostato',
  STATI_NON_DISPONIBILI = "Nessuno stato disponibile"
}

export enum MsgExport {
  MODULO_OBBLIGATORIO = 'È obbligatorio selezionare un modulo',
  COMUNE_OBBLIGATORIO = 'È obbligatorio selezionare un comune',
  ENTE_OBBLIGATORIO = 'È obbligatorio selezionare un ente',
  ERRORE_INTERVALLO_DATE = 'Errore intervallo date',
  ERRORE_UTENTE_NON_ABILITATO = 'Utente non abilitato',
  ERRORE_STATI_MODULO = 'Selezionare una tipologia di modulo',
  ISTANZE_NON_PRESENTI = 'Non è presente alcuna istanza corrispendente al filtro impostato',
  STATI_NON_DISPONIBILI = 'Nessuno stato disponibile',
  UNICA_ISTANZA = 'È presente una sola istanza corrispondente al filtro di ricerca impostato',
  PRESENTI = 'Sono presenti',
  FILTRO_IMPOSTATO = 'istanze corrispondenti al filtro di ricerca impostato',
  RESTRIZIONE_RICERCA = 'restringere la ricerca o selezionare un intervallo',
  INTERVALLO_DATE = 'Selezionare un intervallo',
  CODICE_ESTRAZIONE_OBLIGATORIO = "Codice estrazione è obligatorio"
}

export enum MsgIstanze {
  NO_ISTANZE = 'Non sono presenti istanze',
  ERRORE_UTENTE_NON_ABILITATO = 'Utente non abilitato', 
  MODULO_OBBLIGATORIO = 'È obbligatorio selezionare un modulo',
  ALERT_CANCELLAZIONE = 'Confermi la cancellazione ?'
}

export enum MsgModulo {
  SUCCESS_MODULO = 'Modulo salvato correttamente',
  ERROR_MODULO = 'Errore salvataggio modulo',
  ERRORE_CATEGORIE = 'Errore recupero Categorie', 
  ERRORE_PROCESSI = 'Errore recupero Elenco Processi',
  ERRORE_PORTALI = 'Errore recupero Elenco Portali',
  ERRORE_CODICI_TIPO_ISTANZA = 'Errore recupero Elenco TipoCodiceIstanza'
}

export enum MsgStato { 
  ERRORE_PASSAGGIO_DI_STATO = 'Impossibile effettuare il cambio di stato', 
  SUCCESS_PASSAGGIO_DI_STATO = 'Cambio di stato effettuato con successo',
}

export enum MsgVersioni { 
  SUCCESS_NUOVA_VERSIONE = 'Nuova versione creata correttamente', 
}

export enum MsgDatiGenerali {
  SUCCESS_DATI_GENERALI = 'Salvataggio dati generali effettuato con successo',   
}

export enum MsgAttrGenerali {
  ERROR_ATTR_GENERALI = 'Impossibile effettuare il salvataggio attributi generali', 
  SUCCESS_ATTR_GENERALI = 'Salvataggio attributi generali effettuato con successo', 
  INFO_NO_MODIFY_ATTR_GENERALI = 'Nessuna modifica effettuata', 
}

export enum MsgAttrEmail {
  SUCCESS_ATTR_EMAIL = 'Salvataggio attributi email effettuato con successo', 
  ERROR_ATTR_EMAIL = 'Impossibile effettuare il salvataggio attributi email', 
  INFO_NO_MODIFY_ATTR_EMAIL = 'Nessuna modifica effettuata', 
}

export enum MsgAttrNotificatore {
  SUCCESS_ATTR_NOTIFICATORE = 'Salvataggio attributi notificatore effettuato con successo', 
  ERROR_ATTR_NOTIFICATORE= 'Impossibile effettuare il salvataggio notificatore', 
  INFO_NO_MODIFY_ATTR_NOTIFICATORE = 'Nessuna modifica effettuata', 
}

export enum MsgAttrProtocollo {
  SUCCESS_ATTR_PROTOCOLLO = 'Salvataggio attributi protocollo effettuato con successo', 
  ERROR_ATTR_PROTOCOLLO = 'Impossibile effettuare il salvataggio protocollo', 
  INFO_NO_MODIFY_ATTR_PROTOCOLLO = 'Nessuna modifica effettuata', 
}

export enum MsgAttrCosmo {
  SUCCESS_ATTR_COSMO = 'Salvataggio attributi WF Cosmo effettuato con successo', 
  ERROR_ATTR_COSMO = 'Impossibile effettuare il salvataggio WF Cosmo', 
  INFO_NO_MODIFY_ATTR_COSMO = 'Nessuna modifica effettuata', 
}

export enum MsgAttrAzioni {
  SUCCESS_ATTR_AZIONI = 'Salvataggio attributi WF Azioni effettuato con successo', 
  ERROR_ATTR_AZIONI = 'Impossibile effettuare il salvataggio WF Azioni', 
  INFO_NO_MODIFY_ATTR_AZIONI = 'Nessuna modifica effettuata', 
}

export enum MsgAttrEstraiDichiarante {
  ERROR_ATTR_ESTRAI_DICH = 'Impossibile effettuare il salvataggio attributi Estrai dichiarante', 
  SUCCESS_ATTR_ESTRAI_DICH = 'Salvataggio attributi Estrai dichiarante effettuato con successo', 
  INFO_NO_MODIFY_ATTR_ESTRAI_DICH = 'Nessuna modifica effettuata', 
}

export enum MsgAttrCrm {
  ERROR_ATTR_CRM = 'Impossibile effettuare il salvataggio attributi CRM', 
  ERROR_CONF_ATTR_CRM = 'Configurazione non valida',
  SUCCESS_ATTR_CRM = 'Salvataggio attributi CRM effettuato con successo', 
  INFO_NO_MODIFY_ATTR_CRM = 'Nessuna modifica effettuata', 
}

export enum MsgAttrEpay {
  ERROR_ATTR_EPAY = 'Impossibile effettuare il salvataggio attributi EPAY', 
  SUCCESS_ATTR_EPAY = 'Salvataggio attributi EPAY effettuato con successo', 
  INFO_NO_MODIFY_ATTR_EPAY = 'Nessuna modifica effettuata', 
}


