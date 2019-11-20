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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.sonatype.goodies.common.ComponentSupport;

public class ConanSearchResponseMapper extends ComponentSupport {
    private final ObjectMapper mapper;
    
    public ConanSearchResponseMapper() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JodaModule());
    }
    
    public String writeString(final ConanSearchResponse searchResponse) throws JsonProcessingException {
    return mapper.writeValueAsString(searchResponse);
  }

  public ConanSearchResponse readFromInputStream(final InputStream searchResponseStream) throws IOException {
    try (InputStream in = new BufferedInputStream(searchResponseStream)) {
      return mapper.readValue(in, ConanSearchResponse.class);
    }
  }

}
