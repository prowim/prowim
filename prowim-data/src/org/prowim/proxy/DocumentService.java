/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-10-20 16:44:09 +0200 (Di, 20 Okt 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/proxy/DocumentService.java $
 * $LastChangedRevision: 2500 $
 *------------------------------------------------------------------------------
 * (c) 13.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.services.ejb.documents.DocumentRemote;
import org.prowim.utils.WSDLConstants;

import de.ebcot.tools.logging.Logger;


/**
 * Title and description.
 * 
 * @author Saad Wardi
 * @version $Revision: 2500 $
 */

@WebServiceClient(name = ServiceConstants.PROWIM_DOCUMENTS_SERVICE_NAME, targetNamespace = ServiceConstants.PROWIM_DOCUMENTS_NAMESPACE)
// , wsdlLocation = ServiceConstants.PROWIM_DOCUMENTS_WSDLLOCATION)
public class DocumentService extends Service
{
    private final static URL    PROWIMDOCUMENTSERVICE_WSDL_LOCATION;
    private final static Logger LOG = Logger.getLogger(ActivityService.class);

    static
    {
        URL url = null;
        try
        {

            URL baseUrl;
            baseUrl = org.prowim.proxy.ActivityService.class.getResource(".");
            url = new URL(baseUrl, WSDLConstants.PROWIM_DOCUMENTS_WSDLLOCATION);
        }
        catch (MalformedURLException e)
        {
            LOG.error("Failed to create URL for the wsdl Location: " + "'" + WSDLConstants.PROWIM_DOCUMENTS_WSDLLOCATION
                    + "', retrying as a local file");
        }
        PROWIMDOCUMENTSERVICE_WSDL_LOCATION = url;
    }

    /**
     * Constructor.
     * 
     * @param wsdlLocation the wsdl url
     * @param serviceName the service name
     */
    public DocumentService(URL wsdlLocation, QName serviceName)
    {
        super(wsdlLocation, serviceName);
    }

    /**
     * Default constructor.
     */
    public DocumentService()
    {
        super(PROWIMDOCUMENTSERVICE_WSDL_LOCATION, new QName(ServiceConstants.PROWIM_DOCUMENTS_NAMESPACE,
                                                             ServiceConstants.PROWIM_DOCUMENTS_SERVICE_NAME));
    }

    /**
     * Gets the service port.
     * 
     * @return returns {@link DocumentRemote}
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_DOCUMENTS_SERVICE_PORT)
    public DocumentRemote getProwimDocumentServicePort()
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_DOCUMENTS_NAMESPACE, ServiceConstants.PROWIM_DOCUMENTS_SERVICE_PORT),
                             DocumentRemote.class);
    }

    /**
     * Gets service port with feature
     * 
     * @param features A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code> parameter will have their default values.
     * @return returns {@link DocumentRemote}
     */
    @WebEndpoint(name = ServiceConstants.PROWIM_DOCUMENTS_SERVICE_PORT)
    public DocumentRemote getProwimDocumentServicePort(WebServiceFeature features)
    {
        return super.getPort(new QName(ServiceConstants.PROWIM_DOCUMENTS_NAMESPACE, ServiceConstants.PROWIM_DOCUMENTS_SERVICE_PORT),
                             DocumentRemote.class, features);
    }

}
