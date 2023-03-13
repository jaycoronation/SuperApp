package com.application.alphacapital.superapp.vault.activity.domesticemployees

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.*
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityDomesticEmployeesBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.vault_rowview_list_dialog_selection.view.*
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DomesticEmployeesActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityDomesticEmployeesBinding
    lateinit var domesticEmployeesDetailResponse: DomesticEmployeesDetailResponse.DomesticEmployees
    var domestic_employees_id : String =""
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    var listSelection: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_domestic_employees)
        setStatusBarGradiant(activity)
        holder_id = intent.getStringExtra("holder_id").toString()
        holder_name = intent.getStringExtra("holder_name").toString()
        holder_userName = intent.getStringExtra("holder_userName").toString()
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Domestic Employees"

        binding.txtHolderName.text = holder_userName
        listSelection.add("Yes")
        listSelection.add("No")

        if (holder_name == "A")
        {
            binding.txtMandatory.visibility = View.VISIBLE
        }
        else
        {
            binding.txtMandatory.visibility = View.GONE
        }

        try
        {
            appUtils.removeError(binding.edtIsDomesticEmployee, binding.ipIsDomesticEmployee)
            appUtils.removeError(binding.edtEmployeeInstruction, binding.ipEmployeeInstruction)
        }
        catch (e: Exception)
        {
        }

        if (isOnline())
        {
           getDataCall()
        }
        else
        {
            noInternetToast()
        }
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.edtIsDomesticEmployee.setOnClickListener {
            showListDialog()
        }

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (isOnline())
            {
                addData()
            }
            else
            {
                noInternetToast()
            }
        }
    }

    private fun getDataCall()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getDomesticEmployeesDetail(sessionManager.userId)

                call.enqueue(object : Callback<JsonObject>
                {
                    override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>)
                    {
                        if (response.isSuccessful)
                        {

                            Log.e("<><> RES : ", response.body().toString() + "")
                            val jsonObject: JSONObject? = JSONObject(response.body().toString())
                            var message: String = ""
                            if (jsonObject!!.has("success"))
                            {
                                if (jsonObject.has("message"))
                                {
                                    message = jsonObject.getString("message")
                                }

                                if (jsonObject!!.getInt("success") == 1)
                                {
                                    if (jsonObject.has("domestic_employees"))
                                    {
                                        val jsonObjectdomestic_employees = jsonObject.getJSONObject("domestic_employees")

                                        if (jsonObjectdomestic_employees.has(holder_id))
                                        {
                                            val jsonObjectData = jsonObjectdomestic_employees.getJSONObject(holder_id)

                                            domesticEmployeesDetailResponse = DomesticEmployeesDetailResponse.DomesticEmployees()
                                            domesticEmployeesDetailResponse.domestic_employees_id = jsonObjectData.getString("domestic_employees_id")
                                            domesticEmployeesDetailResponse.is_domestic_employee = jsonObjectData.getString("is_domestic_employee")
                                            domesticEmployeesDetailResponse.employee_instruction = jsonObjectData.getString("employee_instruction")

                                            domestic_employees_id = domesticEmployeesDetailResponse.domestic_employees_id
                                            binding.edtIsDomesticEmployee.setText(domesticEmployeesDetailResponse.is_domestic_employee)
                                            binding.edtEmployeeInstruction.setText(domesticEmployeesDetailResponse.employee_instruction)
                                        }
                                    }
                                }
                                else
                                {
                                    if(message.length >0)
                                    {
                                        //showToast(message)
                                    }
                                    else
                                    {
                                        apiFailedToast()
                                    }
                                }
                            }
                            else
                            {
                                apiFailedToast()
                            }
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable)
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

    private fun addData()
    {
        try
        {
            if (holder_name == "A")
            {
                if (binding.edtIsDomesticEmployee.text!!.isEmpty())
                {
                    binding.ipIsDomesticEmployee.error = "Please select do you have any domestic employees?."
                }
                else if (binding.edtEmployeeInstruction.text!!.isEmpty())
                {
                    binding.ipEmployeeInstruction.error = "Please enter Would you like to include any instructions or directions such as suggestions about continued employment,\n" + "severance arrangements, etc., regarding them or another employee?"
                }
                else
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (binding.edtIsDomesticEmployee.text!!.isNotEmpty() && binding.edtEmployeeInstruction.text!!.isNotEmpty())
                        {
                            if (domestic_employees_id.length > 0)
                            {
                                jGroup.put("domestic_employees_id", domestic_employees_id)
                            }
                            jGroup.put("is_domestic_employee", binding.edtIsDomesticEmployee.text.toString().trim())
                            jGroup.put("employee_instruction", binding.edtEmployeeInstruction.text.toString().trim())
                        }

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addData(rootObject.toString())
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
            }
            else
            {
                if (binding.edtIsDomesticEmployee.text!!.isEmpty() && binding.edtEmployeeInstruction.text!!.isEmpty())
                {

                }
                else if (binding.edtIsDomesticEmployee.text!!.isNotEmpty() && binding.edtEmployeeInstruction.text!!.isNotEmpty())
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val jGroup = JSONObject()
                        if (domestic_employees_id.length > 0)
                        {
                            jGroup.put("domestic_employees_id", domestic_employees_id)
                        }
                        jGroup.put("is_domestic_employee", binding.edtIsDomesticEmployee.text.toString().trim())
                        jGroup.put("employee_instruction", binding.edtEmployeeInstruction.text.toString().trim())

                        rootObject.put(holder_id, jGroup)
                        Log.e("<><> JSON : ", rootObject.toString())
                        addData(rootObject.toString())
                    }
                    else
                    {
                        noInternetToast()
                    }
                }
                else
                {
                    showToast("Please Enter or Clear all fields value.")
                }
            }
        }
        catch (e: Exception)
        {
        }
    }

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE

                val call = apiService.domesticEmployeesSaveCall(items, sessionManager.userId)

                call.enqueue(object : Callback<CommonResponse>
                {
                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                toast(response.body()!!.message)
                                finish()
                                finishActivityAnimation()
                            }
                            else
                            {
                                toast(response.body()!!.message)
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

    fun showListDialog()
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
        val listAdapter = ListAdapter(dialog)
        rvDialog.adapter = listAdapter
        dialog.show()
    }

    inner class ListAdapter(val dialog: Dialog) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
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
                binding.edtIsDomesticEmployee.setText(listSelection[position].toString())
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    override fun onBackPressed()
    {
        hideKeyBoard()
        finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
