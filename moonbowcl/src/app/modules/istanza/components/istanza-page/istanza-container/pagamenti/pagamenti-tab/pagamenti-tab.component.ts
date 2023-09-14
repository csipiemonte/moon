/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { VerificaPagamento } from './../../../../../../../model/dto/verifica-pagamento';
import { Component, Input, OnInit } from '@angular/core';
import { Pagamento } from 'src/app/model/dto/pagamento';
import { faCopy, faRotateLeft } from '@fortawesome/free-solid-svg-icons';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import * as vkbeautify from 'vkbeautify';

@Component({
  selector: 'app-istanza-pagamenti-tab',
  templateUrl: './pagamenti-tab.component.html',
  styleUrls: ['./pagamenti-tab.component.scss']
})
export class PagamentiTabComponent implements OnInit {

  @Input('pagamenti') pagamenti: Pagamento[];

  faCopy = faCopy;
  faRotateLeft = faRotateLeft;

  verifichePagamento: any = Object.create(null);

  constructor(private moonboblService: MoonboblService) { }

  ngOnInit(): void {
    log('pagamenti-tab::ngOnInit this.pagamenti=' + (this.pagamenti ? JSON.stringify(this.pagamenti) : null));
  }

  cfPivaPagatoreRichiesta(p: Pagamento): string {
    if (!p.richiesta) {
      return '';
    }
    const creaIuvRichiesta = JSON.parse(p.richiesta);
    return creaIuvRichiesta.codiceFiscalePartitaIVAPagatore;
  }

  importoRichiesta(p: Pagamento): string {
    if (!p.richiesta) {
      return '';
    }
    const creaIuvRichiesta = JSON.parse(p.richiesta);
    return creaIuvRichiesta.importo;
  }

  statoPagamento(p: Pagamento) {
    if (!p) {
      return '';
    }
    return p.notifica ? 'Pagato' : (p.dataAnnullamento ? 'Annullato' : 'In Attesa di pagamento');
  }
  isStatoPagato(p: Pagamento) {
    return p.notifica ? true : false;
  }
  verificaPagamento(p: Pagamento) {
    log('pagamenti-tab::verificaPagamento() this.verifichePagamento = ' + p.idEpay);
    this.moonboblService.getVerificaPagamento(p.idEpay).subscribe({
      'next': (vp) => {
        this.verifichePagamento[p.idEpay] = vp;
        log('pagamenti-tab::verificaPagamento() this.verifichePagamento = ' + this.verifichePagamento);
      },
      'error': (err) => {
        log('pagamenti-tab::verificaPagamento() err = ' + err);
        const vp = new VerificaPagamento();
        vp.codiceEsito = '400';
        vp.descrizioneEsito = 'IUV Non Pagato';
        vp.descrizioneStatoPagamento = 'Non Pagato';
        this.verifichePagamento[p.idEpay] = vp;
      },
      'complete': () => {
        log('pagamenti-tab::verificaPagamento() complete ');
      }
    });
  }

  isInPagamento(p: Pagamento) {
    return p.notifica ? false : (p.dataAnnullamento ? false : true);
  }

  copyClipboard(textToCopy: string) {
    log('pagamenti-tab::copyClipboard copio data' + textToCopy);
    navigator.clipboard.writeText(textToCopy).then().catch(e => console.error(e));
  }

  downloadRT(p: Pagamento) {
    if (!this.verifichePagamento[p.idEpay] || !this.verifichePagamento[p.idEpay].rtXml) return;
    // this.spinnerService.show();
    const rtXml = this.verifichePagamento[p.idEpay].rtXml;
    const dataStr = "data:text/json;charset=utf-8," + vkbeautify.xml(rtXml);
    let downloadAnchorNode = document.createElement('a');
    downloadAnchorNode.setAttribute("href", dataStr);
    downloadAnchorNode.setAttribute("download", p.idEpay + ".xml");
    document.body.appendChild(downloadAnchorNode); // required for firefox
    downloadAnchorNode.click();
    downloadAnchorNode.remove();

    // setTimeout(() => {
    //   this.spinnerService.hide();
    // }, 100);
  }

}

function log(a: any) {
  console.log(a);
}
