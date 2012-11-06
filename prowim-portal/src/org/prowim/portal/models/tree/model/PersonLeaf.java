/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 11.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;
import org.eclipse.swt.graphics.Image;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * Model to shows a person in tree.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class PersonLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image  imageKey = Resources.Frames.Tree.Images.USER_IMAGE.getImage();

    /** the first name. */
    private final Person person;

    /**
     * Create model with a given {@link Person}.
     * 
     * @param person {@link Person}
     * 
     */
    public PersonLeaf(final Person person)
    {
        super(person.getID(), person.toString());
        setImage(imageKey);

        Validate.notNull(person);
        this.person = person;
    }

    /**
     * getFirstName
     * 
     * @return the firstName
     */
    public String getFirstName()
    {
        return person.getFirstName();
    }

    /**
     * getLastName
     * 
     * @return the lastName
     */
    public String getLastName()
    {
        return person.getLastName();
    }

    /**
     * getShortName
     * 
     * @return the shortName
     */
    public String getShortName()
    {
        return person.getUserName();
    }

    /**
     * getTelefon
     * 
     * @return the telefon
     */
    public String getTelefon()
    {
        return person.getTelefon();
    }

    /**
     * getAddress
     * 
     * @return the address
     */
    public String getAddress()
    {
        return person.getAddress();
    }

    /**
     * getEmailAddress
     * 
     * @return the emailAddress
     */
    public String getEmailAddress()
    {
        return person.getEmailAddress();
    }

    /**
     * getDescription
     * 
     * @return the description
     */
    public String getDescription()
    {
        return person.getDescription();
    }

    /**
     * getOrganisation
     * 
     * @return the organisation
     */
    public String getOrganisation()
    {
        return person.getOrganisation();
    }

    /**
     * getTitle
     * 
     * @return the title
     */
    public String getTitle()
    {
        return person.getTitle();
    }

    /**
     * getPassword.
     * 
     * @return the password of the current person.
     */
    public String getPassword()
    {
        return person.getPassword();
    }

    /**
     * setDescription.
     * 
     * @param description the description
     */
    public void setDescription(String description)
    {
        this.person.setDescription(description);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.impl.DefaultLeaf#canContainKnowledgeObjects()
     */
    @Override
    public boolean canContainKnowledgeObjects()
    {
        return true;
    }

}
