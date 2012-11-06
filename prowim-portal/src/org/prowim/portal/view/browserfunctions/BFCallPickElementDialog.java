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

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.PickElementDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeRootLeaf;
import org.prowim.portal.models.tree.model.SwimlaneLeaf;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * Exposes {@link MainController#setProcessType(String, String)} as {@link AbstractModelEditorFunction}.
 * 
 * To call th {@link PickElementDialog} to select a element from tree. <br>
 * You can call this class with two arguments: <li>flag: <code>true</code> if shape should deletes by cancel. else <code>false</code>. <code>true</code> is making sense by creating a new shape and <code>false</code> by renaming a existing shape</li> <li>elementType: To decide which elements should loads in tree to select one of this.</li>
 * 
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a8
 */
public class BFCallPickElementDialog extends AbstractModelEditorFunction
{

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFCallPickElementDialog(ModelEditor modelEditor, String name)
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
        boolean delete = (Boolean) arguments[0];
        String elementType = (String) arguments[1];
        DefaultLeaf leaf = null;
        Action action = null;

        if (elementType.equals("PROCESSTYPE_LANDSPACE"))
        {
            leaf = new ProcessTypeRootLeaf("", "");
            action = Resources.Frames.Dialog.Actions.SET_PROZESSTYPE_LANDSCAPE.getAction();
        }
        else if (elementType.equals("PROCESS_LANDSPACE"))
        {
            leaf = new ProcessEditorLeaf("", "");
            action = Resources.Frames.Dialog.Actions.SET_PROZESS_LANDSCAPE.getAction();
        }
        else if (elementType.equals("SWIMLANE"))
        {
            leaf = new SwimlaneLeaf();
            action = Resources.Frames.Dialog.Actions.SET_NAME_OF_SWIMLANE.getAction();
        }
        else
            throw new IllegalArgumentException("Error by BFCallPickElementDialog.");

        final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";

        PickElementDialog pickElement = new PickElementDialog(null, action, "", leaf);

        if (pickElement.open() == IDialogConstants.OK_ID)
        {
            DefaultLeaf selectedLeaf = pickElement.getSelectedLeaf();

            String jsInstanceId = EscapeFunctions.toJSString(selectedLeaf.getID());
            String jsLabel = EscapeFunctions.toJSString(selectedLeaf.getName());

            // If null is return that means, that cancel button of dialog is selected. In this case delete the created shape in model editor.
            String jsCode = "if (" + jsContentWindow + " != null){" + //
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var cell = " + "graph.getSelectionCell();" + //
                    "if (cell != null){" + //
                    "var model = graph.getModel(); " + //
                    "model.beginUpdate(); " + //
                    "try {" + //
                    "cell.setAttribute('elementid', '" + jsInstanceId + "');\n" + //
                    "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'elementid', '" + jsInstanceId + "'));" + //
                    "cell.setAttribute('label', '" + jsLabel + "');" + //
                    "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'label', '" + jsLabel + "'));" + //
                    "} finally {" + //
                    "model.endUpdate(); }" + //
                    "graph.refresh(cell);}}"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);
        }
        else
        {
            if (delete)
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
        }
        return null;
    }
}
