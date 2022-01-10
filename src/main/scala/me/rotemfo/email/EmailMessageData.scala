package me.rotemfo.email

/**
  * project: scala-playground
  * package: me.rotemfo.email
  * file:    EmailMessageData
  * created: 2019-05-19
  * author:  rotem
  */
case class EmailMessageData(messageId: String,
                            from: (String, String), // (email -> name)
                            to: Seq[String],
                            subject: String,
                            message: String,
                            cc: Seq[String] = Seq.empty,
                            bcc: Seq[String] = Seq.empty,
                            richMessage: Option[String] = None,
                            attachment: Option[java.io.File] = None)

