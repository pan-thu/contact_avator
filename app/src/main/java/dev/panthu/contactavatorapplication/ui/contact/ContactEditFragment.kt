package dev.panthu.contactavatorapplication.ui.contact

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.databinding.FragmentContactEditBinding
import dev.panthu.contactavatorapplication.ui.avatar.AvatarPickerBottomSheetDialogFragment
import dev.panthu.contactavatorapplication.util.ValidationResult
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Fragment for creating or editing a contact.
 * Handles form input, validation, and avatar selection.
 */
class ContactEditFragment : Fragment() {

    private var _binding: FragmentContactEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ContactEditViewModel by viewModels {
        ViewModelFactory(requireActivity().application, this)
    }

    // Navigation arguments
    private val args: ContactEditFragmentArgs by navArgs()

    // Track if we're editing or creating
    private val isEditMode: Boolean
        get() = args.contactId > 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up avatar picker result listener
        setFragmentResultListener(AvatarPickerBottomSheetDialogFragment.REQUEST_KEY) { _, bundle ->
            val avatarId = bundle.getInt(AvatarPickerBottomSheetDialogFragment.RESULT_KEY_AVATAR_ID, -1)
            val avatarUri = bundle.getString(AvatarPickerBottomSheetDialogFragment.RESULT_KEY_AVATAR_URI)

            if (avatarId != -1 || avatarUri != null) {
                viewModel.updateAvatar(
                    if (avatarId != -1) avatarId else null,
                    avatarUri
                )
            }
        }

        // Handle back press with unsaved changes check
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                handleBackPressed()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupViews()
        setupObservers()
        setupTextWatchers()

        // Load contact if editing
        if (isEditMode && savedInstanceState == null) {
            viewModel.loadContact(args.contactId)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = if (isEditMode) {
                getString(R.string.nav_edit_contact)
            } else {
                getString(R.string.nav_add_contact)
            }

            setNavigationOnClickListener {
                handleBackPressed()
            }
        }
    }

    private fun setupViews() {
        // Avatar change button
        binding.btnChangeAvatar.setOnClickListener {
            showAvatarPicker()
        }

        // Date of birth picker
        binding.dobEditText.setOnClickListener {
            showDatePicker()
        }

        // Save button
        binding.btnSave.setOnClickListener {
            viewModel.saveContact()
        }

        // Cancel button
        binding.btnCancel.setOnClickListener {
            handleBackPressed()
        }

        // Handle "Done" IME action on address field
        binding.addressEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (viewModel.isSaveEnabled.value == true) {
                    viewModel.saveContact()
                }
                true
            } else {
                false
            }
        }
    }

    private fun setupObservers() {
        // Observe contact data
        viewModel.contact.observe(viewLifecycleOwner) { contact ->
            contact?.let {
                // Update avatar display
                updateAvatarDisplay(it.avatarId, it.avatarUri)
            }
        }

        // Observe form fields (only populate if empty)
        viewModel.name.observe(viewLifecycleOwner) { name ->
            if (binding.nameEditText.text.toString() != name) {
                binding.nameEditText.setText(name)
            }
        }

        viewModel.phone.observe(viewLifecycleOwner) { phone ->
            if (binding.phoneEditText.text.toString() != phone) {
                binding.phoneEditText.setText(phone)
            }
        }

        viewModel.email.observe(viewLifecycleOwner) { email ->
            if (binding.emailEditText.text.toString() != email) {
                binding.emailEditText.setText(email)
            }
        }

        viewModel.address.observe(viewLifecycleOwner) { address ->
            if (binding.addressEditText.text.toString() != address) {
                binding.addressEditText.setText(address)
            }
        }

        viewModel.dateOfBirth.observe(viewLifecycleOwner) { dateOfBirth ->
            val formattedDate = dateOfBirth?.let { formatDate(it) } ?: ""
            if (binding.dobEditText.text.toString() != formattedDate) {
                binding.dobEditText.setText(formattedDate)
            }
        }

        // Observe avatar updates
        viewModel.avatarId.observe(viewLifecycleOwner) { avatarId ->
            updateAvatarDisplay(avatarId, viewModel.avatarUri.value)
        }

        viewModel.avatarUri.observe(viewLifecycleOwner) { avatarUri ->
            updateAvatarDisplay(viewModel.avatarId.value, avatarUri)
        }

        // Observe validation states
        viewModel.nameValidation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidationResult.Error -> {
                    binding.nameInputLayout.error = getString(result.messageResId)
                }
                ValidationResult.Success -> {
                    binding.nameInputLayout.error = null
                }
            }
        }

        viewModel.phoneValidation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidationResult.Error -> {
                    binding.phoneInputLayout.error = getString(result.messageResId)
                }
                ValidationResult.Success -> {
                    binding.phoneInputLayout.error = null
                }
            }
        }

        viewModel.emailValidation.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ValidationResult.Error -> {
                    binding.emailInputLayout.error = getString(result.messageResId)
                }
                ValidationResult.Success -> {
                    binding.emailInputLayout.error = null
                }
            }
        }

        // Observe save button state
        viewModel.isSaveEnabled.observe(viewLifecycleOwner) { isEnabled ->
            binding.btnSave.isEnabled = isEnabled
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingOverlay.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnSave.isEnabled = !isLoading && (viewModel.isSaveEnabled.value == true)
            binding.btnCancel.isEnabled = !isLoading
        }

        // Observe save result
        viewModel.saveResult.observe(viewLifecycleOwner) { result ->
            result?.let {
                handleSaveResult(it)
                viewModel.resetSaveResult()
            }
        }
    }

    private fun setupTextWatchers() {
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateName(s?.toString() ?: "")
            }
        })

        binding.phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updatePhone(s?.toString() ?: "")
            }
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateEmail(s?.toString() ?: "")
            }
        })

        binding.addressEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateAddress(s?.toString() ?: "")
            }
        })
    }

    private fun updateAvatarDisplay(avatarId: Int?, avatarUri: String?) {
        binding.avatarView.apply {
            if (avatarUri != null) {
                setAvatarUri(avatarUri)
            } else if (avatarId != null) {
                setAvatarResource(avatarId)
            } else {
                setAvatarResource(R.drawable.avatar_default)
            }
        }
    }

    private fun showAvatarPicker() {
        val currentAvatarId = viewModel.avatarId.value
        val currentAvatarUri = viewModel.avatarUri.value

        val bottomSheet = AvatarPickerBottomSheetDialogFragment.newInstance(
            initialAvatarId = currentAvatarId,
            initialAvatarUri = currentAvatarUri
        )
        bottomSheet.show(parentFragmentManager, AvatarPickerBottomSheetDialogFragment.TAG)
    }

    private fun handleSaveResult(result: Result<Long>) {
        result.fold(
            onSuccess = {
                val message = if (isEditMode) {
                    getString(R.string.contact_updated)
                } else {
                    getString(R.string.contact_saved)
                }
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()

                // Navigate back
                findNavController().navigateUp()
            },
            onFailure = { error ->
                val message = getString(R.string.error_saving_contact)
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.action_ok) { /* Dismiss */ }
                    .show()
            }
        )
    }

    private fun handleBackPressed() {
        if (viewModel.isDirty.value == true) {
            showUnsavedChangesDialog()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showUnsavedChangesDialog() {
        val dialog = UnsavedChangesDialogFragment()
        dialog.show(parentFragmentManager, UnsavedChangesDialogFragment.TAG)

        // Listen for dialog result
        parentFragmentManager.setFragmentResultListener(
            UnsavedChangesDialogFragment.REQUEST_KEY_DISCARD,
            viewLifecycleOwner
        ) { _, bundle ->
            val shouldDiscard = bundle.getBoolean(UnsavedChangesDialogFragment.RESULT_KEY_DISCARD, false)
            if (shouldDiscard) {
                findNavController().navigateUp()
            }
        }
    }

    private fun showDatePicker() {
        val currentDate = viewModel.dateOfBirth.value ?: System.currentTimeMillis()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText(R.string.select_date_of_birth)
            .setSelection(currentDate)
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            viewModel.updateDateOfBirth(selectedDate)
        }

        datePicker.show(parentFragmentManager, "DATE_PICKER")
    }

    private fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
