/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.epay.impl.coto;

import it.csi.moon.commons.dto.Istanza;
import it.csi.moon.moonsrv.business.service.epay.impl.EpayDefaultDelegate;

public class EpayDelegate_EDIL_DEF_COND_PPAY extends EpayDefaultDelegate {

	public EpayDelegate_EDIL_DEF_COND_PPAY(Istanza istanza) {
		super(istanza);
	}

//	protected List<ComponentePagamento> retrieveComponentiPagamento() {
//		List<ComponentePagamento> result = new ArrayList<ComponentePagamento>();
//		ComponentePagamento cp504 = new ComponentePagamento();
//		cp504.setProgressivo(1);
//		cp504.setImporto(BigDecimal.valueOf(Double.parseDouble("172.00")).setScale(2, RoundingMode.HALF_UP));
//		cp504.setCausale("Diritti Vari");
//		cp504.setDatiSpecificiRiscossione("");
//		cp504.setAnnoAccertamento(String.valueOf(LocalDate.now().getYear()));
//		cp504.setNumeroAccertamento("2147");
//		result.add(cp504);
//		ComponentePagamento cp408 = new ComponentePagamento();
//		cp408.setProgressivo(2);
//		cp408.setImporto(BigDecimal.valueOf(Double.parseDouble("16.00")).setScale(2, RoundingMode.HALF_UP));
//		cp408.setCausale("Altre imposte di Bollo");
//		cp408.setDatiSpecificiRiscossione("");
//		cp408.setAnnoAccertamento(String.valueOf(LocalDate.now().getYear()));
//		cp408.setNumeroAccertamento("2130");
//		result.add(cp408);
//		return result;
//	}
	
}
