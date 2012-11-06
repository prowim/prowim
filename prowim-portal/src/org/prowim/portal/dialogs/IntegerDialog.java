/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 30.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.rap.framework.components.impl.IntegerLimit;



/**
 * You can only type 0-9 . Other keys will not accepted. <br/>
 * You can set the attributes min, max and required in constraint
 * 
 * @see org.prowim.rap.framework.components.Constraint
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * 
 */
public final class IntegerDialog extends DefaultDialog
{
    private IntegerLimit            integerLimit;
    private String                  integerValue = "";
    private final DefaultConstraint constraint;

    /**
     * Constructor. You have to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param value a not null integer in string
     * @param defaultConstraint Included the properties min, max and required
     */
    public IntegerDialog(Shell parentShell, Action action, String description, final String value, final DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(value, "the input interger value can not be null");
        Validate.notNull(defaultConstraint, "Constraint can not be null");

        this.integerValue = value;
        this.constraint = defaultConstraint;

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
        createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), false);
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

        integerLimit = new IntegerLimit(control, constraint);
        integerLimit.setFocus();
        integerLimit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        integerLimit.setText(this.integerValue);

        integerLimit.addModifyListener(new ModifyListener()
        {

            @Override
            public void modifyText(ModifyEvent event)
            {
                integerValue = integerLimit.getText();
            }
        });

        return control;
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
        if (integerLimit.isVerified())
        {
            close();
        }
    }

    /**
     * 
     * Returns the given integer value in this dialog
     * 
     * @return not null string.
     */
    public String getIntegerValue()
    {
        return this.integerValue;
    }

}
