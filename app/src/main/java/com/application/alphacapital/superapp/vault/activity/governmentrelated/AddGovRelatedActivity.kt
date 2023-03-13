package com.application.alphacapital.superapp.vault.activity.governmentrelated

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
import com.alphaestatevault.model.AddGovRelatedGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.GovRelatedListResponse
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddGovRelatedBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_financial_institution_account.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.edtNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.edtNotes
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.ipNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.ipNotes
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.txtMandatory
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

class AddGovRelatedActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddGovRelatedBinding
    var listViews: MutableList<AddGovRelatedGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var govRelatedGetSet: GovRelatedListResponse.GovernmentRelatedDetail

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_gov_related)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                govRelatedGetSet = Gson().fromJson(intent.getStringExtra("data"), GovRelatedListResponse.GovernmentRelatedDetail::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Gov. Related".takeIf { !isForEdit } ?: "Update Gov. Related"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddGovRelatedGetSet(govRelatedGetSet.asset_name, govRelatedGetSet.institution, govRelatedGetSet.account_number, govRelatedGetSet.amount,
                govRelatedGetSet.contact_person,govRelatedGetSet.location_of_document,govRelatedGetSet.upload_doc, govRelatedGetSet.notes,govRelatedGetSet.holder,
                govRelatedGetSet.government_related_id,govRelatedGetSet.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddGovRelatedGetSet("", "", "", "", "", "", "", "", holder_name, "",""))
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
            listViews.add(AddGovRelatedGetSet("", "", "", "", "", "", "", "", holder_name, "",""))
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
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("asset_name", listViews[i].asset_name)
                                jGroup.put("institution", listViews[i].institution)
                                jGroup.put("account_number", listViews[i].account_number)
                                jGroup.put("amount", listViews[i].amount)
                                jGroup.put("contact_person", listViews[i].contact_person)
                                jGroup.put("location_of_document", listViews[i].location_of_document)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
                                jGroup.put("government_related_id", listViews[i].government_related_id)
                                dataArray.put(jGroup)
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addSafeDeposit(rootObject.toString())
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
                                    if (listViews[i].asset_name.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("asset_name", listViews[i].asset_name)
                                        jGroup.put("institution", listViews[i].institution)
                                        jGroup.put("account_number", listViews[i].account_number)
                                        jGroup.put("amount", listViews[i].amount)
                                        jGroup.put("nominee_name", listViews[i].nominee_name)
                                        jGroup.put("contact_person", listViews[i].contact_person)
                                        jGroup.put("location_of_document", listViews[i].location_of_document)
                                        jGroup.put("notes", listViews[i].notes)
                                        dataArray.put(jGroup)
                                    }
                                }

                                rootObjectItems.put(holder_id, dataArray)
                                rootObject.put("items", rootObjectItems)
                                addSafeDeposit(rootObject.toString())
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
                            if (listViews[i].asset_name.isEmpty() && listViews[i].institution.isEmpty() && listViews[i].account_number.isEmpty() && listViews[i].amount.isEmpty() && listViews[i].contact_person.isEmpty() && listViews[i].location_of_document.isEmpty() && listViews[i].notes.isEmpty() && listViews[i].upload_doc.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].asset_name.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("asset_name", listViews[i].asset_name)
                                jGroup.put("institution", listViews[i].institution)
                                jGroup.put("account_number", listViews[i].account_number)
                                jGroup.put("amount", listViews[i].amount)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("contact_person", listViews[i].contact_person)
                                jGroup.put("location_of_document", listViews[i].location_of_document)
                                jGroup.put("notes", listViews[i].notes)
                                dataArray.put(jGroup)
                            }
                            else
                            {
                                showToast("Please Enter valid or Clear all fields value of Holder " + listViews[i].holder + ".")
                                break
                            }
                        }

                        Log.e("<><> JSON DATA : ", dataArray.length().toString() + "")
                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)
                        if (dataArray.length() > 0)
                        {
                            addSafeDeposit(rootObject.toString())
                        }
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
            listViews[0].asset_name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAssetName.error = "Please enter Asset Name."
                isValid = false
            }
            listViews[0].institution.isEmpty() ->
            {
                getViewHolder(0).itemView.ipInstitution.error = "Please enter institution."
                isValid = false
            }
            listViews[0].account_number.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAccountNumber.error = "Please enter account number."
                isValid = false
            }
            listViews[0].amount.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAmount.error = "Please enter amount."
                isValid = false
            }
            listViews[0].contact_person.isEmpty() ->
            {
                getViewHolder(0).itemView.ipContactPerson.error = "Please enter contact person."
                isValid = false
            }
            listViews[0].location_of_document.isEmpty() ->
            {
                getViewHolder(0).itemView.ipLocationOfDocument.error = "Please enter location of document."
                isValid = false
            }
            listViews[0].notes.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNotes.error = "Please enter notes."
                isValid = false
            }
            listViews[0].upload_doc.isEmpty() ->
            {
                getViewHolder(0).itemView.ipFile.error = "Please upload document."
                isValid = false
            }
            listViews[0].nominee_name.isEmpty() ->
            {
                getViewHolder(0).itemView.ipNominee_name.error = "Please enter nominee name."
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
            if (listViews[i].asset_name.isEmpty() && listViews[i].institution.isEmpty() && listViews[i].account_number.isEmpty() &&
                listViews[i].amount.isEmpty() && listViews[i].contact_person.isEmpty() && listViews[i].location_of_document.isEmpty() &&
                listViews[i].notes.isEmpty() && listViews[i].upload_doc.isEmpty() && listViews[i].nominee_name.isEmpty() || listViews[i].asset_name.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty() && listViews[i].nominee_name.isNotEmpty() )
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_gov_related, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddGovRelatedGetSet = listViews[position]

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
            holder.itemView.edtAssetName.setText(getSet.asset_name)
            holder.itemView.edtAccountNumber.setText(getSet.account_number)
            holder.itemView.edtInstitution.setText(getSet.institution)
            holder.itemView.edtAmount.setText(getSet.amount)
            holder.itemView.edtContactPerson.setText(getSet.contact_person)
            holder.itemView.edtLocationOfDocument.setText(getSet.location_of_document)
            holder.itemView.edtNotes.setText(getSet.notes)

            holder.itemView.edtNominee_name.setText(getSet.nominee_name)

            if (getSet.upload_doc.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.upload_doc))
            }


            holder.itemView.edtAssetName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAssetName.isErrorEnabled = false
                    listViews[position].asset_name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtInstitution.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipInstitution.isErrorEnabled = false
                    listViews[position].institution = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAccountNumber.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAccountNumber.isErrorEnabled = false
                    listViews[position].account_number = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAmount.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAmount.isErrorEnabled = false
                    listViews[position].amount = charSequence.toString().trim()
                }
            }

            holder.itemView.edtContactPerson.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipContactPerson.isErrorEnabled = false
                    listViews[position].contact_person = charSequence.toString().trim()
                }
            }

            holder.itemView.edtLocationOfDocument.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocationOfDocument.isErrorEnabled = false
                    listViews[position].location_of_document = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNotes.isErrorEnabled = false
                    listViews[position].notes = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
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

                                        getSet.upload_doc = filePath

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
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addSafeDeposit(items: String)
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
                        val paramName ="upload_doc["+holder_id+"]["+0+"]"
                        parts.add(prepareFilePart(paramName, File(listViews[0].upload_doc)))
                    }
                }
                else
                {
                    for (i in listViews.indices)
                    {
                        val paramName ="upload_doc["+holder_id+"]["+i+"]"
                        parts.add(prepareFilePart(paramName, File(listViews[i].upload_doc)))
                    }
                }

                val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")


                val call = apiService.govRelatedSaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(GovRelatedListActivity.handler, "null", 1, 0, 0)

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
