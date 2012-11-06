/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 31.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.modeleditor;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpService;

import de.ebcot.tools.logging.Logger;


/**
 * {@link BundleActivator} for {@link ModelEditor}.
 * 
 * Registers resources under dynamic URL to avoid browser caching problems
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a9
 */
public class ModelEditorActivator implements BundleActivator
{

    /**
     * Dynamic Modeleditor URL
     */
    public static final String  MODELEDITOR_BASE_URL = "/modeleditor_" + System.currentTimeMillis();

    private static final Logger LOG                  = Logger.getLogger(ModelEditorActivator.class);

    /**
     * {@inheritDoc}
     * 
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception
    {
        ServiceReference ref = context.getServiceReference(HttpService.class.getName());
        if (ref != null)
        {
            HttpService httpService = (HttpService) context.getService(ref);
            httpService.registerResources(MODELEDITOR_BASE_URL, "/modeleditor", null);

            if (LOG.isDebugEnabled())
            {
                LOG.debug("Registered resources at URL: " + MODELEDITOR_BASE_URL);
            }
        }
        else
        {
            LOG.error("Cannot get service reference to HttpService");
            throw new IllegalStateException();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception
    {
        ServiceReference ref = context.getServiceReference(HttpService.class.getName());
        if (ref != null)
        {
            HttpService httpService = (HttpService) context.getService(ref);
            httpService.unregister(MODELEDITOR_BASE_URL);
        }
        else
        {
            LOG.error("Cannot get service reference to HttpService");
            throw new IllegalStateException();
        }

    }

}
