/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.10.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */
package org.prowim.datamodel.search;

import org.apache.commons.lang.Validate;


/**
 * An extension of {@link KnowledgeSearchResult search result} for searching in DMS.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0
 */
public class KnowledgeSearchDMSResult extends KnowledgeSearchResult
{

    private String documentId;
    private String documentVersion;
    private String documentName;

    /**
     * Constructs the search result for searching in DMS.
     * 
     * @param title the title of the found document, wiki article etc.
     * @param type the type of the found document, like knowledge element, process element etc.
     * @param source the source of the found document
     * @param documentId the document id of the found document
     * @param documentVersion the version of the found document
     * @param documentName the name of the document
     */
    public KnowledgeSearchDMSResult(String title, KnowledgeType type, KnowledgeSource source, String documentId, String documentVersion,
            String documentName)
    {
        super(title, type, source);
        setDocumentId(documentId);
        setDocumentVersion(documentVersion);
        setDocumentName(documentName);
    }

    /**
     * Sets the id of the document.
     * 
     * @param id the id to set, can be <code>NULL</code>
     * 
     */
    public void setDocumentId(String id)
    {
        Validate.notNull(id, "The document id must not be null");
        Validate.notEmpty(id, "The document id must not be empty");

        this.documentId = id;
    }

    /**
     * Returns the id of the document.
     * 
     * @return the id, can not be <code>NULL</code>
     */
    public String getDocumentId()
    {
        return documentId;
    }

    /**
     * Sets the version of the found document
     * 
     * @param documentVersion the documentVersion to set,can not be <code>NULL</code>
     */
    public void setDocumentVersion(String documentVersion)
    {
        Validate.notNull(documentVersion, "The document version must not be null");
        Validate.notEmpty(documentVersion, "The document version must not be empty");
        this.documentVersion = documentVersion;
    }

    /**
     * Returns the version of the found document
     * 
     * @return the documentVersion, can not be <code>NULL</code>
     */
    public String getDocumentVersion()
    {
        return documentVersion;
    }

    /**
     * Return the document name for opening the document of the result
     * 
     * @return the document name, can not be <code>NULL</code>
     */
    public String getDocumentName()
    {
        return documentName;
    }

    /**
     * Sets the document name for opening the document of the result, if <code>NULL</code> there is nothing to open
     * 
     * @param dcoumentName the document name to set, can not be <code>NULL</code>
     */
    public void setDocumentName(String dcoumentName)
    {
        Validate.notNull(dcoumentName, "The document name must not be null");
        Validate.notEmpty(dcoumentName, "The document name must not be empty");
        this.documentName = dcoumentName;
    }

}
