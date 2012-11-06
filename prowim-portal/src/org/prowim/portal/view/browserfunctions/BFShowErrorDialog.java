/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 19.10.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.portal.view.browserfunctions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.prowim.datamodel.dms.DMSConstants;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.SupportMailDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.i18n.Resources.Frames.Global.Texts;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * A Browser function to show an error dialog with the option to send information by email.
 * 
 * @author Thomas Wiesner
 * @version $Revision$
 * @since 2.0.0
 */
public class BFShowErrorDialog extends AbstractModelEditorFunction
{

    /**
     * Shall only be used by {@link ModelEditorFunctionFactory}.
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFShowErrorDialog(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.browserfunctions.AbstractModelEditorFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] arguments)
    {
        // gets the valid arguments and use it
        StringBuilder exceptionMessage = new StringBuilder();

        for (int i = 0; i < arguments.length; i++)
        {
            Object currentArgument = arguments[i];

            exceptionMessage.append(currentArgument);
            exceptionMessage.append("\n");
        }
        final String exString = exceptionMessage.toString();

        Display.getCurrent().asyncExec(new Runnable()
        {

            @Override
            public void run()
            {
                Action action = Resources.Frames.Dialog.Actions.SUPPORT_MAIL_DIALOG.getAction();
                SupportMailDialog supportMailDialog = new SupportMailDialog(null, action, Resources.Frames.Dialog.Texts.SUPPORT_MAIL_DLG_DESCRIPTION
                        .getText(), exString);

                if (supportMailDialog.open() == IDialogConstants.OK_ID)
                {
                    String subject = "[" + StringUtils.capitalize(DMSConstants.getCustomerFolder()) + "] ";
                    MainController.getInstance().sendMessage(exString, new String[] { Texts.ERROR_EMAIL_RECEIVER.getText() },
                                                             subject + Texts.ERROR_MODEL_EDITOR_EMAIL_SUBJECT.getText(),
                                                             Texts.ERROR_EMAIL_FOOTER.getText());
                }
            }
        });

        return null;
    }
}
