# Changelog MOOnSRV
Tutte le modifiche importanti a questo progetto sono documentate in questo file.

Il formato del file è basato su [Keep Changelog](https://keepachangelog.com/en/1.0.0/)\
Questo progetto aderisce a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
- Added: nuove funzionalità
- Changed: modifiche a funzionalità esistenti
- Fixed: bug fix
- Removed: funzionalità rimosse nella release
- Security: per invitare gli utilizzatori ad aggiornare in caso di vulnerabilità
- Deprecated: funzionalità rimosse nelle future release


## [3.0.0] - 2023-31-08 
### Added
- Versione iniziale per pubblicazione su Developers. Rif. tag COLLAUDO-3.0.0-047
- Attributo ProtocolloAttributoKeys
- api rest /reports/modulo/{}
- api rest /api/moduli
- api rest /aree
- api rest toponomastica ricerca CatastoByTripleta
- api rest /azioni
- api rest /stati
- api rest /firma/has-firma-pades-file/{idFile}
- api rest /firma/verifica-firma-file/{idFile}
- api rest /istanze/{}/data
- StoricoWorkflow.idDatiazione Long
- api rest /moduli/codice/{}/epay-manager-name
- api rest /extra/doc/mydocs
- MaggioliSoapProtocollo
- Rinvio Mail singolo e multiple per idTag
- parametri subject_rinvio e text_rinvio
### Changed
- Valore parametro LIMIT_MAX_DIMENSIONE_TOTALE_ALLEGATI = 20 * MB
- api rest /civici: aggiunto filtro per "stati"
- api rest /moduli: aggiunti campi codiceEnte, codiceArea, codiceAmbito in ModuloFilter
- api rest /extra/tecno/remedy (TroubleTiketing Remedy)
- IstanzaFilter e IIstanzaDAOImpl: allineati con BO & Add idTag
 
### Fixed
- ComponentiPagamento for EDIL_DEF_COND
- Completato DAO Processo Workflow con Azione StatoWf
- Completato servizio /extra/territorio/toponomastica/vie ?filter=false OR ?filter=N per vedere tutte le vie
- Allineamento con FO extractFormIoFileNameByCampo return result

### Removed
- Attributo riportaInBozzaAbilitato

### Security


### Deprecated








