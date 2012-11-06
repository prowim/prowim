/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-22 20:42:21 +0200 (Do, 22 Jul 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/LoggingUtility.java $
 * $LastChangedRevision: 4415 $
 *------------------------------------------------------------------------------
 * (c) 14.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.*/
package org.prowim.utils;

import org.prowim.datamodel.algernon.AlgernonConstants;
import org.prowim.datamodel.algernon.RecordSet;

import de.ebcot.tools.logging.Logger;


/**
 * Logs the result of a rule execution.<br>
 * Used to avoid duplicate code of the method logResult.
 * 
 * @author Saad Wardi
 * @version $Revision: 4415 $
 */
public final class LoggingUtility
{
    private static Logger log = Logger.getLogger(LoggingUtility.class);

    private LoggingUtility()
    {

    }

    /**
     * shows the result.
     * 
     * @param rulename the rule name
     * @param source the source class.
     * @param result the result.
     */

    public static void logResult(String rulename, RecordSet result, Class<? > source)
    {
        log = Logger.getLogger(source);
        if (result != null && result.getResult().equals(AlgernonConstants.ERROR))
        {
            log.error(rulename + " failed:" + result.getResultString());
        }
        else if (result == null)
        {
            log.error(rulename + " failed:" + "result is null");
        }
        else
        {
            log.debug(rulename + ": " + result.getResultString());
        }

    }

}
