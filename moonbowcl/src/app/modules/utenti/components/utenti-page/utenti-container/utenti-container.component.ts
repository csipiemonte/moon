/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, ViewEncapsulation } from '@angular/core'; 
import { UtentiViewEvent, UtentiView } from '../../../utenti.model';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { Alert, AlertService } from 'src/app/modules/alert';

@Component({
  selector: 'app-utenti-container',
  templateUrl: './utenti-container.component.html',
  styleUrls: ['./utenti-container.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UtentiContainerComponent implements OnInit {

  currentView: UtentiView;
  currentMsg: string;
  utenteSelezionato: UtenteEnteAbilitato;

  alertOptions = {
    id: 'alert-utenti-container',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(
    protected alertService: AlertService) { }

//  utenti: UtenteEnteAbilitato[];

  ngOnInit(): void {
    log('utenti-container::ngOnInit() ...');
    this.currentView = UtentiView.ELENCO;
  }

  dettaglio(utente: UtenteEnteAbilitato) {
    log('utenti-container::dettaglio() ...');
    log('utenti-container::dettaglio() ... utente = ' + JSON.stringify(utente));
    this.utenteSelezionato = utente;
    this.currentView = UtentiView.DETTAGLIO;
  }

  eliminaUtenteAreaRuolo(utente: UtenteEnteAbilitato) {
    log('utenti-container::eliminaUtenteAreaRuolo() ...');
    log('utenti-container::eliminaUtenteAreaRuolo() ... utente = ' + JSON.stringify(utente));
  }

  setView(e: UtentiViewEvent) {
    log('utenti-container::setView() ...');
    log('utenti-container::setView() ... e = ' + JSON.stringify(e));

    if (e) {
      if (e.view === UtentiView.DETTAGLIO) {
        if (e.utente) {
          log('utenti-container::setView() e.utente = ' + e.utente);
          this.utenteSelezionato = e.utente;
        } else {
          log('utenti-container::setView() ERROR richiesto UtentiView DETTAGLIO senza idUtente ! set default ELENCO');
          e.view = UtentiView.ELENCO;
        }
      }
      this.currentView = e.view;
      if (e.alertMessage) {
//        this.alertService.success(e.alertMessage, this.alertOptions);
        this.alertService.alert(new Alert({ ...this.alertOptions, type: e.alertType, message: e.alertMessage }));
      }
    }
  }

  modificaUtente(utente: UtenteEnteAbilitato) {
    log('utenti-container::modificaUtente() ...');
    log('utenti-container::modificaUtente() ... utente = ' + JSON.stringify(utente));
//    (this.utenti = this.utenti.filter(u => u.idUtente !== utente.idUtente)).push(utente);
  }

  eliminaUtente(utente: UtenteEnteAbilitato) {
    log('utenti-container::eliminaUtente() ...');
    log('utenti-container::eliminaUtente() ... utente = ' + JSON.stringify(utente));
  }

  creaUtente() {
    log('utenti-container::creaUtente()');
    this.currentView = UtentiView.CREA;
  }
}

function log(a: any) {
  console.log(a);
}
