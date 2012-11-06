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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.portal.view.browserfunctions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.SingleListDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.logging.Logger;
import de.ebcot.tools.string.EscapeFunctions;


/**
 * Exposes {@link MainController#getPossibleRelations(String, String)} as {@link AbstractModelEditorFunction}.
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
@SuppressWarnings("restriction")
public class BFGetPossibleRelations extends AbstractModelEditorFunction
{

    private static final Logger LOG = Logger.getLogger(BFGetPossibleRelations.class);

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFGetPossibleRelations(ModelEditor modelEditor, String name)
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
        String[] relations = MainController.getInstance().getPossibleRelations(argList.get(0), argList.get(1));
        List<String> relationList = new ArrayList<String>();

        for (int i = 0; i < relations.length; i++)
        {
            relationList.add(relations[i]);
        }

        SingleListDialog<String> dilaog = new SingleListDialog<String>(null, Resources.Frames.Dialog.Actions.SELECT_RELATION_DIALOG.getAction(), "",
                                                                       relationList, null, new DefaultConstraint(new Long(1), new Long(1), true));

        final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
        if (dilaog.open() == IDialogConstants.OK_ID)
        {
            String selectedRelation = (String) dilaog.getSelectedValue();

            // Set GUI attributes for the relation
            String strokeColor = "";
            String strokeWidth = "";
            String cell = "";

            if (selectedRelation.equals(GlobalConstants.Relations.END_WITH))
            {
                strokeColor = "#000000";
                strokeWidth = "3";
                cell = "[cell.source]";
            }
            else if (selectedRelation.equals(GlobalConstants.Relations.IS_END_ACTIVITY_OF))
            {
                strokeColor = "#000000";
                strokeWidth = "3";
                cell = "[cell.target]";
            }
            else if (selectedRelation.equals(GlobalConstants.Relations.STARTS_WITH))
            {
                strokeColor = "#444444";
                strokeWidth = "0";
                cell = "[cell.source]";
            }
            else if (selectedRelation.equals(GlobalConstants.Relations.IS_START_ACTIVITY_OF))
            {
                strokeColor = "#444444";
                strokeWidth = "0";
                cell = "[cell.target]";
            }
            else
                throw new IllegalArgumentException(selectedRelation);

            String jsLabel = EscapeFunctions.toJSString(selectedRelation);

            String jsCode = "if (" + jsContentWindow + " != null){" + //
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var cell = " + "graph.getSelectionCell();" + //
                    "if (cell != null){" + //
                    "var model = graph.getModel(); " + //
                    "model.beginUpdate(); " + //
                    "try {" + //
                    "cell.setAttribute('label', '" + jsLabel + "');" + //
                    "graph.setCellStyles('strokeColor', '" + strokeColor + "', " + cell + ");" + //
                    "graph.setCellStyles('strokeWidth', '" + strokeWidth + "', " + cell + ");" + //
                    "} finally {" + //
                    "model.endUpdate(); }" + //
                    "graph.refresh(cell);}}"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);

            // Set relation between two elements
            MainController.getInstance().setRelationValue(argList.get(0), selectedRelation, argList.get(1));
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
