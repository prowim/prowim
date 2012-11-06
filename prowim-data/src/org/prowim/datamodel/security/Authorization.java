/*==============================================================================
 * File $Id: Authorization.java 3529 2010-03-23 15:33:00Z wardi $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-03-23 16:33:00 +0100 (Di, 23 Mrz 2010) $
 * $LastChangedBy: wardi $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-server/src/de/ebcot/prowim/datamodel/security/Authorization.java $
 * $LastChangedRevision: 3529 $
 *------------------------------------------------------------------------------
 * (c) 15.03.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.datamodel.security;

/**
 * Authorization object for authorize user.
 * 
 * @author Saad Wardi
 * @version $Revision: 3529 $
 * @since 2.0.0
 */
public class Authorization
{
    private String  userID;
    private String  roleID;
    private String  rolename;
    private String  classname;
    private String  methodname;
    private boolean methodIncluded = false;

    /**
     * Constructs this.
     * 
     * @param userID {@link Authorization#setUserID(String)}
     * @param roleID {@link Authorization#setUserID(String)}
     * @param rolename {@link Authorization#setUserID(String)}
     * @param classname {@link Authorization#setUserID(String)}
     * @param methodname {@link Authorization#setUserID(String)}
     * @param included {@link Authorization#setUserID(String)}
     */
    public Authorization(String userID, String roleID, String rolename, String classname, String methodname, boolean included)
    {
        setUserID(userID);
        setRoleID(roleID);
        setRolename(rolename);
        setClassname(classname);
        setMethodname(methodname);
        setMethodIncluded(included);

    }

    /**
     * {@link Authorization#setUserID(String)}
     * 
     * @return the userID
     */
    public String getUserID()
    {
        return userID;
    }

    /**
     * Sets the user ID.
     * 
     * @param userID the userID to set
     */
    private void setUserID(String userID)
    {
        this.userID = userID;
    }

    /**
     * {@link Authorization#setRoleID(String)}
     * 
     * @return the roleID
     */
    public String getRoleID()
    {
        return roleID;
    }

    /**
     * Sets the role ID.
     * 
     * @param roleID the roleID to set
     */
    private void setRoleID(String roleID)
    {
        this.roleID = roleID;
    }

    /**
     * {@link Authorization#setRolename(String)}
     * 
     * @return the rolename
     */
    public String getRolename()
    {
        return rolename;
    }

    /**
     * Sets the rolename.
     * 
     * @param rolename the rolename to set
     */
    private void setRolename(String rolename)
    {
        this.rolename = rolename;
    }

    /**
     * {@link Authorization#setClassname(String)}
     * 
     * @return the classname
     */
    public String getClassname()
    {
        return classname;
    }

    /**
     * Sets the classname.
     * 
     * @param classname the classname to set
     */
    private void setClassname(String classname)
    {
        this.classname = classname;
    }

    /**
     * {@link Authorization#setMethodname(String)}
     * 
     * @return the methodname
     */
    public String getMethodname()
    {
        return methodname;
    }

    /**
     * Sets the methodname.
     * 
     * @param methodname the methodname to set
     */
    private void setMethodname(String methodname)
    {
        this.methodname = methodname;
    }

    /**
     * {@link Authorization#setMethodIncluded(boolean)}.
     * 
     * @return the methodIncluded
     */
    private boolean isMethodIncluded()
    {
        return methodIncluded;
    }

    /**
     * Sets the method is included.
     * 
     * @param methodIncluded the methodIncluded to set
     */
    private void setMethodIncluded(boolean methodIncluded)
    {
        this.methodIncluded = methodIncluded;
    }

    /**
     * checks if a user is authorized to call a method.
     * 
     * @return true if the user is authorized.
     */
    public boolean isAuthorized()
    {
        return isMethodIncluded();
    }

    @Override
    public String toString()
    {
        return this.classname + "   " + this.methodname + "  " + this.roleID + "  " + this.rolename + "  " + this.userID;
    }

}
