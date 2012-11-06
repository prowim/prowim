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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.rwt.internal.widgets.JSExecutor;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.KnowledgeObjectDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.string.EscapeFunctions;


/**
 * Shows a dialog to create a new {@link KnowledgeObject}. The arguments comprised the id of selected element. This can be all elements in the ontology. <br>
 * Return value is null. That means, that no action should executed in JS.
 * 
 * Just now null is always returned as a workaround, because JavaScript is not still waiting for a dialog's return value, see http://bugs.ebcot.info/view.php?id=4405
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
@SuppressWarnings("restriction")
public class BFAddKnowledge extends AbstractModelEditorFunction
{

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-sie name of the function.
     */
    BFAddKnowledge(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * Description see {@link BFAddKnowledge}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {
        Action act = Resources.Frames.Knowledge.Actions.ADD_WOB.getAction();

        // gets the valid arguments and use it
        Iterator<String> iterator = ArgumentValidator.convert(args).iterator();
        String elementID = iterator.next();

        boolean okFlag = new KnowledgeObjectDialog(null, act, "").openDialog(elementID);

        String classOfElement = MainController.getInstance().getDirectClassOfInstance(elementID);
        if (okFlag && classOfElement.equals(GlobalConstants.Ontology.ACTIVITY))
        {
            // Update the overlay in view
            final String jsContentWindow = org.eclipse.rwt.lifecycle.WidgetUtil.getId(getBrowser()) + ".children[0].contentWindow";
            if (elementID != null && !elementID.equals(""))
            {
                String jsActivityID = EscapeFunctions.toJSString(elementID);

                String jsCode = "if (" + jsContentWindow + " != null){" + //
                        "var graph = " + jsContentWindow + ".ApplicationEventsHandler.editor.graph; " + //
                        "var overlays = " + jsContentWindow + ".EditorFunctions.cache.overlays['" + jsActivityID + "']; " + //
                        "if (overlays.knowledgeList  == undefined){" + //
                        "overlays.knowledgeList = new Array(); " + //
                        "}" + //
                        "overlays.knowledgeList = " + getNamesOfKnowledgeObjects(elementID) + ";" + //
                        "var cell = " + "graph.getSelectionCell();" + //
                        "if (cell != null){" + //
                        jsContentWindow + ".EditorFunctions.updateOverlay(" + jsContentWindow + ".ApplicationEventsHandler.editor, cell); " + //
                        "graph.refresh(cell);}}"; //

                // IServiceStateInfo stateInfo = ContextProvider.getStateInfo();
                // JavaScriptResponseWriter writer = stateInfo.getResponseWriter();
                // writer.append(jsCode);
                JSExecutor.executeJS(jsCode);
            }

        }
        return null;
    }

    /**
     * 
     * Returns names of knowledge objects to given activity id
     * 
     * @param elementID . not null
     * @return Object[]. array of role names. Empty, if no role exists
     */
    private String getNamesOfKnowledgeObjects(String elementID)
    {

        List<KnowledgeObject> knowList = MainController.getInstance().getKnowledgeObjects(elementID);

        String[] array = new String[knowList.size()];

        for (int i = 0; i < knowList.size(); i++)
        {
            array[i] = EscapeFunctions.toJSString(knowList.get(i).getName());
        }

        String list = StringUtils.join(array, "\",\"");
        list = "[\"" + list + "\"]";

        return list;
    }

}
