package com.application.alphacapital.superapp.supermain.activity

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityFeedBinding
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.application.alphacapital.superapp.supermain.model.FeedResponseModel
import com.application.alphacapital.superapp.supermain.model.FilterResponseModel
import com.application.alphacapital.superapp.supermain.model.Item
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.rowview_feed.view.*
import kotlinx.android.synthetic.main.rowview_filter.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedActivity : BaseActivity()
{
    private lateinit var binding: ActivityFeedBinding
    private var feedItemList: MutableList<Item> = mutableListOf()
    private var filterList : MutableList<FilterResponseModel.Url> = mutableListOf()
    private var feedAdapter = FeedAdapter()

    private var isLoading = false
    private var pageIndex = 1
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_feed)
        initView()
        onClick()
    }

    private fun initView()
    {
        visible(binding.toolbar.tvTitle)
        visible(binding.toolbar.tvManage)
        binding.toolbar.tvTitle.text = "Feeds"
        binding.toolbar.tvManage.text = "All"
        getFilterJSON()

        if (intent.hasExtra("data"))
        {
            feedItemList = Gson().fromJson(intent.getStringExtra("data"),Array<Item>::class.java).toMutableList()
            if (feedItemList.size > 0)
            {
                pageIndex = 2
                binding.rvFeed.adapter = FeedAdapter()
            }
            else
            {
                getFeedJSON(true)
            }
        }
        else
        {
            getFeedJSON(true)
        }
    }

    private fun onClick()
    {
        binding.toolbar.ivBack.backNav()

        binding.toolbar.tvManage.setOnClickListener {
            openFilterBottomSheet()
        }

        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvFeed.layoutManager = linearLayoutManager
        binding.nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null)
            {
                if (scrollY >= v.getChildAt(v.childCount - 1).measuredHeight - v.measuredHeight && scrollY > oldScrollY)
                {
                    val visibleItemCount: Int = linearLayoutManager.childCount
                    val totalItemCount: Int = linearLayoutManager.itemCount
                    val firstVisibleItemPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage)
                    {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0)
                        {
                            if (feedItemList.size > 0)
                            {
                                isLoading = true
                                visible(binding.llMore)
                                getFilterData(binding.toolbar.tvManage.text.toString(),false)
                            }
                        }
                    }
                }
            }
        })
    }

    private fun openFilterBottomSheet()
    {
        val bottomSheetDialog = BottomSheetDialog(this, R.style.MaterialDialogSheetTemp)
        bottomSheetDialog.setContentView(R.layout.dialog_filter)

        val rvFilter = bottomSheetDialog.findViewById<RecyclerView>(R.id.rvFilter)

        rvFilter!!.adapter = FilterAdapter(bottomSheetDialog)

        bottomSheetDialog.show()
    }

    private fun getFilterJSON()
    {
        apiService.getFilter().enqueue(object : Callback<FilterResponseModel>
        {
            override fun onResponse(call: Call<FilterResponseModel>, response: Response<FilterResponseModel>)
            {
                if (response.isSuccessful)
                {
                    filterList = response.body()!!.url
                    for (i in filterList.indices)
                    {
                        if (filterList[i].title == "All")
                        {
                            filterList[i].isSelected = true
                        }
                    }
                }
            }

            override fun onFailure(call: Call<FilterResponseModel>, t: Throwable)
            {
                apiFailedToast()
            }
        })
    }

    private fun getFeedJSON(isFirstTime: Boolean)
    {
        if (isFirstTime)
        {
            visible(binding.llLoading.llLoading)
            isLoading = false
            pageIndex = 1
            isLastPage = false
        }
        apiService.getFeedJsonData(pageIndex.toString()).enqueue(object : Callback<FeedResponseModel>
        {
            override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
            {
                if (response.isSuccessful)
                {
                    if (isFirstTime)
                    {
                        if (feedItemList.size > 0)
                        {
                            feedItemList = mutableListOf()
                        }
                    }

                    val tempList: MutableList<Item> = response.body()!!.items
                    feedItemList.addAll(tempList)

                    if (tempList.size != 0)
                    {
                        pageIndex += 1
                        if (tempList.size != 10)
                        {
                            isLastPage = true
                        }
                    }
                    isLoading = false

                    if (isFirstTime)
                    {
                        if (feedItemList.size > 0)
                        {
                            feedAdapter = FeedAdapter()
                            binding.rvFeed.adapter = feedAdapter
                        }
                    }
                    else
                    {
                        feedAdapter = FeedAdapter()
                        binding.rvFeed.adapter = feedAdapter
                    }
                    gone(binding.llMore)

                    Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                    Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                    Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                }
                else
                {
                    apiFailedToast()
                }
                gone(binding.llLoading.llLoading)
            }

            override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
            {
                gone(binding.llLoading.llLoading)
            }
        })
    }

    inner class FeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rowview_feed, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        {
            val getSet = feedItemList[position]

            Glide.with(activity)
                .load(getSet.image)
                .centerCrop()
                .placeholder(R.drawable.ic_alpha_logo)
                .into(holder.itemView.ivImage)

            holder.itemView.tvTile.text = getSet.title
            holder.itemView.tvByWhom.text = getSet.author.name
            holder.itemView.tvDate.text = AppUtils.universalDateConvert(getSet.date_published,"yyyy-MM-dd'T'HH:mm:ss","dd MMM,yyyy")

            holder.itemView.setOnClickListener {
                val i = Intent(activity,FeedDetailsActivity::class.java)
                i.putExtra("data",getSet.content_html)
                i.putExtra("title", getSet.title)
                startActivity(i)
            }

        }

        override fun getItemCount(): Int
        {
            return feedItemList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    inner class FilterAdapter(var dialog: BottomSheetDialog) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rowview_filter, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        {
            val getSet = filterList[position]

            holder.itemView.tvFilter.text = getSet.title

            val medium: Typeface? = ResourcesCompat.getFont(activity, R.font.medium)
            val bold: Typeface? = ResourcesCompat.getFont(activity, R.font.bold)


            if (getSet.isSelected)
            {
                holder.itemView.tvFilter.setTextColor(ContextCompat.getColor(activity,R.color.blue_new))
                holder.itemView.tvFilter.typeface = bold
            }
            else
            {
                holder.itemView.tvFilter.setTextColor(ContextCompat.getColor(activity,R.color.black))
                holder.itemView.tvFilter.typeface = medium
            }

            holder.itemView.setOnClickListener {
                for (i in filterList.indices)
                {
                    if (filterList[i].isSelected)
                    {
                        filterList[i].isSelected = false
                    }
                    else
                    {
                        if (i == position)
                        {
                            filterList[i].isSelected = true
                        }
                    }
                }
                getSet.isSelected = true
                binding.toolbar.tvManage.text = getSet.title
                getFilterData(getSet.title,true)
                dialog.dismiss()
            }

        }

        override fun getItemCount(): Int
        {
            return filterList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }

    private fun getFilterData(title: String, isFirstTime : Boolean)
    {
        if (isFirstTime)
        {
            visible(binding.llLoading.llLoading)
        }
        when (title) {
            "All" -> {
                apiService.getFeedJsonData(pageIndex.toString()).enqueue(object : Callback<FeedResponseModel>
                {
                    override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.isSuccessful)
                            {
                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedItemList = mutableListOf()
                                    }
                                }

                                val tempList: MutableList<Item> = response.body()!!.items
                                feedItemList.addAll(tempList)

                                if (tempList.size != 0)
                                {
                                    pageIndex += 1
                                    if (tempList.size != 10)
                                    {
                                        isLastPage = true
                                    }
                                }
                                isLoading = false

                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedAdapter = FeedAdapter()
                                        binding.rvFeed.adapter = feedAdapter
                                    }
                                }
                                else
                                {
                                    feedAdapter = FeedAdapter()
                                    binding.rvFeed.adapter = feedAdapter
                                }
                                gone(binding.llMore)

                                Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                                Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                                Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                            }
                            else
                            {
                                apiFailedToast()
                            }
                            gone(binding.llLoading.llLoading)

                        }
                        else
                        {
                            apiFailedToast()
                        }
                        gone(binding.llLoading.llLoading)
                    }

                    override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
                    {
                        gone(binding.llLoading.llLoading)
                    }
                })
            }
            "Financial Planning" -> {
                apiService.getFinPlanJSON().enqueue(object : Callback<FeedResponseModel>
                {
                    override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.isSuccessful)
                            {
                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedItemList = mutableListOf()
                                    }
                                }

                                val tempList: MutableList<Item> = response.body()!!.items
                                feedItemList.addAll(tempList)

                                if (tempList.size != 0)
                                {
                                    pageIndex += 1
                                    if (tempList.size != 10)
                                    {
                                        isLastPage = true
                                    }
                                }
                                isLoading = false

                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedAdapter = FeedAdapter()
                                        binding.rvFeed.adapter = feedAdapter
                                    }
                                }
                                else
                                {
                                    feedAdapter = FeedAdapter()
                                    binding.rvFeed.adapter = feedAdapter
                                }
                                gone(binding.llMore)

                                Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                                Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                                Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                            }
                            else
                            {
                                apiFailedToast()
                            }
                            gone(binding.llLoading.llLoading)
                        }
                        else
                        {
                            apiFailedToast()
                        }
                        gone(binding.llLoading.llLoading)
                    }

                    override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
                    {
                        gone(binding.llLoading.llLoading)
                    }
                })
            }
            "General" -> {
                apiService.getGeneralJSON().enqueue(object : Callback<FeedResponseModel>
                {
                    override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.isSuccessful)
                            {
                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedItemList = mutableListOf()
                                    }
                                }

                                val tempList: MutableList<Item> = response.body()!!.items
                                feedItemList.addAll(tempList)

                                if (tempList.size != 0)
                                {
                                    pageIndex += 1
                                    if (tempList.size != 10)
                                    {
                                        isLastPage = true
                                    }
                                }
                                isLoading = false

                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedAdapter = FeedAdapter()
                                        binding.rvFeed.adapter = feedAdapter
                                    }
                                }
                                else
                                {
                                    feedAdapter = FeedAdapter()
                                    binding.rvFeed.adapter = feedAdapter
                                }
                                gone(binding.llMore)

                                Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                                Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                                Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                            }
                            else
                            {
                                apiFailedToast()
                            }
                            gone(binding.llLoading.llLoading)
                        }
                        else
                        {
                            apiFailedToast()
                        }
                        gone(binding.llLoading.llLoading)
                    }

                    override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
                    {
                        gone(binding.llLoading.llLoading)
                    }
                })
            }
            "Investment Ideas" -> {
                apiService.getInvestmentIdeaJSON().enqueue(object : Callback<FeedResponseModel>
                {
                    override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.isSuccessful)
                            {
                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedItemList = mutableListOf()
                                    }
                                }

                                val tempList: MutableList<Item> = response.body()!!.items
                                feedItemList.addAll(tempList)

                                if (tempList.size != 0)
                                {
                                    pageIndex += 1
                                    if (tempList.size != 10)
                                    {
                                        isLastPage = true
                                    }
                                }
                                isLoading = false

                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedAdapter = FeedAdapter()
                                        binding.rvFeed.adapter = feedAdapter
                                    }
                                }
                                else
                                {
                                    feedAdapter = FeedAdapter()
                                    binding.rvFeed.adapter = feedAdapter
                                }
                                gone(binding.llMore)

                                Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                                Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                                Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                            }
                            else
                            {
                                apiFailedToast()
                            }
                            gone(binding.llLoading.llLoading)
                        }
                        else
                        {
                            apiFailedToast()
                        }
                        gone(binding.llLoading.llLoading)
                    }

                    override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
                    {
                        gone(binding.llLoading.llLoading)
                    }
                })
            }
            else -> {
                apiService.getTaxPlanningJSON().enqueue(object : Callback<FeedResponseModel>
                {
                    override fun onResponse(call: Call<FeedResponseModel>, response: Response<FeedResponseModel>)
                    {
                        if (response.isSuccessful)
                        {
                            if (response.isSuccessful)
                            {
                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedItemList = mutableListOf()
                                    }
                                }

                                val tempList: MutableList<Item> = response.body()!!.items
                                feedItemList.addAll(tempList)

                                if (tempList.size != 0)
                                {
                                    pageIndex += 1
                                    if (tempList.size != 10)
                                    {
                                        isLastPage = true
                                    }
                                }
                                isLoading = false

                                if (isFirstTime)
                                {
                                    if (feedItemList.size > 0)
                                    {
                                        feedAdapter = FeedAdapter()
                                        binding.rvFeed.adapter = feedAdapter
                                    }
                                }
                                else
                                {
                                    feedAdapter = FeedAdapter()
                                    binding.rvFeed.adapter = feedAdapter
                                }
                                gone(binding.llMore)

                                Log.e("<><><> isLastPage", "$isLastPage>>>>>")
                                Log.e("<><><>IS FIRST TIME", "$isFirstTime>>>>>")
                                Log.e("<><><>feedItemList SIZE", feedItemList.size.toString() + ">>>>>")
                            }
                            else
                            {
                                apiFailedToast()
                            }
                            gone(binding.llLoading.llLoading)
                        }
                        else
                        {
                            apiFailedToast()
                        }
                        gone(binding.llLoading.llLoading)
                    }

                    override fun onFailure(call: Call<FeedResponseModel>, t: Throwable)
                    {
                        gone(binding.llLoading.llLoading)
                    }
                })
            }
        }
    }

}