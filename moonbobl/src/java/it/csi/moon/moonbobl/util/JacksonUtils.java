/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.util;

import java.util.Iterator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

public final class JacksonUtils {

	private JacksonUtils() {
	}

	public static JsonNode merge(JsonNode mainNode, JsonNode updateNode) {
		Iterator<String> fieldNames = updateNode.getFieldNames();

		while (fieldNames.hasNext()) {

			String fieldName = fieldNames.next();
			JsonNode jsonNode = mainNode.get(fieldName);

			if (jsonNode != null) {
				if (jsonNode.isObject()) {
					merge(jsonNode, updateNode.get(fieldName));
				} else if (jsonNode.isArray()) {
					for (int i = 0; i < jsonNode.size(); i++) {
						merge(jsonNode.get(i), updateNode.get(fieldName).get(i));
					}
				}
			} else {
				if (mainNode instanceof ObjectNode) {
					// Overwrite field
					JsonNode value = updateNode.get(fieldName);
					if (value.isNull()) {
						continue;
					}
					if (value.isIntegralNumber() && value.toString().equals("0")) {
						continue;
					}
					if (value.isFloatingPointNumber() && value.toString().equals("0.0")) {
						continue;
					}
					((ObjectNode) mainNode).put(fieldName, value);
				}
			}
		}

		return mainNode;
	}
}