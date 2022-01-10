package me.rotemfo.builder

// @formatter:off
sealed trait Method
case object GET extends Method
case object POST extends Method
case object PUT extends Method
case object DELETE extends Method
case object PATCH extends Method
// @formatter:on

case class HttpClientRequest(endpoint: String, method: Method, header: Map[String, String], body: Option[String])

// @formatter:off
case class HttpClientRequestBuilder(endpoint: String, method: Method = GET, header: Map[String, String] = Map.empty[String, String], body: String = "") {
  def withEndPoint(endpoint: String): HttpClientRequestBuilder = copy(endpoint = endpoint)
  def withMethod(method: Method): HttpClientRequestBuilder = copy(method = method)
  def withHeader(headers: Map[String, String]): HttpClientRequestBuilder = copy(header = headers)
  def withBody(body: String): HttpClientRequestBuilder = copy(body = body)
  def build: HttpClientRequest = HttpClientRequest(endpoint, method, header, Option(body))
}
// @formatter:on

object HttpClientRequest {
  def builder(): HttpClientRequestBuilder = HttpClientRequestBuilder("")
}

object HttpClient {
  def send(request: HttpClientRequest): Unit = {
    println(request)
  }
}

object Main extends App {
  val postClientRequest =
    HttpClientRequest
      .builder()
      .withEndPoint("https://someURL.com")
      .withMethod(POST)
      .withHeader(Map("content-type" -> "application/json"))
      .withBody("{\"json\":\"body\"}")
      .build

  HttpClient.send(postClientRequest)
}