/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-11 18:16:58 +0200 (Mo, 11 Okt 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/impl/DefaultLeaf.java $
 * $LastChangedRevision: 4915 $
 *------------------------------------------------------------------------------
 * (c) 03.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.graphics.Image;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.Leaf;



/**
 * This abstract class implemented the functions from Leaf.java interface.
 * 
 * Each Tree model must extends this class. So you can be sure, that each tree element have a id and a name.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4915 $
 */
public class DefaultLeaf implements Leaf
{
    // private variable DefaultLeafs
    private String       id;
    private String       name;
    private List<Object> children;

    /** This a default image. To set a new image for custom model use setImage() method. */
    private Image        image = Resources.Frames.Tree.Images.TREE_FOLDER.getImage();

    /**
     * This is the constructor of this class. This will be deliver of other classes, which are extended from this class.
     * 
     * @param id ID of element
     * @param name name of element
     */
    public DefaultLeaf(String id, String name)
    {
        setID(id);
        setName(name);
        setChildren(new ArrayList<Object>());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.Leaf#getID()
     */
    @Override
    public String getID()
    {
        return this.id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.Leaf#getName()
     */
    @Override
    public String getName()
    {
        return this.name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.Leaf#setID(java.lang.String)
     */
    @Override
    public void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.Leaf#setName(java.lang.String)
     */
    @Override
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return name;
    }

    /**
     * Set children of model
     * 
     * @param children the children to set
     */
    public void setChildren(List<Object> children)
    {
        this.children = children;
    }

    /**
     * Return all children of model
     * 
     * @return the children
     */
    public List<Object> getChildren()
    {
        return children;
    }

    /**
     * Add elements in model. Can be only Person
     * 
     * @param childElement Elements to add in model.
     * 
     */
    public void addChild(Object childElement)
    {
        Validate.notNull(childElement);
        this.children.add(childElement);
    }

    /**
     * Set image of model.
     * 
     * @param image the image to set
     */
    public void setImage(Image image)
    {
        this.image = image;
    }

    /**
     * Get the image of model.
     * 
     * @return the image
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * Checks if the node has children.
     * 
     * @return true if children exist.
     */
    public boolean hasChildren()
    {
        return !getChildren().isEmpty();
    }

    /**
     * 
     * Whether or not this leaf can be a parent for {@link KnowledgeObject}s
     * 
     * @return <code>true</code> if this leaf can contain {@link KnowledgeObject}s
     */
    public boolean canContainKnowledgeObjects()
    {
        return false;
    }

    /**
     * 
     * Whether or not this leaf can be a parent for {@link KnowledgeLink}s
     * 
     * @return <code>true</code> if this leaf can contain {@link KnowledgeLink}s
     */
    public boolean canContainKnowledgeLinks()
    {
        return false;
    }
}
