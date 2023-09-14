/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class BigDecimal2Serializer extends JsonSerializer<BigDecimal> {
    @Override
    public void serialize(BigDecimal value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonGenerationException {
        if (null == value) {
            //write the word 'null' if there's no value available
            jgen.writeNull();
        } else {
            final String output = format2BigDecimal(value);
            jgen.writeNumber(output);
        }
    }

	protected String format2BigDecimal(BigDecimal value) {
		final String pattern = "##0.00";
		//final String pattern = "###,###,##0.00";
		final DecimalFormat myFormatter = new DecimalFormat(pattern);
		final String output = myFormatter.format(value.setScale(2, RoundingMode.HALF_UP));
		return output.replace(",", ".");
	}
}
