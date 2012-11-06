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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents activity.
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Activity")
@XmlType(namespace = "org.prowim.datamodel.prowim", name = "Activity", propOrder = { "automatic", "status", "userID", "processName",
        "processID", "startTime", "finished", "activated", "hasRelations" })
public class Activity extends ProcessElement implements Comparable<Object>
{

    /** auto activity. */
    private boolean automatic;
    /** status. */
    private String  status;
    /** the userID property. */
    private String  userID;
    /** the process name property. */
    private String  processName;
    /** the process id. */
    private String  processID;
    /** the start time property. */
    private String  startTime;
    /** the finished property. */
    private boolean finished;
    /** the activated property. */
    private boolean activated;
    /** has one or more relations to roles, knowledgeobjects or means */
    private boolean hasRelations;

    /**
     * The default constructor.
     * 
     * @param id {@link Activity#setID(String)}.
     * @param createTime {@link Activity#setCreateTime(String)}.
     * @param name {@link Activity#setName(String)}.
     */
    protected Activity(String id, String createTime, String name)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Activity()
    {
    }

    /**
     * Gets the activated flag.
     * 
     * @return the activated
     */
    public boolean isActivated()
    {
        return activated;
    }

    /**
     * Sets the activated flag.
     * 
     * @param activated the activated to set
     */
    public void setActivated(boolean activated)
    {
        this.activated = activated;
    }

    /**
     * Gets the automatic flag.
     * 
     * @return the automatic
     */
    public boolean isAutomatic()
    {
        return automatic;
    }

    /**
     * Sets the autonatic flag.
     * 
     * @param automatic the automatic to set
     */
    public void setAutomatic(boolean automatic)
    {
        this.automatic = automatic;
    }

    /**
     * Gets the utility of the activity.
     * 
     * @return the utility
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * Sets the utility of the activity.
     * 
     * @param status the utility to set
     */
    public void setStatus(String status)
    {
        Validate.notNull(status);
        this.status = status;
    }

    /**
     * Gets the value of the userID property.
     * 
     * @return possible object is {@link String } or null.
     * 
     */
    public String getUserID()
    {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value allowed object is {@link String }. not null.
     * 
     */
    public void setUserID(String value)
    {
        this.userID = value;
    }

    /**
     * Gets the value of the processName property.
     * 
     * @return possible object is {@link String }. not null
     * 
     */
    public String getProcessName()
    {
        return processName;
    }

    /**
     * Sets the value of the processName property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setProcessName(String value)
    {
        this.processName = value;
    }

    /**
     * {@link Activity#setProcessID(String)}.
     * 
     * @return the processID . not null
     */
    public String getProcessID()
    {
        return processID;
    }

    /**
     * Sets the id of the process where the activity was defined.
     * 
     * @param processID not null id of a process.
     */
    public void setProcessID(String processID)
    {
        Validate.notNull(processID);
        this.processID = processID;
    }

    /**
     * Gets the value of the startTime property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getStartTime()
    {
        return startTime;
    }

    /**
     * Sets the value of the startTime property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setStartTime(String value)
    {
        this.startTime = value;
    }

    /**
     * Gets the value of the finished property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public boolean getFinished()
    {
        return finished;
    }

    /**
     * Sets the value of the finished property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setFinished(boolean value)
    {
        this.finished = value;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
        int returnValue = -1;
        if (o != null)
        {
            Activity other = (Activity) o;
            returnValue = this.getID().compareTo(other.getID());
        }
        return returnValue;
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

    /**
     * Sets a flag to indicate if this activity has one or more relations to roles, knowledgeobjects or means.
     * 
     * @param hasRelations the flag to set
     */
    public void setHasRelations(boolean hasRelations)
    {
        this.hasRelations = hasRelations;

    }

    /**
     * Returns a flag to indicate if this activity has relations
     * 
     * @see #setHasRelations(boolean)
     * @return <code>TRUE</code> if relations available, otherwise <code>FALSE</code>
     */
    public boolean hasRelations()
    {
        return hasRelations;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.datamodel.prowim.ProcessElement#toJSON()
     */
    @Override
    public String toJSON()
    {
        StringBuilder builder = new StringBuilder();
        String superJSON = StringUtils.removeEnd(super.toJSON(), "}");
        builder.append(superJSON);

        builder.append(",");
        builder.append("hasRelations:\"");
        builder.append(hasRelations);
        builder.append("\"}");

        return builder.toString();
    }

}
