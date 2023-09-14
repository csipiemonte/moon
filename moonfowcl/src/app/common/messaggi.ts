/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export abstract class Messaggi {
  public static msgSubmitForm = 'Corretto. Puoi proseguire';
  public static msgErrForm = 'Errore generico sul Form';
  // tslint:disable-next-line: quotemark
  public static messaggioConferma = 'Attenzione! Proseguendo i dati di ' +
    // tslint:disable-next-line: quotemark
    'questa istanza verranno inviati all\'ufficio competente e non potranno più essere modificati.';
  // tslint:disable-next-line: quotemark
  public static messaggioConfermaInvio = 'Confermi di voler inviare l\'istanza all\'ufficio competente?';
  public static messaggioConfermaEliminazione = 'Confermi di voler eliminare questa istanza?';
  public static messaggioConfermaDuplicaIstanza = 'Confermi di voler duplicare questa istanza?';
  public static messaggioUscita = 'Non hai effettuato nessun salvataggio, sei sicuro di voler uscire senza salvare?';
  public static messaggioConfermaRiportaInBozza = 'Confermi di voler riportare questa istanza allo stato bozza?';
  public static messaggioConfermaCompleta = 'Confermi di voler salvare, uscire dalla compilazione e proseguire al passo successivo?';
  public static messaggioEsitoCompletamentoSiDescr1 = 'L\'istanza è stata salvata correttamente, ma NON è ancora stata INVIATA.';
  public static messaggioEsitoCompletamentoSiDescr2 = 'Per inviarla all\'ufficio competente DEVI premere sul pulsante "Invia istanza"';
  public static messaggioEsitoCompletamentoNoDescr1 = 'L\'istanza è stata salvata, ma NON è ancora stata INVIATA.';
  public static messaggioEsitoCompletamentoNoDescr2 = 'Per inviarla all\'ufficio competente DEVI premere sul pulsante "Invia istanza"';
  public static messaggioDatiNonCompleti = 'Attenzione! I dati presenti nell\'istanza non risultano completi. Si consiglia di riportare in bozza l\'istanza e verificare. Se il problema si ripresenta, potrebbe essere dovuto al browser utilizzato. Provare a cambiare il browser in uso.';
  public static istruzioniIntegrazione = 'Compila e invia la richiesta di integrazione all\'ufficio competente, inserendo il testo e allegando i documenti necessari';
  public static messaggioAutenticazioneFallita = 'Processo di autenticazione fallito, impossibile accedere al servizio';
  public static messaggioUtentNoAuthIstanza = 'Utente non autorizzato ad accedere all\'istanza';
  public static messaggioUtentNoAuthModulo = 'Utente non autorizzato ad accedere al modulo';
  public static messaggioFeedback = "Il tuo feedback";
  public static messaggioThanksFeedback = "Grazie per il tuo feedback!";
  public static messaggioAvvioPagamento = "Confermi di voler completare l'istanza e avviare il pagamento? <br><b>Attenzione l'istanza non potrà più essere modificata</b>";
  public static messaggioConfermaAvvioPagamento = "Conferma avvio pagamento";
  public static messaggioConfermaPagaOnline = 'Conferma pagamento online';
  public static messaggioPagaOnline = 'Proseguendo, il sistema ti reindirizzerà sul sito <b>PagoPA</b>, dove effettuerai il pagamento. <br><br>Alla fine della procedura riceverai una email di conferma con la ricevuta di avvenuto pagamento.<br><br><b>Conserva la ricevuta, perché potrebbe servirti per completare I\'invio dell\'istanza.</b><br><br><i>Attenzione: per eventual problemi di accesso, verifica se sono attivi bocchi dei PopUP del sito.</i>';
  public static messaggioConfermaGestisciPagamento = 'Conferma pagamento sul territorio';
  public static messaggioGestisciPagamento1 = 'Proseguendo, il sistema ti reindirizzerà sul sito <b>PiemontePay</b>, dove potrai stampare il bollettino da consegnare presso la banca, ufficio postale o esercente convenzionato per effettuare il pagamento.<b><br>'//;Conserva la ricevuta, perché potrebbe servirti per completare I\'invio dell\'istanza.</b><br><br><i>Attenzione: per eventual problemi di accesso, verifica se sono attivi bocchi dei PopUP del sito.</i>';
  public static messaggioGestisciPagamento2 = `
    <br>
    <p>Per effettuare il pagamento sul territorio, segui queste istruzioni:</p>
    <ol>
    <li>copia il codice IUV <b>`;
  public static messaggioGestisciPagamento3 = `
    <li>clicca sul bottone <b>“Stampa avviso di pagamento”</b>, quindi su “Sì, procedi” per confermare la modalità di pagamento. Il sistema ti reindirizzerà al sito <b>PiemontePay</b> in una nuova finestra;</li>
    <li>clicca sul bottone <b>“Paga con piemontepay con IUV”</b>;</li>
    <li>inserisci il codice IUV e clicca su <b>“Prosegui”</b>;</li>
    <li>inserisci il codice fiscale relativo allo IUV (lo trovi nella colonna "CF/PIVA Pagatore" della tabella "Pagamenti" in questa pagina);</li>
    <li>clicca sul bottone <b>“Stampa”</b> in basso a destra;</li>
    <li>a questo punto avrai scaricato l’avviso di pagamento. Stampalo e recati presso l’esercente convenzionato
      per pagare l’avviso;</li>
    <li><b>conserva la ricevuta</b>, potrà essere richiesta per procedere all’invio dell’istanza.</li>
    <li>attendi qualche giorno, riceverai una email di conferma di avvenuta ricezione del tuo pagamento.</li>
    </ol>`;
  public static messaggioGestisciPagamento4 = '</b><i>Attenzione: per eventual problemi di accesso, verifica se sono attivi bocchi dei PopUP del sito.</i>'
  public static messaggioIstruzioniPagamento = 'Ho letto e compreso le istruzioni di pagamento';
  public static messaggioConfermaAnnullamentoPagamento = 'Conferma rigenerazione dello IUV';
  public static messaggioAnnullamentoPagamento = 'Verrà generato un nuovo codice IUV per effettuare un nuovo pagamento.<br><b>Attenzione: questa azione NON ANNULLA eventuali pagamenti in corso.</b><br><br>Confermi di voler generare un nuovo IUV?';
  public static messaggioUtenteNoAuth = "Utente non autorizzato ad accedere ai dati richiesti";
  public static messaggioInitTitle = "Errore in inizializzazione";
  public static messaggioInit = "Si è verificato un problema nella inizializzazione del modulo";
}

export class ErrorRest {

  constructor(public type: TypeErrorRest, public message?: string, public code ?: string,  public title ?: string) { }

}

export enum TypeErrorRest {
  TIMEOUT,
  SCONOSCIUTO,
  OK,
  UNAUTHORIZED,
  HTTP
}


export enum ServiziError {
  GENERIC = "Attenzione! Si è verificato un errore di comunicazione con il server! La sessione di lavoro potrebbe non essere più valida",
  TIMEOUT = "Attenzione! La rete da cui si è collegati risulta essere troppo lenta",
  UNAUTHORIZED = "Attenzione! L'utente non risulta autorizzato ad accedere alla risorsa richiesta",
  NOT_FOUND = "Elemento non trovato"
}
