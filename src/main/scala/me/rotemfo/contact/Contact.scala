package me.rotemfo.contact

case class Contact(name: String, phoneNumber: List[String], email: Option[String])

object ContactApp extends App {
  val alice = Contact("Alice", List("054-4444444"), Some("alice@acme.com"))
  val bob = Contact("Bob", List("054-5555555"), Some("bob@acme.com"))
  val charlie = Contact("Charlie", List("054-8888888"), Some("charlie@acme.com"))
  val david = Contact("David", List("058-9999999"), Some("david@acme.com"))
  val contacts = List(alice, bob, charlie, david)

  val namesAndPHones =
    for {
      contact <- contacts
      phoneNumber <- contact.phoneNumber if phoneNumber.startsWith("054")
    }
    yield (contact.name, phoneNumber)

  println(namesAndPHones)
}