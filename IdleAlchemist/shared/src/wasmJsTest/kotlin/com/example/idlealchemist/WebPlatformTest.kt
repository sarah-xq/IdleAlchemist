package com.example.idlealchemist

import com.example.idlealchemist.model.GameState
import com.example.idlealchemist.model.ProfileDraft
import com.example.idlealchemist.model.afterBuyExtraClick
import com.example.idlealchemist.model.afterTap
import com.example.idlealchemist.model.validateProfileDraft
import com.example.idlealchemist.util.formatCount
import kotlinx.browser.document
import kotlinx.browser.window
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Web (Kotlin/Wasm) tests for Idle Alchemist. These only run in the browser:
 *     ./gradlew :shared:wasmJsBrowserTest
 *
 * They check the web-only actual code, the browser environment, and that shared game logic
 * gives the same answers when compiled to WebAssembly as on Android and desktop.
 */
class WebPlatformTest {

    // ---------- Web actual implementations ----------

    @Test
    fun platformReportsWeb() {
        assertTrue(getPlatform().name.contains("Web"))
    }

    @Test
    fun platformNameIsNotBlank() {
        assertTrue(getPlatform().name.isNotBlank())
    }

    // ---------- Browser environment ----------

    @Test
    fun browserDocumentIsAvailable() {
        // The web app draws Compose into document.body, so it must exist.
        assertNotNull(document.body)
    }

    @Test
    fun pageIsServedOverHttp() {
        assertTrue(window.location.protocol.startsWith("http"))
    }

    // ---------- Shared logic compiled to Wasm ----------

    @Test
    fun tapGivesSamePointsOnWeb() {
        val after = GameState(clickPower = 4, extraHandCount = 2).afterTap()

        assertEquals(8, after.points)
    }

    @Test
    fun upgradeCostRoundsTheSameOnWeb() {
        // 10 + round(10 * 1.15) = 10 + 12 = 22. Checks .5 rounding behaves the same on Wasm.
        val after = GameState(points = 10, extraClickCost = 10).afterBuyExtraClick()

        assertEquals(22, after.extraClickCost)
        assertEquals(0, after.points)
    }

    @Test
    fun formatCountThousandsWorksOnWeb() {
        assertEquals("12.3K", formatCount(12_345))
    }

    @Test
    fun formatCountMillionsWorksOnWeb() {
        assertEquals("1.5M", formatCount(1_500_000))
    }

    // ---------- Profile validation (uses ktor's Url parser, which has a separate web build) ----------

    @Test
    fun validHttpsAvatarUrlIsAcceptedOnWeb() {
        val draft = ProfileDraft(
            alias = "Morgana",
            elementalAffinity = "Fire",
            motto = "From embers, discovery.",
            avatarUrl = "https://example.com/sigil.png",
        )

        assertTrue(validateProfileDraft(draft).isValid)
    }

    @Test
    fun httpAvatarUrlIsRejectedOnWeb() {
        val draft = ProfileDraft(
            alias = "Morgana",
            elementalAffinity = "Fire",
            motto = "From embers, discovery.",
            avatarUrl = "http://example.com/sigil.png",
        )

        val result = validateProfileDraft(draft)

        assertNotNull(result.avatarUrlError)
        assertFalse(result.isValid)
    }
}
