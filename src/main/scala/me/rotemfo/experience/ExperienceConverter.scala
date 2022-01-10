package me.rotemfo.experience

import scala.io.Source

object ExperienceConverter extends App {

  private val camelRegex = "[A-Z\\d]".r

  def camelToSnake(name: String) = camelRegex.replaceAllIn(name, { m =>
    "_" + m.group(0).toLowerCase()
  })

  private val snakeRegex = "_([a-z\\d])".r

  def snakeToCamel(name: String) = snakeRegex.replaceAllIn(name, { m =>
    m.group(1).toUpperCase()
  })

  val source = Source.fromInputStream(getClass.getResourceAsStream("/experiences.json"))
  //  val lines = source.getLines().map(l => fromJson[Map[String, Any]](l)).toList
  val lines = source.getLines().toList
  source.close()

  val map = Map(
    "articlemidabtestvariation" -> "articleMidAbTestVariation",
    "mediasales" -> "mediaSales",
    "secondarytickers" -> "secondaryTickers",
    "stockemailalerts" -> "stockEmailAlerts",
    "loggedingroupmpw" -> "loggedInGroupMpw",
    "ispromoadvisoryuser" -> "isPromoAdvisoryUser",
    "followedauthorscount" -> "followedAuthorsCount",
    "ismp" -> "isMp",
    "authorname" -> "authorName",
    "paywallstatus" -> "paywallStatus",
    "newslettercount" -> "newsletterCount",
    "isproplus" -> "isProPlus",
    "pushappvisit" -> "pushAppVisit",
    "userid" -> "userId",
    "pagetype" -> "pageType",
    "hasportfolio" -> "hasPortfolio",
    "ispremiuminactiveapp30days" -> "isPremiuminActiveApp30Days",
    "isdividendaudience" -> "usDividendAudience",
    "mpservicename" -> "mpServiceName",
    "ispremiumnoapp" -> "isPremiumNoApp",
    "templatetype" -> "templateType",
    "pwabtestvariation" -> "pwAbTestVariable",
    "primaryticker" -> "primaryTicker",
    "publishdate" -> "publishDate",
    "stockalertscount" -> "stockAlertsCount",
    "ispremium" -> "isPremium",
    "isfreemparticle" -> "isFreeMpArticle",
    "dayssinceposted" -> "daysSincePosted",
    "userstatus" -> "userStatus",
    "rtavisit" -> "rtaVisit",
    "roadblock" -> "",
    "isqualitycommenter" -> "isQualityCommenter",
    "mpcheckout" -> "mpCheckout",
    "accesslist" -> "accessList"
  )

  val newLines = map.map { case (k, v) => lines.map(_.replaceAll(k, v)) }
  println(newLines)
}
