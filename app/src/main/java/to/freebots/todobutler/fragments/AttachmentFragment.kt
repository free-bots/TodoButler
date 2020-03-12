package to.freebots.todobutler.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.AttachmentsAdapter
import to.freebots.todobutler.models.entities.Attachment
import to.freebots.todobutler.models.entities.FlatTaskDTO
import to.freebots.todobutler.viewmodels.AttachmentViewModel

/**
 * A simple [Fragment] subclass.
 */
class AttachmentFragment : Fragment(), AttachmentsAdapter.Action {

    companion object {
        private const val FILE_OPEN_REQUEST = 1
        private const val FILE_CREATE_REQUEST = 2
    }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(activity?.application!!)
        ).get(AttachmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_attachment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AttachmentsAdapter().apply { action = this@AttachmentFragment }

        viewModel.attachments.observe(
            viewLifecycleOwner,
            Observer { attachments: MutableList<Attachment> ->
                // todo update recycler
                adapter.attachments = attachments
            })

        exportFile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            FILE_OPEN_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    taskId()?.let {
                        viewModel.importFile(data?.data, it)
                    }
                }
                Toast.makeText(context, "got a file", Toast.LENGTH_LONG).show()
            }

            FILE_CREATE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let {
                        // todo write data
                    }
                }
            }
        }
    }

    private fun exportFile() {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_TITLE, "TEST TITLE")
        }

        startActivityForResult(intent, FILE_CREATE_REQUEST)
    }

    private fun importFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }

        startActivityForResult(intent, FILE_OPEN_REQUEST)
    }

    private fun taskId(): Long? {
        return arguments?.getParcelable<FlatTaskDTO>("flatTaskDTO")?.id
    }

    override fun edit(attachment: Attachment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun open(attachment: Attachment) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
