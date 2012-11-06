/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 *------------------------------------------------------------------------------
 * (c) 07.09.2009 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
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
 *
 */

package org.prowim.services.ejb.ontology;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.jws.WebService;

import org.jboss.security.annotation.SecurityDomain;
import org.jboss.wsf.spi.annotation.WebContext;
import org.prowim.datamodel.algernon.RecordSet;
import org.prowim.datamodel.algernon.RecordsetKV;
import org.prowim.datamodel.algernon.Result;
import org.prowim.jca.connector.algernon.OntologyErrorException;
import org.prowim.services.algernon.AlgernonService;
import org.prowim.services.ejb.ServiceConstants;
import org.prowim.services.ejb.ontology.OntologyWSAccessRemote;

import de.ebcot.tools.logging.Logger;


/**
 * Implements the functions for {@link OntologyWSAccessRemote}.
 * 
 * @author wardi
 * @version $Revision: 4772 $
 */
@Stateless(name = ServiceConstants.ONTOLOGYWSACCESS_BEAN)
@WebService(name = ServiceConstants.ONTOLOGYWSACCESS_SERVICE_NAME, targetNamespace = ServiceConstants.ONTOLOGYWSACCESS_NAMESPACE, serviceName = ServiceConstants.ONTOLOGYWSACCESS_SERVICE_NAME, endpointInterface = ServiceConstants.ONTOLOGYWSACCESS_SEI)
@WebContext(contextRoot = "/" + ServiceConstants.CONTEXT_ROOT)
@SecurityDomain("JBossWS3")
@PermitAll
public class OntologyWSAccessBean implements OntologyWSAccessRemote
{
    private static final Logger LOG = Logger.getLogger(OntologyWSAccessBean.class);

    /** the algernon service. */
    @EJB
    private AlgernonService     algService;

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#getKB()
     */
    @PermitAll
    public String getKB()
    {

        LOG.info("\n\n CALL getKB \n\n");
        String res = algService.getKB();
        LOG.info("  getKB res  :  " + res);
        return res;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#saveKB()
     */
    public String saveKB() throws OntologyErrorException
    {
        return algService.saveKB();
    }

    /**
     * 
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#start(java.lang.String, java.lang.String)
     */
    public Result start(String query, String askTell) throws OntologyErrorException
    {
        return algService.start(query, askTell);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#startKV(java.lang.String, java.lang.String)
     */
    public RecordsetKV startKV(String query, String askTell) throws OntologyErrorException
    {
        return algService.startKV(query, askTell);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#reloadKB()
     */
    @PermitAll
    @Override
    public String reloadKB()
    {
        return algService.getKB();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.prowim.services.ejb.ontology.OntologyWSAccessRemote#startRS(java.lang.String, java.lang.String)
     */
    @Override
    public RecordSet startRS(String arg0, String arg1) throws OntologyErrorException
    {
        return algService.startRS(arg0, arg1);
    }

}
