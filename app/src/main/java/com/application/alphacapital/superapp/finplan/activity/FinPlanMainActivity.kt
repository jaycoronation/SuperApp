package com.application.alphacapital.superapp.finplan.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphaestatevault.model.MenuGetSetForHome
import com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense.FinPlanAspirationListReportActivity
import com.application.alphacapital.superapp.finplan.activity.assetallocation.FinPlanAssetAllocationMacroListActivity
import com.application.alphacapital.superapp.finplan.activity.assetallocation.FinPlanAssetAllocationMicroListActivity
import com.application.alphacapital.superapp.finplan.activity.assetallocation.FinPlanRecommendedAssetAllocationListActivity
import com.application.alphacapital.superapp.finplan.activity.existingassets.FinPlanExistingAssetsListActivity
import com.application.alphacapital.superapp.finplan.activity.existingliabilities.FinPlanExistingLiabilityListActivity
import com.application.alphacapital.superapp.finplan.activity.futureinflow.FinPlanFutureInflowListReportActivity
import com.application.alphacapital.superapp.finplan.activity.networth.FinPlanNetWorthListActivity
import com.application.alphacapital.superapp.finplan.activity.varianceanalysis.FinPlanVarianceAnalysisMacroStrategicListActivity
import com.application.alphacapital.superapp.finplan.activity.varianceanalysis.FinPlanVarianceAnalysisMacroTacticalListActivity
import com.application.alphacapital.superapp.finplan.activity.varianceanalysis.FinPlanVarianceAnalysisMicroStrategicListActivity
import com.application.alphacapital.superapp.finplan.activity.varianceanalysis.FinPlanVarianceAnalysisMicroTacticalListActivity
import com.application.alphacapital.superapp.finplan.model.Dashboard
import com.application.alphacapital.superapp.finplan.utils.AppConstant.Companion.FINAL_REPORT
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.acpital.CapitalPref
import com.application.alphacapital.superapp.acpital.CapitalUserProfileActivity
import com.application.alphacapital.superapp.finplan.activity.aspirationfutureexpense.FinPlanAspirationFutureExpenseListActivity
import com.application.alphacapital.superapp.pms.activity.DashboardPortfolioActivity
import com.application.alphacapital.superapp.pms.utils.ApiUtils
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.application.alphacapital.superapp.vault.activity.VaultHomeActivity
import com.application.alphacapital.superapp.vault.activity.advisor.AdvisorListActivity
import com.application.alphacapital.superapp.vault.activity.assetsnotpossession.AssetsNotPossessionListActivity
import com.application.alphacapital.superapp.vault.activity.business.BusinessActivity
import com.application.alphacapital.superapp.vault.activity.charityrelatedobligations.CharityRelatedObligationsListActivity
import com.application.alphacapital.superapp.vault.activity.constitution_and_values.ConstitutionAndValuesListActivity
import com.application.alphacapital.superapp.vault.activity.creditcardsorpersonalorhomeloan.CreditCardsPersonalLoanHomeLoanListActivity
import com.application.alphacapital.superapp.vault.activity.deathnotifications.DeathNotificationsListActivity
import com.application.alphacapital.superapp.vault.activity.employmentrelated.EmpRelatedListActivity
import com.application.alphacapital.superapp.vault.activity.fiduciaryobligations.FiduciaryObligationsListActivity
import com.application.alphacapital.superapp.vault.activity.financialinstitutionaccounts.BankAccountsListActivity
import com.application.alphacapital.superapp.vault.activity.formerspouseorchildrenofformermarriages.FormerSpouseOrChildrenofFormerMarriagesListActivity
import com.application.alphacapital.superapp.vault.activity.governmentrelated.GovRelatedListActivity
import com.application.alphacapital.superapp.vault.activity.importantdocument.ImpDocListActivity
import com.application.alphacapital.superapp.vault.activity.insurancepolicies.InsurancePoliciesListActivity
import com.application.alphacapital.superapp.vault.activity.intellectualproperty.IntellectualPropertyListActivity
import com.application.alphacapital.superapp.vault.activity.investmenttrustaccounts.FinancialAssetsListActivity
import com.application.alphacapital.superapp.vault.activity.keystoresidence.ResidenceListActivity
import com.application.alphacapital.superapp.vault.activity.mutualfunds.MutualFundsListActivity
import com.application.alphacapital.superapp.vault.activity.otherassets.OtherAssetsListActivity
import com.application.alphacapital.superapp.vault.activity.otherdebts.OtherDebtsListActivity
import com.application.alphacapital.superapp.vault.activity.realproperty.RealPropertyListActivity
import com.application.alphacapital.superapp.vault.activity.safedeposite.SafeDepositListActivity
import com.application.alphacapital.superapp.vault.activity.sharesbonds.ShareBondsListActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fin_plan_activity_main.*
import kotlinx.android.synthetic.main.fin_plan_item_dashboard.view.*
import kotlinx.android.synthetic.main.fin_plan_item_dashboard.view.rvMenuSub
import kotlinx.android.synthetic.main.fin_plan_toolbar_home.*
import kotlinx.android.synthetic.main.vault_rowview_home_menu_main.view.*
import kotlinx.android.synthetic.main.vault_rowview_home_menu_main.view.tvItems
import kotlinx.android.synthetic.main.vault_rowview_home_menu_sub.view.*
import org.jetbrains.anko.*
import kotlin.math.roundToInt

class FinPlanMainActivity : FinPlanBaseActivity(), View.OnClickListener
{
    private var listDashboard: MutableList<Dashboard> = mutableListOf()
    private var dashboardAdapter = DashboardAdapter()

    companion object
    {
        var handler = Handler(Looper.getMainLooper())
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_main)

        handler = Handler(Looper.getMainLooper()){
            when(it.what)
            {
                1 ->
                {
                    fillDashboardList()
                }
            }
            false
        }

        initViews()
        fillDashboardList()
    }

    private fun initViews()
    {
        ivProfilePicToolBar.setOnClickListener(this)

        rvDashboard.layoutManager = GridLayoutManager(activity, 1)

        tvTitle.setOnClickListener {
            showSortPopup()
        }

        ivDown.setOnClickListener {
            showSortPopup()
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
        popupWindow.showAsDropDown(tvTitle)

        val container = popupWindow.contentView.parent as View
        val wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val p = container.layoutParams as WindowManager.LayoutParams

        p.flags = p.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        p.dimAmount = 0.4f
        wm.updateViewLayout(container, p)

        val tvTitle = popupView.findViewById<TextView>(R.id.tvTitle)
        tvTitle.text = "Estate Analysis"
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
            popupWindow.dismiss()
        }

        tvVault?.setOnClickListener {
            val intent = Intent(activity, VaultHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Getting a reference to Close button, and close the popup when clicked.
    }

    private fun fillDashboardList()
    {
        val assetallocation: MutableList<Dashboard.MenuGetSet> = mutableListOf()
        assetallocation.add(Dashboard.MenuGetSet("6", "Asset Allocation(Micro)", R.drawable.ic_micro_asset))
        assetallocation.add(Dashboard.MenuGetSet("7", "Asset Allocation(Macro)", R.drawable.macro))
        assetallocation.add(Dashboard.MenuGetSet("8", "Recommended Asset Allocation", R.drawable.recomm))

        val varianceAnalysis: MutableList<Dashboard.MenuGetSet> = mutableListOf()
        varianceAnalysis.add(Dashboard.MenuGetSet("9", "Variance Analysis (Micro-Strategic)", R.drawable.s_micro))
        varianceAnalysis.add(Dashboard.MenuGetSet("10", "Variance Analysis (Micro-Tactical)", R.drawable.t_micro))
        varianceAnalysis.add(Dashboard.MenuGetSet("11", "Variance Analysis (Macro-Strategic)", R.drawable.s_macro))
        varianceAnalysis.add(Dashboard.MenuGetSet("12", "Variance Analysis (Macro-Tactical)", R.drawable.t_macro))
        listDashboard = mutableListOf()
        listDashboard.add(Dashboard("4", R.drawable.ic_aspiration, R.drawable.ic_aspiration, "Aspirations"))
        listDashboard.add(Dashboard("1", R.drawable.ic_existing_libaliti, R.drawable.ic_existing_libaliti, "Existing Assets"))
        listDashboard.add(Dashboard("2", R.drawable.ic_future_inflow, R.drawable.ic_future_inflow, "Existing Liabilities"))
        listDashboard.add(Dashboard("3", R.drawable.ic_existing_asset, R.drawable.ic_existing_asset, "Future Inflow"))
        if (sessionManager.riskProfile.isNotEmpty())
        {
            listDashboard.add(Dashboard("18", R.drawable.ic_final_report, R.drawable.ic_final_report, "Risk Profile (${sessionManager.riskProfile})"))
        }
        else
        {
            listDashboard.add(Dashboard("18", R.drawable.ic_final_report, R.drawable.ic_final_report, "Risk Profile"))
        }
        listDashboard.add(Dashboard("17", R.drawable.ic_final_report_new, R.drawable.ic_final_report_new, "Generate Final Report"))
        //listDashboard.add(Dashboard("5", R.drawable.ic_networth, R.drawable.ic_networth, "Networth"))
        //listDashboard.add(Dashboard("6", R.drawable.ic_balancesheet_movement, R.drawable.ic_asset_allocation_white, "Asset Allocation", assetallocation)) //listDashboard.add(Dashboard("7", 0, "Asset Allocation(Macro)"))
        //listDashboard.add(Dashboard("8", 0, "Recommended Asset Allocation"))
        //listDashboard.add(Dashboard("9", R.drawable.ic_vranice_analysis, R.drawable.ic_variance_analsys_whitw, "Variance Analysis ", varianceAnalysis)) //listDashboard.add(Dashboard("10", 0, "Variance Analysis (Micro-Tactical)"))
        //listDashboard.add(Dashboard("11", 0, "Variance Analysis (Macro-Strategic)"))
        //listDashboard.add(Dashboard("12", 0, "Variance Analysis (Macro-Tactical)"))
        //listDashboard.add(Dashboard("13", R.drawable.ic_asset_allocation, R.drawable.ic_asset_allocation, "Balance Sheet Movement"))
        //listDashboard.add(Dashboard("14", R.drawable.ic_wealth_required, R.drawable.ic_wealth_required, "Wealth Required"))
        //listDashboard.add(Dashboard("15", R.drawable.ic_return_risk, R.drawable.ic_return_risk, "Return Of Risk"))
        //listDashboard.add(Dashboard("16", R.drawable.ic_final_report, R.drawable.ic_final_report, "Risk Profile Allocation"))

        rvDashboard.adapter = dashboardAdapter
    }

    override fun onClick(v: View?)
    {
        when (v?.id)
        {
            ivProfilePicToolBar.id ->
            {
                showNavigationDialog()
            }
        }
    }

    private fun showNavigationDialog()
    {
        val view = LayoutInflater.from(activity).inflate(R.layout.fin_plan_dialog_navigation, null)
        val dialog = BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
        dialog.setContentView(view)
        dialog.setCanceledOnTouchOutside(false)
        try
        {
            val window = dialog.window
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
                dialog.window!!.statusBarColor = activity.resources.getColor(R.color.fin_plan_transparent)
            }
        }
        catch (e: Exception)
        {
        }

        val llUpdateProfileDialog = view.findViewById<LinearLayout>(R.id.llUpdateProfileDialog)
        val llChangePasswordDialog = view.findViewById<LinearLayout>(R.id.llChangePasswordDialog)
        val llLogoutDialog = view.findViewById<LinearLayout>(R.id.llLogoutDialog)

        llUpdateProfileDialog.setOnClickListener {
            dialog.dismiss()
            startActivity<FinPlanUserProfileActivity>()
            startActivityAnimation()
        }

        llChangePasswordDialog.setOnClickListener {
            startActivity<FinPlanChangePasswordActivity>()
            dialog.dismiss()
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
        alert("Are you sure you want to logout from app?", "Logout?") {
            yesButton {
                sessionManager.logoutUser()
                it.dismiss()
            }
            noButton {
                it.dismiss()
            }
        }.show()
    }

    inner class DashboardAdapter : RecyclerView.Adapter<DashboardAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_dashboard, parent, false))
        }

        override fun getItemCount(): Int
        {
            return listDashboard.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: Dashboard = listDashboard[position]
            holder.itemView.tvDashboard.text = getSet.title

            if (getSet.isOpen)
            {
                if (getSet.title == "Generate Final Report")
                {
                    Log.e("<><>RISK","IN")
                    holder.itemView.llNew.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_new))
                    holder.itemView.tvDashboard.setTextColor(ContextCompat.getColor(activity, R.color.white))
                    holder.itemView.ivArrow.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                    Glide.with(activity).load(getSet.icon_white).into(holder.itemView.ivDashboard)
                    holder.itemView.ivArrow.rotation = 90F
                    visible(holder.itemView.rvMenuSub)
                }
            }
            else
            {
                holder.itemView.llNew.setBackgroundColor(ContextCompat.getColor(activity, R.color.light_blue_new))
                holder.itemView.tvDashboard.setTextColor(ContextCompat.getColor(activity, R.color.blue_new))
                holder.itemView.ivArrow.setColorFilter(ContextCompat.getColor(activity, R.color.blue_new), android.graphics.PorterDuff.Mode.MULTIPLY)
                Glide.with(activity).load(getSet.icon).into(holder.itemView.ivDashboard)
                holder.itemView.ivArrow.rotation = 0F
                gone(holder.itemView.rvMenuSub)
            }

            if (getSet.title == "Generate Final Report" && !getSet.title.contains("Risk Profile"))
            {
                Log.e("<><>RISK","IN")
                val scale = resources.displayMetrics.density
                val dpAsPixels = (18 * scale + 0.5f)
                val start = (8 * scale + 0.5f)
                holder.itemView.llNew.setPadding(start.toInt(), dpAsPixels.toInt(), start.toInt(), dpAsPixels.roundToInt())
                holder.itemView.llNew.setBackgroundColor(ContextCompat.getColor(activity, R.color.blue_new))
                holder.itemView.tvDashboard.setTextColor(ContextCompat.getColor(activity, R.color.white))
                holder.itemView.ivDashboard.setColorFilter(ContextCompat.getColor(activity, R.color.white), android.graphics.PorterDuff.Mode.SRC_IN)
                holder.itemView.ivArrow.visibility = View.GONE
            }

            holder.itemView.rvMenuSub.layoutManager = GridLayoutManager(activity, 2)
            val menuAdapterSub = MenuAdapterSub(getSet.menulist)
            holder.itemView.rvMenuSub.adapter = menuAdapterSub

            holder.itemView.setOnClickListener {
                if (getSet.menulist.size == 0)
                {
                    when (getSet.id)
                    {
                        "1" ->
                        {
                            startActivity<FinPlanExistingAssetsListActivity>()
                            startActivityAnimation()
                        }
                        "2" ->
                        {
                            startActivity<FinPlanExistingLiabilityListActivity>()
                            startActivityAnimation()
                        }
                        "3" ->
                        {
                            startActivity<FinPlanFutureInflowListReportActivity>()
                            startActivityAnimation()
                        }
                        "4" ->
                        {
                            startActivity<FinPlanAspirationFutureExpenseListActivity>()
                            startActivityAnimation()
                        }
                        "5" ->
                        {
                            startActivity<FinPlanNetWorthListActivity>()
                            startActivityAnimation()
                        }
                        "6" ->
                        {
                            startActivity<FinPlanAssetAllocationMicroListActivity>()
                            startActivityAnimation()
                        }
                        "7" ->
                        {
                            startActivity<FinPlanAssetAllocationMacroListActivity>()
                            startActivityAnimation()
                        }
                        "8" ->
                        {
                            startActivity<FinPlanRecommendedAssetAllocationListActivity>()
                            startActivityAnimation()
                        }
                        "9" ->
                        {
                            startActivity<FinPlanVarianceAnalysisMicroStrategicListActivity>()
                            startActivityAnimation()
                        }
                        "10" ->
                        {
                            startActivity<FinPlanVarianceAnalysisMicroTacticalListActivity>()
                            startActivityAnimation()
                        }
                        "11" ->
                        {
                            startActivity<FinPlanVarianceAnalysisMacroStrategicListActivity>()
                            startActivityAnimation()
                        }
                        "12" ->
                        {
                            startActivity<FinPlanVarianceAnalysisMacroTacticalListActivity>()
                            startActivityAnimation()
                        }
                        "13" ->
                        {
                            startActivity<FinPlanBalanceSheetMovementListActivity>()
                            startActivityAnimation()
                        }
                        "14" ->
                        {
                            startActivity<FinPlanWealthRequiredListActivity>()
                            startActivityAnimation()
                        }
                        "15" ->
                        {
                            startActivity<FinPlanReturnRiskListActivity>()
                            startActivityAnimation()
                        }
                        "16" ->
                        {
                            startActivity<FinPlanRiskProfileAllocationListActivity>()
                            startActivityAnimation()
                        }
                        "17" ->
                        {
                            browse(FINAL_REPORT + getBase64(sessionManager.userId))
                        }
                        "18" ->
                        {
                            startActivity<FinPlanCheckRiskProfileActivity>()
                            startActivityAnimation()
                        }
                    }
                }
               /* else
                {
                    closeOther(position)
                }*/
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var ivDashboard: ImageView = itemView.findViewById(R.id.ivDashboard)
            var tvDashboard: TextView = itemView.findViewById(R.id.tvDashboard)
        }

        private fun closeOther(position: Int)
        {
            for (i in listDashboard.indices)
            {
                if (i == position)
                {
                    listDashboard[i].isOpen = !listDashboard[i].isOpen
                }
                else
                {
                    listDashboard[i].isOpen = false
                }
            }

            notifyDataSetChanged()
        }

    }

    inner class MenuAdapterSub(var menulist: MutableList<Dashboard.MenuGetSet>) : RecyclerView.Adapter<MenuAdapterSub.ViewHolder>()
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
            val getSet: Dashboard.MenuGetSet = menulist[position]
            holder.itemView.tvItems.text = getSet.menuName
            Glide.with(activity).load(getSet.drawable).into(holder.itemView.imgMenu)

            holder.itemView.setOnClickListener {
                when (getSet.menuId)
                {
                    "6" ->
                    {
                        startActivity<FinPlanAssetAllocationMicroListActivity>()
                        startActivityAnimation()
                    }
                    "7" ->
                    {
                        startActivity<FinPlanAssetAllocationMacroListActivity>()
                        startActivityAnimation()
                    }
                    "8" ->
                    {
                        startActivity<FinPlanRecommendedAssetAllocationListActivity>()
                        startActivityAnimation()
                    }
                    "9" ->
                    {
                        startActivity<FinPlanVarianceAnalysisMicroStrategicListActivity>()
                        startActivityAnimation()
                    }
                    "10" ->
                    {
                        startActivity<FinPlanVarianceAnalysisMicroTacticalListActivity>()
                        startActivityAnimation()
                    }
                    "11" ->
                    {
                        startActivity<FinPlanVarianceAnalysisMacroStrategicListActivity>()
                        startActivityAnimation()
                    }
                    "12" ->
                    {
                        startActivity<FinPlanVarianceAnalysisMacroTacticalListActivity>()
                        startActivityAnimation()
                    }
                }
            }
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}
