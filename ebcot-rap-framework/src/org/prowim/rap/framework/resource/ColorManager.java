/*==============================================================================
 * File $Id: ColorManager.java 3076 2010-01-07 10:13:26Z khodaei $
 * Project: ebcot.rap.framework
 *
 * $LastChangedDate: 2010-01-07 11:13:26 +0100 (Do, 07 Jan 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/resource/ColorManager.java $
 * $LastChangedRevision: 3076 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.rap.framework.resource;

import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.swt.graphics.Color;


/**
 * 
 * Collection of standard colors.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 3076 $
 */
public final class ColorManager
{

    /**
     * Color for view background.
     */
    public static final Color BACKGROUND_COLOR                = Graphics.getColor(255, 255, 255);

    /**
     * Color for view background.
     */
    public static final Color REQUIRED_FIELD                  = Graphics.getColor(255, 232, 187); // Graphics.getColor(255, 0, 0);

    /**
     * COLOR_LIGHT_GOLDEN_YELLOW_LIGHT
     */
    public static final Color COLOR_LIGHT_GOLDEN_YELLOW_LIGHT = Graphics.getColor(240, 200, 65);

    /**
     * COLOR_GRAYDARK
     */
    public static final Color COLOR_GRAYDARK                  = Graphics.getColor(49, 79, 79);

    /**
     * COLOR_MENU_BACKGR
     */
    public static final Color COLOR_MENU_BACKGR               = Graphics.getColor(192, 192, 192);

    /**
     * COLOR_ORANGE
     */
    public static final Color COLOR_ORANGE                    = Graphics.getColor(255, 232, 187);

    /**
     * COLOR_ORANGE
     */
    public static final Color COLOR_BACKGROUND_BLUE           = Graphics.getColor(49, 106, 197);

    /**
     * COLOR_YELLOWLIGHT
     */
    public static final Color COLOR_YELLOWLIGHT               = Graphics.getColor(254, 242, 209);

    /**
     * FONT_COLOR_WHITE
     */
    public static final Color FONT_COLOR_WHITE                = Graphics.getColor(255, 255, 255);

    private ColorManager()
    {
    }

    /** */
    // public static final Color COLOR_YELLEOWDARK = Graphics.getColor(247, 201, 113);;
    /** */
    // public static final Color COLOR_YELLOWMEDIUM = Graphics.getColor(251, 229, 176);;
    /** */
    // public static final Color COLOR_BRWONMEDIUM = Graphics.getColor(186, 208, 252);
    /** */
    // public static final Color COLOR_BROWNLIGHT = Graphics.getColor(235, 235, 235);;
    /** */
    // public static final Color COLOR_REDBRWON = Graphics.getColor(235, 235, 235);
    /** */
    // public static final Color COLOR_BLUEDARK = Graphics.getColor(180, 180, 180);
    /** */
    // public static final Color COLOR_BLUEMEDIUM = Graphics.getColor(238, 154, 119);
    // other Colors
    /** */
    // public static final Color COLOR_GRAYLIGHT = Graphics.getColor(248,197, 170);
    /** */
    // public static final Color COLOR_GRAYDARK = Graphics.getColor(49,79,79);
    /** */
    // public static final Color COLOR_BLUELIGHT = Graphics.getColor(36,19, 90);
    /** */
    // public static final Color COLOR_GRAYMEDIUM = Graphics.getColor(88, 141, 197);
    /** */
    // public static final Color COLOR_COLORERROR = Graphics.getColor(255, 127, 127);

}
