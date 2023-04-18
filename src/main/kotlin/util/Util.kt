package util

import java.time.LocalTime

fun log(message: String) =  println("${LocalTime.now()} *** $message ***")
