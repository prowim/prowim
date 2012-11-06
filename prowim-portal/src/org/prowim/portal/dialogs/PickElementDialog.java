/*==============================================================================
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 09.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefTreeViewer;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.impl.ElementSorter;
import org.prowim.portal.models.tree.impl.ProcessCategorieFilter;
import org.prowim.portal.models.tree.impl.SwimlaneDlgFilter;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.DomainFolder;
import org.prowim.portal.models.tree.model.DomainLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.MeanLeaf;
import org.prowim.portal.models.tree.model.OrganizationLeaf;
import org.prowim.portal.models.tree.model.OrganizationUnitsLeaf;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.models.tree.model.ProcessCategoryLeaf;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeRootLeaf;
import org.prowim.portal.models.tree.model.ResultsMemoryLeaf;
import org.prowim.portal.models.tree.model.RoleLeaf;
import org.prowim.portal.models.tree.model.RolesFolder;
import org.prowim.portal.models.tree.model.SwimlaneLeaf;
import org.prowim.portal.utils.GlobalConstants;



/**
 * User can give a default leaf. Independent of given leaf user became a tree, where he can use a leaf.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class PickElementDialog extends DefaultDialog
{
    private final List<Class> selectableLeafsForKnowledge = new ArrayList<Class>();
    private final List<Class> reloadLeafs                 = new ArrayList<Class>();

    private Group             treeGroup;
    private TreeViewer        treeViewer;

    private DefaultLeaf       incomingLeaf                = null;
    private DefaultLeaf       outgoingLeaf                = null;
    private DefaultLeaf       processLeaf                 = null;
    private DefaultLeaf       domainLeaf                  = null;

    /**
     * Constructor.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param leaf leaf, which in it should user choose a element
     */
    public PickElementDialog(Shell parentShell, Action action, String description, DefaultLeaf leaf)
    {
        super(parentShell, action, description);

        this.incomingLeaf = leaf;
        setSelectableClasses();
        setReloadLeafs();
    }

    private void setSelectableClasses()
    {
        selectableLeafsForKnowledge.add(ProcessStructureLeaf.class);
        selectableLeafsForKnowledge.add(DomainLeaf.class);
        selectableLeafsForKnowledge.add(ActivityLeaf.class);
        selectableLeafsForKnowledge.add(KnowledgeObjectLeaf.class);
        selectableLeafsForKnowledge.add(RoleLeaf.class);
        selectableLeafsForKnowledge.add(PersonLeaf.class);
        selectableLeafsForKnowledge.add(MeanLeaf.class);
        selectableLeafsForKnowledge.add(ResultsMemoryLeaf.class);
        selectableLeafsForKnowledge.add(ProcessTypeEditorLeaf.class);
        selectableLeafsForKnowledge.add(OrganizationUnitsLeaf.class);
        selectableLeafsForKnowledge.add(OrganizationLeaf.class);
        selectableLeafsForKnowledge.add(ProcessCategoryLeaf.class);
        selectableLeafsForKnowledge.add(DomainFolder.class);
    }

    private void setReloadLeafs()
    {
        reloadLeafs.add(PersonLeaf.class);
        reloadLeafs.add(OrganizationUnitsLeaf.class);
        reloadLeafs.add(ProcessCategoryLeaf.class);
        reloadLeafs.add(DomainFolder.class);
        reloadLeafs.add(ProcessTypeLeaf.class);
        reloadLeafs.add(ProcessTypeRootLeaf.class);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createCustomArea(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Composite createCustomArea(Composite parent)
    {
        Composite control = super.createCustomArea(parent);

        if ((this.incomingLeaf instanceof KnowledgeObjectLeaf))
        {
            createSelectGroup(control);
        }

        createTreeGoup(control);

        if (this.incomingLeaf instanceof KnowledgeObjectLeaf)
        {
            initTree(new KnowledgeStructureController().getProcessElementRootData(false));
        }
        else if ((this.incomingLeaf instanceof PersonLeaf) || (this.incomingLeaf instanceof RoleLeaf)
                || (this.incomingLeaf instanceof OrganizationLeaf))
        {
            DefaultLeaf root = new DefaultLeaf("", "");
            root.addChild(new KnowledgeStructureController().getSubNode(new OrganizationUnitsLeaf()));
            initTree(root);
        }
        else if (this.incomingLeaf instanceof SwimlaneLeaf)
        {
            DefaultLeaf root = new DefaultLeaf("", "");
            root.addChild(new KnowledgeStructureController().getSubNode(new OrganizationUnitsLeaf()));

            RolesFolder roles = new RolesFolder();

            RoleArray roleArray = MainController.getInstance().getGlobalRoles();
            Iterator<Role> roleIt = roleArray.iterator();
            while (roleIt.hasNext())
            {
                RoleLeaf roleModel = new RoleLeaf(roleIt.next());
                roles.addChild(roleModel);
            }

            root.addChild(roles);

            initTree(root);

            this.treeViewer.addFilter(new SwimlaneDlgFilter());
        }
        else if (this.incomingLeaf instanceof ProcessTypeRootLeaf)
        {
            DefaultLeaf root = new DefaultLeaf("", "");
            root.addChild(new KnowledgeStructureController().getTopProcessTypes());

            initTree(root);

            this.treeViewer.addFilter(new ProcessCategorieFilter());
        }
        else if (this.incomingLeaf instanceof ProcessEditorLeaf)
        {
            initTree(new KnowledgeStructureController().getProcessTypeRootLeaf(false));
        }

        return control;
    }

    /**
     * Create the selection area. The user can then select between different trees to select his item.
     * 
     * @param control
     */
    private void createSelectGroup(Control control)
    {
        Group knowLinkGroup = new Group((Composite) control, SWT.SHADOW_IN | SWT.RIGHT);
        knowLinkGroup.setText(Resources.Frames.Dialog.Texts.ADD_ITEM_TO_A_OBJECT.getText());
        knowLinkGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        knowLinkGroup.setLayout(new GridLayout(1, false));

        Composite composite = new Composite(knowLinkGroup, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(2, false));

        if (this.incomingLeaf instanceof KnowledgeObjectLeaf)
            createKnowledgeBtns(composite);

    }

    /**
     * Create selection buttons if a knowledge model is selected.
     * 
     * @param composite
     */
    private void createKnowledgeBtns(Composite composite)
    {
        Button procBtn = new Button(composite, SWT.RADIO);
        procBtn.setText(Resources.Frames.Dialog.Texts.PROCESS_AND_ACTIVTY.getText());
        procBtn.setSelection(true);
        procBtn.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDown(MouseEvent e)
            {
                if (processLeaf == null)
                    initTree(new KnowledgeStructureController().getProcessElementRootData(false));
                else
                    initTree(processLeaf);

                processLeaf = (DefaultLeaf) treeViewer.getInput();
            }
        });

        Button domainBtn = new Button(composite, SWT.RADIO);
        domainBtn.setText(Resources.Frames.Knowledge.Texts.KNOW_DOMAINS.getText());
        domainBtn.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDown(MouseEvent e)
            {
                if (domainLeaf == null)
                    initTree(new KnowledgeStructureController().getDomainRootData(false));
                else
                    initTree(domainLeaf);

                domainLeaf = (DefaultLeaf) treeViewer.getInput();
            }
        });
    }

    /**
     * Create tree which shows the user a list with all sorts data, which he can select.
     * 
     * @param control
     */
    private void createTreeGoup(Control control)
    {
        treeGroup = new Group((Composite) control, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        treeGroup.setText(Resources.Frames.Dialog.Texts.SELECT_ITEM_IN_TREE.getText());
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.minimumHeight = 300;
        treeGroup.setLayoutData(gd);
        treeGroup.setLayout(new GridLayout());
        createTree();
    }

    /**
     * 
     * Create tree.
     */
    private void createTree()
    {
        treeViewer = new DefTreeViewer(treeGroup, SWT.MULTI);
        treeViewer.expandToLevel(2);
        treeViewer.setSorter(new ElementSorter());
        createTreeListner();
    }

    /**
     * Initialize tree with given data.
     */
    private void initTree(final Object mainTree)
    {
        treeViewer.getTree().clearAll(true);
        treeViewer.setInput(mainTree);
    }

    /**
     * createProcTreeListner.
     * 
     * @param wobController ProcessWOBController
     */
    private void createTreeListner()
    {
        final KnowledgeStructureController wobController = new KnowledgeStructureController();
        // Selection listener
        this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            /**
             * {@inheritDoc}
             * 
             * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
             */
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {

                    outgoingLeaf = (DefaultLeaf) ((IStructuredSelection) s).getFirstElement();

                    if (outgoingLeaf != null)
                    {
                        if (outgoingLeaf instanceof DomainLeaf)
                        {
                            outgoingLeaf.setChildren(new ArrayList<Object>());

                            if (outgoingLeaf.getID().equals(GlobalConstants.ROOT_MODEL))
                                outgoingLeaf = (DefaultLeaf) outgoingLeaf.getChildren();
                            else
                                outgoingLeaf = wobController.getSubNode(outgoingLeaf);

                            treeViewer.refresh(outgoingLeaf);
                        }
                        else if (reloadLeafs.contains(outgoingLeaf.getClass()))
                        {
                            outgoingLeaf = wobController.getSubNode(outgoingLeaf);
                            treeViewer.refresh(outgoingLeaf);
                        }
                    }
                }
            }
        });
    }

    /**
     * 
     * Check, if the selected item is compatible with the given object.
     * 
     * @return boolean. true if compatible, else false
     */
    private boolean isSelectable()
    {
        DefaultLeaf templeaf = incomingLeaf;

        if (incomingLeaf instanceof KnowledgeObjectLeaf)
        {
            if (selectableLeafsForKnowledge.contains(outgoingLeaf.getClass()))
                return true;
            else
            {
                incomingLeaf = templeaf;
                return false;
            }
        }
        else if (incomingLeaf instanceof PersonLeaf)
        {
            if ((outgoingLeaf instanceof OrganizationLeaf))
                return true;
            else if (outgoingLeaf instanceof PersonLeaf)
            {
                TreePath[] path = treeViewer.getExpandedTreePaths();
                DefaultLeaf item = (DefaultLeaf) path[path.length - 1].getLastSegment();
                if (item instanceof OrganizationLeaf)
                {
                    outgoingLeaf = item;
                    return true;
                }
                else
                    return false;
            }
            else
            {
                incomingLeaf = templeaf;
                return false;
            }
        }
        else if (incomingLeaf instanceof RoleLeaf)
        {
            if ((outgoingLeaf instanceof OrganizationLeaf))
                return true;
            else
            {
                incomingLeaf = templeaf;
                return false;
            }
        }
        else if (incomingLeaf instanceof OrganizationLeaf)
        {
            if ((outgoingLeaf instanceof OrganizationLeaf) || ((outgoingLeaf instanceof OrganizationUnitsLeaf)))
            {
                return true;
            }
            else
            {
                incomingLeaf = templeaf;
                return false;
            }
        }
        else if (incomingLeaf instanceof SwimlaneLeaf)
        {
            if ((outgoingLeaf instanceof OrganizationLeaf) || ((outgoingLeaf instanceof RoleLeaf)))
            {
                return true;
            }
            else
            {
                incomingLeaf = templeaf;
                return false;
            }
        }

        else if ((incomingLeaf instanceof ProcessTypeRootLeaf)
                && ((outgoingLeaf instanceof ProcessTypeEditorLeaf) || (outgoingLeaf instanceof ProcessTypeLeaf)))
        {
            return true;
        }

        else if ((incomingLeaf instanceof ProcessEditorLeaf) && (outgoingLeaf instanceof ProcessEditorLeaf))
        {
            return true;
        }

        return false;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        setReturnCode(OK);
        if (isSelectable())
            close();
        else
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.INCOMPATIBLE_SELECTION.getAction().getToolTipText());

    }

    /**
     * 
     * ReEturns the selected leaf in tree
     * 
     * @return Returns the selected leaf. Null, if no items is selected.
     */
    public DefaultLeaf getSelectedLeaf()
    {
        return this.outgoingLeaf;
    }

}
