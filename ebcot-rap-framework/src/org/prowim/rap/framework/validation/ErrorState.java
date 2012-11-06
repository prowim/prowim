/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 22.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.validation;

/**
 * Handled the validation for implemented component.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ErrorState
{
    /** No error exist */
    public static final byte NO_ERROR      = 0;

    /** Given value is smaller than allowed */
    public static final byte TOO_SMALL     = 1;

    /** Given value is greater than allowed */
    public static final byte TOO_HIGH      = 2;

    /** Required value is not given */
    public static final byte REQUIRED      = 3;

    /** Not defined status */
    public static final byte ILLEGAL_STATE = 99;

    private byte             errorState    = NO_ERROR;

    /**
     * 
     * Set error state
     * 
     * @param error byte. Can has above definition values
     */
    public void setErrorState(byte error)
    {
        this.errorState = error;
    }

    /**
     * 
     * Check or it exist a error.
     * 
     * @return If it exist a error return true, else return false.
     */
    public boolean hasError()
    {
        return (errorState > NO_ERROR);
    }

}
