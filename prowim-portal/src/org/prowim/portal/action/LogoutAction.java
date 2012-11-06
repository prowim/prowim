/*==============================================================================
 * File $Id: LogoutAction.java 3956 2010-06-17 07:42:28Z khodaei $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-06-17 09:42:28 +0200 (Do, 17 Jun 2010) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/action/LogoutAction.java $
 * $LastChangedRevision: 3956 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 */
package org.prowim.portal.action;

import java.text.MessageFormat;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.internal.lifecycle.JavaScriptResponseWriter;
import org.eclipse.rwt.internal.service.ContextProvider;
import org.eclipse.rwt.lifecycle.PhaseEvent;
import org.eclipse.rwt.lifecycle.PhaseId;
import org.eclipse.rwt.lifecycle.PhaseListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchWindow;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.utils.ExitFlag;



/**
 * Handle the logout action. It get the session and close the LifeCycle of workbench
 * 
 * @author Maziar Khodaei
 * @version $Revision: 3956 $
 */
@SuppressWarnings("restriction")
public class LogoutAction implements IAction
{
    private final IWorkbenchWindow window;

    /**
     * Constructor
     * 
     * @param window IWorkbenchWindow
     */
    public LogoutAction(IWorkbenchWindow window)
    {
        this.window = window;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(IPropertyChangeListener listener)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getAccelerator()
     */
    @Override
    public int getAccelerator()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getActionDefinitionId()
     */
    @Override
    public String getActionDefinitionId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getDescription()
     */
    @Override
    public String getDescription()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getDisabledImageDescriptor()
     */
    @Override
    public ImageDescriptor getDisabledImageDescriptor()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getHoverImageDescriptor()
     */
    @Override
    public ImageDescriptor getHoverImageDescriptor()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getId()
     */
    @Override
    public String getId()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getImageDescriptor()
     */
    @Override
    public ImageDescriptor getImageDescriptor()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getMenuCreator()
     */
    @Override
    public IMenuCreator getMenuCreator()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getStyle()
     */
    @Override
    public int getStyle()
    {
        return 0;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getText()
     */
    @Override
    public String getText()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getToolTipText()
     */
    @Override
    public String getToolTipText()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#isChecked()
     */
    @Override
    public boolean isChecked()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#isEnabled()
     */
    @Override
    public boolean isEnabled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#isHandled()
     */
    @Override
    public boolean isHandled()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#removePropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(IPropertyChangeListener listener)
    {

    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run()
    {
        boolean answer = MessageDialog.openConfirm(window.getShell(), Resources.Frames.Global.Texts.LOGOUT.getText(),
                                                   Resources.Frames.Dialog.Texts.LOGOUT_TEXT.getText());
        if (answer)
        {
            ExitFlag.getInstance().setLogOutFlag(true);

            final String browserText = MessageFormat.format("parent.window.location.href = \"{0}\";", "");

            // RWT.getRequest().getSession().setMaxInactiveInterval( -1);
            final Display display = Display.getCurrent();
            RWT.getLifeCycle().addPhaseListener(new PhaseListener()
            {
                private static final long serialVersionUID = 1L;

                @Override
                public void afterPhase(PhaseEvent event)
                {
                    if (display == Display.getCurrent())
                    {
                        try
                        {
                            final JavaScriptResponseWriter writer = ContextProvider.getStateInfo().getResponseWriter();
                            writer.write(browserText);
                        }
                        finally
                        {
                            RWT.getLifeCycle().removePhaseListener(this);
                        }
                    }
                }

                @Override
                public void beforePhase(PhaseEvent event)
                {
                }

                @Override
                public PhaseId getPhaseId()
                {
                    return PhaseId.RENDER;
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#runWithEvent(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void runWithEvent(Event event)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setAccelerator(int)
     */
    @Override
    public void setAccelerator(int keycode)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setActionDefinitionId(java.lang.String)
     */
    @Override
    public void setActionDefinitionId(String id)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setChecked(boolean)
     */
    @Override
    public void setChecked(boolean checked)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setDescription(java.lang.String)
     */
    @Override
    public void setDescription(String text)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setDisabledImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    @Override
    public void setDisabledImageDescriptor(ImageDescriptor newImage)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setHoverImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    @Override
    public void setHoverImageDescriptor(ImageDescriptor newImage)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setId(java.lang.String)
     */
    @Override
    public void setId(String id)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setImageDescriptor(org.eclipse.jface.resource.ImageDescriptor)
     */
    @Override
    public void setImageDescriptor(ImageDescriptor newImage)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setMenuCreator(org.eclipse.jface.action.IMenuCreator)
     */
    @Override
    public void setMenuCreator(IMenuCreator creator)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setText(java.lang.String)
     */
    @Override
    public void setText(String text)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setToolTipText(java.lang.String)
     */
    @Override
    public void setToolTipText(String text)
    {
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#getHelpListener()
     */
    @Override
    public HelpListener getHelpListener()
    {
        // TODO $Author: khodaei $ Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.jface.action.IAction#setHelpListener(org.eclipse.swt.events.HelpListener)
     */
    @Override
    public void setHelpListener(HelpListener listener)
    {
        // TODO $Author: khodaei $ Auto-generated method stub

    }
}
