/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 21.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.browserfunctions;

import org.apache.log4j.Logger;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.portal.view.process.ProcessBrowserView;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Shows the process model of given {@link Process} id. This is a sub process.<br>
 * 
 * Just now null is always returned as a workaround, because JavaScript is not still waiting for a dialog's return value, see http://bugs.ebcot.info/view.php?id=4405 *
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class BFShowProcess extends AbstractModelEditorFunction
{
    private final static Logger LOG = Logger.getLogger(BFShowProcess.class);

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFShowProcess(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * Description see {@link BFShowProcess}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {
        // gets the valid arguments and use it
        String subProcessID = ArgumentValidator.convert(args).get(0);

        try
        {
            IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
            IViewPart viewPart;

            viewPart = window.getActivePage().showView(ProcessBrowserView.ID, subProcessID, IWorkbenchPage.VIEW_ACTIVATE);

            ProcessBrowserView modelEditorView2 = (ProcessBrowserView) viewPart;
            modelEditorView2.setSubprocess(subProcessID);

        }
        catch (PartInitException error)
        {
            LOG.error("Error by createNewView: ", error);
        }
        return null;
    }

}
