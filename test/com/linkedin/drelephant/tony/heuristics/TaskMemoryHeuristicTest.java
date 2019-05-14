/*
 * Copyright 2019 LinkedIn Corp.
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
 */
package com.linkedin.drelephant.tony.heuristics;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.drelephant.analysis.ApplicationType;
import com.linkedin.drelephant.analysis.HeuristicResult;
import com.linkedin.drelephant.analysis.Severity;
import com.linkedin.drelephant.configurations.heuristic.HeuristicConfigurationData;
import com.linkedin.drelephant.tony.data.TonyApplicationData;
import com.linkedin.tony.Constants;
import com.linkedin.tony.TonyConfigurationKeys;
import com.linkedin.tony.events.Event;
import com.linkedin.tony.events.EventType;
import com.linkedin.tony.events.Metric;
import com.linkedin.tony.events.TaskFinished;
import com.linkedin.tony.rpc.impl.TaskStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.junit.Assert;
import org.junit.Test;


public class TaskMemoryHeuristicTest {

  /**
   * 3g workers requested, max worker memory < 50%
   */
  @Test
  public void testCritical() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
          1.2e9,
          1.1e9,
          1e9,
          1.3e9
        }, Constants.PS_JOB_NAME, new double[]{0.5e9}),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "3g", Constants.PS_JOB_NAME, "2g"),
        Severity.CRITICAL
    );
  }

  /**
   * 3g ps requested, max ps memory < 60%
   */
  @Test
  public void testSevere() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
            1.5e9,
            1.6e9,
        }, Constants.PS_JOB_NAME, new double[]{1.84e9}),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "2g", Constants.PS_JOB_NAME, "3g"),
        Severity.SEVERE
    );
  }

  /**
   * 3g workers requested, max worker memory < 70%
   */
  @Test
  public void testModerate() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
            2.14e9,
            2e9,
        }),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "3g"),
        Severity.MODERATE
    );
  }

  /**
   * 3g workers requested, max worker memory < 80%
   */
  @Test
  public void testLow() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
            2e9,
            2.45e9,
        }),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "3g"),
        Severity.LOW
    );
  }

  /**
   * 3g workers requested, max worker memory > 80%
   */
  @Test
  public void testNone() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
            2.5e9,
            2.6e9,
        }),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "3g"),
        Severity.NONE
    );
  }

  /**
   * Low memory utilization but default container size, so pass.
   */
  @Test
  public void testLowUtilizationDefaultContainerSize() {
    testHelper(
        ImmutableMap.of(Constants.WORKER_JOB_NAME, new double[]{
            0.5e9,
            0.6e9,
        }),
        ImmutableMap.of(Constants.WORKER_JOB_NAME, "2g"),
        Severity.NONE
    );
  }

  public void testHelper(Map<String, double[]> memUsed, Map<String, String> memRequested, Severity expectedSeverity) {
    Configuration conf = new Configuration(false);
    List<Event> events = new ArrayList<>();
    for (Map.Entry<String, String> entry : memRequested.entrySet()) {
      String taskType = entry.getKey();
      conf.set(TonyConfigurationKeys.getResourceKey(taskType, Constants.MEMORY), entry.getValue());
      conf.setInt(TonyConfigurationKeys.getInstancesKey(taskType), memUsed.get(taskType).length);

      for (int i = 0; i < memUsed.get(taskType).length; i++) {
        events.add(new Event(EventType.TASK_FINISHED,
            new TaskFinished(taskType, i, TaskStatus.SUCCEEDED.toString(),
                ImmutableList.of(new Metric(Constants.MAX_MEMORY_BYTES, memUsed.get(taskType)[i]))),
            System.currentTimeMillis()));
      }
    }

    ApplicationType appType = new ApplicationType(Constants.APP_TYPE);
    TonyApplicationData data = new TonyApplicationData("application_123_456", appType, conf, events);

    TaskMemoryHeuristic heuristic = new TaskMemoryHeuristic(new HeuristicConfigurationData("ignored",
        "ignored", "ignored", appType, Collections.EMPTY_MAP));
    HeuristicResult result = heuristic.apply(data);
    Assert.assertEquals(expectedSeverity, result.getSeverity());
  }
}
