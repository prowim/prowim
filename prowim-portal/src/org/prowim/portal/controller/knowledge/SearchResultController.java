/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 30.11.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.portal.controller.knowledge;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.search.KnowledgeSearchLinkResult;
import org.prowim.datamodel.search.KnowledgeSearchResult;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeSource;
import org.prowim.datamodel.search.KnowledgeSearchResult.KnowledgeType;
import org.prowim.datamodel.wiki.WikiContentProperties;
import org.prowim.portal.MainController;



/**
 * Controller to get results of a search and put these in a table.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class SearchResultController
{
    /**
     * Index in selection result array for DMS
     */
    public static final int     SEARCH_ATTRI_DMS              = 0;

    /**
     * Index in selection result array for Wiki
     */
    public static final int     SEARCH_ATTRI_WIKI             = 1;

    /**
     * Index in selection result array for Knowledge Object
     */
    public static final int     SEARCH_ATTRI_KNOWLEDGE_OBJECT = 2;

    /**
     * Index in selection result array for Knowledge Link
     */
    public static final int     SEARCH_ATTRI_KNOWLEDGE_LINK   = 3;

    /**
     * Index in selection result array for Knowledge Domain
     */
    public static final int     SEARCH_ATTRI_KNOWLEDGE_DOMAIN = 4;

    /**
     * Index in selection result array for {@link Process}
     */
    public static final int     SEARCH_ATTRI_PROCESS          = 5;

    /**
     * The constant for a single quote.
     */
    private static final String SINGLE_QOUTE                  = "'";

    /**
     * The constant to match all occurrences of the key words .
     */
    private static final char   ASTERISK                      = '*';

    /**
     * The constant to seperate keywords in expression.
     */
    private static final char   WHITESPACE                    = ' ';

    /**
     * The instance for a logger
     */
    private final static Logger LOG                           = Logger.getLogger(SearchResultController.class);

    /**
     * An array of selected attributes for searching. The index describes the search attribute e.g. DMS, the value, if it is on (<code>TRUE</code>) or off (<code>FALSE</code>)
     */
    private boolean[]           searchSelections;

    /**
     * The constructor return to given keyWord the founded results.
     */
    public SearchResultController()
    {
    }

    /**
     * Returns the result of searching the given key word.
     * 
     * @param keyWord the key word to search
     * 
     * @return the result list, can not be <code>NULL</code>
     */
    public List<KnowledgeSearchResult> search(final String keyWord)
    {
        List<KnowledgeSearchResult> searchResults = new ArrayList<KnowledgeSearchResult>();

        if ( !StringUtils.isEmpty(keyWord))
        {
            String searchExpression;
            if (searchSelections[SEARCH_ATTRI_DMS])
            {
                // search DMS
                searchExpression = parseKeyWordForDMS(keyWord);
                LOG.info("The parsed search expression for DMS is " + searchExpression);
                List<DocumentContentProperties> dmsResults = MainController.getInstance().searchDMSFullText(searchExpression);
                for (DocumentContentProperties documentContentProperties : dmsResults)
                {
                    KnowledgeSearchResult knowledgeSearchResult = DefaultDataObjectFactory
                            .createKnowledgeSearchDMSResult(documentContentProperties.getTitle(), KnowledgeType.KNOWLEDGE_ELEMENT,
                                                            KnowledgeSource.DMS, documentContentProperties.getID(),
                                                            documentContentProperties.getVersion(), documentContentProperties.getContentName());
                    searchResults.add(knowledgeSearchResult);
                }
            }
            if (searchSelections[SEARCH_ATTRI_WIKI])
            {
                // search Wiki
                searchExpression = parseKeyWordForWiki(keyWord);
                LOG.info("The parsed search expression for Wiki is " + searchExpression);
                List<WikiContentProperties> searchWikiFullText = MainController.getInstance()
                        .searchWikiFullText(StringUtils.deleteWhitespace(searchExpression));
                for (WikiContentProperties wikiContentProperties : searchWikiFullText)
                {
                    KnowledgeSearchResult knowledgeSearchResult = DefaultDataObjectFactory.createKnowledgeSearchWikiResult(wikiContentProperties
                            .getTitle(), KnowledgeType.KNOWLEDGE_ELEMENT, KnowledgeSource.WIKI, wikiContentProperties.getWikiHyperLink());
                    searchResults.add(knowledgeSearchResult);
                }
            }
            // search Knowledge in ontology
            ObjectArray searchKnowledgeResults = MainController.getInstance().searchKeyWord(keyWord);
            for (Object searchKnowledgeResult : searchKnowledgeResults)
            {
                KnowledgeSearchResult knowledgeSearchResult = null;

                String title = null;
                String hyperLink = null;
                if (searchKnowledgeResult instanceof KnowledgeObject)
                {
                    if (searchSelections[SEARCH_ATTRI_KNOWLEDGE_OBJECT])
                    {
                        KnowledgeObject knowledgeObject = (KnowledgeObject) searchKnowledgeResult;
                        title = knowledgeObject.getName();
                        knowledgeSearchResult = DefaultDataObjectFactory.createKnowledgeSearchResult(title, KnowledgeType.KNOWLEDGE_ELEMENT,
                                                                                                     KnowledgeSource.KNOWLEDGE_OBJECT);
                        knowledgeSearchResult.setResultID(knowledgeObject.getID());
                    }

                }
                else if (searchKnowledgeResult instanceof KnowledgeLink)
                {
                    if (searchSelections[SEARCH_ATTRI_KNOWLEDGE_LINK])
                    {
                        KnowledgeLink knowledgeLink = (KnowledgeLink) searchKnowledgeResult;
                        title = knowledgeLink.getName();
                        hyperLink = knowledgeLink.getHyperlink();
                        String id = knowledgeLink.getID();
                        String repository = knowledgeLink.getRepository();
                        knowledgeSearchResult = DefaultDataObjectFactory.createKnowledgeSearchLinkResult(title, KnowledgeType.KNOWLEDGE_ELEMENT,
                                                                                                         KnowledgeSource.KNOWLEDGE_LINK, id,
                                                                                                         repository,
                                                                                                         knowledgeLink.getKnowledgeObject());
                        ((KnowledgeSearchLinkResult) knowledgeSearchResult).setHyperLink(hyperLink);
                    }

                }
                else if (searchKnowledgeResult instanceof KnowledgeDomain)
                {
                    if (searchSelections[SEARCH_ATTRI_KNOWLEDGE_DOMAIN])
                    {
                        KnowledgeDomain knowledgeDomain = (KnowledgeDomain) searchKnowledgeResult;
                        title = knowledgeDomain.getName();
                        knowledgeSearchResult = DefaultDataObjectFactory.createKnowledgeSearchResult(title, KnowledgeType.KNOWLEDGE_ELEMENT,
                                                                                                     KnowledgeSource.KNOWLEDGE_DOAMAIN);
                        knowledgeSearchResult.setResultID(knowledgeDomain.getID());
                    }

                }
                else if (searchKnowledgeResult instanceof Process)
                {
                    if (searchSelections[SEARCH_ATTRI_PROCESS])
                    {
                        Process process = (Process) searchKnowledgeResult;
                        title = process.getName();
                        knowledgeSearchResult = DefaultDataObjectFactory.createSearchProcessResult(title, KnowledgeType.PROCESS_ELEMENT,
                                                                                                   KnowledgeSource.PROCESS, process);
                    }
                }
                else
                {
                    throw new IllegalArgumentException("The type of " + searchKnowledgeResult.getClass() + " is not supported");
                }

                if (knowledgeSearchResult != null)
                {
                    searchResults.add(knowledgeSearchResult);
                }
            }
        }
        return searchResults;
    }

    /**
     * Parses the key word(s) to match exactly all key words in the search for media wiki.
     * 
     * @param keyWord the key words to search
     * @return the expression to search in media wiki
     */
    private String parseKeyWordForWiki(String keyWord)
    {
        StringBuilder expression = new StringBuilder();

        String[] keyWords = StringUtils.split(keyWord, WHITESPACE);

        for (int i = 0; i < keyWords.length; i++)
        {
            String singleKeyWord = keyWords[i];
            expression.append(SINGLE_QOUTE);
            expression.append(singleKeyWord);
            expression.append(ASTERISK);
            expression.append(SINGLE_QOUTE);
        }

        return expression.toString().trim();
    }

    /**
     * 
     * Parses the key word(s) to match all key words in the search for DMS.
     * 
     * @param keyWord the key word(s) to search
     * @return the expression to search in DMS
     */
    private String parseKeyWordForDMS(String keyWord)
    {
        StringBuilder expression = new StringBuilder();

        String[] keyWords = StringUtils.split(keyWord, WHITESPACE);

        for (int i = 0; i < keyWords.length; i++)
        {
            String singleKeyWord = keyWords[i];
            expression.append(singleKeyWord);
            expression.append(ASTERISK);
            expression.append(WHITESPACE);
        }

        return expression.toString().trim();
    }

    /**
     * Sets the result of selecting search attributes.
     * 
     * @param searchSelections the array with result of selected search attributes
     */
    public void setSelections(boolean[] searchSelections)
    {
        this.searchSelections = searchSelections;
    }
}
