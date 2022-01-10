package me.rotemfo.database.util

/**
  * project: acmesecurity
  * package: com.acmesecurity.common.util
  * file:    EnvUtils
  * created: 2018-07-30
  * author:  rotem
  */
object EnvUtils {
  private val env = sys.env

  /**
    * look up a property in system environment variables and in system properties
    *
    * @param property the property key to lookup
    * @return property value
    */
  def envOrSysProp(property: String, default: String): String = {
    try {
      env(property)
    } catch {
      case _: NoSuchElementException | _: NullPointerException =>
        /*
         * for testing purposes - inject USER/PASSWORD to system properties
         * since sys.env is of UnModifiableMap type
         */
        System.getProperties.getProperty(property, default)
    }
  }

  def envOrSysProp(property: String): String = {
    envOrSysProp(property, "")
  }
}
