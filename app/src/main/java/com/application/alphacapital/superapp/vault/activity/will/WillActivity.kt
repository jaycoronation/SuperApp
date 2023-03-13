package com.application.alphacapital.superapp.vault.activity.will

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.application.alphacapital.superapp.vault.activity.VaultBaseActivity
import com.alphaestatevault.model.CommonResponse
import com.alphaestatevault.model.WillDetailResponse
import com.android.mit.mitspermissions.MitsPermissions
import com.android.mit.mitspermissions.PermissionListener
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.VaultActivityWillBinding
import com.application.alphacapital.superapp.vault.utils.IOUtil
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.gson.JsonObject
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.vault_rowview_add_credit_cards_and_loan.view.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.ArrayList


class WillActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityWillBinding
    lateinit var willDetailResponse: WillDetailResponse.Will
    var will_id: String = ""
    var holder_id = ""
    var holder_name = ""
    var holder_userName = ""
    var filePath: String = ""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_will)
        setStatusBarGradiant(activity)
        holder_id = intent.getStringExtra("holder_id").toString()
        holder_name = intent.getStringExtra("holder_name").toString()
        holder_userName = intent.getStringExtra("holder_userName").toString()
        initView()
        onClicks()
    }

    private fun initView()
    {
        binding.toolbar.txtTitle.text = "Will"

        binding.txtHolderName.text = holder_userName

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
            appUtils.removeError(binding.edtOriginalWillLocated, binding.ipOriginalWillLocated)
            appUtils.removeError(binding.edtFile, binding.ipFile)
        }
        catch (e: Exception)
        {
        }

        if (isOnline())
        {
            getData()
        }
        else
        {
            noInternetToast()
        }
    }

    private fun onClicks()
    {
        binding.tvOpenWill.setOnClickListener {
            browse(filePath)
        }

        binding.toolbar.llMenuToolBar.setOnClickListener {
            hideKeyBoard()
            finish()
            finishActivityAnimation()
        }

        binding.edtFile.setOnClickListener {

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
                                    filePath= ImagePicker.getFilePath(data).toString()
                                    val f = File(filePath)
                                    val imageName = f.name

                                    Log.d(">>>>>>>", "<><>$filePath")

                                    binding.edtFile.setText(imageName)

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

        binding.txtSubmit.setOnClickListener {
            hideKeyBoard()
            if (holder_name == "A")
            {
                if (binding.edtOriginalWillLocated.text!!.isEmpty())
                {
                    binding.ipOriginalWillLocated.error = "Where is your original Will located?."
                }
                else if (filePath.length == 0)
                {
                    binding.ipFile.error = "Please upload document."
                }
                else
                {
                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val rootObjectItems = JSONObject()
                        val jGroup = JSONObject()
                        if (binding.edtOriginalWillLocated.text!!.isNotEmpty() && filePath.length > 0)
                        {
                            if (will_id.length > 0)
                            {
                                jGroup.put("will_id", will_id)
                            }
                            jGroup.put("original_will_located", binding.edtOriginalWillLocated.text.toString().trim())
                            jGroup.put("upload_doc_review", filePath.toString().trim())
                        }

                        rootObjectItems.put(holder_id, jGroup)
                        rootObject.put("items", rootObjectItems)
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
                if (binding.edtOriginalWillLocated.text!!.isEmpty() && filePath.length == 0)
                {

                }
                else if (binding.edtOriginalWillLocated.text!!.isNotEmpty() && filePath.length > 0)
                {

                    if (isOnline())
                    {
                        val rootObject = JSONObject()
                        val rootObjectItems = JSONObject()
                        val jGroup = JSONObject()
                        if (will_id.length > 0)
                        {
                            jGroup.put("will_id", will_id)
                        }
                        jGroup.put("original_will_located", binding.edtOriginalWillLocated.text.toString().trim())
                        jGroup.put("upload_doc_review", filePath.toString().trim())

                        rootObjectItems.put(holder_id, jGroup)
                        rootObject.put("items", rootObjectItems)
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
    }

    private fun getData()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getWillDetail(sessionManager.userId)
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
                                    if (jsonObject.has("will"))
                                    {
                                        val jsonObjectwill = jsonObject.getJSONObject("will")

                                        if (jsonObjectwill.has(holder_id))
                                        {
                                            val jsonObjectData = jsonObjectwill.getJSONObject(holder_id)
                                            willDetailResponse = WillDetailResponse.Will()
                                            willDetailResponse.will_id = jsonObjectData.getString("will_id")
                                            willDetailResponse.original_will_located = jsonObjectData.getString("original_will_located")
                                            willDetailResponse.upload_doc_review = jsonObjectData.getString("upload_doc_review")

                                            will_id = willDetailResponse.will_id


                                            if (willDetailResponse.original_will_located.isNotEmpty())
                                            {
                                                binding.edtOriginalWillLocated.setText(willDetailResponse.original_will_located)
                                            }
                                            if (willDetailResponse.upload_doc_review.isNotEmpty())
                                            {
                                                filePath = willDetailResponse.upload_doc_review.toString().trim()
                                                binding.edtFile.setText(IOUtil.getFileName(willDetailResponse.upload_doc_review))
                                                visible(binding.tvOpenWill)
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    if (message.length > 0)
                                    {
                                       // showToast(message)
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

    private fun addData(items: String)
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE

                if (filePath.contains("/storage/emulated/"))
                {
                    val file = File(filePath)
                    val fileReqBody: RequestBody = RequestBody.create(file.path.toMediaTypeOrNull(), file)
                    val paramName = "upload_doc[" + holder_id + "]"
                    val part: MultipartBody.Part = MultipartBody.Part.createFormData(paramName, file.name, fileReqBody)

                    val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                    val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                    val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")

                    val call = apiService.willDSaveCall(itemPart, userIdPart, fromAppPart, part)

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
                            Log.e("<><> TAG :",t.message + "")
                            apiFailedToast()
                        }
                    })
                }
                else
                {
                    val itemPart: MultipartBody.Part = MultipartBody.Part.createFormData("items", items)
                    val userIdPart: MultipartBody.Part = MultipartBody.Part.createFormData("user_id", sessionManager.userId)
                    val fromAppPart: MultipartBody.Part = MultipartBody.Part.createFormData("from_app", "true")

                    val call = apiService.willDSaveCallNew(itemPart, userIdPart, fromAppPart)

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
                            Log.e("<><> TAG :",t.message + "")
                            binding.loading.llLoading.visibility = View.GONE
                            apiFailedToast()
                        }
                    })
                }
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
