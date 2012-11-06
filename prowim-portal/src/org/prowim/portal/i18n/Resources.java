/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-10 14:14:31 +0200 (Di, 10 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/i18n/Resources.java $
 * $LastChangedRevision: 5083 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.i18n;

import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.datamodel.prowim.ProcessType;
import org.prowim.rap.framework.i18n.consts.Action;
import org.prowim.rap.framework.i18n.consts.Image;
import org.prowim.rap.framework.i18n.consts.Text;
import org.prowim.rap.framework.i18n.consts.impl.ActionConst;
import org.prowim.rap.framework.i18n.consts.impl.ImageConst;
import org.prowim.rap.framework.i18n.consts.impl.TextConst;


/**
 * Resources for internationalization (i18n)
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5083 $
 */
public final class Resources
{

    /**
     * 
     * This class included all resources used in Frame
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5083 $
     */
    public static final class Frames
    {
        // Main classes

        /**
         * In this class you can find the global texts an actions, which used in several components
         */
        public static final class Global
        {
            /**
             * 
             * Global values
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                // inner classes of Frames.Header.Texts
                /** login failed. */
                public static final Text LOGIN_FAILED                      = new TextConst();

                /** EXISTING Roles. */
                public static final Text EXISTING_ROLES                    = new TextConst();

                /** ASSIGNED_ROLES. */
                public static final Text ASSIGNED_ROLES                    = new TextConst();

                /** ROLES_ASSIGNEMENT. */
                public static final Text ROLES_ASSIGNEMENT                 = new TextConst();

                /** USER_MANAGEMENT. */
                public static final Text USER_MANAGEMENT                   = new TextConst();

                /** USER_MANAGEMENT. */
                public static final Text TYP                               = new TextConst();

                /** GENERAL. */
                public static final Text GENERAL                           = new TextConst();

                /** DOCUMENT. */
                public static final Text DOCUMENT                          = new TextConst();

                /** DOCUMENTS */
                public static final Text DOCUMENTS                         = new TextConst();

                /** CONFIRM PASSWORD. */
                public static final Text CONFIRM_PASSWORD                  = new TextConst();

                /** VERSION_HISTORY. */
                public static final Text VERSION_HISTORY                   = new TextConst();

                /** LABEL. */
                public static final Text LABEL                             = new TextConst();

                /** AUTHOR. */
                public static final Text AUTHOR                            = new TextConst();

                /** HAS_NO_DMS_CONTENT. */
                public static final Text HAS_NO_DMS_CONTENT                = new TextConst();

                /** ERROR_ON_ADD_ROLE_TO_ORGANIZATION. */
                public static final Text ERROR_ON_ADD_ROLE_TO_ORGANIZATION = new TextConst();

                /** NO_PERMISSION. */
                public static final Text NO_PERMISSION                     = new TextConst();

                /** NO_RIGHTS_ROLES. */
                public static final Text NO_RIGHTS_ROLES                   = new TextConst();
                /**
                 * NO_ENTRIES
                 */
                public static final Text NO_ENTRIES                        = new TextConst();

                /**
                 * DESCRIPTION
                 */
                public static final Text DESCRIPTION                       = new TextConst();

                /**
                 * DENOTATION
                 */
                public static final Text DENOTATION                        = new TextConst();

                /**
                 * PROPERTIES.
                 */
                public static final Text PROPERTIES                        = new TextConst();

                /**
                 * NAME.
                 */
                public static final Text NAME                              = new TextConst();

                /**
                 * Version name
                 */
                public static final Text VERSION_NAME                      = new TextConst();

                /** CREATED_AM. */
                public static final Text CREATED_AT                        = new TextConst();

                /**
                 * The user who has created (a process, ...)
                 */
                public static final Text CREATED_FROM                      = new TextConst();

                /**
                 * The user who is allowed to start a process
                 */
                public static final Text CAN_BE_STARTED_FROM               = new TextConst();

                /**
                 * FIRSTNAME.
                 */
                public static final Text FIRSTNAME                         = new TextConst();

                /**
                 * LASTNAME.
                 */
                public static final Text LASTNAME                          = new TextConst();

                /**
                 * NICKNAME.
                 */
                public static final Text NICKNAME                          = new TextConst();

                /**
                 * ORGANISATION.
                 */
                public static final Text ORGANISATION_UNIT                 = new TextConst();

                /** ORGANISATION. */
                public static final Text ORGANISATION_UNITS                = new TextConst();

                /** ORGANISATION. */
                public static final Text ORGANISATION_ELEMENTS             = new TextConst();

                /**
                 * ACTOR.
                 */
                public static final Text ACTOR                             = new TextConst();

                /**
                 * VALUE.
                 */
                public static final Text VALUE                             = new TextConst();

                /**
                 * OK.
                 */
                public static final Text OK                                = new TextConst();

                /**
                 * OK.
                 */
                public static final Text CLOSE                             = new TextConst();

                /**
                 * CANCEL.
                 */
                public static final Text CANCEL                            = new TextConst();

                /**
                 * OF.
                 */
                public static final Text OF                                = new TextConst();

                /**
                 * ORDER.
                 */
                public static final Text ORDER                             = new TextConst();

                /**
                 * YES.
                 */
                public static final Text YES                               = new TextConst();

                /**
                 * NO.
                 */
                public static final Text NO                                = new TextConst();

                /**
                 * FINISHED.
                 */
                public static final Text FINISHED                          = new TextConst();

                /** USER. */
                public static final Text USER                              = new TextConst();

                /** USERS. */
                public static final Text USERS                             = new TextConst();

                /** KNOW_MEMORY. */
                public static final Text KNOW_MEMORY                       = new TextConst();

                /** HYPERLINK. */
                public static final Text HYPERLINK                         = new TextConst();

                /** PASSWORD. */
                public static final Text PASSWORD                          = new TextConst();

                /** VERSION. */
                public static final Text VERSION                           = new TextConst();

                /** RESPONSABLE. */
                public static final Text RESPONSABLE                       = new TextConst();

                /** TEL. */
                public static final Text TEL_LABEL                         = new TextConst();

                /** MAIL. */
                public static final Text MAIL_LABEL                        = new TextConst();

                /** TEL. */
                public static final Text FAX_LABEL                         = new TextConst();

                /** MAIL. */
                public static final Text WEB_LABEL                         = new TextConst();

                /** CONTACTS_PERSON_NAME. */
                public static final Text CONTACTS_PERSON_NAME              = new TextConst();

                /** CONTACTS_PERSON_WEB. */
                public static final Text EBCOT_WEB                         = new TextConst();

                /** CONTACTS_PERSON_MAIL. */
                public static final Text CONTACTS_PERSON_MAIL              = new TextConst();

                /** CONTACTS_PERSON_TEL. */
                public static final Text CONTACTS_PERSON_TEL               = new TextConst();

                /** CONTACTS_PERSON_FAX. */
                public static final Text CONTACTS_PERSON_FAX               = new TextConst();

                /** COPY_RIGHT. */
                public static final Text COPY_RIGHT                        = new TextConst();

                /** EBCOT. */
                public static final Text EBCOT                             = new TextConst();

                /** RIGHT_RESERVED_TEXT. */
                public static final Text RIGHT_RESERVED_TEXT               = new TextConst();

                /** ONTOLOGY. */
                public static final Text ONTOLOGY                          = new TextConst();

                /** SERVER. */
                public static final Text SERVER                            = new TextConst();

                /** MODELLEDITOR. */
                public static final Text MODELLEDITOR                      = new TextConst();

                /** EBCOT. */
                public static final Text PORTAL                            = new TextConst();

                /** ABOUT. */
                public static final Text ABOUT                             = new TextConst();

                /** LOGOUT. */
                public static final Text LOGOUT                            = new TextConst();

                /** TYPE. */
                public static final Text TYPE                              = new TextConst();

                /** DATE. */
                public static final Text DATE                              = new TextConst();

                /** ATTRIBUTE. */
                public static final Text ATTRIBUTES                        = new TextConst();

                /** RELATIONS. */
                public static final Text RELATIONS                         = new TextConst();

                /** SEARCH_KEY_WORD. */
                public static final Text SEARCH_KEY_WORD                   = new TextConst();

                /** SEARCH_RESULT. */
                public static final Text SEARCH_RESULT                     = new TextConst();

                /** PHONE. */
                public static final Text PHONE                             = new TextConst();

                /** MAIL. */
                public static final Text MAIL                              = new TextConst();

                /** "ADDRESS". */
                public static final Text ADDRESS                           = new TextConst();

                /** "PROCESS_ELEMENTS". */
                public static final Text PROCESS_ELEMENTS                  = new TextConst();

                /** "SWIMLANE". */
                public static final Text SWIMLANE                          = new TextConst();

                /** "CHART". */
                public static final Text CHART                             = new TextConst();

                /** "SELECTION". */
                public static final Text SELECTION                         = new TextConst();

                /** "ProWim". */
                public static final Text PROWIM                            = new TextConst();

                /** "PRODUCT_PARAM. */
                public static final Text PRODUCT_PARAM                     = new TextConst();

                /** "INFORMATION_TYPE. */
                public static final Text INFORMATION_TYPE                  = new TextConst();

                /** "FILE. */
                public static final Text FILE                              = new TextConst();

                /** "ACTIVITY. */
                public static final Text ACTIVITY                          = new TextConst();

                /** "ACTIVITIES. */
                public static final Text ACTIVITIES                        = new TextConst();

                /** "MEANS. */
                public static final Text MEANS                             = new TextConst();

                /** "ROLES. */
                public static final Text ROLES                             = new TextConst();

                /** "PROCESS_INFORMATIONS. */
                public static final Text PROCESS_INFORMATIONS              = new TextConst();

                /** "PRODUCT_WAYS. */
                public static final Text PRODUCT_WAYS                      = new TextConst();

                /** "CONTROL_FLOWS. */
                public static final Text CONTROL_FLOWS                     = new TextConst();

                /** "RESULTS_MEMORY. */
                public static final Text RESULTS_MEMORY                    = new TextConst();

                /** "WORKS. */
                public static final Text WORKS                             = new TextConst();

                /** "FUNCTIONS. */
                public static final Text FUNCTIONS                         = new TextConst();

                /** "PRODUCTS. */
                public static final Text PRODUCTS                          = new TextConst();

                /** "MINIMUM. */
                public static final Text MINIMUM                           = new TextConst();

                /** "MAXIMUM. */
                public static final Text MAXIMUM                           = new TextConst();

                /** "REPOSITORY. */
                public static final Text REPOSITORY                        = new TextConst();

                /** "ASSOCIATED_PROPERTIES. */
                public static final Text ASSOCIATED_PROPERTIES             = new TextConst();

                /** "ACTUAL_PROSSES. */
                public static final Text ACTUAL_PROSSES                    = new TextConst();

                /** "ALL_PROSSES_ELEMETS. */
                public static final Text ALL_PROSSES_ELEMETS               = new TextConst();

                /** ERROR_ON_MOVE_ORGANIZATION. */
                public static final Text ERROR_ON_MOVE_ORGANIZATION        = new TextConst();
                /** ERROR_ON_SET_PROCESSTYPE_PARENT. */
                public static final Text ERROR_ON_SET_PROCESSTYPE_PARENT   = new TextConst();
                /** UPDATE_DIALOG_TITLE. */
                public static final Text UPDATE_DIALOG_TITLE               = new TextConst();
                /** UPDATE_DIALOG_MESSAGE. */
                public static final Text UPDATE_DIALOG_MESSAGE             = new TextConst();
                /** UPDATE_DIALOG_MESSAGE_MODEL1. */
                public static final Text UPDATE_DIALOG_MESSAGE_MODEL1      = new TextConst();
                /** UPDATE_DIALOG_MESSAGE_MODEL2. */
                public static final Text UPDATE_DIALOG_MESSAGE_MODEL2      = new TextConst();

                /** NO DOCUMENTS */
                public static final Text NODOCUMENT                        = new TextConst();
                /** ADD. */
                public static final Text ADD                               = new TextConst();
                /** DELETE. */
                public static final Text DELETE                            = new TextConst();

                /** LEVEL. */
                public static final Text LEVEL                             = new TextConst();

                /** COPY. */
                public static final Text COPY                              = new TextConst();

                /**
                 * The subject for an error in model editor send to support by email
                 */
                public static final Text ERROR_MODEL_EDITOR_EMAIL_SUBJECT  = new TextConst();

                /**
                 * The footer text for an error email send to support
                 */
                public static final Text ERROR_EMAIL_FOOTER                = new TextConst();

                /**
                 * The address of the receiver (the support) of the error email
                 */
                public static final Text ERROR_EMAIL_RECEIVER              = new TextConst();

                /**
                 * Problem report uses in support mail dialog
                 */
                public static final Text PROBLEM_REPORT                    = new TextConst();

                /**
                 * IN
                 */
                public static final Text IN                                = new TextConst();

                /**
                 * PART_OF
                 */
                public static final Text PART_OF                           = new TextConst();

                /** FILTER_THE_LIST */
                public static final Text FILTER_THE_LIST                   = new TextConst();

                /** KEY_WORDS */
                public static final Text KEY_WORDS                         = new TextConst();

                /** FEEDBACK_TO */
                public static final Text FEEDBACK_TO                       = new TextConst();

                /**
                 * 
                 * Default constructor for Texts
                 */
                private Texts()
                {

                }
            }

            /**
             * 
             * Image constants in Global items
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * ORGANISATION_UNITS_IMAGE
                 */
                public static final Image ORGANISATION_UNITS_IMAGE = new ImageConst();

                /**
                 * ORGANISATION_UNIT_IMAGE
                 */
                public static final Image ORGANISATION_UNIT_IMAGE  = new ImageConst();

                /**
                 * ProWim logo
                 */
                public static final Image PRODUCT_LOGO             = new ImageConst();

                private Images()
                {
                }

            }

            /**
             * This class included all global actions.
             */
            public static final class Actions
            {
                /** LOGIN. */
                public static final Action LOGIN                           = new ActionConst();

                /** ITEM_DESCRIPTION */
                public static final Action ITEM_DESCRIPTION                = new ActionConst();

                /** ITEM_ADD */
                public static final Action ITEM_ADD                        = new ActionConst();

                /** ITEM_DELETE */
                public static final Action ITEM_DELETE                     = new ActionConst();

                /** ITEM_SECURITY_PERSON */
                public static final Action ITEM_SECURITY_PERSON            = new ActionConst();
                /** ITEM_SECURITY_OE */
                public static final Action ITEM_SECURITY_OE                = new ActionConst();
                /** KNOWLEDGE_OBJECT_EXCEL_EXPORT */
                public static final Action KNOWLEDGE_OBJECT_EXCEL_EXPORT   = new ActionConst();

                /** KNOWLEDGE_DOMAIN_EXCEL_EXPORT */
                public static final Action KNOWLEDGE_DOMAIN_EXCEL_EXPORT   = new ActionConst();

                /** PROCESSES_EXCEL_EXPORT */
                public static final Action PROCESSES_EXCEL_EXPORT          = new ActionConst();

                /** PROCESSES_EXCEL_EXPORT_COMPLETE */
                public static final Action PROCESSES_EXCEL_EXPORT_COMPLETE = new ActionConst();

                /** DOCUMENTS_EXCEL_EXPORT */
                public static final Action DOCUMENTS_EXCEL_EXPORT          = new ActionConst();

                /** FEEADBACK_MAIL */
                public static final Action FEEADBACK_MAIL                  = new ActionConst();

                private Actions()
                {

                }
            }

        }

        /**
         * 
         * This class included all resources used in header
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Header
        {
            // inner classes of Header

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                // inner classes of Frames.Header.Actions

                /**
                 * LOGOUT
                 */
                public static final Action LOGOUT              = new ActionConst();

                /**
                 * MAIN_CONFIG
                 */
                public static final Action MAIN_CONFIG         = new ActionConst();

                /** MAIN_CONFIG */
                public static final Action CHANGE_PASSWORD     = new ActionConst();

                /**
                 * ABAOUT
                 */
                public static final Action ABAOUT              = new ActionConst();

                /**
                 * SEARCH
                 */
                public static final Action SEARCH              = new ActionConst();

                /**
                 * DELETE_SEARCH_FIELD
                 */
                public static final Action DELETE_SEARCH_FIELD = new ActionConst();

                private Actions()
                {
                }
            }

            /**
             * 
             * Text constants in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {
                // inner classes of Frames.Header.Texts
                /**
                 * SEARCH_TEXT
                 */
                public static final Text SEARCH_TEXT = new TextConst();

                /**
                 * LOGIN_USER
                 */
                public static final Text LOGIN_USER  = new TextConst();

                private Texts()
                {
                }

            }

            /**
             * 
             * Image constants in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * IMAGE_BANNER_BG
                 */
                public static final Image IMAGE_BANNER_BG     = new ImageConst();

                /**
                 * IMAGE_PROWIM_LOGO
                 */
                public static final Image IMAGE_PROWIM_LOGO   = new ImageConst();

                /**
                 * IMAGE_BG_ORANGE
                 */
                public static final Image IMAGE_BG_ORANGE     = new ImageConst();

                /**
                 * NAVI_SUB_BGD
                 */
                public static final Image NAVI_SUB_BGD        = new ImageConst();

                /**
                 * NAVI_SHADOW_SUB_BGD
                 */
                public static final Image NAVI_SHADOW_SUB_BGD = new ImageConst();

                private Images()
                {
                }

            }
        }

        /**
         * 
         * This class included all resources used in NavigationBar
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Navigation
        {
            // inner classes of Frames.Navigation

            /**
             * 
             * Resources to execute Actions in NavigationBar
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                // inner classes of Frame.NavigationBar.Actions
                /**
                 * WORK_LIST_NAV
                 */
                public static final Action WORK_LIST_NAV                = new ActionConst();

                /**
                 * PROCESS_NAV
                 */
                public static final Action PROCESS_NAV                  = new ActionConst();

                /**
                 * DOCUMENT_NAV
                 */
                public static final Action DOCUMENT_NAV                 = new ActionConst();

                /**
                 * KNOWLEDGE_NAV
                 */
                public static final Action KNOWLEDGE_NAV                = new ActionConst();

                /**
                 * DOCUMENT_SEARCH_NAV
                 */
                public static final Action DOCUMENT_SEARCH_NAV          = new ActionConst();

                /**
                 * EXISTING_DOCUMENTS_NAV
                 */
                public static final Action EXISTING_DOCUMENTS_NAV       = new ActionConst();

                /**
                 * DOCUMENT_FULL_SEARCH_NAV
                 */
                public static final Action DOCUMENT_FULL_SEARCH_NAV     = new ActionConst();

                /**
                 * DOCUMENT_UPLOAD_NAV
                 */
                public static final Action DOCUMENT_UPLOAD_NAV          = new ActionConst();

                /**
                 * DOCUMENT_MULTIPLE_UPLOAD_NAV
                 */
                public static final Action DOCUMENT_MULTIPLE_UPLOAD_NAV = new ActionConst();

                /**
                 * KNOWLEDGE_PROCESS_NAV
                 */
                public static final Action KNOWLEDGE_PROCESS_NAV        = new ActionConst();

                /**
                 * KNOWLEDGE_BROWSER_NAV
                 */
                public static final Action KNOWLEDGE_BROWSER_NAV        = new ActionConst();

                /**
                 * KNOWLEDGE_SEARCH_NAV
                 */
                public static final Action KNOWLEDGE_SEARCH_NAV         = new ActionConst();

                /** KNOWLEDGE_STRUCTURE_NAV */
                public static final Action KNOWLEDGE_STRUCTURE_NAV      = new ActionConst();

                /** KNOWLEDGE_WIKI_NAV */
                public static final Action KNOWLEDGE_WIKI_NAV           = new ActionConst();

                /** KNOWLEDGE_WIKI_NAV */
                public static final Action ADMIN_NAV                    = new ActionConst();

                private Actions()
                {
                }
            }

            /**
             * Conts for Images of navigation
             */
            public static final class Images
            {
                /**
                 * MENU_SELECTED_BGD
                 */
                public static final Image MENU_SELECTED_BGD   = new ImageConst();

                /**
                 * MENU_SELECTED_BGD
                 */
                public static final Image MENU_SHADOW_SUB_BGD = new ImageConst();

                private Images()
                {
                }

            }

        }

        /**
         * 
         * Toolbar
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Toolbar
        {
            // inner classes of Frame

            /**
             * Conts for Images of toolbar
             */
            public static final class Images
            {
                /**
                 * START_TOOL_BAR
                 */
                public static final Image IMAGE_TOLLBAR_BACKGROUND = new ImageConst();

                private Images()
                {
                }
            }

            /**
             * Actions for Toolbar
             */
            public static final class Actions
            {
                // inner classes of Frame.NavigationBar
                /**
                 * START_TOOL_BAR
                 */
                public static final Action START_PROCESS               = new ActionConst();

                /**
                 * CREATE_PROCESS
                 */
                public static final Action CREATE_PROCESS              = new ActionConst();

                /**
                 * HELP_TOOL_BAR
                 */
                public static final Action HELP_TOOL_BAR               = new ActionConst();

                /**
                 * PRINT_TOOL_BAR
                 */
                public static final Action PRINT_TOOL_BAR              = new ActionConst();

                /**
                 * DELETE_PROC_TOOL_BAR
                 */
                public static final Action DELETE_PROC_TOOL_BAR        = new ActionConst();

                /**
                 * ADD_VALUE_TOOL_BAR
                 */
                public static final Action ADD_VALUE_TOOL_BAR          = new ActionConst();

                /**
                 * DELETE_VALUE_TOOL_BAR
                 */
                public static final Action DELETE_VALUE_TOOL_BAR       = new ActionConst();

                /**
                 * SHOW_PROCESS_MODEL
                 */
                public static final Action SHOW_PROCESS_MODEL          = new ActionConst();

                /**
                 * Action which shows the versions of a process template.
                 */
                public static final Action SHOW_VERSIONS               = new ActionConst();

                /** CREATE_NEW_PROCESS */
                public static final Action CREATE_NEW_PROCESS          = new ActionConst();

                /** CREATE_NEW_ROLE. */
                public static final Action CREATE_NEW_ROLE             = new ActionConst();

                /** CREATE_NEW_MEAN. */
                public static final Action CREATE_NEW_MEAN             = new ActionConst();

                /** CREATE_NEW_RESULTS_MEM. */
                public static final Action CREATE_NEW_RESULTS_MEM      = new ActionConst();

                /** CREATE_NEW_PROCESS_TYPE */
                public static final Action CREATE_NEW_PROCESS_TYPE     = new ActionConst();

                /** DELETE_PROCESS_CATEGORY */
                public static final Action DELETE_PROCESS_CATEGORY     = new ActionConst();

                /** EDIT_NAME_DESCRIPTION */
                public static final Action EDIT_NAME_DESCRIPTION       = new ActionConst();

                /** DELETE SUB PROCESS. */
                public static final Action DELETE_SUB_PROCESS          = new ActionConst();

                /** SHOW_ONLY_PROCESS_LANDSCAPE. */
                public static final Action SHOW_ONLY_PROCESS_LANDSCAPE = new ActionConst();

                /** SHOW_ALL_PROCESSES. */
                public static final Action SHOW_ALL_PROCESSES          = new ActionConst();

                private Actions()
                {
                }
            }

            /**
             * Conts for Texts of toolbar
             */
            public static final class Texts
            {
                /**
                 * START_TOOL_BAR
                 */
                public static final Text DELETE_PROCESS_CATEGORY_TEXT = new TextConst();

                private Texts()
                {
                }

            }

        }

        /**
         * 
         * Included all actions, texts an images, which belong to dialogs
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Dialog
        {
            /**
             * 
             * Actions for dialog area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {

                /** EDIT_USER. */
                public static final Action EDIT_USER                       = new ActionConst();

                /**
                 * ADD_USER
                 */
                public static final Action ADD_USER                        = new ActionConst();

                /**
                 * ADD_USER
                 */
                public static final Action ADD_USER_TO_ROLE                = new ActionConst();

                /** ADD_USER_EXECUTE_ROLE */
                public static final Action ADD_USER_EXECUTE_ROLE           = new ActionConst();

                /**
                 * ADD_USER_TO_LIST
                 */
                public static final Action ADD_USER_TO_LIST                = new ActionConst();

                /**
                 * DELETE_USER_FROM_LIST
                 */
                public static final Action DELETE_USER_FROM_LIST           = new ActionConst();

                /** FORWARD_ADD_SELECTED_ROLE. */
                public static final Action FORWARD_ADD_SELECTED_ROLE       = new ActionConst();

                /** FORWARD_ADD_SELECTED_ROLE. */
                public static final Action FORWARD_DOUBLE_ADD_ALL_ROLES    = new ActionConst();

                /** FORWARD_ADD_SELECTED_ROLE. */
                public static final Action FORWARD_REMOVE_SELECTED_ROLE    = new ActionConst();

                /** FORWARD_ADD_SELECTED_ROLE. */
                public static final Action FORWARD_DOUBLE_REMOVE_ALL_ROLES = new ActionConst();

                /**
                 * ADD_TEXT
                 */
                public static final Action ADD_TEXT                        = new ActionConst();

                /**
                 * ADD_LINK
                 */
                public static final Action ADD_LINK                        = new ActionConst();

                /**
                 * ADD_DATE
                 */
                public static final Action ADD_DATE                        = new ActionConst();

                /**
                 * ADD_FILE
                 */
                public static final Action ADD_FILE                        = new ActionConst();

                /** KNOWLEDGE_LINK_DETAILS. */
                public static final Action KNOWLEDGE_LINK_DETAILS          = new ActionConst();

                /** KNOWLEDGE_LINK_DETAILS. */
                public static final Action REMOVE_KNOWLEDGE_OBJECT         = new ActionConst();

                /**
                 * ADD_LONG_TEXT
                 */
                public static final Action ADD_LONG_TEXT                   = new ActionConst();

                /**
                 * ADD_SINGLE_LIST
                 */
                public static final Action ADD_SINGLE_LIST                 = new ActionConst();

                /**
                 * ADD_MULTI_LIST
                 */
                public static final Action ADD_MULTI_LIST                  = new ActionConst();

                /**
                 * ADD_COMBO_BOX
                 */
                public static final Action ADD_COMBO_BOX                   = new ActionConst();

                /**
                 * ADD_ORG_UNIT
                 */
                public static final Action ADD_ORG_UNIT                    = new ActionConst();

                /**
                 * ADD_INTEGER
                 */
                public static final Action ADD_INTEGER                     = new ActionConst();

                /**
                 * ADD_FLOAT
                 */
                public static final Action ADD_FLOAT                       = new ActionConst();

                /**
                 * DATA_LOSING
                 */
                public static final Action DATA_LOSING                     = new ActionConst();

                /**
                 * ADD_KNOW_LINK
                 */
                public static final Action ADD_KNOW_LINK                   = new ActionConst();

                /**
                 * KNOW_LINK_DETAILS
                 */
                public static final Action KNOW_LINK_DETAILS               = new ActionConst();

                /**
                 * DELETE_KNOW_LINK
                 */
                public static final Action DELETE_KNOW_LINK                = new ActionConst();

                /**
                 * OPEN_HYPER_LINK
                 */
                public static final Action OPEN_HYPER_LINK                 = new ActionConst();

                /**
                 * OPEN_HYPER_LINK
                 */
                public static final Action OPEN_EXISTING_LINK              = new ActionConst();

                /**
                 * ADD_ORGA
                 */
                public static final Action ADD_ORGA                        = new ActionConst();

                /**
                 * ADD_EDIT_ORGA
                 */
                public static final Action EDIT_ORGA                       = new ActionConst();

                /**
                 * ADD_ITEM_TO_OBJEKT
                 */
                public static final Action ADD_ITEM_TO_OBJEKT              = new ActionConst();

                /**
                 * ADD_KNOWLEDGEOBJECT_TO_ITEM
                 */
                public static final Action ADD_KNOWLEDGEOBJECT_TO_ITEM     = new ActionConst();

                /**
                 * SET_NAME_OF_SWIMLANE
                 */
                public static final Action SET_NAME_OF_SWIMLANE            = new ActionConst();

                /**
                 * ADD_PERSON_TO_ORGANIZATION
                 */
                public static final Action ADD_PERSON_TO_ORGANIZATION      = new ActionConst();

                /**
                 * ADD_ROLE_TO_ORGANIZATION
                 */
                public static final Action ADD_ROLE_TO_ORGANIZATION        = new ActionConst();

                /**
                 * MOVE_ORGANIZATION
                 */
                public static final Action MOVE_ORGANIZATION               = new ActionConst();

                /**
                 * DELETE_ORGANIZATION
                 * */
                public static final Action DELETE_ORGANIZATION             = new ActionConst();

                /** REMOVE_ROLE_FROM_ORGANIZATION. */
                public static final Action REMOVE_ROLE_FROM_ORGANIZATION   = new ActionConst();

                /**
                 * ADD_EDIT_USER
                 */
                public static final Action ADD_EDIT_USER                   = new ActionConst();

                /**
                 * Shows informations to a selected person
                 */
                public static final Action SHOW_USER_INFORMATION           = new ActionConst();

                /**
                 * ADD_NEW_USER.
                 * 
                 * */
                public static final Action ADD_NEW_USER                    = new ActionConst();

                /** GET_ALL_USER. */
                public static final Action GET_ALL_USER                    = new ActionConst();

                /**
                 * ADD_EDIT_DOMAIN
                 */
                public static final Action ADD_EDIT_DOMAIN                 = new ActionConst();

                /**
                 * REQUIRED_ERROR_DIALOG
                 */
                public static final Action REQUIRED_ERROR_DIALOG           = new ActionConst();

                /** The error dialog to show when the user has entered a text with wrong length */
                public static final Action TEXT_LENGTH_ERROR_DIALOG        = new ActionConst();

                /** PASSWORDS_EQUALS_DIALOG. */
                public static final Action PASSWORDS_EQUALS_DIALOG         = new ActionConst();

                /**
                 * REQUIRED_ONE_ITEM
                 */
                public static final Action REQUIRED_ONE_ITEM               = new ActionConst();

                /**
                 * INCOMPATIBLE_SELECTION
                 */
                public static final Action INCOMPATIBLE_SELECTION          = new ActionConst();

                /** PRODUCT_PARAM_DIALOG */
                public static final Action PRODUCT_PARAM_DIALOG            = new ActionConst();

                /** DELETE_USER */
                public static final Action DELETE_USER                     = new ActionConst();

                /** SELECT_CREATE_ROLE */
                public static final Action SELECT_CREATE_ROLE              = new ActionConst();

                /** SELECT_CREATE_MEAN */
                public static final Action SELECT_CREATE_MEAN              = new ActionConst();

                /** SELECT_CREATE_RESULTS_MEM */
                public static final Action SELECT_CREATE_RESULTS_MEM       = new ActionConst();

                /** CREATE_NEW_ROLE */
                public static final Action CREATE_NEW_ROLE                 = new ActionConst();

                /** CREATE_NEW_MEAN */
                public static final Action CREATE_NEW_MEAN                 = new ActionConst();

                /** CREATE_NEW_RESULTS_MEM */
                public static final Action CREATE_NEW_RESULTS_MEM          = new ActionConst();

                /** UPLOAD_DOCUMENT */
                public static final Action UPLOAD_DOCUMENT                 = new ActionConst();

                /** Action to activate a process template version */
                public static final Action ACTIVATE_VERSION                = new ActionConst();

                /** Action to activate a process template version */
                public static final Action VIEW_VERSION                    = new ActionConst();

                /** Action to activate the newest version */
                public static final Action ACTIVATE_NEWEST_VERSION         = new ActionConst();

                /** Action to rename a process template version */
                public static final Action RENAME_VERSION                  = new ActionConst();

                /** Action to copy the URL from a textfield (or so) to the clipboard */
                public static final Action COPY_URL_TO_CLIPBOARD           = new ActionConst();

                /**
                 * A dummy action to show information dialog in model editor for showing the activity relations
                 */
                public static final Action SHOW_ACTIVITY_RELATIONS         = new ActionConst();

                /**
                 * Action for support mail dialog to send error stacks through user
                 */
                public static final Action SUPPORT_MAIL_DIALOG             = new ActionConst();

                /**
                 * Action for dialog to select a sub process
                 */
                public static final Action SELECT_SUB_PROCESS_DIALOG       = new ActionConst();

                /**
                 * Action for dialog to select a relation
                 */
                public static final Action SELECT_RELATION_DIALOG          = new ActionConst();

                /**
                 * Action to show url of a model
                 */
                public static final Action SHOW_MODEL_URL                  = new ActionConst();

                /** Action to select a {@link ProcessType} url of a model */
                public static final Action SET_PROZESSTYPE_LANDSCAPE       = new ActionConst();

                /** Action to select a {@link org.prowim.datamodel.prowim.Process} */
                public static final Action SET_PROZESS_LANDSCAPE           = new ActionConst();

                private Actions()
                {

                }
            }

            /**
             * 
             * Images for process area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * DIALOG_TABLE
                 */
                public static final Image DIALOG_PERSON             = new ImageConst();

                /**
                 * DIALOG_DATE
                 */
                public static final Image DIALOG_DATE               = new ImageConst();

                /**
                 * DIALOG_DEFAULT
                 */
                public static final Image DIALOG_DEFAULT            = new ImageConst();

                /** CONFIRM_IMAGE */
                public static final Image CONFIRM_IMAGE             = new ImageConst();

                /** INFORMATION_IMAGE */
                public static final Image INFORMATION_IMAGE         = new ImageConst();

                /** Image for support mail dialog */
                public static final Image SUPPORT_MAIL_DIALOG_IMAGE = new ImageConst();

                private Images()
                {

                }
            }

            /**
             * 
             * Text, which are comming in a dialog
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /** USER_SEARCH. */
                public static final Text USER_SEARCH                          = new TextConst();
                /** USER_SELECTION. */

                /** USER_LABEL. */
                public static final Text USER_LABEL                           = new TextConst();
                /** USER_SELECTION. */
                public static final Text USER_SELECTION                       = new TextConst();
                /**
                 * SELECTED_DATE
                 */
                public static final Text SELECTED_DATE                        = new TextConst();

                /**
                 * SELECTED_TIME
                 */
                public static final Text SELECTED_TIME                        = new TextConst();

                /**
                 * SHOW_DATE_TIME
                 */
                public static final Text SHOW_DATE_TIME                       = new TextConst();

                /**
                 * BROWSE
                 */
                public static final Text BROWSE                               = new TextConst();

                /**
                 * FILE_UPLOAD
                 */
                public static final Text FILE_UPLOAD                          = new TextConst();

                /**
                 * DELETE_DESCRIPTION
                 */
                public static final Text DELETE_DESCRIPTION                   = new TextConst();

                /**
                 * ALL_USERS
                 */
                public static final Text ALL_USERS                            = new TextConst();

                /**
                 * SELECTED_USERS
                 */
                public static final Text SELECTED_USERS                       = new TextConst();

                /**
                 * ALL_ORGS
                 */
                public static final Text ALL_ORGS                             = new TextConst();

                /**
                 * SELECTED_ORGS
                 */
                public static final Text SELECTED_ORGS                        = new TextConst();

                /**
                 * ALL_PROCESS_TYPES
                 */
                public static final Text ALL_PROCESS_TYPES                    = new TextConst();

                /**
                 * SELECTED_PROCESS_TYPES
                 */
                public static final Text SELECTED_PROCESS_TYPES               = new TextConst();

                /**
                 * TEXT_FOR_DATA_LOSING
                 */
                public static final Text TEXT_FOR_DATA_LOSING                 = new TextConst();

                /**
                 * VALUE_NOT_SET
                 */
                public static final Text VALUE_NOT_SET                        = new TextConst();

                /** SAVE_PROCESS_START_DATA */
                public static final Text SAVE_PROCESS_START_DATA              = new TextConst();

                /**
                 * VALUE_NOT_SET_DESC
                 */
                public static final Text VALUE_NOT_SET_DESC                   = new TextConst();

                /** NO_SELECTION */
                public static final Text NO_SELECTION                         = new TextConst();

                /** LOGIN_MAIN_TITLE */
                public static final Text LOGIN_MAIN_TITLE                     = new TextConst();

                /** LOGOUT_TEXT */
                public static final Text LOGOUT_TEXT                          = new TextConst();

                /** ADD_ITEM_TO_A_OBJECT */
                public static final Text ADD_ITEM_TO_A_OBJECT                 = new TextConst();

                /** PROCESS_AND_ACTIVTY */
                public static final Text PROCESS_AND_ACTIVTY                  = new TextConst();

                /** ORGANIZATIONS_AND_ROLES */
                public static final Text ORGANIZATIONS_AND_ROLES              = new TextConst();

                /** SELECT_ITEM_IN_TREE */
                public static final Text SELECT_ITEM_IN_TREE                  = new TextConst();

                /** ABOUT_DESCRIPTION */
                public static final Text ABOUT_DESCRIPTION                    = new TextConst();

                /** CHANGE_PASSWORD_DESCRIPTION. */
                public static final Text CHANGE_PASSWORD_DESCRIPTION          = new TextConst();

                /** VERSION_OF_SOFTWARE */
                public static final Text VERSION_OF_SOFTWARE                  = new TextConst();

                /** FILE_ERROR_MESSAGE */
                public static final Text FILE_ERROR_MESSAGE                   = new TextConst();

                /** Default title of error messages */
                public static final Text ERROR_TITLE                          = new TextConst();

                /** Default Message for exception handling */
                public static final Text ERROR_MESSAGE                        = new TextConst();

                /** Default title of warning messages */
                public static final Text WARNING_TITLE                        = new TextConst();

                /** Default title of confirm messages */
                public static final Text CONFIRM_TITLE                        = new TextConst();

                /** Default title of information messages */
                public static final Text INFORMATION_TITLE                    = new TextConst();

                /** Default title for create edit person dialog */
                public static final Text DESC_USER_CREATE_EDIT                = new TextConst();

                /** Description to show the informations of a user */
                public static final Text DESC_USER_SHOW_INFORMATION           = new TextConst();

                /** Default title for create edit organisation unit dialog */
                public static final Text DESC_ORGANISATION_CREATE             = new TextConst();

                /** Default title for create edit organisation unit dialog */
                public static final Text DESC_ORGANISATION_EDIT               = new TextConst();

                /** Text for validation error in the dialogs */
                public static final Text VALIDATION_INFO_TEXT                 = new TextConst();

                /** EXIST_ROLES */
                public static final Text EXIST_ROLES                          = new TextConst();

                /** EXIST_MEANS */
                public static final Text EXIST_MEANS                          = new TextConst();

                /** EXIST_RESULTS_MEM */
                public static final Text EXIST_RESULTS_MEM                    = new TextConst();

                /** DESC_EXIST_ROLES */
                public static final Text DESC_EXIST_ROLES                     = new TextConst();

                /** DESC_EXIST_MEAN */
                public static final Text DESC_EXIST_MEAN                      = new TextConst();

                /** DESC_EXIST_RESULTS_MEM */
                public static final Text DESC_EXIST_RESULTS_MEM               = new TextConst();

                /** SET_AS_LOCAL */
                public static final Text SET_AS_LOCAL                         = new TextConst();

                /** NO_DATA */
                public static final Text NO_DATA                              = new TextConst();

                /** ASSIGN. */
                public static final Text ASSIGN                               = new TextConst();

                /** OPEN. */
                public static final Text OPEN                                 = new TextConst();

                /** EXISTNG_DOCUMENT. */
                public static final Text EXISTING_DOCUMENT                    = new TextConst();

                /** NEW */
                public static final Text NEW                                  = new TextConst();

                /** The error headerTitle if an error occurs when activating a version */
                public static final Text ERROR_ACTIVATE_VERSION               = new TextConst();

                /** The info headerTitle if a version has been activated */
                public static final Text SUCCESS_ACTIVATE_VERSION             = new TextConst();

                /** The error headerTitle to show if a version name is already set */
                public static final Text ERROR_DUPLICATE_VERSION_NAME         = new TextConst();

                /** Message shown when user clicks on "Save model" */
                public static final Text SAVE_MODEL_PLEASE_WAIT               = new TextConst();

                /** Message shown when process model has been saved */
                public static final Text SAVE_MODEL_SUCCESS                   = new TextConst();

                /** Existing Processes. */
                public static final Text EXISTING_PROCESSES                   = new TextConst();

                /** Successful cloning a process */
                public static final Text PROCESS_CLONED                       = new TextConst();

                /** Clone a process */
                public static final Text CLONE_SELECTED_PROCESS_VERSION       = new TextConst();

                /** Message to waiting while process cloning */
                public static final Text CLONE_MODEL_PLEASE_WAIT              = new TextConst();

                /**
                 * Warning for no hyper link in knowledge link
                 */
                public static final Text WARNING_NO_LINK                      = new TextConst();

                /** Message to show in support mail dialog */
                public static final Text SUPPORT_MAIL_DLG_DESCRIPTION         = new TextConst();

                /** Message to show in support mail dialog if a file in DMS is missing */
                public static final Text SUPPORT_MAIL_DLG_DESCRIPTION_FOR_DMS = new TextConst();

                /** Message to show as content text in support mail dialog */
                public static final Text SUPPORT_MAIL_DLG_CONTENT             = new TextConst();

                /** send mail */
                public static final Text SEND_MAIL                            = new TextConst();

                /** Message to show as content text in activity details dialog */
                public static final Text ACTIVITY_DETAILS_DLG_DESCRIPTION     = new TextConst();

                /** Message to copy the URL of a model */
                public static final Text SHOW_MODEL_URL_DESCRIPTION           = new TextConst();

                /** Title to show a dialog for a existing document in DMS */
                public static final Text DOCUMENT_EXISTS_DIALOG_TITLE         = new TextConst();

                /** Description to show a dialog for a existing document in DMS */
                public static final Text DOCUMENT_EXISTS_DIALOG_DESCRIPTION   = new TextConst();

                /** Description to show a dialog for a relation of a elemnt to a process */
                public static final Text SHOW_RELATIONS_DLG_DESCRIPTION       = new TextConst();

                /** Description to show in a dialog to delete a {@link ProcessElement} */
                public static final Text SHOW_DELETE_ELEMENT_DLG_DESCRIPTION  = new TextConst();

                /** Tool tip for key word filed to separate this wit a comma */
                public static final Text KEY_WORDS_TOOLTIPS_SEPARATE_COMMA    = new TextConst();

                private Texts()
                {

                }
            }
        }

        /**
         * 
         * Consts for process group
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Process
        {

            /**
             * 
             * Actions for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                /**
                 * PROCESS_START_NAV
                 */
                public static final Action PROCESS_START_NAV     = new ActionConst();

                /**
                 * PROCESS_ACTIVE_NAV
                 */
                public static final Action PROCESS_ACTIVE_NAV    = new ActionConst();

                /**
                 * PROCESS_FINISH_NAV
                 */
                public static final Action PROCESS_FINISH_NAV    = new ActionConst();

                /**
                 * PROCESS_MODELER_NAV
                 */
                public static final Action PROCESS_MODELLER_NAV  = new ActionConst();

                /**
                 * PROCESS_MODELER2_NAV
                 */
                public static final Action PROCESS_MODELLER2_NAV = new ActionConst();

                /**
                 * PROCESS_PARAMETER
                 */
                public static final Action PROCESS_PARAMETER     = new ActionConst();

                /**
                 * PROCESS_INFORMATIONS
                 */
                public static final Action PROCESS_INFORMATIONS  = new ActionConst();

                /**
                 * PROCESS_BROWSER
                 */
                public static final Action PROCESS_BROWSER       = new ActionConst();

                /**
                 * NEW_PROCESS
                 */
                public static final Action NEW_PROCESS           = new ActionConst();

                /** NEW_PROCESS */
                public static final Action SET_PROCESS_TYPE      = new ActionConst();

                /** COPY_PROCESS */
                public static final Action COPY_PROCESS          = new ActionConst();

                /** SET_PROCESS_NAME */
                public static final Action SET_PROCESS_NAME      = new ActionConst();

                private Actions()
                {

                }
            }

            /**
             * 
             * Texts constants for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * TITLE_ACTIVE_PROCESSES
                 */
                public static final Text TITLE_ACTIVE_PROCESSES                 = new TextConst();

                /**
                 * TITLE_FINISHED_PROCESSES
                 */
                public static final Text TITLE_FINISHED_PROCESSES               = new TextConst();

                /**
                 * PROCESS_NAME
                 */
                public static final Text PROCESS_NAME                           = new TextConst();

                /**
                 * PROCESS_DESCRIPTION
                 */
                public static final Text PROCESS_DESCRIPTION                    = new TextConst();

                /**
                 * PROCESS_TYPE
                 */
                public static final Text PROCESS_TYPE                           = new TextConst();

                /** PROCESS_TYPES */
                public static final Text PROCESS_TYPES                          = new TextConst();

                /**
                 * PROCESS_GROUP_HEADER
                 */
                public static final Text PROCESS_GROUP_HEADER                   = new TextConst();

                /**
                 * PROCESS_KNOWLEDGE
                 */
                public static final Text PROCESS_KNOWLEDGE                      = new TextConst();

                /**
                 * TITLE_WORKING_LIST.
                 */
                public static final Text TITLE_WORKING_LIST                     = new TextConst();

                /**
                 * ROLE_SETTING_GROUP.
                 */
                public static final Text ROLE_SETTING_GROUP                     = new TextConst();

                /**
                 * START_PARAMETER_FOR.
                 */
                public static final Text START_PARAMETER_FOR                    = new TextConst();

                /**
                 * BESET_WITH.
                 */
                public static final Text BESET_WITH                             = new TextConst();

                /**
                 * PROCESS.
                 */
                public static final Text PROCESS                                = new TextConst();

                /** "PROCESSES ". */
                public static final Text PROCESSES                              = new TextConst();

                /**
                 * START_AT.
                 */
                public static final Text START_AT                               = new TextConst();

                /**
                 * END_AT.
                 */
                public static final Text END_AT                                 = new TextConst();

                /**
                 * DESCRIPTION_OF_PROCESS.
                 */
                public static final Text DESCRIPTION_OF_PROCESS                 = new TextConst();

                /**
                 * DESCRIPTION_FOR_PROCESS_START.
                 */
                public static final Text DESCRIPTION_FOR_PROCESS_START          = new TextConst();

                /**
                 * SELECTIVE_PROCESS.
                 */
                public static final Text SELECTIVE_PROCESS                      = new TextConst();

                /** SELECTIVE_PROCESS. */
                public static final Text SELECTION_PROCESS                      = new TextConst();

                /** CREATE_NEW_PROCESS. */
                public static final Text CREATE_NEW_PROCESS                     = new TextConst();

                /** CREATE_NEW_PROCESS_TYPE. */
                public static final Text CREATE_NEW_PROCESS_TYPE                = new TextConst();

                /** OPEN_EXISTING_PROCESS. */
                public static final Text OPEN_EXISTING_PROCESS                  = new TextConst();

                /** NEW_PROCESS. */
                public static final Text NEW_PROCESS                            = new TextConst();

                /** Newest version of a process */
                public static final Text NEWEST_VERSION                         = new TextConst();

                /** Text is shown when activating a version of a process */
                public static final Text PROCESS_VERSION_ACTIVATE_QUESTION      = new TextConst();

                /** TODO Philipp Leusmann **/
                public static final Text PROCESS_HIJACK_MESSAGE                 = new TextConst();

                /** TODO Philipp Leusmann **/
                public static final Text PROCESS_LOCKED_EDITOR_QUESTION         = new TextConst();

                /** TODO Philipp Leusmann **/
                public static final Text PROCESS_HIJACK_FAILED                  = new TextConst();

                /** TODO Philipp Leusmann **/
                public static final Text PROCESS_LOCKED_VERSION_DIALOG_QUESTION = new TextConst();

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for process area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * START_TOOL_BAR
                 */
                public static final Image PROCESS_TYPE_IMAGE = new ImageConst();

                /**
                 * PROCESS_LAND_SCAPE
                 */
                public static final Image PROCESS_LAND_SCAPE = new ImageConst();

                private Images()
                {
                }
            }

            private Process()
            {
            }

        }

        /**
         * i18n entries for activity
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Activity
        {
            /**
             * 
             * Actions for Activity area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                /**
                 * EDIT_ACTIVITY
                 */
                public static final Action EDIT_ACTIVITY  = new ActionConst();

                /**
                 * SAVE_ACTIVITY
                 */
                public static final Action SAVE_ACTIVITY  = new ActionConst();

                /**
                 * CLOSE_ACTIVITY
                 */
                public static final Action CLOSE_ACTIVITY = new ActionConst();

                private Actions()
                {
                }
            }

            /**
             * i18n entries for activity->texts
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * DELIVERABLES_ACTIVITY
                 */
                public static final Text DELIVERABLES_ACTIVITY   = new TextConst();

                /**
                 * TABLE_HEADER_ACTIVSINCE
                 */
                public static final Text TABLE_HEADER_ACTIVSINCE = new TextConst();

                /**
                 * TITLE_WORKING_LIST
                 */
                public static final Text TITLE_WORKING_LIST      = new TextConst();

                /**
                 * ACTIVITY_GROUP_HEADER.
                 */
                public static final Text ACTIVITY_GROUP_HEADER   = new TextConst();

                /**
                 * SELECTIVE_ACTIVITY.
                 */
                public static final Text SELECTIVE_ACTIVITY      = new TextConst();

                /**
                 * INPUT_PARAMETER.
                 */
                public static final Text INPUT_PARAMETER         = new TextConst();

                /**
                 * OUTPUT_PARAMETER.
                 */
                public static final Text OUTPUT_PARAMETER        = new TextConst();

                /**
                 * DECISION_PARAMETER.
                 */
                public static final Text DECISION_PARAMETER      = new TextConst();

                /**
                 * DESCRIPTION_OF_ACTIVITY.
                 */
                public static final Text DESCRIPTION_OF_ACTIVITY = new TextConst();

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for activity area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /** ACTIVITY_IMAGE */
                public static final Image ACTIVITY_IMAGE = new ImageConst();

                private Images()
                {

                }
            }
        }

        /**
         * i18n entries for mean
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Mean
        {
            /**
             * 
             * Actions for Mean area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                private Actions()
                {
                }
            }

            /**
             * i18n entries for mean->texts
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for mean area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /** MEAN_IMAGE */
                public static final Image MEAN_IMAGE = new ImageConst();

                private Images()
                {

                }
            }
        }

        /**
         * i18n entries for role
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Role
        {
            /**
             * 
             * Actions for role area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                private Actions()
                {
                }
            }

            /**
             * i18n entries for rolen->texts
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * ROLE.
                 */
                public static final Text ROLE = new TextConst();

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for role area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /** ROLE_IMAGE */
                public static final Image ROLE_IMAGE       = new ImageConst();

                /** START_ROLE_IMAGE */
                public static final Image START_ROLE_IMAGE = new ImageConst();

                private Images()
                {

                }
            }
        }

        /**
         * i18n entries for RESULTS_MEMORY
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class RESULTSMEMORY
        {
            /**
             * 
             * Actions for RESULTSMEMORY area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                private Actions()
                {
                }
            }

            /**
             * i18n entries for RESULTSMEMORY->texts
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for RESULTSMEMORY area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /** RESULTSMEMORY_IMAGE */
                public static final Image RESULTSMEMORY_IMAGE = new ImageConst();

                private Images()
                {

                }
            }
        }

        /**
         * 
         * i18n entries for for Processelement component
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Processelement
        {
            // Inner class for Tree

            /**
             * This class included all actions, which can activate in Processelement
             */
            public static final class Actions
            {
                /**
                 * SHOW_RELATIONS
                 */
                public static final Action SHOW_RELATIONS = new ActionConst();

                private Actions()
                {

                }
            }

            /**
             * 
             * Image constants for Processelement class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * Default constructor
                 */
                private Images()
                {
                }
            }

            /**
             * 
             * Texts constants for Processelement
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * Default constructor
                 */
                private Texts()
                {
                }
            }
        }

        /**
         * 
         * i18n entries for for Documents
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Document
        {
            /**
             * This class included all actions, which can activate in Documents
             */
            public static final class Actions
            {
                /**
                 * Default constructor
                 */
                private Actions()
                {
                }
            }

            /**
             * 
             * Image constants for Processelement class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * Default constructor
                 */
                private Images()
                {
                }
            }

            /**
             * 
             * Texts constants for Processelement
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {
                /** FILE_UPLOAD_WAIT */
                public static final Text FILE_UPLOAD_WAIT = new TextConst();
                /** UPLOAD_FINISHED */
                public static final Text UPLOAD_FINISHED  = new TextConst();
                /** FILE_UPDATED */
                public static final Text FILE_UPDATED     = new TextConst();

                /**
                 * Default constructor
                 */
                private Texts()
                {
                }
            }
        }

        /**
         * 
         * i18n entries for for tree component
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Tree
        {
            // Inner class for Tree

            /**
             * This class included all actions, which can activate in tree
             */
            public static final class Actions
            {
                // Inner class for Tree.Actions

                /**
                 * EXPAND_TREE
                 */
                public static final Action EXPAND_TREE = new ActionConst();

                /**
                 * REDUCE_TREE
                 */
                public static final Action REDUCE_TREE = new ActionConst();

                /**
                 * RELOAD_TREE
                 */
                public static final Action RELOAD_TREE = new ActionConst();

                /**
                 * FLAT_TREE
                 */
                public static final Action FLAT_TREE   = new ActionConst();

                private Actions()
                {

                }
            }

            /**
             * 
             * Image constants for tree class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /**
                 * TREE_FOLDER
                 */
                public static final Image TREE_FOLDER      = new ImageConst();

                /**
                 * USER_IMAGE
                 */
                public static final Image USER_IMAGE       = new ImageConst();

                /**
                 * USER_GROUP_IMAGE
                 */
                public static final Image USER_GROUP_IMAGE = new ImageConst();

                /**
                 * Default constructor
                 */
                private Images()
                {
                }
            }

            /**
             * 
             * Texts constants for Tree
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * LOAD_TREE_NODE
                 */
                public static final Text LOAD_TREE_NODE = new TextConst();

                /**
                 * TREE_IS_EMPTY
                 */
                public static final Text TREE_IS_EMPTY  = new TextConst();

                /**
                 * Default constructor
                 */
                private Texts()
                {
                }

            }
        }

        /**
         * 
         * i18n entries for knowledge
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */

        public static final class Knowledge
        {
            // Inner class for Tree

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                // inner classes of Frames.Knowledge.Actions

                /** ADD_WOB */
                public static final Action ADD_WOB                 = new ActionConst();

                /** SHOW_WOB */
                public static final Action SHOW_WOB                = new ActionConst();

                /** DELETE_WOB */
                public static final Action DELETE_WOB              = new ActionConst();

                /** DELETE_KNOW_LINK */
                public static final Action DELETE_KNOW_LINK        = new ActionConst();

                /** EDIT_WOB */
                public static final Action EDIT_WOB                = new ActionConst();

                /** SHOW_KNOW_LINK */
                public static final Action SHOW_KNOW_LINK          = new ActionConst();

                /**
                 * DELETE_KNOW_DOMAIN
                 */
                public static final Action DELETE_KNOWLEDGE_DOMAIN = new ActionConst();

                private Actions()
                {

                }
            }

            /**
             * 
             * Texts constants for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Texts
            {

                /**
                 * "Prozesswissen"
                 */
                public static final Text PROCESS_KNOW                               = new TextConst();

                /**
                 * "Fachwissen"
                 */
                public static final Text DOMAIN_KNOW                                = new TextConst();

                /**
                 * "Bezeichnung des Wissensobjekts"
                 */
                public static final Text DENOT_KNOW_DESC                            = new TextConst();

                /**
                 * "Bezeichnung des Wissenslinks"
                 */
                public static final Text KNOWLEDGE_LINK_NAME                        = new TextConst();

                /**
                 * "Beschreibung des Wissensobjekts"
                 */
                public static final Text DESC_KNOW_DESC                             = new TextConst();

                /**
                 * CREATE_KNOWLEDGE_LINK.
                 */
                public static final Text CREATE_KNOWLEDGE_LINK_INFO                 = new TextConst();

                /**
                 * CREATE_KNOWLEDGE_LINK_INFO_DIALOG_TITLE.
                 */
                public static final Text CREATE_KNOWLEDGE_LINK_INFO_DIALOG_TITLE    = new TextConst();

                /**
                 * DESC_KNOW_DESC
                 */
                public static final Text KNOWLEDGE_LINK_DESCRIPTION                 = new TextConst();

                /**
                 * KNOW_FOR_DOMAIN
                 */
                public static final Text KNOW_FOR_DOMAIN                            = new TextConst();

                /**
                 * KNOW_FOR_PROCESS
                 */
                public static final Text KNOW_FOR_PROCESS                           = new TextConst();

                /**
                 * KNOW_FOR_ACTIVITY
                 */
                public static final Text KNOW_FOR_ACTIVITY                          = new TextConst();

                /**
                 * LINK_OF_KNOWLEDGE
                 */
                public static final Text LINK_OF_KNOWLEDGE                          = new TextConst();

                /**
                 * AREA_OF_KNOWLEDGE
                 */
                public static final Text AREA_OF_KNOWLEDGE                          = new TextConst();

                /**
                 * EXIST_LINK_OF_KNOWLEDGE
                 */
                public static final Text EXIST_LINK_OF_KNOWLEDGE                    = new TextConst();

                /**
                 * SELECT_LINK_OF_KNOW
                 */
                public static final Text SELECT_LINK_OF_KNOW                        = new TextConst();

                /** NEW_KNOW_LINK */
                public static final Text NEW_KNOW_LINK                              = new TextConst();

                /** KNOW_DOMAIN */
                public static final Text KNOW_DOMAIN                                = new TextConst();

                /** KNOW_DOMAINS */
                public static final Text KNOW_DOMAINS                               = new TextConst();

                /** KNOW_LINK */
                public static final Text KNOW_LINK                                  = new TextConst();

                /** KNOW_OBJECT */
                public static final Text KNOW_OBJECT                                = new TextConst();

                /** KNOW_OBJECT */
                public static final Text KNOW_OBJECTS                               = new TextConst();

                /** APPROPRIATE_KNOWLEDGE */
                public static final Text APPROPRIATE_KNOWLEDGE                      = new TextConst();

                /** APPROPRIATE_USER */
                public static final Text APPROPRIATE_USER                           = new TextConst();

                /** ALL_PROCESS_ELEMENTS */
                public static final Text ALL_PROCESS_ELEMENTS                       = new TextConst();
                /** KNOWLEDGE_TYPE_KNOWLEDGE_ELEMENT */
                public static final Text KNOWLEDGE_TYPE_KNOWLEDGE_ELEMENT           = new TextConst();
                /** KNOWLEDGE_SOURCE_DMS */
                public static final Text KNOWLEDGE_SOURCE_DMS                       = new TextConst();
                /** KNOWLEDGE_SOURCE_WIKI */
                public static final Text KNOWLEDGE_SOURCE_WIKI                      = new TextConst();

                /**
                 * Text for Knowledge object
                 */
                public static final Text KNOWLEDGE_SOURCE_KNOWLEDGE_OBJECT          = new TextConst();

                /**
                 * Text for Knowledge link
                 */
                public static final Text KNOWLEDGE_SOURCE_KNOWLEDGE_LINK            = new TextConst();

                /**
                 * Text for Knowledge domain
                 */
                public static final Text KNOWLEDGE_SOURCE_KNOWLEDGE_DOMAIN          = new TextConst();

                /**
                 * Text for Knowledge search table column 'Typ'
                 */
                public static final Text KNOWLEDGE_SEARCH_TABLE_TYP                 = new TextConst();

                /**
                 * Text for Knowledge search table column 'Quelle'
                 */
                public static final Text KNOWLEDGE_SEARCH_TABLE_SOURCE              = new TextConst();

                /**
                 * Text for Knowledge search table column 'Titel'
                 */
                public static final Text KNOWLEDGE_SEARCH_TABLE_TITLE               = new TextConst();

                /**
                 * Text for Knowledge search selection
                 */
                public static final Text KNOWLEDGE_SEARCH_SELECTION                 = new TextConst();

                /**
                 * Warning text for too few chars for searching in Wiki
                 */
                public static final Text KNOWLEDGE_WIKI_TOO_FEW_CHAR                = new TextConst();

                /**
                 * Warning text for empty search area and text
                 */
                public static final Text KNOWLEDGE_SEARCH_AREA_AND_TEXT_EMPTY       = new TextConst();

                /**
                 * Warning text for empty search area
                 * 
                 */
                public static final Text KNOWLEDGE_SEARCH_AREA_EMPTY                = new TextConst();

                /**
                 * Warning text for empty search text
                 */
                public static final Text KNOWLEDGE_SEARCH_TEXT_EMPTY                = new TextConst();

                /** Error which is displayed when trying to save a knowledge object without any knowledge links */
                public static final Text KNOWLEDGE_OBJECT_MINIMUM_LINK_NUMBER_ERROR = new TextConst();

                private Texts()
                {
                }
            }

            /**
             * 
             * Image constants for tree class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Images
            {
                /** TREE_FOLDER */
                public static final Image KNOW_IMAGE    = new ImageConst();

                /** URL_IMAGE */
                public static final Image URL_IMAGE     = new ImageConst();

                /** WIKI_IMAGE */
                public static final Image WIKI_IMAGE    = new ImageConst();

                /** NETWORK_IMAGE */
                public static final Image NETWORK_IMAGE = new ImageConst();

                /** DMS_IMAGE */
                public static final Image DMS_IMAGE     = new ImageConst();

                /** DMS_IMAGE */
                public static final Image DOMAIN        = new ImageConst();

                /**
                 * Default constructor
                 */
                private Images()
                {
                }
            }
        }

        /**
         * 
         * This class included all resources used in Table
         * 
         * @author Maziar Khodaei
         * @version $Revision: 5083 $
         */
        public static final class Table
        {
            // inner classes of table

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 5083 $
             */
            public static final class Actions
            {
                // inner classes of Frames.table.Actions

                /** TABLE_SORT_UP */
                public static final Action TABLE_SORT_UP   = new ActionConst();

                /** TABLE_SORT_DOWN */
                public static final Action TABLE_SORT_DOWN = new ActionConst();

                /** RELOAD_TABLE */
                public static final Action RELOAD_TABLE    = new ActionConst();

                private Actions()
                {
                }
            }

        }
    }

    /**
     * This class included all resources used to i18n the entities / data objects.
     */
    /*
     * public static final class ValueObjects { public static final Text UNIQUE_KEY = new TextConst();
     * 
     * /** Entity document.
     */
    /*
     * public static final class Document { /** i18n attribute
     */
    /*
     * public static final Text VO_NAME_SINGULAR = new TextConst(); /** i18n attribute
     */
    /*
     * public static final Text VO_NAME_PLURAL = new TextConst(); /** i18n attribute
     */
    /*
     * public static final Attribute NAME = new AttributeConst(Document.class, "TODO"); /** i18n attribute
     */
    /*
     * public static final Attribute TYPE = new AttributeConst(Document.class, "TODO"); /** i18n attribute
     */
    /*
     * public static final Attribute SIZE = new AttributeConst(Document.class, "TODO"); /** i18n attribute
     */
    /*
     * public static final Attribute FORMAT = new AttributeConst(Document.class, "TODO"); /** i18n attribute
     */
    /*
     * public static final Attribute CONTENT = new AttributeConst(Document.class, "TODO"); } }
     */
}
