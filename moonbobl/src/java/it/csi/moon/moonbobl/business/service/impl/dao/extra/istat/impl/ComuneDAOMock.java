/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.ComuneDAO;
import it.csi.moon.moonbobl.dto.extra.istat.Comune;
import it.csi.moon.moonbobl.exceptions.business.DAOException;
import it.csi.moon.moonbobl.exceptions.business.ItemNotFoundDAOException;

/**
 * DAO per l'accesso ai comuni italiani MOCK DATI FASULLI
 * 
 * @see Comune
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("mock")
public class ComuneDAOMock implements ComuneDAO {

	/**  */
	private LinkedHashMap<Integer, ArrayList<Comune>> mappaComuniPerProvincia;


	/**
	 *
	 */
	public ComuneDAOMock() {
		init();
	}



	/**
	 *
	 * @return
	 */
	public ArrayList<Comune> findAll() {
		return new ArrayList<Comune>();
	}

	
	/**
	 * 
	 * @param codiceComune
	 * @return
	 */
	public Comune findByPK(Integer codiceComune) {
		for (Integer codProvincia : mappaComuniPerProvincia.keySet()) {
			for (Comune comune : mappaComuniPerProvincia.get(codProvincia)) {
				if (comune.getCodice().equals(codiceComune)) {
					return comune;
				}
			}
		}
		return null;
	}
	public Comune findByPKidx(Integer codiceProvincia, Integer codiceComune) {
		for (Comune comune : mappaComuniPerProvincia.get(codiceProvincia)) {
			if (comune.getCodice().equals(codiceComune)) {
				return comune;
			}
		}
		return null;
	}
	

	/**
	 *
	 * @param codiceProvincia
	 * @return
	 */
	public ArrayList<Comune> listByCodiceProvincia(Integer codiceProvincia) {
		ArrayList<Comune> res = mappaComuniPerProvincia.get(codiceProvincia);
		if ( res == null ) {
			res = new ArrayList<Comune>();
		}
		return res;
	}

	
	/**
	 *
	 * @param nome
	 * @param codiceProvincia
	 * @return
	 */
	public Comune findByNomeCodiceProvincia(String nome, Integer codiceProvincia) {
		for (Comune comune : listByCodiceProvincia(codiceProvincia)) {
			if (comune.getNome().equals(nome)) {
				return comune;
			}
		}
		return null;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// DATI FASULLI
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public void init() {

		/* 
		 * PIEMONTE
		 */
		ArrayList<Comune> listaComuni2 = new ArrayList<Comune>(); // ALESSANDRIA
		ArrayList<Comune> listaComuni6 = new ArrayList<Comune>(); // ASTI
		ArrayList<Comune> listaComuni12 = new ArrayList<Comune>(); // BIELLA
		ArrayList<Comune> listaComuni28 = new ArrayList<Comune>(); // CUNEO
		ArrayList<Comune> listaComuni57 = new ArrayList<Comune>(); // NOVARA
		ArrayList<Comune> listaComuni91 = new ArrayList<Comune>(); // TORINO
		ArrayList<Comune> listaComuni100 = new ArrayList<Comune>(); // VERBANO-CUSIO-OSSOLA
		ArrayList<Comune> listaComuni101 = new ArrayList<Comune>(); // VERCELLI


		/* 
		 * VALLE D'AOSTA
		 */
		ArrayList<Comune> listaComuni97 = new ArrayList<Comune>(); // VALLE D'AOSTA


		/* 
		 * LOMBARDIA
		 */
		ArrayList<Comune> listaComuni11 = new ArrayList<Comune>(); // BERGAMO
		ArrayList<Comune> listaComuni15 = new ArrayList<Comune>(); // BRESCIA
		ArrayList<Comune> listaComuni24 = new ArrayList<Comune>(); // COMO
		ArrayList<Comune> listaComuni26 = new ArrayList<Comune>(); // CREMONA
		ArrayList<Comune> listaComuni45 = new ArrayList<Comune>(); // LECCO
		ArrayList<Comune> listaComuni47 = new ArrayList<Comune>(); // LODI
		ArrayList<Comune> listaComuni50 = new ArrayList<Comune>(); // MANTOVA
		ArrayList<Comune> listaComuni54 = new ArrayList<Comune>(); // MILANO
		ArrayList<Comune> listaComuni63 = new ArrayList<Comune>(); // PAVIA
		ArrayList<Comune> listaComuni87 = new ArrayList<Comune>(); // SONDRIO
		ArrayList<Comune> listaComuni98 = new ArrayList<Comune>(); // VARESE


		/* 
		 * TRENTINO ALTO ADIGE
		 */
		ArrayList<Comune> listaComuni14 = new ArrayList<Comune>(); // BOLZANO - BOZEN
		ArrayList<Comune> listaComuni93 = new ArrayList<Comune>(); // TRENTO


		/* 
		 * VENETO
		 */
		ArrayList<Comune> listaComuni9 = new ArrayList<Comune>(); // BELLUNO
		ArrayList<Comune> listaComuni60 = new ArrayList<Comune>(); // PADOVA
		ArrayList<Comune> listaComuni81 = new ArrayList<Comune>(); // ROVIGO
		ArrayList<Comune> listaComuni94 = new ArrayList<Comune>(); // TREVISO
		ArrayList<Comune> listaComuni99 = new ArrayList<Comune>(); // VENEZIA
		ArrayList<Comune> listaComuni102 = new ArrayList<Comune>(); // VERONA
		ArrayList<Comune> listaComuni104 = new ArrayList<Comune>(); // VICENZA


		/* 
		 * FRIULI VENEZIA GIULIA
		 */
		ArrayList<Comune> listaComuni37 = new ArrayList<Comune>(); // GORIZIA
		ArrayList<Comune> listaComuni71 = new ArrayList<Comune>(); // PORDENONE
		ArrayList<Comune> listaComuni95 = new ArrayList<Comune>(); // TRIESTE
		ArrayList<Comune> listaComuni96 = new ArrayList<Comune>(); // UDINE


		/* 
		 * LIGURIA
		 */
		ArrayList<Comune> listaComuni36 = new ArrayList<Comune>(); // GENOVA
		ArrayList<Comune> listaComuni39 = new ArrayList<Comune>(); // IMPERIA
		ArrayList<Comune> listaComuni41 = new ArrayList<Comune>(); // LA SPEZIA
		ArrayList<Comune> listaComuni84 = new ArrayList<Comune>(); // SAVONA


		/* 
		 * EMILIA ROMAGNA
		 */
		ArrayList<Comune> listaComuni13 = new ArrayList<Comune>(); // BOLOGNA
		ArrayList<Comune> listaComuni30 = new ArrayList<Comune>(); // FERRARA
		ArrayList<Comune> listaComuni34 = new ArrayList<Comune>(); // FORLI'-CESENA
		ArrayList<Comune> listaComuni55 = new ArrayList<Comune>(); // MODENA
		ArrayList<Comune> listaComuni62 = new ArrayList<Comune>(); // PARMA
		ArrayList<Comune> listaComuni67 = new ArrayList<Comune>(); // PIACENZA
		ArrayList<Comune> listaComuni75 = new ArrayList<Comune>(); // RAVENNA
		ArrayList<Comune> listaComuni77 = new ArrayList<Comune>(); // REGGIO NELL'EMILIA
		ArrayList<Comune> listaComuni79 = new ArrayList<Comune>(); // RIMINI


		/* 
		 * TOSCANA
		 */
		ArrayList<Comune> listaComuni4 = new ArrayList<Comune>(); // AREZZO
		ArrayList<Comune> listaComuni31 = new ArrayList<Comune>(); // FIRENZE
		ArrayList<Comune> listaComuni38 = new ArrayList<Comune>(); // GROSSETO
		ArrayList<Comune> listaComuni46 = new ArrayList<Comune>(); // LIVORNO
		ArrayList<Comune> listaComuni48 = new ArrayList<Comune>(); // LUCCA
		ArrayList<Comune> listaComuni51 = new ArrayList<Comune>(); // MASSA-CARRARA
		ArrayList<Comune> listaComuni68 = new ArrayList<Comune>(); // PISA
		ArrayList<Comune> listaComuni69 = new ArrayList<Comune>(); // PISTOIA
		ArrayList<Comune> listaComuni73 = new ArrayList<Comune>(); // PRATO
		ArrayList<Comune> listaComuni85 = new ArrayList<Comune>(); // SIENA


		/* 
		 * UMBRIA
		 */
		ArrayList<Comune> listaComuni64 = new ArrayList<Comune>(); // PERUGIA
		ArrayList<Comune> listaComuni90 = new ArrayList<Comune>(); // TERNI


		/* 
		 * MARCHE
		 */
		ArrayList<Comune> listaComuni3 = new ArrayList<Comune>(); // ANCONA
		ArrayList<Comune> listaComuni5 = new ArrayList<Comune>(); // ASCOLI PICENO
		ArrayList<Comune> listaComuni49 = new ArrayList<Comune>(); // MACERATA
		ArrayList<Comune> listaComuni65 = new ArrayList<Comune>(); // PESARO E URBINO


		/* 
		 * LAZIO
		 */
		ArrayList<Comune> listaComuni35 = new ArrayList<Comune>(); // FROSINONE
		ArrayList<Comune> listaComuni43 = new ArrayList<Comune>(); // LATINA
		ArrayList<Comune> listaComuni78 = new ArrayList<Comune>(); // RIETI
		ArrayList<Comune> listaComuni80 = new ArrayList<Comune>(); // ROMA
		ArrayList<Comune> listaComuni105 = new ArrayList<Comune>(); // VITERBO


		/* 
		 * ABRUZZO
		 */
		ArrayList<Comune> listaComuni23 = new ArrayList<Comune>(); // CHIETI
		ArrayList<Comune> listaComuni42 = new ArrayList<Comune>(); // L'AQUILA
		ArrayList<Comune> listaComuni66 = new ArrayList<Comune>(); // PESCARA
		ArrayList<Comune> listaComuni89 = new ArrayList<Comune>(); // TERAMO


		/* 
		 * MOLISE
		 */
		ArrayList<Comune> listaComuni19 = new ArrayList<Comune>(); // CAMPOBASSO
		ArrayList<Comune> listaComuni40 = new ArrayList<Comune>(); // ISERNIA


		/* 
		 * CAMPANIA
		 */
		ArrayList<Comune> listaComuni7 = new ArrayList<Comune>(); // AVELLINO
		ArrayList<Comune> listaComuni10 = new ArrayList<Comune>(); // BENEVENTO
		ArrayList<Comune> listaComuni20 = new ArrayList<Comune>(); // CASERTA
		ArrayList<Comune> listaComuni56 = new ArrayList<Comune>(); // NAPOLI
		ArrayList<Comune> listaComuni82 = new ArrayList<Comune>(); // SALERNO


		/* 
		 * PUGLIA
		 */
		ArrayList<Comune> listaComuni8 = new ArrayList<Comune>(); // BARI
		ArrayList<Comune> listaComuni16 = new ArrayList<Comune>(); // BRINDISI
		ArrayList<Comune> listaComuni33 = new ArrayList<Comune>(); // FOGGIA
		ArrayList<Comune> listaComuni44 = new ArrayList<Comune>(); // LECCE
		ArrayList<Comune> listaComuni88 = new ArrayList<Comune>(); // TARANTO


		/* 
		 * BASILICATA
		 */
		ArrayList<Comune> listaComuni52 = new ArrayList<Comune>(); // MATERA
		ArrayList<Comune> listaComuni72 = new ArrayList<Comune>(); // POTENZA


		/* 
		 * CALABRIA
		 */
		ArrayList<Comune> listaComuni22 = new ArrayList<Comune>(); // CATANZARO
		ArrayList<Comune> listaComuni25 = new ArrayList<Comune>(); // COSENZA
		ArrayList<Comune> listaComuni27 = new ArrayList<Comune>(); // CROTONE
		ArrayList<Comune> listaComuni76 = new ArrayList<Comune>(); // REGGIO DI CALABRIA
		ArrayList<Comune> listaComuni103 = new ArrayList<Comune>(); // VIBO VALENTIA


		/* 
		 * SICILIA
		 */
		ArrayList<Comune> listaComuni1 = new ArrayList<Comune>(); // AGRIGENTO
		ArrayList<Comune> listaComuni18 = new ArrayList<Comune>(); // CALTANISSETTA
		ArrayList<Comune> listaComuni21 = new ArrayList<Comune>(); // CATANIA
		ArrayList<Comune> listaComuni29 = new ArrayList<Comune>(); // ENNA
		ArrayList<Comune> listaComuni53 = new ArrayList<Comune>(); // MESSINA
		ArrayList<Comune> listaComuni61 = new ArrayList<Comune>(); // PALERMO
		ArrayList<Comune> listaComuni74 = new ArrayList<Comune>(); // RAGUSA
		ArrayList<Comune> listaComuni86 = new ArrayList<Comune>(); // SIRACUSA
		ArrayList<Comune> listaComuni92 = new ArrayList<Comune>(); // TRAPANI


		/* 
		 * SARDEGNA
		 */
		ArrayList<Comune> listaComuni17 = new ArrayList<Comune>(); // CAGLIARI
		ArrayList<Comune> listaComuni58 = new ArrayList<Comune>(); // NUORO
		ArrayList<Comune> listaComuni59 = new ArrayList<Comune>(); // ORISTANO
		ArrayList<Comune> listaComuni83 = new ArrayList<Comune>(); // SASSARI
		
		
		
		/*********************************************************************************************************************
		 * 
		 ********************************************************************************************************************/

		/*
		 * PIEMONTE
		 */

		initComuniAL(listaComuni2);

		initComuniAT(listaComuni6);

		initComuniBI(listaComuni12);

		initComuniCN(listaComuni28);

		initComuniNO(listaComuni57);

		initComuniTO(listaComuni91);

		initComuniVCO(listaComuni100);

		initComuniVC(listaComuni101);


		/*
		 * VALLE D'AOSTA
		 */

		////// VALLE D'AOSTA
		Comune comune242 = new Comune();  // ALLEIN
		comune242.setCodice(242);
		comune242.setNome("ALLEIN");
		listaComuni97.add(comune242);
		Comune comune243 = new Comune();  // ANTEY ST.ANDRE'
		comune243.setCodice(243);
		comune243.setNome("ANTEY ST.ANDRE'");
		listaComuni97.add(comune243);
		Comune comune241 = new Comune();  // AOSTA
		comune241.setCodice(241);
		comune241.setNome("AOSTA");
		listaComuni97.add(comune241);
		Comune comune324 = new Comune();  // ARNAD
		comune324.setCodice(324);
		comune324.setNome("ARNAD");
		listaComuni97.add(comune324);
		Comune comune245 = new Comune();  // ARVIER
		comune245.setCodice(245);
		comune245.setNome("ARVIER");
		listaComuni97.add(comune245);
		Comune comune246 = new Comune();  // AVISE
		comune246.setCodice(246);
		comune246.setNome("AVISE");
		listaComuni97.add(comune246);
		Comune comune247 = new Comune();  // AYAS
		comune247.setCodice(247);
		comune247.setNome("AYAS");
		listaComuni97.add(comune247);
		Comune comune248 = new Comune();  // AYMAVILLES
		comune248.setCodice(248);
		comune248.setNome("AYMAVILLES");
		listaComuni97.add(comune248);
		Comune comune249 = new Comune();  // BARD
		comune249.setCodice(249);
		comune249.setNome("BARD");
		listaComuni97.add(comune249);
		Comune comune250 = new Comune();  // BIONAZ
		comune250.setCodice(250);
		comune250.setNome("BIONAZ");
		listaComuni97.add(comune250);
		Comune comune251 = new Comune();  // BRISSOGNE
		comune251.setCodice(251);
		comune251.setNome("BRISSOGNE");
		listaComuni97.add(comune251);
		Comune comune252 = new Comune();  // BRUSSON
		comune252.setCodice(252);
		comune252.setNome("BRUSSON");
		listaComuni97.add(comune252);
		Comune comune253 = new Comune();  // CHALLAND ST.ANSELME
		comune253.setCodice(253);
		comune253.setNome("CHALLAND ST.ANSELME");
		listaComuni97.add(comune253);
		Comune comune254 = new Comune();  // CHALLAND ST.VICTOR
		comune254.setCodice(254);
		comune254.setNome("CHALLAND ST.VICTOR");
		listaComuni97.add(comune254);
		Comune comune327 = new Comune();  // CHALLANT ST.ANSELME ORA CHALLAND ST. ANSELME
		comune327.setCodice(327);
		comune327.setNome("CHALLANT ST.ANSELME ORA CHALLAND ST. ANSELME");
		listaComuni97.add(comune327);


		/*
		 * LOMBARDIA
		 */

		////// BERGAMO
		Comune comune2231 = new Comune();  // ADRARA S.MARTINO
		comune2231.setCodice(2231);
		comune2231.setNome("ADRARA S.MARTINO");
		listaComuni11.add(comune2231);
		Comune comune2232 = new Comune();  // ADRARA S.ROCCO
		comune2232.setCodice(2232);
		comune2232.setNome("ADRARA S.ROCCO");
		listaComuni11.add(comune2232);
		Comune comune2233 = new Comune();  // ALBANO S.ALESSANDRO
		comune2233.setCodice(2233);
		comune2233.setNome("ALBANO S.ALESSANDRO");
		listaComuni11.add(comune2233);
		Comune comune2234 = new Comune();  // ALBINO
		comune2234.setCodice(2234);
		comune2234.setNome("ALBINO");
		listaComuni11.add(comune2234);
		Comune comune2235 = new Comune();  // ALGUA
		comune2235.setCodice(2235);
		comune2235.setNome("ALGUA");
		listaComuni11.add(comune2235);
		Comune comune2236 = new Comune();  // ALME'
		comune2236.setCodice(2236);
		comune2236.setNome("ALME'");
		listaComuni11.add(comune2236);
		Comune comune2237 = new Comune();  // ALMENNO S.BARTOLOMEO
		comune2237.setCodice(2237);
		comune2237.setNome("ALMENNO S.BARTOLOMEO");
		listaComuni11.add(comune2237);
		Comune comune2238 = new Comune();  // ALMENNO S.SALVATORE
		comune2238.setCodice(2238);
		comune2238.setNome("ALMENNO S.SALVATORE");
		listaComuni11.add(comune2238);
		Comune comune2239 = new Comune();  // ALZANO LOMBARDO
		comune2239.setCodice(2239);
		comune2239.setNome("ALZANO LOMBARDO");
		listaComuni11.add(comune2239);
		Comune comune2240 = new Comune();  // AMBIVERE
		comune2240.setCodice(2240);
		comune2240.setNome("AMBIVERE");
		listaComuni11.add(comune2240);
		Comune comune2241 = new Comune();  // ANTEGNATE
		comune2241.setCodice(2241);
		comune2241.setNome("ANTEGNATE");
		listaComuni11.add(comune2241);
		Comune comune2242 = new Comune();  // ARCENE
		comune2242.setCodice(2242);
		comune2242.setNome("ARCENE");
		listaComuni11.add(comune2242);
		Comune comune2243 = new Comune();  // ARDESIO
		comune2243.setCodice(2243);
		comune2243.setNome("ARDESIO");
		listaComuni11.add(comune2243);
		Comune comune2244 = new Comune();  // ARZAGO D'ADDA
		comune2244.setCodice(2244);
		comune2244.setNome("ARZAGO D'ADDA");
		listaComuni11.add(comune2244);
		Comune comune2245 = new Comune();  // AVERARA
		comune2245.setCodice(2245);
		comune2245.setNome("AVERARA");
		listaComuni11.add(comune2245);

		////// BRESCIA
		Comune comune2535 = new Comune();  // ACQUAFREDDA
		comune2535.setCodice(2535);
		comune2535.setNome("ACQUAFREDDA");
		listaComuni15.add(comune2535);
		Comune comune2536 = new Comune();  // ADRO
		comune2536.setCodice(2536);
		comune2536.setNome("ADRO");
		listaComuni15.add(comune2536);
		Comune comune2537 = new Comune();  // AGNOSINE
		comune2537.setCodice(2537);
		comune2537.setNome("AGNOSINE");
		listaComuni15.add(comune2537);
		Comune comune2538 = new Comune();  // ALFIANELLO
		comune2538.setCodice(2538);
		comune2538.setNome("ALFIANELLO");
		listaComuni15.add(comune2538);
		Comune comune2539 = new Comune();  // ANFO
		comune2539.setCodice(2539);
		comune2539.setNome("ANFO");
		listaComuni15.add(comune2539);
		Comune comune2540 = new Comune();  // ANGOLO TERME
		comune2540.setCodice(2540);
		comune2540.setNome("ANGOLO TERME");
		listaComuni15.add(comune2540);
		Comune comune2541 = new Comune();  // ARTOGNE
		comune2541.setCodice(2541);
		comune2541.setNome("ARTOGNE");
		listaComuni15.add(comune2541);
		Comune comune2542 = new Comune();  // AZZANO MELLA
		comune2542.setCodice(2542);
		comune2542.setNome("AZZANO MELLA");
		listaComuni15.add(comune2542);
		Comune comune2543 = new Comune();  // BAGNOLO MELLA
		comune2543.setCodice(2543);
		comune2543.setNome("BAGNOLO MELLA");
		listaComuni15.add(comune2543);
		Comune comune2544 = new Comune();  // BAGOLINO
		comune2544.setCodice(2544);
		comune2544.setNome("BAGOLINO");
		listaComuni15.add(comune2544);
		Comune comune2545 = new Comune();  // BARBARIGA
		comune2545.setCodice(2545);
		comune2545.setNome("BARBARIGA");
		listaComuni15.add(comune2545);
		Comune comune2546 = new Comune();  // BARGHE
		comune2546.setCodice(2546);
		comune2546.setNome("BARGHE");
		listaComuni15.add(comune2546);
		Comune comune2547 = new Comune();  // BASSANO BRESCIANO
		comune2547.setCodice(2547);
		comune2547.setNome("BASSANO BRESCIANO");
		listaComuni15.add(comune2547);
		Comune comune2548 = new Comune();  // BEDIZZOLE
		comune2548.setCodice(2548);
		comune2548.setNome("BEDIZZOLE");
		listaComuni15.add(comune2548);
		Comune comune2549 = new Comune();  // BERLINGO
		comune2549.setCodice(2549);
		comune2549.setNome("BERLINGO");
		listaComuni15.add(comune2549);

		////// COMO
		Comune comune3039 = new Comune();  // ACQUATE ORA LECCO
		comune3039.setCodice(3039);
		comune3039.setNome("ACQUATE ORA LECCO");
		listaComuni24.add(comune3039);
		Comune comune2796 = new Comune();  // ALBAVILLA
		comune2796.setCodice(2796);
		comune2796.setNome("ALBAVILLA");
		listaComuni24.add(comune2796);
		Comune comune2797 = new Comune();  // ALBESE CON CASSANO
		comune2797.setCodice(2797);
		comune2797.setNome("ALBESE CON CASSANO");
		listaComuni24.add(comune2797);
		Comune comune2798 = new Comune();  // ALBIOLO
		comune2798.setCodice(2798);
		comune2798.setNome("ALBIOLO");
		listaComuni24.add(comune2798);
		Comune comune2799 = new Comune();  // ALSERIO
		comune2799.setCodice(2799);
		comune2799.setNome("ALSERIO");
		listaComuni24.add(comune2799);
		Comune comune2800 = new Comune();  // ALZATE BRIANZA
		comune2800.setCodice(2800);
		comune2800.setNome("ALZATE BRIANZA");
		listaComuni24.add(comune2800);
		Comune comune2802 = new Comune();  // ANZANO DEL PARCO
		comune2802.setCodice(2802);
		comune2802.setNome("ANZANO DEL PARCO");
		listaComuni24.add(comune2802);
		Comune comune2803 = new Comune();  // APPIANO GENTILE
		comune2803.setCodice(2803);
		comune2803.setNome("APPIANO GENTILE");
		listaComuni24.add(comune2803);
		Comune comune2804 = new Comune();  // ARGEGNO
		comune2804.setCodice(2804);
		comune2804.setNome("ARGEGNO");
		listaComuni24.add(comune2804);
		Comune comune2805 = new Comune();  // AROSIO
		comune2805.setCodice(2805);
		comune2805.setNome("AROSIO");
		listaComuni24.add(comune2805);
		Comune comune3044 = new Comune();  // ASNAGO ORA CERMENATE
		comune3044.setCodice(3044);
		comune3044.setNome("ASNAGO ORA CERMENATE");
		listaComuni24.add(comune3044);
		Comune comune2806 = new Comune();  // ASSO
		comune2806.setCodice(2806);
		comune2806.setNome("ASSO");
		listaComuni24.add(comune2806);
		Comune comune2808 = new Comune();  // BARNI
		comune2808.setCodice(2808);
		comune2808.setNome("BARNI");
		listaComuni24.add(comune2808);
		Comune comune2812 = new Comune();  // BELLAGIO
		comune2812.setCodice(2812);
		comune2812.setNome("BELLAGIO");
		listaComuni24.add(comune2812);
		Comune comune2814 = new Comune();  // BENE LARIO
		comune2814.setCodice(2814);
		comune2814.setNome("BENE LARIO");
		listaComuni24.add(comune2814);

		////// CREMONA
		Comune comune3093 = new Comune();  // ACQUANEGRA CREMONESE
		comune3093.setCodice(3093);
		comune3093.setNome("ACQUANEGRA CREMONESE");
		listaComuni26.add(comune3093);
		Comune comune3094 = new Comune();  // AGNADELLO
		comune3094.setCodice(3094);
		comune3094.setNome("AGNADELLO");
		listaComuni26.add(comune3094);
		Comune comune3095 = new Comune();  // ANNICCO
		comune3095.setCodice(3095);
		comune3095.setNome("ANNICCO");
		listaComuni26.add(comune3095);
		Comune comune3096 = new Comune();  // AZZANELLO
		comune3096.setCodice(3096);
		comune3096.setNome("AZZANELLO");
		listaComuni26.add(comune3096);
		Comune comune3097 = new Comune();  // BAGNOLO CREMASCO
		comune3097.setCodice(3097);
		comune3097.setNome("BAGNOLO CREMASCO");
		listaComuni26.add(comune3097);
		Comune comune3098 = new Comune();  // BONEMERSE
		comune3098.setCodice(3098);
		comune3098.setNome("BONEMERSE");
		listaComuni26.add(comune3098);
		Comune comune3099 = new Comune();  // BORDOLANO
		comune3099.setCodice(3099);
		comune3099.setNome("BORDOLANO");
		listaComuni26.add(comune3099);
		Comune comune3100 = new Comune();  // CA' D'ANDREA
		comune3100.setCodice(3100);
		comune3100.setNome("CA' D'ANDREA");
		listaComuni26.add(comune3100);
		Comune comune3101 = new Comune();  // CALVATONE
		comune3101.setCodice(3101);
		comune3101.setNome("CALVATONE");
		listaComuni26.add(comune3101);
		Comune comune3102 = new Comune();  // CAMISANO
		comune3102.setCodice(3102);
		comune3102.setNome("CAMISANO");
		listaComuni26.add(comune3102);
		Comune comune3103 = new Comune();  // CAMPAGNOLA CREMASCA
		comune3103.setCodice(3103);
		comune3103.setNome("CAMPAGNOLA CREMASCA");
		listaComuni26.add(comune3103);
		Comune comune3104 = new Comune();  // CAPERGNANICA
		comune3104.setCodice(3104);
		comune3104.setNome("CAPERGNANICA");
		listaComuni26.add(comune3104);
		Comune comune3105 = new Comune();  // CAPPELLA CANTONE
		comune3105.setCodice(3105);
		comune3105.setNome("CAPPELLA CANTONE");
		listaComuni26.add(comune3105);
		Comune comune3106 = new Comune();  // CAPPELLA DE' PICENARDI
		comune3106.setCodice(3106);
		comune3106.setNome("CAPPELLA DE' PICENARDI");
		listaComuni26.add(comune3106);
		Comune comune3107 = new Comune();  // CAPRALBA
		comune3107.setCodice(3107);
		comune3107.setNome("CAPRALBA");
		listaComuni26.add(comune3107);

		////// LECCO
		Comune comune3229 = new Comune();  // ABBADIA LARIANA
		comune3229.setCodice(3229);
		comune3229.setNome("ABBADIA LARIANA");
		listaComuni45.add(comune3229);
		Comune comune3230 = new Comune();  // AIRUNO
		comune3230.setCodice(3230);
		comune3230.setNome("AIRUNO");
		listaComuni45.add(comune3230);
		Comune comune3231 = new Comune();  // ANNONE DI BRIANZA
		comune3231.setCodice(3231);
		comune3231.setNome("ANNONE DI BRIANZA");
		listaComuni45.add(comune3231);
		Comune comune3232 = new Comune();  // BALLABIO
		comune3232.setCodice(3232);
		comune3232.setNome("BALLABIO");
		listaComuni45.add(comune3232);
		Comune comune3233 = new Comune();  // BARZAGO
		comune3233.setCodice(3233);
		comune3233.setNome("BARZAGO");
		listaComuni45.add(comune3233);
		Comune comune3234 = new Comune();  // BARZANO'
		comune3234.setCodice(3234);
		comune3234.setNome("BARZANO'");
		listaComuni45.add(comune3234);
		Comune comune3235 = new Comune();  // BARZIO
		comune3235.setCodice(3235);
		comune3235.setNome("BARZIO");
		listaComuni45.add(comune3235);
		Comune comune3236 = new Comune();  // BELLANO
		comune3236.setCodice(3236);
		comune3236.setNome("BELLANO");
		listaComuni45.add(comune3236);
		Comune comune3237 = new Comune();  // BOSISIO PARINI
		comune3237.setCodice(3237);
		comune3237.setNome("BOSISIO PARINI");
		listaComuni45.add(comune3237);
		Comune comune3238 = new Comune();  // BRIVIO
		comune3238.setCodice(3238);
		comune3238.setNome("BRIVIO");
		listaComuni45.add(comune3238);
		Comune comune3239 = new Comune();  // BULCIAGO
		comune3239.setCodice(3239);
		comune3239.setNome("BULCIAGO");
		listaComuni45.add(comune3239);
		Comune comune3240 = new Comune();  // CALCO
		comune3240.setCodice(3240);
		comune3240.setNome("CALCO");
		listaComuni45.add(comune3240);
		Comune comune3223 = new Comune();  // CALOLZIOCORTE
		comune3223.setCodice(3223);
		comune3223.setNome("CALOLZIOCORTE");
		listaComuni45.add(comune3223);
		Comune comune3224 = new Comune();  // CARENNO
		comune3224.setCodice(3224);
		comune3224.setNome("CARENNO");
		listaComuni45.add(comune3224);
		Comune comune3241 = new Comune();  // CASARGO
		comune3241.setCodice(3241);
		comune3241.setNome("CASARGO");
		listaComuni45.add(comune3241);

		////// LODI
		Comune comune3315 = new Comune();  // ABBADIA CERRETO
		comune3315.setCodice(3315);
		comune3315.setNome("ABBADIA CERRETO");
		listaComuni47.add(comune3315);
		Comune comune3316 = new Comune();  // BERTONICO
		comune3316.setCodice(3316);
		comune3316.setNome("BERTONICO");
		listaComuni47.add(comune3316);
		Comune comune3317 = new Comune();  // BOFFALORA D'ADDA
		comune3317.setCodice(3317);
		comune3317.setNome("BOFFALORA D'ADDA");
		listaComuni47.add(comune3317);
		Comune comune3318 = new Comune();  // BORGHETTO LODIGIANO
		comune3318.setCodice(3318);
		comune3318.setNome("BORGHETTO LODIGIANO");
		listaComuni47.add(comune3318);
		Comune comune3319 = new Comune();  // BORGO S.GIOVANNI
		comune3319.setCodice(3319);
		comune3319.setNome("BORGO S.GIOVANNI");
		listaComuni47.add(comune3319);
		Comune comune3320 = new Comune();  // BREMBIO
		comune3320.setCodice(3320);
		comune3320.setNome("BREMBIO");
		listaComuni47.add(comune3320);
		Comune comune3321 = new Comune();  // CAMAIRAGO
		comune3321.setCodice(3321);
		comune3321.setNome("CAMAIRAGO");
		listaComuni47.add(comune3321);
		Comune comune3322 = new Comune();  // CASALETTO LODIGIANO
		comune3322.setCodice(3322);
		comune3322.setNome("CASALETTO LODIGIANO");
		listaComuni47.add(comune3322);
		Comune comune3323 = new Comune();  // CASALMAIOCCO
		comune3323.setCodice(3323);
		comune3323.setNome("CASALMAIOCCO");
		listaComuni47.add(comune3323);
		Comune comune3324 = new Comune();  // CASALPUSTERLENGO
		comune3324.setCodice(3324);
		comune3324.setNome("CASALPUSTERLENGO");
		listaComuni47.add(comune3324);
		Comune comune3325 = new Comune();  // CASELLE LANDI
		comune3325.setCodice(3325);
		comune3325.setNome("CASELLE LANDI");
		listaComuni47.add(comune3325);
		Comune comune3326 = new Comune();  // CASELLE LURANI
		comune3326.setCodice(3326);
		comune3326.setNome("CASELLE LURANI");
		listaComuni47.add(comune3326);
		Comune comune3327 = new Comune();  // CASTELNUOVO BOCCA D'ADDA
		comune3327.setCodice(3327);
		comune3327.setNome("CASTELNUOVO BOCCA D'ADDA");
		listaComuni47.add(comune3327);
		Comune comune3328 = new Comune();  // CASTIGLIONE D'ADDA
		comune3328.setCodice(3328);
		comune3328.setNome("CASTIGLIONE D'ADDA");
		listaComuni47.add(comune3328);
		Comune comune3329 = new Comune();  // CASTIRAGA VIDARDO
		comune3329.setCodice(3329);
		comune3329.setNome("CASTIRAGA VIDARDO");
		listaComuni47.add(comune3329);

		////// MANTOVA
		Comune comune3377 = new Comune();  // ACQUANEGRA SUL CHIESE
		comune3377.setCodice(3377);
		comune3377.setNome("ACQUANEGRA SUL CHIESE");
		listaComuni50.add(comune3377);
		Comune comune3378 = new Comune();  // ASOLA
		comune3378.setCodice(3378);
		comune3378.setNome("ASOLA");
		listaComuni50.add(comune3378);
		Comune comune3379 = new Comune();  // BAGNOLO S.VITO
		comune3379.setCodice(3379);
		comune3379.setNome("BAGNOLO S.VITO");
		listaComuni50.add(comune3379);
		Comune comune3380 = new Comune();  // BIGARELLO
		comune3380.setCodice(3380);
		comune3380.setNome("BIGARELLO");
		listaComuni50.add(comune3380);
		Comune comune3381 = new Comune();  // BORGOFORTE
		comune3381.setCodice(3381);
		comune3381.setNome("BORGOFORTE");
		listaComuni50.add(comune3381);
		Comune comune3382 = new Comune();  // BORGOFRANCO SUL PO
		comune3382.setCodice(3382);
		comune3382.setNome("BORGOFRANCO SUL PO");
		listaComuni50.add(comune3382);
		Comune comune3383 = new Comune();  // BOZZOLO
		comune3383.setCodice(3383);
		comune3383.setNome("BOZZOLO");
		listaComuni50.add(comune3383);
		Comune comune3384 = new Comune();  // CANNETO SULL'OGLIO
		comune3384.setCodice(3384);
		comune3384.setNome("CANNETO SULL'OGLIO");
		listaComuni50.add(comune3384);
		Comune comune3385 = new Comune();  // CARBONARA DI PO
		comune3385.setCodice(3385);
		comune3385.setNome("CARBONARA DI PO");
		listaComuni50.add(comune3385);
		Comune comune3386 = new Comune();  // CASALMORO
		comune3386.setCodice(3386);
		comune3386.setNome("CASALMORO");
		listaComuni50.add(comune3386);
		Comune comune3387 = new Comune();  // CASALOLDO
		comune3387.setCodice(3387);
		comune3387.setNome("CASALOLDO");
		listaComuni50.add(comune3387);
		Comune comune3388 = new Comune();  // CASALROMANO
		comune3388.setCodice(3388);
		comune3388.setNome("CASALROMANO");
		listaComuni50.add(comune3388);
		Comune comune3389 = new Comune();  // CASTEL D'ARIO
		comune3389.setCodice(3389);
		comune3389.setNome("CASTEL D'ARIO");
		listaComuni50.add(comune3389);
		Comune comune3390 = new Comune();  // CASTELBELFORTE
		comune3390.setCodice(3390);
		comune3390.setNome("CASTELBELFORTE");
		listaComuni50.add(comune3390);
		Comune comune3391 = new Comune();  // CASTELGOFFREDO
		comune3391.setCodice(3391);
		comune3391.setNome("CASTELGOFFREDO");
		listaComuni50.add(comune3391);

		////// MILANO
		Comune comune1956 = new Comune();  // ABBIATEGRASSO
		comune1956.setCodice(1956);
		comune1956.setNome("ABBIATEGRASSO");
		listaComuni54.add(comune1956);
		Comune comune1957 = new Comune();  // AGRATE BRIANZA
		comune1957.setCodice(1957);
		comune1957.setNome("AGRATE BRIANZA");
		listaComuni54.add(comune1957);
		Comune comune1958 = new Comune();  // AICURZIO
		comune1958.setCodice(1958);
		comune1958.setNome("AICURZIO");
		listaComuni54.add(comune1958);
		Comune comune1959 = new Comune();  // ALBAIRATE
		comune1959.setCodice(1959);
		comune1959.setNome("ALBAIRATE");
		listaComuni54.add(comune1959);
		Comune comune1960 = new Comune();  // ALBIATE
		comune1960.setCodice(1960);
		comune1960.setNome("ALBIATE");
		listaComuni54.add(comune1960);
		Comune comune1961 = new Comune();  // ARCONATE
		comune1961.setCodice(1961);
		comune1961.setNome("ARCONATE");
		listaComuni54.add(comune1961);
		Comune comune1962 = new Comune();  // ARCORE
		comune1962.setCodice(1962);
		comune1962.setNome("ARCORE");
		listaComuni54.add(comune1962);
		Comune comune1963 = new Comune();  // ARESE
		comune1963.setCodice(1963);
		comune1963.setNome("ARESE");
		listaComuni54.add(comune1963);
		Comune comune1964 = new Comune();  // ARLUNO
		comune1964.setCodice(1964);
		comune1964.setNome("ARLUNO");
		listaComuni54.add(comune1964);
		Comune comune1965 = new Comune();  // ASSAGO
		comune1965.setCodice(1965);
		comune1965.setNome("ASSAGO");
		listaComuni54.add(comune1965);
		Comune comune2228 = new Comune();  // BARANZATE
		comune2228.setCodice(2228);
		comune2228.setNome("BARANZATE");
		listaComuni54.add(comune2228);
		Comune comune1966 = new Comune();  // BAREGGIO
		comune1966.setCodice(1966);
		comune1966.setNome("BAREGGIO");
		listaComuni54.add(comune1966);
		Comune comune1967 = new Comune();  // BARLASSINA
		comune1967.setCodice(1967);
		comune1967.setNome("BARLASSINA");
		listaComuni54.add(comune1967);
		Comune comune1968 = new Comune();  // BASIANO
		comune1968.setCodice(1968);
		comune1968.setNome("BASIANO");
		listaComuni54.add(comune1968);
		Comune comune1969 = new Comune();  // BASIGLIO
		comune1969.setCodice(1969);
		comune1969.setNome("BASIGLIO");
		listaComuni54.add(comune1969);

		////// PAVIA
		Comune comune3447 = new Comune();  // ALAGNA
		comune3447.setCodice(3447);
		comune3447.setNome("ALAGNA");
		listaComuni63.add(comune3447);
		Comune comune3448 = new Comune();  // ALBAREDO ARNABOLDI
		comune3448.setCodice(3448);
		comune3448.setNome("ALBAREDO ARNABOLDI");
		listaComuni63.add(comune3448);
		Comune comune3449 = new Comune();  // ALBONESE
		comune3449.setCodice(3449);
		comune3449.setNome("ALBONESE");
		listaComuni63.add(comune3449);
		Comune comune3450 = new Comune();  // ALBUZZANO
		comune3450.setCodice(3450);
		comune3450.setNome("ALBUZZANO");
		listaComuni63.add(comune3450);
		Comune comune3451 = new Comune();  // ARENA PO
		comune3451.setCodice(3451);
		comune3451.setNome("ARENA PO");
		listaComuni63.add(comune3451);
		Comune comune3452 = new Comune();  // BADIA PAVESE
		comune3452.setCodice(3452);
		comune3452.setNome("BADIA PAVESE");
		listaComuni63.add(comune3452);
		Comune comune3453 = new Comune();  // BAGNARIA
		comune3453.setCodice(3453);
		comune3453.setNome("BAGNARIA");
		listaComuni63.add(comune3453);
		Comune comune3454 = new Comune();  // BARBIANELLO
		comune3454.setCodice(3454);
		comune3454.setNome("BARBIANELLO");
		listaComuni63.add(comune3454);
		Comune comune3455 = new Comune();  // BASCAPE'
		comune3455.setCodice(3455);
		comune3455.setNome("BASCAPE'");
		listaComuni63.add(comune3455);
		Comune comune3456 = new Comune();  // BASTIDA DE' DOSSI
		comune3456.setCodice(3456);
		comune3456.setNome("BASTIDA DE' DOSSI");
		listaComuni63.add(comune3456);
		Comune comune3457 = new Comune();  // BASTIDA PANCARANA
		comune3457.setCodice(3457);
		comune3457.setNome("BASTIDA PANCARANA");
		listaComuni63.add(comune3457);
		Comune comune3458 = new Comune();  // BATTUDA
		comune3458.setCodice(3458);
		comune3458.setNome("BATTUDA");
		listaComuni63.add(comune3458);
		Comune comune3459 = new Comune();  // BELGIOIOSO
		comune3459.setCodice(3459);
		comune3459.setNome("BELGIOIOSO");
		listaComuni63.add(comune3459);
		Comune comune3460 = new Comune();  // BEREGUARDO
		comune3460.setCodice(3460);
		comune3460.setNome("BEREGUARDO");
		listaComuni63.add(comune3460);
		Comune comune3461 = new Comune();  // BORGARELLO
		comune3461.setCodice(3461);
		comune3461.setNome("BORGARELLO");
		listaComuni63.add(comune3461);

		////// SONDRIO
		Comune comune3662 = new Comune();  // ALBAREDO PER S.MARCO
		comune3662.setCodice(3662);
		comune3662.setNome("ALBAREDO PER S.MARCO");
		listaComuni87.add(comune3662);
		Comune comune3738 = new Comune();  // ALBOSAGGIA
		comune3738.setCodice(3738);
		comune3738.setNome("ALBOSAGGIA");
		listaComuni87.add(comune3738);
		Comune comune3663 = new Comune();  // ANDALO VALTELLINO
		comune3663.setCodice(3663);
		comune3663.setNome("ANDALO VALTELLINO");
		listaComuni87.add(comune3663);
		Comune comune3664 = new Comune();  // APRICA
		comune3664.setCodice(3664);
		comune3664.setNome("APRICA");
		listaComuni87.add(comune3664);
		Comune comune3665 = new Comune();  // ARDENNO
		comune3665.setCodice(3665);
		comune3665.setNome("ARDENNO");
		listaComuni87.add(comune3665);
		Comune comune3666 = new Comune();  // BEMA
		comune3666.setCodice(3666);
		comune3666.setNome("BEMA");
		listaComuni87.add(comune3666);
		Comune comune3667 = new Comune();  // BERBENNO DI VALTELLINA
		comune3667.setCodice(3667);
		comune3667.setNome("BERBENNO DI VALTELLINA");
		listaComuni87.add(comune3667);
		Comune comune3668 = new Comune();  // BIANZONE
		comune3668.setCodice(3668);
		comune3668.setNome("BIANZONE");
		listaComuni87.add(comune3668);
		Comune comune3669 = new Comune();  // BORMIO
		comune3669.setCodice(3669);
		comune3669.setNome("BORMIO");
		listaComuni87.add(comune3669);
		Comune comune3670 = new Comune();  // BUGLIO IN MONTE
		comune3670.setCodice(3670);
		comune3670.setNome("BUGLIO IN MONTE");
		listaComuni87.add(comune3670);
		Comune comune3671 = new Comune();  // CAIOLO
		comune3671.setCodice(3671);
		comune3671.setNome("CAIOLO");
		listaComuni87.add(comune3671);
		Comune comune3672 = new Comune();  // CAMPODOLCINO
		comune3672.setCodice(3672);
		comune3672.setNome("CAMPODOLCINO");
		listaComuni87.add(comune3672);
		Comune comune3673 = new Comune();  // CASPOGGIO
		comune3673.setCodice(3673);
		comune3673.setNome("CASPOGGIO");
		listaComuni87.add(comune3673);
		Comune comune3674 = new Comune();  // CASTELLO DELL'ACQUA
		comune3674.setCodice(3674);
		comune3674.setNome("CASTELLO DELL'ACQUA");
		listaComuni87.add(comune3674);
		Comune comune3675 = new Comune();  // CASTIONE ANDEVENNO
		comune3675.setCodice(3675);
		comune3675.setNome("CASTIONE ANDEVENNO");
		listaComuni87.add(comune3675);

		////// VARESE
		Comune comune3743 = new Comune();  // AGRA
		comune3743.setCodice(3743);
		comune3743.setNome("AGRA");
		listaComuni98.add(comune3743);
		Comune comune3744 = new Comune();  // ALBIZZATE
		comune3744.setCodice(3744);
		comune3744.setNome("ALBIZZATE");
		listaComuni98.add(comune3744);
		Comune comune3745 = new Comune();  // ANGERA
		comune3745.setCodice(3745);
		comune3745.setNome("ANGERA");
		listaComuni98.add(comune3745);
		Comune comune3746 = new Comune();  // ARCISATE
		comune3746.setCodice(3746);
		comune3746.setNome("ARCISATE");
		listaComuni98.add(comune3746);
		Comune comune3747 = new Comune();  // ARSAGO SEPRIO
		comune3747.setCodice(3747);
		comune3747.setNome("ARSAGO SEPRIO");
		listaComuni98.add(comune3747);
		Comune comune3748 = new Comune();  // AZZATE
		comune3748.setCodice(3748);
		comune3748.setNome("AZZATE");
		listaComuni98.add(comune3748);
		Comune comune3749 = new Comune();  // AZZIO
		comune3749.setCodice(3749);
		comune3749.setNome("AZZIO");
		listaComuni98.add(comune3749);
		Comune comune3750 = new Comune();  // BARASSO
		comune3750.setCodice(3750);
		comune3750.setNome("BARASSO");
		listaComuni98.add(comune3750);
		Comune comune3751 = new Comune();  // BARDELLO
		comune3751.setCodice(3751);
		comune3751.setNome("BARDELLO");
		listaComuni98.add(comune3751);
		Comune comune3882 = new Comune();  // BARZOLA ORA ANGERA
		comune3882.setCodice(3882);
		comune3882.setNome("BARZOLA ORA ANGERA");
		listaComuni98.add(comune3882);
		Comune comune3752 = new Comune();  // BEDERO VALCUVIA
		comune3752.setCodice(3752);
		comune3752.setNome("BEDERO VALCUVIA");
		listaComuni98.add(comune3752);
		Comune comune3753 = new Comune();  // BESANO
		comune3753.setCodice(3753);
		comune3753.setNome("BESANO");
		listaComuni98.add(comune3753);
		Comune comune3754 = new Comune();  // BESNATE
		comune3754.setCodice(3754);
		comune3754.setNome("BESNATE");
		listaComuni98.add(comune3754);
		Comune comune3755 = new Comune();  // BESOZZO
		comune3755.setCodice(3755);
		comune3755.setNome("BESOZZO");
		listaComuni98.add(comune3755);
		Comune comune3756 = new Comune();  // BIANDRONNO
		comune3756.setCodice(3756);
		comune3756.setNome("BIANDRONNO");
		listaComuni98.add(comune3756);


		/*
		 * TRENTINO ALTO ADIGE
		 */

		////// BOLZANO - BOZEN
		Comune comune4204 = new Comune();  // ALDINO
		comune4204.setCodice(4204);
		comune4204.setNome("ALDINO");
		listaComuni14.add(comune4204);
		Comune comune4205 = new Comune();  // ANDRIANO
		comune4205.setCodice(4205);
		comune4205.setNome("ANDRIANO");
		listaComuni14.add(comune4205);
		Comune comune4206 = new Comune();  // ANTERIVO
		comune4206.setCodice(4206);
		comune4206.setNome("ANTERIVO");
		listaComuni14.add(comune4206);
		Comune comune4330 = new Comune();  // APPIANO SULLA STRADA DEL VINO
		comune4330.setCodice(4330);
		comune4330.setNome("APPIANO SULLA STRADA DEL VINO");
		listaComuni14.add(comune4330);
		Comune comune4208 = new Comune();  // AVELENGO
		comune4208.setCodice(4208);
		comune4208.setNome("AVELENGO");
		listaComuni14.add(comune4208);
		Comune comune4209 = new Comune();  // BADIA
		comune4209.setCodice(4209);
		comune4209.setNome("BADIA");
		listaComuni14.add(comune4209);
		Comune comune4210 = new Comune();  // BARBIANO
		comune4210.setCodice(4210);
		comune4210.setNome("BARBIANO");
		listaComuni14.add(comune4210);
		Comune comune4203 = new Comune();  // BOLZANO
		comune4203.setCodice(4203);
		comune4203.setNome("BOLZANO");
		listaComuni14.add(comune4203);
		Comune comune4211 = new Comune();  // BRAIES
		comune4211.setCodice(4211);
		comune4211.setNome("BRAIES");
		listaComuni14.add(comune4211);
		Comune comune4212 = new Comune();  // BRENNERO
		comune4212.setCodice(4212);
		comune4212.setNome("BRENNERO");
		listaComuni14.add(comune4212);
		Comune comune4213 = new Comune();  // BRESSANONE
		comune4213.setCodice(4213);
		comune4213.setNome("BRESSANONE");
		listaComuni14.add(comune4213);
		Comune comune4214 = new Comune();  // BRONZOLO
		comune4214.setCodice(4214);
		comune4214.setNome("BRONZOLO");
		listaComuni14.add(comune4214);
		Comune comune4215 = new Comune();  // BRUNICO
		comune4215.setCodice(4215);
		comune4215.setNome("BRUNICO");
		listaComuni14.add(comune4215);
		Comune comune4216 = new Comune();  // CAINES
		comune4216.setCodice(4216);
		comune4216.setNome("CAINES");
		listaComuni14.add(comune4216);
		Comune comune4333 = new Comune();  // CALDARO SULLA STRADA DEL VINO
		comune4333.setCodice(4333);
		comune4333.setNome("CALDARO SULLA STRADA DEL VINO");
		listaComuni14.add(comune4333);

		////// TRENTO
		Comune comune3926 = new Comune();  // ALA
		comune3926.setCodice(3926);
		comune3926.setNome("ALA");
		listaComuni93.add(comune3926);
		Comune comune3927 = new Comune();  // ALBIANO
		comune3927.setCodice(3927);
		comune3927.setNome("ALBIANO");
		listaComuni93.add(comune3927);
		Comune comune3928 = new Comune();  // ALDENO
		comune3928.setCodice(3928);
		comune3928.setNome("ALDENO");
		listaComuni93.add(comune3928);
		Comune comune3929 = new Comune();  // AMBLAR
		comune3929.setCodice(3929);
		comune3929.setNome("AMBLAR");
		listaComuni93.add(comune3929);
		Comune comune3930 = new Comune();  // ANDALO
		comune3930.setCodice(3930);
		comune3930.setNome("ANDALO");
		listaComuni93.add(comune3930);
		Comune comune3931 = new Comune();  // ARCO
		comune3931.setCodice(3931);
		comune3931.setNome("ARCO");
		listaComuni93.add(comune3931);
		Comune comune3932 = new Comune();  // AVIO
		comune3932.setCodice(3932);
		comune3932.setNome("AVIO");
		listaComuni93.add(comune3932);
		Comune comune3934 = new Comune();  // BASELGA DI PINE'
		comune3934.setCodice(3934);
		comune3934.setNome("BASELGA DI PINE'");
		listaComuni93.add(comune3934);
		Comune comune3936 = new Comune();  // BEDOLLO
		comune3936.setCodice(3936);
		comune3936.setNome("BEDOLLO");
		listaComuni93.add(comune3936);
		Comune comune3937 = new Comune();  // BERSONE
		comune3937.setCodice(3937);
		comune3937.setNome("BERSONE");
		listaComuni93.add(comune3937);
		Comune comune3938 = new Comune();  // BESENELLO
		comune3938.setCodice(3938);
		comune3938.setNome("BESENELLO");
		listaComuni93.add(comune3938);
		Comune comune3939 = new Comune();  // BEZZECCA
		comune3939.setCodice(3939);
		comune3939.setNome("BEZZECCA");
		listaComuni93.add(comune3939);
		Comune comune3940 = new Comune();  // BIENO
		comune3940.setCodice(3940);
		comune3940.setNome("BIENO");
		listaComuni93.add(comune3940);
		Comune comune3941 = new Comune();  // BLEGGIO INFERIORE
		comune3941.setCodice(3941);
		comune3941.setNome("BLEGGIO INFERIORE");
		listaComuni93.add(comune3941);
		Comune comune3942 = new Comune();  // BLEGGIO SUPERIORE
		comune3942.setCodice(3942);
		comune3942.setNome("BLEGGIO SUPERIORE");
		listaComuni93.add(comune3942);


		/*
		 * VENETO
		 */

		////// BELLUNO
		Comune comune4391 = new Comune();  // AGORDO
		comune4391.setCodice(4391);
		comune4391.setNome("AGORDO");
		listaComuni9.add(comune4391);
		Comune comune4392 = new Comune();  // ALANO DI PIAVE
		comune4392.setCodice(4392);
		comune4392.setNome("ALANO DI PIAVE");
		listaComuni9.add(comune4392);
		Comune comune4393 = new Comune();  // ALLEGHE
		comune4393.setCodice(4393);
		comune4393.setNome("ALLEGHE");
		listaComuni9.add(comune4393);
		Comune comune4394 = new Comune();  // ARSIE'
		comune4394.setCodice(4394);
		comune4394.setNome("ARSIE'");
		listaComuni9.add(comune4394);
		Comune comune4395 = new Comune();  // AURONZO DI CADORE
		comune4395.setCodice(4395);
		comune4395.setNome("AURONZO DI CADORE");
		listaComuni9.add(comune4395);
		Comune comune4390 = new Comune();  // BELLUNO
		comune4390.setCodice(4390);
		comune4390.setNome("BELLUNO");
		listaComuni9.add(comune4390);
		Comune comune4396 = new Comune();  // BORCA DI CADORE
		comune4396.setCodice(4396);
		comune4396.setNome("BORCA DI CADORE");
		listaComuni9.add(comune4396);
		Comune comune4397 = new Comune();  // CALALZO DI CADORE
		comune4397.setCodice(4397);
		comune4397.setNome("CALALZO DI CADORE");
		listaComuni9.add(comune4397);
		Comune comune4459 = new Comune();  // CANALE D'AGORDO
		comune4459.setCodice(4459);
		comune4459.setNome("CANALE D'AGORDO");
		listaComuni9.add(comune4459);
		Comune comune4398 = new Comune();  // CASTELLAVAZZO
		comune4398.setCodice(4398);
		comune4398.setNome("CASTELLAVAZZO");
		listaComuni9.add(comune4398);
		Comune comune4460 = new Comune();  // CENCENIGHE AGORDINO
		comune4460.setCodice(4460);
		comune4460.setNome("CENCENIGHE AGORDINO");
		listaComuni9.add(comune4460);
		Comune comune4400 = new Comune();  // CESIOMAGGIORE
		comune4400.setCodice(4400);
		comune4400.setNome("CESIOMAGGIORE");
		listaComuni9.add(comune4400);
		Comune comune4401 = new Comune();  // CHIES D'ALPAGO
		comune4401.setCodice(4401);
		comune4401.setNome("CHIES D'ALPAGO");
		listaComuni9.add(comune4401);
		Comune comune4403 = new Comune();  // CIBIANA DI CADORE
		comune4403.setCodice(4403);
		comune4403.setNome("CIBIANA DI CADORE");
		listaComuni9.add(comune4403);
		Comune comune4404 = new Comune();  // COLLE S.LUCIA
		comune4404.setCodice(4404);
		comune4404.setNome("COLLE S.LUCIA");
		listaComuni9.add(comune4404);

		////// PADOVA
		Comune comune4462 = new Comune();  // ABANO TERME
		comune4462.setCodice(4462);
		comune4462.setNome("ABANO TERME");
		listaComuni60.add(comune4462);
		Comune comune4463 = new Comune();  // AGNA
		comune4463.setCodice(4463);
		comune4463.setNome("AGNA");
		listaComuni60.add(comune4463);
		Comune comune4464 = new Comune();  // ALBIGNASEGO
		comune4464.setCodice(4464);
		comune4464.setNome("ALBIGNASEGO");
		listaComuni60.add(comune4464);
		Comune comune4465 = new Comune();  // ANGUILLARA VENETA
		comune4465.setCodice(4465);
		comune4465.setNome("ANGUILLARA VENETA");
		listaComuni60.add(comune4465);
		Comune comune4466 = new Comune();  // ARQUA' PETRARCA
		comune4466.setCodice(4466);
		comune4466.setNome("ARQUA' PETRARCA");
		listaComuni60.add(comune4466);
		Comune comune4467 = new Comune();  // ARRE
		comune4467.setCodice(4467);
		comune4467.setNome("ARRE");
		listaComuni60.add(comune4467);
		Comune comune4468 = new Comune();  // ARZERGRANDE
		comune4468.setCodice(4468);
		comune4468.setNome("ARZERGRANDE");
		listaComuni60.add(comune4468);
		Comune comune4469 = new Comune();  // BAGNOLI DI SOPRA
		comune4469.setCodice(4469);
		comune4469.setNome("BAGNOLI DI SOPRA");
		listaComuni60.add(comune4469);
		Comune comune4470 = new Comune();  // BAONE
		comune4470.setCodice(4470);
		comune4470.setNome("BAONE");
		listaComuni60.add(comune4470);
		Comune comune4471 = new Comune();  // BARBONA
		comune4471.setCodice(4471);
		comune4471.setNome("BARBONA");
		listaComuni60.add(comune4471);
		Comune comune4472 = new Comune();  // BATTAGLIA TERME
		comune4472.setCodice(4472);
		comune4472.setNome("BATTAGLIA TERME");
		listaComuni60.add(comune4472);
		Comune comune4473 = new Comune();  // BOARA PISANI
		comune4473.setCodice(4473);
		comune4473.setNome("BOARA PISANI");
		listaComuni60.add(comune4473);
		Comune comune4474 = new Comune();  // BORGORICCO
		comune4474.setCodice(4474);
		comune4474.setNome("BORGORICCO");
		listaComuni60.add(comune4474);
		Comune comune4475 = new Comune();  // BOVOLENTA
		comune4475.setCodice(4475);
		comune4475.setNome("BOVOLENTA");
		listaComuni60.add(comune4475);
		Comune comune4476 = new Comune();  // BRUGINE
		comune4476.setCodice(4476);
		comune4476.setNome("BRUGINE");
		listaComuni60.add(comune4476);

		////// ROVIGO
		Comune comune4570 = new Comune();  // ADRIA
		comune4570.setCodice(4570);
		comune4570.setNome("ADRIA");
		listaComuni81.add(comune4570);
		Comune comune4571 = new Comune();  // ARIANO NEL POLESINE
		comune4571.setCodice(4571);
		comune4571.setNome("ARIANO NEL POLESINE");
		listaComuni81.add(comune4571);
		Comune comune4572 = new Comune();  // ARQUA' POLESINE
		comune4572.setCodice(4572);
		comune4572.setNome("ARQUA' POLESINE");
		listaComuni81.add(comune4572);
		Comune comune4573 = new Comune();  // BADIA POLESINE
		comune4573.setCodice(4573);
		comune4573.setNome("BADIA POLESINE");
		listaComuni81.add(comune4573);
		Comune comune4574 = new Comune();  // BAGNOLO DI PO
		comune4574.setCodice(4574);
		comune4574.setNome("BAGNOLO DI PO");
		listaComuni81.add(comune4574);
		Comune comune4575 = new Comune();  // BERGANTINO
		comune4575.setCodice(4575);
		comune4575.setNome("BERGANTINO");
		listaComuni81.add(comune4575);
		Comune comune4576 = new Comune();  // BOSARO
		comune4576.setCodice(4576);
		comune4576.setNome("BOSARO");
		listaComuni81.add(comune4576);
		Comune comune4577 = new Comune();  // CALTO
		comune4577.setCodice(4577);
		comune4577.setNome("CALTO");
		listaComuni81.add(comune4577);
		Comune comune4578 = new Comune();  // CANARO
		comune4578.setCodice(4578);
		comune4578.setNome("CANARO");
		listaComuni81.add(comune4578);
		Comune comune4579 = new Comune();  // CANDA
		comune4579.setCodice(4579);
		comune4579.setNome("CANDA");
		listaComuni81.add(comune4579);
		Comune comune4580 = new Comune();  // CASTELGUGLIELMO
		comune4580.setCodice(4580);
		comune4580.setNome("CASTELGUGLIELMO");
		listaComuni81.add(comune4580);
		Comune comune4581 = new Comune();  // CASTELMASSA
		comune4581.setCodice(4581);
		comune4581.setNome("CASTELMASSA");
		listaComuni81.add(comune4581);
		Comune comune4582 = new Comune();  // CASTELNOVO BARIANO
		comune4582.setCodice(4582);
		comune4582.setNome("CASTELNOVO BARIANO");
		listaComuni81.add(comune4582);
		Comune comune4583 = new Comune();  // CENESELLI
		comune4583.setCodice(4583);
		comune4583.setNome("CENESELLI");
		listaComuni81.add(comune4583);
		Comune comune4584 = new Comune();  // CEREGNANO
		comune4584.setCodice(4584);
		comune4584.setNome("CEREGNANO");
		listaComuni81.add(comune4584);

		////// TREVISO
		Comune comune4647 = new Comune();  // ALTIVOLE
		comune4647.setCodice(4647);
		comune4647.setNome("ALTIVOLE");
		listaComuni94.add(comune4647);
		Comune comune4648 = new Comune();  // ARCADE
		comune4648.setCodice(4648);
		comune4648.setNome("ARCADE");
		listaComuni94.add(comune4648);
		Comune comune4649 = new Comune();  // ASOLO
		comune4649.setCodice(4649);
		comune4649.setNome("ASOLO");
		listaComuni94.add(comune4649);
		Comune comune4650 = new Comune();  // BORSO DEL GRAPPA
		comune4650.setCodice(4650);
		comune4650.setNome("BORSO DEL GRAPPA");
		listaComuni94.add(comune4650);
		Comune comune4651 = new Comune();  // BREDA DI PIAVE
		comune4651.setCodice(4651);
		comune4651.setNome("BREDA DI PIAVE");
		listaComuni94.add(comune4651);
		Comune comune4652 = new Comune();  // CAERANO DI S.MARCO
		comune4652.setCodice(4652);
		comune4652.setNome("CAERANO DI S.MARCO");
		listaComuni94.add(comune4652);
		Comune comune4653 = new Comune();  // CAPPELLA MAGGIORE
		comune4653.setCodice(4653);
		comune4653.setNome("CAPPELLA MAGGIORE");
		listaComuni94.add(comune4653);
		Comune comune4654 = new Comune();  // CARBONERA
		comune4654.setCodice(4654);
		comune4654.setNome("CARBONERA");
		listaComuni94.add(comune4654);
		Comune comune4655 = new Comune();  // CASALE SUL SILE
		comune4655.setCodice(4655);
		comune4655.setNome("CASALE SUL SILE");
		listaComuni94.add(comune4655);
		Comune comune4656 = new Comune();  // CASIER
		comune4656.setCodice(4656);
		comune4656.setNome("CASIER");
		listaComuni94.add(comune4656);
		Comune comune4657 = new Comune();  // CASTELCUCCO
		comune4657.setCodice(4657);
		comune4657.setNome("CASTELCUCCO");
		listaComuni94.add(comune4657);
		Comune comune4658 = new Comune();  // CASTELFRANCO VENETO
		comune4658.setCodice(4658);
		comune4658.setNome("CASTELFRANCO VENETO");
		listaComuni94.add(comune4658);
		Comune comune4659 = new Comune();  // CASTELLO DI GODEGO
		comune4659.setCodice(4659);
		comune4659.setNome("CASTELLO DI GODEGO");
		listaComuni94.add(comune4659);
		Comune comune4660 = new Comune();  // CAVASO DEL TOMBA
		comune4660.setCodice(4660);
		comune4660.setNome("CAVASO DEL TOMBA");
		listaComuni94.add(comune4660);
		Comune comune4661 = new Comune();  // CESSALTO
		comune4661.setCodice(4661);
		comune4661.setNome("CESSALTO");
		listaComuni94.add(comune4661);

		////// VENEZIA
		Comune comune4335 = new Comune();  // ANNONE VENETO
		comune4335.setCodice(4335);
		comune4335.setNome("ANNONE VENETO");
		listaComuni99.add(comune4335);
		Comune comune4336 = new Comune();  // CAMPAGNA LUPIA
		comune4336.setCodice(4336);
		comune4336.setNome("CAMPAGNA LUPIA");
		listaComuni99.add(comune4336);
		Comune comune4337 = new Comune();  // CAMPOLONGO MAGGIORE
		comune4337.setCodice(4337);
		comune4337.setNome("CAMPOLONGO MAGGIORE");
		listaComuni99.add(comune4337);
		Comune comune4338 = new Comune();  // CAMPONOGARA
		comune4338.setCodice(4338);
		comune4338.setNome("CAMPONOGARA");
		listaComuni99.add(comune4338);
		Comune comune4339 = new Comune();  // CAORLE
		comune4339.setCodice(4339);
		comune4339.setNome("CAORLE");
		listaComuni99.add(comune4339);
		Comune comune4388 = new Comune();  // CAVALLINO-TREPORTI
		comune4388.setCodice(4388);
		comune4388.setNome("CAVALLINO-TREPORTI");
		listaComuni99.add(comune4388);
		Comune comune4340 = new Comune();  // CAVARZERE
		comune4340.setCodice(4340);
		comune4340.setNome("CAVARZERE");
		listaComuni99.add(comune4340);
		Comune comune4341 = new Comune();  // CEGGIA
		comune4341.setCodice(4341);
		comune4341.setNome("CEGGIA");
		listaComuni99.add(comune4341);
		Comune comune4342 = new Comune();  // CHIOGGIA
		comune4342.setCodice(4342);
		comune4342.setNome("CHIOGGIA");
		listaComuni99.add(comune4342);
		Comune comune4343 = new Comune();  // CINTO CAOMAGGIORE
		comune4343.setCodice(4343);
		comune4343.setNome("CINTO CAOMAGGIORE");
		listaComuni99.add(comune4343);
		Comune comune4344 = new Comune();  // CONA
		comune4344.setCodice(4344);
		comune4344.setNome("CONA");
		listaComuni99.add(comune4344);
		Comune comune4345 = new Comune();  // CONCORDIA SAGITTARIA
		comune4345.setCodice(4345);
		comune4345.setNome("CONCORDIA SAGITTARIA");
		listaComuni99.add(comune4345);
		Comune comune4346 = new Comune();  // DOLO
		comune4346.setCodice(4346);
		comune4346.setNome("DOLO");
		listaComuni99.add(comune4346);
		Comune comune4347 = new Comune();  // ERACLEA
		comune4347.setCodice(4347);
		comune4347.setNome("ERACLEA");
		listaComuni99.add(comune4347);
		Comune comune4348 = new Comune();  // FIESSO D'ARTICO
		comune4348.setCodice(4348);
		comune4348.setNome("FIESSO D'ARTICO");
		listaComuni99.add(comune4348);

		////// VERONA
		Comune comune4748 = new Comune();  // AFFI
		comune4748.setCodice(4748);
		comune4748.setNome("AFFI");
		listaComuni102.add(comune4748);
		Comune comune4749 = new Comune();  // ALBAREDO D'ADIGE
		comune4749.setCodice(4749);
		comune4749.setNome("ALBAREDO D'ADIGE");
		listaComuni102.add(comune4749);
		Comune comune4750 = new Comune();  // ANGIARI
		comune4750.setCodice(4750);
		comune4750.setNome("ANGIARI");
		listaComuni102.add(comune4750);
		Comune comune4751 = new Comune();  // ARCOLE
		comune4751.setCodice(4751);
		comune4751.setNome("ARCOLE");
		listaComuni102.add(comune4751);
		Comune comune4752 = new Comune();  // BADIA CALAVENA
		comune4752.setCodice(4752);
		comune4752.setNome("BADIA CALAVENA");
		listaComuni102.add(comune4752);
		Comune comune4753 = new Comune();  // BARDOLINO
		comune4753.setCodice(4753);
		comune4753.setNome("BARDOLINO");
		listaComuni102.add(comune4753);
		Comune comune4754 = new Comune();  // BELFIORE
		comune4754.setCodice(4754);
		comune4754.setNome("BELFIORE");
		listaComuni102.add(comune4754);
		Comune comune4755 = new Comune();  // BEVILACQUA
		comune4755.setCodice(4755);
		comune4755.setNome("BEVILACQUA");
		listaComuni102.add(comune4755);
		Comune comune4756 = new Comune();  // BONAVIGO
		comune4756.setCodice(4756);
		comune4756.setNome("BONAVIGO");
		listaComuni102.add(comune4756);
		Comune comune4757 = new Comune();  // BOSCHI S.ANNA
		comune4757.setCodice(4757);
		comune4757.setNome("BOSCHI S.ANNA");
		listaComuni102.add(comune4757);
		Comune comune4758 = new Comune();  // BOSCO CHIESANUOVA
		comune4758.setCodice(4758);
		comune4758.setNome("BOSCO CHIESANUOVA");
		listaComuni102.add(comune4758);
		Comune comune4759 = new Comune();  // BOVOLONE
		comune4759.setCodice(4759);
		comune4759.setNome("BOVOLONE");
		listaComuni102.add(comune4759);
		Comune comune4760 = new Comune();  // BRENTINO BELLUNO
		comune4760.setCodice(4760);
		comune4760.setNome("BRENTINO BELLUNO");
		listaComuni102.add(comune4760);
		Comune comune4761 = new Comune();  // BRENZONE
		comune4761.setCodice(4761);
		comune4761.setNome("BRENZONE");
		listaComuni102.add(comune4761);
		Comune comune4762 = new Comune();  // BUSSOLENGO
		comune4762.setCodice(4762);
		comune4762.setNome("BUSSOLENGO");
		listaComuni102.add(comune4762);

		////// VICENZA
		Comune comune4863 = new Comune();  // AGUGLIARO
		comune4863.setCodice(4863);
		comune4863.setNome("AGUGLIARO");
		listaComuni104.add(comune4863);
		Comune comune4864 = new Comune();  // ALBETTONE
		comune4864.setCodice(4864);
		comune4864.setNome("ALBETTONE");
		listaComuni104.add(comune4864);
		Comune comune4865 = new Comune();  // ALONTE
		comune4865.setCodice(4865);
		comune4865.setNome("ALONTE");
		listaComuni104.add(comune4865);
		Comune comune4866 = new Comune();  // ALTAVILLA VICENTINA
		comune4866.setCodice(4866);
		comune4866.setNome("ALTAVILLA VICENTINA");
		listaComuni104.add(comune4866);
		Comune comune4867 = new Comune();  // ALTISSIMO
		comune4867.setCodice(4867);
		comune4867.setNome("ALTISSIMO");
		listaComuni104.add(comune4867);
		Comune comune4868 = new Comune();  // ARCUGNANO
		comune4868.setCodice(4868);
		comune4868.setNome("ARCUGNANO");
		listaComuni104.add(comune4868);
		Comune comune4869 = new Comune();  // ARSIERO
		comune4869.setCodice(4869);
		comune4869.setNome("ARSIERO");
		listaComuni104.add(comune4869);
		Comune comune4870 = new Comune();  // ARZIGNANO
		comune4870.setCodice(4870);
		comune4870.setNome("ARZIGNANO");
		listaComuni104.add(comune4870);
		Comune comune4871 = new Comune();  // ASIAGO
		comune4871.setCodice(4871);
		comune4871.setNome("ASIAGO");
		listaComuni104.add(comune4871);
		Comune comune4872 = new Comune();  // ASIGLIANO VENETO
		comune4872.setCodice(4872);
		comune4872.setNome("ASIGLIANO VENETO");
		listaComuni104.add(comune4872);
		Comune comune4873 = new Comune();  // BARBARANO VICENTINO
		comune4873.setCodice(4873);
		comune4873.setNome("BARBARANO VICENTINO");
		listaComuni104.add(comune4873);
		Comune comune4874 = new Comune();  // BASSANO DEL GRAPPA
		comune4874.setCodice(4874);
		comune4874.setNome("BASSANO DEL GRAPPA");
		listaComuni104.add(comune4874);
		Comune comune4875 = new Comune();  // BOLZANO VICENTINO
		comune4875.setCodice(4875);
		comune4875.setNome("BOLZANO VICENTINO");
		listaComuni104.add(comune4875);
		Comune comune4876 = new Comune();  // BREGANZE
		comune4876.setCodice(4876);
		comune4876.setNome("BREGANZE");
		listaComuni104.add(comune4876);
		Comune comune4877 = new Comune();  // BRENDOLA
		comune4877.setCodice(4877);
		comune4877.setNome("BRENDOLA");
		listaComuni104.add(comune4877);


		/*
		 * FRIULI VENEZIA GIULIA
		 */

		////// GORIZIA
		Comune comune5002 = new Comune();  // CAPRIVA DEL FRIULI
		comune5002.setCodice(5002);
		comune5002.setNome("CAPRIVA DEL FRIULI");
		listaComuni37.add(comune5002);
		Comune comune5003 = new Comune();  // CORMONS
		comune5003.setCodice(5003);
		comune5003.setNome("CORMONS");
		listaComuni37.add(comune5003);
		Comune comune5004 = new Comune();  // DOBERDO' DEL LAGO
		comune5004.setCodice(5004);
		comune5004.setNome("DOBERDO' DEL LAGO");
		listaComuni37.add(comune5004);
		Comune comune5005 = new Comune();  // DOLEGNA DEL COLLIO
		comune5005.setCodice(5005);
		comune5005.setNome("DOLEGNA DEL COLLIO");
		listaComuni37.add(comune5005);
		Comune comune5006 = new Comune();  // FARRA D'ISONZO
		comune5006.setCodice(5006);
		comune5006.setNome("FARRA D'ISONZO");
		listaComuni37.add(comune5006);
		Comune comune5007 = new Comune();  // FOGLIANO DI REDIPUGLIA
		comune5007.setCodice(5007);
		comune5007.setNome("FOGLIANO DI REDIPUGLIA");
		listaComuni37.add(comune5007);
		Comune comune5001 = new Comune();  // GORIZIA
		comune5001.setCodice(5001);
		comune5001.setNome("GORIZIA");
		listaComuni37.add(comune5001);
		Comune comune5008 = new Comune();  // GRADISCA D'ISONZO
		comune5008.setCodice(5008);
		comune5008.setNome("GRADISCA D'ISONZO");
		listaComuni37.add(comune5008);
		Comune comune5009 = new Comune();  // GRADO
		comune5009.setCodice(5009);
		comune5009.setNome("GRADO");
		listaComuni37.add(comune5009);
		Comune comune5010 = new Comune();  // MARIANO DEL FRIULI
		comune5010.setCodice(5010);
		comune5010.setNome("MARIANO DEL FRIULI");
		listaComuni37.add(comune5010);
		Comune comune5011 = new Comune();  // MEDEA
		comune5011.setCodice(5011);
		comune5011.setNome("MEDEA");
		listaComuni37.add(comune5011);
		Comune comune5012 = new Comune();  // MONFALCONE
		comune5012.setCodice(5012);
		comune5012.setNome("MONFALCONE");
		listaComuni37.add(comune5012);
		Comune comune5013 = new Comune();  // MORARO
		comune5013.setCodice(5013);
		comune5013.setNome("MORARO");
		listaComuni37.add(comune5013);
		Comune comune5014 = new Comune();  // MOSSA
		comune5014.setCodice(5014);
		comune5014.setNome("MOSSA");
		listaComuni37.add(comune5014);
		Comune comune5015 = new Comune();  // ROMANS D'ISONZO
		comune5015.setCodice(5015);
		comune5015.setNome("ROMANS D'ISONZO");
		listaComuni37.add(comune5015);

		////// PORDENONE
		Comune comune5036 = new Comune();  // ANDREIS
		comune5036.setCodice(5036);
		comune5036.setNome("ANDREIS");
		listaComuni71.add(comune5036);
		Comune comune5037 = new Comune();  // ARBA
		comune5037.setCodice(5037);
		comune5037.setNome("ARBA");
		listaComuni71.add(comune5037);
		Comune comune5038 = new Comune();  // ARZENE
		comune5038.setCodice(5038);
		comune5038.setNome("ARZENE");
		listaComuni71.add(comune5038);
		Comune comune5039 = new Comune();  // AVIANO
		comune5039.setCodice(5039);
		comune5039.setNome("AVIANO");
		listaComuni71.add(comune5039);
		Comune comune5040 = new Comune();  // AZZANO DECIMO
		comune5040.setCodice(5040);
		comune5040.setNome("AZZANO DECIMO");
		listaComuni71.add(comune5040);
		Comune comune5041 = new Comune();  // BARCIS
		comune5041.setCodice(5041);
		comune5041.setNome("BARCIS");
		listaComuni71.add(comune5041);
		Comune comune5042 = new Comune();  // BRUGNERA
		comune5042.setCodice(5042);
		comune5042.setNome("BRUGNERA");
		listaComuni71.add(comune5042);
		Comune comune5043 = new Comune();  // BUDOIA
		comune5043.setCodice(5043);
		comune5043.setNome("BUDOIA");
		listaComuni71.add(comune5043);
		Comune comune5044 = new Comune();  // CANEVA
		comune5044.setCodice(5044);
		comune5044.setNome("CANEVA");
		listaComuni71.add(comune5044);
		Comune comune5045 = new Comune();  // CASARSA DELLA DELIZIA
		comune5045.setCodice(5045);
		comune5045.setNome("CASARSA DELLA DELIZIA");
		listaComuni71.add(comune5045);
		Comune comune5046 = new Comune();  // CASTELNOVO DEL FRIULI
		comune5046.setCodice(5046);
		comune5046.setNome("CASTELNOVO DEL FRIULI");
		listaComuni71.add(comune5046);
		Comune comune5047 = new Comune();  // CAVASSO NUOVO
		comune5047.setCodice(5047);
		comune5047.setNome("CAVASSO NUOVO");
		listaComuni71.add(comune5047);
		Comune comune5048 = new Comune();  // CHIONS
		comune5048.setCodice(5048);
		comune5048.setNome("CHIONS");
		listaComuni71.add(comune5048);
		Comune comune5049 = new Comune();  // CIMOLAIS
		comune5049.setCodice(5049);
		comune5049.setNome("CIMOLAIS");
		listaComuni71.add(comune5049);
		Comune comune5050 = new Comune();  // CLAUT
		comune5050.setCodice(5050);
		comune5050.setNome("CLAUT");
		listaComuni71.add(comune5050);

		////// TRIESTE
		Comune comune4996 = new Comune();  // DUINO AURISINA
		comune4996.setCodice(4996);
		comune4996.setNome("DUINO AURISINA");
		listaComuni95.add(comune4996);
		Comune comune4997 = new Comune();  // MONRUPINO
		comune4997.setCodice(4997);
		comune4997.setNome("MONRUPINO");
		listaComuni95.add(comune4997);
		Comune comune4998 = new Comune();  // MUGGIA
		comune4998.setCodice(4998);
		comune4998.setNome("MUGGIA");
		listaComuni95.add(comune4998);
		Comune comune4999 = new Comune();  // S.DORLIGO DELLA VALLE
		comune4999.setCodice(4999);
		comune4999.setNome("S.DORLIGO DELLA VALLE");
		listaComuni95.add(comune4999);
		Comune comune5000 = new Comune();  // SGONICO
		comune5000.setCodice(5000);
		comune5000.setNome("SGONICO");
		listaComuni95.add(comune5000);
		Comune comune4995 = new Comune();  // TRIESTE
		comune4995.setCodice(4995);
		comune4995.setNome("TRIESTE");
		listaComuni95.add(comune4995);

		////// UDINE
		Comune comune5088 = new Comune();  // AIELLO DEL FRIULI
		comune5088.setCodice(5088);
		comune5088.setNome("AIELLO DEL FRIULI");
		listaComuni96.add(comune5088);
		Comune comune5089 = new Comune();  // AMARO
		comune5089.setCodice(5089);
		comune5089.setNome("AMARO");
		listaComuni96.add(comune5089);
		Comune comune5090 = new Comune();  // AMPEZZO
		comune5090.setCodice(5090);
		comune5090.setNome("AMPEZZO");
		listaComuni96.add(comune5090);
		Comune comune5091 = new Comune();  // AQUILEIA
		comune5091.setCodice(5091);
		comune5091.setNome("AQUILEIA");
		listaComuni96.add(comune5091);
		Comune comune5092 = new Comune();  // ARTA TERME
		comune5092.setCodice(5092);
		comune5092.setNome("ARTA TERME");
		listaComuni96.add(comune5092);
		Comune comune5093 = new Comune();  // ARTEGNA
		comune5093.setCodice(5093);
		comune5093.setNome("ARTEGNA");
		listaComuni96.add(comune5093);
		Comune comune5094 = new Comune();  // ATTIMIS
		comune5094.setCodice(5094);
		comune5094.setNome("ATTIMIS");
		listaComuni96.add(comune5094);
		Comune comune5095 = new Comune();  // BAGNARIA ARSA
		comune5095.setCodice(5095);
		comune5095.setNome("BAGNARIA ARSA");
		listaComuni96.add(comune5095);
		Comune comune5096 = new Comune();  // BASILIANO
		comune5096.setCodice(5096);
		comune5096.setNome("BASILIANO");
		listaComuni96.add(comune5096);
		Comune comune5097 = new Comune();  // BERTIOLO
		comune5097.setCodice(5097);
		comune5097.setNome("BERTIOLO");
		listaComuni96.add(comune5097);
		Comune comune5098 = new Comune();  // BICINICCO
		comune5098.setCodice(5098);
		comune5098.setNome("BICINICCO");
		listaComuni96.add(comune5098);
		Comune comune5099 = new Comune();  // BORDANO
		comune5099.setCodice(5099);
		comune5099.setNome("BORDANO");
		listaComuni96.add(comune5099);
		Comune comune5100 = new Comune();  // BUIA
		comune5100.setCodice(5100);
		comune5100.setNome("BUIA");
		listaComuni96.add(comune5100);
		Comune comune5101 = new Comune();  // BUTTRIO
		comune5101.setCodice(5101);
		comune5101.setNome("BUTTRIO");
		listaComuni96.add(comune5101);
		Comune comune5102 = new Comune();  // CAMINO AL TAGLIAMENTO
		comune5102.setCodice(5102);
		comune5102.setNome("CAMINO AL TAGLIAMENTO");
		listaComuni96.add(comune5102);


		/*
		 * LIGURIA
		 */

		////// GENOVA
		Comune comune5240 = new Comune();  // ARENZANO
		comune5240.setCodice(5240);
		comune5240.setNome("ARENZANO");
		listaComuni36.add(comune5240);
		Comune comune5241 = new Comune();  // AVEGNO
		comune5241.setCodice(5241);
		comune5241.setNome("AVEGNO");
		listaComuni36.add(comune5241);
		Comune comune5242 = new Comune();  // BARGAGLI
		comune5242.setCodice(5242);
		comune5242.setNome("BARGAGLI");
		listaComuni36.add(comune5242);
		Comune comune5243 = new Comune();  // BOGLIASCO
		comune5243.setCodice(5243);
		comune5243.setNome("BOGLIASCO");
		listaComuni36.add(comune5243);
		Comune comune5244 = new Comune();  // BORZONASCA
		comune5244.setCodice(5244);
		comune5244.setNome("BORZONASCA");
		listaComuni36.add(comune5244);
		Comune comune5245 = new Comune();  // BUSALLA
		comune5245.setCodice(5245);
		comune5245.setNome("BUSALLA");
		listaComuni36.add(comune5245);
		Comune comune5246 = new Comune();  // CAMOGLI
		comune5246.setCodice(5246);
		comune5246.setNome("CAMOGLI");
		listaComuni36.add(comune5246);
		Comune comune5247 = new Comune();  // CAMPO LIGURE
		comune5247.setCodice(5247);
		comune5247.setNome("CAMPO LIGURE");
		listaComuni36.add(comune5247);
		Comune comune5248 = new Comune();  // CAMPOMORONE
		comune5248.setCodice(5248);
		comune5248.setNome("CAMPOMORONE");
		listaComuni36.add(comune5248);
		Comune comune5249 = new Comune();  // CARASCO
		comune5249.setCodice(5249);
		comune5249.setNome("CARASCO");
		listaComuni36.add(comune5249);
		Comune comune5250 = new Comune();  // CASARZA LIGURE
		comune5250.setCodice(5250);
		comune5250.setNome("CASARZA LIGURE");
		listaComuni36.add(comune5250);
		Comune comune5251 = new Comune();  // CASELLA
		comune5251.setCodice(5251);
		comune5251.setNome("CASELLA");
		listaComuni36.add(comune5251);
		Comune comune5252 = new Comune();  // CASTIGLIONE CHIAVARESE
		comune5252.setCodice(5252);
		comune5252.setNome("CASTIGLIONE CHIAVARESE");
		listaComuni36.add(comune5252);
		Comune comune5253 = new Comune();  // CERANESI
		comune5253.setCodice(5253);
		comune5253.setNome("CERANESI");
		listaComuni36.add(comune5253);
		Comune comune5254 = new Comune();  // CHIAVARI
		comune5254.setCodice(5254);
		comune5254.setNome("CHIAVARI");
		listaComuni36.add(comune5254);

		////// IMPERIA
		Comune comune5329 = new Comune();  // AIROLE
		comune5329.setCodice(5329);
		comune5329.setNome("AIROLE");
		listaComuni39.add(comune5329);
		Comune comune5330 = new Comune();  // APRICALE
		comune5330.setCodice(5330);
		comune5330.setNome("APRICALE");
		listaComuni39.add(comune5330);
		Comune comune5331 = new Comune();  // AQUILA DI ARROSCIA
		comune5331.setCodice(5331);
		comune5331.setNome("AQUILA DI ARROSCIA");
		listaComuni39.add(comune5331);
		Comune comune5332 = new Comune();  // ARMO
		comune5332.setCodice(5332);
		comune5332.setNome("ARMO");
		listaComuni39.add(comune5332);
		Comune comune5333 = new Comune();  // AURIGO
		comune5333.setCodice(5333);
		comune5333.setNome("AURIGO");
		listaComuni39.add(comune5333);
		Comune comune5334 = new Comune();  // BADALUCCO
		comune5334.setCodice(5334);
		comune5334.setNome("BADALUCCO");
		listaComuni39.add(comune5334);
		Comune comune5335 = new Comune();  // BAJARDO
		comune5335.setCodice(5335);
		comune5335.setNome("BAJARDO");
		listaComuni39.add(comune5335);
		Comune comune5336 = new Comune();  // BORDIGHERA
		comune5336.setCodice(5336);
		comune5336.setNome("BORDIGHERA");
		listaComuni39.add(comune5336);
		Comune comune5337 = new Comune();  // BORGHETTO DI ARROSCIA
		comune5337.setCodice(5337);
		comune5337.setNome("BORGHETTO DI ARROSCIA");
		listaComuni39.add(comune5337);
		Comune comune5338 = new Comune();  // BORGOMARO
		comune5338.setCodice(5338);
		comune5338.setNome("BORGOMARO");
		listaComuni39.add(comune5338);
		Comune comune5339 = new Comune();  // CAMPOROSSO
		comune5339.setCodice(5339);
		comune5339.setNome("CAMPOROSSO");
		listaComuni39.add(comune5339);
		Comune comune5340 = new Comune();  // CARAVONICA
		comune5340.setCodice(5340);
		comune5340.setNome("CARAVONICA");
		listaComuni39.add(comune5340);
		Comune comune5341 = new Comune();  // CARPASIO
		comune5341.setCodice(5341);
		comune5341.setNome("CARPASIO");
		listaComuni39.add(comune5341);
		Comune comune5342 = new Comune();  // CASTEL VITTORIO
		comune5342.setCodice(5342);
		comune5342.setNome("CASTEL VITTORIO");
		listaComuni39.add(comune5342);
		Comune comune5343 = new Comune();  // CASTELLARO
		comune5343.setCodice(5343);
		comune5343.setNome("CASTELLARO");
		listaComuni39.add(comune5343);

		////// LA SPEZIA
		Comune comune5420 = new Comune();  // AMEGLIA
		comune5420.setCodice(5420);
		comune5420.setNome("AMEGLIA");
		listaComuni41.add(comune5420);
		Comune comune5421 = new Comune();  // ARCOLA
		comune5421.setCodice(5421);
		comune5421.setNome("ARCOLA");
		listaComuni41.add(comune5421);
		Comune comune5422 = new Comune();  // BEVERINO
		comune5422.setCodice(5422);
		comune5422.setNome("BEVERINO");
		listaComuni41.add(comune5422);
		Comune comune5423 = new Comune();  // BOLANO
		comune5423.setCodice(5423);
		comune5423.setNome("BOLANO");
		listaComuni41.add(comune5423);
		Comune comune5424 = new Comune();  // BONASSOLA
		comune5424.setCodice(5424);
		comune5424.setNome("BONASSOLA");
		listaComuni41.add(comune5424);
		Comune comune5425 = new Comune();  // BORGHETTO DI VARA
		comune5425.setCodice(5425);
		comune5425.setNome("BORGHETTO DI VARA");
		listaComuni41.add(comune5425);
		Comune comune5426 = new Comune();  // BRUGNATO
		comune5426.setCodice(5426);
		comune5426.setNome("BRUGNATO");
		listaComuni41.add(comune5426);
		Comune comune5427 = new Comune();  // CALICE AL CORNOVIGLIO
		comune5427.setCodice(5427);
		comune5427.setNome("CALICE AL CORNOVIGLIO");
		listaComuni41.add(comune5427);
		Comune comune5428 = new Comune();  // CARRO
		comune5428.setCodice(5428);
		comune5428.setNome("CARRO");
		listaComuni41.add(comune5428);
		Comune comune5429 = new Comune();  // CARRODANO
		comune5429.setCodice(5429);
		comune5429.setNome("CARRODANO");
		listaComuni41.add(comune5429);
		Comune comune5430 = new Comune();  // CASTELNUOVO DI MAGRA
		comune5430.setCodice(5430);
		comune5430.setNome("CASTELNUOVO DI MAGRA");
		listaComuni41.add(comune5430);
		Comune comune5431 = new Comune();  // DEIVA MARINA
		comune5431.setCodice(5431);
		comune5431.setNome("DEIVA MARINA");
		listaComuni41.add(comune5431);
		Comune comune5432 = new Comune();  // FOLLO
		comune5432.setCodice(5432);
		comune5432.setNome("FOLLO");
		listaComuni41.add(comune5432);
		Comune comune5433 = new Comune();  // FRAMURA
		comune5433.setCodice(5433);
		comune5433.setNome("FRAMURA");
		listaComuni41.add(comune5433);
		Comune comune5419 = new Comune();  // LA SPEZIA
		comune5419.setCodice(5419);
		comune5419.setNome("LA SPEZIA");
		listaComuni41.add(comune5419);

		////// SAVONA
		Comune comune5452 = new Comune();  // ALASSIO
		comune5452.setCodice(5452);
		comune5452.setNome("ALASSIO");
		listaComuni84.add(comune5452);
		Comune comune5453 = new Comune();  // ALBENGA
		comune5453.setCodice(5453);
		comune5453.setNome("ALBENGA");
		listaComuni84.add(comune5453);
		Comune comune5455 = new Comune();  // ALBISOLA SUPERIORE
		comune5455.setCodice(5455);
		comune5455.setNome("ALBISOLA SUPERIORE");
		listaComuni84.add(comune5455);
		Comune comune5454 = new Comune();  // ALBISSOLA MARINA
		comune5454.setCodice(5454);
		comune5454.setNome("ALBISSOLA MARINA");
		listaComuni84.add(comune5454);
		Comune comune5456 = new Comune();  // ALTARE
		comune5456.setCodice(5456);
		comune5456.setNome("ALTARE");
		listaComuni84.add(comune5456);
		Comune comune5457 = new Comune();  // ANDORA
		comune5457.setCodice(5457);
		comune5457.setNome("ANDORA");
		listaComuni84.add(comune5457);
		Comune comune5458 = new Comune();  // ARNASCO
		comune5458.setCodice(5458);
		comune5458.setNome("ARNASCO");
		listaComuni84.add(comune5458);
		Comune comune5459 = new Comune();  // BALESTRINO
		comune5459.setCodice(5459);
		comune5459.setNome("BALESTRINO");
		listaComuni84.add(comune5459);
		Comune comune5460 = new Comune();  // BARDINETO
		comune5460.setCodice(5460);
		comune5460.setNome("BARDINETO");
		listaComuni84.add(comune5460);
		Comune comune5461 = new Comune();  // BERGEGGI
		comune5461.setCodice(5461);
		comune5461.setNome("BERGEGGI");
		listaComuni84.add(comune5461);
		Comune comune5462 = new Comune();  // BOISSANO
		comune5462.setCodice(5462);
		comune5462.setNome("BOISSANO");
		listaComuni84.add(comune5462);
		Comune comune5463 = new Comune();  // BORGHETTO SANTO SPIRITO
		comune5463.setCodice(5463);
		comune5463.setNome("BORGHETTO SANTO SPIRITO");
		listaComuni84.add(comune5463);
		Comune comune5464 = new Comune();  // BORGIO VEREZZI
		comune5464.setCodice(5464);
		comune5464.setNome("BORGIO VEREZZI");
		listaComuni84.add(comune5464);
		Comune comune5465 = new Comune();  // BORMIDA
		comune5465.setCodice(5465);
		comune5465.setNome("BORMIDA");
		listaComuni84.add(comune5465);
		Comune comune5466 = new Comune();  // CAIRO MONTENOTTE
		comune5466.setCodice(5466);
		comune5466.setNome("CAIRO MONTENOTTE");
		listaComuni84.add(comune5466);


		/*
		 * EMILIA ROMAGNA
		 */

		////// BOLOGNA
		Comune comune5531 = new Comune();  // ANZOLA DELL'EMILIA
		comune5531.setCodice(5531);
		comune5531.setNome("ANZOLA DELL'EMILIA");
		listaComuni13.add(comune5531);
		Comune comune5532 = new Comune();  // ARGELATO
		comune5532.setCodice(5532);
		comune5532.setNome("ARGELATO");
		listaComuni13.add(comune5532);
		Comune comune5533 = new Comune();  // BARICELLA
		comune5533.setCodice(5533);
		comune5533.setNome("BARICELLA");
		listaComuni13.add(comune5533);
		Comune comune5534 = new Comune();  // BAZZANO
		comune5534.setCodice(5534);
		comune5534.setNome("BAZZANO");
		listaComuni13.add(comune5534);
		Comune comune5535 = new Comune();  // BENTIVOGLIO
		comune5535.setCodice(5535);
		comune5535.setNome("BENTIVOGLIO");
		listaComuni13.add(comune5535);
		Comune comune5530 = new Comune();  // BOLOGNA
		comune5530.setCodice(5530);
		comune5530.setNome("BOLOGNA");
		listaComuni13.add(comune5530);
		Comune comune5536 = new Comune();  // BORGO TOSSIGNANO
		comune5536.setCodice(5536);
		comune5536.setNome("BORGO TOSSIGNANO");
		listaComuni13.add(comune5536);
		Comune comune5537 = new Comune();  // BUDRIO
		comune5537.setCodice(5537);
		comune5537.setNome("BUDRIO");
		listaComuni13.add(comune5537);
		Comune comune5538 = new Comune();  // CALDERARA DI RENO
		comune5538.setCodice(5538);
		comune5538.setNome("CALDERARA DI RENO");
		listaComuni13.add(comune5538);
		Comune comune5539 = new Comune();  // CAMUGNANO
		comune5539.setCodice(5539);
		comune5539.setNome("CAMUGNANO");
		listaComuni13.add(comune5539);
		Comune comune5540 = new Comune();  // CASAL FIUMANESE
		comune5540.setCodice(5540);
		comune5540.setNome("CASAL FIUMANESE");
		listaComuni13.add(comune5540);
		Comune comune5541 = new Comune();  // CASALECCHIO DI RENO
		comune5541.setCodice(5541);
		comune5541.setNome("CASALECCHIO DI RENO");
		listaComuni13.add(comune5541);
		Comune comune5542 = new Comune();  // CASTEL D'AIANO
		comune5542.setCodice(5542);
		comune5542.setNome("CASTEL D'AIANO");
		listaComuni13.add(comune5542);
		Comune comune5543 = new Comune();  // CASTEL DEL RIO
		comune5543.setCodice(5543);
		comune5543.setNome("CASTEL DEL RIO");
		listaComuni13.add(comune5543);
		Comune comune5544 = new Comune();  // CASTEL DI CASIO
		comune5544.setCodice(5544);
		comune5544.setNome("CASTEL DI CASIO");
		listaComuni13.add(comune5544);

		////// FERRARA
		Comune comune5597 = new Comune();  // ARGENTA
		comune5597.setCodice(5597);
		comune5597.setNome("ARGENTA");
		listaComuni30.add(comune5597);
		Comune comune5598 = new Comune();  // BERRA
		comune5598.setCodice(5598);
		comune5598.setNome("BERRA");
		listaComuni30.add(comune5598);
		Comune comune5599 = new Comune();  // BONDENO
		comune5599.setCodice(5599);
		comune5599.setNome("BONDENO");
		listaComuni30.add(comune5599);
		Comune comune5623 = new Comune();  // CASAGLIA(FERRARA)
		comune5623.setCodice(5623);
		comune5623.setNome("CASAGLIA(FERRARA)");
		listaComuni30.add(comune5623);
		Comune comune5600 = new Comune();  // CENTO
		comune5600.setCodice(5600);
		comune5600.setNome("CENTO");
		listaComuni30.add(comune5600);
		Comune comune5601 = new Comune();  // CODIGORO
		comune5601.setCodice(5601);
		comune5601.setNome("CODIGORO");
		listaComuni30.add(comune5601);
		Comune comune5602 = new Comune();  // COMACCHIO
		comune5602.setCodice(5602);
		comune5602.setNome("COMACCHIO");
		listaComuni30.add(comune5602);
		Comune comune5603 = new Comune();  // COPPARO
		comune5603.setCodice(5603);
		comune5603.setNome("COPPARO");
		listaComuni30.add(comune5603);
		Comune comune5624 = new Comune();  // CUSUMARO(CENTO)
		comune5624.setCodice(5624);
		comune5624.setNome("CUSUMARO(CENTO)");
		listaComuni30.add(comune5624);
		Comune comune5596 = new Comune();  // FERRARA
		comune5596.setCodice(5596);
		comune5596.setNome("FERRARA");
		listaComuni30.add(comune5596);
		Comune comune5627 = new Comune();  // FINAL DI RERO GIA' FRAZ. FORMIGNANA ORA TRESIGALLO
		comune5627.setCodice(5627);
		comune5627.setNome("FINAL DI RERO GIA' FRAZ. FORMIGNANA ORA TRESIGALLO");
		listaComuni30.add(comune5627);
		Comune comune5604 = new Comune();  // FORMIGNANA
		comune5604.setCodice(5604);
		comune5604.setNome("FORMIGNANA");
		listaComuni30.add(comune5604);
		Comune comune5619 = new Comune();  // GORO
		comune5619.setCodice(5619);
		comune5619.setNome("GORO");
		listaComuni30.add(comune5619);
		Comune comune5605 = new Comune();  // JOLANDA DI SAVOIA
		comune5605.setCodice(5605);
		comune5605.setNome("JOLANDA DI SAVOIA");
		listaComuni30.add(comune5605);
		Comune comune5606 = new Comune();  // LAGOSANTO
		comune5606.setCodice(5606);
		comune5606.setNome("LAGOSANTO");
		listaComuni30.add(comune5606);

		////// FORLI'-CESENA
		Comune comune5629 = new Comune();  // BAGNO DI ROMAGNA
		comune5629.setCodice(5629);
		comune5629.setNome("BAGNO DI ROMAGNA");
		listaComuni34.add(comune5629);
		Comune comune5631 = new Comune();  // BERTINORO
		comune5631.setCodice(5631);
		comune5631.setNome("BERTINORO");
		listaComuni34.add(comune5631);
		Comune comune5632 = new Comune();  // BORGHI
		comune5632.setCodice(5632);
		comune5632.setNome("BORGHI");
		listaComuni34.add(comune5632);
		Comune comune5633 = new Comune();  // CASTROCARO TERME E TERRA DEL SOLE
		comune5633.setCodice(5633);
		comune5633.setNome("CASTROCARO TERME E TERRA DEL SOLE");
		listaComuni34.add(comune5633);
		Comune comune5635 = new Comune();  // CESENA
		comune5635.setCodice(5635);
		comune5635.setNome("CESENA");
		listaComuni34.add(comune5635);
		Comune comune5636 = new Comune();  // CESENATICO
		comune5636.setCodice(5636);
		comune5636.setNome("CESENATICO");
		listaComuni34.add(comune5636);
		Comune comune5637 = new Comune();  // CIVITELLA DI ROMAGNA
		comune5637.setCodice(5637);
		comune5637.setNome("CIVITELLA DI ROMAGNA");
		listaComuni34.add(comune5637);
		Comune comune5639 = new Comune();  // DOVADOLA
		comune5639.setCodice(5639);
		comune5639.setNome("DOVADOLA");
		listaComuni34.add(comune5639);
		Comune comune5628 = new Comune();  // FORLI'
		comune5628.setCodice(5628);
		comune5628.setNome("FORLI'");
		listaComuni34.add(comune5628);
		Comune comune5640 = new Comune();  // FORLIMPOPOLI
		comune5640.setCodice(5640);
		comune5640.setNome("FORLIMPOPOLI");
		listaComuni34.add(comune5640);
		Comune comune5641 = new Comune();  // GALEATA
		comune5641.setCodice(5641);
		comune5641.setNome("GALEATA");
		listaComuni34.add(comune5641);
		Comune comune5642 = new Comune();  // GAMBETTOLA
		comune5642.setCodice(5642);
		comune5642.setNome("GAMBETTOLA");
		listaComuni34.add(comune5642);
		Comune comune5643 = new Comune();  // GATTEO
		comune5643.setCodice(5643);
		comune5643.setNome("GATTEO");
		listaComuni34.add(comune5643);
		Comune comune5645 = new Comune();  // LONGIANO
		comune5645.setCodice(5645);
		comune5645.setNome("LONGIANO");
		listaComuni34.add(comune5645);
		Comune comune5646 = new Comune();  // MELDOLA
		comune5646.setCodice(5646);
		comune5646.setNome("MELDOLA");
		listaComuni34.add(comune5646);

		////// MODENA
		Comune comune5685 = new Comune();  // BASTIGLIA
		comune5685.setCodice(5685);
		comune5685.setNome("BASTIGLIA");
		listaComuni55.add(comune5685);
		Comune comune5686 = new Comune();  // BOMPORTO
		comune5686.setCodice(5686);
		comune5686.setNome("BOMPORTO");
		listaComuni55.add(comune5686);
		Comune comune5687 = new Comune();  // CAMPOGALLIANO
		comune5687.setCodice(5687);
		comune5687.setNome("CAMPOGALLIANO");
		listaComuni55.add(comune5687);
		Comune comune5688 = new Comune();  // CAMPOSANTO
		comune5688.setCodice(5688);
		comune5688.setNome("CAMPOSANTO");
		listaComuni55.add(comune5688);
		Comune comune5689 = new Comune();  // CARPI
		comune5689.setCodice(5689);
		comune5689.setNome("CARPI");
		listaComuni55.add(comune5689);
		Comune comune5690 = new Comune();  // CASTELFRANCO EMILIA
		comune5690.setCodice(5690);
		comune5690.setNome("CASTELFRANCO EMILIA");
		listaComuni55.add(comune5690);
		Comune comune5691 = new Comune();  // CASTELNUOVO RANGONE
		comune5691.setCodice(5691);
		comune5691.setNome("CASTELNUOVO RANGONE");
		listaComuni55.add(comune5691);
		Comune comune5692 = new Comune();  // CASTELVETRO DI MODENA
		comune5692.setCodice(5692);
		comune5692.setNome("CASTELVETRO DI MODENA");
		listaComuni55.add(comune5692);
		Comune comune5693 = new Comune();  // CAVEZZO
		comune5693.setCodice(5693);
		comune5693.setNome("CAVEZZO");
		listaComuni55.add(comune5693);
		Comune comune5694 = new Comune();  // CONCORDIA SULLA SECCHIA
		comune5694.setCodice(5694);
		comune5694.setNome("CONCORDIA SULLA SECCHIA");
		listaComuni55.add(comune5694);
		Comune comune5695 = new Comune();  // FANANO
		comune5695.setCodice(5695);
		comune5695.setNome("FANANO");
		listaComuni55.add(comune5695);
		Comune comune5696 = new Comune();  // FINALE EMILIA
		comune5696.setCodice(5696);
		comune5696.setNome("FINALE EMILIA");
		listaComuni55.add(comune5696);
		Comune comune5697 = new Comune();  // FIORANO MODENESE
		comune5697.setCodice(5697);
		comune5697.setNome("FIORANO MODENESE");
		listaComuni55.add(comune5697);
		Comune comune5698 = new Comune();  // FIUMALBO
		comune5698.setCodice(5698);
		comune5698.setNome("FIUMALBO");
		listaComuni55.add(comune5698);
		Comune comune5699 = new Comune();  // FORMIGINE
		comune5699.setCodice(5699);
		comune5699.setNome("FORMIGINE");
		listaComuni55.add(comune5699);

		////// PARMA
		Comune comune5733 = new Comune();  // ALBARETO
		comune5733.setCodice(5733);
		comune5733.setNome("ALBARETO");
		listaComuni62.add(comune5733);
		Comune comune5734 = new Comune();  // BARDI
		comune5734.setCodice(5734);
		comune5734.setNome("BARDI");
		listaComuni62.add(comune5734);
		Comune comune5735 = new Comune();  // BEDONIA
		comune5735.setCodice(5735);
		comune5735.setNome("BEDONIA");
		listaComuni62.add(comune5735);
		Comune comune5736 = new Comune();  // BERCETO
		comune5736.setCodice(5736);
		comune5736.setNome("BERCETO");
		listaComuni62.add(comune5736);
		Comune comune5737 = new Comune();  // BORE
		comune5737.setCodice(5737);
		comune5737.setNome("BORE");
		listaComuni62.add(comune5737);
		Comune comune5738 = new Comune();  // BORGO VAL DI TARO
		comune5738.setCodice(5738);
		comune5738.setNome("BORGO VAL DI TARO");
		listaComuni62.add(comune5738);
		Comune comune5739 = new Comune();  // BUSSETO
		comune5739.setCodice(5739);
		comune5739.setNome("BUSSETO");
		listaComuni62.add(comune5739);
		Comune comune5740 = new Comune();  // CALESTANO
		comune5740.setCodice(5740);
		comune5740.setNome("CALESTANO");
		listaComuni62.add(comune5740);
		Comune comune5741 = new Comune();  // COLLECCHIO
		comune5741.setCodice(5741);
		comune5741.setNome("COLLECCHIO");
		listaComuni62.add(comune5741);
		Comune comune5742 = new Comune();  // COLORNO
		comune5742.setCodice(5742);
		comune5742.setNome("COLORNO");
		listaComuni62.add(comune5742);
		Comune comune5743 = new Comune();  // COMPIANO
		comune5743.setCodice(5743);
		comune5743.setNome("COMPIANO");
		listaComuni62.add(comune5743);
		Comune comune5744 = new Comune();  // CORNIGLIO
		comune5744.setCodice(5744);
		comune5744.setNome("CORNIGLIO");
		listaComuni62.add(comune5744);
		Comune comune5745 = new Comune();  // FELINO
		comune5745.setCodice(5745);
		comune5745.setNome("FELINO");
		listaComuni62.add(comune5745);
		Comune comune5746 = new Comune();  // FIDENZA
		comune5746.setCodice(5746);
		comune5746.setNome("FIDENZA");
		listaComuni62.add(comune5746);
		Comune comune5747 = new Comune();  // FONTANELLATO
		comune5747.setCodice(5747);
		comune5747.setNome("FONTANELLATO");
		listaComuni62.add(comune5747);

		////// PIACENZA
		Comune comune5789 = new Comune();  // AGAZZANO
		comune5789.setCodice(5789);
		comune5789.setNome("AGAZZANO");
		listaComuni67.add(comune5789);
		Comune comune5790 = new Comune();  // ALSENO
		comune5790.setCodice(5790);
		comune5790.setNome("ALSENO");
		listaComuni67.add(comune5790);
		Comune comune5791 = new Comune();  // BESENZONE
		comune5791.setCodice(5791);
		comune5791.setNome("BESENZONE");
		listaComuni67.add(comune5791);
		Comune comune5792 = new Comune();  // BETTOLA
		comune5792.setCodice(5792);
		comune5792.setNome("BETTOLA");
		listaComuni67.add(comune5792);
		Comune comune5793 = new Comune();  // BOBBIO
		comune5793.setCodice(5793);
		comune5793.setNome("BOBBIO");
		listaComuni67.add(comune5793);
		Comune comune5794 = new Comune();  // BORGONOVO VAL TIDONE
		comune5794.setCodice(5794);
		comune5794.setNome("BORGONOVO VAL TIDONE");
		listaComuni67.add(comune5794);
		Comune comune5795 = new Comune();  // CADEO
		comune5795.setCodice(5795);
		comune5795.setNome("CADEO");
		listaComuni67.add(comune5795);
		Comune comune5796 = new Comune();  // CALENDASCO
		comune5796.setCodice(5796);
		comune5796.setNome("CALENDASCO");
		listaComuni67.add(comune5796);
		Comune comune5797 = new Comune();  // CAMINATA
		comune5797.setCodice(5797);
		comune5797.setNome("CAMINATA");
		listaComuni67.add(comune5797);
		Comune comune5798 = new Comune();  // CAORSO
		comune5798.setCodice(5798);
		comune5798.setNome("CAORSO");
		listaComuni67.add(comune5798);
		Comune comune5799 = new Comune();  // CARPANETO PIACENTINO
		comune5799.setCodice(5799);
		comune5799.setNome("CARPANETO PIACENTINO");
		listaComuni67.add(comune5799);
		Comune comune5800 = new Comune();  // CASTEL S.GIOVANNI
		comune5800.setCodice(5800);
		comune5800.setNome("CASTEL S.GIOVANNI");
		listaComuni67.add(comune5800);
		Comune comune5801 = new Comune();  // CASTELL'ARQUATO
		comune5801.setCodice(5801);
		comune5801.setNome("CASTELL'ARQUATO");
		listaComuni67.add(comune5801);
		Comune comune5802 = new Comune();  // CASTELVETRO PIACENTINO
		comune5802.setCodice(5802);
		comune5802.setNome("CASTELVETRO PIACENTINO");
		listaComuni67.add(comune5802);
		Comune comune5803 = new Comune();  // CERIGNALE
		comune5803.setCodice(5803);
		comune5803.setNome("CERIGNALE");
		listaComuni67.add(comune5803);

		////// RAVENNA
		Comune comune5842 = new Comune();  // ALFONSINE
		comune5842.setCodice(5842);
		comune5842.setNome("ALFONSINE");
		listaComuni75.add(comune5842);
		Comune comune5843 = new Comune();  // BAGNACAVALLO
		comune5843.setCodice(5843);
		comune5843.setNome("BAGNACAVALLO");
		listaComuni75.add(comune5843);
		Comune comune5844 = new Comune();  // BAGNARA DI ROMAGNA
		comune5844.setCodice(5844);
		comune5844.setNome("BAGNARA DI ROMAGNA");
		listaComuni75.add(comune5844);
		Comune comune5845 = new Comune();  // BRISIGHELLA
		comune5845.setCodice(5845);
		comune5845.setNome("BRISIGHELLA");
		listaComuni75.add(comune5845);
		Comune comune5846 = new Comune();  // CASOLA VALSENIO
		comune5846.setCodice(5846);
		comune5846.setNome("CASOLA VALSENIO");
		listaComuni75.add(comune5846);
		Comune comune5847 = new Comune();  // CASTEL BOLOGNESE
		comune5847.setCodice(5847);
		comune5847.setNome("CASTEL BOLOGNESE");
		listaComuni75.add(comune5847);
		Comune comune5848 = new Comune();  // CERVIA
		comune5848.setCodice(5848);
		comune5848.setNome("CERVIA");
		listaComuni75.add(comune5848);
		Comune comune5849 = new Comune();  // CONSELICE
		comune5849.setCodice(5849);
		comune5849.setNome("CONSELICE");
		listaComuni75.add(comune5849);
		Comune comune5850 = new Comune();  // COTIGNOLA
		comune5850.setCodice(5850);
		comune5850.setNome("COTIGNOLA");
		listaComuni75.add(comune5850);
		Comune comune5851 = new Comune();  // FAENZA
		comune5851.setCodice(5851);
		comune5851.setNome("FAENZA");
		listaComuni75.add(comune5851);
		Comune comune5852 = new Comune();  // FUSIGNANO
		comune5852.setCodice(5852);
		comune5852.setNome("FUSIGNANO");
		listaComuni75.add(comune5852);
		Comune comune5853 = new Comune();  // LUGO
		comune5853.setCodice(5853);
		comune5853.setNome("LUGO");
		listaComuni75.add(comune5853);
		Comune comune5854 = new Comune();  // MASSA LOMBARDA
		comune5854.setCodice(5854);
		comune5854.setNome("MASSA LOMBARDA");
		listaComuni75.add(comune5854);
		Comune comune5841 = new Comune();  // RAVENNA
		comune5841.setCodice(5841);
		comune5841.setNome("RAVENNA");
		listaComuni75.add(comune5841);
		Comune comune5855 = new Comune();  // RIOLO TERME
		comune5855.setCodice(5855);
		comune5855.setNome("RIOLO TERME");
		listaComuni75.add(comune5855);

		////// REGGIO NELL'EMILIA
		Comune comune5862 = new Comune();  // ALBINEA
		comune5862.setCodice(5862);
		comune5862.setNome("ALBINEA");
		listaComuni77.add(comune5862);
		Comune comune5863 = new Comune();  // BAGNOLO IN PIANO
		comune5863.setCodice(5863);
		comune5863.setNome("BAGNOLO IN PIANO");
		listaComuni77.add(comune5863);
		Comune comune5864 = new Comune();  // BAISO
		comune5864.setCodice(5864);
		comune5864.setNome("BAISO");
		listaComuni77.add(comune5864);
		Comune comune5865 = new Comune();  // BIBBIANO
		comune5865.setCodice(5865);
		comune5865.setNome("BIBBIANO");
		listaComuni77.add(comune5865);
		Comune comune5866 = new Comune();  // BORETTO
		comune5866.setCodice(5866);
		comune5866.setNome("BORETTO");
		listaComuni77.add(comune5866);
		Comune comune5867 = new Comune();  // BRESCELLO
		comune5867.setCodice(5867);
		comune5867.setNome("BRESCELLO");
		listaComuni77.add(comune5867);
		Comune comune5868 = new Comune();  // BUSANA
		comune5868.setCodice(5868);
		comune5868.setNome("BUSANA");
		listaComuni77.add(comune5868);
		Comune comune5869 = new Comune();  // CADELBOSCO DI SOPRA
		comune5869.setCodice(5869);
		comune5869.setNome("CADELBOSCO DI SOPRA");
		listaComuni77.add(comune5869);
		Comune comune5870 = new Comune();  // CAMPAGNOLA EMILIA
		comune5870.setCodice(5870);
		comune5870.setNome("CAMPAGNOLA EMILIA");
		listaComuni77.add(comune5870);
		Comune comune5871 = new Comune();  // CAMPEGINE
		comune5871.setCodice(5871);
		comune5871.setNome("CAMPEGINE");
		listaComuni77.add(comune5871);
		Comune comune5906 = new Comune();  // CANOSSA
		comune5906.setCodice(5906);
		comune5906.setNome("CANOSSA");
		listaComuni77.add(comune5906);
		Comune comune5872 = new Comune();  // CARPINETI
		comune5872.setCodice(5872);
		comune5872.setNome("CARPINETI");
		listaComuni77.add(comune5872);
		Comune comune5873 = new Comune();  // CASALGRANDE
		comune5873.setCodice(5873);
		comune5873.setNome("CASALGRANDE");
		listaComuni77.add(comune5873);
		Comune comune5874 = new Comune();  // CASINA
		comune5874.setCodice(5874);
		comune5874.setNome("CASINA");
		listaComuni77.add(comune5874);
		Comune comune5875 = new Comune();  // CASTELLARANO
		comune5875.setCodice(5875);
		comune5875.setNome("CASTELLARANO");
		listaComuni77.add(comune5875);

		////// RIMINI
		Comune comune5908 = new Comune();  // BELLARIA IGEA MARINA
		comune5908.setCodice(5908);
		comune5908.setNome("BELLARIA IGEA MARINA");
		listaComuni79.add(comune5908);
		Comune comune5909 = new Comune();  // CATTOLICA
		comune5909.setCodice(5909);
		comune5909.setNome("CATTOLICA");
		listaComuni79.add(comune5909);
		Comune comune5910 = new Comune();  // CORIANO
		comune5910.setCodice(5910);
		comune5910.setNome("CORIANO");
		listaComuni79.add(comune5910);
		Comune comune5911 = new Comune();  // GEMMANO
		comune5911.setCodice(5911);
		comune5911.setNome("GEMMANO");
		listaComuni79.add(comune5911);
		Comune comune5912 = new Comune();  // MISANO ADRIATICO
		comune5912.setCodice(5912);
		comune5912.setNome("MISANO ADRIATICO");
		listaComuni79.add(comune5912);
		Comune comune5913 = new Comune();  // MONDAINO
		comune5913.setCodice(5913);
		comune5913.setNome("MONDAINO");
		listaComuni79.add(comune5913);
		Comune comune5914 = new Comune();  // MONTE COLOMBO
		comune5914.setCodice(5914);
		comune5914.setNome("MONTE COLOMBO");
		listaComuni79.add(comune5914);
		Comune comune5916 = new Comune();  // MONTE GRIDOLFO
		comune5916.setCodice(5916);
		comune5916.setNome("MONTE GRIDOLFO");
		listaComuni79.add(comune5916);
		Comune comune5915 = new Comune();  // MONTEFIORE CONCA
		comune5915.setCodice(5915);
		comune5915.setNome("MONTEFIORE CONCA");
		listaComuni79.add(comune5915);
		Comune comune5917 = new Comune();  // MONTESCUDO
		comune5917.setCodice(5917);
		comune5917.setNome("MONTESCUDO");
		listaComuni79.add(comune5917);
		Comune comune5918 = new Comune();  // MORCIANO DI ROMAGNA
		comune5918.setCodice(5918);
		comune5918.setNome("MORCIANO DI ROMAGNA");
		listaComuni79.add(comune5918);
		Comune comune5919 = new Comune();  // POGGIO BERNI
		comune5919.setCodice(5919);
		comune5919.setNome("POGGIO BERNI");
		listaComuni79.add(comune5919);
		Comune comune5920 = new Comune();  // RICCIONE
		comune5920.setCodice(5920);
		comune5920.setNome("RICCIONE");
		listaComuni79.add(comune5920);
		Comune comune5907 = new Comune();  // RIMINI
		comune5907.setCodice(5907);
		comune5907.setNome("RIMINI");
		listaComuni79.add(comune5907);
		Comune comune5921 = new Comune();  // SALUDECIO
		comune5921.setCodice(5921);
		comune5921.setNome("SALUDECIO");
		listaComuni79.add(comune5921);


		/*
		 * TOSCANA
		 */

		////// AREZZO
		Comune comune5983 = new Comune();  // ANGHIARI
		comune5983.setCodice(5983);
		comune5983.setNome("ANGHIARI");
		listaComuni4.add(comune5983);
		Comune comune5982 = new Comune();  // AREZZO
		comune5982.setCodice(5982);
		comune5982.setNome("AREZZO");
		listaComuni4.add(comune5982);
		Comune comune5984 = new Comune();  // BADIA TEDALDA
		comune5984.setCodice(5984);
		comune5984.setNome("BADIA TEDALDA");
		listaComuni4.add(comune5984);
		Comune comune5985 = new Comune();  // BIBBIENA
		comune5985.setCodice(5985);
		comune5985.setNome("BIBBIENA");
		listaComuni4.add(comune5985);
		Comune comune5986 = new Comune();  // BUCINE
		comune5986.setCodice(5986);
		comune5986.setNome("BUCINE");
		listaComuni4.add(comune5986);
		Comune comune5987 = new Comune();  // CAPOLONA
		comune5987.setCodice(5987);
		comune5987.setNome("CAPOLONA");
		listaComuni4.add(comune5987);
		Comune comune5988 = new Comune();  // CAPRESE MICHELANGELO
		comune5988.setCodice(5988);
		comune5988.setNome("CAPRESE MICHELANGELO");
		listaComuni4.add(comune5988);
		Comune comune5989 = new Comune();  // CASTEL FOCOGNANO
		comune5989.setCodice(5989);
		comune5989.setNome("CASTEL FOCOGNANO");
		listaComuni4.add(comune5989);
		Comune comune5990 = new Comune();  // CASTEL S.NICCOLO'
		comune5990.setCodice(5990);
		comune5990.setNome("CASTEL S.NICCOLO'");
		listaComuni4.add(comune5990);
		Comune comune5991 = new Comune();  // CASTELFRANCO DI SOPRA
		comune5991.setCodice(5991);
		comune5991.setNome("CASTELFRANCO DI SOPRA");
		listaComuni4.add(comune5991);
		Comune comune5993 = new Comune();  // CASTIGLION FIORENTINO
		comune5993.setCodice(5993);
		comune5993.setNome("CASTIGLION FIORENTINO");
		listaComuni4.add(comune5993);
		Comune comune5992 = new Comune();  // CASTIGLIONE FIBOCCHI
		comune5992.setCodice(5992);
		comune5992.setNome("CASTIGLIONE FIBOCCHI");
		listaComuni4.add(comune5992);
		Comune comune5994 = new Comune();  // CAVRIGLIA
		comune5994.setCodice(5994);
		comune5994.setNome("CAVRIGLIA");
		listaComuni4.add(comune5994);
		Comune comune5995 = new Comune();  // CHITIGNANO
		comune5995.setCodice(5995);
		comune5995.setNome("CHITIGNANO");
		listaComuni4.add(comune5995);
		Comune comune5996 = new Comune();  // CHIUSI DELLA VERNA
		comune5996.setCodice(5996);
		comune5996.setNome("CHIUSI DELLA VERNA");
		listaComuni4.add(comune5996);

		////// FIRENZE
		Comune comune5929 = new Comune();  // BAGNO A RIPOLI
		comune5929.setCodice(5929);
		comune5929.setNome("BAGNO A RIPOLI");
		listaComuni31.add(comune5929);
		Comune comune5931 = new Comune();  // BARBERINO DI MUGELLO
		comune5931.setCodice(5931);
		comune5931.setNome("BARBERINO DI MUGELLO");
		listaComuni31.add(comune5931);
		Comune comune5930 = new Comune();  // BARBERINO DI VAL D'ELSA
		comune5930.setCodice(5930);
		comune5930.setNome("BARBERINO DI VAL D'ELSA");
		listaComuni31.add(comune5930);
		Comune comune5932 = new Comune();  // BORGO S.LORENZO
		comune5932.setCodice(5932);
		comune5932.setNome("BORGO S.LORENZO");
		listaComuni31.add(comune5932);
		Comune comune5933 = new Comune();  // CALENZANO
		comune5933.setCodice(5933);
		comune5933.setNome("CALENZANO");
		listaComuni31.add(comune5933);
		Comune comune5934 = new Comune();  // CAMPI BISENZIO
		comune5934.setCodice(5934);
		comune5934.setNome("CAMPI BISENZIO");
		listaComuni31.add(comune5934);
		Comune comune5936 = new Comune();  // CAPRAIA E LIMITE
		comune5936.setCodice(5936);
		comune5936.setNome("CAPRAIA E LIMITE");
		listaComuni31.add(comune5936);
		Comune comune5938 = new Comune();  // CASTELFIORENTINO
		comune5938.setCodice(5938);
		comune5938.setNome("CASTELFIORENTINO");
		listaComuni31.add(comune5938);
		Comune comune5939 = new Comune();  // CERRETO GUIDI
		comune5939.setCodice(5939);
		comune5939.setNome("CERRETO GUIDI");
		listaComuni31.add(comune5939);
		Comune comune5940 = new Comune();  // CERTALDO
		comune5940.setCodice(5940);
		comune5940.setNome("CERTALDO");
		listaComuni31.add(comune5940);
		Comune comune5941 = new Comune();  // DICOMANO
		comune5941.setCodice(5941);
		comune5941.setNome("DICOMANO");
		listaComuni31.add(comune5941);
		Comune comune5942 = new Comune();  // EMPOLI
		comune5942.setCodice(5942);
		comune5942.setNome("EMPOLI");
		listaComuni31.add(comune5942);
		Comune comune5943 = new Comune();  // FIESOLE
		comune5943.setCodice(5943);
		comune5943.setNome("FIESOLE");
		listaComuni31.add(comune5943);
		Comune comune5944 = new Comune();  // FIGLINE VALDARNO
		comune5944.setCodice(5944);
		comune5944.setNome("FIGLINE VALDARNO");
		listaComuni31.add(comune5944);
		Comune comune5928 = new Comune();  // FIRENZE
		comune5928.setCodice(5928);
		comune5928.setNome("FIRENZE");
		listaComuni31.add(comune5928);

		////// GROSSETO
		Comune comune6023 = new Comune();  // ARCIDOSSO
		comune6023.setCodice(6023);
		comune6023.setNome("ARCIDOSSO");
		listaComuni38.add(comune6023);
		Comune comune6024 = new Comune();  // CAMPAGNATICO
		comune6024.setCodice(6024);
		comune6024.setNome("CAMPAGNATICO");
		listaComuni38.add(comune6024);
		Comune comune6025 = new Comune();  // CAPALBIO
		comune6025.setCodice(6025);
		comune6025.setNome("CAPALBIO");
		listaComuni38.add(comune6025);
		Comune comune6026 = new Comune();  // CASTEL DEL PIANO
		comune6026.setCodice(6026);
		comune6026.setNome("CASTEL DEL PIANO");
		listaComuni38.add(comune6026);
		Comune comune6027 = new Comune();  // CASTELL'AZZARA
		comune6027.setCodice(6027);
		comune6027.setNome("CASTELL'AZZARA");
		listaComuni38.add(comune6027);
		Comune comune6028 = new Comune();  // CASTIGLIONE DELLA PESCAIA
		comune6028.setCodice(6028);
		comune6028.setNome("CASTIGLIONE DELLA PESCAIA");
		listaComuni38.add(comune6028);
		Comune comune6029 = new Comune();  // CINIGIANO
		comune6029.setCodice(6029);
		comune6029.setNome("CINIGIANO");
		listaComuni38.add(comune6029);
		Comune comune6030 = new Comune();  // CIVITELLA PAGANICO
		comune6030.setCodice(6030);
		comune6030.setNome("CIVITELLA PAGANICO");
		listaComuni38.add(comune6030);
		Comune comune6031 = new Comune();  // FOLLONICA
		comune6031.setCodice(6031);
		comune6031.setNome("FOLLONICA");
		listaComuni38.add(comune6031);
		Comune comune6032 = new Comune();  // GAVORRANO
		comune6032.setCodice(6032);
		comune6032.setNome("GAVORRANO");
		listaComuni38.add(comune6032);
		Comune comune6022 = new Comune();  // GROSSETO
		comune6022.setCodice(6022);
		comune6022.setNome("GROSSETO");
		listaComuni38.add(comune6022);
		Comune comune6033 = new Comune();  // ISOLA DEL GIGLIO
		comune6033.setCodice(6033);
		comune6033.setNome("ISOLA DEL GIGLIO");
		listaComuni38.add(comune6033);
		Comune comune6034 = new Comune();  // MAGLIANO IN TOSCANA
		comune6034.setCodice(6034);
		comune6034.setNome("MAGLIANO IN TOSCANA");
		listaComuni38.add(comune6034);
		Comune comune6035 = new Comune();  // MANCIANO
		comune6035.setCodice(6035);
		comune6035.setNome("MANCIANO");
		listaComuni38.add(comune6035);
		Comune comune6036 = new Comune();  // MASSA MARITTIMA
		comune6036.setCodice(6036);
		comune6036.setNome("MASSA MARITTIMA");
		listaComuni38.add(comune6036);

		////// LIVORNO
		Comune comune6051 = new Comune();  // BIBBONA
		comune6051.setCodice(6051);
		comune6051.setNome("BIBBONA");
		listaComuni46.add(comune6051);
		Comune comune6052 = new Comune();  // CAMPIGLIA MARITTIMA
		comune6052.setCodice(6052);
		comune6052.setNome("CAMPIGLIA MARITTIMA");
		listaComuni46.add(comune6052);
		Comune comune6053 = new Comune();  // CAMPO NELL'ELBA
		comune6053.setCodice(6053);
		comune6053.setNome("CAMPO NELL'ELBA");
		listaComuni46.add(comune6053);
		Comune comune6054 = new Comune();  // CAPOLIVERI
		comune6054.setCodice(6054);
		comune6054.setNome("CAPOLIVERI");
		listaComuni46.add(comune6054);
		Comune comune6055 = new Comune();  // CAPRAIA ISOLA
		comune6055.setCodice(6055);
		comune6055.setNome("CAPRAIA ISOLA");
		listaComuni46.add(comune6055);
		Comune comune6056 = new Comune();  // CASTAGNETO CARDUCCI
		comune6056.setCodice(6056);
		comune6056.setNome("CASTAGNETO CARDUCCI");
		listaComuni46.add(comune6056);
		Comune comune6057 = new Comune();  // CECINA
		comune6057.setCodice(6057);
		comune6057.setNome("CECINA");
		listaComuni46.add(comune6057);
		Comune comune6058 = new Comune();  // COLLESALVETTI
		comune6058.setCodice(6058);
		comune6058.setNome("COLLESALVETTI");
		listaComuni46.add(comune6058);
		Comune comune6050 = new Comune();  // LIVORNO
		comune6050.setCodice(6050);
		comune6050.setNome("LIVORNO");
		listaComuni46.add(comune6050);
		Comune comune6059 = new Comune();  // MARCIANA
		comune6059.setCodice(6059);
		comune6059.setNome("MARCIANA");
		listaComuni46.add(comune6059);
		Comune comune6060 = new Comune();  // MARCIANA MARINA
		comune6060.setCodice(6060);
		comune6060.setNome("MARCIANA MARINA");
		listaComuni46.add(comune6060);
		Comune comune6061 = new Comune();  // PIOMBINO
		comune6061.setCodice(6061);
		comune6061.setNome("PIOMBINO");
		listaComuni46.add(comune6061);
		Comune comune6062 = new Comune();  // PORTO AZZURRO
		comune6062.setCodice(6062);
		comune6062.setNome("PORTO AZZURRO");
		listaComuni46.add(comune6062);
		Comune comune6063 = new Comune();  // PORTOFERRAIO
		comune6063.setCodice(6063);
		comune6063.setNome("PORTOFERRAIO");
		listaComuni46.add(comune6063);
		Comune comune6064 = new Comune();  // RIO MARINA
		comune6064.setCodice(6064);
		comune6064.setNome("RIO MARINA");
		listaComuni46.add(comune6064);

		////// LUCCA
		Comune comune6072 = new Comune();  // ALTOPASCIO
		comune6072.setCodice(6072);
		comune6072.setNome("ALTOPASCIO");
		listaComuni48.add(comune6072);
		Comune comune6073 = new Comune();  // BAGNI DI LUCCA
		comune6073.setCodice(6073);
		comune6073.setNome("BAGNI DI LUCCA");
		listaComuni48.add(comune6073);
		Comune comune6074 = new Comune();  // BARGA
		comune6074.setCodice(6074);
		comune6074.setNome("BARGA");
		listaComuni48.add(comune6074);
		Comune comune6075 = new Comune();  // BORGO A MOZZANO
		comune6075.setCodice(6075);
		comune6075.setNome("BORGO A MOZZANO");
		listaComuni48.add(comune6075);
		Comune comune6076 = new Comune();  // CAMAIORE
		comune6076.setCodice(6076);
		comune6076.setNome("CAMAIORE");
		listaComuni48.add(comune6076);
		Comune comune6077 = new Comune();  // CAMPORGIANO
		comune6077.setCodice(6077);
		comune6077.setNome("CAMPORGIANO");
		listaComuni48.add(comune6077);
		Comune comune6078 = new Comune();  // CAPANNORI
		comune6078.setCodice(6078);
		comune6078.setNome("CAPANNORI");
		listaComuni48.add(comune6078);
		Comune comune6079 = new Comune();  // CAREGGINE
		comune6079.setCodice(6079);
		comune6079.setNome("CAREGGINE");
		listaComuni48.add(comune6079);
		Comune comune6080 = new Comune();  // CASTELNUOVO DI GARFAGNANA
		comune6080.setCodice(6080);
		comune6080.setNome("CASTELNUOVO DI GARFAGNANA");
		listaComuni48.add(comune6080);
		Comune comune6081 = new Comune();  // CASTIGLIONE DI GARFAGNANA
		comune6081.setCodice(6081);
		comune6081.setNome("CASTIGLIONE DI GARFAGNANA");
		listaComuni48.add(comune6081);
		Comune comune6082 = new Comune();  // COREGLIA ANTELMINELLI
		comune6082.setCodice(6082);
		comune6082.setNome("COREGLIA ANTELMINELLI");
		listaComuni48.add(comune6082);
		Comune comune6083 = new Comune();  // FABBRICHE DI VALLICO
		comune6083.setCodice(6083);
		comune6083.setNome("FABBRICHE DI VALLICO");
		listaComuni48.add(comune6083);
		Comune comune6084 = new Comune();  // FORTE DEI MARMI
		comune6084.setCodice(6084);
		comune6084.setNome("FORTE DEI MARMI");
		listaComuni48.add(comune6084);
		Comune comune6085 = new Comune();  // FOSCIANDORA
		comune6085.setCodice(6085);
		comune6085.setNome("FOSCIANDORA");
		listaComuni48.add(comune6085);
		Comune comune6086 = new Comune();  // GALLICANO
		comune6086.setCodice(6086);
		comune6086.setNome("GALLICANO");
		listaComuni48.add(comune6086);

		////// MASSA-CARRARA
		Comune comune6107 = new Comune();  // AULLA
		comune6107.setCodice(6107);
		comune6107.setNome("AULLA");
		listaComuni51.add(comune6107);
		Comune comune6108 = new Comune();  // BAGNONE
		comune6108.setCodice(6108);
		comune6108.setNome("BAGNONE");
		listaComuni51.add(comune6108);
		Comune comune6109 = new Comune();  // CARRARA
		comune6109.setCodice(6109);
		comune6109.setNome("CARRARA");
		listaComuni51.add(comune6109);
		Comune comune6110 = new Comune();  // CASOLA IN LUNIGIANA
		comune6110.setCodice(6110);
		comune6110.setNome("CASOLA IN LUNIGIANA");
		listaComuni51.add(comune6110);
		Comune comune6111 = new Comune();  // COMANO
		comune6111.setCodice(6111);
		comune6111.setNome("COMANO");
		listaComuni51.add(comune6111);
		Comune comune6112 = new Comune();  // FILATTIERA
		comune6112.setCodice(6112);
		comune6112.setNome("FILATTIERA");
		listaComuni51.add(comune6112);
		Comune comune6113 = new Comune();  // FIVIZZANO
		comune6113.setCodice(6113);
		comune6113.setNome("FIVIZZANO");
		listaComuni51.add(comune6113);
		Comune comune6114 = new Comune();  // FOSDINOVO
		comune6114.setCodice(6114);
		comune6114.setNome("FOSDINOVO");
		listaComuni51.add(comune6114);
		Comune comune6115 = new Comune();  // LICCIANA NARDI
		comune6115.setCodice(6115);
		comune6115.setNome("LICCIANA NARDI");
		listaComuni51.add(comune6115);
		Comune comune6106 = new Comune();  // MASSA
		comune6106.setCodice(6106);
		comune6106.setNome("MASSA");
		listaComuni51.add(comune6106);
		Comune comune6116 = new Comune();  // MONTIGNOSO
		comune6116.setCodice(6116);
		comune6116.setNome("MONTIGNOSO");
		listaComuni51.add(comune6116);
		Comune comune6117 = new Comune();  // MULAZZO
		comune6117.setCodice(6117);
		comune6117.setNome("MULAZZO");
		listaComuni51.add(comune6117);
		Comune comune6118 = new Comune();  // PODENZANA
		comune6118.setCodice(6118);
		comune6118.setNome("PODENZANA");
		listaComuni51.add(comune6118);
		Comune comune6119 = new Comune();  // PONTREMOLI
		comune6119.setCodice(6119);
		comune6119.setNome("PONTREMOLI");
		listaComuni51.add(comune6119);
		Comune comune6120 = new Comune();  // TRESANA
		comune6120.setCodice(6120);
		comune6120.setNome("TRESANA");
		listaComuni51.add(comune6120);

		////// PISA
		Comune comune6127 = new Comune();  // BIENTINA
		comune6127.setCodice(6127);
		comune6127.setNome("BIENTINA");
		listaComuni68.add(comune6127);
		Comune comune6128 = new Comune();  // BUTI
		comune6128.setCodice(6128);
		comune6128.setNome("BUTI");
		listaComuni68.add(comune6128);
		Comune comune6129 = new Comune();  // CALCI
		comune6129.setCodice(6129);
		comune6129.setNome("CALCI");
		listaComuni68.add(comune6129);
		Comune comune6130 = new Comune();  // CALCINAIA
		comune6130.setCodice(6130);
		comune6130.setNome("CALCINAIA");
		listaComuni68.add(comune6130);
		Comune comune6131 = new Comune();  // CAPANNOLI
		comune6131.setCodice(6131);
		comune6131.setNome("CAPANNOLI");
		listaComuni68.add(comune6131);
		Comune comune6132 = new Comune();  // CASALE MARITTIMO
		comune6132.setCodice(6132);
		comune6132.setNome("CASALE MARITTIMO");
		listaComuni68.add(comune6132);
		Comune comune6133 = new Comune();  // CASCIANA TERME
		comune6133.setCodice(6133);
		comune6133.setNome("CASCIANA TERME");
		listaComuni68.add(comune6133);
		Comune comune6134 = new Comune();  // CASCINA
		comune6134.setCodice(6134);
		comune6134.setNome("CASCINA");
		listaComuni68.add(comune6134);
		Comune comune6135 = new Comune();  // CASTELFRANCO DI SOTTO
		comune6135.setCodice(6135);
		comune6135.setNome("CASTELFRANCO DI SOTTO");
		listaComuni68.add(comune6135);
		Comune comune6136 = new Comune();  // CASTELLINA MARITTIMA
		comune6136.setCodice(6136);
		comune6136.setNome("CASTELLINA MARITTIMA");
		listaComuni68.add(comune6136);
		Comune comune6137 = new Comune();  // CASTELNUOVO DI VAL DI CECINA
		comune6137.setCodice(6137);
		comune6137.setNome("CASTELNUOVO DI VAL DI CECINA");
		listaComuni68.add(comune6137);
		Comune comune6138 = new Comune();  // CHIANNI
		comune6138.setCodice(6138);
		comune6138.setNome("CHIANNI");
		listaComuni68.add(comune6138);
		Comune comune6139 = new Comune();  // CRESPINA
		comune6139.setCodice(6139);
		comune6139.setNome("CRESPINA");
		listaComuni68.add(comune6139);
		Comune comune6140 = new Comune();  // FAUGLIA
		comune6140.setCodice(6140);
		comune6140.setNome("FAUGLIA");
		listaComuni68.add(comune6140);
		Comune comune6141 = new Comune();  // GUARDISTALLO
		comune6141.setCodice(6141);
		comune6141.setNome("GUARDISTALLO");
		listaComuni68.add(comune6141);

		////// PISTOIA
		Comune comune6176 = new Comune();  // ABETONE
		comune6176.setCodice(6176);
		comune6176.setNome("ABETONE");
		listaComuni69.add(comune6176);
		Comune comune6177 = new Comune();  // AGLIANA
		comune6177.setCodice(6177);
		comune6177.setNome("AGLIANA");
		listaComuni69.add(comune6177);
		Comune comune6178 = new Comune();  // BUGGIANO
		comune6178.setCodice(6178);
		comune6178.setNome("BUGGIANO");
		listaComuni69.add(comune6178);
		Comune comune6196 = new Comune();  // CHIESINA UZZANESE
		comune6196.setCodice(6196);
		comune6196.setNome("CHIESINA UZZANESE");
		listaComuni69.add(comune6196);
		Comune comune6179 = new Comune();  // CUTIGLIANO
		comune6179.setCodice(6179);
		comune6179.setNome("CUTIGLIANO");
		listaComuni69.add(comune6179);
		Comune comune6180 = new Comune();  // LAMPORECCHIO
		comune6180.setCodice(6180);
		comune6180.setNome("LAMPORECCHIO");
		listaComuni69.add(comune6180);
		Comune comune6181 = new Comune();  // LARCIANO
		comune6181.setCodice(6181);
		comune6181.setNome("LARCIANO");
		listaComuni69.add(comune6181);
		Comune comune6182 = new Comune();  // MARLIANA
		comune6182.setCodice(6182);
		comune6182.setNome("MARLIANA");
		listaComuni69.add(comune6182);
		Comune comune6183 = new Comune();  // MASSA E COZZILE
		comune6183.setCodice(6183);
		comune6183.setNome("MASSA E COZZILE");
		listaComuni69.add(comune6183);
		Comune comune6184 = new Comune();  // MONSUMMANO TERME
		comune6184.setCodice(6184);
		comune6184.setNome("MONSUMMANO TERME");
		listaComuni69.add(comune6184);
		Comune comune6185 = new Comune();  // MONTALE
		comune6185.setCodice(6185);
		comune6185.setNome("MONTALE");
		listaComuni69.add(comune6185);
		Comune comune6186 = new Comune();  // MONTECATINI TERME
		comune6186.setCodice(6186);
		comune6186.setNome("MONTECATINI TERME");
		listaComuni69.add(comune6186);
		Comune comune6187 = new Comune();  // PESCIA
		comune6187.setCodice(6187);
		comune6187.setNome("PESCIA");
		listaComuni69.add(comune6187);
		Comune comune6188 = new Comune();  // PIEVE A NIEVOLE
		comune6188.setCodice(6188);
		comune6188.setNome("PIEVE A NIEVOLE");
		listaComuni69.add(comune6188);
		Comune comune6175 = new Comune();  // PISTOIA
		comune6175.setCodice(6175);
		comune6175.setNome("PISTOIA");
		listaComuni69.add(comune6175);

		////// PRATO
		Comune comune6169 = new Comune();  // CANTAGALLO
		comune6169.setCodice(6169);
		comune6169.setNome("CANTAGALLO");
		listaComuni73.add(comune6169);
		Comune comune6170 = new Comune();  // CARMIGNANO
		comune6170.setCodice(6170);
		comune6170.setNome("CARMIGNANO");
		listaComuni73.add(comune6170);
		Comune comune6171 = new Comune();  // MONTEMURLO
		comune6171.setCodice(6171);
		comune6171.setNome("MONTEMURLO");
		listaComuni73.add(comune6171);
		Comune comune6172 = new Comune();  // POGGIO A CAIANO
		comune6172.setCodice(6172);
		comune6172.setNome("POGGIO A CAIANO");
		listaComuni73.add(comune6172);
		Comune comune6168 = new Comune();  // PRATO
		comune6168.setCodice(6168);
		comune6168.setNome("PRATO");
		listaComuni73.add(comune6168);
		Comune comune6173 = new Comune();  // VAIANO
		comune6173.setCodice(6173);
		comune6173.setNome("VAIANO");
		listaComuni73.add(comune6173);
		Comune comune6174 = new Comune();  // VERNIO
		comune6174.setCodice(6174);
		comune6174.setNome("VERNIO");
		listaComuni73.add(comune6174);

		////// SIENA
		Comune comune6202 = new Comune();  // ABBADIA S.SALVATORE
		comune6202.setCodice(6202);
		comune6202.setNome("ABBADIA S.SALVATORE");
		listaComuni85.add(comune6202);
		Comune comune6203 = new Comune();  // ASCIANO
		comune6203.setCodice(6203);
		comune6203.setNome("ASCIANO");
		listaComuni85.add(comune6203);
		Comune comune6204 = new Comune();  // BUONCONVENTO
		comune6204.setCodice(6204);
		comune6204.setNome("BUONCONVENTO");
		listaComuni85.add(comune6204);
		Comune comune6205 = new Comune();  // CASOLE D'ELSA
		comune6205.setCodice(6205);
		comune6205.setNome("CASOLE D'ELSA");
		listaComuni85.add(comune6205);
		Comune comune6206 = new Comune();  // CASTELLINA IN CHIANTI
		comune6206.setCodice(6206);
		comune6206.setNome("CASTELLINA IN CHIANTI");
		listaComuni85.add(comune6206);
		Comune comune6207 = new Comune();  // CASTELNUOVO BERARDENGA
		comune6207.setCodice(6207);
		comune6207.setNome("CASTELNUOVO BERARDENGA");
		listaComuni85.add(comune6207);
		Comune comune6208 = new Comune();  // CASTIGLIONE D'ORCIA
		comune6208.setCodice(6208);
		comune6208.setNome("CASTIGLIONE D'ORCIA");
		listaComuni85.add(comune6208);
		Comune comune6209 = new Comune();  // CETONA
		comune6209.setCodice(6209);
		comune6209.setNome("CETONA");
		listaComuni85.add(comune6209);
		Comune comune6210 = new Comune();  // CHIANCIANO TERME
		comune6210.setCodice(6210);
		comune6210.setNome("CHIANCIANO TERME");
		listaComuni85.add(comune6210);
		Comune comune6211 = new Comune();  // CHIUSDINO
		comune6211.setCodice(6211);
		comune6211.setNome("CHIUSDINO");
		listaComuni85.add(comune6211);
		Comune comune6212 = new Comune();  // CHIUSI
		comune6212.setCodice(6212);
		comune6212.setNome("CHIUSI");
		listaComuni85.add(comune6212);
		Comune comune6213 = new Comune();  // COLLE DI VAL D'ELSA
		comune6213.setCodice(6213);
		comune6213.setNome("COLLE DI VAL D'ELSA");
		listaComuni85.add(comune6213);
		Comune comune6214 = new Comune();  // GAIOLE IN CHIANTI
		comune6214.setCodice(6214);
		comune6214.setNome("GAIOLE IN CHIANTI");
		listaComuni85.add(comune6214);
		Comune comune6215 = new Comune();  // MONTALCINO
		comune6215.setCodice(6215);
		comune6215.setNome("MONTALCINO");
		listaComuni85.add(comune6215);
		Comune comune6216 = new Comune();  // MONTEPULCIANO
		comune6216.setCodice(6216);
		comune6216.setNome("MONTEPULCIANO");
		listaComuni85.add(comune6216);


		/*
		 * UMBRIA
		 */

		////// PERUGIA
		Comune comune6240 = new Comune();  // ASSISI
		comune6240.setCodice(6240);
		comune6240.setNome("ASSISI");
		listaComuni64.add(comune6240);
		Comune comune6241 = new Comune();  // BASTIA UMBRA
		comune6241.setCodice(6241);
		comune6241.setNome("BASTIA UMBRA");
		listaComuni64.add(comune6241);
		Comune comune6242 = new Comune();  // BETTONA
		comune6242.setCodice(6242);
		comune6242.setNome("BETTONA");
		listaComuni64.add(comune6242);
		Comune comune6243 = new Comune();  // BEVAGNA
		comune6243.setCodice(6243);
		comune6243.setNome("BEVAGNA");
		listaComuni64.add(comune6243);
		Comune comune6244 = new Comune();  // CAMPELLO SUL CLITUNNO
		comune6244.setCodice(6244);
		comune6244.setNome("CAMPELLO SUL CLITUNNO");
		listaComuni64.add(comune6244);
		Comune comune6245 = new Comune();  // CANNARA
		comune6245.setCodice(6245);
		comune6245.setNome("CANNARA");
		listaComuni64.add(comune6245);
		Comune comune6246 = new Comune();  // CASCIA
		comune6246.setCodice(6246);
		comune6246.setNome("CASCIA");
		listaComuni64.add(comune6246);
		Comune comune6247 = new Comune();  // CASTEL RITALDI
		comune6247.setCodice(6247);
		comune6247.setNome("CASTEL RITALDI");
		listaComuni64.add(comune6247);
		Comune comune6248 = new Comune();  // CASTIGLIONE DEL LAGO
		comune6248.setCodice(6248);
		comune6248.setNome("CASTIGLIONE DEL LAGO");
		listaComuni64.add(comune6248);
		Comune comune6249 = new Comune();  // CERRETO DI SPOLETO
		comune6249.setCodice(6249);
		comune6249.setNome("CERRETO DI SPOLETO");
		listaComuni64.add(comune6249);
		Comune comune6250 = new Comune();  // CITERNA
		comune6250.setCodice(6250);
		comune6250.setNome("CITERNA");
		listaComuni64.add(comune6250);
		Comune comune6251 = new Comune();  // CITTA' DELLA PIEVE
		comune6251.setCodice(6251);
		comune6251.setNome("CITTA' DELLA PIEVE");
		listaComuni64.add(comune6251);
		Comune comune6252 = new Comune();  // CITTA' DI CASTELLO
		comune6252.setCodice(6252);
		comune6252.setNome("CITTA' DI CASTELLO");
		listaComuni64.add(comune6252);
		Comune comune6253 = new Comune();  // COLLAZZONE
		comune6253.setCodice(6253);
		comune6253.setNome("COLLAZZONE");
		listaComuni64.add(comune6253);
		Comune comune6254 = new Comune();  // CORCIANO
		comune6254.setCodice(6254);
		comune6254.setNome("CORCIANO");
		listaComuni64.add(comune6254);

		////// TERNI
		Comune comune6299 = new Comune();  // ACQUASPARTA
		comune6299.setCodice(6299);
		comune6299.setNome("ACQUASPARTA");
		listaComuni90.add(comune6299);
		Comune comune6300 = new Comune();  // ALLERONA
		comune6300.setCodice(6300);
		comune6300.setNome("ALLERONA");
		listaComuni90.add(comune6300);
		Comune comune6301 = new Comune();  // ALVIANO
		comune6301.setCodice(6301);
		comune6301.setNome("ALVIANO");
		listaComuni90.add(comune6301);
		Comune comune6302 = new Comune();  // AMELIA
		comune6302.setCodice(6302);
		comune6302.setNome("AMELIA");
		listaComuni90.add(comune6302);
		Comune comune6303 = new Comune();  // ARRONE
		comune6303.setCodice(6303);
		comune6303.setNome("ARRONE");
		listaComuni90.add(comune6303);
		Comune comune6304 = new Comune();  // ATTIGLIANO
		comune6304.setCodice(6304);
		comune6304.setNome("ATTIGLIANO");
		listaComuni90.add(comune6304);
		Comune comune6334 = new Comune();  // AVIGLIANO UMBRO
		comune6334.setCodice(6334);
		comune6334.setNome("AVIGLIANO UMBRO");
		listaComuni90.add(comune6334);
		Comune comune6305 = new Comune();  // BASCHI
		comune6305.setCodice(6305);
		comune6305.setNome("BASCHI");
		listaComuni90.add(comune6305);
		Comune comune6306 = new Comune();  // CALVI DELL'UMBRIA
		comune6306.setCodice(6306);
		comune6306.setNome("CALVI DELL'UMBRIA");
		listaComuni90.add(comune6306);
		Comune comune6307 = new Comune();  // CASTEL GIORGIO
		comune6307.setCodice(6307);
		comune6307.setNome("CASTEL GIORGIO");
		listaComuni90.add(comune6307);
		Comune comune6308 = new Comune();  // CASTEL VISCARDO
		comune6308.setCodice(6308);
		comune6308.setNome("CASTEL VISCARDO");
		listaComuni90.add(comune6308);
		Comune comune6309 = new Comune();  // FABRO
		comune6309.setCodice(6309);
		comune6309.setNome("FABRO");
		listaComuni90.add(comune6309);
		Comune comune6310 = new Comune();  // FERENTILLO
		comune6310.setCodice(6310);
		comune6310.setNome("FERENTILLO");
		listaComuni90.add(comune6310);
		Comune comune6311 = new Comune();  // FICULLE
		comune6311.setCodice(6311);
		comune6311.setNome("FICULLE");
		listaComuni90.add(comune6311);
		Comune comune6312 = new Comune();  // GIOVE
		comune6312.setCodice(6312);
		comune6312.setNome("GIOVE");
		listaComuni90.add(comune6312);


		/*
		 * MARCHE
		 */

		////// ANCONA
		Comune comune6337 = new Comune();  // AGUGLIANO
		comune6337.setCodice(6337);
		comune6337.setNome("AGUGLIANO");
		listaComuni3.add(comune6337);
		Comune comune6336 = new Comune();  // ANCONA
		comune6336.setCodice(6336);
		comune6336.setNome("ANCONA");
		listaComuni3.add(comune6336);
		Comune comune6338 = new Comune();  // ARCEVIA
		comune6338.setCodice(6338);
		comune6338.setNome("ARCEVIA");
		listaComuni3.add(comune6338);
		Comune comune6339 = new Comune();  // BARBARA
		comune6339.setCodice(6339);
		comune6339.setNome("BARBARA");
		listaComuni3.add(comune6339);
		Comune comune6340 = new Comune();  // BELVEDERE OSTRENSE
		comune6340.setCodice(6340);
		comune6340.setNome("BELVEDERE OSTRENSE");
		listaComuni3.add(comune6340);
		Comune comune6341 = new Comune();  // CAMERANO
		comune6341.setCodice(6341);
		comune6341.setNome("CAMERANO");
		listaComuni3.add(comune6341);
		Comune comune6342 = new Comune();  // CAMERATA PICENA
		comune6342.setCodice(6342);
		comune6342.setNome("CAMERATA PICENA");
		listaComuni3.add(comune6342);
		Comune comune6343 = new Comune();  // CASTEL COLONNA
		comune6343.setCodice(6343);
		comune6343.setNome("CASTEL COLONNA");
		listaComuni3.add(comune6343);
		Comune comune6344 = new Comune();  // CASTELBELLINO
		comune6344.setCodice(6344);
		comune6344.setNome("CASTELBELLINO");
		listaComuni3.add(comune6344);
		Comune comune6345 = new Comune();  // CASTELFIDARDO
		comune6345.setCodice(6345);
		comune6345.setNome("CASTELFIDARDO");
		listaComuni3.add(comune6345);
		Comune comune6346 = new Comune();  // CASTELLEONE DI SUASA
		comune6346.setCodice(6346);
		comune6346.setNome("CASTELLEONE DI SUASA");
		listaComuni3.add(comune6346);
		Comune comune6347 = new Comune();  // CASTELPLANIO
		comune6347.setCodice(6347);
		comune6347.setNome("CASTELPLANIO");
		listaComuni3.add(comune6347);
		Comune comune6348 = new Comune();  // CERRETO D'ESI
		comune6348.setCodice(6348);
		comune6348.setNome("CERRETO D'ESI");
		listaComuni3.add(comune6348);
		Comune comune6349 = new Comune();  // CHIARAVALLE
		comune6349.setCodice(6349);
		comune6349.setNome("CHIARAVALLE");
		listaComuni3.add(comune6349);
		Comune comune6350 = new Comune();  // CORINALDO
		comune6350.setCodice(6350);
		comune6350.setNome("CORINALDO");
		listaComuni3.add(comune6350);

		////// ASCOLI PICENO
		Comune comune6391 = new Comune();  // ACQUASANTA TERME
		comune6391.setCodice(6391);
		comune6391.setNome("ACQUASANTA TERME");
		listaComuni5.add(comune6391);
		Comune comune6392 = new Comune();  // ACQUAVIVA PICENA
		comune6392.setCodice(6392);
		comune6392.setNome("ACQUAVIVA PICENA");
		listaComuni5.add(comune6392);
		Comune comune6393 = new Comune();  // ALTIDONA
		comune6393.setCodice(6393);
		comune6393.setNome("ALTIDONA");
		listaComuni5.add(comune6393);
		Comune comune6394 = new Comune();  // AMANDOLA
		comune6394.setCodice(6394);
		comune6394.setNome("AMANDOLA");
		listaComuni5.add(comune6394);
		Comune comune6395 = new Comune();  // APPIGNANO DEL TRONTO
		comune6395.setCodice(6395);
		comune6395.setNome("APPIGNANO DEL TRONTO");
		listaComuni5.add(comune6395);
		Comune comune6396 = new Comune();  // ARQUATA DEL TRONTO
		comune6396.setCodice(6396);
		comune6396.setNome("ARQUATA DEL TRONTO");
		listaComuni5.add(comune6396);
		Comune comune6390 = new Comune();  // ASCOLI PICENO
		comune6390.setCodice(6390);
		comune6390.setNome("ASCOLI PICENO");
		listaComuni5.add(comune6390);
		Comune comune6397 = new Comune();  // BELMONTE PICENO
		comune6397.setCodice(6397);
		comune6397.setNome("BELMONTE PICENO");
		listaComuni5.add(comune6397);
		Comune comune6398 = new Comune();  // CAMPOFILONE
		comune6398.setCodice(6398);
		comune6398.setNome("CAMPOFILONE");
		listaComuni5.add(comune6398);
		Comune comune6399 = new Comune();  // CARASSAI
		comune6399.setCodice(6399);
		comune6399.setNome("CARASSAI");
		listaComuni5.add(comune6399);
		Comune comune6400 = new Comune();  // CASTEL DI LAMA
		comune6400.setCodice(6400);
		comune6400.setNome("CASTEL DI LAMA");
		listaComuni5.add(comune6400);
		Comune comune6401 = new Comune();  // CASTIGNANO
		comune6401.setCodice(6401);
		comune6401.setNome("CASTIGNANO");
		listaComuni5.add(comune6401);
		Comune comune6402 = new Comune();  // CASTORANO
		comune6402.setCodice(6402);
		comune6402.setNome("CASTORANO");
		listaComuni5.add(comune6402);
		Comune comune6403 = new Comune();  // COLLI DEL TRONTO
		comune6403.setCodice(6403);
		comune6403.setNome("COLLI DEL TRONTO");
		listaComuni5.add(comune6403);
		Comune comune6404 = new Comune();  // COMUNANZA
		comune6404.setCodice(6404);
		comune6404.setNome("COMUNANZA");
		listaComuni5.add(comune6404);

		////// MACERATA
		Comune comune6464 = new Comune();  // ACQUACANINA
		comune6464.setCodice(6464);
		comune6464.setNome("ACQUACANINA");
		listaComuni49.add(comune6464);
		Comune comune6465 = new Comune();  // APIRO
		comune6465.setCodice(6465);
		comune6465.setNome("APIRO");
		listaComuni49.add(comune6465);
		Comune comune6466 = new Comune();  // APPIGNANO
		comune6466.setCodice(6466);
		comune6466.setNome("APPIGNANO");
		listaComuni49.add(comune6466);
		Comune comune6467 = new Comune();  // BELFORTE DEL CHIENTI
		comune6467.setCodice(6467);
		comune6467.setNome("BELFORTE DEL CHIENTI");
		listaComuni49.add(comune6467);
		Comune comune6468 = new Comune();  // BOLOGNOLA
		comune6468.setCodice(6468);
		comune6468.setNome("BOLOGNOLA");
		listaComuni49.add(comune6468);
		Comune comune6469 = new Comune();  // CALDAROLA
		comune6469.setCodice(6469);
		comune6469.setNome("CALDAROLA");
		listaComuni49.add(comune6469);
		Comune comune6470 = new Comune();  // CAMERINO
		comune6470.setCodice(6470);
		comune6470.setNome("CAMERINO");
		listaComuni49.add(comune6470);
		Comune comune6471 = new Comune();  // CAMPOROTONDO DI FIASTRONE
		comune6471.setCodice(6471);
		comune6471.setNome("CAMPOROTONDO DI FIASTRONE");
		listaComuni49.add(comune6471);
		Comune comune6472 = new Comune();  // CASTELRAIMONDO
		comune6472.setCodice(6472);
		comune6472.setNome("CASTELRAIMONDO");
		listaComuni49.add(comune6472);
		Comune comune6473 = new Comune();  // CASTELSANTANGELO
		comune6473.setCodice(6473);
		comune6473.setNome("CASTELSANTANGELO");
		listaComuni49.add(comune6473);
		Comune comune6474 = new Comune();  // CESSAPALOMBO
		comune6474.setCodice(6474);
		comune6474.setNome("CESSAPALOMBO");
		listaComuni49.add(comune6474);
		Comune comune6475 = new Comune();  // CINGOLI
		comune6475.setCodice(6475);
		comune6475.setNome("CINGOLI");
		listaComuni49.add(comune6475);
		Comune comune6476 = new Comune();  // CIVITANOVA MARCHE
		comune6476.setCodice(6476);
		comune6476.setNome("CIVITANOVA MARCHE");
		listaComuni49.add(comune6476);
		Comune comune6477 = new Comune();  // COLMURANO
		comune6477.setCodice(6477);
		comune6477.setNome("COLMURANO");
		listaComuni49.add(comune6477);
		Comune comune6478 = new Comune();  // CORRIDONIA
		comune6478.setCodice(6478);
		comune6478.setNome("CORRIDONIA");
		listaComuni49.add(comune6478);

		////// PESARO E URBINO
		Comune comune6522 = new Comune();  // ACQUALAGNA
		comune6522.setCodice(6522);
		comune6522.setNome("ACQUALAGNA");
		listaComuni65.add(comune6522);
		Comune comune6523 = new Comune();  // APECCHIO
		comune6523.setCodice(6523);
		comune6523.setNome("APECCHIO");
		listaComuni65.add(comune6523);
		Comune comune6524 = new Comune();  // AUDITORE
		comune6524.setCodice(6524);
		comune6524.setNome("AUDITORE");
		listaComuni65.add(comune6524);
		Comune comune6525 = new Comune();  // BARCHI
		comune6525.setCodice(6525);
		comune6525.setNome("BARCHI");
		listaComuni65.add(comune6525);
		Comune comune6526 = new Comune();  // BELFORTE ALL'ISAURO
		comune6526.setCodice(6526);
		comune6526.setNome("BELFORTE ALL'ISAURO");
		listaComuni65.add(comune6526);
		Comune comune6527 = new Comune();  // BORGO PACE
		comune6527.setCodice(6527);
		comune6527.setNome("BORGO PACE");
		listaComuni65.add(comune6527);
		Comune comune6528 = new Comune();  // CAGLI
		comune6528.setCodice(6528);
		comune6528.setNome("CAGLI");
		listaComuni65.add(comune6528);
		Comune comune6529 = new Comune();  // CANTIANO
		comune6529.setCodice(6529);
		comune6529.setNome("CANTIANO");
		listaComuni65.add(comune6529);
		Comune comune6530 = new Comune();  // CARPEGNA
		comune6530.setCodice(6530);
		comune6530.setNome("CARPEGNA");
		listaComuni65.add(comune6530);
		Comune comune6531 = new Comune();  // CARTOCETO
		comune6531.setCodice(6531);
		comune6531.setNome("CARTOCETO");
		listaComuni65.add(comune6531);
		Comune comune6532 = new Comune();  // CASTELDELCI
		comune6532.setCodice(6532);
		comune6532.setNome("CASTELDELCI");
		listaComuni65.add(comune6532);
		Comune comune6533 = new Comune();  // COLBORDOLO
		comune6533.setCodice(6533);
		comune6533.setNome("COLBORDOLO");
		listaComuni65.add(comune6533);
		Comune comune6534 = new Comune();  // FANO
		comune6534.setCodice(6534);
		comune6534.setNome("FANO");
		listaComuni65.add(comune6534);
		Comune comune6535 = new Comune();  // FERMIGNANO
		comune6535.setCodice(6535);
		comune6535.setNome("FERMIGNANO");
		listaComuni65.add(comune6535);
		Comune comune6536 = new Comune();  // FOSSOMBRONE
		comune6536.setCodice(6536);
		comune6536.setNome("FOSSOMBRONE");
		listaComuni65.add(comune6536);


		/*
		 * LAZIO
		 */

		////// FROSINONE
		Comune comune6723 = new Comune();  // ACQUAFONDATA
		comune6723.setCodice(6723);
		comune6723.setNome("ACQUAFONDATA");
		listaComuni35.add(comune6723);
		Comune comune6724 = new Comune();  // ACUTO
		comune6724.setCodice(6724);
		comune6724.setNome("ACUTO");
		listaComuni35.add(comune6724);
		Comune comune6725 = new Comune();  // ALATRI
		comune6725.setCodice(6725);
		comune6725.setNome("ALATRI");
		listaComuni35.add(comune6725);
		Comune comune6726 = new Comune();  // ALVITO
		comune6726.setCodice(6726);
		comune6726.setNome("ALVITO");
		listaComuni35.add(comune6726);
		Comune comune6727 = new Comune();  // AMASENO
		comune6727.setCodice(6727);
		comune6727.setNome("AMASENO");
		listaComuni35.add(comune6727);
		Comune comune6728 = new Comune();  // ANAGNI
		comune6728.setCodice(6728);
		comune6728.setNome("ANAGNI");
		listaComuni35.add(comune6728);
		Comune comune6729 = new Comune();  // AQUINO
		comune6729.setCodice(6729);
		comune6729.setNome("AQUINO");
		listaComuni35.add(comune6729);
		Comune comune6730 = new Comune();  // ARCE
		comune6730.setCodice(6730);
		comune6730.setNome("ARCE");
		listaComuni35.add(comune6730);
		Comune comune6731 = new Comune();  // ARNARA
		comune6731.setCodice(6731);
		comune6731.setNome("ARNARA");
		listaComuni35.add(comune6731);
		Comune comune6732 = new Comune();  // ARPINO
		comune6732.setCodice(6732);
		comune6732.setNome("ARPINO");
		listaComuni35.add(comune6732);
		Comune comune6733 = new Comune();  // ATINA
		comune6733.setCodice(6733);
		comune6733.setNome("ATINA");
		listaComuni35.add(comune6733);
		Comune comune6734 = new Comune();  // AUSONIA
		comune6734.setCodice(6734);
		comune6734.setNome("AUSONIA");
		listaComuni35.add(comune6734);
		Comune comune6735 = new Comune();  // BELMONTE CASTELLO
		comune6735.setCodice(6735);
		comune6735.setNome("BELMONTE CASTELLO");
		listaComuni35.add(comune6735);
		Comune comune6736 = new Comune();  // BOVILLE ERNICA
		comune6736.setCodice(6736);
		comune6736.setNome("BOVILLE ERNICA");
		listaComuni35.add(comune6736);
		Comune comune6737 = new Comune();  // BROCCOSTELLA
		comune6737.setCodice(6737);
		comune6737.setNome("BROCCOSTELLA");
		listaComuni35.add(comune6737);

		////// LATINA
		Comune comune6814 = new Comune();  // APRILIA
		comune6814.setCodice(6814);
		comune6814.setNome("APRILIA");
		listaComuni43.add(comune6814);
		Comune comune6815 = new Comune();  // BASSIANO
		comune6815.setCodice(6815);
		comune6815.setNome("BASSIANO");
		listaComuni43.add(comune6815);
		Comune comune6816 = new Comune();  // CAMPODIMELE
		comune6816.setCodice(6816);
		comune6816.setNome("CAMPODIMELE");
		listaComuni43.add(comune6816);
		Comune comune6817 = new Comune();  // CASTELFORTE
		comune6817.setCodice(6817);
		comune6817.setNome("CASTELFORTE");
		listaComuni43.add(comune6817);
		Comune comune6818 = new Comune();  // CISTERNA DI LATINA
		comune6818.setCodice(6818);
		comune6818.setNome("CISTERNA DI LATINA");
		listaComuni43.add(comune6818);
		Comune comune6819 = new Comune();  // CORI
		comune6819.setCodice(6819);
		comune6819.setNome("CORI");
		listaComuni43.add(comune6819);
		Comune comune6820 = new Comune();  // FONDI
		comune6820.setCodice(6820);
		comune6820.setNome("FONDI");
		listaComuni43.add(comune6820);
		Comune comune6821 = new Comune();  // FORMIA
		comune6821.setCodice(6821);
		comune6821.setNome("FORMIA");
		listaComuni43.add(comune6821);
		Comune comune6822 = new Comune();  // GAETA
		comune6822.setCodice(6822);
		comune6822.setNome("GAETA");
		listaComuni43.add(comune6822);
		Comune comune6823 = new Comune();  // ITRI
		comune6823.setCodice(6823);
		comune6823.setNome("ITRI");
		listaComuni43.add(comune6823);
		Comune comune6813 = new Comune();  // LATINA
		comune6813.setCodice(6813);
		comune6813.setNome("LATINA");
		listaComuni43.add(comune6813);
		Comune comune6824 = new Comune();  // LENOLA
		comune6824.setCodice(6824);
		comune6824.setNome("LENOLA");
		listaComuni43.add(comune6824);
		Comune comune6825 = new Comune();  // MAENZA
		comune6825.setCodice(6825);
		comune6825.setNome("MAENZA");
		listaComuni43.add(comune6825);
		Comune comune6826 = new Comune();  // MINTURNO
		comune6826.setCodice(6826);
		comune6826.setNome("MINTURNO");
		listaComuni43.add(comune6826);
		Comune comune6827 = new Comune();  // MONTE S.BIAGIO
		comune6827.setCodice(6827);
		comune6827.setNome("MONTE S.BIAGIO");
		listaComuni43.add(comune6827);

		////// RIETI
		Comune comune6850 = new Comune();  // ACCUMOLI
		comune6850.setCodice(6850);
		comune6850.setNome("ACCUMOLI");
		listaComuni78.add(comune6850);
		Comune comune6851 = new Comune();  // AMATRICE
		comune6851.setCodice(6851);
		comune6851.setNome("AMATRICE");
		listaComuni78.add(comune6851);
		Comune comune6852 = new Comune();  // ANTRODOCO
		comune6852.setCodice(6852);
		comune6852.setNome("ANTRODOCO");
		listaComuni78.add(comune6852);
		Comune comune6853 = new Comune();  // ASCREA
		comune6853.setCodice(6853);
		comune6853.setNome("ASCREA");
		listaComuni78.add(comune6853);
		Comune comune6854 = new Comune();  // BELMONTE IN SABINA
		comune6854.setCodice(6854);
		comune6854.setNome("BELMONTE IN SABINA");
		listaComuni78.add(comune6854);
		Comune comune6855 = new Comune();  // BORBONA
		comune6855.setCodice(6855);
		comune6855.setNome("BORBONA");
		listaComuni78.add(comune6855);
		Comune comune6856 = new Comune();  // BORGO VELINO
		comune6856.setCodice(6856);
		comune6856.setNome("BORGO VELINO");
		listaComuni78.add(comune6856);
		Comune comune6857 = new Comune();  // BORGOROSE
		comune6857.setCodice(6857);
		comune6857.setNome("BORGOROSE");
		listaComuni78.add(comune6857);
		Comune comune6858 = new Comune();  // CANTALICE
		comune6858.setCodice(6858);
		comune6858.setNome("CANTALICE");
		listaComuni78.add(comune6858);
		Comune comune6859 = new Comune();  // CANTALUPO IN SABINA
		comune6859.setCodice(6859);
		comune6859.setNome("CANTALUPO IN SABINA");
		listaComuni78.add(comune6859);
		Comune comune6860 = new Comune();  // CASAPROTA
		comune6860.setCodice(6860);
		comune6860.setNome("CASAPROTA");
		listaComuni78.add(comune6860);
		Comune comune6861 = new Comune();  // CASPERIA
		comune6861.setCodice(6861);
		comune6861.setNome("CASPERIA");
		listaComuni78.add(comune6861);
		Comune comune6862 = new Comune();  // CASTEL DI TORA
		comune6862.setCodice(6862);
		comune6862.setNome("CASTEL DI TORA");
		listaComuni78.add(comune6862);
		Comune comune6863 = new Comune();  // CASTEL S.ANGELO
		comune6863.setCodice(6863);
		comune6863.setNome("CASTEL S.ANGELO");
		listaComuni78.add(comune6863);
		Comune comune6864 = new Comune();  // CASTELNUOVO DI FARFA
		comune6864.setCodice(6864);
		comune6864.setNome("CASTELNUOVO DI FARFA");
		listaComuni78.add(comune6864);

		////// ROMA
		Comune comune6598 = new Comune();  // AFFILE
		comune6598.setCodice(6598);
		comune6598.setNome("AFFILE");
		listaComuni80.add(comune6598);
		Comune comune6599 = new Comune();  // AGOSTA
		comune6599.setCodice(6599);
		comune6599.setNome("AGOSTA");
		listaComuni80.add(comune6599);
		Comune comune6600 = new Comune();  // ALBANO LAZIALE
		comune6600.setCodice(6600);
		comune6600.setNome("ALBANO LAZIALE");
		listaComuni80.add(comune6600);
		Comune comune6601 = new Comune();  // ALLUMIERE
		comune6601.setCodice(6601);
		comune6601.setNome("ALLUMIERE");
		listaComuni80.add(comune6601);
		Comune comune6602 = new Comune();  // ANGUILLARA SABAZIA
		comune6602.setCodice(6602);
		comune6602.setNome("ANGUILLARA SABAZIA");
		listaComuni80.add(comune6602);
		Comune comune6603 = new Comune();  // ANTICOLI CORRADO
		comune6603.setCodice(6603);
		comune6603.setNome("ANTICOLI CORRADO");
		listaComuni80.add(comune6603);
		Comune comune6604 = new Comune();  // ANZIO
		comune6604.setCodice(6604);
		comune6604.setNome("ANZIO");
		listaComuni80.add(comune6604);
		Comune comune6605 = new Comune();  // ARCINAZZO ROMANO
		comune6605.setCodice(6605);
		comune6605.setNome("ARCINAZZO ROMANO");
		listaComuni80.add(comune6605);
		Comune comune6712 = new Comune();  // ARDEA
		comune6712.setCodice(6712);
		comune6712.setNome("ARDEA");
		listaComuni80.add(comune6712);
		Comune comune6606 = new Comune();  // ARICCIA
		comune6606.setCodice(6606);
		comune6606.setNome("ARICCIA");
		listaComuni80.add(comune6606);
		Comune comune6607 = new Comune();  // ARSOLI
		comune6607.setCodice(6607);
		comune6607.setNome("ARSOLI");
		listaComuni80.add(comune6607);
		Comune comune6608 = new Comune();  // ARTENA
		comune6608.setCodice(6608);
		comune6608.setNome("ARTENA");
		listaComuni80.add(comune6608);
		Comune comune6609 = new Comune();  // BELLEGRA
		comune6609.setCodice(6609);
		comune6609.setNome("BELLEGRA");
		listaComuni80.add(comune6609);
		Comune comune6718 = new Comune();  // BOVILLE
		comune6718.setCodice(6718);
		comune6718.setNome("BOVILLE");
		listaComuni80.add(comune6718);
		Comune comune6610 = new Comune();  // BRACCIANO
		comune6610.setCodice(6610);
		comune6610.setNome("BRACCIANO");
		listaComuni80.add(comune6610);

		////// VITERBO
		Comune comune6927 = new Comune();  // ACQUAPENDENTE
		comune6927.setCodice(6927);
		comune6927.setNome("ACQUAPENDENTE");
		listaComuni105.add(comune6927);
		Comune comune6928 = new Comune();  // ARLENA DI CASTRO
		comune6928.setCodice(6928);
		comune6928.setNome("ARLENA DI CASTRO");
		listaComuni105.add(comune6928);
		Comune comune6929 = new Comune();  // BAGNOREGIO
		comune6929.setCodice(6929);
		comune6929.setNome("BAGNOREGIO");
		listaComuni105.add(comune6929);
		Comune comune6930 = new Comune();  // BARBARANO ROMANO
		comune6930.setCodice(6930);
		comune6930.setNome("BARBARANO ROMANO");
		listaComuni105.add(comune6930);
		Comune comune6931 = new Comune();  // BASSANO IN TEVERINA
		comune6931.setCodice(6931);
		comune6931.setNome("BASSANO IN TEVERINA");
		listaComuni105.add(comune6931);
		Comune comune6932 = new Comune();  // BASSANO ROMANO
		comune6932.setCodice(6932);
		comune6932.setNome("BASSANO ROMANO");
		listaComuni105.add(comune6932);
		Comune comune6933 = new Comune();  // BLERA
		comune6933.setCodice(6933);
		comune6933.setNome("BLERA");
		listaComuni105.add(comune6933);
		Comune comune6934 = new Comune();  // BOLSENA
		comune6934.setCodice(6934);
		comune6934.setNome("BOLSENA");
		listaComuni105.add(comune6934);
		Comune comune6935 = new Comune();  // BOMARZO
		comune6935.setCodice(6935);
		comune6935.setNome("BOMARZO");
		listaComuni105.add(comune6935);
		Comune comune6936 = new Comune();  // CALCATA
		comune6936.setCodice(6936);
		comune6936.setNome("CALCATA");
		listaComuni105.add(comune6936);
		Comune comune6937 = new Comune();  // CANEPINA
		comune6937.setCodice(6937);
		comune6937.setNome("CANEPINA");
		listaComuni105.add(comune6937);
		Comune comune6938 = new Comune();  // CANINO
		comune6938.setCodice(6938);
		comune6938.setNome("CANINO");
		listaComuni105.add(comune6938);
		Comune comune6939 = new Comune();  // CAPODIMONTE
		comune6939.setCodice(6939);
		comune6939.setNome("CAPODIMONTE");
		listaComuni105.add(comune6939);
		Comune comune6940 = new Comune();  // CAPRANICA
		comune6940.setCodice(6940);
		comune6940.setNome("CAPRANICA");
		listaComuni105.add(comune6940);
		Comune comune6941 = new Comune();  // CAPRAROLA
		comune6941.setCodice(6941);
		comune6941.setNome("CAPRAROLA");
		listaComuni105.add(comune6941);


		/*
		 * ABRUZZO
		 */

		////// CHIETI
		Comune comune7111 = new Comune();  // ALTINO
		comune7111.setCodice(7111);
		comune7111.setNome("ALTINO");
		listaComuni23.add(comune7111);
		Comune comune7112 = new Comune();  // ARCHI
		comune7112.setCodice(7112);
		comune7112.setNome("ARCHI");
		listaComuni23.add(comune7112);
		Comune comune7113 = new Comune();  // ARI
		comune7113.setCodice(7113);
		comune7113.setNome("ARI");
		listaComuni23.add(comune7113);
		Comune comune7114 = new Comune();  // ARIELLI
		comune7114.setCodice(7114);
		comune7114.setNome("ARIELLI");
		listaComuni23.add(comune7114);
		Comune comune7115 = new Comune();  // ATESSA
		comune7115.setCodice(7115);
		comune7115.setNome("ATESSA");
		listaComuni23.add(comune7115);
		Comune comune7116 = new Comune();  // BOMBA
		comune7116.setCodice(7116);
		comune7116.setNome("BOMBA");
		listaComuni23.add(comune7116);
		Comune comune7117 = new Comune();  // BORRELLO
		comune7117.setCodice(7117);
		comune7117.setNome("BORRELLO");
		listaComuni23.add(comune7117);
		Comune comune7118 = new Comune();  // BUCCHIANICO
		comune7118.setCodice(7118);
		comune7118.setNome("BUCCHIANICO");
		listaComuni23.add(comune7118);
		Comune comune7119 = new Comune();  // BUONANOTTE
		comune7119.setCodice(7119);
		comune7119.setNome("BUONANOTTE");
		listaComuni23.add(comune7119);
		Comune comune7120 = new Comune();  // CANOSA SANNITA
		comune7120.setCodice(7120);
		comune7120.setNome("CANOSA SANNITA");
		listaComuni23.add(comune7120);
		Comune comune7121 = new Comune();  // CARPINETO SINELLO
		comune7121.setCodice(7121);
		comune7121.setNome("CARPINETO SINELLO");
		listaComuni23.add(comune7121);
		Comune comune7122 = new Comune();  // CARUNCHIO
		comune7122.setCodice(7122);
		comune7122.setNome("CARUNCHIO");
		listaComuni23.add(comune7122);
		Comune comune7123 = new Comune();  // CASACANDITELLA
		comune7123.setCodice(7123);
		comune7123.setNome("CASACANDITELLA");
		listaComuni23.add(comune7123);
		Comune comune7124 = new Comune();  // CASALANGUIDA
		comune7124.setCodice(7124);
		comune7124.setNome("CASALANGUIDA");
		listaComuni23.add(comune7124);
		Comune comune7125 = new Comune();  // CASALBORDINO
		comune7125.setCodice(7125);
		comune7125.setNome("CASALBORDINO");
		listaComuni23.add(comune7125);

		////// L'AQUILA
		Comune comune6993 = new Comune();  // ACCIANO
		comune6993.setCodice(6993);
		comune6993.setNome("ACCIANO");
		listaComuni42.add(comune6993);
		Comune comune6994 = new Comune();  // AIELLI
		comune6994.setCodice(6994);
		comune6994.setNome("AIELLI");
		listaComuni42.add(comune6994);
		Comune comune6995 = new Comune();  // ALFEDENA
		comune6995.setCodice(6995);
		comune6995.setNome("ALFEDENA");
		listaComuni42.add(comune6995);
		Comune comune6996 = new Comune();  // ANVERSA DEGLI ABRUZZI
		comune6996.setCodice(6996);
		comune6996.setNome("ANVERSA DEGLI ABRUZZI");
		listaComuni42.add(comune6996);
		Comune comune6997 = new Comune();  // ATELETA
		comune6997.setCodice(6997);
		comune6997.setNome("ATELETA");
		listaComuni42.add(comune6997);
		Comune comune6998 = new Comune();  // AVEZZANO
		comune6998.setCodice(6998);
		comune6998.setNome("AVEZZANO");
		listaComuni42.add(comune6998);
		Comune comune6999 = new Comune();  // BALSORANO
		comune6999.setCodice(6999);
		comune6999.setNome("BALSORANO");
		listaComuni42.add(comune6999);
		Comune comune7000 = new Comune();  // BARETE
		comune7000.setCodice(7000);
		comune7000.setNome("BARETE");
		listaComuni42.add(comune7000);
		Comune comune7001 = new Comune();  // BARISCIANO
		comune7001.setCodice(7001);
		comune7001.setNome("BARISCIANO");
		listaComuni42.add(comune7001);
		Comune comune7002 = new Comune();  // BARREA
		comune7002.setCodice(7002);
		comune7002.setNome("BARREA");
		listaComuni42.add(comune7002);
		Comune comune7003 = new Comune();  // BISEGNA
		comune7003.setCodice(7003);
		comune7003.setNome("BISEGNA");
		listaComuni42.add(comune7003);
		Comune comune7004 = new Comune();  // BUGNARA
		comune7004.setCodice(7004);
		comune7004.setNome("BUGNARA");
		listaComuni42.add(comune7004);
		Comune comune7005 = new Comune();  // CAGNANO AMITERNO
		comune7005.setCodice(7005);
		comune7005.setNome("CAGNANO AMITERNO");
		listaComuni42.add(comune7005);
		Comune comune7006 = new Comune();  // CALASCIO
		comune7006.setCodice(7006);
		comune7006.setNome("CALASCIO");
		listaComuni42.add(comune7006);
		Comune comune7007 = new Comune();  // CAMPO DI GIOVE
		comune7007.setCodice(7007);
		comune7007.setNome("CAMPO DI GIOVE");
		listaComuni42.add(comune7007);

		////// PESCARA
		Comune comune7218 = new Comune();  // ABBATEGGIO
		comune7218.setCodice(7218);
		comune7218.setNome("ABBATEGGIO");
		listaComuni66.add(comune7218);
		Comune comune7219 = new Comune();  // ALANNO
		comune7219.setCodice(7219);
		comune7219.setNome("ALANNO");
		listaComuni66.add(comune7219);
		Comune comune7220 = new Comune();  // BOLOGNANO
		comune7220.setCodice(7220);
		comune7220.setNome("BOLOGNANO");
		listaComuni66.add(comune7220);
		Comune comune7221 = new Comune();  // BRITTOLI
		comune7221.setCodice(7221);
		comune7221.setNome("BRITTOLI");
		listaComuni66.add(comune7221);
		Comune comune7222 = new Comune();  // BUSSI SUL TIRINO
		comune7222.setCodice(7222);
		comune7222.setNome("BUSSI SUL TIRINO");
		listaComuni66.add(comune7222);
		Comune comune7223 = new Comune();  // CAPPELLE SUL TAVO
		comune7223.setCodice(7223);
		comune7223.setNome("CAPPELLE SUL TAVO");
		listaComuni66.add(comune7223);
		Comune comune7224 = new Comune();  // CARAMANICO TERME
		comune7224.setCodice(7224);
		comune7224.setNome("CARAMANICO TERME");
		listaComuni66.add(comune7224);
		Comune comune7225 = new Comune();  // CARPINETO DELLA NORA
		comune7225.setCodice(7225);
		comune7225.setNome("CARPINETO DELLA NORA");
		listaComuni66.add(comune7225);
		Comune comune7226 = new Comune();  // CASTIGLIONE A CASAURIA
		comune7226.setCodice(7226);
		comune7226.setNome("CASTIGLIONE A CASAURIA");
		listaComuni66.add(comune7226);
		Comune comune7227 = new Comune();  // CATIGNANO
		comune7227.setCodice(7227);
		comune7227.setNome("CATIGNANO");
		listaComuni66.add(comune7227);
		Comune comune7228 = new Comune();  // CEPAGATTI
		comune7228.setCodice(7228);
		comune7228.setNome("CEPAGATTI");
		listaComuni66.add(comune7228);
		Comune comune7229 = new Comune();  // CITTA' S.ANGELO
		comune7229.setCodice(7229);
		comune7229.setNome("CITTA' S.ANGELO");
		listaComuni66.add(comune7229);
		Comune comune7230 = new Comune();  // CIVITAQUANA
		comune7230.setCodice(7230);
		comune7230.setNome("CIVITAQUANA");
		listaComuni66.add(comune7230);
		Comune comune7231 = new Comune();  // CIVITELLA CASANOVA
		comune7231.setCodice(7231);
		comune7231.setNome("CIVITELLA CASANOVA");
		listaComuni66.add(comune7231);
		Comune comune7232 = new Comune();  // COLLECORVINO
		comune7232.setCodice(7232);
		comune7232.setNome("COLLECORVINO");
		listaComuni66.add(comune7232);

		////// TERAMO
		Comune comune7266 = new Comune();  // ALBA ADRIATICA
		comune7266.setCodice(7266);
		comune7266.setNome("ALBA ADRIATICA");
		listaComuni89.add(comune7266);
		Comune comune7267 = new Comune();  // ANCARANO
		comune7267.setCodice(7267);
		comune7267.setNome("ANCARANO");
		listaComuni89.add(comune7267);
		Comune comune7268 = new Comune();  // ARSITA
		comune7268.setCodice(7268);
		comune7268.setNome("ARSITA");
		listaComuni89.add(comune7268);
		Comune comune7269 = new Comune();  // ATRI
		comune7269.setCodice(7269);
		comune7269.setNome("ATRI");
		listaComuni89.add(comune7269);
		Comune comune7270 = new Comune();  // BASCIANO
		comune7270.setCodice(7270);
		comune7270.setNome("BASCIANO");
		listaComuni89.add(comune7270);
		Comune comune7271 = new Comune();  // BELLANTE
		comune7271.setCodice(7271);
		comune7271.setNome("BELLANTE");
		listaComuni89.add(comune7271);
		Comune comune7272 = new Comune();  // BISENTI
		comune7272.setCodice(7272);
		comune7272.setNome("BISENTI");
		listaComuni89.add(comune7272);
		Comune comune7273 = new Comune();  // CAMPLI
		comune7273.setCodice(7273);
		comune7273.setNome("CAMPLI");
		listaComuni89.add(comune7273);
		Comune comune7274 = new Comune();  // CANZANO
		comune7274.setCodice(7274);
		comune7274.setNome("CANZANO");
		listaComuni89.add(comune7274);
		Comune comune7275 = new Comune();  // CASTEL CASTAGNA
		comune7275.setCodice(7275);
		comune7275.setNome("CASTEL CASTAGNA");
		listaComuni89.add(comune7275);
		Comune comune7276 = new Comune();  // CASTELLALTO
		comune7276.setCodice(7276);
		comune7276.setNome("CASTELLALTO");
		listaComuni89.add(comune7276);
		Comune comune7277 = new Comune();  // CASTELLI
		comune7277.setCodice(7277);
		comune7277.setNome("CASTELLI");
		listaComuni89.add(comune7277);
		Comune comune7278 = new Comune();  // CASTIGLIONE MESSER RAIMONDO
		comune7278.setCodice(7278);
		comune7278.setNome("CASTIGLIONE MESSER RAIMONDO");
		listaComuni89.add(comune7278);
		Comune comune7279 = new Comune();  // CASTILENTI
		comune7279.setCodice(7279);
		comune7279.setNome("CASTILENTI");
		listaComuni89.add(comune7279);
		Comune comune7280 = new Comune();  // CELLINO ATTANASIO
		comune7280.setCodice(7280);
		comune7280.setNome("CELLINO ATTANASIO");
		listaComuni89.add(comune7280);


		/*
		 * MOLISE
		 */

		////// CAMPOBASSO
		Comune comune7367 = new Comune();  // ACQUAVIVA COLLECROCE
		comune7367.setCodice(7367);
		comune7367.setNome("ACQUAVIVA COLLECROCE");
		listaComuni19.add(comune7367);
		Comune comune7368 = new Comune();  // BARANELLO
		comune7368.setCodice(7368);
		comune7368.setNome("BARANELLO");
		listaComuni19.add(comune7368);
		Comune comune7369 = new Comune();  // BOJANO
		comune7369.setCodice(7369);
		comune7369.setNome("BOJANO");
		listaComuni19.add(comune7369);
		Comune comune7370 = new Comune();  // BONEFRO
		comune7370.setCodice(7370);
		comune7370.setNome("BONEFRO");
		listaComuni19.add(comune7370);
		Comune comune7371 = new Comune();  // BUSSO
		comune7371.setCodice(7371);
		comune7371.setNome("BUSSO");
		listaComuni19.add(comune7371);
		Comune comune7366 = new Comune();  // CAMPOBASSO
		comune7366.setCodice(7366);
		comune7366.setNome("CAMPOBASSO");
		listaComuni19.add(comune7366);
		Comune comune7372 = new Comune();  // CAMPOCHIARO
		comune7372.setCodice(7372);
		comune7372.setNome("CAMPOCHIARO");
		listaComuni19.add(comune7372);
		Comune comune7373 = new Comune();  // CAMPODIPIETRA
		comune7373.setCodice(7373);
		comune7373.setNome("CAMPODIPIETRA");
		listaComuni19.add(comune7373);
		Comune comune7374 = new Comune();  // CAMPOLIETO
		comune7374.setCodice(7374);
		comune7374.setNome("CAMPOLIETO");
		listaComuni19.add(comune7374);
		Comune comune7375 = new Comune();  // CAMPOMARINO
		comune7375.setCodice(7375);
		comune7375.setNome("CAMPOMARINO");
		listaComuni19.add(comune7375);
		Comune comune7376 = new Comune();  // CASACALENDA
		comune7376.setCodice(7376);
		comune7376.setNome("CASACALENDA");
		listaComuni19.add(comune7376);
		Comune comune7377 = new Comune();  // CASALCIPRANO
		comune7377.setCodice(7377);
		comune7377.setNome("CASALCIPRANO");
		listaComuni19.add(comune7377);
		Comune comune7378 = new Comune();  // CASTELBOTTACCIO
		comune7378.setCodice(7378);
		comune7378.setNome("CASTELBOTTACCIO");
		listaComuni19.add(comune7378);
		Comune comune7379 = new Comune();  // CASTELLINO DEL BIFERNO
		comune7379.setCodice(7379);
		comune7379.setNome("CASTELLINO DEL BIFERNO");
		listaComuni19.add(comune7379);
		Comune comune7380 = new Comune();  // CASTELMAURO
		comune7380.setCodice(7380);
		comune7380.setNome("CASTELMAURO");
		listaComuni19.add(comune7380);

		////// ISERNIA
		Comune comune7314 = new Comune();  // ACQUAVIVA D'ISERNIA
		comune7314.setCodice(7314);
		comune7314.setNome("ACQUAVIVA D'ISERNIA");
		listaComuni40.add(comune7314);
		Comune comune7315 = new Comune();  // AGNONE
		comune7315.setCodice(7315);
		comune7315.setNome("AGNONE");
		listaComuni40.add(comune7315);
		Comune comune7316 = new Comune();  // BAGNOLI DEL TRIGNO
		comune7316.setCodice(7316);
		comune7316.setNome("BAGNOLI DEL TRIGNO");
		listaComuni40.add(comune7316);
		Comune comune7317 = new Comune();  // BELMONTE DEL SANNIO
		comune7317.setCodice(7317);
		comune7317.setNome("BELMONTE DEL SANNIO");
		listaComuni40.add(comune7317);
		Comune comune7318 = new Comune();  // CANTALUPO NEL SANNIO
		comune7318.setCodice(7318);
		comune7318.setNome("CANTALUPO NEL SANNIO");
		listaComuni40.add(comune7318);
		Comune comune7319 = new Comune();  // CAPRACOTTA
		comune7319.setCodice(7319);
		comune7319.setNome("CAPRACOTTA");
		listaComuni40.add(comune7319);
		Comune comune7320 = new Comune();  // CAROVILLI
		comune7320.setCodice(7320);
		comune7320.setNome("CAROVILLI");
		listaComuni40.add(comune7320);
		Comune comune7321 = new Comune();  // CARPINONE
		comune7321.setCodice(7321);
		comune7321.setNome("CARPINONE");
		listaComuni40.add(comune7321);
		Comune comune7322 = new Comune();  // CASTEL DEL GIUDICE
		comune7322.setCodice(7322);
		comune7322.setNome("CASTEL DEL GIUDICE");
		listaComuni40.add(comune7322);
		Comune comune7325 = new Comune();  // CASTEL S.VINCENZO
		comune7325.setCodice(7325);
		comune7325.setNome("CASTEL S.VINCENZO");
		listaComuni40.add(comune7325);
		Comune comune7323 = new Comune();  // CASTELPETROSO
		comune7323.setCodice(7323);
		comune7323.setNome("CASTELPETROSO");
		listaComuni40.add(comune7323);
		Comune comune7324 = new Comune();  // CASTELPIZZUTO
		comune7324.setCodice(7324);
		comune7324.setNome("CASTELPIZZUTO");
		listaComuni40.add(comune7324);
		Comune comune7326 = new Comune();  // CASTELVERRINO
		comune7326.setCodice(7326);
		comune7326.setNome("CASTELVERRINO");
		listaComuni40.add(comune7326);
		Comune comune7327 = new Comune();  // CERRO AL VOLTURNO
		comune7327.setCodice(7327);
		comune7327.setNome("CERRO AL VOLTURNO");
		listaComuni40.add(comune7327);
		Comune comune7328 = new Comune();  // CHIAUCI
		comune7328.setCodice(7328);
		comune7328.setNome("CHIAUCI");
		listaComuni40.add(comune7328);


		/*
		 * CAMPANIA
		 */

		////// AVELLINO
		Comune comune7557 = new Comune();  // AIELLO DEL SABATO
		comune7557.setCodice(7557);
		comune7557.setNome("AIELLO DEL SABATO");
		listaComuni7.add(comune7557);
		Comune comune7558 = new Comune();  // ALTAVILLA IRPINA
		comune7558.setCodice(7558);
		comune7558.setNome("ALTAVILLA IRPINA");
		listaComuni7.add(comune7558);
		Comune comune7559 = new Comune();  // ANDRETTA
		comune7559.setCodice(7559);
		comune7559.setNome("ANDRETTA");
		listaComuni7.add(comune7559);
		Comune comune7560 = new Comune();  // AQUILONIA
		comune7560.setCodice(7560);
		comune7560.setNome("AQUILONIA");
		listaComuni7.add(comune7560);
		Comune comune7561 = new Comune();  // ARIANO IRPINO
		comune7561.setCodice(7561);
		comune7561.setNome("ARIANO IRPINO");
		listaComuni7.add(comune7561);
		Comune comune7562 = new Comune();  // ATRIPALDA
		comune7562.setCodice(7562);
		comune7562.setNome("ATRIPALDA");
		listaComuni7.add(comune7562);
		Comune comune7563 = new Comune();  // AVELLA
		comune7563.setCodice(7563);
		comune7563.setNome("AVELLA");
		listaComuni7.add(comune7563);
		Comune comune7556 = new Comune();  // AVELLINO
		comune7556.setCodice(7556);
		comune7556.setNome("AVELLINO");
		listaComuni7.add(comune7556);
		Comune comune7564 = new Comune();  // BAGNOLI IRPINO
		comune7564.setCodice(7564);
		comune7564.setNome("BAGNOLI IRPINO");
		listaComuni7.add(comune7564);
		Comune comune7565 = new Comune();  // BAIANO
		comune7565.setCodice(7565);
		comune7565.setNome("BAIANO");
		listaComuni7.add(comune7565);
		Comune comune7566 = new Comune();  // BISACCIA
		comune7566.setCodice(7566);
		comune7566.setNome("BISACCIA");
		listaComuni7.add(comune7566);
		Comune comune7567 = new Comune();  // BONITO
		comune7567.setCodice(7567);
		comune7567.setNome("BONITO");
		listaComuni7.add(comune7567);
		Comune comune7568 = new Comune();  // CAIRANO
		comune7568.setCodice(7568);
		comune7568.setNome("CAIRANO");
		listaComuni7.add(comune7568);
		Comune comune7569 = new Comune();  // CALABRITTO
		comune7569.setCodice(7569);
		comune7569.setNome("CALABRITTO");
		listaComuni7.add(comune7569);
		Comune comune7570 = new Comune();  // CALITRI
		comune7570.setCodice(7570);
		comune7570.setNome("CALITRI");
		listaComuni7.add(comune7570);

		////// BENEVENTO
		Comune comune7681 = new Comune();  // AIROLA
		comune7681.setCodice(7681);
		comune7681.setNome("AIROLA");
		listaComuni10.add(comune7681);
		Comune comune7682 = new Comune();  // AMOROSI
		comune7682.setCodice(7682);
		comune7682.setNome("AMOROSI");
		listaComuni10.add(comune7682);
		Comune comune7683 = new Comune();  // APICE
		comune7683.setCodice(7683);
		comune7683.setNome("APICE");
		listaComuni10.add(comune7683);
		Comune comune7684 = new Comune();  // APOLLOSA
		comune7684.setCodice(7684);
		comune7684.setNome("APOLLOSA");
		listaComuni10.add(comune7684);
		Comune comune7685 = new Comune();  // ARPAIA
		comune7685.setCodice(7685);
		comune7685.setNome("ARPAIA");
		listaComuni10.add(comune7685);
		Comune comune7686 = new Comune();  // ARPAISE
		comune7686.setCodice(7686);
		comune7686.setNome("ARPAISE");
		listaComuni10.add(comune7686);
		Comune comune7687 = new Comune();  // BASELICE
		comune7687.setCodice(7687);
		comune7687.setNome("BASELICE");
		listaComuni10.add(comune7687);
		Comune comune7680 = new Comune();  // BENEVENTO
		comune7680.setCodice(7680);
		comune7680.setNome("BENEVENTO");
		listaComuni10.add(comune7680);
		Comune comune7688 = new Comune();  // BONEA
		comune7688.setCodice(7688);
		comune7688.setNome("BONEA");
		listaComuni10.add(comune7688);
		Comune comune7689 = new Comune();  // BUCCIANO
		comune7689.setCodice(7689);
		comune7689.setNome("BUCCIANO");
		listaComuni10.add(comune7689);
		Comune comune7690 = new Comune();  // BUONALBERGO
		comune7690.setCodice(7690);
		comune7690.setNome("BUONALBERGO");
		listaComuni10.add(comune7690);
		Comune comune7691 = new Comune();  // CALVI
		comune7691.setCodice(7691);
		comune7691.setNome("CALVI");
		listaComuni10.add(comune7691);
		Comune comune7692 = new Comune();  // CAMPOLATTARO
		comune7692.setCodice(7692);
		comune7692.setNome("CAMPOLATTARO");
		listaComuni10.add(comune7692);
		Comune comune7693 = new Comune();  // CAMPOLI DEL MONTE TABURNO
		comune7693.setCodice(7693);
		comune7693.setNome("CAMPOLI DEL MONTE TABURNO");
		listaComuni10.add(comune7693);
		Comune comune7694 = new Comune();  // CASALDUNI
		comune7694.setCodice(7694);
		comune7694.setNome("CASALDUNI");
		listaComuni10.add(comune7694);

		////// CASERTA
		Comune comune7762 = new Comune();  // AILANO
		comune7762.setCodice(7762);
		comune7762.setNome("AILANO");
		listaComuni20.add(comune7762);
		Comune comune7763 = new Comune();  // ALIFE
		comune7763.setCodice(7763);
		comune7763.setNome("ALIFE");
		listaComuni20.add(comune7763);
		Comune comune7764 = new Comune();  // ALVIGNANO
		comune7764.setCodice(7764);
		comune7764.setNome("ALVIGNANO");
		listaComuni20.add(comune7764);
		Comune comune7765 = new Comune();  // ARIENZO
		comune7765.setCodice(7765);
		comune7765.setNome("ARIENZO");
		listaComuni20.add(comune7765);
		Comune comune7766 = new Comune();  // AVERSA
		comune7766.setCodice(7766);
		comune7766.setNome("AVERSA");
		listaComuni20.add(comune7766);
		Comune comune7767 = new Comune();  // BAIA E LATINA
		comune7767.setCodice(7767);
		comune7767.setNome("BAIA E LATINA");
		listaComuni20.add(comune7767);
		Comune comune7768 = new Comune();  // BELLONA
		comune7768.setCodice(7768);
		comune7768.setNome("BELLONA");
		listaComuni20.add(comune7768);
		Comune comune7769 = new Comune();  // CAIANELLO
		comune7769.setCodice(7769);
		comune7769.setNome("CAIANELLO");
		listaComuni20.add(comune7769);
		Comune comune7770 = new Comune();  // CAIAZZO
		comune7770.setCodice(7770);
		comune7770.setNome("CAIAZZO");
		listaComuni20.add(comune7770);
		Comune comune7771 = new Comune();  // CALVI RISORTA
		comune7771.setCodice(7771);
		comune7771.setNome("CALVI RISORTA");
		listaComuni20.add(comune7771);
		Comune comune7772 = new Comune();  // CAMIGLIANO
		comune7772.setCodice(7772);
		comune7772.setNome("CAMIGLIANO");
		listaComuni20.add(comune7772);
		Comune comune7773 = new Comune();  // CANCELLO ED ARNONE
		comune7773.setCodice(7773);
		comune7773.setNome("CANCELLO ED ARNONE");
		listaComuni20.add(comune7773);
		Comune comune7774 = new Comune();  // CAPODRISE
		comune7774.setCodice(7774);
		comune7774.setNome("CAPODRISE");
		listaComuni20.add(comune7774);
		Comune comune7775 = new Comune();  // CAPRIATI AL VOLTURNO
		comune7775.setCodice(7775);
		comune7775.setNome("CAPRIATI AL VOLTURNO");
		listaComuni20.add(comune7775);
		Comune comune7776 = new Comune();  // CAPUA
		comune7776.setCodice(7776);
		comune7776.setNome("CAPUA");
		listaComuni20.add(comune7776);

		////// NAPOLI
		Comune comune7451 = new Comune();  // ACERRA
		comune7451.setCodice(7451);
		comune7451.setNome("ACERRA");
		listaComuni56.add(comune7451);
		Comune comune7452 = new Comune();  // AFRAGOLA
		comune7452.setCodice(7452);
		comune7452.setNome("AFRAGOLA");
		listaComuni56.add(comune7452);
		Comune comune7453 = new Comune();  // AGEROLA
		comune7453.setCodice(7453);
		comune7453.setNome("AGEROLA");
		listaComuni56.add(comune7453);
		Comune comune7454 = new Comune();  // ANACAPRI
		comune7454.setCodice(7454);
		comune7454.setNome("ANACAPRI");
		listaComuni56.add(comune7454);
		Comune comune7455 = new Comune();  // ARZANO
		comune7455.setCodice(7455);
		comune7455.setNome("ARZANO");
		listaComuni56.add(comune7455);
		Comune comune7456 = new Comune();  // BACOLI
		comune7456.setCodice(7456);
		comune7456.setNome("BACOLI");
		listaComuni56.add(comune7456);
		Comune comune7457 = new Comune();  // BARANO D'ISCHIA
		comune7457.setCodice(7457);
		comune7457.setNome("BARANO D'ISCHIA");
		listaComuni56.add(comune7457);
		Comune comune7458 = new Comune();  // BOSCOREALE
		comune7458.setCodice(7458);
		comune7458.setNome("BOSCOREALE");
		listaComuni56.add(comune7458);
		Comune comune7459 = new Comune();  // BOSCOTRECASE
		comune7459.setCodice(7459);
		comune7459.setNome("BOSCOTRECASE");
		listaComuni56.add(comune7459);
		Comune comune7460 = new Comune();  // BRUSCIANO
		comune7460.setCodice(7460);
		comune7460.setNome("BRUSCIANO");
		listaComuni56.add(comune7460);
		Comune comune7461 = new Comune();  // CAIVANO
		comune7461.setCodice(7461);
		comune7461.setNome("CAIVANO");
		listaComuni56.add(comune7461);
		Comune comune7462 = new Comune();  // CALVIZZANO
		comune7462.setCodice(7462);
		comune7462.setNome("CALVIZZANO");
		listaComuni56.add(comune7462);
		Comune comune7463 = new Comune();  // CAMPOSANO
		comune7463.setCodice(7463);
		comune7463.setNome("CAMPOSANO");
		listaComuni56.add(comune7463);
		Comune comune7464 = new Comune();  // CAPRI
		comune7464.setCodice(7464);
		comune7464.setNome("CAPRI");
		listaComuni56.add(comune7464);
		Comune comune7465 = new Comune();  // CARBONARA DI NOLA
		comune7465.setCodice(7465);
		comune7465.setNome("CARBONARA DI NOLA");
		listaComuni56.add(comune7465);

		////// SALERNO
		Comune comune7886 = new Comune();  // ACERNO
		comune7886.setCodice(7886);
		comune7886.setNome("ACERNO");
		listaComuni82.add(comune7886);
		Comune comune7887 = new Comune();  // AGROPOLI
		comune7887.setCodice(7887);
		comune7887.setNome("AGROPOLI");
		listaComuni82.add(comune7887);
		Comune comune7888 = new Comune();  // ALBANELLA
		comune7888.setCodice(7888);
		comune7888.setNome("ALBANELLA");
		listaComuni82.add(comune7888);
		Comune comune7889 = new Comune();  // ALFANO
		comune7889.setCodice(7889);
		comune7889.setNome("ALFANO");
		listaComuni82.add(comune7889);
		Comune comune7890 = new Comune();  // ALTAVILLA SILENTINA
		comune7890.setCodice(7890);
		comune7890.setNome("ALTAVILLA SILENTINA");
		listaComuni82.add(comune7890);
		Comune comune7891 = new Comune();  // AMALFI
		comune7891.setCodice(7891);
		comune7891.setNome("AMALFI");
		listaComuni82.add(comune7891);
		Comune comune7892 = new Comune();  // ANGRI
		comune7892.setCodice(7892);
		comune7892.setNome("ANGRI");
		listaComuni82.add(comune7892);
		Comune comune7893 = new Comune();  // AQUARA
		comune7893.setCodice(7893);
		comune7893.setNome("AQUARA");
		listaComuni82.add(comune7893);
		Comune comune7894 = new Comune();  // ASCEA
		comune7894.setCodice(7894);
		comune7894.setNome("ASCEA");
		listaComuni82.add(comune7894);
		Comune comune7895 = new Comune();  // ATENA LUCANA
		comune7895.setCodice(7895);
		comune7895.setNome("ATENA LUCANA");
		listaComuni82.add(comune7895);
		Comune comune7896 = new Comune();  // ATRANI
		comune7896.setCodice(7896);
		comune7896.setNome("ATRANI");
		listaComuni82.add(comune7896);
		Comune comune7897 = new Comune();  // AULETTA
		comune7897.setCodice(7897);
		comune7897.setNome("AULETTA");
		listaComuni82.add(comune7897);
		Comune comune7898 = new Comune();  // BARONISSI
		comune7898.setCodice(7898);
		comune7898.setNome("BARONISSI");
		listaComuni82.add(comune7898);
		Comune comune7899 = new Comune();  // BATTIPAGLIA
		comune7899.setCodice(7899);
		comune7899.setNome("BATTIPAGLIA");
		listaComuni82.add(comune7899);
		Comune comune8047 = new Comune();  // BELLIZZI
		comune8047.setCodice(8047);
		comune8047.setNome("BELLIZZI");
		listaComuni82.add(comune8047);


		/*
		 * PUGLIA
		 */

		////// BARI
		Comune comune8049 = new Comune();  // ACQUAVIVA DELLE FONTI
		comune8049.setCodice(8049);
		comune8049.setNome("ACQUAVIVA DELLE FONTI");
		listaComuni8.add(comune8049);
		Comune comune8050 = new Comune();  // ADELFIA
		comune8050.setCodice(8050);
		comune8050.setNome("ADELFIA");
		listaComuni8.add(comune8050);
		Comune comune8051 = new Comune();  // ALBEROBELLO
		comune8051.setCodice(8051);
		comune8051.setNome("ALBEROBELLO");
		listaComuni8.add(comune8051);
		Comune comune8052 = new Comune();  // ALTAMURA
		comune8052.setCodice(8052);
		comune8052.setNome("ALTAMURA");
		listaComuni8.add(comune8052);
		Comune comune8053 = new Comune();  // ANDRIA
		comune8053.setCodice(8053);
		comune8053.setNome("ANDRIA");
		listaComuni8.add(comune8053);
		Comune comune8048 = new Comune();  // BARI
		comune8048.setCodice(8048);
		comune8048.setNome("BARI");
		listaComuni8.add(comune8048);
		Comune comune8054 = new Comune();  // BARLETTA
		comune8054.setCodice(8054);
		comune8054.setNome("BARLETTA");
		listaComuni8.add(comune8054);
		Comune comune8055 = new Comune();  // BINETTO
		comune8055.setCodice(8055);
		comune8055.setNome("BINETTO");
		listaComuni8.add(comune8055);
		Comune comune8056 = new Comune();  // BISCEGLIE
		comune8056.setCodice(8056);
		comune8056.setNome("BISCEGLIE");
		listaComuni8.add(comune8056);
		Comune comune8057 = new Comune();  // BITETTO
		comune8057.setCodice(8057);
		comune8057.setNome("BITETTO");
		listaComuni8.add(comune8057);
		Comune comune8058 = new Comune();  // BITONTO
		comune8058.setCodice(8058);
		comune8058.setNome("BITONTO");
		listaComuni8.add(comune8058);
		Comune comune8059 = new Comune();  // BITRITTO
		comune8059.setCodice(8059);
		comune8059.setNome("BITRITTO");
		listaComuni8.add(comune8059);
		Comune comune8060 = new Comune();  // CANOSA DI PUGLIA
		comune8060.setCodice(8060);
		comune8060.setNome("CANOSA DI PUGLIA");
		listaComuni8.add(comune8060);
		Comune comune8061 = new Comune();  // CAPURSO
		comune8061.setCodice(8061);
		comune8061.setNome("CAPURSO");
		listaComuni8.add(comune8061);
		Comune comune8062 = new Comune();  // CASAMASSIMA
		comune8062.setCodice(8062);
		comune8062.setNome("CASAMASSIMA");
		listaComuni8.add(comune8062);

		////// BRINDISI
		Comune comune8101 = new Comune();  // BRINDISI
		comune8101.setCodice(8101);
		comune8101.setNome("BRINDISI");
		listaComuni16.add(comune8101);
		Comune comune8102 = new Comune();  // CAROVIGNO
		comune8102.setCodice(8102);
		comune8102.setNome("CAROVIGNO");
		listaComuni16.add(comune8102);
		Comune comune8121 = new Comune();  // CEGLIE MESSAPICA
		comune8121.setCodice(8121);
		comune8121.setNome("CEGLIE MESSAPICA");
		listaComuni16.add(comune8121);
		Comune comune8104 = new Comune();  // CELLINO S.MARCO
		comune8104.setCodice(8104);
		comune8104.setNome("CELLINO S.MARCO");
		listaComuni16.add(comune8104);
		Comune comune8105 = new Comune();  // CISTERNINO
		comune8105.setCodice(8105);
		comune8105.setNome("CISTERNINO");
		listaComuni16.add(comune8105);
		Comune comune8106 = new Comune();  // ERCHIE
		comune8106.setCodice(8106);
		comune8106.setNome("ERCHIE");
		listaComuni16.add(comune8106);
		Comune comune8107 = new Comune();  // FASANO
		comune8107.setCodice(8107);
		comune8107.setNome("FASANO");
		listaComuni16.add(comune8107);
		Comune comune8108 = new Comune();  // FRANCAVILLA FONTANA
		comune8108.setCodice(8108);
		comune8108.setNome("FRANCAVILLA FONTANA");
		listaComuni16.add(comune8108);
		Comune comune8109 = new Comune();  // LATIANO
		comune8109.setCodice(8109);
		comune8109.setNome("LATIANO");
		listaComuni16.add(comune8109);
		Comune comune8110 = new Comune();  // MESAGNE
		comune8110.setCodice(8110);
		comune8110.setNome("MESAGNE");
		listaComuni16.add(comune8110);
		Comune comune8111 = new Comune();  // ORIA
		comune8111.setCodice(8111);
		comune8111.setNome("ORIA");
		listaComuni16.add(comune8111);
		Comune comune8112 = new Comune();  // OSTUNI
		comune8112.setCodice(8112);
		comune8112.setNome("OSTUNI");
		listaComuni16.add(comune8112);
		Comune comune8115 = new Comune();  // SAN PANCRAZIO SALENTINO
		comune8115.setCodice(8115);
		comune8115.setNome("SAN PANCRAZIO SALENTINO");
		listaComuni16.add(comune8115);
		Comune comune8113 = new Comune();  // S.DONACI
		comune8113.setCodice(8113);
		comune8113.setNome("S.DONACI");
		listaComuni16.add(comune8113);
		Comune comune8114 = new Comune();  // S.MICHELE SALENTINO
		comune8114.setCodice(8114);
		comune8114.setNome("S.MICHELE SALENTINO");
		listaComuni16.add(comune8114);

		////// FOGGIA
		Comune comune8123 = new Comune();  // ACCADIA
		comune8123.setCodice(8123);
		comune8123.setNome("ACCADIA");
		listaComuni33.add(comune8123);
		Comune comune8124 = new Comune();  // ALBERONA
		comune8124.setCodice(8124);
		comune8124.setNome("ALBERONA");
		listaComuni33.add(comune8124);
		Comune comune8125 = new Comune();  // ANZANO DI PUGLIA
		comune8125.setCodice(8125);
		comune8125.setNome("ANZANO DI PUGLIA");
		listaComuni33.add(comune8125);
		Comune comune8126 = new Comune();  // APRICENA
		comune8126.setCodice(8126);
		comune8126.setNome("APRICENA");
		listaComuni33.add(comune8126);
		Comune comune8127 = new Comune();  // ASCOLI SATRIANO
		comune8127.setCodice(8127);
		comune8127.setNome("ASCOLI SATRIANO");
		listaComuni33.add(comune8127);
		Comune comune8128 = new Comune();  // BICCARI
		comune8128.setCodice(8128);
		comune8128.setNome("BICCARI");
		listaComuni33.add(comune8128);
		Comune comune8129 = new Comune();  // BOVINO
		comune8129.setCodice(8129);
		comune8129.setNome("BOVINO");
		listaComuni33.add(comune8129);
		Comune comune8130 = new Comune();  // CAGNANO VARANO
		comune8130.setCodice(8130);
		comune8130.setNome("CAGNANO VARANO");
		listaComuni33.add(comune8130);
		Comune comune8131 = new Comune();  // CANDELA
		comune8131.setCodice(8131);
		comune8131.setNome("CANDELA");
		listaComuni33.add(comune8131);
		Comune comune8132 = new Comune();  // CARAPELLE
		comune8132.setCodice(8132);
		comune8132.setNome("CARAPELLE");
		listaComuni33.add(comune8132);
		Comune comune8133 = new Comune();  // CARLANTINO
		comune8133.setCodice(8133);
		comune8133.setNome("CARLANTINO");
		listaComuni33.add(comune8133);
		Comune comune8134 = new Comune();  // CARPINO
		comune8134.setCodice(8134);
		comune8134.setNome("CARPINO");
		listaComuni33.add(comune8134);
		Comune comune8135 = new Comune();  // CASALNUOVO MONTEROTARO
		comune8135.setCodice(8135);
		comune8135.setNome("CASALNUOVO MONTEROTARO");
		listaComuni33.add(comune8135);
		Comune comune8136 = new Comune();  // CASALVECCHIO DI PUGLIA
		comune8136.setCodice(8136);
		comune8136.setNome("CASALVECCHIO DI PUGLIA");
		listaComuni33.add(comune8136);
		Comune comune8137 = new Comune();  // CASTELLUCCIO DEI SAURI
		comune8137.setCodice(8137);
		comune8137.setNome("CASTELLUCCIO DEI SAURI");
		listaComuni33.add(comune8137);

		////// LECCE
		Comune comune8190 = new Comune();  // ACQUARICA DEL CAPO
		comune8190.setCodice(8190);
		comune8190.setNome("ACQUARICA DEL CAPO");
		listaComuni44.add(comune8190);
		Comune comune8191 = new Comune();  // ALESSANO
		comune8191.setCodice(8191);
		comune8191.setNome("ALESSANO");
		listaComuni44.add(comune8191);
		Comune comune8192 = new Comune();  // ALEZIO
		comune8192.setCodice(8192);
		comune8192.setNome("ALEZIO");
		listaComuni44.add(comune8192);
		Comune comune8193 = new Comune();  // ALLISTE
		comune8193.setCodice(8193);
		comune8193.setNome("ALLISTE");
		listaComuni44.add(comune8193);
		Comune comune8194 = new Comune();  // ANDRANO
		comune8194.setCodice(8194);
		comune8194.setNome("ANDRANO");
		listaComuni44.add(comune8194);
		Comune comune8195 = new Comune();  // ARADEO
		comune8195.setCodice(8195);
		comune8195.setNome("ARADEO");
		listaComuni44.add(comune8195);
		Comune comune8196 = new Comune();  // ARNESANO
		comune8196.setCodice(8196);
		comune8196.setNome("ARNESANO");
		listaComuni44.add(comune8196);
		Comune comune8197 = new Comune();  // BAGNOLO DEL SALENTO
		comune8197.setCodice(8197);
		comune8197.setNome("BAGNOLO DEL SALENTO");
		listaComuni44.add(comune8197);
		Comune comune8740 = new Comune();  // BOTRICELLO GIA' FRAZ. DI ANDALI
		comune8740.setCodice(8740);
		comune8740.setNome("BOTRICELLO GIA' FRAZ. DI ANDALI");
		listaComuni44.add(comune8740);
		Comune comune8198 = new Comune();  // BOTRUGNO
		comune8198.setCodice(8198);
		comune8198.setNome("BOTRUGNO");
		listaComuni44.add(comune8198);
		Comune comune8199 = new Comune();  // CALIMERA
		comune8199.setCodice(8199);
		comune8199.setNome("CALIMERA");
		listaComuni44.add(comune8199);
		Comune comune8200 = new Comune();  // CAMPI SALENTINA
		comune8200.setCodice(8200);
		comune8200.setNome("CAMPI SALENTINA");
		listaComuni44.add(comune8200);
		Comune comune8201 = new Comune();  // CANNOLE
		comune8201.setCodice(8201);
		comune8201.setNome("CANNOLE");
		listaComuni44.add(comune8201);
		Comune comune8202 = new Comune();  // CAPRARICA DI LECCE
		comune8202.setCodice(8202);
		comune8202.setNome("CAPRARICA DI LECCE");
		listaComuni44.add(comune8202);
		Comune comune8203 = new Comune();  // CARMIANO
		comune8203.setCodice(8203);
		comune8203.setNome("CARMIANO");
		listaComuni44.add(comune8203);

		////// TARANTO
		Comune comune8287 = new Comune();  // AVETRANA
		comune8287.setCodice(8287);
		comune8287.setNome("AVETRANA");
		listaComuni88.add(comune8287);
		Comune comune8288 = new Comune();  // CAROSINO
		comune8288.setCodice(8288);
		comune8288.setNome("CAROSINO");
		listaComuni88.add(comune8288);
		Comune comune8289 = new Comune();  // CASTELLANETA
		comune8289.setCodice(8289);
		comune8289.setNome("CASTELLANETA");
		listaComuni88.add(comune8289);
		Comune comune8290 = new Comune();  // CRISPIANO
		comune8290.setCodice(8290);
		comune8290.setNome("CRISPIANO");
		listaComuni88.add(comune8290);
		Comune comune8291 = new Comune();  // FAGGIANO
		comune8291.setCodice(8291);
		comune8291.setNome("FAGGIANO");
		listaComuni88.add(comune8291);
		Comune comune8292 = new Comune();  // FRAGAGNANO
		comune8292.setCodice(8292);
		comune8292.setNome("FRAGAGNANO");
		listaComuni88.add(comune8292);
		Comune comune8293 = new Comune();  // GINOSA
		comune8293.setCodice(8293);
		comune8293.setNome("GINOSA");
		listaComuni88.add(comune8293);
		Comune comune8294 = new Comune();  // GROTTAGLIE
		comune8294.setCodice(8294);
		comune8294.setNome("GROTTAGLIE");
		listaComuni88.add(comune8294);
		Comune comune8295 = new Comune();  // LATERZA
		comune8295.setCodice(8295);
		comune8295.setNome("LATERZA");
		listaComuni88.add(comune8295);
		Comune comune8296 = new Comune();  // LEPORANO
		comune8296.setCodice(8296);
		comune8296.setNome("LEPORANO");
		listaComuni88.add(comune8296);
		Comune comune8297 = new Comune();  // LIZZANO
		comune8297.setCodice(8297);
		comune8297.setNome("LIZZANO");
		listaComuni88.add(comune8297);
		Comune comune8298 = new Comune();  // MANDURIA
		comune8298.setCodice(8298);
		comune8298.setNome("MANDURIA");
		listaComuni88.add(comune8298);
		Comune comune8299 = new Comune();  // MARTINA FRANCA
		comune8299.setCodice(8299);
		comune8299.setNome("MARTINA FRANCA");
		listaComuni88.add(comune8299);
		Comune comune8300 = new Comune();  // MARUGGIO
		comune8300.setCodice(8300);
		comune8300.setNome("MARUGGIO");
		listaComuni88.add(comune8300);
		Comune comune8301 = new Comune();  // MASSAFRA
		comune8301.setCodice(8301);
		comune8301.setNome("MASSAFRA");
		listaComuni88.add(comune8301);


		/*
		 * BASILICATA
		 */

		////// MATERA
		Comune comune8423 = new Comune();  // ACCETTURA
		comune8423.setCodice(8423);
		comune8423.setNome("ACCETTURA");
		listaComuni52.add(comune8423);
		Comune comune8424 = new Comune();  // ALIANO
		comune8424.setCodice(8424);
		comune8424.setNome("ALIANO");
		listaComuni52.add(comune8424);
		Comune comune8425 = new Comune();  // BERNALDA
		comune8425.setCodice(8425);
		comune8425.setNome("BERNALDA");
		listaComuni52.add(comune8425);
		Comune comune8426 = new Comune();  // CALCIANO
		comune8426.setCodice(8426);
		comune8426.setNome("CALCIANO");
		listaComuni52.add(comune8426);
		Comune comune8427 = new Comune();  // CIRIGLIANO
		comune8427.setCodice(8427);
		comune8427.setNome("CIRIGLIANO");
		listaComuni52.add(comune8427);
		Comune comune8428 = new Comune();  // COLOBRARO
		comune8428.setCodice(8428);
		comune8428.setNome("COLOBRARO");
		listaComuni52.add(comune8428);
		Comune comune8429 = new Comune();  // CRACO
		comune8429.setCodice(8429);
		comune8429.setNome("CRACO");
		listaComuni52.add(comune8429);
		Comune comune8430 = new Comune();  // FERRANDINA
		comune8430.setCodice(8430);
		comune8430.setNome("FERRANDINA");
		listaComuni52.add(comune8430);
		Comune comune8431 = new Comune();  // GARAGUSO
		comune8431.setCodice(8431);
		comune8431.setNome("GARAGUSO");
		listaComuni52.add(comune8431);
		Comune comune8432 = new Comune();  // GORGOGLIONE
		comune8432.setCodice(8432);
		comune8432.setNome("GORGOGLIONE");
		listaComuni52.add(comune8432);
		Comune comune8433 = new Comune();  // GRASSANO
		comune8433.setCodice(8433);
		comune8433.setNome("GRASSANO");
		listaComuni52.add(comune8433);
		Comune comune8434 = new Comune();  // GROTTOLE
		comune8434.setCodice(8434);
		comune8434.setNome("GROTTOLE");
		listaComuni52.add(comune8434);
		Comune comune8435 = new Comune();  // IRSINA
		comune8435.setCodice(8435);
		comune8435.setNome("IRSINA");
		listaComuni52.add(comune8435);
		Comune comune8422 = new Comune();  // MATERA
		comune8422.setCodice(8422);
		comune8422.setNome("MATERA");
		listaComuni52.add(comune8422);
		Comune comune8436 = new Comune();  // MIGLIONICO
		comune8436.setCodice(8436);
		comune8436.setNome("MIGLIONICO");
		listaComuni52.add(comune8436);

		////// POTENZA
		Comune comune8318 = new Comune();  // ABRIOLA
		comune8318.setCodice(8318);
		comune8318.setNome("ABRIOLA");
		listaComuni72.add(comune8318);
		Comune comune8319 = new Comune();  // ACERENZA
		comune8319.setCodice(8319);
		comune8319.setNome("ACERENZA");
		listaComuni72.add(comune8319);
		Comune comune8320 = new Comune();  // ALBANO DI LUCANIA
		comune8320.setCodice(8320);
		comune8320.setNome("ALBANO DI LUCANIA");
		listaComuni72.add(comune8320);
		Comune comune8321 = new Comune();  // ANZI
		comune8321.setCodice(8321);
		comune8321.setNome("ANZI");
		listaComuni72.add(comune8321);
		Comune comune8322 = new Comune();  // ARMENTO
		comune8322.setCodice(8322);
		comune8322.setNome("ARMENTO");
		listaComuni72.add(comune8322);
		Comune comune8323 = new Comune();  // ATELLA
		comune8323.setCodice(8323);
		comune8323.setNome("ATELLA");
		listaComuni72.add(comune8323);
		Comune comune8324 = new Comune();  // AVIGLIANO
		comune8324.setCodice(8324);
		comune8324.setNome("AVIGLIANO");
		listaComuni72.add(comune8324);
		Comune comune8325 = new Comune();  // BALVANO
		comune8325.setCodice(8325);
		comune8325.setNome("BALVANO");
		listaComuni72.add(comune8325);
		Comune comune8326 = new Comune();  // BANZI
		comune8326.setCodice(8326);
		comune8326.setNome("BANZI");
		listaComuni72.add(comune8326);
		Comune comune8327 = new Comune();  // BARAGIANO
		comune8327.setCodice(8327);
		comune8327.setNome("BARAGIANO");
		listaComuni72.add(comune8327);
		Comune comune8328 = new Comune();  // BARILE
		comune8328.setCodice(8328);
		comune8328.setNome("BARILE");
		listaComuni72.add(comune8328);
		Comune comune8329 = new Comune();  // BELLA
		comune8329.setCodice(8329);
		comune8329.setNome("BELLA");
		listaComuni72.add(comune8329);
		Comune comune8330 = new Comune();  // BRIENZA
		comune8330.setCodice(8330);
		comune8330.setNome("BRIENZA");
		listaComuni72.add(comune8330);
		Comune comune8331 = new Comune();  // BRINDISI MONTAGNA
		comune8331.setCodice(8331);
		comune8331.setNome("BRINDISI MONTAGNA");
		listaComuni72.add(comune8331);
		Comune comune8332 = new Comune();  // CALVELLO
		comune8332.setCodice(8332);
		comune8332.setNome("CALVELLO");
		listaComuni72.add(comune8332);


		/*
		 * CALABRIA
		 */

		////// CATANZARO
		Comune comune8576 = new Comune();  // ALBI
		comune8576.setCodice(8576);
		comune8576.setNome("ALBI");
		listaComuni22.add(comune8576);
		Comune comune8577 = new Comune();  // AMARONI
		comune8577.setCodice(8577);
		comune8577.setNome("AMARONI");
		listaComuni22.add(comune8577);
		Comune comune8578 = new Comune();  // AMATO
		comune8578.setCodice(8578);
		comune8578.setNome("AMATO");
		listaComuni22.add(comune8578);
		Comune comune8579 = new Comune();  // ANDALI
		comune8579.setCodice(8579);
		comune8579.setNome("ANDALI");
		listaComuni22.add(comune8579);
		Comune comune8581 = new Comune();  // ARGUSTO
		comune8581.setCodice(8581);
		comune8581.setNome("ARGUSTO");
		listaComuni22.add(comune8581);
		Comune comune8582 = new Comune();  // BADOLATO
		comune8582.setCodice(8582);
		comune8582.setNome("BADOLATO");
		listaComuni22.add(comune8582);
		Comune comune8583 = new Comune();  // BELCASTRO
		comune8583.setCodice(8583);
		comune8583.setNome("BELCASTRO");
		listaComuni22.add(comune8583);
		Comune comune8585 = new Comune();  // BORGIA
		comune8585.setCodice(8585);
		comune8585.setNome("BORGIA");
		listaComuni22.add(comune8585);
		Comune comune8586 = new Comune();  // BOTRICELLO
		comune8586.setCodice(8586);
		comune8586.setNome("BOTRICELLO");
		listaComuni22.add(comune8586);
		Comune comune8591 = new Comune();  // CARAFFA DI CATANZARO
		comune8591.setCodice(8591);
		comune8591.setNome("CARAFFA DI CATANZARO");
		listaComuni22.add(comune8591);
		Comune comune8592 = new Comune();  // CARDINALE
		comune8592.setCodice(8592);
		comune8592.setNome("CARDINALE");
		listaComuni22.add(comune8592);
		Comune comune8594 = new Comune();  // CARLOPOLI
		comune8594.setCodice(8594);
		comune8594.setNome("CARLOPOLI");
		listaComuni22.add(comune8594);
		Comune comune8574 = new Comune();  // CATANZARO
		comune8574.setCodice(8574);
		comune8574.setNome("CATANZARO");
		listaComuni22.add(comune8574);
		Comune comune8597 = new Comune();  // CENADI
		comune8597.setCodice(8597);
		comune8597.setNome("CENADI");
		listaComuni22.add(comune8597);
		Comune comune8598 = new Comune();  // CENTRACHE
		comune8598.setCodice(8598);
		comune8598.setNome("CENTRACHE");
		listaComuni22.add(comune8598);

		////// COSENZA
		Comune comune8743 = new Comune();  // ACQUAFORMOSA
		comune8743.setCodice(8743);
		comune8743.setNome("ACQUAFORMOSA");
		listaComuni25.add(comune8743);
		Comune comune8744 = new Comune();  // ACQUAPPESA
		comune8744.setCodice(8744);
		comune8744.setNome("ACQUAPPESA");
		listaComuni25.add(comune8744);
		Comune comune8745 = new Comune();  // ACRI
		comune8745.setCodice(8745);
		comune8745.setNome("ACRI");
		listaComuni25.add(comune8745);
		Comune comune8746 = new Comune();  // AIELLO CALABRO
		comune8746.setCodice(8746);
		comune8746.setNome("AIELLO CALABRO");
		listaComuni25.add(comune8746);
		Comune comune8747 = new Comune();  // AIETA
		comune8747.setCodice(8747);
		comune8747.setNome("AIETA");
		listaComuni25.add(comune8747);
		Comune comune8748 = new Comune();  // ALBIDONA
		comune8748.setCodice(8748);
		comune8748.setNome("ALBIDONA");
		listaComuni25.add(comune8748);
		Comune comune8749 = new Comune();  // ALESSANDRIA DEL CARRETTO
		comune8749.setCodice(8749);
		comune8749.setNome("ALESSANDRIA DEL CARRETTO");
		listaComuni25.add(comune8749);
		Comune comune8750 = new Comune();  // ALTILIA
		comune8750.setCodice(8750);
		comune8750.setNome("ALTILIA");
		listaComuni25.add(comune8750);
		Comune comune8751 = new Comune();  // ALTOMONTE
		comune8751.setCodice(8751);
		comune8751.setNome("ALTOMONTE");
		listaComuni25.add(comune8751);
		Comune comune8752 = new Comune();  // AMANTEA
		comune8752.setCodice(8752);
		comune8752.setNome("AMANTEA");
		listaComuni25.add(comune8752);
		Comune comune8753 = new Comune();  // AMENDOLARA
		comune8753.setCodice(8753);
		comune8753.setNome("AMENDOLARA");
		listaComuni25.add(comune8753);
		Comune comune8754 = new Comune();  // APRIGLIANO
		comune8754.setCodice(8754);
		comune8754.setNome("APRIGLIANO");
		listaComuni25.add(comune8754);
		Comune comune8755 = new Comune();  // BELMONTE CALABRO
		comune8755.setCodice(8755);
		comune8755.setNome("BELMONTE CALABRO");
		listaComuni25.add(comune8755);
		Comune comune8756 = new Comune();  // BELSITO
		comune8756.setCodice(8756);
		comune8756.setNome("BELSITO");
		listaComuni25.add(comune8756);
		Comune comune8757 = new Comune();  // BELVEDERE MARITTIMO
		comune8757.setCodice(8757);
		comune8757.setNome("BELVEDERE MARITTIMO");
		listaComuni25.add(comune8757);

		////// CROTONE
		Comune comune8902 = new Comune();  // BELVEDERE DI SPINELLO
		comune8902.setCodice(8902);
		comune8902.setNome("BELVEDERE DI SPINELLO");
		listaComuni27.add(comune8902);
		Comune comune8903 = new Comune();  // CACCURI
		comune8903.setCodice(8903);
		comune8903.setNome("CACCURI");
		listaComuni27.add(comune8903);
		Comune comune8904 = new Comune();  // CARFIZZI
		comune8904.setCodice(8904);
		comune8904.setNome("CARFIZZI");
		listaComuni27.add(comune8904);
		Comune comune8905 = new Comune();  // CASABONA
		comune8905.setCodice(8905);
		comune8905.setNome("CASABONA");
		listaComuni27.add(comune8905);
		Comune comune8906 = new Comune();  // CASTELSILANO
		comune8906.setCodice(8906);
		comune8906.setNome("CASTELSILANO");
		listaComuni27.add(comune8906);
		Comune comune8907 = new Comune();  // CERENZIA
		comune8907.setCodice(8907);
		comune8907.setNome("CERENZIA");
		listaComuni27.add(comune8907);
		Comune comune8908 = new Comune();  // CIRO'
		comune8908.setCodice(8908);
		comune8908.setNome("CIRO'");
		listaComuni27.add(comune8908);
		Comune comune8909 = new Comune();  // CIRO' MARINA
		comune8909.setCodice(8909);
		comune8909.setNome("CIRO' MARINA");
		listaComuni27.add(comune8909);
		Comune comune8910 = new Comune();  // COTRONEI
		comune8910.setCodice(8910);
		comune8910.setNome("COTRONEI");
		listaComuni27.add(comune8910);
		Comune comune8901 = new Comune();  // CROTONE
		comune8901.setCodice(8901);
		comune8901.setNome("CROTONE");
		listaComuni27.add(comune8901);
		Comune comune8911 = new Comune();  // CRUCOLI
		comune8911.setCodice(8911);
		comune8911.setNome("CRUCOLI");
		listaComuni27.add(comune8911);
		Comune comune8912 = new Comune();  // CUTRO
		comune8912.setCodice(8912);
		comune8912.setNome("CUTRO");
		listaComuni27.add(comune8912);
		Comune comune8913 = new Comune();  // ISOLA DI CAPO RIZZUTO
		comune8913.setCodice(8913);
		comune8913.setNome("ISOLA DI CAPO RIZZUTO");
		listaComuni27.add(comune8913);
		Comune comune8914 = new Comune();  // MELISSA
		comune8914.setCodice(8914);
		comune8914.setNome("MELISSA");
		listaComuni27.add(comune8914);
		Comune comune8915 = new Comune();  // MESORACA
		comune8915.setCodice(8915);
		comune8915.setNome("MESORACA");
		listaComuni27.add(comune8915);

		////// REGGIO DI CALABRIA
		Comune comune8454 = new Comune();  // AFRICO
		comune8454.setCodice(8454);
		comune8454.setNome("AFRICO");
		listaComuni76.add(comune8454);
		Comune comune8455 = new Comune();  // AGNANA CALABRA
		comune8455.setCodice(8455);
		comune8455.setNome("AGNANA CALABRA");
		listaComuni76.add(comune8455);
		Comune comune8456 = new Comune();  // ANOIA
		comune8456.setCodice(8456);
		comune8456.setNome("ANOIA");
		listaComuni76.add(comune8456);
		Comune comune8457 = new Comune();  // ANTONIMINA
		comune8457.setCodice(8457);
		comune8457.setNome("ANTONIMINA");
		listaComuni76.add(comune8457);
		Comune comune8458 = new Comune();  // ARDORE
		comune8458.setCodice(8458);
		comune8458.setNome("ARDORE");
		listaComuni76.add(comune8458);
		Comune comune8459 = new Comune();  // BAGALADI
		comune8459.setCodice(8459);
		comune8459.setNome("BAGALADI");
		listaComuni76.add(comune8459);
		Comune comune8460 = new Comune();  // BAGNARA CALABRA
		comune8460.setCodice(8460);
		comune8460.setNome("BAGNARA CALABRA");
		listaComuni76.add(comune8460);
		Comune comune8461 = new Comune();  // BENESTARE
		comune8461.setCodice(8461);
		comune8461.setNome("BENESTARE");
		listaComuni76.add(comune8461);
		Comune comune8462 = new Comune();  // BIANCO
		comune8462.setCodice(8462);
		comune8462.setNome("BIANCO");
		listaComuni76.add(comune8462);
		Comune comune8463 = new Comune();  // BIVONGI
		comune8463.setCodice(8463);
		comune8463.setNome("BIVONGI");
		listaComuni76.add(comune8463);
		Comune comune8464 = new Comune();  // BOVA
		comune8464.setCodice(8464);
		comune8464.setNome("BOVA");
		listaComuni76.add(comune8464);
		Comune comune8465 = new Comune();  // BOVA MARINA
		comune8465.setCodice(8465);
		comune8465.setNome("BOVA MARINA");
		listaComuni76.add(comune8465);
		Comune comune8466 = new Comune();  // BOVALINO
		comune8466.setCodice(8466);
		comune8466.setNome("BOVALINO");
		listaComuni76.add(comune8466);
		Comune comune8467 = new Comune();  // BRANCALEONE
		comune8467.setCodice(8467);
		comune8467.setNome("BRANCALEONE");
		listaComuni76.add(comune8467);
		Comune comune8468 = new Comune();  // BRUZZANO ZEFFIRIO
		comune8468.setCodice(8468);
		comune8468.setNome("BRUZZANO ZEFFIRIO");
		listaComuni76.add(comune8468);

		////// VIBO VALENTIA
		Comune comune8929 = new Comune();  // ACQUARO
		comune8929.setCodice(8929);
		comune8929.setNome("ACQUARO");
		listaComuni103.add(comune8929);
		Comune comune8930 = new Comune();  // ARENA
		comune8930.setCodice(8930);
		comune8930.setNome("ARENA");
		listaComuni103.add(comune8930);
		Comune comune8931 = new Comune();  // BRIATICO
		comune8931.setCodice(8931);
		comune8931.setNome("BRIATICO");
		listaComuni103.add(comune8931);
		Comune comune8932 = new Comune();  // BROGNATURO
		comune8932.setCodice(8932);
		comune8932.setNome("BROGNATURO");
		listaComuni103.add(comune8932);
		Comune comune8933 = new Comune();  // CAPISTRANO
		comune8933.setCodice(8933);
		comune8933.setNome("CAPISTRANO");
		listaComuni103.add(comune8933);
		Comune comune8934 = new Comune();  // CESSANITI
		comune8934.setCodice(8934);
		comune8934.setNome("CESSANITI");
		listaComuni103.add(comune8934);
		Comune comune8935 = new Comune();  // DASA'
		comune8935.setCodice(8935);
		comune8935.setNome("DASA'");
		listaComuni103.add(comune8935);
		Comune comune8936 = new Comune();  // DINAMI
		comune8936.setCodice(8936);
		comune8936.setNome("DINAMI");
		listaComuni103.add(comune8936);
		Comune comune8937 = new Comune();  // DRAPIA
		comune8937.setCodice(8937);
		comune8937.setNome("DRAPIA");
		listaComuni103.add(comune8937);
		Comune comune8938 = new Comune();  // FABRIZIA
		comune8938.setCodice(8938);
		comune8938.setNome("FABRIZIA");
		listaComuni103.add(comune8938);
		Comune comune8939 = new Comune();  // FILADELFIA
		comune8939.setCodice(8939);
		comune8939.setNome("FILADELFIA");
		listaComuni103.add(comune8939);
		Comune comune8940 = new Comune();  // FILANDARI
		comune8940.setCodice(8940);
		comune8940.setNome("FILANDARI");
		listaComuni103.add(comune8940);
		Comune comune8941 = new Comune();  // FILOGASO
		comune8941.setCodice(8941);
		comune8941.setNome("FILOGASO");
		listaComuni103.add(comune8941);
		Comune comune8942 = new Comune();  // FRANCAVILLA ANGITOLA
		comune8942.setCodice(8942);
		comune8942.setNome("FRANCAVILLA ANGITOLA");
		listaComuni103.add(comune8942);
		Comune comune8943 = new Comune();  // FRANCICA
		comune8943.setCodice(8943);
		comune8943.setNome("FRANCICA");
		listaComuni103.add(comune8943);


		/*
		 * SICILIA
		 */

		////// AGRIGENTO
		Comune comune9065 = new Comune();  // AGRIGENTO
		comune9065.setCodice(9065);
		comune9065.setNome("AGRIGENTO");
		listaComuni1.add(comune9065);
		Comune comune9066 = new Comune();  // ALESSANDRIA DELLA ROCCA
		comune9066.setCodice(9066);
		comune9066.setNome("ALESSANDRIA DELLA ROCCA");
		listaComuni1.add(comune9066);
		Comune comune9067 = new Comune();  // ARAGONA
		comune9067.setCodice(9067);
		comune9067.setNome("ARAGONA");
		listaComuni1.add(comune9067);
		Comune comune9068 = new Comune();  // BIVONA
		comune9068.setCodice(9068);
		comune9068.setNome("BIVONA");
		listaComuni1.add(comune9068);
		Comune comune9069 = new Comune();  // BURGIO
		comune9069.setCodice(9069);
		comune9069.setNome("BURGIO");
		listaComuni1.add(comune9069);
		Comune comune9070 = new Comune();  // CALAMONACI
		comune9070.setCodice(9070);
		comune9070.setNome("CALAMONACI");
		listaComuni1.add(comune9070);
		Comune comune9071 = new Comune();  // CALTABELLOTTA
		comune9071.setCodice(9071);
		comune9071.setNome("CALTABELLOTTA");
		listaComuni1.add(comune9071);
		Comune comune9072 = new Comune();  // CAMASTRA
		comune9072.setCodice(9072);
		comune9072.setNome("CAMASTRA");
		listaComuni1.add(comune9072);
		Comune comune9073 = new Comune();  // CAMMARATA
		comune9073.setCodice(9073);
		comune9073.setNome("CAMMARATA");
		listaComuni1.add(comune9073);
		Comune comune9074 = new Comune();  // CAMPOBELLO DI LICATA
		comune9074.setCodice(9074);
		comune9074.setNome("CAMPOBELLO DI LICATA");
		listaComuni1.add(comune9074);
		Comune comune9075 = new Comune();  // CANICATTI'
		comune9075.setCodice(9075);
		comune9075.setNome("CANICATTI'");
		listaComuni1.add(comune9075);
		Comune comune9076 = new Comune();  // CASTELTERMINI
		comune9076.setCodice(9076);
		comune9076.setNome("CASTELTERMINI");
		listaComuni1.add(comune9076);
		Comune comune9077 = new Comune();  // CASTROFILIPPO
		comune9077.setCodice(9077);
		comune9077.setNome("CASTROFILIPPO");
		listaComuni1.add(comune9077);
		Comune comune9078 = new Comune();  // CATTOLICA ERACLEA
		comune9078.setCodice(9078);
		comune9078.setNome("CATTOLICA ERACLEA");
		listaComuni1.add(comune9078);
		Comune comune9079 = new Comune();  // CIANCIANA
		comune9079.setCodice(9079);
		comune9079.setNome("CIANCIANA");
		listaComuni1.add(comune9079);

		////// CALTANISSETTA
		Comune comune9110 = new Comune();  // ACQUAVIVA PLATANI
		comune9110.setCodice(9110);
		comune9110.setNome("ACQUAVIVA PLATANI");
		listaComuni18.add(comune9110);
		Comune comune9111 = new Comune();  // BOMPENSIERE
		comune9111.setCodice(9111);
		comune9111.setNome("BOMPENSIERE");
		listaComuni18.add(comune9111);
		Comune comune9112 = new Comune();  // BUTERA
		comune9112.setCodice(9112);
		comune9112.setNome("BUTERA");
		listaComuni18.add(comune9112);
		Comune comune9109 = new Comune();  // CALTANISSETTA
		comune9109.setCodice(9109);
		comune9109.setNome("CALTANISSETTA");
		listaComuni18.add(comune9109);
		Comune comune9113 = new Comune();  // CAMPOFRANCO
		comune9113.setCodice(9113);
		comune9113.setNome("CAMPOFRANCO");
		listaComuni18.add(comune9113);
		Comune comune9114 = new Comune();  // DELIA
		comune9114.setCodice(9114);
		comune9114.setNome("DELIA");
		listaComuni18.add(comune9114);
		Comune comune9115 = new Comune();  // GELA
		comune9115.setCodice(9115);
		comune9115.setNome("GELA");
		listaComuni18.add(comune9115);
		Comune comune9116 = new Comune();  // MARIANOPOLI
		comune9116.setCodice(9116);
		comune9116.setNome("MARIANOPOLI");
		listaComuni18.add(comune9116);
		Comune comune9117 = new Comune();  // MAZZARINO
		comune9117.setCodice(9117);
		comune9117.setNome("MAZZARINO");
		listaComuni18.add(comune9117);
		Comune comune9118 = new Comune();  // MILENA
		comune9118.setCodice(9118);
		comune9118.setNome("MILENA");
		listaComuni18.add(comune9118);
		Comune comune9119 = new Comune();  // MONTEDORO
		comune9119.setCodice(9119);
		comune9119.setNome("MONTEDORO");
		listaComuni18.add(comune9119);
		Comune comune9120 = new Comune();  // MUSSOMELI
		comune9120.setCodice(9120);
		comune9120.setNome("MUSSOMELI");
		listaComuni18.add(comune9120);
		Comune comune9121 = new Comune();  // NISCEMI
		comune9121.setCodice(9121);
		comune9121.setNome("NISCEMI");
		listaComuni18.add(comune9121);
		Comune comune9122 = new Comune();  // RESUTTANO
		comune9122.setCodice(9122);
		comune9122.setNome("RESUTTANO");
		listaComuni18.add(comune9122);
		Comune comune9123 = new Comune();  // RIESI
		comune9123.setCodice(9123);
		comune9123.setNome("RIESI");
		listaComuni18.add(comune9123);

		////// CATANIA
		Comune comune9134 = new Comune();  // ACI BONACCORSI
		comune9134.setCodice(9134);
		comune9134.setNome("ACI BONACCORSI");
		listaComuni21.add(comune9134);
		Comune comune9135 = new Comune();  // ACI CASTELLO
		comune9135.setCodice(9135);
		comune9135.setNome("ACI CASTELLO");
		listaComuni21.add(comune9135);
		Comune comune9136 = new Comune();  // ACI CATENA
		comune9136.setCodice(9136);
		comune9136.setNome("ACI CATENA");
		listaComuni21.add(comune9136);
		Comune comune9137 = new Comune();  // ACI S.ANTONIO
		comune9137.setCodice(9137);
		comune9137.setNome("ACI S.ANTONIO");
		listaComuni21.add(comune9137);
		Comune comune9138 = new Comune();  // ACIREALE
		comune9138.setCodice(9138);
		comune9138.setNome("ACIREALE");
		listaComuni21.add(comune9138);
		Comune comune9139 = new Comune();  // ADRANO
		comune9139.setCodice(9139);
		comune9139.setNome("ADRANO");
		listaComuni21.add(comune9139);
		Comune comune9140 = new Comune();  // BELPASSO
		comune9140.setCodice(9140);
		comune9140.setNome("BELPASSO");
		listaComuni21.add(comune9140);
		Comune comune9141 = new Comune();  // BIANCAVILLA
		comune9141.setCodice(9141);
		comune9141.setNome("BIANCAVILLA");
		listaComuni21.add(comune9141);
		Comune comune9142 = new Comune();  // BRONTE
		comune9142.setCodice(9142);
		comune9142.setNome("BRONTE");
		listaComuni21.add(comune9142);
		Comune comune9143 = new Comune();  // CALATABIANO
		comune9143.setCodice(9143);
		comune9143.setNome("CALATABIANO");
		listaComuni21.add(comune9143);
		Comune comune9144 = new Comune();  // CALTAGIRONE
		comune9144.setCodice(9144);
		comune9144.setNome("CALTAGIRONE");
		listaComuni21.add(comune9144);
		Comune comune9145 = new Comune();  // CAMPOROTONDO ETNEO
		comune9145.setCodice(9145);
		comune9145.setNome("CAMPOROTONDO ETNEO");
		listaComuni21.add(comune9145);
		Comune comune9146 = new Comune();  // CASTEL DI IUDICA
		comune9146.setCodice(9146);
		comune9146.setNome("CASTEL DI IUDICA");
		listaComuni21.add(comune9146);
		Comune comune9147 = new Comune();  // CASTIGLIONE DI SICILIA
		comune9147.setCodice(9147);
		comune9147.setNome("CASTIGLIONE DI SICILIA");
		listaComuni21.add(comune9147);
		Comune comune9133 = new Comune();  // CATANIA
		comune9133.setCodice(9133);
		comune9133.setNome("CATANIA");
		listaComuni21.add(comune9133);

		////// ENNA
		Comune comune9196 = new Comune();  // AGIRA
		comune9196.setCodice(9196);
		comune9196.setNome("AGIRA");
		listaComuni29.add(comune9196);
		Comune comune9197 = new Comune();  // AIDONE
		comune9197.setCodice(9197);
		comune9197.setNome("AIDONE");
		listaComuni29.add(comune9197);
		Comune comune9198 = new Comune();  // ASSORO
		comune9198.setCodice(9198);
		comune9198.setNome("ASSORO");
		listaComuni29.add(comune9198);
		Comune comune9199 = new Comune();  // BARRAFRANCA
		comune9199.setCodice(9199);
		comune9199.setNome("BARRAFRANCA");
		listaComuni29.add(comune9199);
		Comune comune9200 = new Comune();  // CALASCIBETTA
		comune9200.setCodice(9200);
		comune9200.setNome("CALASCIBETTA");
		listaComuni29.add(comune9200);
		Comune comune9201 = new Comune();  // CATENANUOVA
		comune9201.setCodice(9201);
		comune9201.setNome("CATENANUOVA");
		listaComuni29.add(comune9201);
		Comune comune9202 = new Comune();  // CENTURIPE
		comune9202.setCodice(9202);
		comune9202.setNome("CENTURIPE");
		listaComuni29.add(comune9202);
		Comune comune9203 = new Comune();  // CERAMI
		comune9203.setCodice(9203);
		comune9203.setNome("CERAMI");
		listaComuni29.add(comune9203);
		Comune comune9195 = new Comune();  // ENNA
		comune9195.setCodice(9195);
		comune9195.setNome("ENNA");
		listaComuni29.add(comune9195);
		Comune comune9204 = new Comune();  // GAGLIANO CASTELFERRATO
		comune9204.setCodice(9204);
		comune9204.setNome("GAGLIANO CASTELFERRATO");
		listaComuni29.add(comune9204);
		Comune comune9205 = new Comune();  // LEONFORTE
		comune9205.setCodice(9205);
		comune9205.setNome("LEONFORTE");
		listaComuni29.add(comune9205);
		Comune comune9206 = new Comune();  // NICOSIA
		comune9206.setCodice(9206);
		comune9206.setNome("NICOSIA");
		listaComuni29.add(comune9206);
		Comune comune9207 = new Comune();  // NISSORIA
		comune9207.setCodice(9207);
		comune9207.setNome("NISSORIA");
		listaComuni29.add(comune9207);
		Comune comune9208 = new Comune();  // PIAZZA ARMERINA
		comune9208.setCodice(9208);
		comune9208.setNome("PIAZZA ARMERINA");
		listaComuni29.add(comune9208);
		Comune comune9209 = new Comune();  // PIETRAPERZIA
		comune9209.setCodice(9209);
		comune9209.setNome("PIETRAPERZIA");
		listaComuni29.add(comune9209);

		////// MESSINA
		Comune comune9321 = new Comune();  // ACQUEDOLCI
		comune9321.setCodice(9321);
		comune9321.setNome("ACQUEDOLCI");
		listaComuni53.add(comune9321);
		Comune comune9217 = new Comune();  // ALCARA LI FUSI
		comune9217.setCodice(9217);
		comune9217.setNome("ALCARA LI FUSI");
		listaComuni53.add(comune9217);
		Comune comune9218 = new Comune();  // ALI'
		comune9218.setCodice(9218);
		comune9218.setNome("ALI'");
		listaComuni53.add(comune9218);
		Comune comune9219 = new Comune();  // ALI' TERME
		comune9219.setCodice(9219);
		comune9219.setNome("ALI' TERME");
		listaComuni53.add(comune9219);
		Comune comune9220 = new Comune();  // ANTILLO
		comune9220.setCodice(9220);
		comune9220.setNome("ANTILLO");
		listaComuni53.add(comune9220);
		Comune comune9221 = new Comune();  // BARCELLONA POZZO DI GOTTO
		comune9221.setCodice(9221);
		comune9221.setNome("BARCELLONA POZZO DI GOTTO");
		listaComuni53.add(comune9221);
		Comune comune9222 = new Comune();  // BASICO'
		comune9222.setCodice(9222);
		comune9222.setNome("BASICO'");
		listaComuni53.add(comune9222);
		Comune comune9223 = new Comune();  // BROLO
		comune9223.setCodice(9223);
		comune9223.setNome("BROLO");
		listaComuni53.add(comune9223);
		Comune comune9322 = new Comune();  // CALVARUSO ORA VILLAFRANCA TIRRENA
		comune9322.setCodice(9322);
		comune9322.setNome("CALVARUSO ORA VILLAFRANCA TIRRENA");
		listaComuni53.add(comune9322);
		Comune comune9224 = new Comune();  // CAPIZZI
		comune9224.setCodice(9224);
		comune9224.setNome("CAPIZZI");
		listaComuni53.add(comune9224);
		Comune comune9225 = new Comune();  // CAPO D'ORLANDO
		comune9225.setCodice(9225);
		comune9225.setNome("CAPO D'ORLANDO");
		listaComuni53.add(comune9225);
		Comune comune9226 = new Comune();  // CAPRI LEONE
		comune9226.setCodice(9226);
		comune9226.setNome("CAPRI LEONE");
		listaComuni53.add(comune9226);
		Comune comune9227 = new Comune();  // CARONIA
		comune9227.setCodice(9227);
		comune9227.setNome("CARONIA");
		listaComuni53.add(comune9227);
		Comune comune9228 = new Comune();  // CASALVECCHIO SICULO
		comune9228.setCodice(9228);
		comune9228.setNome("CASALVECCHIO SICULO");
		listaComuni53.add(comune9228);
		Comune comune9229 = new Comune();  // CASTEL DI LUCIO
		comune9229.setCodice(9229);
		comune9229.setNome("CASTEL DI LUCIO");
		listaComuni53.add(comune9229);

		////// PALERMO
		Comune comune8979 = new Comune();  // ALIA
		comune8979.setCodice(8979);
		comune8979.setNome("ALIA");
		listaComuni61.add(comune8979);
		Comune comune8980 = new Comune();  // ALIMENA
		comune8980.setCodice(8980);
		comune8980.setNome("ALIMENA");
		listaComuni61.add(comune8980);
		Comune comune8981 = new Comune();  // ALIMINUSA
		comune8981.setCodice(8981);
		comune8981.setNome("ALIMINUSA");
		listaComuni61.add(comune8981);
		Comune comune8982 = new Comune();  // ALTAVILLA MILICIA
		comune8982.setCodice(8982);
		comune8982.setNome("ALTAVILLA MILICIA");
		listaComuni61.add(comune8982);
		Comune comune8983 = new Comune();  // ALTOFONTE
		comune8983.setCodice(8983);
		comune8983.setNome("ALTOFONTE");
		listaComuni61.add(comune8983);
		Comune comune8984 = new Comune();  // BAGHERIA
		comune8984.setCodice(8984);
		comune8984.setNome("BAGHERIA");
		listaComuni61.add(comune8984);
		Comune comune8985 = new Comune();  // BALESTRATE
		comune8985.setCodice(8985);
		comune8985.setNome("BALESTRATE");
		listaComuni61.add(comune8985);
		Comune comune8986 = new Comune();  // BAUCINA
		comune8986.setCodice(8986);
		comune8986.setNome("BAUCINA");
		listaComuni61.add(comune8986);
		Comune comune8987 = new Comune();  // BELMONTE MEZZAGNO
		comune8987.setCodice(8987);
		comune8987.setNome("BELMONTE MEZZAGNO");
		listaComuni61.add(comune8987);
		Comune comune8988 = new Comune();  // BISACQUINO
		comune8988.setCodice(8988);
		comune8988.setNome("BISACQUINO");
		listaComuni61.add(comune8988);
		Comune comune9060 = new Comune();  // BLUFI
		comune9060.setCodice(9060);
		comune9060.setNome("BLUFI");
		listaComuni61.add(comune9060);
		Comune comune8989 = new Comune();  // BOLOGNETTA
		comune8989.setCodice(8989);
		comune8989.setNome("BOLOGNETTA");
		listaComuni61.add(comune8989);
		Comune comune8990 = new Comune();  // BOMPIETRO
		comune8990.setCodice(8990);
		comune8990.setNome("BOMPIETRO");
		listaComuni61.add(comune8990);
		Comune comune8991 = new Comune();  // BORGETTO
		comune8991.setCodice(8991);
		comune8991.setNome("BORGETTO");
		listaComuni61.add(comune8991);
		Comune comune8992 = new Comune();  // CACCAMO
		comune8992.setCodice(8992);
		comune8992.setNome("CACCAMO");
		listaComuni61.add(comune8992);

		////// RAGUSA
		Comune comune9334 = new Comune();  // ACATE
		comune9334.setCodice(9334);
		comune9334.setNome("ACATE");
		listaComuni74.add(comune9334);
		Comune comune9335 = new Comune();  // CHIARAMONTE GULFI
		comune9335.setCodice(9335);
		comune9335.setNome("CHIARAMONTE GULFI");
		listaComuni74.add(comune9335);
		Comune comune9336 = new Comune();  // COMISO
		comune9336.setCodice(9336);
		comune9336.setNome("COMISO");
		listaComuni74.add(comune9336);
		Comune comune9337 = new Comune();  // GIARRATANA
		comune9337.setCodice(9337);
		comune9337.setNome("GIARRATANA");
		listaComuni74.add(comune9337);
		Comune comune9338 = new Comune();  // ISPICA
		comune9338.setCodice(9338);
		comune9338.setNome("ISPICA");
		listaComuni74.add(comune9338);
		Comune comune9339 = new Comune();  // MODICA
		comune9339.setCodice(9339);
		comune9339.setNome("MODICA");
		listaComuni74.add(comune9339);
		Comune comune9340 = new Comune();  // MONTEROSSO ALMO
		comune9340.setCodice(9340);
		comune9340.setNome("MONTEROSSO ALMO");
		listaComuni74.add(comune9340);
		Comune comune9341 = new Comune();  // POZZALLO
		comune9341.setCodice(9341);
		comune9341.setNome("POZZALLO");
		listaComuni74.add(comune9341);
		Comune comune9333 = new Comune();  // RAGUSA
		comune9333.setCodice(9333);
		comune9333.setNome("RAGUSA");
		listaComuni74.add(comune9333);
		Comune comune9343 = new Comune();  // SCICLI
		comune9343.setCodice(9343);
		comune9343.setNome("SCICLI");
		listaComuni74.add(comune9343);
		Comune comune9342 = new Comune();  // S.CROCE CAMERINA
		comune9342.setCodice(9342);
		comune9342.setNome("S.CROCE CAMERINA");
		listaComuni74.add(comune9342);
		Comune comune9344 = new Comune();  // VITTORIA
		comune9344.setCodice(9344);
		comune9344.setNome("VITTORIA");
		listaComuni74.add(comune9344);

		////// SIRACUSA
		Comune comune9348 = new Comune();  // AUGUSTA
		comune9348.setCodice(9348);
		comune9348.setNome("AUGUSTA");
		listaComuni86.add(comune9348);
		Comune comune9349 = new Comune();  // AVOLA
		comune9349.setCodice(9349);
		comune9349.setNome("AVOLA");
		listaComuni86.add(comune9349);
		Comune comune9350 = new Comune();  // BUCCHERI
		comune9350.setCodice(9350);
		comune9350.setNome("BUCCHERI");
		listaComuni86.add(comune9350);
		Comune comune9351 = new Comune();  // BUSCEMI
		comune9351.setCodice(9351);
		comune9351.setNome("BUSCEMI");
		listaComuni86.add(comune9351);
		Comune comune9352 = new Comune();  // CANICATTINI BAGNI
		comune9352.setCodice(9352);
		comune9352.setNome("CANICATTINI BAGNI");
		listaComuni86.add(comune9352);
		Comune comune9353 = new Comune();  // CARLENTINI
		comune9353.setCodice(9353);
		comune9353.setNome("CARLENTINI");
		listaComuni86.add(comune9353);
		Comune comune9354 = new Comune();  // CASSARO
		comune9354.setCodice(9354);
		comune9354.setNome("CASSARO");
		listaComuni86.add(comune9354);
		Comune comune9355 = new Comune();  // FERLA
		comune9355.setCodice(9355);
		comune9355.setNome("FERLA");
		listaComuni86.add(comune9355);
		Comune comune9356 = new Comune();  // FLORIDIA
		comune9356.setCodice(9356);
		comune9356.setNome("FLORIDIA");
		listaComuni86.add(comune9356);
		Comune comune9357 = new Comune();  // FRANCOFONTE
		comune9357.setCodice(9357);
		comune9357.setNome("FRANCOFONTE");
		listaComuni86.add(comune9357);
		Comune comune9358 = new Comune();  // LENTINI
		comune9358.setCodice(9358);
		comune9358.setNome("LENTINI");
		listaComuni86.add(comune9358);
		Comune comune9359 = new Comune();  // MELILLI
		comune9359.setCodice(9359);
		comune9359.setNome("MELILLI");
		listaComuni86.add(comune9359);
		Comune comune9360 = new Comune();  // NOTO
		comune9360.setCodice(9360);
		comune9360.setNome("NOTO");
		listaComuni86.add(comune9360);
		Comune comune9361 = new Comune();  // PACHINO
		comune9361.setCodice(9361);
		comune9361.setNome("PACHINO");
		listaComuni86.add(comune9361);
		Comune comune9362 = new Comune();  // PALAZZOLO ACREIDE
		comune9362.setCodice(9362);
		comune9362.setNome("PALAZZOLO ACREIDE");
		listaComuni86.add(comune9362);

		////// TRAPANI
		Comune comune9369 = new Comune();  // ALCAMO
		comune9369.setCodice(9369);
		comune9369.setNome("ALCAMO");
		listaComuni92.add(comune9369);
		Comune comune9370 = new Comune();  // BUSETO PALIZZOLO
		comune9370.setCodice(9370);
		comune9370.setNome("BUSETO PALIZZOLO");
		listaComuni92.add(comune9370);
		Comune comune9396 = new Comune();  // CALATAFIMI SEGESTA
		comune9396.setCodice(9396);
		comune9396.setNome("CALATAFIMI SEGESTA");
		listaComuni92.add(comune9396);
		Comune comune9372 = new Comune();  // CAMPOBELLO DI MAZARA
		comune9372.setCodice(9372);
		comune9372.setNome("CAMPOBELLO DI MAZARA");
		listaComuni92.add(comune9372);
		Comune comune9373 = new Comune();  // CASTELLAMMARE DEL GOLFO
		comune9373.setCodice(9373);
		comune9373.setNome("CASTELLAMMARE DEL GOLFO");
		listaComuni92.add(comune9373);
		Comune comune9374 = new Comune();  // CASTELVETRANO
		comune9374.setCodice(9374);
		comune9374.setNome("CASTELVETRANO");
		listaComuni92.add(comune9374);
		Comune comune9376 = new Comune();  // CUSTONACI
		comune9376.setCodice(9376);
		comune9376.setNome("CUSTONACI");
		listaComuni92.add(comune9376);
		Comune comune9377 = new Comune();  // ERICE
		comune9377.setCodice(9377);
		comune9377.setNome("ERICE");
		listaComuni92.add(comune9377);
		Comune comune9378 = new Comune();  // FAVIGNANA
		comune9378.setCodice(9378);
		comune9378.setNome("FAVIGNANA");
		listaComuni92.add(comune9378);
		Comune comune9379 = new Comune();  // GIBELLINA
		comune9379.setCodice(9379);
		comune9379.setNome("GIBELLINA");
		listaComuni92.add(comune9379);
		Comune comune9392 = new Comune();  // GUARRATO ORA TRAPANI
		comune9392.setCodice(9392);
		comune9392.setNome("GUARRATO ORA TRAPANI");
		listaComuni92.add(comune9392);
		Comune comune9380 = new Comune();  // MARSALA
		comune9380.setCodice(9380);
		comune9380.setNome("MARSALA");
		listaComuni92.add(comune9380);
		Comune comune9381 = new Comune();  // MAZARA DEL VALLO
		comune9381.setCodice(9381);
		comune9381.setNome("MAZARA DEL VALLO");
		listaComuni92.add(comune9381);
		Comune comune9382 = new Comune();  // PACECO
		comune9382.setCodice(9382);
		comune9382.setNome("PACECO");
		listaComuni92.add(comune9382);
		Comune comune9383 = new Comune();  // PANTELLERIA
		comune9383.setCodice(9383);
		comune9383.setNome("PANTELLERIA");
		listaComuni92.add(comune9383);


		/*
		 * SARDEGNA
		 */

		////// CAGLIARI
		Comune comune9399 = new Comune();  // ARMUNGIA
		comune9399.setCodice(9399);
		comune9399.setNome("ARMUNGIA");
		listaComuni17.add(comune9399);
		Comune comune9400 = new Comune();  // ASSEMINI
		comune9400.setCodice(9400);
		comune9400.setNome("ASSEMINI");
		listaComuni17.add(comune9400);
		Comune comune9401 = new Comune();  // BALLAO
		comune9401.setCodice(9401);
		comune9401.setNome("BALLAO");
		listaComuni17.add(comune9401);
		Comune comune9402 = new Comune();  // BARRALI
		comune9402.setCodice(9402);
		comune9402.setNome("BARRALI");
		listaComuni17.add(comune9402);
		Comune comune9405 = new Comune();  // BURCEI
		comune9405.setCodice(9405);
		comune9405.setNome("BURCEI");
		listaComuni17.add(comune9405);
		Comune comune9397 = new Comune();  // CAGLIARI
		comune9397.setCodice(9397);
		comune9397.setNome("CAGLIARI");
		listaComuni17.add(comune9397);
		Comune comune9407 = new Comune();  // CAPOTERRA
		comune9407.setCodice(9407);
		comune9407.setNome("CAPOTERRA");
		listaComuni17.add(comune9407);
		Comune comune9509 = new Comune();  // CASTIADAS
		comune9509.setCodice(9509);
		comune9509.setNome("CASTIADAS");
		listaComuni17.add(comune9509);
		Comune comune9411 = new Comune();  // DECIMOMANNU
		comune9411.setCodice(9411);
		comune9411.setNome("DECIMOMANNU");
		listaComuni17.add(comune9411);
		Comune comune9412 = new Comune();  // DECIMOPUTZU
		comune9412.setCodice(9412);
		comune9412.setNome("DECIMOPUTZU");
		listaComuni17.add(comune9412);
		Comune comune9413 = new Comune();  // DOLIANOVA
		comune9413.setCodice(9413);
		comune9413.setNome("DOLIANOVA");
		listaComuni17.add(comune9413);
		Comune comune9414 = new Comune();  // DOMUS DE MARIA
		comune9414.setCodice(9414);
		comune9414.setNome("DOMUS DE MARIA");
		listaComuni17.add(comune9414);
		Comune comune9416 = new Comune();  // DONORI
		comune9416.setCodice(9416);
		comune9416.setNome("DONORI");
		listaComuni17.add(comune9416);
		Comune comune9511 = new Comune();  // ELMAS
		comune9511.setCodice(9511);
		comune9511.setNome("ELMAS");
		listaComuni17.add(comune9511);
		Comune comune9420 = new Comune();  // GESICO
		comune9420.setCodice(9420);
		comune9420.setNome("GESICO");
		listaComuni17.add(comune9420);

		////// NUORO
		Comune comune9616 = new Comune();  // ARITZO
		comune9616.setCodice(9616);
		comune9616.setNome("ARITZO");
		listaComuni58.add(comune9616);
		Comune comune9618 = new Comune();  // ATZARA
		comune9618.setCodice(9618);
		comune9618.setNome("ATZARA");
		listaComuni58.add(comune9618);
		Comune comune9619 = new Comune();  // AUSTIS
		comune9619.setCodice(9619);
		comune9619.setNome("AUSTIS");
		listaComuni58.add(comune9619);
		Comune comune9622 = new Comune();  // BELVI'
		comune9622.setCodice(9622);
		comune9622.setNome("BELVI'");
		listaComuni58.add(comune9622);
		Comune comune9623 = new Comune();  // BIRORI
		comune9623.setCodice(9623);
		comune9623.setNome("BIRORI");
		listaComuni58.add(comune9623);
		Comune comune9624 = new Comune();  // BITTI
		comune9624.setCodice(9624);
		comune9624.setNome("BITTI");
		listaComuni58.add(comune9624);
		Comune comune9625 = new Comune();  // BOLOTANA
		comune9625.setCodice(9625);
		comune9625.setNome("BOLOTANA");
		listaComuni58.add(comune9625);
		Comune comune9626 = new Comune();  // BORORE
		comune9626.setCodice(9626);
		comune9626.setNome("BORORE");
		listaComuni58.add(comune9626);
		Comune comune9627 = new Comune();  // BORTIGALI
		comune9627.setCodice(9627);
		comune9627.setNome("BORTIGALI");
		listaComuni58.add(comune9627);
		Comune comune9628 = new Comune();  // BOSA
		comune9628.setCodice(9628);
		comune9628.setNome("BOSA");
		listaComuni58.add(comune9628);
		Comune comune9630 = new Comune();  // DESULO
		comune9630.setCodice(9630);
		comune9630.setNome("DESULO");
		listaComuni58.add(comune9630);
		Comune comune9631 = new Comune();  // DORGALI
		comune9631.setCodice(9631);
		comune9631.setNome("DORGALI");
		listaComuni58.add(comune9631);
		Comune comune9632 = new Comune();  // DUALCHI
		comune9632.setCodice(9632);
		comune9632.setNome("DUALCHI");
		listaComuni58.add(comune9632);
		Comune comune9634 = new Comune();  // ESCALAPLANO
		comune9634.setCodice(9634);
		comune9634.setNome("ESCALAPLANO");
		listaComuni58.add(comune9634);
		Comune comune9635 = new Comune();  // ESCOLCA
		comune9635.setCodice(9635);
		comune9635.setNome("ESCOLCA");
		listaComuni58.add(comune9635);

		////// ORISTANO
		Comune comune9518 = new Comune();  // ABBASANTA
		comune9518.setCodice(9518);
		comune9518.setNome("ABBASANTA");
		listaComuni59.add(comune9518);
		Comune comune9519 = new Comune();  // AIDOMAGGIORE
		comune9519.setCodice(9519);
		comune9519.setNome("AIDOMAGGIORE");
		listaComuni59.add(comune9519);
		Comune comune9597 = new Comune();  // ALBAGIARA
		comune9597.setCodice(9597);
		comune9597.setNome("ALBAGIARA");
		listaComuni59.add(comune9597);
		Comune comune9520 = new Comune();  // ALES
		comune9520.setCodice(9520);
		comune9520.setNome("ALES");
		listaComuni59.add(comune9520);
		Comune comune9521 = new Comune();  // ALLAI
		comune9521.setCodice(9521);
		comune9521.setNome("ALLAI");
		listaComuni59.add(comune9521);
		Comune comune9522 = new Comune();  // ARBOREA
		comune9522.setCodice(9522);
		comune9522.setNome("ARBOREA");
		listaComuni59.add(comune9522);
		Comune comune9523 = new Comune();  // ARDAULI
		comune9523.setCodice(9523);
		comune9523.setNome("ARDAULI");
		listaComuni59.add(comune9523);
		Comune comune9524 = new Comune();  // ASSOLO
		comune9524.setCodice(9524);
		comune9524.setNome("ASSOLO");
		listaComuni59.add(comune9524);
		Comune comune9525 = new Comune();  // ASUNI
		comune9525.setCodice(9525);
		comune9525.setNome("ASUNI");
		listaComuni59.add(comune9525);
		Comune comune9526 = new Comune();  // BARADILI
		comune9526.setCodice(9526);
		comune9526.setNome("BARADILI");
		listaComuni59.add(comune9526);
		Comune comune9527 = new Comune();  // BARATILI S.PIETRO
		comune9527.setCodice(9527);
		comune9527.setNome("BARATILI S.PIETRO");
		listaComuni59.add(comune9527);
		Comune comune9528 = new Comune();  // BARESSA
		comune9528.setCodice(9528);
		comune9528.setNome("BARESSA");
		listaComuni59.add(comune9528);
		Comune comune9529 = new Comune();  // BAULADU
		comune9529.setCodice(9529);
		comune9529.setNome("BAULADU");
		listaComuni59.add(comune9529);
		Comune comune9530 = new Comune();  // BIDONI
		comune9530.setCodice(9530);
		comune9530.setNome("BIDONI");
		listaComuni59.add(comune9530);
		Comune comune9531 = new Comune();  // BONARCADO
		comune9531.setCodice(9531);
		comune9531.setNome("BONARCADO");
		listaComuni59.add(comune9531);

		////// SASSARI
		Comune comune9720 = new Comune();  // ALGHERO
		comune9720.setCodice(9720);
		comune9720.setNome("ALGHERO");
		listaComuni83.add(comune9720);
		Comune comune9721 = new Comune();  // ANELA
		comune9721.setCodice(9721);
		comune9721.setNome("ANELA");
		listaComuni83.add(comune9721);
		Comune comune9722 = new Comune();  // ARDARA
		comune9722.setCodice(9722);
		comune9722.setNome("ARDARA");
		listaComuni83.add(comune9722);
		Comune comune9724 = new Comune();  // BANARI
		comune9724.setCodice(9724);
		comune9724.setNome("BANARI");
		listaComuni83.add(comune9724);
		Comune comune9725 = new Comune();  // BENETUTTI
		comune9725.setCodice(9725);
		comune9725.setNome("BENETUTTI");
		listaComuni83.add(comune9725);
		Comune comune9727 = new Comune();  // BESSUDE
		comune9727.setCodice(9727);
		comune9727.setNome("BESSUDE");
		listaComuni83.add(comune9727);
		Comune comune9728 = new Comune();  // BONNANARO
		comune9728.setCodice(9728);
		comune9728.setNome("BONNANARO");
		listaComuni83.add(comune9728);
		Comune comune9729 = new Comune();  // BONO
		comune9729.setCodice(9729);
		comune9729.setNome("BONO");
		listaComuni83.add(comune9729);
		Comune comune9730 = new Comune();  // BONORVA
		comune9730.setCodice(9730);
		comune9730.setNome("BONORVA");
		listaComuni83.add(comune9730);
		Comune comune9732 = new Comune();  // BORUTTA
		comune9732.setCodice(9732);
		comune9732.setNome("BORUTTA");
		listaComuni83.add(comune9732);
		Comune comune9733 = new Comune();  // BOTTIDDA
		comune9733.setCodice(9733);
		comune9733.setNome("BOTTIDDA");
		listaComuni83.add(comune9733);
		Comune comune9735 = new Comune();  // BULTEI
		comune9735.setCodice(9735);
		comune9735.setNome("BULTEI");
		listaComuni83.add(comune9735);
		Comune comune9736 = new Comune();  // BULZI
		comune9736.setCodice(9736);
		comune9736.setNome("BULZI");
		listaComuni83.add(comune9736);
		Comune comune9737 = new Comune();  // BURGOS
		comune9737.setCodice(9737);
		comune9737.setNome("BURGOS");
		listaComuni83.add(comune9737);
		Comune comune9739 = new Comune();  // CARGEGHE
		comune9739.setCodice(9739);
		comune9739.setNome("CARGEGHE");
		listaComuni83.add(comune9739);



		/*********************************************************************************************************
		 * Aggiungo alla mappa dei comuni
		 ********************************************************************************************************/

		mappaComuniPerProvincia = new LinkedHashMap<Integer, ArrayList<Comune>>();
		
		/* 
		 * PIEMONTE
		 */
		mappaComuniPerProvincia.put(2 , listaComuni2);
		mappaComuniPerProvincia.put(6 , listaComuni6);
		mappaComuniPerProvincia.put(12 , listaComuni12);
		mappaComuniPerProvincia.put(28 , listaComuni28);
		mappaComuniPerProvincia.put(57 , listaComuni57);
		mappaComuniPerProvincia.put(91 , listaComuni91);
		mappaComuniPerProvincia.put(100 , listaComuni100);
		mappaComuniPerProvincia.put(101 , listaComuni101);


		/* 
		 * VALLE D'AOSTA
		 */
		mappaComuniPerProvincia.put(97 , listaComuni97);


		/* 
		 * LOMBARDIA
		 */
		mappaComuniPerProvincia.put(11 , listaComuni11);
		mappaComuniPerProvincia.put(15 , listaComuni15);
		mappaComuniPerProvincia.put(24 , listaComuni24);
		mappaComuniPerProvincia.put(26 , listaComuni26);
		mappaComuniPerProvincia.put(45 , listaComuni45);
		mappaComuniPerProvincia.put(47 , listaComuni47);
		mappaComuniPerProvincia.put(50 , listaComuni50);
		mappaComuniPerProvincia.put(54 , listaComuni54);
		mappaComuniPerProvincia.put(63 , listaComuni63);
		mappaComuniPerProvincia.put(87 , listaComuni87);
		mappaComuniPerProvincia.put(98 , listaComuni98);


		/* 
		 * TRENTINO ALTO ADIGE
		 */
		mappaComuniPerProvincia.put(14 , listaComuni14);
		mappaComuniPerProvincia.put(93 , listaComuni93);


		/* 
		 * VENETO
		 */
		mappaComuniPerProvincia.put(9 , listaComuni9);
		mappaComuniPerProvincia.put(60 , listaComuni60);
		mappaComuniPerProvincia.put(81 , listaComuni81);
		mappaComuniPerProvincia.put(94 , listaComuni94);
		mappaComuniPerProvincia.put(99 , listaComuni99);
		mappaComuniPerProvincia.put(102 , listaComuni102);
		mappaComuniPerProvincia.put(104 , listaComuni104);


		/* 
		 * FRIULI VENEZIA GIULIA
		 */
		mappaComuniPerProvincia.put(37 , listaComuni37);
		mappaComuniPerProvincia.put(71 , listaComuni71);
		mappaComuniPerProvincia.put(95 , listaComuni95);
		mappaComuniPerProvincia.put(96 , listaComuni96);


		/* 
		 * LIGURIA
		 */
		mappaComuniPerProvincia.put(36 , listaComuni36);
		mappaComuniPerProvincia.put(39 , listaComuni39);
		mappaComuniPerProvincia.put(41 , listaComuni41);
		mappaComuniPerProvincia.put(84 , listaComuni84);


		/* 
		 * EMILIA ROMAGNA
		 */
		mappaComuniPerProvincia.put(13 , listaComuni13);
		mappaComuniPerProvincia.put(30 , listaComuni30);
		mappaComuniPerProvincia.put(34 , listaComuni34);
		mappaComuniPerProvincia.put(55 , listaComuni55);
		mappaComuniPerProvincia.put(62 , listaComuni62);
		mappaComuniPerProvincia.put(67 , listaComuni67);
		mappaComuniPerProvincia.put(75 , listaComuni75);
		mappaComuniPerProvincia.put(77 , listaComuni77);
		mappaComuniPerProvincia.put(79 , listaComuni79);


		/* 
		 * TOSCANA
		 */
		mappaComuniPerProvincia.put(4 , listaComuni4);
		mappaComuniPerProvincia.put(31 , listaComuni31);
		mappaComuniPerProvincia.put(38 , listaComuni38);
		mappaComuniPerProvincia.put(46 , listaComuni46);
		mappaComuniPerProvincia.put(48 , listaComuni48);
		mappaComuniPerProvincia.put(51 , listaComuni51);
		mappaComuniPerProvincia.put(68 , listaComuni68);
		mappaComuniPerProvincia.put(69 , listaComuni69);
		mappaComuniPerProvincia.put(73 , listaComuni73);
		mappaComuniPerProvincia.put(85 , listaComuni85);


		/* 
		 * UMBRIA
		 */
		mappaComuniPerProvincia.put(64 , listaComuni64);
		mappaComuniPerProvincia.put(90 , listaComuni90);


		/* 
		 * MARCHE
		 */
		mappaComuniPerProvincia.put(3 , listaComuni3);
		mappaComuniPerProvincia.put(5 , listaComuni5);
		mappaComuniPerProvincia.put(49 , listaComuni49);
		mappaComuniPerProvincia.put(65 , listaComuni65);


		/* 
		 * LAZIO
		 */
		mappaComuniPerProvincia.put(35 , listaComuni35);
		mappaComuniPerProvincia.put(43 , listaComuni43);
		mappaComuniPerProvincia.put(78 , listaComuni78);
		mappaComuniPerProvincia.put(80 , listaComuni80);
		mappaComuniPerProvincia.put(105 , listaComuni105);


		/* 
		 * ABRUZZO
		 */
		mappaComuniPerProvincia.put(23 , listaComuni23);
		mappaComuniPerProvincia.put(42 , listaComuni42);
		mappaComuniPerProvincia.put(66 , listaComuni66);
		mappaComuniPerProvincia.put(89 , listaComuni89);


		/* 
		 * MOLISE
		 */
		mappaComuniPerProvincia.put(19 , listaComuni19);
		mappaComuniPerProvincia.put(40 , listaComuni40);


		/* 
		 * CAMPANIA
		 */
		mappaComuniPerProvincia.put(7 , listaComuni7);
		mappaComuniPerProvincia.put(10 , listaComuni10);
		mappaComuniPerProvincia.put(20 , listaComuni20);
		mappaComuniPerProvincia.put(56 , listaComuni56);
		mappaComuniPerProvincia.put(82 , listaComuni82);


		/* 
		 * PUGLIA
		 */
		mappaComuniPerProvincia.put(8 , listaComuni8);
		mappaComuniPerProvincia.put(16 , listaComuni16);
		mappaComuniPerProvincia.put(33 , listaComuni33);
		mappaComuniPerProvincia.put(44 , listaComuni44);
		mappaComuniPerProvincia.put(88 , listaComuni88);


		/* 
		 * BASILICATA
		 */
		mappaComuniPerProvincia.put(52 , listaComuni52);
		mappaComuniPerProvincia.put(72 , listaComuni72);


		/* 
		 * CALABRIA
		 */
		mappaComuniPerProvincia.put(22 , listaComuni22);
		mappaComuniPerProvincia.put(25 , listaComuni25);
		mappaComuniPerProvincia.put(27 , listaComuni27);
		mappaComuniPerProvincia.put(76 , listaComuni76);
		mappaComuniPerProvincia.put(103 , listaComuni103);


		/* 
		 * SICILIA
		 */
		mappaComuniPerProvincia.put(1 , listaComuni1);
		mappaComuniPerProvincia.put(18 , listaComuni18);
		mappaComuniPerProvincia.put(21 , listaComuni21);
		mappaComuniPerProvincia.put(29 , listaComuni29);
		mappaComuniPerProvincia.put(53 , listaComuni53);
		mappaComuniPerProvincia.put(61 , listaComuni61);
		mappaComuniPerProvincia.put(74 , listaComuni74);
		mappaComuniPerProvincia.put(86 , listaComuni86);
		mappaComuniPerProvincia.put(92 , listaComuni92);


		/* 
		 * SARDEGNA
		 */
		mappaComuniPerProvincia.put(17 , listaComuni17);
		mappaComuniPerProvincia.put(58 , listaComuni58);
		mappaComuniPerProvincia.put(59 , listaComuni59);
		mappaComuniPerProvincia.put(83 , listaComuni83);

	}



	private void initComuniVC(ArrayList<Comune> listaComuni101) {
		////// VERCELLI
		Comune comune1761 = new Comune();  // ALAGNA VALSESIA
		comune1761.setCodice(1761);
		comune1761.setNome("ALAGNA VALSESIA");
		listaComuni101.add(comune1761);
		Comune comune1762 = new Comune();  // ALBANO VERCELLESE
		comune1762.setCodice(1762);
		comune1762.setNome("ALBANO VERCELLESE");
		listaComuni101.add(comune1762);
		Comune comune1763 = new Comune();  // ALICE CASTELLO
		comune1763.setCodice(1763);
		comune1763.setNome("ALICE CASTELLO");
		listaComuni101.add(comune1763);
		Comune comune1765 = new Comune();  // ARBORIO
		comune1765.setCodice(1765);
		comune1765.setNome("ARBORIO");
		listaComuni101.add(comune1765);
		Comune comune1766 = new Comune();  // ASIGLIANO VERCELLESE
		comune1766.setCodice(1766);
		comune1766.setNome("ASIGLIANO VERCELLESE");
		listaComuni101.add(comune1766);
		Comune comune1767 = new Comune();  // BALMUCCIA
		comune1767.setCodice(1767);
		comune1767.setNome("BALMUCCIA");
		listaComuni101.add(comune1767);
		Comune comune1768 = new Comune();  // BALOCCO
		comune1768.setCodice(1768);
		comune1768.setNome("BALOCCO");
		listaComuni101.add(comune1768);
		Comune comune1770 = new Comune();  // BIANZE'
		comune1770.setCodice(1770);
		comune1770.setNome("BIANZE'");
		listaComuni101.add(comune1770);
		Comune comune1773 = new Comune();  // BOCCIOLETO
		comune1773.setCodice(1773);
		comune1773.setNome("BOCCIOLETO");
		listaComuni101.add(comune1773);
		Comune comune1774 = new Comune();  // BORGO D'ALE
		comune1774.setCodice(1774);
		comune1774.setNome("BORGO D'ALE");
		listaComuni101.add(comune1774);
		Comune comune1775 = new Comune();  // BORGO VERCELLI
		comune1775.setCodice(1775);
		comune1775.setNome("BORGO VERCELLI");
		listaComuni101.add(comune1775);
		Comune comune1776 = new Comune();  // BORGOSESIA
		comune1776.setCodice(1776);
		comune1776.setNome("BORGOSESIA");
		listaComuni101.add(comune1776);
		Comune comune1778 = new Comune();  // BREIA
		comune1778.setCodice(1778);
		comune1778.setNome("BREIA");
		listaComuni101.add(comune1778);
		Comune comune1780 = new Comune();  // BURONZO
		comune1780.setCodice(1780);
		comune1780.setNome("BURONZO");
		listaComuni101.add(comune1780);
		Comune comune1784 = new Comune();  // CAMPERTOGNO
		comune1784.setCodice(1784);
		comune1784.setNome("CAMPERTOGNO");
		listaComuni101.add(comune1784);
	}



	private void initComuniVCO(ArrayList<Comune> listaComuni100) {
		////// VERBANO-CUSIO-OSSOLA
		Comune comune1683 = new Comune();  // ANTRONA SCHIERANCO
		comune1683.setCodice(1683);
		comune1683.setNome("ANTRONA SCHIERANCO");
		listaComuni100.add(comune1683);
		Comune comune1684 = new Comune();  // ANZOLA D'OSSOLA
		comune1684.setCodice(1684);
		comune1684.setNome("ANZOLA D'OSSOLA");
		listaComuni100.add(comune1684);
		Comune comune1685 = new Comune();  // ARIZZANO
		comune1685.setCodice(1685);
		comune1685.setNome("ARIZZANO");
		listaComuni100.add(comune1685);
		Comune comune1686 = new Comune();  // AROLA
		comune1686.setCodice(1686);
		comune1686.setNome("AROLA");
		listaComuni100.add(comune1686);
		Comune comune1687 = new Comune();  // AURANO
		comune1687.setCodice(1687);
		comune1687.setNome("AURANO");
		listaComuni100.add(comune1687);
		Comune comune1688 = new Comune();  // BACENO
		comune1688.setCodice(1688);
		comune1688.setNome("BACENO");
		listaComuni100.add(comune1688);
		Comune comune1689 = new Comune();  // BANNIO ANZINO
		comune1689.setCodice(1689);
		comune1689.setNome("BANNIO ANZINO");
		listaComuni100.add(comune1689);
		Comune comune1690 = new Comune();  // BAVENO
		comune1690.setCodice(1690);
		comune1690.setNome("BAVENO");
		listaComuni100.add(comune1690);
		Comune comune1691 = new Comune();  // BEE
		comune1691.setCodice(1691);
		comune1691.setNome("BEE");
		listaComuni100.add(comune1691);
		Comune comune1692 = new Comune();  // BELGIRATE
		comune1692.setCodice(1692);
		comune1692.setNome("BELGIRATE");
		listaComuni100.add(comune1692);
		Comune comune1693 = new Comune();  // BEURA CARDEZZA
		comune1693.setCodice(1693);
		comune1693.setNome("BEURA CARDEZZA");
		listaComuni100.add(comune1693);
		Comune comune1694 = new Comune();  // BOGNANCO
		comune1694.setCodice(1694);
		comune1694.setNome("BOGNANCO");
		listaComuni100.add(comune1694);
		Comune comune1695 = new Comune();  // BROVELLO CARPUGNINO
		comune1695.setCodice(1695);
		comune1695.setNome("BROVELLO CARPUGNINO");
		listaComuni100.add(comune1695);
		Comune comune1696 = new Comune();  // CALASCA CASTIGLIONE
		comune1696.setCodice(1696);
		comune1696.setNome("CALASCA CASTIGLIONE");
		listaComuni100.add(comune1696);
		Comune comune1697 = new Comune();  // CAMBIASCA
		comune1697.setCodice(1697);
		comune1697.setNome("CAMBIASCA");
		listaComuni100.add(comune1697);
	}



	private void initComuniTO(ArrayList<Comune> listaComuni91) {
		////// TORINO
		Comune comune331 = new Comune();  // AGLIE'
		comune331.setCodice(331);
		comune331.setNome("AGLIE'");
		listaComuni91.add(comune331);
		Comune comune332 = new Comune();  // AIRASCA
		comune332.setCodice(332);
		comune332.setNome("AIRASCA");
		listaComuni91.add(comune332);
		Comune comune333 = new Comune();  // ALA DI STURA
		comune333.setCodice(333);
		comune333.setNome("ALA DI STURA");
		listaComuni91.add(comune333);
		Comune comune334 = new Comune();  // ALBIANO D'IVREA
		comune334.setCodice(334);
		comune334.setNome("ALBIANO D'IVREA");
		listaComuni91.add(comune334);
		Comune comune335 = new Comune();  // ALICE SUPERIORE
		comune335.setCodice(335);
		comune335.setNome("ALICE SUPERIORE");
		listaComuni91.add(comune335);
		Comune comune336 = new Comune();  // ALMESE
		comune336.setCodice(336);
		comune336.setNome("ALMESE");
		listaComuni91.add(comune336);
		Comune comune337 = new Comune();  // ALPETTE
		comune337.setCodice(337);
		comune337.setNome("ALPETTE");
		listaComuni91.add(comune337);
		Comune comune338 = new Comune();  // ALPIGNANO
		comune338.setCodice(338);
		comune338.setNome("ALPIGNANO");
		listaComuni91.add(comune338);
		Comune comune339 = new Comune();  // ANDEZENO
		comune339.setCodice(339);
		comune339.setNome("ANDEZENO");
		listaComuni91.add(comune339);
		Comune comune340 = new Comune();  // ANDRATE
		comune340.setCodice(340);
		comune340.setNome("ANDRATE");
		listaComuni91.add(comune340);
		Comune comune341 = new Comune();  // ANGROGNA
		comune341.setCodice(341);
		comune341.setNome("ANGROGNA");
		listaComuni91.add(comune341);
		Comune comune342 = new Comune();  // ARIGNANO
		comune342.setCodice(342);
		comune342.setNome("ARIGNANO");
		listaComuni91.add(comune342);
		Comune comune343 = new Comune();  // AVIGLIANA
		comune343.setCodice(343);
		comune343.setNome("AVIGLIANA");
		listaComuni91.add(comune343);
		Comune comune344 = new Comune();  // AZEGLIO
		comune344.setCodice(344);
		comune344.setNome("AZEGLIO");
		listaComuni91.add(comune344);
		Comune comune345 = new Comune();  // BAIRO
		comune345.setCodice(345);
		comune345.setNome("BAIRO");
		listaComuni91.add(comune345);
	}



	private void initComuniNO(ArrayList<Comune> listaComuni57) {
		////// NOVARA
		Comune comune1463 = new Comune();  // AGRATE CONTURBIA
		comune1463.setCodice(1463);
		comune1463.setNome("AGRATE CONTURBIA");
		listaComuni57.add(comune1463);
		Comune comune1464 = new Comune();  // AMENO
		comune1464.setCodice(1464);
		comune1464.setNome("AMENO");
		listaComuni57.add(comune1464);
		Comune comune1468 = new Comune();  // ARMENO
		comune1468.setCodice(1468);
		comune1468.setNome("ARMENO");
		listaComuni57.add(comune1468);
		Comune comune1470 = new Comune();  // ARONA
		comune1470.setCodice(1470);
		comune1470.setNome("ARONA");
		listaComuni57.add(comune1470);
		Comune comune1474 = new Comune();  // BARENGO
		comune1474.setCodice(1474);
		comune1474.setNome("BARENGO");
		listaComuni57.add(comune1474);
		Comune comune1478 = new Comune();  // BELLINZAGO NOVARESE
		comune1478.setCodice(1478);
		comune1478.setNome("BELLINZAGO NOVARESE");
		listaComuni57.add(comune1478);
		Comune comune1480 = new Comune();  // BIANDRATE
		comune1480.setCodice(1480);
		comune1480.setNome("BIANDRATE");
		listaComuni57.add(comune1480);
		Comune comune1481 = new Comune();  // BOCA
		comune1481.setCodice(1481);
		comune1481.setNome("BOCA");
		listaComuni57.add(comune1481);
		Comune comune1483 = new Comune();  // BOGOGNO
		comune1483.setCodice(1483);
		comune1483.setNome("BOGOGNO");
		listaComuni57.add(comune1483);
		Comune comune1484 = new Comune();  // BOLZANO NOVARESE
		comune1484.setCodice(1484);
		comune1484.setNome("BOLZANO NOVARESE");
		listaComuni57.add(comune1484);
		Comune comune1485 = new Comune();  // BORGO TICINO
		comune1485.setCodice(1485);
		comune1485.setNome("BORGO TICINO");
		listaComuni57.add(comune1485);
		Comune comune1486 = new Comune();  // BORGOLAVEZZARO
		comune1486.setCodice(1486);
		comune1486.setNome("BORGOLAVEZZARO");
		listaComuni57.add(comune1486);
		Comune comune1487 = new Comune();  // BORGOMANERO
		comune1487.setCodice(1487);
		comune1487.setNome("BORGOMANERO");
		listaComuni57.add(comune1487);
		Comune comune1488 = new Comune();  // BRIGA NOVARESE
		comune1488.setCodice(1488);
		comune1488.setNome("BRIGA NOVARESE");
		listaComuni57.add(comune1488);
		Comune comune1489 = new Comune();  // BRIONA
		comune1489.setCodice(1489);
		comune1489.setNome("BRIONA");
		listaComuni57.add(comune1489);
	}



	private void initComuniCN(ArrayList<Comune> listaComuni28) {
		////// CUNEO
		Comune comune1183 = new Comune();  // ACCEGLIO
		comune1183.setCodice(1183);
		comune1183.setNome("ACCEGLIO");
		listaComuni28.add(comune1183);
		Comune comune1184 = new Comune();  // AISONE
		comune1184.setCodice(1184);
		comune1184.setNome("AISONE");
		listaComuni28.add(comune1184);
		Comune comune1185 = new Comune();  // ALBA
		comune1185.setCodice(1185);
		comune1185.setNome("ALBA");
		listaComuni28.add(comune1185);
		Comune comune1186 = new Comune();  // ALBARETTO DELLA TORRE
		comune1186.setCodice(1186);
		comune1186.setNome("ALBARETTO DELLA TORRE");
		listaComuni28.add(comune1186);
		Comune comune1187 = new Comune();  // ALTO
		comune1187.setCodice(1187);
		comune1187.setNome("ALTO");
		listaComuni28.add(comune1187);
		Comune comune1188 = new Comune();  // ARGENTERA
		comune1188.setCodice(1188);
		comune1188.setNome("ARGENTERA");
		listaComuni28.add(comune1188);
		Comune comune1189 = new Comune();  // ARGUELLO
		comune1189.setCodice(1189);
		comune1189.setNome("ARGUELLO");
		listaComuni28.add(comune1189);
		Comune comune1190 = new Comune();  // BAGNASCO
		comune1190.setCodice(1190);
		comune1190.setNome("BAGNASCO");
		listaComuni28.add(comune1190);
		Comune comune1191 = new Comune();  // BAGNOLO PIEMONTE
		comune1191.setCodice(1191);
		comune1191.setNome("BAGNOLO PIEMONTE");
		listaComuni28.add(comune1191);
		Comune comune1192 = new Comune();  // BALDISSERO D'ALBA
		comune1192.setCodice(1192);
		comune1192.setNome("BALDISSERO D'ALBA");
		listaComuni28.add(comune1192);
		Comune comune1193 = new Comune();  // BARBARESCO
		comune1193.setCodice(1193);
		comune1193.setNome("BARBARESCO");
		listaComuni28.add(comune1193);
		Comune comune1194 = new Comune();  // BARGE
		comune1194.setCodice(1194);
		comune1194.setNome("BARGE");
		listaComuni28.add(comune1194);
		Comune comune1195 = new Comune();  // BAROLO
		comune1195.setCodice(1195);
		comune1195.setNome("BAROLO");
		listaComuni28.add(comune1195);
		Comune comune1196 = new Comune();  // BASTIA MONDOVI'
		comune1196.setCodice(1196);
		comune1196.setNome("BASTIA MONDOVI'");
		listaComuni28.add(comune1196);
		Comune comune1197 = new Comune();  // BATTIFOLLO
		comune1197.setCodice(1197);
		comune1197.setNome("BATTIFOLLO");
		listaComuni28.add(comune1197);
	}



	private void initComuniBI(ArrayList<Comune> listaComuni12) {
		////// BIELLA
		Comune comune1098 = new Comune();  // AILOCHE
		comune1098.setCodice(1098);
		comune1098.setNome("AILOCHE");
		listaComuni12.add(comune1098);
		Comune comune1099 = new Comune();  // ANDORNO MICCA
		comune1099.setCodice(1099);
		comune1099.setNome("ANDORNO MICCA");
		listaComuni12.add(comune1099);
		Comune comune1100 = new Comune();  // BENNA
		comune1100.setCodice(1100);
		comune1100.setNome("BENNA");
		listaComuni12.add(comune1100);
		Comune comune1097 = new Comune();  // BIELLA
		comune1097.setCodice(1097);
		comune1097.setNome("BIELLA");
		listaComuni12.add(comune1097);
		Comune comune1101 = new Comune();  // BIOGLIO
		comune1101.setCodice(1101);
		comune1101.setNome("BIOGLIO");
		listaComuni12.add(comune1101);
		Comune comune1102 = new Comune();  // BORRIANA
		comune1102.setCodice(1102);
		comune1102.setNome("BORRIANA");
		listaComuni12.add(comune1102);
		Comune comune1103 = new Comune();  // BRUSNENGO
		comune1103.setCodice(1103);
		comune1103.setNome("BRUSNENGO");
		listaComuni12.add(comune1103);
		Comune comune1104 = new Comune();  // CALLABIANA
		comune1104.setCodice(1104);
		comune1104.setNome("CALLABIANA");
		listaComuni12.add(comune1104);
		Comune comune1105 = new Comune();  // CAMANDONA
		comune1105.setCodice(1105);
		comune1105.setNome("CAMANDONA");
		listaComuni12.add(comune1105);
		Comune comune1106 = new Comune();  // CAMBURZANO
		comune1106.setCodice(1106);
		comune1106.setNome("CAMBURZANO");
		listaComuni12.add(comune1106);
		Comune comune1107 = new Comune();  // CAMPIGLIA CERVO
		comune1107.setCodice(1107);
		comune1107.setNome("CAMPIGLIA CERVO");
		listaComuni12.add(comune1107);
		Comune comune1108 = new Comune();  // CANDELO
		comune1108.setCodice(1108);
		comune1108.setNome("CANDELO");
		listaComuni12.add(comune1108);
		Comune comune1109 = new Comune();  // CAPRILE
		comune1109.setCodice(1109);
		comune1109.setNome("CAPRILE");
		listaComuni12.add(comune1109);
		Comune comune1110 = new Comune();  // CASAPINTA
		comune1110.setCodice(1110);
		comune1110.setNome("CASAPINTA");
		listaComuni12.add(comune1110);
		Comune comune1111 = new Comune();  // CASTELLETTO CERVO
		comune1111.setCodice(1111);
		comune1111.setNome("CASTELLETTO CERVO");
		listaComuni12.add(comune1111);
	}



	private void initComuniAT(ArrayList<Comune> listaComuni6) {
		////// ASTI
		Comune comune1095 = new Comune();  // AGLIANO TERME
		comune1095.setCodice(1095);
		comune1095.setNome("AGLIANO TERME");
		listaComuni6.add(comune1095);
		Comune comune950 = new Comune();  // ALBUGNANO
		comune950.setCodice(950);
		comune950.setNome("ALBUGNANO");
		listaComuni6.add(comune950);
		Comune comune951 = new Comune();  // ANTIGNANO
		comune951.setCodice(951);
		comune951.setNome("ANTIGNANO");
		listaComuni6.add(comune951);
		Comune comune952 = new Comune();  // ARAMENGO
		comune952.setCodice(952);
		comune952.setNome("ARAMENGO");
		listaComuni6.add(comune952);
		Comune comune948 = new Comune();  // ASTI
		comune948.setCodice(948);
		comune948.setNome("ASTI");
		listaComuni6.add(comune948);
		Comune comune953 = new Comune();  // AZZANO D'ASTI
		comune953.setCodice(953);
		comune953.setNome("AZZANO D'ASTI");
		listaComuni6.add(comune953);
		Comune comune954 = new Comune();  // BALDICHIERI D'ASTI
		comune954.setCodice(954);
		comune954.setNome("BALDICHIERI D'ASTI");
		listaComuni6.add(comune954);
		Comune comune955 = new Comune();  // BELVEGLIO
		comune955.setCodice(955);
		comune955.setNome("BELVEGLIO");
		listaComuni6.add(comune955);
		Comune comune956 = new Comune();  // BERZANO DI S.PIETRO
		comune956.setCodice(956);
		comune956.setNome("BERZANO DI S.PIETRO");
		listaComuni6.add(comune956);
		Comune comune957 = new Comune();  // BRUNO
		comune957.setCodice(957);
		comune957.setNome("BRUNO");
		listaComuni6.add(comune957);
		Comune comune958 = new Comune();  // BUBBIO
		comune958.setCodice(958);
		comune958.setNome("BUBBIO");
		listaComuni6.add(comune958);
		Comune comune959 = new Comune();  // BUTTIGLIERA D'ASTI
		comune959.setCodice(959);
		comune959.setNome("BUTTIGLIERA D'ASTI");
		listaComuni6.add(comune959);
		Comune comune960 = new Comune();  // CALAMANDRANA
		comune960.setCodice(960);
		comune960.setNome("CALAMANDRANA");
		listaComuni6.add(comune960);
		Comune comune961 = new Comune();  // CALLIANO
		comune961.setCodice(961);
		comune961.setNome("CALLIANO");
		listaComuni6.add(comune961);
		Comune comune962 = new Comune();  // CALOSSO
		comune962.setCodice(962);
		comune962.setNome("CALOSSO");
		listaComuni6.add(comune962);
	}



	private void initComuniAL(ArrayList<Comune> listaComuni2) {
		////// ALESSANDRIA
		Comune comune737 = new Comune();  // ACQUI TERME
		comune737.setCodice(737);
		comune737.setNome("ACQUI TERME");
		listaComuni2.add(comune737);
		Comune comune738 = new Comune();  // ALBERA LIGURE
		comune738.setCodice(738);
		comune738.setNome("ALBERA LIGURE");
		listaComuni2.add(comune738);
		Comune comune736 = new Comune();  // ALESSANDRIA
		comune736.setCodice(736);
		comune736.setNome("ALESSANDRIA");
		listaComuni2.add(comune736);
		Comune comune739 = new Comune();  // ALFIANO NATTA
		comune739.setCodice(739);
		comune739.setNome("ALFIANO NATTA");
		listaComuni2.add(comune739);
		Comune comune740 = new Comune();  // ALICE BEL COLLE
		comune740.setCodice(740);
		comune740.setNome("ALICE BEL COLLE");
		listaComuni2.add(comune740);
		Comune comune741 = new Comune();  // ALLUVIONI CAMBIO'
		comune741.setCodice(741);
		comune741.setNome("ALLUVIONI CAMBIO'");
		listaComuni2.add(comune741);
		Comune comune742 = new Comune();  // ALTAVILLA MONFERRATO
		comune742.setCodice(742);
		comune742.setNome("ALTAVILLA MONFERRATO");
		listaComuni2.add(comune742);
		Comune comune743 = new Comune();  // ALZANO SCRIVIA
		comune743.setCodice(743);
		comune743.setNome("ALZANO SCRIVIA");
		listaComuni2.add(comune743);
		Comune comune744 = new Comune();  // ARQUATA SCRIVIA
		comune744.setCodice(744);
		comune744.setNome("ARQUATA SCRIVIA");
		listaComuni2.add(comune744);
		Comune comune745 = new Comune();  // AVOLASCA
		comune745.setCodice(745);
		comune745.setNome("AVOLASCA");
		listaComuni2.add(comune745);
		Comune comune746 = new Comune();  // BALZOLA
		comune746.setCodice(746);
		comune746.setNome("BALZOLA");
		listaComuni2.add(comune746);
		Comune comune747 = new Comune();  // BASALUZZO
		comune747.setCodice(747);
		comune747.setNome("BASALUZZO");
		listaComuni2.add(comune747);
		Comune comune748 = new Comune();  // BASSIGNANA
		comune748.setCodice(748);
		comune748.setNome("BASSIGNANA");
		listaComuni2.add(comune748);
		Comune comune749 = new Comune();  // BELFORTE MONFERRATO
		comune749.setCodice(749);
		comune749.setNome("BELFORTE MONFERRATO");
		listaComuni2.add(comune749);
		Comune comune750 = new Comune();  // BERGAMASCO
		comune750.setCodice(750);
		comune750.setNome("BERGAMASCO");
		listaComuni2.add(comune750);
	}



	@Override
	public String findEmailAslByCodiceComune(Integer codiceComune) throws ItemNotFoundDAOException, DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
