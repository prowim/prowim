/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-30 13:50:09 +0200 (Mi, 30 Jun 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/algernon/AlgernonConstants.java $
 * $LastChangedRevision: 4173 $
 *------------------------------------------------------------------------------
 * (c) 20.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

package org.prowim.datamodel.algernon;

/**
 * Constants used from RuleMachine.
 * 
 * @author Saad Wardi
 * @version $Revision: 4173 $
 */
public final class AlgernonConstants
{
    /**
     * Algernon ok status.
     */
    public static final String OK                              = "SUCCEEDED";
    /**
     * Algernon error status.
     */
    public static final String ERROR                           = "ERROR";
    /**
     * Algernon failed status.
     */
    public static final String FAILED                          = "FAILED";

    /** Algernon ask argument. */
    public static final String ASK                             = "ASK";

    /** Algernon tell argument. */
    public static final String TELL                            = "TELL";

    /**
     * status returned if the asked query dont have results.
     */
    public static final String PATH_FAILED                     = "Path failed";

    /**
     * status returned if the asked query have results.
     */
    public static final String PATH_SUCCEEDED                  = "Path succeeded";

    /** error message. */
    public static final String ERROR_MESSAGE                   = " Errors during ask/tell of '";

    /**
     * Description.
     */
    public static final String NEXT_QUERY_OPEN                 = "\n(";
    /**
     * Description.
     */
    public static final String NEXT_QUERY_CLOSE                = "\n)";

    /**
     * Description.
     */
    public static final String IDENTIFIER                      = "?ID";

    /** The label for the alfresco version slot. If a new version is created of a process template, this label is used (nor for the version but for the newly created process). */
    public static final String EDITABLE_ALFRESCO_VERSION_LABEL = "EDITABLE_ALFRESCO_VERSION";

    /** The label for the editable user version: "Aktuelle Version" */
    public static final String EDITABLE_USER_VERSION_LABEL     = "Aktuelle Version";

    private AlgernonConstants()
    {

    }

    /**
     * Slots in ontology. Has to be moved to centralized constants class!
     * 
     * @author Oliver Specht
     * @version $Revision$
     * @since 2.0.0alpha9
     */
    public static final class Slots
    {
        /** This slot stores the version name entered by the user when creating a new version of a process template */
        public final static String USER_DEFINED_VERSION = "benutzerdefinierte_Version";
        /** This slot stores the version in the alfresco DMS system */
        public final static String ALFRESCO_VERSION     = "Alfrescoversion";

        private Slots()
        {
        }
    }
}
