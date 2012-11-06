/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-09-18 13:56:07 +0200 (Fr, 18 Sep 2009) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/KnowledgeLink.java $
 * $LastChangedRevision: 2415 $
 *------------------------------------------------------------------------------
 * (c) 07.08.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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

import org.apache.commons.lang.Validate;


/**
 * This is a data-object-class represents KnowledgeLink.
 * 
 * @author Saad Wardi
 * @version $Revision: 2415 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KnowledgeLink", propOrder = { "hyperlink", "repository", "knowledgeObject" })
public class KnowledgeLink extends ProcessElement
{
    private String hyperlink = "";
    private String repository;
    private String knowledgeObject;

    /**
     * Creates a KnowledgeLink.
     * 
     * @param id {@link KnowledgeLink#setID(String)}
     * @param name {@link KnowledgeLink#setName(String)}
     * @param createTime {@link KnowledgeLink#setCreateTime(String)}
     */
    protected KnowledgeLink(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected KnowledgeLink()
    {
        super();
    }

    /**
     * Returns the hyperlink (URL) of this knowledge link.
     * 
     * @return the hyperlink, never null, can be ""
     */
    public String getHyperlink()
    {
        return hyperlink;
    }

    /**
     * Sets the hyperlink
     * 
     * @param hyperlink the hyperlink to set
     */
    public void setHyperlink(String hyperlink)
    {
        this.hyperlink = hyperlink;
    }

    /**
     * {@link KnowledgeLink#setRepository(String)}
     * 
     * @return the repository ID.
     */
    public String getRepository()
    {
        return repository;
    }

    /**
     * Sets the repository. The repository attribute is the ID in the ontology of the the data storage location (Wissensspeicher), example (Internet or shared folder)<br/>
     * Example: repository name = Internet. repository ID = Wissensspeicher_20093513474728 See {@link KnowledgeRepository}
     * 
     * @param repository not null the repository ID to set
     */
    public void setRepository(String repository)
    {
        Validate.notNull(repository);
        this.repository = repository;
    }

    /**
     * Set the {@link KnowledgeObject} where this {@link KnowledgeLink} belong to.
     * 
     * @param knowledgeObject the knowledgeObject to set
     */
    public void setKnwoledgeObject(String knowledgeObject)
    {
        this.knowledgeObject = knowledgeObject;
    }

    /**
     * Get the {@link KnowledgeObject} where this {@link KnowledgeLink} belong to.
     * 
     * @return the knowledgeObject
     */
    public String getKnowledgeObject()
    {
        return knowledgeObject;
    }

}
