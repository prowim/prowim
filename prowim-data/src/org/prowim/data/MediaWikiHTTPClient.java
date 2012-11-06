/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 13.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *This file is part of ProWim.

ProWim is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

ProWim is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with ProWim.  If not, see <http://www.gnu.org/licenses/>.

Diese Datei ist Teil von ProWim.

ProWim ist Freie Software: Sie können es unter den Bedingungen
der GNU General Public License, wie von der Free Software Foundation,
Version 3 der Lizenz oder (nach Ihrer Option) jeder späteren
veröffentlichten Version, weiterverbreiten und/oder modifizieren.

ProWim wird in der Hoffnung, dass es nützlich sein wird, aber
OHNE JEDE GEWÄHELEISTUNG, bereitgestellt; sogar ohne die implizite
Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
Siehe die GNU General Public License für weitere Details.

Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.#
 */
package org.prowim.data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.prowim.datamodel.wiki.WikiContentProperties;
import org.xml.sax.InputSource;

import de.ebcot.tools.logging.Logger;


/**
 * A HTTP client wrapping {@link HttpClient apache http client} to connect to a media wiki.<br>
 * It encapsulates the access to the media wiki API
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a10
 */
public class MediaWikiHTTPClient
{
    /**
     * Description.
     */
    private static final String           WIKI_PASSWORD = "admBSC";

    /**
     * Description.
     */
    private static final String           WIKI_USER     = "WikiSysop";

    private static final Logger           LOG           = Logger.getLogger(MediaWikiHTTPClient.class);

    /**
     * A constant as a result of login process.
     */
    private static final String           SUCCESS       = "Success";

    /**
     * A constant as a result of login process.
     */
    private static final String           WRONG_PASS    = "WrongPass";

    /**
     * A constant as a result of login process.
     */
    private static final String           NOT_EXISTS    = "NotExists";

    /**
     * A constant as a result of login process.
     */
    private static final String           NEED_TOKEN    = "NeedToken";

    /**
     * The apache commons hhtp client to communicate with http server of the mediawiki.
     */
    private final HttpClient              httpClient;

    /**
     * A response handler for http response.
     */
    private final ResponseHandler<String> responseHandler;

    private String                        wikiURL;

    private String                        path;

    /**
     * Constructs the client and initializes a handler for http response.
     */
    public MediaWikiHTTPClient()
    {
        httpClient = new DefaultHttpClient();
        responseHandler = new ResponseHandler<String>()
        {

            public String handleResponse(HttpResponse response) throws ClientProtocolException
            {
                HttpEntity httpEntity = response.getEntity();
                if (httpEntity != null)
                {
                    try
                    {
                        return EntityUtils.toString(httpEntity);
                    }
                    catch (ParseException e)
                    {
                        LOG.error("Parsing the http response fails: ", e);
                        throw new RuntimeException(e.getMessage());
                    }
                    catch (IOException e)
                    {
                        LOG.error("An I/O Error occurs while handle the http response: ", e);
                        throw new RuntimeException(e.getMessage());
                    }
                }
                else
                {
                    return null;
                }
            }
        };
    }

    /**
     * Logs the user in in the given media wiki.
     * 
     * @param wikiURL the host URL for the mediawiki, e.g. <code>http://ebcot.prowim.com/wiki/</code>, can not be <code>NULL</code>
     * @throws IOException if login fails cause getting no connection to http server
     */
    public void login(String wikiURL) throws IOException
    {
        Validate.notNull(wikiURL, "The URL for the wiki host must not be null");
        this.wikiURL = wikiURL;

        URL hostURL = new URL(wikiURL);

        String currentpath = hostURL.getPath();

        if (currentpath.equalsIgnoreCase(""))
        {
            path = "/";
        }
        else
        {
            path = currentpath;
        }

        LOG.info("Logging in media wiki " + wikiURL);

        HttpHost host = new HttpHost(hostURL.getHost(), hostURL.getPort(), hostURL.getProtocol());
        httpClient.getParams().setParameter(ClientPNames.DEFAULT_HOST, host);

        MultipartEntity partEntity = new MultipartEntity();
        partEntity.addPart("lgname", new StringBody(WIKI_USER));
        partEntity.addPart("lgpassword", new StringBody(WIKI_PASSWORD));

        HttpPost postLogin = new HttpPost(path + "api.php5?action=login&format=xml");
        postLogin.setEntity(partEntity);

        HttpResponse execute = httpClient.execute(postLogin);

        ByteArrayOutputStream byte1 = new ByteArrayOutputStream();
        execute.getEntity().writeTo(byte1);
        String out = new String(byte1.toByteArray());

        String parseLoginContent = parseLoginContent(out);

        execute.getEntity().consumeContent();

        partEntity.addPart("lgtoken", new StringBody(parseLoginContent));
        postLogin = new HttpPost(path + "api.php5?action=login&format=xml");
        postLogin.setEntity(partEntity);

        execute = httpClient.execute(postLogin);

        byte1 = new ByteArrayOutputStream();
        execute.getEntity().writeTo(byte1);
        out = new String(byte1.toByteArray());

        parseLoginContent(out);
        execute.getEntity().consumeContent();
    }

    /**
     * Parses the content of the http response for the login.
     * 
     * @param out the output of http response
     * 
     * @return the parsed result of login
     * @throws IOException if login fails cause getting no connection to http server
     */
    private String parseLoginContent(String out) throws IOException
    {
        String loginResult = null;
        SAXBuilder builder = new SAXBuilder();
        Element root = null;
        try
        {
            Reader i = new StringReader(out);
            Document doc = builder.build(new InputSource(i));

            root = doc.getRootElement();

            Element loginEl = root.getChild("login");
            String result = loginEl.getAttributeValue("result");
            if (result.equalsIgnoreCase(SUCCESS))
            {
                String username = loginEl.getAttributeValue("lgusername");
                loginResult = username;
            }
            else if (result.equalsIgnoreCase(NEED_TOKEN))
            {
                String token = loginEl.getAttributeValue("token");
                loginResult = token;
            }
            else if (result.equalsIgnoreCase(WRONG_PASS))
            {
                throw new RuntimeException("The password for media wiki login was wrong");
            }
            else if (result.equalsIgnoreCase(NOT_EXISTS))
            {
                throw new RuntimeException("The user for media wiki login dows not exist");
            }

            return loginResult;
        }
        catch (JDOMException e)
        {
            throw new RuntimeException("Reading/Parsing the http response content fails");
        }
    }

    /**
     * Searches the given key word in all articles of the media wiki.
     * 
     * @param keyWord the key word to search in wiki articles
     * @return the result as list of article titles in xml format, can be <code>NULL</code>
     * @throws IOException if searching fails cause getting no connection to http server
     */
    public List<WikiContentProperties> searchFullText(String keyWord) throws IOException
    {
        String searchQuery = path + "api.php5?action=query&list=search&srwhat=text&srsearch='" + keyWord + "'&format=xml&srlimit=500";
        HttpGet search = new HttpGet(searchQuery);
        String searchResultXML = httpClient.execute(search, responseHandler);

        LOG.info("The result of search \n" + searchQuery + "\n was \n" + searchResultXML);

        List<WikiContentProperties> searchResult = parseSearchResult(searchResultXML);

        return searchResult;
    }

    /**
     * Parses the search result in XML format.
     * 
     * @param searchResultXML the result to parse
     * @return a list of wiki {@link WikiContentProperties content properties}
     * @throws IOException if error occurs parsing the search result
     */
    private List<WikiContentProperties> parseSearchResult(String searchResultXML) throws IOException
    {
        List<WikiContentProperties> wikiContentList = new ArrayList<WikiContentProperties>();

        SAXBuilder builder = new SAXBuilder();
        Element root = null;

        Reader i = new StringReader(searchResultXML);
        Document doc;
        try
        {
            doc = builder.build(new InputSource(i));
            root = doc.getRootElement();

            Element queryElement = root.getChild("query");
            Element searchElement = queryElement.getChild("search");

            List<Element> resultList = searchElement.getChildren();

            for (Element resultElement : resultList)
            {
                String title = resultElement.getAttributeValue("title");
                String hyperLink = createArticleURL(title);

                WikiContentProperties contentProperties = new WikiContentProperties(title, hyperLink);
                wikiContentList.add(contentProperties);
            }
        }
        catch (JDOMException e)
        {
            throw new RuntimeException("Reading/Parsing the http response content fails");
        }

        return wikiContentList;
    }

    /**
     * Creates the URL to the article in mediawiki.
     * 
     * @param title the title of the article
     */
    private String createArticleURL(String title)
    {
        return wikiURL + "index.php5/" + title;

    }
}