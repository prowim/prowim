/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 04.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.model;

import org.eclipse.swt.graphics.Image;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * Model for an organization unit.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class OrganizationLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey = Resources.Frames.Global.Images.ORGANISATION_UNIT_IMAGE.getImage();

    private String      address;
    private String      telefon;
    private String      email;
    private String      description;

    /**
     * Constructor.
     * 
     * @param id ID of model.
     * @param name Name of model.
     */
    public OrganizationLeaf(String id, String name)
    {
        super(id, name);
        setImage(imageKey);
    }

    /**
     * Constructor.
     * 
     * @param organization Organization
     * 
     */
    public OrganizationLeaf(Organization organization)
    {
        super(organization.getID(), organization.getName());
        setImage(imageKey);
        setAddress(organization.getAddress());
        setTelefon(organization.getTelefon());
        setEmail(organization.getEmail());
        String description = organization.getDescription();
        if (description != null)
            setDescription(description);
        else
            setDescription("");

    }

    /**
     * setAddress
     * 
     * @param address the address to set
     */
    public void setAddress(String address)
    {
        this.address = address;
    }

    /**
     * getAddress
     * 
     * @return the address
     */
    public String getAddress()
    {
        return address;
    }

    /**
     * setTelefon
     * 
     * @param telefon the telefon to set
     */
    public void setTelefon(String telefon)
    {
        this.telefon = telefon;
    }

    /**
     * getTelefon
     * 
     * @return the telefon
     */
    public String getTelefon()
    {
        return telefon;
    }

    /**
     * setEmail
     * 
     * @param email the email to set
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     * getEmail
     * 
     * @return the email
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * setDescription
     * 
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * getDescription
     * 
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

}
