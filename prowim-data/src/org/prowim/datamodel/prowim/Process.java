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
 * This is a data-object-class representing a process.<br>
 * 
 * @author Saad Wardi
 * @version $Revision: 2088 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "process", propOrder = { "instanceID", "templateID", "name", "type", "description", "projectName", "starter", "state", "startTime",
        "endTime", "activities", "userDefinedVersion", "alfrescoVersion", "isActiveVersion", "createTime", "owners", "processType",
        "isProcessLandscape" })
public class Process implements Comparable<Object>
{

    /** The instance id. */
    private String         instanceID;
    /** the template id id. */
    private String         templateID;
    /** the title. */
    private String         name;
    /** the type. */
    private String         type;
    /** the description. */
    private String         description;
    /** the project name. */
    private String         projectName;
    /** the starter. */
    private Person         starter;
    /** the state. */
    private String         state;
    /** the start timestamp. */
    private String         startTime;
    /** the end timestamp. */
    private String         endTime;
    /** The version name as entered by the user, only templates have this member */
    private String         userDefinedVersion;
    /** The version in the alfresco DMS system, only templates have this member */
    private String         alfrescoVersion;
    /** Flag indicating if the process is an active template version */
    private Boolean        isActiveVersion;
    /** Time of creation */
    private String         createTime;
    /** process type/category of process */
    private ProcessType    processType;

    /** Flag is true if the process is defined as landscape */
    private boolean        isProcessLandscape;

    /** the list of activities. */
    @XmlElement(nillable = false)
    private List<Activity> activities;

    /** the list of owners. */
    @XmlElement(nillable = false)
    private List<Person>   owners;

    /**
     * Creates a process with an id, templateID and name.
     * 
     * @param instanceID {@link Process#setInstanceID(String)}
     * @param templateID {@link Process#setTemplateID(String)}
     * @param name {@link Process#setName(String)}
     */
    protected Process(String templateID, String instanceID, String name)
    {
        setTemplateID(templateID);
        setInstanceID(instanceID);
        setName(name);
    }

    /**
     * Creates a process with a templateID and name.
     * 
     * @param templateID {@link Process#setInstanceID(String)}
     * @param name {@link Process#setName(String)}
     */
    protected Process(String templateID, String name)
    {
        setTemplateID(templateID);
        setName(name);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Process()
    {

    }

    /**
     * Gets the value of the activities property.
     * 
     * Objects of the following type(s) are allowed in the list {@link Activity}
     * 
     * @return activities
     * 
     * 
     */
    public List<Activity> getActivities()
    {
        if (activities == null)
        {
            activities = new ArrayList<Activity>();
        }
        return this.activities;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value allowed object is {@link String}
     * 
     */
    public void setDescription(String value)
    {
        this.description = value;
    }

    /**
     * Gets the value of the id property. The process id is the primary key.
     * 
     * @return possible object is {@link String}
     * 
     */
    public String getInstanceID()
    {
        return instanceID;
    }

    /**
     * Sets the value of the instance id property.
     * 
     * @param instanceID allowed object is {@link String}
     * 
     */
    public void setInstanceID(String instanceID)
    {
        this.instanceID = instanceID;
    }

    /**
     * Gets the value of the projectName property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getProjectName()
    {
        return projectName;
    }

    /**
     * Sets the value of the projectName property.
     * 
     * @param value allowed object is {@link String}
     * 
     */
    public void setProjectName(String value)
    {
        this.projectName = value;
    }

    /**
     * Gets the value of the starter property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public Person getStarter()
    {
        return starter;
    }

    /**
     * Sets the value of the starter property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setStarter(Person value)
    {
        this.starter = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getState()
    {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value allowed object is {@link String }
     * 
     */
    public void setState(String value)
    {
        this.state = value;
    }

    /**
     * Gets the value of the template property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTemplateID()
    {
        return this.templateID;
    }

    /**
     * Sets the value of the template property.
     * 
     * @param templateID allowed object is {@link String }
     * 
     */
    public void setTemplateID(String templateID)
    {
        Validate.notNull(templateID);
        this.templateID = templateID;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param name allowed object is {@link String }
     * 
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return possible object is {@link String}
     */
    public String getType()
    {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value allowed object is {@link String}
     */
    public void setType(String value)
    {
        this.type = value;
    }

    /**
     * Gets the start timestamp.
     * 
     * @return the startTime
     */
    public String getStartTime()
    {
        return startTime;
    }

    /**
     * Sets the start timestamp.
     * 
     * @param startTs the startTime to set, null allowed
     */
    public void setStartTime(String startTs)
    {
        this.startTime = startTs;
    }

    /**
     * Sets the acivities property
     * 
     * @param activities the activities to set
     */
    public void setActivities(List<Activity> activities)
    {
        this.activities = activities;
    }

    /**
     * Gets the end time.
     * 
     * @return the endTime
     */
    public String getEndTime()
    {
        return endTime;
    }

    /**
     * Sets the end time.
     * 
     * @param endTs the endTime to set
     */
    public void setEndTime(String endTs)
    {
        this.endTime = endTs;
    }

    /**
     * Returns the user defined version of this process.
     * 
     * Only available if this is a template!
     * 
     * @return the userDefinedVersion, may be null
     */
    public String getUserDefinedVersion()
    {
        return userDefinedVersion;
    }

    /**
     * Sets the user defined version of this process.
     * 
     * Only available if this is a template!
     * 
     * @param userDefinedVersion the userDefinedVersion to set, can be null
     */
    public void setUserDefinedVersion(String userDefinedVersion)
    {
        this.userDefinedVersion = userDefinedVersion;
    }

    /**
     * Returns the corresponding version in the alfresco system.
     * 
     * Only available if this is a template!
     * 
     * @return the alfrescoVersion, can be null
     */
    public String getAlfrescoVersion()
    {
        return alfrescoVersion;
    }

    /**
     * Sets the alfresco version of this process.
     * 
     * Only available if this is a template!
     * 
     * @param alfrescoVersion the alfrescoVersion to set, can be null
     */
    public void setAlfrescoVersion(String alfrescoVersion)
    {
        this.alfrescoVersion = alfrescoVersion;
    }

    /**
     * <p>
     * Returns <code>true</code> if the process is the active version of a template, false otherwise.
     * </p>
     * 
     * <p>
     * If null is returned, this process is not a template.
     * </p>
     * 
     * @return true, false or null if the process is not an active template
     */
    public Boolean getIsActiveVersion()
    {
        return isActiveVersion;
    }

    /**
     * Sets this process to the active version of a process template history.
     * 
     * @param isActiveVersion if <code>true</code> this process is set as active process template version, can be null
     */
    public void setIsActiveVersion(Boolean isActiveVersion)
    {
        this.isActiveVersion = isActiveVersion;
    }

    /**
     * Set create time.
     * 
     * @param createTime the createTime to set
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    /**
     * Get create time
     * 
     * @return the createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * Set the list of {@link Person}s, who can change this {@link Process} or has created it.
     * 
     * @param owners the owners to set
     */
    public void setOwners(List<Person> owners)
    {
        this.owners = owners;
    }

    /**
     * Returns a list of {@link Person}s, which creates the {@link Process} or has the right to change it.
     * 
     * @return the owners
     */
    public List<Person> getOwners()
    {
        if (this.owners == null)
        {
            this.owners = new ArrayList<Person>();
        }
        return this.owners;
    }

    /**
     * Sets the {@link ProcessType} of {@link Process}
     * 
     * @param processType the processType to set
     */
    public void setProcessType(ProcessType processType)
    {
        this.processType = processType;
    }

    /**
     * Returns the {@link ProcessType} of {@link Process}
     * 
     * @return the processType
     */
    public ProcessType getProcessType()
    {
        return processType;
    }

    /**
     * <code>true</code> if the {@link Process} is a process landscape, else <code>false</code>
     * 
     * @param isProcessLandscape the isProcessLandscape to set
     */
    public void setProcessLandscape(boolean isProcessLandscape)
    {
        this.isProcessLandscape = isProcessLandscape;
    }

    /**
     * <code>true</code> if the {@link Process} is a process landscape, else <code>false</code>
     * 
     * @return the isProcessLandscape
     */
    public boolean isProcessLandscape()
    {
        return isProcessLandscape;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {

        if (o != null)
        {
            Process other = (Process) o;
            if (other != null && other.name != null)
            {
                int returnValue = name.compareTo(other.name);
                return returnValue;
            }
            else
            {
                return -1;
            }
        }
        else
        {
            return -1;
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.name;
    }

}
