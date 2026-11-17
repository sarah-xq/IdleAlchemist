# Prompt Technique Log

Full transcript: [Claude_Transcript_Navigation_and_Upgrades.pdf](Claude_Transcript_Navigation_and_Upgrades.pdf). Decisions: [ADR.md](ADR.md).

## 1. Navigation for Achievements, Shop and Contact

| **Challenge addressed (Context of use)** | After merging with my teammate's code, the app used their navigation, so my Achievements, Shop and Contact screens could no longer be opened. They also had to work with the new level system, where every stage has its own game. |
| **AI(s) used** | Claude (Claude Code) |
| **Techniques used** | Direct prompting, building on context from earlier in the same conversation. |
| **Iterations** | 1 prompt. The AI already knew the project from earlier work, so it didn't need more explanation. |
| **Debugging / refinement** | The AI decided how my screens fit the levels: achievements count every stage together, and shop potions boost the last stage played. It fixed one of its own tests that had the wrong timing. |
| **Failed / rejected approaches** | Showing all 7 tab labels was rejected because they don't fit on a phone, so only the selected tab shows its label. The AI's attempt to delete my old navigation files was blocked by a permission check, so they're still in the project. |
| **Strategy changes** | None. Next time I would list every screen I want and say how achievements and potions should work, instead of "such as", so I make those design choices myself. |
| **Verification** | The AI built the app for Android and desktop, ran the unit tests (11 passing, 3 of them new), and tested it on the emulator by finishing stage 1 and buying a potion. |
| **Code impact** | Code written by the AI: new routes, a 7-tab bottom bar, achievements shared across stages, and potions going to the last stage played. |
| **Time** | 1 Hour |
| **Outcome** | My screens are back in the app and work with my teammate's level system. I still need to delete the old navigation files by hand. |

## 2. Milestone ideas

| **Challenge addressed (Context of use)** | Deciding which features to add for the next milestone. |
| **AI(s) used** | Claude (Claude Code) |
| **Techniques used** | Open-ended brainstorming. |
| **Iterations** | 1 prompt. |
| **Debugging / refinement** | None needed. |
| **Failed / rejected approaches** | None yet. |
| **Strategy changes** | None. Next time I would paste the milestone rubric into the prompt, because the AI didn't know what would be graded. |
| **Verification** | Not verified yet. The ideas still need to be checked against the rubric and agreed with my teammate. |
| **Code impact** | 0% code affected. This was planning only. |
| **Time** | 30 Minutes. |
| **Outcome** | A prioritised list of ideas. The top three were saving progress to the device, star ratings per stage, and giving out stage rewards. It also suggested a way to split the work between my teammate and me. |

## 3. Scrolling list for the upgrade buttons

| **Challenge addressed (Context of use)** | The upgrade buttons were hard-coded under the cauldron. Adding more would cover the cauldron, so I wanted them in a scrolling list that hides cards past its edges. |
| **AI(s) used** | Claude (Claude Code) |
| **Techniques used** | Constraint prompting ("DO NOT CODE", then "show me the code in this chat and ill copy/paste"), explain-first prompting, and stating the goal with the reason ("so it doesn't cloud the actual cauldron"). |
| **Iterations** | 2 prompts: first how to do it, then the code. In between, I tried writing it myself. |
| **Debugging / refinement** | My attempt didn't compile, so I asked the AI to help me finish it instead of starting over. It explained the three mistakes: my list had no ids, `items` wasn't imported, and both cards were in one item. |
| **Failed / rejected approaches** | My first attempt, which put both cards in a single list item. |
| **Strategy changes** | I asked for an explanation before any code, so I understood the approach before writing it. |
| **Verification** | Not verified by the AI; it only showed the code in chat. |
| **Code impact** | I pasted the AI's example into `ClickerPage.kt` myself and adapted it, keeping the upgrade list there and using my own names. |
| **Time** | 45 Minutes |
| **Outcome** | The upgrades scroll in their own list below the cauldron, and adding a new upgrade only needs one more entry in the list. |

## 4. Extra Hand upgrade

| **Challenge addressed (Context of use)** | Adding a new upgrade, Extra Hand, that multiplies how much each tap is worth, and learning which files a new upgrade touches. |
| **AI(s) used** | Claude (Claude Code) |
| **Techniques used** | Guided step-by-step prompting ("tell me the files I need to change"), debugging requests ("what is the error I'm currently having"), and giving a worked example ("extra click is at 4, extra hand is at 2 so total per click is 8"). |
| **Iterations** | 3 prompts: the steps, then finding my errors, then correcting the design. |
| **Debugging / refinement** | The AI compiled the project and found 2 compile errors and 2 logic bugs: taps gave 0 points because the multiplier started at 0, and the wrong price was being checked. |
| **Failed / rejected approaches** | I rejected the AI's first guess, that Extra Hand would stir the cauldron once per second, because I wanted it to multiply each tap. |
| **Strategy changes** | I added a worked example with real numbers, which got the exact design I wanted and exposed the starting-at-0 bug. Next time I would give the example in the first prompt. |
| **Verification** | The AI compiled the project to find the errors. The fixed version still needs to be built and tested once I apply the fixes. |
| **Code impact** | Written by me, following the AI's step-by-step guide. The AI didn't change any files. |
| **Time** | 10 Minutes |
| **Outcome** | I have an 8-step checklist for adding any future upgrade, and a clear design for Extra Hand: Stronger Stir at 4 × Extra Hand at 2 = 8 per tap. |

## 5. AI session documentation

| **Challenge addressed (Context of use)** | Our AI policy asks for a copy of the transcript and an ADR after using AI. |
| **AI(s) used** | Claude (Claude Code) |
| **Techniques used** | Constraint prompting, with an exact start point quoted from my first prompt, and giving an example of the format I wanted. |
| **Iterations** | 1 prompt for the transcript PDF, then the ADR log and this technique log, which I redid twice using my class's template as an example. |
| **Debugging / refinement** | I gave the AI a screenshot of the template, then an example of a finished log, to match the format. |
| **Failed / rejected approaches** | The first version of this log didn't follow the template, so I rejected it. |
| **Strategy changes** | I added examples of the template to get the right format. |
| **Verification** | The AI checked that the PDF included all 8 prompts and previewed its first page. |
| **Code impact** | 0% code affected. |
| **Time** | 30 Minutes. |
| **Outcome** | The transcript PDF, ADR log and this technique log are saved together in my Sprint 3 folder, as the AI policy asks. |
