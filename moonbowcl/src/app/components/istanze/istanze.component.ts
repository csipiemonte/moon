/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { MoonboblService } from '../../services/moonbobl.service';
import { Modulo } from '../../model/dto/modulo';
import { Comune } from '../../model/dto/comune';
import { SharedService } from '../../services/shared.service';
import { Nav } from 'src/app/model/dto/nav';
import { CodiceNome } from 'src/app/model/dto/codice-nome';
import { Ente } from 'src/app/model/dto/ente';
import { logByKeyValue, orderByProperty, uniqueByProperty, uniqueByPropMaxValue } from 'src/app/common/utils/array-util';
import { NavSelection } from 'src/app/common/nav-selection';
import { NgxSpinnerService } from 'ngx-spinner';
import { CurrentFilter } from 'src/app/common/current-filter';
import { AlertService } from 'src/app/modules/alert';
import { MODULO, STORAGE_KEYS } from 'src/app/common/costanti';
import { Dichiarante } from 'src/app/model/dto/dichiarante';

@Component({
  selector: 'app-istanze',
  templateUrl: './istanze.component.html',
  styleUrls: ['./istanze.component.css']
})
export class IstanzeComponent implements OnInit {
  selectedModulo: number;
  active = 1;
  moduli: Modulo[];
  idComune: string;
  idEnte: string;
  comuni: Comune[];
  comuniUtente: Comune[];
  entiUtente: CodiceNome[];
  idProvincia: string;
  cfDichiarante: string;
  nomeDichiarante: string;
  cognomeDichiarante: string;
  showFiltroComune: Boolean;
  showFiltroComuneUtente: Boolean;
  showFiltroEnteUtente: Boolean;
  showFiltroDichiarante: Boolean;
  showColonnaComune: Boolean;
  showColonnaEnte: Boolean;

  showFiltroMultiEnte: Boolean = false;
  idMultiEnte: number;
  // enti: Ente[];

  alertId = 'alert-container-istanze';
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

  constructor(private moonboblService: MoonboblService,
    private sharedService: SharedService,
    private spinnerService: NgxSpinnerService,
    protected alertService: AlertService) {

  }


  ngOnInit() {

    this.active = this.sharedService.activeTab;

    // if (this.sharedService.nav && this.sharedService.nav.active === NavSelection.ISTANZE_DA_COMPLETARE) {
    //   this.active = NavSelection.ISTANZE;
    // }

    this.sharedService.nav = new Nav(NavSelection.ISTANZE, 'istanze');
    //this.active = this.sharedService.activeTab;

    this.setMultiEnteParameters();

    if (this.sharedService.mieiModuli.length === 0){
      this.getElencoModuli();
    }else{
      this.moduli = this.sharedService.mieiModuli;
    }

    // this.getElencoModuli();

    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));

    if (moduloSelezionato !== 'undefined' && !(moduloSelezionato == null)) {
      this.selectedModulo = Number(moduloSelezionato.idModulo);
      this.showFiltroDichiarante = true;
      if (moduloSelezionato.showFiltroComune) {
        this.showFiltroComune = true;
        this.showColonnaComune = true;
        if (moduloSelezionato.provinciaSelezionata) {
          this.idProvincia = moduloSelezionato.provinciaSelezionata;
          this.getElencoComuni(this.idProvincia);
          this.idComune = moduloSelezionato.comuneSelezionato;
        }

      }
      if (moduloSelezionato.showFiltroComuneUtente) {
        this.getElencoComuniUtente();
        this.showFiltroComuneUtente = true;
        this.showColonnaComune = true;
        if (moduloSelezionato.comuneUtenteSelezionato) {
          this.idComune = moduloSelezionato.comuneUtenteSelezionato;
        }
      }
      if (moduloSelezionato.showFiltroEnteUtente) {
        this.getElencoEntiUtente();
        this.showFiltroEnteUtente = true;
        this.showColonnaEnte = true;
        if (moduloSelezionato.enteSelezionato) {
          this.idEnte = moduloSelezionato.enteSelezionato;
        }

      }
    }

    if (this.sharedService?.getCurrentFilter(NavSelection.ISTANZE)) {
      console.log('FILTRO: ' + JSON.stringify(this.sharedService?.getCurrentFilter(NavSelection.ISTANZE)));

      let sf = this.sharedService?.getCurrentFilter(NavSelection.ISTANZE);
      if (sf.filter.dichiarante) {
        this.cfDichiarante = sf.filter.cfDichiarante;
        this.nomeDichiarante = sf.filter.nomeDichiarante;
        this.cognomeDichiarante = sf.filter.cognomeDichiarante;
      }

    }

  }


  selectModulo(id) {
    console.log('istanze.selectModulo() id: ' + id);
    this.alertService.clear(this.alertId);

    this.selectedModulo = id; // this.selectedOption;
    this.showFiltroComune = false;
    this.showColonnaComune = false;
    this.showColonnaEnte = false;
    this.showFiltroComuneUtente = false;
    this.showFiltroEnteUtente = false;
    this.showFiltroDichiarante = true;
    this.idComune = '';
    this.idEnte = '';
    this.idProvincia = '';

    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + id));
    console.log('istanze.selectModulo() attributi: ' + datiAtt.attributi);
    console.log('istanze.selectModulo() attributi.TIPO_FILTER_BO: ' + datiAtt.attributi.TIPO_FILTER_BO);
    const tipoShowFilter = datiAtt.attributi.TIPO_FILTER_BO;
    if (tipoShowFilter === 'showComune') {
      this.showFiltroComune = true;
      this.showColonnaComune = true;
      const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
      if (moduloSelezionato) {
        if (moduloSelezionato.idModulo === this.selectedModulo) {
          this.idProvincia = moduloSelezionato.provinciaSelezionata;
          this.idComune = moduloSelezionato.comuneSelezionato;
        } else {
          this.idProvincia = null;
          this.idComune = null;
        }
      }
    }
    if (tipoShowFilter === 'showComuneUtente') {
      this.getElencoComuniUtente();
      this.showFiltroComuneUtente = true;
      this.showColonnaComune = true;
      const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
      if (moduloSelezionato) {
        if (moduloSelezionato.idModulo === this.selectedModulo) {
          this.idComune = moduloSelezionato.comuneEnteSelezionato;
        } else {
          this.idComune = null;
        }
      }
    }
    if (tipoShowFilter === 'showEnteUtente') {
      this.getElencoEntiUtente();
      this.showFiltroEnteUtente = true;
      this.showColonnaEnte = true;
      const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
      if (moduloSelezionato) {
        if (moduloSelezionato.idModulo === this.selectedModulo) {
          this.idEnte = moduloSelezionato.enteSelezionato;
        } else {
          this.idEnte == null;
        }
      }
    }

    // check multi ente 
    // if (this.sharedService.UserLogged) {
    //   if (this.sharedService.UserLogged.multiEntePortale) {
    //     console.log(" IS MULTI ENTE ");
    //     this.showFiltroMultiEnte = true;
    //     this.getEntiMultiEnte();
    //   }
    // }

    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZIONATO, JSON.stringify({
      idModulo: id,
      showFiltroComune: this.showFiltroComune,
      showFiltroComuneUtente: this.showFiltroComuneUtente,
      showFiltroEnteUtente: this.showFiltroEnteUtente,
      showColonnaComune: this.showColonnaComune,
      showColonnaEnte: this.showColonnaEnte,
      enteSelezionato: this.idEnte,
      comuneUtenteSelezionato: this.idComune,
      provinciaSelezionata: this.idProvincia,
      comuneSelezionato: this.idComune,
    }));

    this.setMultiEnteParameters();
  }

  selectComune(id) {
    this.idComune = id;
    this.updateComuneUtenteToStorage(this.idComune);
    // if (!id) {
    //   this.idProvincia = '';
    // }

    if (this.idProvincia) {
      this.updateComuneToStorage(this.idComune);
    }


  }

  selectEnte(id) {
    this.idEnte = id;
    this.updateEnteToStorage(this.idEnte);
  }

  selectProvincia(id) {
    this.idProvincia = id;
    this.updateProvinciaToStorage(this.idProvincia);
    if (!id) {
      this.idComune = '';
    } else {
      this.getElencoComuni(id);
    }
  }

  // selectMultiEnte(id) {
  //   this.idMultiEnte = id;
  // }

  private getElencoModuli(): void {
    // try {

    this.spinnerService.show();
    this.moonboblService.getElencoModuli(true).subscribe(
        (moduli) => {
        this.moduli = moduli.sort((ma, mb) =>
          ma.oggettoModulo.toLocaleLowerCase().trim().localeCompare(mb.oggettoModulo.toLocaleLowerCase().trim()));
        //  const unuqueByIdModulo = uniqueByPropMaxValue(moduli,'idModulo','versioneModulo');
        //  this.moduli = unuqueByIdModulo;
        this.spinnerService.hide();
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

      },
    (error) => {
      this.spinnerService.hide();
      console.error(error);
    });
    // } catch (err) {
    //   console.log(err);
    //   this.spinnerService.hide();
    // }

  }

  private getElencoComuni(idProvincia): void {
    this.moonboblService.getElencoComuni(idProvincia).then(comuni => this.comuni = comuni);
  }

  private getElencoComuniUtente(): void {
    this.moonboblService.getElencoComuniUtente().then(comuniUtente => this.comuniUtente = comuniUtente);
  }

  private getElencoEntiUtente(): void {
    this.moonboblService.getElencoEntiUtente().then(entiUtente => this.entiUtente = entiUtente);
  }

  setMultiEnteParameters(): void {
    if (this.sharedService.UserLogged && this.sharedService.UserLogged.multiEntePortale && this.sharedService.UserLogged.ente) {
      this.idMultiEnte = this.sharedService.UserLogged.ente.idEnte;
      this.showFiltroMultiEnte = true;
    }
  }
  // private getEntiMultiEnte(): void {
  //   this.moonboblService.getEntiMultiEnte().then(enti => this.enti = enti);
  // }

  updateEnteToStorage(idEnte) {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZIONATO));
    moduloSelezionato.enteSelezionato = idEnte;
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

  getAlert(message) {
    const type = message.type;
    const text = message.text;
    let options = {};
    
    if (message.autoclose) {
      options = this.alertOptions;
    } else {
      options = this.alertOptionsNoAutoClose;
    }
    if (message.clear){
      this.alertService.clear(this.alertId);
    }else{
      switch (type) {
        case 'success': {
          this.alertService.success(text, options);
          break;
        }
        case 'info': {
          this.alertService.info(text, options);
          break;
        }
        case 'error': {
          this.alertService.error(text, options);
          break;
        }
        case 'warn': {
          this.alertService.warn(text, options);
          break;
        }
        default: {
          this.alertService.warn(text, options);
          break;
        }
      }
    }
  }

}

// function getId(id, typeId){
//   const ids = id.split('-');
//   switch (typeId) {
//     case MODULO.ID_MODULO:
//       return ids[0];
//     case MODULO.ID_VERSIONE_MODULO:
//       return ids[1];

//     default:
//       return ids[0];
//     }
// }
