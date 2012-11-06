/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-01-20 16:35:16 +0100 (Mi, 20 Jan 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/i18n/Resources.java $
 * $LastChangedRevision: 3131 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
package org.prowim.rap.framework.i18n;

import org.prowim.rap.framework.i18n.consts.Action;
import org.prowim.rap.framework.i18n.consts.impl.ActionConst;


/**
 * Resources for internationalization (i18n)
 * 
 * @author Maziar Khodaei
 * @version $Revision: 3131 $
 */
public final class Resources
{

    /**
     * 
     * This class included all resources used in Frame
     * 
     * @author Maziar Khodaei
     * @version $Revision: 3131 $
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
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {
                // inner classes of Frames.Header.Texts

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
             * @version $Revision: 3131 $
             */
            public static final class Images
            {

                private Images()
                {
                }

            }

            /**
             * This class included all global actions.
             */
            public static final class Actions
            {
                /** ITEM_ADD */
                public static final Action ITEM_ADD    = new ActionConst();

                /** ITEM_DELETE */
                public static final Action ITEM_DELETE = new ActionConst();

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
         * @version $Revision: 3131 $
         */
        public static final class Header
        {
            // inner classes of Header

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {
                // inner classes of Frames.Header.Actions

                private Actions()
                {
                }
            }

            /**
             * 
             * Text constants in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {
                // inner classes of Frames.Header.Texts
                private Texts()
                {
                }

            }

            /**
             * 
             * Image constants in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Images
            {

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
         * @version $Revision: 3131 $
         */
        public static final class Navigation
        {
            // inner classes of Frames.Navigation

            /**
             * 
             * Resources to execute Actions in NavigationBar
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {
                // inner classes of Frame.NavigationBar.Actions
                private Actions()
                {
                }
            }

            /**
             * Conts for Images of navigation
             */
            public static final class Images
            {
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
         * @version $Revision: 3131 $
         */
        public static final class Toolbar
        {
            // inner classes of Frame

            /**
             * Conts for Images of toolbar
             */
            public static final class Images
            {

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

                private Actions()
                {
                }

            }
        }

        /**
         * 
         * Included all actions, texts an images, which belong to dialogs
         * 
         * @author Maziar Khodaei
         * @version $Revision: 3131 $
         */
        public static final class Dialog
        {
            /**
             * 
             * Actions for dialog area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {

                private Actions()
                {

                }
            }

            /**
             * 
             * Images for process area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Images
            {

                private Images()
                {

                }
            }

            /**
             * 
             * Text, which are comming in a dialog
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {

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
         * @version $Revision: 3131 $
         */
        public static final class Process
        {

            /**
             * 
             * Actions for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {
                private Actions()
                {

                }
            }

            /**
             * 
             * Texts constants for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {

                private Texts()
                {

                }
            }

            /**
             * 
             * Images for process area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Images
            {

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
         * @version $Revision: 3131 $
         */
        public static final class Activity
        {
            /**
             * 
             * Actions for Activity area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {

                private Actions()
                {
                }
            }

            /**
             * i18n entries for activity->texts
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {
                private Texts()
                {

                }
            }

            /**
             * 
             * Images for activity area
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Images
            {

                private Images()
                {

                }
            }
        }

        /**
         * 
         * i18n entries for for tree component
         * 
         * @author Maziar Khodaei
         * @version $Revision: 3131 $
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

                private Actions()
                {

                }
            }

            /**
             * 
             * Image constants for tree class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
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
             * Texts constants for Tree
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
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
         * i18n entries for knowledge
         * 
         * @author Maziar Khodaei
         * @version $Revision: 3131 $
         */

        public static final class Knowledge
        {
            // Inner class for Tree

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {
                // inner classes of Frames.Knowledge.Actions

                private Actions()
                {

                }
            }

            /**
             * 
             * Texts constants for process
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Texts
            {

                private Texts()
                {
                }
            }

            /**
             * 
             * Image constants for tree class
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
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
        }

        /**
         * 
         * This class included all resources used in Table
         * 
         * @author Maziar Khodaei
         * @version $Revision: 3131 $
         */
        public static final class Table
        {
            // inner classes of table

            /**
             * 
             * Actions in header view
             * 
             * @author Maziar Khodaei
             * @version $Revision: 3131 $
             */
            public static final class Actions
            {
                // inner classes of Frames.table.Actions

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
