/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 19.01.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.impl;

import java.util.Comparator;

import org.eclipse.swt.widgets.TreeItem;


/**
 * Comparator to search a item in a tree. The input is a {@link TreeItem} and a id of a {@link DefaultLeaf} or a {@link DefaultLeaf} itself. <br>
 * You can expand the comparator for other objects. Please attention for this case, that the current cases for this comparator not changes. <br>
 * If the ID of both elements are the same than the comparator returns 0
 * 
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.3
 */
public class ItemComparator implements Comparator<Object>
{

    /**
     * Default constructor
     */
    public ItemComparator()
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Object arg0, Object arg1)
    {
        System.out.println("arg0: " + arg0.toString() + " arg0: " + arg0.toString());
        DefaultLeaf str1 = null;
        String str2 = null;

        if (arg0 instanceof TreeItem)
            str1 = (DefaultLeaf) ((TreeItem) arg0).getData();
        if (arg1 instanceof DefaultLeaf)
            str2 = ((DefaultLeaf) arg1).getID();
        if (arg1 instanceof String)
            str2 = (String) arg1;

        System.out.println("str1: " + str1 + " str2: " + str2);

        if (str1 != null && str2 != null)
            return str1.getID().compareTo(str2);
        else
            return -1;
    }

}
