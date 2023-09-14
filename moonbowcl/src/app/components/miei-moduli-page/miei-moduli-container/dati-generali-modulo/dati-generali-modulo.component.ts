/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Modulo } from '../../../../model/dto/modulo';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MoonboblService } from '../../../../services/moonbobl.service';
import { Categoria } from '../../../../model/dto/categoria';
import { faSave, faHome } from '@fortawesome/free-solid-svg-icons';
import { PortaliIf } from '../../../../model/dto/portali-if';
import { IDropdownSettings } from 'ng-multiselect-dropdown';
import { NgxSpinnerService } from 'ngx-spinner';
import { ErrorNotificationService } from '../../../../services/error-notification.service';
import { MoonboError } from '../../../../model/common/moonbo-error';
import { Processo } from '../../../../model/dto/processo';
import { SharedService } from 'src/app/services/shared.service';
import { Messaggi, MsgModulo } from 'src/app/common/messaggi';

@Component({
  selector: 'app-dati-generali-modulo',
  templateUrl: './dati-generali-modulo.component.html',
  styleUrls: ['./dati-generali-modulo.component.css']
})
export class DatiGeneraliModuloComponent implements OnInit {

  @Input() moduloInLavorazione: Modulo;
  @Input() elencoCategorie: Categoria[];
  @Input() elencoProcessi: Processo[];
  @Output() backEvent: EventEmitter<string> = new EventEmitter<string>();
  @Output('alertService') alert = new EventEmitter();

  frmDatiGenerali: FormGroup;
  frmDatiGeneraliChanged = false;
  frmPortaliChanged = false;
  isAdmin = false;

  frmPortali: FormGroup;
  faSave = faSave;
  faHome = faHome;
  elencoPortali: PortaliIf[];
  formTypes = [
    { tipo: 'FRM', descrizione: 'FORM' },
    { tipo: 'WIZ', descrizione: 'WIZARD' }];

  struttura = "";

  // indica se ci sono errori
  modeError = false;
  dropdownSettings: IDropdownSettings = {
    singleSelection: false,
    idField: 'idPortale',
    textField: 'nomePortale',
    selectAllText: 'Seleziona Tutti',
    unSelectAllText: 'Deseleziona Tutti',
    enableCheckAll: true,
    noDataAvailablePlaceholderText: 'Dati non disponibili',
    allowSearchFilter: true
  };


  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService) {
    this.frmDatiGenerali = fb.group({
      categoria: [],
      oggetto: [Validators.required],
      codiceModulo: "",
      versioneModulo: "",
      descrizione: [],
      tipoStruttura: [Validators.required],
      processo: ['']
    });

    this.frmPortali = fb.group({
      portaliModulo: []
    });

    // processo e portali riservati a profilo ADMIN
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();

    if (!this.isAdmin) {
      this.frmDatiGenerali.controls['processo'].disable();
      this.frmPortali.controls['portaliModulo'].disable();
    }
    else {
      this.frmDatiGenerali.controls['processo'].enable();
      this.frmPortali.controls['portaliModulo'].enable();
    };

  }

  ngOnInit(): void {
    // recupero elenco portali
    this.spinnerService.show();
    this.moonboblService.getPortali().subscribe(
      (portali) => {
        this.elencoPortali = portali;
        this.spinnerService.hide();
      },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        //this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
        this.modeError = true;
      });

    // Inizializzo i valori delle form  con chiamata servizio
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloInLavorazione.idModulo,
      this.moduloInLavorazione.idVersioneModulo,
      'struttura,portali,processo').subscribe((modulo: Modulo) => {
        this.frmDatiGenerali.controls.categoria.setValue(modulo.categoria.idCategoria);
        this.frmDatiGenerali.controls.oggetto.setValue(modulo.oggettoModulo);
        this.frmDatiGenerali.controls.descrizione.setValue(modulo.descrizioneModulo);
        this.frmDatiGenerali.controls.tipoStruttura.setValue(modulo.tipoStruttura);

        // impostazione struttura
        this.struttura = modulo.struttura;

        // imposto il processo
        this.moduloInLavorazione.processo = modulo.processo;
        this.frmDatiGenerali.controls.processo.setValue(modulo.processo?.idProcesso);
        this.frmDatiGenerali.valueChanges.subscribe(x =>
          this.frmDatiGeneraliChanged = true
        );
        // imposto i portali
        const portali: PortaliIf[] = [];
        modulo.portali.forEach((p) => portali.push(p));
        this.frmPortali.controls.portaliModulo.setValue(portali);
        this.frmPortali.valueChanges.subscribe(x =>
          this.frmPortaliChanged = true
        );
        this.spinnerService.hide();
      },
        (err: MoonboError) => {
          // informazioni sulla chiamata
          //this.errNotificationError.notification.next(err);
          // alert(err.errorMsg);
          this.alert.emit({ text: err.errorMsg, type: 'error', autoclose:false});
          this.modeError = true;
          this.spinnerService.hide();
        });
  }

  salva() {
    console.log(this.frmPortali.controls.portaliModulo.value);
    if (this.frmDatiGeneraliChanged) {
      this.salvaDatiGenerali();
    }
    if (this.frmPortaliChanged) {
      console.log('Portali changed: ' + this.frmPortaliChanged);
      console.log('Portali selezionati ' + JSON.stringify(this.frmPortali.controls.portaliModulo.value));
      this.salvaPortali();
    }
  }

  back() {

    this.spinnerService.show();
    this.backEvent.emit('back');
  }

  onItemSelect($event: any) {
    console.log(JSON.stringify($event));
  }

  onSelectAll($event: Array<any>) {

  }

  // Funzioni provate per salvataggio
  private salvaDatiGenerali() {
    // aggiornaModulo
    this.moduloInLavorazione.categoria.idCategoria = this.frmDatiGenerali.controls.categoria.value;
    this.moduloInLavorazione.oggettoModulo = this.frmDatiGenerali.controls.oggetto.value;
    this.moduloInLavorazione.descrizioneModulo = this.frmDatiGenerali.controls.descrizione.value;
    this.moduloInLavorazione.tipoStruttura = this.frmDatiGenerali.controls.tipoStruttura.value;
    // fixme capire se testare oggetto null o ritorna valore default
    // this.moduloInLavorazione.processo.idProcesso = this.frmDatiGenerali.controls.processo.value;

    // set struttura
    this.moduloInLavorazione.struttura = this.getStrutturaAggiornata();

    let processo = new Processo();
    processo.idProcesso = this.frmDatiGenerali.controls.processo.value;
    this.moduloInLavorazione.processo = processo;

    this.spinnerService.show();
    this.moonboblService.aggiornaModulo(
      this.moduloInLavorazione
    ).subscribe(() => {
      this.spinnerService.hide();
      // TODO: Notifica salvataggio effettuato
      // alert('Salvataggio effettuato');
      this.alert.emit({ text: Messaggi.OK, type: 'success', autoclose: true });

    },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        //this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false });
        this.modeError = true;
      }
    );
  }

  private salvaPortali() {
    const portali: number[] = [];
    this.frmPortali.controls.portaliModulo.value.forEach((el => portali.push(el['idPortale'])));
    this.spinnerService.show();
    this.moonboblService.salvaPortali(
      this.moduloInLavorazione, portali
    ).subscribe(() => {
      this.spinnerService.hide();
      // TODO: Notifica salvataggio effettuato
      // alert('Salvataggio effettuato');
      this.alert.emit({ text: Messaggi.OK, type: 'success', autoclose: true });
    },
      (err: MoonboError) => {
        // informazioni sulla chiamata
        this.spinnerService.hide();
        // this.errNotificationError.notification.next(err);
        // alert(err.errorMsg);
        this.alert.emit({ text: err.errorMsg, type: 'error', autoclose: false });
        this.modeError = true;
      }
    );
  }

  get moduloStrutturaDisplay(): string {
    return this.frmDatiGenerali.controls.tipoStruttura.value === 'WIZ' ? 'wizard' : 'form';
  }

  getStrutturaAggiornata() {
    const display = this.moduloStrutturaDisplay;
    const jsonObj = JSON.parse(this.struttura);
    jsonObj.display = display;
    return JSON.stringify(jsonObj);
  }



}
