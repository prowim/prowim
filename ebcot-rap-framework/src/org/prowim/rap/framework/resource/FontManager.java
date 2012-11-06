/*==============================================================================
 * File $Id: FontManager.java 3076 2010-01-07 10:13:26Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-01-07 11:13:26 +0100 (Do, 07 Jan 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/ebcot-rap-framework/src/de/ebcot/rap/framework/resource/FontManager.java $
 * $LastChangedRevision: 3076 $
 *------------------------------------------------------------------------------
 * (c) 14.04.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;


/**
 * Title and description.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 3076 $
 */
public final class FontManager
{

    /** DIALOG_HEADER */
    public static final Font DIALOG_HEADER               = Graphics.getFont("Verdana", 9, SWT.BOLD);

    /**
     * FONT_TIMENEWROMAN_12_NORMAL
     */
    public static final Font FONT_TIMENEWROMAN_12_NORMAL = Graphics.getFont("Times New Roman", 12, SWT.NORMAL);

    /**
     * FONT_TIMENEWROMAN_12_BOLD
     */
    public static final Font FONT_TIMENEWROMAN_12_BOLD   = Graphics.getFont("Times New Roman", 12, SWT.BOLD);

    /**
     * FONT_TIMENEWROMAN_10_NORMAL
     */
    public static final Font FONT_TIMENEWROMAN_10_NORMAL = Graphics.getFont("Times New Roman", 10, SWT.NORMAL);

    /**
     * FONT_VERDANA_14_BOLD
     */
    public static final Font FONT_VERDANA_14_BOLD        = Graphics.getFont("Verdana", 14, SWT.BOLD);

    /**
     * FONT_VERDANA_16_BOLD
     */
    public static final Font FONT_VERDANA_16_BOLD        = Graphics.getFont("Verdana", 16, SWT.BOLD);

    /**
     * FONT_VERDANA_12_BOLD
     */
    public static final Font FONT_VERDANA_12_BOLD        = Graphics.getFont("Verdana", 12, SWT.BOLD);

    /**
     * FONT_VERDANA_11_BOLD
     */
    public static final Font FONT_VERDANA_11_BOLD        = Graphics.getFont("Verdana", 11, SWT.BOLD);

    /**
     * FONT_VERDANA_10_BOLD
     */
    public static final Font FONT_VERDANA_10_BOLD        = Graphics.getFont("Verdana", 10, SWT.BOLD);

    /**
     * FONT_VERDANA_9_NORMAL
     */
    public static final Font FONT_VERDANA_9_NORMAL       = Graphics.getFont("Verdana", 9, SWT.NORMAL);

    /**
     * FONT_VERDANA_12_NORMAL
     */
    public static final Font FONT_VERDANA_12_NORMAL      = Graphics.getFont("Verdana", 12, SWT.NORMAL);

    /**
     * FONT_VERDANA_18_NORMAL
     */
    public static final Font FONT_VERDANA_18_BOLD        = Graphics.getFont("Verdana", 18, SWT.BOLD);

    /**
     * FONT_VERDANA_16_NORMAL
     */
    public static final Font FONT_VERDANA_16_NORMAL      = Graphics.getFont("Verdana", 16, SWT.NORMAL);

    /**
     * FONT_TAHOMA_12_NORMAL
     */
    public static final Font FONT_TAHOMA_12_NORMAL       = Graphics.getFont("Tahoma", 12, SWT.NORMAL);

    private FontManager()
    {
    }

}
