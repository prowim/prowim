/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 16.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */
package org.prowim.portal.swt.widgets;

import java.lang.ref.SoftReference;

import org.apache.commons.lang.Validate;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.swt.widgets.Display;
import org.prowim.portal.view.DefaultView;



/**
 * An extension of {@link Display}
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0.a9
 */
public final class UpdateDialogSynchronizer
{
    private SoftReference<DefaultView> updateDialogViewRef = null;

    /**
     * Use {@link #getInstance()}
     */
    private UpdateDialogSynchronizer()
    {
    }

    /**
     * get the right to show the UpdateDialog
     * 
     * @param view the view to assign the right to. must not be null
     * 
     * @return true, if the right was granted.
     */
    public synchronized boolean getShowUpdateDialogRights(final DefaultView view)
    {
        Validate.notNull(view);

        if (this.updateDialogViewRef == null || this.updateDialogViewRef.get() == null)
        {
            this.updateDialogViewRef = new SoftReference<DefaultView>(view);
            return true;
        }
        return false;
    }

    /**
     * 
     * return the right to show the update dialog
     * 
     * @param view the view the right was granted to
     */
    public synchronized void returnShowUpdateDialogRights(final DefaultView view)
    {
        Validate.notNull(view);

        if (updateDialogViewRef != null && view == updateDialogViewRef.get())
        {
            updateDialogViewRef.clear();
        }
        else
        {
            throw new IllegalArgumentException("given view is not the view granted the rights to display the update");
        }

    }

    /**
     * 
     * Description.
     * 
     * @return UpdateDialogSynchronizer
     */
    public static UpdateDialogSynchronizer getInstance()
    {
        return (UpdateDialogSynchronizer) SessionSingletonBase.getInstance(UpdateDialogSynchronizer.class);
    }

}
