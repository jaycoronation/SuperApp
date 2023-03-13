package com.application.alphacapital.superapp.vault.activity.realproperty

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
import com.application.alphacapital.superapp.databinding.VaultActivityAddRealPropertyBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_gov_related.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.edtNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.ipNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_real_property.view.txtMandatory
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

class AddRealPropertyActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddRealPropertyBinding
    var listViews: MutableList<AddRealPropertyGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var realPropertyDetails: RealPropertyResponse.RealProperty

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_real_property)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                realPropertyDetails = Gson().fromJson(intent.getStringExtra("data"), RealPropertyResponse.RealProperty::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Real Estate".takeIf { !isForEdit } ?: "Update Real Estate"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddRealPropertyGetSet(realPropertyDetails.type_of_property,
                realPropertyDetails.name,
                realPropertyDetails.location,
                realPropertyDetails.encumbrances,
                realPropertyDetails.approximate_value,
                realPropertyDetails.loan,
                realPropertyDetails.rent,
                realPropertyDetails.purchased_on,
                realPropertyDetails.location_of_document,
                realPropertyDetails.upload_doc,
                realPropertyDetails.notes,
                realPropertyDetails.holder,
                realPropertyDetails.real_property_id,
                realPropertyDetails.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddRealPropertyGetSet("", "", "", "", "", "", "", "", "", "", "", holder_name, "",""))
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
            listViews.add(AddRealPropertyGetSet("", "", "", "", "", "", "", "", "", "", "", holder_name, "",""))
            viewsAdapter.notifyDataSetChanged()
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                if (isForEdit)
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
                            jGroup.put("type_of_property", listViews[i].type_of_property)
                            jGroup.put("name", listViews[i].name)
                            jGroup.put("location", listViews[i].location)
                            jGroup.put("encumbrances", listViews[i].encumbrances)
                            jGroup.put("approximate_value", listViews[i].approximate_value)
                            jGroup.put("loan", listViews[i].loan)
                            jGroup.put("rent", listViews[i].rent)
                            jGroup.put("nominee_name", listViews[i].nominee_name)
                            jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchased_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                            jGroup.put("location_of_document", listViews[i].location_of_document)
                            jGroup.put("upload_doc", listViews[i].upload_doc)
                            jGroup.put("notes", listViews[i].notes)
                            jGroup.put("real_property_id", listViews[i].real_property_id)
                            dataArray.put(jGroup)
                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)
                        addFinancialInstitutionAccount(rootObject.toString())

                        Log.e("<><> JSON DATA : ", rootObject.toString())
                    }
                }
                else
                {
                    if (holder_name.equals("A"))
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
                                jGroup.put("type_of_property", listViews[i].type_of_property)
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("encumbrances", listViews[i].encumbrances)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("approximate_value", listViews[i].approximate_value)
                                jGroup.put("loan", listViews[i].loan)
                                jGroup.put("rent", listViews[i].rent)
                                jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchased_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                jGroup.put("location_of_document", listViews[i].location_of_document)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
                                jGroup.put("notes", listViews[i].notes)
                                dataArray.put(jGroup)
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addFinancialInstitutionAccount(rootObject.toString())
                            Log.e("<><> JSON DATA : ", rootObject.toString())
                        }
                    }
                    else
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
                                jGroup.put("type_of_property", listViews[i].type_of_property)
                                jGroup.put("name", listViews[i].name)
                                jGroup.put("location", listViews[i].location)
                                jGroup.put("encumbrances", listViews[i].encumbrances)
                                jGroup.put("approximate_value", listViews[i].approximate_value)
                                jGroup.put("loan", listViews[i].loan)
                                jGroup.put("rent", listViews[i].rent)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchased_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                jGroup.put("location_of_document", listViews[i].location_of_document)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
                                jGroup.put("notes", listViews[i].notes)
                                dataArray.put(jGroup)
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
            }
            else
            {
                noInternetToast()
            }
        }
    }


    private fun isValidInputForOneHolder(position: Int): Boolean
    {
        var isValid = false

        if(listViews[position].type_of_property.isEmpty())
        {
            getViewHolder(position).itemView.iptype_of_property.error = "Please enter type of property."
            isValid = false
        }
        else if(listViews[position].name.isEmpty())
        {
            getViewHolder(position).itemView.ipname.error = "Please enter name."
            isValid = false
        }
        else if(listViews[position].nominee_name.isEmpty())
        {
            getViewHolder(position).itemView.ipNominee_name.error = "Please enter nominee name."
            isValid = false
        }
        else
        {
            isValid = true
        }

        return isValid
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid = true

        for (i in listViews.indices)
        {
            isValid = isValidInputForOneHolder(i)
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_real_property, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddRealPropertyGetSet = listViews[position]

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
            holder.itemView.edttype_of_property.setText(getSet.type_of_property)
            holder.itemView.edtname.setText(getSet.name)
            holder.itemView.edtlocation.setText(getSet.location)
            holder.itemView.edtencumbrances.setText(getSet.encumbrances)
            holder.itemView.edtapproximate_value.setText(getSet.approximate_value)
            holder.itemView.edtloan.setText(getSet.loan)
            holder.itemView.edtrent.setText(getSet.rent)
            holder.itemView.edtpurchased_on.setText(getSet.purchased_on)
            holder.itemView.edtlocation_of_document.setText(getSet.location_of_document)
            holder.itemView.edtnotes.setText(getSet.notes)

            holder.itemView.edtNominee_name.setText(getSet.nominee_name)

            if (getSet.upload_doc.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.upload_doc))
            }

            holder.itemView.edttype_of_property.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iptype_of_property.isErrorEnabled = false
                    listViews[position].type_of_property = charSequence.toString().trim()
                }
            }

            holder.itemView.edtname.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipname.isErrorEnabled = false
                    listViews[position].name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtlocation.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iplocation.isErrorEnabled = false
                    listViews[position].location = charSequence.toString().trim()
                }
            }

            holder.itemView.edtencumbrances.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipencumbrances.isErrorEnabled = false
                    listViews[position].encumbrances = charSequence.toString().trim()
                }
            }

            holder.itemView.edtapproximate_value.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipapproximate_value.isErrorEnabled = false
                    listViews[position].approximate_value = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
                }
            }


            holder.itemView.edtloan.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iploan.isErrorEnabled = false
                    listViews[position].loan = charSequence.toString().trim()
                }
            }

            holder.itemView.edtrent.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iprent.isErrorEnabled = false
                    listViews[position].rent = charSequence.toString().trim()
                }
            }

            holder.itemView.edtpurchased_on.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ippurchased_on.isErrorEnabled = false
                    listViews[position].purchased_on = charSequence.toString().trim()
                }
            }

            holder.itemView.edtlocation_of_document.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.iplocation_of_document.isErrorEnabled = false
                    listViews[position].location_of_document = charSequence.toString().trim()
                }
            }

            holder.itemView.edtnotes.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipnotes.isErrorEnabled = false
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

            holder.itemView.edtpurchased_on.setOnClickListener {
                appUtils.showDatePicker(activity,holder.itemView.edtpurchased_on)
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
                    if(listViews[0].upload_doc.length >0)
                    {
                        if (listViews[0].upload_doc.contains("/storage/emulated/"))
                        {
                            val paramName = "upload_doc[" + holder_id + "][" + 0 + "]"
                            parts.add(prepareFilePart(paramName, File(listViews[0].upload_doc)))
                        }
                    }
                }
                else
                {
                    for (i in listViews.indices)
                    {
                        if(listViews[i].upload_doc.length >0)
                        {
                            val paramName = "upload_doc[" + holder_id + "][" + i + "]"
                            parts.add(prepareFilePart(paramName, File(listViews[i].upload_doc)))
                        }
                    }
                }

                val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")


                val call = apiService.realPropertySaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(RealPropertyListActivity.handler, "null", 1, 0, 0)

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
