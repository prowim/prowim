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

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.prowim.datamodel.dms.Version;
import org.prowim.rap.framework.components.table.DefaultTableLabelProvider;


/**
 * Label provider for the process versions.
 * 
 * @author Oliver Specht
 * @version $Revision$
 * @since 2.0.0.alpha9
 */
public class ProcessVersionHistoryLabelProvider extends DefaultTableLabelProvider implements ITableColorProvider
{
    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.components.table.DefaultTableLabelProvider#getColumnText(java.lang.Object, int)
     */
    @Override
    public String getColumnText(Object element, int columnIndex)
    {
        Version version = (Version) element;
        switch (columnIndex)
        {
            case 0:
                String prefix = "";
                if (version.getIsActiveVersion() != null && version.getIsActiveVersion())
                {
                    prefix = "* ";
                }

                if (version.getUserDefinedVersion() == null)
                {
                    return prefix + "Unbekannt";
                }
                return prefix + version.getUserDefinedVersion();
            case 1:
                return version.getCreator();
            case 2:
                return version.getCreateTime();
            default:
                return "";
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getBackground(java.lang.Object, int)
     */
    @Override
    public Color getBackground(Object element, int columnIndex)
    {
        Version version = (Version) element;
        if (version.getIsActiveVersion() != null && version.getIsActiveVersion())
        {
            return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.viewers.ITableColorProvider#getForeground(java.lang.Object, int)
     */
    @Override
    public Color getForeground(Object element, int columnIndex)
    {
        Version version = (Version) element;
        if (version.getIsActiveVersion() != null && version.getIsActiveVersion())
        {
            return Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
        }
        return null;
    }
}
