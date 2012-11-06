/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 04.05.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.utils;

import org.prowim.datamodel.dms.Document;
import org.prowim.datamodel.prowim.ProcessElement;
import org.prowim.portal.i18n.Resources;



/**
 * This class included all global function, which used in several other classes. <br>
 * The functions are all static.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.0
 */
public final class GlobalFunctions
{

    // Default constructor
    private GlobalFunctions()
    {
    }

    /**
     * Maps label from type
     * 
     * @param type type of {@link ProcessElement}s
     * 
     * @return non null
     */
    public static String getTypeLabel(String type)
    {
        if (type.equals(GlobalConstants.ACTIVITY))
            return Resources.Frames.Global.Texts.ACTIVITIES.getText();
        else if (type.equals(GlobalConstants.MEAN))
            return Resources.Frames.Global.Texts.MEANS.getText();
        else if (type.equals(GlobalConstants.ROLE))
            return Resources.Frames.Global.Texts.ROLES.getText();
        else if (type.equals(GlobalConstants.PROCESS_INFORMATION))
            return Resources.Frames.Global.Texts.PROCESS_INFORMATIONS.getText();
        else if (type.equals(GlobalConstants.PRODUCT_WAY))
            return Resources.Frames.Global.Texts.PRODUCT_WAYS.getText();
        else if (type.equals(GlobalConstants.CONTROL_FLOW))
            return Resources.Frames.Global.Texts.CONTROL_FLOWS.getText();
        else if (type.equals(GlobalConstants.RESULTS_MEMORY))
            return Resources.Frames.Global.Texts.RESULTS_MEMORY.getText();
        else if (type.equals(GlobalConstants.WORK))
            return Resources.Frames.Global.Texts.WORKS.getText();
        else if (type.equals(GlobalConstants.FUNCTION))
            return Resources.Frames.Global.Texts.FUNCTIONS.getText();
        else if (type.equals(GlobalConstants.PRODUCT))
            return Resources.Frames.Global.Texts.PRODUCTS.getText();
        else
            return "Not implemented";
        // throw new IllegalArgumentException("Unknown type: " + type);
    }

    /**
     * 
     * This part is deal with the suffixes in older file, which are stored in DMS. Check if a file has this suffix and cut it to show this.
     * 
     * @param document {@link Document}
     * @return {@link Document}
     */
    public static Document renameDocument(final Document document)
    {
        String name = document.getName();
        if (name.indexOf("__ts__") > 0)
        {
            document.setName(name.substring(0, name.indexOf("__ts__")));
        }

        return document;
    }

}
