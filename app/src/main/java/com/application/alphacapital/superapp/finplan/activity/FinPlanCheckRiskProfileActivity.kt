package com.application.alphacapital.superapp.finplan.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.finplan.activity.FinPlanUserProfileActivity
import com.alphafinancialplanning.model.CommonResponse
import com.alphafinancialplanning.model.RiskProfileQueAndResponse
import com.application.alphacapital.superapp.R
import kotlinx.android.synthetic.main.fin_plan_activity_check_risk_profile.*
import kotlinx.android.synthetic.main.fin_plan_item_risk_ans.view.*
import kotlinx.android.synthetic.main.fin_plan_toolbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinPlanCheckRiskProfileActivity : FinPlanBaseActivity(), View.OnClickListener
{

    private var listQueAnd: MutableList<RiskProfileQueAndResponse.RiskProfilerQuestion> = mutableListOf()

    private var queCounter = 0

    private lateinit var answerAdapter: AnswerAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fin_plan_activity_check_risk_profile)
        initViews()
        getRiskProfileQueAns()
    }

    private fun initViews()
    {
        tvTitle.text = "Find Risk Profile"
        ivBack.backNav()

        btnPrevious.setOnClickListener(this)
        btnNext.setOnClickListener(this)
        rvAns.layoutManager = LinearLayoutManager(activity)
    }

    override fun onClick(view: View?)
    {
        when (view?.id)
        {
            btnPrevious.id ->
            {
                if (queCounter != 0)
                {
                    queCounter -= 1
                    fillAnswerList(queCounter)
                }
            }
            btnNext.id ->
            {
                answerAdapter.let {
                    if (!it.isQueChecked())
                    {
                        showToast("Please select at least one answer!")
                        return
                    }

                    if (queCounter == listQueAnd.size - 1)
                    {
                        var stringBuilder = StringBuilder()
                        for (i in listQueAnd.indices)
                        {
                            for (ansGetSet in listQueAnd[i].answers)
                            {
                                if (ansGetSet.selected)
                                {
                                    if (i == 0)
                                    {
                                        stringBuilder.append(ansGetSet.answer_id)
                                    }
                                    else
                                    {
                                        stringBuilder.append("," + ansGetSet.answer_id)
                                    }
                                }
                            }
                        }

                        Log.e("Final String", stringBuilder.toString())
                        findRiskProfile(stringBuilder.toString())
                    }
                    else
                    {
                        queCounter += 1
                        fillAnswerList(queCounter)
                    }
                }

            }
        }
    }

    private fun runLayoutAnimation()
    {
        val context: Context = rvAns.context
        val controller: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down)
        rvAns.layoutAnimation = controller
        rvAns.adapter!!.notifyDataSetChanged()
        rvAns.scheduleLayoutAnimation()
    }

    private fun fillAnswerList(quePosition: Int)
    {
        answerAdapter = AnswerAdapter(listQueAnd[quePosition].answers)
        rvAns.adapter = answerAdapter
        answerAdapter.notifyDataSetChanged()
        tvQuestion.text = listQueAnd[quePosition].question

        btnPrevious.visibility = View.INVISIBLE.takeIf { queCounter == 0 } ?: View.VISIBLE
        btnNext.text = "Calculate Risk Profile".takeIf { queCounter == listQueAnd.size - 1 } ?: "Next"
        runLayoutAnimation()
    }

    private fun getRiskProfileQueAns()
    {
        if (isOnline())
        {
            loader.show()
            apiService.getRiskProfileQueAns().enqueue(object : Callback<RiskProfileQueAndResponse>
                {
                    override fun onFailure(call: Call<RiskProfileQueAndResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<RiskProfileQueAndResponse>, response: Response<RiskProfileQueAndResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                listQueAnd.addAll(response.body()!!.risk_profiler_questions)
                                fillAnswerList(queCounter)
                                loader.dismiss()

                            }
                            else
                            {
                                showToast("Error while checking Risk profile")
                                loader.dismiss()
                                finish()
                                finishActivityAnimation()
                            }
                        }
                        else
                        {
                            apiFailedToast()
                            loader.dismiss()
                        }
                    }

                })
        }
        else
        {
            noInternetToast()
            finish()
            finishActivityAnimation()
        }
    }

    private fun findRiskProfile(ansIds: String)
    {
        if (isOnline())
        {
            loader.show()
            apiService.getRiskProfile(ansIds, sessionManager.userId).enqueue(object : Callback<CommonResponse>
                {
                    override fun onFailure(call: Call<CommonResponse>, t: Throwable)
                    {
                        apiFailedToast()
                        loader.dismiss()
                    }

                    override fun onResponse(call: Call<CommonResponse>, response: Response<CommonResponse>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.body()!!.success == 1)
                            {
                                showToast("Your Risk Profile is ${response.body()!!.risk_profile}")
                                sessionManager.riskProfile = response.body()!!.risk_profile
                                appUtils.sendMessageFromHandler(FinPlanUserProfileActivity.handler, response.body()!!.risk_profile, 4, 0, 0)
                                appUtils.sendMessageFromHandler(FinPlanMainActivity.handler, response.body()!!.risk_profile, 1, 0, 0)
                                loader.dismiss()
                                finish()
                                finishActivityAnimation()
                            }
                        }
                        else
                        {
                            apiFailedToast()
                            loader.dismiss()
                        }
                    }

                })

        }
        else
        {
            noInternetToast()
        }
    }


    inner class AnswerAdapter(private val ansList: MutableList<RiskProfileQueAndResponse.RiskProfilerQuestion.Answer>) : RecyclerView.Adapter<AnswerAdapter.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.fin_plan_item_risk_ans, parent, false))
        }

        override fun getItemCount(): Int
        {
            return ansList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            var getSet: RiskProfileQueAndResponse.RiskProfilerQuestion.Answer = ansList[position]
            holder.itemView.tvAns.text = getSet.answer
            holder.rbAns.isChecked = getSet.selected
            holder.itemView.setOnClickListener {
                disSelectOther(position)
            }
        }

        public fun isQueChecked(): Boolean
        {
            var b = false
            for (getSet in ansList)
            {
                if (getSet.selected)
                {
                    b = true
                    return b
                }
            }
            return b
        }

        private fun disSelectOther(position: Int)
        {
            for (i in ansList.indices)
            {
                var getSet: RiskProfileQueAndResponse.RiskProfilerQuestion.Answer = ansList[i]
                getSet.selected = i == position
                ansList[i] = getSet
            }
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            var rbAns: RadioButton = itemView.findViewById(R.id.rbAns)
            var tvAns: TextView = itemView.findViewById(R.id.tvAns)
        }

    }
}
