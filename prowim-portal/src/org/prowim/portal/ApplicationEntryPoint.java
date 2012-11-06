/*==============================================================================
 * File ApplicationEntryPoint.java
 * Project: ProWim
 *
 * $LastChangedDate: 2011-05-10 14:14:31 +0200 (Di, 10 Mai 2011) $
 * $LastChangedBy: khodaei $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/ApplicationEntryPoint.java $
 * $LastChangedRevision: 5083 $
 *------------------------------------------------------------------------------
 * (c) 13.02.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal;

import javax.security.auth.login.LoginException;
import javax.xml.ws.WebServiceException;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.rwt.RWT;
import org.eclipse.rwt.graphics.Graphics;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.prowim.data.RemoteClient;
import org.prowim.portal.dialogs.feedback.WarningDialog;
import org.prowim.portal.i18n.Resources;
import org.prowim.portal.update.UpdateRegistry;
import org.prowim.portal.utils.ExitFlag;
import org.prowim.portal.utils.GlobalConstants;
import org.prowim.rap.framework.components.ButtonFactory;
import org.prowim.rap.framework.i18n.configurator.ResourcesConfigurator;
import org.prowim.rap.framework.resource.FontManager;



/**
 * This is the entry point of application. This is a standard schema of RAP to create one plug-in
 * 
 * @author Maziar Khodaei
 * @version $Revision: 5083 $
 */
public class ApplicationEntryPoint implements IEntryPoint
{
    /** The timout for the session, default is 5400 secs */
    public static final int     SESSION_TIMEOUT;

    /**
     * The default session timeout
     */
    private static final int    DEFAULT_SESSION_TIMEOUT = 5400;

    private final static Logger LOG                     = Logger.getLogger(ApplicationEntryPoint.class);

    /**
     * login boolean state, if successful logged in
     */
    private boolean             login                   = false;

    static
    {
        String timeoutStr = System.getProperty(GlobalConstants.SESSION_TIMEOUT_PROPERTY_KEY);
        int timeout = DEFAULT_SESSION_TIMEOUT;
        if (timeoutStr != null)
        {
            try
            {
                timeout = Integer.parseInt(timeoutStr);
            }
            catch (NumberFormatException e)
            {
                LOG.error("Cannot parse timeout property. using default");
            }
        }
        else
        {
            LOG.warn("Cannot find timeout property. Using default");
        }

        SESSION_TIMEOUT = timeout;

        ResourcesConfigurator.configure(Resources.class, Activator.PLUGIN_ID);
    }

    /**
     * createUI: Entry point of GUI. When the login is successes, than create the workbench
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5083 $
     */
    public int createUI()
    {
        ExitFlag.getInstance().setLogOutFlag(false);

        Display display = null;
        if (Display.getCurrent() == null)
        {

            display = PlatformUI.createDisplay();
        }
        else
        {
            display = Display.getCurrent();
        }

        // Sets session time out
        RWT.getRequest().getSession().setMaxInactiveInterval(SESSION_TIMEOUT);

        // If domain has a extension "prowimid" than shows only the model of given process, else show the main portal site
        String view = RWT.getRequest().getParameter("prowimid");

        if (view != null && !view.equals(""))
        {
            // register UpdateRegistry
            RWT.getLifeCycle().addPhaseListener(UpdateRegistry.getInstance());
            return PlatformUI.createAndRunWorkbench(display, new ViewerAdvisor());
        }
        else
        {
            // If login is OK, then create the workbench
            if (doLogin(display))
            {
                // register UpdateRegistry
                RWT.getLifeCycle().addPhaseListener(UpdateRegistry.getInstance());
                return PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor());
            }
            else
                return 0;
        }
    }

    /**
     * doLogin: this function create a Dialog for login and checks the user information for login
     * 
     * @author Maziar Khodaei
     * @version $Revision: 5083 $
     */
    private boolean doLogin(final Display display)
    {
        // Open a shell window for login
        final Shell shell = new Shell(display, SWT.CENTER | SWT.RESIZE);
        shell.setBackground(Graphics.getColor(238, 238, 238));
        final int height = 250;
        final int width = 400;

        // Show login block
        final Composite mainComposite = new Composite(shell, SWT.CENTER);
        mainComposite.setVisible(true);
        mainComposite.setSize(width, height);
        mainComposite.setLocation(shell.getSize().x / 2, shell.getSize().y / 2);

        // Create a group for the frame
        final Group mainGroup = new Group(mainComposite, SWT.SHADOW_OUT | SWT.CENTER);
        mainGroup.setBackgroundMode(SWT.INHERIT_DEFAULT);
        mainGroup.setSize(width, height);
        mainGroup.setBackground(Graphics.getColor(238, 238, 238));
        mainGroup.setLayout(new GridLayout(4, true));

        // Productlogo in login-Dialog
        Label titleImage = new Label(mainGroup, SWT.CENTER);
        titleImage.setImage(Resources.Frames.Global.Images.PRODUCT_LOGO.getImage());
        titleImage.setAlignment(SWT.CENTER);
        titleImage.setLayoutData(GridDataFactory.fillDefaults().span(4, 10).create());

        // Title in login-Dialog
        Label titleLbl = new Label(mainGroup, SWT.CENTER);
        titleLbl.setText(Resources.Frames.Dialog.Texts.LOGIN_MAIN_TITLE.getText());
        titleLbl.setFont(FontManager.FONT_VERDANA_11_BOLD);
        titleLbl.setAlignment(SWT.CENTER);
        titleLbl.setLayoutData(GridDataFactory.fillDefaults().span(4, 10).create());

        Label userNameLbl = new Label(mainGroup, SWT.NONE);
        userNameLbl.setText(Resources.Frames.Global.Texts.NICKNAME.getLabelText());
        userNameLbl.setAlignment(SWT.RIGHT);
        userNameLbl.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());
        userNameLbl.setFont(FontManager.FONT_VERDANA_10_BOLD);

        final Text userNameTxt = new Text(mainGroup, SWT.BORDER);
        userNameTxt.setFont(FontManager.FONT_TIMENEWROMAN_12_NORMAL);
        userNameTxt.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
        userNameTxt.setFocus();

        Label pswLbl = new Label(mainGroup, SWT.CENTER);
        pswLbl.setText(Resources.Frames.Global.Texts.PASSWORD.getLabelText());
        pswLbl.setAlignment(SWT.RIGHT);
        pswLbl.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());
        pswLbl.setFont(FontManager.FONT_VERDANA_10_BOLD);

        final Text pswTxt = new Text(mainGroup, SWT.BORDER | SWT.PASSWORD | SWT.SINGLE);

        pswTxt.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
        // pswTxt.addKeyListener(new KeyAdapter()
        // {
        // @Override
        // public void keyPressed(KeyEvent e)
        // {
        // if (e.keyCode == 13)
        // login = validateLogin(userNameTxt.getText(), pswTxt.getText());
        // }
        // });
        //
        Label lblDummy = new Label(mainGroup, SWT.NONE);
        lblDummy.setLayoutData(GridDataFactory.fillDefaults().span(3, 1).create());
        lblDummy.setVisible(false);

        Button loginBtn = ButtonFactory.create(mainGroup, Resources.Frames.Global.Actions.LOGIN.getAction(new Listener()
        {
            @Override
            public void handleEvent(Event event)
            {
                login = validateLogin(userNameTxt.getText(), pswTxt.getText());
            }
        }));
        loginBtn.setLayoutData(GridDataFactory.fillDefaults().span(1, 1).create());

        Label separator = new Label(mainGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
        separator.setLayoutData(GridDataFactory.fillDefaults().span(5, 5).create());

        Label copyRightLbl = new Label(mainGroup, SWT.CENTER);
        copyRightLbl.setText(Resources.Frames.Global.Texts.COPY_RIGHT.getText() + " " + Resources.Frames.Global.Texts.EBCOT.getText() + " - "
                + Resources.Frames.Global.Texts.VERSION.getText() + " "
                + Platform.getBundle(GlobalConstants.PORTAL_BUNDLE_NAME).getVersion().toString());
        copyRightLbl.setAlignment(SWT.CENTER);
        copyRightLbl.setLayoutData(GridDataFactory.fillDefaults().span(4, 1).create());
        copyRightLbl.setFont(FontManager.FONT_TIMENEWROMAN_10_NORMAL);

        shell.addListener(SWT.Resize, new Listener()
        {
            public void handleEvent(Event e)
            {
                shell.layout();
            }
        });

        shell.setMaximized(true);

        shell.open();

        while ( !shell.isDisposed())
        {
            if (login)
            {
                break;
            }
            if ( !display.readAndDispatch())
            {
                display.sleep();
            }
        }
        return login;
    }

    /**
     * Tries to do a login. If not possible, the corresponding dialog will pop up.
     */
    private boolean validateLogin(String userName, String password)
    {
        try
        {
            RemoteClient.getInstance().createConnection(userName, password);
        }
        catch (LoginException e)
        {
            LOG.error("No connection: ", e);
            WarningDialog.openWarning(null, "Es konnte keine Verbindung erstellt werden.");
            return false;
        }

        try
        {
            if (MainController.getInstance().login(userName, password))
            {
                LoggedInUserInfo.getInstance().setCurrentUser(userName, password);
                return true;
            }
            else
            {
                WarningDialog.openWarning(null, "Die Anmeldeinformationen sind nicht gültig. Bitte überprüfen Sie ihre Eingaben.");
                return false;
            }
        }
        catch (WebServiceException e)
        {
            LOG.error("Could not transmit message: ", e);
            WarningDialog.openWarning(null, "Es konnte keine Verbindung erstellt werden.");
            return false;
        }
        catch (Exception e)
        {
            LOG.error("Could not transmit message: ", e);
            WarningDialog.openWarning(null, "Es konnte keine Verbindung erstellt werden.");
            return false;
        }

    }
}
