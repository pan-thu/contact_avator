# ContactAvatar+ Implementation Workflow

**Generated from**: `docs/prd.md`
**Project**: ContactAvatar+ Android Application
**Approach**: Systematic implementation with dependency-aware phasing
**Testing**: Excluded per user request (handled separately)

---

## 📋 Overview

This workflow provides a structured implementation path for the ContactAvatar+ application, organized into phases with clear dependencies, success criteria, and validation gates. Each phase builds upon previous work to ensure systematic, quality-driven development.

### 🎯 Core Implementation Goals
- Extend contact persistence with avatar support
- Implement resource-based avatar system with optional custom imports
- Build polished UI with RecyclerView, search/filter, and sorting
- Apply Android theming and styling throughout
- Ensure smooth performance with DiffUtil
- Maintain offline-first architecture

---

## 🏗️ Phase 1: Foundation & Architecture Setup

**Dependencies**: None (starting phase)
**Duration Estimate**: 2-3 days
**Personas**: Architect, Backend, Android Developer

### Objectives
- Set up project architecture and data layer
- Extend data model with avatar support
- Configure Room database and DAOs
- Establish theming and resource structure

### Tasks

#### 1.1 Project Structure & Dependencies
- [ ] **Review existing ContactDatabase codebase** from Lecture 5
- [ ] **Verify Gradle configuration** for Room, Lifecycle, RecyclerView
- [ ] **Add missing dependencies** (if any): Navigation, Material Design, Kotlin coroutines
- [ ] **Set up package structure**: `data/`, `ui/`, `util/`, `resources/`
- [ ] **Configure build variants** (if needed for debugging)

**Success Criteria**: Project builds successfully with all required dependencies

#### 1.2 Data Model Extension
- [ ] **Extend Contact entity** with `avatarId: Int?` field for drawable resource IDs
- [ ] **Add `avatarUri: String?`** field for optional custom avatar URIs (FR-05)
- [ ] **Define avatar fallback logic** in entity: `getAvatarResourceId()` with default
- [ ] **Update database version** and create migration strategy
- [ ] **Implement Room migration** from existing schema to avatar-enabled schema

**Files**: `data/Contact.kt`, `data/ContactDatabase.kt`
**Success Criteria**: Database schema updated without data loss, migrations tested

#### 1.3 DAO & Repository Updates
- [ ] **Update ContactDao** with avatar-related queries (if needed)
- [ ] **Extend Repository** to handle avatar persistence logic
- [ ] **Add avatar validation** in repository layer (resource ID existence checks)
- [ ] **Implement default avatar constant** (e.g., `R.drawable.avatar_default`)

**Files**: `data/ContactDao.kt`, `data/ContactRepository.kt`
**Success Criteria**: Repository can persist and retrieve contacts with avatars

#### 1.4 Theme & Style Foundation
- [ ] **Define app theme** in `res/values/themes.xml` (Material3 or Material2)
- [ ] **Create `colors.xml`** with primary, secondary, surface, background colors
- [ ] **Create `dimens.xml`** with spacing, text sizes, touch targets (≥48dp)
- [ ] **Create `styles.xml`** for reusable text styles, button styles, card styles
- [ ] **Ensure `strings.xml`** has no hard-coded UI text

**Files**: `res/values/themes.xml`, `colors.xml`, `dimens.xml`, `styles.xml`, `strings.xml`
**Success Criteria**: All styling extracted to resources, theme applied app-wide (NFR-01)

#### 1.5 Avatar Resource Collection
- [ ] **Create avatar drawable set** (8-12 diverse avatars) in `res/drawable/`
- [ ] **Name avatars systematically**: `avatar_01.xml`, `avatar_02.xml`, etc.
- [ ] **Define default avatar**: `avatar_default.xml` (fallback for all scenarios)
- [ ] **Create avatar resource array** in `res/values/arrays.xml` for picker
- [ ] **Verify offline availability** of all avatar resources

**Files**: `res/drawable/avatar_*.xml`, `res/values/arrays.xml`
**Success Criteria**: Avatar resources load correctly, array accessible in code (FR-02)

### Phase 1 Validation Gates
✅ Database migration executes without errors
✅ All avatar resources render in preview
✅ Theme applies consistently across sample screens
✅ Repository layer handles avatar persistence correctly

---

## 🎨 Phase 2: Avatar Picker & Management

**Dependencies**: Phase 1 (data model, resources, theming)
**Duration Estimate**: 3-4 days
**Personas**: Frontend Architect, UX Designer, Android Developer

### Objectives
- Build avatar picker dialog/bottom sheet with grid layout
- Implement avatar selection state and preview
- Handle custom avatar import (optional feature)
- Integrate avatar reset functionality

### Tasks

#### 2.1 Avatar Picker UI Component
- [ ] **Create `AvatarPickerDialog`** or `AvatarPickerBottomSheet` component
- [ ] **Design grid layout** with `RecyclerView` or `GridLayout` (3-4 columns)
- [ ] **Implement avatar grid adapter** displaying resource avatars
- [ ] **Add selection state visual** (border, checkmark, background tint)
- [ ] **Show live preview** of selected avatar at top of dialog
- [ ] **Apply theme styling** to dialog (colors, corner radius, elevation)

**Files**: `ui/avatar/AvatarPickerDialog.kt`, `ui/avatar/AvatarGridAdapter.kt`, `res/layout/dialog_avatar_picker.xml`, `res/layout/item_avatar_grid.xml`
**Success Criteria**: Grid displays all avatars, selection state is clear, preview updates (FR-04)

#### 2.2 Avatar Picker Logic
- [ ] **Load avatar array** from resources in ViewModel
- [ ] **Manage selection state** with LiveData/StateFlow
- [ ] **Remember last selection** when reopening dialog (FR-04)
- [ ] **Handle dialog result** (selected avatar ID) via callback/navigation result
- [ ] **Implement "Reset to default"** option in picker

**Files**: `ui/avatar/AvatarPickerViewModel.kt`
**Success Criteria**: Selection persists during dialog lifecycle, result propagates correctly

#### 2.3 Custom Avatar Import (Optional)
- [ ] **Add "Import from gallery"** option in avatar picker
- [ ] **Implement image picker intent** (`ACTION_PICK` or Activity Result API)
- [ ] **Persist URI with permissions** (`FLAG_GRANT_READ_URI_PERMISSION`)
- [ ] **Validate image accessibility** and handle permission errors
- [ ] **Implement fallback logic** if URI becomes inaccessible (FR-05, FR-11)
- [ ] **Add "Remove custom avatar"** action to reset to resource avatars

**Files**: `ui/avatar/AvatarImportHandler.kt`, updated `AvatarPickerDialog.kt`
**Success Criteria**: Custom avatars persist, fallback to default if URI fails (FR-05, FR-11)

#### 2.4 Avatar Display Component
- [ ] **Create reusable `AvatarView` component** (CircularImageView or custom)
- [ ] **Load resource avatars** via drawable resource ID
- [ ] **Load custom avatars** via URI with Glide/Coil (if using custom imports)
- [ ] **Handle loading errors** gracefully with default avatar fallback
- [ ] **Add content descriptions** for accessibility (e.g., "Avatar: Blue circle") (NFR-06)

**Files**: `ui/components/AvatarView.kt`, `res/layout/view_avatar.xml`
**Success Criteria**: Avatars render consistently, fallback works, accessibility labels present (NFR-06)

### Phase 2 Validation Gates
✅ Avatar picker opens with grid layout and preview
✅ Selection state is visually clear and persists during rotation
✅ Custom avatar import works (if implemented) with fallback
✅ AvatarView component renders all avatar types correctly

---

## 📝 Phase 3: Contact Create/Edit Screens

**Dependencies**: Phase 2 (avatar picker), Phase 1 (data model, theming)
**Duration Estimate**: 3-4 days
**Personas**: Frontend Architect, Backend Developer, UX Designer

### Objectives
- Build/enhance contact create and edit screens
- Integrate avatar picker into create/edit flow
- Implement validation logic with inline error messages
- Ensure state preservation across configuration changes

### Tasks

#### 3.1 Create/Edit Screen UI
- [ ] **Design layout** with avatar selection at top, followed by form fields
- [ ] **Add avatar display** with "Change Avatar" button/icon
- [ ] **Build form fields**: Name (required), Phone (required), Email, Address
- [ ] **Apply Material Design** text field styling from theme
- [ ] **Add Save and Cancel** action buttons (styled via theme)
- [ ] **Ensure touch targets** ≥48dp for accessibility (NFR-06)

**Files**: `ui/contact/ContactEditFragment.kt`, `res/layout/fragment_contact_edit.xml`
**Success Criteria**: Layout matches design, theme applied consistently (NFR-01)

#### 3.2 Form Validation Logic
- [ ] **Validate required fields** (name, phone) on input change
- [ ] **Implement inline error display** (TextInputLayout error messages)
- [ ] **Disable Save button** when form is invalid (FR-03)
- [ ] **Validate phone format** (basic regex or Android PhoneNumberUtils)
- [ ] **Validate email format** (if email field present)
- [ ] **Show meaningful error messages** (e.g., "Name is required", "Invalid phone format") (NFR-05)

**Files**: `ui/contact/ContactEditViewModel.kt`, validation utility functions
**Success Criteria**: Validation works in real-time, errors are human-readable (FR-03, NFR-05)

#### 3.3 Avatar Picker Integration
- [ ] **Launch avatar picker** when "Change Avatar" is tapped
- [ ] **Receive selected avatar ID** from picker dialog result
- [ ] **Update avatar preview** in create/edit screen immediately
- [ ] **Persist avatar selection** in ViewModel state
- [ ] **Handle configuration changes** (rotation) without losing selection (NFR-04)

**Files**: Updated `ContactEditFragment.kt`, `ContactEditViewModel.kt`
**Success Criteria**: Avatar selection integrates smoothly, survives rotation (NFR-04)

#### 3.4 Save & Cancel Actions
- [ ] **Implement Save action**: Validate → persist to DB via Repository
- [ ] **Show loading indicator** during save operation
- [ ] **Navigate back** to contact list on successful save
- [ ] **Handle save errors** with user-friendly messages
- [ ] **Implement Cancel action**: Prompt user if unsaved changes exist
- [ ] **Clear form state** after successful save

**Files**: `ContactEditViewModel.kt`, navigation logic
**Success Criteria**: Save persists contact with avatar, cancel handles unsaved state gracefully

### Phase 3 Validation Gates
✅ Create/Edit screen displays correctly with theme styling
✅ Validation prevents invalid saves and shows inline errors
✅ Avatar picker integration works seamlessly
✅ State survives configuration changes (rotation)

---

## 📱 Phase 4: Contact List & Detail Screens

**Dependencies**: Phase 3 (create/edit), Phase 2 (avatar display), Phase 1 (data layer)
**Duration Estimate**: 4-5 days
**Personas**: Frontend Architect, Performance Engineer, Android Developer

### Objectives
- Build RecyclerView-based contact list with avatars
- Implement search/filter functionality
- Implement sorting options (A→Z, Z→A, Recently added)
- Build polished contact detail screen
- Optimize list performance with DiffUtil

### Tasks

#### 4.1 Contact List RecyclerView
- [ ] **Create `ContactListFragment`** with RecyclerView layout
- [ ] **Design list item layout**: Avatar (left) + Name/Phone (center) + navigation icon (right)
- [ ] **Build `ContactListAdapter`** with ViewHolder pattern
- [ ] **Integrate AvatarView component** in list items
- [ ] **Implement item click listener** to navigate to detail screen
- [ ] **Apply theme styling** to list items and dividers

**Files**: `ui/contact/ContactListFragment.kt`, `ui/contact/ContactListAdapter.kt`, `res/layout/fragment_contact_list.xml`, `res/layout/item_contact.xml`
**Success Criteria**: List displays contacts with avatars, navigation works (FR-06)

#### 4.2 DiffUtil Implementation
- [ ] **Create `ContactDiffCallback`** class extending `DiffUtil.ItemCallback`
- [ ] **Implement `areItemsTheSame()`** (compare by contact ID)
- [ ] **Implement `areContentsTheSame()`** (compare all fields including avatar)
- [ ] **Use `ListAdapter`** with DiffUtil for automatic updates
- [ ] **Verify smooth animations** on create/edit/delete operations

**Files**: `ui/contact/ContactDiffCallback.kt`, updated `ContactListAdapter.kt`
**Success Criteria**: List updates smoothly without jank, DiffUtil handles changes (NFR-03)

#### 4.3 Search/Filter Implementation
- [ ] **Add search input** (SearchView or EditText) to list screen toolbar/top
- [ ] **Implement filter logic** in ViewModel: filter by name OR phone
- [ ] **Debounce search input** to avoid excessive filtering (300ms delay)
- [ ] **Update RecyclerView** in real-time as user types
- [ ] **Show empty state** when no results match filter
- [ ] **Clear filter** with X button or back action

**Files**: `ui/contact/ContactListViewModel.kt`, updated layout
**Success Criteria**: Search filters list in real-time, feels instant (FR-08)

#### 4.4 Sort Options Implementation
- [ ] **Add sort menu** in toolbar (overflow menu or chip group)
- [ ] **Implement sort options**: "A→Z", "Z→A", "Recently Added"
- [ ] **Apply sort in ViewModel** before submitting list to adapter
- [ ] **Persist last sort choice** in SharedPreferences or DataStore
- [ ] **Restore sort order** on app launch (FR-09)
- [ ] **Update UI indicator** to show active sort option

**Files**: `ui/contact/ContactListViewModel.kt`, preferences utility, menu resource
**Success Criteria**: Sorting works correctly, persists across sessions (FR-09)

#### 4.5 Contact Detail Screen
- [ ] **Create `ContactDetailFragment`** with scrollable layout
- [ ] **Display large avatar** at top with centered alignment
- [ ] **Show contact fields**: Name, Phone, Email, Address in clear hierarchy
- [ ] **Add quick action buttons**: Call, SMS, Email (using Intents)
- [ ] **Style detail screen** with Material Design cards or sections
- [ ] **Add Edit and Delete actions** in toolbar or floating action button
- [ ] **Handle Intent availability** gracefully (check if phone/SMS app exists)

**Files**: `ui/contact/ContactDetailFragment.kt`, `res/layout/fragment_contact_detail.xml`
**Success Criteria**: Detail screen is polished with clear hierarchy and quick actions (FR-07)

#### 4.6 Delete Contact Functionality
- [ ] **Add delete action** in detail screen (menu or button)
- [ ] **Show confirmation dialog** before deleting
- [ ] **Delete contact from DB** via Repository
- [ ] **Navigate back** to list screen after deletion
- [ ] **Update list automatically** via LiveData/Flow observation
- [ ] **Handle avatar cleanup** (remove avatar association) (FR-10)

**Files**: Updated `ContactDetailFragment.kt`, `ContactDetailViewModel.kt`
**Success Criteria**: Delete works with confirmation, avatar association removed (FR-10)

### Phase 4 Validation Gates
✅ RecyclerView displays contacts with avatars smoothly
✅ DiffUtil produces smooth animations on updates (NFR-03)
✅ Search/filter works in real-time without lag (FR-08)
✅ Sort options persist and apply correctly (FR-09)
✅ Detail screen is polished with quick actions working (FR-07)
✅ Delete functionality works with proper cleanup (FR-10)

---

## 🎨 Phase 5: Polish, Performance & Accessibility

**Dependencies**: Phase 4 (all UI screens), Phase 3 (create/edit), Phase 2 (avatar system)
**Duration Estimate**: 2-3 days
**Personas**: Performance Engineer, UX Designer, Accessibility Specialist

### Objectives
- Optimize app performance and scrolling
- Enhance accessibility features
- Refine UI polish and user experience
- Ensure robust error handling and fallbacks

### Tasks

#### 5.1 Performance Optimization
- [ ] **Profile RecyclerView scrolling** with Android Profiler
- [ ] **Optimize avatar loading**: Use image caching library (Glide/Coil) if custom avatars
- [ ] **Minimize layout inflation time**: Review layout complexity
- [ ] **Use view binding** for efficient view access (if not already)
- [ ] **Optimize database queries**: Add indexes if needed for search/sort
- [ ] **Verify smooth 60fps scrolling** on target devices

**Files**: Performance profiling, optimized adapters and queries
**Success Criteria**: Smooth scrolling and instant interactions on typical devices (NFR-02)

#### 5.2 Accessibility Enhancements
- [ ] **Add content descriptions** to all avatars (NFR-06 completed in Phase 2, verify)
- [ ] **Verify focus order** in create/edit and detail screens
- [ ] **Test with TalkBack** screen reader
- [ ] **Ensure touch targets** ≥48dp across all interactive elements (NFR-06)
- [ ] **Add meaningful labels** to icon buttons and actions
- [ ] **Test keyboard navigation** (if applicable for tablet/ChromeOS)

**Files**: Updated layouts and components
**Success Criteria**: App passes basic accessibility audit, TalkBack navigable (NFR-06)

#### 5.3 Error Handling & Fallbacks
- [ ] **Test avatar resource loading failure** scenarios
- [ ] **Test custom avatar URI inaccessibility** (if custom imports implemented)
- [ ] **Ensure default avatar fallback** triggers correctly (FR-11)
- [ ] **Handle database errors** gracefully with user messages
- [ ] **Test network-free operation** (should work 100% offline) (NFR-07)
- [ ] **Verify no crashes** from missing resources or permissions

**Files**: Error handling in ViewModels, Repository, and UI components
**Success Criteria**: App never crashes, always shows default avatar fallback (FR-11)

#### 5.4 UI/UX Polish Pass
- [ ] **Review all screens** for theme consistency (colors, typography, spacing)
- [ ] **Ensure no hard-coded values** in layouts (use dimens, colors, strings)
- [ ] **Add subtle animations**: Fade-ins, slide transitions, button press states
- [ ] **Refine spacing and alignment** using layout inspector
- [ ] **Test on multiple screen sizes**: Phone, tablet, landscape
- [ ] **Verify status bar and navigation bar** styling

**Files**: All layout and style resources
**Success Criteria**: App feels polished and consistent, theme applied everywhere (NFR-01)

#### 5.5 State Preservation
- [ ] **Test configuration changes** (rotation) across all screens
- [ ] **Verify ViewModel state survival**: Form data, avatar selection, filter/sort state
- [ ] **Test process death scenarios** with "Don't keep activities" enabled
- [ ] **Ensure SavedStateHandle** usage for critical transient state (if needed)

**Files**: ViewModels with SavedStateHandle, proper state management
**Success Criteria**: No data loss on rotation or process death (NFR-04)

### Phase 5 Validation Gates
✅ App scrolls smoothly at 60fps with no jank (NFR-02, NFR-03)
✅ Accessibility passes TalkBack testing (NFR-06)
✅ All error scenarios handled gracefully, no crashes (FR-11, NFR-07)
✅ UI/UX is polished and theme-consistent (NFR-01)
✅ State survives configuration changes (NFR-04)

---

## 📚 Phase 6: Documentation & Submission Preparation

**Dependencies**: Phase 5 (all implementation complete)
**Duration Estimate**: 2-3 days
**Personas**: Technical Writer, Developer

### Objectives
- Prepare logbook with screenshots and code snippets
- Document implementation decisions and evidence
- Package submission files (PDF, ZIP, APK)
- Final review and quality assurance

### Tasks

#### 6.1 Screenshot Collection
- [ ] **Capture contact list** with avatars displayed
- [ ] **Capture create/edit screen** with avatar picker open
- [ ] **Capture avatar picker dialog** showing grid and preview
- [ ] **Capture contact detail screen** with large avatar and quick actions
- [ ] **Capture search/filter** in action with results
- [ ] **Capture sort options** menu and sorted lists
- [ ] **Capture validation errors** (inline error messages)
- [ ] **Capture custom avatar import** (if implemented)

**Success Criteria**: All key features documented visually (FR-12)

#### 6.2 Code Snippet Documentation
- [ ] **Extract Contact entity** code with avatar fields
- [ ] **Extract avatar picker dialog** setup and selection logic
- [ ] **Extract RecyclerView adapter** with DiffUtil implementation
- [ ] **Extract search/filter** ViewModel logic
- [ ] **Extract theme/style** examples from resources
- [ ] **Paste actual code** (not screenshots) with syntax highlighting in logbook
- [ ] **Add brief explanations** for each code snippet (FR-12)

**Files**: Logbook document (Word/PDF)
**Success Criteria**: Logbook has actual code snippets with explanations (NFR-08, FR-12)

#### 6.3 Implementation Evidence
- [ ] **Document database migration** approach and testing
- [ ] **Document avatar resource strategy** and fallback logic
- [ ] **Document DiffUtil implementation** and performance impact
- [ ] **Document theme/style usage** with resource file references (NFR-01)
- [ ] **Document accessibility features** implemented (NFR-06)
- [ ] **Document offline operation** and privacy approach (NFR-07)

**Files**: Logbook document
**Success Criteria**: All NFRs and FRs have supporting evidence

#### 6.4 Submission Package Assembly
- [ ] **Generate APK**: Build → Build Bundle(s) / APK(s) → Build APK(s)
- [ ] **Test APK installation** on physical device or emulator
- [ ] **Create project ZIP**: Include source code, resources, gradle files
- [ ] **Verify ZIP contents**: No sensitive data, all files included
- [ ] **Convert logbook to PDF** (single PDF submission) (NFR-08)
- [ ] **Final naming check**: Follow submission naming conventions

**Files**: `ContactAvatarPlus.apk`, `ContactAvatarPlus_Source.zip`, `Logbook.pdf`
**Success Criteria**: Submission package ready per CW2 requirements (NFR-08)

#### 6.5 Final Quality Review
- [ ] **Test complete user journey**: Create → View → Edit → Delete
- [ ] **Test search and sort** across different data sets
- [ ] **Test avatar persistence** across app restarts
- [ ] **Verify no hard-coded strings** or styling
- [ ] **Verify no TODO comments** or incomplete features
- [ ] **Run lint checks** and fix warnings
- [ ] **Code review pass**: Check for clean code practices

**Success Criteria**: App is production-ready and submission-compliant

### Phase 6 Validation Gates
✅ Logbook complete with screenshots and actual code snippets (FR-12)
✅ All requirements documented with evidence
✅ Submission package assembled per requirements (NFR-08)
✅ Final quality review passed, no blocking issues

---

## 🔄 Cross-Phase Considerations

### Dependency Management
- **Phase 1 → All**: Data model and theming foundation required everywhere
- **Phase 2 → Phase 3, 4**: Avatar picker must be ready for create/edit and list display
- **Phase 3 → Phase 4**: Create/edit functionality needed before list can be properly tested
- **Phase 4 → Phase 5**: All screens must exist before polish and optimization
- **Phase 5 → Phase 6**: Implementation must be complete before documentation

### Continuous Validation
Throughout all phases, continuously validate:
- ✅ **Theme consistency** (NFR-01): All new UI uses theme resources
- ✅ **Accessibility** (NFR-06): Content descriptions and touch targets
- ✅ **Offline operation** (NFR-07): No network dependencies
- ✅ **State preservation** (NFR-04): Test rotation frequently

### Risk Mitigation
- **Database Migration Risk**: Test migrations thoroughly in Phase 1 before building on top
- **Performance Risk**: Profile early in Phase 4, don't wait until Phase 5
- **Custom Avatar Risk**: Implement fallback logic in Phase 2 before UI integration
- **Submission Risk**: Review requirements in Phase 6 early to avoid last-minute issues

---

## ✅ Success Criteria Summary

**Phase 1**: Data model extended, avatars in resources, theme established
**Phase 2**: Avatar picker functional with grid, preview, and selection state
**Phase 3**: Create/edit screens with validation and avatar integration
**Phase 4**: List, detail, search, sort all working smoothly with DiffUtil
**Phase 5**: Performance optimized, accessibility verified, UI polished
**Phase 6**: Logbook complete, submission package ready

**Overall Success**: All functional requirements (FR-01 to FR-12) and non-functional requirements (NFR-01 to NFR-08) met with documented evidence.

---

## 📊 Estimated Timeline

| Phase | Duration | Parallel Work Opportunities |
|-------|----------|----------------------------|
| Phase 1 | 2-3 days | Theme work parallel to data model work |
| Phase 2 | 3-4 days | Custom avatar import can be done in parallel with picker UI |
| Phase 3 | 3-4 days | Validation logic parallel to UI layout work |
| Phase 4 | 4-5 days | Detail screen parallel to list optimization |
| Phase 5 | 2-3 days | Accessibility testing parallel to performance profiling |
| Phase 6 | 2-3 days | Documentation writing parallel to package assembly |
| **Total** | **16-22 days** | With parallelization: **12-18 days** |

---

## 🎓 Educational Alignment

This workflow explicitly demonstrates the educational requirements:
- ✅ **Android Persistence**: Room database with migrations (Phase 1)
- ✅ **Resource Management**: Avatar drawables and resource arrays (Phase 1, 2)
- ✅ **RecyclerView**: List with ViewHolder and DiffUtil (Phase 4)
- ✅ **Theme/Style Usage**: Comprehensive theming throughout (All phases)
- ✅ **Validation & UX**: Inline validation and helpful messages (Phase 3)
- ✅ **Accessibility**: Content descriptions and touch targets (Phase 5)
- ✅ **Offline Operation**: No network dependencies (All phases)
- ✅ **Submission Evidence**: Logbook with screenshots and actual code (Phase 6)

---

**Workflow Version**: 1.0
**Last Updated**: 2025-10-20
**Next Steps**: Begin Phase 1 - Foundation & Architecture Setup
