/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-21 15:52:14 +0200 (Di, 21 Sep 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/utils/StringUtility.java $
 * $LastChangedRevision: 4804 $
 *------------------------------------------------------------------------------
 * (c) 12.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;


/**
 * Contains utility methods to search in String objects.
 * 
 * @author Saad Wardi
 * @version $Revision: 4804 $
 */
public class StringUtility
{

    /**
     * {@link String#indexOf(int)}.
     * 
     * @param source the source {@link String}
     * @param pattern the pattern.
     * @return {@link String#indexOf(int)}.
     */
    public boolean contains(String source, String pattern)
    {
        return source.indexOf(pattern) >= 0;
    }

    /**
     * {@link String#indexOf(int)}.
     * 
     * @param source the source {@link String}
     * @param pattern the pattern.
     * @return {@link String#indexOf(int) }.
     */
    public boolean containsl(String source, String pattern)
    {
        Validate.notNull(source);
        Validate.notNull(pattern);
        String sourceTmp = source.toLowerCase();
        String patternTmp = pattern.toLowerCase();
        return sourceTmp.indexOf(patternTmp) >= 0;
    }

    /**
     * Compares 2 String.
     * 
     * @param source the source String.
     * @param target the target String.
     * @return true if the String are equal.
     */
    public boolean equals(String source, String target)
    {
        return source.equals(target);
    }

}
