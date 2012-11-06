/*==============================================================================
 * File $GlobalConstants$
 * Project: ProWim
 *
 * $17.04.2009$
 * $MK$
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/utils/GlobalConstants.java $
 * $LastChangedRevision: 5070 $
 *------------------------------------------------------------------------------
 * (c) 17.04.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.utils;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.util.Policy;


/**
 * This class included the constants of this platform
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5070 $
 */
public final class GlobalConstants
{
    /**
     * The name of the property key specifying the session time in seconds
     */
    public static final String       SESSION_TIMEOUT_PROPERTY_KEY = "prowim.portal.session.timeout";

    /** Set the domain of server */
    public final static String       WIKI                         = "wiki";

    /** The suffix of Wiki url. */

    public final static String       WIKI_SUFFIX                  = "index.php?title?";

    /** EDIT_WOB */
    public final static String       EDIT_WOB                     = "EDIT_WOB";

    /** ROOT_MODEL */
    public final static String       ROOT_MODEL                   = "ROOT_MODEL";

    /** HTTP */
    public final static String       HTTP                         = "http://";

    /** HTTPS */
    public final static String       HTTPS                        = "https://";

    /** FILE_LINK */
    public final static String       FILE_LINK                    = "file:///";

    /** NETWORK */
    public final static String       NETWORK                      = "Netzlaufwerke / Dateisystem";

    /** PROWIM_DMS */
    public final static String       PROWIM_DMS                   = "Dokumentenablage / DMS";

    /** PROWIM_WIKI */
    public final static String       PROWIM_WIKI                  = "ProWim Wiki";

    /** INTERNET */
    public final static String       INTERNET                     = "Internet / Intranet";

    /** SAVE_ACTIVITY */
    public final static String       SAVE_ACTIVITY                = "SAVE";

    /** FINISH_ACTIVITY */
    public final static String       FINISH_ACTIVITY              = "FINISH";

    /** DOUBLE_POINT */
    public final static String       DOUBLE_POINT                 = ":";

    /** COMMA */
    public final static String       COMMA                        = ",";

    /** MINUS */
    public final static String       MINUS                        = "-";

    /** SPACE2 */
    public final static String       SPACE2                       = "  ";

    /** SPACE2 */
    public final static String       SPACE1                       = " ";

    /** ONTOLOGY. */
    public final static String       ONTOLOGY                     = "Ontology";

    /** SERVER. */
    public final static String       SERVER                       = "Server";

    /** PORTAL. */
    public final static String       PORTAL                       = "Portal";

    /** MODELEDITOR. */
    public final static String       MODELEDITOR                  = "ModelEditor";

    /** DATE_GR_PATTERN */
    public final static String       DATE_GR_PATTERN              = "dd.MM.yyyy";

    /** DATE_TIME_GR_PATTERN */
    public final static String       DATE_TIME_GR_PATTERN         = "dd.MM.yyyy HH:mm";

    /**
     * DATA_FLAG This flag is for tables, when you want to render data information, which is not shows in table self
     */
    public final static String       DATA_FLAG                    = "DATA";

    /** the admin username. */
    public final static String       ADMIN                        = "admin";

    /** the username key. */
    public final static String       USERNAME_KEY                 = "username";

    /**
     * urlToLoginPage
     */
    public final static String       URL_TO_LOGIN_PAGE            = "http://prowim-wivu.ebcot.info/prowim/app";

    /**
     * LOGGED_USER
     */
    public final static String       LOGGED_USER                  = "Demo User";

    /**
     * LOGGED_USER_ID
     */
    public final static String       LOGGED_USER_ID               = "Person_1235550499359";

    /**
     * SHORT_TEXT
     */
    public final static String       SHORT_TEXT                   = "ShortText";

    /**
     * LONG_TEXT
     */
    public final static String       LONG_TEXT                    = "LongText";

    /**
     * INTEGER
     */
    public final static String       INTEGER                      = "Integer";

    /**
     * FLOAT
     */
    public final static String       FLOAT                        = "Float";

    /**
     * SINGLE_LIST
     */
    public final static String       SINGLE_LIST                  = "SingleList";

    /**
     * MULTI_LIST
     */
    public final static String       MULTI_LIST                   = "MultiList";

    /**
     * MULTI_LIST_CONTROL_FLOW
     */
    public final static String       MULTI_LIST_CONTROL_FLOW      = "MultiListControlFlow";

    /**
     * COMBO_BOX
     */
    public final static String       COMBO_BOX                    = "ComboBox";

    /**
     * COMBO_BOX
     */
    public final static String       COMBO_BOX_CONTROL_FLOW       = "ComboBoxControlFlow";

    /**
     * DOCUMENT
     */
    public final static String       DOCUMENT                     = "Document";

    /**
     * DATE
     */
    public final static String       DATE                         = "Date";

    /**
     * TIME_STAMP
     */
    public final static String       TIME_STAMP                   = "TimeStamp";

    /**
     * LINK
     */
    public final static String       LINK                         = "Link";

    /**
     * PERSON
     */
    public final static String       PERSON                       = "Personen";

    /**
     * ORGANIZATION_UNIT
     */
    public final static String       ORGANIZATION_UNIT            = "OrganizationalUnit";

    /** TABLE_COLUMN_WIDTH. */
    public final static int          TABLE_COLUMN_DEFAULT_WIDTH   = 174;

    /** DUMMY_ID. */
    public final static String       DUMMY_ID                     = "DUMMY_00000";

    /** all types beeing a list */
    public final static List<String> TYPE_LISTS                   = Arrays.asList(SINGLE_LIST, MULTI_LIST, COMBO_BOX, MULTI_LIST_CONTROL_FLOW,
                                                                                  COMBO_BOX_CONTROL_FLOW, PERSON, ORGANIZATION_UNIT);

    /** Set the domain of server */
    public final static String       PORTAL_BUNDLE_NAME           = "org.prowim.portal";

    /** COLUMN_VIEWER_KEY */
    public final static String       COLUMN_VIEWER_KEY            = Policy.JFACE + ".columnViewer";

    /** ACTIVITY */
    public final static String       ACTIVITY                     = "Activity";

    /** MEAN */
    public final static String       MEAN                         = "Mean";

    /** MEAN */
    public final static String       MEAN_DE                      = "Mittel";

    /** DEPOT */
    public final static String       DEPOT                        = "Depot";

    /** DEPOT */
    public final static String       DEPOT_DE                     = "Ablage";

    /** ROLE */
    public final static String       ROLE                         = "Role";

    /** ROLE_DE */
    public final static String       ROLE_DE                      = "Rolle";

    /** PROCESS_TYPE */
    public final static String       PROCESS_TYPE                 = "ProcessType";

    /** PROCESS_INFORMATION */
    public final static String       PROCESS_INFORMATION          = "ProcessInformation";

    /** PRODUCT_WAY */
    public final static String       PRODUCT_WAY                  = "ProductWay";

    /** CONTROL_FLOW */
    public final static String       CONTROL_FLOW                 = "ControlFlow";

    /** RESULTS_MEMORY */
    public final static String       RESULTS_MEMORY               = "ResultsMemory";

    /** WORK */
    public final static String       WORK                         = "Work";

    /** FUNCTION */
    public final static String       FUNCTION                     = "Function";

    /** PRODUCT */
    public final static String       PRODUCT                      = "Product";

    /** GLOBAL_ELEMENT */
    public final static String       GLOBAL_ELEMENT               = "GLOBAL_ELEMENT";

    /** LOCAL_ELEMENT */
    public final static String       LOCAL_ELEMENT                = "LOCAL_ELEMENT";

    /** LOCAL */
    public final static String       LOCAL                        = "Local";

    /** LOCAL */
    public final static String       GLOBAL                       = "Global";

    /** "Neuer Prozess". */
    public static final String       NEW_PROCESS                  = "Neuer Prozess";

    /** ZERO. */
    public static final String       ZERO                         = "0";

    /** "EDIT_NAME". */
    public static final String       EDIT_NAME                    = "EDIT_NAME";
    /** PROCESS. */
    public static final String       PROCESS                      = "Prozess";
    /** KNOWLEDGE_DOMAIN. */
    public static final String       KNOWLEDGE_DOMAIN             = "Wissensdomaene";

    /** STARTER_ROLE_PREFIX. */
    public static final String       STARTER_ROLE_PREFIX          = "Startrecht_";
    /** NEW_PROCESS_MODEL_ID. */
    public static final String       NEW_PROCESS_MODEL_ID         = "model";

    /** ERROR */
    public static final String       ERROR                        = "error";

    /** WARN */
    public static final String       WARN                         = "warn";

    /** INFO */
    public static final String       INFO                         = "info";

    /** LINE_BREAK */
    public static final String       LINE_BREAK                   = "\n";

    /**
     * 
     * Global values
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5070 $
     */
    public static final class Ontology
    {
        /** ROLE_DE */
        public final static String ROLE              = "Rolle";

        /** MEAN */
        public final static String MEAN              = "Mittel";

        /** RESULTS_MEMORY */
        public final static String RESULTS_MEMORY    = "Ablage";

        /** ACTIVITY */
        public final static String ACTIVITY          = "Aktivität";

        /** KNOWLEDGE_MANAGER */
        public final static String KNOWLEDGE_MANAGER = "Wissensmanager";

        private Ontology()
        {
        }
    }

    /**
     * 
     * constants for relations
     * 
     * @author Philipp Leusmann
     * @version $Revision: 5070 $
     * @since 2.0.0a9
     */
    public static final class Relations
    {
        /** IS_ALLOWED_TO_START */
        public static final String IS_ALLOWED_TO_START  = "darf_gestartet_werden_von";

        /** END_WITH */
        public static final String END_WITH             = "endet_mit";

        /** IS_END_ACTIVITY_FROM */
        public static final String IS_END_ACTIVITY_OF   = "ist_Endaktivitaet_von";

        /** STARTS_WITH */
        public static final String STARTS_WITH          = "startet_mit";

        /** IS_ALLOWED_TO_START */
        public static final String IS_START_ACTIVITY_OF = "ist_Startaktivitaet_von";

        private Relations()
        {
        }
    }

    /**
     * 
     * constants for http sessions
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5070 $
     * @since 2.0.0a9
     */
    public static final class HttpSession
    {
        /** SESSION_KEY_UPDATE_DIALOG_SYNCHRONIZER */
        public static final String SESSION_KEY_UPDATE_DIALOG_SYNCHRONIZER = "ProWim.UpdateDialogSynchronizer";

        private HttpSession()
        {
        }
    }

    private GlobalConstants()
    {
    }

    /**
     * 
     * Constants for Information types.
     * 
     * @author Maziar KHodaei
     * @version $Revision: 5070 $
     * @since 2.0.0a10
     */
    public static final class InformationTypes
    {
        /** INFORMATION_TYPE_DATE */
        public static final String INFORMATION_TYPE_DATE                    = "Date";
        /** INFORMATION_TYPE_COMBO_BOX */
        public static final String INFORMATION_TYPE_COMBO_BOX               = "ComboBox";
        /** INFORMATION_TYPE_COMBO_BOX_CONTROL_FLOW */
        public static final String INFORMATION_TYPE_COMBO_BOX_CONTROL_FLOW  = "ComboBoxControlFlow";
        /** INFORMATION_TYPE */
        public static final String INFORMATION_TYPE                         = "Informationtyp";
        /** INFORMATION_TYPE_MULTI_LIST_CONTROL_FLOW */
        public static final String INFORMATION_TYPE_MULTI_LIST_CONTROL_FLOW = "MultiListControlFlow";
        /** INFORMATION_TYPE_TIME_STAMP */
        public static final String INFORMATION_TYPE_TIME_STAMP              = "TimeStamp";
        /** INFORMATION_TYPE_PERSONS */
        public static final String INFORMATION_TYPE_PERSONS                 = "Personen";
        /** INFORMATION_TYPE_SINGLE_LIST */
        public static final String INFORMATION_TYPE_SINGLE_LIST             = "SingleList";
        /** INFORMATION_TYPE_ORGANIZATION_UNIT */
        public static final String INFORMATION_TYPE_ORGANIZATION_UNIT       = "OrganizationalUnit";
        /** INFORMATION_TYPE_MULTI_LIST */
        public static final String INFORMATION_TYPE_MULTI_LIST              = "MultiList";
        /** INFORMATION_TYPE_FLOAT */
        public static final String INFORMATION_TYPE_FLOAT                   = "Float";
        /** INFORMATION_TYPE_INTEGER */
        public static final String INFORMATION_TYPE_INTEGER                 = "Integer";

        private InformationTypes()
        {
        }
    }
}
