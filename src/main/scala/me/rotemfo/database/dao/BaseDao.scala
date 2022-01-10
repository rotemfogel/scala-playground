package me.rotemfo.database.dao

import scala.concurrent.Future

/**
  * project: scala-playground
  * package: me.rotemfo.database.dao
  * file:    BaseDao
  * created: 2019-06-15
  * author:  rotem
  */
trait BaseDao[A] {
  /**
    * create table for object persistence
    *
    * @return nada
    */
  def createTable(): Future[Unit]

  /**
    * insert a new entity
    *
    * @param o - the entity
    * @return Future[Unit]
    */
  def save(o: A): Future[Option[Long]]

  /**
    * insert multiple entities
    *
    * @param os - list of entities
    * @return [[Seq[K]]]
    */
  def save(os: Seq[A]): Future[Seq[Long]]
}

trait BaseEntityDao[K, A] extends BaseDao[A] {
  /**
    * get all entity types
    *
    * @param limit - limit returned list size (default unlimited)
    * @return [[A]]
    */
  def all(limit: Int = -1): Future[Seq[A]]

  /**
    * get a single entity by key
    *
    * @param id - entity key
    * @return [[A]]
    */
  def findById(id: K): Future[Option[A]]

  /**
    * update existing entity
    *
    * @param o - the entity
    * @return [[Int]]
    */
  def update(o: A): Future[Int]

  /**
    * update many entities
    *
    * @param os - list of entities
    * @return [[Unit]]
    */
  def update(os: Seq[A]): Future[Seq[Int]]

  /**
    * delete existing entity
    *
    * @param id - the entity
    * @return [[Int]]
    */
  def delete(id: K): Future[Int]

  /**
    * delete existing entity
    *
    * @param ids - the entity
    * @return [[Int]]
    */
  def delete(ids: Seq[K]): Future[Int]
}

trait BaseNamedEntityDao[K, A] extends BaseEntityDao[K, A] {
  /**
    * get element by name
    *
    * @param name - the element name
    * @return Option of element
    */
  def findByName(name: String): Future[Option[A]]
}