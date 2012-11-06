/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-05-28 17:40:44 +0200 (Do, 28 Mai 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/consts/impl/TextConst.java $
 * $LastChangedRevision: 1650 $
 *------------------------------------------------------------------------------
 * (c) 20.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.i18n.consts.impl;

import org.jdom.Element;
import org.prowim.rap.framework.i18n.consts.Text;



/**
 * Handles the text constants in resources.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1650 $
 */
public class TextConst extends AbstractConst implements Text
{
    private String text;

    /**
     * XML element name used in configuration document.
     * 
     * @return XML element name.
     */
    @Override
    public String getTagName()
    {
        return "text";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts.Text#getLabelText()
     */
    @Override
    public String getLabelText()
    {
        return this.text + ":";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts.Text#getText()
     */
    @Override
    public String getText()
    {
        return this.text;
    }

    /**
     * Callback for configuration from XML document.
     * 
     * @param element XML element.
     * @param bundle Name of plug in bundle. Is normally define as Activator ID, e.g. "de.ebcot.rap.framework".
     * @throws ConfigureException In case something is wrong with the configuration data.
     */
    @Override
    public void configure(Element element, String bundle) throws ConfigureException
    {
        this.text = element.getText();
    }

}
