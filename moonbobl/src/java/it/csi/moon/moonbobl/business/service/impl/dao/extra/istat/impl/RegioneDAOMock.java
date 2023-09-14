/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.service.impl.dao.extra.istat.RegioneDAO;
import it.csi.moon.moonbobl.dto.extra.istat.Regione;

/**
 * DAO per l'accesso alle regioni MOCK DATI FASULLI
 * 
 * @see Regione
 * 
 * @author Laurent
 * 
 * @since 1.0.0
 */
@Component
@Qualifier("mock")
public class RegioneDAOMock implements RegioneDAO {

	/**  */
	private ArrayList<Regione> listaRegioni;


	/**
	 *
	 */
	public RegioneDAOMock() {
		init();
	}



	/**
	 *
	 * @return
	 */
	@Override
	public ArrayList<Regione> findAll() {
		return listaRegioni;
	}


	/**
	 * 
	 * @param codice
	 * @return
	 */
	@Override
	public Regione findByPK(Integer codice) {
		for (Regione regione : listaRegioni) {
			if (regione.getCodice().equals(codice)) {
				return regione;
			}
		}
		return null;
	}
	
	@Override
	public Regione findByPKForce(Integer codice) {
		return findByPK(codice);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// DATI FASULLI
	////////////////////////////////////////////////////////////////////////////////////////////////////////////

	private void init() {
		listaRegioni = new ArrayList<Regione>();

		Regione piemonte            = new Regione();
		Regione umbria              = new Regione();
		Regione marche              = new Regione();
		Regione lazio               = new Regione();
		Regione abruzzo             = new Regione();
		Regione molise              = new Regione();
		Regione campania            = new Regione();
		Regione puglia              = new Regione();
		Regione basilicata          = new Regione();
		Regione calabria            = new Regione();
		Regione sicilia             = new Regione();
		Regione valleDAosta         = new Regione();
		Regione sardegna            = new Regione();
		Regione lombardia           = new Regione();
		Regione trentinoAltoAdige   = new Regione();
		Regione veneto              = new Regione();
		Regione friuliVeneziaGiulia = new Regione();
		Regione liguria             = new Regione();
		Regione emiliaRomagna       = new Regione();
		Regione toscana             = new Regione();


		piemonte.setCodice(1);
		umbria.setCodice(10);
		marche.setCodice(11);
		lazio.setCodice(12);
		abruzzo.setCodice(13);
		molise.setCodice(14);
		campania.setCodice(15);
		puglia.setCodice(16);
		basilicata.setCodice(17);
		calabria.setCodice(18);
		sicilia.setCodice(19);
		valleDAosta.setCodice(2);
		sardegna.setCodice(20);
		lombardia.setCodice(3);
		trentinoAltoAdige.setCodice(4);
		veneto.setCodice(5);
		friuliVeneziaGiulia.setCodice(6);
		liguria.setCodice(7);
		emiliaRomagna.setCodice(8);
		toscana.setCodice(9);



		piemonte           .setNome("PIEMONTE");
		umbria             .setNome("UMBRIA");
		marche             .setNome("MARCHE");
		lazio              .setNome("LAZIO");
		abruzzo            .setNome("ABRUZZO");
		molise             .setNome("MOLISE");
		campania           .setNome("CAMPANIA");
		puglia             .setNome("PUGLIA");
		basilicata         .setNome("BASILICATA");
		calabria           .setNome("CALABRIA");
		sicilia            .setNome("SICILIA");
		valleDAosta        .setNome("VALLE D'AOSTA");
		sardegna           .setNome("SARDEGNA");
		lombardia          .setNome("LOMBARDIA");
		trentinoAltoAdige  .setNome("TRENTINO ALTO ADIGE");
		veneto             .setNome("VENETO");
		friuliVeneziaGiulia.setNome("FRIULI VENEZIA GIULIA");
		liguria            .setNome("LIGURIA");
		emiliaRomagna      .setNome("EMILIA ROMAGNA");
		toscana            .setNome("TOSCANA");



		listaRegioni.add(piemonte           );
		listaRegioni.add(umbria             );
		listaRegioni.add(marche             );
		listaRegioni.add(lazio              );
		listaRegioni.add(abruzzo            );
		listaRegioni.add(molise             );
		listaRegioni.add(campania           );
		listaRegioni.add(puglia             );
		listaRegioni.add(basilicata         );
		listaRegioni.add(calabria           );
		listaRegioni.add(sicilia            );
		listaRegioni.add(valleDAosta        );
		listaRegioni.add(sardegna           );
		listaRegioni.add(lombardia          );
		listaRegioni.add(trentinoAltoAdige  );
		listaRegioni.add(veneto             );
		listaRegioni.add(friuliVeneziaGiulia);
		listaRegioni.add(liguria            );
		listaRegioni.add(emiliaRomagna      );
		listaRegioni.add(toscana            );

	}

	

}
