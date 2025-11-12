package dev.panthu.contactavatorapplication.ui.contact

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.databinding.DialogDeleteConfirmationBinding

/**
 * Dialog fragment for confirming contact deletion.
 */
class DeleteConfirmationDialogFragment : DialogFragment() {

    private var _binding: DialogDeleteConfirmationBinding? = null
    private val binding get() = _binding!!

    private var onConfirmListener: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDeleteConfirmationBinding.inflate(LayoutInflater.from(requireContext()))

        val contactName = arguments?.getString(ARG_CONTACT_NAME) ?: ""

        binding.apply {
            // Set the confirmation message with contact name
            dialogMessage.text = getString(R.string.delete_contact_message, contactName)

            // Cancel button
            cancelButton.setOnClickListener {
                dismiss()
            }

            // Delete button
            deleteButton.setOnClickListener {
                onConfirmListener?.invoke()
                dismiss()
            }
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    /**
     * Set the listener for when delete is confirmed.
     */
    fun setOnConfirmListener(listener: () -> Unit) {
        onConfirmListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_CONTACT_NAME = "contact_name"

        /**
         * Create a new instance of the dialog with the contact name.
         */
        fun newInstance(contactName: String): DeleteConfirmationDialogFragment {
            return DeleteConfirmationDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_CONTACT_NAME, contactName)
                }
            }
        }
    }
}
