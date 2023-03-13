package com.application.alphacapital.superapp.vault.activity

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphaestatevault.model.*
import com.application.alphacapital.superapp.vault.network.AppConstant
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityUpdateProfileBinding
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.vault_rowview_list_dialog_selection.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.textChangedListener
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class VaultUpdateProfileActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityUpdateProfileBinding
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

    private val PICK_FROM_GALLERY_CROP = 212
    private val GALLERY = 1
    private val CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_update_profile)
        setStatusBarGradiant(activity)
        initView()
        onClicks()
    }

    private fun initView()
    {
        try
        {
            binding.toolbar.txtTitle.setText("Update Profile")

            binding.edtName.setText(sessionManager.username)
            binding.edtEmail.setText(sessionManager.email)
            binding.edtMobile.setText(sessionManager.phone)
            binding.edtCountry.setText(sessionManager.country_name)
            binding.edtState.setText(sessionManager.state_name)
            binding.edtCity.setText(sessionManager.city_name)

            countryId = sessionManager.countryId
            stateId = sessionManager.stateId
            cityId = sessionManager.cityId

            if (sessionManager.profilePic.isNotEmpty())
            {
                Glide.with(activity)
                        .load(sessionManager.profilePic)
                        .placeholder(R.drawable.vault_img_placeholder)
                        .into(binding.ivProfilePic)
            }
            else
            {
                Glide.with(activity)
                        .load(R.drawable.vault_img_placeholder)
                        .into(binding.ivProfilePic)
            }
        }
        catch (e: Exception)
        {
        }

        try
        {
            if (isOnline())
            {
                getCountries()
                getState(countryId);
                getCity(stateId);
            }
            else
            {
                noInternetToast()
            }
        }
        catch (e: Exception)
        {
        }

        try
        {
            appUtils.removeError(binding.edtName, binding.ipName)
            appUtils.removeError(binding.edtEmail, binding.ipEmail)
            appUtils.removeError(binding.edtMobile, binding.ipMobile)
            appUtils.removeError(binding.edtCountry, binding.ipCountry)
            appUtils.removeError(binding.edtState, binding.ipState)
            appUtils.removeError(binding.edtCity, binding.ipCity)
        }
        catch (e: Exception)
        {
        }
    }

    private fun onClicks()
    {
        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.noInternet.tvRetry.setOnClickListener {
            if (isOnline())
            {
                getCountries()
                getState(countryId);
                getCity(stateId);
            }
            else
            {
                noInternetToast()
            }
        }

        binding.llEditProfilePic.setOnClickListener {
            checkPermission()
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

        binding.txtUpdateProfile.setOnClickListener {

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
                    updateProfile()
                }
                else
                {
                    noInternetToast()
                }
            }
        }
    }

    private fun getUserDetailsCall()
    {
        hideKeyBoard()
        binding.loading.llLoading.visibility = View.VISIBLE
        val call = apiService.getUserDetails(sessionManager.userId)

        call.enqueue(object : Callback<UserProfileDetailsResponse>
        {
            override fun onResponse(call: Call<UserProfileDetailsResponse>, response: Response<UserProfileDetailsResponse>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        sessionManager.username = response.body()!!.profile.name
                        sessionManager.email = response.body()!!.profile.email
                        sessionManager.phone = response.body()!!.profile.mobile
                        sessionManager.country_name = response.body()!!.profile.country_name
                        sessionManager.countryId = response.body()!!.profile.country
                        sessionManager.state_name = response.body()!!.profile.state_name
                        sessionManager.stateId = response.body()!!.profile.state
                        sessionManager.city_name = response.body()!!.profile.city_name
                        sessionManager.cityId = response.body()!!.profile.city

                        binding.edtName.setText(sessionManager.username)
                        binding.edtEmail.setText(sessionManager.email)
                        binding.edtMobile.setText(sessionManager.phone)
                        binding.edtCountry.setText(sessionManager.country_name)
                        binding.edtState.setText(sessionManager.state_name)
                        binding.edtCity.setText(sessionManager.city_name)
                        countryId = sessionManager.countryId
                        stateId = sessionManager.stateId
                        cityId = sessionManager.cityId



                        appUtils.sendMessageFromHandler(VaultHomeActivity.handler, Any(), 100,0,0)
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

            override fun onFailure(call: Call<UserProfileDetailsResponse>, t: Throwable)
            {
                binding.loading.llLoading.visibility = View.GONE
                apiFailedToast()
                Log.e("<><><> FAILURE", t.message.toString())
            }
        })
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

    private fun updateProfile()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.updateProfileCall(
                        sessionManager.userId,
                        getEditTextString(binding.edtName),
                        getEditTextString(binding.edtEmail),
                        getEditTextString(binding.edtMobile),
                        countryId,
                        stateId,
                        cityId,
                        AppConstant.FROM_APP)

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
                            }
                            else
                            {
                                toast(signUpResponse.message)
                            }

                            getUserDetailsCall()
                        }
                        else
                        {
                            apiFailedToast()
                            binding.loading.llLoading.visibility = View.GONE

                        }
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

    private fun checkPermission()
    {
        try
        {
            val permissionlistener = object : PermissionListener
            {
                override fun onPermissionGranted()
                {
                    try
                    {
                        showSelection()
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }

                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>)
                {
                    toast("Permission Denied")
                }
            }
            MitsPermissions(activity).setPermissionListener(permissionlistener).setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA).check()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    fun showSelection()
    {
        val view = layoutInflater.inflate(R.layout.vault_dialog_file_selection, null)
        val dialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)
        appUtils.showFullDialog(dialog)

        val llCamera = view.findViewById<LinearLayout>(R.id.llCamera)
        val llGallery = view.findViewById<LinearLayout>(R.id.llGallery)

        llCamera.setOnClickListener { view ->
            dialog.dismiss()
            openCamera()
        }

        llGallery.setOnClickListener { view ->
            dialog.dismiss()
            choosePhotoFromGallary()
        }

        dialog.show()
    }

    fun openCamera()
    {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    fun choosePhotoFromGallary()
    {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    val path = saveImage(bitmap)
                    val i = Intent(activity, VaultCropActivity::class.java)
                    i.putExtra("imgPath", path)
                    i.putExtra("outputSize", 1000)
                    startActivityForResult(i, PICK_FROM_GALLERY_CROP)
                    Log.v("tempPath to store", path + "*")
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                    Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if (requestCode == CAMERA)
        {
            try
            {
                val thumbnail = data!!.extras!!.get("data") as Bitmap
                binding.ivProfilePic.setImageBitmap(thumbnail)
                val path = saveImage(thumbnail)

                val i = Intent(activity, VaultCropActivity::class.java)
                i.putExtra("imgPath", path)
                i.putExtra("outputSize", 1000)
                startActivityForResult(i, PICK_FROM_GALLERY_CROP)
                Log.v("tempPath to store", path + "*")
            }
            catch (e: Exception)
            {
            }

        }
        else if (requestCode == PICK_FROM_GALLERY_CROP)
        {
            try
            {
                val path = data!!.getStringExtra("single_path")
                Log.e("path >>>>> %%  > ", "onActivityResult: $path")
                val fl1 = File(path)
                val tempPath = fl1.absolutePath
                Log.e("temp path >>>>> %%  > ", "onActivityResult: $tempPath")
                try
                {
                    Glide.with(activity).load(tempPath).into(binding.ivProfilePic)
                    updateProfileImage(tempPath)
                }
                catch (e: java.lang.Exception)
                {
                    e.printStackTrace()
                }
            }
            catch (e: java.lang.Exception)
            {
                e.printStackTrace()
            }
        }
    }

    fun saveImage(myBitmap: Bitmap): String
    {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File((Environment.getExternalStorageDirectory()).toString() + "/Aqualogy")
        // have the object build the directory structure, if needed.
        Log.d("fee", wallpaperDirectory.toString())
        if (!wallpaperDirectory.exists())
        {

            wallpaperDirectory.mkdirs()
        }

        try
        {
            Log.d("heel", wallpaperDirectory.toString())
            val f = File(wallpaperDirectory, ((Calendar.getInstance().getTimeInMillis()).toString() + ".jpg"))
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/jpeg"), null)
            fo.close()
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath())
            return f.getAbsolutePath()
        }
        catch (e1: Exception)
        {
            e1.printStackTrace()
        }

        return ""
    }

    private fun updateProfileImage(path: String)
    {
        if (isOnline())
        {
            gone(binding.noInternet.llNoInternet)
            visible(binding.loading.llLoading)

            val file = File(path)
            val fileReqBody: RequestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val part: MultipartBody.Part = MultipartBody.Part.createFormData("profile_pic", file.name, fileReqBody)

            val userID = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
            val isForApp = MultipartBody.Part.createFormData("from_app", "true")

            val call = apiService.updateProfileImage(userID, isForApp, part)
            call.enqueue(object : Callback<UpdateProfileResponse>
            {
                override fun onResponse(call: Call<UpdateProfileResponse>, response: Response<UpdateProfileResponse>)
                {
                    if (response.isSuccessful)
                    {
                        if (response.body()!!.success == 1)
                        {

                            Log.e("<><> RES :: ", response.body()!!.message + " <><>")

                            sessionManager.profilePic = response.body()!!.profile_pic

                            if (sessionManager.profilePic.isNotEmpty())
                            {
                                Glide.with(activity)
                                        .load(response.body()!!.profile_pic)
                                        .placeholder(R.drawable.vault_img_placeholder)
                                        .into(binding.ivProfilePic)
                            }
                            else
                            {
                                Glide.with(activity)
                                        .load(R.drawable.vault_img_placeholder)
                                        .into(binding.ivProfilePic)
                            }

                            appUtils.sendMessageFromHandler(VaultHomeActivity.handler, Any(), 100,0,0)
                        }
                    }

                    gone(binding.loading.llLoading)
                }

                override fun onFailure(call: Call<UpdateProfileResponse>, t: Throwable)
                {
                    apiFailedToast()
                }
            })
        }
        else
        {
            noInternetToast()
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
