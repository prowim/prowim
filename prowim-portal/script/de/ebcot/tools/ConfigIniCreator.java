package de.ebcot.tools;

/*******************************************************************************
 * Copyright (c) 2002-2006 Innoopract Informationssysteme GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Innoopract Informationssysteme GmbH - initial API and implementation
 ******************************************************************************/

import java.io.File;


/**
 * <p>
 * This tool creates the content of a simple config.ini file. This included all plugins, which are needed for portal <br>
 * and are in the RAP-directory of eclipse. Before starting you have to copy necessary plugins in <br>
 * "..\\build\\deployment\\deploy\\WEB-INF\\eclipse\\plugins". Than you can run this method and copy <br>
 * the result in config.ini file. <br>
 * Later this should go on automatically.
 * </p>
 * 
 */
public final class ConfigIniCreator
{

    private static final String FRAMEWORK_BUNDLE = "org.eclipse.osgi_";
    private static final String EXTENSIONBUNDLE  = "org.eclipse.equinox.servletbridge.extensionbundle";

    private ConfigIniCreator()
    {
    }

    /**
     * 
     * Description.
     * 
     * @param arx argument
     */
    public static void main(final String[] arx)
    {
        // //////////////////////////////////////////////////////////////////////////
        // replace this with the absolute path to the plugin directory of
        // the deployment build for example

        File file = new File("E:\\workspace_galileo\\prowim-portal\\build\\deployment\\deploy\\WEB-INF\\eclipse\\plugins");
        // File file = new File( "C:\\Users\\Ruediger\\RAP\\Presentations\\ECon 2009\\tutorial\\projects\\org.eclipse.rap.demo.feature\\build\\demo\\WEB-INF\\eclipse\\plugins" );
        // //////////////////////////////////////////////////////////////////////////

        String[] list = file.list();
        StringBuffer buffer = new StringBuffer();
        buffer.append("#Eclipse Runtime Configuration File\n");
        buffer.append("osgi.bundles=");
        for (int i = 0; i < list.length; i++)
        {
            String fileName = list[i];
            if (fileName.endsWith(".jar") && !fileName.startsWith(FRAMEWORK_BUNDLE))
            {
                // Remove version number
                int underscorePos = fileName.lastIndexOf("_");
                String bundleName = fileName.substring(0, underscorePos);
                // Append bundle name
                buffer.append("  ");
                buffer.append(bundleName);
                if ( !list[i].startsWith("org.eclipse.rap.rwt.q07_"))
                {
                    if (list[i].startsWith("org.eclipse.equinox.common_"))
                    {
                        buffer.append("@2:start");
                    }
                    else
                    {
                        buffer.append("@start");
                    }
                }
                buffer.append(",\\\n");
                if (i + 1 >= list.length)
                {
                    buffer.append(EXTENSIONBUNDLE);
                }
            }
        }
        buffer.append("\n");
        buffer.append("osgi.bundles.defaultStartLevel=4\n");
        // write the content to the console
        System.out.print(buffer);
    }
}
