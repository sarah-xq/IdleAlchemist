package com.example.idlealchemist.util

/** Turns a price in cents into display text: 199 becomes "$1.99". */
fun formatPrice(cents: Int): String {
    val remainder = (cents % 100).toString().padStart(2, '0')
    return "$${cents / 100}.$remainder"
}
