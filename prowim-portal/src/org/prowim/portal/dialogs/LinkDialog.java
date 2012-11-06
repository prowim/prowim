/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.rap.framework.components.impl.LinkLimit;



/**
 * Create a instance of LinkLimit. Constraint set the min, max and required. <br/>
 * min and max set the minimal and maximal length of text field. Required set, if the field can be empty or not.
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public final class LinkDialog extends DefaultDialog
{
    private LinkLimit               linkLimit;
    private String                  linkValue = "";
    private final DefaultConstraint constraint;

    /**
     * Constructor of this dialog. you have to use openDilaog-Method to call this dialog.
     * 
     * @param parentShell Composite, where this dialog will be shown. Can be null
     * @param action Action to call this dialog
     * @param description see {@link DefaultDialog}
     * @param link not null string
     * @param defaultConstraint Included the properties min, max and required
     */
    public LinkDialog(Shell parentShell, Action action, String description, final String link, final DefaultConstraint defaultConstraint)
    {
        super(parentShell, action, description);
        setHeaderImage(Resources.Frames.Dialog.Images.DIALOG_DEFAULT.getImage());

        Validate.notNull(link, "link can not be null");
        Validate.notNull(defaultConstraint, "Constraint can not be null");

        this.linkValue = link;
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
        Composite control = super.createCustomArea(parent);

        linkLimit = new LinkLimit(control, constraint);
        linkLimit.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        linkLimit.setText(this.linkValue);
        linkLimit.addModifyListener(new ModifyListener()
        {
            @Override
            public void modifyText(ModifyEvent event)
            {
                linkValue = linkLimit.getText();
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
        if (linkLimit.isVerified())
        {
            close();
        }
    }

    /**
     * 
     * Returns the given link value in this dialog
     * 
     * @return not null string.
     */
    public String getLinkValue()
    {
        return this.linkValue;
    }

}
