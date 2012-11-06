/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 07.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.OrganizationLeaf;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidateMail;
import org.prowim.rap.framework.components.impl.ValidatedTextArea;
import org.prowim.rap.framework.components.impl.ValidatedTextField;



/**
 * Dialog to create or edit a organization.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class OrganizationDialog extends DefaultDialog
{
    private ValidatedTextField nameTextField;
    private ValidatedTextField addressTextField;
    private ValidatedTextField phoneTextField;
    private ValidateMail       eMailTextField;
    private ValidatedTextArea  descriptionTextField;

    private String             id           = "";
    private String             name         = "";
    private String             address      = "";
    private String             mail         = "";
    private String             phone        = "";
    private String             description  = "";

    private Organization       organization = null;

    /**
     * Constructor of this dialog.
     * 
     * @param parentShell see {@link DefaultDialog}
     * @param action see {@link DefaultDialog}
     * @param description see {@link DefaultDialog}
     * @param organizationLeaf Model to build the dialog. Can be null if you want to create a new organization
     */
    public OrganizationDialog(Shell parentShell, Action action, String description, OrganizationLeaf organizationLeaf)
    {
        super(parentShell, action, description);

        if (organizationLeaf != null)
        {
            this.id = organizationLeaf.getID();
            this.name = organizationLeaf.getName();
            this.address = organizationLeaf.getAddress();
            this.mail = organizationLeaf.getEmail();
            this.phone = organizationLeaf.getTelefon();
            this.description = organizationLeaf.getDescription();
        }
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

        Composite composite = new Composite(control, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(2, false));

        Label nameLbl = new Label(composite, SWT.RIGHT);
        nameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        nameLbl.setText(Resources.Frames.Global.Texts.NAME.getText() + GlobalConstants.DOUBLE_POINT);

        nameTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true));
        nameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        nameTextField.setText(this.name);
        nameTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                name = nameTextField.getText();
            }
        });

        Label addressLbl = new Label(composite, SWT.RIGHT);
        addressLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        addressLbl.setText(Resources.Frames.Global.Texts.ADDRESS.getText() + GlobalConstants.DOUBLE_POINT);

        addressTextField = new ValidatedTextField(composite, new DefaultConstraint(0L, 1000L, false));
        addressTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addressTextField.setText(this.address);
        addressTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                address = addressTextField.getText();
            }
        });

        Label eMailLbl = new Label(composite, SWT.RIGHT);
        eMailLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        eMailLbl.setText(Resources.Frames.Global.Texts.MAIL.getText() + GlobalConstants.DOUBLE_POINT);

        eMailTextField = new ValidateMail(composite, new DefaultConstraint(0L, 1000L, false));
        eMailTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        eMailTextField.setText(this.mail);
        eMailTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                mail = eMailTextField.getText();
            }
        });

        Label phoneLbl = new Label(composite, SWT.RIGHT);
        phoneLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        phoneLbl.setText(Resources.Frames.Global.Texts.PHONE.getText() + GlobalConstants.DOUBLE_POINT);

        phoneTextField = new ValidatedTextField(composite, new DefaultConstraint(0L, 1000L, false));
        phoneTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        phoneTextField.setText(this.phone);
        phoneTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                phone = phoneTextField.getText();
            }
        });

        Label descLbl = new Label(composite, SWT.RIGHT);
        descLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        descLbl.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText() + GlobalConstants.DOUBLE_POINT);

        descriptionTextField = new ValidatedTextArea(composite, new DefaultConstraint(0L, 1000L, false));
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 3;
        gridData.heightHint = 100;
        gridData.widthHint = 100;
        descriptionTextField.setLayoutData(gridData);
        descriptionTextField.setText(this.description);
        descriptionTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                description = descriptionTextField.getText();
            }
        });

        return control;
    }

    /**
     * 
     * Checks the verifying of fields.
     * 
     * @return true, if all are OK, else false.
     */
    private boolean getVerifingFileds()
    {
        return nameTextField.isVerified();
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
        if (getVerifingFileds())
        {
            setValuesForOutput();
            super.okPressed();
        }
        else
            InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.REQUIRED_ERROR_DIALOG.getTooltip());

    }

    /**
     * 
     * Set values, which are necessary to handle after dialog is closed.
     */
    private void setValuesForOutput()
    {
        organization = DefaultDataObjectFactory.createOrganisation(this.id, this.name, "", this.address, this.mail, this.phone, this.description);
    }

    /**
     * Return organization..
     * 
     * @return returns the information of a {@link Organization}, which can be changed or new created by user. Null if dialog canceled.
     */
    public Organization getOrganization()
    {
        return this.organization;
    }

}
