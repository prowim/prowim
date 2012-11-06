/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-04 10:47:32 +0200 (Mo, 04 Okt 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/model/ProcessTypeLeaf.java $
 * $LastChangedRevision: 4855 $
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
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * This is a model for ProcessTypeModel. Each ProcessTypeModel can have {@link Process} and {@link ProcessType} as its children.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4855 $
 */
public class ProcessTypeLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey = Resources.Frames.Process.Images.PROCESS_TYPE_IMAGE.getImage();

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param id ID of element
     * @param name Name of element
     */
    public ProcessTypeLeaf(String id, String name)
    {
        super(id, name);
        setImage(imageKey);
    }
}
