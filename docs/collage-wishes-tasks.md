# Collage Wishes: requirement and task list

**Status:** All tasks are built, including 20 templates (T16 to T19), the person's name and photo adjusting (T20 and T21). The remaining checks are for the user to do by hand (see T15, T19 and T21), and nothing is committed yet.
**Last updated:** 2026-11-09

## How to use this file across sessions

1. Read this whole file, then pick the first unchecked task whose dependencies are done.
2. Do one task per session. Read the files it lists before changing anything.
3. A task is finished only when its **Done when** checks pass. Tick its box and add a line to the [progress log](#progress-log).
4. Update **Current work** in `CLAUDE.md` at the project root so the next session knows what's next.
5. If a task changes a decision or a file path, update this file in the same session.
6. Commit only when the user asks.

Prompt to start a new session:

> Read `docs/collage-wishes-tasks.md` and do the next unchecked task. Stop when its "Done when" checks pass, then update the file.

---

## 1. Requirement

Add a **Collage Wishes** card to the home screen. The user can:

1. Pick one or more of their own photos.
2. Choose a birthday wish from the app's existing wishes.
3. Choose one of the app's birthday templates.
4. Preview and save the finished image.

An ad shows after saving, through the existing `AdsManager`.

### User flow

```
Home > Collage Wishes card
  > Collage screen (photo picker opens on first entry)
      Live preview at the top
      Controls: Photos | Wish | Template
  > Save
      Image is written to the gallery
      Interstitial shows (skipped for premium users or if no ad is ready)
      Result state: Saved, with Share and Create another
```

The finished image also appears in the existing **Saved** screen, because that screen reads the same gallery folder.

### Out of scope for the first version

- Dragging, zooming or rotating photos inside a slot (photos are center-cropped)
- Typing a custom wish
- Downloading templates from a server
- Premium-only templates
- Video

---

## 2. Decisions

| # | Decision | Choice | Why | Status |
|---|---|---|---|---|
| D1 | Ad after saving | **Interstitial** via `AdsManager.showInterstitial`, plus the normal banner on the collage screen via `AdsManager.loadBanner` | This is what the other images do after saving. A rewarded ad can't show automatically: AdMob requires the user to opt in first | **Confirmed by the user, 2026-11-09** |
| D2 | Output size | 1080×1350 px (4:5) JPEG | Fits phones and social feeds, and uses about 5.8 MB of memory while rendering | Default |
| D3 | Number of photos | 1 to 6 | The layout stays readable and memory stays bounded | Default |
| D4 | Template art | 20 original templates made as XML/vector drawables (the user asked for 20 on 2026-11-09), with room to add designer PNG/WebP files later | Vectors add almost nothing to app size | Confirmed count; the user can supply art |
| D9 | Photo arrangement | Each template has its own `CollageLayoutStyle`, with different positions, shapes, rotation or frames for 2 to 6 photos | The user asked for a unique collage position per template. `CollageLayoutStyleTest` fails if two styles match | Confirmed by the user, 2026-11-09 |
| D5 | Rendering | Draw on a `Canvas` into an offscreen `Bitmap` | The output is the same on every device, whereas capturing a `View` depends on screen size | Default |
| D6 | Photo access | System photo picker with no new permission | Google Play allows `READ_MEDIA_IMAGES` only when a system picker isn't enough | Default |
| D7 | Access | Free for all users | Keeps the first version simple | Default |
| D8 | Home card position | New full-width row below Wishes and Rate Us | `linPremiumFeature` is hidden as a whole row for free users, and its second slot is the hidden Ringtone card, so it can't hold an always-visible card | Default |

### D1 options

The request said "rewarded banner". In this app, those are three different things:

| Format | Existing code | Can it show automatically after saving? |
|---|---|---|
| Interstitial | `AdsManager.showInterstitial` (used after saving other images) | **Yes** |
| Banner | `AdsManager.loadBanner` (used on listing screens) | Yes, it stays on screen |
| Rewarded | `AdsManager.showRewarded` (used by "Watch Ad → Unlock 10 Images") | **No**, the user must accept first and must get a reward |

If a rewarded ad is wanted later, it needs its own button and a real reward, such as "Watch ad to unlock this template".

---

## 3. Research findings

### Photo picker

- `ActivityResultContracts.PickMultipleVisualMedia(maxItems)` needs `androidx.activity` 1.7.0 or newer. The project declares `activity-ktx:1.8.2`.
- `maxItems` must be at least 2. The user can still choose just one photo, so **one launcher covers both single and multiple selection**.
- On devices without the picker, the library falls back to `ACTION_OPEN_DOCUMENT` and **ignores `maxItems`**, so the app must enforce the limit itself.
- A manifest entry enables the backported picker on older devices (see T3).
- Access to the returned URIs lasts until the device restarts or the app stops. This feature doesn't need persistent access, but it must handle a URI that can no longer be read.

### AdMob

- Standard rewarded ads can be served only after the user clearly opts in, for example by tapping "yes" or "accept". They must be skippable, and declining must not block normal use of the app.
- Rewarded interstitials need an intro screen with a visible way to decline.
- Interstitials belong at natural pauses, such as after a task is completed, and shouldn't appear unexpectedly mid-task.

### Image handling

- Decode each photo to the size of its slot rather than loading it full size. Phone photos can use far more memory than the output image.
- Coil performs this downsampling when given a target size. Coil is already a dependency (2.7.0).
- `allowHardware(false)` is required for bitmaps that are drawn on a software `Canvas`. Hardware bitmaps fail with "Software rendering doesn't support hardware bitmaps".
- The Coil recipes page now documents version 3. Check API names against 2.7.0 while implementing, and test EXIF rotation on a real photo (T7).

### Sources

- [Photo picker, Android Developers](https://developer.android.com/training/data-storage/shared/photo-picker)
- [Photo and video permissions, Play Console Help](https://support.google.com/googleplay/android-developer/answer/16558241?hl=en)
- [Policies for ad units that offer rewards, AdMob Help](https://support.google.com/admob/answer/7313578?hl=en)
- [Rewarded interstitial overview, AdMob Help](https://support.google.com/admob/answer/9884467?hl=en)
- [Disallowed interstitial implementations, AdMob Help](https://support.google.com/admob/answer/6201362?hl=en)
- [Interstitial ads for Android, Google for Developers](https://developers.google.com/admob/android/interstitial)
- [Loading large bitmaps efficiently, Android Developers](https://developer.android.com/topic/performance/graphics/load-bitmap)
- [Coil recipes](https://coil-kt.github.io/coil/recipes/)

---

## 4. What already exists

| Need | Reuse | Location |
|---|---|---|
| Home card layout | `layout_home_option` (`conBg`, `ivOption`, `txtTitle`) | `res/layout/fragment_home.xml`, `mvvm/home/view/HomeFragment.kt` |
| Navigation | Fragment plus global action, with helpers in `AppNavigation` | `res/navigation/nav_graph.xml`, `helper/AppNavigation.kt` |
| Screen structure | `BaseFragment`, `setUpToolbar`, ViewModel via `ViewModelProvider` | `mvvm/wishes/view/WishesListingFragment.kt` is a good example |
| Wish text | `LocaleHelper.getAllBhajan(BirthdayPerson)` | `helper/LocalHelper.kt:67` |
| Person categories | `getCategoryData()`, `BirthdayPerson` | `helper/Extensions.kt`, `helper/helper_model/DAY.kt` |
| Ads | `AdsManager.showInterstitial`, `AdsManager.loadBanner` (both already skip premium users) | `helper/ads/AdsHelper.kt` |
| Gallery folder | `Global.folderName`, which the Saved screen reads | `helper/Global.kt:58` |
| Progress dialog | `showProgressDialog("F")`, `hideProgressDialog()` | `helper/BaseActivity.kt:74` |

### Existing problems to avoid copying

| Problem | Where | What to do in this feature |
|---|---|---|
| Filenames collide, so a new save replaces an old one | `ImagaViewActivity.kt:253`, `UtilExtenstions.kt:28-36` | Use a unique filename with a timestamp |
| A failed write on Android 10+ still returns `"ok"` | `UtilExtenstions.kt:85` | Add a new save helper that returns the `Uri`, or `null` on failure |
| The screen closes while the ad is opening | `ImagaViewActivity.kt:276-277` | Continue only inside the `onDismiss` callback |
| Storage permission is never requested on Android 9 and below | No runtime request in `mvvm/` | Request it before saving (T11) |
| `decodeResource` runs on the main thread and scales by density | `ImagaViewActivity.kt:246` | Decode off the main thread, using `inScaled = false` for any bitmap resources |
| Leftover names such as "Bhajan", "Ram" and "Christmas" | Throughout | Use neutral names in new code; call the existing `getAllBhajan` as it is |

---

## 5. Architecture

```
mvvm/collage/
  model/CollageTemplate.kt        Template definition (drawable, photo area, wish area, colors)
  model/CollageTemplates.kt       The list of templates
  model/CollageLayout.kt          Pure Kotlin: photo count and area to slot rectangles
  view_model/CollageViewModel.kt  Photos, wish, template, preview and save state
  view/CollageFragment.kt         Screen
  view/WishPickerBottomSheet.kt   Category and wish selection
  adapter/SelectedPhotoAdapter.kt
  adapter/TemplateAdapter.kt
  adapter/WishPickerAdapter.kt
helper/collage/
  CollageImageLoader.kt           Decodes picked URIs to slot-sized bitmaps through Coil
  CollageRenderer.kt              Draws the template, photos and wish onto a Canvas
  CollageStorage.kt               Saves to the gallery and returns the Uri
```

**Photo layout by count** (inside the template's photo area):

| Photos | Layout |
|---|---|
| 1 | Full area |
| 2 | Side by side |
| 3 | 1 on top, 2 below |
| 4 | 2×2 |
| 5 | 2 on top, 3 below |
| 6 | 3×2 |

Templates store areas as fractions (0 to 1) of the canvas, so the same template works at preview size and at full size.

---

## 6. Tasks

### Phase A: Setup and entry point

- [x] **T0. Confirm the starting point** (build check done; commit and branch left to the user)
  - **Depends on:** nothing
  - **Steps:**
    1. Run `./gradlew :app:assembleDebug` and record the result in the progress log.
    2. **Waiting on the user:** on 2026-11-09 the user asked not to commit anything, so the work stays uncommitted on `master` and no branch was created. Collage changes and the version 1.5 work are mixed in the working tree until the user commits them.
  - **Done when:** the debug build succeeds.

- [x] **T1. Add an empty Collage screen**
  - **Depends on:** T0
  - **Files:** `mvvm/collage/view/CollageFragment.kt`, `mvvm/collage/view_model/CollageViewModel.kt`, `res/layout/fragment_collage.xml`, `res/navigation/nav_graph.xml`, `helper/AppNavigation.kt`, `res/values/strings.xml`, `res/values-ar/strings.xml`
  - **Steps:**
    1. Create the fragment following the structure of `WishesListingFragment`, with `ilToolbar` and `banner_container`.
    2. Add the `navigation_collage` destination and `action_navigation_collage`.
    3. Add `Fragment.navigateToCollage()`.
    4. Add `label_collage_wishes` in English and Arabic.
  - **Done when:** the project builds, and navigating to the screen shows the toolbar and back works.

- [x] **T2. Add the home card**
  - **Depends on:** T1
  - **Files:** `res/layout/fragment_home.xml`, `mvvm/home/view/HomeFragment.kt`, `res/drawable/ic_collage.xml`
  - **Steps:**
    1. Add a full-width row with `ilCollage` after the Wishes and Rate Us row (D8).
    2. Set its color, icon and title in `initializeFields()`, like the other cards.
    3. Call `navigateToCollage()` from `initOnClickListener()`.
  - **Done when:** the card is visible to free and premium users, opens the Collage screen, and the home screen still scrolls fully on a small phone.
  - **Checked:** on a Pixel 2 as a free user. The row is outside the premium-only row in the layout, but it hasn't been seen on a premium account.

### Phase B: Picking photos

- [x] **T3. Add the photo picker**
  - **Depends on:** T1
  - **Files:** `app/src/main/AndroidManifest.xml`, `CollageFragment.kt`, `CollageViewModel.kt`, `fragment_collage.xml`, `mvvm/collage/adapter/SelectedPhotoAdapter.kt`
  - **Steps:**
    1. Add this inside `<application>`:
       ```xml
       <service android:name="com.google.android.gms.metadata.ModuleDependencies"
                android:enabled="false"
                android:exported="false"
                tools:ignore="MissingClass">
           <intent-filter>
               <action android:name="com.google.android.gms.metadata.MODULE_DEPENDENCIES" />
           </intent-filter>
           <meta-data android:name="photopicker_activity:0:required" android:value="" />
       </service>
       ```
    2. Register `PickMultipleVisualMedia(6)` as a fragment property, using `PickVisualMedia.ImageOnly`.
    3. Open the picker the first time the user enters the screen.
    4. Keep the photos as a `List<Uri>` in the ViewModel, and apply `take(6)` with a message because the fallback picker ignores the limit.
    5. Show thumbnails with remove and add buttons.
  - **Done when:** selecting 1 photo and 6 photos both work, cancelling leaves a usable screen, and no new permission appears in the manifest.
  - **Checked on a Pixel 2 (Android 11, system file picker fallback):** one photo, cancel, remove, no new permission.
  - **Checked by unit test:** the limit of 6 and duplicate handling.
  - **Checked on an Android 17 emulator (real photo picker):** selecting 6 at once.
  - **Still to check by hand:** selecting several at once in the system file picker on the Pixel 2.

### Phase C: Rendering

- [x] **T4. Add the template model and photo layout, with tests**
  - **Depends on:** T0
  - **Files:** `mvvm/collage/model/CollageTemplate.kt`, `mvvm/collage/model/CollageLayout.kt`, `app/src/test/java/com/app/happy_birthday/CollageLayoutTest.kt`
  - **Steps:**
    1. Define `CollageTemplate(id, nameRes, backgroundRes, overlayRes?, photoArea, wishArea, wishTextColor, cornerRadius)`.
    2. Write `CollageLayout.slots(count, area, gap)` with no Android dependency, returning a small rectangle data class. It works in any unit, so the renderer (T6) should convert the area to pixels first and pass the gap in pixels. That keeps horizontal and vertical gaps equal.
    3. Test counts 1 to 6: correct slot count, every slot inside the area, and no overlaps.
  - **Done when:** `./gradlew :app:testDebugUnitTest` passes.

- [x] **T5. Create 3 templates**
  - **Depends on:** T4, and D4
  - **Files:** `res/drawable/bg_collage_*.xml`, `mvvm/collage/model/CollageTemplates.kt`, both `strings.xml` files
  - **Steps:**
    1. Create 3 original designs, for example Confetti, Balloons and Elegant Gold.
    2. Register them directly by `R.drawable` reference. Don't use `getIdentifier`, so resource shrinking can't remove them by mistake.
  - **Done when:** the project builds and each template has a name in English and Arabic.

- [x] **T6. Add the renderer**
  - **Depends on:** T4, T5
  - **Files:** `helper/collage/CollageRenderer.kt`
  - **Steps:**
    1. Add `render(context, template, photos, wishText, width, height): Bitmap`.
    2. Draw in this order: background, photos center-cropped with rounded corners, overlay, title, wish text. The title is `R.string.label_collage_title`, drawn in `titleArea` with `titleTextColor` so it can be translated. Resolve it from the app's current language, and pass it in or use a localized context.
    3. Draw the wish with `StaticLayout`, shrinking the font until it fits `wishArea`, with a minimum size and ellipsis as the last resort. Fit the title the same way.
    4. Keep it safe to call off the main thread, and draw vectors with `setBounds` and `draw`.
  - **Done when:** using temporary test code, the longest wish in `LocalHelper.kt` fits without clipping and its emoji render in color. Remove the temporary code afterwards.

- [x] **T7. Add the photo loader**
  - **Depends on:** T3, T4
  - **Files:** `helper/collage/CollageImageLoader.kt`
  - **Steps:**
    1. Add a suspend function that loads each URI through Coil's `execute`, using `size(slotWidth, slotHeight)`, `Scale.FILL` and `allowHardware(false)`.
    2. Convert the result to a `Bitmap`.
    3. Return a success or failure per photo, so one unreadable photo doesn't fail the whole collage.
  - **Done when:** a sideways portrait photo with EXIF rotation appears upright, a large photo (12 MP or more) loads without an out-of-memory error, and a failed URI produces a message rather than a crash.

### Phase D: Screen

- [x] **T8. Show the live preview**
  - **Depends on:** T6, T7
  - **Files:** `CollageViewModel.kt`, `CollageFragment.kt`, `fragment_collage.xml`
  - **Steps:**
    1. Render at half size (540×675) on `Dispatchers.Default` whenever the photos, wish or template change.
    2. Cancel any earlier render job when a new change comes in.
    3. Show the result through `LiveData` in a 4:5 `ImageView`, and don't reuse or recycle a bitmap that is still on screen.
    4. Use the first template and a default wish until the user chooses.
  - **Done when:** the preview updates after each change, and the UI stays responsive with 6 large photos.

- [x] **T9. Add the wish picker**
  - **Depends on:** T8
  - **Files:** `mvvm/collage/view/WishPickerBottomSheet.kt`, `mvvm/collage/adapter/WishPickerAdapter.kt`, `res/layout/bottom_sheet_wish_picker.xml`, `res/layout/item_wish_picker.xml`
  - **Steps:**
    1. Add a Material bottom sheet with person chips from `getCategoryData()`.
    2. List wishes from `getAllBhajan(person)` in a new adapter. Don't reuse `WishListAdapter`, because it has copy and share buttons.
    3. Tapping a wish updates the ViewModel and closes the sheet.
  - **Done when:** every category lists its wishes and the selected wish appears in the preview.

- [x] **T10. Add the template picker**
  - **Depends on:** T8
  - **Files:** `mvvm/collage/adapter/TemplateAdapter.kt`, `res/layout/item_collage_template.xml`, `fragment_collage.xml`, `CollageFragment.kt`
  - **Steps:** add a horizontal list of templates with a clear selected state. Tapping one updates the preview.
  - **Done when:** all 3 templates can be selected, and the selection is kept when returning from the photo picker.

### Phase E: Save, ad and share

- [x] **T11. Add the save helper** (Android 9 and below not run on a device)
  - **Depends on:** T0
  - **Files:** `helper/collage/CollageStorage.kt`
  - **Steps:**
    1. Add a suspend function `save(context, bitmap): Uri?` that runs on `Dispatchers.IO`.
    2. Use the filename `birthday_collage_<timestamp>.jpg` in `Pictures/${Global.folderName}`, with JPEG quality around 92.
    3. On Android 10+, use `RELATIVE_PATH` and `IS_PENDING`. If writing fails, delete the pending row and return `null`.
    4. On Android 9 and below, write through `DATA` and run the media scanner.
    5. Leave `UtilExtension.saveImageToGallery` unchanged so other screens aren't affected.
    6. Add the runtime `WRITE_EXTERNAL_STORAGE` request in the fragment, for Android 9 and below only.
  - **Done when:** two saves create two separate files, both appear in the Saved screen, and a forced failure returns `null`.

- [x] **T12. Connect Save and the ad** (premium path not run on a device)
  - **Depends on:** T8, T11
  - **Files:** `CollageFragment.kt`, `CollageViewModel.kt`, `fragment_collage.xml`
  - **Steps:**
    1. Disable Save until at least one photo is selected, and ignore extra taps while saving.
    2. Show progress, re-render at 1080×1350 off the main thread, then call `CollageStorage.save`.
    3. If saving fails, show an error and **don't show an ad**.
    4. If saving succeeds, call `AdsManager.showInterstitial(mActivity) { ... }` and show the result only inside the callback, checking `isAdded` first.
    5. Load the banner with `binding.root.post { AdsManager.loadBanner(requireActivity(), R.id.banner_container) }`.
    6. Keep `AdsHelper.kt` unchanged.
  - **Done when:** with `isTestModeEnabled = true` temporarily, the ad shows once after a save, the flow continues if no ad is ready, a premium user sees no ad, and the flag is back to `false` afterwards. **Use test ads only: never tap real ads on your own device.**

- [x] **T13. Add the result state** (share sheet checked; sending to WhatsApp still to check by hand)
  - **Depends on:** T12
  - **Files:** `CollageFragment.kt`, `fragment_collage.xml`
  - **Steps:** after saving, show "Saved" with **Share** and **Create another**. Share the returned `Uri` with `ACTION_SEND` and `FLAG_GRANT_READ_URI_PERMISSION`, and don't insert another gallery row. Create another clears the selection.
  - **Done when:** sharing to WhatsApp sends the correct image and adds no duplicate to the gallery.

### Phase F: Finish

- [x] **T14. Polish and translate**
  - **Depends on:** T13
  - **Steps:**
    1. Move all new text to `strings.xml` and `values-ar/strings.xml`.
    2. Add an empty state for when no photo is selected.
    3. Ask for confirmation when leaving with an unsaved collage.
    4. Add `contentDescription` to new icons and images.
    5. Check the screen in dark mode and in Arabic (right-to-left).
  - **Done when:** the new files add no Lint errors (compare with the 4 existing ones) and both languages look correct.

- [ ] **T15. Test the feature end to end** (remaining items are the user's hand checks)
  - **Depends on:** T14
  - **Steps:**
    1. Run `./gradlew :app:testDebugUnitTest :app:assembleRelease`.
    2. Install the release build, because R8 is enabled, and go through the checklist below.
  - **Checklist:**
    - [x] 1 photo (Pixel 2) and 6 photos (emulator)
    - [x] Portrait, landscape and sideways EXIF photos
    - [x] PNG and 12 MP photos
    - [ ] HEIC photos
    - [x] Cancelling the picker
    - [x] Longest wish on every template
    - [ ] Leaving the app and coming back at each step
    - [ ] Android 9 or lower, if a device is available (picker fallback and permission)
    - [x] Android 13 or higher (Android 17 emulator)
    - [x] Free user
    - [ ] Premium user (no ads, no crash)
    - [x] No internet (no ad, and the flow continues)
    - [x] Saved screen shows the collage and can open, delete and set it as wallpaper
    - [ ] Saving and sharing on a real phone, including sending to WhatsApp
    - [ ] Selecting several photos at once in the Pixel 2 file picker
  - **Done when:** every checklist item passes in the release build, and any failures are fixed or written up here.

### Phase G: 20 templates with unique arrangements

- [x] **T16. Add 17 more template designs**
  - **Files:** `res/drawable/bg_collage_*.xml`, `mvvm/collage/model/CollageTemplates.kt`, both `strings.xml` files
  - **Done when:** there are 20 templates, each with its own art, and names in English and Arabic. Art keeps clear of the title, photo and wish areas.

- [x] **T17. Add 20 photo arrangements**
  - **Files:** `mvvm/collage/model/CollageLayoutStyle.kt`, `app/src/test/java/com/app/happy_birthday/CollageLayoutStyleTest.kt`
  - **Done when:** no two styles match for 2 to 6 photos, every style has a slot per photo, and tiled styles don't overlap.

- [x] **T18. Draw shapes, rotation and frames**
  - **Files:** `helper/collage/CollageRenderer.kt`, `mvvm/collage/view_model/CollageViewModel.kt`
  - **Done when:** the renderer draws rectangles, circles, hearts, hexagons, diamonds and arches, with rotation and polaroid frames.

- [x] **T19. Show each arrangement in the template strip** (hand check below)
  - **Files:** `mvvm/collage/view/CollageThumbnailView.kt`, `mvvm/collage/adapter/TemplateAdapter.kt`, `res/layout/item_collage_template.xml`
  - **Done when:** thumbnails show the arrangement for the number of photos picked, or for 3 when none are picked.
  - **Still to check by hand, because this code hasn't run on a device yet:**
    - [ ] The template strip scrolls smoothly through all 20
    - [ ] Hearts, circles, hexagons, diamonds and arches cut photos cleanly
    - [ ] Polaroids and Cascade show white frames and shadows
    - [ ] Fan and Polaroids don't cover the title or wish
    - [ ] Each template looks right with 1, 3 and 6 photos, and the saved image matches the preview

### Phase H: Name and photo adjusting

- [x] **T20. Add the person's name to the title**
  - **Files:** `mvvm/collage/view/CollageFragment.kt`, `res/layout/fragment_collage.xml`, both `strings.xml` files
  - **Done when:** a Name tab adds the name on a second title line under "Happy Birthday", up to 24 characters, and both lines shrink together only as far as needed to fit. Changed from one line on 2026-11-09 at the user's request, because the title looked too small.

- [x] **T21. Let the user move and zoom each photo** (hand check below)
  - **Files:** `mvvm/collage/model/CollageCrop.kt`, `mvvm/collage/view/CollagePreviewGestures.kt`, `mvvm/collage/view_model/CollageViewModel.kt`, `helper/collage/CollageRenderer.kt`, `app/src/test/java/com/app/happy_birthday/CollageCropTest.kt`
  - **Design:** position is saved as fractions of the photo, so it stays correct at preview size, in the saved image and after changing template. Photos can't be zoomed out past covering their slot, so they never leave a gap.
  - **Done when:** drag moves a photo, pinch zooms it up to 3×, and double-tap resets it. The saved image matches the preview.
  - **Still to check by hand, because the touch code hasn't run on a device yet:**
    - [ ] Dragging is smooth with 1 and 6 photos on the Pixel 2
    - [ ] Pinch zooms the photo the first finger touched
    - [ ] Tilted photos (Party Flags, Rainbow) follow the finger
    - [ ] Where photos overlap (Bubbles, Rainbow), the top photo moves
    - [ ] Typing a name keeps the field visible above the keyboard
    - [ ] A long name fits and stays readable on the title line
    - [ ] The saved image keeps the name and each photo's position

---

## 7. Dependency order

```
T0 > T1 > T2
     T1 > T3 ─────────┐
T0 > T4 > T5 > T6 ────┼> T8 > T9, T10
     T4 ────── T7 ────┘    │
T0 > T11 ─────────────────> T12 > T13 > T14 > T15
```

---

## 8. Related work (separate from this feature)

These came from the project analysis and don't block Collage Wishes, but they should be fixed before release:

- Billing: unacknowledged purchases are not recovered (`BillingManager.kt:104`)
- Ad consent: the UMP SDK is installed but never called
- Saved image filename collisions for the existing image screens
- Privacy policy URL points to a Valentine's Day page. **It must also cover user-selected photos**, even though they stay on the device
- Leftover "Jai Shri Ram" text and the `ValentinesDay` folder name. Renaming the folder needs a plan, because older saved images would stop appearing in the Saved screen

---

## Progress log

| Date | Task | Result | Notes |
|---|---|---|---|
| 2026-11-09 | Planning | Done | Requirement, research and task list written. D1 confirmed: interstitial after saving |
| 2026-11-09 | T0 | Done | Baseline `assembleDebug` passes on `master`. User asked for no commits, so no commit or branch was made |
| 2026-11-09 | T1 | Done | Collage screen and navigation work on a Pixel 2 (Android 11). Title has English and Arabic text; Arabic display not checked yet |
| 2026-11-09 | T2 | Done | Card appears and opens the screen, and the home screen still scrolls to Go Premium. Checked as a free user only |
| 2026-11-09 | T3 | Done | One photo, cancel and remove work on the Pixel 2; selecting 6 works in the emulator's photo picker; limit and duplicates pass unit tests. Selecting several in the Pixel 2 file picker still needs a hand check |
| 2026-11-09 | T4 | Done | 13 `CollageLayoutTest` tests pass. Lint still shows only the 4 existing errors. The app is locked to portrait in `BaseActivity`, so rotation checks were removed from this plan |
| 2026-11-09 | T5 | Done | Confetti, Balloons and Elegant Gold added, and the art was checked on the Pixel 2 using a temporary preview, since removed. Added a translated title area to the template model. 4 `CollageTemplatesTest` tests pass |
| 2026-11-09 | T6 to T14 | Done | Checked on an Android 17 emulator with test ads and generated test images, in debug and in a release build with R8. Screen uses Photos, Wish and Template tabs. Fixed during testing: wish picker background, selected chip, stale preview after an unreadable photo |
| 2026-11-09 | T15 | Waiting on the user | Remaining items in the T15 checklist are hand checks. Temporary test code removed; `isTestModeEnabled` is back to `false`. 21 unit tests pass; Lint shows only the 4 existing errors |
| 2026-11-09 | T16 to T19 | Built, device check pending | 20 templates and 20 arrangements added. Art and arrangements were reviewed on generated preview sheets, not on a device. 30 unit tests pass; Lint shows only the 4 existing errors plus minor warnings |
| 2026-11-09 | T20 and T21 | Built, device check pending | Name tab and drag, pinch and double-tap on the preview added. 7 `CollageCropTest` tests cover moving and zooming. Touch handling hasn't run on a device yet |
| 2026-11-09 | Outside this feature | Fixed | Release builds crashed at launch for anyone updating from a non-R8 build, because R8 renamed the saved `AppConfigModel` fields. Fixed with `@SerializedName` and alternate names, plus `AppConfigModelTest`. Upgrade from debug data to a release build then opened normally |
