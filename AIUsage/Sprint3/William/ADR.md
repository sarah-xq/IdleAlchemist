# ADR Log: AI Session (Navigation, Upgrades List, Extra Hand)

**Author:** William D Fraser
**AI used:** Claude (Claude Code)
**Dates:** 2026-11-09 to 2026-11-16
**Full transcript:** [Claude_Transcript_Navigation_and_Upgrades.pdf](Claude_Transcript_Navigation_and_Upgrades.pdf)

This log records the decisions made from each prompt in the transcript, and who wrote the code for each one.

| # | Prompt (short) | Outcome | Code written by |
|---|----------------|---------|-----------------|
| 1 | Add my Achievements and Shop screens to the navigation | Accepted, implemented | Claude, in the project |
| 2 | Ideas for the milestone | Proposed (ideas only) | No code |
| 3 | How to put the upgrade buttons in a lazy list (no code) | Accepted (approach) | No code |
| 4 | Help finish the LazyColumn, show the code in chat | Accepted | Claude showed it in chat, William pasted and adapted it |
| 5 | Steps and files to create a new upgrade | Accepted (guide) | William, following the guide |
| 6 | What error am I currently having | Diagnosed | William fixes them |
| 7 | Extra Hand multiplies the click | Accepted | William, with Claude's corrections |
| 8 | Save a PDF of the transcript | Done | Claude made the PDF |

---

## ADR-01: Achievements, Shop and Contact in the bottom navigation

**Prompt:** "can we have the navigation also have my screens such as achievements and the shop"

**Context**
- After the merge, the app uses my partner's navigation (Create, Trail, Grimoire, Information). My old navigation, with Achievements, Shop and Contact, was no longer reachable.
- Each trail stage now has its own clicker game, so Achievements and the Shop had to decide which game they read.

**Decision**
- Add Achievements, Shop and Contact (with its sent-message screens) as new routes in `AppRoute.kt`, and add them to the bottom bar, which now has 7 tabs.
- Achievements belong to the alchemist profile and count the stats of every stage added together (`ProfileProgress.kt`).
- Shop potions boost the stage the player played most recently, or stage 1 before any stage is played. The shop header names that stage.
- Only the selected tab shows its label, because 7 labels do not fit on a phone.
- Sent messages moved into `TrailProgressViewModel`, so Contact works without my old navigation.

**Consequences**
- All of my screens are reachable again and work with the stage system.
- Achievement awarding moved out of the shared game rules (`GameRules.kt`), so stages do not each award their own achievements.
- 3 new tests: achievements adding up across stages, a deleted achievement being earned again, potions going to the last stage played. All 11 tests passed on desktop and Android.
- My old navigation files are no longer used. Claude could not delete them (blocked by a permission check), so they are still in the project and still compile.
- Progress and messages are still lost when the app is fully closed.

**AI involvement:** Claude wrote and tested this code directly in the project, and checked it on the emulator.

---

## ADR-02: Milestone feature ideas

**Prompt:** "give me ideas on what to add into the milestone"

**Context:** The repo does not contain the milestone requirements, so the ideas came from the app's current gaps and from the README and `AppDecisionContext.md`.

**Decision (proposed)**
- Top three: save progress to the device, 1 to 3 star ratings per stage, and actually give out the stage rewards.
- Other ideas: new goal types, biomes/worlds, using the alchemist tiers, earnings while away, stage achievements, a completion animation, updating the Codex text, a reset-progress option.
- Quality work: bottom bar accessibility, Compose UI tests, removing the old navigation files, running the tests in CI.
- Suggested split: one person on saving to the device plus star ratings, the other on stage rewards plus new goal types.

**Consequences:** Nothing is decided yet. These need to be checked against the milestone rubric and agreed with my teammate.

**AI involvement:** Ideas only, no code.

---

## ADR-03: Approach for scrolling upgrade buttons

**Prompt:** "DO NOT CODE, tell me how to make my clicker buttons in the game be in a lazylist so that when I add more I can scroll. I want them to disappear if they are below the screen or above a certain height so it doesn't cloud the actual cauldron to click"

**Context:** The two upgrade cards were hard-coded under the cauldron. Adding more would push the layout down or cover the cauldron.

**Decision**
- Put the upgrades in their own `LazyColumn` with a limited height below the cauldron. A `LazyColumn` clips its content, so cards disappear past its edges and never cover the cauldron.
- Describe each upgrade as data (id, name, icon, cost, level, label, buy action), and show one list item per upgrade, using the id as the key.
- Keep the "UPGRADES" label outside the list.

**Consequences**
- New upgrades become one more entry in a list.
- Things to watch for: the list needs a limited height, and its parent must not also scroll.
- Optional later: fade the list edges, and a side-by-side layout on wide screens.

**AI involvement:** Explanation only, no code, as requested.

---

## ADR-04: LazyColumn for the upgrades

**Prompt:** "help me finish adding the lazy column but dont directly put the code inside the app, instead show me the code in this chat and ill copy/paste"

**Context:** My first attempt did not compile. `val upgrades = listOf({})` had no `id` for the key, `items` was not imported, and both cards were inside a single item.

**Decision**
- Use a data class for each upgrade, plus a function that builds the list from the `GameState`.
- `ClickerPage` shows a `LazyColumn` capped at about two cards high (`heightIn(max = ...)`), with `items(upgrades, key = { it.id })` and `spacedBy(10.dp)` spacing.
- The cauldron (`ClickArea`, `weight(1f)`) gets the rest of the screen.

**Consequences:** The upgrade list scrolls, and adding an upgrade only needs a new list entry plus its game logic. I put the data class and list builder in `ClickerPage.kt` instead of `Upgrades.kt`.

**AI involvement:** Claude showed the code in chat. I pasted and adapted it myself; Claude did not edit the project.

---

## ADR-05: Steps for adding a new upgrade

**Prompt:** "what are the steps to create a new upgrade, tell me the files I need to change"

**Context:** I had started an "Extra Hand" upgrade and needed to know which files it touches.

**Decision:** Every new upgrade follows the same 8 steps:

1. `model/GameState.kt`: count and cost fields, plus the effect.
2. `model/GameRules.kt`: the buy rule (`afterBuyExtraHand`).
3. `data/TrailProgressRepository.kt`: `buyExtraHand(profileId, stageNumber)`.
4. `data/TrailProgressViewModel.kt`: pass-through.
5. `feature/stage/StageRoute.kt`: connect the button to the view model.
6. `feature/stage/StageScreen.kt`: pass the callback down.
7. `ui/screens/ClickerPage.kt`: parameter and list entry.
8. `data/GameRepository.kt`: fix or remove the old line.

Optional: an icon, an achievement, and a test.

**Consequences:** A repeatable checklist for future upgrades.

**AI involvement:** Guidance and example snippets in chat. I am writing the code.

---

## ADR-06: Fixing the current build errors

**Prompt:** "what is the error I'm currently having"

**Context:** The project stopped building partway through adding Extra Hand.

**Decision**

Claude compiled the project and found 2 compile errors:
- `ClickerPage.kt:81`: the `upgrade()` function cannot see `onBuyExtraHand`, so it needs its own parameter.
- `AppNavigation.kt:63`: `GameViewModel` has no `buyExtraHand`, so add it (or remove the call).

It also found 2 logic bugs:
- `clickGain` multiplied by an Extra Hand count that started at 0, so taps gave 0 points.
- `afterBuyExtraHand` checked `extraClickCost` instead of `extraHandCost`, so points could go negative.

**Consequences:** Once these four are fixed, the build compiles.

**AI involvement:** Diagnosis only. Claude did not change any files.

---

## ADR-07: Extra Hand multiplies the click value

**Prompt:** "no extra hand will multiply click (extra click is at 4, extra hand is at 2 so total per click is 8)"

**Context:** Claude had suggested making Extra Hand work per second, but I want it to multiply each tap.

**Decision**
- Keep `clickGain = clickPower * clickMultiplier() * extraHandCount`.
- Start `extraHandCount` at 1 instead of 0, the same way `clickPower` starts at 1, so the multiplier is ×1 before any purchase.
- The card shows "x{count} per tap", with the badge at `count - 1`.
- Apprentices are not affected, and potions still stack on top (4 × 2 × 2 = 16 with Double Tap).

**Consequences**
- Matches my example: Stronger Stir at 4 × Extra Hand at 2 = 8 per tap.
- Multipliers grow quickly, so the price growth (`GrowthFactorExtraHand`) or starting cost may need balancing.
- The earlier fixes from ADR-06 (price check, `upgrade()` parameter, `GameViewModel.buyExtraHand`) still apply.

**AI involvement:** Corrections and snippets in chat. I am writing the code.

---

## ADR-08: Documenting the AI session

**Prompt:** "save a PDF of the entire transcript (prompt and code)"

**Context:** Our AI policy (`Markdown_Files/AIPolicy.md`) asks for a PDF or copy of the transcript, and an ADR.

**Decision:** Save the transcript, from the navigation prompt onward, as a PDF in `AIUsage/Sprint2/William/`, with this ADR log next to it.

**Consequences:** This session's AI usage is documented as the policy requires.

**AI involvement:** Claude made the PDF and this ADR log.

## Reflection

AI helped me add to code I wrote that I struggled with, otherwise it showed me how my functions worked and how to add on to them / create similar ones.