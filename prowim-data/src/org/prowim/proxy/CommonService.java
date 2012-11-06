/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-01 14:29:24 +0100 (Di, 01 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/proxy/CommonService.java $
 * $LastChangedRevision: 2857 $
 *------------------------------------------------------------------------------
 * (c) 30.11.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.proxy;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.commons.CommonRemote;
import org.prowim.utils.WSDLConstants;

import de.ebcot.tools.logging.Logger;


/**
 * This class provides the proxy for the CommonRemote
 * 
 * @author Saad Wardi
 */
@WebServiceClient(name = ServiceConstants.PROWIM_COMMON_SERVICE_NAME, targetNamespace = ServiceConstants.PROWIM_COMMON_NAMESPACE)
public class CommonService extends Service
{

    private final static URL    PROWIMCOMMONSERVICE_WSDL_LOCATION;
    private final static Logger LOG = Logger.getLogger(ActivityService.class);

    static
    {
        URL url = null;
        try
        {

            URL baseUrl;
            baseUrl = org.prowim.proxy.CommonService.class.getResource(".");
            url = new URL(baseUrl, WSDLConstants.PROWIM_COMMON_WSDLLOCATION);
        }
        catch (MalformedURLException e)
        {
            LOG.error("Failed to create URL for the wsdl Location: " + "'" + WSDLConstants.PROWIM_COMMON_WSDLLOCATION + "', retrying as a local file");
        }
        PROWIMCOMMONSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Constructor.
     * 
     * @param wsdlLocation the wsdl url
     * @param serviceName the service name
     */
    public CommonService(URL wsdlLocation, QName serviceName)
    {
        super(wsdlLocation, serviceName);
    }

    /**
     * Default constructor.
     */
    public CommonService()
    {
        super(PROWIMCOMMONSERVICE_WSDL_LOCATION, new QName(ServiceConstants.PROWIM_COMMON_NAMESPACE, ServiceConstants.PROWIM_COMMON_SERVICE_NAME));
    }

    /**
     * Gets the service port.
     * 
     * @return returns {@link CommonRemote}
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_COMMON_SERVICE_PORT)
    public CommonRemote getProwimCommonServicePort()
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_COMMON_NAMESPACE, ServiceConstants.PROWIM_COMMON_SERVICE_PORT), CommonRemote.class);
    }

    /**
     * Gets service port with feature
     * 
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns {@link CommonRemote}
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_COMMON_SERVICE_PORT)
    public CommonRemote getProwimCommonServicePort(WebServiceFeature features)
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_COMMON_NAMESPACE, ServiceConstants.PROWIM_COMMON_SERVICE_PORT), CommonRemote.class,
                             features);
    }

}
