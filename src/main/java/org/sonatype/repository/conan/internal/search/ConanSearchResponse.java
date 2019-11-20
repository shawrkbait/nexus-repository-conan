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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import javax.annotation.Nullable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ConanSearchResponse {
      @Nullable
  private List<String> results;

  @Nullable
  public List<String> getResults() {
    return results;
  }

  public void setResults(@Nullable final List<String> results) {
    this.results = results;
  }
}
