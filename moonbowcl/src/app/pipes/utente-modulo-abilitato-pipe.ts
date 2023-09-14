/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

import { UtenteModuloAbilitato } from './../model/dto/utente modulo-abilitato';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'formatUtente' })
export class UtenteModuloAbilitatoPipe implements PipeTransform {
    transform(utente: UtenteModuloAbilitato, args?: string): string {
        if (utente) {
            switch (args) {
                case 'cn':
                    return (utente.cognome + ' ' + utente.nome).toLowerCase();
                case 'CN':
                    return (utente.cognome + ' ' + utente.nome).toUpperCase();
                case 'nc':
                    return (utente.nome + ' ' + utente.cognome).toLowerCase();
                case 'NC':
                    return (utente.nome + ' ' + utente.cognome).toUpperCase();
                default:
                    return utente.cognome + ' ' + utente.nome;
            }
        } else {
            return '';
        }
    }
}
