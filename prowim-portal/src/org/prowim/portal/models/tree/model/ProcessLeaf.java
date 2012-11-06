/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-04-14 13:29:20 +0200 (Do, 14 Apr 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/model/ProcessLeaf.java $
 * $LastChangedRevision: 5075 $
 *------------------------------------------------------------------------------
 * (c) 03.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.model;

import org.eclipse.swt.graphics.Image;
import org.prowim.datamodel.prowim.Process;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * This is a model for ProcessModel. Each ProcessModel can have {@link KnowledgeObjectLeaf} as well <br>
 * as {@link ActivityLeaf} as children.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5075 $
 */
public class ProcessLeaf extends DefaultLeaf
{
    /** Image of model */
    private final Image imageKey       = Resources.Frames.Navigation.Actions.PROCESS_NAV.getImage();

    /** Image of process landscape */
    private final Image landscapeImage = Resources.Frames.Process.Images.PROCESS_LAND_SCAPE.getImage();

    private Process     actProcess;

    // *************************************Methods****************************************************************

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param process Actual process
     * 
     */
    public ProcessLeaf(Process process)
    {
        super(process.getTemplateID(), process.getName());
        if (process.isProcessLandscape())
            setImage(landscapeImage);
        else
            setImage(imageKey);

        setProcess(process);
    }

    /**
     * Constructor. Each activity model should has an unique ID and a name
     * 
     * @param getTemplateID ID of tamplate
     * @param name name of tempalte
     */
    public ProcessLeaf(String getTemplateID, String name)
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
