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

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.dialogs.PersonDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefTreeViewer;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.impl.ElementSorter;
import org.prowim.portal.models.tree.impl.ProcessBrowserFilter;
import org.prowim.portal.models.tree.impl.ProcessBrowserTableSorter;
import org.prowim.portal.models.tree.impl.ProcessLandscapeFilter;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.OrganizationPersonLeaf;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeLeaf;
import org.prowim.portal.update.UpdateNotification;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.browserfunctions.BFGetElementsOfProcess;
import org.prowim.portal.view.browserfunctions.BFShowKnowledge;
import org.prowim.portal.view.browserfunctions.BFShowProcess;
import org.prowim.portal.view.browserfunctions.ModelEditorFunctionFactory;
import org.prowim.portal.view.knowledge.open.CallMailClient;
import org.prowim.portal.view.knowledge.open.OpenKnowledgeLink;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * This view shows the tree with processes and included activity and knowledge objects. If user select a process, <br>
 * the image of process model will shown at the head of view.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ProcessBrowserView extends DefaultView
{
    /** ID of view */
    public static final String         ID                         = ProcessBrowserView.class.getName();

    private static final Logger        LOG                        = Logger.getLogger(ProcessBrowserView.class);

    private DefTreeViewer              processTree;
    private DefTreeViewer              knowledgeObjectTree;
    private DefTreeViewer              personTree;
    private ModelEditor                modelEditor;

    private Action                     addEditPerson;
    private Action                     actionShowKnowledgeLink;
    private Action                     actionShowModelInformations;
    private Action                     actionFeedbackMail;
    private PersonLeaf                 selectedPerson;
    private ProcessLeaf                selectedProcess;
    private KnowledgeLinkLeaf          selectedKnowledgeLink;
    private KnowledgeObjectLeaf        selectedKnowledgeObject;

    private final Set<BrowserFunction> registeredBrowserFunctions = new HashSet<BrowserFunction>();

    private Action                     actionFilterTree;

    private ProcessLandscapeFilter     processLandscapeFilter;

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);

        // Remove toolbar from view
        // super.toolbar.dispose();

        // Set title of View
        this.setPartName(Resources.Frames.Navigation.Actions.KNOWLEDGE_BROWSER_NAV.getText());

        // Set image of View
        this.setTitleImage(Resources.Frames.Navigation.Actions.KNOWLEDGE_BROWSER_NAV.getImage());
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        // Parts view in over
        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        sashForm.SASH_WIDTH = 5;

        // Create top container
        createUpSide(sashForm);

        // Create down container
        createBottomSide(sashForm);

        // Set size of shashForm
        sashForm.setWeights(new int[] { 2, 8 });
    }

    /**
     * Create up side of the view. This included the process group, the knowledge objects group and role group.
     * 
     * @param sashForm Container for upside widgets
     */
    private void createUpSide(SashForm sashForm)
    {
        Composite topContainer = new Composite(sashForm, SWT.NO_SCROLL);
        topContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        topContainer.setLayout(new GridLayout(3, false));

        // Create a group to processes
        Group processGroup = new Group(topContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.NO_SCROLL);
        processGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        processGroup.setLayout(new GridLayout(1, false));
        processGroup.setText(Resources.Frames.Process.Texts.SELECTION_PROCESS.getText());
        createProcessTree(processGroup);

        // Create a group to knowledge objects
        Group knowObjectsGroup = new Group(topContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.NO_SCROLL);
        knowObjectsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        knowObjectsGroup.setLayout(new GridLayout(1, false));
        knowObjectsGroup.setText(Resources.Frames.Knowledge.Texts.APPROPRIATE_KNOWLEDGE.getText());
        createKnowledgeObjectTree(knowObjectsGroup);

        // Create a group to knowledge objects
        Group personGroup = new Group(topContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.NO_SCROLL);
        personGroup.setLayoutData(new GridData(SWT.END, SWT.FILL, false, true));
        personGroup.setLayout(new GridLayout(1, false));
        personGroup.setText(Resources.Frames.Knowledge.Texts.APPROPRIATE_USER.getText());
        createPersonTree(personGroup);

        final Button showLandscapeBtn = toolbar.getItem(actionFilterTree);

        showLandscapeBtn.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (showLandscapeBtn.getSelection())
                {
                    processTree.addFilter(processLandscapeFilter);
                    showLandscapeBtn.setText(Resources.Frames.Toolbar.Actions.SHOW_ALL_PROCESSES.getText());
                    showLandscapeBtn.setImage(Resources.Frames.Toolbar.Actions.SHOW_ALL_PROCESSES.getImage());
                }
                else
                {
                    processTree.removeFilter(processLandscapeFilter);
                    showLandscapeBtn.setText(Resources.Frames.Toolbar.Actions.SHOW_ONLY_PROCESS_LANDSCAPE.getText());
                    showLandscapeBtn.setImage(Resources.Frames.Toolbar.Actions.SHOW_ONLY_PROCESS_LANDSCAPE.getImage());
                }
                processTree.refresh();
            }
        });

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
        addEditPerson();
        createActionShowKnowledgeLink();
        createActionShowModelInformations();
        createActionFeedbackMail();
        createActionFilterTree();
    }

    /**
     * Create the tree shows persons to a selected activity or process .
     * 
     * @param personGroup Container for tree
     */
    private void createPersonTree(Group personGroup)
    {
        personTree = new DefTreeViewer(personGroup, SWT.MULTI);
        personTree.expandToLevel(1);
        personTree.setSorter(new ElementSorter());
        createPersonTreeListner();
    }

    /**
     * Listener for person tree.
     */
    private void createPersonTreeListner()
    {
        this.personTree.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    Object item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof PersonLeaf)
                    {
                        selectedPerson = (PersonLeaf) item;
                        createContextMenu(personTree, addEditPerson);
                    }
                    else
                        personTree.getControl().setMenu(null);
                }
            }
        });

        this.personTree.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    Object item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof PersonLeaf)
                    {
                        selectedPerson = (PersonLeaf) item;
                        addEditPerson.runWithEvent(new Event());
                    }
                }
            }
        });
    }

    /**
     * 
     * Creates context menu for given tree with given action
     * 
     * @param tree Tree, which should have this context menu
     * @param action action of context menu
     */
    private void createContextMenu(DefTreeViewer tree, final Action action)
    {
        // Create menu manager.
        MenuManager menuMgr = new MenuManager();
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener()
        {
            public void menuAboutToShow(IMenuManager mgr)
            {
                mgr.add(action);
            }
        });

        // Create menu.
        Menu menu = menuMgr.createContextMenu(tree.getControl());
        tree.getControl().setMenu(menu);

        // Register menu for extension.
        getSite().registerContextMenu(menuMgr, tree);
    }

    /**
     * Shows a dialog to add or edit a person.
     */
    private void addEditPerson()
    {
        this.addEditPerson = Resources.Frames.Dialog.Actions.SHOW_USER_INFORMATION.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                PersonDialog personDilaog = new PersonDialog(null, addEditPerson, Resources.Frames.Dialog.Texts.DESC_USER_SHOW_INFORMATION.getText(),
                                                             selectedPerson, false);
                personDilaog.open();
            }
        });
    }

    /**
     * Create action to show knowledge link of selected knowledge object.
     * 
     */
    private void createActionShowKnowledgeLink()
    {
        actionShowKnowledgeLink = Resources.Frames.Knowledge.Actions.SHOW_KNOW_LINK.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                OpenKnowledgeLink.open(selectedKnowledgeLink);
            }
        });
    }

    /**
     * Create action to show model informations.
     * 
     */
    private void createActionShowModelInformations()
    {
        actionShowModelInformations = Resources.Frames.Process.Actions.PROCESS_BROWSER.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                showModelInformations(selectedProcess.getID());
            }
        });
    }

    /**
     * Action to call a mail client to give a feedback to a knowledge object.
     * 
     */
    private void createActionFeedbackMail()
    {
        this.actionFeedbackMail = Resources.Frames.Global.Actions.FEEADBACK_MAIL.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
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
                        + selectedKnowledgeObject.getName() + EscapeFunctions.toJSString("\""));
            }
        });

    }

    /**
     * Creates a tree to shows knowledge objects to a selected process or activity.
     * 
     * @param knowObjectsGroup Container for tree.
     */
    private void createKnowledgeObjectTree(Group knowObjectsGroup)
    {
        knowledgeObjectTree = new DefTreeViewer(knowObjectsGroup, SWT.MULTI);
        knowledgeObjectTree.expandToLevel(1);
        knowledgeObjectTree.setSorter(new ElementSorter());
        createKnowledgeObjectTreeListner();
    }

    /**
     * Listener for knowledge object tree tree.
     */
    private void createKnowledgeObjectTreeListner()
    {
        this.knowledgeObjectTree.addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    Object item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof KnowledgeLinkLeaf)
                    {
                        KnowledgeLinkLeaf knowLinkModel = (KnowledgeLinkLeaf) item;
                        OpenKnowledgeLink.open(knowLinkModel);
                    }
                    else if (item instanceof KnowledgeObjectLeaf)
                    {
                        KnowledgeObjectLeaf knowledgeObjectModel = (KnowledgeObjectLeaf) item;
                        KnowledgeObject theKnowledgeObject = knowledgeObjectModel.getKnowledgeObject();
                        if (theKnowledgeObject.getKnowledgeLinks().size() == 1)
                        {
                            KnowledgeLinkLeaf knowLinkModel = new KnowledgeLinkLeaf(theKnowledgeObject.getKnowledgeLinks().get(0));
                            OpenKnowledgeLink.open(knowLinkModel);
                        }
                    }
                    else if (item instanceof PersonLeaf)
                    {
                        selectedPerson = (PersonLeaf) item;
                        addEditPerson.runWithEvent(new Event());
                    }
                }
            }
        });

        this.knowledgeObjectTree.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    Object item = ((IStructuredSelection) s).getFirstElement();
                    if (item instanceof KnowledgeLinkLeaf)
                    {
                        selectedKnowledgeLink = (KnowledgeLinkLeaf) item;
                        createContextMenu(knowledgeObjectTree, actionShowKnowledgeLink);
                    }
                    else if (item instanceof KnowledgeObjectLeaf)
                    {
                        selectedKnowledgeObject = (KnowledgeObjectLeaf) item;
                        createContextMenu(knowledgeObjectTree, actionFeedbackMail);
                    }
                    else if (item instanceof PersonLeaf)
                    {
                        selectedPerson = (PersonLeaf) item;
                        createContextMenu(knowledgeObjectTree, addEditPerson);
                    }
                    else
                        knowledgeObjectTree.getControl().setMenu(null);

                }
            }
        });
    }

    /**
     * 
     * Fills the knowledge object tree with data to selected process or activity.
     * 
     * @param itemID
     */
    private void fillKnowledgeObjectTree(final String itemID)
    {
        DefaultLeaf root = new DefaultLeaf("", "");

        // Get knowledge objects of process itself
        List<KnowledgeObject> listOfKnowObj = MainController.getInstance().getKnowledgeObjects(itemID);

        DefaultLeaf processWob = new DefaultLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText()
                + GlobalConstants.DOUBLE_POINT + " " + MainController.getInstance().getName(itemID));
        for (KnowledgeObject knowledgeObject : listOfKnowObj)
        {
            KnowledgeObjectLeaf knowObjModel = new KnowledgeStructureController().getKnowledgeObjectChildren(knowledgeObject);
            processWob.addChild(knowObjModel);

        }

        if (processWob.hasChildren())
            root.addChild(processWob);

        // Get knowledge objects of process elements
        List<KnowledgeObject> listOfPEKnowObj = MainController.getInstance().getKnowledgeObjectsOfProcess(itemID);

        DefaultLeaf allWob = new DefaultLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText()
                + GlobalConstants.DOUBLE_POINT + " " + Resources.Frames.Knowledge.Texts.ALL_PROCESS_ELEMENTS.getText());

        for (KnowledgeObject knowledgeObject : listOfPEKnowObj)
        {
            KnowledgeObjectLeaf knowObjModel = new KnowledgeStructureController().getKnowledgeObjectChildren(knowledgeObject);
            allWob.addChild(knowObjModel);
        }

        if (allWob.hasChildren())
            root.addChild(allWob);

        knowledgeObjectTree.setInputAndExpand(root, true);
    }

    /**
     * 
     * Fills person tree with persons included in given role.
     * 
     * @param roleArray A list of {@link Role}
     */
    private void fillPersonTree(final RoleArray roleArray)
    {
        DefaultLeaf root = new DefaultLeaf("", "");

        OrganizationPersonLeaf orgaUnitModel = new OrganizationPersonLeaf();
        Iterator<Role> roleIt = roleArray.iterator();

        while (roleIt.hasNext())
        {
            PersonArray selectedObjArray = MainController.getInstance().getPreSelection(roleIt.next().getID());
            if (selectedObjArray != null)
            {
                Iterator<Person> itSelectedObj = selectedObjArray.iterator();

                while (itSelectedObj.hasNext())
                    orgaUnitModel.addChild(new PersonLeaf(itSelectedObj.next()));
            }
        }
        root.addChild(orgaUnitModel);
        this.personTree.setInputAndExpand(root, true);
    }

    /**
     * Create the down side of the view. This included the mxViewer to shows the graph of a process.
     * 
     * @param sashForm Container
     */
    private void createBottomSide(SashForm sashForm)
    {
        Composite downContainer = new Composite(sashForm, SWT.NONE);
        downContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        downContainer.setLayout(new GridLayout(1, false));

        // Create a group to tree
        Group downGroup = new Group(downContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        downGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        downGroup.setLayout(new GridLayout(1, false));
        downGroup.setText(Resources.Frames.Global.Texts.CHART.getText());

        modelEditor = new ModelEditor(downGroup);
    }

    /**
     * Initializes browser functions for transaction between RAP and JS(Model editor).
     */
    private void initBrowserFunctions()
    {
        BFShowKnowledge bfShowKnowledge = ModelEditorFunctionFactory.registerModelEditorFunction(BFShowKnowledge.class, modelEditor);
        bfShowKnowledge.setKnowObjTree(knowledgeObjectTree);
        bfShowKnowledge.setPersonTree(personTree);

        // Shows attribute infos to given element id
        registeredBrowserFunctions.add(bfShowKnowledge);

        // Shows attribute infos to given element id
        registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetElementsOfProcess.class, modelEditor));
        // showSubProcess
        registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowProcess.class, modelEditor));

    }

    /**
     * 
     * Dispose all registered browser functions .
     */
    private void disposeBrowserFunctions()
    {
        for (BrowserFunction bf : registeredBrowserFunctions)
        {
            bf.dispose();
        }
        registeredBrowserFunctions.clear();
    }

    /**
     * 
     * Create process tree.
     */
    private void createProcessTree(Group goup)
    {
        processTree = new DefTreeViewer(goup, SWT.MULTI);
        processTree.expandToLevel(1);
        processTree.setSorter(new ProcessBrowserTableSorter());

        processLandscapeFilter = new ProcessLandscapeFilter();
        processTree.addFilter(new ProcessBrowserFilter());
        createProcessTreeListener();

        initProcessTree();
    }

    /**
     * 
     * Initialize the process tree
     */
    private void initProcessTree()
    {
        KnowledgeStructureController controller = new KnowledgeStructureController();
        processTree.setInputAndExpand(controller.getProcessRootData(), true);
    }

    /**
     * createProcTreeListner.
     * 
     * @param wobController ProcessWOBController
     */
    private void createProcessTreeListener()
    {
        // Selection listener
        this.processTree.addSelectionChangedListener(new ISelectionChangedListener()
        {
            private boolean     listenerEnabled    = true;
            private DefaultLeaf lastValidSelection = null;

            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                if (listenerEnabled)
                {
                    DefaultLeaf selectedLeaf = (DefaultLeaf) ((IStructuredSelection) event.getSelection()).getFirstElement();

                    if (selectedLeaf instanceof ProcessLeaf)
                    {
                        selectedProcess = (ProcessLeaf) selectedLeaf;
                        createContextMenu(processTree, actionShowModelInformations);
                        if ( !modelEditor.isLoading())
                        {
                            lastValidSelection = selectedLeaf;

                            // Clear children of processes
                            String id = selectedProcess.getID();
                            if (id != null)
                            {
                                showModelInformations(id);
                            }
                        }
                        else
                        {
                            LOG.warn("Tried to load new model while old one was still loading");

                            this.listenerEnabled = false;
                            ProcessBrowserView.this.processTree.setSelection(new StructuredSelection(lastValidSelection));
                            this.listenerEnabled = true;
                        }
                    }
                    else if (selectedLeaf instanceof ProcessTypeLeaf)
                    {
                        lastValidSelection = selectedLeaf;
                        processTree.getControl().setMenu(null);
                    }
                    else
                        processTree.getControl().setMenu(null);

                }
            }
        });
    }

    /**
     * 
     * Shows the model and get the knowledge objects and persons of model and shows these in trees
     * 
     * @param modelID id of model
     */
    private void showModelInformations(String modelID)
    {
        // get knowledge of process
        fillKnowledgeObjectTree(modelID);

        RoleArray roleArray = MainController.getInstance().getProcessRoles(modelID);
        fillPersonTree(roleArray);

        // init browser functions
        reInitBrowserFunctions();

        modelEditor.setModelName(MainController.getInstance().getName(modelID));
        modelEditor.setViewerMode(modelID);
        modelEditor.setModelXML(MainController.getInstance().loadProcessAsXML(modelID));
    }

    /**
     * Open the subprocess with ProcessBrwoserView.
     * 
     * @param id the subprocess ID. not null.
     */
    public void setSubprocess(String id)
    {
        fillKnowledgeObjectTree(id);

        RoleArray roleArray = MainController.getInstance().getProcessRoles(id);
        fillPersonTree(roleArray);

        // init browser functions
        reInitBrowserFunctions();

        modelEditor.setModelName(MainController.getInstance().getName(id));
        modelEditor.setViewerMode(id);
        modelEditor.setModelXML(MainController.getInstance().loadProcessAsXML(id));

        processTree.refresh();
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#dispose()
     */
    @Override
    public void dispose()
    {
        disposeBrowserFunctions();
        modelEditor.dispose();
        super.dispose();
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
        initProcessTree();

        UpdateNotification element = updates.element();
        Collection<String> entityIds = element.getEntityIds();

        for (String id : entityIds)
        {
            processTree.reloadTreeNode(id);
        }

        this.knowledgeObjectTree.getTree().removeAll();
        this.personTree.getTree().removeAll();

        if (this.modelEditor.getModelId() != null && updates.isContainingEntityId(this.modelEditor.getModelId()))
        {
            reInitBrowserFunctions();
            this.modelEditor.clear();
        }
    }

    /**
     * Reinit all required {@link BrowserFunction}s
     */
    private void reInitBrowserFunctions()
    {
        disposeBrowserFunctions();
        registeredBrowserFunctions.addAll(ModelEditorView.initCommonModelEditorBrowserFunctions(modelEditor));
        initBrowserFunctions();
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
        toolbar.addToolBarItem(actionFilterTree, SWT.TOGGLE | SWT.LEFT);
    }

    /**
     * Description.
     */
    private void createActionFilterTree()
    {
        this.actionFilterTree = Resources.Frames.Toolbar.Actions.SHOW_ONLY_PROCESS_LANDSCAPE.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
            }
        });
    }
}
