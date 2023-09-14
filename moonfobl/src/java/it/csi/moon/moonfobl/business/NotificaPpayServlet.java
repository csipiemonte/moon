/*
* SPDX-FileCopyrightText: (C) Copyright 2023 C.S.I. Piemonte
*
* SPDX-License-Identifier: EUPL-1.2 */

package it.csi.moon.moonfobl.business;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import it.csi.moon.moonfobl.util.HttpRequestUtils;
import it.csi.moon.moonfobl.util.LoggerAccessor;

public class NotificaPpayServlet extends HttpServlet {

	private static final long serialVersionUID = -3650849952028852330L;
	private static final String CLASS_NAME = "NotificaPpayServlet";
    private static final Logger LOG = LoggerAccessor.getLoggerApplication();
    
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		try {
			LOG.info("[" + CLASS_NAME + "::doGet] BEGIN");
			HttpRequestUtils utils = new HttpRequestUtils();
			utils.logInfoAllHeaders(request);
			boolean SUCCESS = "000".equals(request.getParameter("codEsito"));
			response.setContentType("text/html");
			try (PrintWriter out = response.getWriter()) {
				out.println(SUCCESS?getSuccessPage(request):getNegativaPage(request));
			}
			LOG.info("[" + CLASS_NAME + "::doGet] END SUCCESS=" + SUCCESS);
		} catch (IOException ioe) {
			LOG.error("[" + CLASS_NAME + "::doGet] IOException ", ioe);
		}
	}

	private String getSuccessPage(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("<head>");
		sb.append("<meta charset=\"utf-8\">");
		sb.append("<title>MOOn - Esito pagamento</title>");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		sb.append("<link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">");
		sb.append("<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>");
		sb.append("<link href=\"https://fonts.googleapis.com/css2?family=Titillium+Web:wght@400;700&display=swap\" rel=\"stylesheet\">");
		sb.append("<style>body{font-family: 'Titillium Web', sans-serif; color: #19191a; padding: 24px;}.main{display: flex; flex-direction: column; align-items: center; justify-content: center; justify-items: center; height: 100%; width: 100%; text-align: center;}.box{padding: 24px; border: 1px solid #b1b1b3; border-left: 8px solid #008758;}.box button{padding: 12px 24px; font-size: 14px; line-height: 24px; background-color: #008758; border: 2px solid #008758; white-space: initial; text-decoration: none; outline: none; font-weight: bold; color: white;}.box button:hover, .box button:active{background-color: #006d47; border: 2px solid #006d47;}.box button:focus{border: 2px solid #ff7a00;}h1{font-size: 32px; font-weight: bold; margin-bottom: 38px;}h2{font-size: 20px; font-weight: bold;}p{font-size: 16px;}.box-title{display: flex; justify-content: center;}.box-title svg{width: 32px; margin-right: 8px;}@media (min-width: 576px){h1{font-size: 40px; font-weight: bold;}h2{font-size: 24px; font-weight: bold;}p{font-size: 18px;}}</style>");
		sb.append("</head>\r\n");
		sb.append("<body>");
			sb.append("<div class=\"main\">");
				sb.append("<h1>Esito pagamento</h1>");
				sb.append("<div class=\"box\">\r\n");
					sb.append("<div class=\"box-title\">");
	                	sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\"><path fill=\"#008758\" d=\"M17.1 7.9l.7.7-7.8 7.6-4.7-4.6.7-.7 4 3.9zM22 12A10 10 0 1 1 12 2a10 10 0 0 1 10 10zm-1 0a9 9 0 1 0-9 9 9 9 0 0 0 9-9z\"/></svg>");
	                	sb.append("<h2>Operazione terminata con successo</h2>");
	                sb.append("</div>\r\n");
	                sb.append("<p>idPagamento: ").append(request.getParameter("idPagamento")).append("<p/>\r\n");
	                sb.append("<p>Siamo in attesa di ricevere l'esito della transazione di pagamento.<br>");
	                sb.append("La comunicazione dell'esito sara inviato alla casella di posta che hai indicato.</p>");
	                sb.append("<p>Clicca su \"Chiudi\" per tornare al dettaglio dell'istanza.</p>");
//	                sb.append("<p>Clicca su \"Chiudi\" per tornare al dettaglio dell'istanza e procedere all'invio</p>");
	                sb.append("<button class=\"closeButton\" style=\"cursor: pointer\" onclick=\"window.close();\">Chiudi</button>");
	    		sb.append("</div>\r\n");
	    	sb.append("</div>");
	    sb.append("</body></html>");
	/*
		sb.append("<!DOCTYPE html>");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("<head><title>MOOn - Esito Pagamento</title>");
		sb.append("<style>\r\n"
				+ "body, html {\r\n"
				+ "   height: 100%;\r\n"
				+ "   margin: 0;\r\n"
				+ "}\r\n"
				+ "#container {\r\n"
				+ "   width: 500px;\r\n"
				+ "   height: 250px;   \r\n"
				+ "   position: absolute;\r\n"
				+ "   top: 50%;\r\n"
				+ "   left: 50%;\r\n"
				+ "   margin: -125px 0 0 -250px;\r\n"
				+ "}\r\n"
				+ "#container #msg {\r\n"
				+ "   width: 350px;\r\n"
				+ "   height: 150px;\r\n"
				+ "   position: relative;\r\n"
				+ "   background-color: #4caf50;\r\n"
				+ "   border-radius: 15px;\r\n"
				+ "   font-weight: bold;\r\n"
				+ "}\r\n"
				+ "#container #action {   \r\n"
				+ "   position: relative;\r\n"
				+ "}\r\n"
				+ "</style></head>");
		sb.append("<body><center>");
		sb.append("<h1>Esito Pagamento</h1>");
		sb.append("<div id='container'>");
			sb.append("<div id='msg'>");
				sb.append("<br>");
				sb.append("<p>idPagamento: ").append(request.getParameter("idPagamento")).append("<p/>");
				sb.append("<p>Effettuato con Successo</p>");
			sb.append("</div>");
			sb.append("<div id='action'>");
				sb.append("<p>Puoi tornare su MOOn e <b>INVIARE</b> la tua istanza</p>");
			sb.append("</div>");
		sb.append("</div>");
		sb.append("</center></body></html>");
		*/
		return sb.toString();
	}

	private String getNegativaPage(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>\r\n"
				+ "    <head>\r\n"
				+ "        <meta charset=\"utf-8\">\r\n"
				+ "        <title>MOOn - Esito pagamento</title>\r\n"
				+ "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n"
				+ "        <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n"
				+ "        <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n"
				+ "        <link href=\"https://fonts.googleapis.com/css2?family=Titillium+Web:wght@400;700&display=swap\" rel=\"stylesheet\">\r\n"
				+ "		<style>body{font-family: 'Titillium Web', sans-serif; color: #19191a; padding: 24px;}.main{display: flex; flex-direction: column; align-items: center; justify-content: center; justify-items: center; height: 100%; width: 100%; text-align: center;}.box{padding: 24px; border: 1px solid #b1b1b3; border-left: 8px solid #d9364f;}h1{font-size: 32px; font-weight: bold; margin-bottom: 38px;}h2{font-size: 20px; font-weight: bold;}p{font-size: 16px;}.box-title{display: flex; justify-content: center;}.box-title svg{width: 32px; margin-right: 8px;}@media (min-width: 576px){h1{font-size: 40px; font-weight: bold;}h2{font-size: 24px; font-weight: bold;}p{font-size: 18px;}}</style>\r\n"
				+ "    </head>\r\n"
				+ "    <body>\r\n"
				+ "        <div class=\"main\">\r\n"
				+ "            <h1>Esito pagamento</h1>\r\n"
				+ "            <div class=\"box\">\r\n"
				+ "                <div class=\"box-title\">\r\n"
				+ "                    <svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\"><path fill=\"#d9364f\" d=\"M11.5 14.2V5.7h1.2v8.5zm-.1 4.1h1.2v-1.8h-1.2zM22 7.9v8.3L16.1 22H7.9L2 16.2V7.9L7.9 2h8.2zm-1 .4L15.7 3H8.3L3 8.3v7.5L8.3 21h7.4l5.3-5.2z\"/></svg>\r\n"
				+ "                    <h2>ATTENZIONE: operazione annullata!</h2>\r\n"
				+ "                </div>\r\n");
		sb.append("                <p>idPagamento: ").append(request.getParameter("idPagamento")).append("<p/>\r\n");
		sb.append("                <p>" + request.getParameter("codEsito") + " - " + request.getParameter("descEsito") + "</p>\r\n");
		sb.append("                <p>Siamo in attesa di ricevere l'esito della transazione di pagamento.<br>\r\n"
				+ "                La comunicazione dell'esito sara inviato alla casella di posta che hai indicato.</p>\r\n"
				+ "            </div>\r\n"
				+ "        </div>\r\n"
				+ "    </body>\r\n"
				+ "</html>");
		return sb.toString();
	}
	
	private String getOtherPage(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>");
		sb.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		sb.append("<head><title>MOOn - Esito Pagamento</title>");
		sb.append("<style>\r\n"
				+ "body, html {\r\n"
				+ "   height: 100%;\r\n"
				+ "   margin: 0;\r\n"
				+ "}\r\n"
				+ "#container {\r\n"
				+ "   width: 500px;\r\n"
				+ "   height: 250px;   \r\n"
				+ "   position: absolute;\r\n"
				+ "   top: 50%;\r\n"
				+ "   left: 50%;\r\n"
				+ "   margin: -125px 0 0 -250px;\r\n"
				+ "}\r\n"
				+ "#container #msg {\r\n"
				+ "   width: 350px;\r\n"
				+ "   height: 150px;\r\n"
				+ "   position: relative;\r\n"
				+ "   background-color: #4caf50;\r\n"
				+ "   border-radius: 15px;\r\n"
				+ "   font-weight: bold;\r\n"
				+ "}\r\n"
				+ "#container #action {   \r\n"
				+ "   position: relative;\r\n"
				+ "}\r\n"
				+ "</style></head>");
		sb.append("<body><center>");
		sb.append("<h1>Esito Pagamento</h1>");
		sb.append("<div id='container'>");
			sb.append("<div id='msg'>");
				sb.append("<br>");
				sb.append("<p>idPagamento: ").append(request.getParameter("idPagamento")).append("<p/>");
				sb.append("<p>" + request.getParameter("codEsito") + " - " + request.getParameter("descEsito") + "</p>");
			sb.append("</div>");
			sb.append("<div id='action'>");
				sb.append("<p>Puoi tornare su MOOn e <b>INVIARE</b> la tua istanza</p>");
			sb.append("</div>");
		sb.append("</div>");
		sb.append("</center></body></html>");
		return sb.toString();
	}
}
