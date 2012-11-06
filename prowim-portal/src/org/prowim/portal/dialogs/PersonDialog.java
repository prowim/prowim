/*==============================================================================
 * File $Id$
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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.prowim.datamodel.prowim.DefaultDataObjectFactory;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.controller.dialog.OrgaController;
import org.prowim.portal.dialogs.feedback.InformationDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidateMail;
import org.prowim.rap.framework.components.impl.ValidatedTextArea;
import org.prowim.rap.framework.components.impl.ValidatedTextField;

import de.ebcot.tools.crypt.Encrypter;


/**
 * Dialog to add or edit a new person. You can show user information in editable and non-editable modus.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class PersonDialog extends DefaultDialog
{

    private ValidatedTextArea                   descriptionTextArea;

    private ValidatedTextField                  userNameTextField;
    private ValidatedTextField                  addressTextField;
    private ValidatedTextField                  phoneTextField;
    private ValidatedTextField                  firsNameTextField;
    private ValidatedTextField                  lastNameTextField;
    private ValidatedTextField                  organizationTextField;
    private ValidatedTextField                  passwordTextField;
    private ValidatedTextField                  passwordReplayTextField;

    private ValidateMail                        eMailField;

    private final boolean                       editableFileds;
    private String                              userID            = "";
    private String                              userName          = "";
    private String                              firstName         = "";
    private String                              lastName          = "";
    private String                              description       = "";
    private String                              address           = "";
    private String                              eMail             = "";
    private String                              phone             = "";
    private String                              organization      = "";
    private String                              password          = "";

    private final ArrayList<ValidatedTextField> fieldList         = new ArrayList<ValidatedTextField>();

    private Action                              addOrganization;
    private Person                              person            = null;

    private boolean                             isPasswordChanged = false;

    /**
     * Constructor of this dialog. It is better to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog, not null.
     * @param description see {@link DefaultDialog}
     * @param preSelectedPerson person, which is selected and its information shows in this dialog. Can be null, if it should created a new person
     * @param editable <code>true</code> if dialog is to edit informations of persons, else <code>false</code>.
     */
    public PersonDialog(Shell parentShell, Action action, String description, PersonLeaf preSelectedPerson, boolean editable)
    {
        super(parentShell, action, description);
        setPreselctedValues(preSelectedPerson);
        this.editableFileds = editable;
    }

    /**
     * 
     * CreateActions.
     */
    private void createActions()
    {
        this.addOrganization = Resources.Frames.Dialog.Actions.ADD_ORG_UNIT.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {

                DefaultTableModel orgaModel = new DefaultTableModel(new OrgaController().getTableModel(null), new OrgaController().getColumns());

                SelectOrganizationDialog organizationDialog = new SelectOrganizationDialog(null, addOrganization, "", orgaModel,
                                                                                           new ArrayList<Organization>(),
                                                                                           new DefaultConstraint(new Long(0), new Long(1000), false));

                if (organizationDialog.open() == IDialogConstants.OK_ID)
                {
                    Iterator<Organization> itReturnValue = organizationDialog.getSelectedOrganizations().iterator();
                    while (itReturnValue.hasNext())
                    {
                        Organization organization = itReturnValue.next();
                        organizationTextField.setText(organization.getName());
                        organizationTextField.setData(organization.getID());
                    }
                }
            }
        });
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

        createActions();

        Composite composite = new Composite(control, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(3, false));

        // name **********************************************************************************
        Label nameLbl = new Label(composite, SWT.RIGHT);
        nameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        nameLbl.setText(Resources.Frames.Global.Texts.NICKNAME.getText() + GlobalConstants.DOUBLE_POINT);

        userNameTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true));
        userNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        userNameTextField.setText(this.userName);
        userNameTextField.setEditable(editableFileds);
        fieldList.add(userNameTextField);

        setBlankBtn(composite);

        // First name **********************************************************************************
        Label firstNameLbl = new Label(composite, SWT.RIGHT);
        firstNameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        firstNameLbl.setText(Resources.Frames.Global.Texts.FIRSTNAME.getText() + GlobalConstants.DOUBLE_POINT);

        firsNameTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true));
        firsNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        firsNameTextField.setText(this.firstName);
        firsNameTextField.setEditable(editableFileds);
        fieldList.add(firsNameTextField);

        setBlankBtn(composite);

        // Last name **********************************************************************************
        Label lastNameLbl = new Label(composite, SWT.RIGHT);
        lastNameLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        lastNameLbl.setText(Resources.Frames.Global.Texts.LASTNAME.getText() + GlobalConstants.DOUBLE_POINT);

        lastNameTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true));
        lastNameTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lastNameTextField.setText(this.lastName);
        lastNameTextField.setEditable(editableFileds);
        fieldList.add(lastNameTextField);

        setBlankBtn(composite);

        // Address **********************************************************************************
        Label addressLbl = new Label(composite, SWT.RIGHT);
        addressLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        addressLbl.setText(Resources.Frames.Global.Texts.ADDRESS.getText() + GlobalConstants.DOUBLE_POINT);

        addressTextField = new ValidatedTextField(composite, new DefaultConstraint(0L, 1000L, false));
        addressTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        addressTextField.setText(this.address);
        addressTextField.setEditable(editableFileds);
        fieldList.add(addressTextField);

        setBlankBtn(composite);

        // eMail **********************************************************************************
        Label eMailLbl = new Label(composite, SWT.RIGHT);
        eMailLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        eMailLbl.setText(Resources.Frames.Global.Texts.MAIL.getText() + GlobalConstants.DOUBLE_POINT);

        eMailField = new ValidateMail(composite, new DefaultConstraint(0L, 1000L, false));
        eMailField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        eMailField.setText(this.eMail);
        eMailField.setEditable(editableFileds);
        setBlankBtn(composite);

        // Phone **********************************************************************************
        Label phoneLbl = new Label(composite, SWT.RIGHT);
        phoneLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        phoneLbl.setText(Resources.Frames.Global.Texts.PHONE.getText() + GlobalConstants.DOUBLE_POINT);

        phoneTextField = new ValidatedTextField(composite, new DefaultConstraint(0L, 1000L, false));
        phoneTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        phoneTextField.setText(this.phone);
        phoneTextField.setEditable(editableFileds);
        fieldList.add(phoneTextField);

        setBlankBtn(composite);

        // password
        createPassowordFields(composite);
        setBlankBtn(composite);

        // Set organization
        setOrganization(composite);

        // Description **********************************************************************************
        Label descLbl = new Label(composite, SWT.RIGHT);
        descLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        descLbl.setText(Resources.Frames.Global.Texts.DESCRIPTION.getText() + GlobalConstants.DOUBLE_POINT);

        descriptionTextArea = new ValidatedTextArea(composite, new DefaultConstraint(0L, 1000L, false));
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.horizontalSpan = 1;
        gridData.verticalSpan = 3;
        gridData.heightHint = 100;
        gridData.widthHint = 100;
        descriptionTextArea.setLayoutData(gridData);
        descriptionTextArea.setText(this.description);
        descriptionTextArea.setEditable(editableFileds);

        setBlankBtn(composite);

        return control;
    }

    private void createPassowordFields(Composite composite)
    {
        Label passwordLbl = new Label(composite, SWT.RIGHT);
        passwordLbl.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        passwordLbl.setText(Resources.Frames.Global.Texts.PASSWORD.getText() + GlobalConstants.DOUBLE_POINT);
        passwordTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true), SWT.BORDER | SWT.PASSWORD | SWT.FILL);
        passwordTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        passwordTextField.setText(this.password);
        passwordTextField.setEditable(editableFileds);
        passwordTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                if ( !password.equals(Encrypter.encryptMessage((passwordTextField.getText()).toCharArray())))
                    isPasswordChanged = true;
            }
        });

        fieldList.add(passwordTextField);

        setBlankBtn(composite);
        // password
        Label passwordLbl2 = new Label(composite, SWT.RIGHT);
        passwordLbl2.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        passwordLbl2.setText(Resources.Frames.Global.Texts.CONFIRM_PASSWORD.getText() + GlobalConstants.DOUBLE_POINT);
        passwordReplayTextField = new ValidatedTextField(composite, new DefaultConstraint(1L, 1000L, true), SWT.BORDER | SWT.PASSWORD | SWT.FILL);
        passwordReplayTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        passwordReplayTextField.setText(this.password);
        passwordReplayTextField.setEditable(editableFileds);
        passwordReplayTextField.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                if ( !password.equals(Encrypter.encryptMessage((passwordTextField.getText()).toCharArray())))
                    isPasswordChanged = true;
            }
        });

        fieldList.add(passwordReplayTextField);

    }

    /**
     * Set the fields if a to shows information of a person.
     * 
     * @param preSelectedPerson2
     */
    private void setPreselctedValues(PersonLeaf preSelectedPerson)
    {
        if (preSelectedPerson != null)
        {
            userID = preSelectedPerson.getID();
            userName = preSelectedPerson.getShortName();
            firstName = preSelectedPerson.getFirstName();
            lastName = preSelectedPerson.getLastName();
            description = preSelectedPerson.getDescription();
            address = preSelectedPerson.getAddress();
            eMail = preSelectedPerson.getEmailAddress();
            phone = preSelectedPerson.getTelefon();
            organization = preSelectedPerson.getOrganisation();
            password = preSelectedPerson.getPassword();
        }
    }

    /**
     * 
     * set a blank button to design the with tree widgets in a line
     * 
     * @param composite Composite
     */
    private void setBlankBtn(Composite composite)
    {
        Button blankBtn = new Button(composite, SWT.PUSH);
        blankBtn.setVisible(false);
    }

    /**
     * Set the persons of knowledge.
     * 
     * @param selectedKnowLinkGroup
     */
    private void setOrganization(Composite composite)
    {
        Label personLabel = new Label(composite, SWT.TRAIL);
        personLabel.setLayoutData(new GridData(GridData.END, SWT.CENTER, false, false));
        personLabel.setText(Resources.Frames.Global.Texts.ORGANISATION_UNIT.getText() + GlobalConstants.DOUBLE_POINT);

        organizationTextField = new ValidatedTextField(composite, new DefaultConstraint(0L, 1000L, false));
        organizationTextField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        organizationTextField.setEditable(false);
        organizationTextField.setText(this.organization);

        Button orgaBtn = new Button(composite, SWT.PUSH);
        orgaBtn.setEnabled(editableFileds);
        orgaBtn.setImage(this.addOrganization.getImageDescriptor().createImage());

        orgaBtn.addSelectionListener(new SelectionListener()
        {
            @Override
            public void widgetSelected(SelectionEvent e)
            {
                addOrganization.runWithEvent(new Event());
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e)
            {
            }
        });
    }

    /**
     * 
     * Checks the verifying of fields.
     * 
     * @return true, if all are OK, else false.
     */
    private boolean validateFields()
    {
        boolean verfiyValue = false;
        for (ValidatedTextField fileds : fieldList)
        {
            verfiyValue = fileds.isVerified();
        }

        return (eMailField.isVerified() && descriptionTextArea.isVerified() && verfiyValue);
    }

    /**
     * 
     * Checks if the passwords field are equals.
     * 
     * @return true, if all are OK, else false.
     */
    private boolean passwordsEquals()
    {
        return passwordTextField.getText().equals(passwordReplayTextField.getText());
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
        if (editableFileds)
        {
            if (validateFields())
            {
                if ( !passwordsEquals())
                {
                    InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.PASSWORDS_EQUALS_DIALOG.getTooltip());
                }
                else
                {
                    setValuesForOutput();
                    close();
                }
            }
            else
            {
                InformationDialog.openInformation(null, Resources.Frames.Dialog.Actions.REQUIRED_ERROR_DIALOG.getTooltip());
            }
        }
        else
            close();

    }

    /**
     * 
     * Set values, which are necessary to handle after dialog is closed.
     */
    private void setValuesForOutput()
    {
        String password = "";
        if (this.isPasswordChanged)
            password = Encrypter.encryptMessage(passwordReplayTextField.getText().toCharArray());
        else
            password = this.password;

        person = DefaultDataObjectFactory.createPerson(userID, firsNameTextField.getText(), lastNameTextField.getText(), userNameTextField.getText(),
                                                       password);
        person.setAddress(addressTextField.getText());
        person.setEmailAddress(eMailField.getText());
        person.setTelefon(phoneTextField.getText());
        person.setDescription(descriptionTextArea.getText());
        person.setOrganisation(organizationTextField.getText());

        if (organizationTextField.getData() != null)
        {
            person.setOrganisation(organizationTextField.getData().toString());
        }
        else
            person.setOrganisation("");
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        // create OK and Cancel buttons by default
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.OK.getText(), true);
        if (editableFileds)
            createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), false);
    }

    /**
     * 
     * getPerson.
     * 
     * @return returns the information of a {@link Person}, which can be changed or new created by user. Null if dialog canceled.
     */
    public Person getPerson()
    {
        return this.person;
    }
}
