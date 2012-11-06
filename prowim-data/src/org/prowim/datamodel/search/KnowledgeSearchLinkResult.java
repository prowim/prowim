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
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;



/**
 * An extension of {@link KnowledgeSearchResult knowledge search result} for searching knowledge links in Ontology.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0
 */
public class KnowledgeSearchLinkResult extends KnowledgeSearchResult
{

    private String hyperLink;
    private String knowledgeLinkId;
    private String knowledgeLinkRepository;
    private String knowledgeObject;

    /**
     * Constructs the knowledge search result searching knowledge links in Ontology.
     * 
     * @param title {@link KnowledgeSearchLinkResult#setTitle(String)}
     * @param type {@link KnowledgeSearchLinkResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchLinkResult#setSource(KnowledgeSource)}
     * @param knowledgeLinkId {@link KnowledgeSearchLinkResult#setKnowledgeLinkId(String)}
     * @param knowledgeLinkRepository {@link KnowledgeSearchLinkResult#setKnowledgeLinkRepository(String)}
     * @param knowledgeObject {@link KnowledgeObject}, which this {@link KnowledgeLink} belong to
     */
    public KnowledgeSearchLinkResult(String title, KnowledgeType type, KnowledgeSource source, String knowledgeLinkId,
            String knowledgeLinkRepository, String knowledgeObject)
    {
        super(title, type, source);
        setKnowledgeLinkId(knowledgeLinkId);
        setKnowledgeLinkRepository(knowledgeLinkRepository);
        setKnowledgeObject(knowledgeObject);
    }

    /**
     * Returns the hyper link of the knowledge link
     * 
     * @return the hyperLink, can be <code>NULL</code>
     */
    public String getHyperLink()
    {
        return hyperLink;
    }

    /**
     * Sets the hyper link of the knowledge link
     * 
     * @param hyperLink the hyperLink to set, can be <code>NULL</code>
     */
    public void setHyperLink(String hyperLink)
    {
        this.hyperLink = hyperLink;
    }

    /**
     * Returns the id of the knowledge link
     * 
     * @return the knowledgeLinkId , can not be <code>NULL</code>
     */
    public String getKnowledgeLinkId()
    {
        return knowledgeLinkId;
    }

    /**
     * Sets the knowledge link id, can not be <code>NULL</code>
     * 
     * @param knowledgeLinkId the knowledgeLinkId to set
     */
    public void setKnowledgeLinkId(String knowledgeLinkId)
    {
        Validate.notNull(knowledgeLinkId, "The knowledge link id must not be null");
        Validate.notEmpty(knowledgeLinkId, "The knowledge link id must not be empty");

        this.knowledgeLinkId = knowledgeLinkId;
    }

    /**
     * Return the repository of the knowledge link
     * 
     * @return the knowledgeLinkRepository, can not be <code>NULL</code>
     */
    public String getKnowledgeLinkRepository()
    {
        return knowledgeLinkRepository;
    }

    /**
     * Sets the repository of the knowledge link, can not be <code>NULL</code>
     * 
     * @param knowledgeLinkRepository the knowledgeLinkRepository to set
     */
    public void setKnowledgeLinkRepository(String knowledgeLinkRepository)
    {
        Validate.notNull(knowledgeLinkRepository, "The repository of a knowledge link must not be null");
        Validate.notEmpty(knowledgeLinkRepository, "The repository of a knowledge link must not be empty");

        this.knowledgeLinkRepository = knowledgeLinkRepository;
    }

    /**
     * Set the {@link KnowledgeObject} where this {@link KnowledgeLink} belong to.
     * 
     * @param knowledgeObject the knowledgeObject to set
     */
    public void setKnowledgeObject(String knowledgeObject)
    {
        this.knowledgeObject = knowledgeObject;
    }

    /**
     * Get the {@link KnowledgeObject} where this {@link KnowledgeLink} belong to.
     * 
     * @return the knowledgeObject
     */
    public String getKnowledgeObject()
    {
        return knowledgeObject;
    }
}
