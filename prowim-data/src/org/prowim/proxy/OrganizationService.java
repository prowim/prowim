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
import org.prowim.services.ejb.organization.OrganizationRemote;
import org.prowim.utils.WSDLConstants;

import de.ebcot.tools.logging.Logger;


/**
 * This class provides the proxy for the {@link OrganizationRemote}.
 * 
 */
@WebServiceClient(name = ServiceConstants.PROWIM_ORGANIZATION_SERVICE_NAME, targetNamespace = ServiceConstants.PROWIM_ORGANIZATION_NAMESPACE)
public class OrganizationService extends Service
{

    private final static URL    ORGANIZATIONSERVICE_WSDL_LOCATION;
    private final static Logger LOG = Logger.getLogger(org.prowim.proxy.OrganizationService.class.getName());

    static
    {
        URL url = null;
        try
        {

            URL baseUrl;
            baseUrl = org.prowim.proxy.ActivityService.class.getResource(".");
            url = new URL(baseUrl, WSDLConstants.PROWIM_ORGANIZATION_WSDLLOCATION);
        }
        catch (MalformedURLException e)
        {
            LOG.error("Failed to create URL for the wsdl Location: " + "'" + WSDLConstants.PROWIM_ORGANIZATION_WSDLLOCATION
                    + "', retrying as a local file");
        }
        ORGANIZATIONSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Constructs this with a WSDL localtion and service name.
     * 
     * @param wsdlLocation the not null wsdl location.
     * @param serviceName the not null service name.
     */
    public OrganizationService(URL wsdlLocation, QName serviceName)
    {
        super(wsdlLocation, serviceName);
    }

    /**
     * Constructs this.
     */
    public OrganizationService()
    {
        super(ORGANIZATIONSERVICE_WSDL_LOCATION, new QName("http://organization.ejb.services.prowim.org/", "OrganizationService"));
    }

    /**
     * Gets the remote interface.
     * 
     * @return returns OrganizationRemote
     */
    @WebEndpoint(name = "OrganizationServicePort")
    public OrganizationRemote getOrganizationServicePort()
    {
        return super.getPort(new QName("http://organization.ejb.services.prowim.org/", "OrganizationServicePort"), OrganizationRemote.class);
    }

    /**
     * Gets the service port.
     * 
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns OrganizationRemote
     */
    @WebEndpoint(name = "OrganizationServicePort")
    public OrganizationRemote getOrganizationServicePort(WebServiceFeature features)
    {
        return super
                .getPort(new QName("http://organization.ejb.services.prowim.org/", "OrganizationServicePort"), OrganizationRemote.class, features);
    }

}
