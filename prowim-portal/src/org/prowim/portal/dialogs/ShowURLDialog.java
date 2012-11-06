/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 01.12.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.components.impl.DefaultConstraint;



/**
 * A dialog to show the URL of a model to copy this. User can then set the URL in WIKI etc. and call the process model.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.2
 */
public class ShowURLDialog extends ShortTextDialog
{

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
    public ShowURLDialog(Shell parentShell, Action action, String description, final String text, final DefaultConstraint defaultConstraint,
            boolean cancelButton)
    {
        super(parentShell, action, description, text, defaultConstraint, cancelButton);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.dialogs.ShortTextDialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent)
    {
        createButton(parent, IDialogConstants.OK_ID, Resources.Frames.Global.Texts.CLOSE.getText(), true);
    }
}
