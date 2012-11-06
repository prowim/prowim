/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-11 18:16:58 +0200 (Mo, 11 Okt 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/model/DomainLeaf.java $
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
package org.prowim.portal.models.tree.model;

import org.eclipse.swt.graphics.Image;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.utils.GlobalConstants;



/**
 * This is a model for knowledge domains. Each knowledge domain can have {@link KnowledgeObjectLeaf} <br>
 * as well as {@link DomainLeaf} as its children.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4915 $
 */
public class DomainLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey = Resources.Frames.Knowledge.Images.DOMAIN.getImage();

    /**
     * Constructor. Each knowledge domain model should has an unique ID and a name
     * 
     * @param id ID of element
     * @param name Name of element
     */
    public DomainLeaf(String id, String name)
    {
        super(id, name);
        setImage(imageKey);
    }

    /**
     * Default Constructor to create a empty model.
     * 
     */
    public DomainLeaf()
    {
        super(GlobalConstants.ROOT_MODEL, Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());
        setImage(imageKey);
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

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.models.tree.impl.DefaultLeaf#canContainKnowledgeLinks()
     */
    @Override
    public boolean canContainKnowledgeLinks()
    {
        return true;
    }
}
