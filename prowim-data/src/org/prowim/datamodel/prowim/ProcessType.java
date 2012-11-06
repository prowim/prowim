/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 07.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents process type.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessType", propOrder = { "id", "name", "description" })
public class ProcessType implements Comparable<Object>
{
    private String id;
    private String name;
    private String description;

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected ProcessType()
    {
    }

    /**
     * Description.
     * 
     * @param id {@link ProcessType#setID(String)}
     * @param name {@link ProcessType#setName(String)}
     */
    protected ProcessType(String id, String name)
    {
        setID(id);
        setName(name);
    }

    /**
     * Set ID
     * 
     * @param id the id to process type
     */
    public void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * get ID
     * 
     * @return the id
     */
    public String getID()
    {
        return id;
    }

    /**
     * Set name
     * 
     * @param name the name to process type
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * Get name
     * 
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set description
     * 
     * @param description the description to process type
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Get description
     * 
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Object o)
    {
        if (o != null)
        {
            ProcessType other = (ProcessType) o;
            if (other != null && other.name != null)
            {
                int returnValue = name.compareTo(other.name);
                return returnValue;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }

}
