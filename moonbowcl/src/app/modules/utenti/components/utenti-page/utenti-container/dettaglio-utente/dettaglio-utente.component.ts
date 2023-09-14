/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtentiViewEvent } from 'src/app/modules/utenti/utenti.model';
import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { faEdit, faHome, faInfo, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'app-utenti-dettaglio',
  templateUrl: './dettaglio-utente.component.html',
  styleUrls: ['./dettaglio-utente.component.scss']
})
export class DettaglioUtenteComponent implements OnInit {

  @Input() utenteSelezionato: UtenteEnteAbilitato;
  @Output() eventModifica = new EventEmitter<UtenteEnteAbilitato>();
  @Output() eventElimina = new EventEmitter<UtenteEnteAbilitato>();
  @Output() backEvent = new EventEmitter<UtentiViewEvent>();

  isAdmin = false;
  faHome = faHome;
  navVertActive = 'dati-generali';

  constructor(private sharedService: SharedService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('dettaglio-utente::ngOnInit() utenteSelezionato = ' + this.utenteSelezionato);
  }

  modifica(utente: UtenteEnteAbilitato) {
    log('dettaglio-utente::modifica() utente = ' + utente);
    // $event.preventDefault();
    this.utenteSelezionato = utente;
    this.eventModifica.emit(utente);
  }

  elimina(utente: UtenteEnteAbilitato) {
    log('dettaglio-utente::elimina() utente = ' + utente);
    // $event.preventDefault();
    this.eventElimina.emit(utente);
  }

  back() {
//    this.spinnerService.show();
    this.backEvent.emit(new UtentiViewEvent());
  }

}

function log(a: any) {
  console.log(a);
}
