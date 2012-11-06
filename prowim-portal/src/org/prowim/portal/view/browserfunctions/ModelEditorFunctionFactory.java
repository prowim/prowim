/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 09.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import java.lang.reflect.Constructor;

import org.apache.commons.lang.Validate;
import org.prowim.rap.modeleditor.ModelEditor;

import de.ebcot.tools.logging.Logger;


/**
 * Provides a factory-method to instantiate {@link AbstractModelEditorFunction}s registering them using the last part of the class-name ({@link Class#getSimpleName()})
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0a10
 */
public final class ModelEditorFunctionFactory
{
    private final static Logger LOG = Logger.getLogger(ModelEditorFunctionFactory.class);

    /**
     * Invisible do-nothing Constructor
     */
    private ModelEditorFunctionFactory()
    {
    }

    /**
     * 
     * a factory-method to instantiate {@link AbstractModelEditorFunction}s registering them using the last part of the class-name ({@link Class#getSimpleName()})
     * 
     * About BrowserFunctionRegistry.js:<br/>
     * When registering a function which is necessary to start the modelEditor (or is used soon after start) it is good practice to add the function's name to the array in BrowserFunctionRegistry.js, though it's not necessary in all cases.<br/>
     * 
     * 
     * 
     * @param <Function> a subclass of {@link AbstractModelEditorFunction}
     * 
     * @param modelEditorFunctionClass The class name of the {@link AbstractModelEditorFunction} to register. must be neither null nor be an inline-class
     * @param modelEditor the modelEditor-instance to register the function to
     * @return the instance of the function. never null
     * 
     */
    public static <Function extends AbstractModelEditorFunction>Function registerModelEditorFunction(Class<Function> modelEditorFunctionClass,
                                                                                                     ModelEditor modelEditor)
    {
        Validate.notNull(modelEditorFunctionClass);
        Validate.notNull(modelEditorFunctionClass.getCanonicalName(), "Generating name for inline class not supported");
        Validate.notNull(modelEditor);
        Validate.isTrue(modelEditorFunctionClass != AbstractModelEditorFunction.class, "Please use a subclass of "
                + AbstractModelEditorFunction.class.getName());

        String bfName = modelEditorFunctionClass.getSimpleName();
        try
        {
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Registering " + bfName + " on " + modelEditor);
            }

            Constructor<Function> constructor = modelEditorFunctionClass.getDeclaredConstructor(ModelEditor.class, String.class);
            return constructor.newInstance(modelEditor, bfName);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Cannot instantiate ", e);
        }

    }
}
