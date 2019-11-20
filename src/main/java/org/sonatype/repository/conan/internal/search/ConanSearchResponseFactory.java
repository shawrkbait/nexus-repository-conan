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

import java.util.ArrayList;
import static java.util.Collections.emptyList;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.toList;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHits;
import org.sonatype.goodies.common.ComponentSupport;

public class ConanSearchResponseFactory extends ComponentSupport {

    private static final Double DEFAULT_SCORE = 0.0;

    /**
     * Builds an empty search response (used in the scenario where no search
     * text was provided, to mimic conan registry behavior as of this writing.
     */
    public ConanSearchResponse buildEmptyResponse() {
        ConanSearchResponse response = new ConanSearchResponse();
        response.setResults(emptyList());
        return response;
    }

    /**
     * Builds a search response containing each of the included search buckets.
     */
    public ConanSearchResponse buildResponseForResults(final List<Terms.Bucket> buckets, final int size, final int from) {
/*
        List<String> objects = buckets.stream()
                .map(bucket -> (TopHits) bucket.getAggregations().get("versions"))
                .map(TopHits::getHits)
                .map(searchHits -> searchHits.getAt(0))
                .map(this::buildSearchResponseObject)
                .skip(from)
                .limit(size)
                .collect(toList());
*/        
        List<String> objects = new ArrayList<String>();
        for( Terms.Bucket bucket : buckets) {
            TopHits ths = (TopHits) bucket.getAggregations().get("versions");
            SearchHits shs =  ths.getHits();
                log.debug("HERE!!!!");
            for(SearchHit sh : shs) {
                String s = buildSearchResponseObject(sh);
                objects.add(s);
                log.debug("bucket " + bucket.getKey());
                log.debug("hit " + sh.sourceAsString());
            }
        }

        return buildResponseForObjects(objects);
    }

    /**
     * Builds a search response containing the specified objects.
     */
    public ConanSearchResponse buildResponseForObjects(final List<String> results) {
        ConanSearchResponse response = new ConanSearchResponse();
        response.setResults(results);
        return response;
    }

    /**
     * Builds a single package's search response object based on a provided
     * search hit.
     */
    private String buildSearchResponseObject(final SearchHit searchHit) {
        Map<String,Object> objs = searchHit.getSource();
        StringBuilder sb = new StringBuilder();
        sb.append(objs.get("name"));
        sb.append("/");
        sb.append(objs.get("version"));
        Map<String,Object> attrs = (Map<String,Object>)objs.get("attributes");
        if(objs.get("group") != null && attrs.get("state") != null) {
            sb.append("@");
            sb.append(objs.get("group"));
            sb.append("/");
            sb.append(attrs.get("state"));
        }
        for(String s : attrs.keySet())
            log.debug(s);
        log.debug(sb.toString());
        return sb.toString();
    }
}
