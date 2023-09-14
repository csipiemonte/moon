# Prodotto 
MOOn - Modulistica On Line
# Descrizione del prodotto 
Il progetto “MOOn” ha come obiettivo gestire tutto il ciclo della modulistica delle Pubblica Amministrazione dalla creazione del modulo alla relativa pubblicazione online.
**moonbowcl**: ([Github repository](https://github.com/csipiemonte/moon/moonbowcl)): Componente front-end che relativa al back-office di Moon. Il back-office consente agli enti di costruire,configurare e pubblicare i moduli destinati ai cittadini.

# Prerequisiti di sistema 
Angular : 13.3.4  
Node: 16.14.0  
Database: Postgres 9.2  

# Installazione
- I file di configurazione per i relativi environment sono presenti nella cartella /buildfiles (environment.dev.ts,...)
- Eseguire il comando di build: ng build --configuration=<environment> --base-href=""
- La build della versione è presente nella cartella **dist**
- I file generati dal processo di build devono essere copiati nella cartella **src/web** della componente moonbobl

# Versioning  
Per il versionamento del codice si usa la tecnica [Semantic Versioning](http://semver.org)

# Copyrights 
© Copyright C.S.I. Piemonte - 2023

# License 
La soluzione MOOn è rilasciata con licenza EUPL 1.2, in linea con le altre soluzioni rilasciate dal CSI-Piemonte, quale licenza elaborata e supportata dall'Unione Europea (https://joinup.ec.europa.eu/collection/eupl/join-eupl-licensing-community). Rispetto alle indicazioni di cui alle Linee Guida sull'acquisizione e riuso del software da parte delle PA, cui il CSI per analogia si ispira, si è considerata adottabile in combinata ragione dell'esperienza maturata rispetto a detto strumento dall'OSPO interno, nonché data comunque la sua natura di network license, utile per quanto di interesse a garantire tutela paragonabile alla AGPL (https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence e https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-15) e, non in ultimo, della sua maggiore compatibilità in downstream (anche rispetto a GPL e AGPL: https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-18) nonchè per profiil di compatibilità lato upstream.  
Il software adotta librerie rilasciate con licenza Apache\
SPDX-License-Identifier: EUPL-1.2-or-later\
Vedere il file LICENSE per i dettagli.
