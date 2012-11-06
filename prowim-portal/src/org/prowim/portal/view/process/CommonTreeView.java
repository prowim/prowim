/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 19.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.LoggedInUserInfo;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.PersonController;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.dialogs.ActivtiyDetailsDialog;
import org.prowim.portal.dialogs.KnowledgeLinkDetailsDialog;
import org.prowim.portal.dialogs.KnowledgeObjectDialog;
import org.prowim.portal.dialogs.OrganizationDialog;
import org.prowim.portal.dialogs.PersonDialog;
import org.prowim.portal.dialogs.PickElementDialog;
import org.prowim.portal.dialogs.RenameDialog;
import org.prowim.portal.dialogs.SelectPersonDialog;
import org.prowim.portal.dialogs.feedback.ConfirmDialog;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.dialogs.knowledge.KnowledgeLinkTemp;
import org.prowim.portal.export.ExportDMSDocuments;
import org.prowim.portal.export.ExportKnowledgeDomains;
import org.prowim.portal.export.ExportKnowledgeObjects;
import org.prowim.portal.export.ExportProcessElements;
import org.prowim.portal.export.ExportProcesses;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.dnd.DefaultLeafTransfer;
import org.prowim.portal.models.tree.impl.DefTreeViewer;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.impl.ElementSorter;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.DomainFolder;
import org.prowim.portal.models.tree.model.DomainLeaf;
import org.prowim.portal.models.tree.model.FolderLeaf;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectsFolder;
import org.prowim.portal.models.tree.model.MeanLeaf;
import org.prowim.portal.models.tree.model.OrganizationElementsFolder;
import org.prowim.portal.models.tree.model.OrganizationLeaf;
import org.prowim.portal.models.tree.model.OrganizationPersonLeaf;
import org.prowim.portal.models.tree.model.OrganizationUnitsLeaf;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.models.tree.model.ProcessCategoryLeaf;
import org.prowim.portal.models.tree.model.ProcessElementsFolder;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ResultsMemoryLeaf;
import org.prowim.portal.models.tree.model.RoleLeaf;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.knowledge.open.CallMailClient;
import org.prowim.portal.view.knowledge.open.OpenKnowledgeLink;
import org.prowim.rap.framework.components.impl.DefaultConstraint;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * <p>
 * This is a {@link ViewPart} which shows a {@link DefTreeViewer} containing different types of nodes.
 * </p>
 * 
 * <p>
 * See {@link DefaultLeaf} and inherited leaf types for more information.
 * </p>
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class CommonTreeView extends DefaultView
{
    /** ID of view */
    public static final String           ID               = CommonTreeView.class.getName();
    private final static Logger          LOG              = Logger.getLogger(CommonTreeView.class);

    private final List<Class>            leafsToLoad      = new ArrayList<Class>();
    private final List<Class>            notDragableLeafs = new ArrayList<Class>();

    private DefTreeViewer                treeViewer;
    private KnowledgeStructureController knowledgeStructureController;

    private Action                       actionExpandTree;
    private Action                       actionReduceTree;
    private Action                       actionAddKnowledgeObject;
    private Action                       actionDeleteKnowledgeObject;
    private Action                       actionEditKnowledgeObject;
    private Action                       actionShowKnowledgeLink;
    private Action                       actionDeleteKnowledgeLink;
    private Action                       actionDeleteProcess;
    private Action                       actionAddOrganization;
    private Action                       actionEditOrganization;
    private Action                       actionAddEditPerson;
    private Action                       actionAddEditDomain;
    private Action                       actionAddPerson;
    private Action                       actionEditItemDescription;
    private Action                       actionDeletePerson;
    private Action                       actionDeleteKnowledgeDomain;
    private Action                       actionKnowledgeLinkDetails;
    private Action                       actionRemoveKnowledgeObject;
    private Action                       actionAssignRoleToOrganization;
    private Action                       actionRemoveRoleFromOrganization;
    private Action                       actionDeleteOrganization;
    private Action                       actionMoveOrganization;
    private Action                       actionAddKnowledgeObjectToElement;
    private Action                       actionAddPersonToOrganization;
    private Action                       actionCreateRole;
    private Action                       actionCreateMean;
    private Action                       actionCreateResultsMemory;
    private Action                       actionEditNameAndDescription;
    private Action                       actionExportKnowledgeObject;
    private Action                       actionExportDocument;
    private Action                       actionExportKnowledgeDomain;
    private Action                       actionExportProcessElements;
    private Action                       actionExportProcesses;
    private Action                       actionShowRelations;
    private Action                       actionShowRelationsOfKnowledgeObject;
    private Action                       actionDeleteProcessElement;
    private Action                       actionFeedbackMail;

    private final DefaultLeaf            initialTreeData;

    /**
     * Constructor.
     * 
     * @param treeDataObj data to filling tree
     */
    public CommonTreeView(DefaultLeaf treeDataObj)
    {
        this.initialTreeData = treeDataObj;

        setLeafToLoad();
        setNotDragableLeafs();
    }

    /**
     * Description.
     */
    private void setLeafToLoad()
    {
        leafsToLoad.add(OrganizationPersonLeaf.class);
        leafsToLoad.add(OrganizationUnitsLeaf.class);
        leafsToLoad.add(DomainFolder.class);
        leafsToLoad.add(ProcessCategoryLeaf.class);
        leafsToLoad.add(KnowledgeObjectsFolder.class);
    }

    /**
     * Description.
     */

    private void setNotDragableLeafs()
    {
        notDragableLeafs.add(FolderLeaf.class);
        notDragableLeafs.add(OrganizationPersonLeaf.class);
        notDragableLeafs.add(ProcessCategoryLeaf.class);
        notDragableLeafs.add(PersonLeaf.class);
        notDragableLeafs.add(KnowledgeObjectLeaf.class);
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
        treeViewer = new DefTreeViewer(parent, SWT.MULTI);
        knowledgeStructureController = new KnowledgeStructureController();
        init(treeViewer, this.initialTreeData);
        treeViewer.expandToLevel(1);
        treeViewer.setSorter(new ElementSorter());
        // Selection listener
        this.treeViewer.addSelectionChangedListener(createSelectionChangedListener());
        this.treeViewer.addDoubleClickListener(createDoubleClickListener());
        this.treeViewer.addOpenListener(createOpenListener());

        // Set DND support
        addDragSupport(treeViewer);
        addDropSupport(treeViewer);
    }

    private IOpenListener createOpenListener()
    {
        return new IOpenListener()
        {

            @Override
            public void open(OpenEvent event)
            {
                // TODO $Author$ Auto-generated method stub
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    DefaultLeaf currentSelectedLeaf = (DefaultLeaf) ((IStructuredSelection) s).getFirstElement();
                    if (currentSelectedLeaf instanceof KnowledgeLinkLeaf)
                    {
                        actionShowKnowledgeLink.runWithEvent(new Event());
                    }
                    else if (currentSelectedLeaf instanceof KnowledgeObjectLeaf)
                    {
                        KnowledgeObjectLeaf knowledgeObjectModel = (KnowledgeObjectLeaf) currentSelectedLeaf;
                        KnowledgeObject theKnowledgeObject = knowledgeObjectModel.getKnowledgeObject();
                        if (theKnowledgeObject.getKnowledgeLinks().size() == 1)
                        {
                            KnowledgeLinkLeaf knowLinkModel = new KnowledgeLinkLeaf(theKnowledgeObject.getKnowledgeLinks().get(0));
                            OpenKnowledgeLink.open(knowLinkModel);
                        }
                    }
                }

            }
        };
    }

    /**
     * addDoubleClickListener.
     */
    private IDoubleClickListener createDoubleClickListener()
    {
        return new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    DefaultLeaf currentSelectedLeaf = (DefaultLeaf) ((IStructuredSelection) s).getFirstElement();
                    if (currentSelectedLeaf instanceof KnowledgeLinkLeaf)
                    {
                        actionShowKnowledgeLink.runWithEvent(new Event());
                    }
                    else if (currentSelectedLeaf instanceof KnowledgeObjectLeaf)
                    {
                        KnowledgeObjectLeaf knowledgeObjectModel = (KnowledgeObjectLeaf) currentSelectedLeaf;
                        KnowledgeObject theKnowledgeObject = knowledgeObjectModel.getKnowledgeObject();
                        if (theKnowledgeObject.getKnowledgeLinks().size() == 1)
                        {
                            KnowledgeLinkLeaf knowLinkModel = new KnowledgeLinkLeaf(theKnowledgeObject.getKnowledgeLinks().get(0));
                            OpenKnowledgeLink.open(knowLinkModel);
                        }
                    }
                }
            }
        };
    }

    /**
     * addSelectionChangedListener.
     */
    private ISelectionChangedListener createSelectionChangedListener()
    {
        return new ISelectionChangedListener()
        {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                // flag to load data again or not
                boolean loadData = true;

                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    DefaultLeaf currentSelectedLeaf = (DefaultLeaf) ((IStructuredSelection) s).getFirstElement();
                    // Set all actions enabling false
                    setActionEnabled(false);

                    // Set action to each leaf
                    enableActionsForLeaf();

                    if (currentSelectedLeaf instanceof PersonLeaf)
                    {
                        PersonLeaf person = (PersonLeaf) currentSelectedLeaf;
                        createContextMenu();
                        Person user = LoggedInUserInfo.getInstance().getPerson();
                        enableEditPerson(MainController.getInstance().canPersonModifyEntity(person.getID(), user.getID()));
                        loadData = false;
                    }
                    else if (currentSelectedLeaf instanceof RoleLeaf)
                    {
                        loadData = false;
                    }
                    else if (currentSelectedLeaf instanceof ProcessElementsFolder || currentSelectedLeaf instanceof KnowledgeLinkLeaf
                            || currentSelectedLeaf instanceof OrganizationElementsFolder)
                    {
                        createContextMenu();
                        loadData = false;
                    }
                    else if ((currentSelectedLeaf != null) && (leafsToLoad.contains(currentSelectedLeaf.getClass())))
                    {
                        if ((currentSelectedLeaf.getChildren().size() == 0)
                                || (currentSelectedLeaf.getChildren().size() > 1)
                                || (currentSelectedLeaf.getChildren().size() == 1 && !((DefaultLeaf) currentSelectedLeaf.getChildren().get(0))
                                        .getID().equals(GlobalConstants.DUMMY_ID)))
                            loadData = false;
                    }

                    // Load data new if necessary
                    if (loadData && (currentSelectedLeaf instanceof DefaultLeaf))
                    {
                        if (leafsToLoad.contains(currentSelectedLeaf.getClass()))
                        {
                            // Get tree items and refresh it
                            treeViewer.refresh(CommonTreeView.this.knowledgeStructureController.getSubNode(currentSelectedLeaf));
                        }

                        createContextMenu();
                    }

                    setEnableChangeKnowledges(false);
                    actionExpandTree.setEnabled(treeViewer.isExpandable(currentSelectedLeaf));
                }
            }
        };
    }

    /**
     * 
     * Get current selected element
     * 
     * @return {@link DefaultLeaf}
     */
    public DefaultLeaf getCurrentSelection()
    {
        return (DefaultLeaf) ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
    }

    // Set action to each leaf
    private void enableActionsForLeaf()
    {
        final DefaultLeaf currentSelection = getCurrentSelection();
        if (currentSelection instanceof ProcessLeaf || currentSelection instanceof ProcessStructureLeaf)
        {
            actionAddKnowledgeObject.setEnabled(true);
            actionDeleteProcess.setEnabled(true);
        }
        else if (currentSelection instanceof DomainLeaf)
        {
            actionAddKnowledgeObject.setEnabled(true);
            actionAddEditDomain.setEnabled(true);
            actionDeleteKnowledgeDomain.setEnabled(true);
        }
        else if (currentSelection instanceof DomainFolder)
        {
            actionAddEditDomain.setEnabled(true);
        }
        else if (currentSelection instanceof ActivityLeaf)
        {
            actionAddKnowledgeObject.setEnabled(true);
        }
        else if (currentSelection instanceof KnowledgeObjectLeaf)
        {
            actionEditKnowledgeObject.setEnabled(true);
            actionDeleteKnowledgeObject.setEnabled(true);
            actionAddKnowledgeObjectToElement.setEnabled(true);
            actionFeedbackMail.setEnabled(true);
        }
        else if (currentSelection instanceof KnowledgeLinkLeaf)
        {
            actionShowKnowledgeLink.setEnabled(true);
            actionDeleteKnowledgeLink.setEnabled(true);
            actionEditKnowledgeObject.setEnabled(true);
        }
        else if (currentSelection instanceof OrganizationPersonLeaf)
        {
            actionAddEditPerson.setEnabled(true);
        }
        else if (currentSelection instanceof OrganizationUnitsLeaf)
        {
            actionAddOrganization.setEnabled(true);
        }
        else if (currentSelection instanceof KnowledgeObjectsFolder || currentSelection instanceof ResultsMemoryLeaf
                || currentSelection instanceof MeanLeaf)
        {
            actionAddKnowledgeObject.setEnabled(true);
        }
        else if (currentSelection instanceof OrganizationLeaf)
        {
            actionAddOrganization.setEnabled(true);
            actionEditOrganization.setEnabled(true);
        }
        else if (currentSelection instanceof PersonLeaf)
        {
            actionAddPersonToOrganization.setEnabled(true);
            actionAddEditPerson.setEnabled(true);
            actionDeletePerson.setEnabled(true);
        }
        else if (currentSelection instanceof RoleLeaf)
        {
            actionAddPerson.setEnabled(true);
        }
        else
        {
            setActionEnabled(false);
        }
    }

    /**
     * 
     * Set enabled property for all actions
     * 
     * @param flag boolean.
     */
    private void setActionEnabled(boolean flag)
    {
        this.actionAddKnowledgeObject.setEnabled(flag);
        this.actionEditKnowledgeObject.setEnabled(flag);
        this.actionDeleteKnowledgeObject.setEnabled(flag);
        this.actionDeleteKnowledgeLink.setEnabled(flag);
        this.actionShowKnowledgeLink.setEnabled(flag);
        this.actionDeleteProcess.setEnabled(flag);
        this.actionAddOrganization.setEnabled(flag);
        this.actionEditOrganization.setEnabled(flag);
        this.actionAddEditPerson.setEnabled(flag);
        this.actionDeletePerson.setEnabled(flag);
        this.actionAddEditDomain.setEnabled(flag);
        this.actionAddPerson.setEnabled(flag);
        this.actionDeleteKnowledgeDomain.setEnabled(flag);
        this.actionFeedbackMail.setEnabled(flag);
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

        // Expand tree node
        this.actionExpandTree = createActionExpandTree();

        // Reduce tree node
        this.actionReduceTree = createActionReduceTree();

        // Add new knowledge object
        this.actionAddKnowledgeObject = createActionAddKnowledgeObject();

        // Delete a knowledge object
        this.actionDeleteKnowledgeObject = createActionDeleteKnowledgeObject();

        // Edit a knowledge object
        this.actionEditKnowledgeObject = createActionEditKnowledgeObject();

        // Show selected knowledge link
        this.actionShowKnowledgeLink = createActionShowKnowledgeLink();

        this.actionDeleteKnowledgeLink = createActionDeleteKnowledgeLink();

        this.actionDeleteProcess = createActionDeleteProcess();

        this.actionAddOrganization = createActionAddOrganization();

        this.actionEditOrganization = createActionEditOrganization();

        this.actionAddEditPerson = createActionAddEditPerson();

        this.actionDeletePerson = createActionDeletePerson();

        this.actionAddEditDomain = createActionAddEditDomain();

        // Action to add or change a user
        this.actionAddPerson = createActionAddPerson();

        this.actionEditItemDescription = createActionEditItemDescription();

        this.actionDeleteKnowledgeDomain = createActionDeleteKnowledgeDomain();

        actionKnowledgeLinkDetails = createActionKnowledgeLinkDetails();

        actionRemoveKnowledgeObject = createActionRemoveKnowledgeObject();

        actionAssignRoleToOrganization = createActionAssignRoleToOrganization();

        actionRemoveRoleFromOrganization = createActionRemoveRoleFromOrganization();

        actionDeleteOrganization = createActionDeleteOrganization();

        actionMoveOrganization = createActionMoveOrganization();

        this.actionAddKnowledgeObjectToElement = createActionAddKnowledgeObjectToElement();

        this.actionAddPersonToOrganization = createActionAddPersonToOrganization();

        // Action to create a role in view
        this.actionCreateRole = createActionCreateRole();

        this.actionCreateMean = createActionCreateMean();

        this.actionCreateResultsMemory = createActionCreateResultsMemory();

        actionEditNameAndDescription = createActionEditNameAndDescription();

        actionExportKnowledgeObject = createActionExportKnowledgeObjects();

        actionExportKnowledgeDomain = createActionExportKnowledgeDomains();

        actionExportProcessElements = createActionExportProcessesElements();
        actionExportProcesses = createActionExportProcesses();

        this.actionExportDocument = createActionExportDocuments();

        this.actionShowRelations = createActionShowRelations();

        this.actionShowRelationsOfKnowledgeObject = createActionShowRelationsOfKnowledgeObject();
        this.actionDeleteProcessElement = createActionDeleteProecessElement();
        this.actionFeedbackMail = createActionFeedbackMail();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContextMenu()
     */
    @Override
    protected void createContextMenu()
    {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager mgr)
            {
                fillContextMenu(mgr);
            }
        });

        /** only for admin user. */
        final boolean currentUserAdmin = LoggedInUserInfo.getInstance().isCurrentUserAdmin();
        this.actionDeleteProcess.setEnabled(currentUserAdmin);
        this.actionDeletePerson.setEnabled(currentUserAdmin);
        this.actionAddEditPerson.setEnabled(currentUserAdmin);
        this.actionDeleteOrganization.setEnabled(currentUserAdmin);
        this.actionAddPerson.setEnabled(currentUserAdmin);
        this.actionAddOrganization.setEnabled(currentUserAdmin);
        this.actionEditOrganization.setEnabled(currentUserAdmin);
        this.actionMoveOrganization.setEnabled(currentUserAdmin);
        this.actionAddPersonToOrganization.setEnabled(currentUserAdmin);
        this.actionCreateRole.setEnabled(currentUserAdmin);
        this.actionCreateMean.setEnabled(currentUserAdmin);
        this.actionCreateResultsMemory.setEnabled(currentUserAdmin);

        Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
        treeViewer.getControl().setMenu(menu);
    }

    private void enableEditPerson(boolean flag)
    {
        this.actionAddEditPerson.setEnabled(flag || LoggedInUserInfo.getInstance().isCurrentUserAdmin());
    }

    /**
     * Disable all actions that change entities for the user reader.
     * 
     * @param flag false.
     */
    private void setEnableChangeKnowledges(boolean flag)
    {
        if (LoggedInUserInfo.getInstance().isCurrentUserReader())
        {
            this.actionAddEditDomain.setEnabled(flag);
            this.actionAddEditPerson.setEnabled(flag);
            this.actionAddKnowledgeObject.setEnabled(flag);
            this.actionAddOrganization.setEnabled(flag);
            this.actionAddPerson.setEnabled(flag);
            this.actionDeleteKnowledgeDomain.setEnabled(flag);
            this.actionDeleteKnowledgeLink.setEnabled(flag);
            this.actionDeleteKnowledgeObject.setEnabled(flag);
            this.actionDeletePerson.setEnabled(flag);
            this.actionEditKnowledgeObject.setEnabled(flag);
            this.actionEditOrganization.setEnabled(flag);
            this.actionRemoveKnowledgeObject.setEnabled(flag);
            this.actionAssignRoleToOrganization.setEnabled(flag);
            this.actionDeleteOrganization.setEnabled(flag);
            this.actionRemoveRoleFromOrganization.setEnabled(flag);
            this.actionCreateRole.setEnabled(flag);
            this.actionCreateMean.setEnabled(flag);
            this.actionCreateResultsMemory.setEnabled(flag);
            this.actionAddKnowledgeObjectToElement.setEnabled(flag);
            this.actionExportKnowledgeObject.setEnabled(flag);
            this.actionExportKnowledgeDomain.setEnabled(flag);
            this.actionExportProcessElements.setEnabled(flag);
            this.actionExportProcesses.setEnabled(flag);
            this.actionExportDocument.setEnabled(flag);
            this.actionShowRelations.setEnabled(flag);
            this.actionShowRelationsOfKnowledgeObject.setEnabled(flag);
            this.actionDeleteProcessElement.setEnabled(flag);
            this.actionFeedbackMail.setEnabled(flag);
        }
    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param mgr Included all menu items in this view
     */
    public void fillContextMenu(IMenuManager mgr)
    {
        final DefaultLeaf currentSelection = getCurrentSelection();
        if (currentSelection != null)
        {
            if ( !treeViewer.getExpandedState(currentSelection))
                mgr.add(this.actionExpandTree);
            else
                mgr.add(this.actionReduceTree);

            mgr.add(new Separator());

            if (currentSelection instanceof ProcessLeaf)
            {
                mgr.add(this.actionAddKnowledgeObject);
            }
            else if (currentSelection instanceof KnowledgeObjectsFolder)
            {
                mgr.add(this.actionAddKnowledgeObject);
                mgr.add(this.actionExportKnowledgeObject);
                mgr.add(this.actionExportDocument);
            }
            else if (currentSelection instanceof ProcessStructureLeaf)
            {
                mgr.add(this.actionAddKnowledgeObject);
                mgr.add(this.actionEditNameAndDescription);
                mgr.add(this.actionExportProcesses);
            }
            else if (currentSelection instanceof DomainLeaf)
            {
                mgr.add(this.actionAddEditDomain);
                mgr.add(this.actionEditNameAndDescription);
                mgr.add(this.actionAddKnowledgeObject);
                mgr.add(this.actionDeleteKnowledgeDomain);
            }
            else if (currentSelection instanceof DomainFolder)
            {
                mgr.add(this.actionAddEditDomain);
                mgr.add(this.actionExportKnowledgeDomain);
            }
            else if (currentSelection instanceof ActivityLeaf)
            {
                mgr.add(this.actionAddKnowledgeObject);
                mgr.add(this.actionEditNameAndDescription);
            }
            else if (currentSelection instanceof KnowledgeObjectLeaf)
            {
                setContextMenuForKnowledge(mgr);
            }
            else if (currentSelection instanceof KnowledgeLinkLeaf)
            {
                mgr.add(this.actionShowKnowledgeLink);
                mgr.add(this.actionEditKnowledgeObject);
                mgr.add(this.actionEditNameAndDescription);
                mgr.add(this.actionDeleteKnowledgeLink);
                mgr.add(this.actionKnowledgeLinkDetails);
            }
            else if (currentSelection instanceof OrganizationUnitsLeaf)
            {
                mgr.add(this.actionAddOrganization);
            }
            else if (currentSelection instanceof OrganizationLeaf)
            {
                setContextMenuForOrganization(mgr);
            }
            else if (currentSelection instanceof OrganizationPersonLeaf)
            {
                mgr.add(this.actionAddEditPerson);
            }
            else if (currentSelection instanceof RoleLeaf)
            {
                setContextMenuForRole(mgr);
            }
            else if (currentSelection instanceof PersonLeaf)
            {
                mgr.add(this.actionAddEditPerson);
                mgr.add(this.actionEditNameAndDescription);
                mgr.add(this.actionDeletePerson);
                mgr.add(this.actionAddPersonToOrganization);
            }
            else if (currentSelection instanceof ResultsMemoryLeaf || currentSelection instanceof MeanLeaf)
            {
                mgr.add(this.actionAddKnowledgeObject);
                mgr.add(this.actionEditNameAndDescription);
                mgr.add(this.actionShowRelations);
                mgr.add(this.actionDeleteProcessElement);
            }
            else if (currentSelection instanceof ProcessElementsFolder)
            {
                setContextMenuForProcessElements(mgr, currentSelection);
            }
            else if (currentSelection instanceof ProcessCategoryLeaf)
            {
                mgr.add(this.actionExportProcesses);
            }
            mgr.add(new Separator());
            mgr.add(this.actionEditItemDescription);
        }
    }

    /**
     * Description.
     * 
     * @param mgr
     * @param currentSelection
     */
    private void setContextMenuForProcessElements(IMenuManager mgr, final DefaultLeaf currentSelection)
    {
        if (((ProcessElementsFolder) currentSelection).getID().equals(GlobalConstants.ROLE))
            mgr.add(this.actionCreateRole);
        else if (((ProcessElementsFolder) currentSelection).getID().equals(GlobalConstants.MEAN))
            mgr.add(this.actionCreateMean);
        else if (((ProcessElementsFolder) currentSelection).getID().equals(GlobalConstants.RESULTS_MEMORY))
            mgr.add(this.actionCreateResultsMemory);

        // Add export action
        mgr.add(this.actionExportProcessElements);
        mgr.add(this.actionExportProcesses);
    }

    /**
     * Sets contenxt menu for {@link Organization}.
     * 
     * @param mgr
     */
    private void setContextMenuForOrganization(IMenuManager mgr)
    {
        mgr.add(this.actionAddOrganization);
        mgr.add(this.actionEditOrganization);
        mgr.add(this.actionEditNameAndDescription);
        mgr.add(this.actionDeleteOrganization);
        mgr.add(this.actionMoveOrganization);
    }

    private void setContextMenuForRole(IMenuManager mgr)
    {
        mgr.add(this.actionAddPerson);
        mgr.add(actionAssignRoleToOrganization);
        TreePath[] path = treeViewer.getExpandedTreePaths();
        if (path.length >= 2)
        {
            DefaultLeaf leaf = (DefaultLeaf) path[path.length - 2].getLastSegment();
            if (leaf instanceof OrganizationLeaf)
            {
                mgr.add(actionRemoveRoleFromOrganization);
                actionRemoveRoleFromOrganization.setEnabled( !LoggedInUserInfo.getInstance().isCurrentUserReader());
            }
        }
        mgr.add(this.actionEditNameAndDescription);
        mgr.add(this.actionShowRelations);
        mgr.add(this.actionDeleteProcessElement);
    }

    // setContextMenuForKnowledge
    private void setContextMenuForKnowledge(IMenuManager mgr)
    {
        mgr.add(this.actionEditKnowledgeObject);
        mgr.add(this.actionEditNameAndDescription);
        mgr.add(this.actionAddKnowledgeObjectToElement);
        mgr.add(this.actionDeleteKnowledgeObject);
        TreePath[] path = treeViewer.getExpandedTreePaths();
        if (path.length >= 2)
        {
            DefaultLeaf leaf = (DefaultLeaf) path[path.length - 1].getLastSegment();
            if ( !(leaf instanceof KnowledgeObjectLeaf))
                mgr.add(this.actionRemoveKnowledgeObject);

            actionRemoveKnowledgeObject.setEnabled(LoggedInUserInfo.getInstance().isCurrentUserAdmin());
        }

        mgr.add(this.actionShowRelationsOfKnowledgeObject);
        mgr.add(this.actionFeedbackMail);
    }

    /**
     * 
     * Fill trees with data.
     * 
     * @param treeViewer Tree, which should fill.
     * @param model data, which should added to tree.
     */
    public void init(final DefTreeViewer treeViewer, final DefaultLeaf model)
    {
        treeViewer.setInputAndExpand(model, true);
    }

    /**
     * 
     * Return the complete tree.
     * 
     * @return TreeViewer
     */
    public DefTreeViewer getTree()
    {
        return this.treeViewer;
    }

    /**
     * 
     * This method select and expand the three for given process element.
     * 
     * @param element ID of process template
     * 
     */
    public void selectElement(String element)
    {
        // Get treeItems
        TreeItem[] items = this.treeViewer.getTree().getItems();

        // Close first all collapsed tree items
        for (int i = 0; i < items.length; i++)
        {
            TreeItem[] inerItem = items[i].getItems();
            for (int j = 0; j < inerItem.length; j++)
            {
                this.treeViewer.collapseToLevel(inerItem[j].getData(), 1);
            }
        }

        // Expand and select the selected item
        for (int i = 0; i < items.length; i++)
        {
            TreeItem[] inerItem = items[i].getItems();
            for (int j = 0; j < inerItem.length; j++)
            {
                if (inerItem[j].getData() instanceof ProcessLeaf)
                {
                    ProcessLeaf procModel = (ProcessLeaf) inerItem[j].getData();
                    if (procModel.getID().equals(element))
                    {
                        this.treeViewer.setSelection(new StructuredSelection(inerItem[j].getData()), true);
                        this.treeViewer.expandToLevel(inerItem[j].getData(), 2);
                    }
                }
            }
        }
    }

    /**
     * 
     * Reload tree with new data from DB.
     * 
     * @param root This is the new root build in ProcessWOBController(). Is dependent from model, which should load. Can not be null.
     */
    public void reloadTree(DefaultLeaf root)
    {
        Validate.notNull(root);
        init(this.treeViewer, root);
        this.treeViewer.setSorter(new ElementSorter());
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExportDocuments()
    {
        return Resources.Frames.Global.Actions.DOCUMENTS_EXCEL_EXPORT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                new ExportDMSDocuments().exportToExcel();
            }
        });
    }

    /**
     * Action to show the relations of a element. For example it show in which processes a role is used.
     * 
     * @return {@link Action}
     */
    private Action createActionShowRelations()
    {
        return Resources.Frames.Processelement.Actions.SHOW_RELATIONS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getCurrentSelection().getID();
                List<String> parentProcess = MainController.getInstance().getParentProcess(elementID);
                showRelations(parentProcess, Resources.Frames.Dialog.Texts.SHOW_RELATIONS_DLG_DESCRIPTION.getText());
            }

        });
    }

    /**
     * Shows the given list in a dialog.
     * 
     * @param parentProcess
     * @param message Message to show in dialog
     */
    private void showRelations(List<String> parentProcess, String message)
    {
        String plainTextContent = "";
        for (String processID : parentProcess)
        {
            plainTextContent = plainTextContent + GlobalConstants.MINUS + GlobalConstants.SPACE1 + GlobalConstants.PROCESS
                    + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1 + MainController.getInstance().getName(processID);
            plainTextContent = plainTextContent + GlobalConstants.LINE_BREAK;
        }

        ActivtiyDetailsDialog activtiyDetailsDialog = new ActivtiyDetailsDialog(null, actionShowRelations, message, plainTextContent);
        activtiyDetailsDialog.open();
    }

    /**
     * Action to show relations of a knowledge object.
     * 
     * @return
     */
    private Action createActionShowRelationsOfKnowledgeObject()
    {
        return Resources.Frames.Processelement.Actions.SHOW_RELATIONS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getCurrentSelection().getID();
                ObjectArray processElements = MainController.getInstance().getRelationsOfKnowledgeObject(elementID);
                String plainTextContent = "";
                for (Object object : processElements)
                {
                    ProcessElement element = (ProcessElement) object;
                    if ( !element.getClassName().equals(GlobalConstants.KNOWLEDGE_DOMAIN) && !element.getClassName().equals(GlobalConstants.PROCESS))
                    {
                        List<String> parentProcess = MainController.getInstance().getParentProcess(element.getID());
                        String parents = GlobalConstants.PROCESS + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1;
                        for (String string : parentProcess)
                        {
                            parents = parents + MainController.getInstance().getName(string);
                        }
                        plainTextContent = plainTextContent + GlobalConstants.MINUS + GlobalConstants.SPACE1 + element.getClassName()
                                + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1 + ((ProcessElement) object).getName()
                                + GlobalConstants.SPACE1 + Resources.Frames.Global.Texts.IN.getText() + GlobalConstants.SPACE1 + parents;
                    }
                    else
                    {
                        plainTextContent = plainTextContent + GlobalConstants.MINUS + GlobalConstants.SPACE1 + element.getClassName()
                                + GlobalConstants.DOUBLE_POINT + GlobalConstants.SPACE1 + ((ProcessElement) object).getName();

                    }
                    plainTextContent = plainTextContent + GlobalConstants.LINE_BREAK;

                }

                ActivtiyDetailsDialog activtiyDetailsDialog = new ActivtiyDetailsDialog(null, actionShowRelationsOfKnowledgeObject,
                                                                                        Resources.Frames.Dialog.Texts.SHOW_RELATIONS_DLG_DESCRIPTION
                                                                                                .getText(), plainTextContent);
                activtiyDetailsDialog.open();
            }
        });
    }

    /**
     * Deletes process element like {@link Role}, {@link Mean} or {@link ResultsMemory} if they are not included in a {@link Process}.
     * 
     * @return
     */
    private Action createActionDeleteProecessElement()
    {
        return Resources.Frames.Global.Actions.ITEM_DELETE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getCurrentSelection().getID();
                List<String> parentProcess = MainController.getInstance().getParentProcess(elementID);

                if (parentProcess.isEmpty())
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteProcessElement.getToolTipText()))
                        MainController.getInstance().deleteInstance(elementID);
                }
                else
                {
                    showRelations(parentProcess, Resources.Frames.Dialog.Texts.SHOW_DELETE_ELEMENT_DLG_DESCRIPTION.getText());
                }
            }
        });
    }

    /**
     * Action to call a mail client to give a feedback to a knowledge object.
     * 
     * @return Action
     */
    private Action createActionFeedbackMail()
    {
        return Resources.Frames.Global.Actions.FEEADBACK_MAIL.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                DefaultLeaf currentLeaf = getCurrentSelection();
                PersonArray personArray = MainController.getInstance().getPersonsOfRightRoles(GlobalConstants.Ontology.KNOWLEDGE_MANAGER);
                String mailTo = "";
                for (Person person : personArray)
                {
                    if (mailTo.equals(""))
                        mailTo = person.getEmailAddress();
                    else
                        mailTo = mailTo + GlobalConstants.COMMA + person.getEmailAddress();
                }
                CallMailClient.open(mailTo, "", "", Resources.Frames.Global.Texts.FEEDBACK_TO.getText() + " " + EscapeFunctions.toJSString("\"")
                        + currentLeaf.getName() + EscapeFunctions.toJSString("\""));
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExportProcessesElements()
    {
        return Resources.Frames.Global.Actions.PROCESSES_EXCEL_EXPORT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                new ExportProcessElements().exportToExcel();
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExportProcesses()
    {
        return Resources.Frames.Global.Actions.PROCESSES_EXCEL_EXPORT_COMPLETE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                new ExportProcesses().exportToExcel(getCurrentSelection());
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExportKnowledgeDomains()
    {
        return Resources.Frames.Global.Actions.KNOWLEDGE_DOMAIN_EXCEL_EXPORT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                new ExportKnowledgeDomains().exportToExcel();
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExportKnowledgeObjects()
    {
        return Resources.Frames.Global.Actions.KNOWLEDGE_OBJECT_EXCEL_EXPORT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                new ExportKnowledgeObjects().exportToExcel();
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionEditNameAndDescription()
    {
        return Resources.Frames.Toolbar.Actions.EDIT_NAME_DESCRIPTION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getCurrentSelection().getID();
                if (elementID != null)
                {
                    RenameDialog renameDialog = new RenameDialog(null, actionEditNameAndDescription, "", MainController.getInstance()
                            .getName(elementID), MainController.getInstance().getDescription(elementID));
                    if (renameDialog.open() == IDialogConstants.OK_ID)
                    {
                        MainController.getInstance().rename(elementID, renameDialog.getName());
                        MainController.getInstance().setDescription(elementID, renameDialog.getDescritption());
                    }
                }
                else
                {
                    throw new IllegalArgumentException("The ID of the selected element is null");
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionCreateResultsMemory()
    {
        return Resources.Frames.Toolbar.Actions.CREATE_NEW_RESULTS_MEM.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof ProcessElementsFolder)
                {
                    DefaultLeaf tmpLeaf = getCurrentSelection();

                    int i = 1;
                    TreePath[] path = treeViewer.getExpandedTreePaths();
                    while ( !(tmpLeaf instanceof ProcessStructureLeaf) && (path.length >= i))
                    {
                        tmpLeaf = (DefaultLeaf) path[path.length - i].getLastSegment();
                        i++;
                    }
                    RenameDialog renameDialog = new RenameDialog(null, actionCreateResultsMemory, "", "", "");
                    if (renameDialog.open() == IDialogConstants.OK_ID)
                    {
                        if (tmpLeaf instanceof ProcessStructureLeaf)
                        {
                            String objectID = MainController.getInstance().createObject(tmpLeaf.getID(), GlobalConstants.Ontology.RESULTS_MEMORY);
                            MainController.getInstance().rename(objectID, renameDialog.getName());
                            MainController.getInstance().setDescription(objectID, renameDialog.getDescritption());
                        }
                        else
                        {
                            MainController.getInstance().createProcessElement(GlobalConstants.Ontology.RESULTS_MEMORY, renameDialog.getName(), true,
                                                                              renameDialog.getDescritption());

                        }
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionCreateMean()
    {
        return Resources.Frames.Toolbar.Actions.CREATE_NEW_MEAN.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof ProcessElementsFolder)
                {
                    DefaultLeaf tmpLeaf = getCurrentSelection();
                    int i = 1;
                    TreePath[] path = treeViewer.getExpandedTreePaths();
                    while ( !(tmpLeaf instanceof ProcessStructureLeaf) && (path.length >= i))
                    {
                        tmpLeaf = (DefaultLeaf) path[path.length - i].getLastSegment();
                        i++;
                    }

                    RenameDialog renameDialog = new RenameDialog(null, actionCreateMean, "", "", "");
                    if (renameDialog.open() == IDialogConstants.OK_ID)
                    {
                        if (tmpLeaf instanceof ProcessStructureLeaf)
                        {
                            String objectID = MainController.getInstance().createObject(tmpLeaf.getID(), GlobalConstants.Ontology.MEAN);
                            MainController.getInstance().rename(objectID, renameDialog.getName());
                            MainController.getInstance().setDescription(objectID, renameDialog.getDescritption());
                        }
                        else
                        {
                            MainController.getInstance().createProcessElement(GlobalConstants.Ontology.MEAN, renameDialog.getName(), true,
                                                                              renameDialog.getDescritption());
                        }
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionCreateRole()
    {
        return Resources.Frames.Toolbar.Actions.CREATE_NEW_ROLE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof ProcessElementsFolder)
                {
                    DefaultLeaf tmpLeaf = getCurrentSelection();
                    int i = 1;
                    TreePath[] path = treeViewer.getExpandedTreePaths();
                    while ( !(tmpLeaf instanceof ProcessStructureLeaf) && (path.length >= i))
                    {
                        tmpLeaf = (DefaultLeaf) path[path.length - i].getLastSegment();
                        i++;
                    }

                    RenameDialog renameDialog = new RenameDialog(null, actionCreateRole, "", "", "");
                    if (renameDialog.open() == IDialogConstants.OK_ID)
                    {

                        if (tmpLeaf instanceof ProcessStructureLeaf)
                        {
                            String objectID = MainController.getInstance().createObject(tmpLeaf.getID(), GlobalConstants.Ontology.ROLE);
                            MainController.getInstance().rename(objectID, renameDialog.getName());
                            MainController.getInstance().setDescription(objectID, renameDialog.getDescritption());
                        }
                        else
                        {
                            MainController.getInstance().createProcessElement(GlobalConstants.Ontology.ROLE, renameDialog.getName(), true,
                                                                              renameDialog.getDescritption());
                        }
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddPersonToOrganization()
    {
        return Resources.Frames.Dialog.Actions.ADD_PERSON_TO_ORGANIZATION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                DefaultLeaf currentLeaf = getCurrentSelection();

                if (currentLeaf instanceof PersonLeaf)
                {
                    PickElementDialog pickElement = new PickElementDialog(null, actionAddPersonToOrganization, "", getCurrentSelection());

                    if (pickElement.open() == IDialogConstants.OK_ID)
                    {
                        DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();

                        // Set the person in relation to selected organization
                        MainController.getInstance().setPersonAsMember(currentLeaf.getID(), selectedLeaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddKnowledgeObjectToElement()
    {
        return Resources.Frames.Dialog.Actions.ADD_KNOWLEDGEOBJECT_TO_ITEM.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                DefaultLeaf currentLeaf = getCurrentSelection();

                if (currentLeaf instanceof KnowledgeObjectLeaf)
                {
                    PickElementDialog pickElement = new PickElementDialog(null, actionAddKnowledgeObjectToElement, "", currentLeaf);

                    if (pickElement.open() == IDialogConstants.OK_ID)
                    {
                        DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();

                        // Set the current knowledge object in relation to selected leaf
                        MainController.getInstance().addKnowledgeObject(currentLeaf.getID(), selectedLeaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Move the current organization in selected organization.
     * 
     * @return
     */
    private Action createActionMoveOrganization()
    {
        return Resources.Frames.Dialog.Actions.MOVE_ORGANIZATION.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                DefaultLeaf currentLeaf = getCurrentSelection();

                if (currentLeaf instanceof OrganizationLeaf)
                {
                    PickElementDialog pickElement = new PickElementDialog(null, actionMoveOrganization, "", currentLeaf);

                    if (pickElement.open() == IDialogConstants.OK_ID)
                    {
                        DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();
                        // Move the current organization in selected organization
                        MainController.getInstance().moveOrganization(currentLeaf.getID(), selectedLeaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeleteOrganization()
    {
        return Resources.Frames.Dialog.Actions.DELETE_ORGANIZATION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof OrganizationLeaf)
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteOrganization.getToolTipText()))
                    {
                        OrganizationLeaf organizationModel = (OrganizationLeaf) getCurrentSelection();
                        LOG.debug("DELETE ORGANIZATION:  " + organizationModel.getID());
                        MainController.getInstance().deleteOrganization(organizationModel.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionRemoveRoleFromOrganization()
    {
        return Resources.Frames.Dialog.Actions.REMOVE_ROLE_FROM_ORGANIZATION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof RoleLeaf)
                {
                    RoleLeaf roleModel = (RoleLeaf) getCurrentSelection();
                    final ObjectArray rolesIDs = new ObjectArray();
                    rolesIDs.add(roleModel.getID());
                    TreePath[] path = treeViewer.getExpandedTreePaths();
                    if (path.length >= 2)
                    {
                        DefaultLeaf leaf = (DefaultLeaf) path[path.length - 2].getLastSegment();
                        actionRemoveRoleFromOrganization.setEnabled(true);
                        MainController.getInstance().removeRolesFromOrganization(rolesIDs, leaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAssignRoleToOrganization()
    {
        return Resources.Frames.Dialog.Actions.ADD_ROLE_TO_ORGANIZATION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                DefaultLeaf currentLeaf = getCurrentSelection();
                if (currentLeaf instanceof RoleLeaf)
                {
                    PickElementDialog pickElement = new PickElementDialog(null, actionAssignRoleToOrganization, "", currentLeaf);

                    if (pickElement.open() == IDialogConstants.OK_ID)
                    {
                        DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();

                        // Set a role in relation to a organization
                        MainController.getInstance().addRoleToOrganization(currentLeaf.getID(), selectedLeaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionRemoveKnowledgeObject()
    {
        return Resources.Frames.Dialog.Actions.REMOVE_KNOWLEDGE_OBJECT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof KnowledgeObjectLeaf)
                {

                    if (ConfirmDialog.openConfirm(null, actionRemoveKnowledgeObject.getToolTipText()))
                    {
                        KnowledgeObjectLeaf knowObjectModel = (KnowledgeObjectLeaf) getCurrentSelection();
                        TreePath[] path = treeViewer.getExpandedTreePaths();
                        DefaultLeaf leaf = (DefaultLeaf) path[path.length - 1].getLastSegment();

                        MainController.getInstance().removeKnowledgeObject(knowObjectModel.getID(), leaf.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionKnowledgeLinkDetails()
    {
        return Resources.Frames.Dialog.Actions.KNOW_LINK_DETAILS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof KnowledgeLinkLeaf)
                {
                    KnowledgeLinkLeaf knowLinkModel = (KnowledgeLinkLeaf) ((IStructuredSelection) treeViewer.getSelection()).getFirstElement();
                    KnowledgeObject knowledgeObject = MainController.getInstance().getKnowlegdeObj(knowLinkModel.getKnowledgeObjectID());
                    KnowledgeLinkDetailsDialog knowledgeLinkDialg = new KnowledgeLinkDetailsDialog(null, actionKnowledgeLinkDetails, "",
                                                                                                   new KnowledgeLinkTemp(knowLinkModel
                                                                                                           .getKnowledgeLink()), knowledgeObject
                                                                                                           .getOwner());
                    knowledgeLinkDialg.open();
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeleteKnowledgeDomain()
    {
        return Resources.Frames.Knowledge.Actions.DELETE_KNOWLEDGE_DOMAIN.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof DomainLeaf)
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteKnowledgeDomain.getToolTipText()))
                    {
                        DomainLeaf domainModel = (DomainLeaf) getCurrentSelection();
                        MainController.getInstance().deleteInstance(domainModel.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionEditItemDescription()
    {
        return Resources.Frames.Global.Actions.ITEM_DESCRIPTION.getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                String description = Resources.Frames.Global.Texts.NO_ENTRIES.getText();
                if (getCurrentSelection() instanceof DefaultLeaf)
                {
                    String id = (getCurrentSelection()).getID();
                    if ( !id.equals("") && !id.equals(GlobalConstants.ROOT_MODEL))
                    {
                        String tempDesc = MainController.getInstance().getDescription(id);
                        if (tempDesc != null && !tempDesc.equals(""))
                            description = tempDesc;
                    }

                }
                InformationDialog.openInformation(null, description);
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddPerson()
    {
        return Resources.Frames.Dialog.Actions.ADD_USER_TO_ROLE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                // Get and set selected objects
                if (getCurrentSelection() instanceof RoleLeaf)
                {
                    RoleLeaf model = (RoleLeaf) getCurrentSelection();
                    PersonArray selectedObjArray;
                    selectedObjArray = MainController.getInstance().getPreSelection(model.getID());

                    List<Person> selectedList = new ArrayList<Person>();
                    if (selectedObjArray != null)
                    {
                        Iterator<Person> itSelectedObj = selectedObjArray.iterator();

                        while (itSelectedObj.hasNext())
                            selectedList.add(itSelectedObj.next());
                    }

                    PersonController controller = new PersonController();
                    DefaultTableModel personModel = new DefaultTableModel(controller.getTableModel(null), controller.getColumns());

                    SelectPersonDialog personDialog = new SelectPersonDialog(null, actionAddPerson, "", personModel, selectedList,
                                                                             new DefaultConstraint(new Long(0), new Long(1000), false));

                    if (personDialog.open() == IDialogConstants.OK_ID)
                    {
                        PersonArray personArray = new PersonArray();
                        Iterator<Person> itReturnValue = personDialog.getSelectedPersons().iterator();
                        while (itReturnValue.hasNext())
                            personArray.add(itReturnValue.next());

                        MainController.getInstance().setPreSelection(model.getID(), personArray);
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddEditDomain()
    {
        return Resources.Frames.Dialog.Actions.ADD_EDIT_DOMAIN.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String parentID = null;
                if (getCurrentSelection() instanceof DomainLeaf)
                {
                    parentID = getCurrentSelection().getID();
                }

                RenameDialog renameDialog = new RenameDialog(null, actionAddEditDomain, "", "", "");

                if (renameDialog.open() == IDialogConstants.OK_ID)
                {
                    if (parentID == null)
                    {
                        MainController.getInstance().createKnowledgeDomain(renameDialog.getName(), renameDialog.getDescritption());
                        reloadTree(knowledgeStructureController.getDomainRootData(true));
                    }
                    else
                        MainController.getInstance().createKnowledgeDomain(renameDialog.getName(), parentID, renameDialog.getDescritption());

                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeletePerson()
    {
        return Resources.Frames.Dialog.Actions.DELETE_USER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (ConfirmDialog.openConfirm(null, actionDeletePerson.getToolTipText()))
                {
                    PersonLeaf personModel = (PersonLeaf) getCurrentSelection();
                    MainController.getInstance().deleteInstance(personModel.getID());
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddEditPerson()
    {
        return Resources.Frames.Dialog.Actions.ADD_EDIT_USER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                PersonLeaf personModel = null;
                if (getCurrentSelection() instanceof PersonLeaf)
                {
                    personModel = (PersonLeaf) getCurrentSelection();
                }

                PersonDialog personDilaog = new PersonDialog(null, actionAddEditPerson,
                                                             Resources.Frames.Dialog.Texts.DESC_USER_CREATE_EDIT.getText(), personModel, true);

                if (personDilaog.open() == IDialogConstants.OK_ID)
                {
                    Person person = personDilaog.getPerson();

                    if (personModel != null)
                    {
                        MainController.getInstance().updateUserInfo(person);
                    }
                    else
                    {
                        MainController.getInstance().createUser(person);
                        treeViewer.refresh(knowledgeStructureController.getSubNode(getCurrentSelection()));
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionEditOrganization()
    {
        return Resources.Frames.Dialog.Actions.EDIT_ORGA.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                OrganizationLeaf actualModel = null;
                if (getCurrentSelection() instanceof OrganizationLeaf)
                {
                    actualModel = (OrganizationLeaf) getCurrentSelection();
                }

                OrganizationDialog organizationDialog = new OrganizationDialog(null, actionAddOrganization,
                                                                               Resources.Frames.Dialog.Texts.DESC_ORGANISATION_CREATE.getText(),
                                                                               actualModel);

                if (organizationDialog.open() == IDialogConstants.OK_ID)
                {
                    MainController.getInstance().updateOrgaInfo(organizationDialog.getOrganization());
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddOrganization()
    {
        return Resources.Frames.Dialog.Actions.ADD_ORGA.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String parentID = null;
                if (getCurrentSelection() instanceof OrganizationLeaf)
                {
                    OrganizationLeaf model = (OrganizationLeaf) getCurrentSelection();
                    parentID = model.getID();
                }
                OrganizationDialog organizationDialog = new OrganizationDialog(null, actionAddOrganization,
                                                                               Resources.Frames.Dialog.Texts.DESC_ORGANISATION_CREATE.getText(), null);

                if (organizationDialog.open() == IDialogConstants.OK_ID)
                {
                    MainController.getInstance().createOrganization(organizationDialog.getOrganization(), parentID);
                    treeViewer.refresh(knowledgeStructureController.getSubNode(getCurrentSelection()));
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeleteProcess()
    {
        return Resources.Frames.Toolbar.Actions.DELETE_PROC_TOOL_BAR.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof ProcessLeaf || getCurrentSelection() instanceof ProcessStructureLeaf)
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteProcess.getToolTipText()))
                    {
                        MainController.getInstance().deleteProcess(getCurrentSelection().getID());

                        if (getCurrentSelection() instanceof ProcessLeaf)
                        {
                            // reload tree
                            reloadTree(new KnowledgeStructureController().getProcessRootData());
                        }
                        else if (getCurrentSelection() instanceof ProcessStructureLeaf)
                        {
                            // reload tree
                            reloadTree(new KnowledgeStructureController().getProcessElementRootData(true));
                        }
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeleteKnowledgeLink()
    {
        return Resources.Frames.Knowledge.Actions.DELETE_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof KnowledgeLinkLeaf)
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteKnowledgeLink.getToolTipText()))
                    {
                        KnowledgeLinkLeaf knowLinkModel = (KnowledgeLinkLeaf) getCurrentSelection();
                        MainController.getInstance().deleteObject(knowLinkModel.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionShowKnowledgeLink()
    {
        return Resources.Frames.Knowledge.Actions.SHOW_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                KnowledgeLinkLeaf knowLinkModel = (KnowledgeLinkLeaf) getCurrentSelection();
                OpenKnowledgeLink.open(knowLinkModel);
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionEditKnowledgeObject()
    {
        return Resources.Frames.Knowledge.Actions.EDIT_WOB.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof KnowledgeObjectLeaf)
                {
                    KnowledgeObjectLeaf actModel = (KnowledgeObjectLeaf) getCurrentSelection();
                    String objectID = actModel.getID();

                    KnowledgeObjectDialog dialog = new KnowledgeObjectDialog(null, actionEditKnowledgeObject, "");
                    dialog.openDialog(objectID);
                }
                else if (getCurrentSelection() instanceof KnowledgeLinkLeaf)
                {
                    ITreeSelection path = (ITreeSelection) treeViewer.getSelection();
                    TreePath[] paths = path.getPaths();
                    Object obj = paths[0].getSegment(paths[0].getSegmentCount() - 2);
                    if (obj instanceof KnowledgeObjectLeaf)
                    {
                        KnowledgeObjectLeaf actModel = (KnowledgeObjectLeaf) obj;
                        String objectID = actModel.getID();

                        KnowledgeObjectDialog dialog = new KnowledgeObjectDialog(null, actionEditKnowledgeObject, "");
                        dialog.openDialog(objectID);
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionDeleteKnowledgeObject()
    {
        return Resources.Frames.Knowledge.Actions.DELETE_WOB.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection() instanceof KnowledgeObjectLeaf)
                {
                    if (ConfirmDialog.openConfirm(null, actionDeleteKnowledgeObject.getToolTipText()))
                    {
                        KnowledgeObjectLeaf knowModel = (KnowledgeObjectLeaf) getCurrentSelection();
                        MainController.getInstance().deleteKnowObj(knowModel.getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionAddKnowledgeObject()
    {
        return Resources.Frames.Knowledge.Actions.ADD_WOB.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (getCurrentSelection().canContainKnowledgeObjects())
                {
                    if (getCurrentSelection() instanceof KnowledgeObjectsFolder)
                    {
                        KnowledgeObjectDialog dialog = new KnowledgeObjectDialog(null, actionAddKnowledgeObject, "");
                        if (dialog.openDialog(""))
                            reloadTree(knowledgeStructureController.getKnowledgeObjectRootData(true));
                    }
                    else
                    {
                        KnowledgeObjectDialog dialog = new KnowledgeObjectDialog(null, actionAddKnowledgeObject, "");
                        dialog.openDialog(getCurrentSelection().getID());
                    }
                }
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionReduceTree()
    {
        return Resources.Frames.Tree.Actions.REDUCE_TREE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                treeViewer.collapseToLevel(getCurrentSelection(), 1);
            }
        });
    }

    /**
     * Description.
     * 
     * @return
     */
    private Action createActionExpandTree()
    {
        return Resources.Frames.Tree.Actions.EXPAND_TREE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                treeViewer.expandToLevel(getCurrentSelection(), 1);
            }
        });
    }

    // ***************************************************
    /**
     * Adds the drag functions and listener to tree.
     */
    private void addDragSupport(final TreeViewer tree)
    {
        DragSource dragSource = new DragSource(tree.getControl(), DND.DROP_COPY | DND.DROP_DEFAULT);
        dragSource.setTransfer(new Transfer[] { new DefaultLeafTransfer() });
        dragSource.addDragListener(new DragSourceAdapter()
        {
            @Override
            public void dragStart(DragSourceEvent event)
            {
                TreeSelection selection = (TreeSelection) treeViewer.getSelection();

                if (selection == null || !(selection.getFirstElement() instanceof KnowledgeObjectLeaf))
                {
                    event.doit = false;
                }
            }

            @Override
            public void dragSetData(final DragSourceEvent event)
            {
                event.data = treeViewer.getSelection();
            }
        });
    }

    /**
     * 
     * Adds drop functions and listener to the tree.
     * 
     * @param tree
     */
    private void addDropSupport(final TreeViewer tree)
    {
        DropTarget dropTarget = new DropTarget(tree.getControl(), DND.DROP_COPY);
        dropTarget.setTransfer(new Transfer[] { new DefaultLeafTransfer() });
        dropTarget.addDropListener(new DropTargetAdapter()
        {
            @Override
            public void drop(final DropTargetEvent event)
            {
                if (event.detail == DND.DROP_COPY)
                {
                    TreeSelection items = (TreeSelection) event.data;
                    DefaultLeaf sourceLeaf = (DefaultLeaf) items.getFirstElement();
                    // Set the current knowledge object in relation to selected leaf
                    Object targetObject = event.item.getData();

                    // Check the source and target. If the condition is true, then set the knowledge object to the given process element.
                    if ((sourceLeaf instanceof KnowledgeObjectLeaf) && (targetObject instanceof DefaultLeaf))
                    {
                        MainController.getInstance().addKnowledgeObject(sourceLeaf.getID(), ((DefaultLeaf) targetObject).getID());
                    }
                }
            }

            @Override
            public void dragEnter(DropTargetEvent event)
            {
                event.detail = DND.DROP_COPY;
            }

            @Override
            public void dragOver(DropTargetEvent event)
            {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
                TreeItem item = (TreeItem) event.item;
                event.detail = DND.DROP_NONE;
                if ((item != null) && (item.getData() instanceof DefaultLeaf))
                {
                    DefaultLeaf targetLeaf = (DefaultLeaf) item.getData();
                    String leafID = targetLeaf.getID();
                    if ( !leafID.equals("") && !leafID.equals(GlobalConstants.DUMMY_ID) && !leafID.equals(GlobalConstants.ROOT_MODEL)
                            && !(notDragableLeafs.contains(targetLeaf.getClass())))
                        event.detail = DND.DROP_COPY;
                }
            }
        });
    }
}
