package com.application.alphacapital.superapp.finplan.activity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityRiskProfileQuestinnaireBinding
import com.application.alphacapital.superapp.databinding.FinPlanRowviewOptionsBinding
import com.application.alphacapital.superapp.finplan.model.RiskProfileQuestionsModel
import org.jetbrains.anko.startActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RiskProfileQuestinnaireActivity : FinPlanBaseActivity()
{
    private lateinit var binding : ActivityRiskProfileQuestinnaireBinding
    private var listQuestions : MutableList<RiskProfileQuestionsModel.RiskProfilerQuestion> = mutableListOf()
    private var adapter: QuestionaryAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_risk_profile_questinnaire)
        initView()
        onClick()
    }

    private fun initView()
    {
        visible(binding.toolbar.tvTitle)
        binding.toolbar.tvTitle.text = "Find Risk Profile"

        callRiskProfileQuestion()
    }

    private fun onClick()
    {
        binding.tvNext.setOnClickListener {
            if (binding.viewpager.currentItem == listQuestions.size-1)
            {
                startActivity<FinPlanRiskProfileActivity>()
                finish()
            }
            else
            {
                binding.viewpager.currentItem = binding.viewpager.currentItem + 1
                binding.tvCount.text = "${binding.viewpager.currentItem + 1}/${listQuestions.size}"
                if (binding.viewpager.currentItem >= 1)
                {
                    visible(binding.tvPrevious)
                }
                else
                {
                    gone(binding.tvPrevious)
                }
            }
        }

        binding.tvPrevious.setOnClickListener {
            binding.viewpager.currentItem = binding.viewpager.currentItem - 1;
            binding.tvCount.text = "${binding.viewpager.currentItem + 1}/${listQuestions.size}"

            if (binding.viewpager.currentItem >= 1)
            {
                visible(binding.tvPrevious)
            }
            else
            {
                gone(binding.tvPrevious)
            }
        }
    }

    private fun callRiskProfileQuestion()
    {
        loader.show()
        apiService.getRiskProfileQuestionnaire().enqueue(object : Callback<RiskProfileQuestionsModel>{
            override fun onResponse(call: Call<RiskProfileQuestionsModel>, response: Response<RiskProfileQuestionsModel>)
            {
                if (response.isSuccessful)
                {
                    if (response.body()!!.success == 1)
                    {
                        listQuestions = response.body()!!.risk_profiler_questions
                        adapter = QuestionaryAdapter()
                        binding.viewpager.adapter = adapter
                        binding.tvCount.text = "${binding.viewpager.currentItem + 1}/${listQuestions.size}"
                    }
                }
                loader.dismiss()
            }

            override fun onFailure(call: Call<RiskProfileQuestionsModel>, t: Throwable)
            {
                loader.dismiss()
                apiFailedToast()
            }
        })
    }

    inner class QuestionaryAdapter : PagerAdapter()
    {
        private var mLayoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        override fun getCount(): Int
        {
            return listQuestions.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean
        {
            return view === `object` as LinearLayout
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any)
        {
            container.removeView(`object` as LinearLayout)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any
        {
            val itemView: View? = mLayoutInflater.inflate(R.layout.fin_plan_rowview_question, container, false)
            val tvQuestion: AppCompatTextView = itemView?.findViewById(R.id.tvQuestion)!!
            val rvOptions: RecyclerView = itemView.findViewById(R.id.rvOptions)!!
            val getset = listQuestions[position]
            tvQuestion.text = "${position+1}. " + getset.question
            rvOptions.adapter = OptionsAdapter(getset.answers)
            container.addView(itemView)
            return itemView
        }
    }

    inner class OptionsAdapter(var answersList: MutableList<RiskProfileQuestionsModel.RiskProfilerQuestion.Answer>) : RecyclerView.Adapter<OptionsAdapter.ViewHolder>()
    {
        inner class ViewHolder(var bindingadapter : FinPlanRowviewOptionsBinding):RecyclerView.ViewHolder(bindingadapter.root)

        override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder
        {
            val binding: FinPlanRowviewOptionsBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fin_plan_rowview_options, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int)
        {
            val getSet = answersList[position]
            holder.bindingadapter.rbOption.text = getSet.answer
            holder.bindingadapter.rbOption.isChecked = getSet.is_selected

            holder.bindingadapter.rbOption.setOnClickListener {
                if (getSet.is_selected)
                {
                    getSet.is_selected = false
                }
                else
                {
                    getSet.is_selected = true
                }
                answersList[position] = getSet
                adapter?.notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int
        {
            return answersList.size
        }

    }

}