# Prodotto 
MOOn - Modulistica On Line
# Descrizione del prodotto 
Il progetto “MOOn” ha come obiettivo gestire tutto il ciclo della modulistica delle Pubblica Amministrazione dalla creazione del modulo alla relativa pubblicazione online.
Consente ai cittadini di compilare online le richieste su particolari servizi che la P.A. mette a disposizone e digitalizza l'intero processo di gestione delle richieste.
Poichè l'accesso ai servizi avviene tramite internet, MOOn minimizza l'accesso fisico allo sportello da parte dei cittadini e consente la gestione della richiesta da parte dell'area preposta mediante apposito back-office.
L'obiettivo del progetto è quello di consentire alle pubbliche amministrazione di digitalizzare i servizi rivolti ai cittadini elevando il livello di trasparenza verso questi ultimi.

Il prodotto è composto dalle componenti:
- **moonfowcl** ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonfowcl)): componente frontend che realizza l'interfaccia utilizzata dai cittadini per accedere ai servizi messi a disposizione dalla P.A. Realizzata come SAP (Single Page Application) mediante il framework Angular,
- **moonfobl** ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonfobl)):  contiene i servizi che realizzano la logica di business utilizzati dalla componente frontend dei cittadini ,
- **moonbowcl** ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonbowcl)): componente frontend che realizza l'interfaccia utilizzata dalla pubblica amministrazione per gestire le richieste pervenute. Realizzata come SAP (Single Page Application) mediante il framework Angular,
- **moonbobl**: ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonbobl)): contiene i servizi che realizzano la logica di business utilizzati dalla componente frontend della pubblica amministrazione,
- **moonsrv**: ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonsrv)): realizza i servizi comuni alle componenti relative ai cittadini ed alla pubblica amministrazione,
- **moonprint**: ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moonprint)): componente per la stampa delle istanze,
- **moondb**: ([Directory sorgenti](https://github.com/csipiemonte/moon/tree/main/moondb)): script per la creazione del database utilizzato dalla piattaforma

# Prerequisiti di sistema 
Java version : OpenJDK 11  
Ant version : 1.8.4  
Angular : 13.3.4  
Node: 16.14.0  
Database: Postgres 9.2  
Application Server: Wildfly 17.0.0  
Web Server: Apache 2.4

# Installazione
Vedere README dei singoli componenti

# Versioning  
Per il versionamento del codice si usa la tecnica [Semantic Versioning](http://semver.org)

# Authors 
Angela Carzedda\
Alberto Deiro\
Sonia Casciano\
Lara Terlizzi\
Danilo Mosca\
Salvatore Cavalli\
Laurent Pissard\
Moinuddin Ahmad\
Valeria Malacasa\
Irene Zablotna\
Paolo Lombardo\
Francesco Zucaro
# Copyrights 
© Copyright C.S.I. Piemonte - 2023

# License 
La soluzione MOOn è rilasciata con licenza EUPL 1.2, in linea con le altre soluzioni rilasciate dal CSI-Piemonte, quale licenza elaborata e supportata dall'Unione Europea (https://joinup.ec.europa.eu/collection/eupl/join-eupl-licensing-community). Rispetto alle indicazioni di cui alle Linee Guida sull'acquisizione e riuso del software da parte delle PA, cui il CSI per analogia si ispira, si è considerata adottabile in combinata ragione dell'esperienza maturata rispetto a detto strumento dall'OSPO interno, nonché data comunque la sua natura di network license, utile per quanto di interesse a garantire tutela paragonabile alla AGPL (https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence e https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-15) e, non in ultimo, della sua maggiore compatibilità in downstream (anche rispetto a GPL e AGPL: https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-18) nonchè per profiil di compatibilità lato upstream.  
Il software adotta librerie rilasciate con licenza Apache\
SPDX-License-Identifier: EUPL-1.2-or-later\
Vedere il file LICENSE per i dettagli.
