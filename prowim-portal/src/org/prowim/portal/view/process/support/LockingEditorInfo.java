/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 20.09.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.process.support;

import org.apache.commons.lang.Validate;
import org.eclipse.swt.widgets.Display;


/**
 * Locking helper object for Locking the ModelEditor.
 * 
 * @see HijackModelEditorView HijackModelEditorView for more info
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0RC1
 */
public class LockingEditorInfo
{
    private final HijackModelEditorView view;
    private final Display               display;

    /**
     * Create a new locking object to lock the given view
     * 
     * @param view not null
     */
    public LockingEditorInfo(HijackModelEditorView view)
    {
        super();

        Validate.notNull(view);
        Validate.notNull(Display.getCurrent());

        this.view = view;
        this.display = Display.getCurrent();
    }

    /**
     * get the hijacking view
     * 
     * @return the view not null
     */
    public HijackModelEditorView getView()
    {
        return view;
    }

    /**
     * 
     * get the display
     * 
     * @return the display not null
     */
    public Display getDisplay()
    {
        return display;
    }
}
