package orestesk.multipop

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.fragment_create_sound.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.Timestamp
import java.util.*


class CreateSoundFragment : DialogFragment() {
    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result -> handleActivityResult(result) }
    private val cr: ContentResolver = MainActivity.appContext.contentResolver
    private lateinit var appContext : Context
    private lateinit var uri: Uri
    private lateinit var name : EditText
    private lateinit var description : EditText
    private lateinit var btnSelect : Button
    private lateinit var btnCreate: Button
    private val intent = Intent()
        .setType("audio/mpeg")
        .setAction(Intent.ACTION_GET_CONTENT)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_create_sound, container, false)
        appContext = view.context
        name = view.findViewById(R.id.nameField)
        description = view.findViewById(R.id.descriptionField)
        btnSelect = view.btn_select
        btnCreate = view.btn_create

        btnSelect.setOnClickListener{
            btnSelect.error = null
            resultLauncher.launch(Intent.createChooser(intent, "Select a file"))
        }

        btnCreate.setOnClickListener {
            if(valid(name)){
                create(name.text.toString(), description.text.toString())
            }
        }

        return view
    }

    private fun create(name: String, description: String){
        val file = cr.openInputStream(uri)?.use { it.readBytes() }
        lifecycleScope.launch(Dispatchers.IO) {
                createSound(null, LoginActivity.userName, name, description, Timestamp(Date().time).toString(), null, file!!)
        }
    }

    private fun valid(name: EditText) : Boolean{
        if (name.text.toString().trim() == "") {
            name.error = "Enter Name"
            return false
        }
        if(!::uri.isInitialized){
            btnSelect.error = "Choose File"
            return false
        }
        return true
    }
    private fun handleActivityResult(result: ActivityResult){
        if(result.resultCode == Activity.RESULT_OK){
            val inp : Int = cr.openInputStream(result.data?.data!!)?.use { it.available() }!!

            if(inp > 2000000 || inp == 0){
                makeToast(appContext, "Invalid File Size (Required <2MB)")
                return
            }
            uri = result.data?.data!!
            val filename = uri.path?.split("/")!!
            btnSelect.text = filename[filename.size - 1]

            createInstantSound(appContext, uri)
        }
    }
}