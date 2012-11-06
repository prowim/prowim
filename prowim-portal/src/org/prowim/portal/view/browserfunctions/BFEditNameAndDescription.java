/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 28.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.RenameDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.portal.view.menu.MenuBarView;
import org.prowim.portal.view.process.ModelEditorView;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * This dialog is called to edit the name and the description of an element from the modeleditor. <br>
 * 
 * @author Saad Wardi, Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0
 */
@SuppressWarnings("restriction")
public class BFEditNameAndDescription extends AbstractModelEditorFunction
{
    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFEditNameAndDescription(ModelEditor modelEditor, String name)
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
        List<String> argList = ArgumentValidator.convert(args);
        String elementID = argList.get(0);

        Action editNameDescriptionAction = Resources.Frames.Toolbar.Actions.EDIT_NAME_DESCRIPTION.getAction();

        RenameDialog renameDialog = new RenameDialog(null, editNameDescriptionAction, "", MainController.getInstance().getName(elementID),
                                                     MainController.getInstance().getDescription(elementID));

        if (renameDialog.open() == IDialogConstants.OK_ID)
        {
            String elementName = renameDialog.getName();
            String elementDescription = renameDialog.getDescritption();

            MainController.getInstance().rename(elementID, elementName);
            MainController.getInstance().setDescription(elementID, elementDescription);

            // Change the id of model, if this is changes
            ModelEditor modelEditor = getModelEditor();
            if (modelEditor.getModelId().equals(elementID))
            {
                modelEditor.setModelName(elementName);
            }

            final String jsLabel = EscapeFunctions.toJSString(elementName);
            final String jsDescription = EscapeFunctions.toJSString(elementDescription);
            final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
            final String processIDPrefix = "Prozess_";
            String selectedElementIDPref = elementID.subSequence(0, 8).toString();
            String jsCode = "if (" + jsContentWindow + " != null){" + // Formatter hack. Keep format as intended
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var cell = graph.getSelectionCell();\n" + //
                    "if (cell != null){\n" + //
                    "var model = graph.getModel(); " + //
                    "model.beginUpdate();" + //
                    "try {" + //
                    "cell.setAttribute('label', '" + jsLabel + "');" + //
                    "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'label', '" + jsLabel + "'));" + //
                    "cell.setAttribute('description', '" + jsDescription + "');" + //
                    "model.execute(new " + jsContentWindow + ".mxCellAttributeChange(cell, 'description', '" + jsDescription + "'));" + //
                    "} finally {" + //
                    "model.endUpdate(); }" + //
                    "graph.refresh(cell);}" + //
                    "}\n"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);

            MenuBarView.showProcessElementAttributes(getModelEditor().getModelId(), elementID);
            if (selectedElementIDPref.equals(processIDPrefix))
            {
                ModelEditorView.changeTabTitle(getModelEditor().getModelId(), elementID, elementName);
            }

        }
        return null;
    }
}
