/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:50 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/SearchService.java $
 * $LastChangedRevision: 5032 $
 *------------------------------------------------------------------------------
 * (c) 11.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.dms.alfresco;

import java.rmi.RemoteException;

import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.axis.utils.StringUtils;
import org.apache.commons.lang.Validate;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DocumentContentProperties;



/**
 * Searchs in DMS. All search operation are until yet in the base class implemented. see {@link SearchBase}.
 * 
 * @author Saad Wardi
 * @version $Revision: 5032 $
 */
public class SearchService extends SearchBase implements Search
{
    /**
     * Description.
     */
    private static final char   WHITE_SPACE = ' ';
    private static final String DOUBLE_DOT  = ":";

    /**
     * Creates an instance.
     */
    public SearchService()
    {
        super();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Search#getAllDocuments(java.lang.String)
     */
    public DocumentContentPropertiesArray getAllDocuments(final String spaceID) throws RemoteException
    {
        Validate.notNull(spaceID);

        DocumentContentPropertiesArray result = new DocumentContentPropertiesArray();

        /** Get a reference to the respository web service */
        final RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();

        final Reference reference = new Reference(DMSStoreRegistry.STORE_REF, spaceID, null);
        final QueryResult queryResult = repositoryService.queryChildren(reference);

        /** Store and log the results */
        final ResultSet resultSet = queryResult.getResultSet();
        final ResultSetRow[] rows = resultSet.getRows();

        if (rows != null)
        {
            result = this.createContentPropertiesArray(rows);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Search#retrieveMetaData(java.lang.String)
     */
    @Override
    public DocumentContentProperties retrieveMetaData(String uuid)
    {
        Validate.notNull(uuid);
        final RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();
        final Reference reference = new Reference(DMSStoreRegistry.STORE_REF, uuid, null);
        DocumentContentProperties contentProperties = null;
        Node[] nodes;
        try
        {
            nodes = repositoryService.get(new Predicate(new Reference[] { reference }, DMSStoreRegistry.STORE_REF, null));
            String name = null;
            String title = null;
            String description = null;
            String author = null;
            String id = uuid;
            String version = null;
            String contentType = null;

            for (int i = 0; i < nodes[0].getProperties().length; i++)
            {
                /** Get the name property. */
                if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.NAME_PROP))
                {
                    name = nodes[0].getProperties(i).getValue();
                }
                /** Get the title property. */
                else if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.TITLE_PROP))
                {
                    title = nodes[0].getProperties(i).getValue();
                }

                /** Get the description property. */
                else if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.DESCRIPTION_PROP))
                {
                    description = nodes[0].getProperties(i).getValue();
                }
                /** Get the author property. */
                else if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.AUTHOR_PROP))
                {
                    author = nodes[0].getProperties(i).getValue();
                }
                /** Get the version label property. */
                else if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.VERSION_LABEL_PROP))
                {
                    version = nodes[0].getProperties(i).getValue();
                }
                /** Get the content type property. */
                else if (nodes[0].getProperties(i).getName().equalsIgnoreCase(DMSConstants.Content.MIMETYPE_PROP))
                {
                    contentType = nodes[0].getProperties(i).getValue();
                }

            }

            contentProperties = new DocumentContentProperties(id, name, title, description, author, formateCreated(id), version, contentType);

        }
        catch (RepositoryFault e)
        {
            LOG.error("repository fault :  ", e);
        }
        catch (RemoteException e)
        {
            LOG.error("remote exception  :  ", e);
        }

        return contentProperties;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.dms.alfresco.Search#searchFullText(java.lang.String, java.lang.String)
     */
    @Override
    public DocumentContentPropertiesArray searchFullText(String keyWord, String customer) throws RemoteException
    {
        DocumentContentPropertiesArray result = new DocumentContentPropertiesArray();

        /** Get a reference to the respository web service */
        final RepositoryServiceSoapBindingStub repositoryService = WebServiceFactory.getRepositoryService();

        String pathstatement = "(" + SEARCH_ROOT_PATH + "cm:" + customer + "//*\") AND ";

        String contentStatment = createContentStatement(keyWord);

        String folderStatement = createFolderStatement(keyWord);

        String statement = pathstatement + contentStatment + folderStatement;

        LOG.debug("The SEARCH query for full text search :\n " + statement);
        Query searchQuery = new Query(Constants.QUERY_LANG_LUCENE, statement);
        QueryResult queryResult = repositoryService.query(DMSStoreRegistry.STORE_REF, searchQuery, true);

        ResultSet resultSet = queryResult.getResultSet();
        ResultSetRow[] rows = resultSet.getRows();

        if (rows != null)
        {
            LOG.debug("Found 'ResultSet' " + resultSet + " with 'rows' " + rows);
            DocumentContentPropertiesArray documentContentPropertiesArray = this.createContentPropertiesArray(rows);
            for (DocumentContentProperties documentContentProperties : documentContentPropertiesArray)
            {
                String title = documentContentProperties.getTitle();

                if ( !title.startsWith(PROCESS_PREFIX))
                {
                    result.add(documentContentProperties);
                }
            }
            return result;
        }
        return result;
    }

    /**
     * Description.
     * 
     * @param keyWord
     * @return
     */
    private String createFolderStatement(String keyWord)
    {
        String[] searchWord = StringUtils.split(keyWord, WHITE_SPACE);

        StringBuilder statementBuilder = new StringBuilder();
        String startFolderStatement = "( TYPE:\"{" + Constants.NAMESPACE_CONTENT_MODEL + "}folder\" AND (";

        statementBuilder.append(startFolderStatement);

        String endFolderStatement = "))";

        for (int i = 0; i < searchWord.length; i++)
        {
            String currentSearch = searchWord[i];
            String folderStatement = "@\\{" + ESCAPED_NAMESPACE_CONTENT_MODEL + "\\}name" + DOUBLE_DOT + "\"" + escape(currentSearch) + "\" ";

            statementBuilder.append(folderStatement);

        }
        statementBuilder.append(endFolderStatement);

        return statementBuilder.toString();
    }

    /**
     * Creates query statement for the 'content' part for searching the keyword.
     * 
     * @param keyWord the key word to search
     * @return
     */
    private String createContentStatement(String keyWord)
    {
        StringBuilder statementBuilder = new StringBuilder();
        String[] searchWord = StringUtils.split(keyWord, WHITE_SPACE);

        String startContentStatement = "( TYPE:\"{" + Constants.NAMESPACE_CONTENT_MODEL + "}content\" AND (";
        String endContentStatement = "))";

        statementBuilder.append(startContentStatement);

        for (int i = 0; i < searchWord.length; i++)
        {
            String currentSearchWord = searchWord[i];
            String contentStatement = "@\\{" + ESCAPED_NAMESPACE_CONTENT_MODEL + "\\}name" + DOUBLE_DOT + "\"" + escape(currentSearchWord) + "\" ";
            statementBuilder.append(contentStatement);
        }

        if (searchWord.length == 1)
        {
            String textContentStatement = "TEXT:\"" + escape(keyWord) + "\"";
            statementBuilder.append(textContentStatement);
        }
        else
        {
            statementBuilder.append('(');
            for (int i = 0; i < searchWord.length; i++)
            {
                String currentSearchWord = searchWord[i];
                String textContentStatement = "TEXT:\"" + escape(currentSearchWord) + "\" ";
                statementBuilder.append(textContentStatement);

            }
            statementBuilder.append(')');
        }

        statementBuilder.append(endContentStatement);

        return statementBuilder.toString();
    }
}
