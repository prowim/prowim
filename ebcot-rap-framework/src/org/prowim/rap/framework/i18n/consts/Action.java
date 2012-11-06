/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-06-05 18:03:21 +0200 (Fr, 05 Jun 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/consts/Action.java $
 * $LastChangedRevision: 1690 $
 *------------------------------------------------------------------------------
 * (c) 14.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.i18n.consts;

import org.eclipse.swt.widgets.Listener;


/**
 * Action interface
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1690 $
 */
public interface Action
{

    /**
     * Returns the tooltip for the action (String name). By contract the tool tip has to been set in the resources.xml.
     * 
     * @return String cannot be null
     */
    String getTooltip();

    /**
     * Returns the label for the action (String name)
     * 
     * @return String
     */
    String getText();

    /**
     * Returns the label for the action (String name) as a label text with a colon.
     * 
     * @return String
     */
    String getLabelText();

    /**
     * Action with given listener registered.
     * 
     * @param listener Action listener.
     * @return Action.
     */
    org.eclipse.jface.action.Action getAction(Listener listener);

    /**
     * Action with empty listener registered.
     * 
     * @return Action.
     */
    org.eclipse.jface.action.Action getAction();

    /**
     * 
     * getMnemonic.
     * 
     * @return Integer
     */
    Integer getMnemonic();

    /**
     * 
     * Get Image from sources.
     * 
     * @return org.eclipse.swt.graphics.Image
     */
    org.eclipse.swt.graphics.Image getImage();

}
