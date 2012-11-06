/*==============================================================================
 * File $Id: ProwimWorkbenchWindowAdvisor.java 4955 2010-10-21 10:26:33Z wiesner $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-10-21 12:26:33 +0200 (Do, 21 Okt 2010) $
 * $LastChangedBy: wiesner $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/ProwimWorkbenchWindowAdvisor.java $
 * $LastChangedRevision: 4955 $
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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.prowim.rap.framework.resource.ColorManager;



/**
 * 
 * Prepare the workbench for creating views.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4955 $
 */
public class ProwimWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor
{

    /**
     * Constructor
     * 
     * @param configurer IActionBarConfigurer
     */
    public ProwimWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer)
    {
        super(configurer);
    }

    /**
     * {@inheritDoc}
     * 
     */
    @Override
    public void preWindowOpen()
    {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowMenuBar(false);
        configurer.setShellStyle(SWT.NO_TRIM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postWindowOpen()
    {
        final IWorkbenchWindow window = getWindowConfigurer().getWindow();
        Shell shell = window.getShell();
        shell.setMaximized(true);
        shell.setBackground(ColorManager.BACKGROUND_COLOR);
    }

}