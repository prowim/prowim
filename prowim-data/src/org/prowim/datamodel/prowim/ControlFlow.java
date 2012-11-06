/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-12-08 17:06:48 +0100 (Di, 08 Dez 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/ControlFlow.java $
 * $LastChangedRevision: 2915 $
 *------------------------------------------------------------------------------
 * (c) 21.07.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.prowim;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * this is a data-object-class represents controlflow.
 * 
 * @author Saad Wardi
 * @version $Revision: 2915 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ControlFlow", propOrder = { "activityID" })
public class ControlFlow extends ProcessElement
{
    private String activityID;

    /**
     * Creates a WorkFlow with an id, name, create time and activity.
     * 
     * @param id this{@link #setID(String)}
     * @param name this{@link #setName(String)}
     * @param createTime this{@link #setCreateTime(String)}
     */
    protected ControlFlow(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected ControlFlow()
    {
    }

    /**
     * this{@link #setActivityID(String)}
     * 
     * @return the activityID
     */
    public String getActivityID()
    {
        return activityID;
    }

    /**
     * Sets the activity id. the id of the activity activated from the controlflow.
     * 
     * @param activityID the activityID to set, null is possible.
     */
    public void setActivityID(String activityID)
    {
        this.activityID = activityID;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getName();
    }

}
