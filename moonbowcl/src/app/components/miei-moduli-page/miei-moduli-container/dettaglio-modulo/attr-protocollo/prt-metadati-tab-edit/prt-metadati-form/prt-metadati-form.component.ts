/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { faTrashAlt, faPlusCircle, IconDefinition } from '@fortawesome/free-solid-svg-icons';
import * as _ from 'lodash';
import { result } from 'lodash';
import { NgxSpinnerService } from 'ngx-spinner';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { Modulo } from 'src/app/model/dto/modulo';
import { ProtocolloMetadato } from 'src/app/model/dto/protocollo-metadato';
import { ProtocolloMetadatoKeyConf } from 'src/app/model/dto/protocollo-metadato-key-conf';
import { ProtocolloMetadatoStato } from 'src/app/model/dto/protocollo-metadato-stato';
import { AlertService } from 'src/app/modules/alert';
import { MoonboblService } from 'src/app/services/moonbobl.service';

@Component({
  selector: 'app-miei-moduli-dettaglio-attr-protocollo-metadati-tab-edit-form',
  templateUrl: './prt-metadati-form.component.html',
  styleUrls: ['./prt-metadati-form.component.scss']
})
export class PrtMetadatiFormComponent implements OnInit, OnChanges {

  @Input() modulo: Modulo;
  @Input() maPrtMetadati: ModuloAttributo;
  @Input() idxTab: any;
  @Input() metadatiFull: ProtocolloMetadato[];
  @Output() eventFormChange = new EventEmitter<any>();
  @Output() eventFormSave = new EventEmitter<any>();

  elencoModuloAttributi: ModuloAttributo[];

  metadatiNonAssociati: ProtocolloMetadato[];
  metadati: any;
  metadatiJsonKV: any;
  // metadatiKeys: string[];
  metadatiKeysConf: ProtocolloMetadatoKeyConf[];

  newNomeMetadato: string = undefined;

  faTrashAlt = faTrashAlt;
  faPlusCircle = faPlusCircle;
  // faPenCircle = faPenCircle;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    protected alertService: AlertService) { }

  ngOnInit(): void {
    log('prt-metadati-form::ngOnInit() this.idxTab = ' + (this.idxTab));
    log('prt-metadati-form::ngOnInit() this.metadatiFull = ' + this.metadatiFull);
    // this.metadatiJsonKV = _.clone(this.metadati);
    this.metadati = this.metadatiOfTab(this.idxTab);
    this.metadatiJsonKV = _.clone(this.metadati);
    log('prt-metadati-form::ngOnInit() this.metadati = ' + (this.metadati ? JSON.stringify(this.metadati) : this.metadati));
    this.initMetadatiKeysConf(this.metadatiJsonKV);
    this.aggiornaMetadatiNonAssociati();
  }

  ngOnChanges(changes: SimpleChanges) {
    log('prt-metadati-form::ngOnChanges() this.idxTab = ' + (this.idxTab));
    log('prt-metadati-form::ngOnChanges() this.metadatiFull = ' + this.metadatiFull);
    this.metadati = this.metadatiOfTab(this.idxTab);
    this.metadatiJsonKV = _.clone(this.metadati);
    log('prt-metadati-form::ngOnChanges() this.metadati = ' + (this.metadati ? JSON.stringify(this.metadati) : this.metadati));
    this.initMetadatiKeysConf(this.metadatiJsonKV);
    this.aggiornaMetadatiNonAssociati();
  }

  metadatiOfTab(tipoDocToSearch: string): any {
    let result = {};
    if (this.maPrtMetadati) {
      const moduloPrtMetadati = JSON.parse(this.maPrtMetadati.valore);
      if (moduloPrtMetadati) {
        const searchItem = moduloPrtMetadati.find((mpm) => mpm.type === tipoDocToSearch);
        if (searchItem) {
          result = searchItem.metadati;
        }
      }
    }
    log('prt-metadati-form::metadatiOfTab(' + tipoDocToSearch + ') result = ' + JSON.stringify(result));
    return result;
  }

  initMetadatiKeysConf(metadatiJsonKV: any) {
    if (this.metadatiJsonKV) {
      this.metadatiKeysConf = Object.keys(this.metadatiJsonKV).map((k) => {
        return new ProtocolloMetadatoKeyConf(k, ProtocolloMetadatoStato.SAVED);
      });
    }
  }

  aggiornaMetadatiKeysConf(metadatiJsonKV: any) {
    if (this.metadatiJsonKV) {
      this.metadatiKeysConf = Object.keys(this.metadatiJsonKV).map((k) => {
        if (!this.metadati[k]) {
          return new ProtocolloMetadatoKeyConf(k, ProtocolloMetadatoStato.NEW);
        } else {
          if (this.metadatiJsonKV[k] === this.metadati[k]) {
            return new ProtocolloMetadatoKeyConf(k, ProtocolloMetadatoStato.SAVED);
          } else {
            return new ProtocolloMetadatoKeyConf(k, ProtocolloMetadatoStato.MODIFIED);
          }
        }
      });
      this.aggiornaFormChangeId();
    }
  }

  aggiornaOneMetadatiKeysConf(nomeMetadato: string, valueMetadato: string) {
    const metadatoKeyConf = this.metadatiKeysConf.find((mkc) => {
      return mkc.nomeMetadato === nomeMetadato;
    });
    log('prt-metadati-form::aggiornaOneMetadatiKeysConf this.metadati ' + this.metadati);
    if (this.metadati[nomeMetadato]) {
      if (valueMetadato === this.metadati[nomeMetadato]) {
        metadatoKeyConf.statoMetadato = ProtocolloMetadatoStato.SAVED;
      } else {
        metadatoKeyConf.statoMetadato = ProtocolloMetadatoStato.MODIFIED;
      }
    }
    this.aggiornaFormChangeId();
  }

  aggiungiOneMetadatiKeysConf(nomeMetadato: string, valueMetadato: string) {
    const newMKC = new ProtocolloMetadatoKeyConf(this.newNomeMetadato,
      (this.metadati[nomeMetadato] && this.metadati[nomeMetadato] === valueMetadato) ?
        ProtocolloMetadatoStato.SAVED : ProtocolloMetadatoStato.NEW);
    this.metadatiKeysConf = [...this.metadatiKeysConf, newMKC];
    this.aggiornaFormChangeId();
  }

  eliminaOneMetadatiKeysConf(nomeMetadatoDaEliminare: string) {
    const indexOfObject = this.metadatiKeysConf.findIndex(mkc => {
      return mkc.nomeMetadato === nomeMetadatoDaEliminare;
    });
    this.metadatiKeysConf.splice(indexOfObject, 1);
    this.aggiornaFormChangeId();
  }

  aggiornaFormChangeId() {
    const formChangeId = this.metadatiKeysConf.reduce((accumulator, mkc) => {
      return accumulator + mkc.statoMetadato;
    }, 0);
    log('prt-metadati-form::aggiornaFormChangeId() formChangeId = ' + formChangeId);
    this.eventFormChange.emit(formChangeId);
  }

  aggiornaMetadatiNonAssociati() {
    if (this.metadatiFull) {
      this.metadatiNonAssociati = this.metadatiFull.filter(m => !this.isConfigurato(m));
    }
    this.newNomeMetadato = undefined;
  }

  isConfigurato(m: ProtocolloMetadato): boolean {
    return this.metadatiKeysConf.some( ({ nomeMetadato }) => nomeMetadato === m.nomeMetadato );
  }

  isWithIcon(m: ProtocolloMetadato): boolean {
    const metadatoKeyConf = this.metadatiKeysConf.find((mkc) => {
      return mkc.nomeMetadato === m.nomeMetadato;
    });
    return (metadatoKeyConf.statoMetadato === ProtocolloMetadatoStato.SAVED) ? false : true;
  }

  iconOf(m: ProtocolloMetadato): IconDefinition {
    const metadatoKeyConf = this.metadatiKeysConf.find((mkc) => {
      return mkc.nomeMetadato === m.nomeMetadato;
    });
    switch (metadatoKeyConf.statoMetadato) {
      case ProtocolloMetadatoStato.NEW:
        return faPlusCircle;
        break;
      case ProtocolloMetadatoStato.MODIFIED:
        return faPlusCircle;
        break;
      default:
        break;
    }
  }

  elimina(nomeMetadatoDaEliminare: string) {
    log('prt-metadati-form::elimina() nomeMetadatoDaEliminare=' + nomeMetadatoDaEliminare);
    delete this.metadatiJsonKV[nomeMetadatoDaEliminare];
    this.eliminaOneMetadatiKeysConf(nomeMetadatoDaEliminare);
    this.aggiornaMetadatiNonAssociati();
  }

  aggiungi() {
    log('prt-metadati-form::aggiungi() newNomeMetadato = ' + this.newNomeMetadato);
    const locNewMetadato = this.metadatiFull.filter(m => m.nomeMetadato === this.newNomeMetadato)[0];
    this.metadatiJsonKV[this.newNomeMetadato] = locNewMetadato.defaultValue ? locNewMetadato.defaultValue : '';
    this.aggiungiOneMetadatiKeysConf(this.newNomeMetadato, this.metadatiJsonKV[this.newNomeMetadato]);
    log('metadatiJsonKV=' + JSON.stringify(this.metadatiJsonKV));
    log('newValue inizializzata con [' + this.metadatiJsonKV[this.newNomeMetadato] + ']');
    this.aggiornaMetadatiNonAssociati();
  }

  onKeyUp(event: any, nomeMetadato: string) {
    log('prt-metadati-form::onKeyUp() nomeMetadato = ' + nomeMetadato);
    log('prt-metadati-form::onKeyUp() event.target.value = [' + event.target.value + ']');
    this.aggiornaOneMetadatiKeysConf(nomeMetadato, event.target.value);
  }

  salva() {
    log('prt-metadati-form::salva() metadatiJsonKV=' + JSON.stringify(this.metadatiJsonKV));
    log('prt-metadati-form::salva() maPrtMetadati=' + JSON.stringify(this.maPrtMetadati));
    this.spinnerService.show();
    // tslint:disable-next-line:prefer-const
    let maToSave = [];
    const maPrtMetadatiToSave = creaModuloAttributo(this.maPrtMetadati.nome, this.nuovoValoreToSave());
    maToSave.push(maPrtMetadatiToSave);
    log('Save maToSave=' + JSON.stringify(maToSave));
    if (maToSave && maToSave.length > 0) {
      this.moonboblService.aggiornaModuloAttributiProtocollo(this.modulo, maToSave).subscribe(
        (attributi: ModuloAttributo[]) => {
          this.elencoModuloAttributi = attributi;
          log('prt-metadati-form::salva() elencoModuloAttributi=' + JSON.stringify(this.elencoModuloAttributi));
          this.maPrtMetadati = this.findOrCreateWithDefaultValue(this.elencoModuloAttributi, 'PRT_METADATI', '[]');
          log('prt-metadati-form::salva() maPrtMetadati=' + JSON.stringify(this.maPrtMetadati));
          log('prt-metadati-form::salva() this.eventFormSave.emit(this.maPrtMetadati) ...');
          this.eventFormSave.emit(this.maPrtMetadati);
          this.spinnerService.hide();
          // this.alertService.success('Salvataggio attributi WF azioni effettuato con successo !', this.alertOptions);

          // this.alert.emit({ text: MsgAttrAzioni.SUCCESS_ATTR_AZIONI, type: 'success', autoclose: true });
        },
        (err: MoonboError) => {
          this.spinnerService.hide();
          // this.alertService.error('Impossibile effettuare il salvataggio attributi WF azioni !' + err.errorMsg, this.alertOptions);
          // this.alert.emit({ text: MsgAttrAzioni.ERROR_ATTR_AZIONI, type: 'error', autoclose: false });
        }
      );
    // } else {
    //   this.spinnerService.hide();
    //   // this.alertService.info('Nessun modifica effettuata !', this.alertOptions);
    //   this.alert.emit({ text: MsgAttrAzioni.INFO_NO_MODIFY_ATTR_AZIONI, type: 'info', autoclose: false });
    }
  }

  nuovoValoreToSave(): string {
    let result = '';
    // const metadatiJsonToSave = JSON.stringify(this.metadatiJsonKV);
    const metadatiJsonToSave = this.metadatiJsonKV;
    if (this.maPrtMetadati) {
      let moduloPrtMetadati = [];
      moduloPrtMetadati = JSON.parse(this.maPrtMetadati.valore);
      if (moduloPrtMetadati) {
        const searchItem = moduloPrtMetadati.find((mpm) => mpm.type === this.idxTab);
        if (searchItem) {
          searchItem.metadati = metadatiJsonToSave;
        } else {
          moduloPrtMetadati.push({type : this.idxTab, metadati: metadatiJsonToSave});
        }
        result = JSON.stringify(moduloPrtMetadati);
      } else {
        log('prt-metadati-form::nuovoValoreToSave() moduloPrtMetadati undefied.');
      }
    }
    log('prt-metadati-form::nuovoValoreToSave() result=' + result);
    return result;
  }

  findOrCreateWithDefaultValue(moduloAttributi: ModuloAttributo[], nome: string, defaultValore: string): ModuloAttributo {
    let result: ModuloAttributo;
    if (moduloAttributi && moduloAttributi.length > 0) {
      result = moduloAttributi.find(ma => ma.nome === nome);
    }
    if (!result) {
      result = creaModuloAttributo(nome, defaultValore);
    // } else {
    //   this.titleUpd.set(result.nome, 'Ultima modifica il ' + result.dataUpd + ' da ' + result.attoreUpd);
    }
    return result;
  }

}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function log(a: any) {
  console.log(a);
}
