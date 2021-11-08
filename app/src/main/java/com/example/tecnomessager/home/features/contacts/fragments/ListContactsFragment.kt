package com.example.tecnomessager.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.MessageReceiver
import com.example.tecnomessager.databinding.FragmentListContactsBinding
import com.example.tecnomessager.home.features.contacts.activity.ContactActivity
import com.example.tecnomessager.home.features.contacts.adapter.ListContactsAdapter
import com.example.tecnomessager.home.features.contacts.viewmodel.ListContactsViewModel
import com.example.tecnomessager.utils.extension.snackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputLayout
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

    override fun onResume() {
        super.onResume()
        requestContacts()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addContactModal.setOnClickListener {
            val layoutInflater = LayoutInflater.from(context)
            val promptView: View = layoutInflater.inflate(R.layout.modal_add_contact, null)
            val dialog = BottomSheetDialog(requireContext())

            promptView.findViewById<TextView>(R.id.email_modal_add_contact).addTextChangedListener {
                promptView
                    .findViewById<TextInputLayout>(R.id.email_layout_modal_add_contact)
                    .error = ""
            }

            promptView.findViewById<View>(R.id.add_contact).setOnClickListener {
                viewModel.addContact(promptView.findViewById<TextView>(R.id.email_modal_add_contact).text.toString())
                    .observe(viewLifecycleOwner, {
                        it?.let { resource ->
                            if (resource.dado) {
                                view.snackBar(resource.erro!!)
                                dialog.dismiss()
                                requestContacts()
                            } else {
                                promptView
                                    .findViewById<TextInputLayout>(R.id.email_layout_modal_add_contact)
                                    .error = resource.erro
                                //view.snackBar(messageError)
                                //dialog.dismiss()
                            }
                        }
                    })
            }
            dialog.setCanceledOnTouchOutside(true)
            dialog.setContentView(promptView)
            dialog.show()
        }

//        viewModel.requestMessages().observe(viewLifecycleOwner, { listMessages ->
//            binding.listContacts.adapter =
//                ListContactsAdapter(listMessages, this@ListContactsFragment)
//        })

    }

    private fun requestContacts() {
        viewModel.requestContacts().observe(viewLifecycleOwner, { listContacts ->
            val listMessagesByContacts: MutableList<MessageReceiver> = mutableListOf()
            listContacts?.forEach {
                listMessagesByContacts.add(
                    MessageReceiver(
                        userReceiver = it.nameProfile,
                        imageUserReceiver = it.imageProfile
                    )
                )
            }
            binding.listContacts.adapter =
                ListContactsAdapter(listMessagesByContacts, this@ListContactsFragment)
        })
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
