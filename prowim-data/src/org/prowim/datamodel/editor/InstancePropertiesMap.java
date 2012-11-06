/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-05-07 14:37:57 +0200 (Fr, 07 Mai 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/editor/InstancePropertiesMap.java $
 * $LastChangedRevision: 3801 $
 *------------------------------------------------------------------------------
 * (c) 26.11.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 *
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
 */
package org.prowim.datamodel.editor;

import org.apache.commons.lang.Validate;


/**
 * Map for storing the "name,value" properties of a protege instance.
 * 
 * @author Saad Wardi
 * @version $Revision: 3801 $
 */
public class InstancePropertiesMap
{
    private InstancePropertiesNames  names;
    private InstancePropertiesValues values;

    /**
     * Creates an Objects with his attributes.
     * 
     * @param names {@link InstancePropertiesMap#setInstancePropertiesNames(InstancePropertiesNames)}
     * @param values {@link InstancePropertiesMap#setInstancePropertiesValues(InstancePropertiesValues)}
     */
    public InstancePropertiesMap(InstancePropertiesNames names, InstancePropertiesValues values)
    {
        setInstancePropertiesNames(names);
        setInstancePropertiesValues(values);
    }

    /**
     * Sets the attribute names.
     * 
     * @param names not null {@link InstancePropertiesNames}.
     */
    private void setInstancePropertiesNames(InstancePropertiesNames names)
    {
        Validate.notNull(names);
        this.names = names;
    }

    /**
     * Gets the attribute names.
     * 
     * @return not null {@link InstancePropertiesNames}.
     */
    public InstancePropertiesNames getNames()
    {
        return names;
    }

    /**
     * Sets the attribute values.
     * 
     * @param values not null {@link InstancePropertiesValues}.
     */
    private void setInstancePropertiesValues(InstancePropertiesValues values)
    {
        Validate.notNull(values);
        this.values = values;
    }

    /**
     * Gets the attribute values.
     * 
     * @return not null {@link InstancePropertiesNames}.
     */
    public InstancePropertiesValues getValues()
    {
        return this.values;
    }

}
