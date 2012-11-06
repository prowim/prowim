/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

package org.prowim.datamodel.prowim;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.prowim.datamodel.collections.PersonArray;



/**
 * This is a data-object-class represents KnowledgeObject.
 * 
 * @author Saad Wardi
 * @version $Revision: 2415 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class KnowledgeObject extends ProcessElement implements Comparable<KnowledgeObject>
{

    /** the knowledgeinfo list. */
    private List<KnowledgeLink> knowledgeLinks     = new ArrayList<KnowledgeLink>();
    /** the owner. */
    private String              owner;
    /** the dominator persons IDs. */
    private PersonArray         responsiblePersons = new PersonArray();

    /**
     * Creates a KnowledgeObject with id, name and createTime.
     * 
     * @param id {@link KnowledgeObject#setID(String)}
     * @param name {@link KnowledgeObject#setName(String)}
     * @param createTime {@link KnowledgeObject#setCreateTime(String)}
     */
    protected KnowledgeObject(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE Webservices specification.
     */
    protected KnowledgeObject()
    {
    }

    /**
     * Gets the knowledgeLinks.
     * 
     * @return not null {@link List} of {@link KnowledgeLink}
     * 
     * 
     */
    public List<KnowledgeLink> getKnowledgeLinks()
    {
        return this.knowledgeLinks;
    }

    /**
     * Sets the list of {@link KnowledgeLink} objects.
     * 
     * @param knowledgeLinks the not null {@link KnowledgeLink} list.
     */
    public void setKnowledgeLinks(List<KnowledgeLink> knowledgeLinks)
    {
        this.knowledgeLinks = knowledgeLinks;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getOwner()
    {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setOwner(String value)
    {
        this.owner = value;
    }

    /**
     * {@link KnowledgeObject#setResponsiblePersons(PersonArray)}
     * 
     * @return the dominators not null.
     */
    public PersonArray getResponsiblePersons()
    {
        return responsiblePersons;
    }

    /**
     * Sets the persons IDs that dominates this knowledgeobject.
     * 
     * @param dominators not null dominators to set.
     */
    public void setResponsiblePersons(PersonArray dominators)
    {
        this.responsiblePersons = dominators;
    }

    /**
     * Gets the first defined name.
     * 
     * @return the name.
     */
    public String getParentName()
    {
        return super.getName();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(KnowledgeObject other)
    {
        if (other != null)
        {
            // Sorting for German alphabet
            Collator collator = Collator.getInstance(Locale.GERMAN);

            // a == A, a < ä
            collator.setStrength(Collator.SECONDARY);
            return collator.compare(getName(), other.getName());
        }
        throw new NullPointerException("Cannot compare to null object");
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
