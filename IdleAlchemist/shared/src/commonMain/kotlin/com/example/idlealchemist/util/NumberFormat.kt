package com.example.idlealchemist.util

/** Short display form for big numbers (12.3K, 1.5M). Uses only common Kotlin, no String.format. */
fun formatCount(n: Int): String = when {
    n >= 1_000_000 -> { val t = n / 100_000; "${t / 10}.${t % 10}M" }
    n >= 10_000 -> { val t = n / 100; "${t / 10}.${t % 10}K" }
    else -> n.toString()
}
