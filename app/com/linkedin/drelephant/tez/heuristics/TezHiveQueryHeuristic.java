/*
 * Copyright 2017 Electronic Arts Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.linkedin.drelephant.tez.heuristics;

import com.linkedin.drelephant.analysis.Heuristic;
import com.linkedin.drelephant.analysis.HeuristicResult;
import com.linkedin.drelephant.analysis.Severity;
import com.linkedin.drelephant.configurations.heuristic.HeuristicConfigurationData;
import com.linkedin.drelephant.tez.data.TezApplicationData;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Analyzes hive query info
 */
public class TezHiveQueryHeuristic implements Heuristic<TezApplicationData> {

    /**
     * hive conf
     */
    private static final String HIVE_QUERY_ID_CONF = "hive.query.id";
    private static final String HIVE_QUERY_STRING = "hive.query.string";

    private HeuristicConfigurationData _heuristicConfData;

    public TezHiveQueryHeuristic(HeuristicConfigurationData heuristicConfData) {
        this._heuristicConfData = heuristicConfData;
    }

    public HeuristicConfigurationData getHeuristicConfData() {
        return _heuristicConfData;
    }

    public HeuristicResult apply(TezApplicationData data) {
        if (!data.getSucceeded()) {
            return null;
        }

        String queryId = data.getConf().getProperty(HIVE_QUERY_ID_CONF);
        if (queryId == null) {
            return null;
        }

        String queryString = data.getConf().getProperty(HIVE_QUERY_STRING);

        if (queryString == null) {
            return null;
        }

        queryString = decodeQueryString(queryString);
        Severity severity = Severity.NONE;
        HeuristicResult result = new HeuristicResult(_heuristicConfData.getClassName(),
                _heuristicConfData.getHeuristicName(), severity, 0);

        result.addResultDetail("Hive Query String", queryId, queryString);

        return result;

    }

    private String decodeQueryString(String queryString) {
        try {
            return URLDecoder.decode(queryString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return queryString;
        }
    }
}
