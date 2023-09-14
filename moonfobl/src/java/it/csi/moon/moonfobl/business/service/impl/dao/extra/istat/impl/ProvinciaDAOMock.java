/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.service.impl.dao.extra.istat.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.commons.dto.extra.istat.Provincia;
import it.csi.moon.moonfobl.business.service.impl.dao.extra.istat.ProvinciaDAO;

/**
 * DAO per l'accesso alle province MOCK DATI FASULLI
 * 
 * @see Provincia
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("mock")
public class ProvinciaDAOMock implements ProvinciaDAO {

	/**  */
	private LinkedHashMap<Integer, List<Provincia>> mappaProvince;


	/**
	 *
	 */
	public ProvinciaDAOMock() {
		init();
	}


	/**
	 *
	 * @return
	 */
	public List<Provincia> findAll() {
		return new ArrayList<>();
	}

	/**
	 * 
	 * @param codiceProvincia
	 * @return
	 */
	public Provincia findByPK(Integer codiceProvincia) {
		for (Integer codRegione : mappaProvince.keySet()) {
			for (Provincia provincia : mappaProvince.get(codRegione)) {
				if (provincia.getCodice().equals(codiceProvincia)) {
					return provincia;
				}
			}
		}
		return null;
	}
	public Provincia findByPKidx(Integer codiceRegione, Integer codiceProvincia) {
		for (Provincia provincia : mappaProvince.get(codiceRegione)) {
			if (provincia.getCodice().equals(codiceProvincia)) {
				return provincia;
			}
		}
		return null;
	}


	/**
	 *
	 * @param codiceRegione
	 * @return
	 */
	public List<Provincia> listByCodiceRegione(Integer codiceRegione) {
		List<Provincia> res = mappaProvince.get(codiceRegione);
		if ( res == null ) {
			res = new ArrayList<>();
		}
		return res;
	}


	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// DATI FASULLI
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void init() {
		mappaProvince = new LinkedHashMap<Integer, List<Provincia>>();

		/*
		 * Inizializzo la lista delle province
		 */
		List<Provincia> listaProvincePiemonte            = new ArrayList<>();
		List<Provincia> listaProvinceUmbria              = new ArrayList<>();
		List<Provincia> listaProvinceMarche              = new ArrayList<>();
		List<Provincia> listaProvinceLazio               = new ArrayList<>();
		List<Provincia> listaProvinceAbruzzo             = new ArrayList<>();
		List<Provincia> listaProvinceMolise              = new ArrayList<>();
		List<Provincia> listaProvinceCampania            = new ArrayList<>();
		List<Provincia> listaProvincePuglia              = new ArrayList<>();
		List<Provincia> listaProvinceBasilicata          = new ArrayList<>();
		List<Provincia> listaProvinceCalabria            = new ArrayList<>();
		List<Provincia> listaProvinceSicilia             = new ArrayList<>();
		List<Provincia> listaProvinceValleDAosta         = new ArrayList<>();
		List<Provincia> listaProvinceSardegna            = new ArrayList<>();
		List<Provincia> listaProvinceLombardia           = new ArrayList<>();
		List<Provincia> listaProvinceTrentinoAltoAdige   = new ArrayList<>();
		List<Provincia> listaProvinceVeneto              = new ArrayList<>();
		List<Provincia> listaProvinceFriuliVeneziaGiulia = new ArrayList<>();
		List<Provincia> listaProvinceLiguria             = new ArrayList<>();
		List<Provincia> listaProvinceEmiliaRomagna       = new ArrayList<>();
		List<Provincia> listaProvinceToscana             = new ArrayList<>();


		/*
		 * PIEMONTE
		 */
		Provincia provincia2 = new Provincia(); // ALESSANDRIA
		provincia2.setCodice(2);
		provincia2.setNome("ALESSANDRIA");
		provincia2.setSigla("AL");
		listaProvincePiemonte.add(provincia2);

		Provincia provincia6 = new Provincia(); // ASTI
		provincia6.setCodice(6);
		provincia6.setNome("ASTI");
		provincia6.setSigla("AT");
		listaProvincePiemonte.add(provincia6);

		Provincia provincia12 = new Provincia(); // BIELLA
		provincia12.setCodice(12);
		provincia12.setNome("BIELLA");
		provincia12.setSigla("BI");
		listaProvincePiemonte.add(provincia12);

		Provincia provincia28 = new Provincia(); // CUNEO
		provincia28.setCodice(28);
		provincia28.setNome("CUNEO");
		provincia28.setSigla("CN");
		listaProvincePiemonte.add(provincia28);

		Provincia provincia57 = new Provincia(); // NOVARA
		provincia57.setCodice(57);
		provincia57.setNome("NOVARA");
		provincia57.setSigla("NO");
		listaProvincePiemonte.add(provincia57);

		Provincia provincia91 = new Provincia(); // TORINO
		provincia91.setCodice(91);
		provincia91.setNome("TORINO");
		provincia91.setSigla("TO");
		listaProvincePiemonte.add(provincia91);

		Provincia provincia100 = new Provincia(); // VERBANO-CUSIO-OSSOLA
		provincia100.setCodice(100);
		provincia100.setNome("VERBANO-CUSIO-OSSOLA");
		provincia100.setSigla("VC");
		listaProvincePiemonte.add(provincia100);

		Provincia provincia101 = new Provincia(); // VERCELLI
		provincia101.setCodice(101);
		provincia101.setNome("VERCELLI");
		provincia101.setSigla("VE");
		listaProvincePiemonte.add(provincia101);


		/*
		 * VALLE D'AOSTA
		 */
		Provincia provincia97 = new Provincia(); // VALLE D'AOSTA
		provincia97.setCodice(97);
		provincia97.setNome("VALLE D'AOSTA");
		provincia97.setSigla("AO");
		listaProvinceValleDAosta.add(provincia97);


		/*
		 * LOMBARDIA
		 */
		Provincia provincia11 = new Provincia(); // BERGAMO
		provincia11.setCodice(11);
		provincia11.setNome("BERGAMO");
		listaProvinceLombardia.add(provincia11);

		Provincia provincia15 = new Provincia(); // BRESCIA
		provincia15.setCodice(15);
		provincia15.setNome("BRESCIA");
		listaProvinceLombardia.add(provincia15);

		Provincia provincia24 = new Provincia(); // COMO
		provincia24.setCodice(24);
		provincia24.setNome("COMO");
		listaProvinceLombardia.add(provincia24);

		Provincia provincia26 = new Provincia(); // CREMONA
		provincia26.setCodice(26);
		provincia26.setNome("CREMONA");
		listaProvinceLombardia.add(provincia26);

		Provincia provincia45 = new Provincia(); // LECCO
		provincia45.setCodice(45);
		provincia45.setNome("LECCO");
		listaProvinceLombardia.add(provincia45);

		Provincia provincia47 = new Provincia(); // LODI
		provincia47.setCodice(47);
		provincia47.setNome("LODI");
		listaProvinceLombardia.add(provincia47);

		Provincia provincia50 = new Provincia(); // MANTOVA
		provincia50.setCodice(50);
		provincia50.setNome("MANTOVA");
		listaProvinceLombardia.add(provincia50);

		Provincia provincia54 = new Provincia(); // MILANO
		provincia54.setCodice(54);
		provincia54.setNome("MILANO");
		provincia54.setSigla("MI");
		listaProvinceLombardia.add(provincia54);

		Provincia provincia63 = new Provincia(); // PAVIA
		provincia63.setCodice(63);
		provincia63.setNome("PAVIA");
		listaProvinceLombardia.add(provincia63);

		Provincia provincia87 = new Provincia(); // SONDRIO
		provincia87.setCodice(87);
		provincia87.setNome("SONDRIO");
		listaProvinceLombardia.add(provincia87);

		Provincia provincia98 = new Provincia(); // VARESE
		provincia98.setCodice(98);
		provincia98.setNome("VARESE");
		listaProvinceLombardia.add(provincia98);


		/*
		 * TRENTINO ALTO ADIGE
		 */

		Provincia provincia14 = new Provincia(); // BOLZANO - BOZEN
		provincia14.setCodice(14);
		provincia14.setNome("BOLZANO - BOZEN");
		listaProvinceTrentinoAltoAdige.add(provincia14);

		Provincia provincia93 = new Provincia(); // TRENTO
		provincia93.setCodice(93);
		provincia93.setNome("TRENTO");
		listaProvinceTrentinoAltoAdige.add(provincia93);


		/*
		 * VENETO
		 */
		Provincia provincia9 = new Provincia(); // BELLUNO
		provincia9.setCodice(9);
		provincia9.setNome("BELLUNO");
		listaProvinceVeneto.add(provincia9);

		Provincia provincia60 = new Provincia(); // PADOVA
		provincia60.setCodice(60);
		provincia60.setNome("PADOVA");
		listaProvinceVeneto.add(provincia60);

		Provincia provincia81 = new Provincia(); // ROVIGO
		provincia81.setCodice(81);
		provincia81.setNome("ROVIGO");
		listaProvinceVeneto.add(provincia81);

		Provincia provincia94 = new Provincia(); // TREVISO
		provincia94.setCodice(94);
		provincia94.setNome("TREVISO");
		listaProvinceVeneto.add(provincia94);

		Provincia provincia99 = new Provincia(); // VENEZIA
		provincia99.setCodice(99);
		provincia99.setNome("VENEZIA");
		listaProvinceVeneto.add(provincia99);

		Provincia provincia102 = new Provincia(); // VERONA
		provincia102.setCodice(102);
		provincia102.setNome("VERONA");
		listaProvinceVeneto.add(provincia102);

		Provincia provincia104 = new Provincia(); // VICENZA
		provincia104.setCodice(104);
		provincia104.setNome("VICENZA");
		listaProvinceVeneto.add(provincia104);


		/*
		 * FRIULI VENEZIA GIULIA
		 */
		Provincia provincia37 = new Provincia(); // GORIZIA
		provincia37.setCodice(37);
		provincia37.setNome("GORIZIA");
		listaProvinceFriuliVeneziaGiulia.add(provincia37);

		Provincia provincia71 = new Provincia(); // PORDENONE
		provincia71.setCodice(71);
		provincia71.setNome("PORDENONE");
		listaProvinceFriuliVeneziaGiulia.add(provincia71);

		Provincia provincia95 = new Provincia(); // TRIESTE
		provincia95.setCodice(95);
		provincia95.setNome("TRIESTE");
		listaProvinceFriuliVeneziaGiulia.add(provincia95);

		Provincia provincia96 = new Provincia(); // UDINE
		provincia96.setCodice(96);
		provincia96.setNome("UDINE");
		listaProvinceFriuliVeneziaGiulia.add(provincia96);


		/*
		 * LIGURIA
		 */
		Provincia provincia36 = new Provincia(); // GENOVA
		provincia36.setCodice(36);
		provincia36.setNome("GENOVA");
		listaProvinceLiguria.add(provincia36);

		Provincia provincia39 = new Provincia(); // IMPERIA
		provincia39.setCodice(39);
		provincia39.setNome("IMPERIA");
		listaProvinceLiguria.add(provincia39);

		Provincia provincia41 = new Provincia(); // LA SPEZIA
		provincia41.setCodice(41);
		provincia41.setNome("LA SPEZIA");
		listaProvinceLiguria.add(provincia41);

		Provincia provincia84 = new Provincia(); // SAVONA
		provincia84.setCodice(84);
		provincia84.setNome("SAVONA");
		listaProvinceLiguria.add(provincia84);


		/*
		 * EMILIA ROMAGNA
		 */
		Provincia provincia13 = new Provincia(); // BOLOGNA
		provincia13.setCodice(13);
		provincia13.setNome("BOLOGNA");
		listaProvinceEmiliaRomagna.add(provincia13);

		Provincia provincia30 = new Provincia(); // FERRARA
		provincia30.setCodice(30);
		provincia30.setNome("FERRARA");
		listaProvinceEmiliaRomagna.add(provincia30);

		Provincia provincia34 = new Provincia(); // FORLI'-CESENA
		provincia34.setCodice(34);
		provincia34.setNome("FORLI'-CESENA");
		listaProvinceEmiliaRomagna.add(provincia34);

		Provincia provincia55 = new Provincia(); // MODENA
		provincia55.setCodice(55);
		provincia55.setNome("MODENA");
		listaProvinceEmiliaRomagna.add(provincia55);

		Provincia provincia62 = new Provincia(); // PARMA
		provincia62.setCodice(62);
		provincia62.setNome("PARMA");
		listaProvinceEmiliaRomagna.add(provincia62);

		Provincia provincia67 = new Provincia(); // PIACENZA
		provincia67.setCodice(67);
		provincia67.setNome("PIACENZA");
		listaProvinceEmiliaRomagna.add(provincia67);

		Provincia provincia75 = new Provincia(); // RAVENNA
		provincia75.setCodice(75);
		provincia75.setNome("RAVENNA");
		listaProvinceEmiliaRomagna.add(provincia75);

		Provincia provincia77 = new Provincia(); // REGGIO NELL'EMILIA
		provincia77.setCodice(77);
		provincia77.setNome("REGGIO NELL'EMILIA");
		listaProvinceEmiliaRomagna.add(provincia77);

		Provincia provincia79 = new Provincia(); // RIMINI
		provincia79.setCodice(79);
		provincia79.setNome("RIMINI");
		listaProvinceEmiliaRomagna.add(provincia79);


		/*
		 * TOSCANA
		 */

		Provincia provincia4 = new Provincia(); // AREZZO
		provincia4.setCodice(4);
		provincia4.setNome("AREZZO");
		listaProvinceToscana.add(provincia4);

		Provincia provincia31 = new Provincia(); // FIRENZE
		provincia31.setCodice(31);
		provincia31.setNome("FIRENZE");
		listaProvinceToscana.add(provincia31);

		Provincia provincia38 = new Provincia(); // GROSSETO
		provincia38.setCodice(38);
		provincia38.setNome("GROSSETO");
		listaProvinceToscana.add(provincia38);

		Provincia provincia46 = new Provincia(); // LIVORNO
		provincia46.setCodice(46);
		provincia46.setNome("LIVORNO");
		listaProvinceToscana.add(provincia46);

		Provincia provincia48 = new Provincia(); // LUCCA
		provincia48.setCodice(48);
		provincia48.setNome("LUCCA");
		listaProvinceToscana.add(provincia48);

		Provincia provincia51 = new Provincia(); // MASSA-CARRARA
		provincia51.setCodice(51);
		provincia51.setNome("MASSA-CARRARA");
		listaProvinceToscana.add(provincia51);

		Provincia provincia68 = new Provincia(); // PISA
		provincia68.setCodice(68);
		provincia68.setNome("PISA");
		listaProvinceToscana.add(provincia68);

		Provincia provincia69 = new Provincia(); // PISTOIA
		provincia69.setCodice(69);
		provincia69.setNome("PISTOIA");
		listaProvinceToscana.add(provincia69);

		Provincia provincia73 = new Provincia(); // PRATO
		provincia73.setCodice(73);
		provincia73.setNome("PRATO");
		listaProvinceToscana.add(provincia73);

		Provincia provincia85 = new Provincia(); // SIENA
		provincia85.setCodice(85);
		provincia85.setNome("SIENA");
		listaProvinceToscana.add(provincia85);


		/*
		 * UMBRIA
		 */
		Provincia provincia64 = new Provincia(); // PERUGIA
		provincia64.setCodice(64);
		provincia64.setNome("PERUGIA");
		listaProvinceUmbria.add(provincia64);

		Provincia provincia90 = new Provincia(); // TERNI
		provincia90.setCodice(90);
		provincia90.setNome("TERNI");
		listaProvinceUmbria.add(provincia90);

		/*
		 * MARCHE
		 */
		Provincia provincia3 = new Provincia(); // ANCONA
		provincia3.setCodice(3);
		provincia3.setNome("ANCONA");
		listaProvinceMarche.add(provincia3);

		Provincia provincia5 = new Provincia(); // ASCOLI PICENO
		provincia5.setCodice(5);
		provincia5.setNome("ASCOLI PICENO");
		listaProvinceMarche.add(provincia5);

		Provincia provincia49 = new Provincia(); // MACERATA
		provincia49.setCodice(49);
		provincia49.setNome("MACERATA");
		listaProvinceMarche.add(provincia49);

		Provincia provincia65 = new Provincia(); // PESARO E URBINO
		provincia65.setCodice(65);
		provincia65.setNome("PESARO E URBINO");
		listaProvinceMarche.add(provincia65);


		/*
		 * LAZIO
		 */
		Provincia provincia35 = new Provincia(); // FROSINONE
		provincia35.setCodice(35);
		provincia35.setNome("FROSINONE");
		listaProvinceLazio.add(provincia35);

		Provincia provincia43 = new Provincia(); // LATINA
		provincia43.setCodice(43);
		provincia43.setNome("LATINA");
		listaProvinceLazio.add(provincia43);

		Provincia provincia78 = new Provincia(); // RIETI
		provincia78.setCodice(78);
		provincia78.setNome("RIETI");
		listaProvinceLazio.add(provincia78);

		Provincia provincia80 = new Provincia(); // ROMA
		provincia80.setCodice(80);
		provincia80.setNome("ROMA");
		listaProvinceLazio.add(provincia80);

		Provincia provincia105 = new Provincia(); // VITERBO
		provincia105.setCodice(105);
		provincia105.setNome("VITERBO");
		listaProvinceLazio.add(provincia105);


		/*
		 * ABRUZZO
		 */
		Provincia provincia23 = new Provincia(); // CHIETI
		provincia23.setCodice(23);
		provincia23.setNome("CHIETI");
		listaProvinceAbruzzo.add(provincia23);

		Provincia provincia42 = new Provincia(); // L'AQUILA
		provincia42.setCodice(42);
		provincia42.setNome("L'AQUILA");
		listaProvinceAbruzzo.add(provincia42);

		Provincia provincia66 = new Provincia(); // PESCARA
		provincia66.setCodice(66);
		provincia66.setNome("PESCARA");
		listaProvinceAbruzzo.add(provincia66);

		Provincia provincia89 = new Provincia(); // TERAMO
		provincia89.setCodice(89);
		provincia89.setNome("TERAMO");
		listaProvinceAbruzzo.add(provincia89);


		/*
		 * MOLISE
		 */
		Provincia provincia19 = new Provincia(); // CAMPOBASSO
		provincia19.setCodice(19);
		provincia19.setNome("CAMPOBASSO");
		listaProvinceMolise.add(provincia19);

		Provincia provincia40 = new Provincia(); // ISERNIA
		provincia40.setCodice(40);
		provincia40.setNome("ISERNIA");
		listaProvinceMolise.add(provincia40);


		/*
		 * CAMPANIA
		 */
		Provincia provincia7 = new Provincia(); // AVELLINO
		provincia7.setCodice(7);
		provincia7.setNome("AVELLINO");
		listaProvinceCampania.add(provincia7);

		Provincia provincia10 = new Provincia(); // BENEVENTO
		provincia10.setCodice(10);
		provincia10.setNome("BENEVENTO");
		listaProvinceCampania.add(provincia10);

		Provincia provincia20 = new Provincia(); // CASERTA
		provincia20.setCodice(20);
		provincia20.setNome("CASERTA");
		listaProvinceCampania.add(provincia20);

		Provincia provincia56 = new Provincia(); // NAPOLI
		provincia56.setCodice(56);
		provincia56.setNome("NAPOLI");
		listaProvinceCampania.add(provincia56);

		Provincia provincia82 = new Provincia(); // SALERNO
		provincia82.setCodice(82);
		provincia82.setNome("SALERNO");
		listaProvinceCampania.add(provincia82);


		/*
		 * PUGLIA
		 */
		Provincia provincia8 = new Provincia(); // BARI
		provincia8.setCodice(8);
		provincia8.setNome("BARI");
		listaProvincePuglia.add(provincia8);

		Provincia provincia16 = new Provincia(); // BRINDISI
		provincia16.setCodice(16);
		provincia16.setNome("BRINDISI");
		listaProvincePuglia.add(provincia16);

		Provincia provincia33 = new Provincia(); // FOGGIA
		provincia33.setCodice(33);
		provincia33.setNome("FOGGIA");
		listaProvincePuglia.add(provincia33);

		Provincia provincia44 = new Provincia(); // LECCE
		provincia44.setCodice(44);
		provincia44.setNome("LECCE");
		listaProvincePuglia.add(provincia44);

		Provincia provincia88 = new Provincia(); // TARANTO
		provincia88.setCodice(88);
		provincia88.setNome("TARANTO");
		listaProvincePuglia.add(provincia88);


		/*
		 * BASILICATA
		 */
		Provincia provincia52 = new Provincia(); // MATERA
		provincia52.setCodice(52);
		provincia52.setNome("MATERA");
		listaProvinceBasilicata.add(provincia52);

		Provincia provincia72 = new Provincia(); // POTENZA
		provincia72.setCodice(72);
		provincia72.setNome("POTENZA");
		listaProvinceBasilicata.add(provincia72);


		/*
		 * CALABRIA
		 */
		Provincia provincia22 = new Provincia(); // CATANZARO
		provincia22.setCodice(22);
		provincia22.setNome("CATANZARO");
		listaProvinceCalabria.add(provincia22);

		Provincia provincia25 = new Provincia(); // COSENZA
		provincia25.setCodice(25);
		provincia25.setNome("COSENZA");
		listaProvinceCalabria.add(provincia25);

		Provincia provincia27 = new Provincia(); // CROTONE
		provincia27.setCodice(27);
		provincia27.setNome("CROTONE");
		listaProvinceCalabria.add(provincia27);

		Provincia provincia76 = new Provincia(); // REGGIO DI CALABRIA
		provincia76.setCodice(76);
		provincia76.setNome("REGGIO DI CALABRIA");
		listaProvinceCalabria.add(provincia76);

		Provincia provincia103 = new Provincia(); // VIBO VALENTIA
		provincia103.setCodice(103);
		provincia103.setNome("VIBO VALENTIA");
		listaProvinceCalabria.add(provincia103);


		/*
		 * SICILIA
		 */
		Provincia provincia1 = new Provincia(); // AGRIGENTO
		provincia1.setCodice(1);
		provincia1.setNome("AGRIGENTO");
		listaProvinceSicilia.add(provincia1);

		Provincia provincia18 = new Provincia(); // CALTANISSETTA
		provincia18.setCodice(18);
		provincia18.setNome("CALTANISSETTA");
		listaProvinceSicilia.add(provincia18);

		Provincia provincia21 = new Provincia(); // CATANIA
		provincia21.setCodice(21);
		provincia21.setNome("CATANIA");
		listaProvinceSicilia.add(provincia21);

		Provincia provincia29 = new Provincia(); // ENNA
		provincia29.setCodice(29);
		provincia29.setNome("ENNA");
		listaProvinceSicilia.add(provincia29);

		Provincia provincia53 = new Provincia(); // MESSINA
		provincia53.setCodice(53);
		provincia53.setNome("MESSINA");
		listaProvinceSicilia.add(provincia53);

		Provincia provincia61 = new Provincia(); // PALERMO
		provincia61.setCodice(61);
		provincia61.setNome("PALERMO");
		listaProvinceSicilia.add(provincia61);

		Provincia provincia74 = new Provincia(); // RAGUSA
		provincia74.setCodice(74);
		provincia74.setNome("RAGUSA");
		listaProvinceSicilia.add(provincia74);

		Provincia provincia86 = new Provincia(); // SIRACUSA
		provincia86.setCodice(86);
		provincia86.setNome("SIRACUSA");
		listaProvinceSicilia.add(provincia86);

		Provincia provincia92 = new Provincia(); // TRAPANI
		provincia92.setCodice(92);
		provincia92.setNome("TRAPANI");
		listaProvinceSicilia.add(provincia92);


		/*
		 * SARDEGNA
		 */
		Provincia provincia17 = new Provincia(); // CAGLIARI
		provincia17.setCodice(17);
		provincia17.setNome("CAGLIARI");
		listaProvinceSardegna.add(provincia17);

		Provincia provincia58 = new Provincia(); // NUORO
		provincia58.setCodice(58);
		provincia58.setNome("NUORO");
		listaProvinceSardegna.add(provincia58);

		Provincia provincia59 = new Provincia(); // ORISTANO
		provincia59.setCodice(59);
		provincia59.setNome("ORISTANO");
		listaProvinceSardegna.add(provincia59);

		Provincia provincia83 = new Provincia(); // SASSARI
		provincia83.setCodice(83);
		provincia83.setNome("SASSARI");
		listaProvinceSardegna.add(provincia83);



		/*
		 * Aggiungo alla mappa delle province (TOTALE: 103)
		 */
		mappaProvince.put(1 , listaProvincePiemonte           ); //  8
		mappaProvince.put(2 , listaProvinceValleDAosta        ); //  1
		mappaProvince.put(3 , listaProvinceLombardia          ); // 11
		mappaProvince.put(4 , listaProvinceTrentinoAltoAdige  ); //  2
		mappaProvince.put(5 , listaProvinceVeneto             ); //  7
		mappaProvince.put(6 , listaProvinceFriuliVeneziaGiulia); //  4
		mappaProvince.put(7 , listaProvinceLiguria            ); //  4
		mappaProvince.put(8 , listaProvinceEmiliaRomagna      ); //  9
		mappaProvince.put(9 , listaProvinceToscana            ); // 10
		mappaProvince.put(10, listaProvinceUmbria             ); //  2
		mappaProvince.put(11, listaProvinceMarche             ); //  4
		mappaProvince.put(12, listaProvinceLazio              ); //  5
		mappaProvince.put(13, listaProvinceAbruzzo            ); //  4
		mappaProvince.put(14, listaProvinceMolise             ); //  2
		mappaProvince.put(15, listaProvinceCampania           ); //  5
		mappaProvince.put(16, listaProvincePuglia             ); //  5
		mappaProvince.put(17, listaProvinceBasilicata         ); //  2
		mappaProvince.put(18, listaProvinceCalabria           ); //  5
		mappaProvince.put(19, listaProvinceSicilia            ); //  9
		mappaProvince.put(20, listaProvinceSardegna           ); //  4


	}


	@Override
	public Provincia findBySiglia(String siglaProvincia) {
		for (Entry<Integer, List<Provincia>> listaProvinceOfOneRegione : mappaProvince.entrySet()) {
			for (Provincia provincia : listaProvinceOfOneRegione.getValue()) {
				if(siglaProvincia.equals(provincia.getSigla())) {
					return provincia;
				}
			}
	    }
		return null;
	}

	
	@Override
	public Integer getCodiceRegione(Provincia provincia) {
		for (Entry<Integer, List<Provincia>> listaProvinceOfOneRegione : mappaProvince.entrySet()) {
			for (Provincia p : listaProvinceOfOneRegione.getValue()) {
				if(provincia.equals(p)) {
					return listaProvinceOfOneRegione.getKey();
				}
			}
	    }
		return null;
	}
	
}
