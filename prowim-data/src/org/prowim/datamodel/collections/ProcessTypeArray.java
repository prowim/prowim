/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 07.01.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.prowim.datamodel.prowim.ProcessType;



/**
 * Array class for process type. Is a collection of process types.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ProcessTypeArray", propOrder = { "item" })
public class ProcessTypeArray implements Collection<ProcessType>
{
    /**
     * Activities array.
     */
    @XmlElement(nillable = false)
    private List<ProcessType> item;

    /**
     * Description.
     */
    public ProcessTypeArray()
    {
        item = new ArrayList<ProcessType>();
    }

    /**
     * Get item
     * 
     * @return the item
     */
    public List<ProcessType> getItem()
    {
        return item;
    }

    /**
     * Sets the activity list.
     * 
     * @param item not null activity list.
     */
    public void setItem(List<ProcessType> item)
    {
        Validate.notNull(item);
        if (item != null)
        {
            this.item = item;
        }
        else
        {
            this.item = new ArrayList<ProcessType>();
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#add(java.lang.Object)
     */
    @Override
    public boolean add(ProcessType e)
    {
        return item.add(e);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends ProcessType> c)
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
    public Iterator<ProcessType> iterator()
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
