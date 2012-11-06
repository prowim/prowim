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

package org.prowim.datamodel.prowim;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents Knowledge.
 * 
 * @author Saad Wardi
 * @version $Revision: 2088 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "knowledge", propOrder = { "knowledgeObjectsList", "process" })
public class Knowledge
{
    /** the knowledge objects list. */
    @XmlElement(nillable = true)
    private List<KnowledgeObject> knowledgeObjectsList;
    /** the process. */
    private Process               process;

    /**
     * Creates this.
     */
    protected Knowledge()
    {
    }

    /**
     * Gets the value of the knowledgeObjectsList property.
     * 
     * Objects of the following type(s) are allowed in the list {@link KnowledgeObject } if no item exists , an empty liat will be returned.
     * 
     * @return the knowledge objects.
     */
    public List<KnowledgeObject> getKnowledgeObjectsList()
    {
        if (knowledgeObjectsList == null)
        {
            knowledgeObjectsList = new ArrayList<KnowledgeObject>();
        }
        return this.knowledgeObjectsList;
    }

    /**
     * Sets the list.
     * 
     * @param knowledgeObjectsList the knowledge objects list.
     */
    public void setKnowledgeObjectsList(List<KnowledgeObject> knowledgeObjectsList)
    {
        Validate.notNull(knowledgeObjectsList);
        this.knowledgeObjectsList = knowledgeObjectsList;
    }

    /**
     * Gets the value of the process property.
     * 
     * @return possible object is {@link Process }
     * 
     */
    public Process getProcess()
    {
        return process;
    }

    /**
     * Sets the value of the process property.
     * 
     * @param value allowed object is {@link Process }
     * 
     */
    public void setProcess(Process value)
    {
        Validate.notNull(value);
        this.process = value;
    }

}
