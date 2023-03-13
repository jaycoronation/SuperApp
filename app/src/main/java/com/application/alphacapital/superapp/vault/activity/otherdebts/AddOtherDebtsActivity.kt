package com.application.alphacapital.superapp.vault.activity.otherdebts

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
import com.alphaestatevault.model.*
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddCommonBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.edtCreatedon
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.edtNotes
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.ipCreatedon
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.ipNotes
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.txtMandatory
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

class AddOtherDebtsActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddCommonBinding
    var listViews: MutableList<AddOtherDebtsGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var detailGetSet: OtherDebtsResponse.OtherDebtsDetail

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_common)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                detailGetSet = Gson().fromJson(intent.getStringExtra("data"), OtherDebtsResponse.OtherDebtsDetail::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Other Debts".takeIf { !isForEdit } ?: "Update Other Debts"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddOtherDebtsGetSet(detailGetSet.holder, detailGetSet.description, detailGetSet.currently_outstanding, detailGetSet.terms, detailGetSet.to_whom_owed, detailGetSet.contact, detailGetSet.phone, detailGetSet.address, detailGetSet.created_on, detailGetSet.notes, detailGetSet.upload_doc, detailGetSet.other_debt_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddOtherDebtsGetSet(holder_name, "", "", "", "", "", "", "", "", "", "", ""))
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
            listViews.add(AddOtherDebtsGetSet(holder_name, "", "", "", "", "", "", "", "", "", "", ""))
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
                                if (listViews[i].description.isNotEmpty() &&
                                    listViews[i].currently_outstanding.isNotEmpty() &&
                                    listViews[i].terms.isNotEmpty() &&
                                    listViews[i].to_whom_owed.isNotEmpty() &&
                                    listViews[i].contact.isNotEmpty() &&
                                    listViews[i].phone.isNotEmpty() &&
                                    listViews[i].phone.length == 10 &&
                                    listViews[i].address.isNotEmpty() &&
                                    listViews[i].created_on.isNotEmpty() &&
                                    listViews[i].notes.isNotEmpty() &&
                                    listViews[i].upload_doc.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("description", listViews[i].description)
                                    jGroup.put("currently_outstanding", listViews[i].currently_outstanding)
                                    jGroup.put("terms", listViews[i].terms)
                                    jGroup.put("to_whom_owed", listViews[i].to_whom_owed)
                                    jGroup.put("contact", listViews[i].contact)
                                    jGroup.put("phone", listViews[i].phone)
                                    jGroup.put("address", listViews[i].address)
                                    jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                    jGroup.put("notes", listViews[i].notes)
                                    jGroup.put("upload_doc", listViews[i].upload_doc)
                                    jGroup.put("other_debt_id", listViews[i].other_debt_id)
                                    dataArray.put(jGroup)
                                }
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)

                            addFinancialInstitutionAccount(rootObject.toString())

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
                                    if (listViews[i].description.isNotEmpty() &&
                                        listViews[i].currently_outstanding.isNotEmpty() &&
                                        listViews[i].terms.isNotEmpty() &&
                                        listViews[i].to_whom_owed.isNotEmpty() &&
                                        listViews[i].contact.isNotEmpty() &&
                                        listViews[i].phone.isNotEmpty() &&
                                        listViews[i].phone.length == 10 &&
                                        listViews[i].address.isNotEmpty() &&
                                        listViews[i].created_on.isNotEmpty() &&
                                        listViews[i].notes.isNotEmpty() &&
                                        listViews[i].upload_doc.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("description", listViews[i].description)
                                        jGroup.put("currently_outstanding", listViews[i].currently_outstanding)
                                        jGroup.put("terms", listViews[i].terms)
                                        jGroup.put("to_whom_owed", listViews[i].to_whom_owed)
                                        jGroup.put("contact", listViews[i].contact)
                                        jGroup.put("phone", listViews[i].phone)
                                        jGroup.put("address", listViews[i].address)
                                        jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                        jGroup.put("notes", listViews[i].notes)
                                        jGroup.put("upload_doc", listViews[i].upload_doc)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addFinancialInstitutionAccount(rootObject.toString())
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
                            if (listViews[i].description.isEmpty() && listViews[i].currently_outstanding.isEmpty() && listViews[i].terms.isEmpty() && listViews[i].to_whom_owed.isEmpty() && listViews[i].contact.isEmpty() && listViews[i].phone.isEmpty() && listViews[i].address.isEmpty() && listViews[i].created_on.isEmpty() && listViews[i].notes.isEmpty() && listViews[i].upload_doc.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].description.isNotEmpty() &&
                                listViews[i].currently_outstanding.isNotEmpty() &&
                                listViews[i].terms.isNotEmpty() &&
                                listViews[i].to_whom_owed.isNotEmpty() &&
                                listViews[i].contact.isNotEmpty() &&
                                listViews[i].phone.isNotEmpty() &&
                                listViews[i].phone.length == 10 &&
                                listViews[i].address.isNotEmpty() &&
                                listViews[i].created_on.isNotEmpty() &&
                                listViews[i].notes.isNotEmpty() &&
                                listViews[i].upload_doc.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("description", listViews[i].description)
                                jGroup.put("currently_outstanding", listViews[i].currently_outstanding)
                                jGroup.put("terms", listViews[i].terms)
                                jGroup.put("to_whom_owed", listViews[i].to_whom_owed)
                                jGroup.put("contact", listViews[i].contact)
                                jGroup.put("phone", listViews[i].phone)
                                jGroup.put("address", listViews[i].address)
                                jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }
                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)
                        if (dataArray.length() > 0)
                        {
                            addFinancialInstitutionAccount(rootObject.toString())
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
        var isValid = false

        when
        {
            listViews[0].description.isEmpty() ->
            {
                getViewHolder(0).itemView.ipdescription.error = "Please enter description."
                isValid = false
            }
            listViews[0].currently_outstanding.isEmpty() ->
            {
                getViewHolder(0).itemView.ipcurrently_outstanding.error = "Please enter nature of currently_outstanding."
                isValid = false
            }
            listViews[0].terms.isEmpty() ->
            {
                getViewHolder(0).itemView.ipterms.error = "Please enter terms."
                isValid = false
            }
            listViews[0].to_whom_owed.isEmpty() ->
            {
                getViewHolder(0).itemView.ipto_whom_owed.error = "Please enter to_whom_owed."
                isValid = false
            }
            listViews[0].contact.isEmpty() ->
            {
                getViewHolder(0).itemView.ipcontact.error = "Please enter contact details."
                isValid = false
            }
            listViews[0].phone.isEmpty() ->
            {
                getViewHolder(0).itemView.ipPhone.error = "Please enter phone."
                isValid = false
            }
            listViews[0].phone.length != 10 ->
            {
                getViewHolder(0).itemView.ipPhone.error = "Please enter valid phone number."
                isValid = false
            }
            listViews[0].address.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAddress.error = "Please enter address."
                isValid = false
            }
            listViews[0].created_on.isEmpty() ->
            {
                getViewHolder(0).itemView.ipCreatedon.error = "Please select created on."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNotes.error = "Please enter notes."
                isValid = false
            }
            listViews[0].upload_doc.isEmpty() ->
            {
                getViewHolder(0).itemView.ipFile.error = "Please enter upload document."
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
        var isValid = true

        for (i in listViews.indices)
        {
            if (listViews[i].description.isEmpty() &&
                listViews[i].currently_outstanding.isEmpty() &&
                listViews[i].terms.isEmpty() &&
                listViews[i].to_whom_owed.isEmpty() &&
                listViews[i].contact.isEmpty() &&
                listViews[i].phone.isEmpty() &&
                listViews[i].address.isEmpty() &&
                listViews[i].created_on.isEmpty() &&
                listViews[i].notes.isEmpty() &&
                listViews[i].upload_doc.isEmpty() ||
                listViews[i].description.isNotEmpty() &&
                listViews[i].currently_outstanding.isNotEmpty() &&
                listViews[i].terms.isNotEmpty() &&
                listViews[i].to_whom_owed.isNotEmpty() &&
                listViews[i].contact.isNotEmpty() &&
                listViews[i].phone.isNotEmpty() &&
                listViews[i].phone.length == 10 &&
                listViews[i].address.isNotEmpty() &&
                listViews[i].created_on.isNotEmpty() &&
                listViews[i].notes.isNotEmpty() &&
                listViews[i].upload_doc.isNotEmpty())
            {
                continue
            }
            else
            {
                isValid = false
                showToast("Please Enter or Clear all fields value of Holder " + listViews[i].holder + ".")
                break
            }
        }

        return isValid
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_other_debts, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddOtherDebtsGetSet = listViews[position]

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

            holder.itemView.edtdescription.setText(getSet.description)
            holder.itemView.edtcurrently_outstanding.setText(getSet.currently_outstanding)
            holder.itemView.edtterms.setText(getSet.terms)
            holder.itemView.edtto_whom_owed.setText(getSet.to_whom_owed)
            holder.itemView.edtcontact.setText(getSet.contact)
            holder.itemView.edtPhone.setText(getSet.phone)
            holder.itemView.edtAddress.setText(getSet.address)

            holder.itemView.edtCreatedon.setText(getSet.created_on)
            holder.itemView.edtNotes.setText(getSet.notes)

            if (getSet.upload_doc.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.upload_doc))
            }

            holder.itemView.edtdescription.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipdescription.isErrorEnabled = false
                    listViews[position].description = charSequence.toString().trim()
                }
            }

            holder.itemView.edtcurrently_outstanding.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipcurrently_outstanding.isErrorEnabled = false
                    listViews[position].currently_outstanding = charSequence.toString().trim()
                }
            }

            holder.itemView.edtterms.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipterms.isErrorEnabled = false
                    listViews[position].terms = charSequence.toString().trim()
                }
            }

            holder.itemView.edtto_whom_owed.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipto_whom_owed.isErrorEnabled = false
                    listViews[position].to_whom_owed = charSequence.toString().trim()
                }
            }

            holder.itemView.edtcontact.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipcontact.isErrorEnabled = false
                    listViews[position].contact = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPhone.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPhone.isErrorEnabled = false
                    listViews[position].phone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    listViews[position].address = charSequence.toString().trim()
                }
            }

            holder.itemView.edtCreatedon.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipCreatedon.isErrorEnabled = false
                    listViews[position].created_on = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNotes.isErrorEnabled = false
                    listViews[position].notes = charSequence.toString().trim()
                }
            }

            holder.itemView.edtFile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    if (charSequence.toString().trim().length > 0)
                    {
                        holder.itemView.ipFile.isErrorEnabled = false
                    }
                }
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }

            holder.itemView.edtCreatedon.setOnClickListener {
                appUtils.showDatePicker(activity,holder.itemView.edtCreatedon)
            }

            holder.itemView.edtFile.setOnClickListener {
                val permissionlistener  = object : PermissionListener
                {
                    override fun onPermissionGranted()
                    {

                        try
                        {
                            ImagePicker
                                .with(activity)
                                .galleryOnly()
                                .compress(1024)
                                .maxResultSize(1080,1080)
                                .start { resultCode, data ->
                                when (resultCode)
                                {
                                    Activity.RESULT_OK ->
                                    {
                                        val filePath: String = ImagePicker.getFilePath(data).toString()
                                        getSet.upload_doc = filePath
                                        listViews[position] = getSet
                                        val f = File(filePath)
                                        val imageName = f.name
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

                        /* RxPaparazzo.single(activity).usingFiles().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { response ->
                            if (response.resultCode() != Activity.RESULT_OK)
                            {
                                showToast("Error while select image from gallery")
                                return@subscribe
                            }
                            getSet.upload_doc = response.data().file.path
                            listViews[position] = getSet
                            holder.itemView.edtFile.setText(response.data().file.name)

                        }*/
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

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addFinancialInstitutionAccount(items: String)
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
                    if (listViews[0].upload_doc.contains("/storage/emulated/"))
                    {
                        val paramName = "upload_doc[" + holder_id + "][" + 0 + "]"
                        parts.add(prepareFilePart(paramName, File(listViews[0].upload_doc)))
                    }
                }
                else
                {
                    for (i in listViews.indices)
                    {
                        val paramName = "upload_doc[" + holder_id + "][" + i + "]"
                        parts.add(prepareFilePart(paramName, File(listViews[i].upload_doc)))
                    }
                }

                val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")
                val call = apiService.other_debtsSaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(OtherDebtsListActivity.handler, "null", 1, 0, 0)

                                activity.finish()
                                finishActivityAnimation()
                            }
                        }
                        else
                        {
                            apiFailedToast()

                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {
                        binding.loading.llLoading.visibility = View.GONE
                        apiFailedToast()
                    }
                })
            }
            else
            {
                noInternetToast()
            }
        }
        catch (e: Exception)
        {
        }
    }

    private fun prepareFilePart(partName: String, file: File): MultipartBody.Part
    {
        val requestFile: RequestBody = RequestBody.create(file.path.toMediaTypeOrNull(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
