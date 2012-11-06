/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.process;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.portal.LoggedInUserInfo;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.OrgaController;
import org.prowim.portal.controller.dialog.PersonController;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.dialogs.CloneProcessDialog;
import org.prowim.portal.dialogs.PickElementDialog;
import org.prowim.portal.dialogs.ProcessVersionsDialog;
import org.prowim.portal.dialogs.RenameDialog;
import org.prowim.portal.dialogs.SelectOrganizationDialog;
import org.prowim.portal.dialogs.SelectPersonDialog;
import org.prowim.portal.dialogs.SubprocessReferencesDialog;
import org.prowim.portal.dialogs.feedback.ConfirmDialog;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.export.ExportProcesses;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefTreeViewer;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.impl.ModelEditorElementSorter;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeRootLeaf;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.update.UpdateNotification;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidateComboBox;
import org.prowim.rap.framework.components.impl.ValidatedTextArea;
import org.prowim.rap.framework.components.impl.ValidatedTextField;



/**
 * User can create a new process model or open a existing one. <br>
 * The models opens in a new tab, so that we can have for each model one own tab. <br>
 * If a model is opens and the user try to open the model, the opens tab get focus.
 * 
 * 
 * @author Saad Wardi, Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class ProcessModelEditorView extends DefaultView
{
    /** ID of view */
    public static final String           ID                  = ProcessModelEditorView.class.getName();

    private DefTreeViewer                treeViewer;

    private Action                       showModel, deleteProcess, expandTree, reduceTree, setProccesType, createProcessType, createProcess,
            securityPersonAction, securityOEAction, showVersions, editNameAndDescriptionAction, deleteProcessCategory,
            openSubprocessReferencesAction, actionCopyProcess, actionExportProcesses;;

    private Object                       item;

    private Group                        mainGroup;
    private ValidateComboBox             procTypeCombo;
    private ValidatedTextField           nameTxt;
    private KnowledgeStructureController dataController;
    private ProcessTypeEditorLeaf        selectedProcessType = null;
    private boolean                      firstInit           = true;

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
        this.setPartName(Resources.Frames.Process.Actions.PROCESS_MODELLER_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Process.Actions.PROCESS_MODELLER_NAV.getImage());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(final Composite container)
    {
        // Composite for all view
        final Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout());

        Group existProcGroup = new Group(composite, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        existProcGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        existProcGroup.setLayout(new GridLayout());
        existProcGroup.setText(Resources.Frames.Process.Texts.OPEN_EXISTING_PROCESS.getText());

        dataController = new KnowledgeStructureController();

        createTree(existProcGroup);

        initTree(getTreeModel(false));

        createActions();
        toolbar.getItem(createProcess.getText()).setEnabled(false);
        toolbar.getItem(createProcessType.getText()).setEnabled(false);
    }

    /**
     * 
     * Description.
     * 
     * @param flag
     * @return
     */
    private DefaultLeaf getTreeModel(boolean flag)
    {
        return dataController.getProcessTypeRootLeaf(flag);
    }

    /**
     * Created a new process model. User should give the name an process type to create a process. <br>
     * User can also create a new process type
     * 
     * @param sashForm
     */
    @SuppressWarnings("unused")
    private void createNewProc(final SashForm sashForm)
    {
        Composite container = new Composite(sashForm, SWT.RIGHT);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));

        // **********
        Label nameLbl = new Label(container, SWT.NONE);
        nameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        nameLbl.setText(Resources.Frames.Global.Texts.DENOTATION.getText() + GlobalConstants.DOUBLE_POINT);

        this.nameTxt = new ValidatedTextField(container, new DefaultConstraint(new Long(1), new Long(1000), true));
        nameTxt.setLayoutData(new GridData(GridData.FILL, SWT.LEFT, true, false));

        // ************
        Label procTypeLbl = new Label(container, SWT.NONE);
        procTypeLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        procTypeLbl.setText(Resources.Frames.Process.Texts.PROCESS_TYPE.getText() + GlobalConstants.DOUBLE_POINT);

        procTypeCombo = new ValidateComboBox(container);
        procTypeCombo.setLayoutData(new GridData(GridData.FILL, SWT.LEFT, true, false));
        initComboLimit();

        // ************
        Button blankBtn = new Button(container, SWT.PUSH);
        blankBtn.setVisible(false);

        // ************
        Composite btnContainer = new Composite(container, SWT.RIGHT);
        btnContainer.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));
        btnContainer.setLayout(new GridLayout(2, false));
    }

    /**
     * 
     * Create a new process model
     * 
     * @param selectProcType
     * @param nameTxt
     */
    private void createProcessModel(ValidatedTextField nameTxt, String selectProcTypeID)
    {
        // Create the new process
        // TODO Philipp Leusmann Move this into Factory
        String processId = MainController.getInstance().createObject(GlobalConstants.NEW_PROCESS_MODEL_ID, GlobalConstants.PROCESS);
        if (processId != null)
        {
            MainController.getInstance().rename(processId, GlobalConstants.NEW_PROCESS);
            MainController.getInstance().setProcessType(selectProcTypeID, processId);
            MainController.getInstance().setRelationValue(processId, AlgernonConstants.Slots.ALFRESCO_VERSION,
                                                          AlgernonConstants.EDITABLE_ALFRESCO_VERSION_LABEL);
            MainController.getInstance().setRelationValue(processId, AlgernonConstants.Slots.USER_DEFINED_VERSION,
                                                          AlgernonConstants.EDITABLE_USER_VERSION_LABEL);

            StringBuilder xmlProcessBuilder = new StringBuilder();
            xmlProcessBuilder.append("<mxGraphModel>");
            xmlProcessBuilder.append("<root>");
            xmlProcessBuilder.append("<Workflow label=\"ProWimWorkflow\" description=\"\" href=\"\" id=\"0\"><mxCell/></Workflow>");
            xmlProcessBuilder.append("<Layer label=\"Default Layer\" id=\"1\"><mxCell parent=\"0\"/></Layer>");
            xmlProcessBuilder.append("<Process label=\"");
            xmlProcessBuilder.append(GlobalConstants.NEW_PROCESS);
            xmlProcessBuilder.append("\" description=\"\" prowimtype=\"PROCESS\" prowimid=\"");
            xmlProcessBuilder.append(processId);
            xmlProcessBuilder
                    .append("\" id=\"2\"><mxCell style=\"rounded=0;strokeColor=#444444;fillColor=#C3D9FF\" parent=\"1\" vertex=\"1\"><mxGeometry x=\"20\" y=\"20\" width=\"200\" height=\"50\" as=\"geometry\"/></mxCell>");
            xmlProcessBuilder.append("</Process></root></mxGraphModel>");

            MainController.getInstance().saveProcessAsXML(processId, xmlProcessBuilder.toString(), false, null);

            showModelEditor(processId);
        }
    }

    /**
     * Initialization the combo box.
     */
    private void initComboLimit()
    {
        // Clear the combo box
        procTypeCombo.removeAll();

        // Get the data from DB
        ProcessTypeArray procTypeArray = MainController.getInstance().getAllProcessTypes();

        Iterator<ProcessType> procTypeIt = procTypeArray.iterator();

        int idx = 0;
        while (procTypeIt.hasNext())
        {
            ProcessType processType = procTypeIt.next();
            procTypeCombo.add(processType.getName(), idx);
            procTypeCombo.setData(Integer.toString(idx), processType);
            idx++;
        }

        procTypeCombo.select(0);
    }

    /**
     * Initial the area to create a process type.
     * 
     * @param sashForm
     */
    @SuppressWarnings("unused")
    private void createNewProcType(final SashForm sashForm)
    {
        Composite container = new Composite(sashForm, SWT.RIGHT);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));

        Label nameLbl = new Label(container, SWT.NONE);
        nameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        nameLbl.setText(Resources.Frames.Global.Texts.DENOTATION.getText() + GlobalConstants.DOUBLE_POINT);

        final ValidatedTextField nameTxt = new ValidatedTextField(container, new DefaultConstraint(new Long(1), new Long(1000), true));
        nameTxt.setLayoutData(new GridData(GridData.FILL, SWT.LEFT, true, false));

        Label descLbl = new Label(container, SWT.NONE);
        descLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        descLbl.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText() + GlobalConstants.DOUBLE_POINT);

        final ValidatedTextArea descTxt = new ValidatedTextArea(container, new DefaultConstraint(new Long(1), new Long(1000), true));
        GridData gridData = new GridData(GridData.FILL, SWT.LEFT, true, false);
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 1;
        gridData.heightHint = 50;
        gridData.widthHint = 100;
        descTxt.setLayoutData(gridData);

        Button blankBtn = new Button(container, SWT.PUSH);
        blankBtn.setVisible(false);

        Composite btnContainer = new Composite(container, SWT.RIGHT);
        btnContainer.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, true));
        btnContainer.setLayout(new GridLayout(2, false));

        GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        data.widthHint = IDialogConstants.BUTTON_WIDTH;

        Button okBtn = new Button(btnContainer, SWT.PUSH);
        okBtn.setText(Resources.Frames.Global.Texts.OK.getText());
        okBtn.setData(data);
        okBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                createProcType("");
            }
        });

        // Button to create a new process
        Button cancelBtn = new Button(btnContainer, SWT.PUSH);
        cancelBtn.setData(data);
        cancelBtn.setText(Resources.Frames.Global.Texts.CANCEL.getText());
        cancelBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                mainGroup.setText(Resources.Frames.Process.Texts.CREATE_NEW_PROCESS.getText());
                sashForm.setWeights(new int[] { 10, 0 });

                toolbar.getItem(createProcessType.getText()).setEnabled(true);
                toolbar.getItem(createProcess.getText()).setEnabled(true);
            }
        });
    }

    /**
     * 
     * Create a process type to given name and description.
     */
    private void createProcType(String parentID)
    {
        // Create process type
        createCat(parentID);

        toolbar.getItem(createProcessType.getText()).setEnabled(true);
        toolbar.getItem(createProcess.getText()).setEnabled(true);
    }

    private void createCat(String parentID)
    {
        Action act = Resources.Frames.Toolbar.Actions.CREATE_NEW_PROCESS_TYPE.getAction();

        // Get name and description of dialog
        RenameDialog renameDialog = new RenameDialog(null, act, "", "", "");
        if (renameDialog.open() == IDialogConstants.OK_ID)
        {
            String typeID = MainController.getInstance().createProcessType(renameDialog.getName(), renameDialog.getDescritption());

            if (parentID != null && !parentID.equals(""))
            {
                MainController.getInstance().setProcessTypeParent(typeID, parentID);
            }
        }
    }

    private void createRootCat()
    {
        createProcType(null);
        initTree(getTreeModel(true));
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createActions()
     */
    @Override
    protected void createActions()
    {
        super.createActions();

        // Expand tree node
        createActionExpandTree();

        // Reduce tree node
        createActionReduceTree();
        createActionOpenSubprocessReferences();
        createActionDeleteProcess();

        createActionShowModel();

        createActionSetProccesType();

        createActionCreateProcessType();

        createActionCreateNewProcess();
        /** security action. */
        createActionSetSecurity();

        // creates the action to display all versions of the selected process template
        createActionShowVersions();

        createEditNameAndDescriptionAction();

        deleteProcessCategory();

        actionCopyProcess();

        actionExportProcesses = createActionExportProcesses();
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
                if (item instanceof DefaultLeaf)
                {
                    new ExportProcesses().exportToExcel((DefaultLeaf) item);
                }
            }
        });
    }

    /**
     * This action opens a dialog to clone a process.
     */
    private void actionCopyProcess()
    {
        this.actionCopyProcess = Resources.Frames.Process.Actions.COPY_PROCESS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (item instanceof ProcessEditorLeaf)
                {
                    ProcessEditorLeaf model = (ProcessEditorLeaf) item;
                    Process process = DefaultDataObjectFactory.createProwimProcessTemplate(model.getID(), model.getName());
                    CloneProcessDialog dialog = new CloneProcessDialog(null, actionCopyProcess, process, "");
                    dialog.open();
                }
            }
        });
    }

    /**
     * 
     * Return the element id of selected node
     * 
     * @return String id of element. null, if model not find
     */
    private String getElementIDOfSelectedNode()
    {
        String elementID = null;
        if (item instanceof DefaultLeaf)
        {
            DefaultLeaf model = (DefaultLeaf) item;
            elementID = model.getID();
        }
        else
        {
            throw new IllegalArgumentException("Model is not supported! ");
        }

        return elementID;
    }

    /**
     * Action to delete a process category.
     */
    private void deleteProcessCategory()
    {
        deleteProcessCategory = Resources.Frames.Toolbar.Actions.DELETE_PROCESS_CATEGORY.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getElementIDOfSelectedNode();

                // Check if the selected category included processes or other categories
                ProcessArray processList = MainController.getInstance().getTypeProcesses(elementID);
                ProcessTypeArray procTypeArray = MainController.getInstance().getProcessCategories(elementID);

                if (processList.isEmpty() && procTypeArray.isEmpty())
                {
                    if (ConfirmDialog.openConfirm(null, deleteProcessCategory.getToolTipText()))
                    {
                        MainController.getInstance().deleteObject(elementID);
                    }
                }
                else
                {
                    InformationDialog.openInformation(null, Resources.Frames.Toolbar.Texts.DELETE_PROCESS_CATEGORY_TEXT.getText());
                }
            }
        });
    }

    /**
     * Action to move an organization from a location to other.
     */
    private void createEditNameAndDescriptionAction()
    {
        editNameAndDescriptionAction = Resources.Frames.Toolbar.Actions.EDIT_NAME_DESCRIPTION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                String elementID = getElementIDOfSelectedNode();
                if (elementID != null)
                {
                    RenameDialog renameDialog = new RenameDialog(null, editNameAndDescriptionAction, "", MainController.getInstance()
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
     */
    private void createActionSetSecurity()
    {
        this.securityPersonAction = Resources.Frames.Global.Actions.ITEM_SECURITY_PERSON.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                PersonController controller = new PersonController();
                DefaultTableModel personModel = new DefaultTableModel(controller.getTableModel(null), controller.getColumns());
                MainController.getInstance().getPersonsCanModifyEntity(getElementIDOfSelectedNode());

                SelectPersonDialog personDialog = new SelectPersonDialog(null, securityPersonAction, "", personModel, MainController.getInstance()
                        .getPersonsCanModifyEntity(getElementIDOfSelectedNode()), new DefaultConstraint(new Long(0), new Long(1000), false));

                if (personDialog.open() == IDialogConstants.OK_ID)
                {
                    MainController.getInstance().setElementCanModifyEntity(getElementIDOfSelectedNode(),
                                                                           copyReturnPersonsValuesToObjectArray(personDialog.getSelectedPersons()));
                }
            }
        });

        this.securityPersonAction.setEnabled(false);

        this.securityOEAction = Resources.Frames.Global.Actions.ITEM_SECURITY_OE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                List<Organization> selectedOrgs = MainController.getInstance().getOrganizationsCanModifyEntity(getElementIDOfSelectedNode());

                OrgaController controller = new OrgaController();
                DefaultTableModel orgaModel = new DefaultTableModel(controller.getTableModelUnique(selectedOrgs), controller.getColumns());

                SelectOrganizationDialog organizationDialog = new SelectOrganizationDialog(null, securityOEAction, "", orgaModel, selectedOrgs,
                                                                                           new DefaultConstraint(new Long(0), new Long(1000), false));

                if (organizationDialog.open() == IDialogConstants.OK_ID)
                {
                    MainController.getInstance().setElementCanModifyEntity(getElementIDOfSelectedNode(),
                                                                           copyReturnOrgasValuesToObjectArray(organizationDialog
                                                                                   .getSelectedOrganizations()));
                }
            }
        });
        this.securityOEAction.setEnabled(false);
    }

    private ObjectArray copyReturnPersonsValuesToObjectArray(Collection<Person> returnValue)
    {
        final ObjectArray result = new ObjectArray();
        if (returnValue != null)
        {
            Iterator<Person> it = returnValue.iterator();

            while (it.hasNext())
            {
                Person person = it.next();
                result.add(person.getID());
            }
        }
        return result;
    }

    /**
     * Gets the organizations ID and copy them into an array that will be send to the server.
     * 
     * @param returnValue the fetched Organizations Objects.
     * @return not null ObjectArray.
     */
    private ObjectArray copyReturnOrgasValuesToObjectArray(Collection<Organization> returnValue)
    {
        final ObjectArray result = new ObjectArray();
        if (returnValue != null)
        {
            Iterator<Organization> it = returnValue.iterator();

            while (it.hasNext())
            {
                Organization orga = it.next();
                result.add(orga.getID());
            }
        }
        return result;
    }

    /**
     * Description.
     */
    private void createActionCreateProcessType()
    {
        this.createProcessType = Resources.Frames.Toolbar.Actions.CREATE_NEW_PROCESS_TYPE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (item instanceof ProcessTypeEditorLeaf)
                {
                    ProcessTypeEditorLeaf typeModel = (ProcessTypeEditorLeaf) item;
                    createProcType(typeModel.getID());
                }
                else if (item instanceof ProcessTypeRootLeaf)
                {
                    createRootCat();
                }
            }
        });
    }

    /**
     * Description.
     */
    private void createActionCreateNewProcess()
    {
        this.createProcess = Resources.Frames.Toolbar.Actions.CREATE_NEW_PROCESS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                createProcessModel(nameTxt, selectedProcessType.getID());
            }
        });
    }

    /**
     * Action to set or change the process type of process.
     */
    private void createActionSetProccesType()
    {
        this.setProccesType = Resources.Frames.Process.Actions.SET_PROCESS_TYPE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (item instanceof ProcessEditorLeaf)
                {
                    PickElementDialog pickElement = new PickElementDialog(null, setProccesType, "", new ProcessTypeRootLeaf("", ""));

                    if (pickElement.open() == IDialogConstants.OK_ID)
                    {
                        DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();
                        MainController.getInstance().setProcessType(selectedLeaf.getID(), getElementIDOfSelectedNode());
                        initTree(getTreeModel(true));
                    }
                }
            }
        });
    }

    /**
     * 
     * Create tree.
     */
    private void createTree(Composite parent)
    {
        treeViewer = new DefTreeViewer(parent, SWT.MULTI);
        treeViewer.expandToLevel(1);
        treeViewer.setSorter(new ModelEditorElementSorter());
        createTreeListener();
        addDoubleClickListener();
    }

    /**
     * Initialize tree with given data.
     */
    private void initTree(final Object mainTree)
    {
        treeViewer.getTree().clearAll(true);
        treeViewer.setInputAndExpand(mainTree, true);
    }

    /**
     * createProcTreeListner.
     * 
     * @param wobController ProcessWOBController
     */
    private void createTreeListener()
    {
        // Selection listener
        this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof ProcessEditorLeaf)
                    {
                        expandTree.setEnabled(false);
                        reduceTree.setEnabled(false);
                        showModel.setEnabled(true);
                        toolbar.getItem(createProcessType.getText()).setEnabled(false);
                        toolbar.getItem(createProcess.getText()).setEnabled(false);
                        Person person = LoggedInUserInfo.getInstance().getPerson();
                        if (MainController.getInstance().canPersonModifyEntity(((ProcessEditorLeaf) item).getID(), person.getID()))
                        {
                            deleteProcess.setEnabled(true);
                            setProccesType.setEnabled(true);
                            securityPersonAction.setEnabled(true);
                            securityOEAction.setEnabled(true);
                            showVersions.setEnabled(true);
                            editNameAndDescriptionAction.setEnabled(true);
                        }
                    }
                    else if (item instanceof ProcessTypeEditorLeaf)
                    {
                        toolbar.getItem(createProcessType.getText()).setEnabled(true);
                        toolbar.getItem(createProcess.getText()).setEnabled(true);

                        ProcessTypeEditorLeaf catModel = (ProcessTypeEditorLeaf) item;
                        selectedProcessType = catModel;
                        createProcessType.setEnabled(true);
                        createProcess.setEnabled(true);
                        editNameAndDescriptionAction.setEnabled(true);
                        deleteProcessCategory.setEnabled(true);
                    }
                    else if (item instanceof ProcessTypeRootLeaf)
                    {
                        toolbar.getItem(createProcessType.getText()).setEnabled(true);
                        toolbar.getItem(createProcess.getText()).setEnabled(false);
                        createProcessType.setEnabled(true);
                        if (firstInit)
                        {
                            initTree(getTreeModel(true));
                            treeViewer.refresh();
                            item = ((IStructuredSelection) s).getFirstElement();
                            firstInit = false;
                        }
                    }
                    else
                    {
                        setActionsFlag(false);
                    }
                }
            }
        });
    }

    private void setActionsFlag(boolean flag)
    {
        deleteProcess.setEnabled(flag);
        showModel.setEnabled(flag);
        showVersions.setEnabled(flag);
        setProccesType.setEnabled(flag);
        securityPersonAction.setEnabled(flag);
        securityOEAction.setEnabled(flag);
        editNameAndDescriptionAction.setEnabled(flag);
        createProcessType.setEnabled(flag);
        createProcess.setEnabled(flag);
        deleteProcessCategory.setEnabled(flag);
    }

    /**
     * addDoubleClickListener.
     */
    private void addDoubleClickListener()
    {
        this.treeViewer.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof ProcessEditorLeaf)
                    {
                        showModel.runWithEvent(new Event());
                    }
                }
            }
        });
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

        // Create menu.
        Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
        treeViewer.getControl().setMenu(menu);

        // Register menu for extension.
        getSite().registerContextMenu(menuMgr, treeViewer);
    }

    /**
     * 
     * Fill context menu with actions. This actions are the same action as in toolbar.
     * 
     * @param MenuManager Included all menu items in this view
     */
    private void fillContextMenu(IMenuManager mgr)
    {
        if ( !treeViewer.getExpandedState(item))
            mgr.add(expandTree);
        else
            mgr.add(reduceTree);

        mgr.add(new Separator());
        if (item instanceof ProcessEditorLeaf)
        {
            mgr.add(editNameAndDescriptionAction);
            mgr.add(showModel);
            mgr.add(actionCopyProcess);
            mgr.add(deleteProcess);
            mgr.add(setProccesType);
            mgr.add(showVersions);
            mgr.add(actionExportProcesses);
            mgr.add(new Separator());
            mgr.add(securityPersonAction);
            mgr.add(securityOEAction);
            mgr.add(new Separator());
            mgr.add(helpToolBar);
        }
        else if (item instanceof ProcessTypeEditorLeaf)
        {
            mgr.add(editNameAndDescriptionAction);
            mgr.add(this.createProcess);
            mgr.add(this.createProcessType);
            mgr.add(deleteProcessCategory);
            mgr.add(actionExportProcesses);
        }
        else if (item instanceof ProcessTypeRootLeaf)
        {
            mgr.add(this.createProcessType);
            mgr.add(actionExportProcesses);
            createProcessType.setEnabled(true);
        }
    }

    /**
     * Action to reduce the tree.
     */
    private void createActionReduceTree()
    {
        this.reduceTree = Resources.Frames.Tree.Actions.REDUCE_TREE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                treeViewer.collapseToLevel(item, 1);
            }
        });
    }

    /**
     * 
     * Action to expand the tree.
     */
    private void createActionExpandTree()
    {
        this.expandTree = Resources.Frames.Tree.Actions.EXPAND_TREE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                treeViewer.expandToLevel(item, 1);
            }
        });
    }

    private void createActionShowModel()
    {
        this.showModel = Resources.Frames.Toolbar.Actions.SHOW_PROCESS_MODEL.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                Process newestVersionProcess = MainController.getInstance().getNewestProcessModelVersionID(getElementIDOfSelectedNode());
                showModelEditor(newestVersionProcess.getTemplateID());
            }
        });
    }

    private void createActionOpenSubprocessReferences()
    {
        this.openSubprocessReferencesAction = Resources.Frames.Toolbar.Actions.DELETE_SUB_PROCESS.getAction();
    }

    /**
     * Action to deletes the selected process .
     */
    private void createActionDeleteProcess()
    {
        this.deleteProcess = Resources.Frames.Toolbar.Actions.DELETE_PROC_TOOL_BAR.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (ConfirmDialog.openConfirm(null, deleteProcess.getToolTipText()))
                {
                    if (MainController.getInstance().isSubprocess(getElementIDOfSelectedNode()))
                    {
                        if ( !MainController.getInstance().getSubprocessReferences(getElementIDOfSelectedNode()).isEmpty())
                            // TODO: if it is a subprocess first open a dialog with the list of the processes
                            SubprocessReferencesDialog.openDialog(null, openSubprocessReferencesAction, getElementIDOfSelectedNode());
                        else
                            MainController.getInstance().deleteProcess(getElementIDOfSelectedNode());
                    }
                    else
                        MainController.getInstance().deleteProcess(getElementIDOfSelectedNode());
                }
            }
        });
    }

    /**
     * Creates the action to show the versions of the selected process.
     */
    private void createActionShowVersions()
    {
        this.showVersions = Resources.Frames.Toolbar.Actions.SHOW_VERSIONS.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                if (item instanceof ProcessEditorLeaf)
                {
                    ProcessVersionsDialog dialog = new ProcessVersionsDialog(null, showVersions, "", ((ProcessEditorLeaf) item).getProcess(), false);
                    dialog.open();
                }
            }
        });
    }

    /**
     * 
     * Reload tree with new data from DB.
     * 
     */
    public void reloadTree()
    {
        initTree(getTreeModel(true));
        treeViewer.setSorter(new ModelEditorElementSorter());
        treeViewer.refresh();
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
        toolbar.addToolBarItem(this.createProcess);
        toolbar.addToolBarItem(this.createProcessType);
    }

    /**
     * Shows the model with the given id in the model editor. uses the viewer-mode if the current user has insufficient rights.
     * 
     * @param modelId
     */
    private void showModelEditor(String modelId)
    {
        ModelEditorView modelEditorView = null;
        try
        {
            modelEditorView = (ModelEditorView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                    .showView(ModelEditorView.ID, modelId, IWorkbenchPage.VIEW_ACTIVATE);
            modelEditorView.setProcessId(modelId);
        }
        catch (PartInitException e)
        {
            throw new RuntimeException("Cannot open model editor view", e);
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.PROCESS);
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
        Collection<String> entityIds = element.getEntityIds();

        for (String id : entityIds)
        {
            treeViewer.reloadTreeNode(id);
        }
    }

}
