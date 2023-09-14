/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UtenteEnteAbilitato } from 'src/app/model/dto/utente ente-abilitato';
import { SharedService } from 'src/app/services/shared.service';
import { MoonboblService } from 'src/app/services/moonbobl.service';
import { Utente } from 'src/app/model/dto/utente';
import { AlertService } from 'src/app/modules/alert';
import { MoonboError } from 'src/app/model/common/moonbo-error';
import { CodiceDescrizione } from 'src/app/model/dto/codice-descrizione';

@Component({
  selector: 'app-utenti-dettaglio-dati-generali',
  templateUrl: './dati-generali.component.html',
  styleUrls: ['./dati-generali.component.scss']
})
export class DatiGeneraliComponent implements OnInit {

  @Input() utenteSelezionato: UtenteEnteAbilitato;
  @Output() eventModifica = new EventEmitter<UtenteEnteAbilitato>();

  utente: Utente = new Utente();
  utenteEdit: Utente = new Utente();

  isAdmin = false;
  editMode = false;
  tipiUtente = [
    { codice: 'ADM', descrizione: 'utente admin' },
    { codice: 'PA', descrizione: 'utente di backoffice' },
    { codice: 'RUP', descrizione: 'utente rupar di frontoffice' },
    { codice: 'CIT', descrizione: 'utente internet di frontoffice' }];
  tipoUtenteEdit: CodiceDescrizione = new CodiceDescrizione();

  alertOptions = {
    id: 'alert-utenti-dati-generali',
    autoClose: true,
    keepAfterRouteChange: false
  };

  constructor(
    private moonboblService: MoonboblService,
    private sharedService: SharedService,
    protected alertService: AlertService) {
    this.isAdmin = this.sharedService.UserLogged?.isTipoADMIN();
  }

  ngOnInit(): void {
    log('dati-generali::ngOnInit()'); // utenteSelezionato = ' + this.utenteSelezionato);
    this.moonboblService.getUtenteById(this.utenteSelezionato.idUtente).subscribe(
      (res) => {
        log('dati-generali::ngOnInit() moonboblService.getUtenteById() res = ' + JSON.stringify(res));
        this.utente = res;
        log('dati-generali::ngOnInit() moonboblService.getUtenteById() this.utente = ' + JSON.stringify(this.utente));
        // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
        this.aggiornaUtenteEditFromUtente();
      },
      (err) => {
        alert(err.errorMsg);
        //        this.spinnerService.hide();
      }
    );
  }

  private aggiornaUtenteEditFromUtente() {
    this.utenteEdit = { ...this.utente };
    this.tipoUtenteEdit.codice = this.utenteEdit.tipoUtente?.codice;
    log('dati-generali::aggiornaUtenteEditFromUtente() this.utenteEdit = ' + JSON.stringify(this.utenteEdit));
    log('dati-generali::aggiornaUtenteEditFromUtente() this.tipoUtenteEdit.codice = ' + this.tipoUtenteEdit.codice);
  }

  salva() {
    const patchUtente = this.retreivePatchUtente(this.utente, this.utenteEdit);
    if (JSON.stringify(patchUtente) === '{}') {
      this.alertService.warn('Nessun modifica, nessun salvataggio', this.alertOptions);
    } else {
      this.moonboblService.aggiornaUtente(patchUtente).subscribe(
        (res) => {
          this.alertService.success('Salvataggio dati generali effettuato con successo !', this.alertOptions);
          log('dati-generali::salva() moonboblService.getUtenteById() res = ' + JSON.stringify(res));
          this.utente = res;
          log('dati-generali::salva() moonboblService.getUtenteById() this.utente = ' + JSON.stringify(this.utente));
          // Init degli attributi Editabile per il FORM (ci vorrebbe un clone() )
          this.aggiornaUtenteEditFromUtente();
          // @Output() eventModifica
          this.aggiornaUtenteSelezionatoFromUtente();
          log('dati-generali::salva() this.eventModifica.emit(utenteModificato);' + JSON.stringify(this.utenteSelezionato));
          this.eventModifica.emit(this.utenteSelezionato);
        },
        (err: MoonboError) => {
          this.alertService.error('Impossibile effettuare il salvataggio dati generali !' + err.errorMsg, this.alertOptions);
        }
      );
    }
  }

  private aggiornaUtenteFromUtenteEdit() {
    this.utente = { ...this.utenteEdit };
  }

  private aggiornaUtenteSelezionatoFromUtente() {
    this.utenteSelezionato.cognome = this.utente.cognome;
    this.utenteSelezionato.nome = this.utente.nome;
    this.utenteSelezionato.identificativoUtente = this.utente.identificativoUtente;
    this.utenteSelezionato.email = this.utente.email;
    this.utenteSelezionato.tipoUtente = this.utente.tipoUtente;
    log('dati-generali::aggiornaUtenteSelezionatoFromUtente() this.utenteSelezionato = ' + this.utenteSelezionato);
  }

  private retreivePatchUtente(oldUtente: Utente, newUtente: Utente): Utente {
    const result = new Utente();
    if (oldUtente?.cognome === newUtente?.cognome) {
      log('Unchanged cognome ' + oldUtente?.cognome);
    } else {
      log('Changed cognome  FROM: ' + oldUtente?.cognome + ' TO: ' + newUtente?.cognome);
      result.idUtente = newUtente.idUtente;
      result.cognome = newUtente.cognome;
    }
    if (oldUtente?.nome === newUtente?.nome) {
      log('Unchanged cognome ' + oldUtente?.nome);
    } else {
      log('Changed nome  FROM: ' + oldUtente?.nome + ' TO: ' + newUtente?.nome);
      result.idUtente = newUtente.idUtente;
      result.nome = newUtente.nome;
    }
    if (oldUtente?.identificativoUtente === newUtente?.identificativoUtente?.toUpperCase()) {
      log('Unchanged identificativoUtente ' + oldUtente?.identificativoUtente);
    } else {
      log('Changed identificativoUtente  FROM: ' + oldUtente?.identificativoUtente + ' TO: '
        + newUtente?.identificativoUtente?.toUpperCase());
      result.idUtente = newUtente.idUtente;
      result.identificativoUtente = newUtente.identificativoUtente?.toUpperCase();
    }
    if (oldUtente?.email === newUtente?.email?.toLowerCase()) {
      log('Unchanged email ' + oldUtente?.email);
    } else {
      log('Changed email  FROM: ' + oldUtente?.email + ' TO: ' + newUtente?.email?.toLowerCase());
      result.idUtente = newUtente.idUtente;
      result.email = newUtente.email?.toLowerCase();
    }
    if (oldUtente?.tipoUtente?.codice === this.tipoUtenteEdit.codice) {
      log('Unchanged tipoUtente.codice ' + oldUtente?.tipoUtente?.codice);
    } else {
      log('Changed tipoUtente.codice  FROM: ' + oldUtente?.tipoUtente?.codice + ' TO: ' + this.tipoUtenteEdit.codice);
      result.idUtente = newUtente.idUtente;
      const newtipoUtente = new CodiceDescrizione();
      newtipoUtente.codice = this.tipoUtenteEdit.codice;
      result.tipoUtente = newtipoUtente;
    }
    if (oldUtente?.flagAttivo === newUtente?.flagAttivo) {
      log('Unchanged flagAttivo ' + oldUtente?.flagAttivo);
    } else {
      log('Changed flagAttivo  FROM: ' + oldUtente?.flagAttivo + ' TO: ' + newUtente?.flagAttivo);
      result.idUtente = newUtente.idUtente;
      result.flagAttivo = newUtente.flagAttivo;
    }
    log('retreivePatchUtente result : ' + JSON.stringify(result));
    return result;
  }

}

function log(a: any) {
//  console.log(a);
}
