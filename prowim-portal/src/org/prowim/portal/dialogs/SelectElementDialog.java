/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.04.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.table.DefaultTableViewer;


/**
 * Shows a list of items to select or to create a new item. This uses by elements, which are re-usable in model editor. <br>
 * Those are e.g. roles, means (Mittel), depots(Ablage) or products.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class SelectElementDialog extends DefaultDialog
{
    private Button                  checkboxCreateNew;
    private Button                  checkboxIsLocal;
    private final String[]          result             = new String[3];
    private boolean                 isCreateNewChecked = false;
    private boolean                 isIsLocalChecked   = false;
    private final DefaultTableModel selectionModel;                    // Model of table
    private final Action            actualAction;
    private Label                   labelIsLocal       = null;

    /**
     * Default constructor.
     * 
     * @param parentShell {@link Shell}. can be null.
     * @param action action to set properties of dialog. Not null.
     * @param description Description of dialog. not null.
     * @param model model to build the dialog. Not null.
     */
    public SelectElementDialog(Shell parentShell, Action action, String description, final DefaultTableModel model)
    {
        super(parentShell, action, description);
        this.selectionModel = model;
        this.actualAction = action;
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
        // the main composite.
        Composite control = super.createCustomArea(parent);
        (control).setLayout(new GridLayout(1, false));

        // Group for selection table
        Group selectionGroup = new Group(control, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        selectionGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        selectionGroup.setLayout(new GridLayout(1, false));
        String selectionLabel = "";
        if (this.actualAction.getId().equals("SELECT_CREATE_ROLE"))
            selectionLabel = Resources.Frames.Dialog.Texts.EXIST_ROLES.getText();
        else if (this.actualAction.getId().equals("SELECT_CREATE_MEAN"))
            selectionLabel = Resources.Frames.Dialog.Texts.EXIST_MEANS.getText();
        else if (this.actualAction.getId().equals("SELECT_CREATE_RESULTS_MEM"))
            selectionLabel = Resources.Frames.Dialog.Texts.EXIST_RESULTS_MEM.getText();

        selectionGroup.setText(selectionLabel);

        ScrolledComposite scrollComp = new ScrolledComposite(selectionGroup, SWT.NONE);
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, true);
        gridData.heightHint = 150;
        scrollComp.setLayoutData(gridData);
        scrollComp.setExpandHorizontal(true);
        scrollComp.setExpandVertical(true);
        

        final DefaultTable selectionTable = new DefaultTable(scrollComp, selectionModel, SWT.SINGLE | SWT.V_SCROLL);
        selectionTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        selectionTable.sortAtColumn(0);
        selectionTable.setSize(80, 250);

        if (selectionTable.getItemCount() > 0)
        {
            selectionTable.setSelection(0);
            result[0] = ((ProcessElement) selectionTable.getItem(0).getData("0")).getID();
            result[1] = ((ProcessElement) selectionTable.getItem(0).getData("0")).getName();
            result[2] = ((ProcessElement) selectionTable.getItem(0).getData("0")).getDescription();
        }

        selectionTable.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                TableItem item = selectionTable.getItem(selectionTable.getSelectionIndex());
                ProcessElement processElement = (ProcessElement) item.getData("0");
                result[0] = processElement.getID();
                result[1] = processElement.getName();
                String description = processElement.getDescription();
                if (description != null)
                    result[2] = processElement.getDescription();
                else
                    result[2] = "";
            }
        });

        scrollComp.setContent(selectionTable);

        // Group for creating new area
        Group createNewGroup = new Group(control, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        createNewGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        createNewGroup.setLayout(new GridLayout(2, false));

        String createNewLabel = "";
        if (this.actualAction.getId().equals("SELECT_CREATE_ROLE"))
            createNewLabel = Resources.Frames.Dialog.Actions.CREATE_NEW_ROLE.getLabelText();
        else if (this.actualAction.getId().equals("SELECT_CREATE_MEAN"))
            createNewLabel = Resources.Frames.Dialog.Actions.CREATE_NEW_MEAN.getLabelText();
        else if (this.actualAction.getId().equals("SELECT_CREATE_RESULTS_MEM"))
            createNewLabel = Resources.Frames.Dialog.Actions.CREATE_NEW_RESULTS_MEM.getLabelText();

        createNewGroup.setText(createNewLabel);

        checkboxCreateNew = new Button(createNewGroup, SWT.CHECK);
        checkboxCreateNew.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                isCreateNewChecked = checkboxCreateNew.getSelection();

                if (checkboxCreateNew.getSelection())
                {
                    selectionTable.setEnabled(false);
                    if ( !actualAction.getId().equals("SELECT_CREATE_MEAN") && !actualAction.getId().equals("SELECT_CREATE_RESULTS_MEM"))
                    {
                        checkboxIsLocal.setEnabled(true);
                        labelIsLocal.setEnabled(true);
                    }
                }
                else
                {
                    selectionTable.setEnabled(true);
                    checkboxIsLocal.setEnabled(false);
                    labelIsLocal.setEnabled(false);
                }
            }
        });

        Label createNewLbl = new Label(createNewGroup, SWT.NONE);

        String selectedText = "";
        if (this.actualAction.getId().equals("SELECT_CREATE_ROLE"))
            selectedText = Resources.Frames.Dialog.Actions.CREATE_NEW_ROLE.getTooltip();
        else if (this.actualAction.getId().equals("SELECT_CREATE_MEAN"))
            selectedText = Resources.Frames.Dialog.Actions.CREATE_NEW_MEAN.getTooltip();
        else if (this.actualAction.getId().equals("SELECT_CREATE_RESULTS_MEM"))
            selectedText = Resources.Frames.Dialog.Actions.CREATE_NEW_RESULTS_MEM.getTooltip();

        createNewLbl.setText(selectedText);

        createLocalbutton(createNewGroup);

        return control;
    }

    /**
     * 
     * createLocalbutton.
     * 
     * @param createNewGroup
     */
    private void createLocalbutton(Group createNewGroup)
    {
        checkboxIsLocal = new Button(createNewGroup, SWT.CHECK);
        checkboxIsLocal.setEnabled(false);
        checkboxIsLocal.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                isIsLocalChecked = checkboxIsLocal.getSelection();
            }
        });

        labelIsLocal = new Label(createNewGroup, SWT.NONE);
        labelIsLocal.setEnabled(false);
        labelIsLocal.setText(Resources.Frames.Dialog.Texts.SET_AS_LOCAL.getText());
    }

    /**
     * 
     * Returns the result of the selection. If user selects a item then result is
     * 
     * <li>result[0]</li> is the id of element <br>
     * <li>result[1]</li> is the name of element and <br>
     * <li>result[2]</li> is the description of element.
     * 
     * If user want to creates a new element then <br>
     * <li>result[0]</li> is GlobalConstants.LOCAL_ELEMENT or GlobalConstants.GLOBAL_ELEMENT <br>
     * depends of users selection for local or global
     * 
     * 
     * @return A non null String array.
     */
    public String[] getResults()
    {
        return this.result;
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

        if (isCreateNewChecked)
        {
            // Asks if the new element is a local or global element
            if (isIsLocalChecked)
                result[0] = GlobalConstants.LOCAL_ELEMENT;
            else
                result[0] = GlobalConstants.GLOBAL_ELEMENT;
        }

        close();
    }
}
