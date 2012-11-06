/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 *
 */

package org.prowim.rap.modeleditor.resources;

import org.eclipse.rwt.resources.IResource;
import org.eclipse.rwt.resources.IResourceManager.RegisterOptions;
import org.prowim.rap.modeleditor.ModelEditorActivator;



/**
 * 
 * A Resource to deliver prototype.js
 * 
 * @author Philipp Leusmann
 * @version $Revision$
 * @since 2.0.0.a9
 */
public class PrototypeResource implements IResource
{

    @Override
    public String getCharset()
    {
        return "utf-8";
    }

    @Override
    public ClassLoader getLoader()
    {
        return this.getClass().getClassLoader();
    }

    @Override
    public String getLocation()
    {
        return ModelEditorActivator.MODELEDITOR_BASE_URL + "/js/prototype-1.6.1.js";

    }

    @Override
    public RegisterOptions getOptions()
    {
        return RegisterOptions.NONE;
    }

    @Override
    public boolean isExternal()
    {
        return true;
    }

    @Override
    public boolean isJSLibrary()
    {
        return true;
    }

}
