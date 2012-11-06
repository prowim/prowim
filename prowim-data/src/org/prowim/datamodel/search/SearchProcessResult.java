/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 18.02.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.datamodel.prowim.Process;


/**
 * An extension of {@link KnowledgeSearchResult knowledge search result} for searching {@link Process} links in Ontology.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.1.0
 */
public class SearchProcessResult extends KnowledgeSearchResult
{
    private Process process = null;

    /**
     * Constructs the knowledge search result searching knowledge links in Ontology.
     * 
     * @param title {@link KnowledgeSearchLinkResult#setTitle(String)}
     * @param type {@link KnowledgeSearchLinkResult#setType(KnowledgeType)}
     * @param source {@link KnowledgeSearchLinkResult#setSource(KnowledgeSource)}
     * @param process {@link Process} which are searched.
     */
    public SearchProcessResult(String title, KnowledgeType type, KnowledgeSource source, Process process)
    {
        super(title, type, source);
        setProcess(process);
    }

    /**
     * 
     * Set the process
     * 
     * @param process {@link Process}
     */
    public void setProcess(Process process)
    {
        this.process = process;
    }

    /**
     * Return the {@link Process}
     * 
     * @return the process
     */
    public Process getProcess()
    {
        return process;
    }

}
