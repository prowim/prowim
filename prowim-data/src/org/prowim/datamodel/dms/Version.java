/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-04-06 10:44:49 +0200 (Di, 06 Apr 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/Version.java $
 * $LastChangedRevision: 3620 $
 *------------------------------------------------------------------------------
 * (c) 17.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.dms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class representing a version.
 * 
 * The version contains of the alfresco information. Additionally the process version information can be added.
 * 
 * @author Saad Wardi
 * @version $Revision: 3620 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Version", propOrder = { "createTime", "creator", "label", "userDefinedVersion", "instanceID", "isActiveVersion" })
public class Version
{
    private String  createTime;
    private String  creator;
    private String  label;
    private String  userDefinedVersion;
    private String  instanceID;
    private Boolean isActiveVersion;

    /**
     * Creates a Version and initialize its attributes.
     * 
     * @param createTime {@link Version#setCreateTime(String)}
     * @param creator {@link Version#setCreator(String)}
     * @param label {@link Version#setLabel(String)}
     */
    public Version(String createTime, String creator, String label)
    {
        setCreateTime(createTime);
        setCreator(creator);
        setLabel(label);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected Version()
    {

    }

    /**
     * {@link Version#setCreateTime(String)}
     * 
     * @return the createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * Sets the create time of the version.
     * 
     * @param createTime the createTime to set, not null.
     */
    public void setCreateTime(String createTime)
    {
        Validate.notNull(createTime);
        this.createTime = createTime;
    }

    /**
     * {@link Version#setCreator(String)}
     * 
     * @return the creator not null.
     */
    public String getCreator()
    {
        return creator;
    }

    /**
     * Sets the creator of the Version
     * 
     * @param creator the creator to set, not null
     */
    public void setCreator(String creator)
    {
        Validate.notNull(creator);
        this.creator = creator;
    }

    /**
     * {@link Version#setLabel(String)}
     * 
     * @return the label, never null
     */
    public String getLabel()
    {
        return label;
    }

    /**
     * Sets the label of the version
     * 
     * @param label the label to set, not null
     */
    public void setLabel(String label)
    {
        Validate.notNull(label);
        this.label = label;
    }

    /**
     * Sets the user defined version name, used by the process versioning.
     * 
     * @param processVersion the userDefinedVersion to set, may not be null
     */
    public void setUserDefinedVersion(String processVersion)
    {
        Validate.notNull(processVersion);
        this.userDefinedVersion = processVersion;
    }

    /**
     * Returns the user defined version as used by the process versioning.
     * 
     * @return the userDefinedVersion, may be null, if the version does not
     */
    public String getUserDefinedVersion()
    {
        return userDefinedVersion;
    }

    /**
     * <p>
     * Sets the instance ID (frame ID) of the instance in the ontology this version belongs to.
     * </p>
     * 
     * <p>
     * Is only needed for the process versioning right now.
     * </p>
     * 
     * @param instanceID the instanceID to set, can be null
     */
    public void setInstanceID(String instanceID)
    {
        this.instanceID = instanceID;
    }

    /**
     * <p>
     * Returns the instance ID (frame ID) of the instance in the ontology this version belongs to
     * </p>
     * 
     * <p>
     * Is only needed for the process versioning right now.
     * </p>
     * 
     * @return the instanceID, can be null
     */
    public String getInstanceID()
    {
        return instanceID;
    }

    /**
     * Sets this version as active version. Only applies to process templates right now.
     * 
     * @param isActiveVersion the isActiveVersion to set, can be null
     */
    public void setIsActiveVersion(Boolean isActiveVersion)
    {
        this.isActiveVersion = isActiveVersion;
    }

    /**
     * Returns true if this version is an active process template version, false otherwise. Can return null if version does not belong to a process template but a document.
     * 
     * @return the isActiveVersion true if this version is an active process template version, false otherwise, can be null if version does not belong to a process template
     */
    public Boolean getIsActiveVersion()
    {
        return isActiveVersion;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return label;
    }
}
