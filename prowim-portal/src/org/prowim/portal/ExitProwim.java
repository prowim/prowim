/*==============================================================================
 * File $Id: ExitProwim.java 5024 2010-11-30 14:47:06Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-11-30 15:47:06 +0100 (Di, 30 Nov 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/ExitProwim.java $
 * $LastChangedRevision: 5024 $
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

import org.eclipse.rap.ui.branding.IExitConfirmation;
import org.eclipse.rwt.RWT;
import org.prowim.portal.utils.ExitFlag;



/**
 * 
 * configure, what should happen, when user close windows(Browser). If showExitConfirmation return true, confirm a pop-up dialog
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5024 $
 */
public class ExitProwim implements IExitConfirmation
{
    /**
     * Constructor
     */
    public ExitProwim()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.rap.ui.branding.IExitConfirmation#getExitConfirmationText()
     */
    @Override
    public String getExitConfirmationText()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.rap.ui.branding.IExitConfirmation#showExitConfirmation()
     */
    @Override
    public boolean showExitConfirmation()
    {
        // Show the exit confirm for normal portal site. If only one process editor is shown don't show the exit confirm
        if (RWT.getRequest().getParameter("prowimid") == null)
            return !ExitFlag.getInstance().getLogOutFlag();
        else
            return false;
    }
}
