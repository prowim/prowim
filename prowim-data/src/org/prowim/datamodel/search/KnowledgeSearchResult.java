/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 25.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 */
package org.prowim.datamodel.search;

import org.apache.commons.lang.Validate;


/**
 * A representation for the results of a knowledge search.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0a10
 */
public class KnowledgeSearchResult
{
    private String resultID = "";

    /**
     * 
     * The enumeration of knowledge types for which the search result will be sorted.
     * 
     * @author Thomas Wiesner
     * @version $Revision$
     * @since 2.0.0a10
     */
    public enum KnowledgeType
    {
        /**
         * The values for knowledge types
         */
        KNOWLEDGE_ELEMENT, PROCESS_ELEMENT, OE_ELEMENT
    };

    /**
     * 
     * The enumeration of knowledge sources for which the search result will be sorted.
     * 
     * @author Thomas Wiesner
     * @version $Revision$
     * @since 2.0.0a10
     */
    public enum KnowledgeSource
    {
        /**
         * The values for knowledge sources
         */
        DMS, WIKI, KNOWLEDGE_OBJECT, KNOWLEDGE_DOAMAIN, KNOWLEDGE_LINK, PROCESS
    }

    private String          title;
    private KnowledgeType   type;
    private KnowledgeSource source;

    /**
     * Constructs this result with the given parameter
     * 
     * @param title the title of the found document, wiki article etc.
     * @param type the type of the found document, like knowledge element, process element etc.
     * @param source the source of the found document
     */
    public KnowledgeSearchResult(String title, KnowledgeType type, KnowledgeSource source)
    {
        setTitle(title);
        setType(type);
        setSource(source);
    }

    /**
     * Returns the title of the result
     * 
     * @return the title, can not be <code>NULL</code>
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the title of the result
     * 
     * @param title the title to set, can not be <code>NULL</code>
     */
    public void setTitle(String title)
    {
        Validate.notNull(title, "The title of the result must be set");
        this.title = title;
    }

    /**
     * Returns the knowledge type
     * 
     * @return the type, can not be <code>NULL</code>
     */
    public KnowledgeType getType()
    {
        return type;
    }

    /**
     * Sets the knowledge type
     * 
     * @param knowledgeType the type to set, can not be <code>NULL</code>
     */
    public void setType(KnowledgeType knowledgeType)
    {
        Validate.notNull(knowledgeType, "The knowledge type must not be null");
        this.type = knowledgeType;
    }

    /**
     * Returns the knowledge source
     * 
     * @return the source, can not be <code>NULL</code>
     */
    public KnowledgeSource getSource()
    {
        return source;
    }

    /**
     * Sets the knowledge source
     * 
     * @param knowledgeSource the source to set, can not be <code>NULL</code>
     */
    public void setSource(KnowledgeSource knowledgeSource)
    {
        Validate.notNull(knowledgeSource, "The knowledge source must not be null");
        this.source = knowledgeSource;
    }

    /**
     * Get the id of result element
     * 
     * @return the resultID
     */
    public String getResultID()
    {
        return resultID;
    }

    /**
     * 
     * Set the id of result element.
     * 
     * @param id id of element. Not null.
     */
    public void setResultID(String id)
    {
        Validate.notNull(id, "ID can not be null");
        this.resultID = id;
    }

}
