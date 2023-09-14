/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { faEye, faTrashAlt, faCheck } from '@fortawesome/free-solid-svg-icons';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'tr[app-utenti-riga]',
  templateUrl: './utenti-riga.component.html',
  styleUrls: ['./utenti-riga.component.scss']
})
export class UtentiRigaComponent implements OnInit {

  @Input() u: UtenteEnteAbilitato;
  @Input() nomeOperatore: string;
  @Output() eventDettaglioRiga = new EventEmitter<UtenteEnteAbilitato>();
  @Output() eventEliminaRiga = new EventEmitter<UtenteEnteAbilitato>();

  isAdmin = false;
  isOpAdmin = false;

  faEye = faEye;
  faCheck = faCheck;
  faTrashAlt = faTrashAlt;

  constructor(private sharedService: SharedService) {
    // cambia stato e nuova versione riservati a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    log('utenti-riga::ngOnInit()... ');
    // log('utenti-riga::ngOnInit()... IN u=' + JSON.stringify(this.u));
  }

  dettaglioRiga($event: MouseEvent) {
    $event.preventDefault();
    log('utenti-riga::dettaglioRiga() u.identificativoUtente=' + this.u.identificativoUtente);
    this.eventDettaglioRiga.emit(this.u);
  }

  eliminaRiga($event: MouseEvent) {
    $event.preventDefault();
    log('utenti-riga::eliminaRiga() u.identificativoUtente=' + this.u.identificativoUtente);
    this.eventEliminaRiga.emit(this.u);
  }

}

function log(a: any) {
  //  console.log(a);
}
