package me.rotemfo.aws.s3

import com.amazonaws.auth.profile.ProfileCredentialsProvider
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.CopyObjectRequest
import me.rotemfo.aws.s3.AWSUtil._

import scala.collection.JavaConverters._

/**
  * project: scala-playground
  * package: me.rotemfo.aws.s3
  * file:    S3Main
  * created: 2018-12-30
  * author:  rotem
  */
object S3Main extends App {
  private final val VERSION: String = "version"
  private final val PREV_VERSION: String = "pversion"
  private final val KEY: String = "key"
  private final val PREV_KEY: String = "pkey"
  private final val envs = Array(/*"integration", "production", */ "staging")
  private final val bucketTemplate = "metastore-[ENVIRONMENT]-us-east-1"

  envs.foreach(env => {
    val client = AmazonS3ClientBuilder
      .standard()
      .withRegion(Regions.US_EAST_1)
      .withCredentials(new ProfileCredentialsProvider(env))
      .build
    val bucket = bucketTemplate.replace("[ENVIRONMENT]", env)
    val listing = client.listObjects(bucket, "metastore/acme").getObjectSummaries.asScala
    listing.foreach(l => {
      val k = l.getKey
      val md = client.getObjectMetadata(bucket, k)
      var v = try {
        md.getUserMetadata.get(VERSION).toInt
      } catch {
        case _: Throwable => try {
          md.getUserMetadata.get(VERSION).replaceAll("\\.", "").toInt
        } catch {
          case _: Throwable =>
            val s = try {
              getJarVersion(md.getUserMetadata.get(KEY))
            } catch {
              case _: Throwable => getJarVersion(k)
            }
            s.replaceAll("\\.", "").toInt
        }
      }
      if (v < 200)
        v = v % 100
      else if (v < 2000)
        v = v % 1000
      else
        v = v % 10000
      if (md.getUserMetadata.size() < 4) {
        val version = s"1.0.$v"
        val key = if (k.contains("-latest")) k.replace("-latest", s".$v") else k.replace("latest", v.toString)
        val pv = v - 1
        val prevversion = s"1.0.$pv"
        val prevkey = key.replace(version, prevversion)
        println(Array(l.getKey, s"key=$key", s"version=$version", s"prevkey=$prevkey", s"prevversion=$prevversion").mkString(","))
        md.addUserMetadata(KEY, key)
        md.addUserMetadata(PREV_KEY, prevkey)
        md.addUserMetadata(VERSION, version)
        md.addUserMetadata(PREV_VERSION, prevversion)
        val request = new CopyObjectRequest(bucket, k, bucket, k).withNewObjectMetadata(md)
        client.copyObject(request)
      }
    })
    client.shutdown()
  })
}
