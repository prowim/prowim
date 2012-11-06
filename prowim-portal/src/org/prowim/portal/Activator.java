/*==============================================================================
 * File $Id: Activator.java 4632 2010-08-16 09:04:24Z specht $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-08-16 11:04:24 +0200 (Mo, 16 Aug 2010) $
 * $LastChangedBy: specht $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/Activator.java $
 * $LastChangedRevision: 4632 $
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
package org.prowim.portal;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.prowim.portal.utils.GlobalConstants;

import de.ebcot.tools.logging.Logger;


/**
 * 
 * 
 * This is the first class which activate, when the portal start
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4632 $
 */
public class Activator extends AbstractUIPlugin
{
    /**
     * ID of plug-in
     * 
     * @author Maziar Khodaei
     * @version $Revision: 4632 $
     * 
     */
    public static final String  PLUGIN_ID = GlobalConstants.PORTAL_BUNDLE_NAME;

    /** the logger. */
    private static final Logger LOG       = Logger.getLogger(Activator.class);

    /**
     * Start plug-in
     * 
     * @param context BundleContext
     * @throws Exception Exception
     */
    @Override
    public void start(final BundleContext context) throws Exception
    {
        LOG.debug("Starting ProWim RAP Application");

        super.start(context);
    }

}
