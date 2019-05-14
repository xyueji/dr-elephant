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
package com.linkedin.drelephant.tony.data;

import com.linkedin.tony.events.Metric;
import java.util.List;


public class TonyTaskData {
  private final String _taskType;
  private final int _taskIndex;

  private long _taskStartTime;
  private long _taskEndTime;
  private List<Metric> _metrics;

  /**
   * Creates a new {@code TonyTaskData} encapsulating a task's data, such as task type, index, start time,
   * end time, and metrics.
   * @param taskType  the task type
   * @param taskIndex  the task index
   */
  public TonyTaskData(String taskType, int taskIndex) {
    _taskType = taskType;
    _taskIndex = taskIndex;
  }

  /**
   * Get task start time.
   * @return  the task start time
   */
  public long getTaskStartTime() {
    return _taskStartTime;
  }

  /**
   * Set task start time.
   * @param taskStartTime  the task start time
   */
  public void setTaskStartTime(long taskStartTime) {
    _taskStartTime = taskStartTime;
  }

  /**
   * Get task end time.
   * @return  the task end time
   */
  public long getTaskEndTime() {
    return _taskEndTime;
  }

  /**
   * Set task end time.
   * @param taskEndTime  the end time
   */
  public void setTaskEndTime(long taskEndTime) {
    _taskEndTime = taskEndTime;
  }

  /**
   * Get the metrics for this task.
   * @return  the metrics
   */
  public List<Metric> getMetrics() {
    return _metrics;
  }

  /**
   * Sets the metrics for this task.
   * @param metrics  the metrics
   */
  public void setMetrics(List<Metric> metrics) {
    _metrics = metrics;
  }
}
