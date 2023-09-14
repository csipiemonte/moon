/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { LogonModeIf } from './../model/dto/logon-mode-if';
import { Ruolo } from './../model/common/ruolo';
import { UtenteModuloAbilitato } from './../model/dto/utente modulo-abilitato';
import { Injectable } from '@angular/core';
import { Modulo } from '../model/dto/modulo';
import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { ConfigService } from '../config.service';
import { Istanza } from '../model/dto/istanza';
import { Workflow } from '../model/dto/workflow';
import { StoricoWorkflow } from '../model/dto/storicoWorkflow';
import { Comune } from '../model/dto/comune';
import { CriterioRicercaIstanze } from '../model/dto/criterio-ricerca-istanze';
import { empty } from '../services/service.utils';
import { Campo } from '../model/dto/campo';
import { Stato } from '../model/dto/stato';
import {map, Observable, shareReplay, throwError} from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { Allegato } from '../model/dto/allegato';
import { Categoria } from '../model/dto/categoria';
import { CodiceNome } from '../model/dto/codice-nome';
import { StatoModulo } from '../model/dto/statoModulo';
import { MoonboError } from '../model/common/moonbo-error';
import { PortaliIf } from '../model/dto/portali-if';
import { Processo } from '../model/dto/processo';
import { Ente } from '../model/dto/ente';
import { TipoCodiceIstanza } from '../model/dto/tipo-codice-istanza';
import { ModuloAttributo } from '../model/dto/attr/moduloAttributo';
import { Utente } from '../model/dto/utente';
import { UtenteEnteAbilitato } from '../model/dto/utente ente-abilitato';
import { Area } from '../model/dto/area';
import { LogEmail } from '../model/dto/log-email';
import { LogPraticaCosmo } from '../model/dto/log-pratica-cosmo';
import { ProtocolloParametro } from '../model/dto/protocollo-parametro';
import { TicketCrmRichiesta } from '../model/dto/ticket-crm-richiesta';
import { LogServizioCosmo } from '../model/dto/log-servizio-cosmo';
import { ProtocolloMetadato } from '../model/dto/protocollo-metadato';
import {ModuliCacheService} from './moduli-cache.service';
import { ModuloClass, ModuloClassTipologia } from '../model/dto/modulo-class';
import { VerificaPagamento } from '../model/dto/verifica-pagamento';
import { PortaleModuloLogonMode } from '../model/dto/portale-modulo-logon-mode';
import { CustomComponent } from '../model/dto/custom-component';
import { LogMyDocs } from '../model/dto/log-mydocs';
import { Azione } from '../model/dto/azione';



@Injectable()
export class MoonboblService {

  constructor(private http: HttpClient,
              private config: ConfigService,
              private moduliCacheService: ModuliCacheService) {
  }

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error.message); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  // getElencoModuli(): Promise<Modulo[]> {
  //   const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli';
  //   return this.http.get(url).toPromise()
  //     .then(moduli => moduli as Modulo[])
  //     .catch(this.handleError);
  // }

  getElencoModuli(lastVersione: boolean = false): Observable<Modulo[]> {
    let url: string;
    if (lastVersione) {
      url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli?onlyLastVersione=true';
    } else {
      url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli';
    }
    // return this.http.get<Modulo[]>(url);
    let moduli$ = this.moduliCacheService.getValue();
    if (!moduli$) {
      moduli$ = this.http.get<Modulo[]>(url)
          .pipe(
              shareReplay(1)
          );
      this.moduliCacheService.setValue(moduli$);
      return moduli$;
    }
  }
  
  getElencoModuliForce(lastVersione: boolean = false, idModulo: number): Observable<Modulo[]> {
    let url: string;
    if (lastVersione) {
      url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli?onlyLastVersione=true';
      if (idModulo) {
        url = url + '&idModulo=' + idModulo;
      }
    } else {
      url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli';
      if (idModulo) {
        url = url + '?idModulo=' + idModulo;
      }
    }

//    let moduli$ = this.moduliCacheService.getValue();
//    if (!moduli) {
      const moduli$ = this.http.get<Modulo[]>(url)
        .pipe(
          shareReplay(1)
        );
    if (!idModulo) {
      this.moduliCacheService.setValue(moduli$);
    }
    return moduli$;
//    }
  }

  getElencoModuliPubblicati(): Promise<Modulo[]> {
    let url: string;
    url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli?pubblicatoBO=true';
    return this.http.get(url).toPromise()
      .then(moduli => moduli as Modulo[])
      .catch(this.handleError);
  }

  getModuli(): Observable<Modulo[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli';
    return this.http.get<Modulo[]>(url);
  }
  getModuliAbilitatiOfOneUtente(otherIdentificativoUtente: string): Observable<Modulo[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli?onlyLastVersione=true&otherIdentificativoUtente='
      + otherIdentificativoUtente;
    return this.http.get<Modulo[]>(url);
  }

  getModulo(idModulo, idVersione) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/' + idModulo + '/v/' + idVersione;
    return this.http.get(url);
  }

  getModuloWithFields(idModulo, idVersione, fields): Observable<Modulo> {
    const params = new HttpParams().set('fields', fields);
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${idModulo}/v/${idVersione}`;
    return this.http.get<Modulo>(url, { params }).pipe(
      catchError(error => this.handleServiceError(error, 'getModuloWithFields'))
    );
  }

  getPortali(): Observable<PortaliIf[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/portali`;
    return this.http.get<PortaliIf[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getPortali'))
    );
  }


  getStruttura(idStruttura): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/struttura/' + idStruttura;
    return this.http.get(url).toPromise()
      .then(struttura => struttura as any)
      .catch(this.handleError);
  }


  getElencoIstanze(): Promise<Istanza[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze';
    return this.http.get(url).toPromise()
      .then(righe => righe as Istanza[])
      .catch(this.handleError);
  }

  getElencoIstanzeInLavorazione(idModulo, filtro: string, cfDichiarante: string): Promise<Istanza[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/lav/moduli/' + idModulo;
    let parametri = new HttpParams();

    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }

    if (cfDichiarante) {
      parametri = parametri.append('cfDichiarante', '' + cfDichiarante);
    }

    return this.http.get(url, { params: parametri }).toPromise()
      .then(righe => righe as Istanza[])
      .catch(this.handleError);
  }

  getElencoIstanzeInLavorazionePaginate(idModulo, sort: string, filtro: string, 
    cfDichiarante: string, 
    nomeDichiarante: string, 
    cognomeDichiarante: string, 
    idMultiEnte: number, filtroEpay:string, offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/lav/moduli/paginate/' + idModulo;
    let parametri = new HttpParams();

    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }
    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }
    if (cfDichiarante) {
      parametri = parametri.append('cfDichiarante', '' + cfDichiarante);
    }
    if (nomeDichiarante) {
      parametri = parametri.append('nomeDichiarante', '' + nomeDichiarante);
    }
    if (cognomeDichiarante) {
      parametri = parametri.append('cognomeDichiarante', '' + cognomeDichiarante);
    }
    if (nomeDichiarante) {
      parametri = parametri.append('nomeDichiarante', '' + nomeDichiarante);
    }
    if (cognomeDichiarante) {
      parametri = parametri.append('cognomeDichiarante', '' + cognomeDichiarante);
    }
    if (idMultiEnte) {
      parametri = parametri.append('idEnte', '' + idMultiEnte);
    }

    if (filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + filtroEpay);
    }


    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.http.get<any>(url, { params: parametri });
  }

  getElencoPerModello(idModulo: number, stato: number, sort: string, filtro: string, cfDichiarante: string, nomeDichiarante: string, cognomeDichiarante: string): Promise<Istanza[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/moduli/' + idModulo;

    console.log('url elenco istanze per modulo: ' + url);
    let parametri = new HttpParams();

    if (stato) {
      parametri = parametri.append('stato', '' + stato);
    }
    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }
    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }
    if (cfDichiarante) {
      parametri = parametri.append('cfDichiarante', '' + cfDichiarante);
    }
    if (nomeDichiarante) {
      parametri = parametri.append('nomeDichiarante', '' + nomeDichiarante);
    }
    if (cognomeDichiarante) {
      parametri = parametri.append('cognomeDichiarante', '' + cognomeDichiarante);
    }
    if (nomeDichiarante) {
      parametri = parametri.append('nomeDichiarante', '' + nomeDichiarante);
    }
    if (cognomeDichiarante) {
      parametri = parametri.append('cognomeDichiarante', '' + cognomeDichiarante);
    }

    return this.http.get(url, { params: parametri }).toPromise()
      .then(righe => righe as Istanza[])
      .catch(this.handleError);
  }

  cercaIstanzePaginato(criterioRicerca: CriterioRicercaIstanze, offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/paginate';

    console.log('chiamata a ' + url);

    let parametri = new HttpParams();
    if (criterioRicerca.stato != null && criterioRicerca.stato !== -1) {
      parametri = parametri.append('stato', '' + criterioRicerca.stato);
    }
    if (!empty(criterioRicerca.codiceIstanza)) {
      parametri = parametri.append('codiceIstanza', '' + criterioRicerca.codiceIstanza);
    }
    if (criterioRicerca.idMultiEnte) {
      parametri = parametri.append('idEnte', '' + criterioRicerca.idMultiEnte);
    }
    if (!empty(criterioRicerca.protocollo)) {
      parametri = parametri.append('protocollo', '' + criterioRicerca.protocollo);
    }

    if (!empty(criterioRicerca.cfDichiarante)) {
      parametri = parametri.append('cfDichiarante', '' + criterioRicerca.cfDichiarante);
    }
    if (!empty(criterioRicerca.nomeDichiarante)) {
      parametri = parametri.append('nomeDichiarante', '' + criterioRicerca.nomeDichiarante);
    }
    if (!empty(criterioRicerca.cognomeDichiarante)) {
      parametri = parametri.append('cognomeDichiarante', '' + criterioRicerca.cognomeDichiarante);
    }
    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }

    if (!empty(criterioRicerca.titoloModulo)) {
      // parametri = parametri.append('titoloModulo', '' + criterioRicerca.titoloModulo);
    }
    if (criterioRicerca.idModulo != null && criterioRicerca.idModulo !== -1) {
      parametri = parametri.append('idModulo', '' + criterioRicerca.idModulo);
    }
    if (criterioRicerca.sort != null) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }

    if (criterioRicerca.dataDa != null) {
      const s = '' + criterioRicerca.dataDa.getDate() + '/'
        + (criterioRicerca.dataDa.getMonth() + 1) + '/'
        + criterioRicerca.dataDa.getFullYear();
      parametri = parametri.append('dataDal', s);
    }
    if (criterioRicerca.dataA != null) {
      const s = '' + criterioRicerca.dataA.getDate() + '/'
        + (criterioRicerca.dataA.getMonth() + 1) + '/'
        + criterioRicerca.dataA.getFullYear();
      parametri = parametri.append('dataAl', s);
    }

    if (criterioRicerca.filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + criterioRicerca.filtroEpay);
    }

    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.http.get<any>(url, { params: parametri });

  }

  cercaIstanzeInBozzaPaginato(criterioRicerca: CriterioRicercaIstanze, offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/bozza/moduli/paginate/' + criterioRicerca.idModulo;

    console.log('chiamata a ' + url);
    let parametri = new HttpParams();

    if (criterioRicerca.sort) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }
    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }
    if (criterioRicerca.idMultiEnte) {
      parametri = parametri.append('idEnte', '' + criterioRicerca.idMultiEnte);
    }
    
    if (!empty(criterioRicerca.cfDichiarante)) {
      parametri = parametri.append('cfDichiarante', '' + criterioRicerca.cfDichiarante);
    }

    if (!empty(criterioRicerca.nomeDichiarante)) {
      parametri = parametri.append('nomeDichiarante', '' + criterioRicerca.nomeDichiarante);
    }
    if (!empty(criterioRicerca.cognomeDichiarante)) {
      parametri = parametri.append('cognomeDichiarante', '' + criterioRicerca.cognomeDichiarante);
    }

    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }
    if (!empty(criterioRicerca.titoloModulo)) {
      // parametri = parametri.append('titoloModulo', '' + criterioRicerca.titoloModulo);
    }
    // if (criterioRicerca.idModulo != null  &&  criterioRicerca.idModulo !== -1 ) {
    //   parametri = parametri.append('idModulo', '' + criterioRicerca.idModulo);
    // }
    if (criterioRicerca.sort != null) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }

    // if (criterioRicerca.dataDa != null) {
    //   const s = '' + criterioRicerca.dataDa.getDate() + '/'
    //     + (criterioRicerca.dataDa.getMonth() + 1)   + '/'
    //     + criterioRicerca.dataDa.getFullYear();
    //   parametri = parametri.append('dataDal', s);
    // }
    // if (criterioRicerca.dataA != null) {
    //   const s = '' + criterioRicerca.dataA.getDate() + '/'
    //     + (criterioRicerca.dataA.getMonth() + 1)   + '/'
    //     + criterioRicerca.dataA.getFullYear();
    //   parametri = parametri.append('dataAl', s);
    // }
    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.http.get<any>(url, { params: parametri });

  }

  cercaIstanzeDaCompletarePaginato(criterioRicerca: CriterioRicercaIstanze, offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/dacompletare/moduli/paginate/' + criterioRicerca.idModulo;

    console.log('chiamata a ' + url);
    let parametri = new HttpParams();

    if (criterioRicerca.sort) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }
    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }
    if (criterioRicerca.idMultiEnte) {
      parametri = parametri.append('idEnte', '' + criterioRicerca.idMultiEnte);
    }
 
    if (!empty(criterioRicerca.cfDichiarante)) {
      parametri = parametri.append('cfDichiarante', '' + criterioRicerca.cfDichiarante);
    }
    if (!empty(criterioRicerca.nomeDichiarante)) {
      parametri = parametri.append('nomeDichiarante', '' + criterioRicerca.nomeDichiarante);
    }
    if (!empty(criterioRicerca.cognomeDichiarante)) {
      parametri = parametri.append('cognomeDichiarante', '' + criterioRicerca.cognomeDichiarante);
    }
    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }
    if (!empty(criterioRicerca.titoloModulo)) {
    }

    if (criterioRicerca.sort != null) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }

    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.http.get<any>(url, { params: parametri });
  }

  /*
    stato = 1 Bozza
    stato = 2 Inviata
    importanza = 1
    sort = '-dataCreazione'
     */

  getElencoIstanzeNonpaginato(stato: number, importanza: number, sort: string): Promise<Istanza[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/';

    console.log('url elenco istanze per modulo: ' + url);
    let parametri = new HttpParams();

    if (stato) {
      parametri = parametri.append('stato', '' + stato);
    }
    if (importanza) {
      parametri = parametri.append('importanza', '' + importanza);
    }

    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }

    return this.http.get(url, { params: parametri }).toPromise()
      .then(righe => righe as Istanza[])
      .catch(this.handleError);
  }


  getIstanza(idIstanza): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + idIstanza;
    return this.http.get(url).toPromise()
      .then(istanza => istanza as Istanza)
      .catch(this.handleError);
  }

  getIstanzaBozza(idIstanza): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/bozza/' + idIstanza;
    return this.http.get(url).toPromise()
      .then(istanza => istanza as Istanza)
      .catch(this.handleError);
  }

  /*
    Salva istanza
   */
  salvaIstanza(data: any, idModulo: number, idVersioneModulo: number, idIstanza: number, codiceStato: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/';
    const payload = {
      'idIstanza': idIstanza,
      'stato': { 'idStato': codiceStato },
      'data': JSON.stringify(data),
      'modulo': { 'idModulo': idModulo, 'idVersioneModulo': idVersioneModulo }
    };
    return this.http.post(url, payload);
  }

  salvaIstanzaConMotivazione(data: any, metadata: any, idModulo: number, idVersioneModulo: number, idIstanza: number, codiceStato: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/';
    const payload = {
      'idIstanza': idIstanza,
      'stato': { 'idStato': codiceStato },
      'data': JSON.stringify(data),
      'metadata': JSON.stringify(metadata),
      'modulo': { 'idModulo': idModulo, 'idVersioneModulo': idVersioneModulo }
    };
    return this.http.post(url, payload);
  }

  // to deprecate
  salvaCompilaIstanza(data: any, idModulo: number, idVersioneModulo: number, idIstanza: number, codiceStato: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/compila/';
    const payload = {
      'idIstanza': idIstanza,
      'stato': { 'idStato': codiceStato },
      'data': JSON.stringify(data),
      'modulo': { 'idModulo': idModulo, 'idVersioneModulo': idVersioneModulo }
    };
    return this.http.post(url, payload);
  }

  salvaCompila(payload: any) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/compila/';
    return this.http.post(url, payload);
  }



  // getDatiInizializzazioneModulo(idModulo: number) {
  //   const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/init/' + idModulo;
  //   return this.http.get(url);
  // }


  getDatiInizializzazioneModulo(idModulo: number, idVersioneModulo: number, datiInit: any) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/init/' + idModulo + '/v/' + idVersioneModulo;
    return this.http.post(url, datiInit);
  }


  /*
  Cambia importanza
  */
  cambiaImportanzaIstanza(istanza: Istanza) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + istanza.idIstanza;
    const payload = {
      'importanza': istanza.importanza
    };
    return this.http.patch(url, payload);
  }

  doAzione(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/compieAzione';
    const payload = {
      'idWorkflow': idWorkflow,
      'datiAzione': '',
      'nomeAzione': ''
    };
    return this.http.post(url, payload);
  }

  completaAzione(data: any, idWorkflow, idIstanza) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/completaAzione';
    const payload = {
      'idWorkflow': idWorkflow,
      'datiAzione': JSON.stringify(data),
      'idIstanza': idIstanza
    };
    return this.http.post(url, payload);
  }

  motivaModifica(data: any, idIstanza) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/motivaModifica';
    const payload = {
      'datiAzione': JSON.stringify(data),
      'idIstanza': idIstanza
    };
    return this.http.post(url, payload);
  }

  annullaAzione(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/annullaAzione';
    const payload = {
      'idWorkflow': idWorkflow
    };
    return this.http.post(url, payload);
  }

  getWorkflows(idIstanza): Promise<Workflow[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza;
    return this.http.get(url).toPromise()
      .then(elencoWorkflow => elencoWorkflow as Workflow[])
      .catch(this.handleError);
  }

  getPrevWorkflow(idIstanza): Promise<Workflow> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/ultimo';
    return this.http.get(url).toPromise()
      .then(workflow => workflow as Workflow)
      .catch(this.handleError);
  }

  getWorkflowByIdWorkflow(idWorkflow): Promise<Workflow> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/' + idWorkflow;
    return this.http.get(url).toPromise()
      .then(elencoWorkflow => elencoWorkflow as Workflow[])
      .catch(this.handleError);
  }

  geStoricotWorkflow(idIstanza): Promise<StoricoWorkflow[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/storico/';
    return this.http.get(url).toPromise()
      .then(elencoStoricoWorkflow => elencoStoricoWorkflow as StoricoWorkflow[])
      .catch(this.handleError);
  }

  getWorkflowsByIdProcesso(idProcesso: number): Observable<Workflow[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows?idProcesso=' + idProcesso;
    return this.http.get<Workflow[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getWorkflowsByIdProcesso'))
    );
  }

  getInfoStoricoWorkflow(idStoricoWorkflow): Promise<StoricoWorkflow> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/storico/' + idStoricoWorkflow;

    return this.http.get(url).toPromise()
      .then(infoStoricoWorkflow => infoStoricoWorkflow as StoricoWorkflow)
      .catch(this.handleError);
  }

  getStrutturaAzione(idIstanza, idWorkflow): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/struttura/' + idWorkflow;
    return this.http.get(url).toPromise()
      .then(strutturaAzione => strutturaAzione as any)
      .catch(this.handleError);
  }

  getStrutturaByCodiceAzione(codiceAzione): Promise<any> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/struttura/' + codiceAzione;
    return this.http.get(url).toPromise()
      .then(strutturaAzione => strutturaAzione as any)
      .catch(this.handleError);
  }

  getDatiInizializzaDatiAzione(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/init/' + idWorkflow;
    return this.http.get(url);
  }

  getStrutturaAzioneX(idIstanza, idWorkflow) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/workflows/istanza/' + idIstanza + '/struttura/' + idWorkflow;
    return this.http.get(url);
  }

  getElencoComuni(codiceProvincia): Promise<Comune[]> {
    const codiceRegione = '01';

    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/extra/regioni/' + codiceRegione +
      '/province/' + codiceProvincia + '/comuni/';
    return this.http.get(url).toPromise()
      .then(comuni => comuni as Comune[])
      .catch(this.handleError);
  }

  getElencoComuniUtente(): Promise<Comune[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/utenti/comuni-abilitati/';
    return this.http.get(url).toPromise()
      .then(comuni => comuni as Comune[])
      .catch(this.handleError);
  }

  getElencoEntiUtente(): Promise<CodiceNome[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/utenti/enti-abilitati/';
    return this.http.get(url).toPromise()
      .then(enti => enti as CodiceNome[])
      .catch(this.handleError);
  }

  getEntiMultiEnte(): Promise<Ente[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/enti/';
    return this.http.get(url).toPromise()
      .then(enti => enti as Ente[])
      .catch(this.handleError);
  }

  getElencoEnti(): Observable<Ente[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/enti';
    return this.http.get<Ente[]>(url);
  }

  aggiornaEnte(ente: Ente) {
    console.log('imposta ente id ente: ' + ente.idEnte);
    const payload = {
      'ente': ente
    };
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/currentUser';
    //const options = {};
    return this.http.patch(url, payload, { observe: 'response' });
  }

  getEnte(idEnte: number): Promise<Ente> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/enti/' + idEnte;
    return this.http.get(url).toPromise()
      .then(ente => ente as Ente)
      .catch(this.handleError);
  }

  public getPdf(idIstanza): Observable<Blob> {
    const url = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + idIstanza + '/pdf';
    return this.http.get(url, { responseType: 'blob' });
  }

  // public getJson(idIstanza): Observable<Blob> {
  //   const url = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + idIstanza + '/json';
  //   return this.http.get(url, { responseType: 'blob' });
  // }

  public getRicevuta(idFile): Observable<Blob> {
    const url = this.config.getBEServer() + '/moonbobl/restfacade/be/repository/file/' + idFile;
    return this.http.get(url, { responseType: 'blob' });
  }



  getCampiPerModulo(idModulo: number): Promise<Campo[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/' + idModulo + '/campi?onlyFirstLevel=S';
    console.log('url elenco campi per modulo: ' + url);
    return this.http.get(url).toPromise()
      .then(campi => campi as Campo[])
      .catch(this.handleError);
  }

  getStatiBoPerModulo(idModulo: number): Promise<Stato[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/stati/bo?idModulo=' + idModulo;
    console.log('url elenco stati per modulo: ' + url);
    return this.http.get(url).toPromise()
      .then(stati => stati as Stato[])
      .catch(this.handleError);
  }

  getIstanzeJson(idModulo: number, stati: number[], inviateDataDa: Date, inviateDataA: Date, dataDa: Date, dataA: Date, sort: string, filtroEpay:string, filtro: string) {

    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/modulo/' + idModulo + '/json';
    console.log('url elenco istanze per modulo: ' + url);
    let parametri = new HttpParams();

    if (stati) {
      for (const st of stati) {
        parametri = parametri.append('stati', '' + st);
      }
    }

    if (inviateDataDa) {
      const month: number = inviateDataDa.getMonth() + 1;
      const s = '' + inviateDataDa.getFullYear() + '/'
        + month + '/'
        + inviateDataDa.getDate();
      console.log('created_start ' + s);
      parametri = parametri.append('created_start', '' + s);
    }
    if (inviateDataA) {
      const month: number = inviateDataA.getMonth() + 1;
      const s = '' + inviateDataA.getFullYear() + '/'
        + month + '/'
        + inviateDataA.getDate();
      console.log('created_end ' + s);
      parametri = parametri.append('created_end', '' + s);
    }

    if (dataDa) {
      const month: number = dataDa.getMonth() + 1;
      const s = '' + dataDa.getFullYear() + '/'
        + month + '/'
        + dataDa.getDate();
      console.log('state_start ' + s);
      parametri = parametri.append('state_start', '' + s);
    }
    if (dataA) {
      const month: number = dataA.getMonth() + 1;
      const s = '' + dataA.getFullYear() + '/'
        + month + '/'
        + dataA.getDate();
      console.log('state_end ' + s);
      parametri = parametri.append('state_end', '' + s);
    }
    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }
    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }
    if (filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + filtroEpay);
    }

    return this.http.get<any>(url, { params: parametri });
  }


  getIstanzePaginateJson(idModulo: number, stati: number[], inviateDataDa: Date, inviateDataA: Date, dataDa: Date, dataA: Date, sort: string, filtroEpay:string, filtro: string, offset: number, limit: number) {

    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/paginate/modulo/' + idModulo + '/json';
    console.log('url elenco istanze per modulo: ' + url);
    let parametri = new HttpParams();

    if (stati) {
      for (const st of stati) {
        parametri = parametri.append('stati', '' + st);
      }
    }

    if (inviateDataDa) {
      const month: number = inviateDataDa.getMonth() + 1;
      const s = '' + inviateDataDa.getFullYear() + '/'
        + month + '/'
        + inviateDataDa.getDate();
      console.log('created_start ' + s);
      parametri = parametri.append('created_start', '' + s);
    }
    if (inviateDataA) {
      const month: number = inviateDataA.getMonth() + 1;
      const s = '' + inviateDataA.getFullYear() + '/'
        + month + '/'
        + inviateDataA.getDate();
      console.log('created_end ' + s);
      parametri = parametri.append('created_end', '' + s);
    }

    if (dataDa) {
      const month: number = dataDa.getMonth() + 1;
      const s = '' + dataDa.getFullYear() + '/'
        + month + '/'
        + dataDa.getDate();
      console.log('state_start ' + s);
      parametri = parametri.append('state_start', '' + s);
    }
    if (dataA) {
      const month: number = dataA.getMonth() + 1;
      const s = '' + dataA.getFullYear() + '/'
        + month + '/'
        + dataA.getDate();
      console.log('state_end ' + s);
      parametri = parametri.append('state_end', '' + s);
    }
    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }
    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }
    if (filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + filtroEpay);
    }


    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);

    return this.http.get<any>(url, { params: parametri });
  }



  getCountIstanzeJson(idModulo: number, stati: number[], inviateDataDa: Date, inviateDataA: Date, dataDa: Date, dataA: Date, sort: string, filtroEpay:string, filtro: string) {

    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/modulo/' + idModulo + '/json/count';
    console.log('url elenco istanze per modulo: ' + url);
    let parametri = new HttpParams();

    if (stati) {
      for (const st of stati) {
        parametri = parametri.append('stati', '' + st);
      }
    }
    if (inviateDataDa) {
      const month: number = inviateDataDa.getMonth() + 1;
      const s = '' + inviateDataDa.getFullYear() + '/'
        + month + '/'
        + inviateDataDa.getDate();
      console.log('created_start ' + s);
      parametri = parametri.append('created_start', '' + s);
    }
    if (inviateDataA) {
      const month: number = inviateDataA.getMonth() + 1;
      const s = '' + inviateDataA.getFullYear() + '/'
        + month + '/'
        + inviateDataA.getDate();
      console.log('created_end ' + s);
      parametri = parametri.append('created_end', '' + s);
    }

    if (dataDa) {
      const month: number = dataDa.getMonth() + 1;
      const s = '' + dataDa.getFullYear() + '/'
        + month + '/'
        + dataDa.getDate();
      console.log('state_start ' + s);
      parametri = parametri.append('state_start', '' + s);
    }
    if (dataA) {
      const month: number = dataA.getMonth() + 1;
      const s = '' + dataA.getFullYear() + '/'
        + month + '/'
        + dataA.getDate();
      console.log('state_end ' + s);
      parametri = parametri.append('state_end', '' + s);
    }

    if (sort) {
      parametri = parametri.append('sort', '' + sort);
    }
    if (filtro) {
      parametri = parametri.append('filtroRicerca', '' + filtro);
    }
    if (filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + filtroEpay);
    }


    return this.http.get<any>(url, { params: parametri });
  }


  cercaIstanzeArchivioPaginato(criterioRicerca: CriterioRicercaIstanze, stati: number[], offset: number, limit: number) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/archivio/paginate';

    let parametri = new HttpParams();
    // if (criterioRicerca.stato != null && criterioRicerca.stato !== -1 ) {
    //   parametri = parametri.append('stato', '' + criterioRicerca.stato);
    // }
    if (stati) {
      for (const st of stati) {
        parametri = parametri.append('stati', '' + st);
      }
    }
    if (!empty(criterioRicerca.codiceIstanza)) {
      parametri = parametri.append('codiceIstanza', '' + criterioRicerca.codiceIstanza);
    }
    if (criterioRicerca.idMultiEnte) {
      parametri = parametri.append('idEnte', '' + criterioRicerca.idMultiEnte);
    }
    if (!empty(criterioRicerca.protocollo)) {
      parametri = parametri.append('protocollo', '' + criterioRicerca.protocollo);
    }

    if (!empty(criterioRicerca.cfDichiarante)) {
      parametri = parametri.append('cfDichiarante', '' + criterioRicerca.cfDichiarante);
    }

    if (!empty(criterioRicerca.nomeDichiarante)) {
      parametri = parametri.append('nome', '' + criterioRicerca.nomeDichiarante);
    }
    if (!empty(criterioRicerca.cognomeDichiarante)) {
      parametri = parametri.append('cognome', '' + criterioRicerca.cognomeDichiarante);
    }
    if (!empty(criterioRicerca.nomeElementoIstanza)) {
      parametri = parametri.append('filtroRicerca', criterioRicerca.nomeElementoIstanza + '=' + criterioRicerca.valoreElementoIstanza);
    }

    // if (! empty(criterioRicerca.titoloModulo)) {
    //   parametri = parametri.append('titoloModulo', '' + criterioRicerca.titoloModulo);
    // }
    if (criterioRicerca.idModulo != null && criterioRicerca.idModulo !== -1) {
      parametri = parametri.append('idModulo', '' + criterioRicerca.idModulo);
    }
    if (criterioRicerca.sort != null) {
      parametri = parametri.append('sort', '' + criterioRicerca.sort);
    }
    if (criterioRicerca.dataDa) {
      const month: number = criterioRicerca.dataDa.getMonth() + 1;
      const s = '' + criterioRicerca.dataDa.getFullYear() + '/'
        + month + '/'
        + criterioRicerca.dataDa.getDate();
      console.log('state_start ' + s);
      parametri = parametri.append('state_start', '' + s);
    }
    if (criterioRicerca.dataA) {
      const month: number = criterioRicerca.dataA.getMonth() + 1;
      const s = '' + criterioRicerca.dataA.getFullYear() + '/'
        + month + '/'
        + criterioRicerca.dataA.getDate();
      console.log('state_end ' + s);
      parametri = parametri.append('state_end', '' + s);
    }

    if (criterioRicerca.inviateDataDa) {
      const month: number = criterioRicerca.inviateDataDa.getMonth() + 1;
      const s = '' + criterioRicerca.inviateDataDa.getFullYear() + '/'
        + month + '/'
        + criterioRicerca.inviateDataDa.getDate();
      console.log('created_start ' + s);
      parametri = parametri.append('created_start', '' + s);
    }
    if (criterioRicerca.inviateDataA) {
      const month: number = criterioRicerca.inviateDataA.getMonth() + 1;
      const s = '' + criterioRicerca.inviateDataA.getFullYear() + '/'
        + month + '/'
        + criterioRicerca.inviateDataA.getDate();
      console.log('created_end ' + s);
      parametri = parametri.append('created_end', '' + s);
    }

    if (criterioRicerca.filtroEpay) {
      parametri = parametri.append('filtroEpay', '' + criterioRicerca.filtroEpay);
    }

    parametri = parametri.append('offset', '' + offset);
    parametri = parametri.append('limit', '' + limit);
    return this.http.get<any>(url, { params: parametri });

  }


  public updateStrutturaModulo(modulo: Modulo): Observable<any> {
    const url = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/' + modulo.idModulo + '/v/' + modulo.idVersioneModulo + '/struttura';
    return this.http.put<Modulo>(url, modulo).pipe(
      tap(data => console.log('Data:' + JSON.stringify(data))),
      catchError(error => this.handleServiceError(error, 'updateStrutturaModulo'))
    );
  }


  getAllegati(idIstanza: number): Promise<Allegato[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + idIstanza + '/allegati';
    console.log('url elenco allegati per istanza: ' + url);
    return this.http.get(url).toPromise()
      .then(allegati => allegati as Allegato[])
      .catch(this.handleError);
  }

  getAllegato(formioName: string): Observable<Blob> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/allegato/' + formioName;
    return this.http.get(url, { responseType: 'blob' });
  }

  getElencoCategorie() {
    return this.http.get<Categoria[]>(this.config.getBEServer() + '/moonbobl/restfacade/be/categorie')
      .pipe(tap(
        (categorie) => {
        },
        (httpResponse: HttpErrorResponse) => {
          throw httpResponse;
        }
      ));
  }

  inserisciModulo(modulo: Modulo) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli';
    return this.http.post(url, modulo);
  }

  aggiornaModulo(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/v/${modulo.idVersioneModulo}`;
    return this.http.put(url, modulo);
  }

  pubblicaModulo(modulo: Modulo) {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/pubblica/' + modulo.idModulo + '/v/' + modulo.idVersioneModulo;
    return this.http.put(url, modulo);
  }


  getElencoStatiModulo(codiceProvenienza: string): Observable<StatoModulo[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/stati/';
    const params = new HttpParams()
      .set('codiceProvenienza', codiceProvenienza);
    return this.http.get<StatoModulo[]>(url, { params }).pipe(
      catchError(error => this.handleServiceError(error, 'getElencoStatiModulo'))
    );
  }

  //   @Path("/cambiaStato/{idModulo}/v/{idVersioneModulo}/newStato/{newStato}")
  //   @Path("/cambiaStato/{idModulo}/v/{idVersioneModulo}/newStato/{newStato}?inDataOra=2021-")
  cambiaStatoModulo(modulo: Modulo, codiceNuovoStato: string): Observable<HttpResponse<any>> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/cambiaStato/${modulo.idModulo}/v/${modulo.idVersioneModulo}/newStato/${codiceNuovoStato}`;
    return this.http.put<HttpResponse<any>>(url, {}).pipe(
      catchError(error => this.handleServiceError(error, 'cambiaStatoModulo'))
    );
  }

  cambiaStatoModuloInDataOra(modulo: Modulo, codiceNuoboStato: string, inDataOra: string): Observable<HttpResponse<any>> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/cambiaStato/${modulo.idModulo}/v/${modulo.idVersioneModulo}/newStato/${codiceNuoboStato}?inDataOra=${inDataOra}`;
    return this.http.put<HttpResponse<any>>(url, {}).pipe(
      catchError(error => this.handleServiceError(error, 'cambiaStatoModuloInDataOra'))
    );
  }



  salvaPortali(modulo: Modulo, elencoPortali: number[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/v/${modulo.idVersioneModulo}/portali`;
    return this.http.post<HttpResponse<any>>(url, elencoPortali).pipe(
      catchError(error => this.handleServiceError(error, 'salvaPortali'))
    );
  }

  eliminaModulo(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/v/${modulo.idVersioneModulo}`;
    return this.http.delete(url).pipe(
      catchError(error => this.handleServiceError(error, 'eliminaModulo'))
    );
  }

  eliminaIstanza(istanza: Istanza) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/${istanza.idIstanza}`;
    return this.http.delete(url).pipe(
      catchError(error => this.handleServiceError(error, 'eliminaIstanza'))
    );
  }


  // Servizi Gestione della versione
  initNuovaVersioneModulo(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/initNuovaVersioneModulo`;
    return this.http.get(url).pipe(
      catchError(error => this.handleServiceError(error, 'initNuovaVersioneModulo'))
    );
  }

  initCambioStato(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/v/${modulo.idVersioneModulo}/initCambiaStato`;
    return this.http.get(url).pipe(
      catchError(error => this.handleServiceError(error, 'initCambioStato'))
    );
  }

  creaNuovaVersione(modulo: Modulo, idversionePartenza: number, nuovaVersione: string) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/nuovaVersione`;
    const payload = {
      'idVersionePartenza': idversionePartenza,
      'versione': nuovaVersione
    };
    return this.http.post(url, payload).pipe(
      catchError(error => this.handleServiceError(error, 'creaNuovaVersione'))
    );
  }

  getElencoProcessi(): Observable<Processo[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/processi';
    return this.http.get<Processo[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getElencoProcessi'))
    );
  }


  protocolla(idIstanza) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/protocolla/${idIstanza}`;
    return this.http.post<HttpResponse<any>>(url, {}).pipe(
      catchError(error => this.handleServiceError(error, 'protocolla'))
    );
  }

  rinviaEmail(idIstanza, dest: string) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/${idIstanza}/rinvia-email?dest=` + dest;
    return this.http.post<HttpResponse<any>>(url, {}).pipe(
      catchError(error => this.handleServiceError(error, 'rinviaEmail'))
    );
  }

  private handleServiceError(err: HttpErrorResponse, scope: string) {
    const moonboError = new MoonboError();
    if (!scope) {
      moonboError.scope = 'UNKNOW';
    } else {
      moonboError.scope = scope;
    }

    if (err.error instanceof ErrorEvent) {
      moonboError.errorMsg = `Errore: ${err.error.message}`;
      moonboError.codice = 0;
    } else {
      moonboError.techMsg = err.message;
      moonboError.url = err.url;
      moonboError.codice = err.status;
      if (err.error) {
        moonboError.errorMsg = `${err.error.msg}`;
        moonboError.errorCodice = `${err.error.code}`;
        moonboError.errorTitle = `${err.error.title}`;
      } else {
        moonboError.errorMsg = 'Errore invocazione servizio';
      }
    }
    return throwError(moonboError);
  }

  getElencoTipoCodiceIstanza(): Observable<TipoCodiceIstanza[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/tipi-codice-istanza';
    return this.http.get<TipoCodiceIstanza[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getElencoTipoCodiceIstanza'))
    );
  }

  aggiornaModuloAttributiGenerali(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-generali`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiEmail(modulo: Modulo, attributi: ModuloAttributo[]): Observable<any> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-email`;
    return this.http.post(url, attributi).pipe(
      catchError(error => this.handleServiceError(error, 'aggiornaMoPromiseduloAttributiEmail'))
    );
  }

  aggiornaModuloAttributiNotificatore(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-notificatore`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiNotificatorePromise(modulo: Modulo, attributi: ModuloAttributo[]): Promise<ModuloAttributo[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-notificatore`;
    return this.http.post(url, attributi).toPromise()
      .then(attrs => attrs as ModuloAttributo[])
      .catch(this.handleError);
  }

  aggiornaModuloAttributiProtocollo(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-protocollo`;
    return this.http.post(url, attributi);
  }
  aggiornaModuloAttributiCosmo(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-cosmo`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiAzione(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-azione`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiEstraiDichiarante(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-estrai-dichiarante`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiCrm(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-crm`;
    return this.http.post(url, attributi);
  }

  aggiornaModuloAttributiEpay(modulo: Modulo, attributi: ModuloAttributo[]) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/attributi-epay`;
    return this.http.post(url, attributi);
  }

  getModuloAttributo(m: Modulo, attr: string): string {
    // const attributi = m['attributi'];
    // if (attributi) {
    //   for ( let i = 0; i < attributi.length; i++) {
    //     if (attributi[i]['nome'] === attr) {
    //       return attributi[i]['valore'];
    //     }
    //   }
    // } else {
    //   return null;
    // }
    if (m['objAttributi']) {
      let attr = JSON.parse(m['objAttributi']);
      let cTerzi = attr['attributi']['CONTO_TERZI'];
      return cTerzi;
    }
    else return null;
  }

  getUtenteById(idUtente: number): Observable<Utente> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/utenti/' + idUtente;
    return this.http.get<Utente>(url);
  }

  getUtenteByIdentificativo(identificativoUtente: string): Observable<Utente> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/utenti/byIdentificativo/' + identificativoUtente;
    return this.http.get<Utente>(url);
  }

  getUtentiAbilitatiByModulo(idModulo: number): Observable<UtenteModuloAbilitato[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${idModulo}/utenti`;
    return this.http.get<UtenteModuloAbilitato[]>(url);
  }

  getUtentiEnteAbilitato(): Observable<UtenteEnteAbilitato[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/utenti/ente-abilitato`;
    return this.http.get<UtenteEnteAbilitato[]>(url);
  }

  aggiornaUtente(utente: Utente): Observable<Utente> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/utenti/${utente.idUtente}`;
    return this.http.patch<Utente>(url, utente);
  }

  getAreeByIdEnte(idEnte: number): Observable<Area[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/enti/${idEnte}/aree`;
    return this.http.get<Area[]>(url);
  }

  getRuoli(): Observable<Ruolo[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/ruoli`;
    return this.http.get<Ruolo[]>(url);
  }

  riportaIstanzaInBozza(istanza: Istanza) {
    console.log('START riporto in bozza ' + istanza.idIstanza);
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + istanza.idIstanza + '/riportaInBozza';
    const options = {};
    return this.http.patch(url, options);
  }

  inviaIstanza(istanza: Istanza) {
    console.log('START invia istanza' + istanza.idIstanza);
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/' + istanza.idIstanza + '/invia';
    const options = {};
    return this.http.patch(url, options);
  }

  postAggiungiAreaRuolo(idUtente: number, idEnte: number, idArea: number, idRuolo: number): Observable<UtenteEnteAbilitato> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/utenti/${idUtente}/ente/${idEnte}/area/${idArea}/ruolo/${idRuolo}`;
    return this.http.post<UtenteEnteAbilitato>(url, null);
  }
  deleteUtenteAreaRuolo(idUtente: number, idEnte: number, idArea: number, idRuolo: number): Observable<UtenteEnteAbilitato> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/utenti/${idUtente}/ente/${idEnte}/area/${idArea}/ruolo/${idRuolo}`;
    return this.http.delete<UtenteEnteAbilitato>(url);
  }

  postUtenteModulo(idUtente: number, idModulo: number) {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/utenti/${idUtente}/modulo/${idModulo}`;
    return this.http.post(url, null);
  }
  deleteUtenteModulo(idUtente: number, idModulo: number) {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/utenti/${idUtente}/modulo/${idModulo}`;
    return this.http.delete(url);
  }
  postUtente(utente: Utente): Observable<Utente> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/utenti`;
    return this.http.post<Utente>(url, utente);
  }

  getUtentiEnteNoAbilitato(): Observable<UtenteEnteAbilitato[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/utenti/ente-no-abilitato`;
    return this.http.get<UtenteEnteAbilitato[]>(url);
  }

  getLogEmail(idIstanza: number): Promise<LogEmail[]> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/istanze/${idIstanza}/log-email`;
    return this.http.get<LogEmail[]>(url).toPromise();
  }

  getLogPraticaCosmo(idIstanza: number): Promise<LogPraticaCosmo[]> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/istanze/${idIstanza}/log-pratica-cosmo`;
    return this.http.get<LogPraticaCosmo[]>(url).toPromise();
  }

  getLogServizioCosmo(idIstanza: number): Promise<LogServizioCosmo[]> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/istanze/${idIstanza}/log-servizio-cosmo`;
    return this.http.get<LogServizioCosmo[]>(url).toPromise();
  }

  getProtocolloParametri(modulo: Modulo): Observable<ProtocolloParametro[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/v/${modulo.idVersioneModulo}/protocollo-parametri`;
    return this.http.get<ProtocolloParametro[]>(url);
  }

  getImageProcesso(idProcesso: number) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/processi/${idProcesso}/image`;
    return this.http.get(url, { responseType: 'blob' });
  }

  getTicketCrmRichiesteIstanzaTx(idIstanza): Observable<TicketCrmRichiesta[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/ticket-crm-richieste/istanza-tx/${idIstanza}`;
    return this.http.get<TicketCrmRichiesta[]>(url);
  }

  creaPraticaEdAvviaProcessoCosmo(idIstanza) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/creaPraticaEdAvviaProcesso/${idIstanza}`;
    return this.http.post(url, null);
  }

  inviaIntegrazione(idIstanza) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/${idIstanza}/integrazione-cosmo`;
    return this.http.post(url, null);
  }

  creaTicketCrm(idIstanza) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/crea-ticket-crm/${idIstanza}`;
    return this.http.post(url, null);
  }

  getParametriProtocollo(selectedModulo: number): Promise<boolean> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/' + selectedModulo + '/has-protocollo-parametri';
    return this.http.get(url).toPromise()
      .then()
      .catch(this.handleError);
  }

  getProtocolloMetadati(): Observable<ProtocolloMetadato[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/protocollo-metadati`;
    return this.http.get<ProtocolloMetadato[]>(url);
  }

  postFileModuloClass(modulo: Modulo, tipologia: ModuloClassTipologia, fileToUpload: File) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/modulo-class-for-tipologia/${tipologia}`;
    const formData: FormData = new FormData();
    formData.append('file', fileToUpload, fileToUpload?.name);
    return this.http.post(url, formData);
  }

  deleteModuloClass(modulo: Modulo, tipologia: ModuloClassTipologia) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/modulo-class-for-tipologia/${tipologia}`;
    return this.http.delete(url);
  }

  getModuloClassByTipologia(modulo: Modulo, tipologia: ModuloClassTipologia) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/${modulo.idModulo}/modulo-class-for-tipologia/${tipologia}`;
    return this.http.get<ModuloClass>(url);
  }

  getPrintMapperName(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/codice/${modulo.codiceModulo}/print-mapper-name`;
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    const options: Object = {
      headers: headers,
      responseType: 'text'
    };
    return this.http.get<string>(url, options);
  }

  getProtocolloManagerName(modulo: Modulo) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/moduli/codice/${modulo.codiceModulo}/protocollo-manager-name`;
    const headers = new HttpHeaders().set('Content-Type', 'text/plain; charset=utf-8');
    const options: Object = {
      headers: headers,
      responseType: 'text'
    };
    return this.http.get<string>(url, options);
  }

  getVerificaPagamento(idEpay: string) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/epay/verifica-pagamento/${idEpay}`;
    return this.http.get<VerificaPagamento>(url);
  }

  autenticaUtenteByShibbolet(qryParams: string) {   
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/auth/login/idp/wrup' + qryParams;    
    return this.http.post(url, {});
  }

  getPortaliLogonMode(idModulo: number): Observable<PortaleModuloLogonMode[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/modulistica/portali-logon-mode-by-modulo/${idModulo}`;
    return this.http.get<PortaleModuloLogonMode[]>(url);
  }

  getLogonMode(): Observable<LogonModeIf[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/modulistica/logon-mode`;
    return this.http.get<LogonModeIf[]>(url);
  }

  postPortaleModuloLogonMode(idPortale: number, idModulo: number, idLogonMode: number, filtro: string): Observable<PortaleModuloLogonMode> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/modulistica/portale-modulo-logon-mode`;
    const payload = {
      'idPortale': idPortale,
      'idModulo': idModulo,
      'idLogonMode': idLogonMode,
      'filtro': filtro,
    };
    return this.http.post<PortaleModuloLogonMode>(url, payload);
  }

  deletePortaleModuloLogonMode(plm: PortaleModuloLogonMode) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/modulistica/portale/${plm.portale.idPortale}/modulo/${plm.idModulo}`;
    return this.http.delete(url);
  }

  public getFormioCustomComponent(idComponent): Observable<CustomComponent> {
    const url = this.config.getBEServer() +"/moonbobl/restfacade/be/custom-components/" +idComponent;
    return this.http.get<CustomComponent>(url);
  }

  public getFormioCustomComponents(): Observable<CustomComponent[]> {
    const url = this.config.getBEServer() + "/moonbobl/restfacade/be/custom-components";
    return this.http.get<CustomComponent[]>(url);
  }

  hasProtocolloBo(selectedModulo: number): Promise<boolean> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/moduli/' + selectedModulo + '/has-protocollo-bo';
    return this.http.get(url).toPromise()
      .then()
      .catch(this.handleError);
  }

  getValutazioneModuloSintesi(idModulo: number) {
    const url = this.config.getBEServer() + "/moonbobl/restfacade/be/valutazioni/sintesi?idModulo=" + idModulo;
    return this.http.get(url);
  }

  getAzioneWorkflowDaEseguireDopoInviata(idModulo: number): Promise<Workflow> {
    const url = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/modulo/${idModulo}/dopo-inviata`;
    return this.http.get(url).toPromise()
      .then()
      .catch(this.handleError);
  }

  pubblicaMyDocs(idIstanza,idStoricoWorkflow) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/mydocs/${idIstanza}/storico/${idStoricoWorkflow}`;
    return this.http.post(url, null);
  }

  // pubblicaFileMyDocs(idFile) {
  //   const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/mydocs/${idFile}`;
  //   return this.http.post(url, null);
  // }

  retryPubblicazioneFileMyDocs(idFile, idRichiesta) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/mydocs/${idFile}/log-richiesta/${idRichiesta}`;
    return this.http.post(url, null);
  }

  retryPubblicazioneIstanzaMyDocs(idIstanza, idRichiesta) {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/istanze/mydocs/${idIstanza}/log-richiesta/${idRichiesta}`;
    return this.http.post(url, null);
  }

  getLogMyDocs(idIstanza: number): Promise<LogMyDocs[]> {
    const url: string = this.config.getBEServer() +
      `/moonbobl/restfacade/be/istanze/mydocs/richieste/${idIstanza}`;
    return this.http.get<LogMyDocs[]>(url).toPromise();
  }

  getLogTicket(idIstanza: number): Promise<TicketCrmRichiesta[]> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/ticket-crm-richieste/log/${idIstanza}`;
    return this.http.get<TicketCrmRichiesta[]>(url).toPromise();
  }

  //
  postProcesso(processo: Processo): Observable<Processo> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/processi`;
    return this.http.post<Processo>(url, processo);
  }

  getElencoStati(): Observable<Stato[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/stati';
    return this.http.get<Stato[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getElencoStati'))
    );
  }

  getElencoAzioni(): Observable<Azione[]> {
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/azioni';
    return this.http.get<Azione[]>(url).pipe(
      catchError(error => this.handleServiceError(error, 'getElencoAzioni'))
    );
  }

  salvaWorkflow(workflow: Workflow): Observable<Workflow> {
    console.log('salvaWorkflow : ' + JSON.stringify(workflow));
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows`;
    return this.http.post<Workflow>(url, workflow);
  }

  patchWorkflow(workflow: Workflow): Observable<Workflow> {
    console.log('patchWorkflow : ' + JSON.stringify(workflow));
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/${workflow.idWorkflow}`;
    return this.http.patch<Workflow>(url, workflow);
  }

  eliminaWorkflow(workflow: Workflow): Observable<void> {
    console.log('eliminaWorkflow : ' + JSON.stringify(workflow));
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/${workflow.idWorkflow}`;
    return this.http.delete<void>(url);
  }

  getWorkflow(idWorkflow: number): Observable<Workflow> {
    const url: string = this.config.getBEServer() + `/moonbobl/restfacade/be/workflows/${idWorkflow}`;
    let parametri = new HttpParams();
    parametri = parametri.append('fields', 'statoPartenza,statoArrivo');
    return this.http.get<Workflow>(url, { params: parametri }).pipe(
      catchError(error => this.handleServiceError(error, 'getWorkflow'))
    );
  }
  getCustomExportCsv(idModulo: number, codice_estrazione:string, created_start: Date, created_end: Date){
    const url: string = this.config.getBEServer() + '/moonbobl/restfacade/be/istanze/modulo/' + idModulo + '/custom-json';
    
    let parametri = new HttpParams();
    if(codice_estrazione){
      parametri = parametri.append('codice_estrazione', '' + codice_estrazione)
    }
    if(created_start){
      const inputDateStr = created_start;
      const inputDate = new Date(inputDateStr);
      const outputDate = new Date(inputDate.getTime() - (inputDate.getTimezoneOffset() * 60000));
      const outputDateStr = outputDate.toISOString();
      parametri = parametri.append('created_start', '' + outputDateStr);
    }

    if(created_end){
      const inputDateStr = created_end;
      const inputDate = new Date(inputDateStr);
      const outputDate = new Date(inputDate.getTime() - (inputDate.getTimezoneOffset() * 60000));
      const outputDateStr = outputDate.toISOString();
      parametri = parametri.append('created_end', '' + outputDateStr);
    }
    console.log(parametri);
    
    return this.http.get<any>(url, { params: parametri,observe: 'response', responseType: 'text' as 'json'}); 
  }
}
