/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { Istanza } from 'src/app/model/dto/istanza';
import { faCopy } from '@fortawesome/free-solid-svg-icons';
import { AlertService } from 'src/app/modules/alert/alert.service';

@Component({
  selector: 'app-istanza-pagamenti',
  templateUrl: './pagamenti.component.html',
  styleUrls: ['./pagamenti.component.scss']
})
export class PagamentiComponent implements OnInit {

  @Input('istanza') istanza: Istanza;
  @Output('onAnnullaPagamento') pagamentiAnnullaEvent = new EventEmitter();

  faCopy = faCopy;
  // tslint:disable:max-line-length
  // msgInAttesaDiPagamento = 'In attesa di pagamaneto. La notifica di pagamento arriva anche dopo diversi minuti.';
  msgInAttesaDiPagamentoInvia = 'Nel caso avessi effettuato il pagamento con Successo (con ricevuta o email con esito \'Pagamento eseguito\'), puoi prosseguire l\'<b>Invio</b> dell\'istanza senza aspettare ulterioramente.';
  msgInAttesaDiPagamentoAnnulla = 'Nel caso avessi iniziato una procedura di pagamento non andata a buon fine (o ricevuto un email con esito \'Pagamento non eseguito\'), lo IUV/Codice avviso risulterebbe già utilizzato. Puoi annullare la richiesta di pagamento e riprovare in seguito la gestione del pagamento (questo genererà un nuovo IUV / Codice avviso) con <a (click)="annullaPagamento()">Annulla Pagamento</a>.';
  //msgPagatoDeviInviare = 'Pagamento eseguito con Successo, DEVI ancora prosseguire con l\'<b>Invio</b> dell\'istanza.';
  // tslint:enable:max-line-length

  alertOptionsContainer = {
    id: 'alert-istanza-container',
    autoClose: false,
    showCloseButton: false,
    keepAfterRouteChange: true
  };
  alertOptionsPagamenti = {
    id: 'alert-istanza-pagamenti',
    autoClose: false,
    keepAfterRouteChange: true
  };

  constructor(
    private alertService: AlertService) {
  }

  ngOnInit(): void {
    log('pagamenti::ngOnInit this.istanza=' + this.istanza);
    // if (this.istanza?.stato?.nome === 'IN PAGAMENTO' && !this.istanza?.dataEsitoPagamento) {
    //   log('pagamenti::ngOnInit IN ATTESA DI PAGAMENTO');
    //   // this.alertService.info(this.msgInAttesaDiPagamento, this.alertOptionsPagamenti);
    //   this.alertService.info(this.msgInAttesaDiPagamento, this.alertOptionsContainer);

    //   // this.alertService.warn(this.msgInAttesaDiPagamentoInvia, this.alertOptionsPagamenti);
    //   // this.alertService.warn(this.msgInAttesaDiPagamentoAnnulla, this.alertOptionsPagamenti);
    // }
    // if (this.istanza?.stato?.nome === 'IN PAGAMENTO' && this.istanza?.dataEsitoPagamento) {
    //   log('pagamenti::ngOnInit ALERT DEVI INVIARE');
    //   // this.alertService.success(this.msgPagatoDeviInviare, this.alertOptionsPagamenti);
    //   this.alertService.success(this.msgPagatoDeviInviare, this.alertOptionsContainer);
    // }
  }

  copyClipboard(textToCopy: string) {
    log('pagamenti::copyClipboard copio data' + textToCopy);
    navigator.clipboard.writeText(textToCopy).then().catch(e => console.error(e));
    // this.clipboard.writeText(textToCopy).then((() => console.log('testo copiato')));
  }

  annullaPagamento() {
    log('pagamenti::annullaPagamento BEGIN');
    this.pagamentiAnnullaEvent.emit();
  }
}

function log(a: any) {
//  console.log(a);
}
