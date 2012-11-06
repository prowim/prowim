/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-27 18:12:35 +0200 (Fr, 27 Aug 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/dms/alfresco/SearchBase.java $
 * $LastChangedRevision: 4713 $
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

import org.alfresco.webservice.authoring.AuthoringFault;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Version;
import org.alfresco.webservice.types.VersionHistory;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.joda.time.DateTime;
import org.prowim.datamodel.collections.DocumentContentPropertiesArray;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.utils.DateUtility;

import de.ebcot.tools.logging.Logger;


/**
 * Base methods to search into alfresco DMS. Classes can extends this class to provide the base search operations and the extended operations.<br>
 * At time are base operations: <br>
 * - getAllItems(String baseName): Gets all stored documents in the given spacename.<br>
 * - retrieveMetaData(String id): Gets the meta data such (contentName, author, createTime , ...) to a document.
 * 
 * @author Saad Wardi
 * @version $Revision: 4713 $
 */
public abstract class SearchBase
{
    /**
     * The instance of a logger
     */
    protected static final Logger LOG                             = Logger.getLogger(SearchBase.class);

    /**
     * The search path in XPath-Notation for Lucene
     */
    protected static final String SEARCH_ROOT_PATH                = "PATH:\"/app:company_home/cm:ProWim/";

    /**
     * The alfresco name space for content model with escaping some chars
     */
    protected static final String ESCAPED_NAMESPACE_CONTENT_MODEL = "http\\://www.alfresco.org/model/content/1.0";

    /**
     * The lucene notation for full text search
     */
    protected static final String SEARCH_TEXT                     = "TEXT:";

    /**
     * A prefix to filter prcoess documents
     */
    protected static final String PROCESS_PREFIX                  = "Prozess_";
    private static final String   TITLE_PROPERTY                  = DMSConstants.Content.TITLE_PROP;
    private static final String   NO_TIME_DEFINED                 = "No Time defined";

    /**
     * Creates an instance with creating a session object to the alfresco DMS.
     */
    protected SearchBase()
    {
    }

    /**
     * Formats a date.
     * 
     * @param createdIn the date as ISOxxxx format
     * @return the formated date as dd.mm.yyy hh:mm
     */
    protected String formateCreated(String createdIn)
    {
        Validate.notNull(createdIn);
        return getDocumentInitialVersionCreatedProperty(createdIn);
    }

    /**
     * Returns a String where those characters that QueryParser expects to be escaped are escaped by a preceding <code>\</code>. '*' and '?' are not escaped.
     * 
     * @param s string to escape
     * @return escaped String, can not be <code>NULL</code>
     */
    protected String escape(String s)
    {
        StringBuffer sb = new StringBuffer(s.length() + 4);
        char[] characterToEscape = { '\\', '+', '-', '!', '(', ')', ':', '^', '[', ']', '\"', '{', '}', '~' };
        for (int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if (ArrayUtils.contains(characterToEscape, c))
            {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Iterates the returned ResultSetRow from the search and creates a {@link DocumentContentPropertiesArray}.
     * 
     * @param rows the returned result set not null
     * @return not null {@link DocumentContentPropertiesArray}. if no item exists, an empty list is returned.
     */
    protected DocumentContentPropertiesArray createContentPropertiesArray(ResultSetRow[] rows)
    {
        Validate.notNull(rows);
        final DocumentContentPropertiesArray contentPropertiesArray = new DocumentContentPropertiesArray();
        for (int x = 0; x < rows.length; x++)
        {
            ResultSetRow row = rows[x];

            ResultSetRowNode node = row.getNode();
            String id = node.getId();
            if ( !isProcessModel(row))
                contentPropertiesArray.add(retrieveMetaData(id));
        }
        return contentPropertiesArray;
    }

    /**
     * Retrieves the properties of a content.
     * 
     * @param uuid the uuid of the content.
     * @return not null {@link DocumentContentProperties}
     */
    public abstract DocumentContentProperties retrieveMetaData(String uuid);

    /**
     * Checks if a document is the process chart document.
     * 
     * @param row the row to be checked. not null.
     * @return if the row is a process row.
     */
    private boolean isProcessModel(ResultSetRow row)
    {
        Validate.notNull(row);
        NamedValue[] columns = row.getColumns();
        for (int y = 0; y < columns.length; y++)
        {
            NamedValue column = row.getColumns(y);
            LOG.debug("Found column for 'row' " + row + " name: " + column.getName() + " value: " + column.getValue());
            if (column.getName().equals(TITLE_PROPERTY))
            {
                String name = column.getValue();
                return (name.startsWith(PROCESS_PREFIX));
            }
        }
        return false;
    }

    private String getDocumentInitialVersionCreatedProperty(String uuid)
    {
        Validate.notNull(uuid);
        /** Get the version history. */
        VersionHistory versionHistory = null;
        try
        {
            versionHistory = WebServiceFactory.getAuthoringService().getVersionHistory(new Reference(DMSStoreRegistry.STORE_REF, uuid, null));
        }
        catch (AuthoringFault e)
        {
            LOG.error("Authoring fault: ", e);
        }
        catch (RemoteException e)
        {
            LOG.error("Remote exception: ", e);
        }

        if (versionHistory != null)
        {
            Version[] versions = versionHistory.getVersions();

            if (versions != null)
            {
                for (Version version : versions)
                {
                    if (version.getLabel().equals(DMSConstants.INITIAL_VERSION_LABEL))
                    {
                        long createdAsLong = version.getCreated().getTime().getTime();
                        org.prowim.datamodel.dms.Version clientVersion = new org.prowim.datamodel.dms.Version(
                                                                                                                        DateUtility
                                                                                                                                .getDateTimeString(new DateTime(
                                                                                                                                                                createdAsLong)),
                                                                                                                        version.getCreator(), version
                                                                                                                                .getLabel());

                        return clientVersion.getCreateTime();
                    }
                }
            }
        }
        return NO_TIME_DEFINED;
    }
}
