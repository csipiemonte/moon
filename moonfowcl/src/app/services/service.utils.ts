/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Observable, throwError, timer} from 'rxjs';
import {finalize, mergeMap,} from 'rxjs/operators';
import {HttpErrorResponse} from '@angular/common/http';
import {SrvError} from '../common/srv-error';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';


/**
 * Strategia di retry selettivo. permette di rieseguire la chiamata a fronte di un errore ma solo
 * se il codice di errore non rientra in un set di esclusioni
 * @param opzioni
 */
export const selectiveRetryStrategy = ({
                                         maxRetryAttempts = 3,
                                         scalingDuration = 1000,
                                         excludedStatusCodes = [302]
}: {
  maxRetryAttempts?: number,
  scalingDuration?: number,
  excludedStatusCodes?: number[]
} = {}) => (attempts: Observable<any>) => {
  return attempts.pipe(
    mergeMap((error, i) => {
      const retryAttempt = i + 1;
      // if maximum number of retries have been met
      // or response is a status code we don't wish to retry, throw error
      // console.log('error:'+error.status+","+excludedStatusCodes.find(e => e === error.status));
      if (
        retryAttempt > maxRetryAttempts ||
        excludedStatusCodes.find(e => e === error.status) != null
      ) {
        return throwError(error);
      }
      console.log(
        `Attempt ${retryAttempt}: retrying in ${retryAttempt *
        scalingDuration}ms`
      );
      // retry after 1s, 2s, etc...
      return timer(retryAttempt * scalingDuration);
    }),
    finalize(() => console.log('We are done!'))
  );
};

/**
 * Gestore generico dell'errore durante l'invocazione di servizi.
 * Si intende l'utilizzo come handler nell'operatore catchError di RxJs.
 * Trasforma ogni eventuale errore (client o server side) in un oggetto SrvError
 * che contiene le informazioni utili per riconoscere la tipologia di errore
 * permette anche di riconoscere la condizione di redirect, tipicamente legata
 * alla scadenza della sessione di autenticazione: in quesot caso ridirige il
 * flusso applicativo verso una pagina di cortesia che informa l'utente e gli permette
 * di ritornare alla pagina di accesso.
 * @param error l'errore originariamente osservato nello stream
 */
export function handleError(error: HttpErrorResponse): Observable<never> {
  const err: SrvError = new SrvError();
  err.code = '' + error.status;
  err.errorMessage = error.message;
  err.messaggioCittadino = 'cittadino, ' + error.message;
  err.sessionExpired = false;
  if (error.error instanceof ErrorEvent) {
    // Errore client-side o di rete => costruisco un oggetto di errore da restituire al componente
    // chiamante
    console.error('Errore di rete o client-side:', error.error.message);
    return throwError(err);
  } else if (typeof(error.error) === 'string') {
    // Il backend ha risposto con un codice di risposta di errore (4xx, 5xx, ...).
    // Il corpo della risposta nn e' formattato ma il testo potrebbe contenere informazioni utili
    // per la gestione dell'errore (es. sessione scaduta)
    // console.log("errore non formattato in JSON: "+error.error);
    if (error.error.indexOf('XSRF TOKEN not already initialized') > -1) {
      console.error('trovato messaggio XSRF invalid, segnale di sessione scaduta');
      err.sessionExpired = true;
      return throwError(err);
    } else if (error.status === 302) {
      // redirect di shibboleth??
      console.error('ricevuto HTTP 302: sessione di autenticazione scaduta? => salto a pagina di cortesia');
      this.securityService.jumpToCourtesyPage(err);

      //  const notifica = new Notifica(null,  err.errorMessage, null, null);
      //  this.securityService.goToNotificationError(notifica);

      return throwError(err); // restituisco comunque un errore per non rompere lo stream
    } else if (error.status === 504) {   // Gestione Timeout
      // redirect di shibboleth??
      err.messaggioCittadino = 'Errore timeout invocazione servizi ';
      console.error(err.errorMessage);
      this.securityService.jumpToCourtesyPage(err);
      // const notifica = new Notifica(null,  err.errorMessage, null, null);
      // this.securityService.goToNotificationError(notifica);
      return throwError(err); // restituisco comunque un errore per non rompere lo stream
    }
  } else {
    // Il backend ha risposto con un codice di risposta di errore (4xx, 5xx, ...).
    // Il corpo della risposta potrebbe contenere informazioni utili per la gestione dell'errore
    // nel componente chiamante => le restituisco nell'oggetto di errore
    console.error(
      `Ricevuto errore strutturato dal backend. status code: ${error.status}, ` +
      `body: ${error.error}`);
    if (error.status === 302) {
      console.error('ricevuto HTTP 302: sessione di autenticazione scaduta? => salto a pagina di cortesia');
      // redirect di shibboleth??
      this.securityService.jumpToCourtesyPage(err);

      //  const notifica = new Notifica(null,  err.errorMessage, null, null);
      //  this.securityService.goToNotificationError(notifica);
      return throwError(err); // restituisco comunque un errore per non rompere lo stream
    }  else {
      return throwError(err);
    }
  }
}


export function compareDateBase(sDate: string, eDate: string): boolean {
  let valDate = true;

  if ((sDate != null && eDate != null) && (eDate) < (sDate)){
    valDate = false;
  }
  return valDate;
}

export function compareDate(sDate: NgbDateStruct, eDate: NgbDateStruct): boolean {
  let valDate = true;

  if (sDate != null && eDate != null) {
    let s = new Date(sDate.year, sDate.month, sDate.day);
    let e = new Date(eDate.year, eDate.month, eDate.day);
    valDate = (s < e);
  }
  return valDate;
}

export function empty(str) {
  if (typeof str == 'undefined' || !str || str.length === 0 || str === "" || !/[^\s]/.test(str) || /^\s*$/.test(str) || str.replace(/\s/g,"") === "") {
    return true;
  } else {
    return false;
  }
}

export function saveBlobIE(newBlob: any) {
  if (window.navigator) {
    const nav = (window.navigator as any);
    if (nav.msSaveOrOpenBlob) {
      nav.msSaveOrOpenBlob(newBlob);
      return;
    }
  }
}



