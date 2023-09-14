# Prodotto 
MOOn - Modulistica On Line
# Descrizione del prodotto 
Il progetto “MOOn” ha come obiettivo gestire tutto il ciclo della modulistica delle Pubblica Amministrazione dalla creazione del modulo alla relativa pubblicazione online.
**moonsrv**: ([Github repository](https://github.com/csipiemonte/moon/moonsrv)): realizza i servizi comuni alle componenti relative ai cittadini ed alla pubblica amministrazione.
I servizi sono esposti mediante api rest con basic authentication.


# Prerequisiti di sistema 
Java version : OpenJDK 11  
Ant version : 1.8.4   
Database: Postgres 9.2  
Application Server: Wildfly 17.0.0  
Web Server: Apache 2.4



# Installazione
- Definire in wildfly un datasource con jndi name MoonDS che punti al database di Moon.
- Creare in wildfly i seguenti role: moon,moonprint
- Creare in wildfly i seguenti utenti assegnandogli il role corrispondente come nella tabella seguente  
<center>

| User | Role |
| --- | --- |
| moonfo | moon |
| moonsrv | moonprint |
| moonbo | moon,moonprint |
</center>

- Nella directory **buildfiles** sono presenti i file di properties contenenti i valori delle proprietà riferite al rispettivo environment. Valorizzare le proprietà nel rispettivo file di environment per cui si vuole effettuare la build.
- Compilare i sorgenti con apache ant mediante il comando ant -Dtarget=<environment>. Dove environment rappresenta il file di properties relativo all'ambiente per cui si vuole effettuare la build. I file di definizione degli environment si trovano sotto la cartella buildfiles con estrensione .properties.
Ad esempio se si vuole effettuare la build per environment dev, verificare la presenza del file dev.properties nella cartella buildfiles ed eseguire il comando: ant -Dtarget=dev.
- Al termine del build il file .ear prodotto viene generato nella persorso buil/archive/dev.
Deploiare il file ear in wildfly.

# Versioning  
Per il versionamento del codice si usa la tecnica [Semantic Versioning](http://semver.org)

# Copyrights 
© Copyright C.S.I. Piemonte - 2023

# License 
La soluzione MOOn è rilasciata con licenza EUPL 1.2, in linea con le altre soluzioni rilasciate dal CSI-Piemonte, quale licenza elaborata e supportata dall'Unione Europea (https://joinup.ec.europa.eu/collection/eupl/join-eupl-licensing-community). Rispetto alle indicazioni di cui alle Linee Guida sull'acquisizione e riuso del software da parte delle PA, cui il CSI per analogia si ispira, si è considerata adottabile in combinata ragione dell'esperienza maturata rispetto a detto strumento dall'OSPO interno, nonché data comunque la sua natura di network license, utile per quanto di interesse a garantire tutela paragonabile alla AGPL (https://joinup.ec.europa.eu/collection/eupl/introduction-eupl-licence e https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-15) e, non in ultimo, della sua maggiore compatibilità in downstream (anche rispetto a GPL e AGPL: https://joinup.ec.europa.eu/collection/eupl/how-use-eupl#section-18) nonchè per profiil di compatibilità lato upstream.  
Il software adotta librerie rilasciate con licenza Apache\
SPDX-License-Identifier: EUPL-1.2-or-later\
Vedere il file LICENSE per i dettagli.
