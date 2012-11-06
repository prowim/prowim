/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 25.11.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
This file is part of ProWim.

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
Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>. */
package org.prowim.portal.view.process;

import java.util.HashSet;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.apache.log4j.Logger;
import org.eclipse.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.prowim.data.RemoteClient;
import org.prowim.portal.MainController;
import org.prowim.portal.dialogs.feedback.WarningDialog;
import org.prowim.portal.view.DefaultView;
import org.prowim.portal.view.browserfunctions.BFGetElementsOfProcess;
import org.prowim.portal.view.browserfunctions.BFShowKnowledge;
import org.prowim.portal.view.browserfunctions.BFShowProcess;
import org.prowim.portal.view.browserfunctions.ModelEditorFunctionFactory;
import org.prowim.rap.modeleditor.ModelEditor;



/**
 * View to show process model externally. User can only show the model and click the overlay to call dateils of a activity.
 * 
 * @author Maziar Khodaei
 * @version $Revision$
 * @since 2.0.2
 */
public class ViewerEditorView extends DefaultView
{
    /** ID of view */
    public static final String         ID                         = ViewerEditorView.class.getName();

    private final static Logger        LOG                        = Logger.getLogger(ViewerEditorView.class);

    private ModelEditor                modelEditor;

    private final Set<BrowserFunction> registeredBrowserFunctions = new HashSet<BrowserFunction>();

    private final String               user                       = "Support-Ebcot-User";
    private final char[]               psw                        = { 'l', 'i', 's', 'a', 'm', 'i', 'n', 'e', 'l', 'l', 'i' };

    /**
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {
        super.createPartControl(parent);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.portal.view.DefaultView#createContent(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createContent(Composite container)
    {
        Composite composite = new Composite(container, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(1, false));

        try
        {
            RemoteClient.getInstance().createConnection(user, psw.toString());
        }
        catch (LoginException e)
        {
            LOG.error("No connection: ", e);
            WarningDialog.openWarning(null, "Es konnte keine Verbindung erstellt werden.");
        }

        modelEditor = new ModelEditor(composite);

        reInitBrowserFunctions();

        String prowimID = RWT.getRequest().getParameter("prowimid");
        modelEditor.setModelName(MainController.getInstance().getName(prowimID));
        modelEditor.setViewerMode(prowimID);
        modelEditor.setModelXML(MainController.getInstance().loadProcessAsXML(prowimID));
    }

    /**
     * Reinit all required {@link BrowserFunction}s
     */
    private void reInitBrowserFunctions()
    {
        registeredBrowserFunctions.addAll(ModelEditorView.initCommonModelEditorBrowserFunctions(modelEditor));
        initBrowserFunctions();
    }

    /**
     * Initializes browser functions for transaction between RAP and JS(Model editor).
     */
    private void initBrowserFunctions()
    {
        // Shows attribute infos to given element id
        registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowKnowledge.class, modelEditor));
        // Shows attribute infos to given element id
        registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetElementsOfProcess.class, modelEditor));
        // Returns the class name of the given element id.
        // registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFGetDirectClassOfInstance.class, modelEditor));
        // showSubProcess
        registeredBrowserFunctions.add(ModelEditorFunctionFactory.registerModelEditorFunction(BFShowProcess.class, modelEditor));
    }
}
