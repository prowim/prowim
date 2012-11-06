/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/collections/ParameterComparator.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 27.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.collections;

import java.util.Comparator;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.prowim.Parameter;



/**
 * Compares 2 {@link Parameter}.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 * @since 2.0
 */
public class ParameterComparator implements Comparator<Parameter>
{
    private boolean isAscending = true;

    /**
     * Create a performance sorter for the given sorting order ascending and pniOrder. Depending on parameter ascending and pniOrder the sorting order will be defined.
     * 
     * @param ascending if false, that means the highest value is the first, the lowest the last (depending on criticality). This has effect only to the criticality (first order) category. The values inside this category is order by paramter pniOrder.
     * 
     */
    public ParameterComparator(boolean ascending)
    {
        this.isAscending = ascending;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Parameter o1, Parameter o2)
    {
        Validate.notNull(o2);
        Validate.notNull(o1);
        if (o1.getOrder().compareTo(o2.getOrder()) != 0)
        {
            if (isAscending)
            {

                return o2.getOrder().compareTo(o1.getOrder());
            }
            else
            {
                return o1.getOrder().compareTo(o2.getOrder());
            }
        }
        else
        {
            if (isAscending)
            {
                return o2.getName().compareTo(o1.getName());
            }
            else
            {
                return o1.getName().compareTo(o2.getName());
            }
        }

    }

}
