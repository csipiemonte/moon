/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { Istanza } from '../../../model/dto/istanza';
import { environment } from '../../../../environments/environment';
import { MoonboblService } from '../../../services/moonbobl.service';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedService } from 'src/app/services/shared.service';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { CurrentFilter } from 'src/app/common/current-filter';
import { NavSelection } from 'src/app/common/nav-selection';
import { FiltroEnte } from 'src/app/common/filtro-ente';
import { MsgIstanze } from 'src/app/common/messaggi';
import { EPAY, STORAGE_KEYS } from 'src/app/common/costanti';
import { Sort } from 'src/app/common/sort';
import { faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { Dichiarante } from 'src/app/model/dto/dichiarante';

@Component({
  selector: 'app-tab-lavorazione',
  templateUrl: './tab-lavorazione.component.html',
  styleUrls: ['./tab-lavorazione.component.css']
})
export class TabLavorazioneComponent implements OnInit, OnChanges {

  @Input() tab;

  righeIstanza: Istanza[] = [];
  pageSize = environment.pageSize;
  currPage: number;
  istanzeTotali: number;
  msg = '';
  showFiltroComune: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showFiltroComuneUtente: Boolean = false;
  showColonnaComune: Boolean = false;
  showFiltroEnteUtente: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroMultiEnte: Boolean = false;

  Sort = Sort;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;
  sortDataInvio = Sort.DESC;

  isPagamenti: Boolean = false;
  paid = EPAY.ALL;

  @Input('idModulo') idModulo: string;
  @Input('idComune') idComune: string;
  @Input('idEnte') idEnte: string;
  @Input('cfDichiarante') cfDichiarante: string;
  @Input('nomeDichiarante') nomeDichiarante: string;
  @Input('cognomeDichiarante') cognomeDichiarante: string;
  @Input('idMultiEnte') idMultiEnte: number;

  @Output('alertService') alert = new EventEmitter();

  constructor(private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private handleException: HandleExceptionService,
    private sharedService: SharedService) {
  }

  ngOnInit() {

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    this.sharedService.activeTab = this.tab;
    this.pageSize = environment.pageSize;

    if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)?.page) {
      this.currPage = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)?.page;
      if (this.currPage != 1) {
        this.pageChanged(this.currPage);
      }
    }
    else {
      this.route.queryParams.subscribe(
        (params) => {
          this.currPage = +params['page'];
          if (!this.currPage) {
            this.currPage = 1;
          }
          console.log('Pagina corrente: ' + this.currPage);
        }
      );
    }

    console.log('Current Page: ' + this.currPage);
  }

  ngOnChanges(val) {

    this.alert.emit({ clear: true });

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    let offset = 0;
    let limit = environment.pageSize;
    this.currPage = 1;
    if (!(val.cfDichiarante && (val.cfDichiarante.currentValue != val.cfDichiarante.previousValue)) ||
        !(val.nomeDichiarante && (val.nomeDichiarante.currentValue != val.nomeDichiarante.previousValue)) ||
        !(val.cognomeDichiarante && (val.cognomeDichiarante.currentValue != val.cognomeDichiarante.previousValue))) {
      if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)) {
        console.log('FILTRO: ' + JSON.stringify(this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)));
        let sf = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE);
        if (sf.page) {
          this.currPage = sf.page
        }

        if (sf.paid) {
          this.paid = sf.paid;
        }

        offset = (this.currPage - 1) * this.pageSize;
        limit = environment.pageSize;
      }
    }


    this.righeIstanza = [];
    console.log('Valore modulo selezionato onchange: ' + this.idModulo);

    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    if (moduloSelezionato) {

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(moduloSelezionato.idModulo);
      let filtroEpay = null;
      if (this.isPagamenti) {
        filtroEpay = this.paid;
      }

      this.showColonnaComune = moduloSelezionato.showColonnaComune;
      this.showFiltroComuneUtente = moduloSelezionato.showFiltroComuneUtente;
      this.showColonnaEnte = moduloSelezionato.showColonnaEnte;
      this.showFiltroEnteUtente = moduloSelezionato.showFiltroEnteUtente;

      console.log('showFiltroComuneUtente ' + this.showFiltroComuneUtente);
      console.log('showFiltroEnteUtente ' + this.showFiltroEnteUtente);
      console.log('showFiltroMultiEnte ' + this.showFiltroMultiEnte);

      if (!(this.showFiltroComuneUtente || this.showFiltroEnteUtente)) {
        if (this.idModulo !== undefined) {
          console.log('cfDichiarante ' + this.cfDichiarante);

          // this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte);
          this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte, filtroEpay, offset, limit);
        }
      }
      else {
        if (this.idModulo !== undefined && ((this.idComune !== undefined && this.idComune !== '') || (this.idEnte !== undefined && this.idEnte !== ''))) {
          console.log('idComune ' + this.idComune);
          console.log('idEnte ' + this.idEnte);
          // this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte);
          this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte, filtroEpay, offset, limit);
        }
        else {
          // clean risulatato ricerca se è stato annullata la selezione comune/ente       
        }
      }
    }
  }

  getPagamentiAttribute(idModulo: any) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + idModulo));
    if (!datiAtt) {
      console.log('tab-lavorazione::getPagamentiAttribute ERROR NOT FOUND datiAtt: modattr' + idModulo);
      return false;
    }
    console.log('tab-lavorazione::getPagamentiAttribute datiAtt = ' + datiAtt);
    console.log('tab-lavorazione::getPagamentiAttribute datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    if (datiAtt.attributi?.PSIT_EPAY && datiAtt.attributi.PSIT_EPAY === 'S') {
      return true;
    } else {
      return false;
    }
  }


  getIstanze(idModulo: string, idComune: string, idEnte: string, idMultiEnte: number, filtroEpay: string, offset: number = 0, limit: number = environment.pageSize) {

    let filtroRicerca = '';
    if (idComune !== undefined && idComune !== '') {
      console.log('Valore comune selezionato: ' + idComune);
      filtroRicerca = 'comune=' + idComune;
    }
    else if (idEnte !== undefined && idEnte !== '') {
      console.log('Valore ente selezionato: ' + idEnte);
      //filtroRicerca = 'asr=' + idEnte;
      if (this.idEnte.startsWith(FiltroEnte.ENTE_CCR)) {
        filtroRicerca = FiltroEnte.ENTE_CCR + '=' + idEnte;
      }
      else {
        filtroRicerca = 'asr=' + idEnte;
      }
    }

    //disabilitazione spinner per filtro epay
    // if (!filtroEpay) {
    //   this.spinnerService.show();
    // }
    let sort = '-dataCreazione';
    if (this.sortDataInvio == Sort.ASC){
      sort = '+dataCreazione';
    }
  
    this.spinnerService.show();

    this.moonboblService.getElencoIstanzeInLavorazionePaginate(idModulo, sort, filtroRicerca, 
      this.cfDichiarante?.toUpperCase(),
      this.nomeDichiarante?.toUpperCase(), 
      this.cognomeDichiarante?.toUpperCase(),  
      idMultiEnte, filtroEpay, offset, limit).subscribe(righe => {
      this.istanzeTotali = righe.totalElements;
      this.righeIstanza = righe.items;
      if (this.righeIstanza.length === 0) {
        console.log('qui ' + this.righeIstanza.length);
        this.msg = 'Nessun elemento presente !';
        // this.alertService.info(MsgIstanze.NO_ISTANZE, this.alertOptions); 
        this.alert.emit({ clear: true});
        this.alert.emit({ text: MsgIstanze.NO_ISTANZE, type: 'info', autoclose: true });
      } else {
        this.msg = null;
      }

      this.salvaRicerca();

      this.spinnerService.hide();
    },
      error => {
        console.log(error);
        this.spinnerService.hide();
        if (error.status === 401) {
          // this.msg = 'Utente non abilitato';
          this.alert.emit({ text: MsgIstanze.ERRORE_UTENTE_NON_ABILITATO, type: 'error', autoclose: false });
        }
        this.handleException.handleNotBlockingError(error);
      });

  }


  incPage() {
    this.currPage = this.currPage + 1;
    console.log('Valore dopo incremento ' + this.currPage);
  }

  pageChanged($event) {
    console.log($event);

    if (this.sharedService.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE);
      filter.page = $event;
      this.sharedService.setCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE, filter);
    }

    const limit = environment.pageSize;
    const offset = limit * this.currPage - limit;
    this.ricercaIstanze(offset, limit);
  }

  ricercaIstanze(offset: number, limit: number) {
    if (!this.righeIstanza) {
      this.righeIstanza = [];
    }
    console.log('Valore modulo selezionato onchange: ' + this.idModulo);
    if (!this.showFiltroComuneUtente) {
      if (this.idModulo !== undefined) {
        this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte, this.paid, offset, limit);
      }
    } else {
      if (this.idModulo !== undefined && this.idComune !== undefined) {
        this.getIstanze(this.idModulo, this.idComune, this.idEnte, this.idMultiEnte, this.paid, offset, limit);
      }
    }
  }

  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.showFiltroMultiEnte = true;
    }
  }

  salvaRicerca() {
    let currentFilter = new CurrentFilter();
    currentFilter.idModulo = Number(this.idModulo);
    currentFilter.sezione = NavSelection.ISTANZE_LAVORAZIONE;
    let dichiarante = {
      cfDichiarante: this.cfDichiarante,
      nomeDichiarante: this.nomeDichiarante,
      cognomeDichiarante: this.cognomeDichiarante
    };
    currentFilter.filter = dichiarante;
    currentFilter.page = this.currPage;
    currentFilter.paid = this.paid;
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE, currentFilter);
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE, currentFilter);
  }

  changePaid(e) {
    console.log(e.target.value);
    this.paid = e.target.value;
    this.currPage = 1;

    if (this.sharedService.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE);
      if (this.paid) {
        filter.paid = this.paid;
        this.sharedService.setCurrentFilter(NavSelection.ISTANZE_LAVORAZIONE, filter);
      }
    }

    this.ricercaIstanze(0, environment.pageSize);
  }

  toggleDataInvio() {
    // this.sortDataInvio = !this.sortDataInvio ;
    if (this.sortDataInvio === Sort.ASC) {
      this.sortDataInvio = Sort.DESC
    }
    else {
      this.sortDataInvio = Sort.ASC
    }
    this.currPage = 1;
    this.ricercaIstanze(0, environment.pageSize);
  }



}
