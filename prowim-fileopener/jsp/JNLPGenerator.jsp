<%@page import="java.net.URLEncoder"%>
<%@ page language="java" contentType="application/x-java-jnlp-file; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
String codeBase = request.getScheme() + "://" + request.getServerName() + "/prowim-jnlp";
String requestedDocument = request.getParameter("DOC_PATH");
if (requestedDocument != null) {
	requestedDocument = URLEncoder.encode(requestedDocument, "UTF-8");
}
response.setHeader("Pragma", "no-cache");
%>
<?xml version="1.0" encoding="UTF-8"?>
<jnlp codebase="<% out.print(codeBase); %>" spec="1.0+">
  <information>
    <title>Datei öffnen</title>
    <vendor>Ebcot Business Solutions GmbH</vendor>
    <homepage href="index.html"/>
    <description>Die Datei wird vom lokalen Betriebssystem geöffnet.</description>
  </information>
  <resources>
    <j2se version="1.6+"/>
    <jar href="fileopener.jar"/>
  </resources>
    
  <application-desc main-class="de.ebcot.prowim.filesystem.open.OpenFileApp">
  <% 
  	if (requestedDocument != null && !"".trim().equals(requestedDocument)){
  %>
  		<argument><%= requestedDocument %></argument>		
  <%
  	}
  %>
  
  </application-desc>
  <security>
    <all-permissions/>
  </security>
</jnlp>
