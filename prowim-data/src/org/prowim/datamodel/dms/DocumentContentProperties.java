/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/dms/ContentProperties.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 11.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.dms;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.ContentProperties;



/**
 * This is a data-object-class represents properties to a document.
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentContentProperties", propOrder = { "id", "contentName", "description", "author", "createTime", "version", "contentType" })
public class DocumentContentProperties extends ContentProperties
{
    private String id;
    private String contentName;
    private String description;
    private String author;
    private String createTime;
    private String version;
    private String contentType;

    /**
     * Creates an instance.
     * 
     * @param id {@link DocumentContentProperties#setID(String)}
     * @param name {@link DocumentContentProperties#setContentName(String)}
     * @param title {@link DocumentContentProperties#setTitle(String)}
     * @param description {@link DocumentContentProperties#setDescription(String)}
     * @param author {@link DocumentContentProperties#setAuthor(String)}
     * @param createTime {@link DocumentContentProperties#setCreateTime(String)}
     * @param version {@link DocumentContentProperties#setVersion(String)}
     * @param contentType {@link DocumentContentProperties#setContentType(String)}
     */
    public DocumentContentProperties(String id, String name, String title, String description, String author, String createTime, String version,
            String contentType)
    {
        super();
        setID(id);
        setContentName(name);
        setTitle(title);
        setDescription(description);
        setAuthor(author);
        setCreateTime(createTime);
        setVersion(version);
        setContentType(contentType);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected DocumentContentProperties()
    {
    }

    /**
     * {@link DocumentContentProperties#setID(String)}
     * 
     * @return the id non null
     */
    public String getID()
    {
        return id;
    }

    /**
     * Sets the not null ID of the content.
     * 
     * @param id the id to set
     */
    public void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * {@link DocumentContentProperties#setContentName(String)}
     * 
     * @return the contentName non null
     */
    public String getContentName()
    {
        return contentName;
    }

    /**
     * Sets the name of the content. This is the unique filename and this one cannot be used twice.
     * 
     * @param contentName the not null contentName to set
     */
    public void setContentName(String contentName)
    {
        Validate.notNull(contentName);
        this.contentName = contentName;
    }

    /**
     * {@link DocumentContentProperties#setDescription(String)}
     * 
     * @return the description null possible
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the description. This is a longer description text for the document and is optional.
     * 
     * @param description text null if no description suitable
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * {@link DocumentContentProperties#setAuthor(String)}
     * 
     * @return the author, null possible.
     */
    public String getAuthor()
    {
        return author;
    }

    /**
     * Sets the author.
     * 
     * @param author the not null author to set
     */
    public void setAuthor(String author)
    {
        this.author = author;
    }

    /**
     * {@link DocumentContentProperties#setCreateTime(String)}
     * 
     * @return the createTime, not null.
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * Sets the create time.
     * 
     * @param createTime the not null createTime to set
     */
    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    /**
     * <code>setVersion(String version)</code>
     * 
     * @return the version, null possible.
     */
    public String getVersion()
    {
        return version;
    }

    /**
     * Sets the version label value. When a document will be updated, his version will be incremented.
     * 
     * @param version the version to set.
     */
    public void setVersion(String version)
    {
        this.version = version;
    }

    /**
     * REturns the content type of given document
     * 
     * @return The content type of document
     */
    public String getContentType()
    {
        return contentType;
    }

    /**
     * Sets the content type of document
     * 
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType)
    {
        this.contentType = contentType;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.contentName;
    }
}
