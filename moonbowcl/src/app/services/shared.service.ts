/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtenteModuloAbilitato } from './../model/dto/utente modulo-abilitato';
import {Injectable} from '@angular/core';
import {Modulo} from '../model/dto/modulo';
import {UserInfo} from '../model/common';
import { Nav } from '../model/dto/nav';
import { Ente } from '../model/dto/ente';
import { NavSelection } from '../common/nav-selection';
import { CurrentFilter } from '../common/current-filter';
import { Filter } from '../common/filter';
import { Notifica } from '../model/dto/notifica';
import { Comune } from '../model/dto/comune';
import { CodiceNome } from '../model/dto/codice-nome';

@Injectable({
  providedIn: 'root'
})
export class SharedService {
  userLogged: UserInfo;
  activeMenu: number;
  activeTab: number;
  page: number;
  pageSizeExport: number;
  elencoModuliNuovaIstanza: Modulo[];
  // MieiModuli
  mieiModuli: Modulo[] = [];
  pageMieiModuli: number;
  filtroModuli: string;
  // Utenti
  utenti: UtenteModuloAbilitato[] = [];
  pageUtenti: number;
  filtroUtenti: string;
  filtroFlagAttivoUtenti: string;
  filtroFlagAbilitatoUtenti: string;
  // Processi
  processi: UtenteModuloAbilitato[] = [];
  pageProcessi: number;
  filtroProcessi: string;
  //
  nav: Nav = new Nav(NavSelection.ISTANZE, 'istanze');
  elencoEnti: Ente[];

  //Filtri 
  elencoComuni: Comune[];
  elencoEntiUtente: CodiceNome[];
  elencoComuniUtente: Comune[];

  ente: Ente;
  // currentFilter: CurrentFilter;
  searchFilter: Filter = new Filter();

  datiContoTerzi: { ct: boolean, dati: {} } = {ct: false, dati: null};

  notifica: Notifica;
  datiCompilaOperatore: { dati: {} } = {dati: null};

  set UserLogged(u: UserInfo) {
    this.userLogged = u;
  }

  get UserLogged() {
    return this.userLogged;
  }

  set SearchFilter(u: Filter) {
    this.searchFilter = u;
  }

  get SearchFilter() {
    return this.searchFilter;
  }

  setCurrentFilter(sezione: number, cf: CurrentFilter) {
    this.searchFilter.setCurrentFilter(sezione, cf);
  }

  getCurrentFilter(sezione: number) {
    return this.searchFilter.getCurrentFilter(sezione);
  }

  constructor() { }
}
