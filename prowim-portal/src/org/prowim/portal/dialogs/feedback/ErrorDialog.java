/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 14:14:37 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/dialogs/feedback/ErrorDialog.java $
 * $LastChangedRevision: 4961 $
 *------------------------------------------------------------------------------
 * (c) 04.02.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs.feedback;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;



/**
 * Default Dialog for error messages.
 * 
 * @author Saad Wardi
 * @version $Revision: 4961 $
 * @since 2.0
 */
public final class ErrorDialog extends MessageDialog
{

    /**
     * Description.
     * 
     * @param parentShell parent {@link Shell}. Can be null.
     * @param dialogTitle Title of dialog.
     * @param dialogTitleImage image of dialog.
     * @param dialogMessage Main headerTitle of dialog.
     * @param dialogImageType dialog icon
     * @param dialogButtonLabels labels of buttons for dialog.
     * @param defaultIndex the index in the button label array of the default button
     */
    private ErrorDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType,
            String[] dialogButtonLabels, int defaultIndex)
    {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
    }

    /**
     * {@link MessageDialog#openError(Shell, String, String)}.
     * 
     * @param parent parent shell.
     * @param message the headerTitle.
     */
    public static void openError(Shell parent, String message)
    {
        MessageDialog.openError(parent, Resources.Frames.Dialog.Texts.ERROR_TITLE.getText(), message);
    }

    /**
     * 
     * Opens an error dialog using {@link MessageDialog#openError(Shell, String, String) headerTitle dialog} and displays the headerTitle from the given exception with further information.
     * 
     * @param exception the exception to display
     */
    public static void openException(Exception exception)
    {
        String message = Resources.Frames.Dialog.Texts.ERROR_MESSAGE.getText() + "\n" + exception.getMessage();
        MessageDialog.openError(null, Resources.Frames.Dialog.Texts.ERROR_TITLE.getText(), message);

    }

}
