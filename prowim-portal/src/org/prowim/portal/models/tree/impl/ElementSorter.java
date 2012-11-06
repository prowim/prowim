/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 06.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.viewers.ViewerSorter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.models.tree.model.ActivityLeaf;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.OrganizationLeaf;
import org.prowim.portal.models.tree.model.ProcessCategoryLeaf;
import org.prowim.portal.models.tree.model.ProcessEditorLeaf;
import org.prowim.portal.models.tree.model.ProcessElementsFolder;
import org.prowim.portal.models.tree.model.ProcessStructureLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeEditorLeaf;



/**
 * Set order to show elements in tree.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ElementSorter extends ViewerSorter
{
    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.ViewerComparator#category(java.lang.Object)
     */
    @Override
    public int category(Object element)
    {
        if (element instanceof Person)
            return 1;
        if (element instanceof OrganizationLeaf)
            return 2;
        if (element instanceof KnowledgeLinkLeaf)
            return 3;
        if (element instanceof KnowledgeObjectLeaf)
            return 4;
        if (element instanceof ActivityLeaf)
            return 5;
        if (element instanceof ProcessElementsFolder)
            return 6;
        if (element instanceof ProcessStructureLeaf)
            return 7;
        if (element instanceof ProcessEditorLeaf)
            return 8;
        if (element instanceof ProcessTypeEditorLeaf)
            return 9;
        if (element instanceof ProcessCategoryLeaf)
            return 9;

        return 99;
    }

}
