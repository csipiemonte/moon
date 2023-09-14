/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.azione.coto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import it.csi.moon.moonbobl.business.service.impl.azione.AzioneService;
import it.csi.moon.moonbobl.business.service.impl.azione.Azione_Default;
import it.csi.moon.moonbobl.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.LogEmailDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonprintDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.NotificaDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.StoricoWorkflowDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.NotificaEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.StoricoWorkflowEntity;
import it.csi.moon.moonbobl.business.service.impl.helper.ComunicazioneHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.RinnovoConcessioniHelper;
import it.csi.moon.moonbobl.business.service.impl.helper.dto.NotificaActionEntity;
import it.csi.moon.moonbobl.dto.moonfobl.EmailAttachment;
import it.csi.moon.moonbobl.dto.moonfobl.EmailRequest;
import it.csi.moon.moonbobl.dto.moonfobl.Istanza;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.util.Constants;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoLogEmail;

/**
 * Azione_Default - Azioni di default per i moduli che non necessitano specializzazione.
 *
 * @author laurent
 *
 */
public class Azione_COMM_RINNOVO extends Azione_Default implements AzioneService {

	private final static String CLASS_NAME = "Azione_Default";
	protected Logger log = LoggerAccessor.getLoggerBusiness();

	private final static String FIRMA =
		"Ai sensi della determinazione dirigenziale n. mecc. 2019-41344/016 del 26/03/2019, il presente provvedimento viene trasmesso "
		+ "all'indirizzo di posta elettronica certificata, fornita in fase di compilazione della richiesta di rinnovo concessione/i, "
		+ "quale domicilio digitale ex art. 3-bis del D.Lgs 82/2005.\n\n"

		+ "Distinti saluti.\n\n"
		+ "                                                                               IL DIRIGENTE AREA COMMERCIO\n"
		+ "                                                                                  Dott. Roberto MANGIARDI\n"
		+ "                                                                  (Firma omessa ai sensi dell'art. 3 del d.lgs. 39 del 12/02/1993)\n" ;

	private final static Boolean flagPec = false;
			
	@Autowired
	MoonprintDAO moonprintDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	StoricoWorkflowDAO storicoWorkflowDAO;
	@Autowired
	NotificaDAO notificaDAO;
	@Autowired
	LogEmailDAO logEmailDAO;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	IstanzaDAO istanzaDAO;

	public Azione_COMM_RINNOVO() {
		super();
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	@Override
	public void richiediIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getPec();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);

		String oggettoNotifica = "RICHIESTA INTEGRAZIONI – IRREGOLARITÀ DIRITTI ISTRUTTORIA";
		request.setSubject(oggettoNotifica);

		String urlSistema = "https://torinofacile-moon.csi.it/moonfobl/accesso/gasp_coto";
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =
				"OGGETTO: RICHIESTA DI INTEGRAZIONI DELLA DOMANDA PER RINNOVO CONCESSIONE/I AREE PUBBLICHE – IRREGOLARITÀ DIRITTI DI ISTRUTTORIA\n\n" +
				"Si comunica che, in base a quanto emerso dall'attività istruttoria, la domanda n. "+ istanza.getCodiceIstanza() +
				" del " + dataInvio + ", risulta non conforme ai sensi della deliberazione della Giunta Comunale n. 2018 02245/016 "
						+ "del 12.06.2018 immediatamente esecutiva, in base alla quale è dovuto il pagamento dei diritti d'istruttoria.\n\n" +

				"Dal controllo effettuato non risulta allegata l'attestazione dell'avvenuto pagamento dei diritti di istruttoria. "
				+ "Pertanto occorre effettuare un versamento di Euro 50,00 sul conto corrente postale n. 68700137 intestato al "
				+ "Comune di Torino – Settore Attività economiche e di servizio - Via Meucci 4 - 10121 Torino. "
				+ "Nella causale occorre indicare il codice fiscale del richiedente e specificare: diritti istruttoria. "
				+ "Codice IBAN:  IT72 E076 0101 0000 0006 8700 137.\n\n" +
				"						Si invita il richiedente \n\n" +
				"a trasmettere mediante inserimento nel portale," + urlSistema +" la documentazione attestante l'avvenuto "
				+ "versamento entro sessanta giorni dalla notifica della presente. \n\n" +

				"Si avvisa che il mancato pagamento dei diritti di istruttoria comporterà l'avvio delle procedure coattive previste dalla normativa "
				+ "vigente per la riscossione del debito.\n\n"

				+ testoAggiuntivo + FIRMA;

				testo += "\n\n" + Constants.TESTO_NOREPLY;

		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

		// creo legame tra notifica e allegati
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}

		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );
		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));
		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
	}

	@Override
	public void richiediUlterioreIntegrazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getPec();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);

		String oggettoNotifica = "RICHIESTA INTEGRAZIONI";
		request.setSubject(oggettoNotifica);

		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =
				"OGGETTO: RICHIESTA DI INTEGRAZIONI DELLA DOMANDA PER RINNOVO CONCESSIONE/I AREE PUBBLICHE \n\n" +
				"Si comunica che, in base a quanto emerso dall'attività istruttoria, la domanda n. "+ istanza.getCodiceIstanza() +
				" del " + dataInvio + ", risulta non conforme.\n\n"

				+ testoAggiuntivo + FIRMA;

				testo += "\n\n" + Constants.TESTO_NOREPLY;

		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

		// creo legame tra notifica e allegati
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}

		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );

		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));

//		updateDatiIstanza (user, istanza, storicoEntity);

		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
	}

	@Override
	public void invitoConformareDebiti(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getPec();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setPec(flagPec);
		request.setTo(emailDestinatario);

		String oggettoNotifica = "RICHIESTA INTEGRAZIONE – IRREGOLARITÀ POSIZIONI DEBITORIE";
		request.setSubject(oggettoNotifica);

		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}


		String testo =
				"OGGETTO: RICHIESTA DI INTEGRAZIONE DELLA DOMANDA PER RINNOVO CONCESSIONE/I AREE PUBBLICHE PER IRREGOLARITÀ POSIZIONI DEBITORIE. "
				+ "PERDITA DI EFFICACIA DELLA/E CONCESSIONE/I NEL CASO DI ASSENZA O PARZIALE REGOLARIZZAZIONE\n\n"
				+ "Si comunica che, in base a quanto emerso dall'attività istruttoria, la domanda n." + istanza.getCodiceIstanza()
				+ " del " + dataInvio + ", risulta non conforme in quanto dal controllo effettuato presso l'ente riscossore (Soris S.p.A.) "
				+ " risulta presente una situazione non regolare rispetto alle somme dovute."
				+ "Pertanto occorre visionare la propria "
				+ "posizione come sotto indicato e provvedere alla relativa regolarizzazione. \n\n"
				+ "Considerato che in assenza o parziale regolarizzazione non sussistono i presupposti e/o requisiti previsti "
				+ "dalla normativa vigente per il rinnovo della/e concessione/i, \n\n"

				+ "							SI INVITA IL RICHIEDENTE\n\n"

				+ "a trasmettere, entro sessanta giorni dalla notifica della presente, mediante inserimento nel portale: \n"
				+ "•	la documentazione attestante l’avvenuta regolarizzazione della propria posizione debitoria "
				+ "(ricevute di pagamento o piano di rateizzazione approvato da Soris); \n"
				+ "•	la ricevuta di versamento di Euro 50,00 sul conto corrente postale n. 68700137 intestato al "
				+ "Comune di Torino – Settore Attività economiche e di servizio - Via Meucci 4 - 10121 Torino. "
				+ "Nella causale occorre indicare il codice fiscale del richiedente e specificare: diritti istruttoria. "
				+ "Codice IBAN: IT72 E076 0101 0000 0006 8700 137. "
				+ "Si avvisa che il mancato pagamento dei diritti di istruttoria comporterà l'avvio delle procedure coattive "
				+ "previste dalla normativa vigente per la riscossione del debito.\n\n"

				+ "Per visionare il dettaglio di quanto dovuto e delle relative scadenze, si invita ad accedere al servizio "
				+ "‘ESTRATTO CONTO’ di Soris, consultabile al seguente link: https://www.soris.torino.it/cms/estratto-conto \n"
				+ "Per ulteriori informazioni relative alla situazione dei pagamenti, importi e modalità di pagamento occorre "
				+ "rivolgersi a Soris attraverso uno di questi canali: \n"
				+ "•	sportelli di Via Vigone n. 80 – dal lunedì al venerdì, ore 8.30/13.30, previo appuntamento on line "
				+ "al seguente indirizzo https://www.soris.torino.it/cms/prenota-appuntamento; \n"
				+ "•	call center al n. 800-848141  \n"
				+ "•	indirizzo pec: postacertificata@pec.soris.torino.it \n"
				+ "•	al link http://www.soris.torino.it/spaziocontribuenti/pagamenti.htm  \n\n"

				+ "							SI COMUNICA \n\n "

				+ "che in caso di assenza o parziale integrazione entro il suddetto termine, l'istanza di rinnovo della/e concessione/i "
				+ "in oggetto non verrà accolta, senza ulteriori comunicazioni, con conseguente perdita di efficacia della/e concessione/i. \n"

				+ "Contro il presente provvedimento è possibile ricorrere al Tribunale Amministrativo Regionale per il Piemonte entro "
				+ "60 giorni dalla data di notificazione (d.lgs. 2 luglio 2010 n. 104); in alternativa, entro 120 giorni dalla data di "
				+ "notificazione, può essere presentato ricorso straordinario al Presidente della Repubblica (D.P.R. 24/11/1971 n. 1199). \n\n"

				+ testoAggiuntivo + FIRMA;

		testo += "\n\n" + Constants.TESTO_NOREPLY;


		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

		// creo legame tra notifica e allegati
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}

		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );

		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));

//		updateDatiIstanza (user, istanza, storicoEntity);

		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
	}

	@Override
	public void concludiEsitoPositivo(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getPec();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);

		String oggettoNotifica = "COMUNICAZIONE DI ACCOGLIMENTO DOMANDA DI RINNOVO CONCESSIONI AREE PUBBLICHE";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =
				"In riferimento alla domanda n." + istanza.getCodiceIstanza() +
				" del " + dataInvio + "si comunica la conclusione positiva del procedimento di istruttoria per il conseguente "
				+ "rinnovo della/e concessione/i oggetto della richiesta in ottemperanza della vigente normativa.	\n\n"

				+ testoAggiuntivo + FIRMA;

				testo += "\n\n" + Constants.TESTO_NOREPLY;

		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

		// creo legame tra notifica e allegati
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}

		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );

		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));

//		updateDatiIstanza (user, istanza, storicoEntity);

		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RICHIESTA_INTEGRAZIONE);
	}

	@Override
	public void inviaComunicazione(UserInfo user, String datiAzione, Istanza istanza, StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = RinnovoConcessioniHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getPec();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";
		List<String> formIoFileNames = notificaActionEntity.getFormIoFileNames();

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);

		String oggettoNotifica = "COMUNICAZIONE SU RINNOVO CONCESSIONI AREE PUBBLICHE";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}

		String testo =
				"In riferimento alla domanda n." + istanza.getCodiceIstanza() +
				" del " + dataInvio + "si comunica quanto segue: \n"

				+ testoAggiuntivo + FIRMA;

		testo += "\n\n" + Constants.TESTO_NOREPLY;

		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

		// creo legame tra notifica e allegati
		for (String formioNomeFileName : formIoFileNames) {
			RepositoryFileEntity repositoryFileEntity = repositoryFileDAO.findByFormioNameFile(formioNomeFileName);
			notificaDAO.insertAllegatoNotifica(idNotifica, repositoryFileEntity.getIdFile());
			repositoryFileDAO.updateIdIstanza(repositoryFileEntity.getIdFile(), istanza.getIdIstanza());
		}

		// imposto l'attachment con i dati della notifica, sarà poi moonsrv ad acquisirli per allegarli
		EmailAttachment attachment = new EmailAttachment();
		attachment.setIdNotifica(idNotifica);
		request.setAttachment(attachment );

		request.setCc(String.join(";", notificaActionEntity.getEmailCc()));

//		updateDatiIstanza (user, istanza, storicoEntity);

		sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_INVIA_COMUNICAZIONE);
	}

	@Override
	public void respingiConComunicazione(UserInfo user, String datiAzione, Istanza istanza,
			StoricoWorkflowEntity storicoEntity) throws BusinessException {

		// 1. Recupera dati della notifica
		NotificaActionEntity notificaActionEntity = ComunicazioneHelper.parseAzioneNotifica(datiAzione);
		String emailDestinatario = notificaActionEntity.getEmail();
		String cfDestinatario = (!notificaActionEntity.getCodiceFiscale().equals(""))? notificaActionEntity.getCodiceFiscale() : "--assente--";

		// 2. Prepara contenuto notifica
		EmailRequest request = new EmailRequest();
		request.setTo(emailDestinatario);
		request.setPec(flagPec);
		
		String oggettoNotifica = "DOMANDA PER RINNOVO CONCESSIONE/I AREE PUBBLICHE – ARCHIVIAZIONE";
		request.setSubject(oggettoNotifica);
		DateFormat dF = new SimpleDateFormat("dd-MM-yyyy");
		String dataInvio = dF.format(istanza.getCreated());
		String testoAggiuntivo = "";
		if (notificaActionEntity.getTesto() != null && !notificaActionEntity.getTesto().equals(""))
		{
			testoAggiuntivo = notificaActionEntity.getTesto() +"\n\n";
		}
			
		String testo =
				"Si comunica che, in base a quanto emerso dall'attività istruttoria, la domanda n." + istanza.getCodiceIstanza() +
				" del " + dataInvio + " viene archiviata senza esito per la seguente motivazione: \n"

				+ testoAggiuntivo + FIRMA;

		testo += "\n\n" + Constants.TESTO_NOREPLY;

		request.setText(testo);

		// salvo record in tabella notifica
		NotificaEntity notificaEntity = new NotificaEntity();
		notificaEntity.setIdIstanza(istanza.getIdIstanza());
		notificaEntity.setCfDestinatario(cfDestinatario);
		notificaEntity.setEmailDestinatario(emailDestinatario);
		notificaEntity.setOggettoNotifica(oggettoNotifica);
		notificaEntity.setTestoNotifica(testo);
		notificaEntity.setFlagLetta("N");
		notificaEntity.setFlagArchiviata("N");
		Long idNotifica = notificaDAO.insert(notificaEntity);

//		updateDatiIstanza (user, istanza, storicoEntity);

     	sendLogEmail(istanza, emailDestinatario, request, DecodificaTipoLogEmail.BO_RESPINGI_EMAIL);
	}

}
