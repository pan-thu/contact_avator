package dev.panthu.contactavatorapplication.ui.avatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dev.panthu.contactavatorapplication.databinding.DialogAvatarPickerBinding
import dev.panthu.contactavatorapplication.ui.contact.ViewModelFactory

/**
 * Bottom sheet dialog fragment for selecting contact avatars.
 * Features:
 * - Grid layout with 3 columns
 * - Live preview of selected avatar
 * - Custom avatar import from gallery
 * - Reset to default avatar option
 * - State preservation across rotation
 * - Fragment result callback for selection
 * - Smooth animations and Material Design
 */
class AvatarPickerBottomSheetDialogFragment : BottomSheetDialogFragment() {

    companion object {
        const val TAG = "AvatarPickerBottomSheet"
        const val REQUEST_KEY = "avatar_picker_request"
        const val RESULT_KEY_AVATAR_ID = "avatar_id"
        const val RESULT_KEY_AVATAR_URI = "avatar_uri"

        private const val ARG_INITIAL_AVATAR_ID = "initial_avatar_id"
        private const val ARG_INITIAL_AVATAR_URI = "initial_avatar_uri"

        /**
         * Create a new instance with optional initial selection.
         */
        fun newInstance(
            @DrawableRes initialAvatarId: Int? = null,
            initialAvatarUri: String? = null
        ): AvatarPickerBottomSheetDialogFragment {
            return AvatarPickerBottomSheetDialogFragment().apply {
                arguments = bundleOf(
                    ARG_INITIAL_AVATAR_ID to initialAvatarId,
                    ARG_INITIAL_AVATAR_URI to initialAvatarUri
                )
            }
        }
    }

    private var _binding: DialogAvatarPickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AvatarPickerViewModel by viewModels {
        ViewModelFactory(requireActivity().application, this)
    }

    private lateinit var avatarAdapter: AvatarGridAdapter
    private lateinit var avatarImportHandler: AvatarImportHandler

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogAvatarPickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecyclerView()
        setupAvatarImport()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupViewModel() {
        // Set initial selection from arguments
        val initialAvatarId = arguments?.getInt(ARG_INITIAL_AVATAR_ID, 0)?.takeIf { it != 0 }
        val initialAvatarUri = arguments?.getString(ARG_INITIAL_AVATAR_URI)
        viewModel.setInitialSelection(initialAvatarId, initialAvatarUri)
    }

    private fun setupRecyclerView() {
        // Create adapter with selection callback
        avatarAdapter = AvatarGridAdapter(emptyList()) { selectedAvatarId ->
            viewModel.selectAvatar(selectedAvatarId)
        }

        // Setup RecyclerView with GridLayoutManager (3 columns)
        binding.avatarGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = avatarAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupAvatarImport() {
        // Initialize avatar import handler
        avatarImportHandler = AvatarImportHandler(
            fragment = this,
            onImageSelected = { uri ->
                if (uri != null) {
                    viewModel.selectCustomAvatar(uri)
                }
            },
            onError = { exception ->
                // Show error to user with Snackbar
                view?.let {
                    com.google.android.material.snackbar.Snackbar.make(
                        it,
                        "Failed to import image: ${exception.message}",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        )
        avatarImportHandler.initialize()

        // Always show the import button - let user try even if availability check fails
        binding.importFromGalleryButton.visibility = View.VISIBLE
    }

    private fun setupClickListeners() {
        // Import from gallery
        binding.importFromGalleryButton.setOnClickListener {
            avatarImportHandler.launchImagePicker()
        }

        // Reset to default
        binding.resetButton.setOnClickListener {
            viewModel.resetToDefault()
        }

        // Cancel button
        binding.cancelButton.setOnClickListener {
            dismiss()
        }

        // Confirm button
        binding.confirmButton.setOnClickListener {
            confirmSelection()
        }
    }

    private fun observeViewModel() {
        // Observe available avatars
        viewModel.availableAvatars.observe(viewLifecycleOwner) { avatars ->
            avatarAdapter = AvatarGridAdapter(avatars) { selectedAvatarId ->
                viewModel.selectAvatar(selectedAvatarId)
            }
            binding.avatarGrid.adapter = avatarAdapter

            // Set initial selection in adapter
            val currentSelection = viewModel.selectedAvatarId.value
            if (currentSelection != null) {
                avatarAdapter.setSelectedAvatar(currentSelection)
            }
        }

        // Observe selected avatar ID
        viewModel.selectedAvatarId.observe(viewLifecycleOwner) { avatarId ->
            // Update preview
            if (avatarId != null && !viewModel.hasCustomUri()) {
                binding.avatarPreview.setAvatarResource(avatarId)
                avatarAdapter.setSelectedAvatar(avatarId)
            }
        }

        // Observe selected avatar URI (custom)
        viewModel.selectedAvatarUri.observe(viewLifecycleOwner) { avatarUri ->
            if (!avatarUri.isNullOrEmpty()) {
                // Show custom avatar in preview
                binding.avatarPreview.setAvatarUri(avatarUri)
                // Clear adapter selection (custom avatar not in grid)
                avatarAdapter.setSelectedAvatar(null)
            }
        }

        // Observe changes to enable/disable confirm button
        viewModel.hasChanges.observe(viewLifecycleOwner) { hasChanges ->
            // Could use this to show unsaved changes indicator
            // For now, confirm is always enabled
        }
    }

    private fun confirmSelection() {
        val selection = viewModel.getSelectionResult()

        // Send result back to caller
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(
                RESULT_KEY_AVATAR_ID to selection.avatarId,
                RESULT_KEY_AVATAR_URI to selection.avatarUri
            )
        )

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
