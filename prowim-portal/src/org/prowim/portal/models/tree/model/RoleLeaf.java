/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.utils.GlobalConstants;



/**
 * Model for Roles. It has following structure: <br>
 * 
 * <ul>
 * <li>{@link PersonLeaf}
 * </ul>
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class RoleLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image defRoleImg   = Resources.Frames.Role.Images.ROLE_IMAGE.getImage();
    private final Image startRoleImg = Resources.Frames.Role.Images.START_ROLE_IMAGE.getImage();

    private boolean     isStartRole;

    /**
     * Constructor. Each knowledge domain model should has an unique ID and a name
     * 
     * @param id ID of element
     * @param name Name of element
     */
    public RoleLeaf(String id, String name)
    {
        super(id, name);
        setImage(defRoleImg);
    }

    /**
     * Default Constructor to create a empty model.
     * 
     * @param role Role
     * 
     */
    public RoleLeaf(Role role)
    {
        super(role.getID(), role.getName());
        setStartRole(role.isStartRole());
        if (role.isStartRole())
            setImage(startRoleImg);
        else
            setImage(defRoleImg);
    }

    /**
     * Default Constructor to create a empty model.
     * 
     */
    public RoleLeaf()
    {
        super(GlobalConstants.ROOT_MODEL, Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());
        setImage(defRoleImg);
    }

    /**
     * setStartRole
     * 
     * @param isStartRole the isStartRole to set
     */
    public void setStartRole(boolean isStartRole)
    {
        this.isStartRole = isStartRole;
    }

    /**
     * isStartRole
     * 
     * @return the isStartRole
     */
    public boolean isStartRole()
    {
        return isStartRole;
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
