/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class BigDecimal2Serializer extends JsonSerializer<BigDecimal> {
	
    private static final String PATTERN_2DECIMAL = "##0.00";

	@Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if (null == value) {
            //write the word 'null' if there's no value available
            jgen.writeNull();
        } else {
            final String output = format2BigDecimal(value);
            jgen.writeNumber(output);
        }
    }

	protected String format2BigDecimal(BigDecimal value) {
		final DecimalFormat myFormatter = new DecimalFormat(PATTERN_2DECIMAL);
		final String result = myFormatter.format(value.setScale(2, RoundingMode.HALF_UP));
		return result.replace(",", ".");
	}
}
