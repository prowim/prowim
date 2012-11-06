/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
package org.prowim.rap.framework.components.impl;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.prowim.rap.framework.components.table.DefTableViewer;
import org.prowim.rap.framework.components.table.TableModel;
import org.prowim.rap.framework.i18n.Resources;



/**
 * This component allows the user to add or delete a item into a table. <br>
 * table can shows all objects.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0
 */
public class SelectItemsComp
{
    private Button         addBtn;
    private Button         deleteBtn;
    private DefTableViewer viewer;
    private Action         addAction    = null;
    private final Action   deleteAction = null;

    /**
     * This constructor of component.
     * 
     * @param parent Composite, where this component shows
     */
    public SelectItemsComp(Composite parent)
    {
        Group mainGroup = new Group(parent, SWT.SHADOW_IN);
        mainGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        mainGroup.setLayout(new GridLayout(1, false));
        mainGroup.setText("");

        // create Buttons
        createButtons(mainGroup);

        // create table
        createTabel(mainGroup);

    }

    /**
     * Description.
     * 
     * @param mainGroup
     */
    private void createButtons(Group mainGroup)
    {
        Composite btnComposite = new Composite(mainGroup, SWT.RIGHT);
        btnComposite.setLayoutData(new GridData(SWT.END, SWT.TOP, true, false));
        btnComposite.setLayout(new GridLayout(2, false));

        addBtn = new Button(btnComposite, SWT.PUSH);
        addBtn.setImage(Resources.Frames.Global.Actions.ITEM_ADD.getImage());
        addBtn.setToolTipText(Resources.Frames.Global.Actions.ITEM_ADD.getTooltip());
        addBtn.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (addAction != null)
                    addAction.runWithEvent(new Event());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

        deleteBtn = new Button(btnComposite, SWT.PUSH);
        deleteBtn.setImage(Resources.Frames.Global.Actions.ITEM_DELETE.getImage());
        deleteBtn.setToolTipText(Resources.Frames.Global.Actions.ITEM_DELETE.getTooltip());
        deleteBtn.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                if (deleteAction != null)
                    deleteAction.runWithEvent(new Event());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });

    }

    /**
     * Description.
     * 
     * @param mainGroup
     */
    private void createTabel(Group mainGroup)
    {
        viewer = new DefTableViewer(mainGroup, SWT.BORDER);
        viewer.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        viewer.addSelectionChangedListener(new ISelectionChangedListener()
        {
            @Override
            public void selectionChanged(SelectionChangedEvent event)
            {

            }
        });
    }

    /**
     * 
     * Set action for add button. This are image, tool tips and the action by select button
     * 
     * @param action Action
     */
    public void setAddBtnAction(Action action)
    {
        Validate.notNull(action);
        this.addAction = action;
    }

    /**
     * 
     * Set the model of table.
     * 
     * @param model Model to fill in table
     * @param labelProvider To sets the label of table
     */
    public void setMainModel(TableModel model, IBaseLabelProvider labelProvider)
    {
        viewer.setModel(model, labelProvider);
        viewer.sortAtIndex(0);
    }

    /**
     * 
     * Sets the label provider.
     * 
     * @param labelProvider Label provider for given table
     * 
     */
    public void setLableProvaider(IBaseLabelProvider labelProvider)
    {
        viewer.setLabelProvider(labelProvider);
    }

    /**
     * 
     * Sets a list to fill table.
     * 
     * @param list List of elements to fill table
     * 
     */
    public void setInput(List<Object> list)
    {
        viewer.setInput(list);
    }

}
