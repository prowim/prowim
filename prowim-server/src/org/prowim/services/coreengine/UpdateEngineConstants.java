/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-17 13:15:58 +0200 (Do, 17 Jun 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/coreengine/UpdateEngineConstants.java $
 * $LastChangedRevision: 3984 $
 *------------------------------------------------------------------------------
 * (c) 22.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 * Saad WArdi.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 3984 $
 * @since !!VERSION!!
 */
public final class UpdateEngineConstants
{
    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 3984 $
     * @since !!VERSION!!
     */
    public final class Rules
    {

        /** Gets all versions. */
        public static final String GET_ALL_VERSIONS            = "getAllVersions";
        /** Gets the update frames. */
        public static final String GET_UPDATE_FRAMES           = "getUpdateFrames";
        /** Gets the update frame properties. */
        public static final String GET_UPDATE_FRAME_PROPERTIES = "getUpdateFrameProperties";
        /** Checks if a frame is a class. */
        public static final String IS_CLASS                    = "isClass";
        /** Checks if a frame is a slot. */
        public static final String IS_SLOT                     = "isSlot";
        /** Checks if a frame is an instance. */
        public static final String IS_INSTANCE                 = "isInstance";
        /** Gets the version properties. */
        public static final String GET_VERSION_PROPERTIES      = "getVersionProperties";
        /** Cleans the ontology. */
        public static final String CLEAN                       = "clean";
        /** Gets the frames, that have be deleted. */
        public static final String GET_DELETE_FRAMES           = "getDeleteFrames";
        /** Sets the version as invalid. */
        public static final String SET_VERSION_INVALID         = "setVersionInvalid";
        /** Sets the version as valid. */
        public static final String SET_VERSION_VALID           = "setVersionValid";
        /** Checks if the version is valid. */
        public static final String IS_VERSION_VALID            = "isVersionValid";

        private Rules()
        {
        }
    }

    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 3984 $
     * @since !!VERSION!!
     */
    public final class Variables
    {
        /** The version ID. */
        public static final String ID          = "?ID";
        /** The version creator. */
        public static final String CREATOR     = "?creator";
        /** The version label. */
        public static final String LABEL       = "?label";
        /** The version create time. */
        public static final String CREATE_TIME = "?createTime";
        /** The changes count. */
        public static final String COUNT       = "?count";
        /** desription. */
        public static final String NAME        = "?name";
        /** version ID. */
        public static final String VERSION_ID  = "?versionID";
        /** frame ID. */
        public static final String FRAME_ID    = "?frameID";
        /** clean rule. */
        public static final String CLEAN_RULE  = "((:SUBCLASS ModelRules ?x)(RuleBody ?x ?body)(:BIND ?R (:JAVA (org.prowim.utils.StringUtility.contains ?body \"\\\"\")))(:NEQ ?R false))";
        /** rule body. */
        public static final String RULE_BODY   = "?body";

        private Variables()
        {
        }

    }

    private UpdateEngineConstants()
    {

    }
}
