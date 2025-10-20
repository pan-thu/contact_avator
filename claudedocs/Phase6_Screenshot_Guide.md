# Phase 6: Screenshot Collection Guide

## Overview
This guide documents the screenshots needed for the ContactAvatar+ application submission. Screenshots should demonstrate all key features and user workflows.

## Screenshot Requirements

### 1. Contact List with Avatars
**File Name**: `screenshot_01_contact_list.png`

**What to Capture**:
- Main contact list screen showing multiple contacts
- Each contact should display their assigned avatar
- Ensure variety of avatars (different colors/initials) are visible
- Show at least 5-7 contacts for comprehensive view
- Floating Action Button (FAB) should be visible

**Key Features to Highlight**:
- Avatar images displayed correctly in list items
- Contact names and phone numbers
- Consistent spacing and layout
- Material Design styling

---

### 2. Create Contact Screen
**File Name**: `screenshot_02_create_contact.png`

**What to Capture**:
- Create contact screen with empty form fields
- Avatar picker button/area clearly visible
- All input fields (Name, Phone, Email) shown
- Save button visible
- Toolbar with "Add Contact" title

**Key Features to Highlight**:
- Clean, organized form layout
- Clear avatar selection area
- Material Design text fields
- Action buttons

---

### 3. Avatar Picker Dialog
**File Name**: `screenshot_03_avatar_picker.png`

**What to Capture**:
- Avatar picker dialog in opened state
- Grid of avatar options visible (all 10 avatars)
- Preview of selected avatar (if applicable)
- Dialog title and buttons
- Currently selected avatar highlighted

**Key Features to Highlight**:
- Complete grid layout showing all avatar options
- Visual indication of selection
- Dialog design and spacing
- Color variety in avatars

---

### 4. Avatar Selection Preview
**File Name**: `screenshot_04_avatar_selected.png`

**What to Capture**:
- Create/edit screen AFTER avatar selection
- Chosen avatar displayed in the avatar area
- Form fields can be partially filled
- Shows the selected avatar integrated into the form

**Key Features to Highlight**:
- Avatar successfully assigned
- Visual confirmation of selection
- Integration with contact form

---

### 5. Contact Detail Screen
**File Name**: `screenshot_05_contact_detail.png`

**What to Capture**:
- Contact detail screen for a single contact
- Large avatar display at top
- Contact information (name, phone, email)
- Quick action buttons (Call, Edit)
- Delete option in menu/toolbar

**Key Features to Highlight**:
- Large, clear avatar display
- Organized information layout
- Action buttons for user interactions
- Material Design card/layout

---

### 6. Edit Contact Screen
**File Name**: `screenshot_06_edit_contact.png`

**What to Capture**:
- Edit screen with existing contact data
- Pre-filled form fields
- Current avatar displayed
- Option to change avatar visible
- Update/Save button shown

**Key Features to Highlight**:
- Data persistence (fields pre-populated)
- Avatar change capability
- Edit functionality

---

### 7. Search Functionality
**File Name**: `screenshot_07_search_active.png`

**What to Capture**:
- Search icon/field in toolbar
- Search query entered (e.g., "John")
- Filtered results showing matching contacts
- Contacts that don't match query are hidden

**Key Features to Highlight**:
- Real-time search/filtering
- Search UI integration
- Results accuracy

---

### 8. Search Results
**File Name**: `screenshot_08_search_results.png`

**What to Capture**:
- List filtered by search query
- Only matching contacts visible
- Search query visible in search field
- Clear search option (if available)

**Key Features to Highlight**:
- Search effectiveness
- Filtered list display
- User feedback

---

### 9. Sort Options Menu
**File Name**: `screenshot_09_sort_menu.png`

**What to Capture**:
- Overflow menu or toolbar menu opened
- Sort options visible (Name A-Z, Name Z-A, Recently Added)
- Current sort option indicated (if possible)
- Other menu options (if any)

**Key Features to Highlight**:
- Sort options availability
- Menu design
- User choice clarity

---

### 10. Sorted List (A-Z)
**File Name**: `screenshot_10_sorted_az.png`

**What to Capture**:
- Contact list sorted alphabetically A-Z
- Visible alphabetical ordering (Adam, Bob, Charlie, etc.)
- Multiple contacts to show clear ordering

**Key Features to Highlight**:
- Correct alphabetical sorting
- List organization

---

### 11. Sorted List (Z-A)
**File Name**: `screenshot_11_sorted_za.png`

**What to Capture**:
- Contact list sorted reverse alphabetically Z-A
- Visible reverse ordering (Zoe, Yara, Xavier, etc.)
- Different from A-Z sort

**Key Features to Highlight**:
- Reverse sort functionality
- Clear ordering difference

---

### 12. Validation Error - Empty Name
**File Name**: `screenshot_12_validation_name.png`

**What to Capture**:
- Create/edit screen with empty name field
- Error message displayed below name field
- "Name is required" or similar message
- Error state styling (red text/underline)

**Key Features to Highlight**:
- Inline validation
- Clear error messaging
- User guidance

---

### 13. Validation Error - Invalid Phone
**File Name**: `screenshot_13_validation_phone.png`

**What to Capture**:
- Create/edit screen with invalid phone number
- Error message for phone field
- "Invalid phone format" or similar message
- Error state visual indicator

**Key Features to Highlight**:
- Phone validation logic
- Error feedback
- Format requirements

---

### 14. Empty State (Optional)
**File Name**: `screenshot_14_empty_state.png`

**What to Capture**:
- Contact list with no contacts
- Empty state message or illustration
- FAB button still visible
- Guidance for adding first contact

**Key Features to Highlight**:
- User-friendly empty state
- Call to action

---

### 15. Delete Confirmation (Optional)
**File Name**: `screenshot_15_delete_dialog.png`

**What to Capture**:
- Delete confirmation dialog
- Contact name/details in confirmation message
- Confirm and Cancel buttons
- Warning message

**Key Features to Highlight**:
- Destructive action confirmation
- User safety mechanism

---

## Screenshot Instructions

### Using Android Studio Emulator

#### Step 1: Launch Emulator
1. Open Android Studio
2. Click **Tools** → **Device Manager**
3. Start your preferred emulator (Pixel 6 or similar recommended)
4. Wait for emulator to fully boot

#### Step 2: Run Application
1. In Android Studio, click **Run** button (▶) or press `Shift+F10`
2. Select the running emulator
3. Wait for application to install and launch

#### Step 3: Prepare Test Data
1. Create 5-7 test contacts with different:
   - Names (varying lengths, alphabetical diversity)
   - Phone numbers (different formats)
   - Email addresses
   - Avatar selections (use all 10 avatar options)

2. Example test contacts:
   - Alice Anderson (555-0101) - Avatar 1
   - Bob Brown (555-0102) - Avatar 2
   - Charlie Chen (555-0103) - Avatar 3
   - Diana Davis (555-0104) - Avatar 4
   - Eve Evans (555-0105) - Avatar 5
   - Frank Foster (555-0106) - Avatar 6
   - Grace Green (555-0107) - Avatar 7

#### Step 4: Capture Screenshots
1. Navigate to the screen you want to capture
2. Use **Camera icon** in emulator toolbar (right side)
3. Or click **Screenshot** in emulator controls
4. Screenshot saves to: `~/Screenshots/` or specified directory

**Alternative Methods**:
- **Windows**: Emulator controls → Camera icon
- **Mac**: Emulator controls → Camera icon
- **Manual**: Use OS screenshot tool (Windows: Win+Shift+S, Mac: Cmd+Shift+4)

#### Step 5: Organize Screenshots
1. Rename files according to naming convention above
2. Store in dedicated folder: `ContactAvatar_Screenshots/`
3. Verify all 13-15 screenshots are clear and complete

---

## Screenshot Quality Guidelines

### Technical Requirements
- **Resolution**: Minimum 720x1280 (emulator default)
- **Format**: PNG (preferred) or JPG
- **Orientation**: Portrait mode
- **Clarity**: No blur, all text readable

### Content Requirements
- **Complete UI**: No cut-off elements
- **Representative Data**: Use realistic test data
- **Feature Visibility**: Key features clearly shown
- **Consistency**: Same device/theme across all screenshots

### Best Practices
- Use light theme for consistency (unless dark theme required)
- Ensure good contrast for readability
- Hide emulator controls if possible
- Capture at actual size (no scaling)
- Remove personal/sensitive test data

---

## Screenshot Checklist

Before finalizing screenshots, verify:

- [ ] All 13 required screenshots captured
- [ ] Files named according to convention
- [ ] All features clearly visible
- [ ] No placeholder/debug data visible
- [ ] Consistent device and theme
- [ ] High quality and readable
- [ ] Organized in dedicated folder
- [ ] Ready for inclusion in logbook

---

## Integration with Logbook

### Where to Use Screenshots

1. **Feature Demonstrations**:
   - Each functional requirement should have supporting screenshot
   - Reference screenshot numbers in requirement descriptions

2. **User Journey Documentation**:
   - Include screenshots in workflow section
   - Show complete create → view → edit → delete flow

3. **UI/UX Evidence**:
   - Use screenshots to demonstrate Material Design compliance
   - Show accessibility features (error messages, clear labels)

4. **Validation Evidence**:
   - Screenshots of error messages prove validation implementation
   - Before/after screenshots show edit functionality

### Screenshot References Format
```markdown
**Screenshot 01**: Contact List - Shows avatar display in RecyclerView
**Screenshot 03**: Avatar Picker - Demonstrates grid layout and selection UI
**Screenshot 12**: Validation - Inline error message for empty name field
```

---

## Submission Package
Place all screenshots in submission folder:
```
ContactAvatar_Submission/
├── screenshots/
│   ├── screenshot_01_contact_list.png
│   ├── screenshot_02_create_contact.png
│   ├── screenshot_03_avatar_picker.png
│   └── ... (all remaining screenshots)
├── logbook.pdf
├── ContactAvatar.apk
└── ContactAvatar_SourceCode.zip
```

---

## Notes
- Screenshot file size should be reasonable (< 2MB each)
- Total screenshot folder should be < 20MB
- If using dark theme, maintain consistency across all screenshots
- Consider capturing both light and dark themes if bonus feature implemented
- Screenshots should match code implementation exactly
