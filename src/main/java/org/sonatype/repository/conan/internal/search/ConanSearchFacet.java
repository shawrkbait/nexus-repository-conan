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

import java.io.IOException;
import org.sonatype.nexus.repository.Facet;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.Parameters;

@Facet.Exposed
public interface ConanSearchFacet
    extends Facet
{
  /**
   * Fetches the v1 search results.
   */
  Content searchV1(final Parameters parameters) throws IOException;
}
