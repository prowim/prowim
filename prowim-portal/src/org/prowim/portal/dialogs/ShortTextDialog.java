/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-12-01 19:14:51 +0100 (Mi, 01 Dez 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/ShortTextDialog.java $
 * $LastChangedRevision: 5025 $
 *------------------------------------------------------------------------------
 * (c) 12.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.framework.components.impl.ValidatedTextField;



/**
 * This dialog has only one text field, which is single. You can set the properties min, max and required <br/>
 * with a constraint.
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5025 $
 */
public class ShortTextDialog extends DefaultDialog
{
    private ValidatedTextField      shortTextLimit;
    private String                  textValue    = "";
    private final DefaultConstraint constraint;
    private boolean                 editable     = true;
    private boolean                 cancelButton = true;

    /**
     * Constructor of this dialog. you have to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action which is used for the title, image and headerTitle (~action tooltip) text, may not be null
     * @param description see {@link DefaultDialog}
     * @param text A string to edit in this dialog
     * @param defaultConstraint Included the properties min, max and required
     * @param cancelButton Set if the dialog should have a cancel button or not. <code>true</code> if dialog should have cancel button, else <code>false</code>
     */
    public ShortTextDialog(Shell parentShell, Action action, String description, final String text, final DefaultConstraint defaultConstraint,
            boolean cancelButton)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(text, "text can not be null");
        Validate.notNull(defaultConstraint, "Constraint can not be null");

        this.textValue = text;
        this.constraint = defaultConstraint;
        this.cancelButton = cancelButton;
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

        shortTextLimit = new ValidatedTextField(control, constraint);
        shortTextLimit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        shortTextLimit.setText(this.textValue);
        shortTextLimit.setEditable(editable);
        shortTextLimit.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                if (constraint.isRequired())
                {
                    textValue = shortTextLimit.getText();
                }
            }
        });

        return control;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.DefaultDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.OK.getText(), true);
        if (cancelButton)
            createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), false);
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
        if (shortTextLimit.isVerified())
        {
            close();
        }
    }

    /**
     * 
     * Returns given text in this dialog.
     * 
     * @return not null string
     */
    public String getText()
    {
        return this.textValue;
    }

    /**
     * 
     * Set the editable of text field. Default is text field editabel.
     * 
     * @param editable <code>true</code> if text field is editable, else <code>false</code>
     */
    public void setEditabel(boolean editable)
    {
        this.editable = editable;
    }

}
