package com.application.alphacapital.superapp.vault.activity.insurancepolicies

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
import com.alphaestatevault.model.AddInsurancePolicyGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.InsurancePoliciesListResponse
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddInsurancePoliciesBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.edtNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.edtNotes
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.ipNominee_name
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.ipNotes
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_insurance_policy.view.txtMandatory
import kotlinx.android.synthetic.main.vault_rowview_add_investment_trust_account.view.*
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

class AddInsurancePoliciesActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddInsurancePoliciesBinding
    var listViews: MutableList<AddInsurancePolicyGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var insurancePoliciesDetails: InsurancePoliciesListResponse.InsurancePoliciesDetail

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_insurance_policies)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                insurancePoliciesDetails = Gson().fromJson(intent.getStringExtra("data"), InsurancePoliciesListResponse.InsurancePoliciesDetail::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Insurance Policy".takeIf { !isForEdit } ?: "Update Insurance Policy"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddInsurancePolicyGetSet(insurancePoliciesDetails.insurance_company, insurancePoliciesDetails.type_of_policy, insurancePoliciesDetails.policy_number, insurancePoliciesDetails.person_thing_insured, insurancePoliciesDetails.sum_assured, insurancePoliciesDetails.current_value, insurancePoliciesDetails.purchased_on, insurancePoliciesDetails.agent_name, insurancePoliciesDetails.agent_phone, insurancePoliciesDetails.agent_address, insurancePoliciesDetails.location_of_document, insurancePoliciesDetails.upload_doc, insurancePoliciesDetails.notes, insurancePoliciesDetails.holder, insurancePoliciesDetails.insurance_policies_id,insurancePoliciesDetails.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddInsurancePolicyGetSet("", "", "", "", "", "", "", "", "", "", "", "", "", holder_name, "",""))
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
            listViews.add(AddInsurancePolicyGetSet("", "", "", "", "", "", "", "", "", "", "", "", "", holder_name, "",""))
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
                            jGroup.put("insurance_company", listViews[i].insuranceCompany)
                            jGroup.put("type_of_policy", listViews[i].typeofpolicy)
                            jGroup.put("policy_number", listViews[i].policynumber)
                            jGroup.put("person_thing_insured", listViews[i].personthinginsured)
                            jGroup.put("sum_assured", listViews[i].sumassured)
                            jGroup.put("nominee_name", listViews[i].nominee_name)
                            jGroup.put("current_value", listViews[i].currentvalue)

                            if(listViews[i].purchasedon.length >0)
                            {
                                jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchasedon, "dd/MM/yyyy", "yyyy-MM-dd"))
                            }
                            else
                            {
                                jGroup.put("purchased_on", "")
                            }

                            jGroup.put("agent_name", listViews[i].agentname)
                            jGroup.put("agent_phone", listViews[i].agentphone)
                            jGroup.put("agent_address", listViews[i].agentaddress)
                            jGroup.put("location_of_document", listViews[i].locationofdocument)
                            jGroup.put("notes", listViews[i].notes)
                            jGroup.put("upload_doc", listViews[i].uploaddocument)
                            jGroup.put("insurance_policies_id", listViews[i].insurancepolicyId)
                            dataArray.put(jGroup)
                        }

                        rootObjectItems.put(holder_id, dataArray)
                        rootObject.put("items", rootObjectItems)
                        addInsurancePolicy(rootObject.toString())

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
                                jGroup.put("insurance_company", listViews[i].insuranceCompany)
                                jGroup.put("type_of_policy", listViews[i].typeofpolicy)
                                jGroup.put("policy_number", listViews[i].policynumber)
                                jGroup.put("person_thing_insured", listViews[i].personthinginsured)
                                jGroup.put("sum_assured", listViews[i].sumassured)
                                jGroup.put("current_value", listViews[i].currentvalue)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                if(listViews[i].purchasedon.length >0)
                                {
                                    jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchasedon, "dd/MM/yyyy", "yyyy-MM-dd"))
                                }
                                else
                                {
                                    jGroup.put("purchased_on", "")
                                }
                                jGroup.put("agent_name", listViews[i].agentname)
                                jGroup.put("agent_phone", listViews[i].agentphone)
                                jGroup.put("agent_address", listViews[i].agentaddress)
                                jGroup.put("location_of_document", listViews[i].locationofdocument)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("upload_doc", listViews[i].uploaddocument)
                                dataArray.put(jGroup)
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)
                            addInsurancePolicy(rootObject.toString())
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
                                jGroup.put("insurance_company", listViews[i].insuranceCompany)
                                jGroup.put("type_of_policy", listViews[i].typeofpolicy)
                                jGroup.put("policy_number", listViews[i].policynumber)
                                jGroup.put("person_thing_insured", listViews[i].personthinginsured)
                                jGroup.put("sum_assured", listViews[i].sumassured)
                                jGroup.put("current_value", listViews[i].currentvalue)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                if(listViews[i].purchasedon.length >0)
                                {
                                    jGroup.put("purchased_on", appUtils.universalDateConvert(listViews[i].purchasedon, "dd/MM/yyyy", "yyyy-MM-dd"))
                                }
                                else
                                {
                                    jGroup.put("purchased_on", "")
                                }
                                jGroup.put("agent_name", listViews[i].agentname)
                                jGroup.put("agent_phone", listViews[i].agentphone)
                                jGroup.put("agent_address", listViews[i].agentaddress)
                                jGroup.put("location_of_document", listViews[i].locationofdocument)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("upload_doc", listViews[i].uploaddocument)
                                dataArray.put(jGroup)
                            }

                            rootObjectItems.put(holder_id, dataArray)
                            rootObject.put("items", rootObjectItems)

                            if (dataArray.length() > 0)
                            {
                                addInsurancePolicy(rootObject.toString())
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

        if(listViews[position].insuranceCompany.isEmpty())
        {
            getViewHolder(position).itemView.ipInsuranceCompany.error = "Please enter insurance company."
            isValid = false
        }
        else if(listViews[position].agentphone.isNotEmpty() && listViews[position].agentphone.length != 10)
        {
            getViewHolder(position).itemView.ipAgentPhone.error = "Please enter valid agent phone number."
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

        return true
    }

    private fun isValidInputForOther(): Boolean
    {
        var isValid = true

        for (i in listViews.indices)
        {
            isValid = isValidInputForOneHolder(i)
        }

        return true
    }

    private fun getViewHolder(i: Int): RecyclerView.ViewHolder
    {
        return binding.rvList.findViewHolderForLayoutPosition(i) as ViewsAdapter.ViewHolder
    }

    inner class ViewsAdapter : RecyclerView.Adapter<ViewsAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_insurance_policy, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddInsurancePolicyGetSet = listViews[position]

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
            holder.itemView.edtInsuranceCompany.setText(getSet.insuranceCompany)
            holder.itemView.edtTypeofPolicy.setText(getSet.typeofpolicy)
            holder.itemView.edtPolicyNumber.setText(getSet.policynumber)
            holder.itemView.edtPersonThingInsured.setText(getSet.personthinginsured)
            holder.itemView.edtSumAssured.setText(getSet.sumassured)
            holder.itemView.edtCurrentValue.setText(getSet.currentvalue)

            holder.itemView.edtPurchasedon.setText(getSet.purchasedon)
            holder.itemView.edtAgentName.setText(getSet.agentname)
            holder.itemView.edtAgentPhone.setText(getSet.agentphone)
            holder.itemView.edtAddress.setText(getSet.agentaddress)
            holder.itemView.edtLocationofdocument.setText(getSet.personthinginsured)
            holder.itemView.edtNominee_name.setText(getSet.nominee_name)
            holder.itemView.edtNotes.setText(getSet.personthinginsured)

            if (getSet.uploaddocument.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.uploaddocument))
            }

            holder.itemView.edtInsuranceCompany.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipInsuranceCompany.isErrorEnabled = false
                    listViews[position].insuranceCompany = charSequence.toString().trim()
                }
            }

            holder.itemView.edtTypeofPolicy.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipTypeofPolicy.isErrorEnabled = false
                    listViews[position].typeofpolicy = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPolicyNumber.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPolicyNumber.isErrorEnabled = false
                    listViews[position].policynumber = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPersonThingInsured.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPersonThingInsured.isErrorEnabled = false
                    listViews[position].personthinginsured = charSequence.toString().trim()
                }
            }

            holder.itemView.edtSumAssured.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipSumAssured.isErrorEnabled = false
                    listViews[position].sumassured = charSequence.toString().trim()
                }
            }

            holder.itemView.edtCurrentValue.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipCurrentValue.isErrorEnabled = false
                    listViews[position].currentvalue = charSequence.toString().trim()
                }
            }

            holder.itemView.edtPurchasedon.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipPurchasedon.isErrorEnabled = false
                    listViews[position].purchasedon = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAgentName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAgentName.isErrorEnabled = false
                    listViews[position].agentname = charSequence.toString().trim()
                }
            }

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAgentPhone.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAgentPhone.isErrorEnabled = false
                    listViews[position].agentphone = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAddress.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAddress.isErrorEnabled = false
                    listViews[position].agentaddress = charSequence.toString().trim()
                }
            }

            holder.itemView.edtLocationofdocument.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipLocationofdocument.isErrorEnabled = false
                    listViews[position].locationofdocument = charSequence.toString().trim()
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
                                        getSet.uploaddocument =filePath

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

            holder.itemView.edtPurchasedon.setOnClickListener {
                appUtils.showDatePicker(activity,holder.itemView.edtPurchasedon)
            }

            holder.itemView.llDelete.setOnClickListener {
                listViews.removeAt(position)
                notifyDataSetChanged()
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    private fun addInsurancePolicy(items: String)
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
                    if(listViews[0].uploaddocument.length >0)
                    {
                        if (listViews[0].uploaddocument.contains("/storage/emulated/"))
                        {
                            val paramName = "upload_doc[" + holder_id + "][" + 0 + "]"
                            parts.add(prepareFilePart(paramName, File(listViews[0].uploaddocument)))
                        }
                    }
                }
                else
                {
                    for (i in listViews.indices)
                    {
                        if(listViews[i].uploaddocument.length >0)
                        {
                            val paramName = "upload_doc[" + holder_id + "][" + i + "]"
                            parts.add(prepareFilePart(paramName, File(listViews[i].uploaddocument)))
                        }
                    }
                }

                val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")


                val call = apiService.insurancePolicySaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(InsurancePoliciesListActivity.handler, "null", 1, 0, 0)

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
