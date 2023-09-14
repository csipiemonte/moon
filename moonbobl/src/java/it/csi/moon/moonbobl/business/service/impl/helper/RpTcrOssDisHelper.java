/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.helper;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrAccoglimentoEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.RicevutaTcrDiniegoEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.regp.TasseOsservazioniDiscaricoEntity;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;


/**
 * Helper per operazioni sul json data delle istanze
 * 
 * @author Laurent
 *
 * @since 1.0.0
 */
public class RpTcrOssDisHelper extends DatiIstanzaHelper {
	
	private final static String CLASS_NAME = "RpTcrOssDisHelper";
	
	
	/*  PER FABIO
	 *  DEFINIRE QUI TuTTI I TESTI
	 *  
	 *  			
			    varieNonMotivata
				varieMotivazioneIrrilevante
				varieDomandaIrricevibile
				varieNessunaRichiesta
				prescrizioneTreTerzo
				pagamentoRicevutaErrata
				pagamentoTributoErrato
				pagamentoSanzioneRitardo
				pagamentoSanzioneRidotta
				pagamentoRilascioRinnovo
				pagamentoPescatoriSanzione
				presuppostoCessazioneSuccessiva
				presuppostoRiduzioneSuccessiva
				presuppostoRiduzioneIncerta
				presuppostoautorizzazionidiverse
				presuppostoFarmacieRurali
	 */
	
	// Motivi diniego per Osservazioni
	private final static String strVarieNonMotivata = "La domanda è priva di motivazione, in quanto in essa non è spiegato il motivo per cui si è ritenuto di poter chiedere l'annullamento, nè ad essa è allegata alcuna documentazione dalla quale tale motivo possa eventualmente essere compreso";
	private final static String strVarieMotivazioneIrrilevante = "La motivazione posta a sostegno della domanda è irrilevante o priva di fondamento giuridico, e pertanto inidonea giustificare la richiesta di annullamento.";
	private final static String strVarieDomandaIrricevibile = "Gli atti impositivi (avvisi di accertamento, cartelle o ingiunzioni di pagamento, atti di riscossione) devono essere contestati o impugnati nel termine di sessanta giorni dalla data di notificazione; di conseguenza la contestazione sollevata oltre detto termine rende la domanda irricevibile.";
	private final static String strVarieNessunaRichiesta = "Secondo la legge, le tasse sulle concessioni regionali sono gestite in regime di autoliquidazione, e devono quindi essere pagate, entro il 31 gennaio di ciascun anno, dal titolare della concessione od autorizzazione senza che debba intervenire alcuna richiesta od invito da parte dell'amministrazione.";
	private final static String strPrescrizioneTreTerzo = "Per le tasse sulle concessioni regionali il termine di prescrizione è quello di cui all'articolo 10 della legge regionale 6 marzo 1980, n. 13, e successive modificazioni ed integrazioni, che lo fissa al terzo anno e non, come talvolta surrettiziamente sostenuto, dopo tre anni dalla data in cui il versamento è stato o doveva essere eseguito o la violazione è stata commessa.";
	private final static String strPagamentoRicevutaErrata = "La ricevuta allegata alla domanda a prova dell'avvenuto pagamento non è quella riferita all'anno in contestazione.";
	private final static String strPagamentoTributoErrato = "La ricevuta allegata alla domanda o i cui estremi sono riportati nella medesima a prova dell'avvenuto pagamento non è riferita a tasse sulle concessioni regionali nè ad altri tributi regionali.";
	private final static String strPagamentoSanzioneRitardo = "Premesso che il pagamento delle tasse sulle concessioni regionali deve essere eseguito entro il 31 gennaio di ogni anno, l'emissione della cartella è stata disposta per la sola applicazione della sanzione per ritardato pagamento (vale a dire della sanzione di cui all'articolo 13 del decreto legislativo 18 dicembre 1997, n. 473, determinata in misura pari al trenta per cento della tassa pagata in ritardo), e degli eventuali interessi di mora. e, infatti, come risulta agli atti ed è del resto confermato dalla stessa documentazione allegata alla domanda, la tassa è stata pagata oltre il termine prescritto dalla legge.";
	private final static String strPagamentoSanzioneRidotta = "Premesso che il pagamento delle tasse sulle concessioni regionali deve essere eseguito entro il 31 gennaio di ogni anno, l'emissione della cartella è stata disposta per la sola applicazione della sanzione per ritardato pagamento (vale a dire della sanzione di cui all'articolo 13 del decreto legislativo 18 dicembre 1997, n. 473, determinata in misura pari al trenta per cento della tassa pagata in ritardo), e degli eventuali interessi di mora. la sanzione in misura ridotta cui la richiesta si riferisce e prevista dalle norme sul ravvedimento deve essere versata spontaneamente dal contribuente contestualmente alla tassa e agli interessi se dovuti. se invece, come nel caso presente, si versa la sola tassa la sanzione deve essere applicata in misura intera.";
	private final static String strPagamentoRilascioRinnovo = "Secondo il vigente ordinamento la tassa di rilascio è dovuta all'atto del ritiro della concessione o autorizzazione, mentre la tassa annuale è dovuta entro il 31 gennaio di ciascun anno a partire dall'anno successivo a quello di rilascio. si tratta dunque di due diverse quote del tributo, legate ciascuna a due diverse situazioni, la prima riguardante l'evento cui consegue il diritto all'esercizio dell'attività connessa al provvedimento, che può verificarsi in qualunque momento dell'anno, la seconda alla continuità, periodicamente verificabile, nell'esercizio di tale diritto. L'obbligo di corrispondere la tassa annuale, perciò, sussiste con il decorso dell'anno, indipendentemente dal fatto che nell'anno precedente, in data particolarmente ravvicinata, si sia già dovuta corrispondere la tassa di rilascio.";
	private final static String strPagamentoPescatoriSanzione = "Come già spiegato nello stesso avviso di accertamento, l'esercizio della pesca nelle acque della regione piemonte è disciplinato dalla legge regionale 29 dicembre 2006, n. 37, e successive modificazioni ed integrazioni. secondo l'articolo 19, comma 1, del capo iv di tale legge, l'esercizio della pesca è subordinato al possesso di valida licenza; conseguentemente, a norma dell'articolo 26, comma 1, lettera j, del capo v, chiunque peschi senza licenza o con licenza non valida è soggetto ad una sanzione amministrativa: il versamento da Lei eseguito è appunto riferito a tale sanzione. la licenza, tuttavia, è soggetta ad una tassa di concessione annuale (articolo 27 del capo vi) il cui mancato versamento comporta necessariamente, oltre al normale recupero della tassa medesima, l'applicazione delle sanzioni e degli interessi di mora di cui al vigente ordinamento, e l'avviso che le è stato notificato è infatti riferito a questo. si tratta, in sostanza, di due diversi e distinti procedimenti, l'uno relativo all'illecito amministrativo, l'altro al recupero fiscale ed al conseguente ulteriore illecito relativo all'omesso versamento della tassa di concessione.";
	private final static String strPresuppostoCessazioneSuccessiva = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, la cessazione dell'attività, come risulta agli atti ed è del resto confermato dalla stessa documentazione allegata alla domanda, si è verificata in data successiva a quella di scadenza del pagamento.";
	private final static String strPresuppostoRiduzioneSuccessiva = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, ed avuto riguardo alla situazione di fatto e di diritto esistente a tale data, l'autorizzazione alla riduzione di superficie, che comporta la conseguente riduzione dell'imponibile, deriva da un provvedimento in cui è indicata una data di decorrenza successiva a quella in cui l'obbligazione tributaria doveva essere assolta.";
	private final static String strPresuppostoRiduzioneIncerta = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, ed avuto riguardo alla situazione di fatto e di diritto esistente a tale data, l'autorizzazione alla riduzione di superficie, che comporta la conseguente riduzione dell'imponibile, deriva da un provvedimento in cui non è indicata una data di decorrenza, per cui l'autorizzazione medesima non può avere efficacia che dalla data del provvedimento stesso, successiva a quella in cui l'obbligazione tributaria doveva essere assolta.";
	private final static String strPresuppostoautorizzazionidiverse = "Risulta, allo stato degli atti, che l'intestatario fosse, all'epoca, titolare di più autorizzazioni, e la ricevuta esibita a pretesa comprova dell'indebito è relativa a un'autorizzazione diversa da quella in contestazione.";
	private final static String strPresuppostoFarmacieRurali = "Secondo il vigente ordinamento le farmacie classificate rurali sussidiate sono tenute al pagamento della sola tassa annuale di ispezione; detta tassa, tuttavia, è commisurata alla classe demografica del comune (e non della frazione o di altra qualsiasi ripartizione amministrativa, ancorchè riconosciuta dalla legge).";


	//Motivi diniego per Domanda
	private final static String strMotivoNonMotivata = "La domanda è priva di motivazione, in quanto in essa non è spiegato il motivo per cui si è ritenuto di poter chiedere il discarico, nè ad essa è allegata alcuna documentazione dalla quale tale motivo possa eventualmente essere compreso.";
	private final static String strMotivoIrrilevante = "La motivazione posta a sostegno della domanda è irrilevante o priva di fondamento giuridico, e pertanto inidonea giustificare la richiesta di discarico.";
	private final static String strMotivoIrricevibile = "Gli atti impositivi (avvisi di accertamento, cartelle o ingiunzioni di pagamento, atti di riscossione) devono essere contestati o impugnati nel termine di sessanta giorni dalla data di notificazione; di conseguenza la contestazione sollevata oltre detto termine rende la domanda irricevibile.";
	private final static String strMotivoNessunaRichiesta = "Secondo la legge, le tasse sulle concessioni regionali sono gestite in regime di autoliquidazione, e devono quindi essere pagate, entro il 31 gennaio di ciascun anno, dal titolare della concessione od autorizzazione senza che debba intervenire alcuna richiesta od invito da parte dell'Amministrazione";
	private final static String strPrescrizioneCompiutaGiacenza = "La notificazione di idonei avvisi di accertamento, nei confronti dei quali non è stata proposta, nei termini di legge, alcuna impugnazione, per cui sono divenuti definitivi, si dà per avvenuta o comunque iniziata nel corso dei rispettivi anni di competenza per compiuta giacenza, cosi' come prevede la legge, in quanto l'operatore cui era stato affidato il recapito, indipendentemente dalle diciture apposte sulla busta, può fornire prova di aver lasciato avviso a fronte del quale non è stato curato il ritiro del plico depositato.";
	private final static String strDomPagamentoRicevutaErrata = "La ricevuta allegata alla domanda a prova dell'avvenuto pagamento non è quella riferita all'anno in contestazione.";
	private final static String strDomPagamentoTributoErrato = "La ricevuta allegata alla domanda o i cui estremi sono riportati nella medesima a prova dell'avvenuto pagamento non è riferita a tasse sulle concessioni regionali nè ad altri tributi regionali.";
	private final static String strDomPagamentoSanzioneRitardo = "Premesso che il pagamento delle tasse sulle concessioni regionali deve essere eseguito entro il 31 gennaio di ogni anno, l'emissione della cartella è stata disposta per la sola applicazione della sanzione per ritardato pagamento (vale a dire della sanzione di cui all'articolo 13 del decreto legislativo 18 dicembre 1997, n. 473, determinata in misura pari al trenta per cento della tassa pagata in ritardo), e degli eventuali interessi di mora. E, infatti, come risulta agli atti ed è del resto confermato dalla stessa documentazione allegata alla domanda, la tassa è stata pagata oltre il termine prescritto dalla legge.";
	private final static String strDomPagamentoSanzioneRidotta = "Premesso che il pagamento delle tasse sulle concessioni regionali deve essere eseguito entro il 31 gennaio di ogni anno, l'emissione della cartella è stata disposta per la sola applicazione della sanzione per ritardato pagamento (vale a dire della sanzione di cui all'articolo 13 del decreto legislativo 18 dicembre 1997, n. 473, determinata in misura pari al trenta per cento della tassa pagata in ritardo), e degli eventuali interessi di mora. La sanzione in misura ridotta cui la richiesta si riferisce e prevista dalle norme sul ravvedimento deve essere versata spontaneamente dal contribuente contestualmente alla tassa e agli interessi se dovuti. Se invece, come nel caso presente, si versa la sola tassa la sanzione deve essere applicata in misura intera.";
	private final static String strDomPagamentoRilascioRinnovo = "Secondo il vigente ordinamento la tassa di rilascio è dovuta all'atto del ritiro della concessione o autorizzazione, mentre la tassa annuale è dovuta entro il 31 gennaio di ciascun anno a partire dall'anno successivo a quello di rilascio. Si tratta dunque di due diverse quote del tributo, legate ciascuna a due diverse situazioni, la prima riguardante l'evento cui consegue il diritto all'esercizio dell'attività connessa al provvedimento, che può verificarsi in qualunque momento dell'anno, la seconda alla continuità, periodicamente verificabile, nell'esercizio di tale diritto. L'obbligo di corrispondere la tassa annuale, perciò, sussiste con il decorso dell'anno, indipendentemente dal fatto che nell'anno precedente, in data particolarmente ravvicinata, si sia già dovuta corrispondere la tassa di rilascio.";
	private final static String strDomPagamentoPescatoriSanzione = "Come già spiegato nella stessa ingiunzione di pagamento, l'esercizio della pesca nelle acque della Regione Piemonte è disciplinato dalla legge regionale 29 dicembre 2006, n. 37, e successive modificazioni ed integrazioni. Secondo l'articolo 19, comma 1, del capo IV di tale legge, l'esercizio della pesca è subordinato al possesso di valida licenza; conseguentemente, a norma dell'articolo 26, comma 1, lettera j, del capo V, chiunque peschi senza licenza o con licenza non valida è soggetto ad una sanzione amministrativa: il versamento da Lei eseguito è appunto riferito a tale sanzione. La licenza, tuttavia, è soggetta ad una tassa di concessione annuale (articolo 27 del capo VI) il cui mancato versamento comporta necessariamente, oltre al normale recupero della tassa medesima, l'applicazione delle sanzioni e degli interessi di mora di cui al vigente ordinamento, e l'ingiunzione che Le è stata notificata preceduta peraltro a suo tempo da un avviso che non risulta essere mai stato contestato è infatti riferito a questo. Si tratta, in sostanza, di due diversi e distinti procedimenti, l'uno relativo all'illecito amministrativo, l'altro al recupero fiscale ed al conseguente ulteriore illecito relativo all'omesso versamento della tassa di concessione.";
	private final static String strDomPresuppostoCessazioneSuccessiva = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, la cessazione dell'attività, come risulta agli atti ed è del resto confermato dalla stessa documentazione allegata alla domanda, si è verificata in data successiva a quella di scadenza del pagamento.";
	private final static String strDomPresuppostoRiduzioneSuccessiva = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, ed avuto riguardo alla situazione di fatto e di diritto esistente a tale data, l'autorizzazione alla riduzione di superficie, che comporta la conseguente riduzione dell'imponibile, deriva da un provvedimento in cui è indicata una data di decorrenza successiva a quella in cui l'obbligazione tributaria doveva essere assolta.";
	private final static String strDomPresuppostoRiduzioneIncerta = "Premesso che, a norma di legge, è tenuto al pagamento della tassa sulle concessioni regionali chi risulta titolare, al 31 gennaio di ciascun anno, della concessione od autorizzazione cui la tassa medesima si riferisce, ed avuto riguardo alla situazione di fatto e di diritto esistente a tale data, l'autorizzazione alla riduzione di superficie, che comporta la conseguente riduzione dell'imponibile, deriva da un provvedimento in cui non è indicata una data di decorrenza, per cui l'autorizzazione medesima non può avere efficacia che dalla data del provvedimento stesso, successiva a quella in cui l'obbligazione tributaria doveva essere assolta.";
	private final static String strDomPresuppostoAutorizzazioniDiverse = "Risulta, allo stato degli atti, che l'intestatario fosse, all'epoca, titolare di più autorizzazioni, e la ricevuta esibita a pretesa comprova dell'indebito è relativa a un'autorizzazione diversa da quella in contestazione.";
	private final static String strDomPresuppostoFarmacieRurali = "Secondo il vigente ordinamento le farmacie classificate rurali sussidiate sono tenute al pagamento della sola tassa annuale di ispezione; detta tassa, tuttavia, è commisurata alla classe demografica del Comune (e non della frazione o di altra qualsiasi ripartizione amministrativa, ancorchè riconosciuta dalla legge).";
	
	
	//Motivi diniego per rimborso
	private final static String strDomandaNonMotivata = "La motivazione posta a sostegno della domanda è irrilevante o priva di fondamento giuridico, e pertanto inidonea giustificare la richiesta di restituzione.";
	private final static String strDomandaQuotaPrescritta = "Secondo la legge la domanda di rimborso deve essere presentata, pena la decadenza del diritto, entro tre anni dalla data del versamento. Poichè dalla data del versamento a quella in cui è stata presentata la domanda risultano essere trascorsi più di tre anni, quest'ultima è da ritenersi fuori termine.";
	private final static String strDomandaVersamentoCorretto = "Il versamento eseguito risulta corretto: l'importo pagato è infatti quello dovuto sulla base della tariffa in vigore alla data del versamento medesimo e anche il parametro a base del calcolo risulta essere quello esatto.";
	private final static String strDomandaMancaProva = "Non è possibile ottenere prova della duplicazione di pagamento e, com'è noto, in materia di tributi in particolare per quanto riguarda le attestazioni di pagamento la legge non ammette l'autocertificazione.";
	private final static String strDomandaImportoEsiguo = "Secondo la legge non si può procedere alla restituzione se la somma da restituire non supera l'importo di euro 10,33. Nel caso specifico, la differenza tra l'importo dovuto e quello effettivamente versato è inferiore a detta somma, perciò la differenza stessa è inferiore all'importo minimo rimborsabile.";
	private final static String strDomandaCompetenzaAde = "Il versamento non si riferisce ad una tassa sulle concessioni regionali ma ad una tassa sulle concessioni governative pagata a favore dell'Agenzia delle entrate e non a favore della Regione.";
	private final static String strDomandaCompetenzaAltra = "Il versamento si riferisce ad una tassa sulle concessioni regionali pagata a favore di altra Regione e non della Regione Piemonte.";
	private final static String strDomandaVersamentoCompensato = "Dai riscontri eseguiti sull'archivio regionale delle tasse sulle concessioni regionali risulta che, per lo stesso periodo, è stato riconosciuto un credito da portare in compensazione sui futuri pagamenti dovuti.";
	
	
	
	//Osservazioni motivazioni
	private final static String strMotivoCorrezioneParametri = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra il totale dell’accertamento e quello derivato dal ricalcolo della tassa dovuta sulla base dei parametri corretti.";
	private final static String strMotivoSanzioniEredi = "In particolare, è stato disposto lo scomputo della sanzione, non dovuta in quanto non trasmissibile agli eredi, e pertanto, per effetto di quanto sopra, in relazione agli importi posti in riscossione per conto di questa Amministrazione regionale, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla tassa dovuta, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito";
	private final static String strMotivoPescatoriTassa = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla sanzione, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito perché la tassa è stata pagata in data successiva a quella del verbale di infrazione. Come già spiegato nello stesso avviso di accertamento, l’esercizio della pesca nelle acque della Regione Piemonte è disciplinato dalla legge regionale 29 dicembre 2006, n. 37, e successive modificazioni ed integrazioni. Secondo l’articolo 19, comma 1, del capo IV di tale legge, l’esercizio della pesca è subordinato al possesso di valida licenza; conseguentemente, a norma dell’articolo 26, comma 1, lettera j, del capo V, chiunque peschi senza licenza o con licenza non valida è soggetto ad una sanzione amministrativa. La licenza, tuttavia, è soggetta ad una tassa di concessione annuale (articolo 27 del capo VI) il cui mancato versamento comporta necessariamente, oltre al normale recupero della tassa medesima, l’applicazione delle sanzioni e degli interessi di mora di cui al vigente ordinamento. Si tratta, in sostanza, di due diversi e distinti procedimenti, l’uno relativo all’illecito amministrativo, l’altro al recupero fiscale ed al conseguente ulteriore illecito relativo all’omesso versamento della tassa di concessione.";
	private final static String strMotivoSoloTassa = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla sanzione, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito perché la tassa è stata pagata in ritardo senza le maggiorazioni previste dalle norme sul ravvedimento.";
	private final static String strMotivoSoloAcconto = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra importo dell’accertamento e somma pagata, che rimane a debito perché il pagamento è stato eseguito dopo che l’accertamento era già stato emesso e notificato.";
	private final static String strMotivoPagamentoInsufficiente = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra importo dell’accertamento e somma pagata, che rimane a debito perché il pagamento di cui alla ricevuta esibita è insufficiente.";
	
	
	//Osservazioni motivazion1
	private final static String motivoCorrezioneParametri = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra il totale dell’ingiunzione e quello derivato dal ricalcolo della tassa dovuta sulla base dei parametri corretti.";
	private final static String motivoSanzioniEredi = "In particolare, è stato disposto lo scomputo della sanzione, non dovuta in quanto non trasmissibile agli eredi, e pertanto, per effetto di quanto sopra, in relazione agli importi posti in riscossione per conto di questa Amministrazione regionale, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla tassa dovuta, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito.";
	private final static String motivoPescatoriTassa = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla sanzione, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito perché la tassa è stata pagata in data successiva a quella del verbale di infrazione. Come già spiegato nello stesso avviso di accertamento, l’esercizio della pesca nelle acque della Regione Piemonte è disciplinato dalla legge regionale 29 dicembre 2006, n. 37, e successive modificazioni ed integrazioni. Secondo l’articolo 19, comma 1, del capo IV di tale legge, l’esercizio della pesca è subordinato al possesso di valida licenza; conseguentemente, a norma dell’articolo 26, comma 1, lettera j, del capo V, chiunque peschi senza licenza o con licenza non valida è soggetto ad una sanzione amministrativa. La licenza, tuttavia, è soggetta ad una tassa di concessione annuale (articolo 27 del capo VI) il cui mancato versamento comporta necessariamente, oltre al normale recupero della tassa medesima, l’applicazione delle sanzioni e degli interessi di mora di cui al vigente ordinamento. Si tratta, in sostanza, di due diversi e distinti procedimenti, l’uno relativo all’illecito amministrativo, l’altro al recupero fiscale ed al conseguente ulteriore illecito relativo all’omesso versamento della tassa di concessione.";
	private final static String motivoAltraPartita = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente ad altra partita iscritta nello stesso ruolo e che rimane a debito.";
	private final static String motivoSoloTassa = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla sanzione, agli interessi di mora ed agli eventuali oneri accessori, che rimangono a debito perché la tassa è stata pagata in ritardo senza le maggiorazioni previste dalle norme sul ravvedimento.";
	private final static String motivoSoloAcconto = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra importo dell’ingiunzione e somma pagata, che rimane a debito perché il pagamento è stato eseguito dopo che l’ingiunzione era già stata emessa e notificata.";
	private final static String motivoDopoAccertamento = "Pertanto, per effetto di quanto sopra, rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra importo dell’ingiunzione e somma pagata, che rimane a debito perché il pagamento è stato eseguito dopo che l’accertamento era già stato emesso e notificato.";
	private final static String motivoPagamentoInsufficiente = "Pertanto, per effetto di quanto sopra rimane dovuto e, di conseguenza, è da pagare, il solo importo corrispondente alla differenza fra importo dell’ingiunzione e somma pagata, che rimane a debito perché il pagamento di cui alla ricevuta esibita è insufficiente.";
	
	/* FINE PER FABIO */
	
	public static TasseOsservazioniDiscaricoEntity parse(String datiIstanza) throws BusinessException {
		TasseOsservazioniDiscaricoEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new TasseOsservazioniDiscaricoEntity();
			
			JsonNode contribuente= data.get("contribuente");
			if(contribuente.has("tipoPersona")) {
				if(contribuente.get("tipoPersona").getTextValue().equals("personaFisica")) {
					result.setTipoPersona("personaFisica");
					JsonNode personaFisica=contribuente.get("personaFisica");
					if(personaFisica.has("nome") && personaFisica.get("nome") != null)
						result.setpFnome(personaFisica.get("nome").getTextValue());
					if(personaFisica.has("cognome") && personaFisica.get("cognome") != null)
						result.setpFcognome(personaFisica.get("cognome").getTextValue());
					if(personaFisica.has("codiceFiscale") && personaFisica.get("codiceFiscale") != null)
						result.setpFcodiceFiscale(personaFisica.get("codiceFiscale").getTextValue());
					if(personaFisica.has("dataNascita") && personaFisica.get("dataNascita") != null)
						result.setpFdataNascita(personaFisica.get("dataNascita").getTextValue());
					if(personaFisica.has("luogoDiNascita")
							&& personaFisica.get("luogoDiNascita") != null
							&& personaFisica.get("luogoDiNascita").getTextValue().equals("italia")) {
						result.setpFluogoNascita("Italia");
						if(personaFisica.has("provinciaNascita")
								&& personaFisica.has("comuneNascita")
								&& personaFisica.get("provinciaNascita").has("nome")
								&& personaFisica.get("comuneNascita").has("nome")
								&& personaFisica.get("provinciaNascita").get("nome") != null
								&& personaFisica.get("comuneNascita").get("nome") != null)
							result.setpFluogoNascita(personaFisica.get("comuneNascita").get("nome").getTextValue()+ "("+personaFisica.get("comuneNascita").get("nome").getTextValue()+")");
					}else if(personaFisica.has("luogoDiNascita")
							&& personaFisica.get("luogoDiNascita") != null
							&& personaFisica.get("luogoDiNascita").getTextValue().equals("statoEstero")) {
						result.setpFluogoNascita("Stato estero");
						if(personaFisica.has("statoNascita")
								&& personaFisica.get("statoNascita").has("nome")
								&& personaFisica.get("statoNascita").get("nome") != null)
								
							result.setpFluogoNascita(personaFisica.get("statoNascita").get("nome").getTextValue());
					}
					
				}else if(contribuente.get("tipoPersona").getTextValue().equals("personaGiuridica")) {
					JsonNode personaGiuridica=contribuente.get("personaGiuridica");
					result.setTipoPersona("personaGiuridica");
					if(personaGiuridica.has("nome") && personaGiuridica.get("nome") != null)
						result.setlRnome(personaGiuridica.get("nome").getTextValue());
					if(personaGiuridica.has("cognome") && personaGiuridica.get("cognome") != null)
						result.setlRcognome(personaGiuridica.get("cognome").getTextValue());
					if(personaGiuridica.has("codiceFiscale") && personaGiuridica.get("codiceFiscale") != null)
						result.setlRcodiceFiscale(personaGiuridica.get("codiceFiscale").getTextValue());
					if(personaGiuridica.has("dataNascita") && personaGiuridica.get("dataNascita") != null)
						result.setlRdataNascita(personaGiuridica.get("dataNascita").getTextValue());
					if(personaGiuridica.has("luogoDiNascita")
							&& personaGiuridica.get("luogoDiNascita") != null
							&& personaGiuridica.get("luogoDiNascita").getTextValue().equals("italia")) {
						result.setlRluogoNascita("Italia");
						if(personaGiuridica.has("provinciaNascita")
								&& personaGiuridica.has("comuneNascita")
								&& personaGiuridica.get("provinciaNascita").has("nome")
								&& personaGiuridica.get("comuneNascita").has("nome")
								&& personaGiuridica.get("provinciaNascita").get("nome") != null
								&& personaGiuridica.get("comuneNascita").get("nome") != null)
							result.setlRluogoNascita(personaGiuridica.get("comuneNascita").get("nome").getTextValue()+ "("+personaGiuridica.get("comuneNascita").get("nome").getTextValue()+")");
					}else if(personaGiuridica.has("luogoDiNascita")
							&& personaGiuridica.get("luogoDiNascita") != null
							&& personaGiuridica.get("luogoDiNascita").getTextValue().equals("statoEstero")) {
						result.setlRluogoNascita("Stato estero");
						if(personaGiuridica.has("statoNascita")
								&& personaGiuridica.get("statoNascita").has("nome")
								&& personaGiuridica.get("statoNascita").get("nome") != null)
								
							result.setlRluogoNascita(personaGiuridica.get("statoNascita").get("nome").getTextValue());
					}
					
				}
			}
			JsonNode domicilio=contribuente.get("domicilio");
			if(domicilio.has("indirizzo")
					&& domicilio.get("indirizzo") != null )
				result.setIndirizzo(domicilio.get("indirizzo").getTextValue());
			if(domicilio.has("cap")
					&& domicilio.get("cap") != null )
				result.setCap(domicilio.get("cap").getTextValue());
			if(domicilio.has("provincia")
					&& domicilio.get("provincia").has("sigla")
					&& domicilio.get("provincia").get("sigla") != null) {
				result.setProvincia(domicilio.get("provincia").get("sigla").getTextValue());
			}
			if(domicilio.has("comune")
					&& domicilio.get("comune").has("nome")
					&& domicilio.get("comune").get("nome") != null) {
				result.setComune(domicilio.get("comune").get("nome").getTextValue());
			}
			if(data.has("oggettoDomanda")) {
				JsonNode oggettoDomanda=data.get("oggettoDomanda");
				
				result.setOggettoDomanda(oggettoDomanda.get("tipoDomanda").getTextValue());
				// log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+oggettoDomanda.get("numero").asText());
				result.setNumeroDomanda(oggettoDomanda.get("numero").asText());
				
			}
			
			return result;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
	public static TasseOsservazioniDiscaricoEntity parseRimborso(String datiIstanza) throws BusinessException {
		TasseOsservazioniDiscaricoEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parse] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			
			result = new TasseOsservazioniDiscaricoEntity();
			
			JsonNode contribuente= data.get("titolare");
			if(contribuente.has("tipoPersona")) {
				if(contribuente.get("tipoPersona").getTextValue().equals("personaFisica")) {
					result.setTipoPersona("personaFisica");
					JsonNode personaFisica=contribuente.get("personaFisica");
					if(personaFisica.has("nome") && personaFisica.get("nome") != null)
						result.setpFnome(personaFisica.get("nome").getTextValue());
					if(personaFisica.has("cognome") && personaFisica.get("cognome") != null)
						result.setpFcognome(personaFisica.get("cognome").getTextValue());
					if(personaFisica.has("codiceFiscale") && personaFisica.get("codiceFiscale") != null)
						result.setpFcodiceFiscale(personaFisica.get("codiceFiscale").getTextValue());
					if(personaFisica.has("dataNascita") && personaFisica.get("dataNascita") != null)
						result.setpFdataNascita(personaFisica.get("dataNascita").getTextValue());
					if(personaFisica.has("luogoDiNascita")
							&& personaFisica.get("luogoDiNascita") != null
							&& personaFisica.get("luogoDiNascita").getTextValue().equals("italia")) {
						result.setpFluogoNascita("Italia");
						if(personaFisica.has("provinciaNascita")
								&& personaFisica.has("comuneNascita")
								&& personaFisica.get("provinciaNascita").has("nome")
								&& personaFisica.get("comuneNascita").has("nome")
								&& personaFisica.get("provinciaNascita").get("nome") != null
								&& personaFisica.get("comuneNascita").get("nome") != null)
							result.setpFluogoNascita(personaFisica.get("comuneNascita").get("nome").getTextValue()+ "("+personaFisica.get("comuneNascita").get("nome").getTextValue()+")");
					}else if(personaFisica.has("luogoDiNascita")
							&& personaFisica.get("luogoDiNascita") != null
							&& personaFisica.get("luogoDiNascita").getTextValue().equals("statoEstero")) {
						result.setpFluogoNascita("Stato estero");
						if(personaFisica.has("statoNascita")
								&& personaFisica.get("statoNascita").has("nome")
								&& personaFisica.get("statoNascita").get("nome") != null)
								
							result.setpFluogoNascita(personaFisica.get("statoNascita").get("nome").getTextValue());
					}
					
				}else if(contribuente.get("tipoPersona").getTextValue().equals("personaGiuridica")) {
					JsonNode personaGiuridica=contribuente.get("personaGiuridica");
					result.setTipoPersona("personaGiuridica");
					if(personaGiuridica.has("nome") && personaGiuridica.get("nome") != null)
						result.setlRnome(personaGiuridica.get("nome").getTextValue());
					if(personaGiuridica.has("cognome") && personaGiuridica.get("cognome") != null)
						result.setlRcognome(personaGiuridica.get("cognome").getTextValue());
					if(personaGiuridica.has("codiceFiscale") && personaGiuridica.get("codiceFiscale") != null)
						result.setlRcodiceFiscale(personaGiuridica.get("codiceFiscale").getTextValue());
					if(personaGiuridica.has("dataNascita") && personaGiuridica.get("dataNascita") != null)
						result.setlRdataNascita(personaGiuridica.get("dataNascita").getTextValue());
					if(personaGiuridica.has("luogoDiNascita")
							&& personaGiuridica.get("luogoDiNascita") != null
							&& personaGiuridica.get("luogoDiNascita").getTextValue().equals("italia")) {
						result.setlRluogoNascita("Italia");
						if(personaGiuridica.has("provinciaNascita")
								&& personaGiuridica.has("comuneNascita")
								&& personaGiuridica.get("provinciaNascita").has("nome")
								&& personaGiuridica.get("comuneNascita").has("nome")
								&& personaGiuridica.get("provinciaNascita").get("nome") != null
								&& personaGiuridica.get("comuneNascita").get("nome") != null)
							result.setlRluogoNascita(personaGiuridica.get("comuneNascita").get("nome").getTextValue()+ "("+personaGiuridica.get("comuneNascita").get("nome").getTextValue()+")");
					}else if(personaGiuridica.has("luogoDiNascita")
							&& personaGiuridica.get("luogoDiNascita") != null
							&& personaGiuridica.get("luogoDiNascita").getTextValue().equals("statoEstero")) {
						result.setlRluogoNascita("Stato estero");
						if(personaGiuridica.has("statoNascita")
								&& personaGiuridica.get("statoNascita").has("nome")
								&& personaGiuridica.get("statoNascita").get("nome") != null)
								
							result.setlRluogoNascita(personaGiuridica.get("statoNascita").get("nome").getTextValue());
					}
					
				}
			}
			JsonNode domicilio=contribuente.get("domicilio");
			if(domicilio.has("indirizzo")
					&& domicilio.get("indirizzo") != null )
				result.setIndirizzo(domicilio.get("indirizzo").getTextValue());
			if(domicilio.has("cap")
					&& domicilio.get("cap") != null )
				result.setCap(domicilio.get("cap").getTextValue());
			if(domicilio.has("provincia") && domicilio.get("provincia").has("sigla")) 
			{
				result.setProvincia(domicilio.get("provincia").get("sigla").getTextValue());
			}
			if(domicilio.has("comune") && domicilio.get("comune").has("nome")) {
				result.setComune(domicilio.get("comune").get("nome").getTextValue());
			}
				
			if(data.has("oggettoDomanda")) {
				JsonNode oggettoDomanda=data.get("oggettoDomanda");
				result.setOggettoDomanda(oggettoDomanda.get("tipoDomanda").getTextValue());
			}
			
			// motivoSegnalazione.pagamentoNonDovuto.annoDiRiferimento
			if (data.has("motivoSegnalazione")) {
				JsonNode motivoSegnalazione = data.get("motivoSegnalazione");
				if (motivoSegnalazione.has("pagamentoNonDovuto")) {
					JsonNode pagamentoNonDovuto = motivoSegnalazione.get("pagamentoNonDovuto");
					if (pagamentoNonDovuto.has("annoDiRiferimento")) {
						String annoDiRiferimento = pagamentoNonDovuto.get("annoDiRiferimento").getTextValue();
						result.setAnnoRiferimento(annoDiRiferimento);
					}
				}
			}
			
			return result;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
	
	public static RicevutaTcrDiniegoEntity parseAzioneDiniego(String datiIstanza) throws BusinessException {

		RicevutaTcrDiniegoEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseAzioneDiniego] IN datiIstanza=" + datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode) istanzaNode.get("data");
			int i = 0;
			List<String> motivi_selezionati = new ArrayList<>();
			result = new RicevutaTcrDiniegoEntity();
			if (data.has("rispostaOsservazioni") && data.get("rispostaOsservazioni") != null
					&& !data.get("rispostaOsservazioni").getTextValue().isBlank()) {
				result.setTipologiaTemplate(data.get("rispostaOsservazioni").getTextValue());
				
			} else if ((data.has("rispostaDiscarico") && data.get("rispostaDiscarico") != null
					&& !data.get("rispostaDiscarico").getTextValue().isBlank())) {
				result.setTipologiaTemplate(data.get("rispostaDiscarico").getTextValue());
				
			}
			else if ((data.has("rispostaDomandaRimborso") && data.get("rispostaDomandaRimborso") != null
					&& !data.get("rispostaDomandaRimborso").getTextValue().isBlank())) {
				result.setTipologiaTemplate(data.get("rispostaDomandaRimborso").getTextValue());
				
			}
			
			if (data.has("nome") && data.get("nome") != null)
				result.setNome(data.get("nome").getTextValue());
			if (data.has("cognome") && data.get("cognome") != null)
				result.setCognome(data.get("cognome").getTextValue());
			if (data.has("codiceFiscalePartitaIva") && data.get("codiceFiscalePartitaIva") != null)
				result.setCodiceFiscale(data.get("codiceFiscalePartitaIva").getTextValue());
			if (data.has("indirizzo") && data.get("indirizzo") != null)
				result.setIndirizzo(data.get("indirizzo").getTextValue());
			if (data.has("comune") && data.get("comune") != null)
				result.setCitta(data.get("comune").getTextValue());
			if (data.has("provincia") && data.get("provincia") != null)
				result.setProvincia(data.get("provincia").getTextValue());
			if (data.has("cap") && data.get("cap") != null)
				result.setCap(data.get("cap").getTextValue());
			if (data.has("numeroAccertamento") && data.get("numeroAccertamento") != null)
				result.setNumAccertamento(data.get("numeroAccertamento").getTextValue());
			if (data.has("numeroProtocolloIngresso") && data.get("numeroProtocolloIngresso") != null)
				result.setNumProtIngr(data.get("numeroProtocolloIngresso").getTextValue());
			if (data.has("annoDiPagamento") && data.get("annoDiPagamento") != null)
				result.setAnnoPagamento(data.get("annoDiPagamento").getTextValue());
			if (data.has("scadenza") && data.get("scadenza") != null)
				result.setDataScadenza(data.get("scadenza").getTextValue());

			if (data.has("classificazioneDOQUI")) {
				log.debug("[" + CLASS_NAME + "::parseAzioneDiniego] IN classificazioneDOQUI="
						+ data.get("classificazioneDOQUI").getTextValue());
				result.setClassificazioneDOQUI(data.get("classificazioneDOQUI").getTextValue());
			}
			

			if (data.has("osservazioni")) {
				JsonNode osservazioni = data.get("osservazioni");
				if (osservazioni.has("motivazioni")) {
					result.setTipologiaTemplate("osservazioni" + result.getTipologiaTemplate());
					JsonNode motivi = osservazioni.get("motivazioni");
					if (motivi.has("varieNonMotivata") && motivi.get("varieNonMotivata").getBooleanValue()) {
						motivi_selezionati.add(strVarieNonMotivata);
					}
					if (motivi.has("varieMotivazioneIrrilevante") && motivi.get("varieMotivazioneIrrilevante").getBooleanValue()) {
						motivi_selezionati.add(strVarieMotivazioneIrrilevante);
					}
					if (motivi.has("varieDomandaIrricevibile") && motivi.get("varieDomandaIrricevibile").getBooleanValue()) {
						motivi_selezionati.add(strVarieDomandaIrricevibile);
					}
					if (motivi.has("varieNessunaRichiesta") && motivi.get("varieNessunaRichiesta").getBooleanValue()) {
						motivi_selezionati.add(strVarieNessunaRichiesta);
					}
					if (motivi.has("prescrizioneTreTerzo") && motivi.get("prescrizioneTreTerzo").getBooleanValue()) {
						motivi_selezionati.add(strPrescrizioneTreTerzo);
					}
					if (motivi.has("pagamentoRicevutaErrata") && motivi.get("pagamentoRicevutaErrata").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoRicevutaErrata);
					}
					if (motivi.has("pagamentoTributoErrato") && motivi.get("pagamentoTributoErrato").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoTributoErrato);
					}
					if (motivi.has("pagamentoSanzioneRitardo") && motivi.get("pagamentoSanzioneRitardo").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoSanzioneRitardo);
					}
					if (motivi.has("pagamentoSanzioneRidotta") && motivi.get("pagamentoSanzioneRidotta").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoSanzioneRidotta);
					}
					if (motivi.has("pagamentoRilascioRinnovo") && motivi.get("pagamentoRilascioRinnovo").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoRilascioRinnovo);
					}
					if (motivi.has("pagamentoPescatoriSanzione") && motivi.get("pagamentoPescatoriSanzione").getBooleanValue()) {
						motivi_selezionati.add(strPagamentoPescatoriSanzione);
					}
					if (motivi.has("presuppostoCessazioneSuccessiva") && motivi.get("presuppostoCessazioneSuccessiva").getBooleanValue()) {
						motivi_selezionati.add(strPresuppostoCessazioneSuccessiva);
					}
					if (motivi.has("presuppostoRiduzioneSuccessiva") && motivi.get("presuppostoRiduzioneSuccessiva").getBooleanValue()) {
						motivi_selezionati.add(strPresuppostoRiduzioneSuccessiva);
					}
					if (motivi.has("presuppostoRiduzioneIncerta") && motivi.get("presuppostoRiduzioneIncerta").getBooleanValue()) {
						motivi_selezionati.add(strPresuppostoRiduzioneIncerta);
					}
					if (motivi.has("presuppostoautorizzazionidiverse") && motivi.get("presuppostoautorizzazionidiverse").getBooleanValue()) {
						motivi_selezionati.add(strPresuppostoautorizzazionidiverse);
					}
					if (motivi.has("presuppostoFarmacieRurali") && motivi.get("presuppostoFarmacieRurali").getBooleanValue()) {
						motivi_selezionati.add(strPresuppostoFarmacieRurali);
					}
					

					if (motivi_selezionati != null)
						result.setMotivazioni(motivi_selezionati);
				}
			}
			if (data.has("domanda")){
				JsonNode domanda = data.get("domanda");
				if (domanda.has("motivazioni")) {
					JsonNode motivi = domanda.get("motivazioni");
					result.setTipologiaTemplate("discarico" + result.getTipologiaTemplate());

					if (motivi.has("motivoNonMotivata") && motivi.get("motivoNonMotivata").getBooleanValue()) {
						motivi_selezionati.add(strMotivoNonMotivata);
					}
					if (motivi.has("motivoIrrilevante") && motivi.get("motivoIrrilevante").getBooleanValue()) {
						motivi_selezionati.add(strMotivoIrrilevante);
					}
					if (motivi.has("motivoIrricevibile") && motivi.get("motivoIrricevibile").getBooleanValue()) {
						motivi_selezionati.add(strMotivoIrricevibile);
					}
					if (motivi.has("motivoNessunaRichiesta") && motivi.get("motivoNessunaRichiesta").getBooleanValue()) {
						motivi_selezionati.add(strMotivoNessunaRichiesta);
					}
					if (motivi.has("prescrizioneCompiutaGiacenza") && motivi.get("prescrizioneCompiutaGiacenza").getBooleanValue()) {
						motivi_selezionati.add(strPrescrizioneCompiutaGiacenza);
					}
					if (motivi.has("pagamentoRicevutaErrata") && motivi.get("pagamentoRicevutaErrata").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoRicevutaErrata);
					}
					if (motivi.has("pagamentoTributoErrato") && motivi.get("pagamentoTributoErrato").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoTributoErrato);
					}
					if (motivi.has("pagamentoSanzioneRitardo") && motivi.get("pagamentoSanzioneRitardo").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoSanzioneRitardo);
					}
					if (motivi.has("pagamentoSanzioneRidotta") && motivi.get("pagamentoSanzioneRidotta").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoSanzioneRidotta);
					}
					if (motivi.has("pagamentoRilascioRinnovo") && motivi.get("pagamentoRilascioRinnovo").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoRilascioRinnovo);
					}
					if (motivi.has("pagamentoPescatoriSanzione") && motivi.get("pagamentoPescatoriSanzione").getBooleanValue()) {
						motivi_selezionati.add(strDomPagamentoPescatoriSanzione);
					}
					if (motivi.has("presuppostoCessazioneSuccessiva") && motivi.get("presuppostoCessazioneSuccessiva").getBooleanValue()) {
						motivi_selezionati.add(strDomPresuppostoCessazioneSuccessiva);
					}
					if (motivi.has("presuppostoRiduzioneSuccessiva") && motivi.get("presuppostoRiduzioneSuccessiva").getBooleanValue()) {
						motivi_selezionati.add(strDomPresuppostoRiduzioneSuccessiva);
					}
					if (motivi.has("presuppostoRiduzioneIncerta") && motivi.get("presuppostoRiduzioneIncerta").getBooleanValue()) {
						motivi_selezionati.add(strDomPresuppostoRiduzioneIncerta);
					}
					
					if (motivi.has("presuppostoAutorizzazioniDiverse") && motivi.get("presuppostoAutorizzazioniDiverse").getBooleanValue()) {
						motivi_selezionati.add(strDomPresuppostoAutorizzazioniDiverse);
					}
					if (motivi.has("presuppostoFarmacieRurali") && motivi.get("presuppostoFarmacieRurali").getBooleanValue()) {
						motivi_selezionati.add(strDomPresuppostoFarmacieRurali);
					}
					if (motivi_selezionati != null)
						result.setMotivazioni(motivi_selezionati);
				}
			}
			if (data.has("rimborso")) {
				JsonNode rimborso = data.get("rimborso");
				if (rimborso.has("motivazioni")) {

					JsonNode motivi = rimborso.get("motivazioni");
					result.setTipologiaTemplate("diniegoDomandaRimborso" + result.getTipologiaTemplate());
			
					if (motivi.has("domandaNonMotivata") && motivi.get("domandaNonMotivata").getBooleanValue()) {
						motivi_selezionati.add(strDomandaNonMotivata);
					}
					if (motivi.has("domandaQuotaPrescritta") && motivi.get("domandaQuotaPrescritta").getBooleanValue()) {
						motivi_selezionati.add(strDomandaQuotaPrescritta);
					}
					if (motivi.has("domandaVersamentoCorretto") && motivi.get("domandaVersamentoCorretto").getBooleanValue()) {
						motivi_selezionati.add(strDomandaVersamentoCorretto);
					}
					if (motivi.has("domandaMancaProva") && motivi.get("domandaMancaProva").getBooleanValue()) {
						motivi_selezionati.add(strDomandaMancaProva);
					}
					if (motivi.has("domandaImportoEsiguo") && motivi.get("domandaImportoEsiguo").getBooleanValue()) {
						motivi_selezionati.add(strDomandaImportoEsiguo);
					}
					if (motivi.has("domandaCompetenzaAde") && motivi.get("domandaCompetenzaAde").getBooleanValue()) {
						motivi_selezionati.add(strDomandaCompetenzaAde);
					}
					if (motivi.has("domandaCompetenzaAltra") && motivi.get("domandaCompetenzaAltra").getBooleanValue()) {
						motivi_selezionati.add(strDomandaCompetenzaAltra);
					}
					if (motivi.has("domandaVersamentoCompensato") && motivi.get("domandaVersamentoCompensato").getBooleanValue()) {
						motivi_selezionati.add(strDomandaVersamentoCompensato);
					}
					
					
					if (motivi_selezionati != null)
						result.setMotivazioni(motivi_selezionati);
				}
			}
			log.info("[" + CLASS_NAME + "::parseAzioneDiniego] TipologiaTemplate:" + result.getTipologiaTemplate());

			return result;
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseAzioneDiniego] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parseAzioneDiniego] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parseAzioneDiniego] OUT result:" + result);
		}
	}
	
	public static RicevutaTcrAccoglimentoEntity parseAzioneAccoglimento(String datiIstanza) throws BusinessException {
		
		RicevutaTcrAccoglimentoEntity result = null;
		try {
			log.debug("[" + CLASS_NAME + "::parseAzioneAccoglimento] IN datiIstanza="+datiIstanza);
			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
			int i=0;
			List<String> motivi_selezionati = new ArrayList<>();
			result = new RicevutaTcrAccoglimentoEntity();
			
			if(data.has("accoglimento") && data.get("accoglimento").has("rispostaOsservazione") && 
					data.get("accoglimento").get("rispostaOsservazione")!= null && 
					!data.get("accoglimento").get("rispostaOsservazione").getTextValue().isBlank()){
				//log.debug("[" + CLASS_NAME + "::parseAzioneAccoglimento] IN TipologiaTemplate->rispostaOsservazione="+data.get("accoglimento").get("rispostaOsservazione").getTextValue());
				result.setTipologiaTemplate(data.get("accoglimento").get("rispostaOsservazione").getTextValue());
			}else if(data.has("accoglimento") && data.get("accoglimento").has("rispostaDiscarico") && 
					data.get("accoglimento").get("rispostaDiscarico")!= null && 
					!data.get("accoglimento").get("rispostaDiscarico").getTextValue().isBlank()){
				//log.debug("[" + CLASS_NAME + "::parseAzioneAccoglimento] IN TipologiaTemplate->rispostaDiscarico="+data.get("accoglimento").get("rispostaDiscarico").getTextValue());
				result.setTipologiaTemplate(data.get("accoglimento").get("rispostaDiscarico").getTextValue());
			}
			else if(data.has("accoglimento") && data.get("accoglimento").has("rispostaDomandaRimborso") && 
					data.get("accoglimento").get("rispostaDomandaRimborso")!= null && 
					!data.get("accoglimento").get("rispostaDomandaRimborso").getTextValue().isBlank()){

				result.setTipologiaTemplate("accoglimentoDomandaRimborso");
			}
			
			if(data.has("nome") && data.get("nome")!= null) 
				result.setNome(data.get("nome").getTextValue());
			if(data.has("cognome") && data.get("cognome")!= null) 
				result.setCognome(data.get("cognome").getTextValue());
			if(data.has("codiceFiscalePartitaIva") && data.get("codiceFiscalePartitaIva")!= null) 
				result.setCf(data.get("codiceFiscalePartitaIva").getTextValue());
			if(data.has("indirizzo") && data.get("indirizzo")!= null) 
				result.setIndirizzo(data.get("indirizzo").getTextValue());
			if(data.has("comune") && data.get("comune")!= null) 
				result.setCitta(data.get("comune").getTextValue());
			if(data.has("provincia") && data.get("provincia")!= null) 
				result.setProvincia(data.get("provincia").getTextValue());
			if(data.has("cap") && data.get("cap")!= null) 
				result.setCap(data.get("cap").getTextValue());
			if(data.has("accertamentoN") && data.get("accertamentoN")!= null) 
				result.setNumAccertamento(data.get("accertamentoN").getTextValue());
			if(data.has("classificazione") && data.get("classificazione")!= null) 
				result.setClassificazioneDOQUI(data.get("classificazione").getTextValue());
			if(data.has("annoDiPagamento") && data.get("annoDiPagamento")!= null) 
				result.setAnnoPagamento(data.get("annoDiPagamento").getTextValue());
			if(data.has("scadenza") && data.get("scadenza")!= null) 
				result.setDataScadenza(data.get("scadenza").getTextValue());
			if(data.has("osservazioni")) {
				JsonNode osservazioni=data.get("osservazioni");
				if(osservazioni.has("motivazioni")) {
				JsonNode motivi=osservazioni.get("motivazioni");
				/*if(motivi.has("nonMotivata") && motivi.get("nonMotivata").getBooleanValue())
					motivi_selezionati
						.add("La domanda è priva di motivazione, in quanto in essa non è spiegato il motivo per cui si è"
								+ " ritenuto di poter chiedere l'annullamento, né ad essa è allegata alcuna documentazione"
								+ " dalla quale tale motivo possa eventualmente essere compreso.La domanda è priva di motivazione,"
								+ " in quanto in essa non è spiegato il motivo per cui si è ritenuto di poter chiedere l'annullamento,"
								+ " né ad essa è allegata alcuna documentazione dalla quale tale motivo possa eventualmente essere compreso.");

				if(motivi.has("documentazioneInvalida") && motivi.get("documentazioneInvalida").getBooleanValue())
					motivi_selezionati
						.add("La richiesta non è documentata , oppure la documentazione allegata alla domanda a prova"
								+ " dell'indebito non è valida o non è leggibile.");

				if(motivi.has("motivoIrrilevante") && motivi.get("motivoIrrilevante").getBooleanValue()) 
					motivi_selezionati
					.add("La motivazione posta a sostegno della domanda è irrilevante o priva di fondamento"
							+ " giuridico, e pertanto inidonea giustificare la richiesta di discarico.");
				*/
				
				
				if (motivi.has("motivoCorrezioneParametri") && motivi.get("motivoCorrezioneParametri").getBooleanValue()) {
					motivi_selezionati.add(strMotivoCorrezioneParametri);
				}
				if (motivi.has("motivoSanzioniEredi") && motivi.get("motivoSanzioniEredi").getBooleanValue()) {
					motivi_selezionati.add(strMotivoSanzioniEredi);
				}
				if (motivi.has("motivoPescatoriTassa") && motivi.get("motivoPescatoriTassa").getBooleanValue()) {
					motivi_selezionati.add(strMotivoPescatoriTassa);
				}
				if (motivi.has("motivoSoloTassa") && motivi.get("motivoSoloTassa").getBooleanValue()) {
					motivi_selezionati.add(strMotivoSoloTassa);
				}
				if (motivi.has("motivoSoloAcconto") && motivi.get("motivoSoloAcconto").getBooleanValue()) {
					motivi_selezionati.add(strMotivoSoloAcconto);
				}
				if (motivi.has("motivoPagamentoInsufficiente") && motivi.get("motivoPagamentoInsufficiente").getBooleanValue()) {
					motivi_selezionati.add(strMotivoPagamentoInsufficiente);
				}
				
				if(motivi_selezionati!=null) result.setMotivazioni(motivi_selezionati);
				}
				
				
				if (osservazioni.has("motivazioni1")) {
					JsonNode motivi=osservazioni.get("motivazioni1");
					
					
					if (motivi.has("motivoCorrezioneParametri") && motivi.get("motivoCorrezioneParametri").getBooleanValue()) {
						motivi_selezionati.add(motivoCorrezioneParametri);
					}
					if (motivi.has("motivoSanzioniEredi") && motivi.get("motivoSanzioniEredi").getBooleanValue()) {
						motivi_selezionati.add(motivoSanzioniEredi);
					}
					if (motivi.has("motivoPescatoriTassa") && motivi.get("motivoPescatoriTassa").getBooleanValue()) {
						motivi_selezionati.add(motivoPescatoriTassa);
					}
					if (motivi.has("motivoAltraPartita") && motivi.get("motivoAltraPartita").getBooleanValue()) {
						motivi_selezionati.add(motivoAltraPartita);
					}
					if (motivi.has("motivoSoloTassa") && motivi.get("motivoSoloTassa").getBooleanValue()) {
						motivi_selezionati.add(motivoSoloTassa);
					}
					if (motivi.has("motivoSoloAcconto") && motivi.get("motivoSoloAcconto").getBooleanValue()) {
						motivi_selezionati.add(motivoSoloAcconto);
					}
					if (motivi.has("motivoDopoAccertamento") && motivi.get("motivoDopoAccertamento").getBooleanValue()) {
						motivi_selezionati.add(motivoDopoAccertamento);
					}
					if (motivi.has("motivoPagamentoInsufficiente") && motivi.get("motivoPagamentoInsufficiente").getBooleanValue()) {
						motivi_selezionati.add(motivoPagamentoInsufficiente);
					}
					
					if(motivi_selezionati!=null) result.setMotivazioni(motivi_selezionati);
				}
			}
			if(data.has("rimborso")) {
				JsonNode rimborso=data.get("rimborso");
				if(rimborso.has("numeroDetermina")) 
					result.setNumeroDetermina(rimborso.get("numeroDetermina").getTextValue());
				
				if(rimborso.has("dataDetermina")) 
					result.setDataDetermina(rimborso.get("dataDetermina").getTextValue());
				
				if(rimborso.has("numeroImpegno")) 
					result.setNumeroImpegno(rimborso.get("numeroImpegno").getTextValue());
				
			}
			
			return result;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parseAzioneDiniego] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parseAzioneDiniego] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parseAzioneDiniego] OUT result:" + result);
		}
	}
	
	public static String getEmail(String datiIstanza) throws BusinessException {
		String result = "";
		try {

			JsonNode istanzaNode = readIstanzaData(datiIstanza);
			JsonNode data = (ObjectNode)istanzaNode.get("data");
					
			 // contatti.email
			JsonNode contatti = data.get("contatti");
			if(contatti.has("email") && contatti.get("email") != null )
			{
				result = contatti.get("email").getTextValue();
			}
			
			return result;
			
		} catch (ParseException e) {
			log.error("[" + CLASS_NAME + "::parse] ParseException " + datiIstanza, e);
			throw new BusinessException();
		} catch (Exception e) {
			log.error("[" + CLASS_NAME + "::parse] Exception " + datiIstanza, e);
			throw new BusinessException();
		} finally {
			log.debug("[" + CLASS_NAME + "::parse] OUT result:" + result);
		}
	}
}
