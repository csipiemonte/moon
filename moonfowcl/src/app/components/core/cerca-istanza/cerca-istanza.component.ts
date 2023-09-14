/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {faSearch, faUndo} from '@fortawesome/free-solid-svg-icons';
import {NgbCalendar, NgbDate, NgbDateParserFormatter, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {STORAGE_KEYS} from 'src/app/common/costanti';
import {Stato} from 'src/app/model/dto/stato';
import {environment} from 'src/environments/environment';
import {Messaggi} from '../../../common/messaggi';
import {CriterioRicercaIstanze} from '../../../model/dto/criterio-ricerca-istanze';
import {Istanza} from '../../../model/dto/istanza';
import {Modulo} from '../../../model/dto/modulo';
import {MoonfoblService} from '../../../services/moonfobl.service';
import {SecurityService} from '../../../services/security.service';
import {compareDate} from '../../../services/service.utils';
import {SharedService} from '../../../services/shared.service';
import {ModalBasicComponent} from '../../modal-basic/modal-basic.component';
import {StorageManager} from 'src/app/util/storage-manager';

@Component({
  selector: 'app-cerca-istanza',
  templateUrl: './cerca-istanza.component.html',
  styleUrls: ['./cerca-istanza.component.scss']
})
export class CercaIstanzaComponent implements OnInit {

  @Output() eseguoRicerca = new EventEmitter<boolean>();

  righeIstanza: Istanza[] = [];
  pageSize = environment.pageSize;
  currPage: number;
  istanzeTotali: number;
  criterioRicerca: CriterioRicercaIstanze;
  statiForm: Stato[] = [];
  moduli: Modulo[];
  msg: string;
  msgValidazione: string;
  initFrm = true;
  hoveredDate: NgbDate | null = null;
  isOperatore : Boolean = false;
  faSearch = faSearch;
  faUndo = faUndo;


  constructor(private moonfoblService: MoonfoblService,
              private moonservice: MoonfoblService,
              private _modalService: NgbModal,
              private calendar: NgbCalendar, public formatter: NgbDateParserFormatter,
              private sharedService: SharedService,
              private securityService: SecurityService) {

    const user = StorageManager.get(STORAGE_KEYS.USER);
    if (user != null && user.operatore){
      this.isOperatore = true;
      console.log(" USER : "+JSON.stringify(user));
    }
  }

  onDateSelection(date: NgbDate) {
    if (!this.criterioRicerca.dataDa && !this.criterioRicerca.dataA) {
      this.criterioRicerca.dataDa = date;
    } else if (this.criterioRicerca.dataDa && !this.criterioRicerca.dataA && date && date.after(this.criterioRicerca.dataDa)) {
      this.criterioRicerca.dataA = date;
    } else {
      this.criterioRicerca.dataA = null;
      this.criterioRicerca.dataDa = date;
    }
  }

  isHovered(date: NgbDate) {
    return this.criterioRicerca.dataDa && !this.criterioRicerca.dataA && this.hoveredDate && date.after(this.criterioRicerca.dataDa) && date.before(this.hoveredDate);
  }

  isInside(date: NgbDate) {
    return this.criterioRicerca.dataA && date.after(this.criterioRicerca.dataDa) && date.before(this.criterioRicerca.dataA);
  }

  isRange(date: NgbDate) {
    return date.equals(this.criterioRicerca.dataDa) || (this.criterioRicerca.dataA && date.equals(this.criterioRicerca.dataA)) || this.isInside(date) || this.isHovered(date);
  }

  validateInput(currentValue: NgbDate | null, elem: HTMLInputElement): NgbDate | null {
    const parsed = this.formatter.parse(elem.value);
    const errElem = document.getElementsByClassName(elem.name).item(0);
    if (parsed && this.calendar.isValid(NgbDate.from(parsed))) {
      elem.classList.remove('ng-invalid');
      errElem.setAttribute('style', '');
      return NgbDate.from(parsed);
    } else {
      elem.classList.add('ng-invalid');
      errElem.setAttribute('style', 'display: block');
      return currentValue;
    }
  }

  ngOnInit() {
    // fixme FZ-NAV cambio per navigazione
    // this.sharedService.nav = new Nav(NavSelection.CERCA_ISTANZA, 'home/cerca-istanza');

    this.loadStatiPresenti();
    this.getElencoModuli();
    const obj: CriterioRicercaIstanze = StorageManager.get(STORAGE_KEYS.CRITERIO_RICERCA);
    if (obj == null) {
      this.criterioRicerca = new CriterioRicercaIstanze();
      if (this.isOperatore) {
        this.criterioRicerca.dataDa = this.calendar.getNext(this.calendar.getToday(), 'd', -7);
      }
      this.criterioRicerca.dataA = this.calendar.getToday();
    } else {
      this.criterioRicerca = new CriterioRicercaIstanze();
      this.criterioRicerca.fromJson(obj);
    }
    const ris = StorageManager.get(STORAGE_KEYS.ISTANZE_SEARCH_RESULT);
    if (ris == null) {
      this.righeIstanza = [];
    } else {
      this.righeIstanza = ris;
    }
    this.istanzeTotali = StorageManager.get(STORAGE_KEYS.ISTANZE_RICERCA_TOTALI);

    if (this.sharedService.page) {
      this.currPage = this.sharedService.page;
    } else {
      this.currPage = 1;
    }
    console.log('Current Page: ' + this.currPage);
  }

  resetMsg() {
    this.msg = null;
  }

  onDeleteRiga(rigaIstanza: Istanza) {
    this.resetMsg();
    console.log('elimino riga ' + rigaIstanza.idIstanza);
    const mdRef = this._modalService.open(ModalBasicComponent);
    mdRef.componentInstance.modal_titolo = 'Richiesta conferma';
    mdRef.componentInstance.modal_contenuto = Messaggi.messaggioConfermaEliminazione;
    mdRef.componentInstance.msgContenuto = rigaIstanza.codiceIstanza;
    mdRef.result.then(
      (result) => {
        console.log(result);
        this.moonfoblService.eliminaIstanza(rigaIstanza).subscribe(
          res => {

            const idx = this.righeIstanza.indexOf(rigaIstanza);
            console.log(' Indice ' + idx);
            this.righeIstanza.splice(idx, 1);
            this.msg = 'Istanza ' + rigaIstanza.codiceIstanza + ' eliminata';
          }
        );
      },
      // conferma
      (reason) => {
        //Cancel
        this.msg = 'Operazione annullata';
      }
    );
  }

  getElencoModuli() {

    this.moonservice.getElencoModuliPerUtente().subscribe(moduli => {
      const moduliSorted = moduli.sort(function (m1, m2) {
        return (m1.codiceModulo.localeCompare(m2.codiceModulo));
      });
      // Distinct
      let distinct = [];
      let lastDistinctCodiceModulo = "";
      if (moduliSorted.length>=1) {
        distinct.push(moduliSorted[0]);
        lastDistinctCodiceModulo = moduliSorted[0].codiceModulo;
        for (var i = 0; i < moduliSorted.length; i++) {
          if (moduliSorted[i].codiceModulo !== lastDistinctCodiceModulo) {
            distinct.push(moduliSorted[i]);
            lastDistinctCodiceModulo = moduliSorted[i].codiceModulo;
          }
        }
      }
      this.moduli = distinct;
      //
      const emptyModulo = new Modulo();
      emptyModulo.idModulo = -1;
      emptyModulo.codiceModulo = 'tutti';
      this.moduli.unshift(emptyModulo);
    });
  }

  incPage() {
    this.currPage = this.currPage + 1;
    console.log('Valore dopo incremento ' + this.currPage);
  }

  pageChanged($event) {
    this.sharedService.page = $event;
    let offset = (this.currPage - 1) * this.pageSize;
    let limit = environment.pageSize;
    this.ricercaIstanze(offset, limit);
  }

  cerca() {
    this.currPage = 1;
    this.ricercaIstanze(0, environment.pageSize);
  }

  ricercaIstanze(offset: number, limit: number) {
    this.msgValidazione = null;
    this.initFrm = false;

    // resetto ricerca
    this.righeIstanza = [];

    if (this.criterioRicerca.dataA != null) {
      if (this.criterioRicerca.dataDa != null) {
        if (compareDate(this.criterioRicerca.dataA, this.criterioRicerca.dataDa)) {
          // console.log(this.criterioRicerca.startDate.before(this.criterioRicerca.endDate));
          this.msgValidazione = 'Attenzione: la data inserita nel campo "Fino al" deve essere successiva alla data inserita nel campo "A partire dal"';
          return;
        }
      } else {
        //console.log(this.criterioRicerca.startDate.before(this.criterioRicerca.endDate));
        // console.log('End date null e start not null');
      }
    }


    this.criterioRicerca.sort = '-dataCreazione';

    this.criterioRicerca.codiceFiscale  = this.criterioRicerca.codiceFiscale ? this.criterioRicerca.codiceFiscale.toUpperCase() : null;
    this.criterioRicerca.nome  = this.criterioRicerca.nome ? this.criterioRicerca.nome.trim().toUpperCase() : null;
    this.criterioRicerca.cognome  = this.criterioRicerca.cognome? this.criterioRicerca.cognome.trim().toUpperCase() : null;

    StorageManager.add(STORAGE_KEYS.CRITERIO_RICERCA, this.criterioRicerca);
    console.log('Criteria ricerca: ' + JSON.stringify(this.criterioRicerca));
    this.moonfoblService.cercaIstanzePaginato(this.criterioRicerca, offset, limit).subscribe(
      (risposta) => {
        this.istanzeTotali = risposta.totalElements;
        StorageManager.add(STORAGE_KEYS.ISTANZE_RICERCA_TOTALI, this.istanzeTotali);
        this.righeIstanza = risposta.items;
        StorageManager.add(STORAGE_KEYS.ISTANZE_SEARCH_RESULT, this.righeIstanza);
        // Segnal che è stata effettuata una ricerca
        this.eseguoRicerca.emit(true);
      });
  }

  clear() {

    this.initFrm = true;
    this.criterioRicerca = new CriterioRicercaIstanze();
    this.criterioRicerca.stato = null;
    this.criterioRicerca.dataDa = null;
    this.criterioRicerca.dataA = null;
    this.currPage = 1;
    this.istanzeTotali = 0;
    StorageManager.remove(STORAGE_KEYS.CRITERIO_RICERCA);
    StorageManager.remove(STORAGE_KEYS.ISTANZE_SEARCH_RESULT);
    StorageManager.remove(STORAGE_KEYS.ISTANZE_RICERCA_TOTALI);
    this.righeIstanza = [];
    this.eseguoRicerca.emit(false);
  }

  loadStatiPresenti() {
    this.moonfoblService.getStati().subscribe(
      (stati) => {
        this.statiForm = stati;
      });
  }

}
