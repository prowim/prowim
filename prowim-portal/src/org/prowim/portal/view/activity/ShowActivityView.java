/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 01.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.portal.view.activity;

import java.util.EnumSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;



/**
 * This view show at left site parameters to actually activity and at the right site the knowledge object to process and activity. <br>
 * User can see the input parameters and edit or update the outgoing parameter. <br>
 * At the right site user can see the knowledge objects, add new or edit existing.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ShowActivityView extends DefaultView
{
    private final Activity         acitvity;
    private final ActivityEditView actStartView;

    /**
     * Constructor.
     * 
     * @param currentAct current activity
     * @param activityEditView view to edit parameter of activity
     */
    public ShowActivityView(Activity currentAct, ActivityEditView activityEditView)
    {
        this.acitvity = currentAct;
        this.actStartView = activityEditView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createContent(Composite parent)
    {
        super.createContent(parent);

        actStartView.createPartControl(leftContainer);

        // Create model for domain
        DomainKnowledgeArray domKnowArray = MainController.getInstance().getBusinessDomains(this.acitvity.getProcessID());
        // Create process Model
        ProcessLeaf procModel = new ProcessLeaf(this.acitvity.getProcessID(), this.acitvity.getProcessName());
        // Create ACtivity model
        ActivityLeaf actModel = new ActivityLeaf(this.acitvity);

        // shows knowledge objects of activity
        ShowKnowObjView showWOBView = new ShowKnowObjView(this.acitvity, domKnowArray, procModel, actModel);

        Group rightGroup = new Group(rightContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        rightGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightGroup.setLayout(new GridLayout(1, false));

        rightGroup.setText(Resources.Frames.Activity.Texts.SELECTIVE_ACTIVITY.getText() + GlobalConstants.DOUBLE_POINT + " "
                + this.acitvity.getName());

        showWOBView.createPartControl(rightGroup);

        sashForm.setMaximizedControl(null);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.ACTIVITY);
    }

}
