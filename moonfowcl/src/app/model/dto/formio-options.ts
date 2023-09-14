/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Messaggi} from '../../common/messaggi';
import {Modulo} from './modulo';

export class FormIOOptions {
  mod: Modulo;
  alerts = {'submitMessage': ''};
  errors = {'message': ''};
  i18n = {
    language: 'it',
    it: {
      complete: 'Salvataggio completato',
      error: 'Si prega di correggere gli errori prima di inviare.',
      required: '{{field}} è obbligatorio',
      unique: '{{field}} deve essere unico',
      array: '{{field}} deve essere un elenco',
      array_nonempty: '{{field}} deve essere un elenco non vuoto',
      // eslint-disable-line camelcase
      nonarray: '{{field}} non deve essere un elenco',
      select: '{{field}} contiene una selezione non valida',
      pattern: '{{field}} non corrisponde al formato previsto {{pattern}}',
      minLength: '{{field}} deve avere almeno {{length}} caratteri.',
      maxLength: '{{field}} non deve avere più di {{length}} caratteri.',
      minWords: '{{field}} deve avere almeno {{length}} parole.',
      maxWords: '{{field}} non deve avere più di {{length}} parole.',
      min: '{{field}} non può essere minore di {{min}}.',
      max: '{{field}} non può essere maggiore di {{max}}.',
      maxDate: '{{field}} non può essere posteriore alla data {{- maxDate}}',
      minDate: '{{field}} non può essere antecedente alla data {{- minDate}}',
      maxYear: '{{field}} non può essere posteriore all\'anno {{maxYear}}',
      minYear: '{{field}} non può essere antecedente all\'anno {{minYear}}',
      invalid_email: '{{field}} deve essere una email valida.',
      // eslint-disable-line camelcase
      invalid_url: '{{field}} deve essere un url valido.',
      // eslint-disable-line camelcase
      invalid_regex: '{{field}} non corrisponde al formato previsto {{regex}}.',
      // eslint-disable-line camelcase
      invalid_date: '{{field}} non è una data valida.',
      // eslint-disable-line camelcase
      invalid_day: '{{field}} non è un giorno valido.',
      // eslint-disable-line camelcase
      mask: '{{field}} non corrisponde al formato previsto.',
      stripe: '{{stripe}}',
      month: 'Mese',
      day: 'Giorno',
      year: 'Anno',
      january: 'gennaio',
      february: 'febbraio',
      march: 'marzo',
      april: 'aprile',
      may: 'maggio',
      june: 'giugno',
      july: 'luglio',
      august: 'agosto',
      september: 'settembre',
      october: 'ottobre',
      november: 'novembre',
      december: 'dicembre',
      next: 'Successivo',
      previous: 'Precedente',
      cancel: 'Cancella',
      'Do you want to clear data?': 'Vuoi cancellare i dati?',
      'Cancel': 'Annulla',
      'Yes, delete it': 'Si, confermo',
      submit: 'Salva e prosegui',
      'Drop files to attach,': 'Sposta qui un file da allegare',
      'browse': 'selezionalo',
      'or': 'o',
      'File Name': 'nome file',
      'Size': 'kB',
      'File is the wrong type; it must be {{ pattern }}': 'Sono permessi solo file con estensione: {{ pattern }}',
      'File is too big; it must be at most {{ size }}': 'Il file è troppo grande, deve avere una dimensione massima di {{ size }}',
      unsavedRowsError: 'Salvare i dati richiesti prima di proseguire',
      invalidRowsError: 'Completare i dati richiesti prima di proseguire',
      invalidRowError: 'Dati mancanti. Completare i dati richiesti'
    }
  };

  constructor() {
    this.alerts.submitMessage = Messaggi.msgSubmitForm;
    this.errors.message = Messaggi.msgErrForm;
  }


}
