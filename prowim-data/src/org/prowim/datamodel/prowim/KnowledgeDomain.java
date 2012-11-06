/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-01 17:47:11 +0100 (Di, 01 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/KnowledgeDomain.java $
 * $LastChangedRevision: 2870 $
 *------------------------------------------------------------------------------
 * (c) 01.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.prowim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a data-object-class represents {@link KnowledgeDomain}.
 * 
 * @author Saad Wardi
 * @version $Revision: 2870 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KnowledgeDomain", propOrder = { "subdomains", "knowledgeObjects", "hasSubdomains", "countSubdomains", "countKnowledgeObjects",
        "isIndirectDomain" })
public class KnowledgeDomain extends ProcessElement implements Comparable<Object>
{

    /** the sub domains list. */
    private KnowledgeDomain[] subdomains;
    /** the Knowledge objects. */
    private KnowledgeObject[] knowledgeObjects;
    /** the has sub domains flag. */
    private int               hasSubdomains         = 0;
    /** the sub domains count. */
    private int               countSubdomains       = 0;
    /** the knowledgeObjects objects count. */
    private int               countKnowledgeObjects = 0;
    /** the isIndirect domain flag. */
    private boolean           isIndirectDomain      = false;

    /**
     * Creates a DomainKnowledge with id and name.
     * 
     * @param id {@link KnowledgeDomain#setID(String)} not null.
     * @param name {@link KnowledgeDomain#setName(String)} not null.
     * @param createTime {@link KnowledgeObject#setCreateTime(String)}
     */
    protected KnowledgeDomain(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * no-arg constructor.
     */
    protected KnowledgeDomain()
    {

    }

    /**
     * Gets the count of the sub domains.
     * 
     * @return Returns the countSubdomains.
     */

    public int getCountSubdomains()
    {
        return countSubdomains;
    }

    /**
     * Sets the sub domains count. Each domain has n ( > = 0) subdomains.
     * 
     * @param countsub The countSubdomains to set.
     */
    public void setCountSubdomains(int countsub)
    {
        this.countSubdomains = countsub;
    }

    /**
     * Gets the count of
     * 
     * @return returns the countKnowledgeObjects.
     */
    public int getCountKnowledge()
    {
        return countKnowledgeObjects;
    }

    /**
     * Sets the count of the Knowledge Objects.
     * 
     * @param countKnowledgeObjects The countKnowledgeObjects to set.
     */
    public void setCountKnowledge(int countKnowledgeObjects)
    {
        this.countKnowledgeObjects = countKnowledgeObjects;
    }

    /**
     * Gets the sub domains.
     * 
     * @return KnowledgeDomain that are sub domains for this domain.
     */
    public KnowledgeDomain[] getSubdomains()
    {
        return subdomains;
    }

    /**
     * Sets the sub domains to this domain.
     * 
     * @param subdomains the sub domains.
     */
    public void setSubdomains(KnowledgeDomain[] subdomains)
    {
        this.subdomains = subdomains;
    }

    /**
     * Gets the Knowledge objects.
     * 
     * @return the knowledgeObjects objects.
     */
    public KnowledgeObject[] getKnowledgeObjects()
    {
        return knowledgeObjects;
    }

    /**
     * Sets the knowledgeObjects objects to this domain knowledgeObjects id.
     * 
     * @param knowledgeObjects the knowledgeObjects object list.
     */
    public void setKnowledgeObjects(KnowledgeObject[] knowledgeObjects)
    {
        this.knowledgeObjects = knowledgeObjects;
    }

    /**
     * Gets the number of sub domains for this domain.
     * 
     * @return number of sub domain
     */
    public int getHasSubdomains()
    {
        return hasSubdomains;
    }

    /**
     * Sets the number of the sub domains to this domain.
     * 
     * @param hasSubdomains the number of the sub domains.
     */
    public void setHasSubDomains(int hasSubdomains)
    {
        this.hasSubdomains = hasSubdomains;
    }

    /**
     * Description.
     * 
     * @return true if it is a direct sub domain
     */
    public boolean isIndirectDomain()
    {
        return isIndirectDomain;
    }

    /**
     * Description.
     * 
     * @param isIndirectDomain flag
     */
    public void setIndirectDomain(boolean isIndirectDomain)
    {
        this.isIndirectDomain = isIndirectDomain;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        KnowledgeDomain other = (KnowledgeDomain) o;
        int returnValue = getName().compareTo(other.getName());
        return returnValue;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getName();

    }
}
