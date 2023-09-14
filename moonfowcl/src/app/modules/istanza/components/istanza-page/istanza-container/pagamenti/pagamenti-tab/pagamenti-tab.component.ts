/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, Input, OnInit} from '@angular/core';
import {Pagamento} from 'src/app/model/dto/pagamento';
import {faCopy} from '@fortawesome/free-solid-svg-icons';

@Component({
  selector: 'app-istanza-pagamenti-tab',
  templateUrl: './pagamenti-tab.component.html',
  styleUrls: ['./pagamenti-tab.component.scss']
})
export class PagamentiTabComponent implements OnInit {

  @Input('pagamenti') pagamenti: Pagamento[];

  faCopy = faCopy;

  constructor() {
  }

  ngOnInit(): void {
    log('pagamenti-tab::ngOnInit this.pagamenti=' + this.pagamenti);
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
    return p.notifica ? 'Pagato' : (p.dataAnnullamento ? 'Annullato' : 'In Attesa di pagamento');
  }

  isInPagamento(p: Pagamento) {
    return p.notifica ? false : (p.dataAnnullamento ? false : true);
  }

  copyClipboard(textToCopy: string) {
    var feedbackIcon = document.getElementById('feedbackCopyIcon');
    var copyIcon = document.getElementById('copyIcon');

    log('pagamenti-tab::copyClipboard copio data' + textToCopy);
    navigator.clipboard.writeText(textToCopy).then().catch(e => console.error(e));

    feedbackIcon.classList.remove('d-none');
    copyIcon.classList.add('d-none');

    setTimeout(function() {
      feedbackIcon.classList.add('d-none');
      copyIcon.classList.remove('d-none');

    }, 2000    )
  }

}

function log(a: any) {
  console.log(a);
}
