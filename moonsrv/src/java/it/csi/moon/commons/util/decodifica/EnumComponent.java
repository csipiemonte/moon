/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util.decodifica;

import java.util.HashMap;
import java.util.Map;

public enum EnumComponent {

    MOONSRV(0),
    MOONPRINT(1),
    MOONFOBL(2),
    MOONBOBL(3),
    MODULISTICA(4),
    MOONBUILDER(5),
    ;

    /**
    * Value for this component
    */
    public final int value;

    private EnumComponent(int valore)
    {
    	value = valore;
    }

    // Mapping idComponent to EnumComponent
    private static final Map<Integer, EnumComponent> _map = new HashMap<>();
    static
    {
        for (EnumComponent difficulty : EnumComponent.values())
            _map.put(difficulty.value, difficulty);
    }

    /**
    * Get component from value idComponent
    * @param idComponent Value
    * @return EnumComponent
    */
    public static EnumComponent from(int idComponent)
    {
        return _map.get(idComponent);
    }
}
