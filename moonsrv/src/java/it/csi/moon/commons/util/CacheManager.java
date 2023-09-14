/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import it.csi.moon.commons.dto.CacheInfo;
import it.csi.moon.commons.util.decodifica.DecodificaMoonCache;


public class CacheManager {
	
	private static CacheManager INSTANCE;
	private static ConcurrentHashMap<String,MoonCache> cacheManager = new ConcurrentHashMap<>();
	
	
	private CacheManager() {        
    }
    
    public static CacheManager getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CacheManager();
        }
        return INSTANCE;
    }

    public MoonCache getCache(DecodificaMoonCache dm) {
    	MoonCache result = this.cacheManager.get(dm.getCodice());
    	if(result == null) {
    		result = new MoonCache(dm);
    		this.cacheManager.put(dm.getCodice(), result);
    	}
    	return result;
    }
    
    public Optional<MoonCache> getCacheByCodice(String codice) {
    	this.cacheManager.forEachKey(0, System.out::print);
    	if( !this.cacheManager.containsKey(codice) ) {
    		return Optional.empty();
    	}
    	MoonCache result = this.cacheManager.get(codice);
    	return Optional.ofNullable(result);
    }

    public List<CacheInfo> getInfo() {
    	List<CacheInfo> list = new ArrayList<>();
    	CacheInfo c = null;
    	for(Map.Entry<String, MoonCache> entry : this.cacheManager.entrySet() ) {
    		DecodificaMoonCache d = DecodificaMoonCache.byCodice(entry.getKey());
    		c = new CacheInfo();
    		c.setCodice(entry.getKey());
    		c.setNome(d.getNome());
    		c.setCount(entry.getValue().count());
    		c.setLastReset(Timestamp.valueOf(entry.getValue().getLastResetCache()));
    		list.add(c);
    		c = null;
    	}
    	return list;
    }
}
