/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, ComponentFactoryResolver, HostListener, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgxSpinnerService } from 'ngx-spinner';
import { Campo } from 'src/app/model/dto/campo';
import { Comune } from 'src/app/model/dto/comune';
import { Modulo } from 'src/app/model/dto/modulo';
import { Stato } from 'src/app/model/dto/stato';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { DatePipe } from '@angular/common';
import { compareDate, saveBlobIE } from 'src/app/services/service.utils';
import { CodiceNome } from 'src/app/model/dto/codice-nome';
import { SharedService } from 'src/app/services/shared.service';
import { environment } from '../../../environments/environment';
import { ExportRange } from 'src/app/model/dto/export-range';
import { ReportService } from '../../services/report.service';
import { formatDate } from '@angular/common';
import { Costanti, EPAY, STORAGE_KEYS } from 'src/app/common/costanti';
import { FiltroEnte } from 'src/app/common/filtro-ente';
import * as lodash from 'lodash';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalConfirmComponent } from '../modal/modal-confirm/modal-confirm.component';
import { Messaggi, MsgExport } from 'src/app/common/messaggi';
import { AlertService } from 'src/app/modules/alert';
import { roundToNearestMinutesWithOptions } from 'date-fns/fp';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-export-istanze',
  templateUrl: './export-istanze.component.html',
  styleUrls: ['./export-istanze.component.css']
})
export class ExportIstanzeComponent implements OnInit {

  SEPARATORE_ELENCO = ';';

  selectedModulo: number;
  active = 1;
  idComune: string;
  moduli: Modulo[];
  comuni: Comune[];
  idEnte: string;
  comuniUtente: Comune[];
  entiUtente: CodiceNome[];
  idProvincia: string;
  showFiltroComune: Boolean;
  showFiltroComuneUtente: Boolean;
  showFiltroEnteUtente: Boolean;

  exForm: FormGroup;
  disabled = false;
  limitSelection = false;

  stati: Array<Stato> = [];
  statiSelezionati: Array<Stato> = [];
  campi: Array<Campo> = [];
  campiSelezionati: Array<Campo> = [];
  campiIstanza: Array<Campo> = [];
  campiIstanzaSelezionati: Array<Campo> = [];

  isPagamenti: Boolean = false;
  filtroEpay = EPAY.ALL;

  dropdownStatoSettings = {
    singleSelection: false,
    idField: 'idStato',
    textField: 'nome',
    selectAllText: 'Seleziona tutti',
    unSelectAllText: 'Deseleziona tutti',
    itemsShowLimit: 3
  };
  dropdownIstanzaSettings = {
    singleSelection: false,
    idField: 'idIstanza',
    textField: 'codiceIstanza',
    selectAllText: 'Seleziona tutti',
    unSelectAllText: 'Deseleziona tutti',
    itemsShowLimit: 4
  };
  dropdownCampoSettings = {
    singleSelection: false,
    idField: 'key',
    textField: 'key',
    selectAllText: 'Seleziona tutti',
    unSelectAllText: 'Deseleziona tutti',
    itemsShowLimit: 3
  };
  dropdownCampoIstanzaSettings = {
    singleSelection: false,
    idField: 'key',
    textField: 'key',
    selectAllText: 'Seleziona tutti',
    unSelectAllText: 'Deseleziona tutti',
    itemsShowLimit: 3
  };

  ranges: Array<ExportRange> = [];
  showPagingExport: boolean = false;

  filterStati: number[];
  filterJson: string;

  msg: string;
  msgValidazione: string;

  codice_estrazione: string

  initFrm = true;
  dataDal: Date;
  dataAl: Date;
  inviateDataDal: Date;
  inviateDataAl: Date;

  showBaseFilter = false;
  showFieldsFilter = false;
  showColonnaComune: boolean;
  showColonnaEnteUtente: boolean;

  isFilterNotMandatory: boolean;

  idMultiEnte: number;
  showFiltroMultiEnte: boolean;

  showCustomExport: boolean = false;
  showReportByStreaming: boolean = false;

  showIntervals: boolean = false;
  today: Date;

  records: Object[];
  isModuloTari: boolean
  ElencoCodiceModuloTari: string[] = [
    "TARI_UD_VAR",
    "TARI_UD_CESS",
    "TARI_UD_ATT",
    "TARI_UND_CESS",
    "TARI_UND_VAR",
    "TARI_UND_ATT",
    "TARI_NOAUT",
    "TARI_BACKOFFICE",
    "TARI_UD_INFO",
    "TARI_UD_RECL",
    "TARI_UND_INFO",
    "TARI_UND_RECL"
  ];

  alertId = 'alert-export';
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
    private reportService: ReportService,
    private datePipe: DatePipe,
    private modalService: NgbModal,
    protected alertService: AlertService) { }

  ngOnInit() {

    this.alertService.clear(this.alertId);

    this.resetFilters();
    this.setMultiEnteParameters();
    this.records = environment.pagingSizeExport;

    //inizializzazione service prima della selezione 
    this.sharedService.pageSizeExport = Number(this.records[0]);

    this.exForm = this.fb.group({
      filtroEpay: [this.filtroEpay],
      modulo: [this.moduli],
      comune: [this.comuni],
      enteUtente: [this.entiUtente],
      comuneUtente: [this.comuniUtente],
      stato: [this.statiSelezionati],
      campo: [this.campiSelezionati],
      campoIstanza: [this.campiIstanzaSelezionati],
      dataDal: [''],
      dataAl: [''],
      inviateDataDal: [''],
      inviateDataAl: [''],
      provincia: [],
      range: [],
      records: [this.records],
      codice_estrazione: [this.codice_estrazione],
      custDataDal: [''],
      custDataAl: [''],
      
    });
    // this.custExportForm = this.fb.group({
    //   custDataDal: [''],
    //   custDataAl: [''],
    //   custCodice: ['']
    // })
    this.isFilterNotMandatory = this.sharedService.UserLogged.hasRuoloOperatorADV() || this.sharedService.UserLogged.isTipoADMIN();

    if (this.sharedService.mieiModuli.length > 0) {
      this.moduli = this.sharedService.mieiModuli.sort((ma, mb) =>
        ma.oggettoModulo.toLocaleLowerCase().trim().localeCompare(mb.oggettoModulo.toLocaleLowerCase().trim()));
    } else {
      this.getElencoModuli();
    }

    this.statiSelezionati = [];
    let moduloSelezionato = null;

    moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    if ((moduloSelezionato !== undefined) && (moduloSelezionato !== null)) {
      this.selectedModulo = Number(moduloSelezionato.idModulo);
      this.exForm.controls['modulo'].patchValue(this.selectedModulo, {
        onlySelf: true
      });

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(moduloSelezionato.idModulo);
      if (this.isPagamenti) {
        this.exForm.controls['filtroEpay'].patchValue(EPAY.ALL, {
          onlySelf: true
        });
      }

      if (moduloSelezionato.showFiltroComune) {
        this.showFiltroComune = true;
        if (moduloSelezionato.provinciaSelezionata) {
          this.idProvincia = moduloSelezionato.provinciaSelezionata;
          this.exForm.controls['provincia'].patchValue(this.idProvincia, {
            onlySelf: true
          });
          this.getElencoComuni(this.idProvincia);
          if (moduloSelezionato.comuneSelezionato) {
            this.idComune = moduloSelezionato.comuneSelezionato;
            this.exForm.controls['comune'].patchValue(this.idComune, {
              onlySelf: true
            });
          }
        }
      }
      if (moduloSelezionato.showFiltroComuneUtente) {
        this.getElencoComuniUtente();
        this.showFiltroComuneUtente = true;
        if (moduloSelezionato.comuneUtenteSelezionato) {
          this.idComune = moduloSelezionato.comuneUtenteSelezionato;
          this.exForm.controls['comuneUtente'].patchValue(this.idComune, {
            onlySelf: true
          });
        }
      }
      if (moduloSelezionato.showFiltroEnteUtente) {
        this.getElencoEntiUtente();
        this.showFiltroEnteUtente = true;
        if (moduloSelezionato.enteSelezionato) {
          this.idEnte = moduloSelezionato.enteSelezionato;
          this.exForm.controls['enteUtente'].patchValue(this.idEnte, {
            onlySelf: true
          });
        }
      }
      this.setCustomReportByModulo(moduloSelezionato.idModulo);
      // showBaseFilter solo abilitato nel caso esistano stati
      this.getStatiBoPerModulo(this.selectedModulo);
      this.getCampiPerModulo(this.selectedModulo);

      if (moduloSelezionato.showCustomExport) {
        this.showCustomExport = true;
      }
    } else {
      this.showBaseFilter = false;
    }

    this.campi = [
      { key: 'idIstanza', label: 'idIstanza', type: '', fullKey: '', gridKey: '', gridFullKey: '' },
      { key: 'codiceIstanza', label: 'codiceIstanza', type: '', fullKey: '', gridKey: '', gridFullKey: '' },
      { key: 'stato', label: 'stato', type: '', fullKey: '', gridKey: '', gridFullKey: '' },
      { key: 'dataInvio', label: 'dataInvio', type: '', fullKey: '', gridKey: '', gridFullKey: '' },
      { key: 'codiceFiscaleDichiarante', label: 'codiceFiscaleDichiarante', type: '', fullKey: '', gridKey: '', gridFullKey: '' }
    ];

    if (moduloSelezionato != null) {
      this.addProtocolloeDatainCampi(moduloSelezionato.idModulo);
    }

  }

  @HostListener('click', ['$event.target']) onClick(e) {

    if (e.innerHTML === 'Seleziona tutti') {
      if (e.closest('#stati') !== null) {
        if ((e.parentNode.childNodes[0].type === 'checkbox') && e.parentNode.childNodes[0].checked === false) {
          //('stati selezionati uncheck');
          this.statiSelezionati = [];
        }
      }
      if (e.closest('#campi') != null) {
        if ((e.parentNode.childNodes[0].type === 'checkbox') && e.parentNode.childNodes[0].checked === false) {
          //console.log('campi selezionati uncheck');
          this.campiSelezionati = [];
        }
      }
      if (e.closest('#campiIstanza') != null) {
        if ((e.parentNode.childNodes[0].type === 'checkbox') && e.parentNode.childNodes[0].checked === false) {
          //console.log('campi istanza selezionati uncheck');
          this.campiIstanzaSelezionati = [];
        }
      }
    }

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
    this.showFieldsFilter = false;
    this.showCustomExport = false;
    this.showReportByStreaming = false;
    this.showIntervals = false;

    // reset msg
    this.msgValidazione = '';
    this.msg = '';

    this.selectedModulo = e;
    const codiceModulo = this.moduli.find(i => i.idModulo === Number(this.selectedModulo)).codiceModulo
    const exists = this.ElencoCodiceModuloTari.includes(codiceModulo);
    if(exists === true){
      this.isModuloTari = true
    }else {
      this.isModuloTari = false
    }

    if (e) {

      this.exForm.get('modulo').setValue(e, {
        onlySelf: true
      });

      // check pagamenti
      this.isPagamenti = this.getPagamentiAttribute(e);
      if (this.isPagamenti) {
        this.addPagamenti();
        this.exForm.controls['filtroEpay'].patchValue(EPAY.ALL, {
          onlySelf: true
        });
      }

      this.spinnerService.show();
      this.getStatiBoPerModulo(this.selectedModulo);
      this.setFilters(this.selectedModulo);
      this.addProtocolloeDatainCampi(this.selectedModulo);
      
    } else {
      this.showBaseFilter = false;
    }

    console.log('custom export =' + this.showCustomExport);
  }

  addProtocolloeDatainCampi(selectedModulo: number) {
    let protocollo = new Campo('numeroProtocollo', 'numeroProtocollo', '', '', '', '');
    let dataProtocollo = new Campo('dataProtocollo', 'dataProtocollo', '', '', '', '');
    this.moonboblService.getParametriProtocollo(selectedModulo).then(
      result => {
        if (result == true) {
          if ((this.campi.find(x => x.key === 'numeroProtocollo')) == undefined) {
            this.campi.push(protocollo, dataProtocollo);
          }
        } else {
          if (this.campi.find(x => x.key === 'numeroProtocollo') != undefined) {
            this.campi = this.campi.filter(x => x.key !== 'numeroProtocollo' && x.key !== 'dataProtocollo');
          }
        }
      }
    );
  }

  addPagamenti() {

    let statoPagamento = new Campo('statoPagamento', 'statoPagamento', '', '', '', '');
    let codiceAvviso = new Campo('codiceAvviso', 'codiceAvviso', '', '', '', '');
    let iuv = new Campo('iuv', 'iuv', '', '', '', '');
    let dataEsitoPagamento = new Campo('dataEsitoPagamento', 'dataEsitoPagamento', '', '', '', '');
    let importo = new Campo('importo', 'importo', '', '', '', '');

    this.campi.push(statoPagamento);
    this.campi.push(codiceAvviso);
    this.campi.push(iuv);
    this.campi.push(dataEsitoPagamento);
    this.campi.push(importo);

  }

  setFilters(e: number) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + e));
    if (datiAtt && datiAtt.attributi && datiAtt.attributi.TIPO_FILTER_BO) {
      //console.log('TIPO filtro' + datiAtt.attributi.TIPO_FILTER_BO);
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
      if (tipoShowFilter === 'showCustomExport') {
        this.showCustomExport = true;
      }
      else {
        this.showCustomExport = false;
      }
    }
    if (e) {

      this.setCustomReportByModulo(e);

      localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify({
        idModulo: e,
        showFiltroComune: this.showFiltroComune,
        showFiltroComuneUtente: this.showFiltroComuneUtente,
        showColonnaComune: this.showColonnaComune,
        showFiltroEnteUtente: this.showFiltroEnteUtente,
        showColonnaEnteUtente: this.showColonnaEnteUtente,
        showCustomExport: this.showCustomExport,
        showReportByStreaming: this.showReportByStreaming,
        enteSelezionato: this.idEnte,
        comuneUtenteSelezionato: this.idComune,
        comuneSelezionato: this.idComune
      }));

      // fixme let mod = JSON.parse(localStorage.getItem('moduloSelezionato'));
      // fixme console.log('modulo selezionato in storage = ' + JSON.stringify(mod));
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

  updateShowCustomReportToStorage(value) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    if (moduloSelezionato) {
      moduloSelezionato.showReportByStreaming = value;
      localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify(moduloSelezionato));
    }
  }

  selectProvincia(e) {
    this.idComune = null;
    this.comuni = null;
    this.idProvincia = e;
    this.updateProvinciaToStorage(this.idProvincia);
    if (!e) {
      this.idComune = '';
    }
    else {
      this.getElencoComuni(e);
    }

  }

  private getElencoComuni(idProvincia): void {
    this.moonboblService.getElencoComuni(idProvincia).then(comuni => {
      this.comuni = comuni;
    });
  }

  private getElencoComuniUtente(): void {
    this.moonboblService.getElencoComuniUtente().then(
      comuniUtente => {
        this.comuniUtente = comuniUtente;
      }
    );
  }

  private getElencoEntiUtente(): void {
    this.moonboblService.getElencoEntiUtente().then(entiUtente => {
      this.entiUtente = entiUtente;
    });
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
    //console.log('onItemStatoSelect', item);
    this.statiSelezionati.push(item);
    //console.log('stati selezionati ' + JSON.stringify(this.statiSelezionati));
  }

  onItemCampoSelect(item: any) {
    //console.log('onItemCampoSelect', item);
    this.campiSelezionati.push(item);
    //console.log('campi selezionati ' + JSON.stringify(this.campiSelezionati));
  }

  onItemCampoIstanzaSelect(item: any) {
    //console.log('onItemCampoIstanzaSelect', item);
    this.campiIstanzaSelezionati.push(item);
    //console.log('campi istanza selezionati ' + JSON.stringify(this.campiIstanzaSelezionati));
  }

  onItemStatoDeSelect(item: any) {
    //console.log('onItemStatoDeSelect', item);
    this.statiSelezionati = this.statiSelezionati.filter((stato) => stato.idStato !== item.idStato);
    //console.log('on deselect stati selezionati ' + JSON.stringify(this.statiSelezionati));
  }

  onItemCampoDeSelect(item: any) {
    //console.log('onItemCampoDeSelect', item);
    this.campiSelezionati = this.campiSelezionati.filter((campo) => campo.key !== item.key);
    //console.log('on deselect campi selezionati ' + JSON.stringify(this.campiSelezionati));
  }

  onItemCampoIstanzaDeSelect(item: any) {
    //console.log('onItemCampoistanzaDeSelect', item);
    this.campiIstanzaSelezionati = this.campiIstanzaSelezionati.filter((campo) => campo.key !== item.key);
    //console.log('on deselect campi sitanza selezionate ' + JSON.stringify(this.campiIstanzaSelezionati));
  }

  onItemStatoSelectAll(items: any) {
    this.statiSelezionati = [...this.stati];
    //console.log('stati selezionati all' + JSON.stringify(this.statiSelezionati));
    //console.log('stati selezionati lenght ' + this.statiSelezionati.length);
  }

  onItemCampoSelectAll(items: any) {
    this.campiSelezionati = [...this.campi];
    //console.log('campi selezionati all' + JSON.stringify(this.campiSelezionati));
    //console.log('campi selezionati lenght ' + this.campiSelezionati.length);
  }
  onItemCampoIstanzaSelectAll(items: any) {
    this.campiIstanzaSelezionati = [...this.campiIstanza];
    //console.log('campi istanza selezionati all' + JSON.stringify(this.campiIstanzaSelezionati));
    //console.log('campi istanza selezionati selezionati lenght ' + this.campiIstanzaSelezionati.length);
  }

  handleLimitSelection() {
    if (this.limitSelection) {
      this.dropdownStatoSettings = Object.assign({}, this.dropdownStatoSettings, { limitSelection: 2 });
    } else {
      this.dropdownStatoSettings = Object.assign({}, this.dropdownStatoSettings, { limitSelection: null });
    }
    if (this.limitSelection) {
      this.dropdownIstanzaSettings = Object.assign({}, this.dropdownIstanzaSettings, { limitSelection: 2 });
    } else {
      this.dropdownIstanzaSettings = Object.assign({}, this.dropdownIstanzaSettings, { limitSelection: null });
    }
    if (this.limitSelection) {
      this.dropdownCampoSettings = Object.assign({}, this.dropdownCampoSettings, { limitSelection: 2 });
    } else {
      this.dropdownCampoSettings = Object.assign({}, this.dropdownCampoSettings, { limitSelection: null });
    }
  }


  getJsonIstanzePerModulo(idModulo: number, idComune: string, idEnte: string) {

    this.spinnerService.show();

    // let filtroRicerca = '';

    if (idModulo !== undefined) {

      if (!this.isFilterNotMandatory && ((this.showFiltroComune || this.showFiltroComuneUtente) && !this.idComune)) {
        // this.msgValidazione = 'è obbligatorio selezionare un comune';
        this.alertService.info(MsgExport.COMUNE_OBBLIGATORIO, this.alertOptionsNoAutoClose);
      }
      else if (!this.isFilterNotMandatory && (this.showFiltroEnteUtente && !this.idEnte)) {
        // this.msgValidazione = 'è obbligatorio selezionare un ente';
        this.alertService.info(MsgExport.ENTE_OBBLIGATORIO, this.alertOptionsNoAutoClose);
      }
      else {

        if ((this.showFiltroComune || this.showFiltroComuneUtente) && this.idComune) {
          //console.log('Valore comune selezionato: ' + this.idComune);
          // filtroRicerca = 'comune=' + idComune;
          this.filterJson = 'comune=' + idComune;

        }
        else if (this.showFiltroEnteUtente && this.idEnte) {
          //console.log('Valore ente selezionato: ' + this.idEnte);
          // filtroRicerca = 'asr=' + idEnte;
          if (this.idEnte.startsWith(FiltroEnte.ENTE_CCR)) {
            this.filterJson = FiltroEnte.ENTE_CCR + '=' + idEnte;
          }
          else {
            this.filterJson = 'asr=' + idEnte;
          }
        }

        // const stati: number[] = this.statiSelezionati.map(st => st.idStato);
        // set filter stati / filter json

        this.filterStati = this.statiSelezionati.map(st => st.idStato);

        this.moonboblService.getCountIstanzeJson(idModulo, this.filterStati, this.exForm.get('inviateDataDal').value, this.exForm.get('inviateDataAl').value, this.exForm.get('dataDal').value, this.exForm.get('dataAl').value, '', this.filtroEpay, this.filterJson)
          .subscribe((json) => {
            console.log('getCountIstanzeJson');
            console.log(json);
            if (json.count < this.sharedService.pageSizeExport) {
              console.log('numero di istanze inferiore al page size');
              if (Number(json.count) === 1) {
                // this.msgValidazione = 'È presente una sola istanza corrispondente al filtro di ricerca impostato';
                this.alertService.info(MsgExport.UNICA_ISTANZA, this.alertOptionsNoAutoClose);
              }
              else {
                // this.msgValidazione = 'Sono presenti ' + json.count + ' istanze corrispondenti al filtro di ricerca impostato';
                this.alertService.info(MsgExport.PRESENTI + ` ${json.count} ` + MsgExport.FILTRO_IMPOSTATO, this.alertOptionsNoAutoClose);
              }
              // reset intervals
              this.ranges = null;
              this.showIntervals = false;
              this.getIstanze(idModulo, this.filterStati, this.filterJson);
            } else {
              console.log('numero di istanze superiore al page size - necessita paginazione');
              this.spinnerService.hide();
              // this.msgValidazione = 'Sono presenti ' + json.count + ' istanze corrispondenti al filtro di ricerca impostato: restringere la ricerca o selezionare un intervallo';
              this.alertService.info(MsgExport.PRESENTI + ` ${json.count} ` + MsgExport.RESTRIZIONE_RICERCA + `: `, this.alertOptionsNoAutoClose);
              this.disabled = true;
              this.showPagingExport = true;
              this.buildPaging(json.count);
            }
          },
            error => {
              console.log(error);
              this.spinnerService.hide();
              if (error.status === 401) {
                // this.msgValidazione = 'Utente non abilitato';
                this.alertService.error(MsgExport.ERRORE_UTENTE_NON_ABILITATO, this.alertOptionsNoAutoClose)
              }
              this.handleException.handleNotBlockingError(error);
            });
      }
    } else {
      this.msg = 'Selezionare una tipologia di modulo !';
      this.spinnerService.hide();
    }
  }


  buildPaging(istanzeTotali) {

    // let limit = environment.pageSizeExport;
    let limit = this.sharedService.pageSizeExport;
    let pages = Math.ceil(istanzeTotali / limit);

    this.ranges = [];

    let rangePrev = new ExportRange();
    rangePrev.offset = 1;
    rangePrev.limit = limit;
    rangePrev.description = rangePrev.offset + ' - ' + rangePrev.limit;
    this.ranges.push(rangePrev);

    for (let i = 1; i < pages; i++) {
      let range = new ExportRange();
      range.offset = rangePrev.offset + limit;
      if (i < pages - 1) {
        range.limit = rangePrev.limit + limit;
        range.description = range.offset + ' - ' + range.limit;
      }
      else {
        range.limit = istanzeTotali;
        range.description = range.offset + ' - ' + istanzeTotali;
      }
      this.ranges.push(range);
      rangePrev = range;
    }
  }


  private flattenCsvByJson(json: any, csv: any[]) {
    for (const key in json) {
      if (json.hasOwnProperty(key)) {
        //console.log(key + ' -> ' + json[key]);
        csv.push(this.flattenObject(JSON.parse(json[key])));
      }
    }
  }

  private selectHeaderByFields(selectedFields: any[], header: any[]) {

    if (this.campiSelezionati.length > 0) {
      selectedFields = [...selectedFields, ...this.campiSelezionati.map(c => c.key)];
    } else {
      selectedFields = [...selectedFields, ...this.campi.map(c => c.key)];
    }
    if (this.campiIstanzaSelezionati.length > 0) {
      selectedFields = [...selectedFields, ...this.campiIstanzaSelezionati.map(c => c.key)];
    } else {
      selectedFields = [...selectedFields, ...this.campiIstanza.map(c => c.key)];
    }
    if (selectedFields.length > 0) {
      header = this.filterHeader(selectedFields, header);
    }
    return { selectedFields, header };
  }

  filterHeader(selectedFields: any[], header: any[]) {
    const filteredHeader = [];
    selectedFields.forEach(f => {
      header.forEach(h => {

        if (h !== undefined && f !== undefined) {
          if ((h.indexOf('.') > -1 && h.startsWith(f + '.')) || (f === h)) {
            filteredHeader.push(h);
          }
        }
      });
    });
    return filteredHeader;
  }

  getMergedHeader(csv: any[]) {
    let current;
    let res;
    let index;
    let prev = Object.keys(csv[0]);
    // tslint:disable-next-line: forin
    for (const key in csv) {
      index = Object.keys(csv).indexOf(key);
      if (index !== 0) {
        current = Object.keys(csv[key]);
        res = Array.from(new Set([...prev, ...current]));
        prev = res;
      }
    }
    const fields = prev.slice(0, 4);
    const instanceFields = prev.slice(4);
    const collator = new Intl.Collator(undefined, { numeric: true, sensitivity: 'base' });
    return fields.concat(instanceFields.sort(collator.compare));
  }

  getCampiPerModulo(idModulo: number) {
    this.campiIstanza = [];
    if (idModulo !== undefined) {
      this.moonboblService.getCampiPerModulo(idModulo)
        .then(campi => {
          this.campiIstanza = campi.filter(c => c.type !== 'button');
          // rimozione duplicati campi istanza
          this.campiIstanza = this.campiIstanza.reduce((acc, current) => {
            const found = acc.find(field => field.key === current.key);
            if (!found) {
              return acc.concat([current]);
            } else {
              return acc;
            }
          }, []);

          if (this.campiIstanza.length === 0) {
            this.msg = 'Nessuna campo trovato !';
          }
          this.spinnerService.hide();
          //console.log('spinner hide');
        }
        ).catch(errore => {
          this.spinnerService.hide();
          //console.log('***' + errore);
        });
    } else {
      this.msg = 'Selezionare una tipologia di modulo !';
      this.spinnerService.hide();
    }
  }

  getStatiBoPerModulo(idModulo: number) {
    this.stati = [];
    if (idModulo !== undefined) {
      this.moonboblService.getStatiBoPerModulo(idModulo)
        .then(stati => {
          // stati =  lodash.remove(stati, stato => stato.idStato !== Costanti.ISTANZA_STATO_BOZZA);
          this.stati = stati;
          if (this.stati.length === 0) {
            // this.msg = 'Nessuno stato disponibile';
            this.alertService.info(MsgExport.STATI_NON_DISPONIBILI, this.alertOptionsNoAutoClose);

          } else {
            this.showBaseFilter = true;
          }
          this.spinnerService.hide();
          //console.log('spinner hide');
        }
        ).catch(errore => {
          this.spinnerService.hide();
          //console.log('***' + errore);
        });
    } else {
      this.msg = 'Selezionare una tipologia di modulo !';
      this.spinnerService.hide();
    }
  }

  filtraCampi() {
    this.msgValidazione = null;
    //console.log('abilita filtro campi');
    if (this.checkIntervalloDateInvio() || this.checkIntervalloDateStato()) {
      // this.msgValidazione = 'Errore Data Al deve essere maggiore di Data Dal';
      // this.msgValidazione = 'Errore intervallo date';
      this.alertService.error(MsgExport.ERRORE_INTERVALLO_DATE, this.alertOptionsNoAutoClose)
    } else {
      this.showFieldsFilter = true;
      this.getCampiPerModulo(this.selectedModulo);
    }
  }

  checkIntervalloDateStato() {
    let isIntervalOkOrNotInterval: boolean = false;
    if ((this.exForm.get('dataDal').value !== null) && (this.exForm.get('dataDal').value !== '')
      && (this.exForm.get('dataAl').value !== null) && (this.exForm.get('dataAl').value !== '')) {
      if (!compareDate(this.exForm.get('dataDal').value, this.exForm.get('dataAl').value)) {
        // this.msgValidazione = 'Errore intervallo date';
        isIntervalOkOrNotInterval = true;
      }
    }
    return isIntervalOkOrNotInterval
  }

  checkIntervalloDateInvio() {
    let isIntervalOkOrNotInterval: boolean = false;
    if ((this.exForm.get('inviateDataDal').value !== null) && (this.exForm.get('inviateDataDal').value !== '')
      && (this.exForm.get('inviateDataAl').value !== null) && (this.exForm.get('inviateDataAl').value !== '')) {
      if (!compareDate(this.exForm.get('inviateDataDal').value, this.exForm.get('inviateDataAl').value)) {
        // this.msgValidazione = 'Errore intervallo date';
        isIntervalOkOrNotInterval = true;
      }
    }
    return isIntervalOkOrNotInterval
  }


  cerca() {
    this.alertService.clear(this.alertId);
    this.msgValidazione = null;
    //console.log('seleziona istanze ');
    if (!this.isFilterNotMandatory && ((this.showFiltroComune || this.showFiltroComuneUtente) && !this.idComune)) {
      // this.msgValidazione = 'è obbligatorio selezionare un comune';
      this.alertService.info(MsgExport.COMUNE_OBBLIGATORIO, this.alertOptionsNoAutoClose)
      this.spinnerService.hide();
    }
    else if (!this.isFilterNotMandatory && (this.showFiltroEnteUtente && !this.idEnte)) {
      // this.msgValidazione = 'è obbligatorio selezionare un ente';
      this.alertService.info(MsgExport.ENTE_OBBLIGATORIO, this.alertOptionsNoAutoClose)
      this.spinnerService.hide();
    }
    if (this.checkIntervalloDateInvio() || this.checkIntervalloDateStato()) {
      //this.msgValidazione = 'Errore Data Al deve essere maggiore di Data Dal';
      // this.msgValidazione = 'Errore intervallo date';
      this.alertService.error(MsgExport.ERRORE_INTERVALLO_DATE, this.alertOptionsNoAutoClose)
    } else {
      this.getJsonIstanzePerModulo(this.selectedModulo, this.idComune, this.idEnte);
    }
  }

  reset() {

    this.alertService.clear(this.alertId);

    //console.log('reset');
    this.disabled = false;

    this.exForm.reset();
    this.showFieldsFilter = false;
    this.showBaseFilter = false;
    this.campiSelezionati = [];
    this.campiIstanzaSelezionati = [];
    this.statiSelezionati = [];

    this.resetFilters();
    this.ranges = null;

    this.showFiltroComune = false;
    this.showFiltroComuneUtente = false;
    this.showFiltroEnteUtente = false;
    this.showColonnaComune = false;
    this.showColonnaEnteUtente = false;
    this.showPagingExport = false;
    this.idComune = null;
    this.idEnte = null;
    this.idProvincia = null;

    this.isPagamenti = false;

    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, null);
    localStorage.setItem(STORAGE_KEYS.MODATTR, null);

  }

  flattenObject = (obj) => {
    const flattenKeys = {};
    for (const i in obj) {
      if (!obj.hasOwnProperty(i)) { continue; }
      if ((typeof obj[i]) == 'object') {

        const flatObject = this.flattenObject(obj[i]);
        for (const j in flatObject) {
          if (!flatObject.hasOwnProperty(j)) { continue; }
          flattenKeys[i + '.' + j] = flatObject[j];

          // console.log('flattened keys ' + i + '.' + j);
          // console.log('flattened object ' + flatObject[j]);
        }
      } else {
        flattenKeys[i] = obj[i];
        //console.log('simple prop i ' + obj[i]);
      }
    }
    return flattenKeys;
  }


  downloadCsvFile(data, header, filename = 'data') {
    const csvData = this.ConvertToCSV(data, header);
    //console.log(csvData);
    const blob = new Blob(['\ufeff' + csvData], { type: 'text/csv;charset=utf-8;' });
    const dwldLink = document.createElement('a');
    const url = URL.createObjectURL(blob);
    const isSafariBrowser = navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1;
    if (isSafariBrowser) {  // if Safari open in new window to save file with random filename.
      dwldLink.setAttribute('target', '_blank');
    }
    dwldLink.setAttribute('href', url);
    dwldLink.setAttribute('download', filename + '.csv');
    dwldLink.style.visibility = 'hidden';
    document.body.appendChild(dwldLink);
    dwldLink.click();
    document.body.removeChild(dwldLink);
  }

  ConvertToCSV(objArray, headerList) {
    const array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
    let str = '';
    let row = '';

    // tslint:disable-next-line: forin
    for (const index in headerList) {
      row += headerList[index] + ';';
    }
    row = row.slice(0, -1);
    str += row + '\r\n';
    for (let i = 0; i < array.length; i++) {

      // let line = (i+1)+'';
      let line = '';
      let cellContent = '';
      // tslint:disable-next-line: forin
      for (const index in headerList) {
        const head = headerList[index];

        cellContent = ((array[i][head] === undefined) || (array[i][head] === null) || (array[i][head] === '')) ? '-' : array[i][head];
        cellContent = this.replaceCell(cellContent);

        // clean base64
        cellContent = cellContent.toString().indexOf(';base64') > -1 ? 'base64' : cellContent;

        // put separatore elenco
        cellContent = cellContent.toString().replace(/;/g, "");

        line += cellContent + this.SEPARATORE_ELENCO;

      }
      str += line + '\r\n';
    }
    return str;
  }

  replaceCell(cellContent) {
    return cellContent.toString()
      .replace(/[\b]/g, '')
      .replace(/[\f]/g, '')
      .replace(/[\n]/g, '')
      .replace(/[\r]/g, '')
      .replace(/[\t]/g, '')
  }

  pageChanged($event) {
    this.setMultiEnteParameters();
  }

  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.idMultiEnte = this.sharedService.UserLogged.ente.idEnte;
      this.showFiltroMultiEnte = true;
    }
  }


  getIstanzePaginate(idModulo, stati, filtroRicerca, offset, limit) {

    this.moonboblService.getIstanzePaginateJson(idModulo, stati, this.exForm.get('inviateDataDal').value, this.exForm.get('inviateDataAl').value, this.exForm.get('dataDal').value, this.exForm.get('dataAl').value, 'i.id_istanza', this.filtroEpay, filtroRicerca, offset - 1, limit)
      .subscribe((json) => {
        this.jsonToCsv(json, offset, offset + limit - 1);
        //this.jsonToCsv(json, offset, offset + limit - 1);
      },
        error => {
          //console.log(error);
          this.spinnerService.hide();
          if (error.status === 401) {
            // this.msgValidazione = 'Utente non abilitato';
            this.alertService.error(MsgExport.ERRORE_UTENTE_NON_ABILITATO, this.alertOptionsNoAutoClose)
          }
          this.handleException.handleNotBlockingError(error);
        });

  }

  getIstanze(idModulo, stati, filtroRicerca) {
    this.moonboblService.getIstanzeJson(idModulo, stati, this.exForm.get('inviateDataDal').value, this.exForm.get('inviateDataAl').value, this.exForm.get('dataDal').value, this.exForm.get('dataAl').value, 'i.id_istanza', this.filtroEpay, filtroRicerca)
      .subscribe((json) => {
        this.jsonToCsv(json);
      },
        error => {
          //console.log(error);
          this.spinnerService.hide();
          if (error.status === 401) {
            // this.msgValidazione = 'Utente non abilitato';
            this.alertService.error(MsgExport.ERRORE_UTENTE_NON_ABILITATO, this.alertOptionsNoAutoClose)
          }
          this.handleException.handleNotBlockingError(error);
        });

  }

  cercaPerRange() {

    this.alertService.clear(this.alertId);

    let limit = this.exForm.get('range').value ? this.exForm.get('range').value.limit : null;
    let offset = this.exForm.get('range').value ? this.exForm.get('range').value.offset : null;

    if (offset && limit) {
      this.spinnerService.show();
      this.getIstanzePaginate(this.selectedModulo, this.filterStati, this.filterJson, offset, this.sharedService.pageSizeExport);
    }
    else {
      // this.msgValidazione = 'Selezionare un intervallo';
      this.alertService.info(MsgExport.INTERVALLO_DATE, this.alertOptionsNoAutoClose);
    }
  }

  jsonToCsv(json, offset = null, limit = null) {

    this.alertService.clear(this.alertId);

    const csv = [];
    let header = [];
    let selectedFields = [];
    // console.log('json ' + json);

    if (json.length > 0) {

      this.flattenCsvByJson(json, csv);
      header = this.getMergedHeader(csv);
      ({ selectedFields, header } = this.selectHeaderByFields(selectedFields, header));

      if (offset && limit) {
        this.downloadCsvFile(csv, header, 'report-istanze_' + offset + '-' + limit + '-' + this.datePipe.transform(new Date(), 'yyyyMMdd-HHmmss'));
      }
      else {
        this.downloadCsvFile(csv, header, 'report-istanze_' + this.datePipe.transform(new Date(), 'yyyyMMdd-HHmmss'));
      }

      this.spinnerService.hide();
      //console.log('spinner hide');

    } else {
      //console.log('Non è presente alcuna istanza corrispendente al filtro impostato');
      // this.msgValidazione = 'Non è presente alcuna istanza corrispendente al filtro impostato';
      this.alertService.info(MsgExport.ISTANZE_NON_PRESENTI, this.alertOptionsNoAutoClose);
      this.spinnerService.hide();
    }
  }

  sbloccaFiltro() {

    this.disabled = false;
    this.showPagingExport = false;
    this.showIntervals = false;
    this.ranges = null;

    this.exForm.get('range').setValue(null);
    this.exForm.get('records').setValue(null);

    this.msgValidazione = null;

  }

  resetFilters() {
    this.filterStati = null;
    this.filterJson = null;
  }

  onChange(eventChanged: any) {
    // this.alertService.clear(this.alertId);
    console.log(' on change = ' + eventChanged);
  }

  downloadExcel() {

    this.spinnerService.show();
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    // let start = new Date().getTime();

    this.reportService.getReport(moduloSelezionato.idModulo)
      .subscribe(x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        var newBlob = new Blob([x], { type: 'application/vnd.ms-excel' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /* if (window.navigator && window.navigator.msSaveOrOpenBlob) {
          window.navigator.msSaveOrOpenBlob(newBlob);
          return;
        }*/
        saveBlobIE(newBlob);
        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        var link = document.createElement('a');
        link.href = data;
        this.today = new Date();
        const currentTime = formatDate(this.today, 'yyyyMMdd_hhmmss', 'en');
        link.download = 'report_' + currentTime + '.csv';

        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();

        }, 100);

        this.spinnerService.hide();

      });
  }

  downloadCsvByStreaming() {

    this.spinnerService.show();
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    // let start = new Date().getTime();

    this.reportService.getReportByStreaming(moduloSelezionato.idModulo, { idEnte: this.idEnte })
      .subscribe(x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        var newBlob = new Blob([x], { type: 'application/vnd.ms-excel' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator) {
          const nav = (window.navigator as any);
          if (nav.msSaveOrOpenBlob) {
            nav.msSaveOrOpenBlob(newBlob);
            return;
          }
        }


        if (window.navigator) {
          const nav = (window.navigator as any);
          if (nav.msSaveOrOpenBlob) {
            nav.msSaveOrOpenBlob(newBlob);
            return;
          }
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        var link = document.createElement('a');
        link.href = data;
        this.today = new Date();
        const currentTime = formatDate(this.today, 'yyyyMMdd_hhmmss', 'en');
        link.download = 'report_' + currentTime + '.csv';

        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));

        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();

          // let end = new Date().getTime();
          // let time = end - start;
          // console.log('downloadExcelByStreaming: execution time: ' + time);

        }, 100);

        this.spinnerService.hide();

      });
  }


  selectNumeroRecordsPerPagina(elem) {
    if (elem) {
      this.sharedService.pageSizeExport = Number(elem);
      this.showIntervals = true;
      this.cerca();
    }
    else {
      this.ranges = null;
      this.showIntervals = false;
    }

  }

  setCustomReportByModulo(e) {
    const modulo: any = this.moduli.filter(m => Number(e) === m.idModulo);
    if (modulo && modulo[0] && modulo[0].codiceModulo === 'RPCCSR') {
      this.showReportByStreaming = true;
    }
    else {
      this.showReportByStreaming = false;
    }
    this.updateShowCustomReportToStorage(this.showReportByStreaming);
  }


  customizedDownload() {
    this.checkInformativa();
  }

  checkInformativa() {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    const modulo: any = this.moduli.filter(m => Number(moduloSelezionato.idModulo) === m.idModulo);
    if (modulo && modulo[0] && modulo[0].codiceModulo === 'RPCCSR') {
      const mdRef = this.modalService.open(ModalConfirmComponent);
      mdRef.componentInstance.modal_titolo = 'Informativa';
      mdRef.componentInstance.modal_contenuto = Messaggi.messaggioInformativaRPCCSR;
      mdRef.result.then((result) => {
        console.log('Closed with: ${result}' + result);
        this.downloadCsvByStreaming();
      }, (reason) => {
        console.log(reason);
      });
    } else {
      this.downloadCsvByStreaming();
    }
  }

  getPagamentiAttribute(idModulo: any) {
    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + idModulo));
    if (!datiAtt) {
      console.log('export-istanze::getPagamentiAttribute ERROR NOT FOUND datiAtt: modattr' + idModulo);
      return false;
    }
    console.log('export-istanze::getPagamentiAttribute datiAtt = ' + datiAtt);
    console.log('export-istanze::getPagamentiAttribute datiAtt.attributi = ' + JSON.stringify(datiAtt.attributi));
    if (datiAtt.attributi?.PSIT_EPAY && datiAtt.attributi.PSIT_EPAY === 'S') {
      return true;
    } else {
      return false;
    }
  }

  changePaid(e) {
    console.log(e.target.value);
    this.filtroEpay = e.target.value;

    this.showPagingExport = false;

    //this.cerca();
  }

  setCustomExportCSV(){
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    const idModulo = Number(moduloSelezionato.idModulo)
    const codiceModulo = this.moduli.find(i => i.idModulo === idModulo).codiceModulo
    const codiceEstrazione = this.exForm.get('codice_estrazione').value
    if(this.exForm.get('codice_estrazione').value !== null){
      if (this.checkIntervalloDataEsporta()) {
        this.alertService.error(MsgExport.ERRORE_INTERVALLO_DATE, this.alertOptionsNoAutoClose)
      } else {
        this.moonboblService.getCustomExportCsv(idModulo, codiceEstrazione, this.exForm.get('custDataDal').value, this.exForm.get('custDataAl').value)
        .subscribe((response: any) => {
  
      const blob = new Blob([response.body], { type: 'text/csv' });
  
      const downloadLink = document.createElement('a');
      downloadLink.href = URL.createObjectURL(blob);
      downloadLink.download = codiceModulo + '_' + codiceEstrazione;
  
      downloadLink.click();
  
      URL.revokeObjectURL(downloadLink.href);
       });  
      }
      
    }else {
      this.alertService.error(MsgExport.CODICE_ESTRAZIONE_OBLIGATORIO, this.alertOptionsNoAutoClose)
    }    
  }
  checkIntervalloDataEsporta() {
    let isIntervalOkOrNotInterval: boolean = false;
    if ((this.exForm.get('custDataDal').value !== null) && (this.exForm.get('custDataDal').value !== '')
      && (this.exForm.get('custDataAl').value !== null) && (this.exForm.get('custDataAl').value !== '')) {
      if (!compareDate(this.exForm.get('custDataDal').value, this.exForm.get('custDataAl').value)) {
        // this.msgValidazione = 'Errore intervallo date';
        isIntervalOkOrNotInterval = true;
      }
    }
    return isIntervalOkOrNotInterval
  }
}
