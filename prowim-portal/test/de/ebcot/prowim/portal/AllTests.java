/*==============================================================================
 * File $Id$
 * Project: ProWim
 *
 * $LastChangedDate$
 * $LastChangedBy$
 * $HeadURL$
 * $LastChangedRevision$
 *------------------------------------------------------------------------------
 * (c) 23.07.2010 Ebcot Business Solutions GmbH. More info: http://www.ebcot.de
 * All rights reserved. Use is subject to license terms.
 *==============================================================================
 */
package de.ebcot.prowim.portal;

import junit.framework.Test;
import junit.framework.TestSuite;
import de.ebcot.prowim.portal.utils.StringSubstitutionTest;


/**
 * Suite to bind all JUnit tests project wide.
 * 
 * @author Oliver Wolff
 * @version $Revision$
 * @since 2.0.a9
 */
public final class AllTests
{
    private AllTests()
    {

    }

    /**
     * 
     * Start the suite.
     * 
     * @return Test result of the integrated tests.
     */
    public static Test suite()
    {
        TestSuite testSuite = new TestSuite();
        testSuite.setName("All tests");

        testSuite.addTestSuite(StringSubstitutionTest.class);

        return testSuite;
    }
}
