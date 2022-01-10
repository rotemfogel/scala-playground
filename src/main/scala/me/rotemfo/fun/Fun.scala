package me.rotemfo.fun

object Fun {
  /**
    * default always run
    *
    * @return True
    */
  private final def alwaysRun(): Boolean = true

  /**
    * should run ? based on an input function
    *
    * @param f function that returns Boolean
    * @return Boolean
    */
  def shouldRun(f: () => Boolean = alwaysRun): Boolean = {
    f()
  }
}

object Main {

  /**
    * might run depending on the hour
    *
    * @param hour - the input hour
    * @return Boolean
    */
  def mightRun(hour: Int): Boolean = {
    hour match {
      // if hours 00:00 to 02:00
      case x if 0 to 2 contains x => false
      case _ => true
    }
  }

  def main(args: Array[String]): Unit = {
    // uses default always run
    assert(Fun.shouldRun())

    // loop over hours and assert the response
    for (hour <- 0 to 5) {
      // create a function call
      def f(): Boolean = mightRun(hour)

      val assertion: Boolean = if (hour < 3) false else true
      // pass the function call
      assert(Fun.shouldRun(f) == assertion)
      println(s"shouldRun($hour) yielded $assertion")
    }
  }
}