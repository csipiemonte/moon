/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package org.objectweb.asm;

import net.sf.cglib.core.DebuggingClassWriter;

public class MyClassWriter extends DebuggingClassWriter {

	public MyClassWriter(int flags) {
		super(flags);
	}

	@Override
	public void visit(int a, int b, String c, String d, String e, String[] f) {
        // do nothing
	}

}
