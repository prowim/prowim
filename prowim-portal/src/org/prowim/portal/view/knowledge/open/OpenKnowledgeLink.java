/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.portal.view.knowledge.open;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.eclipse.rwt.widgets.ExternalBrowser;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.collections.KnowledgeRepositoryArray;
import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeRepository;
import org.prowim.datamodel.prowim.Parameter;
import org.prowim.datamodel.search.KnowledgeSearchDMSResult;
import org.prowim.datamodel.search.KnowledgeSearchLinkResult;
import org.prowim.datamodel.search.KnowledgeSearchResult;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeSource;
import org.prowim.datamodel.search.KnowledgeSearchWikiResult;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.feedback.WarningDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.utils.GlobalFunctions;
import org.prowim.resources.ResourcesLocator;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Organize the opening of files and URLs to a given knowledge link. By URI the link would gives oder to the <br>
 * external browser and will shown in it. This is the case, when the knowledge link is "Internet" or "Prowim Wiki". <br>
 * In case of "Prowim DMS" the class call the content of file from data base and creates a download link for it. <br>
 * Finally the link will be sent to a browser widget and this will shown this.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public final class OpenKnowledgeLink
{
    private static final Logger LOG               = Logger.getLogger(OpenKnowledgeLink.class);
    private static final String SERVLET_PARAMETER = "/prowim-jnlp/JNLPGenerator.jsp?DOC_PATH=";
    private static final String URL_PREFIX        = "http://";
    private static final String OPEN              = "org.eclipse.rwt.widgets.ExternalBrowser." + "open( \"{0}\", \"{1}\", \"{2}\" );";

    /**
     * hidden default constructor
     */
    private OpenKnowledgeLink()
    {
    }

    /**
     * 
     * Open to given knowledge link the existing file or URL.
     * 
     * @param knowLinkLeaf opens to this given knowledge link
     */
    public static void open(final KnowledgeLinkLeaf knowLinkLeaf)
    {
        String repository = knowLinkLeaf.getRepository();

        KnowledgeRepositoryArray knowRepArray = MainController.getInstance().getKnowledgeRepositories();

        for (KnowledgeRepository rep : knowRepArray)
        {
            if (rep.getID().equals(repository))
            {
                repository = rep.getName();
                break;
            }
        }

        String documentID = knowLinkLeaf.getID();
        if (repository.equals(GlobalConstants.PROWIM_DMS))
        {
            try
            {
                openDocument(documentID);
            }
            catch (Exception e)
            {
                LOG.error("Error by opening file in OpenKnwoLink: ", e);
            }
        }
        else
        {
            String hyperLink = knowLinkLeaf.getLink();

            if (StringUtils.isEmpty(hyperLink))
            {
                WarningDialog.openWarning(null, Resources.Frames.Dialog.Texts.WARNING_NO_LINK.getText());
            }
            else
            {
                if (repository.equals(GlobalConstants.PROWIM_WIKI) || repository.equals(GlobalConstants.INTERNET))
                {
                    if ( !hyperLink.startsWith(GlobalConstants.HTTP) && !hyperLink.startsWith(GlobalConstants.HTTPS))
                    {
                        knowLinkLeaf.setLink(GlobalConstants.HTTP + hyperLink);
                    }

                    // Open the given URL in a external browser
                    openURL(documentID, knowLinkLeaf.getLink());
                }
                else if (repository.equals(GlobalConstants.NETWORK))
                {
                    String filename = hyperLink;
                    filename = filename.replace('\\', '/');

                    String serverName = ResourcesLocator.getServerHost();
                    try
                    {
                        filename = URLEncoder.encode(filename, "UTF-8");
                    }
                    catch (UnsupportedEncodingException e)
                    {
                        LOG.error("The encoding UTF-8 is not supported ", e);
                    }

                    // Create a hidden dialog to open created URL
                    new DownloadDialog(PlatformUI.createDisplay().getActiveShell(), URL_PREFIX + serverName + SERVLET_PARAMETER + filename);
                }
                else
                    throw new IllegalArgumentException(documentID);

            }
        }
    }

    /**
     * Opens the given hyper link in an external browser.
     * 
     * @param documentID the id of the document
     * @param hyperLink the url to open in browser
     */
    private static void openURL(String documentID, String hyperLink)
    {
        ExternalBrowser.open(documentID, EscapeFunctions.toJSString(hyperLink), ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR
                | ExternalBrowser.STATUS);
    }

    /**
     * Opens the document with the given ID.
     * 
     * @param documentID the id of the document, can not be <code>NULL</code>
     * 
     */
    private static void openDocument(String documentID)
    {
        Validate.notNull(documentID);
        // Get Document from Data base
        showDocument(MainController.getInstance().downloadDocument(documentID));
    }

    /**
     * 
     * Opens the document for the given uuid and the version.
     * 
     * @param uuid the id of the documents
     * @param version the version of the document
     */
    private static void openDocument(String uuid, String version)
    {
        Validate.notNull(uuid);
        Validate.notNull(version);
        // Get Document from Data base
        showDocument(MainController.getInstance().downloadDocumentFromID(uuid, version));
    }

    /**
     * 
     * Shows the given document at browser.
     * 
     * @param document {@link Document} Not null.
     */
    private static void showDocument(Document document)
    {
        if (document != null)
        {
            // This part is deal with the suffixes in older file, which are stored in DMS. Check if a file has this suffix and cut it to show this.
            Document doc = GlobalFunctions.renameDocument(document);
            DownloadFile.download(doc.getContent(), doc.getName());
        }
    }

    /**
     * 
     * Open to given knowledge link the existing file or URL.
     * 
     * @param parameter opens this given parameter
     * 
     */
    public static void open(final Parameter parameter)
    {
        String infoType = parameter.getInfoTypeID();

        String documentID = parameter.getID();
        if (infoType.equals(GlobalConstants.DOCUMENT))
        {
            try
            {
                openDocument(documentID);

            }
            catch (Exception e)
            {
                LOG.error("Error by opening file in OpenKnwoLink: ", e);
            }
        }
        else if (infoType.equals(GlobalConstants.LINK))
        {

            // Open the given URL in a external browser
            ExternalBrowser.open(documentID, parameter.toString(), ExternalBrowser.LOCATION_BAR | ExternalBrowser.NAVIGATION_BAR
                    | ExternalBrowser.STATUS);

        }
        else
            throw new IllegalArgumentException(documentID);
    }

    /**
     * Opens the depending document e.g. file or URL of the knowledge search result.
     * 
     * @param knowledgeSearchResult the search result, can not be <code>NULL</code>
     */
    public static void open(KnowledgeSearchResult knowledgeSearchResult)
    {
        Validate.notNull(knowledgeSearchResult, "The search result ot open a document muts not be null");

        KnowledgeSource knowledgeSource = knowledgeSearchResult.getSource();
        String title;
        String hyperLink;

        switch (knowledgeSource)
        {
            case DMS:
                KnowledgeSearchDMSResult knowledgeSearchDMSResult = (KnowledgeSearchDMSResult) knowledgeSearchResult;
                String documentID = knowledgeSearchDMSResult.getDocumentId();
                String documentVersion = knowledgeSearchDMSResult.getDocumentVersion();
                openDocument(documentID, documentVersion);
                break;
            case WIKI:
                KnowledgeSearchWikiResult knowledgeSearchWikiResult = (KnowledgeSearchWikiResult) knowledgeSearchResult;
                hyperLink = knowledgeSearchWikiResult.getHyperLink();
                title = knowledgeSearchWikiResult.getTitle();
                openURL(title, hyperLink);
                break;
            case KNOWLEDGE_LINK:
                KnowledgeSearchLinkResult knowledgeSearchLinkResult = (KnowledgeSearchLinkResult) knowledgeSearchResult;
                hyperLink = knowledgeSearchLinkResult.getHyperLink();
                title = knowledgeSearchLinkResult.getTitle();
                String knowledgeLinkId = knowledgeSearchLinkResult.getKnowledgeLinkId();
                String knowledgeLinkRepository = knowledgeSearchLinkResult.getKnowledgeLinkRepository();

                KnowledgeLink knowledgeLink = DefaultDataObjectFactory.createKnowledgeLink(knowledgeLinkId, title, "tempTime");
                knowledgeLink.setRepository(knowledgeLinkRepository);

                KnowledgeLinkLeaf knowledgeLinkModel = new KnowledgeLinkLeaf(knowledgeLink);
                knowledgeLinkModel.setLink(hyperLink);

                open(knowledgeLinkModel);
                break;
            default:
                throw new IllegalArgumentException("The knowledge source " + knowledgeSource + " is not supported");
        }
    }
}
