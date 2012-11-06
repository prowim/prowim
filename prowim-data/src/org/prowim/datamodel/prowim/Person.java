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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents Person.
 * 
 * @author Saad Wardi
 * @version $Revision: 4338 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "person", propOrder = { "id", "firstName", "lastName", "userName", "telefon", "emailAddress", "description", "address",
        "organisation", "password", "title", "isAdmin", "isModeler", "isReader" })
public class Person implements Comparable<Object>
{
    /** the id of the person is the unique key generated from protege. */
    private String  id;
    /** the first name. */
    private String  firstName;
    /** the last name. */
    private String  lastName;
    /** the user name. */
    private String  userName;
    /** the telefon. */
    private String  telefon;
    /** the address. */
    private String  address;
    /** the email address. */
    private String  emailAddress;
    /** the description. */
    private String  description;
    /** the organisation. */
    @XmlAttribute
    private String  organisation;
    /** the password. */
    private String  password;
    /** the title of the person. */
    private String  title;
    /** the isadmin attribute. */
    private boolean isAdmin   = false;
    /** the ismodeler attribute. */
    private boolean isModeler = false;
    /** the isReader attribute. */
    private boolean isReader  = false;

    /**
     * Constructor.
     * 
     * @param userName the shortname
     * @param firstName the firstName
     * @param lastName the lastName
     * @param id the id of the person generated.
     * @param password the not null password.
     */
    protected Person(String id, String firstName, String lastName, String userName, String password)
    {
        setID(id);
        setFirstName(firstName);
        setLastName(lastName);
        setUserName(userName);
        setPassword(password);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Person()
    {

    }

    /**
     * Gets the value of the id property.
     * 
     * @return possible object is {@link String}. {@link Person#setID(String)}
     * 
     */
    public String getID()
    {
        return id;
    }

    /**
     * Sets the value of the id property. A person id is the primary key of the Person in the ontology.
     * 
     * @param value allowed object is not null {@link String}
     * 
     */
    private void setID(String value)
    {
        Validate.notNull(value);
        this.id = value;
    }

    /**
     * Gets the value of the emailAddress property.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getEmailAddress()
    {
        return emailAddress;
    }

    /**
     * Sets the value of the emailAddress property.
     * 
     * @param emailAddress allowed object is {@link String}
     * 
     */
    public void setEmailAddress(String emailAddress)
    {
        // Validate.notNull(emailAddress);
        this.emailAddress = emailAddress;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getFirstName()
    {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value allowed object is {@link String}. Not null is not allowed.
     * 
     */
    public void setFirstName(String value)
    {
        Validate.notEmpty(value);
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return possible object is {@link String}.
     * 
     */
    public String getLastName()
    {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value allowed object is {@link String}
     * 
     */
    public void setLastName(String value)
    {
        Validate.notNull(value);
        this.lastName = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value allowed object is {@link String}
     * 
     */
    public void setUserName(String value)
    {
        Validate.notNull(value);
        this.userName = value;
    }

    /**
     * Gets the value of the telefon property.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getTelefon()
    {
        return telefon;
    }

    /**
     * Sets the value of the telefon property.
     * 
     * @param value allowed object is {@link String}
     * 
     */
    public void setTelefon(String value)
    {
        this.telefon = value;
    }

    /**
     * Gets the description attribute
     * 
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description attribute
     * 
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Gets the address attribute
     * 
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * Sets the address attribute
     * 
     * @param address the not null address to set.
     */
    public void setAddress(String address)
    {
        // Validate.notNull(address);
        this.address = address;
    }

    /**
     * Gets the organisation attribute.
     * 
     * @return the organisation
     */
    public String getOrganisation()
    {
        return organisation;
    }

    /**
     * Sets the organisation Attribute.
     * 
     * @param organisation the not null organisation ID to set.
     */
    public void setOrganisation(String organisation)
    {
        // Validate.notNull(organisation);
        this.organisation = organisation;
    }

    /**
     * {@link Person#setPassword(String)}
     * 
     * @return the password not null password.
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets the user password.
     * 
     * @param password the password to set
     */
    public void setPassword(String password)
    {
        Validate.notNull(password);
        this.password = password;
    }

    /**
     * See {@link Person#setTitle(String)}
     * 
     * @return not null
     * @see Person#setTitle(String)
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * Sets the value of the title property. The title is the title of the person.
     * 
     * @param title allowed object is {@link String}.
     * 
     */
    public void setTitle(String title)
    {
        this.title = title;
    }

    /**
     * Sets the user as admin
     * 
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    /**
     * Checks if a user is admin.
     * 
     * @return the isAdmin
     */
    public boolean isAdmin()
    {
        return isAdmin;
    }

    /**
     * Sets the user as admin
     * 
     * @param isModler the isAdmin to set
     */
    public void setModeler(boolean isModler)
    {
        this.isModeler = isModler;
    }

    /**
     * Checks if a user is admin.
     * 
     * @return the isAdmin
     */
    public boolean isModeler()
    {
        return isModeler;
    }

    /**
     * Sets the user as reader
     * 
     * @param isReader the isReader to set
     */
    public void setReader(boolean isReader)
    {
        this.isReader = isReader;
    }

    /**
     * Checks if a user is reader.
     * 
     * @return the isAdmin
     */
    public boolean isReader()
    {
        return isReader && !isAdmin && !isModeler;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {

        Person other = (Person) o;
        int returnValue = 0;
        if ( !other.lastName.equals(""))
        {
            returnValue = lastName.compareTo(other.lastName);
            if (returnValue == 0)
            {
                returnValue = firstName.compareTo(other.firstName);
                if (returnValue == 0)
                {
                    returnValue = userName.compareTo(other.userName);
                }
            }
        }
        else
        {
            returnValue = userName.compareTo(other.userName);
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
        String result = "";
        String theTitle = title;
        String firstname = firstName;
        String lastname = lastName;
        if (theTitle == null)
        {
            theTitle = "";
        }
        else if (firstname == null)
        {
            firstname = "";
        }
        else if (lastname == null)
        {
            lastname = "";
        }
        result = firstname + " " + lastname;
        // theTitle + " " +
        return result;
    }
}
