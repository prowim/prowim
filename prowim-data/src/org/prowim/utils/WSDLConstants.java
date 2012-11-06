/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-01 09:16:47 +0200 (Do, 01 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/ServerProperties.java $
 * $LastChangedRevision: 4188 $
 *------------------------------------------------------------------------------
 * (c) 03.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.utils;

import org.prowim.resources.ResourcesLocator;
import org.prowim.services.ejb.ServiceConstants;



/**
 * Properties and constants for the proxy.
 * 
 * @author Saad Wardi
 * 
 * @version $Revision: 4188 $
 */
public final class WSDLConstants
{
    /**
     * The wsdl location.
     */
    public static final String PROWIM_ACTIVITY_WSDLLOCATION     = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.ACTIVITY_BEAN + "?wsdl";

    /* ********************* ProwimKnowledgeService************************************************** */

    /**
     * The wsdl location.
     */
    public static final String PROWIM_KNOWLEDGE_WSDLLOCATION    = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.KNOWLEDGE_BEAN + "?wsdl";

    /* ********************* DocumentsService************************************************** */

    /**
     * The wsdl location.
     */
    public static final String PROWIM_DOCUMENTS_WSDLLOCATION    = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.DOCUMENTS_BEAN + "?wsdl";

    /* *************************************** OntologyWSAccessService********************** */

    /**
     * The wsdl location.
     */
    public static final String ONTOLOGYWSACCESS_WSDLLOCATION    = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.ONTOLOGYWSACCESS_BEAN + "?wsdl";

    /* *********************** ProwimSystemInfoService **************************************** */

    /**
     * The wsdl location.
     */
    public static final String PROWIM_SYSTEMINFO_WSDLLOCATION   = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_SYSTEMINFO_BEAN + "?wsdl";

    /* ********************************ProcessService**************************************************** */

    /**
     * The wsdl location.
     */
    public static final String PROWIM_PROCESS_WSDLLOCATION      = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_PROCESS_BEAN + "?wsdl";

    /* *********************************************************************************************** */

    /* ********************************WorkflowService**************************************************** */

    /**
     * The wsdl location.
     */
    public static final String PROWIM_WORKFLOW_WSDLLOCATION     = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_WORKFLOW_BEAN + "?wsdl";

                                                                                                                                     /* *********************************************************************************************** */

                                                                                                                                     /* ********************************EditorService**************************************************** */
                                                                                                                                     ;
    /**
     * The wsdl location.
     */
    public static final String PROWIM_EDITOR_WSDLLOCATION       = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_EDITOR_BEAN + "?wsdl"

                                                                /* *********************************************************************************************** */
                                                                /* ********************************EditorService**************************************************** */
                                                                ;
    /**
     * The wsdl location.
     */
    public static final String PROWIM_COMMON_WSDLLOCATION       = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_COMMON_BEAN + "?wsdl"

                                                                /* *********************************************************************************************** */
                                                                /* ********************************EditorService**************************************************** */
                                                                ;
    /**
     * The wsdl location.
     */
    public static final String PROWIM_ORGANIZATION_WSDLLOCATION = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_ORGANIZATION_BEAN + "?wsdl";

                                                                                                                                     /* *********************************************************************************************** */
                                                                                                                                     /* ********************************AdminService**************************************************** */
                                                                                                                                     ;
    /**
     * The wsdl location.
     */
    public static final String PROWIM_ADMIN_WSDLLOCATION        = "http://" + ResourcesLocator.getServerHost() + ":"
                                                                        + ResourcesLocator.getServerPort() + "/" + ServiceConstants.CONTEXT_ROOT
                                                                        + "/" + ServiceConstants.PROWIM_ADMIN_BEAN + "?wsdl";

    /* *********************************************************************************************** */

    private WSDLConstants()
    {

    }

}
