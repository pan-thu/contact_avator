package dev.panthu.contactavatorapplication.ui.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dev.panthu.contactavatorapplication.R
import dev.panthu.contactavatorapplication.databinding.FragmentContactListBinding
import dev.panthu.contactavatorapplication.util.PreferencesManager

/**
 * Fragment displaying the list of contacts with search and sort functionality.
 */
class ContactListFragment : Fragment() {

    private var _binding: FragmentContactListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ContactListViewModel
    private lateinit var adapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ContactListViewModel::class.java]

        // Setup RecyclerView
        setupRecyclerView()

        // Setup Search
        setupSearch()

        // Setup Toolbar and Sort Menu
        setupToolbar()

        // Setup FAB
        setupFab()

        // Observe filtered contacts
        observeContacts()
    }

    private fun setupRecyclerView() {
        adapter = ContactListAdapter { contact ->
            // Navigate to contact details
            val action = ContactListFragmentDirections
                .actionContactListToContactDetails(contact.id)
            findNavController().navigate(action)
        }

        binding.contactsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@ContactListFragment.adapter

            // Add divider decoration
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable?.toString() ?: ""
            viewModel.setSearchQuery(query)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            // Set initial sort menu checked state
            val currentSort = viewModel.getCurrentSortOrder()
            menu.findItem(getSortMenuItemId(currentSort))?.isChecked = true

            // Handle menu item clicks
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.sort_name_asc -> {
                        setSortOrder(PreferencesManager.SortOrder.NAME_ASC, menuItem)
                        true
                    }
                    R.id.sort_name_desc -> {
                        setSortOrder(PreferencesManager.SortOrder.NAME_DESC, menuItem)
                        true
                    }
                    R.id.sort_recent -> {
                        setSortOrder(PreferencesManager.SortOrder.RECENTLY_ADDED, menuItem)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupFab() {
        binding.fabAddContact.setOnClickListener {
            // Navigate to contact edit fragment with new contact (-1 ID)
            val action = ContactListFragmentDirections
                .actionContactListToContactEdit(-1L)
            findNavController().navigate(action)
        }
    }

    private fun observeContacts() {
        viewModel.filteredContacts.observe(viewLifecycleOwner) { contacts ->
            // Update adapter
            adapter.submitList(contacts)

            // Show/hide empty state
            if (contacts.isEmpty()) {
                binding.emptyStateView.visibility = View.VISIBLE
                binding.contactsRecyclerView.visibility = View.GONE
            } else {
                binding.emptyStateView.visibility = View.GONE
                binding.contactsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    private fun setSortOrder(order: PreferencesManager.SortOrder, menuItem: MenuItem) {
        viewModel.setSortOrder(order)
        menuItem.isChecked = true
    }

    private fun getSortMenuItemId(sortOrder: PreferencesManager.SortOrder): Int {
        return when (sortOrder) {
            PreferencesManager.SortOrder.NAME_ASC -> R.id.sort_name_asc
            PreferencesManager.SortOrder.NAME_DESC -> R.id.sort_name_desc
            PreferencesManager.SortOrder.RECENTLY_ADDED -> R.id.sort_recent
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
