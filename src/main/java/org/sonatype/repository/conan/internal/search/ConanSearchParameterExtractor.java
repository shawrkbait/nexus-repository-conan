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

import com.google.common.base.Strings;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Named;
import javax.inject.Singleton;
import org.sonatype.goodies.common.ComponentSupport;
import org.sonatype.nexus.repository.view.Parameters;

@Named
@Singleton
public class ConanSearchParameterExtractor extends ComponentSupport {
    
    public String extractQ(final Parameters parameters) {
        String q = Strings.nullToEmpty(parameters.get("q")).trim();
        StringBuilder outs = new StringBuilder();
        String[] qargs = q.split("[/@]");
        outs.append("name:").append(qargs[0]);
        if(qargs.length >= 2)
            outs.append(" AND version:").append(qargs[1]);
        if(qargs.length >= 3) 
            outs.append(" AND group:").append(qargs[2]);
        if(qargs.length >= 4) 
            outs.append(" AND attributes.state:").append(qargs[3]);
        return outs.toString();
    }
}
