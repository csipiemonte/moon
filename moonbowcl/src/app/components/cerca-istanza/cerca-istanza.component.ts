/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, HostListener, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Comune } from 'src/app/model/dto/comune';
import { Modulo } from 'src/app/model/dto/modulo';
import { Stato } from 'src/app/model/dto/stato';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
// fixme import { DatePipe } from '@angular/common';
import { compareDate } from 'src/app/services/service.utils';
import { Istanza } from 'src/app/model/dto/istanza';
import { environment } from 'buildfiles/environment.local-embedded';
import { CriterioRicercaIstanze } from 'src/app/model/dto/criterio-ricerca-istanze';
import { SharedService } from 'src/app/services/shared.service';
import { faAngleDown, faAngleUp, faCogs } from '@fortawesome/free-solid-svg-icons';
import { Nav } from 'src/app/model/dto/nav';
import { CodiceNome } from 'src/app/model/dto/codice-nome';
import { Ente } from 'src/app/model/dto/ente';
import { NavSelection } from 'src/app/common/nav-selection';
import { CurrentFilter } from 'src/app/common/current-filter';
import { Costanti, EPAY, STORAGE_KEYS } from 'src/app/common/costanti';
import { FiltroEnte } from 'src/app/common/filtro-ente';
import * as lodash from 'lodash';
import { AlertService } from 'src/app/modules/alert';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { MsgCercaIstanza } from 'src/app/common/messaggi';
import { StorageManager } from 'src/app/common/utils/storage-manager';

@Component({
  selector: 'app-cerca-istanza',
  templateUrl: './cerca-istanza.component.html',
  styleUrls: ['./cerca-istanza.component.css']
})
export class CercaIstanzaComponent implements OnInit {
  faCogs = faCogs;

  archivioForm: FormGroup;
  selectedModulo: number;
  modulo: Modulo;
  moduli: Modulo[];
  idComune: string;
  idEnte: string;
  idMultiEnte: number;
  comuni: Comune[];
  entiUtente: CodiceNome[];
  comuniUtente: Comune[];
  entiMultiEnte: Ente[];
  idProvincia: string;
  showFiltroComune: Boolean;
  showFiltroEnteUtente: Boolean;
  showFiltroComuneUtente: Boolean;
  showFiltroMultiEnte: Boolean = false;
  criterioRicerca: CriterioRicercaIstanze;
  righeIstanza: Istanza[] = [];
  pageSize = environment.pageSize;
  currPage: number;
  istanzeTotali: number;
  ShowFilter = false;
  limitSelection = false;
  stati: Array<Stato> = [];
  statiSelezionati: Array<Stato> = [];
  protocollo: string;
  codiceIstanza: string;
  nome: string;
  cognome: string;
  codiceFiscale: string;
  dataDal: Date;
  dataAl: Date;
  inviateDataDal: Date;
  inviateDataAl: Date;
  dropdownStatoSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'idStato',
    textField: 'nome',
    selectAllText: 'Seleziona tutti',
    unSelectAllText: 'Deseleziona tutti',
    itemsShowLimit: 3,
    allowSearchFilter: false
  };
  msgValidazione: string;
  showBaseFilter = false;
  showColonnaComune: boolean;
  showColonnaEnteUtente: boolean;
  isFilterNotMandatory: boolean;
  isStatoSelezionato: boolean;

  // Sort = Sort;
  // faAngleDown = faAngleDown;
  // faAngleUp = faAngleUp;
  // sortDataInvio = Sort.DESC;

  isPagamenti: Boolean = false;
  filtroEpay = EPAY.ALL;

  alertId = 'alert-cerca-istanza';
  alertOptions = {
    id: this.alertId,
    autoClose: true,
    keepAfterRouteChange: false
  };
  alertOptionsNoAutoClose = {
    id: this.alertId,
    autoClose: false,
    keepAfterRouteChange: false
  };

  constructor(private fb: FormBuilder, private moonboblService: MoonboblService, private route: ActivatedRoute,
    private spinnerService: NgxSpinnerService,
    private handleException: HandleExceptionService,
    private sharedService: SharedService,
    protected alertService: AlertService) { }

  ngOnInit() {

    this.setMultiEnteParameters();

    this.isFilterNotMandatory = this.sharedService.UserLogged.hasRuoloOperatorADV() || this.sharedService.UserLogged.isTipoADMIN();

    this.sharedService.nav = new Nav(NavSelection.CERCA, 'cerca-istanza');

    this.archivioForm = this.fb.group({
      filtroEpay: [this.filtroEpay],
      modulo: [this.moduli],
      comune: [this.comuni],
      enteUtente: [this.entiUtente],
      comuneUtente: [this.comuniUtente],
      stato: [this.statiSelezionati],
      dataDal: [''],
      dataAl: [''],
      inviateDataDal: [''],
      inviateDataAl: [''],
      codiceIstanza: [this.codiceIstanza],
      protocollo: [this.protocollo],
      nome: [this.nome],
      cognome: [this.cognome],
      codiceFiscale: [this.codiceFiscale],
      provincia: []
    });

    if (this.sharedService.mieiModuli.length > 0) {
      this.moduli = this.sharedService.mieiModuli.sort((ma, mb) =>
        ma.oggettoModulo.toLocaleLowerCase().trim().localeCompare(mb.oggettoModulo.toLocaleLowerCase().trim()));
    } else {
      this.getElencoModuli();
    }

    this.statiSelezionati = [];
    let moduloSelezionato = null;
    this.criterioRicerca = new CriterioRicercaIstanze();

    moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    if ((moduloSelezionato !== undefined) && (moduloSelezionato !== null)) {

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(moduloSelezionato.idModulo);
      // let filtroEpay = null;
      if (this.isPagamenti) {
       
        // this.archivioForm.controls['paid'].patchValue(EPAY.ALL, {
        //   onlySelf: true
        // });
        let paid = this.sharedService?.getCurrentFilter(NavSelection.CERCA)?.paid;
        if (paid) {
          this.filtroEpay = paid;    
        } else {
          // this.archivioForm.controls['paid'].patchValue(EPAY.ALL, {
          //   onlySelf: true
          // });

          this.filtroEpay = EPAY.ALL;
        }

        // filtroEpay = this.paid;
      }


      this.selectedModulo = Number(moduloSelezionato.idModulo);
      this.archivioForm.controls['modulo'].patchValue(this.selectedModulo, {
        onlySelf: true
      });

      if (moduloSelezionato.showFiltroComune) {
        this.showFiltroComune = true;
        this.showColonnaComune = true;
        const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
        if (moduloSelezionato.provinciaSelezionata) {
          this.idProvincia = moduloSelezionato.provinciaSelezionata;
          this.archivioForm.controls['provincia'].patchValue(this.idProvincia, {
            onlySelf: true
          });
          this.getElencoComuni(this.idProvincia);
          if (moduloSelezionato.comuneSelezionato) {
            this.idComune = moduloSelezionato.comuneSelezionato;
            this.archivioForm.controls['comune'].patchValue(this.idComune, {
              onlySelf: true
            });
          }
        }
      }
      if (moduloSelezionato.showFiltroEnteUtente) {
        this.getElencoEntiUtente();
        this.showFiltroEnteUtente = true;
        this.showColonnaEnteUtente = true;
        if (moduloSelezionato.enteSelezionato) {
          this.idEnte = moduloSelezionato.enteSelezionato;
          this.archivioForm.controls['enteUtente'].patchValue(this.idEnte, {
            onlySelf: true
          });
        }
      }
      if (moduloSelezionato.showFiltroComuneUtente) {
        this.getElencoComuniUtente();
        this.showFiltroComuneUtente = true;
        this.showColonnaComune = true;
        if (moduloSelezionato.comuneUtenteSelezionato) {
          this.idComune = moduloSelezionato.comuneUtenteSelezionato;
          this.archivioForm.controls['comuneUtente'].patchValue(this.idComune, {
            onlySelf: true
          });
        }
      }

      // showBaseFilter solo abilitato nel caso esistano stati
      this.getStatiBoPerModulo(this.selectedModulo);

    } else {
      this.showBaseFilter = false;
    }
    /*
        this.dropdownStatoSettings = {
          singleSelection: false,
          idField: 'idStato',
          textField: 'nome',
          selectAllText: 'Seleziona tutti',
          unSelectAllText: 'Deseleziona tutti',
          // itemsShowLimit: 6,
          itemsShowLimit: 3
    //      allowSearchFilter: this.ShowFilter
        };
    */

    console.log('FILTRO: ' + this.sharedService?.getCurrentFilter(NavSelection.CERCA));

    // if (this.sharedService?.getCurrentFilter(NavSelection.CERCA) && this.stati.length > 0) {
    if (this.sharedService?.getCurrentFilter(NavSelection.CERCA)) {
      console.log('FILTRO: ' + JSON.stringify(this.sharedService?.getCurrentFilter(NavSelection.CERCA)));
      let currentFilter = this.sharedService?.getCurrentFilter(NavSelection.CERCA);
      if (currentFilter && currentFilter.filter) {
        // let filter = this.sharedService?.getCurrentFilter(NavSelection.CERCA).filter;
        if (this.selectedModulo === Number(currentFilter.idModulo)) {
          // recupero oggetto modulo per visualizzazione risultato ricerca
          this.modulo = this.moduli.filter((modulo) => modulo.idModulo === Number(this.selectedModulo))[0];
          this.archivioForm.setValue(currentFilter.filter);
          this.currPage = currentFilter.page;

          if (currentFilter.paid) {
            this.filtroEpay = currentFilter.paid;
            // this.archivioForm.controls['paid'].patchValue(this.paid, {
            //   onlySelf: true
            // });
          }

          const limit = environment.pageSize;
          const offset = limit * this.currPage - limit;
          this.validaRicerca(offset, limit);
        }
      }
    }

  }

  ngOnChanges(val) {
    console.log('on change val ' + val);
  }


  @HostListener('click', ['$event.target']) onClick(e) {

    if (e.innerHTML === 'Seleziona tutti') {
      if (e.closest('#stati') !== null) {
        if ((e.parentNode.childNodes[0].type === 'checkbox') && e.parentNode.childNodes[0].checked === false) {
          console.log('stati selezionati uncheck');
          this.statiSelezionati = [];
        }
      }
    }
  }

  cerca() {   
    this.currPage = 1;
    this.validaRicerca(0, environment.pageSize);
  }

  validaRicerca(offset: number, limit: number) {

    this.alertService.clear(this.alertId);

    this.setMultiEnteParameters();

    this.msgValidazione = null;

    console.log('selectedModulo = ' + this.selectedModulo);
    if (this.selectedModulo) {
      this.msgValidazione = '';

      if (!this.isFilterNotMandatory && ((this.showFiltroComune || this.showFiltroComuneUtente) && !this.idComune)) {
        // this.msgValidazione = 'è obbligatorio selezionare un comune';
        
        this.alertService.info(MsgCercaIstanza.COMUNE_OBBLIGATORIO, this.alertOptionsNoAutoClose);

        this.righeIstanza = [];
      }
      else if (!this.isFilterNotMandatory && (this.showFiltroEnteUtente && !this.idEnte)) {
        // this.msgValidazione = 'è obbligatorio selezionare un ente';
        this.alertService.info(MsgCercaIstanza.ENTE_OBBLIGATORIO, this.alertOptionsNoAutoClose);
        this.righeIstanza = [];
      }
      else {

        if ((this.showFiltroComune || this.showFiltroComuneUtente) && this.idComune) {
          console.log('Valore comune selezionato: ' + this.idComune);
          this.setFilters(this.selectedModulo);
          this.criterioRicerca.nomeElementoIstanza = 'comune';
          this.criterioRicerca.valoreElementoIstanza = this.idComune;
        }
        else if (this.showFiltroEnteUtente && this.idEnte) {
          console.log('Valore ente selezionato: ' + this.idEnte);
          this.setFilters(this.selectedModulo);

          if (this.idEnte.startsWith(FiltroEnte.ENTE_CCR)) {
            this.criterioRicerca.nomeElementoIstanza = FiltroEnte.ENTE_CCR;
            this.criterioRicerca.valoreElementoIstanza = this.idEnte;
          }
          else {
            this.criterioRicerca.nomeElementoIstanza = 'asr';
            this.criterioRicerca.valoreElementoIstanza = this.idEnte;
          }
        }
        else {
          console.log('comune o ente non presenti');
          if (!this.isFilterNotMandatory) {
            this.showColonnaComune = false;
            this.showColonnaEnteUtente = false;
          }
          else {
            this.setFilters(this.selectedModulo);
          }
          //localStorage.setItem('moduloSelezionato-archivio', null);
          this.criterioRicerca.nomeElementoIstanza = null;
          this.criterioRicerca.valoreElementoIstanza = null;

        }

        // const stati: number[] = this.statiSelezionati.map(st => st.idStato);

        let stati: number[];

        if (this.archivioForm.get('stato').value) {
          stati = this.archivioForm.get('stato').value.map(st => st.idStato);
        }
        //const stati: number[] = this.archivioForm.get('stato').value.map(st => st.idStato);

        // sort
        this.criterioRicerca.sort = '-i.id_istanza';

        this.criterioRicerca.idModulo = this.archivioForm.get('modulo').value;
        this.criterioRicerca.protocollo = this.archivioForm.get('protocollo').value;
        this.criterioRicerca.codiceIstanza = this.archivioForm.get('codiceIstanza').value;
        this.criterioRicerca.cfDichiarante = this.archivioForm.get('codiceFiscale').value;
        this.criterioRicerca.nomeDichiarante = this.archivioForm.get('nome').value;
        this.criterioRicerca.cognomeDichiarante = this.archivioForm.get('cognome').value;

        // trim
        if (this.criterioRicerca.protocollo) this.criterioRicerca.protocollo = this.criterioRicerca.protocollo.trim();
        if (this.criterioRicerca.codiceIstanza) this.criterioRicerca.codiceIstanza = this.criterioRicerca.codiceIstanza.trim();
        if (this.criterioRicerca.cfDichiarante) this.criterioRicerca.cfDichiarante = this.criterioRicerca.cfDichiarante.trim();

        this.criterioRicerca.dataA = (this.archivioForm.get('dataAl').value != null &&
          this.archivioForm.get('dataAl').value instanceof Date && !isNaN(this.archivioForm.get('dataAl').value.valueOf())) ?
          this.archivioForm.get('dataAl').value : null;
        this.criterioRicerca.dataDa = (this.archivioForm.get('dataDal').value != null &&
          this.archivioForm.get('dataDal').value instanceof Date && !isNaN(this.archivioForm.get('dataDal').value.valueOf())) ?
          this.archivioForm.get('dataDal').value : null;

        this.criterioRicerca.inviateDataA = (this.archivioForm.get('inviateDataAl').value != null &&
          this.archivioForm.get('inviateDataAl').value instanceof Date && !isNaN(this.archivioForm.get('inviateDataAl').value.valueOf())) ?
          this.archivioForm.get('inviateDataAl').value : null;
        this.criterioRicerca.inviateDataDa = (this.archivioForm.get('inviateDataDal').value != null &&
          this.archivioForm.get('inviateDataDal').value instanceof Date && !isNaN(this.archivioForm.get('inviateDataDal').value.valueOf())) ?
          this.archivioForm.get('inviateDataDal').value : null;


        // impostazione filtro pagamenti
        if (this.isPagamenti) {
          this.criterioRicerca.filtroEpay = this.filtroEpay;
        }

        this.righeIstanza = [];

        if (this.checkIntervalloDateInvio() || this.checkIntervalloDateStato()) {
          // this.msgValidazione = 'Errore intervallo date';
          //@TODO esempio utilizzo alert service in ottica sostituzione notidiche ( contestuali a revisione css x rwd ) 
          this.alertService.error(MsgCercaIstanza.ERRORE_INTERVALLO_DATE, this.alertOptionsNoAutoClose);
        }
        else {
          this.cercaIstanzeConPaginazione(this.criterioRicerca, stati, offset, limit);
        }
      }
    }
    else {
      // this.msgValidazione = 'è obbligatorio selezionare un modulo';      
      this.alertService.info(MsgCercaIstanza.MODULO_OBBLIGATORIO, this.alertOptionsNoAutoClose);
    }

  }

  checkIntervalloDateStato() {
    let isIntervalOkOrNotInterval: boolean = false;
    if ((this.archivioForm.get('dataDal').value !== null) && (this.archivioForm.get('dataDal').value !== '')
      && (this.archivioForm.get('dataAl').value !== null) && (this.archivioForm.get('dataAl').value !== '')) {
      if (!compareDate(this.archivioForm.get('dataDal').value, this.archivioForm.get('dataAl').value)) {
        // this.msgValidazione = 'Errore intervallo date';
        isIntervalOkOrNotInterval = true;
      }
    }
    return isIntervalOkOrNotInterval
  }

  checkIntervalloDateInvio() {
    let isIntervalOkOrNotInterval: boolean = false;
    if ((this.archivioForm.get('inviateDataDal').value !== null) && (this.archivioForm.get('inviateDataDal').value !== '')
      && (this.archivioForm.get('inviateDataAl').value !== null) && (this.archivioForm.get('inviateDataAl').value !== '')) {
      if (!compareDate(this.archivioForm.get('inviateDataDal').value, this.archivioForm.get('inviateDataAl').value)) {
        // this.msgValidazione = 'Errore intervallo date';
        isIntervalOkOrNotInterval = true;
      }
    }
    return isIntervalOkOrNotInterval
  }

  cercaIstanzeConPaginazione(criteri, stati, offset, limit) {
    
    this.spinnerService.show();  

    // aggiornamento criteri di ricerca
    StorageManager.add(STORAGE_KEYS.KEY_CRITERIO_RICERCA, criteri);
    this.moonboblService.cercaIstanzeArchivioPaginato(criteri, stati, offset, limit).subscribe(
      (risposta) => {

        this.istanzeTotali = risposta.totalElements;
        // recupero oggetto modulo per visualizzazione risultato ricerca
        this.modulo = this.moduli.filter((modulo) => modulo.idModulo === Number(this.selectedModulo))[0];

        StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RICERCA_TOTALI, this.istanzeTotali);
        this.righeIstanza = risposta.items;
        console.log(' this.istanzeTotali ' + this.istanzeTotali);
        console.log(' this.righeIstanza ' + this.righeIstanza.length);
        StorageManager.add(STORAGE_KEYS.KEY_ISTANZE_RISULTATO_RICERCA, this.righeIstanza);

        console.log('SALVA RICERCA');
        this.salvaRicerca();

        if (this.istanzeTotali === 0) {

          this.alertService.info(MsgCercaIstanza.ISTANZE_NON_PRESENTI, this.alertOptionsNoAutoClose);
        }
        this.spinnerService.hide();
      },
      error => {
        console.log(error);
        this.spinnerService.hide();
        if (error.status === 401) {
          // this.msgValidazione = 'Utente non abilitato';
          this.alertService.info(MsgCercaIstanza.ERRORE_UTENTE_NON_ABILITATO, this.alertOptionsNoAutoClose);
        }
        this.handleException.handleNotBlockingError(error);
      });
    console.log('fine ricerca');
  }

  selectModulo(e) {

    this.alertService.clear(this.alertId);

    // reset form
    this.reset();

    // reset filtri
    this.showFiltroComune = false;
    this.showFiltroComuneUtente = false;
    this.showFiltroEnteUtente = false;
    this.showBaseFilter = false;

    // reset msg
    this.msgValidazione = '';

    // reset filtro numero istanza / protocollo
    this.codiceIstanza = null;
    this.protocollo = null;
    this.criterioRicerca.codiceIstanza = null;
    this.criterioRicerca.protocollo = null;
    this.archivioForm.patchValue({
      codiceIstanza: null,
      protocollo: null
    });

    this.selectedModulo = e;

    this.modulo = this.moduli.filter((modulo) => modulo.idModulo === Number(this.selectedModulo))[0];

    if (e) {
      this.archivioForm.get('modulo').setValue(e, {
        onlySelf: true
      });

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(this.modulo.idModulo);
      if (this.isPagamenti) {
        // this.archivioForm.controls['paid'].patchValue(EPAY.ALL, {
        //   onlySelf: true
        // });
        let paid = this.sharedService?.getCurrentFilter(NavSelection.CERCA)?.paid;
        if (paid) {
          this.filtroEpay = paid;
          // this.archivioForm.controls['paid'].patchValue(this.paid, {
          //   onlySelf: true
          // });
        } else {
          this.archivioForm.controls['filtroEpay'].patchValue(EPAY.ALL, {
            onlySelf: true
          });
          this.filtroEpay = EPAY.ALL;
        }
      }



      this.spinnerService.show();
      this.getStatiBoPerModulo(this.selectedModulo);
      this.setFilters(this.selectedModulo);

    } else {
      this.showBaseFilter = false;
    }
  }

  setFilters(e: number) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + e));
    if (datiAtt && datiAtt.attributi && datiAtt.attributi.TIPO_FILTER_BO) {
      console.log('TIPO filtro' + datiAtt.attributi.TIPO_FILTER_BO);
      const tipoShowFilter = datiAtt.attributi.TIPO_FILTER_BO;
      if (tipoShowFilter === 'showComune') {
        this.showFiltroComune = true;
        this.showColonnaComune = true;
        const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
        if (moduloSelezionato && moduloSelezionato.provinciaSelezionata) {
          this.idProvincia = moduloSelezionato.provinciaSelezionata;
          this.getElencoComuni(this.idProvincia);
          this.idComune = moduloSelezionato.comuneSelezionato;
        }
      }
      if (tipoShowFilter === 'showComuneUtente') {
        this.getElencoComuniUtente();
        this.showFiltroComuneUtente = true;
        this.showColonnaComune = true;
        const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
        if (moduloSelezionato && moduloSelezionato.comuneEnteSelezionato)
          this.idComune = moduloSelezionato.comuneEnteSelezionato;
      }
      if (tipoShowFilter === 'showEnteUtente') {
        this.getElencoEntiUtente();
        this.showFiltroEnteUtente = true;
        this.showColonnaEnteUtente = true;

        const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
        if (moduloSelezionato && moduloSelezionato.enteSelezionato) {
          this.idEnte = moduloSelezionato.enteSelezionato;
        }
      }
      if (tipoShowFilter === 'show') {
        this.getElencoEntiUtente();
        this.showFiltroEnteUtente = true;
        this.showColonnaEnteUtente = true;
      }
    }

    if (e) {
      localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify({
        idModulo: e,
        showFiltroComune: this.showFiltroComune,
        showFiltroComuneUtente: this.showFiltroComuneUtente,
        showColonnaComune: this.showColonnaComune,
        showFiltroEnteUtente: this.showFiltroEnteUtente,
        showColonnaEnteUtente: this.showColonnaEnteUtente,
        enteSelezionato: this.idEnte,
        comuneUtenteSelezionato: this.idComune,
        provinciaSelezionata: this.idProvincia,
        comuneSelezionato: this.idComune
      }));

      let mod = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
      console.log('modulo selezionato in storage = ' + JSON.stringify(mod));

    }

  }

  selectComune(e) {
    this.idComune = e;
    this.updateComuneUtenteToStorage(this.idComune);
    if (this.idProvincia) {
      this.updateComuneToStorage(this.idComune);
    }
  }

  selectEnte(e) {
    this.idEnte = e;
    this.updateEnteToStorage(this.idEnte);
  }

  updateEnteToStorage(value) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    moduloSelezionato.enteSelezionato = value;
    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify(moduloSelezionato));
  }

  updateComuneUtenteToStorage(idComune) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    moduloSelezionato.comuneUtenteSelezionato = idComune;
    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify(moduloSelezionato));
  }

  updateComuneToStorage(idComune) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    moduloSelezionato.comuneSelezionato = idComune;
    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify(moduloSelezionato));
  }

  updateProvinciaToStorage(idProvincia) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    moduloSelezionato.provinciaSelezionata = idProvincia;
    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify(moduloSelezionato));
  }

  selectProvincia(e) {
    // this.idProvincia = null;
    this.idComune = null;
    this.comuni = null;
    this.idProvincia = e;
    this.updateProvinciaToStorage(this.idProvincia);
    if (!e) {
      this.idComune = '';
    }
    else {
      this.sharedService.elencoComuni = null;
      this.getElencoComuni(e);
    }

  }

  // private getElencoComuni(idProvincia): void {
  //   this.moonboblService.getElencoComuni(idProvincia).then(comuni => {
  //     this.comuni = comuni;
  //   });
  // }

  // private getElencoEntiUtente(): void {
  //   this.moonboblService.getElencoEntiUtente().then(entiUtente => {
  //     this.entiUtente = entiUtente;
  //   });
  // }

  // private getElencoComuniUtente(): void {
  //   this.moonboblService.getElencoComuniUtente().then(
  //     comuniUtente => {
  //       this.comuniUtente = comuniUtente;
  //     }
  //   );
  // }

  private getElencoComuni(idProvincia): void {
    if (!this.sharedService.elencoComuni || this.sharedService.elencoComuni.length === 0) {
      this.moonboblService.getElencoComuni(idProvincia).then(comuni => {
        this.comuni = comuni;
        this.sharedService.elencoComuni = comuni;
      });
    }
    else {
      this.comuni = this.sharedService.elencoComuni;
    }
  }

  private getElencoEntiUtente(): void {
    if (!this.sharedService.elencoEntiUtente || this.sharedService.elencoEntiUtente.length === 0) {
      this.moonboblService.getElencoEntiUtente().then(entiUtente => {
        this.entiUtente = entiUtente;
        this.sharedService.elencoEntiUtente = entiUtente;
      });
    }
    else {
      this.entiUtente = this.sharedService.elencoEntiUtente;
    }
  }

  private getElencoComuniUtente(): void {
    if (!this.sharedService.elencoComuniUtente || this.sharedService.elencoComuniUtente.length === 0) {
      this.moonboblService.getElencoComuniUtente().then(
        comuniUtente => {
          this.comuniUtente = comuniUtente;
          this.sharedService.elencoComuniUtente = comuniUtente;
        }
      );
    }
    else {
      this.comuniUtente = this.sharedService.elencoComuniUtente;
    }
  }

  private getElencoModuli(): void {
    this.moonboblService.getElencoModuli(true).subscribe(
        (moduli) => {

        this.moduli = moduli;
        // const unuqueByIdModulo = uniqueByPropMaxValue(moduli,'idModulo','versioneModulo');
        // this.moduli = unuqueByIdModulo;  
        this.sharedService.mieiModuli = this.moduli;

        this.moduli.forEach((modulo) => {
          localStorage.setItem(STORAGE_KEYS.MODATTR + modulo.idModulo, modulo.objAttributi);
        });

        if (this.moduli.length === 1) {
          this.selectedModulo = this.moduli[0].idModulo;
          this.selectModulo(this.selectedModulo);
        } else {
          const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
          if (moduloSelezionato) {
            this.selectedModulo = moduloSelezionato.idModulo;
          }
        }
      }
    );

  }

  onItemStatoSelect(item: any) {
    console.log('onItemStatoSelect', item);
    this.statiSelezionati.push(item);
    console.log('stati selezionati ' + JSON.stringify(this.statiSelezionati));
  }

  onItemStatoDeSelect(item: any) {
    console.log('onItemStatoDeSelect', item);
    this.statiSelezionati = this.statiSelezionati.filter((stato) => stato.idStato !== item.idStato);
    console.log('on deselect stati selezionati ' + JSON.stringify(this.statiSelezionati));
  }

  onItemStatoSelectAll(items: any) {
    this.statiSelezionati = [...this.stati];
    console.log('stati selezionati all' + JSON.stringify(this.statiSelezionati));
    console.log('stati selezionati lenght ' + this.statiSelezionati.length);
  }

  //  toogleShowFilter() {
  //    this.ShowFilter = !this.ShowFilter;
  //    this.dropdownStatoSettings = Object.assign({}, this.dropdownStatoSettings, { allowSearchFilter: this.ShowFilter });
  //  }

  handleLimitSelection() {
    if (this.limitSelection) {
      this.dropdownStatoSettings = Object.assign({}, this.dropdownStatoSettings, { limitSelection: 2 });
    } else {
      this.dropdownStatoSettings = Object.assign({}, this.dropdownStatoSettings, { limitSelection: null });
    }
  }

  getStatiBoPerModulo(idModulo: number) {
    this.stati = [];
    if (idModulo !== undefined) {
      this.moonboblService.getStatiBoPerModulo(idModulo).then(stati => {
        // stati = lodash.remove(stati, stato => stato.idStato !== Costanti.ISTANZA_STATO_BOZZA);
        this.stati = stati;
        if (this.stati.length === 0) {
          //this.msg = 'Nessuno stato trovato !';
          //this.msgValidazione = 'Nessuno stato trovato !';
          this.alertService.info(MsgCercaIstanza.STATI_NON_DISPONIBILI, this.alertOptionsNoAutoClose);
        } else {
          this.showBaseFilter = true;
        }
        this.spinnerService.hide();
        console.log('spinner hide');
      }).catch(errore => {
        this.spinnerService.hide();
        console.log('***' + errore);
      });
    } else {
      // this.msgValidazione = 'Selezionare una tipologia di modulo !';
      this.alertService.error(MsgCercaIstanza.ERRORE_INTERVALLO_DATE, this.alertOptionsNoAutoClose);
      this.spinnerService.hide();
    }
  }

  incPage() {
    this.currPage = this.currPage + 1;
    console.log('Valore dopo incremento ' + this.currPage);
  }

  pageChanged($event) {
    const limit = environment.pageSize;
    const offset = limit * this.currPage - limit;
    this.validaRicerca(offset, limit);
  }

  updateResult($event) {
    const limit = environment.pageSize;
    const offset = limit * this.currPage - limit;
    this.validaRicerca(offset, limit);
  }

  reset() {
    //console.log('reset');
    this.alertService.clear(this.alertId);
    this.archivioForm.reset();
    this.msgValidazione = '';
    this.showBaseFilter = false;
    this.criterioRicerca = new CriterioRicercaIstanze();
    this.righeIstanza = [];
    this.showFiltroComune = false;
    this.showFiltroComuneUtente = false;
    this.showColonnaComune = false;
    this.showFiltroEnteUtente = false;
    this.showColonnaEnteUtente = false;
    this.idComune = null;
    this.idProvincia = null;
    this.idEnte = null;
    this.selectedModulo = null;
    this.idMultiEnte = null;

    this.isPagamenti = false;

    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, null);
    localStorage.setItem(STORAGE_KEYS.MODATTR, null);

    this.sharedService.setCurrentFilter(NavSelection.CERCA, null);
  }

  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.idMultiEnte = this.sharedService.UserLogged.ente.idEnte;
      this.showFiltroMultiEnte = true;
      if (this.criterioRicerca) {
        this.criterioRicerca.idMultiEnte = this.idMultiEnte;
      }
    }
  }


  salvaRicerca() {
    let currentFilter = new CurrentFilter();
    currentFilter.idModulo = this.selectedModulo;
    currentFilter.sezione = NavSelection.CERCA;
    currentFilter.filter = this.archivioForm.getRawValue();
    currentFilter.page = this.currPage;
    currentFilter.paid = this.filtroEpay;
    this.sharedService.setCurrentFilter(NavSelection.CERCA, currentFilter);
  }


  getPagamentiAttribute(idModulo: any) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + idModulo));
    if (!datiAtt) {
      console.log('cerca-istanza::getPagamentiAttribute ERROR NOT FOUND datiAtt: modattr' + idModulo);
      return false;
    }
    console.log('cerca-istanza::getPagamentiAttribute datiAtt = ' + datiAtt);
    console.log('cerca-istanza::getPagamentiAttribute datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    if (datiAtt.attributi?.PSIT_EPAY && datiAtt.attributi.PSIT_EPAY === 'S') {
      return true;
    } else {
      return false;
    }
  }

  changePaid(e) {
    console.log(e.target.value);
    this.filtroEpay = e.target.value;
    this.currPage = 1;

    let stati: number[];
    if (this.archivioForm.get('stato').value) {
      stati = this.archivioForm.get('stato').value.map(st => st.idStato);
    }

    if (this.sharedService.getCurrentFilter(NavSelection.CERCA)) {
      let filter = this.sharedService.getCurrentFilter(NavSelection.CERCA);
      if (this.filtroEpay) {
        filter.paid = this.filtroEpay;
        this.sharedService.setCurrentFilter(NavSelection.CERCA, filter);
      }
    }

    this.alertService.clear(this.alertId);

    this.criterioRicerca.filtroEpay = this.filtroEpay;
        
    this.cercaIstanzeConPaginazione(this.criterioRicerca, stati, 0, environment.pageSize);
  }


  getValue(obj: HTMLInputElement) {
    return obj.value;
  }

}
