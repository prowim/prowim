/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.Iterator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.dialog.ProcessElementController;
import org.prowim.portal.controller.dialog.RoleController;
import org.prowim.portal.dialogs.SelectElementDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.tables.DefaultTableModel;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * Calls a dialog, which show exists roles, means or depots. User can select one of this elements or create a new one. <br>
 * Return value updates the attributes at model editor. <br>
 * The arguments comprised the id of {@link Process} of model and a flag, which can be <br>
 * 
 * {@link Role} <br>
 * 
 * {@link Mean} or <br>
 * 
 * {@link ResultsMemory} "Depot". <br>
 * 
 * Just now null is always returned as a workaround, because JavaScript is not still waiting for a dialog's return value, see http://bugs.ebcot.info/view.php?id=4405
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
@SuppressWarnings("restriction")
public class BFSelectProcessElement extends AbstractModelEditorFunction
{
    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFSelectProcessElement(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * Description see {@link BFSelectProcessElement}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {
        // gets the valid arguments and use it
        Iterator<String> iterator = ArgumentValidator.convert(args).iterator();
        String elementID = iterator.next();
        String elementClass = iterator.next();

        // depending on the elementClass create now the necessary actions
        DefaultTableModel model = null;
        Action action = null;

        String description = "";
        if (elementClass.equals(GlobalConstants.ROLE))
        {
            final RoleController controller = new RoleController();
            model = new DefaultTableModel(controller.getTableModel(), controller.getColumns());
            description = Resources.Frames.Dialog.Texts.DESC_EXIST_ROLES.getText();
            action = Resources.Frames.Dialog.Actions.SELECT_CREATE_ROLE.getAction();
        }
        else if (elementClass.equals(GlobalConstants.MEAN))
        {
            final ProcessElementController controller = new ProcessElementController();
            model = new DefaultTableModel(controller.getTableModel(GlobalConstants.MEAN), controller.getColumns());
            description = Resources.Frames.Dialog.Texts.DESC_EXIST_MEAN.getText();
            action = Resources.Frames.Dialog.Actions.SELECT_CREATE_MEAN.getAction();
        }
        else if (elementClass.equals(GlobalConstants.DEPOT))
        {
            final ProcessElementController controller = new ProcessElementController();
            model = new DefaultTableModel(controller.getTableModel(GlobalConstants.DEPOT), controller.getColumns());
            description = Resources.Frames.Dialog.Texts.DESC_EXIST_RESULTS_MEM.getText();
            action = Resources.Frames.Dialog.Actions.SELECT_CREATE_RESULTS_MEM.getAction();
        }
        else
            throw new IllegalArgumentException("Unknown elementClass: " + elementClass);

        // Open the dialog to select or create a element
        String[] resultTxt = null;

        SelectElementDialog dialog = new SelectElementDialog(null, action, description, model);
        if (dialog.open() == IDialogConstants.OK_ID)
            resultTxt = dialog.getResults();

        // If null is return that means, that cancel button of dialog is selected. In this case delete the created shape in model editor.
        executeJSScript(elementID, elementClass, resultTxt);

        return null;
    }

    private void executeJSScript(String elementID, String elementClass, String[] resultTxt)
    {
        final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
        if (resultTxt == null)
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
        else
        {
            String instanceId = resultTxt[0];
            if (resultTxt != null && !instanceId.equals("") && !instanceId.equals(GlobalConstants.LOCAL_ELEMENT)
                    && !instanceId.equals(GlobalConstants.GLOBAL_ELEMENT))
            {
                String jsInstanceId = EscapeFunctions.toJSString(instanceId);
                String jsLabel = EscapeFunctions.toJSString(resultTxt[1]);
                String jsDescription = EscapeFunctions.toJSString(resultTxt[2]);

                String jsCode = "if (" + jsContentWindow + " != null){" + //
                        "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                        "var cell = " + "graph.getSelectionCell();" + //
                        "if (cell != null){" + //
                        "var model = graph.getModel(); " + //
                        "model.beginUpdate(); " + //
                        "try {" + //
                        "cell.setAttribute('prowimid', '" + jsInstanceId + "');\n" + //
                        "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'prowimid', '" + jsInstanceId + "'));" + //
                        "cell.setAttribute('label', '" + jsLabel + "');" + //
                        "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'label', '" + jsLabel + "'));" + //
                        "cell.setAttribute('description', '" + jsDescription + "');" + //
                        "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'description', '" + jsDescription + "'));" + //
                        "} finally {" + //
                        "model.endUpdate(); }" + //
                        "graph.refresh(cell);}}"; //
                //
                // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
                // HtmlResponseWriter writer = stateInfo.getResponseWriter();
                // writer.append(jsCode);
                JSExecutor.executeJS(jsCode);

                // Set relation between element and process
                MainController.getInstance().setElementToModel(elementID, instanceId);
            }
            // if an object should be created newly, create this object here and update the shape at model editor.
            else if (instanceId.equals(GlobalConstants.LOCAL_ELEMENT) || instanceId.equals(GlobalConstants.GLOBAL_ELEMENT))
            {
                String instanceID = null;

                if (elementClass.equals(GlobalConstants.ROLE))
                {
                    instanceID = MainController.getInstance().createObject(elementID, GlobalConstants.ROLE_DE);
                }
                else if (elementClass.equals(GlobalConstants.MEAN))
                {
                    instanceID = MainController.getInstance().createObject(elementID, GlobalConstants.MEAN_DE);
                }
                else if (elementClass.equals(GlobalConstants.DEPOT))
                {
                    instanceID = MainController.getInstance().createObject(elementID, GlobalConstants.DEPOT_DE);
                }
                else
                    throw new IllegalArgumentException("Unknown elementClass: " + elementClass);

                String jsLabel = EscapeFunctions.toJSString(MainController.getInstance().getName(instanceID));
                String jsInstanceid = EscapeFunctions.toJSString(instanceID);

                if (instanceId.equals(GlobalConstants.GLOBAL_ELEMENT))
                    MainController.getInstance().setScope(instanceID, GlobalConstants.GLOBAL);
                else
                    MainController.getInstance().setScope(instanceID, GlobalConstants.LOCAL);

                String jsCode = "if (" + jsContentWindow + " != null){" + //
                        "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                        "var cell = " + "graph.getSelectionCell();" + //
                        "if (cell != null)" + //
                        "{" + //
                        "var model = graph.getModel(); " + //
                        "model.beginUpdate(); " + //
                        "try {" + //
                        "cell.setAttribute('prowimid', '" + jsInstanceid + "');" + //
                        "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'prowimid', '" + jsInstanceid + "'));" + //
                        "cell.setAttribute('label', '" + jsLabel + "');" + //
                        "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'label', '" + jsLabel + "'));" + //
                        "} finally {" + //
                        "model.endUpdate(); " + //
                        "}" + //
                        "graph.refresh(cell);}" + //
                        "}"; //

                // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
                // HtmlResponseWriter writer = stateInfo.getResponseWriter();
                // writer.append(jsCode);

                JSExecutor.executeJS(jsCode);
            }
            else
                throw new IllegalStateException("Unknown selection: " + resultTxt);
        }
    }

}
