/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 11.12.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.dialogs.feedback;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.prowim.portal.i18n.Resources;



/**
 * Shows descriptions to selected object. Input is a string, which included the descriptions.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public final class InformationDialog extends MessageDialog
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
    private InformationDialog(Shell parentShell, String dialogTitle, Image dialogTitleImage, String dialogMessage, int dialogImageType,
            String[] dialogButtonLabels, int defaultIndex)
    {
        super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
    }

    /**
     * {@link MessageDialog#openInformation(Shell, String, String)}.
     * 
     * @param parent parent shell.
     * @param message the headerTitle.
     */
    public static void openInformation(final Shell parent, final String message)
    {
        MessageDialog.openInformation(parent, Resources.Frames.Dialog.Texts.INFORMATION_TITLE.getText(), message);
    }
}
