/*==============================================================================
 * File $Id: OpenViewAction.java 2748 2009-11-18 17:23:39Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2009-11-18 18:23:39 +0100 (Mi, 18 Nov 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/action/OpenViewAction.java $
 * $LastChangedRevision: 2748 $
 *------------------------------------------------------------------------------
 * (c) 12.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.action;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;


/**
 * This is a main class to create and open views. You have to give the workbench, where the vies should created and the ID of view
 * 
 * @author Maziar Khodaei
 * @version $Revision: 2748 $
 */
public class OpenViewAction extends Action
{
    private final IWorkbenchWindow window;
    private final String           viewId;

    /**
     * Description.
     * 
     * @param window IWorkbenchWindow
     * @param viewId String
     */
    public OpenViewAction(IWorkbenchWindow window, String viewId)
    {
        Validate.notNull(window);
        Validate.notNull(viewId);

        this.window = window;
        this.viewId = viewId;

    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run()
    {
        if (window != null)
        {
            try
            {
                window.getActivePage().showView(viewId, Integer.toString(0), IWorkbenchPage.VIEW_ACTIVATE);
            }
            catch (PartInitException e)
            {
                MessageDialog.openError(window.getShell(), "Error", "Error opening view:" + e.getMessage());
            }
        }
    }
}
