/*==============================================================================
 * File $Id: HeaderView.java 4823 2010-09-27 15:59:36Z leusmann $
 * Project: ProWim
 *
 * $LastChangedDate: 2010-09-27 17:59:36 +0200 (Mo, 27 Sep 2010) $
 * $LastChangedBy: leusmann $
 * $HeadURL: https://repository.ebcot.info/wivu/trunk/prowim-portal/src/de/ebcot/prowim/portal/view/HeaderView.java $
 * $LastChangedRevision: 4823 $
 *------------------------------------------------------------------------------
 * (c) 13.05.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
package org.prowim.portal.view;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.prowim.portal.BannerActionBar;
import org.prowim.portal.LoggedInUserInfo;
import org.prowim.portal.i18n.Resources;
import org.prowim.rap.framework.resource.FontManager;



/**
 * 
 * This view shows the header of ProWim windows. The function createBannerActionBar(banner) create afterwards the menu points such exit, configuration and search as actions in header
 * 
 * @author Maziar Khodaei
 * @version $Revision: 4823 $
 */
public class HeaderView extends ViewPart
{
    /**
     * ID of view
     */
    public static final String ID = HeaderView.class.getName();

    /**
     * 
     * Description.
     */
    public HeaderView()
    {
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public void createPartControl(Composite parent)
    {

        Composite container = new Composite(parent, SWT.FILL);
        container.setLayout(new FormLayout());

        // Create Composie for banner
        Composite banner = new Composite(container, SWT.NONE);
        banner.setBackgroundMode(SWT.INHERIT_DEFAULT);
        banner.setBackgroundImage(Resources.Frames.Header.Images.IMAGE_BANNER_BG.getImage());
        FormData fdBanner = new FormData();
        banner.setLayoutData(fdBanner);
        fdBanner.top = new FormAttachment(0, 0);
        fdBanner.left = new FormAttachment(0, 0);
        fdBanner.height = 42;
        fdBanner.right = new FormAttachment(100, 0);
        banner.setLayout(new FormLayout());

        // Label for ProWim-Logo
        Label prowimLabel = new Label(banner, SWT.NONE);
        prowimLabel.setImage(Resources.Frames.Header.Images.IMAGE_PROWIM_LOGO.getImage());
        prowimLabel.pack();
        FormData fdProwimLabel = new FormData();
        prowimLabel.setLayoutData(fdProwimLabel);
        fdProwimLabel.top = new FormAttachment(0, 0);
        fdProwimLabel.height = 42;

        // Banner for Orange Line
        Composite bgBanner2 = new Composite(container, SWT.NONE);
        bgBanner2.setBackgroundMode(SWT.INHERIT_DEFAULT);
        bgBanner2.setBackgroundImage(Resources.Frames.Header.Images.IMAGE_BG_ORANGE.getImage());
        FormData fdBgBanner2 = new FormData();
        bgBanner2.setLayoutData(fdBgBanner2);
        fdBgBanner2.top = new FormAttachment(0, 42);
        fdBgBanner2.left = new FormAttachment(0, 0);
        fdBgBanner2.height = 2;
        fdBgBanner2.right = new FormAttachment(100, 0);
        bgBanner2.setLayout(new FormLayout());

        // Composite for second banner
        Composite naviSubBgBanner = new Composite(container, SWT.NONE);
        naviSubBgBanner.setBackgroundMode(SWT.INHERIT_DEFAULT);
        naviSubBgBanner.setBackgroundImage(Resources.Frames.Header.Images.NAVI_SUB_BGD.getImage());
        FormData fdNaviSubBgBanner = new FormData();
        naviSubBgBanner.setLayoutData(fdNaviSubBgBanner);
        fdNaviSubBgBanner.top = new FormAttachment(0, 44);
        fdNaviSubBgBanner.left = new FormAttachment(0, 0);
        fdNaviSubBgBanner.height = 20;
        fdNaviSubBgBanner.right = new FormAttachment(100, 0);
        naviSubBgBanner.setLayout(new FormLayout());

        // Label for login user
        Label loginLabel = new Label(naviSubBgBanner, SWT.NONE);
        loginLabel.setText(Resources.Frames.Header.Texts.LOGIN_USER.getText() + ": ");
        loginLabel.setFont(FontManager.FONT_VERDANA_9_NORMAL);
        loginLabel.pack();
        FormData fdLoginLabel = new FormData();
        loginLabel.setLayoutData(fdLoginLabel);
        fdLoginLabel.top = new FormAttachment(21, 0);
        fdLoginLabel.left = new FormAttachment(0, 5);
        fdLoginLabel.height = 42;

        // Label for user name
        Label loginName = new Label(naviSubBgBanner, SWT.NONE);
        loginName.setText(LoggedInUserInfo.getInstance().getCurrentUser());
        // (GlobalConstants.LOGGED_USER);
        loginName.setFont(FontManager.FONT_VERDANA_9_NORMAL);
        loginName.pack();
        FormData fdLoginName = new FormData();
        loginName.setLayoutData(fdLoginName);
        fdLoginName.top = new FormAttachment(21, 0);
        fdLoginName.left = new FormAttachment(loginLabel, 5);
        fdLoginName.height = 42;

        // Get current date and time
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();

        // Label for date and time
        Label dateLabel = new Label(naviSubBgBanner, SWT.NONE);
        dateLabel.setText(dateFormat.format(date));
        dateLabel.setFont(FontManager.FONT_VERDANA_9_NORMAL);
        dateLabel.pack();
        FormData fdDateLabel = new FormData();
        dateLabel.setLayoutData(fdDateLabel);
        fdDateLabel.top = new FormAttachment(21, 0);
        fdDateLabel.right = new FormAttachment(99, 0);
        fdDateLabel.height = 42;

        // Composite for shadow in Banner
        Composite naviShadowBgBanner = new Composite(container, SWT.NONE);
        naviShadowBgBanner.setBackgroundMode(SWT.INHERIT_DEFAULT);
        naviShadowBgBanner.setBackgroundImage(Resources.Frames.Header.Images.NAVI_SHADOW_SUB_BGD.getImage());
        FormData fdNaviShadowBgBanner = new FormData();
        naviShadowBgBanner.setLayoutData(fdNaviShadowBgBanner);
        fdNaviShadowBgBanner.top = new FormAttachment(0, 62);
        fdNaviShadowBgBanner.left = new FormAttachment(0, 0);
        fdNaviShadowBgBanner.height = 5;
        fdNaviShadowBgBanner.right = new FormAttachment(100, 0);
        naviShadowBgBanner.setLayout(new FormLayout());

        // Create ActionBar
        createBannerActionBar(banner);
    }

    /**
     * Create actionbar, which included logout, configuration and search
     * 
     * @author Maziar Khodaei
     * @version $Revision: 4823 $
     */
    private void createBannerActionBar(final Composite banner)
    {
        BannerActionBar actionBar = new BannerActionBar(banner);

        FormData fdComposite = new FormData();
        fdComposite.top = new FormAttachment(0, 0);
        fdComposite.bottom = new FormAttachment(0, 60);
        actionBar.setLayoutData(fdComposite);
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
     */
    @Override
    public void setFocus()
    {
    }

}
