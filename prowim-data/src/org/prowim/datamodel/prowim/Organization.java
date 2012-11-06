/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-22 17:47:16 +0100 (Di, 22 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/Organization.java $
 * $LastChangedRevision: 3045 $
 *------------------------------------------------------------------------------
 * (c) 02.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * This is a data-object-class represents organization.
 * 
 * @author Saad Wardi
 * @version $Revision: 3045 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "organization", propOrder = { "address", "telefon", "email" })
public class Organization extends ProcessElement implements Comparable<Object>
{

    @XmlAttribute
    private String address;
    @XmlAttribute
    private String telefon;
    @XmlAttribute
    private String email;

    /**
     * Creates an Organization Object with id, name and createTime.
     * 
     * @param id this{@link #setID(String)}
     * @param name this{@link #setName(String)}
     * @param createTime this{@link #setCreateTime(String)}
     */
    protected Organization(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Organization()
    {
    }

    /**
     * this{@link #setAddress(String)}
     * 
     * @return the organisation address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Sets the organisation address
     * 
     * @param address the organisation address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * this{@link #setTelefon(String)}
     * 
     * @return the telefon the organisation telefon.
     */
    public String getTelefon()
    {
        return telefon;
    }

    /**
     * Sets the organisation telefon.
     * 
     * @param telefon the organisation telefon to set
     */
    public void setTelefon(String telefon)
    {
        this.telefon = telefon;
    }

    /**
     * this{@link #setEmail(String)}
     * 
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * Sets the organisation email address.
     * 
     * @param email the organisation email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
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
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        int returnValue = -1;
        if (o != null)
        {
            Organization other = (Organization) o;
            returnValue = getID().compareTo(other.getID());
        }
        return returnValue;
    }

}
