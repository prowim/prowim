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

import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.prowim.datamodel.prowim.ProcessInformation;
import org.prowim.datamodel.prowim.Product;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.ProductParamDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Browser function to open the product parameter dialog from JS (Model editor). The arguments comprised the id of {@link Product}.
 * 
 * Just now null is always returned as a workaround, because JavaScript is not still waiting for a dialog's return value, see http://bugs.ebcot.info/view.php?id=4405
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class BFOpenProductParameters extends AbstractModelEditorFunction
{
    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFOpenProductParameters(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * Description see {@link BFOpenProductParameters}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {

        Action action = Resources.Frames.Dialog.Actions.PRODUCT_PARAM_DIALOG.getAction();

        // gets the valid arguments and use it
        Iterator<String> iterator = ArgumentValidator.convert(args).iterator();
        int i = 0;
        String productID = null;
        while (iterator.hasNext())
        {
            String arg = iterator.next();
            if (i == 0)
            {
                productID = arg;
                i++;
            }
        }

        ProductParamDialog prarameterDialog = new ProductParamDialog(null, action, "", productID);
        if (prarameterDialog.open() == IDialogConstants.OK_ID)
        {
            List<ProcessInformation> actElements = prarameterDialog.getProcessInformations();
            for (int j = 0; j < actElements.size(); j++)
            {
                ProcessInformation procInfo = actElements.get(j);
                MainController.getInstance().rename(procInfo.getID(), procInfo.getName());
                MainController.getInstance().setInformationType(procInfo.getID(), procInfo.getInfoType().getID());
            }
        }

        return null;
    }
}
