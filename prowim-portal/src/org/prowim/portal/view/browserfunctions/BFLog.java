/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 04.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view.browserfunctions;

import java.util.Iterator;

import org.prowim.portal.utils.GlobalConstants;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.logging.Logger;


/**
 * To write logs from JS in server log file. To call this method you have to give tree parameters: <br>
 * 
 * 1. element id: is the id element of element for which you want write this log. <br>
 * 2. logFlag: to decide, which log you want to write: <li>error</li>, <li>warn</li> or <li>info</li>. This are case sensitive <br>
 * 3. headerTitle: The headerTitle to write in log file.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0.a9
 */
public class BFLog extends AbstractModelEditorFunction
{
    private final static Logger LOG         = Logger.getLogger(BFLog.class);
    private final ModelEditor   modelEditor = null;

    /**
     * Constructor. Shall only be used by {@link ModelEditorFunctionFactory}
     * 
     * @param modelEditor The ModelEditor to register this function to
     * @param name the JS-side name of the function.
     */
    BFLog(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] arguments)
    {
        // gets the valid arguments and use it
        Iterator<String> iterator = ArgumentValidator.convert(arguments).iterator();
        String elementID = iterator.next();
        String logFlag = iterator.next();
        String message = iterator.next();

        if (logFlag.equals(GlobalConstants.ERROR))
            LOG.error(message + " Element : " + elementID + " ModelID: " + this.modelEditor.getModelId());
        else if (logFlag.equals(GlobalConstants.WARN))
            LOG.warn(message + " Element : " + elementID + " ModelID: " + this.modelEditor.getModelId());
        else if (logFlag.equals(GlobalConstants.INFO))
            LOG.info(message + " Element : " + elementID + " ModelID: " + this.modelEditor.getModelId());

        return null;
    }
}
