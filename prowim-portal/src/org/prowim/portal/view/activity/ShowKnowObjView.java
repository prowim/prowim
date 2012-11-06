/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Iterator;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.prowim.datamodel.collections.DomainKnowledgeArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.impl.ProcessFilter;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.DomainLeaf;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.process.CommonTreeView;



/**
 * Shows WOB to a element. This included WOBs of domain, WOBs of process and of activity self.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ShowKnowObjView extends DefaultView
{
    /** Id of View */
    public static final String         ID = ShowKnowObjView.class.getName();
    private final DomainKnowledgeArray domainArray;
    private final ProcessLeaf          processModel;
    private final ActivityLeaf         activityModel;
    private final Activity             activity;

    /**
     * Constructor.
     * 
     * @param currentAct Current activity, for which these WOBs shows
     * @param domainArray Model for domains of this activity
     * @param processModel Model for process of this activity
     * @param activityModel Model for activity self
     */
    public ShowKnowObjView(Activity currentAct, DomainKnowledgeArray domainArray, ProcessLeaf processModel, ActivityLeaf activityModel)
    {
        this.activity = currentAct;
        this.domainArray = domainArray;
        this.processModel = processModel;
        this.activityModel = activityModel;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {

        createActions();

        // Create domain area if exist
        // if (this.domainModel != null)
        createDomainArea(parent);

        // if (this.processModel != null)
        createProcessArea(parent);

        // if (this.activityModel != null)
        createActivityArea(parent);

    }

    /**
     * 
     * Create a tree for domain knowledges.
     * 
     * @param parent Composite
     */
    private void createDomainArea(Composite parent)
    {
        // Group
        Group domainGroup = new Group(parent, SWT.SHADOW_IN);
        domainGroup.setText(Resources.Frames.Knowledge.Texts.AREA_OF_KNOWLEDGE.getText());
        domainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        domainGroup.setLayout(new GridLayout(1, false));
        domainGroup.pack();

        DefaultLeaf root = new DefaultLeaf("", "");
        // Tree to show data
        Iterator<KnowledgeDomain> domainknowIt = this.domainArray.iterator();
        while (domainknowIt.hasNext())
        {
            KnowledgeDomain domainKnow = domainknowIt.next();
            DomainLeaf domainModel = new DomainLeaf(domainKnow.getID(), domainKnow.getName());
            root.addChild(new KnowledgeStructureController().getSubNode(domainModel));
        }

        CommonTreeView showProcessView = new CommonTreeView(root);
        showProcessView.createPartControl(domainGroup);
    }

    /**
     * 
     * Create tree for process and its WOBs.
     * 
     * @param parent Composite
     */
    private void createProcessArea(Composite parent)
    {
        Group processGroup = new Group(parent, SWT.SHADOW_IN);
        processGroup.setText(Resources.Frames.Knowledge.Texts.KNOW_FOR_PROCESS.getText() + GlobalConstants.DOUBLE_POINT + " "
                + this.activity.getProcessName());
        processGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        processGroup.setLayout(new GridLayout(1, false));
        processGroup.pack();

        DefaultLeaf root = new DefaultLeaf("", "");
        root.addChild(new KnowledgeStructureController().getSubNode(this.processModel));
        CommonTreeView showProcessView = new CommonTreeView(root);
        showProcessView.createPartControl(processGroup);
        TreeViewer tree = showProcessView.getTree();
        ProcessFilter filters = new ProcessFilter();
        filters.setFilter(this.activity.getProcessName());
        tree.addFilter(filters);
    }

    /**
     * 
     * Create Tree for activity and its WOBs.
     * 
     * @param parent Composite
     */
    private void createActivityArea(Composite parent)
    {
        Group activityGroup = new Group(parent, SWT.SHADOW_IN);
        activityGroup.setText(Resources.Frames.Knowledge.Texts.KNOW_FOR_ACTIVITY.getText() + GlobalConstants.DOUBLE_POINT + " "
                + this.activity.getName());
        activityGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        activityGroup.setLayout(new GridLayout(1, false));
        activityGroup.pack();

        DefaultLeaf root = new DefaultLeaf("", "");
        root.addChild(new KnowledgeStructureController().getSubNode(this.activityModel));
        CommonTreeView showProcessView = new CommonTreeView(root);
        showProcessView.createPartControl(activityGroup);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.KNOWLEDGE);
    }

}
