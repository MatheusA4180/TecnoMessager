package com.example.tecnomessager.home.features.contacts.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tecnomessager.R
import com.example.tecnomessager.data.model.Message
import com.example.tecnomessager.data.model.MessageSend
import com.example.tecnomessager.databinding.FragmentMessagesContactBinding
import com.example.tecnomessager.home.features.contacts.adapter.ListMessagesContactAdapter
import com.example.tecnomessager.home.features.contacts.viewmodel.MessageContactViewModel
import com.example.tecnomessager.home.fragments.ListContactsFragment.Companion.EMAIL_CONTACT
import com.example.tecnomessager.home.fragments.ListContactsFragment.Companion.IMAGE_CONTACT
import com.example.tecnomessager.home.fragments.ListContactsFragment.Companion.NAME_CONTACT
import com.example.tecnomessager.intro.fragments.LoginFragmentDirections
import com.example.tecnomessager.utils.extension.HelperFunctions.formatDate
import com.example.tecnomessager.utils.extension.HelperFunctions.formatHour
import com.example.tecnomessager.utils.extension.snackBar
import com.squareup.picasso.Picasso
import org.koin.android.ext.android.bind
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MessagesContactFragment : Fragment() {

    private val binding: FragmentMessagesContactBinding by lazy {
        FragmentMessagesContactBinding.inflate(layoutInflater)
    }
    private val viewModel: MessageContactViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val intent = requireActivity().intent

        if (intent.getStringExtra(IMAGE_CONTACT).isNullOrEmpty()) {
            binding.imageviewContact.setImageResource(R.drawable.icon_app)
        } else{
            Picasso.get().load(intent.getStringExtra(IMAGE_CONTACT)).into(binding.imageviewContact)
        }

        binding.nameContact.text = intent.getStringExtra(NAME_CONTACT)

        binding.toolbarMessagesContact.setNavigationOnClickListener {
            requireActivity().finish()
        }

        viewModel.requestMessagesByUser(intent.getStringExtra(EMAIL_CONTACT)!!).observe(viewLifecycleOwner,{ listMessages ->
            binding.recyclerviewChatLog.adapter = ListMessagesContactAdapter(listMessages)
        })

        binding.imageviewContact.setImageResource(R.drawable.icon_app)

        binding.sendButtonChat.setOnClickListener {
            if(binding.edittextChatLog.text.toString().isNotEmpty()) {
                viewModel.sendMenssage(
                    Message(
                        date = formatDate(Calendar.getInstance(Locale("pt","BR")).time),
                        hour = formatHour(Calendar.getInstance(Locale("pt","BR")).time),
                        contentMessage = binding.edittextChatLog.text.toString(),
                        file = null,
                        userSend = null,
                        userReceiver = intent.getStringExtra(EMAIL_CONTACT)!!
                    )
                ).observe(viewLifecycleOwner,{
                    it?.let { resource ->
                        if (resource.dado) {
                            binding.edittextChatLog.setText("")
                            view.snackBar(resource.erro!!)
                        } else {
                            view.snackBar(resource.erro!!)
                        }
                    }
                })
            }
        }

        binding.toolbarMessagesContact.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.contact_notify -> {
                    true
                }
                R.id.config_options_contact -> {
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

}