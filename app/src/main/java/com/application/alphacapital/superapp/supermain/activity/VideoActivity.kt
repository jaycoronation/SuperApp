package com.application.alphacapital.superapp.supermain.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.application.alphacapital.superapp.R
import com.application.alphacapital.superapp.databinding.ActivityVideoBinding
import com.application.alphacapital.superapp.pms.utils.AppUtils
import com.application.alphacapital.superapp.supermain.model.Item
import com.application.alphacapital.superapp.supermain.model.VideoResponseModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.android.synthetic.main.rowview_feed.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoActivity : BaseActivity()
{
    private lateinit var binding : ActivityVideoBinding
    private var videoList: MutableList<VideoResponseModel.Item> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(activity,R.layout.activity_video)
        initView()
        onClick()
    }

    private fun initView()
    {
        visible(binding.toolbar.tvTitle)
        binding.toolbar.tvTitle.text = "Videos"
        if (intent.hasExtra("data"))
        {
            videoList = Gson().fromJson(intent.getStringExtra("data"),Array<VideoResponseModel.Item>::class.java).toMutableList()
            if (videoList.size > 0)
            {
                binding.rvFeed.adapter = VideoAdapter()
            }
            else
            {
                getVideoList()
            }
        }
        else
        {
            getVideoList()
        }
    }

    private fun onClick()
    {
        binding.toolbar.ivBack.backNav()
    }

    private fun getVideoList()
    {
        visible(binding.llLoading.llLoading)
        apiService.getVideos().enqueue(object : Callback<VideoResponseModel>{
            override fun onResponse(call: Call<VideoResponseModel>, response: Response<VideoResponseModel>) {
                if (response.isSuccessful)
                {
                    videoList = response.body()!!.items
                    binding.rvFeed.adapter = VideoAdapter()
                }
                gone(binding.llLoading.llLoading)
            }

            override fun onFailure(call: Call<VideoResponseModel>, t: Throwable) {
                apiFailedToast()
                finish()
                gone(binding.llLoading.llLoading)
            }
        })
    }

    inner class VideoAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
        {
            return ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rowview_feed, parent, false))
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
        {
            val getSet = videoList[position]

            Glide.with(activity)
                .load(getSet.snippet.thumbnails.high.url)
                .centerCrop()
                .placeholder(R.drawable.ic_alpha_logo)
                .into(holder.itemView.ivImage)

            holder.itemView.tvTile.text = getSet.snippet.title
            gone(holder.itemView.tvByWhom)
            holder.itemView.tvDate.text = AppUtils.universalDateConvert(getSet.snippet.publishedAt,"yyyy-MM-dd'T'HH:mm:ss","dd MMM,yyyy")

            holder.itemView.setOnClickListener {
                val i = Intent(activity, YoutubePlayerActivity::class.java)
                i.putExtra("id", getSet.id.videoId)
                startActivity(i)
            }

        }

        override fun getItemCount(): Int
        {
            return videoList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    }
}