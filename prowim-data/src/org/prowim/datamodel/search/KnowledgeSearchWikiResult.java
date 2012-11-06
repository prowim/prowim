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
 */
package org.prowim.datamodel.search;

import org.apache.commons.lang.Validate;


/**
 * An extension of {@link KnowledgeSearchResult knowledge search result} for searching in Wiki.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0
 */
public class KnowledgeSearchWikiResult extends KnowledgeSearchResult
{

    private String hyperLink;

    /**
     * Constructs the knowledge result for searching in Wiki.
     * 
     * @param title {@link KnowledgeSearchResult#setTitle(String)}
     * @param type {@link KnowledgeSearchResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchResult#setSource(KnowledgeSource)}
     * @param hyperLink the hyper link of the wiki article
     */
    public KnowledgeSearchWikiResult(String title, KnowledgeType type, KnowledgeSource source, String hyperLink)
    {
        super(title, type, source);
        setHyperLink(hyperLink);
    }

    /**
     * Return the hyper link of the wiki article
     * 
     * @return the hyperLink, must be <code>NOT NULL</code>
     */
    public String getHyperLink()
    {
        return hyperLink;
    }

    /**
     * Sets the hyper link of the wiki article
     * 
     * @param hyperLink the hyperLink to set, must be <code>NOT NULL</code>
     */
    public void setHyperLink(String hyperLink)
    {
        Validate.notNull(hyperLink, "The link of a wiki article must not be null");
        Validate.notEmpty(hyperLink, "The link of a wiki article must not be empty");

        this.hyperLink = hyperLink;
    }

}
