/*
 * Copyright 2016 LinkedIn Corp.
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

package org.apache.spark.deploy.history

import com.linkedin.drelephant.spark.data.SparkApplicationData
import com.linkedin.drelephant.spark.legacydata.LegacyDataConverters
import org.scalatest.{FunSpec, Matchers}

import java.io.BufferedInputStream

class Spark3DataCollectionTest extends FunSpec with Matchers {

  val eventLogDir = "spark_event_logs/"

  describe("SparkDataCollection") {
    val dataCollection = new SparkDataCollection

    val in = new BufferedInputStream(classOf[Spark3DataCollectionTest].getClassLoader
      .getResourceAsStream(eventLogDir + "application_1693387421105_0019_1.txt"))
    dataCollection.load(in, in.toString)
    in.close()

    val sparkApplicationData: SparkApplicationData = LegacyDataConverters.convert(dataCollection)
    sparkApplicationData.jobDatas.size should be(1)
  }
}


