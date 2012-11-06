/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-13 11:47:38 +0200 (Fr, 13 Aug 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/ProcessEngineConstants.java $
 * $LastChangedRevision: 4605 $
 *------------------------------------------------------------------------------
 * (c) 20.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine;

/**
 * Defines constants like rules names and variables names defined in the protege project. Those rules and variables are needed for the process management.
 * 
 * @author Saad Wardi
 * @version $Revision: 4605 $
 * @deprecated Use {@link EngineConstants} instead
 */
@Deprecated
public final class ProcessEngineConstants
{

    /* ******************************* Rules***************************************** */
    /**
     * Rules.
     * 
     * @author Saad Wardi
     * @version $Revision: 4605 $
     */
    public static final class Rules
    {

        /**
         * Process rules.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Process
        {

            /** Gets process type. Input: processID. Output: process type. */
            public final static String GET_PROCESSTYPE                     = "getProcessType";

            /** Gets all process types. Input: -. Output: record of process types. */
            public final static String GET_ALL_PROCESS_TYPES               = "getAllProcessTypes";

            /** Create a process type. Input: name , description. Output: record of process types. */
            public final static String CREATE_PROCESS_TYPE                 = "createProcessType";

            /** Set the process type of a process. Input: ID of process type , ID of process. Output: record of process types. */
            public final static String SET_PROCESS_TYPE                    = "setProcessType";

            /** Gets all processes. Input: -. Output: record of processes. */
            public final static String GET_ALLPROCESSES                    = "getAllProcesses";

            /** Gets all executable processes. Input: userID. Output: record of processes. */
            public final static String GET_EXECUTABLE_PROCESSES            = "getStartableProcesses";

            /** Gets all running processes. Input: userID. Output: record of processes. */
            public final static String GET_RUNNING_PROCESSES               = "getRunningProcesses";

            /** Gets all running processes. Input: userID. Output: record of processes. */
            public final static String GET_FINISHED_PROCESSES              = "getFinishedProcesses";

            /** Gets process information. */
            public final static String GET_PROCESS_INFORMATION             = "getProcessInformation";

            /** Gets process name. */
            public final static String GET_PROCESS_INSTANCE_IDENT          = "CL4_getInstanzbezeichnung";

            /** Activates the start activity. */
            public final static String ACTIVATE_START_ACTIVITY             = "activateStartActivity";

            /** Sets the process starter. */
            @Deprecated
            public final static String SET_STARTER                         = "CL4_setStarter";

            /** Sets the process starter. */
            public final static String SET_PROCESS_STARTER                 = "setProcessStarter";

            /** Creates the process relations. */
            @Deprecated
            public final static String CREATE_PROCESS_RELATION             = "CL_createProcessRelations";

            /** Creates the process relations. */
            public final static String CREATE_PROCESS_RELATIONS            = "createProcessRelations";

            /** Creates the process relations local. */
            @Deprecated
            public final static String CREATE_PROCESS_RELATIONS_LOCAL      = "CL_createInstanceRelationsLocal";

            /** Creates the process relations local. */
            public final static String CREATE_LOCAL_PROC_RELATIONS         = "createInstanceOfLocalRelations";

            /** Creates the process relations global. */
            @Deprecated
            public final static String CREATE_PROCESS_RELATIONS_GLOBAL     = "CL_createInstanceRelationsGlobalAndAttribut";

            /** Creates the global relations and attributes of process. */
            public final static String CREATE_GLOBAL_PROC_REL_AND_ATTR     = "createInstanceOfGlobalRelationsAndAttributs";

            /** Creates the process id elements. */
            public final static String CREATE_PROCESS_INSTANCE_ELEMENTS    = "createInstanceElements";

            /** Creates the process id. */
            public final static String CREATE_PROCESS_INSTANCE             = "createProcessInstance";

            /** Clones a process template */
            public final static String CLONE_PROCESS_TEMPLATE              = "cloneProcessTemplate";

            /** Sets a template as active Version */
            public final static String SET_AS_ACTIVE_VERSION               = "setAsActiveVersion";

            /** Sets a template as inactive Version */
            public final static String SET_AS_INACTIVE_VERSION             = "setAsInactiveVersion";

            /** Gets process parameters. */
            public static final String GET_PROCESS_PARAMETERS              = "getProcessParameters";

            /** Gets process parameters. */
            public static final String GET_PROCESS_TEMPLATE_PARAMETERS     = "getProcessTemplateParameters";

            /** Removes a process. */
            public static final String DELETE_PROCESS                      = "CL_deleteProcess";

            /** Removes a process. */
            public static final String DELETE_PROCESS_INSTANCES            = "deleteInstances";

            /** Gets the parameter selection list. */
            public static final String GET_PARAMETER_VALUE                 = "getParameterValue";

            /** Gets the parameter selection list. */
            public static final String GET_PARAMETER_SCALAR_VALUE          = "getParameterScalarValue";

            /** Gets the parameter possible selection list. */
            public static final String GET_PARAMETER_SELECT_LIST           = "getParameterSelectList";

            /** Gets the path to process browser chart. */
            public static final String GET_PROCESS_BROWSERCHARTPATH        = "getProcessBrowserChartPath";

            /** Gets the ID of the processinformation. */
            public static final String GET_NOT_EDITABLE_PROCESSINFORMATION = "getNotEditableProcessInformations";

            /** Gets the ID of the start activity. */
            public static final String GET_START_ACTIVITY                  = "getStartActivity";

            /** Checks if a process is template. */
            public static final String IS_PROCESS_TEMPLATE                 = "isProcessTemplate";

            /** Sets the parent of a processtype. */
            public static final String SET_PROCESS_TYPE_PARENT             = "setProcessTypeParent";
            /** Gets getSubProcessTypes. */
            public static final String GET_SUB_PROCESSTYPES                = "getSubProcessTypes";
            /** getTypeProcesses. */
            public static final String GET_TYPE_PROCESSES                  = "getTypeProcesses";
            /** getAllProcessTypesWithoutSubs. */
            public static final String GET_ALL_TOP_PROCESS_TYPES           = "getAllProcessTypesWithoutSubs";

            /**
             * Rules for the subprocesses.
             * 
             * @author !!YOUR NAME!!
             * @version $Revision: 4605 $
             */
            public static final class Subprocess
            {
                /** Gets the subprocesses. ASK. */
                public static final String GET_SUB_PROCESSES          = "CL_getSubprozesse";

                /** Connects a subprocess. TELL. */
                public static final String CONNECT_SUB_PROCESS        = "CL_connectSubprozess";

                /** Connects the subprocess input. TELL. */
                public static final String CONNECT_SUB_INPUT          = "CL_connectSubInput";

                /** Connects the subprocess output. TELL. */
                public static final String CONNECT_SUB_OUTPUT         = "CL_connectSubOutput";

                /** Connects the subprocess roles. TELL. */
                public static final String CONNECT_SUB_ROLES          = "CL_connectSubRolle";

                /** Connects the subprocess elements. */
                public static final String CONNECT_SUB_ELEMENTS       = "CL_connectSubelements";
                /** Get the sub process for activity. */
                public final static String GET_SUBPROCESS_ACITVITY    = "CL4_getSubprozessA";
                /** Activates the start activity of the subprocess. */
                public final static String START_ACTIVITY_SUB_PROCESS = "CL4_StartaktivitaetSUBAktivieren";

                /** GETS THE Subprocesses. */
                public final static String GET_SUBPROCESSES           = "getSubprocesses";

                private Subprocess()
                {
                }
            }

            private Process()
            {
            }

        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Activity
        {
            /** Gets all active activities for the logged user. Input: userID. Output: record of activities. */
            public final static String GET_MYACTIVEACTIVITIES          = "getMyActiveActivities";
            /** Gets all activities defined in a process. Input: userID. Output: record of activities. */
            public final static String GET_ACTIVITIES                  = "getActivities";
            /** Gets all active activities. Input: userID. Output: record of activities. */
            public final static String GET_ACTIVE_ACTIVITIES           = "getActiveActivities";
            /** Gets the processinformation to a product. */
            public final static String GET_PRODUCT_PROCESSINFORMATIONS = "getProductProcessInformations";
            /** Gets the Output products. */
            public static final String GET_OUTPUT_PRODUCTS             = "getActivityOutputProducts";
            /** Gets the Output products. */
            public static final String GET_INPUT_PRODUCTS              = "getActivityInputProducts";
            /** Gets the value of the Freigabetyp slot defined on the Activity. */
            public static final String GET_ENABLE_TYPE                 = "getEnableType";
            /** Gets the rule used to finish the activity. */
            public static final String GET_DONE_RULE                   = "CL6_getFertigRegel";
            /** Sets the Activity status. */
            public static final String SET_ACTIVITY_STATE              = "setActivityState";
            /** Clears the fertiggesetzt slot value. */
            public static final String CLEARS_DONE                     = "CL6_clearFertiggesetzt";
            /** Gets the activate rule. */
            public static final String GET_ACTIVATE_RULE               = "CL4_getAKRegel";
            /** Get the controlflows rule. */
            public final static String GET_CONTROLFLOW_RULE            = "CL4_getKFRegel";
            /** Standard activation rule. */
            public static final String STANDARD_ACTIVATION             = "_CL4_RGA_Standardaktivierung";
            /** Get the activating activity for a subprocess. */
            public final static String GET_CALL_PROCESS                = "CL4_getCallprozess";
            /** Sets the action to the status "done". */
            public final static String SET_ACTION_DONE                 = "CL4_setTaetigkeitFertig";
            /** Gets persons of the activity. */
            public final static String GET_ACTIVITY_PERSONS            = "CL4_getPersonenVonAktivitaet";
            /** Gets the responsible persons for the activity. */
            public final static String GET_RESPONSIBLE_PERSONS         = "getResponsiblePersonForActivity";
            /** Gets the persons. */
            public final static String GET_ASSIGNEDPERSONS_INTO_ROLES  = "CL4_getFertigPersonen";
            /** Activation standard rule. */
            public final static String STANDARD_ACTIVATION_RULE        = "_CL4_Standard_AND";
            /** Sends the activity. */
            public final static String SEND_EMAIL_FOR_ACTIVITY         = "sendArbeitsauftrag";
            /** Checks if a relation is a product. */
            public static final String IS_PRODUCT                      = "isProduct";
            /** Gets the active auto activities. */
            public static final String GET_ACTIVE_AUTO_ACTIVITIES_OLD  = "CL4_getActiveAutoActivities";

            /** Gets times activities. */
            public static final String GET_TIMES_ACTIVITIES            = "CL4_getTerminActivities";

            /** Gets the activity entity. */
            public static final String GET_ACTIVITY                    = "getActivity";

            private Activity()
            {
            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class ControlFlow
        {
            /** Gets ControlFlow. */
            public final static String GET_CONTROLFLOW         = "getControlFlow";
            /** Sets a controlflow activ. */
            public final static String SET_CONTROLFLOW_ACTIV   = "CL4_setKFaktiv";
            /** Sets a controlflow to no activ. */
            public final static String SET_CONTROLFLOW_INACTIV = "CL4_setKFinaktiv";
            /** Gets the ID of the activity. */
            public final static String GET_ACTIVITY_ID         = "getFrameToOutputProduct";

            private ControlFlow()
            {

            }

        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Knowledge
        {

            /** Gets the top level domains. Input: -. Output: record of domains. */
            public final static String GET_TOPLEVELDOMAINS                         = "CL4_getToplevelDomaenen";
            /** Gets the top level domains. Input: -. Output: record of domains. */
            public final static String GET_SUBDOMAINS                              = "CL4_getSubdomaenen";
            /** Gets instance knowledgeObjects. */
            public final static String GET_INSTANCE_KNOWLEDGEOBJECTS               = "getInstanceKnowledgeObjects";
            /** Gets all Knowledgeobjects. */
            public final static String GET_KNOWLEDGEOBJECTS                        = "getKnowledgeObjects";
            /** Gets the top level domains. Input: -. Output: record of KnowledgeDomains. */
            public final static String GET_KNOWLEDGEOBJECTS_DOMAINS                = "getKnowledgeObjectsDomains";
            /** Adds a knowledgeobject to a process element. */
            public final static String ADD_KNOWLEDGEOBJECT_TO_PROCESS_ELEMENT      = "CL5_addWissensobjekt";
            /** Generates a new knowledgeobject andd adds it to a processelement. */
            public final static String GENERATES_KNOWLEDGE_OBJECT_WITH_ELEMENT     = "genKnowledgeObjectWithElement";
            /** Generates a new knowledgeobject andd adds it to a processelement. */
            public final static String GENERATES_KNOWLEDGE_OBJECT                  = "genKnowledgeObject";
            /** Generates a new knowledgelink. */
            public final static String GENERATES_KNOWLEDGE_LINK                    = "genKnowledgeLink";
            /** Get a knowledge object. */
            public final static String GET_KNOWLEDGE_OBJECT                        = "getKnowledgeObject";
            /** Get a knowledge link. */
            public static final String GET_KNOWLEDGE_LINK                          = "getKnowledgeLink";
            /** Get the knowledge repository. */
            public static final String GET_KNOWLEDGE_REPOSITORY                    = "getKnowledgeRepositories";
            /** Get the knowledge domain. */
            public static final String GET_KNOWLEDGE_DOMAIN                        = "getKnowledgeDomain";
            /** Get the business domains. */
            public static final String GET_BUSINESS_DOMAINS                        = "getBusinessDomains";
            /** Get the persons ID that domiates the knowledgeobject. */
            public static final String GET_KNOWLEDGE_OBJECT_DOMINATORS             = "getKnowledgeObjectDominators";
            /** Get the links to a knowledgeobject. */
            public final static String GET_KNOWLEDGE_OBJECT_LINKS                  = "getKnowledgeObjectLinks";
            /** Get all knowledgeobjects. */
            public static final String GET_ALL_KNOWLEDGE_OBJECTS                   = "getAllKnowledgeObjects";
            /** Get the knowledgeobject creator. */
            public static final String GET_KNOWLEDGE_OBJECT_CREATOR_NUMBER         = "getKnowledgeObjectCreatorNumber";
            /** Get the knowledgeobject creator. */
            public static final String GET_KNOWLEDGE_OBJECT_CREATOR                = "getKnowledgeObjectCreator";
            /** Search in the name of links. */
            public static final String SEARCH_LINKS                                = "searchLinks";
            /** Search in the name of KnowledgeObjects. */
            public static final String SEARCH_KNOWLEDGEOBJECTS                     = "searchKnowledgeObjects";
            /** Search in the name of KnowledgeDomains. */
            public static final String SEARCH_KNOWLEDGEDOMAINS                     = "searchKnowledgeDomains";
            /** The count of the links. */
            public static final String GET_KNOWLEDGE_LINKS_NUMBER                  = "getCountOfKnowledgeLinks";
            /** Gets the links used in the knowledgeObjct */
            public static final String GET_KNOWLEDGELINKS_FOR_OBJECTS              = "getKnowledgeLinkForKnowledgeObject";
            /** removes a knowledgeobject from an element. */
            public static final String REMOVE_KNOWLEDGEOBJECT_FROM_PROCESS_ELEMENT = "removeKnowledgeObject";

            private Knowledge()
            {

            }

        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Document
        {
            /** Gets the document identification for a knowledgelink stored in the ontology. */
            public final static String GET_DOCUMENT_IDENTIFICATION    = "getDocumentIdentification";
            /** Gets the document identification for a processinformation. */
            public final static String GET_DOCUMENT_IDENTIFICATION_PI = "getDocumentIdentificationForPI";

            private Document()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Role
        {
            /** Gets the assigend person to a role. */
            public final static String GET_ASSIGNED_PERSONS      = "getAssignedPersons";
            /** Gets the the list of roles defined in a process. Input: processID. Output: record of Roles. */
            public final static String GET_ROLES                 = "getRoles";
            /** Gets the rules to be executed to assigne the roles. */
            public static final String GET_ASSIGENEMT_RULE       = "CL4_Prozessrollen";
            /** Gets the role. */
            public static final String GET_ROLE                  = "getRole";
            /** Get the preselection persons set. */
            public final static String GET_PRE_SELECTION         = "getPreSelection";
            /** Get the activity roles. */
            public final static String GET_ACTIVITY_ROLES        = "getActivityRoles";
            /** Adds a role to an organization. */
            public static final String ADD_ROLE_TO_ORGANIZATION  = "addRoleToOrganization";
            /** Gets roles to an organization. */
            public static final String GET_ROLES_TO_ORGANIZATION = "getRolesToOrganization";

            private Role()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Mean
        {
            /** Gets the assigend person to a role. */
            public final static String GET_FUND = "getMittel";

            private Mean()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class ResultsMemory
        {
            /** Gets the results memory () ontology : ablage. */
            public final static String GET_RESULTSMEMORY = "getResultsMemory";

            private ResultsMemory()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Work
        {
            /** Gets the results memory () ontology : ablage. */
            public final static String GET_WORK = "getWork";

            private Work()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Function
        {
            /** Gets the results memory () ontology : ablage. */
            public final static String GET_FUNCTION = "getFunction";

            private Function()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Product
        {
            /** Gets the product. */
            public final static String GET_PRODUCT    = "getProduct";
            /** Gets the productway. */
            public final static String GET_PRODUCTWAY = "getProductWay";

            private Product()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class ProcessInformation
        {
            /** Gets the processinformation. */
            public final static String GET_PROCESSINFORMATION             = "getProcessinformation";
            /** Generates a process information. */
            public final static String GENERATE_PROCESSINFORMATION        = "genProcessInformation";
            /** Get the frame id where the processinformation is defined. */
            public final static String GET_FRAME_ID_TO_PROCESSINFORMATION = "getFrameToProcessInformation";
            /** Checks if a frame vom infotype. */
            public static final String IS_FRAME_FROMINFOTYPE              = "isFrameFromInformationType";
            /** Checks if a frame vom infotype document. */
            public static final String IS_FRAME_FROMINFOTYPE_DOCUMENT     = "isInfoTypeDocument";

            private ProcessInformation()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Organisation
        {

            /** Gets the rules output parameter. Input: ruleName. Output: record of Parameter. */
            public final static String GET_RULE_OUTPUT       = "getRuleOutputParameters";

            /** Gets the the list of registered users. Input: -. Output: record of Person. */
            public final static String GET_USERS             = "getUsers";

            /** Gets the person with a given id. */
            public final static String GET_PERSON            = "getUser";
            /** Gets the registered organisations. */
            public static final String GET_ORGANISATIONS     = "getOrganisations";
            /** Gets the top registered organizations. */
            public static final String GET_TOP_ORGANIZATIONS = "getTopOrganizations";
            /** Gets the sub organizations to given organization ID. */
            public static final String GET_SUB_ORGANIZATIONS = "getSubOrganizations";

            /** Gets the organisation infos with a given id. */
            public static final String GET_ORGANISATION      = "getOrganisation";
            /** Get the memebers of an organization. */
            public static final String GET_MEMBERS           = "getMembers";

            private Organisation()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Admin
        {

            /** Gets the prowim system version. Input: -. Output: record of Version. */
            public final static String GET_PROWIM_VERSION                   = "getVersion";
            /** Set the user password. */
            public static final String CHANGE_USER_PASSWORD                 = "changePassword";
            /** Get the user. */
            public static final String CHECK_USER_PASSWORD                  = "checkUserPassword";
            /** Seachrs for persons. */
            public static final String SEARCH_PERSONS                       = "searchPersons";

            /** Get user with name. */
            public static final String GET_USER_WITH_NAME                   = "getUserWithName";
            /** Checks if a username exists. */
            public static final String CHECK_USERNAME                       = "tstBenutzernameUnique";
            /** Gets the user rights roles. */
            public static final String GET_RIGHTS_ROLES                     = "getUserRightRoles";
            /** Gets the role authorizations. */
            public static final String GET_ROLE_AUTHORIZATIONS              = "getRoleAuthorization";
            /** checkCallAllowed. */
            public static final String CHECK_CALL_ALLOWED                   = "checkCallAllowed";
            /** Gets the user password. */
            public static final String GET_USER_PASSWORD                    = "getUserPassword";
            /** Checks if a person with username can modify an entity with id = entityID.. */
            public static final String CHECK_PERSON_CAN_MODIFIY_ENTITY      = "checkUserEntityAccess";
            // "checkPersonCanModifyEntity";
            /** Set a person with username can modify an entity with id = entityID. */
            public static final String SET_PERSON_CAN_MODIFIY_ENTITY        = "setPersonCanModifyEntity";
            /** Gets the rights roles. */
            public static final String GET_RIGHTSROLES                      = "getRightsRolesAssignedToUser";
            /** Gets all RoghtsRoles. */
            public static final String GET_ALL_RIGHTSROLES                  = "getRightsRoles";
            /** isPersonModeler . */
            public static final String IS_PERSON_MODELER                    = "isPersonModeler";

            /** isPersonAdmin . */
            public static final String IS_PERSON_ADMIN                      = "isPersonAdmin";
            /** isPersonReader. */
            public static final String IS_PERSON_READER                     = "isPersonReader";
            /** getPersonCanModifyEntity. */
            public static final String GET_PERSON_CAN_MODIFIY_ENTITY        = "getPersonCanModifyEntity";
            /** getParentProcess. */
            public static final String GET_PARENT_PROCESS                   = "getParentProcess";
            /** getOrganizationsCanModifyEntity. */
            public static final String GET_ORGANIZATIONS_CAN_MODIFIY_ENTITY = "getOrganizationsCanModifyEntity";

            private Admin()
            {

            }
        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Common
        {
            /** Gets the process information types */
            public final static String GET_INFORMATION_TYPES                 = "getInformationTypes";
            /** Gets the InformationType. */
            public static final String GET_INFORMATION_TYPE                  = "getInformationType";
            /** Sets a slot value */
            public static final String SET_SLOT_VALUE                        = "setSlotValue";
            /** Clears a slot value. */
            public static final String CLEAR_SLOT_VALUE                      = "clearSlotValue";
            /** Clears the values of a relation. */
            public static final String CLEAR_RELATION_VALUE                  = "clearRelationValue";
            /** Gets the rule name to be executed to get the possible selection list for a process information frame */
            public static final String GET_SELECTION_ATTRIBUTE               = "getSelectionAttribute";
            /** Gets the rule name to be executed from information type to get the possible selection list for a process information frame */
            public static final String GET_SELECTION_ATTRIBUTE_FROM_INFOTYPE = "getSelectionAttributeFromInfoType";
            /** Gets the values of the slots Minimum, Maximum , Pflichtfeld for a PROCESSINFORMATION_CHART . */
            public static final String GET_PROCESSINFORMATION_CONSTRAINT     = "getProcessInformationConstraint";
            /** Get template id, name. */
            public static final String GET_TEMPLATE_INFORMATION              = "getTemplateInformation";
            /** Get template ID. */
            public final static String GET_TEMPLATE_ID                       = "getTemplateID";
            /** Get attribut value. */
            public final static String GET_VALUE                             = "CL4_getValue";
            /** Deletes an instance. */
            public final static String DELETE_INSTANCE                       = "CL_deleteInstance";

            /** Gets the processelements. */
            public static final String GET_PROCESSELEMENTS                   = "getProcessElements";
            /** Gets the processelements instances. */
            public static final String GET_PROCESSELEMENTS_INSTANCES         = "getProcessElementsInstances";
            /** Gets the version. */
            public static final String GET_ONTOLOGY_VERSION                  = "getVersion";
            /** Gets the infotape ID. */
            public static final String GET_INFORMATION_TYPE_ID               = "getInformationTypeID";

            private Common()
            {

            }
        }
    }

    /* *********************Variables************************************ * */
    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4605 $
     */
    public static final class Variables
    {
        /**
         * Process Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Process
        {

            /** Id of process. */
            public final static String PROCESS_ID          = "?processID";

            /** Id of process instance. */
            public final static String PROCESS_INSTANCE_ID = "?processInsID";

            /** type of process. */
            public final static String PROCESS_TYPE        = "?processType";
            /** PROCESS_TYPE_NAME. */
            public final static String PROCESS_TYPE_NAME   = "?processTypeName";
            /** PROCESS_TYPE_ID. */
            public final static String PROCESS_TYPE_ID     = "?processTypeID";
            /** PROCESS_TYPE_DESC. */
            public final static String PROCESS_TYPE_DESC   = "?processTypeDesc";

            /** type of process. */
            public final static String VAR_PROCESS_TYPE    = "?Vorgangsart";
            /** type of process. */
            public final static String PROCESS             = "?prozess";
            /** type of process. */
            public final static String DESCRIPTION_PROCESS = "?BEZp";
            /** type of process. */
            public final static String DESCRIPTION_PERSON  = "?BEZP";
            /** type of process. */
            public final static String ID_PROCESS          = "?IDp";
            /** type of process. */
            public final static String ID_PROCESS_UPPER    = "?IDP";
            /** the user id of the starter. */
            public final static String STARTER             = "?Starter";
            /** type of process. */
            public final static String CORE_PROCESS        = "prowimcore_Instance_120005";
            /** Identification of process. */
            public final static String PROCESS_IDENT       = "?processIdent";
            /** the process id id. */
            public final static String PROCESS_INST_ID     = "?PINS";
            /** the id of the process to be deleted. */
            public final static String DELETE_PROCESS_ID   = "?PR";
            /** . */
            public final static String SUBPROCESS_NUMBER   = "?NSUB";
            /** . */
            public final static String SUBPROCESS_ID       = "?P";
            /** . */
            public static final String SUBPROCESS_ID_DE    = "?SubprozessID";
            /** . */
            public static final String SUBPROCESS_NAME     = "?Subprozess";
            /** . */
            public static final String TEILPROCESS_ID      = "?TeilprozessID";
            /** . */
            public static final String TEIL_INSTANCE_ID    = "?TeilInsID";
            /** . */
            public static final String SUB_INS_ID          = "?SubInsID";
            /** . */
            public static final String TEIL_TEMP_ID        = "?TeilTempID";
            /** . */
            public static final String INS_ID              = "?InsID";
            /** . */
            public static final String MAIN                = "?MAIN";
            /** . */
            public static final String SUB                 = "?SUB";
            /** . */
            public static final String PARENT              = "?parent";

            private Process()
            {

            }

        }

        /**
         * Activity Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Activity
        {
            /** activity id. */
            public final static String ACTIVITY_ID           = "?IDA";
            /** activity finished attribute. */
            public final static String FINISHED              = "?fertiggesetzt";
            /** automatic. */
            public final static String ACTIVITY_AUTOMATIC    = "?automatic";
            /** activity automatic start value. */
            public final static String AUTOMATIC             = "Autom.";
            /** activity description. */
            public final static String DESCRIPTION_ACTIVITY  = "?BEZA";
            /** activity name. */
            public final static String ACTIVITY              = "?VORGANG";
            /** VID. */
            public final static String VID                   = "?VID";
            /** activity status. */
            public final static String STATUS                = "?status";
            /** activity done status. */
            public final static String STATUS_DONE           = "fertig";
            /** old rules valriable for activity. */
            public final static String ACTIVITY_FINISH       = "?VG";
            /** activity rule. */
            public final static String ACTIVITY_RULE         = "?AR";
            /** activity value. */
            public final static String ATTRIBUT_VALUE        = "?AT";
            /** ControlFlows. */
            public final static String ACTIVITY_CONTROLFLOWS = "?KF";
            /** acitivity. */
            public final static String ACTIVITY_DE           = "?AKT";
            /** DEB. */
            public static final String DEPENDENCY            = "?DEB";
            /** VR. */
            public final static String VR                    = "?VR";
            /** Teilprocess caller. */
            public final static String ACTIVITY_CALLER       = "?HP";
            /** Enable type "Jeder". */
            @Deprecated
            public final static String ENABLE_EVERYBODY      = "Jeder";
            /** Enable type "Keiner". */
            @Deprecated
            public final static String ENABLE_NOBODY         = "Keiner";
            /** Enable type "Alle". */
            @Deprecated
            public final static String ENABLE_ALL            = "Alle";
            /** Enable type "Verantwortlicher". */
            @Deprecated
            public final static String ENABLE_ASSIGNEDPERSON = "Verantwortlicher";
            /** Status. */
            public final static String ACTIVITY_STATUS       = "?STATUS";
            /** activity id old rules. */
            public final static String PRODUCT_ID            = "?productID";
            /** Term when the activity will be done. */
            public final static String TERM                  = "?Termin";

            private Activity()
            {
            }
        }

        /**
         * Knowledge Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Knowledge
        {
            /** . */
            public final static String KNOWLEDGEOBJECT        = "?wissensobjekt";
            /** . */
            public final static String KNOWLEDGEOBJECT_ID     = "?wissensobjektID";
            /** Wissensspeicher ID . */
            public final static String REPOSITORY_ID          = "?wsID";
            /** . */
            public final static String APPLIED                = "?ANGELEGT";
            /** . */
            public final static String SUBDOMAINS_COUNT       = "?anzsub";
            /** . */
            public final static String KNOWLEDGEOBJECTS_COUNT = "?anzwob";
            /** . */
            public final static String DOM                    = "?DOM";
            /** . */
            public final static String WO                     = "?WO";
            /** . */
            public final static String PROCESS_ELEMENT        = "?PE";
            /** . */
            public final static String KNOWLEDGEOBJECT_ID_EN  = "?knowledgeObjectID";
            /** . */
            public static final String KNOWLEDGELINK_ID       = "?knowledgeLinkID";

            /** . */
            public static final String LINK                   = "?link";
            /** . */
            public static final String REPOSITORY             = "?repository";
            /** . */
            public static final String LINK_TYPE              = "?linkType";
            /** . */
            public static final String DOMAINKNOWLEDGE_ID     = "?knowledgeDomainID";
            /** . */
            public static final String PATTERN                = "?pattern";

            private Knowledge()
            {

            }

        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class ControlFlow
        {
            /** . */
            public static final String ID = "?controlFlowID";

            private ControlFlow()
            {

            }

        }

        /**
         * Title and description.
         * 
         * @author !!YOUR NAME!!
         * @version $Revision: 4605 $
         */
        public static final class Organisation
        {
            /** . */
            public final static String ADDRESS         = "?address";
            /** . */
            public final static String TELEFON         = "?telefon";
            /** . */
            public final static String EMAIL           = "?Email";
            /** . */
            public final static String ORGANISATION_ID = "?orgID";

            /**
             * Person variables.
             * 
             * @author Saad Wardi
             * @version $Revision: 4605 $
             */
            public static final class Person
            {
                /** Id of person. */
                public final static String ID              = "?personID";
                /** ID of person. */
                public final static String PERSON          = "?Person";
                /** ID of person. */
                public final static String PERSON_UPP      = "?PERSON";
                /** . */
                public final static String SHORTNAME       = "?shortName";                  // "?Kuerzel"
                /** . */
                public final static String FIRSTNAME       = "?firstName";
                /** . */
                public final static String LASTNAME        = "?lastName";
                /** . */
                public final static String FUNKTIONBERECHT = "?Funktionsberechtigungsstufe";
                /** . */
                public final static String USER_ID         = "?userID";
                /** . */
                public final static String USER_NAME       = "?username";

                /** . */
                public final static String ORGANISATION    = "?ist_Mitglied_von";
                /** . */
                public final static String PERSON_ID       = "?p";
                /** . */
                public final static String ORG             = "?org";
                /** . */
                public final static String TITLE           = "?title";
                /** . */
                public static final String PASSWORD        = "?password";

                private Person()
                {
                }
            }

            private Organisation()
            {

            }

        }

        /**
         * Role Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Role
        {
            /** . */
            public final static String NAME             = "?Rolle";
            /** . */
            public final static String NAME_LARGE       = "?ROLLE";
            /** . */
            public final static String ROLLE_ID         = "?roleID";
            /** . */
            public final static String RULE_ASSIGENMENT = "?Regelbesetzung";

            private Role()
            {

            }
        }

        /**
         * Role Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Fund
        {
            /** fund ID . */
            public final static String FUND_ID = "?fundID";

            private Fund()
            {

            }
        }

        /**
         * Document Variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Document
        {
            /** . */
            public final static String UUID = "?uuid";

            /** . */
            public final static String PATH = "?path";

            private Document()
            {

            }
        }

        /**
         * Commons variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 4605 $
         */
        public static final class Common
        {
            /** ID . */
            public final static String ID                            = "?ID";
            /** Identifier. */
            public final static String IDENT                         = "?ident";
            /** INS. */
            public final static String INS                           = "?INS";
            /** Template id. */
            public final static String TEMPLATE_ID                   = "?PTEM";
            /** . */
            public final static String NAME                          = "?BEZ";
            /** . */
            public final static String NAME_EN                       = "?name";
            /** . */
            public final static String NAME_EN_M                     = "?Name";
            /** . */
            public final static String TYPE                          = "?TYP";
            /** . */
            public final static String DESCRIPTION_DEFAULT           = "?description";
            /** Version. */
            public final static String VAR_VERSION                   = "?Version";
            /** v. */
            public final static String VAR_VARIABLE                  = "?v";
            /** v. */
            public final static String VAR_VARIABLE_V                = "?V";

            /** Repository ID pattern. */
            public final static String REPOSITORY_ID_PATTERN         = "Wissensspeicher_";
            /** Count. */
            public final static String COUNT                         = "?ANZ";
            /** ruleName . */
            public final static String RULE_NAME                     = "?ruleName";
            /** output parameter. */
            public final static String OUTPUT_PARAM                  = "?outputParameter";
            /** . */
            public final static String DESCRIPTION                   = "?BEZT";
            /** start time attribute. */
            public final static String START                         = "?Start";
            /** end time attribute. */
            public final static String END                           = "?Ende";
            /** auto process. */
            public final static String AUTO                          = "?AUTO";
            /** value. */
            public final static String ATTRIBUTE_VALUE               = "?VALUE";
            /** unknown value. */
            public final static String UNKNOWN                       = "ungultig";
            /** unknown person. */
            public final static String UNKNOWN_PERSON                = "unknown";
            /** "aktiv". */
            public final static String ACTIV                         = "aktiv";
            /** ja. */
            public final static String OK                            = "ja";
            /** true as String. */
            public final static String TRUE                          = "true";
            /** Algernon true. */
            public final static String ALGERNON_TRUE                 = ":TRUE";
            /** false as String . */
            public final static String FALSE                         = "false";
            /** Algernon false. */
            public final static String ALGERNON_FALSE                = ":FALSE";
            /** Zero. */
            public final static String ZERO                          = "0";
            /** ONE . */
            public final static String ONE                           = "1";
            /** Null value. */
            public final static String NULL_VALUE                    = "NULL";
            /** empty String . */
            public final static String EMPTY                         = "";
            /** create time attribute. */
            public final static String CREATE_TIME                   = "?createTime";
            /** the prameterID. */
            public final static String PARAMETER_ID                  = "?paramID";
            /** prefix for the depending method name for reflection */
            public final static String CONVERT                       = "convert";
            /** the parameter select list. */
            public final static String PARAMETER_SELECT_LIST         = "?selectList";
            /** the relation id. */
            public final static String PARAMETER_RELATION_ID         = "?relationID";
            /** the relation content id. */
            public static final String PARAMETER_RELATION_CONTENT_ID = "?relationContentID";
            /** the template ID: ?templateID */
            public static final String TEMPLATE_ID_EN                = "?templateID";
            /** the slot value. */
            public static final String VALUE                         = "?value";
            /** the required slot. */
            public static final Object REQUIRED                      = "?required";
            /** the order. */
            public static final Object ORDER                         = "?order";
            /** the frame ID. */
            public static final String FRAME_ID                      = "?frameID";
            /** the slot id. like Inhalt_List. */
            public static final String SLOT_ID                       = "?slotID";
            /** . */
            public final static String PROCESS_ELEMENT_INSTANCE_ID   = "?processElementInstanceID";
            /** the process element ID. */
            public final static String PROCESS_ELEMENT_ID            = "?processElementID";
            /** the class id. */
            public static final String CLASS_ID                      = "?ClassID";
            /** the path. */
            public static final String PATH                          = "?path";
            /** the number of the persons. */
            public static final String NUMBER_PERSON                 = "?numPerson";
            /** the number of the links. */
            public static final String NUMBER_LINKS                  = "?numLinks";
            /** this is a suffix used in the name of the processinformation that contains the UUID of the chart. */
            public static final String PROCESSINFORMATION_CHART      = "_Chart_PI";
            /** editable. */
            public static final String EDITABLE                      = "?editable";
            /** Verweis. */
            public static final String VERWEIS                       = "?Verweis";
            /** element. */
            public static final String ELEMENT                       = "?element";
            /** element. */
            public static final String ELEMENT_ID                    = "?elementID";
            /** pattern. */
            public static final String PATTERN                       = "?pattern";
            /** servicename. */
            public static final String SERVICE_NAME                  = "?servicename";
            /** the called servicename. */
            public static final String CALL                          = "?call";

            /**
             * Title and description.
             * 
             * @author !!YOUR NAME!!
             * @version $Revision: 4605 $
             */
            public static final class InformationType
            {
                /** the name (id). */
                public final static String PARAMETER_NAME                 = "?parameterName";
                /** the denotation. */
                public final static String DENOTATION                     = "?denotation";
                /** the max value. */
                public final static String MAX_VALUE                      = "?maxValue";
                /** the min value. */
                public final static String MIN_VALUE                      = "?minValue";
                /** the content string. */
                public final static String CONTENT_STRING                 = "?contentString";
                /** the parameter instance id. */
                public final static String PARAMETER_INSTANCE_ID          = "?parameterInstanceID";
                /** the parameter template id. */
                public final static String PARAMETER_TEMPLATE_ID          = "?parameterTemplateID";
                /** the information type id. */
                public final static String INFORMATION_TYPE_NAME          = "?infoTypeName";
                /** the information type. */
                public final static String INFORMATION_TYPE               = "?infoType";
                /** the content of the information type. */
                public final static String INFORMATION_TYPE_CONTENT       = "?content";
                /** the content of the information type. */
                public final static String INFORMATION_TYPE_CONTENT_ID    = "?contentID";
                /** the content field. */
                public final static String INFORMATION_TYPE_CONTENT_FIELD = "?ablage";
                /** the parameter instance id. */
                public final static String PROCESSINFORMATION_ID          = "?processInformationID";
                /** the parameter instance id. */
                public final static String REFERENCE_ID                   = "?referenceID";

                private InformationType()
                {

                }
            }

            private Common()
            {
            }

        }

    }

}
