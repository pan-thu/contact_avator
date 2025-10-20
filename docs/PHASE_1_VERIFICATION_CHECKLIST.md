# Phase 1 Verification Checklist

**Quick verification guide for Phase 1 implementation**

---

## Pre-Verification Setup

### 1. Open Project in Android Studio
```bash
# Navigate to project directory
cd /path/to/ContactAvatorApplication

# Open in Android Studio
studio .
# OR open Android Studio and select "Open Project"
```

### 2. Sync Gradle
- Click "Sync Now" in the notification bar
- OR: File → Sync Project with Gradle Files
- Wait for sync to complete (should be successful)

---

## Build Verification

### ✅ Step 1: Clean Build
```bash
./gradlew clean
```
**Expected**: Task completed successfully

### ✅ Step 2: Compile Debug
```bash
./gradlew compileDebugKotlin
```
**Expected**: BUILD SUCCESSFUL
**Check**: No compilation errors in Kotlin files

### ✅ Step 3: Full Build
```bash
./gradlew build
```
**Expected**: BUILD SUCCESSFUL
**Check**: APK generated in `app/build/outputs/apk/debug/`

---

## Database Schema Verification

### ✅ Step 4: Verify Contact Entity
**File**: `app/src/main/java/dev/panthu/contactavatorapplication/data/Contact.kt`

**Check**:
- [ ] File compiles without errors
- [ ] `@Entity` annotation present
- [ ] 8 fields defined (id, name, phone, email, address, avatarId, avatarUri, createdAt)
- [ ] Primary key has `@PrimaryKey(autoGenerate = true)`
- [ ] Helper methods present (getAvatarResourceId, hasCustomAvatar, getDisplayAvatarResourceId)

### ✅ Step 5: Verify ContactDao
**File**: `app/src/main/java/dev/panthu/contactavatorapplication/data/ContactDao.kt`

**Check**:
- [ ] File compiles without errors
- [ ] `@Dao` annotation present
- [ ] getAllContacts() returns `Flow<List<Contact>>`
- [ ] Insert/update/delete methods use `suspend`
- [ ] Search method exists with LIKE query

### ✅ Step 6: Verify ContactDatabase
**File**: `app/src/main/java/dev/panthu/contactavatorapplication/data/ContactDatabase.kt`

**Check**:
- [ ] File compiles without errors
- [ ] `@Database` annotation with Contact entity
- [ ] Version = 1
- [ ] Singleton pattern implemented
- [ ] getDatabase() method thread-safe

### ✅ Step 7: Verify ContactRepository
**File**: `app/src/main/java/dev/panthu/contactavatorapplication/data/ContactRepository.kt`

**Check**:
- [ ] File compiles without errors
- [ ] Takes Context in constructor
- [ ] Wraps all DAO methods
- [ ] Avatar validation in insert/update
- [ ] getAvailableAvatars() method present

---

## Theme Resources Verification

### ✅ Step 8: Verify themes.xml
**File**: `app/src/main/res/values/themes.xml`

**Check**:
- [ ] Parent theme is `Theme.MaterialComponents.Light.DarkActionBar`
- [ ] colorPrimary defined as `@color/primary`
- [ ] colorSecondary defined as `@color/secondary`
- [ ] No Compose references
- [ ] Theme.ContactAvatorApplication.NoActionBar exists

**Visual Test**: Open themes.xml in Android Studio → should show no errors

### ✅ Step 9: Verify colors.xml
**File**: `app/src/main/res/values/colors.xml`

**Check**:
- [ ] At least 29 colors defined
- [ ] primary, primary_dark, primary_light exist
- [ ] secondary, secondary_dark, secondary_light exist
- [ ] error color exists
- [ ] Avatar colors (avatar_blue through avatar_amber) exist
- [ ] No syntax errors

**Count Colors**:
```bash
grep -c "<color name=" app/src/main/res/values/colors.xml
# Expected: 29 or more
```

### ✅ Step 10: Verify dimens.xml
**File**: `app/src/main/res/values/dimens.xml`

**Check**:
- [ ] Spacing dimensions (spacing_tiny through spacing_xxlarge)
- [ ] Text sizes (text_size_display through text_size_caption)
- [ ] Avatar sizes (avatar_size_small through avatar_size_xlarge)
- [ ] touch_target_min = 48dp (accessibility requirement)
- [ ] No syntax errors

**Count Dimensions**:
```bash
grep -c "<dimen name=" app/src/main/res/values/dimens.xml
# Expected: 45 or more
```

### ✅ Step 11: Verify styles.xml
**File**: `app/src/main/res/values/styles.xml`

**Check**:
- [ ] Text appearance styles (6+)
- [ ] Button styles (3+)
- [ ] TextInputLayout styles
- [ ] Avatar styles (4 size variants)
- [ ] All styles reference theme attributes or resource values
- [ ] No hard-coded values

**Count Styles**:
```bash
grep -c "<style name=" app/src/main/res/values/styles.xml
# Expected: 20 or more
```

### ✅ Step 12: Verify strings.xml
**File**: `app/src/main/res/values/strings.xml`

**Check**:
- [ ] app_name = "ContactAvatar+"
- [ ] Action strings (action_save, action_cancel, etc.)
- [ ] Contact form labels and hints
- [ ] Error messages
- [ ] Content descriptions for accessibility
- [ ] No "TODO" or placeholder strings

**Count Strings**:
```bash
grep -c "<string name=" app/src/main/res/values/strings.xml
# Expected: 85 or more
```

### ✅ Step 13: Verify arrays.xml
**File**: `app/src/main/res/values/arrays.xml`

**Check**:
- [ ] avatar_resources integer-array exists
- [ ] Contains 10 items (avatar_01 through avatar_10)
- [ ] avatar_content_descriptions string-array exists
- [ ] Contains 10 accessibility descriptions

---

## Avatar Resources Verification

### ✅ Step 14: Verify Avatar Drawables Exist
```bash
ls app/src/main/res/drawable/avatar_*.xml
# Expected: 11 files (avatar_default + avatar_01 through avatar_10)
```

**Check**:
- [ ] avatar_default.xml exists
- [ ] avatar_01.xml through avatar_10.xml exist
- [ ] All files are valid XML

**Count Avatars**:
```bash
ls app/src/main/res/drawable/avatar_*.xml | wc -l
# Expected: 11
```

### ✅ Step 15: Visual Preview of Avatars
**In Android Studio**:
1. Open any avatar file (e.g., avatar_01.xml)
2. Click "Preview" tab on the right
3. Avatar should render with colored background and white icon

**Check**:
- [ ] Avatar renders without errors
- [ ] Background color is visible
- [ ] Person icon is white and centered
- [ ] No "Resource not found" errors

### ✅ Step 16: Verify Avatar Colors in colors.xml
**Check these colors exist**:
- [ ] avatar_blue
- [ ] avatar_green
- [ ] avatar_orange
- [ ] avatar_purple
- [ ] avatar_red
- [ ] avatar_teal
- [ ] avatar_pink
- [ ] avatar_indigo
- [ ] avatar_lime
- [ ] avatar_amber
- [ ] avatar_gray

---

## MainActivity Verification

### ✅ Step 17: Verify MainActivity.kt
**File**: `app/src/main/java/dev/panthu/contactavatorapplication/MainActivity.kt`

**Check**:
- [ ] File compiles without errors
- [ ] Extends AppCompatActivity (not ComponentActivity)
- [ ] No Compose imports
- [ ] Uses ActivityMainBinding for view binding
- [ ] setSupportActionBar() called with binding.toolbar
- [ ] onCreate() sets content view with binding.root

### ✅ Step 18: Verify activity_main.xml
**File**: `app/src/main/res/layout/activity_main.xml`

**Check**:
- [ ] Root is CoordinatorLayout
- [ ] AppBarLayout with MaterialToolbar exists
- [ ] FragmentContainerView with id="nav_host_fragment" exists
- [ ] No Compose-related elements
- [ ] Layout preview renders without errors

**Visual Test**: Open activity_main.xml → Design view → should show toolbar and empty content area

---

## Gradle Configuration Verification

### ✅ Step 19: Verify app/build.gradle.kts
**File**: `app/build.gradle.kts`

**Check Plugins**:
- [ ] kotlin-kapt plugin applied (not kotlin-compose)

**Check Build Features**:
- [ ] viewBinding = true
- [ ] compose removed or set to false

**Check Dependencies**:
- [ ] Room: runtime, ktx, compiler (with kapt)
- [ ] RecyclerView, CardView
- [ ] Material Design Components
- [ ] Navigation: fragment-ktx, ui-ktx
- [ ] Lifecycle: viewmodel-ktx, livedata-ktx
- [ ] Coroutines: android, core
- [ ] No Compose dependencies

**Verify Kapt**:
```bash
grep "kapt" app/build.gradle.kts
# Expected: kapt(libs.androidx.room.compiler)
```

### ✅ Step 20: Verify gradle/libs.versions.toml
**File**: `gradle/libs.versions.toml`

**Check Versions Section**:
- [ ] room version defined
- [ ] recyclerview version defined
- [ ] material version defined
- [ ] navigation version defined
- [ ] coroutines version defined

**Check Libraries Section**:
- [ ] androidx-room-runtime
- [ ] androidx-room-ktx
- [ ] androidx-room-compiler
- [ ] androidx-recyclerview
- [ ] material
- [ ] androidx-navigation-fragment-ktx
- [ ] androidx-navigation-ui-ktx

**Check Plugins Section**:
- [ ] kotlin-kapt plugin defined

---

## Compose Removal Verification

### ✅ Step 21: Verify Compose Code Removed
```bash
# Search for Compose imports (should find none)
grep -r "androidx.compose" app/src/main/java/
# Expected: No results

# Search for setContent calls (should find none)
grep -r "setContent" app/src/main/java/
# Expected: No results

# Verify theme directory deleted
ls app/src/main/java/dev/panthu/contactavatorapplication/ui/theme/
# Expected: No such file or directory
```

**Check**:
- [ ] No Compose imports in any .kt file
- [ ] No @Composable functions
- [ ] ui/theme/ directory deleted

---

## Integration Verification

### ✅ Step 22: Verify Resource References Work
**Create test in Android Studio**:

1. Open MainActivity.kt
2. In onCreate, after setContentView, add:
```kotlin
// Test resource references
val primaryColor = getColor(R.color.primary)
val spacing = resources.getDimension(R.dimen.spacing_medium)
val appName = getString(R.string.app_name)
val avatars = resources.getIntArray(R.array.avatar_resources)

// Log to verify
Log.d("MainActivity", "Primary color: $primaryColor")
Log.d("MainActivity", "Spacing: $spacing")
Log.d("MainActivity", "App name: $appName")
Log.d("MainActivity", "Avatars count: ${avatars.size}")
```

3. Run app on emulator
4. Check Logcat for output

**Expected**:
- Primary color: valid color int
- Spacing: 16.0 (or similar)
- App name: ContactAvatar+
- Avatars count: 10

### ✅ Step 23: Verify Database Can Initialize
**Create test in Android Studio**:

1. Create a test activity or add to MainActivity:
```kotlin
lifecycleScope.launch {
    val database = ContactDatabase.getDatabase(applicationContext)
    val dao = database.contactDao()
    val count = dao.getContactCount()
    Log.d("MainActivity", "Database initialized. Contact count: $count")
}
```

2. Run app
3. Check Logcat

**Expected**: "Database initialized. Contact count: 0"

---

## Final Verification

### ✅ Step 24: Run App on Emulator
1. Start Android Emulator (API 24+)
2. Run app (Shift+F10 or Run button)
3. App should launch without crashes

**Expected**:
- [ ] App installs successfully
- [ ] App launches without crashes
- [ ] Toolbar displays "ContactAvatar+"
- [ ] Empty content area (no fragments yet)
- [ ] No errors in Logcat

### ✅ Step 25: Code Quality Check
**Run lint**:
```bash
./gradlew lint
```

**Expected**:
- No critical errors
- Warnings acceptable (will be addressed in polish phase)

---

## Validation Summary

### Quick Checklist
- [ ] ✅ Project builds successfully
- [ ] ✅ Database schema is valid (4 files: Contact, DAO, Database, Repository)
- [ ] ✅ Theme resources are complete (6 files: themes, colors, dimens, styles, strings, arrays)
- [ ] ✅ Avatar resources load correctly (11 drawables)
- [ ] ✅ MainActivity converted to traditional Views
- [ ] ✅ Compose code completely removed
- [ ] ✅ View binding enabled and working
- [ ] ✅ App runs without crashes

### Files to Review (Priority Order)
1. app/build.gradle.kts
2. Contact.kt
3. ContactDatabase.kt
4. MainActivity.kt
5. themes.xml
6. strings.xml
7. avatar_*.xml (sample 2-3 files)

### Common Issues to Check
- [ ] No "cannot resolve symbol" errors
- [ ] No "resource not found" errors
- [ ] No ClassNotFoundException at runtime
- [ ] No SQLiteException on database access
- [ ] No InflateException on layout inflation

---

## If Issues Found

### Build Fails
1. Check JDK version (should be JDK 11+)
2. Invalidate caches: File → Invalidate Caches → Invalidate and Restart
3. Clean project: Build → Clean Project
4. Rebuild: Build → Rebuild Project

### Resource Errors
1. Check XML syntax in resource files
2. Verify all color/dimen/string references exist
3. Sync Gradle files

### Database Errors
1. Check Room annotations are correct
2. Verify kapt is processing annotations (check build output)
3. Clear build cache and rebuild

### Runtime Crashes
1. Check Logcat for stack trace
2. Verify theme is applied in AndroidManifest.xml
3. Check view binding is enabled in build.gradle.kts

---

**Verification Complete**: All checks passed = Phase 1 successful ✅

**Next Step**: Begin Phase 2 (Avatar Picker & Management)
