/*==============================import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.xml.namespace.QName;

import org.apache.commons.lang.Validate;

import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.data.RemoteClient;
import org.prowim.datamodel.algernon.Field;
import org.prowim.datamodel.algernon.RecordKV;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.OntologyVersion;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.ejb.ontology.OntologyWSAccessRemote;
import de.ebcot.tools.logging.Logger;
 see <http://www.gnu.org/licenses/>.

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
 *
 */

package de.ebcot.prowim.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.xml.namespace.QName;

import org.apache.commons.lang.Validate;
import org.prowim.data.RemoteClient;
import org.prowim.datamodel.algernon.Field;
import org.prowim.datamodel.algernon.RecordKV;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.collections.ObjectArray;
import org.prowim.datamodel.collections.UpdateFrameArray;
import org.prowim.datamodel.prowim.OntologyVersion;
import org.prowim.datamodel.prowim.UpdateWork;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.admin.AdminRemote;
import org.prowim.services.ejb.ontology.OntologyWSAccessRemote;

import de.ebcot.tools.logging.Logger;


/**
 * Proxy class that calls the {@link AdminRemote} interface to migrate and cleans the ontology.
 * 
 * @author Saad Wardi
 * @version $Revision: 4905 $
 */
public class MigrationController
{
    private static final String    ERROR = "ERROR";

    private static final Logger    LOG   = Logger.getLogger(MigrationController.class);
    private AdminRemote            adminService;
    private OntologyWSAccessRemote updateService;

    /**
     * Connects to the server.
     * 
     * @param wsdl the not null wsdl.
     */
    public void connect(String wsdl)
    {
        Validate.notNull(wsdl);
        URL baseURL;
        URL url = null;
        QName qname = new QName(ServiceConstants.PROWIM_ADMIN_NAMESPACE, ServiceConstants.PROWIM_ADMIN_SERVICE_NAME);
        try
        {
            baseURL = org.prowim.proxy.AdminService.class.getResource(".");
            url = new URL(baseURL, wsdl);

        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }

        RemoteClient client = createAdminConnection("Support-Ebcot-User", "lisaminelli", url, qname);
        adminService = client.getAdminService();
        adminService.initKnowledgeBase();
    }

    /**
     * Connects to a server using the wsdl.
     * 
     * @param wsdl the not null wsdl
     */
    public void connecAccess(String wsdl)
    {
        Validate.notNull(wsdl);
        URL baseURL;
        URL url = null;
        QName qname = new QName(ServiceConstants.ONTOLOGYWSACCESS_NAMESPACE, ServiceConstants.ONTOLOGYWSACCESS_SERVICE_NAME);
        try
        {

            baseURL = org.prowim.proxy.OntologyWSAccessService.class.getResource(".");
            url = new URL(baseURL, wsdl);
        }
        catch (MalformedURLException e)
        {
            System.out.println(e);
        }

        RemoteClient client = createAlgernonConnection("Support-Ebcot-User", "lisaminelli", url, qname);
        updateService = client.getAlgernonService();

    }

    /**
     * Returns the last version of the ontology..
     * 
     * @return not null version.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public String getLastVersion() throws OntologyErrorException
    {
        return adminService.getOntologyVersion();
    }

    /**
     * Gets all versions.
     * 
     * @return not null list of the {@link OntologyVersion}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public List<OntologyVersion> getAllVersions() throws OntologyErrorException
    {
        return adminService.getVersions().getItem();
    }

    /**
     * Gets the updateframes.
     * 
     * @param versionID not null version ID.
     * @return not null {@link UpdateFrameArray}. If no item exists, an empty list is returned.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public UpdateFrameArray getUpdateFrames(String versionID) throws OntologyErrorException
    {
        Validate.notNull(versionID);
        return adminService.getUpdateFrames(versionID);
    }

    /**
     * Gets the updatework for the given version.
     * 
     * @param versionID not null verison ID.
     * @return not null {@link UpdateWork}.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public UpdateWork getUpdateWork(String versionID) throws OntologyErrorException
    {
        Validate.notNull(versionID);
        return adminService.getUpdateWork(versionID);
    }

    /**
     * Executes the updates.
     * 
     * @param updateWork not null updateWork.
     * @param serverName not null serverName.
     * @throws OntologyErrorException if an error occurs in ontology back end
     * @return true if successful, otherwise false
     */
    public boolean executeUpdate(UpdateWork updateWork, String serverName) throws OntologyErrorException
    {
        Validate.notNull(updateWork);
        Validate.notNull(serverName);
        boolean result = true;
        final UpdateWork updates = updateWork;

        // logMigration(updates.getVersionLabel(), updates.getUpdateScript(), serverName);
        ObjectArray logs = adminService.executeUpdate(updates);
        Iterator<Object> updatesIt = logs.iterator();
        while (updatesIt.hasNext())
        {
            Object updateObject = updatesIt.next();
            if (updateObject.equals(ERROR))
            {
                result = false;
            }
        }

        // logMigrationErros(logs, updates.getVersionLabel(), serverName);
        return result;
    }

    /**
     * Sets the version as not valid.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void setVersionInvalid() throws OntologyErrorException
    {
        adminService.setVersionInvalid();
    }

    /**
     * Sets the version as valid.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void setVersionValid() throws OntologyErrorException
    {
        adminService.setVersionValid();
    }

    /**
     * Executes the updates.
     * 
     * @param filename not null updateWork.
     * 
     * @return true if the updates succedded.
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public boolean executeUpdateFromFile(String filename) throws OntologyErrorException
    {
        Validate.notNull(filename);
        String updateScript = UpdateFileReader.importScriptFromFile(filename);

        return adminService.executeUpdate(updateScript);

    }

    /**
     * Executes the updates.
     * 
     * 
     * @return true if the updates succedded.
     */
    public boolean encryptPasswords()
    {
        return false;
        // return adminService.encryptPasswords();

    }

    /**
     * Cleans the ontology.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void clean() throws OntologyErrorException
    {
        adminService.clean();
    }

    /**
     * Title and description.
     * 
     * @author !!YOUR NAME!!
     * @version $Revision: 4905 $
     */
    final class WikiUpdateWork
    {
        private String linkID;
        private String oldValue;
        private String newValue;

        private WikiUpdateWork()
        {
        }
    }

    /**
     * Makes changes on the Knowledgelinks for example: the old wiki is moved from prowim-2.ebcot.info to developer.prowim.com URL.
     * 
     * @throws OntologyErrorException if an error occurs in ontology back end
     */
    public void updateWIKI() throws OntologyErrorException
    {
        List<WikiUpdateWork> list = new ArrayList<WikiUpdateWork>();
        String query = "(" + "(:BIND ?NAME :NAME)" + "(:BIND ?ref verweist_auf)" + "(:BIND ?hyper Hyperlink)" + "(:instance Wissensverweis ?w)"
                + "(?ref ?w ?st)" + "(Bezeichnung ?st ?b)" + "(:FAIL (:NEQ ?b \"ProWim Wiki\"))" + "(?NAME ?w ?wn)(?hyper ?w ?hyperlink))";
        RecordsetKV results = updateService.startKV(query, "ASK");
        List<RecordKV> records = results.getRecords();

        if (records != null)
            for (int i = 0; i < records.size(); i++)
            {
                WikiUpdateWork w = new WikiUpdateWork();
                String paramID = "";
                String oldHyper = "";
                String newHyper = "";
                RecordKV r = records.get(i);
                List<Field> ff = r.getFields();
                for (int j = 0; j < ff.size(); j++)
                {
                    Field f = ff.get(j);
                    if (f.getKey().equals("?wn") || f.getKey().equals("?hyperlink"))
                    {

                        if (f.getKey().equals("?hyperlink"))
                        {
                            String url = f.getValue();

                            url = url.replaceAll("ebcot.info", "prowim.com");
                            // Hier wird immer der Domainnamen zu �ndern f�r jeden Server
                            url = url.replace("prowim-wivu", "wivu");
                            System.out.print("the new URL " + url);
                            oldHyper = f.getValue();
                            newHyper = url;

                            w.oldValue = url;
                            w.newValue = newHyper;

                        }

                        if (f.getKey().equals("?wn"))
                        {
                            paramID = f.getValue();
                            w.linkID = paramID;
                        }
                        System.out.print("key " + f.getKey() + "  ");
                        System.out.print("value " + f.getValue() + "  ");

                        String clearQuery = "((:BIND ?slot Hyperlink)" + "(:BIND ?rid " + paramID + ")" + "(:BIND ?acontent " + oldHyper + ")"
                                + "(:DELETE (?slot ?rid ?acontent)))";

                        String setQuery = "((:BIND ?slot Hyperlink)" + "(?slot " + paramID + " \"" + newHyper + "\"))";
                        System.out.print("CLEAR-QUERY " + clearQuery);
                        System.out.print("SET-QUERY " + setQuery);
                        // ser2.start(clearQuery, "TELL");
                        // ser2.start(setQuery, "TELL");

                    }

                }
                list.add(w);
                System.out.println("------------------------");
            }
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        for (int i = 0; i < list.size(); i++)
        {
            WikiUpdateWork ww = list.get(i);
            System.out.println(ww.linkID + "  " + ww.oldValue + "  " + ww.newValue);

            String clearQuery2 = "((:CLEAR-RELATION " + ww.linkID + " Hyperlink))";
            System.out.println("  CLEAR --> " + clearQuery2);

            String setQuery = "((:BIND ?slot Hyperlink)" + "(?slot " + ww.linkID + " \"" + ww.newValue + "\"))";
            System.out.println("  SET --> " + setQuery);

            updateService.start(clearQuery2, "TELL");
            updateService.start(setQuery, "TELL");
        }
    }

    /**
     * Creates connection to the prowim server.
     * 
     * @param username the username.
     * @param password the password.
     * @param url the url.
     * @param qname the qname.
     * @return {@link RemoteClient}.
     */
    private static RemoteClient createAdminConnection(String username, String password, URL url, QName qname)
    {
        try
        {
            RemoteClient client = RemoteClient.getInstance();
            client.createAdminConnection(username, password, url, qname);
            return client;
        }
        catch (LoginException e)
        {
            LOG.error("login failed: ", e);
        }
        return null;
    }

    /**
     * Creates connection to the prowim server.
     * 
     * @param username the username.
     * @param password the password.
     * @param url the url.
     * @param qname the qname.
     * @return {@link RemoteClient}.
     */
    private static RemoteClient createAlgernonConnection(String username, String password, URL url, QName qname)
    {
        try
        {
            RemoteClient client = RemoteClient.getInstance();
            client.createAlgernonConnection(username, password, url, qname);
            return client;
        }
        catch (LoginException e)
        {
            LOG.error("login failed: ", e);
        }
        return null;
    }

}
