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
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalConfirmComponent } from 'src/app/components/modal/modal-confirm/modal-confirm.component';
import { Messaggi } from 'src/app/common/messaggi';
import { STORAGE_KEYS } from 'src/app/common/costanti';

@Component({

  selector: 'tr[riga-istanza]',
  templateUrl: './riga-istanza.component.html',
  styleUrls: ['./riga-istanza.component.css']
})
export class RigaIstanzaComponent implements OnInit {
  faEdit = faEdit;
  faEye = faEye;
  // showFiltroComune: Boolean = false;
  // showFiltroEnte: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showColonnaComune: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroMultiEnte: Boolean = false;

  ente: Ente = new Ente();

  isUtenteAbilitatoModifica: boolean;
  entiAreeRuoli: EnteAreaRuolo[];

  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  @Input('i') index: number;
  // pagina corrente
  @Input('currPage') currPage: number;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();
  constructor(private router: Router, private moonboblService: MoonboblService, private sharedService: SharedService, private modalService: NgbModal) { }
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

    this.isUtenteAbilitatoModifica = this.sharedService.UserLogged?.hasRuoloOperatorMinADV() ||
      this.sharedService.UserLogged?.isTipoADMIN();
  }

  mostraDettaglioIstanza() {
    // this.router.navigate() ;  // mostra dettaglio istanza impostare poi read only
    // this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    // if (this.rigaIstanza.modulo.codiceModulo === 'RPCCSR') {
    //   const mdRef = this.modalService.open(ModalConfirmComponent);
    //   mdRef.componentInstance.modal_titolo = 'Informativa';
    //   mdRef.componentInstance.modal_contenuto = Messaggi.messaggioInformativaRPCCSR;
    //   mdRef.result.then((result) => {
    //     console.log('Closed with: ${result}' + result);       
    //     this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    //   }, (reason) => {
    //     console.log(reason);
    //   });
    // } else {
    //   this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    // }
    this.checkInformativa('istanze/view-form');
  }

  modificaIstanza() {
    // this.router.navigate(['istanzebozza/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    this.checkInformativa('istanzebozza/view-form');
  }

  cancellaIstanza() {
    // emette evento rigaIstanzaDeleted visto all'esterno come onDeleteRiga
    this.rigaIstanzaDeleted.emit(this.rigaIstanza);
  }

  modificaImportanza($event) {
    $event.preventDefault();
    //    console.log('modificaImportanza BEGIN ID ' + this.rigaIstanza.idIstanza + ' was ' + this.rigaIstanza.importanza);

    // emette evento rigaIstanzaCambiaImportanza visto all'esterno come onCambiaImportanza
    this.rigaIstanzaCambiaImportanza.emit(this.rigaIstanza);

    //    console.log('modificaImportanza END ID ' + this.rigaIstanza.idIstanza + ' is ' + this.rigaIstanza.importanza);
  }

  private getEnte(idEnte: number): void {
    this.moonboblService.getEnte(idEnte).then(ente => {
      console.log('codice ente' + ente.codiceEnte);
      console.log('nome ente' + ente.nomeEnte);
      this.ente = ente;
    });
  }

  checkInformativa(targetRoute: string) {
    if (this.rigaIstanza.modulo.codiceModulo === 'RPCCSR') {
      const mdRef = this.modalService.open(ModalConfirmComponent);
      mdRef.componentInstance.modal_titolo = 'Informativa';
      mdRef.componentInstance.modal_contenuto = Messaggi.messaggioInformativaRPCCSR;
      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.router.navigate([targetRoute+'/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } })
      }, (reason) => {
        console.log(reason);
      });
    } else {
      this.router.navigate([targetRoute+'/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } })
    }
  }

}
