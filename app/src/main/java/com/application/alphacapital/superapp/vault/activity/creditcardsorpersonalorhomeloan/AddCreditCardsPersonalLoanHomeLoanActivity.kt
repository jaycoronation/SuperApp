package com.application.alphacapital.superapp.vault.activity.creditcardsorpersonalorhomeloan

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
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
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.*
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.edtCreatedon
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.edtFile
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.edtNotes
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.ipCreatedon
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.ipFile
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.ipNotes
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.llDelete
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.txtHolderName
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.txtMandatory
import kotlinx.android.synthetic.main.vault_rowview_add_other_debts.view.*
import kotlinx.android.synthetic.main.vault_rowview_list_dialog_selection.view.*
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

class AddCreditCardsPersonalLoanHomeLoanActivity : VaultBaseActivity()
{
    private lateinit var binding: VaultActivityAddCommonBinding
    var listViews: MutableList<AddCreditCardsPersonalLoanHomeLoanGetSet> = mutableListOf()
    private val viewsAdapter = ViewsAdapter()
    var isForEdit = false
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    lateinit var creditCardsAndLoanDetails: CreditCardsAndLoansResponse.CreditCardsAndLoan
    var listSelection: MutableList<String> = mutableListOf()

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
                creditCardsAndLoanDetails = Gson().fromJson(intent.getStringExtra("data"), CreditCardsAndLoansResponse.CreditCardsAndLoan::class.java)
            }
        }

        initView()
        onClicks()
    }

    private fun initView()
    {
        if (intent.hasExtra("from"))
        {
            binding.tvTitleNew.text = "Enter All Details"
            binding.toolbar.txtTitle.text = "Add Credit Cards and Loans".takeIf { !isForEdit } ?: "Update Credit Cards and Loans"
        }
        else
        {
            binding.toolbar.txtTitle.text = "Add Credit Cards and Loans".takeIf { !isForEdit } ?: "Update Credit Cards and Loans"
        }
        binding.rvList.layoutManager = LinearLayoutManager(activity)

        listSelection.add("Car Loan")
        listSelection.add("Credit Card")
        listSelection.add("Home Loan")
        listSelection.add("Lone money invested Equity")
        listSelection.add("Other Loan")
        listSelection.add("Lucknow Plot Loan")
        listSelection.add("Insurance Premium")
        listSelection.add("Others")

        setUpViews()
    }

    private fun setUpViews()
    {
        if (isForEdit)
        {
            gone(binding.txtAddMore)
            listViews.add(AddCreditCardsPersonalLoanHomeLoanGetSet(
                creditCardsAndLoanDetails.type,
                creditCardsAndLoanDetails.account_number,
                creditCardsAndLoanDetails.institution,
                creditCardsAndLoanDetails.amount,
                creditCardsAndLoanDetails.created_on,
                creditCardsAndLoanDetails.contact_person,
                creditCardsAndLoanDetails.location_of_document,
                creditCardsAndLoanDetails.upload_doc,
                creditCardsAndLoanDetails.notes,
                creditCardsAndLoanDetails.holder,
                creditCardsAndLoanDetails.credit_cards_and_loan_id))
        }
        else
        {
            visible(binding.txtAddMore)
            listViews.add(AddCreditCardsPersonalLoanHomeLoanGetSet("", "", "", "", "", "", "", "", "", holder_name, ""))
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
            listViews.add(AddCreditCardsPersonalLoanHomeLoanGetSet("", "", "", "", "", "", "", "", "", holder_name, ""))
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
                                if (listViews[i].type.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].created_on.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
                                {
                                    val jGroup = JSONObject()
                                    jGroup.put("holder", listViews[i].holder)
                                    jGroup.put("type", listViews[i].type)
                                    jGroup.put("account_number", listViews[i].account_number)
                                    jGroup.put("institution", listViews[i].institution)
                                    jGroup.put("amount", listViews[i].amount)
                                    jGroup.put("type", listViews[i].type)
                                    jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                    jGroup.put("contact_person", listViews[i].contact_person)
                                    jGroup.put("location_of_document", listViews[i].location_of_document)
                                    jGroup.put("notes", listViews[i].notes)
                                    jGroup.put("upload_doc", listViews[i].upload_doc)
                                    jGroup.put("credit_cards_and_loan_id", listViews[i].credit_cards_and_loan_id)
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
                                    if (listViews[i].type.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].created_on.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
                                    {
                                        val jGroup = JSONObject()
                                        jGroup.put("holder", listViews[i].holder)
                                        jGroup.put("type", listViews[i].type)
                                        jGroup.put("account_number", listViews[i].account_number)
                                        jGroup.put("institution", listViews[i].institution)
                                        jGroup.put("amount", listViews[i].amount)
                                        jGroup.put("type", listViews[i].type)
                                        jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
                                        jGroup.put("contact_person", listViews[i].contact_person)
                                        jGroup.put("location_of_document", listViews[i].location_of_document)
                                        jGroup.put("notes", listViews[i].notes)
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
                            if (listViews[i].type.isEmpty() && listViews[i].account_number.isEmpty() && listViews[i].institution.isEmpty() && listViews[i].amount.isEmpty() && listViews[i].created_on.isEmpty() && listViews[i].contact_person.isEmpty() && listViews[i].location_of_document.isEmpty() && listViews[i].notes.isEmpty() && listViews[i].upload_doc.isEmpty())
                            {
                                continue
                            }
                            else if (listViews[i].type.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].created_on.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
                            {
                                val jGroup = JSONObject()
                                jGroup.put("holder", listViews[i].holder)
                                jGroup.put("type", listViews[i].type)
                                jGroup.put("account_number", listViews[i].account_number)
                                jGroup.put("institution", listViews[i].institution)
                                jGroup.put("amount", listViews[i].amount)
                                jGroup.put("type", listViews[i].type)
                                jGroup.put("created_on", appUtils.universalDateConvert(listViews[i].created_on, "dd/MM/yyyy", "yyyy-MM-dd"))
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
            listViews[0].type.isEmpty() ->
            {
                getViewHolder(0).itemView.ipType.error = "Please select type."
                isValid = false
            }
            listViews[0].account_number.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAccountNumber.error = "Please enter account number."
                isValid = false
            }
            listViews[0].institution.isEmpty() ->
            {
                getViewHolder(0).itemView.ipInstitution.error = "Please enter institution."
                isValid = false
            }
            listViews[0].amount.isEmpty() ->
            {
                getViewHolder(0).itemView.ipAmount.error = "Please enter amount."
                isValid = false
            }
            listViews[0].created_on.isEmpty() ->
            {
                getViewHolder(0).itemView.ipCreatedon.error = "Please select created on."
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
            if (listViews[i].type.isEmpty() && listViews[i].account_number.isEmpty() && listViews[i].institution.isEmpty() && listViews[i].amount.isEmpty() && listViews[i].created_on.isEmpty() && listViews[i].contact_person.isEmpty() && listViews[i].location_of_document.isEmpty() && listViews[i].notes.isEmpty() && listViews[i].upload_doc.isEmpty() || listViews[i].type.isNotEmpty() && listViews[i].account_number.isNotEmpty() && listViews[i].institution.isNotEmpty() && listViews[i].amount.isNotEmpty() && listViews[i].created_on.isNotEmpty() && listViews[i].contact_person.isNotEmpty() && listViews[i].location_of_document.isNotEmpty() && listViews[i].notes.isNotEmpty() && listViews[i].upload_doc.isNotEmpty())
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
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_add_credit_cards_and_loan, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listViews.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: AddCreditCardsPersonalLoanHomeLoanGetSet = listViews[position]

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
            holder.itemView.edtType.setText(getSet.type)
            holder.itemView.edtAccountNumber.setText(getSet.account_number)
            holder.itemView.edtInstitution.setText(getSet.institution)
            holder.itemView.edtAmount.setText(getSet.amount)
            holder.itemView.edtCreatedon.setText(getSet.created_on)
            holder.itemView.edtContactPerson.setText(getSet.contact_person)
            holder.itemView.edtLocationOfDocument.setText(getSet.location_of_document)
            holder.itemView.edtNotes.setText(getSet.notes)
            if (getSet.upload_doc.isNotEmpty() && isForEdit)
            {
                holder.itemView.edtFile.setText(IOUtil.getFileName(getSet.upload_doc))
            }

            holder.itemView.edtType.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipType.isErrorEnabled = false
                    listViews[position].type = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAccountNumber.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAccountNumber.isErrorEnabled = false
                    listViews[position].account_number = charSequence.toString().trim()
                }
            }

            holder.itemView.edtInstitution.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipInstitution.isErrorEnabled = false
                    listViews[position].institution = charSequence.toString().trim()
                }
            }

            holder.itemView.edtAmount.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipAmount.isErrorEnabled = false
                    listViews[position].amount = charSequence.toString().trim()
                }
            }

            holder.itemView.edtCreatedon.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    holder.itemView.ipCreatedon.isErrorEnabled = false
                    listViews[position].created_on = charSequence.toString().trim()
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

            holder.itemView.edtFile.textChangedListener {
                onTextChanged { charSequence, i, i2, i3 ->
                    if (charSequence.toString().trim().length > 0)
                    {
                        holder.itemView.ipFile.isErrorEnabled = false
                    }
                }
            }

            holder.itemView.edtCreatedon.setOnClickListener {
                appUtils.showDatePicker(activity,holder.itemView.edtCreatedon)
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
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE).check()

            }

            holder.itemView.edtType.setOnClickListener {
                showListDialog(position)
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    }

    fun showListDialog(position: Int)
    {
        val view = layoutInflater.inflate(R.layout.vault_dialog_list, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)
        //appUtils.setStatusBarForDialog(dialog, activity)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val edtSearch = view.findViewById<EditText>(R.id.edtSearch)
        val rvDialog = view.findViewById<RecyclerView>(R.id.rvDialog)
        rvDialog.layoutManager = LinearLayoutManager(activity)
        tvTitle.text = "Select Option"
        edtSearch.visibility = View.GONE
        val listAdapter = ListAdapter(dialog,position)
        rvDialog.adapter = listAdapter
        dialog.show()
    }

    inner class ListAdapter(val dialog: Dialog, val positionNew: Int) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_list_dialog_selection, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listSelection.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            try
            {
                holder.itemView.tvItems.text = listSelection[position].toString()
            }
            catch (e: Exception)
            {
            }


            holder.itemView.setOnClickListener {
                dialog.dismiss()
                listViews[positionNew].type = listSelection[position].toString()
                getViewHolder(positionNew).itemView.edtType.setText(listSelection[position].toString())
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


                val call = apiService.creditCardsAndLoansSaveCall(itemPart, userIdPart, fromAppPart, parts)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            toast(response.body()!!.message)
                            if (response.body()!!.success == 1)
                            {
                                appUtils.sendMessageFromHandler(
                                    CreditCardsPersonalLoanHomeLoanListActivity.handler, "null", 1, 0, 0)

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
