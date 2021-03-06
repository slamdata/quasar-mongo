/*
 * Copyright 2020 Precog Data
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package quasar.physical.mongo

import slamdata.Predef._

import argonaut._

import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class SSLConfigSpec extends Specification with ScalaCheck {
  "json codec is lawful"  >> prop { params: (Option[String], Option[String], Boolean, Option[String]) =>
    CodecJson.codecLaw(SSLConfig.codecSSLConfig)(SSLConfig(params._1, params._2, params._3, params._4))
  }
}
