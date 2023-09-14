/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {NgbDate, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

export class CriterioRicercaIstanze {
  stato: number;
  idTabFo: number;
  importanza: number;
  codiceIstanza: string;
  idModulo: number;
  titoloModulo: string;
  // private createdStart: NgbDate;
  // private createdEnd: NgbDate;
  dataDa: NgbDateStruct;
  dataA: NgbDateStruct;
  sort: string;
  nome: string;
  cognome: string;
  codiceFiscale: string;
  statiIstanza:  number[];

  // get startDate() {
  //   if (this.dataDa == null) {
  //     return null;
  //   }
  //   // new Date(Date.UTC(this.dataDa.year, this.dataDa.month -1, this.dataDa.day));
  //   return new NgbDate(this.dataDa.year, this.dataDa.month , this.dataDa.day);
  // }

  // get endDate() {
  //   if (this.dataA == null) {
  //     return null;
  //   }
  //   return new NgbDate(this.dataA.year, this.dataA.month  , this.dataA.day);
  // }

  fromJson(json: CriterioRicercaIstanze) {
    if(json.stato != undefined) {
      this.stato = json.stato;
    }
    if(json.idTabFo != undefined) {
      this.idTabFo = json.idTabFo;
    }
    if(json.importanza != undefined) {
      this.importanza = json.importanza;
    }
    if(json.codiceIstanza != undefined) {
      this.codiceIstanza = json.codiceIstanza;
    }
    if(json.idModulo != undefined) {
      this.idModulo = json.idModulo;
    }
    if(json.titoloModulo != undefined) {
      this.titoloModulo = json.titoloModulo;
    }
    if(json.dataDa != undefined) {
      this.dataDa = json.dataDa;
    }
    if(json.dataA != undefined) {
      this.dataA = json.dataA;
    }
    if(json.sort != undefined) {
      this.sort = json.sort;
    }
    if(json.nome != undefined) {
      this.nome = json.nome;
    }
    if(json.cognome != undefined) {
      this.cognome = json.cognome;
    }
    if(json.codiceFiscale != undefined) {
      this.codiceFiscale = json.codiceFiscale;
    }
  }

}
