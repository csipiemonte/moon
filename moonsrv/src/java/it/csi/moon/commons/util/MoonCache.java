/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.commons.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import it.csi.moon.commons.util.decodifica.DecodificaMoonCache;


public class MoonCache {
	
	private ConcurrentHashMap<String,Object> mooncache = null;

	private static final Duration DURATION_CACHE = Duration.ofMinutes(5);
	private LocalDateTime lastResetCache = LocalDateTime.now();
	
	
	public MoonCache(DecodificaMoonCache dm) {
		this.mooncache = new ConcurrentHashMap<>();
	}
	
	
	private void forceResetCache() {
    	mooncache.clear();
    	lastResetCache = LocalDateTime.now();
    }
	
    public void resetCacheIfNecessary() {
    	if (Duration.between(this.lastResetCache, LocalDateTime.now()).compareTo(DURATION_CACHE)>0) {
    		forceResetCache();
    	}
    }
    
    public void put(String k , Object v) {
    	this.mooncache.put(k, v);
    }

    public Object get(String k) {
    	return this.mooncache.get(k);
    }
    
    public int count() {
    	return this.mooncache.size();
    }
    
    public Enumeration<String> keys() {
    	return this.mooncache.keys();
    }
    
    public void resetAll() {
    	this.mooncache.clear();
    	lastResetCache = LocalDateTime.now();
    }
    
    public void reset(String k) {
    	this.mooncache.remove(k);
    }
    
    public LocalDateTime getLastResetCache() {
		return lastResetCache;
	}
}
