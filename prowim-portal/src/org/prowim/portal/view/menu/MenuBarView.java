/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.menu;

import java.util.EnumSet;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.knowledge.ElementPropertiesTableModelBuilder;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.model.MeanLeaf;
import org.prowim.portal.models.tree.model.ProcessElementsFolder;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ResultsMemoryLeaf;
import org.prowim.portal.models.tree.model.RoleLeaf;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.update.UpdateNotificationCollection;
import org.prowim.portal.update.UpdateRegistry.EntityType;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.utils.GlobalFunctions;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.process.CommonTreeView;
import org.prowim.portal.view.process.ModelEditorView;



/**
 * The menuBar view included three sections. The first section, a tree, shows the attributes of selected {@link ProcessElement}s. <br>
 * The second section shows the actual {@link Process} in a tree, which included the all {@link ProcessElement}s, which are in this {@link Process}. <br>
 * The third section shows all {@link ProcessElement}s, which existing global in system and the existing {@link KnowledgeObject}s. <br>
 * 
 * 
 * To use this view create an instance of this and then call createPartControl and set the container for this. <br>
 * To initialize the view call {@link MenuBarView#initTrees(String, String)}, in which the first String is the id and the second String is the name of <br>
 * the given {@link Process}.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0 a9
 */
public class MenuBarView extends DefaultView
{
    private CommonTreeView repositoryView, globalElemView;

    /** Tree leaf to shows global elements */
    private DefaultLeaf    globalElemRoot = null;

    private String         modelID        = null;
    private String         modelName      = null;

    private DefaultTable   attributeTable;

    /**
     * Default constructor. This is necessary to create a instance of this view.
     * 
     */
    public MenuBarView()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        registerUpdateListener(parent);
        SashForm rightSashForm = new SashForm(parent, SWT.VERTICAL);
        rightSashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        attributeTable = createAttributArea(rightSashForm);

        createDownSide(rightSashForm);

        rightSashForm.setWeights(new int[] { 2, 8 });

        globalElemRoot = new DefaultLeaf("", "");

    }

    /**
     * Initialize the attribute area for a selected item in result table.
     * 
     * @param infoComp
     */
    private DefaultTable createAttributArea(Composite infoComp)
    {
        Group attributeGroup = new Group(infoComp, SWT.SHADOW_IN | SWT.RIGHT);
        attributeGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        attributeGroup.setLayout(new GridLayout());
        attributeGroup.setText(Resources.Frames.Global.Texts.ATTRIBUTES.getText());

        ScrolledComposite attScrollComp = new ScrolledComposite(attributeGroup, SWT.NONE);
        attScrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        attScrollComp.setLayout(new GridLayout());
        attScrollComp.setExpandHorizontal(true);
        attScrollComp.setExpandVertical(true);

        DefaultTable attributeTable = createAttributTable(attScrollComp);
        attScrollComp.setContent(attributeTable);
        return attributeTable;
    }

    /**
     * Create the down side area of menu bar.
     * 
     * @param rightSashForm
     */
    private void createDownSide(SashForm rightSashForm)
    {
        Composite container = new Composite(rightSashForm, SWT.NONE);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout gl = new GridLayout(1, true);
        gl.horizontalSpacing = 0;
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        container.setLayout(gl);

        SashForm sashForm = new SashForm(container, SWT.VERTICAL);
        sashForm.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        repositoryView = createRepositoryView(sashForm);

        globalElemView = createGlobalElementsView(sashForm);

        sashForm.setWeights(new int[] { 5, 5 });
    }

    /**
     * Create the repository area. This include the CreateTreeView Class.
     * 
     * @param rightSashForm
     */
    private CommonTreeView createRepositoryView(Composite container)
    {
        Group repositoryGroup = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        repositoryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        repositoryGroup.setLayout(new GridLayout());
        repositoryGroup.setText(Resources.Frames.Global.Texts.ACTUAL_PROSSES.getText());

        CommonTreeView treeView = new CommonTreeView(globalElemRoot);
        treeView.createPartControl(repositoryGroup);

        return treeView;
    }

    /**
     * 
     * create and fill result table with data
     * 
     * @return DefaultTable
     */
    private DefaultTable createAttributTable(final ScrolledComposite searchAttribut)
    {
        DefaultTableModel resultMode = new DefaultTableModel();
        DefaultTable attributeTable = new DefaultTable(searchAttribut, resultMode, SWT.SINGLE);
        attributeTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        return attributeTable;
    }

    /**
     * Create the repository area. This include the CreateTreeView Class.
     * 
     * @param rightSashForm
     */
    private CommonTreeView createGlobalElementsView(Composite container)
    {
        Group repositoryGroup = new Group(container, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        repositoryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        repositoryGroup.setLayout(new GridLayout());
        repositoryGroup.setText(Resources.Frames.Global.Texts.ALL_PROSSES_ELEMETS.getText());

        CommonTreeView treeView = new CommonTreeView(globalElemRoot);
        treeView.createPartControl(repositoryGroup);

        return treeView;
    }

    /**
     * 
     * Create and fill the trees in menu bar.
     * 
     * @param id id of model. This is normally the {@link Process} id. Not null.
     * @param name Name of model. This is normally the name of {@link Process}.
     */
    public void initTrees(String id, String name)
    {
        Validate.notNull(id);
        Validate.notNull(name);
        this.modelID = id;
        this.modelName = name;

        // fill repository tree
        initProcessElementTree();

        // fill global Elements tree
        // fill knowledge objects
        initKnowTree();

        // fill role tree
        initRoleTree();

        // fill global means
        initMeanTree();

        // fill global depots
        initResMemoryTree();

        // reload tree
        globalElemView.reloadTree(globalElemRoot);
    }

    /**
     * Initialize the tree to show all global result memories.
     */
    private void initResMemoryTree()
    {
        ObjectArray objectArray = MainController.getInstance().getGlobalResultsMem();
        ProcessElementsFolder processResMenModel = new ProcessElementsFolder(GlobalConstants.RESULTS_MEMORY,
                                                                             GlobalFunctions.getTypeLabel(GlobalConstants.RESULTS_MEMORY));
        Iterator<Object> resMemIt = objectArray.iterator();
        while (resMemIt.hasNext())
        {
            ResultsMemoryLeaf resMemModel = new ResultsMemoryLeaf((ResultsMemory) resMemIt.next());
            processResMenModel.addChild(resMemModel);
        }

        globalElemRoot.addChild(processResMenModel);
    }

    /**
     * Initialize the tree to show all means.
     */
    private void initMeanTree()
    {
        ObjectArray objectArray = MainController.getInstance().getGlobalMeans();

        ProcessElementsFolder processMeanModel = new ProcessElementsFolder(GlobalConstants.MEAN, GlobalFunctions.getTypeLabel(GlobalConstants.MEAN));
        Iterator<Object> meanIt = objectArray.iterator();
        while (meanIt.hasNext())
        {
            MeanLeaf meanModel = new MeanLeaf((Mean) meanIt.next());
            processMeanModel.addChild(meanModel);
        }

        globalElemRoot.addChild(processMeanModel);
    }

    /**
     * Initialize the tree to show all global roles.
     */
    private void initRoleTree()
    {
        RoleArray roleArray = MainController.getInstance().getGlobalRoles();

        ProcessElementsFolder processRoleModel = new ProcessElementsFolder(GlobalConstants.ROLE, GlobalFunctions.getTypeLabel(GlobalConstants.ROLE));

        Iterator<Role> roleIt = roleArray.iterator();
        while (roleIt.hasNext())
        {
            RoleLeaf roleModel = new RoleLeaf(roleIt.next());
            roleModel = new KnowledgeStructureController().getRolePreSelctList(roleModel);
            processRoleModel.addChild(roleModel);
        }
        globalElemRoot.addChild(processRoleModel);
    }

    /**
     * Initialize the tree to show all knowledge objects.
     */
    private void initKnowTree()
    {
        KnowledgeStructureController controller = new KnowledgeStructureController();
        globalElemRoot = controller.getKnowledgeObjectRootData(true);
    }

    /**
     * Initializes the tree which shows the process elements of the current process.
     */
    private void initProcessElementTree()
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        ProcessStructureLeaf processModel = new ProcessStructureLeaf(this.modelID, this.modelName);
        root.addChild(new KnowledgeStructureController().getProcessStructureNode(processModel));
        repositoryView.reloadTree(root);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#getUpdateListenerType()
     */
    @Override
    protected EnumSet<EntityType> getUpdateListenerType()
    {
        return EnumSet.of(EntityType.PROCESS, EntityType.KNOWLEDGE, EntityType.ROLE, EntityType.MEAN, EntityType.RESULTSMEMORY, EntityType.ACTIVITY);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#handleUpdateEvents(org.prowim.portal.update.UpdateNotificationCollection)
     */
    @Override
    public void handleUpdateEvents(UpdateNotificationCollection updates)
    {
        super.handleUpdateEvents(updates);

        if (this.modelID != null)
            initTrees(this.modelID, MainController.getInstance().getName(modelID));
    }

    /**
     * getAttributeTable
     * 
     * @return the attributeTable. may be null if called before {@link #createPartControl(Composite)}
     */
    public DefaultTable getAttributeTable()
    {
        return attributeTable;
    }

    /**
     * Displays attributes of process elements in attributes-table of {@link ModelEditorView} for given modelId.
     * 
     * @param modelId must not be null
     * @param elementID must not be null
     */
    public static void showProcessElementAttributes(String modelId, String elementID)
    {
        for (IViewReference view : PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences())
        {
            if (view.getId().equals(ModelEditorView.ID) && modelId.equals(view.getSecondaryId()))
            {

                ModelEditorView modelEditorView = (ModelEditorView) view.getView(false);
                if (modelEditorView != null)
                {
                    DefaultTable table = modelEditorView.getMenuBar().getAttributeTable();

                    if (table != null)
                    {
                        ElementPropertiesTableModelBuilder controller = new ElementPropertiesTableModelBuilder(elementID);
                        table.setTableModel(new DefaultTableModel(controller.getTableModel(), controller.getColumns()));
                        table.redraw();
                        table.sortAtColumn(0);

                    }
                }
            }
        }
    }
}
