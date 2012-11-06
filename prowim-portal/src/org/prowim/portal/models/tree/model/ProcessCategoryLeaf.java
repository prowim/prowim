/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.Leaf;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * Includes {@link Process} and they are structured through their process categories. <br>
 * Categories can self have process categories. The leaves of this structure are elements of:
 * 
 * <li> {@link Activity}</li> <li> {@link Role}</li> <li> {@link Mean}</li> <li> {@link ResultsMemory}</li> <li> {@link KnowledgeObject}</li>
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0a9
 */
public class ProcessCategoryLeaf extends DefaultLeaf implements Leaf
{
    /** Image of model */
    private final Image imageKey = Resources.Frames.Process.Images.PROCESS_TYPE_IMAGE.getImage();

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param id ID of element
     * @param name Name of element
     */
    public ProcessCategoryLeaf(String id, String name)
    {
        super(id, name);
        setImage(imageKey);
    }

}
