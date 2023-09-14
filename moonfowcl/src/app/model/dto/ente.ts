/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export class Ente {
  idEnte: number;
  readonly codiceEnte: string;
  readonly nomeEnte: string;
  descrizione: string;
  idTipoEnte: number;
  logo: string;
  indirizzo: string;
  dataUpd: Date;
  attoreUpd: string;
  idEntePadre: number;
  flagAttivo: boolean;

  /*
    constructor(idEnte: number, codiceEnte: string, nomeEnte: string, descrizione: string,
                idTipoEnte: number, logo: string, indirizzo: string, dataUpd: Date,
                attoreUpd: string, idEntePadre: number, flagAttivo: boolean) {
      this.idEnte = idEnte;
      this.codiceEnte = codiceEnte;
      this.nomeEnte = nomeEnte;
      this.descrizione = descrizione;
      this.idTipoEnte = idTipoEnte;
      this.logo = logo;
      this.indirizzo = indirizzo;
      this.dataUpd = dataUpd;
      this.attoreUpd = attoreUpd;
      this.idEntePadre = idEntePadre;
      this.flagAttivo = flagAttivo;
    }
  */

  // constructor(ente: Ente) {
  //   this.idEnte = ente.idEnte;
  //   this.codiceEnte = ente.codiceEnte;
  //   this.nomeEnte = ente.nomeEnte;
  //   this.descrizione = ente.descrizione;
  //   this.idTipoEnte = ente.idTipoEnte;
  //   this.logo = ente.logo;
  //   this.indirizzo = ente.indirizzo;
  //   this.dataUpd = ente.dataUpd;
  //   this.attoreUpd = ente.attoreUpd;
  //   this.idEntePadre = ente.idEntePadre;
  //   this.flagAttivo = ente.flagAttivo;
  // }


}
