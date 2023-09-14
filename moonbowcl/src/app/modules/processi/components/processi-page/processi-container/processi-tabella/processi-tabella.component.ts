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
import { UtentiFilterPipe } from 'src/app/pipes/utenti-filter-pipe';
import { UtentePipe } from 'src/app/pipes/utente-pipe';
import { faHome, faSave, faPlus } from '@fortawesome/free-solid-svg-icons';
import { UserInfo } from 'src/app/model/common/user-info';
import { FormControl, FormGroup } from '@angular/forms';
import { Processo } from 'src/app/model/dto/processo';
import { ProcessiView, ProcessiViewEvent } from 'src/app/modules/processi/processi.model';


@Component({
  selector: 'app-processi-tabella',
  templateUrl: './processi-tabella.component.html',
  styleUrls: ['./processi-tabella.component.scss']
})
export class ProcessiTabellaComponent implements OnInit {

  @Output() eventCreaProcesso = new EventEmitter();
  @Output() eventDettaglio = new EventEmitter<Processo>();
  @Output() eventEliminaProcesso = new EventEmitter<Processo>();

  currentUser: UserInfo;
  isAdmin = false;

  processi: Processo[];
  elencoProcessiFiltrato: Processo[];

  private _filtro: string;
  get filtro(): string {
    return this._filtro;
  }
  set filtro(value: string) {
    this._filtro = value;
    this.sharedService.filtroProcessi = value;
    this.filtraProcessi();
  }

  pageSize = environment.pageSize;
  currPage = 1;

  faPlus = faPlus;
  mapOperatori: Map<string, Utente> = new Map<string, Utente>();
  spinnerLevel = 0;

  constructor(
    private moonboblService: MoonboblService,
    private spinnerService: NgxSpinnerService,
    private sharedService: SharedService,
    protected alertService: AlertService,
    private utentiFilterPipe: UtentiFilterPipe,
    private utentePipe: UtentePipe) {
    log('[app-processi-tabella::constructor]');
    this.currentUser = this.sharedService.UserLogged;
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
    this.currPage = this.sharedService.pageProcessi ? this.sharedService.pageProcessi : 1;
    this._filtro = this.sharedService.filtroProcessi ? this.sharedService.filtroProcessi : null;
  }

  ngOnInit(): void {
    log('processi-tabella::ngOnInit()...');
    log('processi-tabella::ngOnInit() processi = ' + this.processi); // valorizzato in seguito con ngOnChanges()
    this.currPage = this.sharedService.pageProcessi ? this.sharedService.pageProcessi : 1;
    log('processi-tabella::ngOnInit() currPage = ' + this.currPage);
    log('processi-tabella::ngOnInit() pageSize=' + this.pageSize);
    this._filtro = this.sharedService.filtroProcessi ? this.sharedService.filtroProcessi : null;
    log('processi-tabella::ngOnInit() _filtro = ' + this._filtro);
    this.init();
  }

  init(): void {
    log('processi-tabella::init()');
    this.initProcessi();
  }

  initProcessi() {
    log('processi-tabella::initProcessi()');
    this.showSpinner();
    this.moonboblService.getElencoProcessi().subscribe({
      'next': (res) => {
        log('processi-tabella::initProcessi() moonboblService.getElencoProcessi() res = ' + res);
        this.processi = res;
        this.filtraProcessi();
        this.hideSpinner();
      },
      'error': (err) => {
        alert(err.errorMsg);
        this.hideSpinner();
      }
    });
  }

  showSpinner() {
    log('processi-tabella::showSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel++;
    if (this.spinnerLevel === 1) {
      log('processi-tabella::showSpinner() spinnerService.show().');
      this.spinnerService.show();
    }
  }
  hideSpinner() {
    log('processi-tabella::hideSpinner() IN spinnerLevel = ' + this.spinnerLevel);
    this.spinnerLevel--;
    if (this.spinnerLevel === 0) {
      log('processi-tabella::hideSpinner() spinnerService.hide().');
      this.spinnerService.hide();
    }
  }

  processiDaFiltrare(): Processo[] {
    log('processi-tabella::processiDaFiltrare() ...');
    return this.processi;
  }

  filtraProcessi() {
    log('processi-tabella::filtraProcessi()');
    log('processi-tabella::filtraProcessi() filtro=' + this.filtro);
    log('processi-tabella::filtraProcessi() processiDaFiltrare()= ' + this.processiDaFiltrare());
    if (this.processiDaFiltrare()) {
      if (this.filtro) {
        log('processi-tabella::filtraProcessi() con filtro.');
        this.elencoProcessiFiltrato = this.processiDaFiltrare().filter(p =>
          (
            (p.codiceProcesso.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
            (p.nomeProcesso && p.nomeProcesso.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1) ||
          (p.descProcesso && p.descProcesso.toLowerCase().indexOf(this.filtro.toLowerCase()) !== -1)
          ));
      } else {
        log('processi-tabella::filtraProcessi() senza filtro.');
        this.elencoProcessiFiltrato = this.processiDaFiltrare();
      }
      log('processi-tabella::filtraProcessi() OUT elencoProcessiFiltrato = ' + this.elencoProcessiFiltrato);
      this.completaMapOperatori();
    }
  }

  ngOnChanges(changes: SimpleChanges) {
    log('processi-tabella::ngOnChanges() changes.processi.currentValue = ' + changes.processi.currentValue);
    this.init();
  }

  completaMapOperatori() {
    if (this.elencoProcessiFiltrato) {
      const processiDellaPagine = this.elencoProcessiFiltrato.slice(
        (this.currPage - 1) * this.pageSize,
        (this.currPage - 1) * this.pageSize + this.pageSize);
      const attori = processiDellaPagine.map(u => u.attoreUpd);
      const distinctAttori = attori.filter((n, i) => attori.indexOf(n) === i);
      log('processi-tabella::popolaMapOperatori() distinctAttori = ' + distinctAttori);
      distinctAttori.forEach(attore => {
        log('processi-tabella::popolaMapOperatori() LOOP attore = ' + attore);
        if (!this.mapOperatori.get(attore)) {
          this.moonboblService.getUtenteByIdentificativo(attore).subscribe({
            'next': (resOperatore: Utente) => {
              this.mapOperatori.set(attore, resOperatore);
            },
            'error': (err) => {
              log('processi-tabella::popolaMapOperatori() distinctAttori = ' + err.errorMsg);
            }
          });
        }
      });
    }
    log('processi-tabella::popolaMapOperatori() mapOperatori = ' + this.mapOperatori);
  }

  findNomeOperatore(identificativoAttore: string): string {
    const operatore = this.mapOperatori.get(identificativoAttore);
    return operatore ? this.utentePipe.transform(operatore, 'NC') : identificativoAttore;
  }

  pageChanged(currPage: number) {
    log('processi-tabella::pageChanged()');
    log('processi-tabella::pageChanged() currPage = ' + currPage);
    this.sharedService.pageProcessi = currPage;
  }

  dettaglioRiga(processo: Processo) {
    log('processi-tabella::dettaglioRiga()');
    log('processi-tabella::dettaglioRiga() idProcesso = ' + processo.idProcesso);
    this.eventDettaglio.emit(processo);
  }

  eliminaRiga(processo: Processo) {
    log('processi-tabella::eliminaRiga()');
    log('processi-tabella::eliminaRiga() idProcesso = ' + processo.idProcesso);
    this.eventEliminaProcesso.emit(processo);
    for (let i = 0; i < this.processi.length; i++) {
      if (this.processi[i].idProcesso === processo.idProcesso) {
        this.processi.splice(i, 1);
      }
    }
    this.init();
  }

  creaProcesso() {
    log('processi-tabella::creaProcesso()');
    this.eventCreaProcesso.emit();
  }

}

function log(a: any) {
  console.log(a);
}
