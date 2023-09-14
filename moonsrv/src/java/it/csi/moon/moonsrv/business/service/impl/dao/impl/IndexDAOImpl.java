/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonsrv.business.service.impl.dao.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import it.csi.moon.moonsrv.business.service.impl.dao.IndexDAO;
import it.csi.moon.moonsrv.exceptions.business.DAOException;
import it.csi.moon.moonsrv.util.AspectIndex;
import it.csi.moon.moonsrv.util.EnvProperties;
import it.csi.moon.moonsrv.util.LoggerAccessor;
import it.doqui.index.cxfclient.Aspect;
import it.doqui.index.cxfclient.Content;
import it.doqui.index.cxfclient.EcmEngineTransactionException_Exception;
import it.doqui.index.cxfclient.InsertException_Exception;
import it.doqui.index.cxfclient.InvalidCredentialsException_Exception;
import it.doqui.index.cxfclient.InvalidParameterException_Exception;
import it.doqui.index.cxfclient.MtomOperationContext;
import it.doqui.index.cxfclient.NoSuchNodeException_Exception;
import it.doqui.index.cxfclient.Node;
import it.doqui.index.cxfclient.NodeResponse;
import it.doqui.index.cxfclient.PermissionDeniedException_Exception;
import it.doqui.index.cxfclient.Property;
import it.doqui.index.cxfclient.ReadException_Exception;
import it.doqui.index.cxfclient.SearchException_Exception;
import it.doqui.index.cxfclient.SearchParams;
import it.doqui.index.cxfclient.SearchResponse;
import it.doqui.index.cxfclient.StreamingService;
import it.doqui.index.cxfclient.StreamingService_Service;
import it.doqui.index.cxfclient.SystemException_Exception;
import it.doqui.index.cxfclient.TooManyResultsException_Exception;

@Component
public class IndexDAOImpl implements IndexDAO{

	private static final String CLASS_NAME = "IndexDAOImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerIntegration();
	//private String path = "PATH:\"/app:company_home/\"";
	private StreamingService delegate; //getDelegate();
	private MtomOperationContext operator;  //getMOC();
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	@Override
	public String luceneSearch(String path) {
		try {
				LOG.info("[" + CLASS_NAME + "::luceneSearch] path="+path);
				String ris=null;
				SearchParams searchParams = new SearchParams();
				searchParams.setPageIndex(0);
				searchParams.setPageSize(10);
				searchParams.setLuceneQuery(path);
			
				NodeResponse response = getDelegate().luceneSearchNoMetadata(searchParams, getOperator());
				if(response.getNodeArray().size() != 0) {
					ris = response.getNodeArray().get(0).getUid(); 
				}
				return ris;
				
			} catch (PermissionDeniedException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] PermissionDeniedException_Exception: ",e);
				throw new DAOException(e);
			} catch (SearchException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] SearchException_Exception: ",e);
				throw new DAOException(e);
			} catch (EcmEngineTransactionException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] EcmEngineTransactionException_Exception: ",e);
				throw new DAOException(e);
			} catch (InvalidParameterException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] InvalidParameterException_Exception: ",e);
				throw new DAOException(e);
			} catch (TooManyResultsException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] TooManyResultsException_Exception: ",e);
				throw new DAOException(e);
			} catch (InvalidCredentialsException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::luceneSearch] InvalidCredentialsException_Exception: ",e);
				throw new DAOException(e);
			}
	}
	
	@Override
	public SearchResponse luceneSearchTest(String path) {
		try {
			LOG.info("[" + CLASS_NAME + "::luceneSearchTest] path="+path);
			SearchParams searchParams = new SearchParams();
			searchParams.setPageIndex(0);
			//searchParams.setPageSize(10);
			searchParams.setLuceneQuery(path);
		
			//NodeResponse response = delegate.luceneSearchNoMetadata(searchParams, operator);
			SearchResponse response = getDelegate().luceneSearch(searchParams, getOperator());
			return response;
		} catch (SystemException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::luceneSearchTest] SystemException_Exception: ",e);
		}
		return null;
	}
	
	private StreamingService getDelegate() {
		try {
			 if(delegate != null) return delegate;
			 //LOG.info("Istanza delegate");
			 //URL wsdl = new URL("http://tst-applogic.reteunitaria.piemonte.it/ecmenginecxf-exp03/ws/streamingWS?wsdl");
			 URL wsdl = new URL(EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_ENDPOINT));
			 StreamingService_Service streamService = new StreamingService_Service(wsdl);
			 delegate = streamService.getPort(StreamingService.class);
			 return delegate;
		} catch (MalformedURLException mu) {
			LOG.error("[" + CLASS_NAME + "::getDelegate] Errore URL: ");
			throw new DAOException();
		} catch (Exception e) {
			LOG.error("[" + CLASS_NAME + "::getDelegate] Errore : ",e);
			throw e;
		}
	}
	
	private MtomOperationContext getOperator() { 
		 if(operator !=null ) return operator;
		 
		 operator = new MtomOperationContext();
		 //operator.setUsername("admin@MOON.COTO");
		 //operator.setPassword("admin20");
		 //operator.setFruitore("MOON");// prova cn abc
		 operator.setUsername("admin@"+EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_TENANT));
		 operator.setPassword(EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_PASSWORD));
		 operator.setRepository("primary");
		 operator.setFruitore(EnvProperties.readFromFile(EnvProperties.DOQUI_INDEX_FRUITORE));
		 return operator;
	}
	
	@Override
	public String creaFolder(String uid_parent, String name_folder) {
	try {
			LOG.info("[" + CLASS_NAME + "::creaFolder] nomefolder="+name_folder);
			Node parent = new Node();
			parent.setUid(uid_parent);
			Content content = new Content();
			content.setPrefixedName("cm:"+name_folder);
			content.setParentAssocTypePrefixedName("cm:contains");
			content.setTypePrefixedName("cm:folder");
					
			//Node result = delegate.createContent(parent, content, operator);
			Node result = getDelegate().createContent(parent, content, getOperator());
			
			return result.getUid();
		} catch (PermissionDeniedException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] PermissionDeniedException_Exception: ",e);
			throw new DAOException(e);
		} catch (EcmEngineTransactionException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] EcmEngineTransactionException_Exception: ",e);
			throw new DAOException(e);
		} catch (InvalidParameterException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] InvalidParameterException_Exception: ",e);
			throw new DAOException(e);
		} catch (InsertException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] InsertException_Exception: ",e);
			throw new DAOException(e);
		} catch (InvalidCredentialsException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] InvalidCredentialsException_Exception: ",e);
			throw new DAOException(e);
		} catch (NoSuchNodeException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolder] NoSuchNodeException_Exception: ",e);
			throw new DAOException(e);
		}
	}
	
	@Override
	public String creaFolderWithAspect(String uid_parent, String name_folder, AspectIndex aspectIndex) {
	try {
			LOG.info("[" + CLASS_NAME + "::creaFolderWithAspect] nomefolder="+name_folder);
			Node parent = new Node();
			parent.setUid(uid_parent);
			Content content = new Content();
			content.setPrefixedName("cm:"+name_folder);
			content.setParentAssocTypePrefixedName("cm:contains");
			content.setTypePrefixedName("cm:folder");
			
			Aspect aspect = new Aspect();
			aspect.setPrefixedName("moon:Istanza");
			aspect.setModelPrefixedName("moon");
			List<Property> properties = creaPropertyForIstanzaAspect(aspectIndex);
			aspect.getProperties().addAll(properties);
			content.getAspects().add(aspect);
			
			//Node result = delegate.createContent(parent, content, operator);
			Node result = getDelegate().createContent(parent, content, getOperator());
			
			return result.getUid();
		} catch (PermissionDeniedException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] PermissionDeniedException_Exception: ",e);
			throw new DAOException(e);
		} catch (EcmEngineTransactionException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] EcmEngineTransactionException_Exception: ",e);
			throw new DAOException(e);
		} catch (InvalidParameterException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] InvalidParameterException_Exception: ",e);
			throw new DAOException(e);
		} catch (InsertException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] InsertException_Exception: ",e);
			throw new DAOException(e);
		} catch (InvalidCredentialsException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] InvalidCredentialsException_Exception: ",e);
			throw new DAOException(e);
		} catch (NoSuchNodeException_Exception e) {
			LOG.error("[" + CLASS_NAME + "::creaFolderWithAspect] NoSuchNodeException_Exception: ",e);
			throw new DAOException(e);
		}
	}

	private List<Property> creaPropertyForIstanzaAspect(AspectIndex aspectIndex) {
		List<Property> ris = new ArrayList<>();
		Property p = null;
		
		
		if(aspectIndex.getIdentificativoUtente() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:identificativoUtente");
			p.getValues().add(aspectIndex.getIdentificativoUtente());	
			ris.add(p);
		}
		if(aspectIndex.getDataCreazione() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:dataCreazione");
			p.getValues().add(sdf.format(aspectIndex.getDataCreazione()));
			ris.add(p);
		}
		if(aspectIndex.getCognomeDichiarante() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:cognomeDichiarante");
			p.getValues().add(aspectIndex.getCognomeDichiarante());	
			ris.add(p);
		}
		if(aspectIndex.getNomeDichiarante() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:nomeDichiarante");
			p.getValues().add(aspectIndex.getNomeDichiarante());	
			ris.add(p);
		}
		if(aspectIndex.getCodiceFiscaleDichiarante() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:cfDichiarante");
			p.getValues().add(aspectIndex.getCodiceFiscaleDichiarante());	
			ris.add(p);
		}
		if(aspectIndex.getNumeroProtocollo() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:numeroProtocollo");
			p.getValues().add(aspectIndex.getNumeroProtocollo());	
			ris.add(p);
		}
		if(aspectIndex.getDataProtocollo() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:dataProtocollo");
			p.getValues().add(sdf.format(aspectIndex.getDataProtocollo()));	
			ris.add(p);
		}
		if(aspectIndex.getCodiceIstanza() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:codiceIstanza");
			p.getValues().add(aspectIndex.getCodiceIstanza());	
			ris.add(p);
		}
		if(aspectIndex.getCodiceEnte() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:codiceEnte");
			p.getValues().add(aspectIndex.getCodiceEnte());	
			ris.add(p);
		}
		if(aspectIndex.getCodiceModulo() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:codiceModulo");
			p.getValues().add(aspectIndex.getCodiceModulo());	
			ris.add(p);
		}
		if(aspectIndex.getOggettoModulo() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:oggettoModulo");
			p.getValues().add(aspectIndex.getOggettoModulo());	
			ris.add(p);
		}
		if(aspectIndex.getVersioneModulo() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:Istanza");
			p.setPrefixedName("moon:versioneModulo");
			p.getValues().add(aspectIndex.getVersioneModulo());	
			ris.add(p);
		}
		
		return ris;
	}
	
	private List<Property> creaPropertyForFileAspect(AspectIndex aspectIndex) {
		List<Property> ris = new ArrayList<>();
		Property p = null;
		
		if(aspectIndex.getNomeFile() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:File");
			p.setPrefixedName("moon:nomeFile");
			p.getValues().add(aspectIndex.getNomeFile());	
			ris.add(p);
		}
		if(aspectIndex.getCodiceFile() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:File");
			p.setPrefixedName("moon:codiceFile");
			p.getValues().add(aspectIndex.getCodiceFile());	
			ris.add(p);
		}
		if(aspectIndex.getHashFile() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:File");
			p.setPrefixedName("moon:hashFile");
			p.getValues().add(aspectIndex.getHashFile());	
			ris.add(p);
		}
		if(aspectIndex.getCodiceTipologia() != null) {
			p= new Property();
			p.setRelativeAspectPrefixedName("moon:File");
			p.setPrefixedName("moon:codiceTipologia");
			p.getValues().add(aspectIndex.getCodiceTipologia());	
			ris.add(p);
		}
				
		return ris;
	}

	@Override
	public String creaDocumento(String uid_parent, String nomeFile, byte[] bytes, String mimeType, AspectIndex aspectIndex) {
		try {
			LOG.info("[" + CLASS_NAME + "::creaDocumento] nomefile="+nomeFile);
			Node parent = new Node();
			parent.setUid(uid_parent);
			Content content = new Content();
			content.setPrefixedName("cm:"+nomeFile);
			content.setTypePrefixedName("cm:content");
			content.setParentAssocTypePrefixedName("cm:contains");
			content.setContent(bytes);
			content.setMimeType(mimeType);
			content.setContentPropertyPrefixedName("cm:content");
			content.setEncoding("UTF-8");
			
			Aspect aspect = new Aspect();
			aspect.setPrefixedName("moon:File");
			aspect.setModelPrefixedName("moon");
			List<Property> propertiesFile = creaPropertyForFileAspect(aspectIndex);
			List<Property> propertiesIstanza = creaPropertyForIstanzaAspect(aspectIndex);
			aspect.getProperties().addAll(propertiesFile);
			aspect.getProperties().addAll(propertiesIstanza);
			content.getAspects().add(aspect);
					
			//Node result = delegate.createContent(parent, content, operator);
			Node result = getDelegate().createContent(parent, content, getOperator());
			return result.getUid();
			} catch (PermissionDeniedException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] PermissionDeniedException_Exception: ",e);
				throw new DAOException(e);
			} catch (EcmEngineTransactionException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] EcmEngineTransactionException_Exception: ",e);
				throw new DAOException(e);
			} catch (InvalidParameterException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] InvalidParameterException_Exception: ",e);
				throw new DAOException(e);
			} catch (InsertException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] InsertException_Exception: ",e);
				throw new DAOException(e);
			} catch (InvalidCredentialsException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] InvalidCredentialsException_Exception: ",e);
				throw new DAOException(e);
			} catch (NoSuchNodeException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::creaDocumento] NoSuchNodeException_Exception: ",e);
				throw new DAOException(e);
			}
	}

	@Override
	public byte[] getContentByUid(String uid) {
		try {
			LOG.info("[" + CLASS_NAME + "::getContentByUid] uid="+uid);
			Node node = new Node();
			node.setUid(uid);
			Content content = new Content();
			content.setContentPropertyPrefixedName("cm:content");
			byte[] result = getDelegate().retrieveContentData(node,content,getOperator());
			return result;
		} catch (PermissionDeniedException_Exception | EcmEngineTransactionException_Exception
					| ReadException_Exception | InvalidParameterException_Exception
					| InvalidCredentialsException_Exception | NoSuchNodeException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::getContentByUid] "+e.getClass()+": ",e);
				throw new DAOException();
		}
	}

	@Override
	public String getPathByUid(String uid) {
		try {
			LOG.info("[" + CLASS_NAME + "::getPathByUid] uid="+uid);
			Node node = new Node();
			node.setUid(uid);
			
			StringBuilder result = new StringBuilder();
			getDelegate().getPaths(node, getOperator()).forEach(p -> result.append(p.getPath()));
			return result.toString();
		} catch (PermissionDeniedException_Exception | InvalidParameterException_Exception
					| InvalidCredentialsException_Exception | NoSuchNodeException_Exception | SearchException_Exception e) {
				LOG.error("[" + CLASS_NAME + "::getContentByUid] "+e.getClass()+": ",e);
				throw new DAOException();
		} 
	}
	
	

}
