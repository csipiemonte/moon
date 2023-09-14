/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {Component, EventEmitter, Input, OnInit, OnChanges, Output, SimpleChanges} from '@angular/core';
import {Istanza} from 'src/app/model/dto/istanza';
import {faCopy} from '@fortawesome/free-solid-svg-icons';
import {Workflow} from 'src/app/model/dto/workflow';
import {initFormioPlugin} from 'src/app/app.module';

@Component({
  selector: 'app-istanza-pagamenti',
  templateUrl: './pagamenti.component.html',
  styleUrls: ['./pagamenti.component.scss']
})
export class PagamentiComponent implements OnInit {

  @Input('istanza') istanza: Istanza;
  @Input('nextWorkflow') nextWorkflow: Workflow[];
  @Output('onAnnullaPagamento') pagamentiAnnullaEvent = new EventEmitter();

  isNextWorkflowWithINVIA = false;

  faCopy = faCopy;
  // tslint:disable:max-line-length
  // msgInAttesaDiPagamento = 'In attesa di pagamaneto. La notifica di pagamento arriva anche dopo diversi minuti.';
  msgInAttesaDiPagamentoInvia = 'Nel caso avessi effettuato il pagamento con Successo (con ricevuta o email con esito \'Pagamento eseguito\'), puoi proseguire l\'<b>Invio</b> dell\'istanza senza aspettare ulterioramente.';
  msgInAttesaDiPagamentoAnnulla = 'Nel caso avessi iniziato una procedura di pagamento non andata a buon fine (o ricevuto un email con esito \'Pagamento non eseguito\'), lo IUV/Codice avviso risulterebbe già utilizzato. Puoi annullare la richiesta di pagamento e riprovare in seguito la gestione del pagamento (questo genererà un nuovo IUV / Codice avviso) con <a (click)="annullaPagamento()">Annulla Pagamento</a>.';
  //msgPagatoDeviInviare = 'Pagamento eseguito con Successo, DEVI ancora prosseguire con l\'<b>Invio</b> dell\'istanza.';
  // tslint:enable:max-line-length

  constructor() {
  }

  ngOnInit(): void {
    log('pagamenti::ngOnInit this.istanza=' + this.istanza);
    this.init();
  }

  ngOnChanges(changes: SimpleChanges) {
    log('pagamenti::ngOnChanges this.istanza=' + this.istanza);
    this.init();
  }

  init() {
    log('pagamenti::init this.istanza=' + this.istanza + ' this.nextWorkflow=' + this.nextWorkflow);
    this.isNextWorkflowWithINVIA = (this.nextWorkflow && this.nextWorkflow.filter(w => w.codiceAzione === 'INVIA').length > 0);
    log('pagamenti::init this.isNextWorkflowWithINVIA=' + this.isNextWorkflowWithINVIA);
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
  console.log(a);
}
