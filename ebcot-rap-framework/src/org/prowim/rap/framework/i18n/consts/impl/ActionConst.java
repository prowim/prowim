/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate: 2009-06-05 18:03:34 +0200 (Fr, 05 Jun 2009) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/portal/src/de/ebcot/prowim/portal/consts/impl/ActionConst.java $
 * $LastChangedRevision: 1691 $
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
package org.prowim.rap.framework.i18n.consts.impl;

import javax.swing.KeyStroke;

import org.apache.commons.lang.Validate;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jdom.Element;
import org.prowim.rap.framework.i18n.consts.Action;



/**
 * This class get the informations from resources and put this in one action. To crate a instance of action, you have to call the getAction() function of this class.
 * 
 * @author Maziar Khodaei
 * @version $Revision: 1691 $
 */
public class ActionConst extends AbstractConst implements Action
{

    // String constants for the attribute variables of an action
    private static final String ATTRIBUTE_ID          = "id";
    private static final String ATTRIBUTE_LABEL       = "label";
    private static final String ATTRIBUTE_MNEMONIC    = "mnemonic";
    private static final String ATTRIBUTE_ACCELERATOR = "accelerator";
    private static final String ATTRIBUTE_TOOLTIP     = "tooltip";
    private static final String ATTRIBUTE_SRC         = "src";
    // private static final String ATTRIBUTE_ID = "id";

    // ~ Instance fields ------------------------------------------------------------------------------------------------------------------------------

    // properties of a action
    private String              id;
    private ImageDescriptor     image;
    private Integer             mnemonic;
    private KeyStroke           accelerator;
    private String              label;
    private String              mnemonicStr;
    private String              tooltip;

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getAction(org.eclipse.swt.widgets.Listener)
     */
    @Override
    public org.eclipse.jface.action.Action getAction(final Listener listener)
    {
        if (listener == null)
        {
            throw new IllegalArgumentException("actionListener is null");
        }

        // create action for listener
        org.eclipse.jface.action.Action action = new org.eclipse.jface.action.Action()
        {
            @Override
            public void runWithEvent(Event event)
            {
                listener.handleEvent(event);
            }

        };

        // Set the ID of action
        action.setId(this.id);

        // Set the label of action
        action.setText(this.label);

        // if (this.mnemonic != null)
        // {
        // // action.putValue(javax.swing.Action.MNEMONIC_KEY, m_mnemonic);
        // }

        // set accelerator of action
        if (this.accelerator != null)
        {
            action.setAccelerator(org.eclipse.jface.action.Action.convertAccelerator(accelerator.toString()));
        }

        // Set image og action as ImageDescriptor
        if (this.image != null)
        {
            action.setImageDescriptor(this.image);
        }

        // Set tooltip of action
        if (this.tooltip != null)
        {
            action.setToolTipText(this.tooltip);
        }

        return action;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getLabelText()
     */
    @Override
    public String getLabelText()
    {
        return label + ":";
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getMnemonic()
     */
    @Override
    public Integer getMnemonic()
    {
        return this.mnemonic;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getText()
     */
    @Override
    public String getText()
    {
        return this.label;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts#getTooltip()
     */
    @Override
    public String getTooltip()
    {
        Validate.notNull(this.tooltip);
        return tooltip;
    }

    /**
     * XML element name used in configuration document.
     * 
     * @return XML element name.
     */
    @Override
    public String getTagName()
    {
        return "action";
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts.impl.AbstractConst#configure(org.jdom.Element, java.lang.String)
     */
    @Override
    public void configure(Element element, String bundle) throws ConfigureException
    {
        // ID of action
        this.setId(element.getAttributeValue(ATTRIBUTE_ID));

        // label
        this.label = element.getAttributeValue(ATTRIBUTE_LABEL);

        // image
        if (element.getAttributeValue(ATTRIBUTE_SRC) != null)
        {

            this.image = loadImage(element.getAttributeValue(ATTRIBUTE_SRC), bundle);
        }

        // mnemonic
        if (element.getAttributeValue(ATTRIBUTE_MNEMONIC) != null)
        {
            int mnemonic = 0;

            // shortCutKey is a String containing the short cut
            this.mnemonicStr = element.getAttributeValue(ATTRIBUTE_MNEMONIC).toUpperCase();
            // ********************** this part not working OK. It must reimplemented for this application *************************************
            // Field theField = null;
            // try
            // {
            // theField = java.awt.event.KeyEvent.class.getField("VK_" /* NOI18N */+ m_mnemonicStr);
            // }
            // catch (SecurityException e)
            // {
            // e.printStackTrace();
            // throw new ConfigureException("Could not find mnemonic constant" /* NOI18N */+ m_mnemonicStr + " for element " /* NOI18N */
            // + element.getAttributeValue(ATTRIBUTE_ID), e);
            // }
            // catch (NoSuchFieldException e)
            // {
            // e.printStackTrace();
            // }
            // try
            // {
            // mnemonic = theField.getInt(null);
            // }
            // catch (IllegalArgumentException e1)
            // {
            // e1.printStackTrace();
            // throw new ConfigureException("Could not find mnemonic constant" + m_mnemonicStr + " for element " /* NOI18N */
            // + element.getAttribute(ATTRIBUTE_ID), e1);
            //
            // }
            // catch (IllegalAccessException e1)
            // {
            // e1.printStackTrace();
            // }

            this.mnemonic = new Integer(mnemonic);
        }

        // mnemonic
        if (this.mnemonic != null)
        {
            if (this.label == null)
            {
                throw new ConfigureException("If mnemonic is given, the label attribute is required!  action element: " + element);
            }

            if (this.label.toUpperCase().indexOf(this.mnemonicStr.toUpperCase()) == -1)
            {
                throw new ConfigureException("Mnemonic is not part of label! label:" + this.label + " mnemonic:" + this.mnemonicStr);
            }
        }

        // accelerator
        if (element.getAttributeValue(ATTRIBUTE_ACCELERATOR) != null)
        {
            this.accelerator = KeyStroke.getKeyStroke(element.getAttributeValue(ATTRIBUTE_ACCELERATOR));

            if (this.accelerator == null)
            {
                throw new ConfigureException("Could not parse accelerator: " + element.getAttribute(ATTRIBUTE_ACCELERATOR));
            }
        }

        // short description
        this.tooltip = element.getAttributeValue(ATTRIBUTE_TOOLTIP);
    }

    /**
     * set ID of action
     * 
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }

    /**
     * get ID of action
     * 
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts.Action#getImage()
     */
    @Override
    public Image getImage()
    {
        return this.image.createImage();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.rap.framework.i18n.consts.Action#getAction()
     */
    @Override
    public org.eclipse.jface.action.Action getAction()
    {
        return getAction(new Listener()
        {

            @Override
            public void handleEvent(Event event)
            {
                // default action. does nothing
            }
        });
    }

}
