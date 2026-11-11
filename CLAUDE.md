# Happy Birthday Android

Project context for Claude Code. Keep this file short and up to date.

## Current work

**Feature:** Collage Wishes
**Task file:** [docs/collage-wishes-tasks.md](docs/collage-wishes-tasks.md) (requirement, decisions, research and all tasks)
**Done:** T0 to T14 work in debug and release builds. T16 to T21 (20 templates, the person's name, and moving and zooming photos) are built and unit tested, but haven't been confirmed on a device yet
**Next task:** the user's hand checks in T15, T19 and T21. Fix anything they report
**Testing:** the user tests on the device by hand. Build and run unit tests, then give them a short checklist; don't run long device or emulator sessions unless asked
**Git:** the user asked for no commits on 2026-11-09. Everything is uncommitted on `master`, including the separate version 1.5 work
**Last updated:** 2026-11-09

### Rules for every session

1. When the user asks to continue, read the task file and do the first unchecked task whose dependencies are done.
2. Do one task at a time, and finish it only when its **Done when** checks pass.
3. At the end of a task, tick it in the task file, add a line to its progress log, and update **Current work** above.
4. If new work starts, add it to **Current work** with its own task file in `docs/`.
5. Commit, push or publish only when the user asks.
6. Don't record anything as done or verified unless it was actually checked.

## What the app is

A birthday images and wishes app, published on Google Play under the publisher name YUGAL KUMAR.

- **Features:** birthday images by person, about 650 built-in wishes, an image editor, saved images, setting a wallpaper
- **Income:** AdMob ads, a rewarded ad that unlocks premium images for 24 hours, and a one-time premium purchase (`premium_unlock_birthday`)
- **Data:** bundled in the app; no active feature uses a backend

## Tech stack

Kotlin, XML layouts with ViewBinding, Navigation, MVVM-style ViewModels, Coil 2.7, Google Play Billing, AdMob, PhotoEditor library. AGP 9.2.1, Gradle 9.4.1, target SDK 36, minimum SDK 24. There's no version catalog; dependencies are in `app/build.gradle.kts`.

## Project structure

Paths are under `app/src/main/java/com/app/happy_birthday/`.

| Path | Purpose |
|---|---|
| `mvvm/<feature>/{view,view_model,model,adapter}` | Feature code: home, category, wallpaper, wishes, saved, edit_image, splash |
| `helper/ads/AdsHelper.kt` | `AdsManager`: all banner, interstitial and rewarded ads |
| `helper/billing/BillingManager.kt` | Premium purchase, created in `AppController` |
| `helper/AppNavigation.kt` | Navigation helper functions |
| `helper/Extensions.kt` | Image lists, premium unlock checks, store links and more |
| `helper/LocalHelper.kt` | `LocaleHelper`, including all wishes through `getAllBhajan(BirthdayPerson)` |
| `helper/Global.kt` | `isTestModeEnabled`, `folderName` and shared UI helpers |
| `helper/UtilExtenstions.kt` | `UtilExtension`: existing save-to-gallery code |
| `res/navigation/nav_graph.xml` | Single navigation graph |

## Conventions

- Fragments extend `BaseFragment`, and activities extend `BaseActivity`. `WishesListingFragment` is a good example to follow.
- Fragments use `setUpToolbar(...)`, and show banners with `AdsManager.loadBanner(requireActivity(), R.id.banner_container)`.
- Navigation uses a global action in `nav_graph.xml` plus a helper in `AppNavigation`.
- Add user-visible text to both `res/values/strings.xml` and `res/values-ar/strings.xml`; a missing Arabic string is a Lint error.
- Use neutral names in new code. "Bhajan", "Ram" and "Christmas" are leftovers from earlier apps.

## Commands

```bash
./gradlew :app:assembleDebug
```

```bash
./gradlew :app:testDebugUnitTest
```

```bash
./gradlew :app:lintDebug
```

Lint takes several minutes, so run it in the background. As of 2026-11-09 it fails on 4 existing errors, so check only that a change adds no new ones. The report is at `app/build/reports/lint-results-debug.html`.

## Things to watch out for

- `Global.isTestModeEnabled` is a hand-set constant, currently `false`, so debug builds serve **real ads**. Set it to `true` temporarily when testing ads, and restore it before finishing.
- `BaseActivity` locks the app to portrait, so screens don't rotate.
- Debug builds can be installed with `adb install -r` on the test Pixel 2 (Android 11), which uses the system file picker as the photo picker fallback. Don't uninstall the app from a device without asking, because that deletes its data.
- Release builds use R8 and resource shrinking, with an empty `proguard-rules.pro`. Anything saved as JSON with Gson needs `@SerializedName` on its fields, or R8 renames them and old saved data breaks. This caused a launch crash on update, fixed in `AppConfigModel` on 2026-11-09.
- Arabic strings exist, but users can't reach them: the language can only change from the unused intro screen, so the app runs in English.
- The root has no `.gitignore`, so `.gradle/` and `local.properties` are tracked, and `app/build` and `app/release` show as untracked. Stage files by name rather than with `git add .`.
- `mvvm/login`, `helper/SocialRegisterActivity.kt`, `data_source/`, `helper/location/` and the ringtone screen are unused template code. Don't build on them without asking.

## Known issues to fix before release

Found in the project analysis on 2026-11-09 and not fixed yet:

1. **Billing:** unacknowledged purchases aren't recovered on launch, so Google can refund them after 3 days (`BillingManager.kt:104`).
2. **Ad consent:** the UMP SDK is installed but never called, and AdMob requires consent in the EEA and UK.
3. **Saved images:** filenames collide across categories, and failed writes on Android 10+ report success (`ImagaViewActivity.kt:253`, `UtilExtenstions.kt:85`).
4. **Privacy policy:** the URL points to a Valentine's Day page (`Extensions.kt:1154`).
5. **Leftover text:** "Jai Shri Ram" after setting a wallpaper (`strings.xml:193`), and the `ValentinesDay` folder name. Renaming the folder needs a migration plan, or older saved images will disappear from the Saved screen.
6. **Pushwoosh:** the manifest still references the removed SDK (`AndroidManifest.xml:52-82`).
