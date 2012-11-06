/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 29.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.tables.labelprovider;

import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;


/**
 * Decorating label provider for the process version history table.
 * 
 * Highlights the active version.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.0alpha9
 */
public class ProcessVersionHistoryDecoratingLabelProvider extends DecoratingLabelProvider
{
    /** The {@link ITableLabelProvider} for the table */
    private final ITableLabelProvider provider;
    /** The {@link ILabelDecorator} for the table */
    private final ILabelDecorator     decorator;

    /**
     * Creates a new {@link ProcessVersionHistoryDecoratingLabelProvider}.
     * 
     * @param provider the {@link ILabelProvider label provider} to use, may not be null
     * @param decorator the decorator to use initially, can be null
     * @see DecoratingLabelProvider
     */
    public ProcessVersionHistoryDecoratingLabelProvider(ILabelProvider provider, ILabelDecorator decorator)
    {
        super(provider, decorator);
        this.provider = (ITableLabelProvider) provider;
        this.decorator = decorator;
    }

    /**
     * 
     * {@link org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)}
     * 
     * @param element the element to return the text for
     * @param columnIndex the column index of the element to get the text for
     * @return the column text
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex)
    {
        String text = provider.getColumnText(element, columnIndex);
        if (decorator != null)
        {
            String decorated = decorator.decorateText(text, element);
            if (decorated != null)
            {
                return decorated;
            }
        }
        return text;
    }
}
