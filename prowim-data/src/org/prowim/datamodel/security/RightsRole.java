/*==============================================================================
 * File $Id: RightsRole.java 4323 2010-07-19 15:01:15Z wardi $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-07-19 17:01:15 +0200 (Mon, 19 Jul 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/security/RightsRole.java $
 * $LastChangedRevision: 4323 $
 *------------------------------------------------------------------------------
 * (c) 12.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.security;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.prowim.Activity;
import org.prowim.datamodel.prowim.ProcessElement;



/**
 * This is a data-object-class represents RightsRole (Rechterolle -> protege).
 * 
 * @author Saad Wardi
 * @version $Revision: 4323 $
 * @since 2.0.0
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RightsRole")
@XmlType(namespace = "org.prowim.datamodel.security", name = "RightsRole", propOrder = { "allowedServices" })
public class RightsRole extends ProcessElement
{
    /** The list of the servicesnames that a RightsRole can call. */
    private ObjectArray allowedServices;

    /**
     * The default constructor.
     * 
     * @param id {@link Activity#setID(String)}.
     * @param createTime {@link Activity#setCreateTime(String)}.
     * @param name {@link Activity#setName(String)}.
     */
    public RightsRole(String id, String createTime, String name)
    {
        super(id, name, createTime);
    }

    /**
     * This non-arg constructor is needed to bind this DataObject in EJB-Context. See the J2EE specification.
     */
    public RightsRole()
    {
    }

    /**
     * {@link RightsRole#setAllowedServices(ObjectArray)}
     * 
     * @return the allowedServices not null list of the servicenames
     */
    public ObjectArray getAllowedServices()
    {
        return allowedServices;
    }

    /**
     * Sets the list of the services that the a user can call. <br>
     * Every RightsRole is configured in prowimcore protege project, so that if a user has this RightsRole laso can call toose services.
     * 
     * @param allowedServices not null services names list to set.
     */
    public void setAllowedServices(ObjectArray allowedServices)
    {
        this.allowedServices = allowedServices;
    }

}
