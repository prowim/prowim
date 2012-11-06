/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 19.08.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.List;

import org.eclipse.swt.browser.BrowserFunction;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Role;
import org.prowim.portal.MainController;
import org.prowim.portal.view.browserfunctions.tools.ArgumentValidator;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * Returns all {@link Role}s which connected to given {@link Activity}.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0a10
 */
public class BFGetActivtiyRoles extends AbstractModelEditorFunction
{

    /**
     * Description.
     * 
     * @param modelEditor reference browser, where the {@link BrowserFunction} is registered
     * @param name name of {@link BrowserFunction}.
     */
    BFGetActivtiyRoles(ModelEditor modelEditor, String name)
    {
        super(modelEditor, name);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * Description see {@link BFAddKnowledge}
     * 
     * @see org.eclipse.swt.browser.BrowserFunction#function(java.lang.Object[])
     */
    @Override
    public Object function(Object[] args)
    {
        // gets the valid arguments and use it
        Iterator<String> iterator = ArgumentValidator.convert(args).iterator();
        String elementID = iterator.next();

        return getRoleNames(elementID);
    }

    /**
     * 
     * Returns names of roles to given activity id
     * 
     * @param elementID . not null
     * @return Object[]. array of role names. Empty, if no role exists
     */
    public Object[] getRoleNames(String elementID)
    {
        List<Role> roleList = MainController.getInstance().getActivityRoles(elementID).getItem();

        Object[] array = new Object[roleList.size()];

        for (int i = 0; i < roleList.size(); i++)
        {
            array[i] = roleList.get(i).getName();
        }

        return array;
    }
}
