/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 02.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.prowim.datamodel.prowim.Person;
import org.prowim.portal.models.tree.model.KnowledgeLinkLeaf;
import org.prowim.portal.models.tree.model.KnowledgeObjectLeaf;
import org.prowim.portal.models.tree.model.ProcessLeaf;
import org.prowim.portal.models.tree.model.ProcessTypeLeaf;



/**
 * This class filter a tree of given element. Tree shows only those element, which are listed below.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 */
public class ActivityFilter extends ViewerFilter
{
    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    @Override
    public boolean select(Viewer viewer, Object parentElement, Object element)
    {
        Boolean firstFilter = element instanceof ProcessTypeLeaf || element instanceof KnowledgeObjectLeaf;
        Boolean secondFilter = element instanceof KnowledgeLinkLeaf || element instanceof Person || element instanceof ProcessLeaf;
        return (firstFilter || secondFilter);
    }

}
