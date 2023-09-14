/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {CODICE_MODULO} from 'src/app/common/costanti';

import {Istanza} from '../model/dto/istanza';

@Injectable({
  providedIn: 'root'
})
export class ValidationService {

  constructor() { }


  checkDatiIstanza(istanza: Istanza) {
    let ret = true;
    switch (istanza.modulo.codiceModulo) {
      case CODICE_MODULO.VOUCHER:
        ret = this.validateVOUCHER(istanza);
        break;
      case CODICE_MODULO.ESTA_RAGA_21:
        ret = this.validateESTA_RAGA_21(istanza);
        break;
      default:
        ret = true;
    }
    return ret;
  }

  validateVOUCHER_OLD(istanza) {

    try {
      let jsonData = JSON.parse(istanza.data);
      let cfRichiedente = jsonData.data.richiedente.codiceFiscale;
      let cfStudente = jsonData.data.STUDENTE_CODFISC;
      let provResidenzaRichiedente = jsonData.data.RICHIEDENTE_PROVINCIA.codice;
      let flagProvEstera = jsonData.data.FLAG_PROV_ESTERA;
      let scuolaAttualeCodice = jsonData.data.SCUOLAATTUALE_CODICE.id;
      let scuolaAttualeTipo = jsonData.data.SCUOLAATTUALE_TIPO;

      if (!(cfRichiedente && cfRichiedente.trim() && cfRichiedente !== "null") || !(cfStudente && cfStudente.trim() && cfStudente !== "null")) {
        return false;
      }
      if (!((flagProvEstera === false) && provResidenzaRichiedente)) {
        return false;
      }
      if (!scuolaAttualeCodice || !scuolaAttualeTipo) {
        return false;
      }
      return true;
    } catch (err) {
      console.log(err.message);
      return false;
    }


  }


  validateVOUCHER(istanza) {

    try {

      let jsonData = JSON.parse(istanza.data);
      let cfRichiedente = jsonData.data.richiedente.codiceFiscale;
      let cfStudente = jsonData.data.studente.codiceFiscale;
      let scuolaAttualeCodice = jsonData.data.scuolaAttuale.codiceScuola.id;
      let scuolaAttualeTipo = jsonData.data.scuolaAttuale.tipo;

      if (!(cfRichiedente && cfRichiedente.trim() && cfRichiedente !== "null") || !(cfStudente && cfStudente.trim() && cfStudente !== "null")) {
        return false;
      }

      if (!scuolaAttualeCodice || !scuolaAttualeTipo) {
        return false;
      }
      return true;

    } catch (err) {
      console.log(err.message);
      return false;
    }

  }

  validateESTA_RAGA_21(istanza) {
    let jsonData = JSON.parse(istanza.data);
    let nome = jsonData.data.nome;
    let cognome = jsonData.data.cognome;
    let codicefiscale = jsonData.data.codiceFiscale;

    if (!(nome && nome.trim() && nome !== "null") || !(cognome && cognome.trim() && cognome !== "null") || !(codicefiscale && codicefiscale.trim() && codicefiscale !== "null")) {
      return false;
    }
    return true;
  }

}
