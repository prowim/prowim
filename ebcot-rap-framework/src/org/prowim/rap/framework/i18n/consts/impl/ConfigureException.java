/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-05-28 17:36:39 +0200 (Do, 28 Mai 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/consts/impl/ConfigureException.java $
 * $LastChangedRevision: 1648 $
 *------------------------------------------------------------------------------
 * (c) 14.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */
package org.prowim.rap.framework.i18n.consts.impl;

/**
 * Handle exceptions for resource classes
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1648 $
 */
public final class ConfigureException extends Exception
{

    /**
     * Description.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates a new ConfigureException object.
     */
    public ConfigureException()
    {
        super();
    }

    /**
     * 
     * Creates a new ConfigureException object
     * 
     * @param description String
     */
    public ConfigureException(String description)
    {
        super(description);
    }

    /**
     * Creates a new ConfigureException object.
     * 
     * @param cause Exception
     */
    public ConfigureException(Exception cause)
    {
        super(cause);
    }

    /**
     * Creates a new ConfigureException object.
     * 
     * @param description String
     * @param cause Exception
     */
    public ConfigureException(String description, Exception cause)
    {
        super(description, cause);
    }

}
