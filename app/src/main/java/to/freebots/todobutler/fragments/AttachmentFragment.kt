package to.freebots.todobutler.fragments


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.content_attachments.*
import kotlinx.android.synthetic.main.fragment_attachment.*
import to.freebots.iconhelper.IconHelper
import to.freebots.todobutler.R
import to.freebots.todobutler.adapters.label.AttachmentsAdapter
import to.freebots.todobutler.models.entities.Attachment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        taskId()?.let {
            viewModel.getAttachmentsByTaskId(it)
        }
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
                if (attachments.isEmpty()) {
                    iv_empty_attachment.setImageResource(IconHelper.randomEmptyIcon())
                    iv_empty_attachment.visibility = View.VISIBLE
                } else {
                    iv_empty_attachment.visibility = View.GONE
                }
                adapter.attachments = attachments
            })

        rv_attachments.adapter = adapter

        viewModel.uri.observe(viewLifecycleOwner, Observer {
            it.consume()?.let { uri ->
                val intent = ShareCompat.IntentBuilder.from(activity!!)
                    .setStream(uri)
                    .intent
                    .setAction(Intent.ACTION_SEND) //Change if needed
                    .setDataAndType(uri, "*/*")
                    .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                startActivity(intent)
            }
        })

        addListener()
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
            }

            FILE_CREATE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data?.let { uri ->
                        viewModel.createFile(uri)
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
        return arguments?.getLong("flatTaskDTO")
    }

    override fun edit(attachment: Attachment) {
//        viewModel.getUri(attachment)
    }

    override fun open(attachment: Attachment) {
        AlertDialog.Builder(context)
            .setMessage("Files")
            .setPositiveButton("OPEN") { _, _ ->
                exportFile()
                viewModel.selectedAttachment.postValue(attachment)
            }
            .setNeutralButton("SHARE") { _, _ ->
                viewModel.getUri(attachment)
            }
            .setNegativeButton("DELETE") { _, _ ->
                viewModel.delete(attachment)
            }
            .create()
            .show()
//        exportFile()
//        viewModel.selectedAttachment.postValue(attachment)
    }

    private fun addListener() {
        addAttachmentFab.setOnClickListener {
            importFile()
        }
    }
}
