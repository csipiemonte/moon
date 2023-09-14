/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input, EventEmitter, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Istanza } from '../../../../model/dto/istanza';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { Ente } from 'src/app/model/dto/ente';
import { SharedService } from 'src/app/services/shared.service';
import { EnteAreaRuolo } from 'src/app/model/dto/ente-area-ruolo';
import { faCogs, faTrash } from '@fortawesome/free-solid-svg-icons';
import { STORAGE_KEYS } from 'src/app/common/costanti';

@Component({
  selector: 'tr[riga-istanza-da-completare]',
  templateUrl: './riga-istanza-da-completare.component.html',
  styleUrls: ['./riga-istanza-da-completare.component.scss']
})
export class RigaIstanzaDaCompletareComponent implements OnInit {
  faEdit = faEdit;
  faEye = faEye;
  faCogs = faCogs;
  faTrash = faTrash;
  // showFiltroComune: Boolean = false;
  // showFiltroEnte: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showColonnaComune: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroMultiEnte: Boolean = false;

  ente: Ente = new Ente();

  isUtenteAbilitatoModifica: boolean;
  isUtenteAbilitatoWorkflow: boolean;
  entiAreeRuoli: EnteAreaRuolo[];

  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  @Input('i') index: number;
  // pagina corrente
  @Input('currPage') currPage: number;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();
 
  constructor(private router: Router, private moonboblService: MoonboblService, private sharedService: SharedService) { }
  // evento richiesta cambio importanza
  @Output('onCambiaImportanza') rigaIstanzaCambiaImportanza = new EventEmitter();

  ngOnInit() {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    this.showColonnaComune = moduloSelezionato.showColonnaComune;
    this.showColonnaEnte = moduloSelezionato.showColonnaEnte;

    if (this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.showFiltroMultiEnte = true;
      if (this.showFiltroMultiEnte) {
        this.getEnte(this.rigaIstanza.idEnte);
      }
    }

    this.isUtenteAbilitatoModifica = ((this.rigaIstanza.stato.idStato === 1) && (this.sharedService.UserLogged?.isTipoADMIN() || this.sharedService.UserLogged?.hasRuoloOperatorADV() || this.sharedService.UserLogged?.hasRuoloOperatorCOMP()));
    this.isUtenteAbilitatoWorkflow = (this.rigaIstanza.stato.idStato === 10)  && (this.sharedService.UserLogged?.isTipoADMIN() || this.sharedService.UserLogged?.hasRuoloOperatorCOMP());

  }

  mostraDettaglioIstanza() {
    this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } });
  }

  modificaIstanza() {  
    this.router.navigate(['istanzebozza-da-inviare/view-form/' + this.rigaIstanza.idIstanza]);
  }

  cancellaIstanza() {
    this.rigaIstanzaDeleted.emit(this.rigaIstanza);
  }

  lavoraIstanza() {
    this.router.navigate(['istanza/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
  }

  modificaImportanza($event) {
    $event.preventDefault();
    this.rigaIstanzaCambiaImportanza.emit(this.rigaIstanza);
  }

 
  private getEnte(idEnte: number): void {
    this.moonboblService.getEnte(idEnte).then(ente => {
      console.log('codice ente' + ente.codiceEnte);
      console.log('nome ente' + ente.nomeEnte);
      this.ente = ente;

    }

    );
  }
}
