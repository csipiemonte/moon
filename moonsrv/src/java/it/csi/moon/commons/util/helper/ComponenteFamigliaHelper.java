/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.tika.utils.StringUtils;

import it.csi.moon.commons.dto.extra.demografia.ComponenteFamiglia;

public class ComponenteFamigliaHelper {

	/**
	 * Restituisce il ComponenteFamiglia sulla base del CF (use equalsIgnoreCase)
	 * Resituisce null se elenco null o vuota oppure se non trovato
	 * @param codiceFiscale da ricercare
	 * @param componenti lista dei componenti su quale effettuare la ricerca
	 * @return ComponenteFamiglia il componente corrispondente al codiceFiscale ricercato
	 */
	public static ComponenteFamiglia findByCF(String codiceFiscale, List<ComponenteFamiglia> componenti) throws Exception {
		if (StringUtils.isBlank(codiceFiscale)) 
			throw new Exception("ComponenteFamigliaHelper.findByCF() :: codiceFiscale required empty or blank");
		if (componenti==null || componenti.isEmpty()) 
			return null;
		for (ComponenteFamiglia c : componenti) {
			if (codiceFiscale.equalsIgnoreCase(c.getCodiceFiscale())) {
				return c;
			}
		}
		return null;
	}

	public static boolean isDataNascitaInRange(ComponenteFamiglia componente, int annoMin, int annoMax) {
		return isDataNascitaInRange(componente, annoMin, annoMax, 12);
	}
	public static boolean isDataNascitaInRange(ComponenteFamiglia componente, int annoMin, int annoMax, int meseMax) {
		boolean isDataNascitaInRange = false;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate dataNascitaLD = LocalDate.parse(componente.getDataNascita(), formatter);
		//
		LocalDate startDate = LocalDate.of(annoMin,1,1);
		LocalDate endDate = LocalDate.of(annoMax,meseMax,1);
		isDataNascitaInRange = ( (dataNascitaLD.isAfter(startDate) || dataNascitaLD.isEqual(startDate) )   &&
				(dataNascitaLD.isBefore(endDate) || dataNascitaLD.isEqual(endDate)) );
				//!(dataNascitaLD.isBefore(startDate) || dataNascitaLD.isAfter(endDate));
		return isDataNascitaInRange;
	}

	public static boolean isMinorenne(ComponenteFamiglia c) {
		return getAge(c.getDataNascita())<18;
	}
	public static int getAge(String dataNascita) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
		LocalDate dataNascitaLD = LocalDate.parse(dataNascita, formatter);
		return Period.between(
				dataNascitaLD, 
				LocalDateTime.now().toLocalDate() //.truncatedTo(ChronoUnit.DAYS)
			).getYears();
	}
}
