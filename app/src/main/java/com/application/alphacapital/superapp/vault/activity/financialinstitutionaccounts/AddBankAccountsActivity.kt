package com.application.alphacapital.superapp.vault.activity.financialinstitutionaccounts

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
import com.alphaestatevault.model.AddFinancialInstitutionAccountGetSet
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.FinancialInstitutionAccountsResponse
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityAddFinancialInstitutionAccountsBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_financial_institution_account.view.*
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

class AddBankAccountsActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddFinancialInstitutionAccountsBinding
    var listViews: MutableList<AddFinancialInstitutionAccountGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var financialInstitutionAccountsDetails: FinancialInstitutionAccountsResponse.FinancialInstitutionAccount

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setStatusBarGradiant(activity)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_add_financial_institution_accounts)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
            holder_id = intent.getStringExtra("holder_id").toString()
            holder_name = intent.getStringExtra("holder_name").toString()
            holder_userName = intent.getStringExtra("holder_userName").toString()
            if (isForEdit && intent.hasExtra("data"))
            {
                financialInstitutionAccountsDetails = Gson().fromJson(intent.getStringExtra("data"), FinancialInstitutionAccountsResponse.FinancialInstitutionAccount::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Add Bank Account".takeIf { !isForEdit } ?: "Update Bank Account"
        binding.rvList.layoutManager = LinearLayoutManager(activity)
        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddFinancialInstitutionAccountGetSet(financialInstitutionAccountsDetails.bank_name, financialInstitutionAccountsDetails.branch,
                financialInstitutionAccountsDetails.account_number_and_type,
                financialInstitutionAccountsDetails.other_than_own_name, financialInstitutionAccountsDetails.approximate_value,
                financialInstitutionAccountsDetails.upload_doc,
                financialInstitutionAccountsDetails.notes,
                financialInstitutionAccountsDetails.holder,
                financialInstitutionAccountsDetails.financial_institution_account_id,
            financialInstitutionAccountsDetails.nominee_name))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddFinancialInstitutionAccountGetSet("", "", "", "", "", "", "", holder_name, "",""))
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
            listViews.add(AddFinancialInstitutionAccountGetSet("", "", "", "", "", "", "", holder_name, "",""))
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
                            jGroup.put("bank_name", listViews[i].bankName)
                            jGroup.put("branch", listViews[i].branch)
                            jGroup.put("account_number_and_type", listViews[i].accountNumberAndType)
                            jGroup.put("other_than_own_name", listViews[i].mode_of_holding)
                            jGroup.put("approximate_value", listViews[i].approximateValue)
                            jGroup.put("notes", listViews[i].notes)
                            jGroup.put("nominee_name", listViews[i].nominee_name)
                            jGroup.put("upload_doc", listViews[i].upload_doc)
                            jGroup.put("financial_institution_account_id", listViews[i].financialInstitutionAccountId)
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
                                jGroup.put("bank_name", listViews[i].bankName)
                                jGroup.put("branch", listViews[i].branch)
                                jGroup.put("account_number_and_type", listViews[i].accountNumberAndType)
                                jGroup.put("other_than_own_name", listViews[i].mode_of_holding)
                                jGroup.put("approximate_value", listViews[i].approximateValue)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
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
                                jGroup.put("bank_name", listViews[i].bankName)
                                jGroup.put("branch", listViews[i].branch)
                                jGroup.put("account_number_and_type", listViews[i].accountNumberAndType)
                                jGroup.put("other_than_own_name", listViews[i].mode_of_holding)
                                jGroup.put("approximate_value", listViews[i].approximateValue)
                                jGroup.put("notes", listViews[i].notes)
                                jGroup.put("nominee_name", listViews[i].nominee_name)
                                jGroup.put("upload_doc", listViews[i].upload_doc)
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

        if(listViews[position].bankName.isEmpty())
        {
            getViewHolder(position).itemView.ipBankName.error = "Please enter bank name."
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_financial_institution_account, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddFinancialInstitutionAccountGetSet = listViews[position]

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
            holder.itemView.edtBankName.setText(getSet.bankName)
            holder.itemView.edtBranch.setText(getSet.branch)
            holder.itemView.edtAccountNumberAndType.setText(getSet.accountNumberAndType)
            holder.itemView.edtModeofHolding.setText(getSet.mode_of_holding)
            holder.itemView.edtApproximateValue.setText(getSet.approximateValue)
            holder.itemView.edtNotes.setText(getSet.notes)
            holder.itemView.edtNominee_name.setText(getSet.nominee_name)

            if (getSet.upload_doc.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.upload_doc))
            }

            holder.itemView.edtBankName.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBankName.isErrorEnabled = false
                    listViews[position].bankName = charSequence.toString().trim()
                }
            }


            holder.itemView.edtBranch.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipBranch.isErrorEnabled = false
                    listViews[position].branch = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAccountNumberAndType.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAccountNumberAndType.isErrorEnabled = false
                    listViews[position].accountNumberAndType = charSequence.toString().trim()
                }
            }

            holder.itemView.edtModeofHolding.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipModeofHolding.isErrorEnabled = false
                    listViews[position].mode_of_holding = charSequence.toString().trim()
                }
            }

            holder.itemView.edtApproximateValue.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipApproximateValue.isErrorEnabled = false
                    listViews[position].approximateValue = charSequence.toString().trim()
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

            holder.itemView.edtNominee_name.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipNominee_name.isErrorEnabled = false
                    listViews[position].nominee_name = charSequence.toString().trim()
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

                val call = apiService.financialInstitutionAccountsSaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(BankAccountsListActivity.handler, "null", 1, 0, 0)

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
