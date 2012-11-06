/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:29 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/SelectOrganizationDialog.java $
 * $LastChangedRevision: 4960 $
 *------------------------------------------------------------------------------
 * (c) 19.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */
package org.prowim.portal.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.portal.controller.dialog.OrgaController;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DefaultTable;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.validation.ErrorState;
import org.prowim.rap.framework.validation.Validator;



/**
 * This dialog help you to select a organization from a list, which are in this system.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4960 $
 */
public final class SelectOrganizationDialog extends DefaultDialog
{
    private List<Organization>      selectedOrgnization = new ArrayList<Organization>();

    private DefaultTable            selectedOrgTable;
    private DefaultTable            orgTable;
    private final DefaultTableModel organizationModel;

    private ScrolledComposite       scrollAddComp;
    private ScrolledComposite       scrollComp;
    private final DefaultConstraint constraint;

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog. Not null.
     * @param description see {@link DefaultDialog}
     * @param model List of Organization to show in left site of dialog. Not null.
     * @param preSelectedOrganuzation List of preselected organizations to show in the right table of dialog.
     * @param defaultConstraint Included the properties min, max and required. Not null.
     */
    public SelectOrganizationDialog(final Shell parentShell, final Action action, String description, final DefaultTableModel model,
            final List<Organization> preSelectedOrganuzation, final DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(model, "model can not be null");
        Validate.notNull(preSelectedOrganuzation, "preSelectedOrganuzation can not be null");
        Validate.notNull(defaultConstraint, "defaultConstraint can not be null");

        this.organizationModel = model;
        this.selectedOrgnization = preSelectedOrganuzation;
        this.constraint = defaultConstraint;
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
        (control).setLayout(new GridLayout(3, false));

        // Container for left table
        Composite leftContainer = new Composite(control, SWT.NONE);
        leftContainer.setLayoutData(new GridData(GridData.FILL_BOTH));
        leftContainer.setLayout(new GridLayout(1, false));

        // Group for left table
        Group allOrgaGroup = new Group(leftContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        allOrgaGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
        allOrgaGroup.setLayout(new GridLayout(1, false));
        allOrgaGroup.setText(Resources.Frames.Dialog.Texts.ALL_ORGS.getText());

        scrollComp = new ScrolledComposite(allOrgaGroup, SWT.NONE);
        scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrollComp.setLayout(new GridLayout());
        scrollComp.setExpandHorizontal(true);
        scrollComp.setExpandVertical(false);
        scrollComp.setSize(100, 100);

        orgTable = new DefaultTable(scrollComp, this.organizationModel, SWT.SINGLE);
        orgTable.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        orgTable.sortAtColumn(0);
        orgTable.redraw();
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(orgTable);

        scrollComp.setContent(orgTable);
        orgTable.pack();

        orgTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                addItem();
            }
        });

        // Create move buttons
        Composite middleContainer = new Composite(control, SWT.NONE);
        middleContainer.setLayout(new GridLayout(1, false));

        // Create add button
        Button addButton = new Button(middleContainer, SWT.PUSH);
        addButton.setImage(Resources.Frames.Dialog.Actions.ADD_USER_TO_LIST.getImage());
        addButton.setToolTipText(Resources.Frames.Dialog.Actions.ADD_USER_TO_LIST.getTooltip());
        addButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                addItem();
            }
        });

        // Create delete button
        Button deleteButton = new Button(middleContainer, SWT.PUSH);
        deleteButton.setImage(Resources.Frames.Dialog.Actions.DELETE_USER_FROM_LIST.getImage());
        deleteButton.setToolTipText(Resources.Frames.Dialog.Actions.DELETE_USER_FROM_LIST.getTooltip());
        deleteButton.addSelectionListener(new SelectionAdapter()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                deleteItem();
            }
        });

        // Create right container
        Composite rightContainer = new Composite(control, SWT.NONE);
        rightContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        rightContainer.setLayout(new GridLayout(1, false));

        // Create group for right table
        Group selectOrg = new Group(rightContainer, SWT.SHADOW_OUT | SWT.TOP | SWT.H_SCROLL | SWT.V_SCROLL);
        selectOrg.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        selectOrg.setLayout(new GridLayout(1, false));
        selectOrg.setText(Resources.Frames.Dialog.Texts.SELECTED_ORGS.getText());
        selectOrg.pack();

        scrollAddComp = new ScrolledComposite(selectOrg, SWT.NONE);
        scrollAddComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        scrollAddComp.setLayout(new GridLayout());
        scrollAddComp.setExpandHorizontal(true);
        scrollAddComp.setExpandVertical(false);
        scrollAddComp.setSize(100, 100);
        scrollAddComp.pack();

        // Create right table
        createRightTable();

        return control;
    }

    // Add a item from selectable list in selected list
    private void addItem()
    {
        if (orgTable.getSelectionIndex() > -1)
        {
            TableItem item = orgTable.getItem(orgTable.getSelectionIndex());
            Organization orga = (Organization) item.getData("0");
            addOrgToList(orga);
            selectedOrgTable.dispose();
            // Create right table
            createRightTable();
        }
    }

    // Delete a item from selected list in selectable list
    private void deleteItem()
    {
        if (selectedOrgTable.getSelectionIndex() > -1)
        {
            TableItem item = selectedOrgTable.getItem(selectedOrgTable.getSelectionIndex());
            Organization orga = (Organization) item.getData("0");
            selectedOrgnization.remove(orga);
            selectedOrgTable.remove(selectedOrgTable.getSelectionIndex());
            createLeftTable();
        }
    }

    /**
     * 
     * This method create the table of the right site. It including selected organizations.
     */
    private void createRightTable()
    {
        OrgaController controller = new OrgaController();
        DefaultTableModel orgaModel = new DefaultTableModel(controller.getTableModel(selectedOrgnization), controller.getColumns());

        this.selectedOrgTable = new DefaultTable(this.scrollAddComp, orgaModel, SWT.SINGLE);
        this.selectedOrgTable.sortAtColumn(0);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, true)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(selectedOrgTable);

        scrollAddComp.setContent(this.selectedOrgTable);
        this.selectedOrgTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                deleteItem();
            }
        });
    }

    /**
     * 
     * This method create the table of the right site. It including selected organizations.
     */
    private void createLeftTable()
    {
        OrgaController controller = new OrgaController();
        DefaultTableModel orgaModel = new DefaultTableModel(controller.getTableModelUnique(selectedOrgnization), controller.getColumns());

        this.orgTable = new DefaultTable(this.scrollComp, orgaModel, SWT.SINGLE);
        this.orgTable.sortAtColumn(0);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, true)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 3), SWT.DEFAULT).applyTo(orgTable);

        scrollComp.setContent(this.orgTable);
        this.orgTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseDoubleClick(MouseEvent e)
            {
                deleteItem();
            }
        });
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
        if (constraint != null)
        {
            ErrorState verified = new ErrorState();
            // Get Validator to valid values
            final Validator validator = new Validator(constraint);

            verified.setErrorState(validator.checkList(selectedOrgnization.size()));
            if ( !verified.hasError())
                close();
            else
                InformationDialog.openInformation(null, Resources.Frames.Dialog.Texts.VALIDATION_INFO_TEXT.getText() + "\n"
                        + Resources.Frames.Global.Texts.MINIMUM.getText() + GlobalConstants.DOUBLE_POINT + " " + constraint.getMin() + "\n"
                        + Resources.Frames.Global.Texts.MAXIMUM.getText() + GlobalConstants.DOUBLE_POINT + " " + constraint.getMax() + "\n");
        }
        else
            close();

    }

    /**
     * 
     * This method actualize the outgoing object, which included all organization, that should be returned.
     * 
     * @param orga Organization, which should including
     */
    private void addOrgToList(Organization orga)
    {
        boolean orgExist = false;
        Iterator<Organization> orgIt = selectedOrgnization.iterator();
        while (orgIt.hasNext())
        {
            if (orgIt.next().getID().equals(orga.getID()))
            {
                orgExist = true;
                break;
            }
        }
        if ( !orgExist)
        {
            selectedOrgnization.add(orga);
            orgTable.remove(orgTable.getSelectionIndex());
            orgTable.redraw();
        }
    }

    /**
     * 
     * Returns a list of {@link Organization}, which are in this dialog selected.
     * 
     * @return {@link List} of {@link Organization}s. Not null. If no item selected, returns a empty list.
     */
    public List<Organization> getSelectedOrganizations()
    {
        return this.selectedOrgnization;
    }
}
