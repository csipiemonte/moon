/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Modulo} from '../../../../../model/dto/modulo';
import { faEdit, faSync, faFolderPlus, faInfo, faTrash, faTrashAlt, faPlus, faEye } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app/services/shared.service';

@Component({
  selector: 'tr[riga-miei-moduli]',
  templateUrl: './miei-moduli-riga.component.html',
  styleUrls: ['./miei-moduli-riga.component.css']
})
export class MieiModuliRigaComponent implements OnInit {

  @Input('data') rigaModulo: Modulo;
  // Eventi da gestire
  @Output() eventCambiaStato = new EventEmitter<Modulo>();
  @Output() eventNuovaVersione = new EventEmitter<Modulo>();
  @Output() eventDettaglioModulo = new EventEmitter<Modulo>();
  @Output() eventDatiGenerali = new EventEmitter<Modulo>();
  @Output() eventModificaRiga = new EventEmitter<Modulo>();
  @Output() eventEliminaModulo = new EventEmitter<Modulo>();


  isAdmin = false;
  isOpAdmin = false;
  isModificabile = false;
  isOwner = true;

  faEye = faEye;
  faEdit = faEdit;
  faSync = faSync;
  faFolderPlus = faFolderPlus;
  faInfo = faInfo;
  faTrash = faTrash;
  faTrashAlt = faTrashAlt;
  faPlus = faPlus;

  constructor(private sharedService: SharedService) {
    // cambia stato e nuova versione riservati a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.isOpAdmin = this.sharedService.UserLogged.hasRuoloOperatorMinADM();
  }

  ngOnInit(): void {
    // tslint:disable-next-line:max-line-length
    if (this.isAdmin || this.isOpAdmin ||
        ((this.rigaModulo.stato.codice  === 'INIT' || this.rigaModulo.stato.codice  === 'TST' || this.rigaModulo.stato.codice  === 'MOD')
        && this.isOwner && this.sharedService.UserLogged.hasRuoloOperatorADV())) {
      this.isModificabile = true;
    }
  }


  cambiaStato($event: MouseEvent) {
    $event.preventDefault();
    this.eventCambiaStato.emit(this.rigaModulo);
  }

  nuovaVersione($event: MouseEvent) {
    $event.preventDefault();
    this.eventNuovaVersione.emit(this.rigaModulo);
  }

  dettaglioModulo($event: MouseEvent) {
    $event.preventDefault();
    this.eventDettaglioModulo.emit(this.rigaModulo);
  }

  datiGenerali($event: MouseEvent) {
    $event.preventDefault();
    this.eventDatiGenerali.emit(this.rigaModulo);
  }

  modificaModulo($event) {
    $event.preventDefault();
    console.log(this.rigaModulo.codiceModulo);
    this.eventModificaRiga.emit(this.rigaModulo);
  }

  eliminaModulo($event: MouseEvent) {
    console.log('elimina');
    $event.preventDefault();
    this.eventEliminaModulo.emit(this.rigaModulo);
  }
}
