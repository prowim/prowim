/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 25.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.browserfunctions.tools;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;


/**
 * Convert Object array to a sorted String list.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */
public final class ArgumentValidator
{
    private ArgumentValidator()
    {
    }

    /**
     * Converts to a valid string collections. It always returns at least one element in the returned {@link List}.
     * 
     * @param arguments the arguments to convert. array has to be bigger than size 0. Only non-empty strings are allowed.
     * 
     * @return Not null collection of strings, which returns not empty strings in given order.
     */
    public static List<String> convert(Object[] arguments)
    {
        // check for null parameters and if arguments array is null
        Validate.noNullElements(arguments);
        // check, if array has at least one parameter
        Validate.notEmpty(arguments);

        ArrayList<String> convertedArguments = new ArrayList<String>();

        for (Object argument : arguments)
        {
            String argumentString = (String) argument;
            if (argumentString.isEmpty())
                throw new IllegalArgumentException("Argument is empty String");

            convertedArguments.add(argumentString);
        }

        if (convertedArguments.isEmpty())
            throw new IllegalArgumentException("Empty arguments array found, size is 0");

        return convertedArguments;
    }
}
