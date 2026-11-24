package com.example.idlealchemist

import com.example.idlealchemist.navigation.AppRoute
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * Web (Kotlin/Wasm) tests for the Part B launcher -> main app cycle, on the main app's side.
 *
 * The launcher opens a deep link; on web, main.kt hands window.location.href to
 * ExternalUriHandler, whose listener asks RouterDeepLinkResolver which screens to show.
 *
 * Run with: ./gradlew :shared:wasmJsBrowserTest
 */
class LauncherDeepLinkWebTest {

    private val resolver = RouterDeepLinkResolver()
    private val received = mutableListOf<String>()

    /** ExternalUriHandler is a singleton, so clear its listener and cached link between tests. */
    private fun resetHandler() {
        ExternalUriHandler.listener = { }   // consumes anything left in the cache
        ExternalUriHandler.listener = null
        received.clear()
    }

    @BeforeTest
    fun setUp() = resetHandler()

    @AfterTest
    fun tearDown() = resetHandler()

    // ---------- RouterDeepLinkResolver: the links the launcher's buttons open ----------

    @Test
    fun startLinkOpensOnboarding() {
        assertEquals(
            listOf(AppRoute.Onboarding),
            resolver.resolve("example://compose.deeplink/start"),
        )
    }

    @Test
    fun aboutLinkOpensAcademyCodex() {
        val stack = resolver.resolve("example://compose.deeplink/about")

        assertEquals(AppRoute.AcademyCodex, stack?.last())
    }

    @Test
    fun backFromAboutGoesToOnboarding() {
        // Fails until RouterDeepLinkResolver line 34 builds listOf(AppRoute.Onboarding, targetKey).
        assertEquals(
            listOf(AppRoute.Onboarding, AppRoute.AcademyCodex),
            resolver.resolve("example://compose.deeplink/about"),
        )
    }

    @Test
    fun unknownLinkIsIgnored() {
        assertNull(resolver.resolve("example://compose.deeplink/not-a-screen"))
    }

    @Test
    fun linkFromAnotherAppIsIgnored() {
        assertNull(resolver.resolve("otherapp://compose.deeplink/start"))
    }

    // ---------- ExternalUriHandler: how a web link reaches the app ----------

    @Test
    fun linkIsDeliveredToListenerImmediately() {
        ExternalUriHandler.listener = { received += it }

        ExternalUriHandler.onNewUri("example://compose.deeplink/about")

        assertEquals(listOf("example://compose.deeplink/about"), received)
    }

    @Test
    fun linkArrivingOnPageLoadIsDeliveredOnceAppIsReady() {
        // On web, main.kt passes window.location.href before App() has attached its listener.
        ExternalUriHandler.onNewUri("example://compose.deeplink/start")

        ExternalUriHandler.listener = { received += it }

        assertEquals(listOf("example://compose.deeplink/start"), received)
    }

    @Test
    fun cachedLinkIsDeliveredOnlyOnce() {
        ExternalUriHandler.onNewUri("example://compose.deeplink/start")
        ExternalUriHandler.listener = { received += it }

        ExternalUriHandler.listener = { received += "again:$it" }

        assertEquals(listOf("example://compose.deeplink/start"), received)
    }

    @Test
    fun handlerAndResolverTogetherProduceTheScreen() {
        var stack: List<Any>? = null
        ExternalUriHandler.listener = { uri -> stack = resolver.resolve(uri) }

        ExternalUriHandler.onNewUri("example://compose.deeplink/start")

        assertTrue(stack == listOf(AppRoute.Onboarding))
    }
}
