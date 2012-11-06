/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */

package org.prowim.proxy;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.activity.ActivityRemote;
import org.prowim.utils.WSDLConstants;

import de.ebcot.tools.logging.Logger;


/**
 * This class provides the proxy for the ProwimActivityService.
 * 
 * @author Saad Wardi
 * @version $Revision: 3749 $
 */
@WebServiceClient(name = ServiceConstants.PROWIM_ACTIVITY_SERVICE_NAME, targetNamespace = ServiceConstants.PROWIM_ACTIVITY_NAMESPACE)
public class ActivityService extends Service
{

    private final static URL    PROWIMACTIVITYSERVICE_WSDL_LOCATION;
    private final static Logger LOG = Logger.getLogger(ActivityService.class);

    static
    {
        URL url = null;
        try
        {

            URL baseUrl;
            baseUrl = org.prowim.proxy.ActivityService.class.getResource(".");
            url = new URL(baseUrl, WSDLConstants.PROWIM_ACTIVITY_WSDLLOCATION);
        }
        catch (MalformedURLException e)
        {
            LOG.error("Failed to create URL for the wsdl Location: " + "'" + WSDLConstants.PROWIM_ACTIVITY_WSDLLOCATION
                    + "', retrying as a local file");
        }
        PROWIMACTIVITYSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Constructor.
     * 
     * @param wsdlLocation the wsdl url
     * @param serviceName the service name
     */
    public ActivityService(URL wsdlLocation, QName serviceName)
    {
        super(wsdlLocation, serviceName);
    }

    /**
     * Default constructor.
     */
    public ActivityService()
    {
        super(PROWIMACTIVITYSERVICE_WSDL_LOCATION, new QName(ServiceConstants.PROWIM_ACTIVITY_NAMESPACE,
                                                             ServiceConstants.PROWIM_ACTIVITY_SERVICE_NAME));
    }

    /**
     * Gets the service port.
     * 
     * @return returns ProwimActivityRemote
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_ACTIVITY_SERVICE_PORT)
    public ActivityRemote getProwimActivityServicePort()
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_ACTIVITY_NAMESPACE, ServiceConstants.PROWIM_ACTIVITY_SERVICE_PORT),
                             ActivityRemote.class);
    }

    /**
     * Gets service port with feature
     * 
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns ProwimActivityRemote
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_ACTIVITY_SERVICE_PORT)
    public ActivityRemote getProwimActivityServicePort(WebServiceFeature features)
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_ACTIVITY_NAMESPACE, ServiceConstants.PROWIM_ACTIVITY_SERVICE_PORT),
                             ActivityRemote.class, features);
    }

}
