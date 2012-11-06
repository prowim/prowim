/*==============================================================================
 * File $Id: SamplePrincipal.java 3513 2010-03-19 16:10:22Z wardi $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-03-19 17:10:22 +0100 (Fr, 19 Mrz 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/services/aspects/security/SamplePrincipal.java $
 * $LastChangedRevision: 3513 $
 *------------------------------------------------------------------------------
 * (c) 05.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.jaas;

import java.security.Principal;


/**
 * Title and description.
 * 
 * @author !!YOUR NAME!!
 * @version $Revision: 3513 $
 * @since !!VERSION!!
 */
public class SamplePrincipal implements Principal, java.io.Serializable
{

    /**
     * Description.
     */
    private static final long serialVersionUID = 1L;
    private final String      name;

    /**
     * Create a SamplePrincipal with a Sample username.
     * 
     * <p>
     * 
     * @param name the Sample username for this user.
     */
    public SamplePrincipal(String name)
    {
        if (name == null)
            throw new NullPointerException("illegal null input");

        this.name = name;
    }

    /**
     * Return the Sample username for this <code>SamplePrincipal</code>.
     * 
     * <p>
     * 
     * @return the Sample username for this <code>SamplePrincipal</code>
     */
    public String getName()
    {
        return name;
    }

    /**
     * Return a string representation of this <code>SamplePrincipal</code>.
     * 
     * <p>
     * 
     * @return a string representation of this <code>SamplePrincipal</code>.
     */
    @Override
    public String toString()
    {
        return ("SamplePrincipal:  " + name);
    }

    /**
     * Compares the specified Object with this <code>SamplePrincipal</code> for equality. Returns true if the given object is also a <code>SamplePrincipal</code> and the two SamplePrincipals have the same username.
     * 
     * <p>
     * 
     * @param o Object to be compared for equality with this <code>SamplePrincipal</code>.
     * 
     * @return true if the specified Object is equal equal to this <code>SamplePrincipal</code>.
     */
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
            return false;

        if (this == o)
            return true;

        if ( !(o instanceof SamplePrincipal))
            return false;
        SamplePrincipal that = (SamplePrincipal) o;

        if (this.getName().equals(that.getName()))
            return true;
        return false;
    }

    /**
     * Return a hash code for this <code>SamplePrincipal</code>.
     * 
     * <p>
     * 
     * @return a hash code for this <code>SamplePrincipal</code>.
     */
    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}
