package com.example.idlealchemist.platform

// Written with Claude (AI): Assignment 4 Part B, complex approach.

/** Desktop: operating system and Java version. */
actual fun platformDetails(): String =
    "${System.getProperty("os.name")} ${System.getProperty("os.version")}, Java ${System.getProperty("java.version")}"
