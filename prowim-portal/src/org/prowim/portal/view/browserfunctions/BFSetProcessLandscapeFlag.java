/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 11.04.2011 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.browserfunctions;

import org.prowim.portal.MainController;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Set a {@link Process} as landscape or not. The fist argument is the ID of {@link Process}, the second the flag. <code>true</code> to set <br>
 * the {@link Process} as landscape and else <code>false</code>
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.2.0
 */
public class BFSetProcessLandscapeFlag extends AbstractModelEditorFunction
{

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-site name of the function.
     */
    public BFSetProcessLandscapeFlag(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.browserfunctions.AbstractModelEditorFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] arguments)
    {
        return MainController.getInstance().setProcessLandscapeFlag((String) arguments[0], (Boolean) arguments[1]);
    }
}
