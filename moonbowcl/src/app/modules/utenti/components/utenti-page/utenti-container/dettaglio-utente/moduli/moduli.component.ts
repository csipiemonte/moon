/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtenteModuloAbilitato } from 'src/app/model/dto/utente modulo-abilitato';
import { ModuloAttributo } from 'src/app/model/dto/attr/moduloAttributo';
import { environment } from 'src/environments/environment';
import { Component, Input, OnInit } from '@angular/core';
import { faTrashAlt } from '@fortawesome/free-solid-svg-icons';
import { Modulo } from 'src/app/model/dto/modulo';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { AlertService } from 'src/app/modules/alert/alert.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { NgxSpinnerService } from 'ngx-spinner';
import { ModuliFilterPipe } from 'src/app/modules/custom-pipes/pipes/moduli-filter-pipe';

@Component({
  selector: 'app-utenti-dettaglio-moduli',
  templateUrl: './moduli.component.html',
  styleUrls: ['./moduli.component.scss']
})
export class ModuliComponent implements OnInit {

  @Input() utenteSelezionato: UtenteEnteAbilitato;

  isAdmin = false;

  moduliTutti: Modulo[];
  moduliAbilitati: Modulo[];
  moduliNonAbilitati: Modulo[];
  elencoSuggestModuli: string[] = [];
  moduloSuggestSelezionato: string;

  pageSize = environment.pageSize;
  currPage = 1;

  faTrashAlt = faTrashAlt;
  spinnerLevel = 0;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    protected alertService: AlertService,
    private filterModuliPipe: ModuliFilterPipe) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('moduli::ngOnInit() utenteSelezionato = ' + this.utenteSelezionato);
    this.init();
  }

  init() {
    this.initModuliTutti();
    this.initModuliAbilitati();
  }

  initModuliTutti(): void {
    log('moduli::initModuliTutti() ...');
    if (this.sharedService.mieiModuli && this.sharedService.mieiModuli.length > 0) {
      this.moduliTutti = this.sharedService.mieiModuli;
    } else {
      this.getElencoModuli();
    }
//    log('moduli::initModuliTutti() moduliTutti.length = ' + this.moduliTutti?.length);
  }
  getElencoModuli(): void {
    log('moduli::getElencoModuli() ...');
    this.showSpinner();
    this.moonboblService.getElencoModuli(true).subscribe(
        (moduli) => {
          this.moduliTutti = moduli;
          this.hideSpinner();
          this.aggiornaElencoSuggestModuli();
      }
    ),
        (error) =>  {
      this.hideSpinner();
    };
  }

  initModuliAbilitati() {
    log('moduli::initModuliAbilitati() ...');
    this.showSpinner();
    this.moonboblService.getModuliAbilitatiOfOneUtente(this.utenteSelezionato.identificativoUtente).subscribe(
      (res) => {
        this.moduliAbilitati = res;
        log('moduli::initModuliAbilitati() moonboblService.getModuliAbilitatiOfOneUtente() this.moduliAbilitati = '
          + JSON.stringify(this.moduliAbilitati));
        this.hideSpinner();
        this.aggiornaElencoSuggestModuli(); // da chiamare dopo this.hideSpinner()
        log('moduli::initModuliAbilitati() moduliAbilitati.length = ' + this.moduliAbilitati.length);
      },
      (err) => {
        alert(err.errorMsg);
        this.hideSpinner();
      }
    );
  }

  aggiornaElencoSuggestModuli() {
    log('moduli::aggiornaElencoSuggestModuli() ...');
    if (this.spinnerLevel > 0) {
      log('moduli::aggiornaElencoSuggestModuli() Waiting for data...');
      return;
    }
    if (!this.moduliTutti || !this.moduliAbilitati) {
      log('moduli::aggiornaElencoSuggestModuli() ERROR data non dispo !');
      return;
    }

    const elencoIdModuliAbilitati = this.moduliAbilitati?.map(m => m.idModulo);
    log('moduli::aggiornaElencoSuggestModuli() elencoIdModuliAbilitati = ' + elencoIdModuliAbilitati);
    if (elencoIdModuliAbilitati) {
      this.moduliNonAbilitati = this.moduliTutti.filter(m => (elencoIdModuliAbilitati.indexOf(m.idModulo) === -1));
      log('moduli::aggiornaElencoSuggestModuli() this.moduliNonAbilitati = ' + this.moduliNonAbilitati);
      log('moduli::aggiornaElencoSuggestModuli() this.moduliNonAbilitati = ' + JSON.stringify(this.moduliNonAbilitati));
    } else {
      log('moduli::aggiornaElencoSuggestModuli() ALL');
      this.moduliNonAbilitati = this.moduliTutti;
    }

    this.elencoSuggestModuli = [];
    this.moduliNonAbilitati.forEach(el => {
      if (el.codiceModulo != null) {
        this.elencoSuggestModuli.push(`${el.oggettoModulo} (${el.codiceModulo})` +
          (this.isAdmin ? ` [${el.idModulo}]` : ``));
      }
    });
    this.elencoSuggestModuli.sort();
    log('moduli::aggiornaElencoSuggestModuli() this.elencoSuggestModuli = ' + this.elencoSuggestModuli);
  }

  selectEvent($event: any) {
    this.moduloSuggestSelezionato = $event;
  }

  clearFilter() {
    log('moduli::clearFilter() this.moduloSuggestSelezionato was ' + this.moduloSuggestSelezionato);
    this.moduloSuggestSelezionato = '';
    log('moduli::clearFilter() this.moduloSuggestSelezionato is now ' + this.moduloSuggestSelezionato);
  }

  pageChanged(currPage: number) {
    log('moduli::pageChanged()');
    log('moduli::pageChanged() currPage = ' + currPage);
//    this.sharedService.pageUtentiModuli = currPage;
  }

  elimina(idModulo: number) {
    log('moduli::elimina() idUtente=' + this.utenteSelezionato.idUtente + '  idModulo= ' + idModulo);
    this.moonboblService.deleteUtenteModulo(this.utenteSelezionato.idUtente, idModulo).subscribe(
      (res) => {
        log('moduli::elimina() moonboblService.deleteUtenteModulo() Done.');
        this.init();
      },
      (err) => {
        alert(err.errorMsg);
      }
    );
  }

  showSpinner() {
    log('moduli::showSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel++;
    if (this.spinnerLevel === 1) {
      log('moduli::showSpinner() spinnerService.show().');
      this.spinnerService.show();
    }
  }
  hideSpinner() {
    log('moduli::hideSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel--;
    if (this.spinnerLevel === 0) {
      log('moduli::hideSpinner() spinnerService.hide().');
      this.spinnerService.hide();
    }
  }

  async aggiungiModulo() {
    this.showSpinner();
    log('moduli::aggiungiModulo() this.moduloSuggestSelezionato = ' + this.moduloSuggestSelezionato);
    const moduloSelezionato = this.filterModuliPipe.transformWithCodice(this.moduliNonAbilitati, this.moduloSuggestSelezionato)[0];
    log('moduli::aggiungiModulo() this.moduloSelezionato = ' + JSON.stringify(moduloSelezionato));
    log('moduli::aggiungiModulo() this.moduloSelezionato.idModulo = ' + moduloSelezionato.idModulo);
    this.moonboblService.postUtenteModulo(this.utenteSelezionato.idUtente, moduloSelezionato.idModulo).subscribe(
      (res) => {
        log('moduli::aggiungiModulo() moonboblService.postUtenteModulo() Done.');
        this.clearFilter();
        this.hideSpinner();
        this.init();
      },
      (err) => {
        this.hideSpinner();
        alert(err.errorMsg);
      }
    );
  }
}

function log(a: any) {
  console.log(a);
}

