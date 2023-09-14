/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

export let DecodificaAzione = {

  INVIA: {id: 1, codice: 'INVIA', nome: 'Invia'},
  SALVA_BOZZA: {id: 2, codice: 'SALVA_BOZZA', nome: 'Salva in bozza'},
  ELIMINA: {id: 3, codice: 'ELIMINA', nome: 'Elimina'},
  ARCHIVIA: {id: 4, codice: 'ARCHIVIA', nome: 'Archivia'},
  GESTISCI_PAGAMENTO: {id: 28, codice: 'GESTISCI_PAGAMENTO', nome: 'Avvia pagamento'},
  COMPLETA: {id: 10, codice: 'COMPLETA', nome: 'Completa'},
  PAGA_ONLINE: {id: 60, codice: 'PAGA_ONLINE', nome: 'Pagamento online'},
  PAGA_SPORTELLO: {id: 61, codice: 'PAGA_SPORTELLO', nome: 'Stampa avviso di pagamento'},
  RIPORTA_IN_BOZZA: {id: 17, codice: 'RIPORTA_IN_BOZZA', nome: 'Riporta in bozza'},
  MODIFICA: {id: 17, codice: 'RIPORTA_IN_BOZZA', nome: 'Modifica'}
};

// Using:
// (<Decodifica>DecodificaAzione['INVIA']).id
// OR
// (<Decodifica>DecodificaAzione['INVIA']).codice
