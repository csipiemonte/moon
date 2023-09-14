/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Injectable} from '@angular/core';
import {Modulo} from '../model/dto/modulo';
import {Notifica} from '../model/dto/notifica';
import {Nav} from '../model/dto/nav';
import {NavSelection} from '../common/nav-selection';
import {Ente} from '../model/dto/ente';


@Injectable({
  providedIn: 'root'
})
export class SharedService {
  activeMenu: number;
  activeTab: number;
  page: number;
  elencoModuliNuovaIstanza: Modulo[];
  elencoEnti: Ente[];
  notifica: Notifica;
  nav: Nav = new Nav(NavSelection.BACHECA, 'home/bacheca');
  datiContoTerzi: { ct: boolean, dati: {} } = {ct: false, dati: null};
  simulatePortale: string;
  isDirectRouterLink: boolean = false;
  // fixme FZ-NAV Ricerca Avanzata
  // indica che la navigazione è partita dal risultato di una ricerca
  fromElencoRicerca: boolean = false;
}
