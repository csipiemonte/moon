/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Istanza } from '../../../../model/dto/istanza';
import { Router } from '@angular/router';
import { faCogs } from '@fortawesome/free-solid-svg-icons';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { faUser } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { Ente } from 'src/app/model/dto/ente';
import { SharedService } from 'src/app/services/shared.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalConfirmComponent } from 'src/app/components/modal/modal-confirm/modal-confirm.component';
import { Messaggi } from 'src/app/common/messaggi';
import { STORAGE_KEYS } from 'src/app/common/costanti';


@Component({
  // tslint:disable-next-line: component-selector
  selector: 'tr[riga-istanze-pervenute]',
  templateUrl: './riga-istanze-pervenute.component.html',
  styleUrls: ['./riga-istanze-pervenute.component.css']
})
export class RigaIstanzePervenuteComponent implements OnInit {
  faCogs = faCogs;
  faEye = faEye;
  faUser = faUser;
  showColonnaComune: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showFiltroMultiEnte: Boolean = false;
  ente: Ente = new Ente();
  isCompiledByUser: Boolean = false;
  // isPagato: Boolean = false;
  isPaidStyle: string = 'red';

  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  @Input('i') index: number;
  // pagina corrente
  @Input('currPage') currPage: number;
  // evento richeista eliminazione istanza
  @Output('onDeleteRiga') rigaIstanzaDeleted = new EventEmitter();
  @Input('isPagamenti') isPagamenti : boolean;

  constructor(private router: Router, private moonboblService: MoonboblService, private sharedService: SharedService, private modalService: NgbModal) { }
  // evento richiesta cambio importanza
  @Output('onCambiaImportanza') rigaIstanzaCambiaImportanza = new EventEmitter();


  ngOnInit() {


    this.isCompiledByUser = (this.rigaIstanza.codiceFiscaleDichiarante != this.rigaIstanza.attoreIns);

    this.checkPagamenti();

    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    this.showColonnaComune = moduloSelezionato.showColonnaComune;
    this.showColonnaEnte = moduloSelezionato.showColonnaEnte;

    if (this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.showFiltroMultiEnte = true;
      if (this.showFiltroMultiEnte) {
        this.getEnte(this.rigaIstanza.idEnte);
      }
    }
  }
  

  mostraDettaglioIstanza() {
    // mostra dettaglio istanza impostare poi read only
    //this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});

    // if (this.rigaIstanza.modulo.codiceModulo === 'RPCCSR') {
    //   const mdRef = this.modalService.open(ModalConfirmComponent);
    //   mdRef.componentInstance.modal_titolo = 'Informativa';
    //   mdRef.componentInstance.modal_contenuto = Messaggi.messaggioInformativaRPCCSR;
    //   mdRef.result.then((result) => {
    //     console.log('Closed with: ${result}' + result);
    //     this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } });
    //   }, (reason) => {
    //     console.log(reason);
    //   });
    // } else {
    //   this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } });
    // }
    this.checkInformativa('istanze/view-form');
  }

  lavoraIstanza() {
    //this.router.navigate(['istanza/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    // if (this.rigaIstanza.modulo.codiceModulo === 'RPCCSR') {
    //   const mdRef = this.modalService.open(ModalConfirmComponent);
    //   mdRef.componentInstance.modal_titolo = 'Informativa';
    //   mdRef.componentInstance.modal_contenuto = Messaggi.messaggioInformativaRPCCSR;
    //   mdRef.result.then((result) => {
    //     console.log('Closed with: ${result}' + result);
    //     this.router.navigate(['istanza/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } })
    //   }, (reason) => {
    //     console.log(reason);
    //   });
    // } else {
    //   this.router.navigate(['istanza/' + this.rigaIstanza.idIstanza], { queryParams: { page: this.currPage } })
    // }
    this.checkInformativa('istanza');
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
    this.moonboblService.getEnte(idEnte).then(ente => this.ente = ente
      // {
      //   console.log('codice ente'+ ente.codiceEnte);
      //   console.log('nome ente'+ ente.nomeEnte);
      //   this.ente = ente;

      // }      
    );
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

  checkPagamenti(){
    // this.moonboblService.getIstanza(idIstanza)
    // .then(rigaIstanza => {
    //   if (rigaIstanza && rigaIstanza.pagamenti) {
    //     if (((rigaIstanza.stato?.nome === 'IN PAGAMENTO' || rigaIstanza.stato?.nome === 'INVIATA') && !rigaIstanza?.dataEsitoPagamento)
    //     || (rigaIstanza.stato?.nome === 'IN PAGAMENTO' && rigaIstanza?.dataEsitoPagamento))
    //     {
    //       this.isPaidStyle = 'red';
    //     }        
    //     else{
    //       this.isPaidStyle = 'green';
    //     }        
    //   }
    // })
    // .catch(errore => {
    //   console.log(errore);
    // });

     if (this.rigaIstanza.isPagato) {      
      this.isPaidStyle = this.rigaIstanza.isPagato ? 'green':'red';
     }

 
  }
  



}

