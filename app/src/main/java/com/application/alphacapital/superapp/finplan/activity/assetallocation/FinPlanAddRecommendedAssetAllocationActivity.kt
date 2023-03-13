package com.application.alphacapital.superapp.finplan.activity.assetallocation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphafinancialplanning.model.AddRecommendedAllocation
import com.alphafinancialplanning.model.AssetsClassesListResponse
import com.alphafinancialplanning.model.CommonResponse
import com.application.alphacapital.superapp.finplan.model.RecommendedAssetAllocationListResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.finplan.activity.FinPlanBaseActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.fin_plan_activity_add_recommended_asset_allocation.*
import kotlinx.android.synthetic.main.fin_plan_item_add_recommended_allocation.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanAddRecommendedAssetAllocationActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private val listTypes: MutableList<AddRecommendedAllocation> = mutableListOf()

    private var allocationAdapter: AllocationAdapter? = null

    var isForEdit = false

    var dataList: List<RecommendedAssetAllocationListResponse.RecommendedAssetAllocation.AssetList> = listOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_add_recommended_asset_allocation)

        if (intent.hasExtra("isForEdit"))
        {
            isForEdit = intent.getBooleanExtra("isForEdit", false)
        }

        initViews()

        if (isForEdit)
        {
            for (getSet in FinPlanRecommendedAssetAllocationListActivity.listAspiration)
            {
                listTypes.add(AddRecommendedAllocation(getSet.recommended_asset_id, getSet.asset_class, getSet.allocation.replace("%", ""), getSet.return_expectation.replace("%", "")))
            }

            allocationAdapter = AllocationAdapter(listTypes)
            rvRecommendedAllocation.adapter = allocationAdapter

        }
        else
        {
            getAssetClasses()
        }
    }

    private fun initViews()
    {

        tvTitle.text = "Add Recommended Asset Allocation".takeIf { !isForEdit } ?: "Update Recommended Asset Allocation"

        ivBack.backNav()

        btnSubmit.setOnClickListener(this)

        rvRecommendedAllocation.layoutManager = LinearLayoutManager(activity)

    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            btnSubmit.id ->
            {
                allocationAdapter.let {
                    if (it?.isAllDataFiled()!!)
                    {

                        val rootObject = JSONObject()
                        val dataArray = JSONArray()

                        for (i in listTypes.indices)
                        {
                            val jGroup = JSONObject()
                            jGroup.put("asset_class", listTypes[i].asset_class)
                            jGroup.put("allocation", listTypes[i].allocation)
                            jGroup.put("return_expectation", listTypes[i].return_expectation)
                            if (isForEdit)
                            {
                                jGroup.put("recommended_asset_id", listTypes[i].recommended_asset_id)
                            }
                            dataArray.put(jGroup)
                        }

                        rootObject.put("items", dataArray)

                        saveAllocations(rootObject.toString())

                    }
                    else
                    {
                        showToast("Please fill all allocation and return expectation")
                    }
                }
            }
        }
    }

    private fun getAssetClasses()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getAssetClassesList().enqueue(object : Callback<AssetsClassesListResponse>
            {
                override fun onFailure(call: Call<AssetsClassesListResponse>, t: Throwable)
                {
                    loader.dismiss()
                    apiFailedToast()
                }

                override fun onResponse(call: Call<AssetsClassesListResponse>, response: Response<AssetsClassesListResponse>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.isSuccessful)
                        {

                            for (getSet in response.body()!!.assets_classes)
                            {
                                listTypes.add(AddRecommendedAllocation("", getSet, "", ""))
                            }

                            allocationAdapter = AllocationAdapter(listTypes)
                            rvRecommendedAllocation.adapter = allocationAdapter

                            loader.dismiss()


                        }
                        else
                        {
                            loader.dismiss()
                            apiFailedToast()
                        }
                    }
                    else
                    {
                        loader.dismiss()
                        apiFailedToast()
                    }
                }

            })
        }
        else
        {
            noInternetToast()
        }
    }

    private fun saveAllocations(items: String)
    {
        if (isOnline())
        {

            loader.show()
            apiService.saveRecommendedAssetAllocation(sessionManager.userId, items).enqueue(object : Callback<CommonResponse>
            {
                override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                {
                    loader.dismiss()
                    apiFailedToast()
                }

                override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                {
                    if (response.isSuccessful)
                    {
                        loader.dismiss()
                        showToast(response.body()!!.message)
                        if (response.body()!!.success == 1)
                        {
                            appUtils.sendMessageFromHandler(FinPlanRecommendedAssetAllocationListActivity.handler, "", 1, 0, 0)
                            finish()
                        }
                    }
                    else
                    {
                        loader.dismiss()
                        apiFailedToast()
                    }
                }

            })


        }
        else
        {
            noInternetToast()
        }
    }

    inner class AllocationAdapter(private val list: MutableList<AddRecommendedAllocation>) : RecyclerView.Adapter<AllocationAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_add_recommended_allocation, parent, false))
        }

        override fun getItemCount(): Int
        {
            return list.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: AddRecommendedAllocation = list[position]
            holder.itemView.tvAssetsType.text = getSet.asset_class
            holder.itemView.edtReturnExpectation.setText(getSet.return_expectation)
            holder.itemView.edtAllocation.setText(getSet.allocation)

            holder.itemView.edtAllocation.addTextChangedListener(object : TextWatcher
            {
                override fun afterTextChanged(p0: Editable?)
                {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {

                }

                override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                    if (value?.length!! > 0)
                    {
                        getSet.allocation = value.toString()
                    }
                }

            })

            holder.itemView.edtReturnExpectation.addTextChangedListener(object : TextWatcher
            {
                override fun afterTextChanged(p0: Editable?)
                {

                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
                {

                }

                override fun onTextChanged(value: CharSequence?, p1: Int, p2: Int, p3: Int)
                {
                    if (value?.length!! > 0)
                    {
                        getSet.return_expectation = value.toString()
                    }
                }

            })


        }

        public fun isAllDataFiled(): Boolean
        {

            var isFilled = true

            for (getSet in list)
            {
                if (getSet.allocation.isEmpty() || getSet.return_expectation.isEmpty())
                {
                    isFilled = false
                }
            }

            return isFilled
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var tvAssetsType: TextView = itemView.findViewById(R.id.tvAssetsType)
            var edtAllocation: EditText = itemView.findViewById(R.id.edtAllocation)
            var edtReturnExpectation: EditText = itemView.findViewById(R.id.edtReturnExpectation)
            var inputAllocation: TextInputLayout = itemView.findViewById(R.id.inputAllocation)
            var inputReturnExpectation: TextInputLayout = itemView.findViewById(R.id.inputReturnExpectation)
        }

    }
}
