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
import static java.util.Collections.singletonList;
import javax.inject.Inject;
import javax.inject.Named;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.sonatype.nexus.repository.FacetSupport;
import org.sonatype.nexus.repository.search.SearchService;
import org.sonatype.nexus.repository.view.Content;
import org.sonatype.nexus.repository.view.ContentTypes;
import org.sonatype.nexus.repository.view.Parameters;
import org.sonatype.nexus.repository.view.payloads.StringPayload;

@Named
public class ConanSearchFacetHosted extends FacetSupport implements ConanSearchFacet {

    private final SearchService searchService;
    private final ConanSearchParameterExtractor conanSearchParameterExtractor;
    private final ConanSearchResponseFactory conanSearchResponseFactory;
    private final ConanSearchResponseMapper conanSearchResponseMapper;

    @Inject
    public ConanSearchFacetHosted(final SearchService searchService,
            final ConanSearchResponseFactory conanSearchResponseFactory,
            final ConanSearchResponseMapper conanSearchResponseMapper,
            final ConanSearchParameterExtractor conanSearchParameterExtractor) {
        this.conanSearchResponseFactory = conanSearchResponseFactory;
        this.searchService = searchService;
        this.conanSearchResponseMapper = conanSearchResponseMapper;
        this.conanSearchParameterExtractor = conanSearchParameterExtractor;
    }

    public Content searchV1(final Parameters parameters) throws IOException {
        String q = conanSearchParameterExtractor.extractQ(parameters);
        
        ConanSearchResponse response;
        
        if (q.isEmpty()) {
            response = conanSearchResponseFactory.buildEmptyResponse();
        } else {
            QueryStringQueryBuilder query = QueryBuilders.queryStringQuery(q)
                    .allowLeadingWildcard(true)
                    .analyzeWildcard(true);
            TermsBuilder terms = AggregationBuilders.terms("name")
                    .field("name")
                    .size(19)
                    .subAggregation(AggregationBuilders.topHits("versions")
                            .setTrackScores(true)
                            .setSize(19)
                    );
            SearchResponse searchResponse = searchService.searchInReposWithAggregations(query,
                    singletonList(terms),
                    singletonList(getRepository().getName()));
            Aggregations aggregations = searchResponse.getAggregations();
            Terms nameTerms = aggregations.get("name");

            response = conanSearchResponseFactory.buildResponseForResults(nameTerms.getBuckets(), 20, 0);
        }

        String content = conanSearchResponseMapper.writeString(response);
        return new Content(new StringPayload(content, ContentTypes.APPLICATION_JSON));
    }
}
