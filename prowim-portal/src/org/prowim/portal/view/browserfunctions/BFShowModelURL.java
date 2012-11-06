/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 26.11.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.util.List;

import org.eclipse.rwt.RWT;
import org.prowim.portal.dialogs.ShowURLDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.framework.components.impl.DefaultConstraint;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Shows a dialog with the URL to a process model. User can copy the URL and open the model externally.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.2
 */
public class BFShowModelURL extends AbstractModelEditorFunction
{
    private final String extension = "?prowimid=";

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-site name of the function.
     */
    public BFShowModelURL(ModelEditor modelEditor, String name)
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
        // gets the valid arguments and use it
        List<String> argument = ArgumentValidator.convert(arguments);

        StringBuffer buffer = RWT.getRequest().getRequestURL();
        // Create url
        String url = buffer.toString() + extension + argument.get(0);

        // Create dialog to show the url
        ShowURLDialog dialog = new ShowURLDialog(null, Resources.Frames.Dialog.Actions.SHOW_MODEL_URL.getAction(),
                                                 Resources.Frames.Dialog.Texts.SHOW_MODEL_URL_DESCRIPTION.getText(), url,
                                                 new DefaultConstraint(0, 100, false), false);
        dialog.setEditabel(false);
        dialog.open();

        return null;
    }
}
