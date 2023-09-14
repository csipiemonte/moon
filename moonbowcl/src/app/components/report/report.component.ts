/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit } from '@angular/core';
import { ReportService } from '../../services/report.service';
import { faSyncAlt } from '@fortawesome/free-solid-svg-icons';
import {Modulo} from '../../model/dto/modulo';
import {MoonboblService} from '../../services/moonbobl.service';
import {formatDate } from '@angular/common';
import {saveBlobIE} from '../../services/service.utils';
import { STORAGE_KEYS } from 'src/app/common/costanti';

@Component({
  selector: 'app-report',
  templateUrl: './report.component.html',
  styleUrls: ['./report.component.scss']
})

export class ReportComponent implements OnInit {
  faSyncAlt = faSyncAlt;
  selectedModulo: number;
  moduli: Modulo[];
  numComuni: number;
  numModuliInviati: number;
  numServizi02: number;
  totfreq02: number;
  numServizi36: number;
  totfreq36: number;
  today: Date;

  constructor(private reportService: ReportService,
    private moonboblService: MoonboblService) { }

  ngOnInit() {
    this.getElencoModuli();

    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZ_REPORT));

    if (moduloSelezionato && moduloSelezionato.idModulo) {
      this.selectedModulo = Number(moduloSelezionato.idModulo);
    }

  }

  selectModulo(id) {
    this.selectedModulo = id; // this.selectedOption;

    const datiAtt = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODATTR + id));

    localStorage.setItem(STORAGE_KEYS.MODULO_SELEZ_REPORT, JSON.stringify({ idModulo: id  }));
  }

  private getElencoModuli(): void {
    this.moonboblService.getElencoModuli(true).subscribe(
        (moduli) => {
        
        this.moduli = moduli;
        if (this.moduli.length === 1) {
          this.selectedModulo = this.moduli[0].idModulo;
          this.selectModulo(this.selectedModulo);
        } else {
          const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZ_REPORT));
          if (moduloSelezionato) {
            this.selectedModulo = moduloSelezionato.idModulo;
          }
        }
      }
    );

  }

  downloadExcel()  {
    const moduloSelezionato = JSON.parse(localStorage.getItem(STORAGE_KEYS.MODULO_SELEZ_REPORT));
    this.reportService.getReport(moduloSelezionato.idModulo)
        .subscribe(x => {
            // It is necessary to create a new blob object with mime-type explicitly set
            // otherwise only Chrome works like it should
            var newBlob = new Blob([x], { type: 'application/vnd.ms-excel' });
            saveBlobIE(newBlob);
            // IE doesn't allow using a blob object directly as link href
            // instead it is necessary to use msSaveOrOpenBlob
            /*if (window.navigator && window.navigator.msSaveOrOpenBlob) {
                window.navigator.msSaveOrOpenBlob(newBlob);
                return;
            }*/

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
        });
  }

  aggiornatutto() {
    this.reportService.getReporNumModuliInviati().then(valore => this.numModuliInviati = valore);
    this.reportService.getReportNumComuni().then(valore => this.numComuni = valore);
    this.reportService.getReporNumServizi02().then(valore => this.numServizi02 = valore);
    this.reportService.getReporTotFreq02().then(valore => this.totfreq02 = valore);
    this.reportService.getReporNumServizi36().then(valore => this.numServizi36 = valore);
    this.reportService.getReporTotFreq36().then(valore => this.totfreq36 = valore);
  }

  aggiornaNumModuliInviati() {
    console.log('Aggiorno numero moduli inviati');
    this.reportService.getReporNumModuliInviati().then(valore => this.numModuliInviati = valore);
  }

  aggiornaComuni() {
    this.reportService.getReportNumComuni().then(valore => this.numComuni = valore);
  }

  aggiornaNumServizi02() {
    this.reportService.getReporNumServizi02().then(valore => this.numServizi02 = valore);
  }

  aggiornaTotFreq02() {
    this.reportService.getReporTotFreq02().then(valore => this.totfreq02 = valore);
  }

  aggiornaNumServizi36() {
    this.reportService.getReporNumServizi36().then(valore => this.numServizi36 = valore);
  }

  aggiornaTotFreq36() {
    this.reportService.getReporTotFreq36().then(valore => this.totfreq36 = valore);
  }
}
