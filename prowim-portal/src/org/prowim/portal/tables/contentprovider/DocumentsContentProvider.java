/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 09.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 */

/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.06.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>./
 */
package org.prowim.portal.tables.contentprovider;

import java.util.List;

import org.eclipse.jface.viewers.IContentProvider;
import org.prowim.datamodel.dms.DocumentContentProperties;
import org.prowim.rap.framework.components.table.DefaultContentProvider;


/**
 * {@link IContentProvider} for activities. Simply passes on the list of activities.
 * 
 * @author
 * @version $Revision$
 * @since
 */
public class DocumentsContentProvider extends DefaultContentProvider
{

    /**
     * The {@link ActivityContentProvider} expects a list of activities.
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.DefaultContentProvider#getElements(java.lang.Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public Object[] getElements(Object inputElement)
    {
        List<DocumentContentProperties> list = (List<DocumentContentProperties>) inputElement;
        return list.toArray();
    }
}