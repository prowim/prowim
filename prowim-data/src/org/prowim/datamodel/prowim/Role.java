/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/Role.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 16.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.algernon.ProcessEngineConstants;



/**
 * This is a data-object-class represents role.
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Role")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "Role", propOrder = { "referenceID", "type", "personsList", "isStartRole" })
public class Role extends ProcessElement implements Comparable<Object>
{
    private String       referenceID;
    private String       type;
    @XmlElement(nillable = false)
    private List<Person> personsList;
    private boolean      isStartRole = false;

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Role()
    {

    }

    /**
     * Gets the role.
     * 
     * @param id not null ID.
     * @param name not null name.
     * @param createTime not null createTime.
     */
    protected Role(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * Gets the referenceID attribute
     * 
     * @return the referenceID this can a process id or an activity id.
     */
    public String getReferenceID()
    {
        return referenceID;
    }

    /**
     * Sets the processID attribute
     * 
     * @param referenceID not null
     */
    public void setReferenceID(String referenceID)
    {
        this.referenceID = referenceID;
    }

    /**
     * Gets the persons list attribute {@link Role#setPersonsList(List)}
     * 
     * @return a not null persons list. At the client side must be checked for not null.
     */
    public List<Person> getPersonsList()
    {
        return personsList;
    }

    /**
     * Sets the personID attribute
     * 
     * @param personslist the person list to set
     */
    public void setPersonsList(List<Person> personslist)
    {
        Validate.notNull(personslist);
        this.personsList = personslist;
    }

    /**
     * Gets the type attribute
     * 
     * @return the type
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the type attribute
     * 
     * @param type the type to set
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * Checks if the role is a start role.
     * 
     * @return the isStartRole
     */
    public boolean isStartRole()
    {
        return isStartRole;
    }

    /**
     * Sets the isStartRole flag.
     * 
     * @param isStartRole the isStartRole to set
     */
    public void setStartRole(boolean isStartRole)
    {
        this.isStartRole = isStartRole;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {

        Role other = (Role) o;
        int returnValue = 0;
        if (o != null && other != null)
        {
            // Sorting for German alphabet
            Collator collator = Collator.getInstance(Locale.GERMAN);

            // a == A, a < ä
            collator.setStrength(Collator.SECONDARY);
            returnValue = collator.compare(getName(), other.getName());
        }
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
        return getName();
    }

    /**
     * 
     * Show the list of persons which assigns for this role
     * 
     * @return String
     */
    public String showPersonList()
    {
        if (personsList != null && !personsList.isEmpty())
        {
            if (personsList.get(0) != null)
            {
                final String etc = ",...";
                if (personsList.size() > 1)
                {
                    return personsList.get(0).toString() + etc;
                }
                else
                {
                    return personsList.get(0).toString();
                }
            }
            else
            {
                return ProcessEngineConstants.Variables.Common.EMPTY;
            }
        }
        else
        {
            return ProcessEngineConstants.Variables.Common.EMPTY;
        }

    }
}
