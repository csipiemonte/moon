/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { LogMyDocs } from 'src/app/model/dto/log-mydocs';
import { faArrowRight } from '@fortawesome/free-solid-svg-icons';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalNotifyComponent } from 'src/app/components/modal/modal-notify/modal-notify.component';
import { TIPO_DOC, DESC_TIPO_DOC } from 'src/app/model/common/documento';



@Component({
  selector: 'app-istanza-log-mydocs',
  templateUrl: './log-mydocs.component.html',
  styleUrls: ['./log-mydocs.component.scss']
})
export class LogMydocsComponent implements OnInit {
  
  @Input('data') data: LogMyDocs[];
  @Output('initData') initData = new EventEmitter();

  faArrowRight = faArrowRight;

  isAdmin = false;

  TIPO_DOC = TIPO_DOC;

  constructor(private sharedService: SharedService,private moonboblService: MoonboblService,
    private modalService: NgbModal) {
    this.isAdmin = sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
  }

  reinviaFile(idFile:string, idRichiesta){
    console.log(`*** retryPubblicazioneFileMyDocs idFile: ${idFile} idRichiesta ${idRichiesta}`);
    this.moonboblService.retryPubblicazioneFileMyDocs(idFile,idRichiesta).subscribe(
          response => {
            console.log(`*** retryPubblicazioneFileMyDocs response: ${response}`);
            this.notify('Reinvio file con esito positivo', function () { this.initData.emit();}.bind(this));           
          } ,
          error => {        
           console.log(`*** retryPubblicazioneFileMyDocs error: ${error}`);
           this.notify('Reinvio file con esito negativo', function () { this.initData.emit();}.bind(this));
          }
        );
  }


  reinviaIstanza(idIstanza, idRichiesta){
    console.log(`*** retryPubblicazioneIstanzaMyDocs idIstanza: ${idIstanza} idRichiesta ${idRichiesta}`);
    this.moonboblService.retryPubblicazioneIstanzaMyDocs(idIstanza,idRichiesta).subscribe(
          response => {
            console.log(`*** retryPubblicazioneIstanzaMyDocs response: ${response}`);
            this.notify('Reinvio pdf istanza con esito positivo', function () { this.initData.emit();}.bind(this));           
          } ,
          error => {        
           console.log(`*** retryPubblicazioneIstanzaMyDocs error: ${error}`);
           this.notify('Reinvio pdf istanza con esito negativo', function () { this.initData.emit();}.bind(this));
          }
        );
  }

  notify(contenuto: string, doAction: () => void) {
    const mdRef = this.modalService.open(ModalNotifyComponent);
    mdRef.componentInstance.modal_titolo = 'Pubblicazione';
    mdRef.componentInstance.modal_contenuto = contenuto;
    mdRef.result.then((result) => {
      console.log('Closed with: ${result}' + result);
      doAction();
    }, (reason) => {
      console.log(reason);
      doAction();
    });
  }

  getDescTipoDoc(log:LogMyDocs){
    let desc = log.tipoDoc === TIPO_DOC.ISTANZA ? DESC_TIPO_DOC.ISTANZA: DESC_TIPO_DOC.DOCUMENTO_PA;    
    return desc;    
  }

}
