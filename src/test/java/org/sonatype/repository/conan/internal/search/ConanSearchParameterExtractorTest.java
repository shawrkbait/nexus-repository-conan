/*
 * Copyright (c) 2019 Sonatype, Inc..
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation and/or initial documentation
 */
package org.sonatype.repository.conan.internal.search;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.sonatype.nexus.repository.view.Parameters;

public class ConanSearchParameterExtractorTest {
    private ConanSearchParameterExtractor cspe;
    
    @Test
    public void checkNameOnly() {
        
        Parameters params = new Parameters();
        params.set("q", "Poco");
        
        cspe = new ConanSearchParameterExtractor();
        String q = cspe.extractQ(params);
        assertThat(q, is(equalTo("name:Poco")));
    }
    
    @Test
    public void checkNameAndVersion() {
        
        Parameters params = new Parameters();
        params.set("q", "Poco/1.0p8");
        
        cspe = new ConanSearchParameterExtractor();
        String q = cspe.extractQ(params);
        assertThat(q, is(equalTo("name:Poco AND version:1.0p8")));
    }
    
    @Test
    public void checkNameAndVersionAndUserChan() {
        
        Parameters params = new Parameters();
        params.set("q", "Poco/1.0p8@abc/def");
        
        cspe = new ConanSearchParameterExtractor();
        String q = cspe.extractQ(params);
        assertThat(q, is(equalTo("name:Poco AND version:1.0p8 AND group:abc AND attributes.state:def")));
    }
    
    @Test
    public void checkLeadingWildcard() {
        
        Parameters params = new Parameters();
        params.set("q", "*oco");
        
        cspe = new ConanSearchParameterExtractor();
        String q = cspe.extractQ(params);
        assertThat(q, is(equalTo("name:*oco")));
    }
}
