/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input, OnChanges, EventEmitter, Output } from '@angular/core';
import { Istanza } from '../../../model/dto/istanza';
import { environment } from '../../../../environments/environment';
import { MoonboblService } from '../../../services/moonbobl.service';
import { ActivatedRoute } from '@angular/router';
import { Costanti, STORAGE_KEYS, EPAY } from 'src/app/common/costanti';
import { NgxSpinnerService } from 'ngx-spinner';
import { CriterioRicercaIstanze } from '../../../model/dto/criterio-ricerca-istanze';
import { HandleExceptionService } from '../../../services/handle-exception.service';
import { SharedService } from 'src/app/services/shared.service';
import { CurrentFilter } from 'src/app/common/current-filter';
import { NavSelection } from 'src/app/common/nav-selection';
import { FiltroEnte } from 'src/app/common/filtro-ente';
import { MsgIstanze } from 'src/app/common/messaggi';
import { faAngleDown, faAngleUp } from '@fortawesome/free-solid-svg-icons';
import { Sort } from 'src/app/common/sort';
import { StorageManager } from 'src/app/common/utils/storage-manager';

@Component({
  selector: 'app-tab-pervenute',
  templateUrl: './tab-pervenute.component.html',
  styleUrls: ['./tab-pervenute.component.css']
})
export class TabPervenuteComponent implements OnInit, OnChanges {
  righeIstanza: Istanza[] = [];


  pageSize = environment.pageSize;
  currPage: number;
  msg = '';
  criterioRicerca: CriterioRicercaIstanze;
  istanzeTotali: number;
  showColonnaComune: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showFiltroComuneUtente: Boolean = false;
  showFiltroEnteUtente: Boolean = false;
  showFiltroMultiEnte: Boolean = false;

  Sort = Sort;
  faAngleDown = faAngleDown;
  faAngleUp = faAngleUp;
  sortDataInvio = Sort.DESC;

  isPagamenti: Boolean = false;
  paid = EPAY.ALL;

  @Input() tab;
  @Input('idModulo') idModulo: number;
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

  ngOnChanges(val) {

    this.alert.emit({ clear: true });

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    this.sharedService.activeTab = this.tab;
    console.log('Valore modulo selezionato onchange: ' + this.idModulo);

    console.log('value changed ' + val);

    let offset = 0;
    let limit = environment.pageSize;
    this.currPage = 1;
    if (!(val.cfDichiarante && (val.cfDichiarante.currentValue != val.cfDichiarante.previousValue)) ||
    !(val.nomeDichiarante && (val.nomeDichiarante.currentValue != val.nomeDichiarante.previousValue)) ||
    !(val.cognomeDichiarante && (val.cognomeDichiarante.currentValue != val.cognomeDichiarante.previousValue))) {
      if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)) {
        console.log('FILTRO: ' + JSON.stringify(this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)));
        let sf = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE);
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
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    if (moduloSelezionato != null) {

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(moduloSelezionato.idModulo);

      this.showColonnaComune = moduloSelezionato.showColonnaComune;
      this.showFiltroComuneUtente = moduloSelezionato.showFiltroComuneUtente;
      this.showColonnaEnte = moduloSelezionato.showColonnaEnte;
      this.showFiltroEnteUtente = moduloSelezionato.showFiltroEnteUtente;
      //this.showFiltroMultiEnte = moduloSelezionato.showFiltroMultiEnte;

      console.log('showFiltroComuneUtente ' + this.showFiltroComuneUtente);
      console.log('showFiltroEnteUtente ' + this.showFiltroEnteUtente);

      if (!(this.showFiltroComuneUtente || this.showFiltroEnteUtente)) {
        if (this.idModulo !== undefined) {

          console.log('cfDichiarante ' + this.cfDichiarante);

          if (this.criterioRicerca) {
            this.criterioRicerca.nomeElementoIstanza = null;
            this.criterioRicerca.valoreElementoIstanza = null;
            this.istanzeTotali = null;
            this.righeIstanza = null;
            StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA, null);
            StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI, null);
          }

          // this.ricercaIstanze(0, environment.pageSize, this.idModulo, this.cfDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
          this.ricercaIstanze(offset, limit, this.idModulo, this.cfDichiarante, this.nomeDichiarante, this.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
        }
      }
      else {
        if (this.idModulo !== undefined && ((this.idComune !== undefined && this.idComune !== '') || (this.idEnte !== undefined && this.idEnte !== ''))) {
          console.log('idComune ' + this.idComune);
          console.log('idEnte ' + this.idEnte);
          console.log('idEnte selezionato portale multi ente ' + this.idEnte);
          // this.ricercaIstanze(0, environment.pageSize, this.idModulo, this.cfDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
          this.ricercaIstanze(offset, limit, this.idModulo, this.cfDichiarante,  this.nomeDichiarante, this.cognomeDichiarante,this.idComune, this.idEnte, this.idMultiEnte);
        }
        else {
          // clean risulatato ricerca se è stato annullata la selezione comune/ente
          StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA, null);
          StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI, null);
        }
      }

    }
    else {
      StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA, null);
      StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI, null);
    }


  }

  getPagamentiAttribute(idModulo: any) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + idModulo));
    if (!datiAtt) {
      console.log('tab-pervenute::getPagamentiAttribute ERROR NOT FOUND datiAtt: modattr' + idModulo);
      return false;
    }
    console.log('tab-pervenute::getPagamentiAttribute datiAtt = ' + datiAtt);
    console.log('tab-pervenute::getPagamentiAttribute datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    if (datiAtt.attributi?.PSIT_EPAY && datiAtt.attributi.PSIT_EPAY === 'S') {
      return true;
    } else {
      return false;
    }
  }

  ngOnInit() {

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    console.log('init');
    // const obj = StorageManager.get(STORAGE_KEYS.KEY_CRITERIO_RICERCA);
    // if (obj == null) {
    //   this.criterioRicerca = new CriterioRicercaIstanze();
    // } else {
    //   this.criterioRicerca = obj;
    // }

    // const ris = StorageManager.get(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA);
    // if (ris == null) {
    //   this.righeIstanza = [];
    // } else {
    //   this.righeIstanza = ris;
    // }
    // this.istanzeTotali = StorageManager.get(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI);

    if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)?.page) {
      this.currPage = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)?.page;
      if (this.paid) {
        this.paid = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)?.paid;
      }
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
    console.log('fine init');

  }

  ricercaIstanze(offset: number, limit: number, idModulo: number, 
    cfDichiarante: string, 
    nomeDichiarante: string,
    cognomeDichiarante: string,
    idComune: string, idEnte: string, idMultiEnte: number) {
    console.log('inizio ricerca');
    // resetto ricerca

    if (!this.righeIstanza) {
      this.righeIstanza = [];
    }
    if (this.criterioRicerca === undefined) {
      this.criterioRicerca = new CriterioRicercaIstanze();
    }
    this.criterioRicerca.idModulo = idModulo;
    
    if (cfDichiarante !== undefined) {
      this.criterioRicerca.cfDichiarante = cfDichiarante.toUpperCase();
    }

    if (nomeDichiarante !== undefined) {
      this.criterioRicerca.nomeDichiarante = nomeDichiarante.toUpperCase();
    }
    if (cognomeDichiarante !== undefined) {
      this.criterioRicerca.cognomeDichiarante = cognomeDichiarante.toUpperCase();
    }
    this.criterioRicerca.stato = Costanti.ISTANZA_STATO_INVIATA;
    if (idComune !== undefined && idComune !== '') {
      console.log('Valore comune selezionato: ' + idComune);
      this.criterioRicerca.nomeElementoIstanza = 'comune';
      this.criterioRicerca.valoreElementoIstanza = idComune;
    }
    if (idEnte !== undefined && idEnte !== '') {
      console.log('Valore ente selezionato: ' + idEnte);
      // this.criterioRicerca.nomeElementoIstanza = 'asr';
      // this.criterioRicerca.valoreElementoIstanza = idEnte;
      if (this.idEnte.startsWith(FiltroEnte.ENTE_CCR)) {
        this.criterioRicerca.nomeElementoIstanza = FiltroEnte.ENTE_CCR;
        this.criterioRicerca.valoreElementoIstanza = this.idEnte;
      }
      else {
        this.criterioRicerca.nomeElementoIstanza = 'asr';
        this.criterioRicerca.valoreElementoIstanza = this.idEnte;
      }
    }
    if (idMultiEnte !== undefined) {
      console.log('Valore ente selezionato portale multiente: ' + idMultiEnte);
      this.criterioRicerca.idMultiEnte = idMultiEnte;
    }

    if (this.isPagamenti) {
      this.criterioRicerca.filtroEpay = this.paid;
    }
    // else {
    //   // abilitazione spinner quando non c'è filtro epay
    //   this.spinnerService.show();
    // }

    // this.criterioRicerca.sort = 'dataCreazione';

    if (!this.criterioRicerca.sort) {
      this.criterioRicerca.sort = '-dataCreazione';
    }

    // DISABILITAZIONE SPINNER
    this.spinnerService.show();

    StorageManager.add(STORAGE_KEYS.KEY_CRITERIO_RICERCA, this.criterioRicerca);
    this.moonboblService.cercaIstanzePaginato(this.criterioRicerca, offset, limit).subscribe(
      (risposta) => {
        this.istanzeTotali = risposta.totalElements;
        StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI, this.istanzeTotali);
        this.righeIstanza = risposta.items;
        console.log(' this.istanzeTotali ' + this.istanzeTotali);
        console.log(' this.righeIstanza ' + this.righeIstanza.length);
        StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA, this.righeIstanza);

        if (this.righeIstanza.length === 0) {
          // this.msg = 'Nessun elemento presente !';
          this.alert.emit({ clear: true});
          this.alert.emit({ text: MsgIstanze.NO_ISTANZE, type: 'info', autoclose: true });
        } else {
          this.msg = '';
        }

        this.salvaRicerca();

        this.spinnerService.hide();


      },
      error => {
        console.log(error);
        this.spinnerService.hide();
        if (error.status === 401) {
          //this.resetFiltroEnti();
          // this.msg = 'Utente non abilitato';
          this.alert.emit({ text: MsgIstanze.ERRORE_UTENTE_NON_ABILITATO, type: 'error', autoclose: false });
        }
        this.handleException.handleNotBlockingError(error);
      });
    console.log('fine ricerca');
  }

  onDeleteRiga(rigaIstanza: Istanza) {
    console.log('elimino riga ' + rigaIstanza.idIstanza);
  }

  onCambiaImportanza(rigaIstanza: Istanza) {
    // console.log('onCambiaImportanza id=' + rigaIstanza.idIstanza);
    // console.log('onCambiaImportanza imp=' + rigaIstanza.importanza);
    if (rigaIstanza.importanza === 1) {
      rigaIstanza.importanza = 0;
    } else {
      rigaIstanza.importanza = 1;
    }
    this.moonboblService.cambiaImportanzaIstanza(rigaIstanza).subscribe(response => {
      const respIstanza = response as Istanza;
      rigaIstanza.importanza = respIstanza.importanza;
    });
  }



  incPage() {
    this.currPage = this.currPage + 1;
    console.log('Valore dopo incremento ' + this.currPage);
  }

  pageChanged($event) {
    console.log($event);

    if (this.sharedService.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE);
      filter.page = $event;
      this.sharedService.setCurrentFilter(NavSelection.ISTANZE_PERVENUTE, filter);
    }

    const offset = (this.currPage - 1) * this.pageSize;
    const limit = environment.pageSize;
    this.criterioRicerca = StorageManager.get(STORAGE_KEYS.KEY_CRITERIO_RICERCA);

    this.ricercaIstanze(offset, limit, this.criterioRicerca.idModulo, this.criterioRicerca.cfDichiarante, this.criterioRicerca.nomeDichiarante,this.criterioRicerca.cognomeDichiarante,this.idComune, this.idEnte, this.idMultiEnte);
  }

  getElencoNonPaginato(idModulo: number, idComune: string) {
    let filtroRicerca = '';
    this.righeIstanza = [];
    if (idModulo !== undefined) {
      this.msg = '';
      if (idComune !== undefined && idComune !== '') {
        console.log('Valore comune selezionato: ' + idComune);
        filtroRicerca = 'comune=' + idComune;
      } else {
        console.log('comune non presente');
        filtroRicerca = '';
      }
      this.moonboblService.getElencoPerModello(idModulo, Costanti.ISTANZA_STATO_INVIATA, '', filtroRicerca, this.cfDichiarante,this.nomeDichiarante,this.cognomeDichiarante)
        .then(righe => {
          this.righeIstanza = righe;
          if (this.righeIstanza.length === 0) {
            // this.msg = 'Nessuna istanza trovata !';
            this.alert.emit({ text: MsgIstanze.NO_ISTANZE, type: 'info', autoclose: true });
          }
          this.spinnerService.hide();
          console.log('spinner hide');
        }

        ).catch(errore => {
          this.spinnerService.hide();
          console.log('***' + errore);
        });
    } else {
      // this.msg = 'Selezionare una tipologia di modulo !';
      this.alert.emit({ text: MsgIstanze.MODULO_OBBLIGATORIO, type: 'info', autoclose: false });
      this.spinnerService.hide();
    }
  }


  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.showFiltroMultiEnte = true;
    }
  }


  // resetFiltroEnti() {
  //   this.showColonnaComune = false;
  //   this.showFiltroComuneUtente = false;
  //   this.showColonnaEnte = false;
  //   this.showFiltroEnteUtente = false;
  // }


  salvaRicerca() {
    let currentFilter = new CurrentFilter();
    currentFilter.idModulo = Number(this.idModulo);
    currentFilter.sezione = NavSelection.ISTANZE_PERVENUTE;
    let dichiarante = {
      cfDichiarante: this.cfDichiarante,
      nomeDichiarante: this.nomeDichiarante,
      cognomeDichiarante: this.cognomeDichiarante
    };
    currentFilter.filter = dichiarante;
    currentFilter.page = this.currPage;
    currentFilter.paid = this.paid;
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE_PERVENUTE, currentFilter);
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE, currentFilter);
  }

  changePaid(e) {
    console.log(e.target.value);
    this.paid = e.target.value;
    // const offset = (this.currPage - 1) * this.pageSize;
    // const limit = environment.pageSize;

    if (this.sharedService.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.ISTANZE_PERVENUTE);
      if (this.paid) {
        filter.paid = this.paid;
        this.sharedService.setCurrentFilter(NavSelection.ISTANZE_PERVENUTE, filter);
      }
    }

    this.criterioRicerca = StorageManager.get(STORAGE_KEYS.KEY_CRITERIO_RICERCA);

    // this.ricercaIstanze(offset, limit, this.criterioRicerca.idModulo, this.criterioRicerca.cfDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
    this.currPage = 1;
    this.ricercaIstanze(0, environment.pageSize, this.criterioRicerca.idModulo, this.criterioRicerca.cfDichiarante, this.criterioRicerca.nomeDichiarante,this.criterioRicerca.cognomeDichiarante,this.idComune, this.idEnte, this.idMultiEnte);

  }

  toggleDataInvio() {

    // this.sortDataInvio = !this.sortDataInvio ;

    if (this.sortDataInvio === Sort.ASC) {
      this.sortDataInvio = Sort.DESC
    }
    else {
      this.sortDataInvio = Sort.ASC
    }

    this.criterioRicerca = StorageManager.get(STORAGE_KEYS.KEY_CRITERIO_RICERCA);
    if (this.sortDataInvio === Sort.DESC) {
      this.criterioRicerca.sort = '-dataCreazione';
    }
    else if (this.sortDataInvio === Sort.ASC) {
      this.criterioRicerca.sort = '+dataCreazione';
    }
    this.currPage = 1;
    this.ricercaIstanze(0, environment.pageSize, this.criterioRicerca.idModulo, this.criterioRicerca.cfDichiarante, this.criterioRicerca.nomeDichiarante, this.criterioRicerca.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
  }

  // setPagamentiAttribute(idModulo,
  //   idVersioneModulo){
  //   this.moonboblService.getModuloWithFields(idModulo,
  //     idVersioneModulo,
  //     'attributiEpay').subscribe(
  //       (modulo: Modulo) => {
  //         if ( modulo.attributi &&  modulo.attributi.length > 0) {
  //           this.isPagamenti = (modulo.attributi.find(ma => ma.nome === 'PSIT_EPAY').valore === 'S') ?  true : false
  //         }      
  //       },
  //       (err: MoonboError) => {         
  //         this.alert.emit({ text: err.errorMsg, type: 'info', autoclose:true});
  //       }
  //     );
  // }
}
