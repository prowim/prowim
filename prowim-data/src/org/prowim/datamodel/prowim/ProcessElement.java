/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/ProcessElement.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 06.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.Validate;
import org.prowim.datamodel.collections.StringArray;
import org.prowim.utils.JSONConvertible;



/**
 * This is a data object as parent for all process element objects.<br>
 * A process element is an element of a {@link Process process}
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessElement", propOrder = { "id", "name", "createTime", "description", "className", "keyWords" })
public class ProcessElement implements JSONConvertible
{
    private String      id;
    private String      name;
    private String      createTime;
    private String      description;
    private String      className;
    private StringArray keyWords = new StringArray();

    /**
     * Creates a ProcessElement.
     * 
     * @param id {@link ProcessElement#setID(String)}
     * @param name {@link ProcessElement#setName(String)}
     * @param createTime {@link ProcessElement#setCreateTime(String)}
     */
    protected ProcessElement(String id, String name, String createTime)
    {
        setID(id);
        setName(name);
        setCreateTime(createTime);

    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected ProcessElement()
    {

    }

    /**
     * ProcessElementement#setID(String)}
     * 
     * @return the id . not null
     */
    public String getID()
    {
        return id;
    }

    /**
     * Sets the ID of the template to a ProcessElement instance.
     * 
     * @param id the not null ID to set
     */
    private void setID(String id)
    {
        Validate.notNull(id);
        this.id = id;
    }

    /**
     * {@link ProcessElement#setName(String)}
     * 
     * @return the name . not null
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the template to a ProcessElement instance.
     * 
     * @param name the not null name to set
     */
    public void setName(String name)
    {
        Validate.notNull(name);
        this.name = name;
    }

    /**
     * {@link ProcessElement#setCreateTime(String)}
     * 
     * @return the createTime
     */
    public String getCreateTime()
    {
        return createTime;
    }

    /**
     * Sets the name of the template to a ProcessElement instance.
     * 
     * @param createTime the not null createTime to set.
     */
    private void setCreateTime(String createTime)
    {
        Validate.notNull(createTime);
        this.createTime = createTime;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String}. cann be null if no description is specified.
     * 
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value allowed object is {@link String}. null is possible.
     * 
     */
    public void setDescription(String value)
    {
        this.description = value;
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
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.utils.JSONConvertible#toJSON()
     */
    public String toJSON()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("className:\"");
        builder.append(StringEscapeUtils.escapeJavaScript(this.getClassName()));
        builder.append("\",");
        builder.append("name:\"");
        builder.append(StringEscapeUtils.escapeJavaScript(this.getName()));
        builder.append("\",");
        builder.append("description:\"");
        builder.append(StringEscapeUtils.escapeJavaScript(this.getDescription()));
        builder.append("\",");
        builder.append("id:\"");
        builder.append(StringEscapeUtils.escapeJavaScript(this.getID()));
        builder.append("\"}");
        return builder.toString();
    }

    /**
     * Sets the class name of the concrete instance
     * 
     * @param className the className to set
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * Gets the class name of the concrete instance
     * 
     * @return the className, can be <code>NULL</code>
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * Sets a list of key words for element
     * 
     * @param keyWords the keyWords to set
     */
    public void setKeyWords(StringArray keyWords)
    {
        this.keyWords = keyWords;
    }

    /**
     * Get all key words of this element
     * 
     * @return the keyWords
     */
    public StringArray getKeyWords()
    {
        return keyWords;
    }

    /**
     * 
     * Add the given key word to the list of key words. It can not be null.
     * 
     * @param keyWord a key word for given element. Not null.
     */
    public void addKeyWord(String keyWord)
    {
        Validate.notNull(keyWord, "Key word can not be null.");
        this.keyWords.add(keyWord);
    }

}
