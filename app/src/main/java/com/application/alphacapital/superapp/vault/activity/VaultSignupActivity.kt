package com.application.alphacapital.superapp.vault.activity

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphaestatevault.model.CityResponse
import com.alphaestatevault.model.CountriesResponse
import com.alphaestatevault.model.SignUpResponse
import com.alphaestatevault.model.StateResponse
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivitySignupBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.vault_rowview_list_dialog_selection.view.*
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaultSignupActivity : VaultBaseActivity()
{

    lateinit var binding: VaultActivitySignupBinding

    var listCountries: MutableList<CountriesResponse.Country> = mutableListOf()
    var listState: MutableList<StateResponse.State> = mutableListOf()
    var listCity: MutableList<CityResponse.City> = mutableListOf()
    var listCountries_search: MutableList<CountriesResponse.Country> = mutableListOf()
    var listState_search: MutableList<StateResponse.State> = mutableListOf()
    var listCity_search: MutableList<CityResponse.City> = mutableListOf()

    private var countryId: String = ""
    private var stateId: String = ""
    private var cityId: String = ""

    val COUNTRY: String = "Country"
    val STATE: String = "State"
    val CITY: String = "City"
    val COUNTRY_SEARCH: String = "CountrySearch"
    val STATE_SEARCH: String = "StateSearch"
    val CITY_SEARCH: String = "CitySearch"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_signup)
        appUtils.setLightStatusBar(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {

        if (isOnline())
        {
            getCountries()
        }
        else
        {
            noInternetToast()
        }

        appUtils.removeError(binding.edtName, binding.ipName)
        appUtils.removeError(binding.edtEmail, binding.ipEmail)
        appUtils.removeError(binding.edtMobile, binding.ipMobile)
        appUtils.removeError(binding.edtPassword, binding.ipPassword)
        appUtils.removeError(binding.edtCountry, binding.ipCountry)
        appUtils.removeError(binding.edtState, binding.ipState)
        appUtils.removeError(binding.edtCity, binding.ipCity)
    }

    private fun onClicks()
    {
        binding.txtSignup.setOnClickListener {

            if (binding.edtName.text!!.isEmpty())
            {
                binding.ipName.error = "Please enter your full name."
            }
            else if (binding.edtEmail.text!!.isEmpty())
            {
                binding.ipEmail.error = "Please enter your email address."
            }
            else if (!appUtils.isEmailValid(binding.edtEmail.text.toString()))
            {
                binding.ipEmail.error = "Please enter your valid email address."
            }
            else if (binding.edtMobile.text!!.isEmpty())
            {
                binding.ipMobile.error = "Please enter your mobile number."
            }
            else if (binding.edtMobile.text!!.length != 10)
            {
                binding.ipMobile.error = "Please enter your valid mobile number."
            }
            else if (binding.edtPassword.text!!.isEmpty())
            {
                binding.ipPassword.error = "Please enter your password."
            }
            else if (countryId.isEmpty())
            {
                binding.ipCountry.error = "Please select country."
            }
            else if (stateId.isEmpty())
            {
                binding.ipState.error = "Please select state."
            }
            else if (cityId.isEmpty())
            {
                binding.ipCity.error = "Please select city."
            }
            else
            {
                hideKeyBoard()
                if (isOnline())
                {
                    signup()
                }
                else
                {
                    noInternetToast()
                }
            }
        }

        binding.edtCountry.setOnClickListener { view ->
            hideKeyBoard()
            if (listCountries.size > 0)
            {
                showListDialog(COUNTRY)
            }
            else
            {
                showToast("Countries list not found.")
            }
        }

        binding.edtState.setOnClickListener { view ->
            hideKeyBoard()
            if (countryId.isNotEmpty())
            {
                if (listState.size > 0)
                {
                    showListDialog(STATE)
                }
                else
                {
                    showToast("State list not found.")
                }
            }
            else
            {
                showToast("Please select country first.")
            }
        }

        binding.edtCity.setOnClickListener { view ->
            hideKeyBoard()
            if (countryId.isNotEmpty())
            {
                if (stateId.isNotEmpty())
                {
                    if (listCity.size > 0)
                    {
                        showListDialog(CITY)
                    }
                    else
                    {
                        showToast("City list not found.")
                    }
                }
                else
                {
                    showToast("Please select state first.")
                }
            }
            else
            {
                showToast("Please select country first.")
            }


        }
    }

    private fun getCountries()
    {
        hideKeyBoard()
        binding.loading.llLoading.visibility = View.VISIBLE
        val call = apiService.getCountriesList()

        call.enqueue(object : Callback<CountriesResponse>
        {
            override fun onResponse(call: Call<CountriesResponse>, response: Response<CountriesResponse>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        if (listCountries.size > 0)
                        {
                            listCountries.clear()
                        }

                        listCountries = response.body()!!.countries
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

            override fun onFailure(call: Call<CountriesResponse>, t: Throwable)
            {
                binding.loading.llLoading.visibility = View.GONE
                apiFailedToast()
            }
        })
    }

    private fun getState(countryId: String)
    {
        hideKeyBoard()
        binding.loading.llLoading.visibility = View.VISIBLE
        val call = apiService.getStateList(countryId)

        call.enqueue(object : Callback<StateResponse>
        {
            override fun onResponse(call: Call<StateResponse>, response: Response<StateResponse>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        if (listState.size > 0)
                        {
                            listState.clear()
                        }

                        listState = response.body()!!.states
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

            override fun onFailure(call: Call<StateResponse>, t: Throwable)
            {
                binding.loading.llLoading.visibility = View.GONE
                apiFailedToast()
            }
        })
    }

    private fun getCity(stateId: String)
    {
        hideKeyBoard()
        binding.loading.llLoading.visibility = View.VISIBLE
        val call = apiService.getCityList(stateId)

        call.enqueue(object : Callback<CityResponse>
        {
            override fun onResponse(call: Call<CityResponse>, response: Response<CityResponse>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        if (listCity.size > 0)
                        {
                            listCity.clear()
                        }

                        listCity = response.body()!!.cities
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

            override fun onFailure(call: Call<CityResponse>, t: Throwable)
            {
                binding.loading.llLoading.visibility = View.GONE
                apiFailedToast()
            }
        })
    }

    private fun signup()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.signup(
                        getEditTextString(binding.edtName),
                        getEditTextString(binding.edtEmail),
                        getEditTextString(binding.edtMobile),
                        getEditTextString(binding.edtPassword),
                        countryId, stateId, cityId)

                call.enqueue(object : Callback<SignUpResponse>
                {
                    override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            val signUpResponse = response.body()!!

                            if (signUpResponse.success == 1)
                            {
                                toast(signUpResponse.message)
                                activity.finish()
                                finishActivityAnimation()
                            }
                            else
                            {
                                toast(signUpResponse.message)
                            }
                        }
                        else
                        {
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<SignUpResponse>, t: Throwable)
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

    fun showListDialog(isFor: String)
    {
        val view = layoutInflater.inflate(R.layout.vault_dialog_list, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)
        //appUtils.setStatusBarForDialog(dialog, activity)
        val tvTitle = view.findViewById<TextView>(R.id.tvTitle)
        val edtSearch = view.findViewById<EditText>(R.id.edtSearch)
        val rvDialog = view.findViewById<RecyclerView>(R.id.rvDialog)
        rvDialog.layoutManager = LinearLayoutManager(activity)
        tvTitle.text = "Select " + isFor

        edtSearch.visibility = View.VISIBLE

        edtSearch.textChangedListener {
            onTextChanged { charSequence, i, i2, i3 ->

                if (isFor.equals(COUNTRY))
                {
                    var searchText = charSequence.toString().toLowerCase()
                    listCountries_search.clear()

                    for (i in listCountries.indices)
                    {
                        if (listCountries[i].name.toLowerCase().contains(searchText))
                        {
                            listCountries_search.add(listCountries[i])
                        }
                    }

                    if (listCountries_search.size > 0)
                    {
                        val listAdapter = ListAdapter(COUNTRY_SEARCH, dialog, edtSearch)
                        rvDialog.adapter = listAdapter
                    }

                }
                else if (isFor.equals(STATE))
                {
                    var searchText = charSequence.toString().toLowerCase()
                    listState_search.clear()

                    for (i in listState.indices)
                    {

                        if (listState[i].name.toLowerCase().contains(searchText))
                        {
                            listState_search.add(listState[i])
                        }
                    }

                    if (listState_search.size > 0)
                    {
                        val listAdapter = ListAdapter(STATE_SEARCH, dialog, edtSearch)
                        rvDialog.adapter = listAdapter
                    }

                }
                else if (isFor.equals(CITY))
                {
                    var searchText = charSequence.toString().toLowerCase()
                    listCity_search.clear()

                    for (i in listCity.indices)
                    {

                        if (listCity[i].name.toLowerCase().contains(searchText))
                        {
                            listCity_search.add(listCity[i])
                        }
                    }

                    if (listCity_search.size > 0)
                    {
                        val listAdapter = ListAdapter(CITY_SEARCH, dialog, edtSearch)
                        rvDialog.adapter = listAdapter
                    }
                }

            }
        }

        val listAdapter = ListAdapter(isFor, dialog, edtSearch)
        rvDialog.adapter = listAdapter

        dialog.show()
    }

    inner class ListAdapter(val isFor: String, val dialog: Dialog, val editText: EditText) : RecyclerView.Adapter<ListAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_list_dialog_selection, parent, false))
        }

        override fun getItemCount(): Int
        {
            if (isFor.equals(COUNTRY))
            {
                return listCountries.size
            }
            else if (isFor.equals(STATE))
            {
                return listState.size
            }
            else if (isFor.equals(CITY))
            {
                return listCity.size
            }
            else if (isFor.equals(COUNTRY_SEARCH))
            {
                return listCountries_search.size
            }
            else if (isFor.equals(STATE_SEARCH))
            {
                return listState_search.size
            }
            else
            {
                return listCity_search.size
            }
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            when (isFor)
            {
                COUNTRY ->
                {
                    try
                    {
                        val getSet: CountriesResponse.Country = listCountries[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
                STATE ->
                {
                    try
                    {
                        val getSet: StateResponse.State = listState[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
                CITY ->
                {
                    try
                    {
                        val getSet: CityResponse.City = listCity[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
                COUNTRY_SEARCH ->
                {
                    try
                    {
                        val getSet: CountriesResponse.Country = listCountries_search[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
                STATE_SEARCH ->
                {
                    try
                    {
                        val getSet: StateResponse.State = listState_search[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
                CITY_SEARCH ->
                {
                    try
                    {
                        val getSet: CityResponse.City = listCity_search[position]
                        holder.itemView.tvItems.text = getSet.name
                    }
                    catch (e: Exception)
                    {
                    }
                }
            }


            holder.itemView.setOnClickListener {
                hideKeyBoard(editText)
                dialog.dismiss()
                when (isFor)
                {
                    COUNTRY ->
                    {
                        try
                        {
                            countryId = listCountries[position].id
                            binding.edtCountry.setText(listCountries[position].name)

                            stateId = ""
                            binding.edtState.setText("")
                            cityId = ""
                            binding.edtCity.setText("")
                            getState(countryId)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                    STATE ->
                    {
                        try
                        {
                            stateId = listState[position].id
                            binding.edtState.setText(listState[position].name)
                            cityId = ""
                            binding.edtCity.setText("")
                            getCity(stateId)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                    CITY ->
                    {
                        try
                        {
                            cityId = listCity[position].id
                            binding.edtCity.setText(listCity[position].name)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                    COUNTRY_SEARCH ->
                    {
                        try
                        {
                            countryId = listCountries_search[position].id
                            binding.edtCountry.setText(listCountries_search[position].name)
                            stateId = ""
                            binding.edtState.setText("")
                            cityId = ""
                            binding.edtCity.setText("")
                            getState(countryId)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                    STATE_SEARCH ->
                    {
                        try
                        {
                            stateId = listState_search[position].id
                            binding.edtState.setText(listState_search[position].name)
                            cityId = ""
                            binding.edtCity.setText("")
                            getCity(stateId)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                    CITY_SEARCH ->
                    {
                        try
                        {
                            cityId = listCity_search[position].id
                            binding.edtCity.setText(listCity_search[position].name)
                        }
                        catch (e: Exception)
                        {
                        }
                    }
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    override fun onBackPressed()
    {
        activity.finish()
        finishActivityAnimation()
        super.onBackPressed()
    }
}
