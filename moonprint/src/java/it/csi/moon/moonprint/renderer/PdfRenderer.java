/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonprint.renderer;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.InvalidPathException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.openhtmltopdf.util.XRLog;

import it.csi.moon.moonprint.exceptions.service.ServiceException;
import it.csi.moon.moonprint.renderer.config.CommandLineArgs;
import it.csi.moon.moonprint.renderer.config.Config;
import it.csi.moon.moonprint.renderer.template.datamodel.DataModel;
import it.csi.moon.moonprint.renderer.template.datamodel.DefaultDataModel;
import it.csi.moon.moonprint.renderer.utils.ExtensionLoader;
import it.csi.moon.moonprint.renderer.utils.Globals;
import it.csi.moon.moonprint.renderer.utils.Log;
import it.csi.moon.moonprint.renderer.utils.QRGenerator;
import it.csi.moon.moonprint.renderer.utils.Util;
import it.csi.moon.moonprint.util.LoggerAccessor;

@Component
public class PdfRenderer extends BaseRenderer {
	
	private static final String CLASS_NAME = "PdfRenderer";
	private static final Logger LOG = LoggerAccessor.getLoggerBusiness();

	private static final String TEMPLATE_XHTML = "/template.xhtml";
	private static final String STYLE_CSS = "/style.css";
	private static final String TEMPLATES_PATH = "TemplatesPath";
	private static String baseDirectory = null;

    //    private static final Logger LOGGER = LoggerFactory.getLogger(Generator.class);
//    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ObjectMapper MAPPER = new ObjectMapper(createJsonFactory());
    private static final TemplateEngine THYMELEAF = new TemplateEngine();
    private static final Map<String, String> templates = new ConcurrentHashMap<>();
    private static final Map<String, String> templatesCss = new ConcurrentHashMap<>();

    //private static String currentExecPath = null;
    private enum Template {
        DEFAULT("default");/*,
        BOZZA("Bozza");*/
        public final String slug;

        Template(String slug) {
            this.slug = slug;
        }
    }

    /**
     * Default constructor
     * @param _baseDirectory the directory where the jar is executed, or from where it is loaded. This directory contains the custom templates
     */
    
    public PdfRenderer() {        
    	 this(retrieveEnvTemplatesPath(), null);
         Stream.of(Template.values()).forEach(PdfRenderer::loadTemplate);
         Stream.of(Template.values()).forEach(PdfRenderer::loadTemplateCss);
         Log.i("[PdfRenderer:PdfRenderer] templates.size() = " + templates.size());
         Log.i("[PdfRenderer:PdfRenderer] templatesCss.size() = " + templatesCss.size());
    }
    
    public PdfRenderer(String _baseDirectory) {
        this(_baseDirectory, null);
    }

    public PdfRenderer(String _baseDirectory, String[] args) {
        try {
            if(_baseDirectory == null)
                throw new InvalidPathException("", "Il percorso specificato per la classe "+ Globals.APP_NAME +" non è valido.");

            Util.normalizePath(_baseDirectory);
            if (args != null) {
                this.initConf(_baseDirectory, args);
            } else {
                this.initConf(_baseDirectory, new String[0]);
            }
            File dir = new File(_baseDirectory);
            if (dir == null || !dir.exists()) {
                throw new InvalidPathException(_baseDirectory, "Impossibile accedere alla directory specificata. Verificare che la directory esista e che si dispongano i permessi necessari.");
            }
            if (_baseDirectory.endsWith("/") || _baseDirectory.endsWith("\\")) {
                _baseDirectory = _baseDirectory.substring(0, _baseDirectory.length()-1);
            }
            _baseDirectory = _baseDirectory.replace("\\\\", "/");

            this.baseDirectory = _baseDirectory;
            //this.baseDirectory = Util.normalizePath(_baseDirectory);;
        } catch (Exception e) {
            throw new InvalidPathException(_baseDirectory, "Impossibile accedere alla directory specificata. Verificare che la directory esista e che si dispongano i permessi necessari.");
        }
    }

    private static void loadTemplate(Template t) {
        try {
            if (t.slug.equals(Template.DEFAULT.slug)) {
            	
//                templates.put(t.slug,
//                        IOUtils.toString(PdfRenderer.class.getClassLoader().getResource("/template.hxtml"), StandardCharsets.ISO_8859_1));
//            	Log.i("Loading xhtml default: " + PdfRenderer.class.getResource("/template.xhtml"));
            	templates.put(t.slug,
                        IOUtils.toString(PdfRenderer.class.getResource(TEMPLATE_XHTML), StandardCharsets.ISO_8859_1));
                Log.i("Loaded xhtml default: " + Template.DEFAULT.slug);
            } else {
                //currentExecPath = PdfRenderer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
//                String directory = baseDirectory;
//                if (directory.endsWith("/") || directory.endsWith("\\")) {
//                    directory = directory.substring(0, directory.length() - 1);
//                }
                String directory = Util.normalizePath(baseDirectory);
                //String directory = baseDirectory.substring(0, baseDirectory.lastIndexOf("/"));
//                Log.i("Loading default template: " + t + " --> " + directory + "/templates/" + t.slug + "/template.xhtml");
                templates.put(t.slug,
                        IOUtils.toString(URI.create(directory + "/templates/" + t.slug + TEMPLATE_XHTML), StandardCharsets.ISO_8859_1));
                Log.i("Loaded default template: " + t + " --> " + directory + "/templates/" + t.slug + TEMPLATE_XHTML);
            }
        } catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::loadTemplate] IOException", e);
            System.exit(-1);
        }
    }

    private static void loadTemplate(String name) {
        try {
            if (name.equals(Template.DEFAULT.slug)) {
                templates.put(name,
                        IOUtils.toString(PdfRenderer.class.getResource(TEMPLATE_XHTML), StandardCharsets.ISO_8859_1));
                Log.i("Loaded xhtml default: " + Template.DEFAULT.slug + " template.xhtml" );
            } else {
                //directory = PdfRenderer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                //Log.d("PdfRenderer:loadTemplate] baseDirectory: " + baseDirectory);
                String directory = Util.normalizePath(baseDirectory);

                //directory = "file:" + baseDirectory.substring(0, baseDirectory.lastIndexOf("/"));
                //Log.d("[loadTemplate] -> "+directory + "/templates/" + name + "/template.xhtml");
                URI fUri = URI.create(directory + "/templates/" + name + TEMPLATE_XHTML);
                File f = new File(fUri);
                if (f.exists()) {
                    templates.put(name, IOUtils.toString(fUri, StandardCharsets.ISO_8859_1));
                    Log.i("Loaded xhtml template: " + name + " --> " + fUri.getPath());
                }
            }
        } catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::loadTemplate] IOException", e);
            System.exit(-1);
        } catch (IllegalArgumentException iae) {
			LOG.error("[" + CLASS_NAME + "::loadTemplate] IllegalArgumentException", iae);
        }
    }

    private static void loadTemplateCss(Template t) {
        try {
            if (t.slug.equals(Template.DEFAULT.slug)) {
                templatesCss.put(t.slug,
                        IOUtils.toString(PdfRenderer.class.getResource(STYLE_CSS), StandardCharsets.UTF_8));
                Log.i("Loaded CSS template: " + Template.DEFAULT.slug + " --> /style.css");
            } else {
                templatesCss.put(t.slug,
                        IOUtils.toString(URI.create(baseDirectory + "templates/" + t.slug + STYLE_CSS), StandardCharsets.UTF_8));
                Log.i("Loaded CSS template: " + t + " --> " + baseDirectory + "templates/" + t.slug + STYLE_CSS);
            }
        } catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::loadTemplateCss] IOException", e);
            System.exit(-1);
        }
    }

    private static void loadTemplateCss(String name) {
        try {
            if (name.equals(Template.DEFAULT.slug)) {
                templatesCss.put(name,
                        IOUtils.toString(PdfRenderer.class.getResource(STYLE_CSS), StandardCharsets.ISO_8859_1));
                Log.i("Loaded CSS template: " + Template.DEFAULT.slug + " --> /style.css");
            } else {
                //directory = PdfRenderer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                //Log.d("currentExecPath: " + currentExecPath);
//                String directory = baseDirectory;
//                if (directory.endsWith("/") || directory.endsWith("\\")) {
//                    directory = "file:" + directory.substring(0, directory.length()-1);
//                }
                String directory = Util.normalizePath(baseDirectory);
//                String directory = "file:" + baseDirectory.substring(0, baseDirectory.lastIndexOf("/"));
                URI fUri = URI.create(directory + "/templates/" + name + STYLE_CSS);
                File f = new File(fUri);
                if (f.exists()) {
                    templatesCss.put(name,
                            IOUtils.toString(fUri, StandardCharsets.ISO_8859_1));
                    Log.i("Loaded CSS template: " + name + " --> " + fUri.getPath());
                }
            }
        } catch (IOException e) {
			LOG.error("[" + CLASS_NAME + "::loadTemplateCss] IOException", e);
            System.exit(-1);
        }
    }

    private static JsonFactory createJsonFactory() {
        JsonFactoryBuilder builder = new JsonFactoryBuilder();
        builder.enable(JsonReadFeature.ALLOW_MISSING_VALUES);
        builder.enable(JsonReadFeature.ALLOW_TRAILING_COMMA);
        builder.enable(JsonReadFeature.ALLOW_UNQUOTED_FIELD_NAMES);
        builder.enable(JsonReadFeature.ALLOW_SINGLE_QUOTES);
        builder.enable(JsonReadFeature.ALLOW_JAVA_COMMENTS);
        return builder.build();
    }

    private String readJsonFromFile(String filePath) {
        String json = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                json += line;
            }
            reader.close();

            return json;
        } catch (IOException e) {
            Log.d("Errure durante la lettura del file json");
            return null;
        }
    }


    /**
     * Main class
     *
     * @param args The command line parameters
     */
    public static void main(String[] args) throws FileNotFoundException {
            String currentExecPath = PdfRenderer.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            if (currentExecPath.endsWith(".jar")) {
                int lastSlashIdx = -1;
                if (currentExecPath.lastIndexOf("/") > 0) {
                    lastSlashIdx = currentExecPath.lastIndexOf("/");
                } else if (currentExecPath.lastIndexOf("\\") > 0) {
                    lastSlashIdx = currentExecPath.lastIndexOf("\\");
                }
                currentExecPath = currentExecPath.substring(0, lastSlashIdx);
            }
            //System.err.println("[Main] calling constructor - currentExecPath="+currentExecPath);
            if (currentExecPath.endsWith("/") || currentExecPath.endsWith("\\")) {
                currentExecPath = currentExecPath.substring(0, currentExecPath.length() - 1);
            }

            if (currentExecPath.startsWith("/") && currentExecPath.length() > 2 && currentExecPath.substring(2,3).equals(":")) {
                //windows only PATH ISSUE
                currentExecPath = currentExecPath.substring(1);
            }

            // System.err.println("[Main] calling constructor - currentExecPath=" + currentExecPath);
            PdfRenderer instance = new PdfRenderer(currentExecPath, args);

            String dashes = "\r\n\t" + String.join("", Collections.nCopies(Config.appNameAndVersion().length(), "-")) + "\r\n\t";
            Log.i(dashes + Config.appNameAndVersion() + dashes);
            Log.i(CommandLineArgs.getInstance().toString());
            Log.i(Globals.config.toString());
            // System.err.println(Globals.config.toString());


            String jsonString = null;

            String jsonFilePath = Globals.config.getJsonFile();
            if (jsonFilePath != null && !jsonFilePath.isEmpty()) {
                File tmpDir = new File(jsonFilePath);
                if (tmpDir.exists()) {
                    jsonString = instance.readJsonFromFile(Globals.config.getJsonFile());
                }
            }
            if (jsonString == null) {
                Log.f("Nessun dato fornito! Si prega di specificare i dati (come stringa o come file) nel file di configurazione o da linea di comando.");
                return;
            }


            //****************************
            byte[] result = instance.convert(jsonString, Globals.config.getTemplate(), Globals.config.getOutputFormat());
            //****************************

            // Print to standard out using PrintStream (Note: using system out Will not work because the bytes may contains \0, causing the result to be truncated)
            PrintStream outStream = new PrintStream(System.out, true);

            if (result != null) {
                outStream.write(result, 0, result.length);
            }
            outStream.flush();
            outStream.close();

//            System.gc();
    }


    public byte[] toPdf(String _jsonString, String _templateName) {
        return convert(_jsonString, _templateName, Globals.OUTPUT_FORMATS.PDF.toString());
    }

    public byte[] toHtml(String _jsonString, String _templateName) {
        return convert(_jsonString, _templateName, Globals.OUTPUT_FORMATS.HTML.toString());
    }

    public byte[] toZip(String _jsonString, String _templateName) {
        return convert(_jsonString, _templateName, Globals.OUTPUT_FORMATS.ZIP.toString());
    }

    private byte[] convert(String _jsonString, String _templateName, String _outputFormat) {
//    	Log.d("[PdfRenderer:convert] BEGIN");
//    	long startTime = System.nanoTime();
//    	long localStartTime = startTime;
        try {
            DataModel.Document module = null;
            Class<DataModel.Document> dataModelDocumentClass = null;
            // XRLog.listRegisteredLoggers().forEach(logger -> XRLog.setLevel(logger, java.util.logging.Level.OFF));
		
            if (_templateName == null || _templateName.isEmpty()) {
                _templateName = Template.DEFAULT.slug;
            }

            String camelCaseTemplateName = "";
            if (_templateName != null && _templateName.length() > 0) {
                camelCaseTemplateName = _templateName.substring(0, 1).toUpperCase() + _templateName.substring(1);
            }

            if (!_templateName.equals(Template.DEFAULT.slug)) {
                String templateDirectory = baseDirectory + "/templates/" + camelCaseTemplateName + "/";

                ExtensionLoader<DataModel> loader = new ExtensionLoader<DataModel>();
                DataModel dm = loader.LoadClass(templateDirectory, "it.csi.moon.moonprint.renderer.template.datamodel." + camelCaseTemplateName + "DataModel", DataModel.class);
                      
                // Looking for specific (subclass) document class
                for (Class c : dm.getClass().getClasses()) {
                    if (c.getCanonicalName().equals(dm.getClass().getCanonicalName() + ".Document")) {
                        dataModelDocumentClass = c;
                        break;
                    }
                }
                
                try {
                    module = MAPPER.readValue(_jsonString, dataModelDocumentClass);
                } catch (Exception e) {
        			LOG.error("[" + CLASS_NAME + "::convert] Exception", e);
                }
            } else {
                module = MAPPER.readValue(_jsonString, DefaultDataModel.Document.class.asSubclass(DataModel.Document.class));
            }
//            long endTime = System.nanoTime();
//            Log.d("[PdfRenderer:convert] Document readed after " + (endTime - startTime) / 1000000 + " ms.");
//            localStartTime = endTime;
            
            // Stream.of(Template.values()).forEach(PdfRenderer::loadTemplate);
            // Stream.of(Template.values()).forEach(PdfRenderer::loadTemplateCss);

            String template = templates.get(_templateName);
            if (template == null) {
            	loadTemplate(_templateName);
            	loadTemplateCss(_templateName);
            	template = templates.get(_templateName);
    			LOG.debug("[" + CLASS_NAME + "::convert] Load cache template - missed " + _templateName );
            	Log.d("[PdfRenderer:convert]  Load cache template - missed " + _templateName );
            }
            if (template == null) {
    			LOG.error("[" + CLASS_NAME + "::convert] Template '" + _templateName + "' non trovato nella directory '" + baseDirectory + "'! Assicurarsi che il nome sia scirtto correttamente e che i file siano presenti nella cartella. Per maggiori dettagli consultare la documentazione.");
                Log.f("[PdfRenderer:convert] Template '" + _templateName + "' non trovato nella directory '" + baseDirectory + "'! Assicurarsi che il nome sia scirtto correttamente e che i file siano presenti nella cartella. Per maggiori dettagli consultare la documentazione.");
                //System.exit(-1);
                return null;
            }
//            endTime = System.nanoTime();
//            Log.d("[PdfRenderer:convert] loaded Template readeds after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//            localStartTime = endTime;
            
            if (_outputFormat == null || _outputFormat.isEmpty()) {
                _outputFormat = Globals.OUTPUT_FORMATS.PDF.toString();
            }
			LOG.info("[" + CLASS_NAME + "::convert] Outputting " + _outputFormat.toUpperCase() + " now. TEMPLATE: " + _templateName);
            Log.i("[PdfRenderer:convert] Outputting " + _outputFormat.toUpperCase() + " now. TEMPLATE: " + _templateName);

            Context ctx = new Context();
            ctx.setVariable("document", module);
            if (module!=null && _templateName.equals(Template.DEFAULT.slug)) {
                String imgb64 = QRGenerator.generateQR(((DefaultDataModel.Document) module).metadata.qrContent);
                ctx.setVariable("qrb64", imgb64);
            }
            ctx.setVariable("outputFormat", "PDF");
//            endTime = System.nanoTime();
//            Log.d("[PdfRenderer:convert] inizialized (QR) Context after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//            localStartTime = endTime;
            
            String html = THYMELEAF.process(template, ctx);
//            endTime = System.nanoTime();
//            Log.d("[PdfRenderer:convert] processed THYMELEAF TemplateEngine after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//            localStartTime = endTime;
            
            if (_outputFormat.equalsIgnoreCase(Globals.OUTPUT_FORMATS.PDF.toString())) {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                PdfRendererBuilder builder = new PdfRendererBuilder();
                try {
                    if (_templateName.equals(Template.DEFAULT.slug)) {
                        builder.withHtmlContent(html, PdfRenderer.class.getResource("/root.htm").toExternalForm());
                    } else {
                        String baseDirectoryTrimmed = baseDirectory;
                        if (baseDirectoryTrimmed.endsWith("/") || baseDirectoryTrimmed.endsWith("\\")) {
                            baseDirectoryTrimmed = baseDirectoryTrimmed.substring(0, baseDirectoryTrimmed.length() - 1);
                        }
                        
                        builder.withHtmlContent(html, "file:///" + baseDirectoryTrimmed + "/templates/" + _templateName + "/root.htm");
                    }
                    builder.useFastMode();
                    builder.toStream(os);
                    builder.run();
                } catch (Exception e) {
                    Log.f("[PdfRenderer:convert:toPDF]  Errore durante la generazione del file PDF. Verificare che i dati siano formattati in modo corretto. Dettagli: " + e.getMessage());
                }
//                endTime = System.nanoTime();
//                Log.d("[PdfRenderer:convert] PdfRendererBuilder ended after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//                localStartTime = endTime;

                // clear variables and request Garbage Collector execution
                builder = null;
                html = null;
                template = null;
                dataModelDocumentClass = null;
                module = null;
                _jsonString = null;
                templates.clear();
                templatesCss.clear();
                camelCaseTemplateName = null;
                ctx = null;
                System.gc();

//                endTime = System.nanoTime();
//                Log.d("[PdfRenderer:convert] System.gc() ended after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//                localStartTime = endTime;

                return os.toByteArray();
            } else if (_outputFormat.equalsIgnoreCase(Globals.OUTPUT_FORMATS.HTML.toString())) { // return as HTML
                return html.getBytes();
            } else if (_outputFormat.equalsIgnoreCase(Globals.OUTPUT_FORMATS.ZIP.toString())) { // Prints ZIP file containing HTML, CSS, resources)
                try {
                    Log.f("[PdfRenderer:convert:convert] ZIP Work in progress! The zip file will not include all the resources!");

                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ZipOutputStream zos = new ZipOutputStream(os);

                    // add HTML
                    String htmlFileName = "index.html";
                    addToZipFile(htmlFileName, html.getBytes(), zos);

                    // add CSS
                    String cssFileName = _templateName + ".css";
                    Log.d("[PdfRenderer:convert:convert] ZIP Loading template CSS: " + cssFileName);
                    addToZipFile(cssFileName, templatesCss.get(_templateName).getBytes(), zos);

                    // TODO: add ALL images/resources (logos, qrcodes, etc..). May be static files, link to images to be downloaded from the web, or dynamically generated (qr codes)

                    zos.close();
                    return os.toByteArray();
                } catch (Exception e) {
        			LOG.error("[" + CLASS_NAME + "::convert] Exception", e);
                    Log.e(e.getMessage());
                    return null;
                }
            } else {
                Log.f("[PdfRenderer:convert] Formato di output non riconosciuto! Verificare la configurazione o i parametri");
                return null;
            }

        } catch (ClassNotFoundException cnfe) {
			LOG.error("[" + CLASS_NAME + "::convert] ClassNotFoundException", cnfe);
            return null;
        } catch (JsonProcessingException jpe) {
			LOG.error("[" + CLASS_NAME + "::convert] JsonProcessingException", jpe);
            return null;
//        } finally {
//            long endTime = System.nanoTime();
//            Log.d("[PdfRenderer:convert] Ended after " + (endTime - startTime) / 1000000 + " ms. in " + (endTime - localStartTime) / 1000000 + " ms.");
//        	Log.d("[PdfRenderer:convert] END");
        }
    }

    public static void addToZipFile(String fileName, ZipOutputStream zos) throws IOException {
        //System.err.println("Writing '" + fileName + "' to zip file");
    	try (FileInputStream fis = new FileInputStream(new File(fileName))) {
	        ZipEntry zipEntry = new ZipEntry(fileName);
	        zos.putNextEntry(zipEntry);
	
	        byte[] bytes = new byte[1024];
	        int length;
	        while ((length = fis.read(bytes)) >= 0) {
	            zos.write(bytes, 0, length);
	        }
	        zos.closeEntry();
    	}
    }

    public static void addToZipFile(String fileName, byte[] fileContent, ZipOutputStream zos) throws IOException {
        //System.err.println("Writing '" + fileName + "' to zip file");
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipEntry.setSize(fileContent.length);
        zos.putNextEntry(zipEntry);
        zos.write(fileContent, 0, fileContent.length);
        zos.closeEntry();
    }

    private void initConf(String _baseDirectory, String[] args) {
        // parse command line arguments
        // read cfg file (passed via command line or default)
        // init cfg file
        // override cfg file with command line cfg
    	XRLog.listRegisteredLoggers().forEach(logger -> {
    		XRLog.setLevel(logger, java.util.logging.Level.OFF);
			LOG.debug("[" + CLASS_NAME + "::initConf] XR Logger : " + logger);
    	});
        CommandLineArgs clArgs = CommandLineArgs.getInstance();
        clArgs.init(args);

        Globals.config = Config.getInstance();
        int initRes;
        if (clArgs.cfg.containsKey(Config.CONFIG_PARAM_CONFIG_FILE)) {
            initRes = Globals.config.init(clArgs, null, clArgs.cfg.get(Config.CONFIG_PARAM_CONFIG_FILE));
        } else {
            initRes = Globals.config.init(clArgs, _baseDirectory);
        }
        if (initRes < 0) {
            Log.f("Errore nella configurazione. Verificare il file o i parametri");
            System.exit(-1);
        }
    }
    
    private static String retrieveEnvTemplatesPath() {
		// Retrieve template path
		// IMPORTANTE: il path e' configurato nel file nella directory di wildfly: \standalone\configuration\pdfRenderer.properties
		//     e deve essere inserito nel formato: TemplatesPath=PATH_COMPLETO
		String fileName = "";
		try {
			fileName = System.getProperty("jboss.server.config.dir")+File.separator+"pdfRenderer.properties";
			File propFile = new File(fileName);
			java.util.Properties props = new java.util.Properties();
			if (!propFile.exists()) {
				throw new ServiceException("Errore recupero getPdf: Il file con la definzione del path ai template non trovato");
			}
			try(FileInputStream fis = new FileInputStream(propFile)) {
				props.load(fis);
			}
			if (!props.containsKey(TEMPLATES_PATH)) {
				throw new ServiceException("Errore recupero getPdf: Errora durante la lettura del path ai template --> Verificare il file pdfRenderer.properties");
			}
			if (!(new File(props.getProperty(TEMPLATES_PATH)).exists())) {
				throw new ServiceException("Errore recupero getPdf: Path con i template non valido --> Verificare il file pdfRenderer.properties; verificare che il percorso specificato sia presente e i relativi permessi di accesso");
			}
	//			System.out.println("Props => "+props.getProperty("TemplatesPath"));
			//mettere il percorso assoluto della cartella dove ci sono il jar e il template 
			//private String destinazione = "D:\\W3Lab\\2020\\CSI\\_src\\MOOnPrintRenderer\\target";
			String destinazione = props.getProperty(TEMPLATES_PATH);
			return destinazione;
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			Log.e("[PdfRenderer:getPdf] Errore recupero file di properties : " + fileName);
			throw new ServiceException("Errore recupero file di properties");
		}
	}       
    
}
