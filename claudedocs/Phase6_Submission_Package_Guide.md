# Phase 6: Submission Package Guide

## Overview
This guide provides step-by-step instructions for generating and preparing all required submission files for the ContactAvatar+ application.

---

## Table of Contents
1. [APK Generation](#apk-generation)
2. [Source Code ZIP Creation](#source-code-zip-creation)
3. [Logbook PDF Conversion](#logbook-pdf-conversion)
4. [Screenshots Organization](#screenshots-organization)
5. [Final Package Assembly](#final-package-assembly)
6. [Submission Checklist](#submission-checklist)

---

## 1. APK Generation

### Step 1: Clean Project
1. In Android Studio, click **Build** → **Clean Project**
2. Wait for clean operation to complete
3. Check Build output for any errors

### Step 2: Build APK
1. Click **Build** → **Build Bundle(s) / APK(s)** → **Build APK(s)**
2. Android Studio will start the build process
3. Wait for "BUILD SUCCESSFUL" message in Build output
4. Build typically takes 30-60 seconds

### Step 3: Locate APK
1. When build completes, notification appears: "APK(s) generated successfully"
2. Click **locate** in the notification
3. Or navigate manually to:
   ```
   /mnt/c/Users/panth/Documents/Projects/ContactAvatorApplication/app/build/outputs/apk/debug/
   ```
4. File will be named: `app-debug.apk`

### Step 4: Rename APK
1. Copy `app-debug.apk` to your submission folder
2. Rename to: `ContactAvatar.apk`
3. Verify file size (typically 2-5 MB for this project)

### Alternative: Gradle Command Line
```bash
cd /mnt/c/Users/panth/Documents/Projects/ContactAvatorApplication
./gradlew assembleDebug
```
APK will be in same location: `app/build/outputs/apk/debug/app-debug.apk`

### Troubleshooting APK Build

**Build Fails with Errors**:
- Check Build output for specific error messages
- Verify all code files compile without errors
- Run **Build** → **Rebuild Project** to force full rebuild
- Ensure no lint errors prevent build

**APK Not Found**:
- Check exact path: `app/build/outputs/apk/debug/`
- Ensure build completed successfully (check Build tab)
- Try **Build** → **Clean Project** then rebuild

**APK Too Large (> 10 MB)**:
- Normal size: 2-5 MB for this project
- If larger, check for unnecessary resources
- Verify no extra libraries in dependencies

---

## 2. Source Code ZIP Creation

### What to Include

#### Required Files and Folders
```
ContactAvatorApplication/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/contactavatar/
│   │   │   │   ├── data/
│   │   │   │   ├── ui/
│   │   │   │   ├── viewmodel/
│   │   │   │   └── utils/
│   │   │   ├── res/
│   │   │   │   ├── drawable/
│   │   │   │   ├── layout/
│   │   │   │   ├── menu/
│   │   │   │   ├── values/
│   │   │   │   └── mipmap-*/
│   │   │   └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle.kts (project level)
├── settings.gradle.kts
├── gradle.properties
└── gradlew, gradlew.bat
```

#### Files/Folders to EXCLUDE
```
DO NOT INCLUDE:
├── .gradle/              # Gradle cache
├── .idea/                # Android Studio settings
├── app/build/            # Build outputs
├── build/                # Build directory
├── local.properties      # Local machine paths
├── *.iml                 # IntelliJ/Android Studio files
├── .DS_Store             # Mac system files
└── claudedocs/           # Development documentation (optional)
```

### Method 1: Create ZIP Manually (Windows)

#### Step 1: Navigate to Project
1. Open File Explorer
2. Navigate to: `C:\Users\panth\Documents\Projects\`
3. Locate `ContactAvatorApplication` folder

#### Step 2: Create Clean Copy
1. Copy entire `ContactAvatorApplication` folder
2. Paste to temporary location (e.g., Desktop)
3. Rename copy to: `ContactAvatar_SourceCode`

#### Step 3: Clean Temporary Copy
Delete these folders/files from the copy:
- `.gradle` folder
- `.idea` folder
- `app/build` folder
- `build` folder
- `local.properties` file
- Any `.iml` files
- `claudedocs` folder (if you don't want to include documentation)

#### Step 4: Create ZIP
1. Right-click the `ContactAvatar_SourceCode` folder
2. Select **Send to** → **Compressed (zipped) folder**
3. Windows creates `ContactAvatar_SourceCode.zip`
4. Verify ZIP size (should be 1-3 MB)

### Method 2: Create ZIP via Command Line

#### Using Linux/WSL
```bash
# Navigate to project parent directory
cd /mnt/c/Users/panth/Documents/Projects/

# Create ZIP excluding build files
zip -r ContactAvatar_SourceCode.zip ContactAvatorApplication \
  -x "*.gradle/*" \
  -x "*/.idea/*" \
  -x "*/build/*" \
  -x "*/local.properties" \
  -x "*.iml" \
  -x "*/.DS_Store" \
  -x "*/claudedocs/*"

# Verify ZIP created
ls -lh ContactAvatar_SourceCode.zip
```

#### Using PowerShell (Windows)
```powershell
# Navigate to project directory
cd C:\Users\panth\Documents\Projects\ContactAvatorApplication

# Create ZIP using PowerShell
Compress-Archive -Path . -DestinationPath ..\ContactAvatar_SourceCode.zip `
  -Force `
  -CompressionLevel Optimal
```

Note: PowerShell method includes everything. Manual cleanup of extracted folder recommended.

### Method 3: Git Clean Export (If Using Git)

```bash
cd /mnt/c/Users/panth/Documents/Projects/ContactAvatorApplication

# Export clean repository
git archive --format=zip --output=../ContactAvatar_SourceCode.zip HEAD
```

This automatically excludes .gitignored files (build/, .idea/, etc.)

### Verify ZIP Contents

#### Step 1: Extract to Test Location
1. Extract ZIP to temporary folder
2. Verify folder structure matches expected layout

#### Step 2: Verify Essential Files Present
Check for:
- [ ] `app/src/main/java/com/example/contactavatar/` - All Kotlin files
- [ ] `app/src/main/res/` - All resource folders
- [ ] `app/src/main/AndroidManifest.xml`
- [ ] `app/build.gradle.kts`
- [ ] Project `build.gradle.kts`
- [ ] `settings.gradle.kts`
- [ ] `gradle/wrapper/` files

#### Step 3: Verify Exclusions
Confirm these are NOT present:
- [ ] No `.gradle/` folder
- [ ] No `.idea/` folder
- [ ] No `build/` folders
- [ ] No `local.properties`
- [ ] No `.iml` files

#### Step 4: Test Build (Optional but Recommended)
1. Open extracted project in Android Studio
2. Let Gradle sync complete
3. Build → Clean Project
4. Build → Build APK
5. Verify successful build from clean source

---

## 3. Logbook PDF Conversion

### Prepare Logbook Document

#### Step 1: Complete Logbook Template
1. Open `Phase6_Logbook_Template.md` from claudedocs/
2. Fill in all sections with your information:
   - Student name and ID
   - Submission date
   - Any customization needed
3. Insert screenshots in appropriate sections
4. Verify all code snippets are present and formatted

#### Step 2: Format for Conversion
If using Markdown editor:
- Ensure proper heading hierarchy
- Verify code blocks have language tags
- Check image references are correct
- Review table formatting

### Method 1: Use Markdown to PDF Converter

#### Option A: Online Converter (Quick)
1. Go to: https://www.markdowntopdf.com/
2. Upload your completed logbook .md file
3. Click "Convert"
4. Download generated PDF
5. Rename to: `ContactAvatar_Logbook.pdf`

#### Option B: Pandoc (Professional)
```bash
# Install pandoc (if not installed)
sudo apt install pandoc

# Convert Markdown to PDF
pandoc Phase6_Logbook_Template.md \
  -o ContactAvatar_Logbook.pdf \
  --pdf-engine=pdflatex \
  -V geometry:margin=1in \
  --toc \
  --number-sections

# Or with custom styling
pandoc Phase6_Logbook_Template.md \
  -o ContactAvatar_Logbook.pdf \
  --pdf-engine=pdflatex \
  -V geometry:margin=1in \
  -V fontsize=12pt \
  -V linestretch=1.5 \
  --toc \
  --number-sections
```

#### Option C: Visual Studio Code (With Extension)
1. Install "Markdown PDF" extension
2. Open logbook .md file in VS Code
3. Press `Ctrl+Shift+P` (Windows) or `Cmd+Shift+P` (Mac)
4. Type "Markdown PDF: Export (pdf)"
5. Select export option
6. PDF saved in same directory
7. Rename appropriately

### Method 2: Google Docs Conversion

#### Step 1: Convert Markdown to Google Docs
1. Open Google Docs
2. Install "Docs to Markdown" add-on (or similar)
3. Copy logbook content from .md file
4. Paste into Google Doc
5. Apply formatting (headings, code blocks, etc.)

#### Step 2: Insert Screenshots
1. Insert → Image → Upload from computer
2. Add each screenshot in appropriate section
3. Add captions below each image
4. Resize images appropriately (width: 80% of page)

#### Step 3: Format Document
1. Apply heading styles (Heading 1, 2, 3)
2. Format code blocks with monospace font and gray background
3. Apply consistent spacing
4. Add page numbers (Insert → Page numbers)
5. Add table of contents (Insert → Table of contents)

#### Step 4: Export to PDF
1. File → Download → PDF Document (.pdf)
2. Save as: `ContactAvatar_Logbook.pdf`
3. Verify PDF quality and formatting

### Method 3: Microsoft Word Conversion

#### Step 1: Import Markdown
1. Open Microsoft Word
2. Install "Writage" plugin (Markdown plugin for Word)
3. Open logbook .md file directly
4. Word converts to formatted document

#### Step 2: Format and Export
1. Review formatting, adjust as needed
2. Insert screenshots
3. Apply styles and formatting
4. File → Save As → PDF
5. Save as: `ContactAvatar_Logbook.pdf`

### PDF Quality Checklist

Before finalizing PDF:
- [ ] All sections present and complete
- [ ] All screenshots inserted and visible
- [ ] Code snippets properly formatted and readable
- [ ] Page numbers present
- [ ] Table of contents accurate
- [ ] Student information filled in
- [ ] No formatting errors
- [ ] File size reasonable (< 10 MB)
- [ ] All pages readable at 100% zoom
- [ ] Headings properly styled

---

## 4. Screenshots Organization

### Organize Screenshots in Folder

#### Step 1: Create Screenshots Folder
```
ContactAvatar_Submission/
└── screenshots/
    ├── screenshot_01_contact_list.png
    ├── screenshot_02_create_contact.png
    ├── screenshot_03_avatar_picker.png
    ├── screenshot_04_avatar_selected.png
    ├── screenshot_05_contact_detail.png
    ├── screenshot_06_edit_contact.png
    ├── screenshot_07_search_active.png
    ├── screenshot_08_search_results.png
    ├── screenshot_09_sort_menu.png
    ├── screenshot_10_sorted_az.png
    ├── screenshot_11_sorted_za.png
    ├── screenshot_12_validation_name.png
    └── screenshot_13_validation_phone.png
```

#### Step 2: Verify Screenshot Quality
For each screenshot:
- [ ] File name follows naming convention
- [ ] Image is clear and readable
- [ ] Resolution adequate (720x1280 minimum)
- [ ] File format is PNG or JPG
- [ ] File size reasonable (< 500 KB each)
- [ ] Content matches description
- [ ] UI elements fully visible (no cut-offs)

#### Step 3: Create Screenshots Index (Optional)
Create `screenshots/README.txt`:
```
ContactAvatar+ Application Screenshots
======================================

Screenshot 01: Contact List - Main screen with contacts and avatars
Screenshot 02: Create Contact - Form for adding new contact
Screenshot 03: Avatar Picker - Dialog showing all 10 avatar options
Screenshot 04: Avatar Selected - Form with chosen avatar
Screenshot 05: Contact Detail - Individual contact view with actions
Screenshot 06: Edit Contact - Editing existing contact
Screenshot 07: Search Active - Search bar with query entered
Screenshot 08: Search Results - Filtered contact list
Screenshot 09: Sort Menu - Options menu with sort choices
Screenshot 10: Sorted A-Z - Alphabetically sorted list
Screenshot 11: Sorted Z-A - Reverse alphabetical sort
Screenshot 12: Validation Name - Error message for empty name
Screenshot 13: Validation Phone - Error for invalid phone format
```

---

## 5. Final Package Assembly

### Recommended Submission Structure

```
ContactAvatar_Submission/
├── ContactAvatar.apk                        # Android application file
├── ContactAvatar_SourceCode.zip             # Complete source code
├── ContactAvatar_Logbook.pdf                # Project logbook/report
├── screenshots/                             # Application screenshots folder
│   ├── screenshot_01_contact_list.png
│   ├── screenshot_02_create_contact.png
│   ├── ... (all 13+ screenshots)
│   └── README.txt (optional)
└── README.txt                               # Submission package description
```

### Create Master README.txt

Create `ContactAvatar_Submission/README.txt`:
```
ContactAvatar+ Application - Submission Package
================================================

Student Information:
- Name: [Your Name]
- Student ID: [Your ID]
- Module: Mobile Application Development
- Submission Date: [Date]

Package Contents:
-----------------

1. ContactAvatar.apk
   - Installable Android application (debug build)
   - Install on Android device (API 24+) or emulator
   - File size: ~3-5 MB

2. ContactAvatar_SourceCode.zip
   - Complete Android Studio project
   - Extract and open in Android Studio Hedgehog or later
   - All source files, resources, and build configuration included
   - File size: ~1-3 MB

3. ContactAvatar_Logbook.pdf
   - Complete project documentation and logbook
   - Includes: requirements analysis, architecture, implementation details,
     code snippets, screenshots, testing evidence
   - File size: ~5-10 MB

4. screenshots/
   - 13+ application screenshots demonstrating all features
   - PNG format, 720x1280 resolution
   - Organized with descriptive file names

Application Features:
---------------------
- Contact management (Create, Read, Update, Delete)
- Avatar selection from 10 pre-designed options
- Real-time search and filtering
- Multiple sort options (A-Z, Z-A, Recently Added)
- Form validation with error messages
- Material Design UI
- Offline operation with local database
- MVVM architecture

Technical Stack:
----------------
- Language: Kotlin
- Architecture: MVVM (Model-View-ViewModel)
- Database: Room Persistence Library
- UI: Android Views (XML layouts)
- Async: Kotlin Coroutines + LiveData
- Min SDK: 24 (Android 7.0)
- Target SDK: 34 (Android 14)

Installation Instructions (APK):
---------------------------------
1. Transfer ContactAvatar.apk to Android device
2. Enable "Install from Unknown Sources" in device settings
3. Open APK file and tap "Install"
4. Launch "ContactAvatar+" from app drawer

Build Instructions (Source Code):
----------------------------------
1. Extract ContactAvatar_SourceCode.zip
2. Open Android Studio
3. File → Open → Select extracted folder
4. Wait for Gradle sync to complete
5. Build → Clean Project
6. Build → Build APK or Run on emulator/device

Contact:
--------
For questions or issues, contact: [Your Email]

Submission Declaration:
-----------------------
I declare that this is my own work and all sources have been
appropriately acknowledged.

Signature: [Your Name]
Date: [Submission Date]
```

### Assembly Checklist

Before creating final submission package:

#### Files Present
- [ ] ContactAvatar.apk (2-5 MB)
- [ ] ContactAvatar_SourceCode.zip (1-3 MB)
- [ ] ContactAvatar_Logbook.pdf (5-10 MB)
- [ ] screenshots/ folder with 13+ images
- [ ] README.txt

#### File Verification
- [ ] APK installs and runs successfully
- [ ] Source code ZIP extracts correctly
- [ ] Source code builds successfully in Android Studio
- [ ] PDF opens and is readable
- [ ] All screenshots clear and properly named

#### Content Verification
- [ ] Student information filled in all documents
- [ ] No placeholder text remaining
- [ ] All code snippets in logbook are accurate
- [ ] All screenshots match current implementation
- [ ] README.txt information is accurate

#### Naming Convention
- [ ] File names follow specified format
- [ ] No spaces in file names (use underscores)
- [ ] Consistent capitalization
- [ ] Version numbers removed (e.g., no "v1", "final2")

---

## 6. Submission Checklist

### Pre-Submission Verification

#### Application Testing
- [ ] Install APK on physical device or emulator
- [ ] Test complete user journey (Create → View → Edit → Delete)
- [ ] Verify search functionality works
- [ ] Verify all sort options work
- [ ] Test form validation (try invalid inputs)
- [ ] Verify avatar selection and display
- [ ] Test app restart - data persists
- [ ] No crashes or errors during testing

#### Source Code Verification
- [ ] Extract source code ZIP to new location
- [ ] Open in fresh Android Studio instance
- [ ] Gradle sync completes without errors
- [ ] Build → Clean Project succeeds
- [ ] Build → Build APK succeeds
- [ ] Generated APK runs correctly
- [ ] No missing files or broken references

#### Documentation Verification
- [ ] Logbook PDF opens in PDF reader
- [ ] All sections complete and filled in
- [ ] Student information present
- [ ] All screenshots inserted and visible
- [ ] Code snippets properly formatted
- [ ] No "TODO" or "[INSERT]" placeholders
- [ ] Table of contents accurate
- [ ] Page numbers correct
- [ ] Professional formatting throughout

#### Screenshots Verification
- [ ] All 13+ required screenshots present
- [ ] Screenshots follow naming convention
- [ ] Images are clear and readable
- [ ] UI elements not cut off
- [ ] Screenshots represent current app state
- [ ] File sizes reasonable (< 500 KB each)
- [ ] Total screenshot folder < 10 MB

#### Package Integrity
- [ ] All files in submission folder
- [ ] Folder structure matches recommended layout
- [ ] README.txt present and complete
- [ ] No unnecessary files (temp files, system files)
- [ ] Total package size reasonable (< 25 MB)
- [ ] All files virus-scanned

### Final Submission Steps

#### Step 1: Create Final ZIP (If Required)
If submission platform requires single ZIP:
```bash
# Navigate to submission folder parent
cd /path/to/submission/folder/parent

# Create final submission ZIP
zip -r ContactAvatar_FINAL_SUBMISSION.zip ContactAvatar_Submission/

# Verify ZIP
unzip -l ContactAvatar_FINAL_SUBMISSION.zip
```

#### Step 2: Upload to Submission Platform
1. Log in to submission platform (Moodle, Turnitin, etc.)
2. Navigate to assignment submission page
3. Upload files according to platform requirements:
   - Option A: Upload all files individually
   - Option B: Upload single ZIP
   - Option C: Upload to designated folders
4. Verify all files uploaded successfully
5. Check file names and sizes match local copies

#### Step 3: Verify Submission
1. Download your submitted files
2. Verify files are not corrupted
3. Spot-check APK, ZIP, PDF
4. Ensure submission timestamp is correct
5. Take screenshot of submission confirmation

#### Step 4: Submit Declaration (If Required)
1. Complete any required declaration forms
2. Confirm originality and academic integrity
3. Submit plagiarism checks if required
4. Acknowledge submission guidelines

#### Step 5: Retain Local Backup
1. Keep complete backup of submission package
2. Store in multiple locations (cloud + local)
3. Keep until final grades released
4. Include submission confirmation email/screenshot

---

## Submission Platform-Specific Notes

### Moodle Submission
- Maximum file size: Usually 100 MB
- Supported formats: ZIP, PDF, APK
- Multiple file upload: Typically supported
- Recommended: Upload files individually for clarity

### Turnitin Submission
- PDF required for plagiarism check
- Additional files as supplementary materials
- APK and ZIP as supplementary uploads
- Submit logbook PDF as main document

### Google Drive / OneDrive Submission
- Create shared folder with view-only access
- Upload all files to folder
- Ensure sharing settings correct
- Submit folder link to instructor

### Email Submission
- Check email size limits (usually 25 MB)
- If package exceeds limit:
  - Use file sharing service (Google Drive, Dropbox)
  - Send download link in email
- Include all required information in email body
- Request read receipt for confirmation

---

## Troubleshooting Common Issues

### APK Won't Install on Device
**Problem**: "App not installed" error

**Solutions**:
- Enable "Install from Unknown Sources"
- Check device has sufficient storage
- Uninstall any previous version first
- Verify APK not corrupted (re-download/rebuild)

### Source Code Won't Build
**Problem**: Gradle sync fails or build errors

**Solutions**:
- Verify Android Studio version (Hedgehog or later)
- Check Java/JDK version (Java 17 required)
- Delete `.gradle` and `.idea` folders, re-sync
- Verify all files extracted correctly
- Check internet connection (Gradle downloads dependencies)

### PDF Too Large
**Problem**: Logbook PDF exceeds size limit

**Solutions**:
- Compress images before inserting (reduce resolution)
- Use lower quality PDF export setting
- Use online PDF compressor tool
- Remove unnecessary images/content
- Export to PDF/A standard for smaller size

### ZIP Too Large
**Problem**: Source code ZIP exceeds limit

**Solutions**:
- Verify `build/` folders excluded
- Check `.gradle/` folder not included
- Remove any unnecessary resource files
- Verify no duplicate libraries
- Check for accidentally included large files

---

## Post-Submission

### After Successful Submission

1. **Save Confirmation**:
   - Save submission confirmation email
   - Screenshot submission page with timestamp
   - Note submission ID/reference number

2. **Backup Submission**:
   - Keep complete submission package
   - Upload to cloud storage (Google Drive, OneDrive)
   - Keep local copy on external drive
   - Retain until final grades published

3. **Verify Submission Status**:
   - Check submission platform for "Submitted" status
   - Verify all files show as uploaded
   - Confirm no error messages
   - Check submission received before deadline

4. **Wait for Feedback**:
   - Note expected feedback date
   - Monitor submission platform for comments
   - Check email for instructor communication
   - Be prepared to answer questions if needed

---

## Quick Reference Commands

### Build APK
```bash
cd /mnt/c/Users/panth/Documents/Projects/ContactAvatorApplication
./gradlew assembleDebug
```

### Create Source ZIP (Linux/WSL)
```bash
cd /mnt/c/Users/panth/Documents/Projects/
zip -r ContactAvatar_SourceCode.zip ContactAvatorApplication \
  -x "*.gradle/*" -x "*/.idea/*" -x "*/build/*" -x "*/local.properties"
```

### Convert Markdown to PDF (Pandoc)
```bash
pandoc Phase6_Logbook_Template.md \
  -o ContactAvatar_Logbook.pdf \
  --pdf-engine=pdflatex \
  -V geometry:margin=1in \
  --toc --number-sections
```

### Create Final Submission ZIP
```bash
cd /path/to/ContactAvatar_Submission/..
zip -r ContactAvatar_FINAL_SUBMISSION.zip ContactAvatar_Submission/
```

---

**End of Submission Package Guide**

For additional assistance, refer to:
- Phase6_Screenshot_Guide.md
- Phase6_Logbook_Template.md
- Phase6_Final_Quality_Checklist.md
- Module submission guidelines
