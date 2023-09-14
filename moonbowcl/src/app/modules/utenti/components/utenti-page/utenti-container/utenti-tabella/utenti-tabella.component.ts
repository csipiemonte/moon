/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { environment } from 'src/environments/environment';
import { NgxSpinnerService } from 'ngx-spinner';
import { Utente } from 'src/app/model/dto/utente';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { SharedService } from 'src/app/services/shared.service';
import { AlertService } from 'src/app/modules/alert';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { UtentiFilterPipe } from 'src/app/pipes/utenti-filter-pipe';
import { UtentePipe } from 'src/app/pipes/utente-pipe';
import { faHome, faSave, faPlus } from '@fortawesome/free-solid-svg-icons';
import { UserInfo } from 'src/app/model/common/user-info';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-utenti-tabella',
  templateUrl: './utenti-tabella.component.html',
  styleUrls: ['./utenti-tabella.component.scss']
})
export class UtentiTabellaComponent implements OnInit, OnChanges {

  @Output() eventCreaUtente = new EventEmitter();
  @Output() eventDettaglio = new EventEmitter<UtenteEnteAbilitato>();
  @Output() eventEliminaUtenteAreaRuolo = new EventEmitter<UtenteEnteAbilitato>();

  currentUser: UserInfo;
  isAdmin = false;

  utentiAbilitati: UtenteEnteAbilitato[];
  utentiNoAbilitati: UtenteEnteAbilitato[];
  elencoUtentiFiltrato: UtenteEnteAbilitato[];
  private _filtro: string;
  get filtro(): string {
    return this._filtro;
  }
  set filtro(value: string) {
    this._filtro = value;
    this.sharedService.filtroUtenti = value;
    this.filtraUtenti();
  }
  filtroFlagAttivo: string; // SI/NO/TUTTI
  filtroFlagAbilitato: string; // SI/NO

  pageSize = environment.pageSize;
  currPage = 1;

  faPlus = faPlus;
  mapOperatori: Map<string, Utente> = new Map<string, Utente>();
  spinnerLevel = 0;

  utentiForm = new FormGroup({
    filtroFlagAttivo: new FormControl()
  });

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    protected alertService: AlertService,
    private utentiFilterPipe: UtentiFilterPipe,
    private utentePipe: UtentePipe) {
    this.currentUser = this.sharedService.UserLogged;
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.currPage = this.sharedService.pageUtenti ? this.sharedService.pageUtenti : 1;
    this._filtro = this.sharedService.filtroUtenti ? this.sharedService.filtroUtenti : null;
    this.filtroFlagAttivo = this.sharedService.filtroFlagAttivoUtenti ? this.sharedService.filtroFlagAttivoUtenti : 'SI';
    this.filtroFlagAbilitato = this.sharedService.filtroFlagAbilitatoUtenti ? this.sharedService.filtroFlagAbilitatoUtenti : 'SI';
  }

  ngOnInit(): void {
    log('utenti-tabella::ngOnInit()...');
    log('utenti-tabella::ngOnInit() utentiAbilitati = ' + this.utentiAbilitati); // valorizzato in seguito con ngOnChanges()
    this.currPage = this.sharedService.pageUtenti ? this.sharedService.pageUtenti : 1;
    log('utenti-tabella::ngOnInit() currPage = ' + this.currPage);
    log('utenti-tabella::ngOnInit() pageSize=' + this.pageSize);
    this._filtro = this.sharedService.filtroUtenti ? this.sharedService.filtroUtenti : null;
    log('utenti-tabella::ngOnInit() _filtro = ' + this._filtro);
    this.filtroFlagAttivo = this.sharedService.filtroFlagAttivoUtenti ? this.sharedService.filtroFlagAttivoUtenti : 'SI';
    log('utenti-tabella::ngOnInit() filtroFlagAttivo = ' + this.filtroFlagAttivo);
    this.filtroFlagAbilitato = this.sharedService.filtroFlagAbilitatoUtenti ? this.sharedService.filtroFlagAbilitatoUtenti : 'SI';
    log('utenti-tabella::ngOnInit() filtroFlagAbilitato = ' + this.filtroFlagAbilitato);
    this.init();
  }

  init() {
    log('utenti-tabella::init()');
    this.initUtentiEnteAbilitato();
    this.initUtentiEnteNoAbilitato();
  }

  initUtentiEnteAbilitato() {
    log('utenti-tabella::initUtentiEnteAbilitato()');
    this.showSpinner();
    this.moonboblService.getUtentiEnteAbilitato().subscribe(
      (res) => {
        log('utenti-tabella::initUtentiEnteAbilitato() moonboblService.getUtentiEnteAbilitato() res = ' + res);
        res.forEach(u => {
          u.wclKey = u['entiAreeRuoli'][0]['areeRuoli'][0]['nomeArea'] + ' ' + u['entiAreeRuoli'][0]['areeRuoli'][0]['nomeRuolo'];
        });
        this.utentiAbilitati = res;
        // this.popolaMapOperatori();
        log('utenti-tabella::initUtentiEnteAbilitato() moonboblService.getUtentiEnteAbilitato() this.utentiAbilitati = '
          + this.utentiAbilitati);

        if (!this.utentiAbilitati) {
          this.utentiAbilitati = [];
        }
        if (this.filtroFlagAbilitato === 'SI') {
          this.filtraUtenti();
          log('utenti-tabella::initUtentiEnteAbilitato() elencoUtentiFiltrato.length = ' + this.elencoUtentiFiltrato.length);
        }
        this.hideSpinner();
      },
      (err) => {
        alert(err.errorMsg);
        this.hideSpinner();
      }
    );
  }
  initUtentiEnteNoAbilitato() {
    log('utenti-tabella::initUtentiEnteNoAbilitato()');
    this.showSpinner();
    this.moonboblService.getUtentiEnteNoAbilitato().subscribe(
      (res) => {
        log('utenti-tabella::initUtentiEnteNoAbilitato() moonboblService.getUtentiEnteNoAbilitato() res = ' + res);
        this.utentiNoAbilitati = res;
        // this.popolaMapOperatori();
        log('utenti-tabella::initUtentiEnteNoAbilitato() moonboblService.getUtentiEnteNoAbilitato() this.utentiNoAbilitati = '
          + this.utentiNoAbilitati);

        if (!this.utentiNoAbilitati) {
          this.utentiNoAbilitati = [];
        }
        if (this.filtroFlagAbilitato === 'NO') {
          this.filtraUtenti();
          log('utenti-tabella::initUtentiEnteAbilitato() elencoUtentiFiltrato.length = ' + this.elencoUtentiFiltrato.length);
        }
        this.hideSpinner();
      },
      (err) => {
        alert(err.errorMsg);
        this.hideSpinner();
      }
    );
  }
  showSpinner() {
    log('utenti-tabella::showSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel++;
    if (this.spinnerLevel === 1) {
      log('utenti-tabella::showSpinner() spinnerService.show().');
      this.spinnerService.show();
    }
  }
  hideSpinner() {
    log('utenti-tabella::hideSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel--;
    if (this.spinnerLevel === 0) {
      log('utenti-tabella::hideSpinner() spinnerService.hide().');
      this.spinnerService.hide();
    }
  }

  utentiDaFiltrare(): UtenteEnteAbilitato[] {
    log('utenti-tabella::utentiDaFiltrare() filtroFlagAbilitato=' + this.filtroFlagAbilitato);
    if (this.filtroFlagAbilitato === 'SI') {
      return this.utentiAbilitati;
    } else {
      return this.utentiNoAbilitati;
    }
  }

  filtraUtenti() {
    log('utenti-tabella::filtraUtenti()');
    log('utenti-tabella::filtraUtenti() filtro=' + this.filtro);
    log('utenti-tabella::filtraUtenti() filtroFlagAttivo=' + this.filtroFlagAttivo);
    log('utenti-tabella::filtraUtenti() filtroFlagAbilitato=' + this.filtroFlagAbilitato);
    log('utenti-tabella::filtraUtenti() utentiDaFiltrare()= ' + this.utentiDaFiltrare());
    if (this.utentiDaFiltrare()) {
      if (this.filtro) {
        log('utenti-tabella::filtraUtenti() con filtro.');
        this.elencoUtentiFiltrato = this.utentiDaFiltrare().filter(u =>
          (u.flagAttivo === (this.filtroFlagAttivo === 'TUTTI' ? u.flagAttivo : (this.filtroFlagAttivo === 'NO' ? false : true))) &&
          (
            (u.identificativoUtente.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
            (u.cognome && u.cognome.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
            (u.nome && u.nome.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
            (u.email && u.email.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
            (u.wclKey && u.wclKey.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1)
          ));
      } else {
        log('utenti-tabella::filtraUtenti() senza filtro.');
        log('utenti-tabella::filtraUtenti() senza filtro.'
          + (this.filtroFlagAttivo === 'TUTTI' ? 'u.flagAttivo' : (this.filtroFlagAttivo === 'NO' ? 'false' : 'true')) );
        this.elencoUtentiFiltrato = this.utentiDaFiltrare().filter(u =>
          (u.flagAttivo === (this.filtroFlagAttivo === 'TUTTI' ? u.flagAttivo : (this.filtroFlagAttivo === 'NO' ? false : true))));
      }
      log('utenti-tabella::filtraUtenti() OUT elencoUtentiFiltrato = ' + this.elencoUtentiFiltrato);
      this.completaMapOperatori();
    }
  }



  ngOnChanges(changes: SimpleChanges) {
    log('utenti-tabella::ngOnChanges() changes.utenti.currentValue = ' + changes.utenti.currentValue);
    this.init();
  }

  completaMapOperatori() {
    if (this.elencoUtentiFiltrato) {
      const utentiDellaPagine = this.elencoUtentiFiltrato.slice(
        (this.currPage - 1) * this.pageSize,
        (this.currPage - 1) * this.pageSize + this.pageSize);
      const attori = utentiDellaPagine.map(u => u.attoreUpd);
      const distinctAttori = attori.filter((n, i) => attori.indexOf(n) === i);
      log('utenti-tabella::popolaMapOperatori() distinctAttori = ' + distinctAttori);
      distinctAttori.forEach(attore => {
        log('utenti-tabella::popolaMapOperatori() LOOP attore = ' + attore);
        if (!this.mapOperatori.get(attore)) {
          this.moonboblService.getUtenteByIdentificativo(attore).subscribe(
            (resOperatore: Utente) => {
              this.mapOperatori.set(attore, resOperatore);
            },
            (err: MoonboError) => {
              log('utenti-tabella::popolaMapOperatori() distinctAttori = ' + err.errorMsg);
            }
          );
        }
      });
    }
    log('utenti-tabella::popolaMapOperatori() mapOperatori = ' + this.mapOperatori);
  }

  findNomeOperatore(identificativoAttore: string): string {
    const operatore = this.mapOperatori.get(identificativoAttore);
    return operatore ? this.utentePipe.transform(operatore, 'NC') : identificativoAttore;
  }

  pageChanged(currPage: number) {
    log('utenti-tabella::pageChanged()');
    log('utenti-tabella::pageChanged() currPage = ' + currPage);
    this.sharedService.pageUtenti = currPage;
  }

  dettaglioRiga(utente: UtenteEnteAbilitato) {
    log('utenti-tabella::dettaglioRiga()');
    log('utenti-tabella::dettaglioRiga() idUtente = ' + utente.idUtente);
    this.eventDettaglio.emit(utente);
  }

  eliminaRiga(utente: UtenteEnteAbilitato) {
    log('utenti-tabella::eliminaRiga()');
    log('utenti-tabella::eliminaRiga() idUtente = ' + utente.idUtente);
    this.eventEliminaUtenteAreaRuolo.emit(utente);
    for (let i = 0; i < this.utentiAbilitati.length; i++) {
      if (this.utentiAbilitati[i].idUtente === utente.idUtente) {
        this.utentiAbilitati.splice(i, 1);
      }
    }
    this.init();
  }

  creaUtente() {
    log('utenti-tabella::creaUtente()');
    this.eventCreaUtente.emit();
  }

  changeFiltroFlagAttivo(filtroFlagAttivo: string) {
    log('utenti-tabella::changeFiltroFlagAttivo() filtroFlagAttivo = ' + filtroFlagAttivo);
    this.filtroFlagAttivo = filtroFlagAttivo;
    this.sharedService.filtroFlagAttivoUtenti = filtroFlagAttivo;
    this.filtraUtenti();
  }
  changeFiltroFlagAbilitato(filtroFlagAbilitato: string) {
    log('utenti-tabella::changeFiltroFlagAbilitato() filtroFlagAbilitato = ' + filtroFlagAbilitato);
    this.filtroFlagAbilitato = filtroFlagAbilitato;
    this.sharedService.filtroFlagAbilitatoUtenti = filtroFlagAbilitato;
    this.filtraUtenti();
  }
}

function log(a: any) {
  console.log(a);
}
