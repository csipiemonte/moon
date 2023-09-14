/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { UtentiViewEvent, UtentiView } from '../../../../utenti.model';
import { Component, OnInit } from '@angular/core';
import { Utente } from 'src/app/model/dto/utente';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
//import { NgxSpinnerService } from 'ngx-spinner';
import { Router } from '@angular/router';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { faSave, faHome } from '@fortawesome/free-solid-svg-icons';
import { EventEmitter } from '@angular/core';
import { Output } from '@angular/core';
import { CodiceDescrizione } from 'src/app/model/dto/codice-descrizione';
import { NgxSpinnerService } from 'ngx-spinner';
import { AlertType } from 'src/app/modules/alert/alert.model';
import { AlertService } from 'src/app/modules/alert/alert.service';

@Component({
  selector: 'app-utenti-crea',
  templateUrl: './utenti-crea.component.html',
  styleUrls: ['./utenti-crea.component.scss']
})
export class UtentiCreaComponent implements OnInit {

  utente: Utente = new Utente();

  isAdmin = false;
  tipiUtente: CodiceDescrizione[] = [
    { codice: 'ADM', descrizione: 'utente admin' },
    { codice: 'PA', descrizione: 'utente di backoffice' },
    { codice: 'RUP', descrizione: 'utente rupar di frontoffice' },
    { codice: 'CIT', descrizione: 'utente internet di frontoffice' }];

  // area: Area[];
  faSave = faSave;
  faHome = faHome;

  alertOptions = {
    id: 'alert-utenti-crea',
    autoClose: false,
    keepAfterRouteChange: false
  };

  @Output() backEvent: EventEmitter<UtentiViewEvent> = new EventEmitter<UtentiViewEvent>();

  constructor(
    private router: Router,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private library: FaIconLibrary,
    private alertService: AlertService
  ) {
    library.addIcons(faSave, faHome);
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit() {
    log('utenti-crea::ngOnInit()');
    const defaultTipoUtente = { codice: 'PA', descrizione: 'utente di backoffice' };
    this.utente.tipoUtente = defaultTipoUtente;
//    this.spinnerService.show();
    // this.caricaElencoArea();
//    this.spinnerService.hide();
  }

  // async caricaElencoArea() {
  //   this.spinnerService.show();
  //   try {
  //     this.aree = await this.moonboblService.getElencoArea().toPromise() as Area[];
  //     this.spinnerService.hide();
  //   } catch (e) {
  //     this.spinnerService.hide();
  //     switch (e.status) {
  //       case 404:
  //         alert('Servizio non trovato');
  //         break;
  //       case 403:
  //         alert('Utente non abilitato al servizio');
  //         break;
  //       default:
  //         alert('Errore servizio: ' + e.message);
  //     }
  //   }
  // }

  onSubmit(ev: any) {
    log('utenti-crea::onSubmit()' + ev);
    this.salva();
  }

  salva() {
    log('utenti-crea::salva()');
    log('utenti-crea::salva() ' + JSON.stringify(this.utente));
    this.spinnerService.show();

    this.moonboblService.postUtente(this.utente).subscribe(response => {
      const risposta = response as Utente;
      this.spinnerService.hide();

      const utentiViewEvent = new UtentiViewEvent();
      utentiViewEvent.alertMessage = 'Utente ' + risposta.cognome + ' ' + risposta.nome +
        ' (' + risposta.identificativoUtente + ') inserito con id ' + risposta.idUtente;
      utentiViewEvent.alertType = AlertType.Success;
      utentiViewEvent.utente = _constructorUtenteEnteAbilitato(risposta);
      utentiViewEvent.view = UtentiView.DETTAGLIO;
      this.backEvent.emit(utentiViewEvent);
    },
    (e) => {
      log('utenti-crea::salva() ' + JSON.stringify(e));
      this.spinnerService.hide();
      this.alertService.error('Impossibile effettuare il salvataggio.<br>' + e.error?.msg, this.alertOptions);
    });
  }

  changeDisplay() {
  }

  back() {
//    this.spinnerService.show();
    this.backEvent.emit(new UtentiViewEvent());
  }

}

function log(a: any) {
  console.log(a);
}

function _constructorUtenteEnteAbilitato(utente: Utente): UtenteEnteAbilitato {
  const response = new UtenteEnteAbilitato();
  response.idUtente = utente.idUtente;
  response.identificativoUtente = utente.identificativoUtente;
  response.nome = utente.nome;
  response.cognome = utente.cognome;
  response.email = utente.email;
  response.flagAttivo = utente.flagAttivo;
  response.tipoUtente = utente.tipoUtente;
  response.dataIns = utente.dataIns;
  response.dataUpd = utente.dataUpd;
  response.attoreIns = utente.attoreIns;
  response.attoreUpd = utente.attoreUpd;
  response.entiAreeRuoli = [];
  return response;
}

