/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.eclipse.swt.browser.BrowserFunction;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Relation;
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
 * The elements does not shows in model editor. These will create only in database. <br>
 * The arguments comprised the id of {@link Process} of model, id of element, which should set in relation to the created element and a flag, which can be <br>
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
public class BFAddProcessElementToActivity extends AbstractModelEditorFunction
{

    /**
     * Description.
     * 
     * @param modelEditor reference browser, where the {@link BrowserFunction} is registered
     * @param name name of {@link BrowserFunction}.
     */
    BFAddProcessElementToActivity(ModelEditor modelEditor, String name)
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
        String processID = iterator.next();
        String activityID = iterator.next();
        String elementClass = iterator.next();

        // depending on the elementClass create now the necessary actions
        DefaultTableModel model = null;
        Action action = null;
        String description = "";
        if (elementClass.equals(GlobalConstants.ROLE))
        {
            RoleController controller = new RoleController();
            model = new DefaultTableModel(controller.getTableModel(), controller.getColumns());
            description = Resources.Frames.Dialog.Texts.DESC_EXIST_ROLES.getText();
            action = Resources.Frames.Dialog.Actions.SELECT_CREATE_ROLE.getAction();
        }
        else if (elementClass.equals(GlobalConstants.MEAN))
        {
            ProcessElementController controller = new ProcessElementController();
            model = new DefaultTableModel(controller.getTableModel(GlobalConstants.MEAN), controller.getColumns());
            description = Resources.Frames.Dialog.Texts.DESC_EXIST_MEAN.getText();
            action = Resources.Frames.Dialog.Actions.SELECT_CREATE_MEAN.getAction();
        }
        else
            throw new IllegalArgumentException("Unknown elementClass: " + elementClass);

        // Open the dialog to select or create a element
        String[] resultTxt = null;

        SelectElementDialog dialog = new SelectElementDialog(null, action, description, model);
        if (dialog.open() == IDialogConstants.OK_ID)
            resultTxt = dialog.getResults();

        String elementId = resultTxt[0];

        String newElementId = null;
        if (resultTxt != null && !elementId.equals("") && !elementId.equals(GlobalConstants.LOCAL_ELEMENT)
                && !elementId.equals(GlobalConstants.GLOBAL_ELEMENT))
        {
            // Set relation between element and process
            MainController.getInstance().setElementToModel(processID, elementId);
            newElementId = elementId;
        }
        // if an object should be created newly, create this object here and update the shape at model editor.
        else if (elementId.equals(GlobalConstants.LOCAL_ELEMENT) || elementId.equals(GlobalConstants.GLOBAL_ELEMENT))
        {

            if (elementClass.equals(GlobalConstants.ROLE))
            {
                newElementId = MainController.getInstance().createObject(processID, GlobalConstants.ROLE_DE);
            }
            else if (elementClass.equals(GlobalConstants.MEAN))
            {
                newElementId = MainController.getInstance().createObject(processID, GlobalConstants.MEAN_DE);
            }
            else
                throw new IllegalArgumentException("Unknown elementClass: " + elementClass);

            // If Global-Flag is selected then set the element as global, else local
            if (elementId.equals(GlobalConstants.GLOBAL_ELEMENT))
                MainController.getInstance().setScope(newElementId, GlobalConstants.GLOBAL);
            else
                MainController.getInstance().setScope(newElementId, GlobalConstants.LOCAL);

        }
        else
            throw new IllegalStateException("Unknown selection: " + resultTxt);

        // Create Connection and set it in relation with activity
        if (elementClass.equals(GlobalConstants.ROLE))
        {
            // Create a work relation between activity and role
            String workID = MainController.getInstance().createObject(processID, Relation.Classes.WORK);
            // Set relation between role and activity
            MainController.getInstance().connectActivityRole(activityID, newElementId, workID);
        }
        else if (elementClass.equals(GlobalConstants.MEAN))
        {
            // Create a function relation between activity and mean
            String functionID = MainController.getInstance().createObject(processID, Relation.Classes.FUNCTION);
            // Set relation between mean and activity
            MainController.getInstance().connectActivityMean(activityID, newElementId, functionID);
        }
        else
            throw new IllegalStateException("Unknown selection: " + resultTxt);

        // Update the overlay in view
        final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
        if (activityID != null && !activityID.equals(""))
        {
            String jsActivityID = EscapeFunctions.toJSString(activityID);

            String jsCode = "if (" + jsContentWindow + " != null){" + //
                    "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                    "var overlays = " + jsContentWindow + ".EditorFunctions.cache.overlays['" + jsActivityID + "']; " + //
                    buildArrayPart(elementClass, activityID) + //
                    "var cell = " + "graph.getSelectionCell();" + //
                    "if (cell != null){" + //
                    jsContentWindow + ".EditorFunctions.updateOverlay(" + jsContentWindow + ".ApplicationEventsHandler.editor, cell); " + //
                    "graph.refresh(cell);}}"; //

            // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
            // HtmlResponseWriter writer = stateInfo.getResponseWriter();
            // writer.append(jsCode);
            JSExecutor.executeJS(jsCode);
        }

        return null;
    }

    /**
     * 
     * Build the js code part to set the overlay informations
     * 
     * @param className Name of ontology classes. Can be {@link Role} or {@link Mean}.
     * @param activityID id of reference {@link Activity}, which is connected with {@link Role} or {@link Mean}.
     * @return String js code to set the arrays for overlay. Not null. Created exception if given class names or wrong
     */
    private String buildArrayPart(String className, String activityID)
    {
        String jsCode = "";
        if (className.equals(GlobalConstants.ROLE))
        {
            jsCode = "if (overlays.roleList  == undefined){" + //
                    "overlays.roleList = new Array(); " + //
                    "}" + //
                    "overlays.roleList = " + getRoleNames(activityID) + ";"; //
        }
        else if (className.equals(GlobalConstants.MEAN))
        {
            jsCode = "if (overlays.meanList  == undefined){" + //
                    "overlays.meanList = new Array(); " + //
                    "}" + //
                    "overlays.meanList = " + getMeanNames(activityID) + ";"; //
        }
        else
            throw new IllegalStateException("Unknown class name: " + className);

        return jsCode;
    }

    /**
     * 
     * Returns names of means to given activity id
     * 
     * @param elementID . not null
     * @return Object[]. array of mean names. Empty, if no role exists
     */
    private String getMeanNames(String elementID)
    {
        List<Mean> meanList = MainController.getInstance().getActivityMeans(elementID).getItem();

        String[] array = new String[meanList.size()];

        for (int i = 0; i < meanList.size(); i++)
        {
            array[i] = EscapeFunctions.toJSString(meanList.get(i).getName());
        }

        String list = StringUtils.join(array, "\",\"");
        list = "[\"" + list + "\"]";

        return list;
    }

    /**
     * 
     * Returns names of roles to given activity id
     * 
     * @param elementID . not null
     * @return Object[]. array of role names. Empty, if no role exists
     */
    private String getRoleNames(String elementID)
    {
        List<Role> roleList = MainController.getInstance().getActivityRoles(elementID).getItem();

        String[] array = new String[roleList.size()];

        for (int i = 0; i < roleList.size(); i++)
        {
            array[i] = EscapeFunctions.toJSString(roleList.get(i).getName());
        }

        String list = StringUtils.join(array, "\",\"");
        list = "[\"" + list + "\"]";

        return list;
    }

}
