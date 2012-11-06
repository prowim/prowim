/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 05.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
 *This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.portal.controller.knowledge;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.collections.KnowledgeObjectArray;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.OrganisationArray;
import org.prowim.datamodel.collections.PersonArray;
import org.prowim.datamodel.collections.ProcessArray;
import org.prowim.datamodel.collections.ProcessTypeArray;
import org.prowim.datamodel.collections.RoleArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.KnowledgeDomain;
import org.prowim.datamodel.prowim.KnowledgeLink;
import org.prowim.datamodel.prowim.KnowledgeObject;
import org.prowim.datamodel.prowim.Mean;
import org.prowim.datamodel.prowim.Organization;
import org.prowim.datamodel.prowim.Person;
import org.prowim.datamodel.prowim.Process;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.datamodel.prowim.ResultsMemory;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.DomainFolder;
import org.prowim.portal.models.tree.model.DomainLeaf;
import org.prowim.portal.models.tree.model.FolderLeaf;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectsFolder;
import org.prowim.portal.models.tree.model.MeanLeaf;
import org.prowim.portal.models.tree.model.OrganizationElementsFolder;
import org.prowim.portal.models.tree.model.OrganizationLeaf;
import org.prowim.portal.models.tree.model.OrganizationPersonLeaf;
import org.prowim.portal.models.tree.model.OrganizationUnitsLeaf;
import org.prowim.portal.models.tree.model.PersonLeaf;
import org.prowim.portal.models.tree.model.ProcessCategoryLeaf;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessElementsFolder;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeRootLeaf;
import org.prowim.portal.models.tree.model.ResultsMemoryLeaf;
import org.prowim.portal.models.tree.model.RoleLeaf;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.process.ProcessModelEditorView;



/**
 * Controller to fill models with data to fill a tree.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class KnowledgeStructureController
{
    /**
     * 
     * Create tree with processType->process->knowledge
     * 
     * @return Return a empty tree, when anything wrong, else a TreeNode
     */
    public DefaultLeaf getProcessRootData()
    {
        DefaultLeaf root = new DefaultLeaf("", "");

        ProcessTypeArray procTypeArray = MainController.getInstance().getAllTopProcessTypes();

        for (ProcessType processType : procTypeArray)
        {
            ProcessTypeLeaf node = new ProcessTypeLeaf(processType.getID(), processType.getName());
            root.addChild(getEnabledProcesses(node));
        }
        return root;
    }

    /**
     * 
     * Create tree with processType->process->knowledge
     * 
     * @return {@link ProcessTypeLeaf}. Not null.
     */
    public DefaultLeaf getTopProcessTypes()
    {
        ProcessTypeRootLeaf processTypeRoot = new ProcessTypeRootLeaf(GlobalConstants.DUMMY_ID,
                                                                      Resources.Frames.Process.Texts.PROCESS_TYPES.getText());

        ProcessTypeArray procTypeArray = MainController.getInstance().getAllTopProcessTypes();

        for (ProcessType processType : procTypeArray)
        {
            ProcessTypeEditorLeaf node = new ProcessTypeEditorLeaf(processType.getID(), processType.getName());

            ProcessTypeArray processTypesDown = MainController.getInstance().getProcessCategories(node.getID());
            for (Iterator iterator = processTypesDown.iterator(); iterator.hasNext();)
            {
                ProcessType processSubType = (ProcessType) iterator.next();
                ProcessTypeLeaf nodeInner = new ProcessTypeLeaf(processSubType.getID(), processSubType.getName());
                ProcessTypeLeaf dummyModel = new ProcessTypeLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText());
                nodeInner.addChild(dummyModel);

                node.addChild(nodeInner);
            }

            processTypeRoot.addChild(node);
        }

        return processTypeRoot;
    }

    /**
     * 
     * Returns sub processes to given {@link Process}id
     * 
     * @param leaf {@link ProcessLeaf}
     * @return {@link DefaultLeaf}
     */
    public ProcessLeaf getSubprocesses(ProcessLeaf leaf)
    {
        // Get the Subprocesses
        ProcessLeaf processModel = leaf;
        processModel.getChildren().clear();
        List<Process> subProcessesList = MainController.getInstance().getSubProcesses(leaf.getID());
        for (Process process : subProcessesList)
        {
            processModel.addChild(getSubprocesses(new ProcessLeaf(process)));
        }

        return processModel;
    }

    /**
     * 
     * Create tree with processType->process. This shows all process models, also if the process would be not enabled.
     * 
     * @param leaf {@link ProcessTypeRootLeaf}
     * 
     * @return Return a empty tree, when anything wrong, else a TreeNode
     */
    public ProcessTypeRootLeaf getProcessModelEditorRootData(ProcessTypeRootLeaf leaf)
    {
        ProcessTypeRootLeaf rootDomain = leaf;
        rootDomain.getChildren().clear();

        // Get all process types and the get the process of each process type
        ProcessTypeArray procTypeArray = MainController.getInstance().getAllTopProcessTypes();

        for (ProcessType processType : procTypeArray)
        {
            ProcessTypeEditorLeaf node = new ProcessTypeEditorLeaf(processType.getID(), processType.getName());
            rootDomain.addChild(getProcessTypeEditorNode(node));
        }

        return rootDomain;
    }

    /**
     * Gets all processes and their categories, independing from their enable or disable state. All is shown. <br>
     * Create tree with processType->process. This shows all process models, also if the process would be not enabled. <br>
     * This is used in {@link ProcessModelEditorView} to show all processes.
     * 
     * @param completeData <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * 
     * @return Return a empty tree, when anything wrong, else a TreeNode
     */
    public DefaultLeaf getProcessTypeRootLeaf(boolean completeData)
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        ProcessTypeRootLeaf processTypeRootLeaf = new ProcessTypeRootLeaf(GlobalConstants.DUMMY_ID,
                                                                          Resources.Frames.Process.Texts.PROCESSES.getText());

        if ( !completeData)
        {
            ProcessTypeEditorLeaf knowModel = new ProcessTypeEditorLeaf(GlobalConstants.DUMMY_ID,
                                                                        Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText());
            processTypeRootLeaf.addChild(knowModel);
        }
        else
            processTypeRootLeaf = getProcessModelEditorRootData(processTypeRootLeaf);

        root.addChild(processTypeRootLeaf);

        return root;

    }

    /**
     * 
     * Create tree with process elements as root
     * 
     * @param completeData <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * 
     * @return Return a empty tree, when anything wrong, else a TreeNode
     * 
     *         REVIEWED
     */
    public DefaultLeaf getProcessElementRootData(boolean completeData)
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        ProcessElementsFolder listOfPrcessElem = new ProcessElementsFolder();

        // Get data from DB
        ProcessTypeArray procTypeArray = MainController.getInstance().getAllTopProcessTypes();
        for (ProcessType processType : procTypeArray)
        {
            ProcessCategoryLeaf node = new ProcessCategoryLeaf(processType.getID(), processType.getName());
            if ( !completeData)
            {
                node.addChild(new ProcessCategoryLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            }
            else
            {
                node = getProcessAndElements(node, true);
            }

            listOfPrcessElem.addChild(node);
        }

        root.addChild(listOfPrcessElem);

        return root;
    }

    /**
     * 
     * Returns all {@link Process}s and there's {@link ProcessElement}s. It gets first the {@link ProcessType}s and than sets to each {@link ProcessType} its {@link Process}s
     * 
     * @param leaf {@link ProcessElementsFolder}
     * @return {@link ProcessElementsFolder}
     */
    public ProcessElementsFolder getProcessElementRootData(ProcessElementsFolder leaf)
    {
        ProcessElementsFolder root = leaf;
        root.getChildren().clear();

        // Get data from DB
        ProcessTypeArray procTypeArray = MainController.getInstance().getAllTopProcessTypes();
        for (ProcessType processType : procTypeArray)
        {
            ProcessCategoryLeaf node = new ProcessCategoryLeaf(processType.getID(), processType.getName());
            node = getProcessAndElements(node, true);

            root.addChild(node);
        }

        return root;
    }

    /**
     * Get information to domain to build tree. Domains can included sub domains and WOBs.
     * 
     * @param completeData <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * 
     * @return {@link DefaultLeaf}. Not null.
     * 
     *         REVIEWED
     */
    public DefaultLeaf getDomainRootData(boolean completeData)
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        DomainFolder rootDomain = new DomainFolder();

        if ( !completeData)
        {
            rootDomain.addChild(new DomainLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
        }
        else
            rootDomain = getDomainRootNode(rootDomain);

        root.addChild(rootDomain);

        return root;
    }

    /**
     * 
     * Get data of organization elements.
     * 
     * @param completeData <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * 
     * @return {@link DefaultLeaf} not null
     */
    public DefaultLeaf getOrganiazationElementRootData(boolean completeData)
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        OrganizationElementsFolder organizationElementFolder = new OrganizationElementsFolder();
        OrganizationUnitsLeaf organizationUnitLeaf = new OrganizationUnitsLeaf();
        OrganizationPersonLeaf orgPersonModel = new OrganizationPersonLeaf();

        if ( !completeData)
        {
            organizationUnitLeaf.addChild(new OrganizationLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            organizationElementFolder.addChild(organizationUnitLeaf);
            orgPersonModel.addChild(new OrganizationPersonLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            organizationElementFolder.addChild(orgPersonModel);
        }
        else
        {
            organizationElementFolder.addChild(getOrganizationUnitsNode(organizationUnitLeaf));
            organizationElementFolder.addChild(getOrganizationPersonNode(orgPersonModel));
        }

        root.addChild(organizationElementFolder);

        return root;
    }

    /**
     * 
     * Get all knowledge objects and put these in the tree.
     * 
     * @param completeData <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * 
     * @return DefaultLeaf
     * 
     *         REVIEWED
     */
    public DefaultLeaf getKnowledgeObjectRootData(boolean completeData)
    {
        DefaultLeaf root = new DefaultLeaf("", "");
        KnowledgeObjectsFolder knowObjModel = new KnowledgeObjectsFolder();

        if ( !completeData)
        {
            knowObjModel.addChild(new KnowledgeObjectLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
        }
        else
            knowObjModel = getAllKnowObjects(knowObjModel);

        root.addChild(knowObjModel);

        return root;
    }

    /**
     * 
     * Get sub nodes of given object.
     * 
     * @param leaf Given object can be KnowledgeModel, ProcessModel, ProcessTypeModel and ActivityModel
     * 
     * @return object. may be null
     */
    public DefaultLeaf getSubNode(final DefaultLeaf leaf)
    {
        if (leaf instanceof DomainLeaf)
        {
            return getDomainNode((DomainLeaf) leaf);
        }
        else if (leaf instanceof DomainFolder)
        {
            return getDomainRootNode((DomainFolder) leaf);
        }
        else if (leaf instanceof KnowledgeObjectLeaf)
        {
            return getKnowObjectNode((KnowledgeObjectLeaf) leaf);
        }
        else if (leaf instanceof ProcessLeaf)
        {
            return getProcessNode((ProcessLeaf) leaf);
        }
        else if (leaf instanceof ProcessEditorLeaf)
        {
            return getProcessEditorNode((ProcessEditorLeaf) leaf);
        }
        else if (leaf instanceof ProcessStructureLeaf)
        {
            return getProcessStructureNode((ProcessStructureLeaf) leaf);
        }
        else if (leaf instanceof ProcessTypeLeaf)
        {
            return getProcessTypeNode((ProcessTypeLeaf) leaf);
        }
        else if (leaf instanceof ProcessTypeEditorLeaf)
        {
            return getProcessTypeEditorNode((ProcessTypeEditorLeaf) leaf);
        }
        else if (leaf instanceof ActivityLeaf)
        {
            return getActivityNode((ActivityLeaf) leaf);
        }
        else if (leaf instanceof OrganizationUnitsLeaf)
        {
            return getOrganizationUnitsNode((OrganizationUnitsLeaf) leaf);
        }
        else if (leaf instanceof OrganizationPersonLeaf)
        {
            return getOrganizationPersonNode((OrganizationPersonLeaf) leaf);
        }
        else if (leaf instanceof KnowledgeObjectsFolder)
        {
            return getAllKnowObjects((KnowledgeObjectsFolder) leaf);
        }
        else if (leaf instanceof OrganizationLeaf)
        {
            return getOrganizationNode((OrganizationLeaf) leaf);
        }
        else if (leaf instanceof RoleLeaf)
        {
            return getRolePreSelctList((RoleLeaf) leaf);
        }
        else if (leaf instanceof MeanLeaf)
        {
            return getMeanElements((MeanLeaf) leaf);
        }
        else if (leaf instanceof ResultsMemoryLeaf)
        {
            return getResMemElements((ResultsMemoryLeaf) leaf);
        }
        else if (leaf instanceof FolderLeaf)
        {
            return getProcessElements((FolderLeaf) leaf);
        }
        else if (leaf instanceof ProcessCategoryLeaf)
        {
            return getProcessAndElements((ProcessCategoryLeaf) leaf, true);
        }
        else if (leaf instanceof ProcessTypeRootLeaf)
        {
            return getProcessModelEditorRootData((ProcessTypeRootLeaf) leaf);
        }
        else if (leaf instanceof ProcessElementsFolder)
        {
            return getProcessElementRootData((ProcessElementsFolder) leaf);
        }

        return null;
    }

    /**
     * Get all enabled processes and returns it as {@link ProcessCategoryLeaf} <br>
     * 
     * Returns elements of {@link ProcessCategoryLeaf}. These can be other {@link ProcessCategoryLeaf}s or {@link ProcessStructureLeaf}s
     * 
     * @see MainController#getEnabledProcesses(String)
     * @param leaf {@link ProcessCategoryLeaf}. Not null.
     * @param completeFlag <code>true</code> if the method should load the complete data in tree all at once, <code>false</code> load only the root node. The sub roots will than load by clicking the nodes itself.
     * @return {@link ProcessCategoryLeaf}. Not null.
     */
    public ProcessCategoryLeaf getProcessAndElements(ProcessCategoryLeaf leaf, boolean completeFlag)
    {
        long currentTimeMillis = System.currentTimeMillis();
        ProcessCategoryLeaf category = leaf;
        category.getChildren().clear();

        // Add the included processes
        ProcessArray processArray = MainController.getInstance().getEnabledProcesses(category.getID());

        System.out.println("getEnabledProcesses " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        currentTimeMillis = System.currentTimeMillis();
        for (Process process : processArray)
        {
            ProcessStructureLeaf processModel = new ProcessStructureLeaf(process);
            if ( !completeFlag)
            {
                processModel.addChild(new ProcessStructureLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            }
            else
                processModel = getProcessStructureNode(processModel);

            category.addChild(processModel);
        }

        System.out.println("For schleife getProcessStructureNode " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        currentTimeMillis = System.currentTimeMillis();
        // Add the included sub types
        ProcessTypeArray processTypesDown = MainController.getInstance().getProcessCategories(category.getID());

        System.out.println("getProcessCategories " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        currentTimeMillis = System.currentTimeMillis();

        for (ProcessType processType : processTypesDown)
        {
            ProcessCategoryLeaf nodeInner = new ProcessCategoryLeaf(processType.getID(), processType.getName());
            if ( !completeFlag)
            {
                nodeInner.addChild(new ProcessCategoryLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            }
            else
                nodeInner = getProcessAndElements(nodeInner, completeFlag);

            category.addChild(nodeInner);
        }

        System.out.println("getProcessAndElements Schleife " + (System.currentTimeMillis() - currentTimeMillis) + " ms");

        System.out.println("createProcessElementsTree " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return category;
    }

    /**
     * Return the global process elements. Leaf should be a {@link FolderLeaf}
     * 
     * @param leaf {@link FolderLeaf}
     * @return {@link DefaultLeaf}
     */
    private FolderLeaf getProcessElements(FolderLeaf leaf)
    {
        FolderLeaf model = leaf;
        if ( !leaf.getID().equals(GlobalConstants.ROOT_MODEL))
        {
            model.getChildren().clear();

            if (leaf.getID().equals(GlobalConstants.ROLE))
            {
                RoleArray roleArray = MainController.getInstance().getGlobalRoles();
                for (Role role : roleArray)
                {
                    model.addChild(new RoleLeaf(role));
                }
            }
            else if (leaf.getID().equals(GlobalConstants.MEAN))
            {
                ObjectArray objectArray = MainController.getInstance().getGlobalMeans();
                for (Object object : objectArray)
                {
                    model.addChild(new MeanLeaf((Mean) object));
                }

            }
            else if (leaf.getID().equals(GlobalConstants.RESULTS_MEMORY))
            {
                ObjectArray objectArray = MainController.getInstance().getGlobalResultsMem();
                for (Object object : objectArray)
                {
                    model.addChild(new ResultsMemoryLeaf((ResultsMemory) object));
                }
            }
        }

        return model;
    }

    /**
     * 
     * Get the knowledge objects to given model.
     * 
     * @param leaf This is the selected leaf.
     * @return {@link DefaultLeaf}
     */
    private DefaultLeaf getKnowledgeObjects(final DefaultLeaf leaf)
    {
        DefaultLeaf model = leaf;
        model.getChildren().clear();

        // Get to each template the knowledge bases from DB and add this in tree
        List<KnowledgeObject> listOfKnow = MainController.getInstance().getKnowledgeObjects(model.getID());
        for (KnowledgeObject knowledgeObject : listOfKnow)
        {
            model.addChild(getKnowledgeObjectChildren(knowledgeObject));
        }

        return model;
    }

    /**
     * Get the knowledge objects to a result memory and give model back.
     * 
     * @param leaf The selected leaf in tree. Should bee {@link ResultsMemoryLeaf}
     * @return {@link ResultsMemoryLeaf}
     */
    private ResultsMemoryLeaf getResMemElements(ResultsMemoryLeaf leaf)
    {
        ResultsMemoryLeaf model = leaf;
        model.getChildren().clear();

        model = (ResultsMemoryLeaf) getKnowledgeObjects(leaf);

        return model;
    }

    /**
     * Get the knowledge objects to a mean and give model back.
     * 
     * @param leaf The selected leaf in tree. Should bee {@link MeanLeaf }
     * @return {@link MeanLeaf }
     */
    private MeanLeaf getMeanElements(MeanLeaf leaf)
    {
        MeanLeaf model = leaf;
        model.getChildren().clear();

        model = (MeanLeaf) getKnowledgeObjects(leaf);
        return model;
    }

    /**
     * Get elements to a process in structure tree.
     * 
     * @param leaf {@link ProcessStructureLeaf}
     * @return {@link ProcessStructureLeaf}, not null
     */
    public ProcessStructureLeaf getProcessStructureNode(final ProcessStructureLeaf leaf)
    {
        ProcessStructureLeaf model = leaf;
        model.getChildren().clear();

        // get first all knowledge objects of this process
        model = (ProcessStructureLeaf) getKnowledgeObjects(model);

        // get the process elements
        ObjectArray listOfElements = new ObjectArray();
        listOfElements.add(GlobalConstants.ACTIVITY);
        listOfElements.add(GlobalConstants.ROLE);
        listOfElements.add(GlobalConstants.MEAN);
        listOfElements.add(GlobalConstants.RESULTS_MEMORY);

        for (Object element : listOfElements)
        {
            FolderLeaf folderLeaf = null;

            ObjectArray elements = MainController.getInstance().getProcessElementsInstances(model.getID(), element.toString());
            if ( !elements.isEmpty())
                folderLeaf = new FolderLeaf(element.toString(), getTypeLabel(element.toString()));

            for (Object innerObj : elements)
            {
                if (element.toString().equals(GlobalConstants.ACTIVITY))
                {
                    ActivityLeaf actualModel = new ActivityLeaf((Activity) innerObj);
                    folderLeaf.addChild(getActivityNode(actualModel));
                }
                else if (element.toString().equals(GlobalConstants.ROLE))
                {
                    RoleLeaf actualModel = new RoleLeaf((Role) innerObj);
                    folderLeaf.addChild(getRolePreSelctList(actualModel));
                }
                else if (element.toString().equals(GlobalConstants.MEAN))
                {
                    MeanLeaf actualModel = new MeanLeaf((Mean) innerObj);
                    folderLeaf.addChild(getMeanElements(actualModel));
                }
                else if (element.toString().equals(GlobalConstants.RESULTS_MEMORY))
                {
                    ResultsMemoryLeaf actualModel = new ResultsMemoryLeaf((ResultsMemory) innerObj);
                    folderLeaf.addChild(getResMemElements(actualModel));
                }
            }

            if (folderLeaf != null)
                model.addChild(folderLeaf);
        }

        // Get the Subprocesses
        List<Process> subProcessesList = MainController.getInstance().getSubProcesses(model.getID());
        for (Process process : subProcessesList)
        {
            ProcessStructureLeaf subModel = new ProcessStructureLeaf(process);
            model.addChild(getProcessStructureNode(subModel));
        }

        return model;
    }

    /**
     * 
     * Get preselected person to a Role
     * 
     * @param leaf Selected {@link RoleLeaf}
     * @return {@link RoleLeaf} model with preselected persons as children
     */
    public RoleLeaf getRolePreSelctList(final RoleLeaf leaf)
    {
        leaf.getChildren().clear();
        RoleLeaf actualModel = leaf;
        actualModel = (RoleLeaf) getKnowledgeObjects(actualModel);
        PersonArray selectedObjArray = MainController.getInstance().getPreSelection(actualModel.getID());

        for (Person person : selectedObjArray)
        {
            actualModel.addChild(new PersonLeaf(person));
        }

        return actualModel;
    }

    /**
     * Maps label from type
     * 
     * @param type Given {@link ProcessElement}.
     * 
     * @return non null
     */
    public String getTypeLabel(String type)
    {
        if (type.equals(GlobalConstants.ACTIVITY))
            return Resources.Frames.Global.Texts.ACTIVITIES.getText();
        else if (type.equals(GlobalConstants.MEAN))
            return Resources.Frames.Global.Texts.MEANS.getText();
        else if (type.equals(GlobalConstants.ROLE))
            return Resources.Frames.Global.Texts.ROLES.getText();
        else if (type.equals(GlobalConstants.PROCESS_INFORMATION))
            return Resources.Frames.Global.Texts.PROCESS_INFORMATIONS.getText();
        else if (type.equals(GlobalConstants.PRODUCT_WAY))
            return Resources.Frames.Global.Texts.PRODUCT_WAYS.getText();
        else if (type.equals(GlobalConstants.CONTROL_FLOW))
            return Resources.Frames.Global.Texts.CONTROL_FLOWS.getText();
        else if (type.equals(GlobalConstants.RESULTS_MEMORY))
            return Resources.Frames.Global.Texts.RESULTS_MEMORY.getText();
        else if (type.equals(GlobalConstants.WORK))
            return Resources.Frames.Global.Texts.WORKS.getText();
        else if (type.equals(GlobalConstants.FUNCTION))
            return Resources.Frames.Global.Texts.FUNCTIONS.getText();
        else if (type.equals(GlobalConstants.PRODUCT))
            return Resources.Frames.Global.Texts.PRODUCTS.getText();
        else
            throw new IllegalArgumentException("Unknown type: " + type);
    }

    /**
     * Get all knowledge domains.
     * 
     * @param leaf {@link DomainFolder}
     * @return {@link DomainFolder}
     * 
     *         REVIEWED
     */
    private DomainFolder getDomainRootNode(final DomainFolder leaf)
    {
        DomainFolder rootDomain = leaf;
        rootDomain.getChildren().clear();

        long currentTimeMillis = System.currentTimeMillis();

        List<KnowledgeDomain> listOfDomains = MainController.getInstance().getTopDomainKnowledge();

        for (KnowledgeDomain knowledgeDomain : listOfDomains)
        {
            DomainLeaf domainModel = new DomainLeaf(knowledgeDomain.getID(), knowledgeDomain.getName());
            rootDomain.addChild(getDomainNode(domainModel));
        }

        System.out.println("getDomainRootNode " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return rootDomain;
    }

    /**
     * returns all persons and sub organizations to given organization.
     * 
     * @param leaf {@link OrganizationLeaf}
     * @return {@link OrganizationLeaf}
     */
    private OrganizationLeaf getOrganizationNode(final OrganizationLeaf leaf)
    {
        OrganizationLeaf model = leaf;
        model.getChildren().clear();

        model = (OrganizationLeaf) getKnowledgeObjects(leaf);
        // Get roles to given organization
        FolderLeaf processElementModel = new FolderLeaf(GlobalConstants.DUMMY_ID, getTypeLabel(GlobalConstants.ROLE));

        // Add the existing roles in this Organization
        List<Role> roleList = MainController.getInstance().getRolesToOrganization(model.getID());
        for (Role role : roleList)
        {
            if (role != null)
            {
                processElementModel.addChild(new RoleLeaf(role));
            }
        }

        if (roleList.size() > 0)
            model.addChild(processElementModel);

        // Get persons/users to given organization
        PersonArray personArray = MainController.getInstance().getMembers(model.getID());
        for (Person person : personArray)
        {
            model.addChild(new PersonLeaf(person));
        }

        // Get sub organizations to given organization
        OrganisationArray orgaArray = MainController.getInstance().getSubOrganizations(model.getID());
        for (Organization organization : orgaArray)
        {
            OrganizationLeaf orgaModel = new OrganizationLeaf(organization);
            orgaModel = getOrganizationNode(orgaModel);
            model.addChild(orgaModel);
        }

        return model;
    }

    /**
     * Get all knowledge objects for given node.
     * 
     * @param leaf {@link KnowledgeObjectsFolder}
     * @return {@link KnowledgeObjectsFolder}
     */
    private KnowledgeObjectsFolder getAllKnowObjects(final KnowledgeObjectsFolder leaf)
    {
        KnowledgeObjectsFolder knowObjModel = leaf;
        knowObjModel.getChildren().clear();
        long currentTimeMillis = System.currentTimeMillis();

        KnowledgeObjectArray knowObjArray = MainController.getInstance().getKnowledgeObjects();
        for (KnowledgeObject knowledgeObject : knowObjArray)
        {
            knowObjModel.addChild(getKnowledgeObjectChildren(knowledgeObject));
        }

        System.out.println("getAllKnowObjects " + (System.currentTimeMillis() - currentTimeMillis) + " ms");
        return knowObjModel;
    }

    /**
     * Get all persons for given node.
     * 
     * @param leaf {@link OrganizationPersonLeaf}
     * @return {@link OrganizationPersonLeaf}
     */
    private OrganizationPersonLeaf getOrganizationPersonNode(final OrganizationPersonLeaf leaf)
    {
        OrganizationPersonLeaf orgPersonModel = leaf;
        orgPersonModel.getChildren().clear();

        // Get all users
        PersonArray personArray = MainController.getInstance().getUsers();
        for (Person person : personArray)
        {
            orgPersonModel.addChild(new PersonLeaf(person));
        }

        return orgPersonModel;
    }

    /**
     * Get all organizations to given node.
     * 
     * @param leaf {@link OrganizationUnitsLeaf}
     * @return {@link OrganizationUnitsLeaf}
     */
    private OrganizationUnitsLeaf getOrganizationUnitsNode(final OrganizationUnitsLeaf leaf)
    {
        OrganizationUnitsLeaf orgUnitModel = leaf;
        orgUnitModel.getChildren().clear();

        OrganisationArray listOfOrga = MainController.getInstance().getTopOrganizations();
        for (Organization organization : listOfOrga)
        {
            orgUnitModel.addChild(getOrganizationNode(new OrganizationLeaf(organization)));
        }

        return orgUnitModel;
    }

    /**
     * Build node for this given activity.
     * 
     * @param leaf {@link ActivityLeaf}
     * @return {@link ActivityLeaf}
     */
    private ActivityLeaf getActivityNode(final ActivityLeaf leaf)
    {
        ActivityLeaf actModel = leaf;
        actModel.getChildren().clear();
        actModel = (ActivityLeaf) getKnowledgeObjects(leaf);
        return actModel;
    }

    /**
     * Build node for given process.
     * 
     * @param leaf {@link ProcessLeaf}
     * @return {@link ProcessLeaf}
     */
    private ProcessLeaf getProcessNode(final ProcessLeaf leaf)
    {
        ProcessLeaf processModel = leaf;
        processModel.getChildren().clear();

        // Get to each template the knowledge bases from DB and add this in tree
        List<KnowledgeObject> listOfKnow = MainController.getInstance().getKnowledgeObjects(processModel.getID());
        for (KnowledgeObject knowledgeObject : listOfKnow)
        {
            processModel.addChild(knowledgeObject);
        }

        // Get Activities
        List<Activity> activityList = MainController.getInstance().getActivities(processModel.getID());
        for (Activity activity : activityList)
        {
            ActivityLeaf activityModel = new ActivityLeaf(activity);
            activityModel.addChild(new ActivityLeaf(GlobalConstants.DUMMY_ID, Resources.Frames.Tree.Texts.LOAD_TREE_NODE.getText()));
            processModel.addChild(activityModel);
        }

        return processModel;
    }

    /**
     * Build node for given domain.
     * 
     * @param leaf {@link DomainLeaf}
     * @return {@link DomainLeaf}
     * 
     *         REVIEWED
     */
    private DomainLeaf getDomainNode(final DomainLeaf leaf)
    {
        DomainLeaf domainModel = leaf;
        domainModel.getChildren().clear();

        // Get knowledge object of given domain
        List<KnowledgeObject> knowledgeObjectList = MainController.getInstance().getDomainKnowObj(domainModel.getID());
        for (KnowledgeObject knowledgeObject : knowledgeObjectList)
        {
            domainModel.addChild(getKnowledgeObjectChildren(knowledgeObject));
        }

        // Get sub domains of given domain
        List<KnowledgeDomain> subdomainList = MainController.getInstance().getSubDomainKnow(domainModel.getID());
        for (KnowledgeDomain knowledgeDomain : subdomainList)
        {
            DomainLeaf subDomain = new DomainLeaf(knowledgeDomain.getID(), knowledgeDomain.getName());
            domainModel.addChild(getDomainNode(subDomain));
        }

        return domainModel;
    }

    /**
     * 
     * Returns children of a node with which is defined as {@link ProcessTypeLeaf}
     * 
     * @param leaf Defined as {@link ProcessTypeLeaf}. Not null.
     * @return {@link ProcessTypeLeaf}. Not null.
     */
    public ProcessTypeLeaf getProcessTypeNode(final ProcessTypeLeaf leaf)
    {
        ProcessTypeLeaf typeModel = leaf;
        typeModel.getChildren().clear();
        ProcessTypeArray processTypes = MainController.getInstance().getProcessCategories(typeModel.getID());
        Iterator<ProcessType> typesIt = processTypes.iterator();
        while (typesIt.hasNext())
        {
            ProcessType type = typesIt.next();
            ProcessTypeLeaf node = new ProcessTypeLeaf(type.getID(), type.getName());
            // Get the process to this type.

            if ( !MainController.getInstance().getTypeProcesses(type.getID()).isEmpty()
                    || !MainController.getInstance().getProcessCategories(typeModel.getID()).isEmpty())
            {
                node.addChild("dummy");
            }

            typeModel.addChild(node);
        }
        ProcessArray processes = MainController.getInstance().getTypeProcesses(typeModel.getID());
        for (Process process : processes)
        {
            typeModel.addChild(new ProcessLeaf(process));
        }

        return typeModel;
    }

    /**
     * Gets only processes which are enabled. Disabled processes are only shown in {@link #getProcessTypeRootLeaf(boolean)} <br>
     * Returns children of a node with which is defined as {@link ProcessTypeLeaf}.
     * 
     * @see MainController#getEnabledProcesses(String)
     * @param leaf Defined as {@link ProcessTypeLeaf}. Not null.
     * @return {@link ProcessTypeLeaf}. Not null.
     */
    public ProcessTypeLeaf getEnabledProcesses(final ProcessTypeLeaf leaf)
    {
        Validate.notNull(leaf, "Leaf should not be null.");
        ProcessTypeLeaf typeModel = leaf;
        typeModel.getChildren().clear();

        // receive the categories and add them to the root node
        ProcessTypeArray processTypes = MainController.getInstance().getProcessCategories(typeModel.getID());
        for (ProcessType processType : processTypes)
        {
            ProcessTypeLeaf node = new ProcessTypeLeaf(processType.getID(), processType.getName());
            typeModel.addChild(getEnabledProcesses(node));
        }

        // just now add the processes to their categories
        ProcessArray processes = MainController.getInstance().getEnabledProcesses(typeModel.getID());
        for (Process process : processes)
        {
            ProcessLeaf processLeaf = new ProcessLeaf(process);
            typeModel.addChild(getSubprocesses(processLeaf));
        }

        return typeModel;
    }

    /**
     * 
     * Get the {@link ProcessType}s to given {@link ProcessTypeEditorLeaf}. This is used to get process types in {@link ProcessModelEditorView}
     * 
     * @param leaf Given {@link ProcessType}. Not <code>null</code>
     * @return {@link ProcessTypeEditorLeaf}. Not null.
     */
    private ProcessTypeEditorLeaf getProcessTypeEditorNode(final ProcessTypeEditorLeaf leaf)
    {
        ProcessTypeEditorLeaf typeModel = leaf;
        typeModel.getChildren().clear();

        // Get the process types of given process type
        ProcessTypeArray processTypes = MainController.getInstance().getProcessCategories(typeModel.getID());
        for (ProcessType processType : processTypes)
        {
            ProcessTypeEditorLeaf node = new ProcessTypeEditorLeaf(processType.getID(), processType.getName());
            // Get the process to this type.
            typeModel.addChild(getProcessTypeEditorNode(node));
        }

        // Get processes of given process type
        ProcessArray processes = MainController.getInstance().getTypeProcesses(typeModel.getID());
        for (Process process : processes)
        {
            ProcessEditorLeaf processTopLevel = new ProcessEditorLeaf(process);
            typeModel.addChild(getProcessEditorNode(processTopLevel));
        }

        return typeModel;
    }

    private ProcessEditorLeaf getProcessEditorNode(final ProcessEditorLeaf leaf)
    {
        ProcessEditorLeaf typeModel = leaf;
        typeModel.getChildren().clear();
        List<Process> subProcesses = MainController.getInstance().getSubProcesses(typeModel.getID());
        for (Process process : subProcesses)
        {
            typeModel.addChild(new ProcessEditorLeaf(process));
        }

        return typeModel;
    }

    /**
     * 
     * Returns all knowledge links and persons to given knowledge object.
     * 
     * @param knowObj {@link KnowledgeObject} given Knowledge object.
     * @return {@link KnowledgeObject}
     * 
     *         REVIEWED
     */
    public KnowledgeObjectLeaf getKnowledgeObjectChildren(KnowledgeObject knowObj)
    {
        KnowledgeObjectLeaf childNode = new KnowledgeObjectLeaf(knowObj);

        // Get persons of knowledge object
        PersonArray personArray = knowObj.getResponsiblePersons();
        for (Person person : personArray)
        {
            childNode.addChild(new PersonLeaf(person));
        }

        // Get knowledge link of knowledge object
        List<KnowledgeLink> listKnowLink = knowObj.getKnowledgeLinks();
        for (KnowledgeLink knowledgeLink : listKnowLink)
        {
            childNode.addChild(new KnowledgeLinkLeaf(knowledgeLink));
        }

        return childNode;
    }

    /**
     * Build node for given knowledge.
     * 
     * @param leaf {@link KnowledgeObjectLeaf}
     * @return {@link KnowledgeObjectLeaf}
     */
    private KnowledgeObjectLeaf getKnowObjectNode(final KnowledgeObjectLeaf leaf)
    {
        KnowledgeObjectLeaf knowledgeModel = leaf;
        knowledgeModel.getChildren().clear();

        KnowledgeObject knowledgeObject = MainController.getInstance().getKnowlegdeObj(knowledgeModel.getID());

        List<Object> list = getKnowledgeObjectChildren(knowledgeObject).getChildren();
        if ( !list.isEmpty())
            knowledgeModel.setChildren(list);

        return knowledgeModel;
    }
}
