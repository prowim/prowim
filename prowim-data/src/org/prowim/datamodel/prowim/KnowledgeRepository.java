/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-14 14:26:31 +0200 (Mo, 14 Jun 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/prowim/KnowledgeRepository.java $
 * $LastChangedRevision: 3908 $
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
 * This is a data-object-class represents Wissensspeicher.
 * 
 * @author Saad Wardi
 * @version $Revision: 3908 $
 */
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "KnowledgeRepository", propOrder = { "storage" })
public class KnowledgeRepository extends ProcessElement
{
    private String storage;

    /**
     * Creates a KnowledgeRepository .
     * 
     * @param id {@link KnowledgeRepository#setID(String)}
     * @param name {@link KnowledgeRepository#setName(String)}
     * @param createTime {@link KnowledgeRepository#setCreateTime(String)}
     */
    protected KnowledgeRepository(String id, String name, String createTime)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    protected KnowledgeRepository()
    {

    }

    /**
     * {@link KnowledgeRepository#setStorage(String)}
     * 
     * @return the storage is the
     */
    public String getStorage()
    {
        return storage;
    }

    /**
     * Sets the storage of the {@link KnowledgeRepository}
     * 
     * @param storage the storage to set
     */
    public void setStorage(String storage)
    {
        Validate.notNull(storage);
        this.storage = storage;
    }

}
