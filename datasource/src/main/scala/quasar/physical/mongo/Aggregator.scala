/*
 * Copyright 2014–2018 SlamData Inc.
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

import cats.syntax.foldable._
import cats.instances.list._

import org.bson.BsonValue
import org.mongodb.scala._

import monocle.{Iso, PTraversal, Prism}

import quasar.common.{CPath, CPathField, CPathNode}

trait Aggregator {
  def toDocument: Document
}

object Aggregator {
  final case class ReplaceRootWithList(rootField: String, prjs: MongoConstruct) extends Aggregator {
    def toDocument: Document = Document("$$replaceRoot" -> prjs.toBsonValue)
  }

  final case class Project(obj: MongoExpression.MongoObject) extends Aggregator {
    def toDocument: Document = Document("$$project" -> obj.toBsonValue)
  }

  final case class AddFields(obj: MongoExpression.MongoObject) extends Aggregator {
    def toDocument: Document = Document("$$addFields" -> obj.toBsonValue)
  }

  def replaceRootWithList: Prism[Aggregator, (String, MongoConstruct)] =
    Prism.partial[Aggregator, (String, MongoConstruct)] {
      case ReplaceRootWithList(r, p) => (r, p)
    } { case (r, p) => ReplaceRootWithList(r, p) }

  def project: Prism[Aggregator, MongoExpression.MongoObject] =
    Prism.partial[Aggregator, MongoExpression.MongoObject] {
      case Project(obj) => obj
    } (x => Project(x))

  def addFields: Prism[Aggregator, MongoExpression.MongoObject] =
    Prism.partial[Aggregator, MongoExpression.MongoObject] {
      case AddFields(obj) => obj
    } (x => AddFields(x))
}