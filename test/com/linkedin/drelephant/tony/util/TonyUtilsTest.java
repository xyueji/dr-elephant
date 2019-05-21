package com.linkedin.drelephant.tony.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.linkedin.drelephant.tony.data.TonyTaskData;
import com.linkedin.tony.Constants;
import com.linkedin.tony.events.Metric;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Assert;
import org.junit.Test;


public class TonyUtilsTest {
  /**
   * Worker 0 is missing metrics, but worker 1 has metrics; we should use worker 1's
   * max memory metrics.
   */
  @Test
  public void testGetMaxMemorySomeTasksMissingMetrics() {
    Map<Integer, TonyTaskData> taskDataMap = new TreeMap<>();
    TonyTaskData worker0Data = new TonyTaskData("worker", 0);
    TonyTaskData worker1Data = new TonyTaskData("worker", 1);
    double worker1MaxMemoryBytes = 123d;
    worker1Data.setMetrics(ImmutableList.of(new Metric(Constants.MAX_MEMORY_BYTES, worker1MaxMemoryBytes)));

    taskDataMap.put(0, worker0Data);
    taskDataMap.put(1, worker1Data);

    Assert.assertEquals(worker1MaxMemoryBytes,
        TonyUtils.getMaxMemoryBytesUsedForTaskType(ImmutableMap.of("worker", taskDataMap), "worker"), 0);
  }
}
