/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 27.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
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
package org.prowim.rap.framework.components.table;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;


/**
 * This is only a default label provider for {@link DefTableViewer} and set the labels for a <br>
 * table with two columns, shows the name of given object in model. You can set also a tool tip or <br>
 * a image for each cells. Please don´t personalize this class for your table. Create your own provider class <br>
 * if you want show more informations in your table and use setLabelProvider of table, to set your own provider.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 * @deprecated Use {@link DefaultTableLabelProvider} or implement a derived class of that.
 */
@Deprecated
public class DefLabelProvider extends CellLabelProvider
{

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
     */
    @Override
    public void update(ViewerCell cell)
    {
        Object[] element = (Object[]) cell.getElement();
        int columnIndex = cell.getColumnIndex();

        switch (columnIndex)
        {
            case 0:
                cell.setText(String.valueOf(element[0]));
                break;
            case 1:
                cell.setText(String.valueOf(element[1]));
                break;
            case 2:
                cell.setText(String.valueOf(element[2]));
                break;
            case 3:
                cell.setText(String.valueOf(element[3]));
                break;
            case 4:
                cell.setText(String.valueOf(element[4]));
                break;
            case 5:
                cell.setText(String.valueOf(element[5]));
                break;
            default:
                break;
        }
    }
}
