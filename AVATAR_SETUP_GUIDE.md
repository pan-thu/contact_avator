# Avatar Setup Guide

## Current System

The avatar system is already configured to use Android drawable resources. The code references these avatar files:

- `avatar_default.xml` - Default avatar (currently a gray circle with person icon)
- `avatar_01.xml` through `avatar_10.xml` - Avatar options (currently colored circles with person icons)

## How to Replace with Your Images

### Step 1: Prepare Your Images

1. Prepare 11 images (1 default + 10 options)
2. Recommended dimensions: 192x192px or 256x256px (square)
3. Supported formats: PNG or JPG
4. Image names should follow the pattern:
   - `avatar_default.png` (or `.jpg`)
   - `avatar_01.png` through `avatar_10.png`

### Step 2: Add Images to Project

1. Place your image files in one of these folders:
   - `app/src/main/res/drawable/` (for all screen densities)
   - Or use density-specific folders:
     - `app/src/main/res/drawable-mdpi/` (medium density)
     - `app/src/main/res/drawable-hdpi/` (high density)
     - `app/src/main/res/drawable-xhdpi/` (extra high density)
     - `app/src/main/res/drawable-xxhdpi/` (extra extra high density)
     - `app/src/main/res/drawable-xxxhdpi/` (extra extra extra high density)

2. **Delete or rename** the existing XML files with the same names:
   - Current location: `app/src/main/res/drawable/avatar_*.xml`
   - Either delete them or rename them (e.g., `avatar_01_old.xml`)

### Step 3: File Naming Rules

- Use **lowercase** letters only
- Use **underscores** for spaces (avatar_01, not avatar-01)
- No spaces or special characters
- PNG files: `avatar_01.png`, `avatar_02.png`, etc.
- JPG files: `avatar_01.jpg`, `avatar_02.jpg`, etc.

### Example Structure

```
app/src/main/res/
├── drawable/
│   ├── avatar_default.png
│   ├── avatar_01.png
│   ├── avatar_02.png
│   ├── avatar_03.png
│   ├── avatar_04.png
│   ├── avatar_05.png
│   ├── avatar_06.png
│   ├── avatar_07.png
│   ├── avatar_08.png
│   ├── avatar_09.png
│   └── avatar_10.png
```

## Adding More Avatars (Optional)

If you want more than 10 avatar options:

1. Add more images following the naming pattern: `avatar_11.png`, `avatar_12.png`, etc.

2. Update the avatar list in code:
   - File: `app/src/main/java/dev/panthu/contactavatorapplication/ui/avatar/AvatarPickerViewModel.kt`
   - Function: `loadAvailableAvatars()` (line 139)
   - Add entries like: `R.drawable.avatar_11`, `R.drawable.avatar_12`, etc.

## How It Works

The system uses Android's resource system:
- Images are referenced by ID: `R.drawable.avatar_01`
- The `AvatarView` component loads them using the Coil library
- Images are automatically cropped to circles
- All images are stored in the database by resource ID
- Custom images from gallery are stored as URIs

## Current Code References

- **Contact Model**: `app/src/main/java/dev/panthu/contactavatorapplication/data/Contact.kt`
  - Stores `avatarId` (Int) for drawable resources
  - Stores `avatarUri` (String) for custom gallery images

- **Avatar View**: `app/src/main/java/dev/panthu/contactavatorapplication/ui/components/AvatarView.kt`
  - Displays avatars with circular cropping
  - Supports both resource IDs and URIs

- **Avatar Picker**: `app/src/main/java/dev/panthu/contactavatorapplication/ui/avatar/AvatarPickerViewModel.kt`
  - Defines available avatar options (line 139-155)

## No Code Changes Needed

The system is already fully configured! You only need to:
1. Add your image files to the drawable folder
2. Delete or rename the existing XML placeholder files
3. Rebuild the app

That's it! The avatar system will automatically use your images.
