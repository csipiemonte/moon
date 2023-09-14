/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input, OnChanges, Output, EventEmitter } from '@angular/core';
import { Istanza } from '../../../model/dto/istanza';
import { MoonboblService } from '../../../services/moonbobl.service';
import { ActivatedRoute, Router } from '@angular/router';
import { Costanti, STORAGE_KEYS } from 'src/app/common/costanti';
import { environment } from '../../../../environments/environment';
import { NgxSpinnerService } from 'ngx-spinner';
import { SharedService } from 'src/app/services/shared.service';
import { CriterioRicercaIstanze } from 'src/app/model/dto/criterio-ricerca-istanze';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { CurrentFilter } from 'src/app/common/current-filter';
import { NavSelection } from 'src/app/common/nav-selection';
import { FiltroEnte } from 'src/app/common/filtro-ente';
import { Messaggi, MsgIstanze } from 'src/app/common/messaggi';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalConfirmComponent } from '../../modal/modal-confirm/modal-confirm.component';
import { ModalActionComponent } from '../../modal/modal-action/modal-action.component';

@Component({
  selector: 'app-tab-da-completare',
  templateUrl: './tab-da-completare.component.html',
  styleUrls: ['./tab-da-completare.component.scss']
})
export class TabDaCompletareComponent implements OnInit {

  righeIstanza: Istanza[] = [];
  pageSize = environment.pageSize;
  currPage: number;
  msg = '';
  criterioRicerca: CriterioRicercaIstanze = new CriterioRicercaIstanze();
  istanzeTotali: number;
  showFiltroComune: Boolean = false;
  showFiltroDichiarante: Boolean = true;
  showColonnaComune: Boolean = false;
  showFiltroComuneUtente: Boolean = false;
  showColonnaEnte: Boolean = false;
  showFiltroEnteUtente: Boolean = false;
  showFiltroMultiEnte: Boolean = false;


  constructor(private moonboblService: MoonboblService,
    private route: ActivatedRoute,
    private router: Router,
    private spinnerService: NgxSpinnerService,
    private handleException: HandleExceptionService,
    private modalService: NgbModal,
    private sharedService: SharedService) {
  }

  @Input() tab;
  @Input('idModulo') idModulo: number;
  @Input('idComune') idComune: string;
  @Input('idEnte') idEnte: string;
  @Input('cfDichiarante') cfDichiarante: string;
  @Input('nomeDichiarante') nomeDichiarante: string;
  @Input('cognomeDichiarante') cognomeDichiarante: string;
  @Input('idMultiEnte') idMultiEnte: number;


 
  @Output('onReload') onReload = new EventEmitter();
  @Output('alertService') alert = new EventEmitter();

  ngOnInit() {

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    this.sharedService.activeTab = this.tab;
    this.pageSize = environment.pageSize;

    if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_BOZZA)?.page) {
      this.currPage = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_BOZZA)?.page;
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
  }

  ngOnChanges(val) {

    this.alert.emit({ clear: true });
    // console.log('Valore modulo selezionato onchange: ' + this.idModulo);

    // set showFiltroMultiEnte
    this.setMultiEnteParameters();

    let offset = 0;
    let limit = environment.pageSize;
    this.currPage = 1;
    if (!(val.cfDichiarante && (val.cfDichiarante.currentValue != val.cfDichiarante.previousValue)) ||
      !(val.nomeDichiarante && (val.nomeDichiarante.currentValue != val.nomeDichiarante.previousValue)) ||
      !(val.cognomeDichiarante && (val.cognomeDichiarante.currentValue != val.cognomeDichiarante.previousValue))) {
      if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_BOZZA)) {
        console.log('FILTRO: ' + JSON.stringify(this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_BOZZA)));
        let sf = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE_BOZZA);
        if (sf.page) {
          this.currPage = sf.page
        }
        offset = (this.currPage - 1) * this.pageSize;
        limit = environment.pageSize;
      }
    }


    this.righeIstanza = [];
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    if (moduloSelezionato) {
      this.showColonnaComune = moduloSelezionato.showColonnaComune;
      this.showFiltroComuneUtente = moduloSelezionato.showFiltroComuneUtente;
      this.showColonnaEnte = moduloSelezionato.showColonnaEnte;
      this.showFiltroEnteUtente = moduloSelezionato.showFiltroEnteUtente;

      console.log('showFiltroComuneUtente ' + this.showFiltroComuneUtente);
      console.log('showFiltroEnteUtente ' + this.showFiltroEnteUtente);
      console.log('showFiltroMultiEnte ' + this.showFiltroMultiEnte);

      if (!(this.showFiltroComuneUtente || this.showFiltroEnteUtente)) {
        if (this.idModulo !== undefined) {
          console.log('dichiarante ' + this.cfDichiarante);

          if (this.criterioRicerca) {
            this.criterioRicerca.nomeElementoIstanza = null;
            this.criterioRicerca.valoreElementoIstanza = null;
          }

          // this.ricercaIstanzeinBozza(0, this.pageSize, this.idModulo, this.cfDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
          this.ricercaIstanzeDaCompletare(offset, limit, this.idModulo, this.cfDichiarante, this.nomeDichiarante, this.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
        }
      }
      else {
        if (this.idModulo !== undefined && ((this.idComune !== undefined && this.idComune !== '') || (this.idEnte !== undefined && this.idEnte !== ''))) {
          console.log('idComune ' + this.idComune);
          console.log('idEnte ' + this.idEnte);
          // this.ricercaIstanzeinBozza(0, this.pageSize, this.idModulo, this.cfDichiarante, this.idComune, this.idEnte,this.idMultiEnte);
          this.ricercaIstanzeDaCompletare(offset, limit, this.idModulo, this.cfDichiarante, this.nomeDichiarante, this.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
        }
        else {
          // clean risulatato ricerca se è stato annullata la selezione comune/ente

        }
      }
    }

  }

  onDeleteRiga(rigaIstanza: Istanza) {
    console.log('elimino riga ' + rigaIstanza.idIstanza);

    const mdRef = this.modalService.open(ModalActionComponent);
    mdRef.componentInstance.modal_titolo = 'Notifica';
    mdRef.componentInstance.modal_contenuto = MsgIstanze.ALERT_CANCELLAZIONE;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      this.moonboblService.eliminaIstanza(rigaIstanza).subscribe(response => {
        this.spinnerService.hide();
        this.righeIstanza =  this.righeIstanza.filter(function(el) {return el.idIstanza !== rigaIstanza.idIstanza});
        this.ngOnInit();
       
      }, (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
    
        // this.errNotificationError.notification.next(err);
        alert(err.errorMsg);
      });
      
    });

  }

  reload(){
    this.onReload.emit()
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

  getIstanze(idModulo: number, idComune: string, idEnte: string) {

    let filtroRicerca = '';
    this.righeIstanza = [];
    if (idModulo !== undefined) {
      this.msg = '';
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

      this.moonboblService.getElencoPerModello(idModulo, Costanti.ISTANZA_STATO_BOZZA, '-dataCreazione', filtroRicerca,
        this.cfDichiarante?.toUpperCase(),
        this.nomeDichiarante?.toUpperCase(),
        this.cognomeDichiarante?.toUpperCase()
      )
        .then(righe => {
          this.righeIstanza = righe;
          if (this.righeIstanza.length === 0) {
            // this.msg = 'Nessun elemento presente !';
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


  ricercaIstanzeDaCompletare(offset: number = 0, limit: number = environment.pageSize, idModulo: number,
    cfDichiarante: string,
    nomeDichiarante: string,
    cognomeDichiarante: string,
    idComune: string, idEnte: string, idMultiEnte: number) {

    console.log('inizio ricerca');
    // resetto ricerca
    this.righeIstanza = [];
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

    if (idComune !== undefined && idComune !== '') {
      console.log('Valore comune selezionato: ' + idComune);
      this.criterioRicerca.nomeElementoIstanza = 'comune';
      this.criterioRicerca.valoreElementoIstanza = idComune;
    }
    else if (idEnte !== undefined && idEnte !== '') {
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

    this.criterioRicerca.sort = '-dataCreazione';
    this.spinnerService.show();

    this.moonboblService.cercaIstanzeDaCompletarePaginato(this.criterioRicerca, offset, limit).subscribe(
      (risposta) => {
        this.istanzeTotali = risposta.totalElements;
        this.righeIstanza = risposta.items;
        console.log(' this.istanzeTotali ' + this.istanzeTotali);
        console.log(' this.righeIstanza ' + this.righeIstanza.length);
        if (this.righeIstanza.length === 0) {
          // this.msg = 'Nessun elemento presente !';
          this.alert.emit({ clear: true });
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
          // this.msg = 'Utente non abilitato';
          this.alert.emit({ text: MsgIstanze.ERRORE_UTENTE_NON_ABILITATO, type: 'error', autoclose: false });
        }
        this.handleException.handleNotBlockingError(error);
      });
    console.log('fine ricerca');
  }

  getIstanzeNonpaginato() {
    this.moonboblService.getElencoIstanzeNonpaginato(Costanti.ISTANZA_STATO_BOZZA, null, '-dataCreazione').then(righe => {
      this.spinnerService.hide();
      this.righeIstanza = righe;
      if (this.righeIstanza.length === 0) {
        // this.msg = 'Nessuna istanza trovata !';
        this.alert.emit({ text: MsgIstanze.NO_ISTANZE, type: 'info', autoclose: true });
      }
    }).catch(errore => {
      console.log('***' + errore);
    });
  }


  incPage() {
    this.currPage = this.currPage + 1;
    console.log('Valore dopo incremento ' + this.currPage);
  }

  pageChanged($event) {
    console.log($event);

    if (this.sharedService.getCurrentFilter(NavSelection.ISTANZE_BOZZA)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.ISTANZE_BOZZA);
      filter.page = $event;
      this.sharedService.setCurrentFilter(NavSelection.ISTANZE_BOZZA, filter);
    }

    const offset = (this.currPage - 1) * this.pageSize;
    const limit = environment.pageSize;
    this.ricercaIstanzeDaCompletare(offset, limit, this.idModulo, this.cfDichiarante, this.nomeDichiarante, this.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
  }

  ricercaIstanze() {
    const offset = (this.currPage - 1) * this.pageSize;
    const limit = environment.pageSize;
    this.criterioRicerca.idModulo = this.idModulo;
    this.ricercaIstanzeDaCompletare(offset || 0, limit, this.criterioRicerca.idModulo, this.criterioRicerca.cfDichiarante, this.nomeDichiarante, this.cognomeDichiarante, this.idComune, this.idEnte, this.idMultiEnte);
  }


  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.showFiltroMultiEnte = true;
    }
  }

  salvaRicerca() {
    let currentFilter = new CurrentFilter();
    currentFilter.idModulo = Number(this.idModulo);
    currentFilter.sezione = NavSelection.ISTANZE_BOZZA;
    let dichiarante = {
      cfDichiarante: this.cfDichiarante,
      nomeDichiarante: this.nomeDichiarante,
      cognomeDichiarante: this.cognomeDichiarante
    };
    currentFilter.filter = dichiarante;
    currentFilter.page = this.currPage;
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE_BOZZA, currentFilter);
    this.sharedService.setCurrentFilter(NavSelection.ISTANZE, currentFilter);
  }

}
