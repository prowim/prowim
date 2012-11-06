/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-03-02 09:56:14 +0100 (Di, 02 Mrz 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/models/tree/provider/TreeLabelProvider.java $
 * $LastChangedRevision: 3398 $
 *------------------------------------------------------------------------------
 * (c) 29.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.models.tree.provider;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.models.tree.impl.DefaultLeaf;



/**
 * This provider get labels to a tree node
 * 
 * @author Maziar Khodaei
 * @version $Revision: 3398 $
 */
public class TreeLabelProvider extends CellLabelProvider
{

    @Override
    public String getToolTipText(final Object object)
    {
        DefaultLeaf leaf = (DefaultLeaf) object;
        return leaf.toString();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
     */
    @Override
    public void update(ViewerCell cell)
    {
        if (cell.getElement() instanceof DefaultLeaf)
        {
            DefaultLeaf object = (DefaultLeaf) cell.getElement();
            cell.setText(object.toString());
            cell.setImage((object).getImage());
        }
        else if (cell.getElement() instanceof Person)
        {
            cell.setText(cell.getElement().toString());
            cell.setImage(Resources.Frames.Tree.Images.USER_IMAGE.getImage());
        }
        else
        {
            cell.setImage(Resources.Frames.Tree.Images.TREE_FOLDER.getImage());
        }
    }

    @Override
    public int getToolTipTimeDisplayed(Object object)
    {
        return 15000;
    }

}
