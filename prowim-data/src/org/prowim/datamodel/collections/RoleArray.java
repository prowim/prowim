/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2010-04-26 12:55:13 +0200 (Mo, 26 Apr 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/collections/RoleArray.java $
 * $LastChangedRevision: 3705 $
 *------------------------------------------------------------------------------
 * (c) 17.06.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.Validate;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.Role;



/**
 * a Class for the {@link Role} list that will be transmited from server to the client.
 * 
 * @author Saad Wardi
 * @version $Revision: 3705 $
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoleArray", propOrder = { "item" })
public class RoleArray implements Collection<Role>
{

    /**
     * Activities array.
     */
    @XmlElement(nillable = false)
    private List<Role> item;

    /**
     * Constructs an empty list with the initial capacity.
     */
    public RoleArray()
    {
        item = new ArrayList<Role>();
    }

    /**
     * 
     * Description.
     * 
     * @param items gzgh
     */
    void setItems(List<Role> items)
    {
        this.item = items;
    }

    /**
     * Gets the value of the item property. {@link Activity}
     * 
     * @return a not null list.
     * 
     */
    public List<Role> getItem()
    {
        if (item == null)
        {
            item = new ArrayList<Role>();
        }
        return this.item;
    }

    /**
     * Sets the role list.
     * 
     * @param item not null role list.
     */
    public void setItem(List<Role> item)
    {
        Validate.notNull(item);
        if (item != null)
        {
            this.item = item;
        }
        else
        {
            this.item = new ArrayList<Role>();
        }
    }

    /**
     * Description.
     * 
     * @param roles activities array.
     */
    public void addArray(Role[] roles)
    {

        this.item = Arrays.asList(roles);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#add(java.lang.Object)
     */
    @Override
    public boolean add(Role e)
    {
        return item.add(e);
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.util.Collection#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends Role> c)
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
        boolean result = false;
        final Iterator<Role> itemsIt = item.iterator();
        while (itemsIt.hasNext())
        {
            Role role = itemsIt.next();
            if (role.getID().equals(((Role) o).getID()))
            {
                result = true;
                break;
            }
        }
        return result;
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
    public Iterator<Role> iterator()
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