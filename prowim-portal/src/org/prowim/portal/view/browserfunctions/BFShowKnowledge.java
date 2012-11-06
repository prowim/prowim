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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.browserfunctions;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.viewers.TreeViewer;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.controller.knowledge.KnowledgeStructureController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.OrganizationPersonLeaf;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Shows the attributes info to given element id. The arguments comprised the id of {@link Activity}.<br>
 * 
 * Just now null is always returned as a workaround, because JavaScript is not still waiting for a dialog's return value, see http://bugs.ebcot.info/view.php?id=4405
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class BFShowKnowledge extends AbstractModelEditorFunction
{
    private TreeViewer  knowObjTree;
    private TreeViewer  personTree;
    private ModelEditor modelEditor = null;

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFShowKnowledge(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
        this.modelEditor = modelEditor;
    }

    /**
     * setKnowObjTree
     * 
     * @param knowObjTree {@link TreeViewer}, which shows the knowledge object of selected item.
     */
    public void setKnowObjTree(TreeViewer knowObjTree)
    {
        Validate.notNull(knowObjTree);

        this.knowObjTree = knowObjTree;
    }

    /**
     * setPersonTree
     * 
     * @param personTree {@link TreeViewer}, which shows the roles of selected item.
     */
    public void setPersonTree(TreeViewer personTree)
    {
        Validate.notNull(personTree);

        this.personTree = personTree;
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * Description see {@link BFShowKnowledge}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {

        // gets the valid arguments and use it
        List<String> argument = ArgumentValidator.convert(args);
        String elementType = argument.get(0);
        String elementID = argument.get(1);

        //
        if ( !elementID.equals(GlobalConstants.ZERO))
        {
            fillKnowObjTree(elementID);

            if (elementType.equals(GlobalConstants.ACTIVITY))
                fillPersonTree(elementID);
        }
        else
        {
            this.knowObjTree.setInput(new DefaultLeaf("", ""));
            this.knowObjTree.refresh();
            this.personTree.setInput(new DefaultLeaf("", ""));
            this.personTree.refresh();
        }

        return null;
    }

    /**
     * 
     * Fills the knowledge object tree with data to selected process or activity.
     * 
     * @param elementID
     */
    private void fillKnowObjTree(String elementID)
    {
        // Create the root node
        DefaultLeaf root = new DefaultLeaf("", "");

        String actualElementID = elementID;

        String subProcessID = MainController.getInstance().getSubProcessOfActivity(elementID);
        if (subProcessID != null)
        {
            actualElementID = subProcessID;
        }

        // Get the knowledge objects of process if other element than process is selected
        if (this.modelEditor.getModelId().equals(actualElementID) || subProcessID != null)
        {
            // Get knowledge objects of process itself
            List<KnowledgeObject> listOfKnowObj = MainController.getInstance().getKnowledgeObjects(actualElementID);
            DefaultLeaf processWob = new DefaultLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText()
                    + GlobalConstants.DOUBLE_POINT + " " + MainController.getInstance().getName(actualElementID));
            for (KnowledgeObject knowledgeObject : listOfKnowObj)
            {
                KnowledgeObjectLeaf knowObjModel = new KnowledgeStructureController().getKnowledgeObjectChildren(knowledgeObject);
                processWob.addChild(knowObjModel);

            }

            if (processWob.hasChildren())
                root.addChild(processWob);

            // Get knowledge objects of process elements
            List<KnowledgeObject> listOfPEKnowObj = MainController.getInstance().getKnowledgeObjectsOfProcess(actualElementID);

            DefaultLeaf allWob = new DefaultLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText()
                    + GlobalConstants.DOUBLE_POINT + " " + Resources.Frames.Knowledge.Texts.ALL_PROCESS_ELEMENTS.getText());
            for (KnowledgeObject knowledgeObject : listOfPEKnowObj)
            {
                KnowledgeObjectLeaf knowObjModel = new KnowledgeStructureController().getKnowledgeObjectChildren(knowledgeObject);
                allWob.addChild(knowObjModel);
            }

            if (allWob.hasChildren())
                root.addChild(allWob);
        }
        else
        {
            // Get the knowledge objects of process elements
            DefaultLeaf elementWob = new DefaultLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Knowledge.Texts.KNOW_OBJECTS.getText()
                    + GlobalConstants.DOUBLE_POINT + " " + MainController.getInstance().getName(actualElementID));

            List<KnowledgeObject> listOfKnowObj = MainController.getInstance().getKnowledgeObjects(actualElementID);

            for (KnowledgeObject knowledgeObject : listOfKnowObj)
            {
                KnowledgeObjectLeaf knowObjModel = new KnowledgeStructureController().getKnowledgeObjectChildren(knowledgeObject);
                elementWob.addChild(knowObjModel);
            }

            if (elementWob.hasChildren())
                root.addChild(elementWob);

        }

        this.knowObjTree.setInput(root);
        this.knowObjTree.refresh();
        this.knowObjTree.expandToLevel(2);
    }

    /**
     * 
     * Fills person tree with persons included in given role.
     * 
     * @param roleArray A list of {@link Role}
     */
    private void fillPersonTree(final String elementID)
    {
        DefaultLeaf root = new DefaultLeaf("", "");

        RoleArray roleArray = MainController.getInstance().getActivityRoles(elementID);

        OrganizationPersonLeaf orgaUnitModel = new OrganizationPersonLeaf();
        Iterator<Role> roleIt = roleArray.iterator();

        while (roleIt.hasNext())
        {
            PersonArray selectedObjArray = MainController.getInstance().getPreSelection(roleIt.next().getID());
            if (selectedObjArray != null)
            {
                Iterator<Person> itSelectedObj = selectedObjArray.iterator();

                while (itSelectedObj.hasNext())
                    orgaUnitModel.addChild(itSelectedObj.next());
            }
        }
        root.addChild(orgaUnitModel);
        this.personTree.setInput(root);
        this.personTree.refresh();
    }

}
