package dev.panthu.contactavatorapplication.ui.contact

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.data.Contact
import dev.panthu.contactavatorapplication.databinding.FragmentContactDetailsBinding

/**
 * Fragment displaying detailed contact information with quick actions.
 */
class ContactDetailsFragment : Fragment() {

    private var _binding: FragmentContactDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactDetailsViewModel
    private val args: ContactDetailsFragmentArgs by navArgs()

    private var currentContact: Contact? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ContactDetailsViewModel::class.java]

        // Setup Toolbar
        setupToolbar()

        // Setup FAB
        setupFab()

        // Observe contact data
        observeContact()

        // Observe delete result
        observeDeleteResult()

        // Load contact
        viewModel.loadContact(args.contactId)
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            setNavigationOnClickListener {
                findNavController().navigateUp()
            }

            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_delete -> {
                        showDeleteConfirmationDialog()
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupFab() {
        binding.fabEdit.setOnClickListener {
            currentContact?.let { contact ->
                val action = ContactDetailsFragmentDirections
                    .actionContactDetailsToContactEdit(contact.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeContact() {
        viewModel.contact.observe(viewLifecycleOwner) { contact ->
            if (contact == null) {
                // Contact not found or deleted by another process
                showError(getString(R.string.error_loading_contact))
                findNavController().navigateUp()
                return@observe
            }

            currentContact = contact
            displayContact(contact)
        }
    }

    private fun observeDeleteResult() {
        viewModel.deleteResult.observe(viewLifecycleOwner) { result ->
            if (result == null) return@observe

            result.onSuccess {
                // Show success message and navigate back
                Toast.makeText(
                    requireContext(),
                    R.string.contact_deleted,
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            }

            result.onFailure {
                // Show error message
                showError(getString(R.string.error_deleting_contact))
            }

            // Reset result to avoid re-triggering
            viewModel.resetDeleteResult()
        }
    }

    private fun displayContact(contact: Contact) {
        binding.apply {
            // Set name
            nameTextView.text = contact.name

            // Set avatar
            avatarView.apply {
                when {
                    contact.avatarUri != null -> {
                        setAvatarUri(contact.avatarUri)
                    }
                    contact.avatarId != null -> {
                        setAvatarResource(contact.avatarId)
                    }
                    else -> {
                        setAvatarResource(R.drawable.avatar_default)
                    }
                }

                contentDescription = getString(R.string.avatar_description, contact.name)
            }

            // Set phone
            phoneTextView.text = contact.phone
            setupPhoneActions(contact.phone)

            // Set email
            if (!contact.email.isNullOrBlank()) {
                emailCard.visibility = View.VISIBLE
                emailTextView.text = contact.email
                setupEmailAction(contact.email)
            } else {
                emailCard.visibility = View.GONE
            }

            // Set address
            if (!contact.address.isNullOrBlank()) {
                addressCard.visibility = View.VISIBLE
                addressTextView.text = contact.address
                setupAddressAction(contact.address)
            } else {
                addressCard.visibility = View.GONE
            }
        }
    }

    private fun setupPhoneActions(phone: String) {
        binding.callButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phone")
            }
            startIntentSafely(intent)
        }

        binding.smsButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("smsto:$phone")
            }
            startIntentSafely(intent)
        }
    }

    private fun setupEmailAction(email: String) {
        binding.emailButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$email")
            }
            startIntentSafely(intent)
        }
    }

    private fun setupAddressAction(address: String) {
        binding.mapButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
            }
            startIntentSafely(intent)
        }
    }

    /**
     * Start an intent safely, checking if an app can handle it.
     */
    private fun startIntentSafely(intent: Intent) {
        val packageManager = requireContext().packageManager
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            showError(getString(R.string.error_no_app))
        }
    }

    private fun showDeleteConfirmationDialog() {
        val contact = currentContact ?: return

        DeleteConfirmationDialogFragment.newInstance(
            contactName = contact.name
        ).apply {
            setOnConfirmListener {
                viewModel.deleteContact()
            }
        }.show(parentFragmentManager, "delete_confirmation")
    }

    private fun showError(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
