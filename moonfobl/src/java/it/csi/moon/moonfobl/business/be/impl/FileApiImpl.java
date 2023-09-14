/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business.be.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.csi.moon.commons.dto.Allegato;
import it.csi.moon.commons.entity.RepositoryFileEntity;
import it.csi.moon.commons.util.decodifica.DecodificaTipoRepositoryFile;
import it.csi.moon.moonfobl.business.be.FileApi;
import it.csi.moon.moonfobl.business.service.AllegatiService;
import it.csi.moon.moonfobl.business.service.RepositoryFileService;
import it.csi.moon.moonfobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonfobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonfobl.dto.moonfobl.MultipartBodyFormIo;
import it.csi.moon.moonfobl.exceptions.business.BusinessException;
import it.csi.moon.moonfobl.exceptions.business.ItemNotFoundBusinessException;
import it.csi.moon.moonfobl.exceptions.service.FileUploadException;
import it.csi.moon.moonfobl.exceptions.service.ResourceNotFoundException;
import it.csi.moon.moonfobl.exceptions.service.ServiceException;
import it.csi.moon.moonfobl.util.LoggerAccessor;


@Component
public class FileApiImpl extends MoonBaseApiImpl implements FileApi {
	
	private static final String CLASS_NAME = "FileApiImpl";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	RepositoryFileService repositoryFileService;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	
	class SizeEntry {
        public int size;
        public LocalDateTime time;
    }
	
	static Map<String, SizeEntry> sizeMap = new ConcurrentHashMap<>();
    int counter;
    
    
	@Override
	public Response uploadFile(String baseUrl, String form, String project, 
		String filter, String errMsg, String signed, MultipartFormDataInput input, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws FileUploadException {
//		List<Allegato>elencoAllegati = new ArrayList<Allegato>();
		
		LOG.info("[FileApiImpl::uploadFile] BEGIN");
//		LOG.debug("[FileApiImpl::uploadFile] IN baseUrl = " + baseUrl);
//		LOG.debug("[FileApiImpl::uploadFile] IN form = " + form);
//		LOG.debug("[FileApiImpl::uploadFile] IN project = " + project);
		LOG.debug("[FileApiImpl::uploadFile] IN filter = " + filter);
		LOG.debug("[FileApiImpl::uploadFile] IN errMsg = " + errMsg);
		LOG.debug("[FileApiImpl::uploadFile] IN signed = " + signed);
//		input.getFormDataMap().entrySet().forEach( entry -> {
//			String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
//			LOG.debug("[FileApiImpl::uploadFile] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
//		}); 

		String[] filterTypesArr = null;
		if (StringUtils.isNotBlank(filter)) {
			filterTypesArr = filter.split(",");
			LOG.debug("[FileApiImpl::uploadFile] filterTypesArr.len = " + filterTypesArr.length);
		}
		/*
		 * Impostazione codice Allegati
		 */
		String elencoFile = "";
		// nome del file valorizzato da formio
		String formioNameFile = "";
		String formioDir= "";
		try {
			Allegato allegato = new Allegato();
			if (input.getParts().size() > 0) {
				for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {
					
					if (entry.getKey().equalsIgnoreCase("name")) {
						formioNameFile = getFirstMultipartValue(entry.getKey(), input);
						LOG.debug("[FileApiImpl::uploadFile] name: " + formioNameFile);
						allegato.setFormioNameFile(formioNameFile);
					}
					
					if (entry.getKey().equalsIgnoreCase("dir")) {
						formioDir = getFirstMultipartValue(entry.getKey(), input);
						LOG.debug("[FileApiImpl::uploadFile] dir: " + formioDir);
						allegato.setFormioDir(formioDir);
					}
					
					// caso file
					if (entry.getKey().equalsIgnoreCase("file")) {
						List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
						for (InputPart inputPart : inputParts) {

							try {
								LOG.debug("[FileApiImpl::uploadFile] "+entry.getKey());
								MultivaluedMap<String, String> header = inputPart.getHeaders();
								allegato.setFormioKey(entry.getKey());
								allegato.setNomeFile(getFileName(header));
								if (StringUtils.isEmpty(allegato.getFormioNameFile())) {
									allegato.setFormioNameFile(allegato.getNomeFile());
								}
								allegato.setMediaType(inputPart.getMediaType().getType());
								allegato.setMediaSubType(inputPart.getMediaType().getSubtype());
								allegato.setContentType(getContentTypeHeader(header));
								InputStream inputStream = inputPart.getBody(InputStream.class, null);
								allegato.setContenuto(inputStream.readAllBytes());
								allegato.setIpAddress(getIpAddress(httpRequest));
								String contentType = null;
								if (StringUtils.isNotEmpty(filter)) {
									contentType = allegatiService.validaBytesContentTypesContains(allegato.getContenuto(), filterTypesArr);
								}
								if (StringUtils.isNotEmpty(signed)) {
									boolean flagFirmato = allegatiService.validaBytesSigned(allegato.getContenuto(), signed, contentType);
									allegato.setFlagFirmato(flagFirmato);
								}
								
//								elencoAllegati.add(allegato);
								elencoFile += allegato.getNomeFile() + "-" + allegato.getLunghezza();
							} catch (BusinessException be) {
								LOG.warn("[FileApiImpl::uploadFile] BusinessException " + be.getCode() + " - " + be.getMessage());
								throw be;
							} catch (IOException e) {
								LOG.error("[FileApiImpl::uploadFile] Errore upload file", e);
								throw  new FileUploadException();
							}

						}
					}
				}
				//
				if (allegato.isValid()) {
					allegatiService.insert(allegato);
				} else {
					LOG.error("[FileApiImpl::uploadFile] allegato NOT Valid() [contenuto,nomeFile,formioNameFile] " + allegato);
					throw new ServiceException("Parametri obbligatori non valorizzati");
				}
			}
			return Response.status(200).entity("File Caricati : " + elencoFile).build();
		} catch (BusinessException be) {
			LOG.error("[FileApiImpl::uploadFile] BusinessException ", be);
			throw new FileUploadException(be, errMsg);
		} catch (Exception ex) {
			LOG.error("[FileApiImpl::uploadFile] Exception ", ex);
			throw new FileUploadException(errMsg);
		} finally {
			LOG.info("[FileApiImpl::uploadFile] END");
		}
	}
	
	    
	    private String getFileName(MultivaluedMap<String, String> header) {

			String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
			
			for (String filename : contentDisposition) {
				if ((filename.trim().startsWith("filename"))) {

					String[] name = filename.split("=");
					
					String finalFileName = name[1].trim().replace("\"", "");
					return finalFileName;
				}
			}
			return "unknown";
		}
	    
	    
	    private String getContentTypeHeader(MultivaluedMap<String, String> header) {

			String contentType = header.getFirst("Content-Type");
			return (contentType== null ? "unknown" : contentType);
						
		}
	    
	    
	    private String getMultipartValueOrNull(String key,MultipartFormDataInput input) {
	    	String result = null;
	    	try {
	    		result = getFirstMultipartValue(key, input);
	    	} catch (Exception e) {
	    		LOG.warn("[FileApiImpl::getMultipartValueOrNull] Null for key "+key, e);
			}
	    	return result;
	    }
	    private String getFirstMultipartValue(String key, MultipartFormDataInput input) throws Exception {
	    	String value = "";
	    	List<InputPart> inputsPartName = input.getFormDataMap().get(key);
	    	if (!inputsPartName.isEmpty()) {
	    		value = inputsPartName.get(0).getBodyAsString();
	    	}
			return value;
	    }



		@Override
		public Response getFile(String baseUrl, String nameFile, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.info("[FileApiImpl::getFile] BEGIN");
				if (LOG.isDebugEnabled()) {
					LOG.debug("[FileApiImpl::getFile] IN baseUrl = " + baseUrl);
					LOG.debug("[FileApiImpl::getFile] IN form (nameFile)= " + nameFile);
				}
				
//				File file = new File(UPLOADED_FILE_PATH + nameFile.substring(1));
//				return Response.ok(file)
//    		 			.header("Content-Disposition", "attachment; filename=\"" + nameFile + "\"" )
//	 					.header("Content-Type", "image/jpeg")
//	 					.build();
				
				// skip primo caratteri che è /
				Allegato fileAllegato = allegatiService.getByFormIoNameFile(nameFile.substring(1));
				
				return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
					.header("Content-Type", fileAllegato.getContentType())
					.build();
			} catch (Exception ex) {
				LOG.error("[FileApiImpl::getFile] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " + nameFile); 
			} finally {
				LOG.info("[FileApiImpl::getFile] END");
			}
		}
//		
//		@Override
//		public Response getRepositoryFile(String baseUrl, Long idFile, SecurityContext securityContext,
//				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
//			try {
//				LOG.info("[FileApiImpl::getRepositoryFile] BEGIN");
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("[FileApiImpl::getRepositoryFile] IN baseUrl = " + baseUrl);
//					LOG.debug("[FileApiImpl::getRepositoryFile] IN form (nameFile)= " + nameFile);
//				}
//
//				
//				RepositoryFileEntity repositoryFile = repositoryFileService.getRepositoryFile(idFile);
//				
//				return Response.ok(repositoryFile.getContenuto())
//					.header("Content-Disposition", "attachment; filename=\"" + repositoryFile.getNomeFile() + "\"" )
//					.header("Content-Type", repositoryFile.getContentType())
//					.build();
//			} catch (Exception ex) {
//				LOG.error("[FileApiImpl::getFile] Errore upload file ",ex);
//				throw new ServiceException("Errore nel recupero del file " + idFile); 
//			} finally {
//				LOG.info("[FileApiImpl::getRepositoryFile] END");
//			}
//		}	
		
		@Override
		public Response uploadFileNotifica(String baseUrl, String form, String project, MultipartFormDataInput input, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws FileUploadException {
		
			LOG.info("[FileApiImpl::uploadFileNotifica] BEGIN");
			LOG.info("[FileApiImpl::uploadFileNotifica] IN baseUrl = " + baseUrl);
			LOG.info("[FileApiImpl::uploadFileNotifica] IN form = " + form);
			LOG.info("[FileApiImpl::uploadFileNotifica] IN project = " + project);

			input.getFormDataMap().entrySet().forEach( entry -> {
				String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
				LOG.debug("[FileApiImpl::uploadFileNotifica] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
			}); 


			String elencoFile = "";
			// nome del file valorizzato da formio
			String formioNameFile = "";
			String formioDir= "";
			try {
								
				RepositoryFileEntity file = new RepositoryFileEntity();
				file.setDataCreazione(new Date());
				file.setIdTipologia(DecodificaTipoRepositoryFile.FO_ALLEGATO_RISPOSTA_INTEGRAZIONE.getId());
				file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
				if (input.getParts().size() > 0) {
					for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {
						
						if (entry.getKey().equalsIgnoreCase("name")) {
							formioNameFile = getFirstMultipartValue(entry.getKey(), input);
							file.setFormioNameFile(formioNameFile);
						}
						

						// caso file
						if (entry.getKey().equalsIgnoreCase("file")) {
							List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
							for (InputPart inputPart : inputParts) {

								try {
									LOG.info("[FileApiImpl::uploadFileNotifica] key = " + entry.getKey());
									MultivaluedMap<String, String> header = inputPart.getHeaders();
									LOG.info("[FileApiImpl::uploadFileNotifica] header = " + header);
									file.setFormioKey(entry.getKey());
									file.setNomeFile(getFileName(header));
									if (StringUtils.isEmpty(file.getFormioNameFile())) {
										file.setFormioNameFile(file.getNomeFile());
									}
									file.setContentType(getContentTypeHeader(header));
									InputStream inputStream = inputPart.getBody(InputStream.class, null);
									file.setContenuto(inputStream.readAllBytes());
									MessageDigest digest = MessageDigest.getInstance("SHA-256");
							        byte[] hash = digest.digest(file.getContenuto());
							        file.setHashFile(Base64.getEncoder().encodeToString(hash));
							        file.setLunghezza(file.getContenuto().length);
									elencoFile += file.getNomeFile();
								} catch (IOException e) {
									LOG.error("[FileApiImpl::uploadFileNotifica] Errore upload file ", e);
									throw  new FileUploadException();
								}

							}
						}
					}
					//
					if (file.isValid()) {
					 	repositoryFileDAO.insert(file);
					} else {
						LOG.error("[FileApiImpl::uploadFileNotifica] file NOT Valid() [contenuto,nomeFile,formioNameFile] " + file);
						throw new ServiceException("Parametri obbligatori non valorizzati");
					}
				}
				return Response.status(200).entity("File Caricati : " + elencoFile).build();
			}	catch (Exception ex) {
				LOG.error("[FileApiImpl::uploadFileNotifica]- Errore upload file ",ex);
				throw  new FileUploadException();
			} finally {
				LOG.info("[FileApiImpl::uploadFileNotifica]- END");
			}
		}
		
		@Override
		public Response getFileNotifica(String baseUrl, String nameFile, 
			SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.info("[FileApiImpl::getFileNotifica] BEGIN");
								
				// skip primo caratteri che è /
				//RepositoryFileEntity fileAllegato = repositoryFileDAO.findByFormIoNameFile(nameFile.substring(1));
				RepositoryFileEntity fileAllegato = repositoryFileService.findByFormIoNameFile(nameFile.substring(1)); 
				
				return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
					.header("Content-Type", fileAllegato.getContentType())
					.build();
			} catch (Exception ex) {
				LOG.error("[FileApiImpl::getFileNotifica] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " + nameFile); 
			} finally {
				LOG.info("[FileApiImpl::getFileNotifica] END");
			}
		}
		
		@Override
		public Response getFileNotificaFruitore(String urlRef, String nomeFile, Long idIstanza, Long idStoricoWorkflow, 
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.info("[FileApiImpl::getFileNotificaFruitore] BEGIN");

				RepositoryFileEntity fileAllegato = repositoryFileDAO.findByNomeFileIstanzaIdStWf(nomeFile, idIstanza, idStoricoWorkflow);
				byte[] contenuto = null;

				if (urlRef != null && !urlRef.contentEquals("null")) {
					String[] urlParts = urlRef.split("/");
					String idPratica = urlParts[urlParts.length - 2];
					contenuto = moonsrvDAO.getAllegatoFruitore(idPratica);
				} else {
					contenuto = (fileAllegato != null) ? fileAllegato.getContenuto() : null;
				}
				if (fileAllegato==null) {
					LOG.error("[" + CLASS_NAME + "::getFileNotificaFruitore] fileAllegato==null per idIstanza:"
							+ idIstanza + " idStoricoWorkflow:" + idStoricoWorkflow + " nomeFile:" + nomeFile + " urlRef:" + urlRef);
					throw new ResourceNotFoundException();
				} else {
					return Response.ok(contenuto)
							.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"")
							.header("Content-Type", fileAllegato.getContentType()).build();
				}
			} catch (ItemNotFoundBusinessException notFoundEx) {
				LOG.warn("[" + CLASS_NAME + "::getFileNotificaFruitore] notifica non trovata per idIstanza:"
						+ idIstanza + " idStoricoWorkflow:" + idStoricoWorkflow + " nomeFile:" + nomeFile + " urlRef:" + urlRef, notFoundEx);
				throw new ResourceNotFoundException();
			} catch (Exception ex) {
				LOG.error("[FileApiImpl::getFileNotificaFruitore] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " +nomeFile); 
			} finally {
				LOG.info("[FileApiImpl::getFileNotificaFruitore] END");
			}
		
		}


		@Override
		public Response deleteFile(String baseUrl, String form, String project, String input,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				LOG.info("[FileApiImpl::deleteFile] BEGIN");
				LOG.debug(baseUrl);
				LOG.debug(form);
				LOG.debug(project);
				LOG.debug(input);
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode su = mapper.readValue(input, JsonNode.class);
				String nomeFile = su.get("name").asText();
				allegatiService.deleteAllegatoByNameFormio(nomeFile);
				
				return Response.ok().build();
			} catch (Exception e) {
				LOG.error("[FileApiImpl::deleteFile] Errore delete file allegato ",e);
				throw new ServiceException("Errore delete file allegato " );
			}
		}
	
		
	@Override
	public Response uploadRepositoryFile(String baseUrl, String form, String project, MultipartFormDataInput input, String idTipologiaPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws FileUploadException {
	
		LOG.info("[FileApiImpl::uploadRepositoryFile] BEGIN");
		LOG.info("[FileApiImpl::uploadRepositoryFile] IN baseUrl = " + baseUrl);
		LOG.info("[FileApiImpl::uploadRepositoryFile] IN form = " + form);
		LOG.info("[FileApiImpl::uploadRepositoryFile] IN project = " + project);
		LOG.info("[FileApiImpl::uploadRepositoryFile] IN idTipologia = " + idTipologiaPP);

		input.getFormDataMap().entrySet().forEach( entry -> {
			String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
			LOG.debug("[FileApiImpl::uploadRepositoryFile] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
		});

		String elencoFile = "";
		// nome del file valorizzato da formio
		String formioNameFile = "";
		String formioDir= "";
		try {
			Integer idTipologia = validaIntegerRequired(idTipologiaPP);
			RepositoryFileEntity file = new RepositoryFileEntity();
			file.setDataCreazione(new Date());
			file.setIdTipologia(idTipologia);
			file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
			if (input.getParts().size() > 0) {
				for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {
					
					if (entry.getKey().equalsIgnoreCase("name")) {
						formioNameFile = getFirstMultipartValue(entry.getKey(), input);
						file.setFormioNameFile(formioNameFile);
					}

					// caso file
					if (entry.getKey().equalsIgnoreCase("file")) {
						List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
						for (InputPart inputPart : inputParts) {

							try {
								LOG.info("[FileApiImpl::uploadRepositoryFile] key = " + entry.getKey());
								MultivaluedMap<String, String> header = inputPart.getHeaders();
								LOG.info("[FileApiImpl::uploadRepositoryFile] header = " + header);
								file.setFormioKey(entry.getKey());
								file.setNomeFile(getFileName(header));
								if (StringUtils.isEmpty(file.getFormioNameFile())) {
									file.setFormioNameFile(file.getNomeFile());
								}
								file.setContentType(getContentTypeHeader(header));
								InputStream inputStream = inputPart.getBody(InputStream.class, null);
								file.setContenuto(inputStream.readAllBytes());
								MessageDigest digest = MessageDigest.getInstance("SHA-256");
						        byte[] hash = digest.digest(file.getContenuto());
						        file.setHashFile(Base64.getEncoder().encodeToString(hash));
						        file.setLunghezza(file.getContenuto().length);
								elencoFile += file.getNomeFile();
							} catch (IOException e) {
								LOG.error("[FileApiImpl::uploadRepositoryFile] Errore upload file ", e);
								throw  new FileUploadException();
							}

						}
					}
				}
				//
				if (file.isValid())
				 	repositoryFileDAO.insert(file);
				else
					throw new ServiceException("Parametri obbligatori non valorizzati");
			}
			return Response.status(200).entity("File Caricati : " + elencoFile).build();
		}	catch (Exception ex) {
			LOG.error("[FileApiImpl::uploadRepositoryFile]- Errore upload file ",ex);
			throw  new FileUploadException();
		} finally {
			LOG.info("[FileApiImpl::uploadRepositoryFile]- END");
		}
	}
	
	@Override
	public Response getRepositoryFile(String baseUrl, String nameFile, String idTipologiaPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[FileApiImpl::getRepositoryFile] BEGIN");
			Integer idTipologia = validaIntegerRequired(idTipologiaPP);
			
			// skip primo caratteri che è /
			//RepositoryFileEntity fileAllegato = repositoryFileDAO.findByFormIoNameFile(nameFile.substring(1));
			RepositoryFileEntity fileAllegato = repositoryFileService.findByFormIoNameFile(nameFile.substring(1)); 
			
			return Response.ok(fileAllegato.getContenuto())
				.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
				.header("Content-Type", fileAllegato.getContentType())
				.build();
		} catch (Exception ex) {
			LOG.error("[FileApiImpl::getRepositoryFile] Errore upload file ",ex);
			throw new ServiceException("Errore nel recupero del file " + nameFile);
		} finally {
			LOG.info("[FileApiImpl::getRepositoryFile] END");
		}
	}

	@Override
    public Response retrieveContentType(MultipartBodyFormIo data, 
        	SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			LOG.info("[FileApiImpl::retrieveContentType] BEGIN");
			String contentType = allegatiService.retrieveContentType(data.file.readAllBytes());
			return Response.ok(contentType).build();
		} catch (Exception ex) {
			LOG.error("[FileApiImpl::retrieveContentType] Errore upload file ",ex);
			throw new ServiceException("Errore nel recupero del contentType del file " + data.name);
		} finally {
			LOG.info("[FileApiImpl::retrieveContentType] END");
		}
    }
    
}
