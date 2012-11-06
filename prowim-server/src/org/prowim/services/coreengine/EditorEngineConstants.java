/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-02-02 16:44:50 +0100 (Mi, 02 Feb 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/EditorEngineConstants.java $
 * $LastChangedRevision: 5032 $
 *------------------------------------------------------------------------------
 * (c) 16.10.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.services.coreengine;

/**
 * Defines constants like rules names and variables names defined in the protege project. Those rules and variables are needed for the prowim model editor.
 * 
 * @author Saad Wardi
 * @version $Revision: 5032 $
 */
public class EditorEngineConstants
{

    /* ******************************* Rules***************************************** */
    /**
     * Rules.
     * 
     * @author Saad Wardi
     * @version $Revision: 5032 $
     */
    public static final class Rules
    {

        /**
         * Rules that are used commonly in the prowim editor.
         * 
         * @author Saad Wardi
         * @version $Revision: 5032 $
         */
        public static final class Common
        {
            /** Tests the scope of an object. */
            public static final String TEST_SCOPE                    = "testScope";
            /** Gets the type ID. */
            public static final String GET_TYPE_ID                   = "getTypeID";
            /** Tests if the given ID is an ID of an existing frame in the ontology. */
            public static final String IS_FRAME                      = "isFrame";
            /** Creates a new instance. */
            public static final String CREATE_NEW_INSTANCE           = "addInstance";
            /** Gets the instance count. */
            public static final String GET_INSTANCE_COUNT            = "getInstanceCount";
            /** Gets the properties of an object. */
            public static final String GET_INSTANCE_PROPERTIES       = "getInstanceProperties";
            /** Gets the relations of an object. */
            public static final String GET_INSTANCE_RELATIONS        = "getInstanceRelations";
            /** Gets the values of relations. */
            public static final String GET_INSTANCE_RELATIONS_VALUES = "getInstanceDefinedRelations";
            /** Gets the name of an instance. */
            public static final String GET_INSTANCE_NAME             = "getName";
            /** Gets the direct class of an instance. */
            public static final String GET_DIRECTCLASSOFINSTANCE     = "getDirectClassOfInstance";
            /** Set element to model. */
            public static final String SET_ELEMENT_TO_MODEL          = "setElementToModel";
            /** Gets the description. */
            public static final String GET_DESCRIPTION               = "getDescription";
            /** Sets the description. */
            public static final String SET_DESCRIPTION               = "setDescription";
            /** Gets the models. */
            public static final String GET_MODELS                    = "getProcessModels";
            /** Connects 2 activities with a controlflow. */
            public static final String CONNECT_ACTIVITIES            = "connectActivities";
            /** Sets the scope. */
            public static final String SET_SCOPE                     = "setScope";
            /** Activate a controlflow. */
            public static final String ACTIVATE_CONTROLFLOW          = "activateControlFlow";
            /** Set the next activity active. */
            public static final String SET_ACTIVITY_ACTIVE           = "setActivityActive";
            /** Connect an activity with role. */
            public static final String CONNECT_ACTIVITY_ROLE         = "connectActivityRole";
            /** Gets all relations beetwin 2 objects. */
            public static final String GET_RELATIONS                 = "getRelations";
            /** Sets the a product as output for an activity. */
            public static final String SET_PRODUCT_OUT               = "setProductOut";
            /** Sets the a product as input for an activity. */
            public static final String SET_PRODUCT_IN                = "setProductIn";
            /** check informationtype. */
            public static final String IS_INFORMATION_TYPE           = "isInformationType";
            /** Get the slot value. */
            public static final String GET_SLOT_VALUE                = "getSlotValue";
            /** Get controlflows count. */
            public static final String GET_CONTROLFLOWS_COUNT        = "getControlFlowsCount";
            /** Get combination rules. */
            public static final String GET_COMBINATION_RULES         = "getCombinationRules";
            /** Get activation rules. */
            public static final String GET_ACTIVATION_RULES          = "getActivationRules";
            /** Connects an activity with a mittel. */
            public static final String CONNECT_ACTIVITY_MITTEL       = "connectActivityMittel";
            /** Gets the constraint rule. */
            public static final String GET_SLOT_CONSTRAINT           = "getConstraintRule";

            private Common()
            {

            }
        }
    }

    /* ******************************* Slots***************************************** */
    /**
     * Slots.
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5032 $
     */
    public static final class Slots
    {

        /**
         * Slots that are used commonly in the prowim editor.
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5032 $
         */
        public static final class Common
        {
            /** HAS_ACTIVATION_RULE. */
            public static final String HAS_ACTIVATION_RULE = "hat_Aktivierungsregel";

            private Common()
            {

            }
        }
    }

    /* *********************Variables************************************ * */
    /**
     * Variables names defined in the algernon rules.
     * 
     * @author Saad Wardi
     * @version $Revision: 5032 $
     */
    public static final class Variables
    {

        /**
         * Commons variables.
         * 
         * @author Saad Wardi
         * @version $Revision: 5032 $
         */
        public static final class Common
        {
            /** general variable must be used to get the ontology ID. */
            public static final String ID               = "?ID";
            /** Object ID . */
            public static final String OID              = "?OID";
            /** Object name. */
            public static final String NAME             = "?name";
            /** To fetch the value of the slot VALIDITY. */
            public static final String SCOPE            = "?scope";
            /** the createTime. */
            public static final String CREATE_TIME      = "?createTime";
            /** the instance count. */
            public static final String INSTANCE_COUNT   = "?count";
            /** direct class id. */
            public static final String DCID             = "?DCID";
            /** model ID. */
            public static final String MODEL_ID         = "?modelID";
            /** instance ID. */
            public static final String ELEMENT_ID       = "?elementID";
            /** slot. */
            public static final String SLOT             = "?slot";
            /** value. */
            public static final String VALUE            = "?value";
            /** source ID. */
            public static final String SOURCE_ID        = "?sourceID";
            /** target ID. */
            public static final String TARGET_ID        = "?targetID";
            /** connector ID. */
            public static final String CONNECTOR_ID     = "?connectorID";
            /** activity ID. */
            public static final String ACTIVITY_ID      = "?activityID";
            /** roleID. */
            public static final String ROLE_ID          = "?roleID";
            /** relation. */
            public static final String RELATION         = "?relation";
            /** task (Tätigkeit) ID. */
            public static final String TASK_ID          = "?taskID";
            /** product ID. */
            public static final String PRODUCT_ID       = "?productID";
            /** count. */
            public static final String COUNT            = "?count";
            /** mittel ID. */
            public static final String MITTEL_ID        = "?mittelID";
            /** function ID. */
            public static final String FUNCTION_ID      = "?functionID";
            /** Combination rule. */
            public static final String COMBINATION_RULE = "?VR";
            /** Activity ID. */
            public static final String ACTIVITY         = "?A";
            /** Controlflow ID. */
            public static final String CONTROLFLOW      = "?K";
            /** Attribute. */
            public static final Object ATTRIBUTE        = "?attr";
            /** person ID. */
            public static final String PERSON_ID        = "?personID";

            private Common()
            {
            }

        }

    }

    /**
     * Commons constants.
     * 
     * @author Saad Wardi
     * @version $Revision: 5032 $
     */
    public static final class Consts
    {

        /** not valida value of the VALIDITY slot. */
        public static final String VALIDITY                             = "LocalGlobal";
        /** type COMPONENT. */
        public static final String TYPE_COMPONENT                       = "SYS_COMPONENT-CLASS";
        /** Component. */
        public static final String COMPONENT                            = "Component";
        /** type CONNECTION. */
        public static final String TYPE_CONNECTION                      = "SYS_CONNECTION-CLASS";
        /** CONNECTION. */
        public static final String CONNECTION                           = "Connection";
        /** model. */
        public static final String MODEL                                = "model";
        /** empty String. */
        public static final String EMPTY_STRING                         = "";
        /** ID_NAME_SPLIT_TOKEN. */
        public static final String ID_NAME_SPLIT_TOKEN                  = "/";
        /** CONTENT_FORMAT_FOR_XML. */
        public static final String CONTENT_FORMAT_FOR_XML               = "text/xml";
        /** Not Available flag. */
        public static final String NOT_AVAILABLE                        = "NOT_AVAILABLE";
        /** The desciption of the processinformation that stores the dms ID. */
        public static final String PROCESS_INFORMATION_UUID_DESCRIPTION = "This instance is used by ProWim system and has no use by the user. It saves the DMS ID.";
        /** The extension of the model content file. */
        public static final String MODEL_DMS_CONTENT_FILE_EXTENSION     = ".xml";

        private Consts()
        {
        }

    }

}
