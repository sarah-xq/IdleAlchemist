package com.example.idlealchemist

import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.deeplink.DeepLinkMatcher
import androidx.navigation3.runtime.deeplink.DeepLinkRequest
import androidx.navigation3.runtime.deeplink.DeepLinkUri
import androidx.navigation3.runtime.deeplink.UriDeepLinkMatcher
import com.example.idlealchemist.navigation.AppRoute
import kotlinx.serialization.serializer

class RouterDeepLinkResolver {
    // Map urls to your existing keys
    private val aboutMatcher: DeepLinkMatcher<*, *> = UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("example://compose.deeplink/about"),
        serializer = serializer<AppRoute.AcademyCodex>()
    )
    private val boardingMatcher: DeepLinkMatcher<*, *> = UriDeepLinkMatcher(
        uriPattern = DeepLinkUri("example://compose.deeplink/start"),
        serializer = serializer<AppRoute.Onboarding>()
    )
    private val matchers = listOf(aboutMatcher, boardingMatcher)
    fun resolve(uriString: String): List<NavKey>? {
        val request = DeepLinkRequest(uri = uriString)
        for (matcher in matchers) {
            val matchResult = matcher.match(request)
            if (matchResult != null) {
                // Safe cast: only proceeds if the matched key is a valid NavKey
                val targetKey = matchResult?.key as? NavKey
                // Build the synthetic backstack directly in Kotlin without unsafe casts
                if (targetKey != null) {
                    return if (targetKey == AppRoute.Onboarding) {
                        listOf(AppRoute.Onboarding)
                    } else {
                        listOf(AppRoute.Onboarding, targetKey)
                    }
                }
            }
        }
        return null
    }
}
