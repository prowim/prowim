/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 29.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.SingleListDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * Exposes {@link MainController#getPossibleCombinationRules()} as {@link AbstractModelEditorFunction}.
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
@SuppressWarnings("restriction")
public class BFGetPossibleCombinationRules extends AbstractModelEditorFunction
{
    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFGetPossibleCombinationRules(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] arguments)
    {
        List<String> argList = ArgumentValidator.convert(arguments);

        // Get combination rules
        String[] combinationRules = MainController.getInstance().getPossibleCombinationRules();
        List<String> ruleList = new ArrayList<String>();

        for (int i = 0; i < combinationRules.length; i++)
        {
            ruleList.add(combinationRules[i]);
        }

        // Open dialog to select a rule
        SingleListDialog<String> dilaog = new SingleListDialog<String>(null, Resources.Frames.Dialog.Actions.SELECT_RELATION_DIALOG.getAction(), "",
                                                                       ruleList, null, new DefaultConstraint(new Long(1), new Long(1), true));
        final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
        if (dilaog.open() == IDialogConstants.OK_ID)
        {
            String selectedRule = (String) dilaog.getSelectedValue();

            String jsLabel = EscapeFunctions.toJSString(selectedRule);

            String jsCode = "if (" + jsContentWindow + " != null){" + //
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var cell = " + "graph.getSelectionCell();" + //
                    "if (cell != null){" + //
                    "var model = graph.getModel(); " + //
                    "model.beginUpdate(); " + //
                    "try {" + //
                    "cell.setAttribute('label', '" + jsLabel + "');" + //
                    "} finally {" + //
                    "model.endUpdate(); }" + //
                    "graph.refresh(cell);}}"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);

            MainController.getInstance().setCombinationRule(argList.get(0), selectedRule);
        }
        else
        {
            String jsCode = "if (" + jsContentWindow + " != null){" + //
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var cell = " + "graph.getSelectionCell();" + //
                    "if (cell != null){" + //
                    "var model = graph.getModel(); " + //
                    "model.beginUpdate(); " + //
                    "try {" + //
                    "graph.removeCells([cell]);" + //
                    "} finally {" + //
                    "model.endUpdate(); }" + //
                    "graph.refresh(cell);}}"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);
        }
        return null;
    }
}
