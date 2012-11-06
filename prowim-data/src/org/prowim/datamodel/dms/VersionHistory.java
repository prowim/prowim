/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-09-18 13:56:07 +0200 (Fr, 18 Sep 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/VersionHistory.java $
 * $LastChangedRevision: 2415 $
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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents VersionHistory.
 * 
 * @author Saad Wardi
 * @version $Revision: 2415 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VersionHistory", propOrder = { "uuid", "versions" })
public class VersionHistory
{

    private String        uuid;
    @XmlElement(nillable = false)
    private List<Version> versions = new ArrayList<Version>();

    /**
     * Creates an instance of this.
     * 
     * @param uuid {@link VersionHistory#setUuid(String)}, may not be null
     * @param versions {@link VersionHistory#setVersions(List)}, may not be null
     */
    public VersionHistory(String uuid, List<Version> versions)
    {
        setUuid(uuid);
        setVersions(versions);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected VersionHistory()
    {

    }

    /**
     * {@link VersionHistory#setUuid(String)}
     * 
     * @return the uuid
     */
    public String getUuid()
    {
        return uuid;
    }

    /**
     * Sets the uuid of the document to be handled.
     * 
     * @param uuid the uuid to set
     */
    public void setUuid(String uuid)
    {
        Validate.notNull(uuid);
        this.uuid = uuid;
    }

    /**
     * {@link VersionHistory#setVersions(List)}
     * 
     * @return the versions, never null
     */
    public List<Version> getVersions()
    {
        return versions;
    }

    /**
     * Returns the correct version for the given version label.
     * 
     * @param versionLabel the label to search for
     * @return the {@link Version} or null, if nothing was found
     */
    public Version getVersion(String versionLabel)
    {
        for (Version version : versions)
        {
            if (version.getLabel().equals(versionLabel))
            {
                return version;
            }
        }

        return null;
    }

    /**
     * Sets the list of the versions objects. Each {@link Version} object describes a version for the document with id = uuid stored on the DMS.
     * 
     * @param versions the versions to set, not null.
     */
    public void setVersions(List<Version> versions)
    {
        Validate.notNull(versions);
        this.versions = versions;
    }

    /**
     * Adds the given version to the list of versions.
     * 
     * @param version the {@link Version} to add, may not be null
     */
    public void add(Version version)
    {
        Validate.notNull(version);
        this.versions.add(version);
    }
}
