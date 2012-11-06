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
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.utils.GlobalConstants;



/**
 * Model to build a process element in structure tree. A process has following structure:<br>
 * <ul>
 * <li>elements as folder
 * <li>category of process
 * <li>{@link Process}
 * <li>{@link ActivityLeaf} or {@link RoleLeaf} and other models from same package
 * <li>{@link org.prowim.datamodel.prowim.Activity} or other entity class for the specific model
 * </ul>
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class ProcessStructureLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey       = Resources.Frames.Navigation.Actions.PROCESS_NAV.getImage();

    /** Image of process landscape */
    private final Image landscapeImage = Resources.Frames.Process.Images.PROCESS_LAND_SCAPE.getImage();

    /**
     * Constructor.
     * 
     * @param id ID of a process
     * @param name Name of a process
     */
    public ProcessStructureLeaf(String id, String name)
    {
        super(id, name);
        setImage(imageKey);
    }

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param process Actual process
     * 
     */
    public ProcessStructureLeaf(Process process)
    {
        super(process.getTemplateID(), process.getName());
        if (process.isProcessLandscape())
            setImage(landscapeImage);
        else
            setImage(imageKey);
    }

    /**
     * Default Constructor to create a empty model.
     * 
     */
    public ProcessStructureLeaf()
    {
        super(GlobalConstants.ROOT_MODEL, "");
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
