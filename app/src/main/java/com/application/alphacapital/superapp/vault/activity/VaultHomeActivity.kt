package com.application.alphacapital.superapp.vault.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphaestatevault.model.AccountHolderListResponse
import com.alphaestatevault.model.MenuGetSetForHome
import com.alphaestatevault.utils.DialogActionInterface
import com.alphaestatevault.utils.UniversalAlertDialog
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.acpital.CapitalPref
import com.application.alphacapital.superapp.acpital.CapitalUserProfileActivity
import com.application.alphacapital.superapp.databinding.VaultActivityHomeBinding
import com.application.alphacapital.superapp.finplan.activity.FinPlanLoginActivity
import com.application.alphacapital.superapp.finplan.activity.FinPlanSplashActivity
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity
import com.application.alphacapital.superapp.pms.activity.OneDayChangeActivity
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.application.alphacapital.superapp.vault.activity.advisor.AdvisorListActivity
import com.application.alphacapital.superapp.vault.activity.assetsnotpossession.AssetsNotPossessionListActivity
import com.application.alphacapital.superapp.vault.activity.business.BusinessActivity
import com.application.alphacapital.superapp.vault.activity.charityrelatedobligations.CharityRelatedObligationsListActivity
import com.application.alphacapital.superapp.vault.activity.constitution_and_values.ConstitutionAndValuesListActivity
import com.application.alphacapital.superapp.vault.activity.creditcardsorpersonalorhomeloan.CreditCardsPersonalLoanHomeLoanListActivity
import com.application.alphacapital.superapp.vault.activity.deathnotifications.DeathNotificationsListActivity
import com.application.alphacapital.superapp.vault.activity.domesticemployees.DomesticEmployeesActivity
import com.application.alphacapital.superapp.vault.activity.employmentrelated.EmpRelatedListActivity
import com.application.alphacapital.superapp.vault.activity.fiduciaryobligations.FiduciaryObligationsListActivity
import com.application.alphacapital.superapp.vault.activity.financialinstitutionaccounts.BankAccountsListActivity
import com.application.alphacapital.superapp.vault.activity.formerspouseorchildrenofformermarriages.FormerSpouseOrChildrenofFormerMarriagesListActivity
import com.application.alphacapital.superapp.vault.activity.generally.GenerallyActivity
import com.application.alphacapital.superapp.vault.activity.governmentrelated.GovRelatedListActivity
import com.application.alphacapital.superapp.vault.activity.importantdocument.ImpDocListActivity
import com.application.alphacapital.superapp.vault.activity.insurancepolicies.InsurancePoliciesListActivity
import com.application.alphacapital.superapp.vault.activity.intellectualproperty.IntellectualPropertyListActivity
import com.application.alphacapital.superapp.vault.activity.investmenttrustaccounts.FinancialAssetsListActivity
import com.application.alphacapital.superapp.vault.activity.keystoresidence.ResidenceListActivity
import com.application.alphacapital.superapp.vault.activity.minorchildrenadultdependents.MinorChildrenAdultDependentsActivity
import com.application.alphacapital.superapp.vault.activity.mutualfunds.MutualFundsListActivity
import com.application.alphacapital.superapp.vault.activity.otherassets.OtherAssetsListActivity
import com.application.alphacapital.superapp.vault.activity.otherdebts.OtherDebtsListActivity
import com.application.alphacapital.superapp.vault.activity.realproperty.RealPropertyListActivity
import com.application.alphacapital.superapp.vault.activity.safedeposite.SafeDepositListActivity
import com.application.alphacapital.superapp.vault.activity.sharesbonds.ShareBondsListActivity
import com.application.alphacapital.superapp.vault.activity.will.WillActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.vault_rowview_home_menu_main.view.*
import kotlinx.android.synthetic.main.vault_rowview_home_menu_main.view.tvItems
import kotlinx.android.synthetic.main.vault_rowview_home_menu_sub.view.*
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt

class VaultHomeActivity : VaultBaseActivity()
{
    lateinit var binding: VaultActivityHomeBinding
    var listMenuForHome: MutableList<MenuGetSetForHome> = mutableListOf()
    lateinit var tvTitle: TextView

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.vault_activity_home)
        initView()
        onClicks()
        if (isOnline())
        {
            getHoldersData()
            getAccountHolderListListData()
        }
        else
        {
            noInternetToast()
        }

        handler = Handler(Looper.getMainLooper()) { msg ->
            try
            {
                if (msg.what == 100)
                {
                    setProfileData()
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            false
        }
    }

    private fun initView()
    {
        tvTitle = findViewById(R.id.tvTitle)
        setMenuAdapter()
        setProfileData()
    }

    private fun setProfileData()
    {
        if (sessionManager.profilePic.isNotEmpty())
        {
            Glide.with(activity).load(sessionManager.profilePic).placeholder(R.drawable.vault_img_placeholder).into(binding.toolbar.ivProfilePicToolBar)
        }
        else
        {
            Glide.with(activity).load(R.drawable.vault_img_placeholder).into(binding.toolbar.ivProfilePicToolBar)
        }
    }

    private fun onClicks()
    {
        binding.toolbar.tvTitle.setOnClickListener {
            showSortPopup()
        }

        binding.toolbar.llMenuToolBar.setOnClickListener {
            //showNavigationDialog()
        }

        binding.toolbar.imgShare.setOnClickListener {
            startActivity<VaultShareActivity>()
        }
    }

    private fun showSortPopup()
    {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView: View = inflater.inflate(R.layout.dialog_apps, null)

        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val focusable = true

        val popupWindow = PopupWindow(popupView, width, height, focusable)
        popupWindow.showAsDropDown(binding.toolbar.imgShare)

        val container = popupWindow.contentView.parent as View
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams

        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.4f
        wm.updateViewLayout(container, p)

        val tvTitle = popupView.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = "Estate Vault"
        val tvAlphaCapital = popupView.findViewById<TextView>(R.id.tvAlphaCapital)
        val tvFinacialPlanning = popupView.findViewById<TextView>(R.id.tvFinacialPlanning)
        val tvPMS = popupView.findViewById<TextView>(R.id.tvPMS)
        val tvVault = popupView.findViewById<TextView>(R.id.tvVault)

        tvAlphaCapital?.setOnClickListener {
            val `in` = Intent(baseContext, CapitalUserProfileActivity::class.java)
            `in`.putExtra("loginScuccess", "http://m.investwell.in/hcver8/pages/iwapplogin.jsp?bid=" + (CapitalPref.getString(baseContext, "bid") + "&user=" + CapitalPref.getString(baseContext, "uid") + "&password=" + CapitalPref.getString(baseContext, "password")))
            startActivity(`in`)
            finish()
        }

        tvFinacialPlanning?.setOnClickListener {
            ApiUtils.END_USER_ID = sessionManager.userId
            val intent = Intent(activity, DashboardPortfolioActivity::class.java)
            startActivity(intent)
            AppUtils.startActivityAnimation(activity)
            finish()
        }

        tvPMS?.setOnClickListener {
            val intent = Intent(activity, FinPlanSplashActivity::class.java)
            startActivity(intent)
            finish()
        }

        tvVault?.setOnClickListener {
          popupWindow.dismiss()
        }

        // Getting a reference to Close button, and close the popup when clicked.
    }


    private fun setMenuAdapter()
    {
        binding.rvMenuHome.layoutManager = LinearLayoutManager(activity)

        val list1: MutableList<MenuGetSetForHome.MenuGetSet> = mutableListOf()

        list1.add(MenuGetSetForHome.MenuGetSet("0", "Constitution And Values", ContextCompat.getDrawable(activity,R.drawable.vault_ic_account_holder)))
        list1.add(MenuGetSetForHome.MenuGetSet("1", "Death Notifications", ContextCompat.getDrawable(activity,R.drawable.vault_ic_death_notification)))
        list1.add(MenuGetSetForHome.MenuGetSet("2", "Advisors", ContextCompat.getDrawable(activity,R.drawable.vault_ic_advisor)))
        list1.add(MenuGetSetForHome.MenuGetSet("3", "Keys to Residence", ContextCompat.getDrawable(activity,R.drawable.vault_ic_key_to_residences)))
        list1.add(MenuGetSetForHome.MenuGetSet("4", "Safe Deposit Boxes", ContextCompat.getDrawable(activity,R.drawable.vault_ic_safe_deposit_box)))
        list1.add(MenuGetSetForHome.MenuGetSet("5", "Important Documents", ContextCompat.getDrawable(activity,R.drawable.vault_ic_important_documents)))
        listMenuForHome.add(MenuGetSetForHome("General Information", true, list1))

        val list2: MutableList<MenuGetSetForHome.MenuGetSet> = mutableListOf()
        list2.add(MenuGetSetForHome.MenuGetSet("6", "Medical & Funeral", ContextCompat.getDrawable(activity,R.drawable.vault_ic_generally)))
        list2.add(MenuGetSetForHome.MenuGetSet("7", "Dependent Children", ContextCompat.getDrawable(activity,R.drawable.vault_ic_dependant)))
        list2.add(MenuGetSetForHome.MenuGetSet("8", "Will", ContextCompat.getDrawable(activity,R.drawable.vault_ic_will)))
        list2.add(MenuGetSetForHome.MenuGetSet("9", "Business(es)", ContextCompat.getDrawable(activity,R.drawable.vault_ic_business)))
        list2.add(MenuGetSetForHome.MenuGetSet("10", "Domestic Employees", ContextCompat.getDrawable(activity,R.drawable.vault_ic_domestic_employee)))
        listMenuForHome.add(MenuGetSetForHome("Directions and Instructions", false, list2))


        val list3: MutableList<MenuGetSetForHome.MenuGetSet> = mutableListOf()
        list3.add(MenuGetSetForHome.MenuGetSet("11", "Government Related", ContextCompat.getDrawable(activity,R.drawable.vault_ic_goverment)))
        list3.add(MenuGetSetForHome.MenuGetSet("12", "Employment-Related", ContextCompat.getDrawable(activity,R.drawable.vault_ic_employee))) //kiran
        list3.add(MenuGetSetForHome.MenuGetSet("13", "Insurance Policies", ContextCompat.getDrawable(activity,R.drawable.vault_ic_instruction)))
        list3.add(MenuGetSetForHome.MenuGetSet("25", "Mutual Funds", ContextCompat.getDrawable(activity,R.drawable.vault_ic_finance)))
        list3.add(MenuGetSetForHome.MenuGetSet("26", "Shares Bonds", ContextCompat.getDrawable(activity,R.drawable.vault_ic_finance)))
        list3.add(MenuGetSetForHome.MenuGetSet("14", "Other Financial Assets", ContextCompat.getDrawable(activity,R.drawable.vault_ic_finance))) //kiran
        list3.add(MenuGetSetForHome.MenuGetSet("15", "Bank Accounts", ContextCompat.getDrawable(activity,R.drawable.vault_ic_bank)))
        list3.add(MenuGetSetForHome.MenuGetSet("16", "Intellectual Property", ContextCompat.getDrawable(activity,R.drawable.vault_ic_property)))
        list3.add(MenuGetSetForHome.MenuGetSet("17", "Real Estate", ContextCompat.getDrawable(activity,R.drawable.vault_ic_real_estate)))
        list3.add(MenuGetSetForHome.MenuGetSet("19", "Other Assets", ContextCompat.getDrawable(activity,R.drawable.vault_ic_other_assests)))
        listMenuForHome.add(MenuGetSetForHome("Assets", false, list3))

        val list4: MutableList<MenuGetSetForHome.MenuGetSet> = mutableListOf()
        list4.add(MenuGetSetForHome.MenuGetSet("20", "Credit Cards and Loans", ContextCompat.getDrawable(activity,R.drawable.vault_ic_credit_card)))
        list4.add(MenuGetSetForHome.MenuGetSet("21", "Former Spouse/ Children from previous marriage", ContextCompat.getDrawable(activity,R.drawable.vault_ic_former_spouse)))
        list4.add(MenuGetSetForHome.MenuGetSet("22", "Charity Related", ContextCompat.getDrawable(activity,R.drawable.vault_ic_charity)))
        list4.add(MenuGetSetForHome.MenuGetSet("23", "Fiduciary Obligations", ContextCompat.getDrawable(activity,R.drawable.vault_ic_fiduciary_obligations)))
        list4.add(MenuGetSetForHome.MenuGetSet("24", "Other Debts", ContextCompat.getDrawable(activity,R.drawable.vault_ic_debts)))
        listMenuForHome.add(MenuGetSetForHome("Obligation & Debt", false, list4))

        val menuAdapterHome = MenuAdapterHome()
        binding.rvMenuHome.adapter = menuAdapterHome
    }

    inner class MenuAdapterHome() : RecyclerView.Adapter<MenuAdapterHome.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_home_menu_main, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listMenuForHome.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: MenuGetSetForHome = listMenuForHome[position]
            holder.itemView.tvItems.text = getSet.menuName

            if (getSet.isOpen)
            {
                holder.itemView.llMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_new))
                holder.itemView.tvItems.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.itemView.imgArrow.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                holder.itemView.imgArrow.rotation = 90F
                visible(holder.itemView.rvMenuSub)
            }
            else
            {
                holder.itemView.llMain.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_blue_new))
                holder.itemView.tvItems.setTextColor(ContextCompat.getColor(activity, R.color.blue_new))
                holder.itemView.imgArrow.setColorFilter(ContextCompat.getColor(activity, R.color.blue_new), android.graphics.PorterDuff.Mode.MULTIPLY)
                holder.itemView.imgArrow.rotation = 0F
                gone(holder.itemView.rvMenuSub)
            }

            holder.itemView.rvMenuSub.layoutManager = GridLayoutManager(activity, 2)
            val menuAdapterSub = MenuAdapterSub(getSet.menulist)
            holder.itemView.rvMenuSub.adapter = menuAdapterSub

            holder.itemView.setOnClickListener {
                closeOther(position)
            }
        }

        private fun closeOther(position: Int)
        {
            for (i in listMenuForHome.indices)
            {
                if (i == position)
                {
                    listMenuForHome[i].isOpen = !listMenuForHome[i].isOpen
                }
                else
                {
                    listMenuForHome[i].isOpen = false
                }
            }

            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    inner class MenuAdapterSub(var menulist: MutableList<MenuGetSetForHome.MenuGetSet>) : RecyclerView.Adapter<MenuAdapterSub.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.vault_rowview_home_menu_sub, parent, false))
        }

        override fun getItemCount(): Int
        {
            return menulist.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet: MenuGetSetForHome.MenuGetSet = menulist[position]
            holder.itemView.tvItems.text = getSet.menuName
            holder.itemView.imgMenu.setImageDrawable(getSet.drawable)

            holder.itemView.setOnClickListener {
                if (getHoldersFullList().size > 0)
                {
                    when (getSet.menuId) {
                        "0" -> {
                            startActivity<ConstitutionAndValuesListActivity>()
                        }
                        "1" -> {
                            startActivity<DeathNotificationsListActivity>()
                        }
                        "2" -> {
                            startActivity<AdvisorListActivity>()
                        }
                        "3" -> {
                            startActivity<ResidenceListActivity>()
                        }
                        "4" -> {
                            startActivity<SafeDepositListActivity>()
                        }
                        "5" -> {
                            startActivity<ImpDocListActivity>()
                        }
                        "6" -> {
                            accountHolderDialog("6")
                        }
                        "7" -> {
                            accountHolderDialog("7")
                        }
                        "8" -> {
                            accountHolderDialog("8")
                        }
                        "9" -> {
                            startActivity<BusinessActivity>()
                        }
                        "10" -> {
                            accountHolderDialog("10")
                        }
                        "11" -> {
                            startActivity<GovRelatedListActivity>()
                        }
                        "12" -> { //kiran
                            startActivity<EmpRelatedListActivity>()
                        }
                        "13" -> {
                            startActivity<InsurancePoliciesListActivity>()
                        }
                        "14" -> { //kiran
                            startActivity<FinancialAssetsListActivity>()
                        }
                        "15" -> {
                            startActivity<BankAccountsListActivity>()
                        }
                        "16" -> { //kiran
                            startActivity<IntellectualPropertyListActivity>()
                        }
                        "17" -> {
                            startActivity<RealPropertyListActivity>()
                        }
                        "18" -> { //kiran
                            startActivity<AssetsNotPossessionListActivity>()
                        }
                        "19" -> {
                            startActivity<OtherAssetsListActivity>()
                        }
                        "20" -> {
                            startActivity<CreditCardsPersonalLoanHomeLoanListActivity>()
                        }
                        "21" -> {
                            startActivity<FormerSpouseOrChildrenofFormerMarriagesListActivity>()
                        }
                        "22" -> {
                            startActivity<CharityRelatedObligationsListActivity>()
                        }
                        "23" -> {
                            startActivity<FiduciaryObligationsListActivity>()
                        }
                        "24" -> {
                            startActivity<OtherDebtsListActivity>()
                        }
                        "25" -> { //kiran
                            startActivity<MutualFundsListActivity>()
                        }
                        "26" -> { //kiran
                            startActivity<ShareBondsListActivity>()
                        }
                    }
                }
                else
                {
                    showToast("Please add your ACCOUNT HOLDER first.")
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    private fun getHoldersData()
    {
        if (isOnline())
        {
            apiService.getHolders().enqueue(object : Callback<List<String>>
            {
                override fun onFailure(call: Call<List<String>>, t: Throwable)
                {
                    TODO("Not yet implemented")
                }

                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>)
                {
                    sessionManager.holders = Gson().toJson(response.body()!!)
                }
            })
        }
    }

    private fun getAccountHolderListListData()
    {
        try
        {
            if (isOnline())
            {
                hideKeyBoard()
                binding.loading.llLoading.visibility = View.VISIBLE
                val call = apiService.getaccountHoldersCall(sessionManager.userId, "", "", "", "", "")

                call.enqueue(object : Callback<AccountHolderListResponse>
                {
                    override fun onResponse(call: Call<AccountHolderListResponse>, response: Response<AccountHolderListResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                sessionManager.holdersList = Gson().toJson(response.body()!!.holders)
                            }
                            else
                            {
                                var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                                sessionManager.holdersList = Gson().toJson(listHolder)
                            }
                        }
                        else
                        {
                            var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                            sessionManager.holdersList = Gson().toJson(listHolder)
                            apiFailedToast()
                        }

                        binding.loading.llLoading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<AccountHolderListResponse>, t: Throwable)
                    {
                        var listHolder: MutableList<AccountHolderListResponse.Holder> = mutableListOf()
                        sessionManager.holdersList = Gson().toJson(listHolder)
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

    private fun accountHolderDialog(menuId: String)
    {
        if (getHoldersFullList().size > 0)
        {
            if (menuId == "6")
            {
                val intent = Intent(activity, GenerallyActivity::class.java)
                intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
                intent.putExtra("holder_name", getHoldersFullList()[0].holder)
                intent.putExtra("holder_userName", getHoldersFullList()[0].name)
                startActivity(intent)
                startActivityAnimation()
            }
            else if (menuId == "7")
            {
                val intent = Intent(activity, MinorChildrenAdultDependentsActivity::class.java)
                intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
                intent.putExtra("holder_name", getHoldersFullList()[0].holder)
                intent.putExtra("holder_userName", getHoldersFullList()[0].name)
                startActivity(intent)
                startActivityAnimation()
            }
            else if (menuId == "8")
            {
                val intent = Intent(activity, WillActivity::class.java)
                intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
                intent.putExtra("holder_name", getHoldersFullList()[0].holder)
                intent.putExtra("holder_userName", getHoldersFullList()[0].name)
                startActivity(intent)
                startActivityAnimation()
            }
            else if (menuId == "10")
            {
                val intent = Intent(activity, DomesticEmployeesActivity::class.java)
                intent.putExtra("holder_id", getHoldersFullList()[0].holder_id)
                intent.putExtra("holder_name", getHoldersFullList()[0].holder)
                intent.putExtra("holder_userName", getHoldersFullList()[0].name)
                startActivity(intent)
                startActivityAnimation()
            }
        }
        else
        {
            showToast("Holder Data Not Found.")
        }
    }

    private fun showNavigationDialog()
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.vault_dialog_navigation, null)
        val dialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)

        try
        {
            val window = dialog.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
                dialog.window!!.statusBarColor = ContextCompat.getColor(activity,R.color.vault_transparent)
            }
        }
        catch (e: Exception)
        {
        }

        dialog.setCanceledOnTouchOutside(true)

        val llUpdateProfileDialog = view.findViewById<LinearLayout>(R.id.llUpdateProfileDialog)
        val llChangePasswordDialog = view.findViewById<LinearLayout>(R.id.llChangePasswordDialog)
        val llLogoutDialog = view.findViewById<LinearLayout>(R.id.llLogoutDialog)

        llUpdateProfileDialog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, VaultUpdateProfileActivity::class.java)
            startActivity(intent)
            startActivityAnimation()
        }

        llChangePasswordDialog.setOnClickListener {
            dialog.dismiss()
            val intent = Intent(activity, VaultChangePasswordActivity::class.java)
            startActivity(intent)
            startActivityAnimation()
        }

        llLogoutDialog.setOnClickListener {
            dialog.dismiss()
            logoutDialog()
        }

        dialog.show()
    }

    private fun logoutDialog()
    {
        UniversalAlertDialog(activity, "Logout from Alpha Vault", "Are you sure you want to logout from app?", object : DialogActionInterface
        {
            override fun okClick()
            {
                sessionManager.logoutUser()
            }

            override fun cancelClick()
            {
            }

            override fun onDismiss()
            {
            }

        }).showAlert()
    }
}
