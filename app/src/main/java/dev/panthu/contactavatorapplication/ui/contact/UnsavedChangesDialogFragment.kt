package dev.panthu.contactavatorapplication.ui.contact

import android.app.Dialog
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.panthu.contactavatorapplication.databinding.DialogUnsavedChangesBinding

/**
 * Dialog fragment to confirm discarding unsaved changes.
 */
class UnsavedChangesDialogFragment : DialogFragment() {

    companion object {
        const val TAG = "UnsavedChangesDialog"
        const val REQUEST_KEY_DISCARD = "request_discard_changes"
        const val RESULT_KEY_DISCARD = "result_discard"
    }

    private var _binding: DialogUnsavedChangesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogUnsavedChangesBinding.inflate(layoutInflater)

        setupViews()

        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun setupViews() {
        // Keep Editing button
        binding.btnKeepEditing.setOnClickListener {
            setFragmentResult(REQUEST_KEY_DISCARD, bundleOf(RESULT_KEY_DISCARD to false))
            dismiss()
        }

        // Discard button
        binding.btnDiscard.setOnClickListener {
            setFragmentResult(REQUEST_KEY_DISCARD, bundleOf(RESULT_KEY_DISCARD to true))
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
