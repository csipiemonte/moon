/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { faEye, faTrashAlt, faCheck } from '@fortawesome/free-solid-svg-icons';
import { Processo } from 'src/app/model/dto/processo';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'tr[app-processi-riga]',
  templateUrl: './processi-riga.component.html',
  styleUrls: ['./processi-riga.component.scss']
})
export class ProcessiRigaComponent implements OnInit {

  @Input() p: Processo;
  @Input() nomeOperatore: string;
  @Output() eventDettaglioRiga = new EventEmitter<Processo>();
  @Output() eventEliminaRiga = new EventEmitter<Processo>();

  isAdmin = false;
  isOpAdmin = false;

  faEye = faEye;
  faCheck = faCheck;
  faTrashAlt = faTrashAlt;

  constructor(private sharedService: SharedService) {
    console.log('[app-processi-riga::constructor]');
    // cambia stato e nuova versione riservati a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    log('processi-riga::ngOnInit()... ');
    // log('processi-riga::ngOnInit()... IN p=' + JSON.stringify(this.p));
  }

  dettaglioRiga($event: MouseEvent) {
    $event.preventDefault();
    log('processi-riga::dettaglioRiga() p.idProcesso=' + this.p.idProcesso);
    this.eventDettaglioRiga.emit(this.p);
  }

  eliminaRiga($event: MouseEvent) {
    $event.preventDefault();
    log('processi-riga::eliminaRiga() p.idProcesso=' + this.p.idProcesso);
    this.eventEliminaRiga.emit(this.p);
  }

}

function log(a: any) {
  console.log(a);
}
