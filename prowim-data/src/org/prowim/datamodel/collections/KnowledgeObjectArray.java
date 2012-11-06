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

package org.prowim.datamodel.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.prowim.KnowledgeObject;



/**
 * a Class for the {@link KnowledgeObject} list that will be transmited from server to the client.
 * 
 * @author Saad Wardi
 * @version $Revision: 2188 $
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "knowledgeObjectArray", propOrder = { "item" })
public class KnowledgeObjectArray implements Collection<KnowledgeObject>
{

    /** the item list. */
    @XmlElement(nillable = true)
    private List<KnowledgeObject> item;

    /**
     * Constructs an empty list with an initial capacity of ten.
     */
    public KnowledgeObjectArray()
    {
        item = new ArrayList<KnowledgeObject>();
    }

    /**
     * Gets the value of the item property.
     * 
     * {@link KnowledgeObject }
     * 
     * @return a not null list of {@link KnowledgeObject}.
     * 
     */
    public List<KnowledgeObject> getItem()
    {
        if (item == null)
        {
            item = new ArrayList<KnowledgeObject>();
        }
        return this.item;
    }

    /**
     * Sets the KnowledgeObject list.
     * 
     * @param item not null activity list.
     */
    public void setItem(List<KnowledgeObject> item)
    {
        Validate.notNull(item);
        if (item != null)
        {
            this.item = item;
        }
        else
        {
            this.item = new ArrayList<KnowledgeObject>();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#add(java.lang.Object)
     */
    @Override
    public boolean add(KnowledgeObject e)
    {
        return item.add(e);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends KnowledgeObject> c)
    {
        return item.addAll(c);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#clear()
     */
    @Override
    public void clear()
    {
        item.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#contains(java.lang.Object)
     */
    @Override
    public boolean contains(Object o)
    {
        return item.contains(o);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#containsAll(java.util.Collection)
     */
    @Override
    public boolean containsAll(Collection<? > c)
    {
        return item.containsAll(c);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#isEmpty()
     */
    @Override
    public boolean isEmpty()
    {
        return item.isEmpty();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#iterator()
     */
    @Override
    public Iterator<KnowledgeObject> iterator()
    {
        return item.iterator();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object o)
    {
        return item.remove(o);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#removeAll(java.util.Collection)
     */
    @Override
    public boolean removeAll(Collection<? > c)
    {
        return item.removeAll(c);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#retainAll(java.util.Collection)
     */
    @Override
    public boolean retainAll(Collection<? > c)
    {
        return item.retainAll(c);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#size()
     */
    @Override
    public int size()
    {
        return item.size();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#toArray()
     */
    @Override
    public Object[] toArray()
    {
        return item.toArray();
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#toArray(T[])
     */
    @Override
    public <T>T[] toArray(T[] a)
    {
        return item.toArray(a);
    }

}
