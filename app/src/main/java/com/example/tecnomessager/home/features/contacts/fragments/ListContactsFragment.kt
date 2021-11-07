package com.example.tecnomessager.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.databinding.FragmentListContactsBinding
import com.example.tecnomessager.home.features.contacts.activity.ContactActivity
import com.example.tecnomessager.home.features.contacts.adapter.ListContactsAdapter
import com.example.tecnomessager.home.features.contacts.viewmodel.ListContactsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ListContactsFragment : Fragment(), ListContactsAdapter.ContactClickListener {

    private val binding: FragmentListContactsBinding by lazy {
        FragmentListContactsBinding.inflate(layoutInflater)
    }
    private val viewModel: ListContactsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.requestContacts().observe(viewLifecycleOwner, { listContacts ->
                val listMessagesByContacts: MutableList<MessageReceiver> = mutableListOf()
                listContacts?.forEach {
                    listMessagesByContacts.add(MessageReceiver(userReceiver = it.nameProfile,imageUserReceiver = it.imageProfile))
                }
                binding.listContacts.adapter =
                    ListContactsAdapter(listMessagesByContacts, this@ListContactsFragment)
        })

//        viewModel.requestMessages().observe(viewLifecycleOwner, { listMessages ->
//            binding.listContacts.adapter =
//                ListContactsAdapter(listMessages, this@ListContactsFragment)
//        })

    }

    override fun clickListener(message: MessageReceiver) {
        startActivity(
            Intent(requireActivity(), ContactActivity::class.java).apply {
                putExtra(IMAGE_CONTACT, message.imageUserReceiver)
                putExtra(NAME_CONTACT, message.userReceiver)
            }
        )
    }

    companion object {
        const val NAME_CONTACT = "nameContact"
        const val IMAGE_CONTACT = "imageContact"
    }

}
