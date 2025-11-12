### **Product Requirements Document: ContactAvatar+ Application**

**1. Introduction & Vision**

This document defines the requirements for “ContactAvatar+,” an Android app that extends the Lecture 5 ContactDatabase by letting users assign and persist an **avatar/profile image** for each contact. Avatars must be maintainable in **Android resources** and the UI should use **themes/styles** per the brief. :contentReference[oaicite:0]{index=0}

**2. Goals & Objectives**

* **Product Goal:** Persist contacts with avatars and provide fast list browsing, search/filtering, and sorting.
* **User Goal:** Make it easy to create/edit contacts, pick or import avatars, and quickly find people.
* **Educational Goal:** Demonstrate Android **persistence**, resource use, RecyclerView, and theme/style usage required by the exercise. :contentReference[oaicite:1]{index=1}

**3. User Stories**

* As a user, I want to **create/edit** a contact and **choose an avatar** from a predefined set **so that** contacts are easy to recognise. :contentReference[oaicite:2]{index=2}  
* As a user, I want to **import a custom avatar** from my device **so that** I’m not limited to the preset set. *(Optional enhancement)*  
* As a user, I want **search/filter** and **sort** options **so that** I can quickly find contacts.  
* As a user, I want clear **validation and helpful messages** **so that** I avoid errors when saving.  
* As a user, I want a polished **contact detail** screen with quick actions **so that** key info is obvious.

**4. Features & Requirements**

**4.1. Functional Requirements**

| ID | Requirement | Details |
|---|---|---|
| **FR-01** | **Data Model: Avatar field** | Extend contact entity with `avatarId` (drawable resource ID or stable key) and persist via DAO/Room so it survives app restarts. :contentReference[oaicite:3]{index=3} |
| **FR-02** | **Avatar source (resources)** | Provide a bundled set of avatars in `res/drawable`. Resource-backed avatars must work fully offline. :contentReference[oaicite:4]{index=4} |
| **FR-03** | **Create/Edit with validation** | Add/Edit screen must validate required fields (e.g., name/phone). Disable **Save** until valid and show inline, human-readable errors. (See also NFR-05.) :contentReference[oaicite:5]{index=5} |
| **FR-04** | **Avatar picker UX upgrade** | Open a dialog/bottom sheet **grid** of resource avatars with clear selection state and **preview** before saving. Remember last choice when re-opening Edit. |
| **FR-05** | **Import custom avatar (optional)** | Allow picking an image from gallery (persist a stable URI). If inaccessible later, gracefully **fallback** to the default resource avatar without crashing. *(Enhancement beyond the minimum resource avatar requirement.)* :contentReference[oaicite:6]{index=6} |
| **FR-06** | **List with avatars** | RecyclerView list shows each contact’s avatar (or default) beside name/phone; tapping opens **Contact Detail**. :contentReference[oaicite:7]{index=7} |
| **FR-07** | **Contact detail polish** | Detail screen shows a **large avatar**, name, primary fields, and quick actions (e.g., call/SMS intents). Visual hierarchy is clear and consistent. |
| **FR-08** | **Save & filter** | Persist contacts; provide a **search/filter** input that narrows the list by name/phone in real time. |
| **FR-09** | **Sort** | Provide options to sort **A→Z / Z→A** and **Recently added**. Persist the last chosen sort order (e.g., in preferences). |
| **FR-10** | **Delete/Reset avatar** | Deleting a contact removes its avatar association; “Reset avatar” returns to the default. |
| **FR-11** | **Robust fallbacks** | If an avatar resource/URI can’t be resolved, show a stable default and continue—no crashes. |
| **FR-12** | **Submission evidence** | Prepare screenshots and **actual code** snippets with brief explanations in the logbook template. :contentReference[oaicite:8]{index=8}

**4.2. Non-Functional Requirements**

| ID | Requirement | Details |
|---|---|---|
| **NFR-01** | **Styling & theming** | Use app theme, styles, and resource files (colors, dimens, strings). Avoid hard-coded styling. This is explicitly required across the logbook. :contentReference[oaicite:9]{index=9} |
| **NFR-02** | **Performance** | Smooth list scrolling and edits; main interactions should feel instant. |
| **NFR-03** | **RecyclerView efficiency** | Use **DiffUtil** for list updates (create/edit/delete) to minimise jank and improve performance. |
| **NFR-04** | **Persistence & state** | Avatar choice and contacts are persisted in DB; transient UI state (e.g., picker selection) survives rotation. |
| **NFR-05** | **Validation & UX quality** | Inline validation, sensible defaults, and **meaningful messages**—all highlighted as good UI practice. :contentReference[oaicite:10]{index=10} |
| **NFR-06** | **Accessibility** | Content descriptions for avatar images (e.g., “Avatar: Blue circle”); focus order and touch targets ≥48dp. |
| **NFR-07** | **Offline & privacy** | Entirely offline by default; resource avatars require no network. No personal data leaves the device. :contentReference[oaicite:11]{index=11} |
| **NFR-08** | **Submission logistics** | Submit a **single PDF** (no screenshots of code; paste actual code) plus a **ZIP and the App** as required for CW2. :contentReference[oaicite:12]{index=12}

**5. Success Metrics**

* Users can **create/edit** contacts with an avatar from **resources**; choices persist across app restarts. :contentReference[oaicite:13]{index=13}  
* **Search/filter** and **sort** work reliably and feel instant on typical device lists.  
* **Avatar picker** feels polished (preview + clear selection) and **validation** prevents bad saves with helpful guidance. :contentReference[oaicite:14]{index=14}  
* **DiffUtil** produces smooth list updates for add/edit/delete.  
* **Contact detail** presents a clear visual hierarchy with a large avatar and quick actions.  
* Logbook evidence includes **screenshots** and **actual code** with brief explanations per the template. :contentReference[oaicite:15]{index=15}
