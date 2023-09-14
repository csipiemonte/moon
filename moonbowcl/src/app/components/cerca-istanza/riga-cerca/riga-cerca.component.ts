/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, OnInit, Input, EventEmitter, Output} from '@angular/core';

import {Router} from '@angular/router';
import {Istanza} from '../../../model/dto/istanza';
import {CriterioRicercaIstanze} from '../../../model/dto/criterio-ricerca-istanze';
import { faCogs } from '@fortawesome/free-solid-svg-icons';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { faEdit } from '@fortawesome/free-solid-svg-icons';
import { Ente } from 'src/app/model/dto/ente';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { ModalConfirmComponent } from '../../modal/modal-confirm/modal-confirm.component';
import { Messaggi } from 'src/app/common/messaggi';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { STORAGE_KEYS } from 'src/app/common/costanti';

@Component({

  selector: 'tr[riga-istanza-cerca]',
  templateUrl: './riga-cerca.component.html',
  styleUrls: ['./riga-cerca.component.css']
})
export class RigaCercaComponent implements OnInit {
  faCogs = faCogs;
  faEye = faEye;
  faEdit = faEdit;

  showFiltroMultiEnte: boolean = false;
  ente: Ente = new Ente();
  isPaidStyle: string = 'red';

  @Input('i') index: number;
  
  // riga istanza passata in input
  @Input('riga-istanza-data') rigaIstanza: Istanza;
  // pagina corrente
  @Input('currPage') currPage: number;
  //criteri ricerca
  @Input('criterioRicerca') criterioRicerca: CriterioRicercaIstanze;

  //parametro eventuali pagamenti
  @Input('isPagamenti') isPagamenti : boolean;  

  showColonnaComune: any;
  showColonnaEnteUtente: any;

  constructor(private router: Router, private moonboblService: MoonboblService, private sharedService: SharedService, private modalService: NgbModal) { }
  

  ngOnInit() {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    if (moduloSelezionato) {
      this.showColonnaComune = moduloSelezionato.showColonnaComune;
      this.showColonnaEnteUtente = moduloSelezionato.showColonnaEnteUtente;      
    }
  
    if (this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {        
      this.showFiltroMultiEnte = true;
      if(this.showFiltroMultiEnte) {
        this.getEnte(this.rigaIstanza.idEnte);
      }
    } 

    this.checkPagamenti();

  }

  mostraDettaglioIstanza() {   
    if (this.rigaIstanza.stato.idStato === 1){
      // this.router.navigate(['istanzebozza/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
      this.checkInformativa('istanzebozza/view-form');
    }
    else{
      // this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
      this.checkInformativa('istanze/view-form');
    }
  }

  // modificaIstanza() {   
  //   if (this.rigaIstanza.stato.idStato === 1){
  //     this.router.navigate(['istanzebozza/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
  //   }
  //   else{
  //     this.router.navigate(['istanze/view-form/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
  //   }    
  // }

  lavoraIstanza() {    
    // this.router.navigate(['istanza/' + this.rigaIstanza.idIstanza], {queryParams: {page: this.currPage}});
    this.checkInformativa('istanza');
  }

  private getEnte(idEnte: number): void {
    this.moonboblService.getEnte(idEnte).then(ente =>  this.ente = ente
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
    if (this.rigaIstanza.isPagato) {      
     this.isPaidStyle = this.rigaIstanza.isPagato ? 'green':'red';
    } 
 }
 
  
}
