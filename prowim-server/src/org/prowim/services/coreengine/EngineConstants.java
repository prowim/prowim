/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 10.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.prowim.datamodel.prowim.ProcessElement;


/**
 * This class included all constants, which are used here. The constants are actually divided in "EditorEngineConstants" <br>
 * "ProcessEngineConstants" and "UpdateEngineConstants". In this classes exists many constants from old-rules and <br>
 * many constants are redundant. The aim is to create unique keys for existing constants. Please create the new constants only in this <br>
 * class and delete the old constants from other classes if possible
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public class EngineConstants
{

    /******************************** Rules***************************************** */
    /**
     * All constants for rule names.
     * 
     * @author Maziar Khodaei
     * @version $Revision$
     * @since 2.0
     */
    public static final class Rules
    {

        /**
         * 
         * Rules for activity functions.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Common
        {
            /** Checks, if a instance is global or local. */
            public static final String IS_GLOBAL = "isGlobal";

            private Common()
            {
            }
        }

        /**
         * 
         * Rules for admin functions.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.2.0
         */
        public static final class Admin
        {
            /** Get persons of given right role. */
            public static final String GET_PERSONS_OF_RIGHT_ROLES = "getPersonsOfRightRoles";

            private Admin()
            {
            }
        }

        /**
         * 
         * Rules for workflow functions.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Workflow
        {
            /** Gets the active auto activities. */
            public static final String GET_ACTIVE_AUTO_ACTIVITIES                  = "getActiveAutoActivities";
            /** sets a job finished with given person. */
            public static final String SET_JOB_FINISH                              = "setJobFinish";
            /** Gets persons, who are finished a activity */
            public static final String GET_PERSONS_FINISHED_ACTIVITY               = "getPersonsFinishedActivity";

            /** Gets which set a activity or process done */
            public static final String GET_DONE_RULE                               = "getDoneRule";
            /** Get activate rule "hat_Aktivierungsregel" of one activity. */
            public static final String GET_ACTIVATE_RULE                           = "getActivateRule";
            /** Default activate rule "Aktivierungsregel" of one activity. */
            public static final String DEFAULT_ACTIVATE_RULE                       = "defaultActivateRule";
            /** Set given controlflow active. */
            public static final String SET_CONTROLFLOW_ACTIVE                      = "setControlFlowActive";
            /** Set given control flow inactive. */
            public static final String SET_CONTROLFLOW_INACTIVE                    = "setControlFlowInactive";
            /** Set given connection rule. */
            public static final String GET_CONNECTION_RULE                         = "getConnectionRule";
            /** Get sub process. */
            public static final String GET_SUB_PROCESSES                           = "getSubProcesses";
            /** Set attribute hat_submodel . */
            public static final String SET_HAS_SUB_MODEL_ATTR                      = "setHasSubModelAtrr";
            /** Set inputs of sub process . */
            public static final String SET_SUB_PROCESS_INPUTS                      = "setSubProcessInputs";
            /** Gets the possible selection values. */
            public static final String GET_POSSIBLE_SELECTION                      = "getPossibleSelection";
            /** Set outputs of sub process . */
            public static final String SET_SUB_PROCESS_OUTPUTS                     = "setSubProcessOutputs";
            /** Set roles of sub process . */
            public static final String SET_SUB_PROCESS_ROLES                       = "setSubProcessRoles";
            /** Set Elements of sub process in relation with main process. */
            public static final String SET_SUB_PROCESS_ELEMENTS                    = "setSubProcessElements";
            /** Get the information type of following decision for given activity. */
            public static final String GET_INFO_TYPE_OF_FOLLOW_DECISION            = "getInformationTypeOfFollowingDecision";
            /** Checks if an activity an end activity. */
            public static final String IS_ENDACTIVITY                              = "isEndAcivity";
            /** finish the rest activities. */
            public static final String FINISH_REST_ACTIVITIES                      = "finishRestActiveActivities";
            /** creates the process elements to cloning a process */
            public static final String CREATE_PROCESS_ELEMENTS_FOR_CLONING         = "createProcessElementsForCloning";
            /** Set relation between global elements and the given process */
            public static final String SET_RELATION_OF_GLOBAL_ELOEMENTS_TO_PROCESS = "setRelationOfGlobalElementsToProcess";

            private Workflow()
            {
            }
        }

        /**
         * 
         * Rules for activity functions.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Activity
        {
            /** Clears the "wurde_fertig_gesetzt_von" relation of the "Tätigikeit" from this activity . */
            public static final String CLREAR_DONE_FROM_REL_OF_ACTIVIY = "clearDoneFromRelationOfActivty";
            /** Get the sub procces, which this avtivity calls. */
            public static final String GET_SUB_PROCESS_OF_ACTIVIY      = "getSubProcessOfActivity";
            /** Set the sub procces, which this avtivity calls. */
            public static final String SET_SUB_PROCESS_OF_ACTIVIY      = "setSubProcessOfActivity";
            /** Set the sub procces, which this avtivity calls. */
            public static final String SET_ACTIVIY_AS_AUTO             = "setActitvityAsAuto";
            /** Get the of given activity . */
            public static final String GET_ACTIVIY_MEANS               = "getActivtiyMeans";

            private Activity()
            {
            }
        }

        /**
         * 
         * Rules for {@link Process} functions.
         * 
         * @author Oliver Wolff
         * @version $Revision$
         * @since 2.0.0.a8
         */
        public static final class Process
        {
            /** getting the startRole of a process */
            public static final String GET_START_ROLE          = "getStartRole";

            /** getting the startRole of a process */
            public static final String GET_PROCESS_FOR_VERSION = "getProcessVersion";

            /** Returns all versions of a process template */
            public static final String GET_TEMPLATE_VERSIONS   = "getTemplateVersions";

            /** Returns the initial version of a process template */
            public static final String GET_INITIAL_TEMPLATE    = "getInitialTemplate";

            /** Returns the initial version ID of a process element instance */
            public static final String GET_INITIAL_VERSION     = "getInitialVersion";

            /** getting all {@link ProcessElement}s of a {@link Process} */
            public static final String GET_ELEMENTS_OF_PROCESS = "getElementsOfProcess";

            /** Returns the {@link Process} of a given process id */
            public static final String GET_PROCESS             = "getProcess";

            /** Returns list of {@link Process}es which have given pattern in there name */
            public static final String SEARCH_PROCESSES        = "searchProcesses";

            /** Returns <code>true</code> if the {@link org.prowim.datamodel.prowim.Process} is a landscape, else <code>false</code> */
            public static final String IS_PROCESS_LANDSCAPE    = "isProcessLandscape";

            private Process()
            {
            }
        }

        /**
         * 
         * Rules for SubProcess functions.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class SubProcess
        {
            /** Get caller of given sub process . */
            public static final String GET_SUB_PROCESS_CALLER             = "getSubProcessCaller";
            /** Get all exists sub process . */
            public static final String GET_ALL_EXIST_SUB_PROECESSES       = "getAllExistSubProcesses";
            /** Set a activity as starter of a sub process . */
            public static final String SET_SUB_PROECESS_FLAG_FOR_ACTIVITY = "setSubProcessFlagForActivity";
            /** Set a activity as starter of a sub process . */
            public static final String SET_SUB_PROECESS_FLAG_FOR_PROCESS  = "setSubProcessFlagForProcess";
            /** Check if given process is a sub process . */
            public static final String IS_SUB_PROECESS                    = "isSubProcess";

            private SubProcess()
            {
            }
        }

        /**
         * 
         * Rules for control flow functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Controlflow
        {
            /** Get all follower of control flow. */
            public static final String GET_FOLLOWER_OF_CONTROLFLOW = "getFollowerOfControlFlow";

            private Controlflow()
            {
            }

        }

        /**
         * 
         * Rules for role functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Role
        {
            /** Get all follower of control flow. */
            public static final String GET_ASSIGNMENT_ROLES = "getAssignmentRoles";

            /** Get all global roles. */
            public static final String GET_GLOBAL_ROLES     = "getGlobalRoles";

            private Role()
            {
            }

        }

        /**
         * 
         * Rules for mean functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Mean
        {
            /** Get all global means. */
            public static final String GET_GLOBAL_MEANS = "getGlobalMeans";

            private Mean()
            {
            }

        }

        /**
         * 
         * Rules for resultmemory functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class ResultsMemory
        {
            /** Get all global ResultsMemory. */
            public static final String GET_GLOBAL_RESULTS_MEM = "getGlobalResultsMemory";

            private ResultsMemory()
            {
            }

        }

        /**
         * 
         * Rules for editor functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Editor
        {
            /** Get all global ResultsMemory. */
            public static final String DELETE_ELEMENT_FROM_MODEL  = "deleteElementFromModel";

            /** SEt the flag of process for landscape. */
            public static final String SET_PROCESS_LANDSCAPE_FLAG = "setProcessLandscapeFlag";

            private Editor()
            {
            }

        }

        /**
         * 
         * Rules for knowledge functions
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class KnowledgeObject
        {
            /** Get all knowledge object of given process id. */
            public static final String GET_KNOWLEDGE_OBJECT_OF_PROCESS   = "getKnowledgeObjectsOfProcess";

            /** Get all relations of given knowledge object */
            public static final String GET_RELATIONS_OF_KNOWLEDGE_OBJECT = "getRelationsOfKnowledgeObject";

            /** Search in key words of {@link KnowledgeObject}s for a pattern */
            public static final String SEARCH_KNOWLEDGEOBJECT_KEY_WORDS  = "searchKnowledgeObjectKeyWords";

            /** Get all key words of {@link KnowledgeObject}s */
            public static final String GET_KEY_WORDS_OF_KNOWLEDGE_OBJECT = "getKeyWordsOfKnowledgeObject";

            private KnowledgeObject()
            {
            }

        }

    }

    /********************** Variables************************************ * */
    /**
     * All constants for variables or attributes in rules
     * 
     * @author Maziar Khodaei
     * @version $Revision$
     * @since 2.0
     */
    public static final class Variables
    {

        /**
         * 
         * Global variables
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Common
        {
            /** Name of object. In ontology is synonym "Bezeichnung" */
            public final static String NAME         = "?name";
            /** Timer. */
            public static final String TIMER        = "Timer";
            /** instance id. */
            public static final String INSTANCE_ID  = "?instanceID";
            /** Id of done rule. */
            public static final String DONE_RULE_ID = "?doneRuleID";
            /** empty String . */
            public final static String EMPTY        = "";
            /** ONE . */
            public final static String ONE          = "1";
            /** "aktiv". */
            public final static String ACTIVE       = "aktiv";
            /** slot. */
            public static final String SLOT         = "?slot";
            /** value. */
            public static final String VALUE        = "?value";
            /** denotation. */
            public static final String DENOTATION   = "?denotation";
            /** "active". */
            public final static String ACTIVE_EN    = "?active";
            /** description */
            public static final String DESCRIPTION  = "?description";
            /** element id. */
            public static final String ELEMENT_ID   = "?elementID";
            /** class name */
            public static final String CLASS        = "?class";
            /** createTime */
            public static final String CREATE_TIME  = "?createTime";
            /** version */
            public static final String VERSION      = "?version";
            /** KEY_WORDS */
            public static final String KEY_WORDS    = "?keyWords";

            private Common()
            {
            }

        }

        /**
         * Special variables for process.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Process
        {
            /** ID of Process: ?processID */
            public final static String PROCESS_ID             = "?processID";
            /** ID of sub Process: ?subProcessID */
            public final static String SUB_PROCESS_ID         = "?subProcessID";

            /** The user defined version: ?version */
            public final static String USER_DEFINED_VERSION   = "?version";

            /** The template id: ?PTEM */
            public final static String TEMPLATE_ID            = "?PTEM";
            /** the process id id. */
            public final static String PROCESS_INST_ID        = "?PINS";

            /** The target process id. */
            public final static String TARGET_PROCESS_ID      = "?targetID";

            /** The process type id. */
            public final static String PROCESS_TYPE_ID        = "?processTypeID";

            /** The process type id. */
            public final static String PROCESS_LANDSCPAE_FLAG = "?processLandscapeFlag";

            private Process()
            {
            }
        }

        /**
         * 
         * Special variables for activity
         * 
         * @author Maziar KHodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Activity
        {
            /** ID of activity */
            public final static String ACTIVITY_ID           = "?activityID";
            /** reference. In ontology is synonym "Verweis" */
            public final static String REFERENCE             = "?reference";
            /** Enable type "Jeder". */
            public final static String ENABLE_EVERYBODY      = "Jeder";
            /** Enable type "Keiner". */
            public final static String ENABLE_NOBODY         = "Keiner";
            /** Enable type "Alle". */
            public final static String ENABLE_ALL            = "Alle";
            /** Enable type "Verantwortlicher". */
            public final static String ENABLE_ASSIGNEDPERSON = "Verantwortlicher";
            /** activity done status. */
            public final static String STATUS_DONE           = "fertig";
            /** activate rule. */
            public final static String ACTIVATE_RULE         = "?activateRule";
            /** flag for "Autovorgang". */
            public final static String AUTO_ACTIVITY_FLAG    = "?autoActivityFlag";

            private Activity()
            {
            }
        }

        /**
         * 
         * Special variables for person.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Person
        {
            /** ID of person */
            public final static String PERSON_ID = "?personID";

            private Person()
            {
            }
        }

        /**
         * 
         * Special variables for Controlflow.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Controlflow
        {
            /** ID of controlflow */
            public final static String CONTROLFLOW_ID         = "?controlFlowID";
            /** connection rule */
            public final static String CONNECTION_RULE        = "?connectionRule";
            /** ID of the follower */
            public final static String FOLLOWER_ID            = "?followerID";
            /** Class name of follower */
            public final static String CLASS_NAME_OF_FOLLOWER = "?classOfFollower";

            private Controlflow()
            {
            }
        }

        /**
         * 
         * Special variables for Decision.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Decision
        {
            /** ID of Decision */
            public final static String DECISION_ID = "?decisionID";

            private Decision()
            {
            }
        }

        /**
         * 
         * Special variables for Conjunction.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Conjunction
        {
            /** ID of Conjunction */
            public final static String CONJUNCTION_ID = "?conjunctionID";

            private Conjunction()
            {
            }
        }

        /**
         * 
         * Special variables for Role.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Role
        {
            /** ID of role */
            public final static String ROLE_ID         = "?roleID";
            /** Rule to assign a role */
            public final static String ASSIGNMENT_RULE = "?assignmentRule";

            /** Name of role */
            public final static String ROLE_NAME       = "?roleName";

            private Role()
            {
            }
        }

        /**
         * 
         * Special variables for Mean.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class Mean
        {
            /** ID of mean */
            public final static String MEAN_ID = "?meanID";

            private Mean()
            {
            }
        }

        /**
         * 
         * Special variables for Mean.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class ResultsMemory
        {
            /** ID of mean */
            public final static String MEMORY_ID = "?memoryID";

            private ResultsMemory()
            {
            }
        }

        /**
         * 
         * Special variables for InformationType.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class InformationType
        {
            /** Unique name of information type */
            public final static String INFORMATION_TYPE_NAME = "?informationTypeName";

            private InformationType()
            {
            }
        }

        /**
         * 
         * Special variables for ProcessInformation.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class ProcessInformation
        {
            /** Unique name of information type */
            public final static String PROCESS_INFORMATION_ID = "?processInformationID";

            private ProcessInformation()
            {
            }
        }

        /**
         * 
         * Special variables for {@link KnowledgeObject}.
         * 
         * @author Maziar Khodaei
         * @version $Revision$
         * @since 2.0
         */
        public static final class KnowledgeObject
        {
            /** Unique name of information type */
            public final static String KNOWLEDGE_OBJECT_ID = "?knowledgeObjectID";

            private KnowledgeObject()
            {
            }
        }

    }

    /**
     * Commons constants.
     * 
     * @author Saad Wardi
     * @version $Revision: 3272 $
     */
    public static final class Consts
    {

        /** empty String. */
        public static final String EMPTY_STRING                    = "";
        /** ID_NAME_SPLIT_TOKEN. */
        public static final String ID_NAME_SPLIT_TOKEN             = "/";

        /** the ID of the prowim DMS repository in the ontology. */
        public static final String DMS_REPOSITORY_INSTANCEID       = "Wissensspeicher_200935134820338";

        /** The algernon value for boolean true */
        public static final String TRUE                            = ":TRUE";

        /** The algernon value for boolean false */
        public static final String FALSE                           = ":FALSE";

        /** The value for the initial version of a process template in alfresco */
        public static final String INITIAL_ALFRESCO_VERSION        = "1.0";

        /** The label for the alfresco version slot. If a new version is created of a process template, this label is used (nor for the version but for the newly created process). */
        public static final String EDITABLE_ALFRESCO_VERSION_LABEL = "EDITABLE_ALFRESCO_VERSION";

        /** The label for the editable user version */
        public static final String EDITABLE_USER_VERSION_LABEL     = "Aktuelle Version";

        /** The default name to create a new product parameter. */
        public static final String PRODUCT_PARAMETER               = "Neuer Produktparameter";

        private Consts()
        {
        }

        /**
         * This has to be extended and is not complete!
         * 
         * @author Oliver Specht
         * @version $Revision$
         * @since 2.0.0alpha9
         */
        public static final class XMLElementTypes
        {
            /** A process */
            public static final String PROCESS           = "PROCESS";
            /** An activity */
            public static final String ACTIVITY          = "ACTIVITY";
            /** A relation */
            public static final String RELATION          = "RELATION";
            /** A swimlane */
            public static final String SWIMLANE          = "SWIMLANE";
            /** A processpoint */
            public static final String PROCESS_POINT     = "PROCESSPOINT";
            /** An AND connection */
            public static final String AND               = "AND";
            /** An OR connection */
            public static final String OR                = "OR";
            /** A role */
            public static final String ROLE              = "ROLE";
            /** A depot */
            public static final String DEPOT             = "DEPOT";
            /** A mean */
            public static final String MEAN              = "MEAN";
            /** A group */
            public static final String GROUP             = "GROUP";
            /** A control flow */
            public static final String CONTROLFLOW       = "CONTROLFLOW";
            /** A decision */
            public static final String DECISION          = "DECISION";
            /** A job */
            public static final String JOB               = "JOB";
            /** A function */
            public static final String FUNCTION          = "FUNCTION";
            /** A product */
            public static final String PRODUCT           = "PRODUCT";
            /** A combination rule */
            public static final String COMBINATION_RULES = "COMBINATIONRULES";

            private XMLElementTypes()
            {
            }
        }

        /**
         * Process element types. Has to be completed
         * 
         * @author Oliver Specht
         * @version $Revision$
         * @since 2.0.0alpha9
         */
        public static final class ProcessElementTypes
        {
            /** A process information element */
            public static final String PROCESS_INFORMATION_CLASS = "ProcessInformation";

            private ProcessElementTypes()
            {
            }
        }
    }

    /**
     * Defines all slot constants from the prowim ontology.
     * 
     * @author Oliver Specht
     * @version $Revision$
     * @since 2.0.9alpha
     */
    public static final class Slots
    {
        /** This process template slot stores the IDs of the process instances which have been created from the template */
        public final static String HAS_TEMPLATE         = "hat_Template";
        /** Flag to determine if the instance is the currently active version */
        public final static String IS_ACTIVE_VERSION    = "ist_aktive_Version";
        /** Slot to store the instance which is the initial version of the process */
        public final static String IS_VERSION_OF        = "ist_Version_von";
        /** This slot stores the ID of the process template a process instance / workflow is created from */
        public final static String FROM_TEMPLATE        = "aus_Template";
        /** This slot stores the version name entered by the user when creating a new version of a process template */
        public final static String USER_DEFINED_VERSION = "benutzerdefinierte_Version";
        /** This slot stores the version in the alfresco DMS system */
        public final static String ALFRESCO_VERSION     = "Alfrescoversion";
        /** This slot stores the name of a class in ontology */
        public final static String DENOTATION           = "Bezeichnung";
        /** This slot stores the description of a class in ontology */
        public final static String DESCRIPTION          = "Beschreibung";
        /** This slot stores the key words of a class in ontology */
        public final static String KEY_WORDS            = "Schlagworte";

        private Slots()
        {
        }

        /**
         * 
         * Constants for {@link org.prowim.datamodel.prowim.Activity}
         * 
         * @author Philipp Leusmann
         * @version $Revision$
         * @since 2.0.0a10
         */
        public static final class Activity
        {
            /**
             * Key for Protégé slot "Autovorgang"
             */
            public static final String IS_AUTOMATIC = "Autovorgang";

            private Activity()
            {
            }
        }
    }
}
