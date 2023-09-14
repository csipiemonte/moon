/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.be.impl;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.be.IndexApi;
import it.csi.moon.moonsrv.business.service.IndexService;
import it.csi.moon.moonsrv.business.service.PrintIstanzeService;
import it.csi.moon.moonsrv.business.service.impl.dao.IndexDAO;
import it.csi.moon.moonsrv.business.service.impl.dao.IstanzaDAO;
import it.csi.moon.moonsrv.exceptions.business.BusinessException;
import it.csi.moon.moonsrv.exceptions.service.ServiceException;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.doqui.index.cxfclient.Content;
import it.doqui.index.cxfclient.SearchResponse;



@Component
public class IndexApiImpl implements IndexApi{

	private static final String CLASS_NAME = "IndexApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	IndexService indexService;
	@Autowired
	IndexDAO indexDAO;
	@Autowired
	IstanzaDAO istanzaDAO;
	@Autowired
	PrintIstanzeService printIstanzeService;
	
	
	@Override
	public Response getContentByUid(String uid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			byte[] ris = indexService.getContentByUid(uid);
			return Response.ok(ris).build(); 
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::getContentByUid] BusinessException");
			throw new ServiceException(be);
		}
	}
	
	@Override
	public Response archiviaIndexByModulo(String codiceOrIdModulo,  Date data, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String numIstProc = indexService.archiviaIndexByModulo(codiceOrIdModulo,data);
			String ris = "Istanze archiviate modulo="+codiceOrIdModulo+" fino al="+data+" numero istanze processate="+numIstProc;
			return Response.ok(ris).build(); 
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::archiviaIndexByModulo] BusinessException");
			throw new ServiceException(be);
		}
	}
	
	@Override
	public Response deleteIndexByModulo(String codiceOrIdModulo, Date data, SecurityContext securityContext,
			HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			String ris = indexService.deleteContentByModulo(codiceOrIdModulo,data);
			return Response.ok(ris).build();
		} catch (BusinessException be) {
			LOG.error("[" + CLASS_NAME + "::deleteIndexByModulo] BusinessException");
			throw new ServiceException(be);
		}
	}
	
	/***************x test***************/
	
	@Override
	public Response ricercaByPath(String path, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			String s = "PATH:\""+path+"\"";
			SearchResponse ris = indexDAO.luceneSearchTest(s);
			
			String result = formatResult(ris); 
			return Response.ok(result).build(); 
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::ricercaByPath] ",e);
			throw new ServiceException(e);
		}
	}
	
	@Override
	public Response ricerca(String q, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			q = q.replace("moon", "moon\\");
			SearchResponse ris = indexDAO.luceneSearchTest(q);
			
			String result = formatResult(ris); 
			return Response.ok(result).build(); 
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::ricerca] ",e);
			throw new ServiceException(e);
		}
	}

	private String formatResult(SearchResponse response) {
		StringBuilder result = new StringBuilder();
		Content[] contents = new Content[response.getContentArray().size()];
        response.getContentArray().toArray(contents);
//        result.append("Found Got ").append(contents == null ? 0 : response.getContentArray().size()+" items\n");
        result.append("Found Got ").append(response.getContentArray().size()).append(" items\n");
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                Content content = contents[i];
                String uuid = content.getUid();
                result.append("UUID["+i+"] = "+uuid+" pref_name= "+contents[i].getPrefixedName()+" CP_pref_name= "+contents[i].getContentPropertyPrefixedName());
                content.getProperties().stream().filter(f -> f.getPrefixedName().contains("moon:")).forEach(p -> result.append(p.getPrefixedName()+"="+p.getValues()+" "));
                result.append("\n");
            }
        }
        return result.toString();
	}

	@Override
	public Response deleteContentIstanza(Long idIstanza, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			String ris = indexService.deleteContentIstanzaById(idIstanza);
			return Response.ok(ris).build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::deleteContentIstanza] ",e);
			throw new ServiceException(e);
		}
	}

	@Override
	public Response getPathByUid(String uid, SecurityContext securityContext, HttpHeaders httpHeaders,
			HttpServletRequest httpRequest) {
		try {
			String ris = indexDAO.getPathByUid(uid);
			return Response.ok(ris).build();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getPathByUid] ",e);
			throw new ServiceException(e);
		}
	}
}
