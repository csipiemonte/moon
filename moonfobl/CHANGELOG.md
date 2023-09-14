# Changelog MOOnFOBL
Tutte le modifiche importanti a questo progetto sono documentate in questo file.

Il formato del file è basato su [Keep Changelog](https://keepachangelog.com/en/1.0.0/)\
Questo progetto aderisce a [Semantic Versioning](https://semver.org/spec/v2.0.0.html).
- Added: nuove funzionalità
- Changed: modifiche a funzionalità esistenti
- Fixed: bug fix
- Removed: funzionalità rimosse nella release
- Security: per invitare gli utilizzatori ad aggiornare in caso di vulnerabilità
- Deprecated: funzionalità rimosse nelle future release


## [3.0.0] - 2023-08-31 
### Added
- Versione iniziale per pubblicazione su Developers. Rif. tag COLLAUDO-3.0.0-032
- Invocazione servizio REST icerca CatastoByTripleta
- Login without IRIDE
- PublishOnMyDocsTask
- Invocazione servizio REST /file?signed=all|true|false|pdf_all|pdf_true|pdf_false  per verifica firma via DoSign
- Integrazione api REST /file/retrieve-content-type
- Integrazione api REST /file?filter=jpg,jpeg,pdf
### Changed
- Allineamento SendMail con LIMIT 20MB 
### Fixed
- Completed DAO Processo Workflow Azione StatoWf
### Removed
- jettison-1.3.1.jar
### Security
- Migrate org.codehaus to com.fasterxml.jackson

### Deprecated








