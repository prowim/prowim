/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:35 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/knowledge/ProcessKnowView.java $
 * $LastChangedRevision: 5031 $
 *------------------------------------------------------------------------------
 * (c) 23.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.knowledge;

import java.util.EnumSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.process.CommonTreeView;



/**
 * This view shows knowledges of processes and domains. View is extended from DefaultView. You see at one site all Domains and <br\>
 * included knowledgebases. In other site you can find all knowledgbases sorted to process and activity.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5031 $
 */
public class ProcessKnowView extends DefaultView
{
    /** ID of view */
    public static final String ID = ProcessKnowView.class.getName();

    private CommonTreeView     showProcessView;
    private CommonTreeView     domainKnowView;

    /**
     * Description.
     */
    public ProcessKnowView()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Set title of View
        this.setPartName(Resources.Frames.Navigation.Actions.KNOWLEDGE_PROCESS_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.KNOWLEDGE_PROCESS_NAV.getImage());

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        super.createContent(container);

        Group leftGroup = new Group(leftContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        leftGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        leftGroup.setLayout(new GridLayout(1, false));

        // Create domain group
        leftGroup.setText(Resources.Frames.Knowledge.Texts.PROCESS_KNOW.getText());

        KnowledgeStructureController wobController = new KnowledgeStructureController();
        this.showProcessView = new CommonTreeView(wobController.getProcessElementRootData(false));

        this.showProcessView.createPartControl(leftGroup);

        // Create process group
        Group rightGroup = new Group(rightContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        rightGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        rightGroup.setLayout(new GridLayout(1, false));

        rightGroup.setText(Resources.Frames.Knowledge.Texts.DOMAIN_KNOW.getText());

        this.domainKnowView = new CommonTreeView(wobController.getDomainRootData(false));
        this.domainKnowView.createPartControl(rightGroup);

        createContextMenu();

        // Resize the shashForm
        sashForm.setMaximizedControl(null);

        // reload tree
        showProcessView.reloadTree(new KnowledgeStructureController().getProcessElementRootData(true));

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createToolbar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createToolbar(Composite parent)
    {
        super.createToolbar(parent);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createActions()
     */
    @Override
    protected void createActions()
    {
        super.createActions();
    }

    /**
     * 
     * This method selects a given element (in this case a process) with given process template ID. <br>
     * For selecting other elements the method selectElement should re implemented.
     * 
     * @param filter Id of selected template
     */
    public void setItemSelection(String filter)
    {
        showProcessView.selectElement(filter);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK);
    }
}
