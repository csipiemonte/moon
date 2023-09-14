/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonbobl.business.be.impl;

import java.io.File;
import java.io.FileOutputStream;
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
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.csi.moon.moonbobl.business.be.FileApi;
import it.csi.moon.moonbobl.business.service.AllegatiService;
import it.csi.moon.moonbobl.business.service.AuditService;
import it.csi.moon.moonbobl.business.service.RepositoryFileService;
import it.csi.moon.moonbobl.business.service.impl.dao.MoonsrvDAO;
import it.csi.moon.moonbobl.business.service.impl.dao.RepositoryFileDAO;
import it.csi.moon.moonbobl.business.service.impl.dto.AuditEntity;
import it.csi.moon.moonbobl.business.service.impl.dto.RepositoryFileEntity;
import it.csi.moon.moonbobl.dto.moonfobl.Allegato;
import it.csi.moon.moonbobl.dto.moonfobl.UserInfo;
import it.csi.moon.moonbobl.exceptions.business.BusinessException;
import it.csi.moon.moonbobl.exceptions.service.FileUploadException;
import it.csi.moon.moonbobl.exceptions.service.ServiceException;
import it.csi.moon.moonbobl.exceptions.service.UnprocessableEntityException;
import it.csi.moon.moonbobl.util.LoggerAccessor;
import it.csi.moon.moonbobl.util.decodifica.DecodificaTipoRepositoryFile;


@Component
public class FileApiImpl extends MoonBaseApiImpl implements FileApi {
	
	private final static String CLASS_NAME = "FileApiImpl";
	private Logger log = LoggerAccessor.getLoggerBusiness();
	
	@Autowired
	AllegatiService allegatiService;
	@Autowired
	RepositoryFileService repositoryFileService;
	@Autowired
	RepositoryFileDAO repositoryFileDAO;
	@Autowired
	MoonsrvDAO moonsrvDAO;
	@Autowired
	AuditService auditService;
	
	class SizeEntry {
        public int size;
        public LocalDateTime time;
    }
	
	static Map<String, SizeEntry> sizeMap = new ConcurrentHashMap<>();
    int counter;
    
    
	@Override
	public Response uploadFile(String baseUrl, String form, String project, 
		String filter, String errMsg, MultipartFormDataInput input, 
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws FileUploadException {
//		List<Allegato>elencoAllegati = new ArrayList<Allegato>();
		
		log.info("[FileApiImpl::uploadFile] BEGIN");
//		log.debug("[FileApiImpl::uploadFile] IN baseUrl = " + baseUrl);
//		log.debug("[FileApiImpl::uploadFile] IN form = " + form);
//		log.debug("[FileApiImpl::uploadFile] IN project = " + project);
		log.debug("[FileApiImpl::uploadFile] IN filter = " + filter);
		log.debug("[FileApiImpl::uploadFile] IN errMsg = " + errMsg);
//		input.getFormDataMap().entrySet().forEach( entry -> {
//			String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
//			log.debug("[FileApiImpl::uploadFile] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
//		}); 

		String[] filterTypesArr = null;
		if (StringUtils.isNotBlank(filter)) {
			filterTypesArr = filter.split(",");
			log.debug("[FileApiImpl::uploadFile] filterTypesArr.len = " + filterTypesArr.length);
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
						formioNameFile = getMultipartValue(entry.getKey(), input);
						log.debug("[FileApiImpl::uploadFile] name: " + formioNameFile);
						allegato.setFormioNameFile(formioNameFile);
					}
					
					if (entry.getKey().equalsIgnoreCase("dir")) {
						formioDir = getMultipartValue(entry.getKey(), input);
						log.debug("[FileApiImpl::uploadFile] dir: " + formioDir);
						allegato.setFormioDir(formioDir);
					}
					
					// caso file
					if (entry.getKey().equalsIgnoreCase("file")) {
						List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
						for (InputPart inputPart : inputParts) {

							try {
								log.debug("[FileApiImpl::uploadFile] "+entry.getKey());
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
								if (StringUtils.isNotEmpty(filter)) {
									allegatiService.validaBytesTypes(allegato.getContenuto(), filterTypesArr);
								}
								
//								elencoAllegati.add(allegato);
								elencoFile += allegato.getNomeFile() + "-" + allegato.getLunghezza();
							} catch (BusinessException be) {
								log.warn("[FileApiImpl::uploadFile] BusinessException " + be.getCode() + " - " + be.getMessage());
								throw be;
							} catch (IOException e) {
								log.error("[FileApiImpl::uploadFile]- Errore upload file ", e);
								throw  new FileUploadException();
							}

						}
					}
				}
				//
				if (allegato.isValid()) {
					allegatiService.insert(allegato);
				} else {
					log.error("[FileApiImpl::uploadFile] allegato NOT Valid() [contenuto,nomeFile,formioNameFile] " + allegato);
					throw new ServiceException("Parametri obbligatori non valorizzati");
				};
			}
			return Response.status(200).entity("File Caricati : " + elencoFile).build();
		} catch (BusinessException be) {
			log.error("[FileApiImpl::uploadFile] BusinessException ", be);
			throw new FileUploadException(be, errMsg);
		} catch (Exception ex) {
			log.error("[FileApiImpl::uploadFile] Exception ", ex);
			throw new FileUploadException(errMsg);
		} finally {
			log.info("[FileApiImpl::uploadFile] END");
		}
	}
	
	    
	    private String getFileName(MultivaluedMap<String, String> header) {

			String[] contentDisposition = header.getFirst("Content-Disposition").split(";");
			
			for (String filename : contentDisposition) {
				if ((filename.trim().startsWith("filename"))) {

					String[] name = filename.split("=");
					
					String finalFileName = name[1].trim().replaceAll("\"", "");
					return finalFileName;
				}
			}
			return "unknown";
		}
	    
	    
	    private String getContentTypeHeader(MultivaluedMap<String, String> header) {

			String contentType = header.getFirst("Content-Type");
			return (contentType== null ? "unknown" : contentType);
						
		}
	    
	    
	    private void writeFile(byte[] content, String filename) throws IOException {
	    		
	    	
			File file = new File(filename);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileOutputStream fop = new FileOutputStream(file);

			fop.write(content);
			fop.flush();
			fop.close();

		}
	    
	    private String getMultipartValueOrNull(String key,MultipartFormDataInput input) {
	    	String result = null;
	    	try {
	    		result = getMultipartValue(key, input);
	    	} catch (Exception e) {
	    		log.warn("[FileApiImpl::getMultipartValueOrNull] Null for key "+key, e);
			}
	    	return result;
	    }
	    private String getMultipartValue(String key,MultipartFormDataInput input) throws Exception {
	    	String value = "";
	    	List<InputPart> inputsPartName = input.getFormDataMap().get(key);
			for (InputPart inputPart : inputsPartName) {
				value = inputPart.getBodyAsString();
				break;
			}
			return value;
	    }




		@Override
		public Response getFile(String baseUrl, String nameFile, SecurityContext securityContext,
				HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				log.info("[FileApiImpl::getFile] BEGIN");
				if (log.isDebugEnabled()) {
					log.debug("[FileApiImpl::getFile] IN baseUrl = " + baseUrl);
					log.debug("[FileApiImpl::getFile] IN form (nameFile)= " + nameFile);
				}
				
//				File file = new File(UPLOADED_FILE_PATH + nameFile.substring(1));
//				return Response.ok(file)
//    		 			.header("Content-Disposition", "attachment; filename=\"" + nameFile + "\"" )
//	 					.header("Content-Type", "image/jpeg")
//	 					.build();
				
				// skip primo caratteri che è /
				Allegato fileAllegato = allegatiService.getByFormIoNameFile(nameFile.substring(1));
				// Audit Operazione
				try {
					UserInfo user = retrieveUserInfo(httpRequest);
					AuditEntity auditEntity = new AuditEntity(httpRequest.getRemoteAddr(), 
							auditService.retrieveUser(user), 
							AuditEntity.EnumOperazione.READ,
							"getFile-nomeFile", 
							nameFile);
					auditService.traceOperazione(auditEntity);
				} catch (Exception e) {
					log.error("[FileApiImpl::getFile] Errore audit operazione getFile ",e);
				}
				return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
					.header("Content-Type", fileAllegato.getContentType())
					.build();
			} catch (Exception ex) {
				log.error("[FileApiImpl::getFile] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " + nameFile); 
			} finally {
				log.info("[FileApiImpl::getFile] END");
			}
		}

		@Override
		public Response uploadFileNotifica(String baseUrl, String form, String project, MultipartFormDataInput input, String idTipoMydocsQP,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws FileUploadException {
		
			log.info("[FileApiImpl::uploadFileNotifica] BEGIN");
			if (log.isDebugEnabled()) {
				input.getFormDataMap().entrySet().forEach( entry -> {
					String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
					log.debug("[FileApiImpl::uploadFileNotifica] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
				});
			}

			Long idTipologiaMydocs = validaLong(idTipoMydocsQP);
			String elencoFile = "";
			// nome del file valorizzato da formio
			String formioNameFile = "";
			String formioDir= "";
			try {
				RepositoryFileEntity file = new RepositoryFileEntity();
				file.setIdTipologia(DecodificaTipoRepositoryFile.BO_ALLEGATO_RICHIESTA_INTEGRAZIONE.getId());
				file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
				file.setDataCreazione(new Date());
				if (input.getParts().size() > 0) {
					for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {

						if (entry.getKey().equalsIgnoreCase("name")) {
							formioNameFile = getMultipartValue(entry.getKey(), input);
							file.setFormioNameFile(formioNameFile);
						}
						
						// caso file
						if (entry.getKey().equalsIgnoreCase("file")) {
							List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
							for (InputPart inputPart : inputParts) {

								try {
									log.debug("[FileApiImpl::uploadFileNotifica] "+entry.getKey());
									MultivaluedMap<String, String> header = inputPart.getHeaders();
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
							        file.setIdTipologiaMydocs(idTipologiaMydocs);
									
									elencoFile += file.getNomeFile();
								} catch (IOException e) {
									log.error("[FileApiImpl::uploadFile]- Errore upload file ", e);
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
				log.error("[FileApiImpl::uploadFileNotifica]- Errore upload file ",ex);
				throw  new FileUploadException();
			} finally {
				log.info("[FileApiImpl::uploadFileNotifica]- END");
			}
		}
		
		@Override
		public Response getFileNotifica(String baseUrl, String nameFile, 
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				log.info("[FileApiImpl::getFileNotifica] BEGIN");
				
				// skip primo caratteri che è /
				//RepositoryFileEntity fileAllegato = repositoryFileDAO.findByFormIoNameFile(nameFile.substring(1));
				RepositoryFileEntity fileAllegato = repositoryFileService.findByFormIoNameFile(nameFile.substring(1));
				
				return Response.ok(fileAllegato.getContenuto())
					.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
					.header("Content-Type", fileAllegato.getContentType())
					.build();
			} catch (Exception ex) {
				log.error("[FileApiImpl::getFileNotifica] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " + nameFile); 
			} finally {
				log.info("[FileApiImpl::getFileNotifica] END");
			}
		}
		
		@Override
		public Response getFileNotificaFruitore(String urlRef, String nomeFile, Long idIstanza, Long idStoricoWorkflow, 
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				log.info("[FileApiImpl::getFileNotificaFruitore] BEGIN");

				RepositoryFileEntity fileAllegato = repositoryFileDAO.findByNomeFileIstanzaIdStWf(nomeFile, idIstanza, idStoricoWorkflow);
				byte[] contenuto = null;
	
				if (urlRef != null && !urlRef.contentEquals("null")) {
					String[] urlParts = urlRef.split("/");
					String idPratica = urlParts[urlParts.length - 2];
					contenuto = moonsrvDAO.getAllegatoFruitore(idPratica);
				} else {
					contenuto = (fileAllegato != null) ? fileAllegato.getContenuto(): null;
				}
	
				return Response.ok(contenuto)
						.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"")
						.header("Content-Type", fileAllegato.getContentType()).build();
			} catch (Exception ex) {
				log.error("[FileApiImpl::getFileNotificaFruitore] Errore upload file ",ex);
				throw new ServiceException("Errore nel recupero del file " +nomeFile); 
			} finally {
				log.info("[FileApiImpl::getFileNotificaFruitore] END");
			}
		
		}


		@Override
		public Response deleteFile(String baseUrl, String form, String project, String input,
				SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
			try {
				log.info("[FileApiImpl::deleteFile] BEGIN");
				log.debug(baseUrl);
				log.debug(form);
				log.debug(project);
				log.debug(input);
				
				ObjectMapper mapper = new ObjectMapper();
				JsonNode su = mapper.readValue(input, JsonNode.class);
				String nomeFile = su.get("name").getTextValue();
				allegatiService.deleteAllegatoByNameFormio(nomeFile);
				
				return Response.ok().build();
			} catch (Exception e) {
				log.error("[FileApiImpl::deleteFile] Errore delete file allegato ",e);
				throw new ServiceException("Errore delete file allegato " );
			}
		}
	
		
	@Override
	public Response uploadRepositoryFile(String baseUrl, String form, String project, 
		MultipartFormDataInput input, String idTipologiaPP, String idTipoMydocsQP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) throws UnprocessableEntityException, FileUploadException {
	
		log.info("[FileApiImpl::uploadRepositoryFile] BEGIN");
		log.info("[FileApiImpl::uploadRepositoryFile] IN baseUrl = " + baseUrl);
		log.info("[FileApiImpl::uploadRepositoryFile] IN form = " + form);
		log.info("[FileApiImpl::uploadRepositoryFile] IN project = " + project);
		log.info("[FileApiImpl::uploadRepositoryFile] IN idTipologia = " + idTipologiaPP);
		log.info("[FileApiImpl::uploadRepositoryFile] IN idTipoMydocs = " + idTipoMydocsQP);

		input.getFormDataMap().entrySet().forEach( entry -> {
			String v = ("file".equals(entry.getKey()))?getMultipartValueOrNull(entry.getKey(), input).substring(0,16)+".....":getMultipartValueOrNull(entry.getKey(), input);
			log.debug("[FileApiImpl::uploadRepositoryFile] IN MultipartFormDataInput Key : " + entry.getKey() +" Value : " + v );
		});

		String elencoFile = "";
		// nome del file valorizzato da formio
		String formioNameFile = "";
		String formioDir= "";
		try {
			Integer idTipologia = validaIntegerRequired(idTipologiaPP);
			Long idTipologiaMydocs = validaLong(idTipoMydocsQP);
			RepositoryFileEntity file = new RepositoryFileEntity();
			file.setDataCreazione(new Date());
			file.setIdTipologia(idTipologia);
			file.setIdTipologiaMydocs(idTipologiaMydocs);
			file.setCodiceFile(UUID.randomUUID().toString()); // CodiceFile MOOn UUID usato per comunicazione a sistemi esterni (Protocolo)
			if (input.getParts().size() > 0) {
				for (Map.Entry<String, List<InputPart>> entry : input.getFormDataMap().entrySet()) {
					
					if (entry.getKey().equalsIgnoreCase("name")) {
						formioNameFile = getMultipartValue(entry.getKey(), input);
						file.setFormioNameFile(formioNameFile);
					}

					// caso file
					if (entry.getKey().equalsIgnoreCase("file")) {
						List<InputPart> inputParts = input.getFormDataMap().get(entry.getKey());
						for (InputPart inputPart : inputParts) {

							try {
								log.info("[FileApiImpl::uploadRepositoryFile] key = " + entry.getKey());
								MultivaluedMap<String, String> header = inputPart.getHeaders();
								log.info("[FileApiImpl::uploadRepositoryFile] header = " + header);
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
								
								elencoFile += file.getNomeFile();
							} catch (IOException e) {
								log.error("[FileApiImpl::uploadRepositoryFile] Errore upload file ", e);
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
		} catch (UnprocessableEntityException uee) {
			log.warn("[" + CLASS_NAME + "::uploadRepositoryFile] UnprocessableEntityException");
			throw uee;
		} catch (Exception ex) {
			log.error("[FileApiImpl::uploadRepositoryFile]- Errore upload file ",ex);
			throw  new FileUploadException();
		} finally {
			log.info("[FileApiImpl::uploadRepositoryFile]- END");
		}
	}
	
	@Override
	public Response getRepositoryFile(String baseUrl, String nameFile, String idTipologiaPP,
		SecurityContext securityContext, HttpHeaders httpHeaders, HttpServletRequest httpRequest) {
		try {
			log.info("[FileApiImpl::getRepositoryFile] BEGIN");
			Integer idTipologia = validaIntegerRequired(idTipologiaPP);
			
			// skip primo caratteri che è /
			//RepositoryFileEntity fileAllegato = repositoryFileDAO.findByFormIoNameFile(nameFile.substring(1));
			RepositoryFileEntity fileAllegato = repositoryFileService.findByFormIoNameFile(nameFile.substring(1)); 
			
			return Response.ok(fileAllegato.getContenuto())
				.header("Content-Disposition", "attachment; filename=\"" + fileAllegato.getNomeFile() + "\"" )
				.header("Content-Type", fileAllegato.getContentType())
				.build();
		} catch (Exception ex) {
			log.error("[FileApiImpl::getRepositoryFile] Errore upload file ",ex);
			throw new ServiceException("Errore nel recupero del file " + nameFile);
		} finally {
			log.info("[FileApiImpl::getRepositoryFile] END");
		}
	}

}
