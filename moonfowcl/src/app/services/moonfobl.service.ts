/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import {HttpParams} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Observable} from 'rxjs';
import {ErrorRest} from 'src/app/common/messaggi';
import {ModalAlertComponent} from '../components/modal-alert/modal-alert.component';
import {Allegato} from '../model/dto/allegato';
import {CriterioRicercaIstanze} from '../model/dto/criterio-ricerca-istanze';
import {DatiPagamento} from '../model/dto/dati-pagamento';
import {Documento} from '../model/dto/documento';
import {Ente} from '../model/dto/ente';
import {FiltroModuli} from '../model/dto/filtro-moduli';
import {Istanza} from '../model/dto/istanza';
import {IstanzaSaveResponse} from '../model/dto/istanza-save-response';
import {MessaggioBacheca} from '../model/dto/messaggioBacheca';
import {Modulo} from '../model/dto/modulo';
import {Stato} from '../model/dto/stato';
import {StoricoWorkflow} from '../model/dto/storicoWorkflow';
import {Workflow} from '../model/dto/workflow';
import {empty} from '../services/service.utils';
import {ConfigService} from './config.service';
import {GenericHttpService} from './generic-http.service';
import {ValutazioneModulo} from '../model/dto/valutazione-modulo';
import { ValutazioneModuloCitFac } from '../model/dto/valutazione-modulo-citfac';

@Injectable()
export class MoonfoblService {

  constructor(
    private config: ConfigService,
    private router: Router,
    private modalService: NgbModal,
    private genericHttpService: GenericHttpService
  ) {
  }

  getElencoModuli(): Observable<Modulo[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/moduli';
    return this.genericHttpService.httpGet<Modulo[]>(url);
  }

  getModuloFiltrato(filtro: FiltroModuli): Observable<Modulo[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/moduli';
    let parametri = new HttpParams();
    if (filtro.idModulo) {
      parametri = parametri.append('idModulo', '' + filtro.idModulo);
    }
    if (filtro.codiceModulo) {
      parametri = parametri.append('codiceModulo', filtro.codiceModulo);
    }
    return this.genericHttpService.httpGet<Modulo[]>(url, { params: parametri });
  }

  getModuloWithFields(idModulo, idVersione, fields): Observable<Modulo> {
    const params = new HttpParams().set('fields', fields);
    const url: string = this.config.getBEServer() + `/moonfobl/restfacade/be/moduli/${idModulo}/v/${idVersione}`;
    return this.genericHttpService.httpGet<Modulo>(url, { params });
  }

  getElencoModuliPerUtente(): Observable<Modulo[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/moduli';
    let parametri = new HttpParams();
    parametri = parametri.append('conPresenzaIstanze', 'true');
    return this.genericHttpService.httpGet<Modulo[]>(url, { params: parametri });
  }

  getModulo(idModulo, idVersioneModulo) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/moduli/' + idModulo + '/v/' + idVersioneModulo;
    return this.genericHttpService.httpGet(url);
  }

  cercaIstanzePaginato(criterioRicerca: CriterioRicercaIstanze, offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/paginate';
    let parametri = new HttpParams();

    if (criterioRicerca.statiIstanza) {
      for (const st of criterioRicerca.statiIstanza) {
        parametri = parametri.append('statiIstanza', '' + st);
      }
    } else {
      if (criterioRicerca.stato != null && criterioRicerca.stato !== -1) {
        parametri = parametri.append('stato', '' + criterioRicerca.stato);
      }
    }

    if (criterioRicerca.idTabFo) {
      parametri = parametri.append('idTabFo', '' + criterioRicerca.idTabFo);
    }

    if (!empty(criterioRicerca.codiceIstanza)) {
      parametri = parametri.append('codiceIstanza', '' + criterioRicerca.codiceIstanza);
    }

    if (!empty(criterioRicerca.titoloModulo)) {
      parametri = parametri.append('titoloModulo', '' + criterioRicerca.titoloModulo);
    }
    if (criterioRicerca.idModulo != null && criterioRicerca.idModulo !== -1) {
      parametri = parametri.append('idModulo', '' + criterioRicerca.idModulo);
    }
    if (criterioRicerca.sort != null) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }

    if (criterioRicerca.dataDa != null) {
      const s = '' + criterioRicerca.dataDa.year + '/'
        + (criterioRicerca.dataDa.month) + '/'
        + criterioRicerca.dataDa.day;
      parametri = parametri.append('created_start', s);
    }

    if (criterioRicerca.dataA != null) {
      const s = '' + criterioRicerca.dataA.year + '/'
        + (criterioRicerca.dataA.month) + '/'
        + criterioRicerca.dataA.day;
      parametri = parametri.append('created_end', s);
    }
    if (criterioRicerca.nome != null) {
      parametri = parametri.append('nome', '' + criterioRicerca.nome);
    }
    if (criterioRicerca.cognome != null) {
      parametri = parametri.append('cognome', '' + criterioRicerca.cognome);
    }
    if (criterioRicerca.codiceFiscale != null) {
      parametri = parametri.append('codiceFiscale', '' + criterioRicerca.codiceFiscale);
    }

    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.genericHttpService.httpGet<any>(url, { params: parametri });

  }

  getElencoIstanzeFiltratoEOrdinato(stato: number, statiIstanza: string[],idTabFo: number, importanza: number, sort: string) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/';
    let parametri = new HttpParams();

    if (stato) {
      parametri = parametri.append('stato', '' + stato);
    }

    if (statiIstanza) {
      for (const st of statiIstanza) {
        parametri = parametri.append('statiIstanza', '' + st);
      }
    }
    if (idTabFo) {
      parametri = parametri.append('idTabFo', '' + idTabFo);
    }

    if (importanza) {
      parametri = parametri.append('importanza', '' + importanza);
    }

    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }

    return this.genericHttpService.httpGet<Array<Istanza>>(url, { params: parametri });
  }

  getIstanza(idIstanza): Observable<Istanza> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza;
    return this.genericHttpService.httpGet<Istanza>(url);

  }

  getIstanzaByCodice(codiceIstanza): Observable<Istanza> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/codice/' + codiceIstanza;
    return this.genericHttpService.httpGet<Istanza>(url);

  }

  salvaIstanza(data: any, idModulo: number,
               idVersioneModulo: number, codiceModulo: string,
               idIstanza: number, codiceStato: number, currentStep, sendMessageError?: boolean) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/';
    const payload = {
      'idIstanza': idIstanza,
      // 'idIstanza': 10000,
      'stato': { 'idStato': codiceStato },
      'data': JSON.stringify(data),
      'modulo': { 'idModulo': idModulo, 'idVersioneModulo': idVersioneModulo, 'codiceModulo': codiceModulo },
      'currentStep': currentStep
    };
    return this.genericHttpService.httpPost(url, payload, sendMessageError);
  }

  getDatiInizializzazioneModulo(idModulo: number, idVersioneModulo: number, datiInit: any) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/init/' + idModulo + '/v/' + idVersioneModulo;
    return this.genericHttpService.httpPost(url, datiInit, {}, false);
  }

  cambiaImportanzaIstanza(idIstanza: string, value: number) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza;
    const payload = {
      'importanza': value
    };
    const options = {};
    return this.genericHttpService.httpPatch(url, payload, options);
  }

  eliminaIstanza(istanza: Istanza) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + istanza.idIstanza;
    const options = {};
    return this.genericHttpService.httpDelete(url, options);
  }

  riportaIstanzaInBozza(istanza: Istanza) {
    console.log('START riporto in bozza ' + istanza.idIstanza);
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + istanza.idIstanza + '/riportaInBozza';
    const options = {};
    return this.genericHttpService.httpPatch(url, options);
  }

  inviaIstanza(istanza: Istanza) {
    console.log('START invia istanza' + istanza.idIstanza);
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + istanza.idIstanza + '/invia';
    const options = {};
    return this.genericHttpService.httpPatch(url, options);
  }

  public getPdf(idIstanza: string): Observable<Blob> {
    const url = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/pdf';
    return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  }

  // public getNotifica(idIstanza: string): Observable<Blob> {
  //   const url = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/notifica';
  //   return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  // }

  // public getDocumentoNotifica(idIstanza: string): Observable<Documento> {
  //   const url = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/documento-notifica';
  //   return this.genericHttpService.httpGet<Documento>(url);
  // }

  // public getNotificaByFormioNameFile(formioNameFile: string): Observable<Blob> {
  //   const url = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/documento-by-name/' + formioNameFile;
  //   return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  // }

  public getNotificaByIdFile(idFile: number): Observable<Blob> {
    const url = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/documento-by-id/' + idFile;
    return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  }

  getWorkflowByIdWorkflow(idWorkflow): Observable<Workflow> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/' + idWorkflow;
    return this.genericHttpService.httpGet(url);
  }

  getStrutturaAzione(idIstanza, idWorkflow): Observable<any> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/struttura/' + idWorkflow;
    return this.genericHttpService.httpGet(url);
  }

  getDatiInizializzaDatiAzione(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/init/' + idWorkflow;
    return this.genericHttpService.httpGet(url);
  }

  completaAzione(data: any, idWorkflow, idIstanza) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/completaAzione';
    const payload = {
      'idWorkflow': idWorkflow,
      'datiAzione': JSON.stringify(data),
      'idIstanza': idIstanza
    };
    return this.genericHttpService.httpPost<any, any>(url, payload);
  }

  doAzione(idIstanza, idAzione): Observable<IstanzaSaveResponse> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/compieAzione/' + idAzione;
    // return this.http.post<IstanzaSaveResponse>(url, null);
    return this.genericHttpService.httpPost<any, IstanzaSaveResponse>(url, null);
  }

  annullaAzione(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/annullaAzione';
    const payload = {
      'idWorkflow': idWorkflow
    };
    return this.genericHttpService.httpPost<any, Istanza>(url, payload);
  }

  getModuloAttributo(m: Modulo, attr: string): string {
    const attributi = m['attributi'];
    if (attributi) {
      for (let i = 0; i < attributi.length; i++) {
        if (attributi[i]['nome'] === attr) {
          return attributi[i]['valore'];
        }
      }
    } else {
      return null;
    }
  }

  getInfoStoricoWorkflow(idStoricoWorkflow): Observable<StoricoWorkflow> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/storico/' + idStoricoWorkflow;
    return this.genericHttpService.httpGet(url);
  }

  geStoricoWorkflow(idIstanza): Observable<StoricoWorkflow[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/storico/';
    return this.genericHttpService.httpGet(url);
  }

  //invocato nel componente istanza-container con inibizione dell'alert di default per la lettura delle istanze in bozza
  getWorkflows(idIstanza): Observable<Workflow[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza;
    return this.genericHttpService.httpGet(url, {}, false);
  }

  //invocato nel componente istanza-container con inibizione dell'alert di default per la lettura delle istanze in bozza
  getPrevWorkflow(idIstanza): Observable<Workflow> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/workflows/istanza/' + idIstanza + '/ultimo';
    return this.genericHttpService.httpGet(url, {}, false);
  }

  getAllegati(idIstanza: number): Observable<Allegato[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/allegati';
    console.log('url elenco allegati per istanza: ' + url);
    return this.genericHttpService.httpGet(url);

  }

  // getDocumenti(idIstanza: number): Observable<Documento[]> {
  //   const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/documenti';
  //   console.log('url elenco documenti per istanza: ' + url);
  //   return this.genericHttpService.httpGet(url);
  // }

  getDocumentiProtocollati(idIstanza: number): Observable<Documento[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/documenti-protocollati';
    console.log('url elenco documenti per istanza: ' + url);
    return this.genericHttpService.httpGet(url);
  }

  getDocumentiEmessiDaUfficio(idIstanza: number): Observable<Documento[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/documenti-emessi-ufficio';
    console.log('url elenco documenti emessi da ufficio per istanza: ' + url);
    return this.genericHttpService.httpGet(url);
  }

  getAllegato(formioName: string): Observable<Blob> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/allegato/' + formioName;
    return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  }

  public getRicevuta(idFile): Observable<Blob> {
    const url = this.config.getBEServer() + '/moonfobl/restfacade/be/repository/file/' + idFile;
    return this.genericHttpService.httpGet(url, { responseType: 'blob' });
  }

  getElencoEnti(): Observable<Ente[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/enti';
    console.log(' *** url call get elenco enti *** '+url)
    return this.genericHttpService.httpGet<Ente[]>(url);
  }

  aggiornaEnte(ente: Ente) {
    console.log('aggiorna ente id ente: ' + ente.idEnte);
    const payload = {
      'ente': ente
    };
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/currentUser';
    return this.genericHttpService.httpPatch<any, any>(url, payload, { observe: 'response' });
  }

  getEnte(codiceEnte: string): Observable<Ente> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/enti/codice/' + codiceEnte;
    return this.genericHttpService.httpGet<Ente>(url);
  }

  getMessaggiBacheca(): Observable<MessaggioBacheca[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/bacheca/messaggi';
    return this.genericHttpService.httpGet<MessaggioBacheca[]>(url);
  }

  getStati(): Observable<Stato[]> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/stati';
    let parametri = new HttpParams();
    parametri = parametri.append('conPresenzaIstanze', 'true');
    return this.genericHttpService.httpGet<Stato[]>(url, { params: parametri });
  }

  pagamentoSportello(datiPagamento: DatiPagamento) {
    const urlSportello = this.config.getUrlSportello();
    console.log(urlSportello);

    const mapForm = document.createElement('form');
    mapForm.target = '_blank';
    mapForm.method = 'POST';
    mapForm.action = urlSportello;

    document.body.appendChild(mapForm);
    mapForm.submit();

  }

  postUrl(postUrl) {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be' + postUrl;
    return this.genericHttpService.httpPost(url, null);
  }

  postDuplicaIstanza(idIstanza): Observable<IstanzaSaveResponse> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/istanze/' + idIstanza + '/duplica';
    // return this.http.post<IstanzaSaveResponse>(url, null);
    return this.genericHttpService.httpPost<null, IstanzaSaveResponse>(url, null);
  }

  inserisciValutazione(valutazione: ValutazioneModulo): Observable<IstanzaSaveResponse> {
    const url: string = this.config.getBEServer() + '/moonfobl/restfacade/be/valutazioni';
    // return this.http.post<IstanzaSaveResponse>(url, null);
    return this.genericHttpService.httpPost(url, valutazione);
  }

  confermaDuplicaIstanza(idIstanza: string) {
    var msg = '';

    this.postDuplicaIstanza(idIstanza).subscribe(
      res => {

        const mdRef = this.modalService.open(ModalAlertComponent);
        mdRef.componentInstance.modal_titolo = 'Avviso';
        mdRef.componentInstance.modal_contenuto = 'L\'istanza e\' stata duplicata con successo!';
        this.router.navigate(['/home/istanza/' + res.istanza.idIstanza]);
      },
      (err: ErrorRest) => {

        console.log('ERR: ' + JSON.stringify(err));
        if (err.code === 'MOONFOBL-10021') {
          msg = 'Impossibile duplicare l\'istanza, deve essere nello stato INVIATA!';
        } else if (err.code === 'MOONFOBL-10022') {
          msg = 'Impossibile duplicare l\'istanza. Modulo non trovato o non PUBBLICATO!';
        } else {
          msg = 'Impossibile duplicare l\'istanza!';
        }
        const mdRef = this.modalService.open(ModalAlertComponent);
        mdRef.componentInstance.modal_titolo = 'Errore';
        mdRef.componentInstance.modal_contenuto = msg;
      }
    );
  }

  getParametriConfigurazione(consumer: string, codiceEnte: string): Observable<any> {
    const url: string = this.config.getBEServer() + `/moonfobl/restfacade/be/parameters/${consumer}/${codiceEnte}`;

    return this.genericHttpService.httpGet<any>(url,);
  }

  postFeedbackCitFac(valutazioneCitFac: ValutazioneModuloCitFac){

    const url: string = this.config.getBEServer() + '/api-public/feedback-moon';

    return this.genericHttpService.httpPost(url, valutazioneCitFac);
  }
}
