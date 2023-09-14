# MOONPRINT - url-001-moonprint - Oggetti MOON

## Generale
Moonprint contiene servizi di generazione del PDF.
Viene utato per la generazione dei PDF delle istanze con l'uso del template generico di default
Viene anche usao con ulteriore template come per esempio per la generazione della ricevuta di richiesta di CambioIndirizzo con uso del template


### BaseUrl {endpoint}
`http://localhost:10110/moonsrv/restfacade/be`

### Service
* GET  /ping
* GET  /pdf/mock
* POST /pdf                                 OLD Funziona ma non da RESTeasyClient (moonsrv)
* POST /pdf/template/{codiceTemplate}       TEST Funziona ma non da RESTeasyClient (moonsrv)
* GET  /pdf?template=default&doc={...}      Usato da moonbo ; OLD Usato da moonsrv MoonprintDAOImpl (fino a 2.3.3) ; ancora usato da moonbo
* POST /pdf?template=default                Usato da moonsrv MoonprintDAOImpl  da 2.3.3  12/05/2021

### BasicAuth {auth}
L'accesso è protetto alla baseUrl è protetto con *BasicAuth* su wildfly. Solo gli utenti con ruolo "moonprint" possono fruire dei servizi moonprint. 


### Intallazione configurazione template esterni

Creare il fi configurazione sotto jboss/wildfly :  \standalone\configuration\pdfRenderer.properties
Il file deve contenere la variabile 'TemplatesPath con il percorso completo : TemplatesPath=PATH_COMPLETO
es.1 win : TemplatesPath=D:\\Temp\\MoonPrintPdf
es.2 unix: TemplatesPath=/usr/prod/moon/coto-01/moonprint/moonprint_fs
seguendo nomenclatura CSI : /usr/prod/nome_prodotto/linea_cliente/nome_componente/nome_componente_fs
mkdir -p moon/{coto-01/moonprint/moonprint_fs}

Sotto TemplatesPath (/usr/prod/moon/coto-01/moonprint/moonprint_fs), devono essere presenti :
- MOOnPrintRenderer.cfg
- /templates
- /templates/CambioResidenzaAccettata/
- /templates/CambioResidenzaAccettata/CambioResidenzaAccettata.jar
- /templates/CambioResidenzaAccettata/data.json
- /templates/CambioResidenzaAccettata/root.html
- /templates/CambioResidenzaAccettata/style.css
- /templates/CambioResidenzaAccettata/template.xhtml
- /templates/CambioResidenzaAccettata/fonts/*
- /templates/CambioResidenzaAccettata/img/*


## /ping:
Il ping va fino allo livello Service di MoonPrint (moonprint non accede al database).

<http://localhost:10110/moonprint/restfacade/be/ping>

moonprint backend service OK. [BackendServiceImpl.getMessage()]



## GET /pdf/mock:

<http://localhost:10110/moonprint/restfacade/be/pdf/mock>

Request:

```
GET /pdf/mock HTTP/1.1
```

Response:

```

```



## POST /pdf:

Nel boby deve essere passato in string :
<codiceTemplate>|{...json del documento...}

Request:

```
POST /pdf HTTP/1.1
default|{...json del documento...}
```

Response:

```

```


## POST /pdf/template/{codiceTemplate}:

<http://localhost:10110/moonprint/restfacade/be/pdf/template/default>

Request:

```
POST /pdf/template/default HTTP/1.1
{
json del documento
}
```

Response:

```

```


## GET /pdf:
Questo è il servizio finalmente usato da MoonSRV per generare i PDF.

Request:

```
GET /pdf?template=default&doc={...json del documento...} HTTP/1.1
```

Response:

```

```


## Adeguamento jboss
La componente necessita delle seguenti librerie per poter funzionare correttamente
- jackson-core-2.10.0.jar /jackson-core
- jackson-annotations-2.10.0 /jackson-annotations
- jackson-databind-2.10.0 jackson-databind
da installare in modules/system/layers/base/com/fasterxml/jackson/core

## Comando per convertire certificato binario in pem
openssl x509 -in sicurezzapostale.it.cer -inform der -out sicurezzapostale.it.pem -outform pem