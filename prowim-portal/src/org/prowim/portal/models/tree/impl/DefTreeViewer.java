/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.models.tree.model.FolderLeaf;
import org.prowim.portal.models.tree.provider.ContentProvider;
import org.prowim.portal.models.tree.provider.TreeLabelProvider;
import org.prowim.rap.framework.resource.FontManager;



/**
 * Default tree viewer to show a model in. This should used instead of {@link TreeViewer} in Prowim.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class DefTreeViewer extends TreeViewer
{
    private final static int ACT_STYLE = SWT.SINGLE | SWT.VIRTUAL | SWT.H_SCROLL | SWT.V_SCROLL;

    /**
     * Constructor.
     * 
     * @param parent Composite to show view
     * @param style Style
     */
    public DefTreeViewer(Composite parent, int style)
    {
        super(parent, parent.getStyle() | ACT_STYLE);

        getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        setContentProvider(new ContentProvider());
        setLabelProvider(new TreeLabelProvider());
        getTree().setFont(FontManager.FONT_VERDANA_12_NORMAL);
        refresh();
        setComparator(new ViewerComparator());

        // Double click listener
        addDoubleClickListener(new IDoubleClickListener()
        {
            @Override
            public void doubleClick(DoubleClickEvent event)
            {
                ISelection s = event.getSelection();
                if (s instanceof IStructuredSelection)
                {
                    Object item = ((IStructuredSelection) s).getFirstElement();
                    if (getExpandedState(item))
                        collapseToLevel(item, 1);
                    else
                        expandToLevel(item, 1);
                }
            }
        });
    }

    /**
     * 
     * Loads a model in the tree and expand the tree to the positions as before loading, if the flag is sets.
     * 
     * @param input the input of this viewer, or null if none
     * 
     * @param flag <code>true</code> if expanding the tree in same position as before after setInput , else <code>false</code>
     */
    public void setInputAndExpand(final Object input, final boolean flag)
    {
        // get expanded elements
        Object[] expandedElements = getExpandedElements();

        setInput(input);

        if (flag)
        {
            // Expand the tree as before
            expandTree(expandedElements);
        }
    }

    /**
     * 
     * Get the parent path of the given item. The item is id of a {@link DefaultLeaf}.
     * 
     * @param itemID id of a {@link DefaultLeaf} to search in a tree. Not null.
     * @return {@link TreePath} Parents of given item. It can be more than one parent for given item. Not null.
     */
    public List<DefaultLeaf> getParentItemPath(final String itemID)
    {
        List<DefaultLeaf> itemList = new ArrayList<DefaultLeaf>();

        TreeItem[] items = getTree().getItems();

        for (TreeItem treeItem : items)
        {
            List<TreeItem> findItemInTree = findItemInTree(treeItem, itemID);
            for (TreeItem treeItem2 : findItemInTree)
            {
                TreeItem parentItem = treeItem2.getParentItem();
                if (parentItem != null)
                {
                    if ((((DefaultLeaf) parentItem.getData()) instanceof FolderLeaf))
                        parentItem = parentItem.getParentItem();

                    itemList.add((DefaultLeaf) parentItem.getData());
                }
            }
        }

        return itemList;
    }

    /**
     * 
     * Search in given {@link TreeItem} for given item and returns a list of {@link TreeItem}s, which includes the item. The method is recursive.
     * 
     * @param treeItem To searching tree
     * @param item item to search. Not null. In this case it can be a id of {@link DefaultLeaf} or a {@link DefaultLeaf}
     * @return Not null List of {@link TreeItem}s. Can be empty.
     */
    private List<TreeItem> findItemInTree(final TreeItem treeItem, final String item)
    {
        Validate.notNull(item, "Item to search in a tree can not be null");

        List<TreeItem> treeItems = new ArrayList<TreeItem>();
        int pos = searchForItem(treeItem, item);
        if (pos >= 0)
        {
            treeItems.add(treeItem);
        }
        else
        {
            int numChildren = treeItem.getItemCount();
            for (int i = 0; i < numChildren; ++i)
            {
                treeItems.addAll(findItemInTree(treeItem.getItem(i), item));
            }
        }

        return treeItems;
    }

    /**
     * 
     * Search, if the given object exists in the given tree.
     * 
     * @param arg0 {@link TreeItem} to search in.
     * @param arg1 item id, which we search
     * @return 0 if id find, else -1
     */
    private int searchForItem(TreeItem arg0, String arg1)
    {
        Validate.notNull(arg0, "Tree item can not be null");
        Validate.notNull(arg1, "Item id can not be null");

        if ((arg0).getData() instanceof DefaultLeaf)
        {
            DefaultLeaf str1 = (DefaultLeaf) (arg0).getData();
            String str2 = arg1;

            if (str1 != null && str1.getID().equals(str2))
                return 0;
            else
                return -1;
        }
        else
            return -1;
    }

    /**
     * 
     * Search given ID of a node in tree and reload this.
     * 
     * @param leafID ID of {@link DefaultLeaf}
     */
    public void reloadTreeNode(String leafID)
    {
        if (leafID != null && !leafID.trim().isEmpty())
        {
            KnowledgeStructureController knowledgeStructureController = new KnowledgeStructureController();
            List<DefaultLeaf> itemPath = getParentItemPath(leafID);
            for (DefaultLeaf defaultLeaf : itemPath)
            {
                // get expanded elements
                Object[] expandedElements = getExpandedElements();

                refresh(knowledgeStructureController.getSubNode(defaultLeaf));

                expandTree(expandedElements);
            }
        }
    }

    /**
     * Expands tree for given elements.
     * 
     * @param expandedElements list of elements, which are expanded and should set expand again
     */
    public void expandTree(Object[] expandedElements)
    {
        expandAll();
        Object[] elem1 = getExpandedElements();
        collapseAll();

        for (int i = 0; i < expandedElements.length; i++)
        {
            for (int j = 0; j < elem1.length; j++)
            {
                if (((DefaultLeaf) expandedElements[i]).getID().equals(((DefaultLeaf) elem1[j]).getID()))
                {
                    expandToLevel(elem1[j], 1);
                }
            }
        }
    }
}
