/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';

import { NgxSpinnerService } from 'ngx-spinner';
import { HandleExceptionService } from 'src/app/services/handle-exception.service';
import { SharedService } from 'src/app/services/shared.service';
import { faHome } from '@fortawesome/free-solid-svg-icons';
import { Router } from '@angular/router';
import { Modulo } from 'src/app/model/dto/modulo';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { CompilaOperatoreFormComponent } from '../forms/compila-operatore-form/compila-operatore-form.component';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { Identita } from 'src/app/common/identita';

@Component({
  selector: 'app-nuova-istanza',
  templateUrl: './nuova-istanza.component.html',
  styleUrls: ['./nuova-istanza.component.scss']
})
export class NuovaIstanzaComponent implements OnInit {

  moduli: Modulo[];
  msg: string;
  faHome = faHome;

  constructor(
    private moonservice: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private handleException: HandleExceptionService,
    private router: Router,
    private sharedService: SharedService,
    private modalService: NgbModal,
    private alertService: AlertService) {
  }

  ngOnInit(): void {
    if (this.sharedService.elencoModuliNuovaIstanza) {
      this.moduli = this.sharedService.elencoModuliNuovaIstanza;
      this.sharedService.elencoModuliNuovaIstanza = undefined;
    } else {
      this.getElencoModuli();
    }
  }

  alertOptions = {
    id: 'alert-compila-istanza-nuova-istanza',
    autoClose: true,
    keepAfterRouteChange: false
  };

  private getElencoModuli(): void {
    this.moonservice.getElencoModuliPubblicati().then(moduli => {
      this.moduli = moduli;
      if (this.moduli.length === 0) {
        this.alertService.warn('Al momento non sono disponibili moduli per presentare istanze!', this.alertOptions);
        //this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
      }
      this.spinnerService.hide();
    }
    )
      .catch(err => {
        console.log(err);
        this.spinnerService.hide();
        //this.msg = 'Al momento non sono disponibili moduli per presentare istanze!';
        this.alertService.warn('Al momento non sono disponibili moduli per presentare istanze!', this.alertOptions);
      }
      );
  }

  isModuloCompilabileContoTerzi(m: Modulo): boolean {
    if (m) {
      const v = this.moonservice.getModuloAttributo(m, 'CONTO_TERZI');
      if (v) {
        return true;
      } else {
        return false;
      }
    }
  }

  initCompilazioneOperatore(moduloSelezionato: Modulo) {
    this.moonservice.getModuloWithFields(moduloSelezionato.idModulo,
      moduloSelezionato.idVersioneModulo,
      'attributiGenerali').subscribe((modulo: Modulo) => {

        let attributi = modulo.attributi;
        let compilaBo: ModuloAttributo = findOrCreateWithDefaultValue(attributi, 'COMPILA_BO', '');

        if (compilaBo) {
          if (compilaBo?.valore !== Identita.NO_CONTO_TERZI){
            const modalRef = this.modalService.open(CompilaOperatoreFormComponent, { centered: true, backdrop: 'static' });
            modalRef.componentInstance.form = compilaBo.valore;
            modalRef.result.then(
              (result) => {
                console.log(result);  
                const cf_pIva = result.value.cf_pIva;
                const nome = result.value.nome;
                const cognome = result.value.cognome;
                this.sharedService.datiCompilaOperatore.dati = { cf_pIva, nome, cognome };
                this.router.navigate(['nuova-istanza/view-form/' + moduloSelezionato.idModulo + '/' + moduloSelezionato.idVersioneModulo]);
              },
              (reason) => {
                // console.log(reason);
              }
            );
          }
          else{
            this.sharedService.datiCompilaOperatore.dati = { no_conto_terzi: Identita.NO_CONTO_TERZI};
            this.router.navigate(['nuova-istanza/view-form/' + moduloSelezionato.idModulo + '/' + moduloSelezionato.idVersioneModulo]);
          }
        }
      },
        (err: MoonboError) => {
          //return null;
          this.alertService.error(err.errorMsg, this.alertOptions);
        }
      );
  }

  isCompilabileOperatore(moduloSelezionato: Modulo) {
    this.moonservice.getModuloWithFields(moduloSelezionato.idModulo,
      moduloSelezionato.idVersioneModulo,
      'attributiGenerali').subscribe((modulo: Modulo) => {

        let attributi = modulo.attributi;
        let compilaBo: ModuloAttributo = findOrCreateWithDefaultValue(attributi, 'COMPILA_BO', '');
        if (compilaBo) {
          return true;
        }
        else return false;

      },
        (err: MoonboError) => {
          //return false;
          this.alertService.error('Errore servizio selezione modulo '+ err.errorMsg, this.alertOptions);
        }
      );
  }

  checkPartitaIVA(pIva) {
    return (/^[0-9]{11}$/.test(pIva));
  }

  checkCF(cf) {
    return (/^[a-zA-Z]{6}[0-9]{2}[a-zA-Z][0-9]{2}[a-zA-Z][0-9]{3}[a-zA-Z]$/.test(cf));
  }

  back() {
    this.router.navigate(['categorie']);
  }

}

function creaModuloAttributo(nome: string, valore: string): ModuloAttributo {
  const obj = new ModuloAttributo();
  obj.nome = nome;
  obj.valore = valore;
  return obj;
}

function findOrCreateWithDefaultValue(moduloAttributi: ModuloAttributo[], nome: string, defaultValore: string): ModuloAttributo {
  let result: ModuloAttributo;
  if (moduloAttributi && moduloAttributi.length > 0) {
    result = moduloAttributi.find(ma => ma.nome === nome);
  }
  if (!result) {
    result = creaModuloAttributo(nome, defaultValore);
  }
  return result;
}


