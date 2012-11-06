/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.10.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.prowim.portal.i18n.Resources;



/**
 * Shows user a view with error stacks to send this as mail or to copy it.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0
 */
public class SupportMailDialog extends DefaultDialog
{
    private String errorMessage = "";

    /**
     * Description @see SupportMailDialog
     * 
     * @param parentShell @see {@link DefaultDialog}
     * @param action @see {@link DefaultDialog}
     * @param headerDescription @see {@link DefaultDialog}
     * @param errorMessage error stack to show in dialog
     */
    public SupportMailDialog(Shell parentShell, Action action, String headerDescription, String errorMessage)
    {
        super(parentShell, action, headerDescription);
        setHeaderImage(Resources.Frames.Dialog.Images.SUPPORT_MAIL_DIALOG_IMAGE.getImage());

        this.errorMessage = errorMessage;
    }

    /**
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
        composite.setLayout(new GridLayout(1, false));

        Label nameLbl = new Label(composite, SWT.WRAP | SWT.SHADOW_OUT);
        nameLbl.setText(Resources.Frames.Dialog.Texts.SUPPORT_MAIL_DLG_CONTENT.getText());
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.BEGINNING).grab(true, false)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), SWT.DEFAULT).applyTo(nameLbl);

        Composite empty = new Composite(composite, SWT.NONE);
        empty.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        empty.setLayout(new GridLayout(1, false));

        Group errorGroup = new Group(composite, SWT.NONE);
        errorGroup.setText(Resources.Frames.Global.Texts.PROBLEM_REPORT.getText());
        errorGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        errorGroup.setLayout(new GridLayout(1, false));

        Text text = new Text(errorGroup, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.WRAP);
        text.setText(this.errorMessage);
        GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false)
                .hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH), 100).applyTo(text);

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
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Dialog.Texts.SEND_MAIL.getText(), true);
        createButton(parent, IDialogConstants.CANCEL_ID, Resources.Frames.Global.Texts.CANCEL.getText(), false);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    @Override
    protected void okPressed()
    {
        super.okPressed();
    }
}
