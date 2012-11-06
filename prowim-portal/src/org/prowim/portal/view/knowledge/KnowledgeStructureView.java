/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.knowledge;

import java.util.Collection;
import java.util.EnumSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.update.UpdateNotification;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.process.CommonTreeView;



/**
 * Shows the structure of knowledge. This is a implementation of old explorer.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class KnowledgeStructureView extends DefaultView
{
    /** ID of view */
    public static final String           ID = KnowledgeStructureView.class.getName();

    private KnowledgeStructureController knowledgeStructureController;

    private CommonTreeView               treeViewKnowledgeDomains;
    private CommonTreeView               treeViewProcessElements;
    private CommonTreeView               treeViewKnowledgeObjects;
    private CommonTreeView               treeViewOrganizationalElements;

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
        this.setPartName(Resources.Frames.Navigation.Actions.KNOWLEDGE_STRUCTURE_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.KNOWLEDGE_STRUCTURE_NAV.getImage());

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        Composite topComposite = new Composite(sashForm, SWT.NONE);
        topComposite.setLayout(new GridLayout(2, true));
        topComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        Composite lowerComposite = new Composite(sashForm, SWT.NONE);
        lowerComposite.setLayout(new GridLayout(2, true));
        lowerComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

        knowledgeStructureController = new KnowledgeStructureController();

        treeViewKnowledgeDomains = createKnowledgeDomainsTree(topComposite);
        treeViewProcessElements = createProcessElementsTree(topComposite);
        treeViewKnowledgeObjects = createKnowledgeObjectsTree(lowerComposite);
        treeViewOrganizationalElements = createOrganizationalElementsTree(lowerComposite);
    }

    /**
     * Create knowledge domain area.
     * 
     * @param container Composite to add the widget
     */
    private CommonTreeView createKnowledgeDomainsTree(Composite container)
    {
        Group group = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setLayout(new GridLayout());
        group.setText(Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());

        CommonTreeView showProcessView = new CommonTreeView(knowledgeStructureController.getDomainRootData(false));
        showProcessView.createPartControl(group);
        return showProcessView;
    }

    /**
     * Create knowledge object area.
     * 
     * @param container Composite to add the widget
     */
    private CommonTreeView createKnowledgeObjectsTree(Composite container)
    {
        Group group = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setLayout(new GridLayout());
        group.setText(Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText());

        CommonTreeView showProcessView = new CommonTreeView(knowledgeStructureController.getKnowledgeObjectRootData(false));
        showProcessView.createPartControl(group);
        return showProcessView;

    }

    /**
     * Create process element area.
     * 
     * @param container Composite to add the widget
     */
    private CommonTreeView createProcessElementsTree(Composite container)
    {
        Group group = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setLayout(new GridLayout());
        group.setText(Resources.Frames.Global.Texts.PROCESS_ELEMENTS.getText());

        CommonTreeView showProcessView = new CommonTreeView(knowledgeStructureController.getProcessElementRootData(false));

        showProcessView.createPartControl(group);
        return showProcessView;
    }

    /**
     * Create organization element area.
     * 
     * @param container Composite to add the widget
     */
    private CommonTreeView createOrganizationalElementsTree(Composite container)
    {
        Group group = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setLayout(new GridLayout());
        group.setText(Resources.Frames.Global.Texts.ORGANISATION_ELEMENTS.getText());

        CommonTreeView showProcessView = new CommonTreeView(knowledgeStructureController.getOrganiazationElementRootData(false));
        showProcessView.createPartControl(group);
        return showProcessView;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.KNOWLEDGE, EntityType.KNOWLEDGEDOMAIN, EntityType.KNOWLEDGELINK, EntityType.ORGANIZATION, EntityType.PROCESS,
                          EntityType.PERSON, EntityType.ROLE);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#handleUpdateEvents(UpdateNotificationCollection)
     */
    @Override
    public void handleUpdateEvents(UpdateNotificationCollection updates)
    {
        super.handleUpdateEvents(updates);
        UpdateNotification element = updates.element();
        for (UpdateNotification updateNotification : updates)
        {
            Collection<String> entityIds = updateNotification.getEntityIds();
            EntityType entityType = updateNotification.getEntityType();

            // Collection<String> entityIds = element.getEntityIds();

            for (String id : entityIds)
            {
                if (entityType.equals(EntityType.KNOWLEDGE) || entityType.equals(EntityType.KNOWLEDGELINK))
                    treeViewKnowledgeObjects.getTree().reloadTreeNode(id);
                if (entityType.equals(EntityType.PROCESS))
                    treeViewProcessElements.getTree().reloadTreeNode(id);
                if (entityType.equals(EntityType.KNOWLEDGEDOMAIN))
                    treeViewKnowledgeDomains.getTree().reloadTreeNode(id);
                if (entityType.equals(EntityType.ORGANIZATION) || entityType.equals(EntityType.PERSON) || entityType.equals(EntityType.ROLE))
                    treeViewOrganizationalElements.getTree().reloadTreeNode(id);
            }
        }
    }

}
