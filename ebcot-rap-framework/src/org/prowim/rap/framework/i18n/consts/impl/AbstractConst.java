/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-05-28 17:32:49 +0200 (Do, 28 Mai 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/consts/impl/AbstractConst.java $
 * $LastChangedRevision: 1645 $
 *------------------------------------------------------------------------------
 * (c) 14.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.==============================================================================
 *
 */
package org.prowim.rap.framework.i18n.consts.impl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.jdom.Element;
import org.prowim.rap.framework.i18n.consts.Const;



/**
 * Base class for constants.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1645 $
 */
public class AbstractConst implements Const
{
    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getIdAttrName()
     */
    @Override
    public String getIdAttrName()
    {
        return "id";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getTagName()
     */
    @Override
    public String getTagName()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#configure(org.jdom.Element, java.lang.String)
     */
    @Override
    public void configure(Element element, String bundle) throws ConfigureException
    {
    }

    /**
     * 
     * This function gets the path of image and get the descriptor of this back. You have to call getImage(), when you use this and want to became the image
     * 
     * @param imageStr Images in action
     * @param bundle Name of plug in bundle. Is normally define as Activator ID, e.g. "de.ebcot.rap.framework".
     * 
     * @return Image
     * @throws ConfigureException Exception handling
     */
    protected ImageDescriptor loadImage(String imageStr, String bundle) throws ConfigureException
    {
        // Get image from source

        if (Display.getCurrent() == null)
        {
            PlatformUI.createDisplay();
        }

        final ImageDescriptor imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(bundle, imageStr);

        if (imageDescriptor != null)
        {
            Image image = imageDescriptor.createImage();

            return ImageDescriptor.createFromImage(image);
        }
        else
        {
            throw new IllegalArgumentException("Cannot find image [" + imageStr + "] in bundle [" + bundle + "]");
        }
    }
}
