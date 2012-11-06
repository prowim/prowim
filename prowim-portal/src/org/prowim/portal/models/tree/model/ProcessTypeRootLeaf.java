/*==============================================================================
 * File $Id: ProcessTypeRootLeaf.java 4855 2010-10-04 08:47:32Z leusmann $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-04 10:47:32 +0200 (Mo, 04 Okt 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/model/ProcessTypeRootLeaf.java $
 * $LastChangedRevision: 4855 $
 *------------------------------------------------------------------------------
 * (c) 05.05.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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



/**
 * Title and description.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 4855 $
 * @since !!VERSION!!
 */

public class ProcessTypeRootLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey = Resources.Frames.Navigation.Actions.PROCESS_NAV.getImage();

    private Process     actProcess;

    // *************************************Methods****************************************************************

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param process Actual process
     * 
     */
    public ProcessTypeRootLeaf(Process process)
    {
        super(process.getTemplateID(), process.getName());
        setImage(imageKey);
        setProcess(process);
    }

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param getTemplateID ID of template
     * @param name name of template
     */
    public ProcessTypeRootLeaf(String getTemplateID, String name)
    {
        super(getTemplateID, name);
        setImage(imageKey);
        setProcess(null);
    }

    /**
     * 
     * Set actual process
     * 
     * @param process Process
     */
    public void setProcess(Process process)
    {
        this.actProcess = process;
    }

    /**
     * 
     * Return the actual process of this model. When you get this, than you can extend all other informations about this process.
     * 
     * @return Process
     * @see org.prowim.datamodel.prowim.Process
     */
    public Process getProcess()
    {
        return this.actProcess;
    }
}
