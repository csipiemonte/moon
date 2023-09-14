/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { Modulo } from 'src/app/model/dto/modulo';
import { ErrorNotificationService } from 'src/app/services/error-notification.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { faClone, faEdit, faHome, faInfo, faLaptopHouse, faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { AttrCosmoConf } from 'src/app/model/dto/attr/attrCosmoConf';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MsgAttrCosmo } from 'src/app/common/messaggi';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-wf-cosmo',
  templateUrl: './attr-wf-cosmo.component.html',
  styleUrls: ['./attr-wf-cosmo.component.scss']
})
export class AttrWfCosmoComponent implements OnInit {

  @Input() moduloSelezionato: Modulo;
  @Output('alertService') alert = new EventEmitter();

  modulo: Modulo = new Modulo();
  elencoModuloAttributi: ModuloAttributo[];
  maCosmo: ModuloAttributo = new ModuloAttributo();
  maCosmoEdit: ModuloAttributo = new ModuloAttributo();
  maCosmoConf: ModuloAttributo = new ModuloAttributo();

  attrCosmoConf: object = new Object();
  attrCosmoConfEdit: AttrCosmoConf = new AttrCosmoConf();

  titleUpd: Map<string, string> = new Map<string, string>();
  mapPresent: Map<string, boolean> = new Map<string, boolean>();

  isAdmin = false;
  isModificabile = false;
  isOwner = true;
  editMode = false;
  faHome = faHome;
  faClone = faClone;
  faEdit = faEdit;
  faTrashAlt = faTrashAlt;
  modeError = false;

  maCosmoEditT = false;

  alertOptions = {
    id: 'alert-moduli-attr-wf-cosmo',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(fb: FormBuilder,
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    private errNotificationError: ErrorNotificationService,
    protected alertService: AlertService) {
      this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    this.alert.emit({clear: true});
    this.init();
  }

  init(): void {
    this.spinnerService.show();
    this.moonboblService.getModuloWithFields(this.moduloSelezionato.idModulo,
                                            this.moduloSelezionato.idVersioneModulo,
                                            'attributiCosmo').subscribe((modulo: Modulo) => {
          // Init degli attributi per viewMode
          this.modulo = modulo;
          this.elencoModuloAttributi = modulo.attributi;
          this.initFields();

          // tslint:disable-next-line:max-line-length
          if (this.isAdmin || ((this.modulo.stato.codice === 'INIT' || this.modulo.stato.codice === 'TST' || this.modulo.stato.codice === 'MOD') && this.isOwner)) {
            this.isModificabile = true;
          }

          this.spinnerService.hide();
      },
        (err: MoonboError) => {
          // informazioni sulla chiamata
          // this.errNotificationError.notification.next(err);
          alert(err.errorMsg);
          this.modeError = true;
          this.spinnerService.hide();
        }
      );
  }

  initFields() {
    log('initFields() this.elencoModuloAttributi: ' + this.elencoModuloAttributi);
    //
    this.maCosmo = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PSIT_COSMO', 'N');
    this.maCosmoEdit = { ...this.maCosmo };
    this.maCosmoEditT = this.maCosmoEdit ? (this.maCosmoEdit['valore'] === 'N' ? false: true) : false;
    log('initFields() this.maCosmo: ' + JSON.stringify(this.maCosmo));
    log('initFields() this.maCosmoEdit: ' + JSON.stringify(this.maCosmoEdit));
    //
    if (this.elencoModuloAttributi && this.elencoModuloAttributi.length > 0) {
      //
      this.maCosmoConf = this.elencoModuloAttributi.find(ma => ma.nome === 'PSIT_COSMO_CONF');
      log('initFields() this.maCosmoConf: ' + JSON.stringify(this.maCosmoConf));
      if (this.maCosmoConf) {
        this.attrCosmoConf = JSON.parse(this.maCosmoConf.valore);
        this.attrCosmoConfEdit = JSON.parse(this.maCosmoConf.valore); // Object.assign({}, this.maCosmoConf);
        this.titleUpd.set(this.maCosmoConf.nome, 'Ultima modifica il ' + this.maCosmoConf.dataUpd + ' da ' + this.maCosmoConf.attoreUpd);
      }
      log('initFields() this.attrCosmoConf: ' + JSON.stringify(this.attrCosmoConf));
      log('initFields() this.attrCosmoConfEdit: ' + JSON.stringify(this.attrCosmoConfEdit));
    }
    if (!this.maCosmoConf) {
      this.maCosmoConf = creaModuloAttributo('PSIT_COSMO_CONF', '{}');
    }
  }


  findOrCreateWithDefaultValue(moduloAttributi: ModuloAttributo[], nome: string, defaultValore: string): ModuloAttributo {
    let result: ModuloAttributo;
    if (moduloAttributi && moduloAttributi.length > 0) {
      result = moduloAttributi.find(ma => ma.nome === nome);
    }
    if (!result) {
      this.mapPresent.set(nome, false);
      result = creaModuloAttributo(nome, defaultValore);
    } else {
      this.mapPresent.set(nome, true);
      this.titleUpd.set(nome, 'Ultima modifica il ' + result.dataUpd + ' da ' + result.attoreUpd);
    }
    return result;
  }

  salva() {
    this.spinnerService.show();
    // tslint:disable-next-line:prefer-const
    let maToSave = [];
    if (this.maCosmoEdit)  this.maCosmoEdit['valore'] = this.maCosmoEditT ? 'S':'N'; 
    verificaToSave(maToSave, this.maCosmo, this.maCosmoEdit);
    addToSaveAttrConfJsonStr(maToSave, 'PSIT_COSMO_CONF', this.attrCosmoConf, this.CosmoConfJsonStr);
    log('salva::aggiornaModuloAttributiCosmo maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0 ) {
      this.moonboblService.aggiornaModuloAttributiCosmo(this.modulo, maToSave).subscribe(
          (attributi: ModuloAttributo[]) => {
            this.elencoModuloAttributi = attributi;
            this.init();
            this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi WF COSMO effettuato con successo !', this.alertOptions);

          this.alert.emit({ text: MsgAttrCosmo.SUCCESS_ATTR_COSMO, type: 'success', autoclose:true});
          },
          (err: MoonboError) => {
            this.spinnerService.hide();
            // this.alertService.error('Impossibile effettuare il salvataggio attributi WF COSMO !' + err.errorMsg, this.alertOptions);
            this.alert.emit({ text: MsgAttrCosmo.ERROR_ATTR_COSMO, type: 'error', autoclose:false});
          }
        );
    } else {
      this.spinnerService.hide();
      // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
      this.alert.emit({ text: MsgAttrCosmo.INFO_NO_MODIFY_ATTR_COSMO, type: 'info', autoclose:false});
    }
  }

  get CosmoConfJsonStr(): string {
    // Non permettiamo il salvataggio di un attributo di tipo strong vuoto nel JSON, lo togliamo completamento
    // tslint:disable-next-line:prefer-const
    let locAttrCosmoConf: AttrCosmoConf = new AttrCosmoConf();
    // tslint:disable:max-line-length
    if (((this.attrCosmoConfEdit.idPratica || '').trim().length > 0)) { locAttrCosmoConf.idPratica = this.attrCosmoConfEdit.idPratica.trim(); }
    if (((this.attrCosmoConfEdit.codiceTipologia || '').trim().length > 0)) { locAttrCosmoConf.codiceTipologia = this.attrCosmoConfEdit.codiceTipologia.trim(); }
    if (((this.attrCosmoConfEdit.oggetto || '').trim().length > 0)) { locAttrCosmoConf.oggetto = this.attrCosmoConfEdit.oggetto.trim(); }
    if (((this.attrCosmoConfEdit.codiceIpaEnte || '').trim().length > 0)) { locAttrCosmoConf.codiceIpaEnte = this.attrCosmoConfEdit.codiceIpaEnte.trim(); }
    if (((this.attrCosmoConfEdit.riassunto || '').trim().length > 0)) { locAttrCosmoConf.riassunto = this.attrCosmoConfEdit.riassunto.trim(); }
    if (((this.attrCosmoConfEdit.utenteCreazione || '').trim().length > 0)) { locAttrCosmoConf.utenteCreazione = this.attrCosmoConfEdit.utenteCreazione.trim(); }
    if (((this.attrCosmoConfEdit.codiceTipoDocIstanza || '').trim().length > 0)) { locAttrCosmoConf.codiceTipoDocIstanza = this.attrCosmoConfEdit.codiceTipoDocIstanza.trim(); }
    if (((this.attrCosmoConfEdit.codiceTipoDocAllegato || '').trim().length > 0)) { locAttrCosmoConf.codiceTipoDocAllegato = this.attrCosmoConfEdit.codiceTipoDocAllegato.trim(); }
    if (((this.attrCosmoConfEdit.codiceTipoDocIntegrazione || '').trim().length > 0)) { locAttrCosmoConf.codiceTipoDocIntegrazione = this.attrCosmoConfEdit.codiceTipoDocIntegrazione.trim(); }
    if (((this.attrCosmoConfEdit.codiceTipoDocIntegrazioneAllegato || '').trim().length > 0)) { locAttrCosmoConf.codiceTipoDocIntegrazioneAllegato = this.attrCosmoConfEdit.codiceTipoDocIntegrazioneAllegato.trim(); }
    // tslint:enable:max-line-length
    return JSON.stringify(locAttrCosmoConf);
  }

}

function verificaToSave(result: ModuloAttributo[], ma: ModuloAttributo, maEdit: ModuloAttributo): ModuloAttributo[] {
  if (ma?.valore === maEdit['valore']) {
    log('Unchanged ' + ma['nome'] + ': ' + ma['valore']);
  } else {
    if (maEdit.nome && (maEdit.valore || (!maEdit.valore && ma.valore))) {
      log('Changed ' + ma['nome'] + ' FROM: ' + ma['valore'] + ' TO: ' + maEdit['valore']);
      result.push(maEdit);
    } else {
      log('verificaToSave INVALID ' + ma['nome'] + ' ma: ' + JSON.stringify(ma) + '  maEdit: ' + JSON.stringify(maEdit));
    }
  }
  return result;
}

function addToSaveAttrConfJsonStr(result: ModuloAttributo[], nomeAttributoJson: string, attrConf: Object, confJsonStrEdit: string)
  : ModuloAttributo[] {
  if (attrConf && JSON.stringify(attrConf) === JSON.stringify(JSON.parse(confJsonStrEdit))) {
    log('Unchanged ' + nomeAttributoJson + ': ' + JSON.stringify(attrConf));
  } else {
    log('Changed ' + nomeAttributoJson + ' \nFROM: ' + JSON.stringify(attrConf) + ' \nTO: ' + confJsonStrEdit);
    result.push(creaModuloAttributo(nomeAttributoJson, confJsonStrEdit));
  }
  return result;
}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function log(a: any) {
  // console.log(a);
}
