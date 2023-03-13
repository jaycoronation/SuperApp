package com.application.alphacapital.superapp.vault.activity.importantdocument

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.AddImpDocGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.ImpDocListResponse
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddImpDocBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_imp_doc.view.txtMandatory
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class AddImpDocActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddImpDocBinding
    var listViews: MutableList<AddImpDocGetSet> = mutableListOf()
    val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    lateinit var impDocGetSet: ImpDocListResponse.Document
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_imp_doc)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                impDocGetSet = Gson().fromJson(intent.getStringExtra("data"), ImpDocListResponse.Document::class.java
                )
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text =
            "Add Important Documents".takeIf { !isForEdit } ?: "Update Important Documents"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            // listViews.add(AddImpDocGetSet(impDocGetSet.holder, impDocGetSet.location, impDocGetSet.document, impDocGetSet.softcopy, impDocGetSet.document_id))
            listViews.add(AddImpDocGetSet(impDocGetSet.document, impDocGetSet.location, impDocGetSet.softcopy, holder_name, impDocGetSet.document_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddImpDocGetSet("", "", "", holder_name, ""))
        }

        binding.rvList.adapter = viewsAdapter
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.txtAddMore.setOnClickListener {
            listViews.add(AddImpDocGetSet("", "", "", holder_name, ""))
            viewsAdapter.notifyDataSetChanged()
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                if (isForEdit)
                {
                    if (isValidInputForOneHolder())
                    {
                        if (isValidInputForOther())
                        {
                            val rootObject = JSONObject()
                            val rootObjectItems = JSONObject()
                            val dataArray = JSONArray()

                            for (i in listViews.indices)
                            {
                                if (listViews[i].document.isNotEmpty() && listViews[i].location.isNotEmpty() && listViews[i].file.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("location", listViews[i].location)
                                    jGroup.put("document", listViews[i].document)
                                    jGroup.put("user_id", sessionManager.userId)
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("document_id", listViews[i].document_id)
                                    jGroup.put("softcopy", listViews[i].file)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addImportantDocument(rootObject.toString())
                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                }
                else
                {
                    if (holder_name.equals("A"))
                    {
                        if (isValidInputForOneHolder())
                        {
                            if (isValidInputForOther())
                            {
                                val rootObject = JSONObject()
                                val rootObjectItems = JSONObject()
                                val dataArray = JSONArray()

                                for (i in listViews.indices)
                                {
                                    if (listViews[i].document.isNotEmpty() && listViews[i].location.isNotEmpty() && listViews[i].file.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("location", listViews[i].location)
                                        jGroup.put("document", listViews[i].document)
                                        jGroup.put("user_id", sessionManager.userId)
                                        jGroup.put("holder", listViews[i].holder)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)

                                addImportantDocument(rootObject.toString())
                                Log.e("<><> JSON DATA : ", rootObject.toString())
                            }
                        }
                    }
                    else
                    {
                        val rootObject = JSONObject()
                        val rootObjectItems = JSONObject()
                        val dataArray = JSONArray()

                        for (i in listViews.indices)
                        {
                            if (listViews[i].document.isEmpty() && listViews[i].location.isEmpty() && listViews[i].file.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].document.isNotEmpty() && listViews[i].location.isNotEmpty() && listViews[i].file.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("document", listViews[i].document)
                                jGroup.put("user_id", sessionManager.userId)
                                jGroup.put("holder", listViews[i].holder)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }
                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)

                        if (dataArray.length() > 0)
                        {
                            addImportantDocument(rootObject.toString())
                        }

                        Log.e("<><> JSON DATA : ", rootObject.toString())
                    }
                }
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun isValidInputForOneHolder(): Boolean
    {
        var isValid: Boolean = false

        when
        {
            listViews[0].document.isEmpty() ->
            {
                getViewHolder(0).itemView.ipDocument.error = "Please enter document name."
                isValid = false
            }
            listViews[0].location.isEmpty() ->
            {
                getViewHolder(0).itemView.ipLocation.error = "Please enter original  location."
                isValid = false
            }
            listViews[0].file.isEmpty() ->
            {
                getViewHolder(0).itemView.ipFile.error = "Please upload file document."
                isValid = false
            }
            else ->
            {
                isValid = true
            }
        }

        return isValid
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid: Boolean = true;

        for (i in listViews.indices)
        {
            if (listViews[i].document.isEmpty() && listViews[i].location.isEmpty() && listViews[i].file.isEmpty() || listViews[i].document.isNotEmpty() && listViews[i].location.isNotEmpty() && listViews[i].file.isNotEmpty())
            {
                continue
            }
            else
            {
                isValid = false
                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                break
            }
        }

        return isValid
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        val viewHolder: ViewsAdapter.ViewHolder =
            binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
        return viewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(
                LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_imp_doc, parent, false)
            )
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddImpDocGetSet = listViews[position]

            if (holder_name == "A")
            {
                if (position == 0)
                {
                    holder.itemView.txtMandatory.visibility = View.VISIBLE
                }
                else
                {
                    holder.itemView.txtMandatory.visibility = View.GONE
                }
            }
            else
            {
                holder.itemView.txtMandatory.visibility = View.GONE
            }

            if (isForEdit)
            {
                gone(holder.itemView.llDelete)
            }
            else
            {
                if (position == 0)
                {
                    gone(holder.itemView.llDelete)
                }
                else
                {
                    visible(holder.itemView.llDelete)
                }
            }

            holder.itemView.txtHolderName.text = holder_userName
            holder.itemView.edtLocation.setText(getSet.location)
            holder.itemView.edtDocument.setText(getSet.document)

            if(getSet.file.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.file))
            }

            holder.itemView.edtLocation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocation.isErrorEnabled = false
                    listViews.get(position).location = charSequence.toString().trim()
                }
            }
            holder.itemView.edtDocument.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipDocument.isErrorEnabled = false
                    listViews.get(position).document = charSequence.toString().trim()
                }
            }

            holder.itemView.edtFile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    if(charSequence.toString().trim().length >0)
                    {
                        holder.itemView.ipFile.isErrorEnabled = false
                    }
                }
            }

            holder.itemView.edtFile.setOnClickListener {

                val permissionlistener  = object : PermissionListener
                {
                    override fun onPermissionGranted()
                    {
                        try
                        {
                            ImagePicker.with(activity).galleryOnly().compress(1024).maxResultSize(1080,
                                1080).start { resultCode, data ->
                                when (resultCode)
                                {
                                    Activity.RESULT_OK ->
                                    {
                                        val filePath= ImagePicker.getFilePath(data).toString()
                                        getSet.file =filePath

                                        Log.d(">>>>>>>", "<><>$filePath")

                                        val f = File(filePath)
                                        val imageName = f.name
                                        listViews[position] = getSet
                                        holder.itemView.edtFile.setText(imageName)

                                    }
                                    ImagePicker.RESULT_ERROR ->
                                    {
                                        showToast(ImagePicker.getError(data))
                                    }
                                    else ->
                                    {
                                    }
                                }
                            }
                        }
                        catch (e: Exception)
                        {
                            e.printStackTrace()
                        }
                    }

                    override fun onPermissionDenied(deniedPermissions: ArrayList<String>?)
                    {
                        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(activity)
                        alertDialogBuilder.setTitle("Read Storage Permission")
                        alertDialogBuilder
                            .setMessage("Please enable storage permission to upload document")
                            .setCancelable(false)
                            .setPositiveButton("Settings") { dialog, id ->
                                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                val uri: Uri = Uri.fromParts("package", packageName, null)
                                intent.data = uri
                                startActivityForResult(intent, 1000) // Comment 3.
                            }
                            .setNegativeButton("Close"){dialog, id ->
                                dialog.dismiss()
                            }
                        val alertDialog: AlertDialog = alertDialogBuilder.create()
                        alertDialog.show()
                    }
                }
                MitsPermissions(activity).setPermissionListener(permissionlistener).setPermissions(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).check()
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part
    {
        val requestFile: RequestBody = RequestBody.create(file.path.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }


    private fun addImportantDocument(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE

                val parts: MutableList<MultipartBody.Part> = mutableListOf()
                if (isForEdit)
                {
                    if (listViews[0].file.contains("/storage/emulated/"))
                    {
                        val paramName ="files["+holder_id+"]["+0+"]"
                        parts.add(prepareFilePart(paramName, File(listViews[0].file)))
                    }
                }
                else
                {
                    for (i in listViews.indices)
                    {
                        val paramName ="files["+holder_id+"]["+i+"]"
                        Log.e("<><> FILE NAME : " ,listViews[i].file + " <><>")
                        parts.add(prepareFilePart(paramName, File(listViews[i].file)))
                    }
                }

                val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")


                val call = apiService.impDocSaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(
                        call: Call<CommonResponse>, response: Response<CommonResponse>
                    )
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                appUtils.sendMessageFromHandler(ImpDocListActivity.handler, "null", 1, 0, 0)
                                activity.finish()
                                finishActivityAnimation()
                            }
                            else
                            {
                                toast(response.body()!!.message)
                            }

                            binding.loading.llLoading.visibility = View.GONE
                        }
                        else
                        {
                            binding.loading.llLoading.visibility = View.GONE
                            apiFailedToast()
                        }
                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                        Log.d(">>>>", t.message.toString())
                    }
                })
            }
            else
            {
                noInternetToast()
            }
        } catch (e: Exception)
        {
        }
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
