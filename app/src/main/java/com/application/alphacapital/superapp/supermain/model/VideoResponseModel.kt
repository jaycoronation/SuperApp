package com.application.alphacapital.superapp.supermain.model

data class VideoResponseModel(
    val etag: String, // hENTUt-g3d4P8OoxJ7LmNU9cnkw
    val items: MutableList<Item>,
    val kind: String, // youtube#searchListResponse
    val pageInfo: PageInfo,
    val regionCode: String // US
) {
    data class Item(
        val etag: String, // JyJ8JVGoHauDYdhvmeTLBImRMQA
        val id: Id,
        val kind: String, // youtube#searchResult
        val snippet: Snippet
    ) {
        data class Id(
            val channelId: String, // UC4b3XGclFHdug9IbdsnbwjA
            val kind: String, // youtube#video
            val videoId: String // naq6U7gja74
        )

        data class Snippet(
            val channelId: String, // UC4b3XGclFHdug9IbdsnbwjA
            val channelTitle: String, // ALPHA CAPITAL
            val description: String,
            val liveBroadcastContent: String, // none
            val publishTime: String, // 2022-08-16T09:00:30Z
            val publishedAt: String, // 2022-08-16T09:00:30Z
            val thumbnails: Thumbnails,
            val title: String // Market Outlook for the month of Aug&#39;22 with Mrs. Neeru Seal, Sr. Research Analyst, Alpha Capital
        ) {
            data class Thumbnails(
                val default: Default,
                val high: High,
                val medium: Medium
            ) {
                data class Default(
                    val height: Int, // 90
                    val url: String, // https://i.ytimg.com/vi/naq6U7gja74/default.jpg
                    val width: Int // 120
                )

                data class High(
                    val height: Int, // 360
                    val url: String, // https://i.ytimg.com/vi/naq6U7gja74/hqdefault.jpg
                    val width: Int // 480
                )

                data class Medium(
                    val height: Int, // 180
                    val url: String, // https://i.ytimg.com/vi/naq6U7gja74/mqdefault.jpg
                    val width: Int // 320
                )
            }
        }
    }

    data class PageInfo(
        val resultsPerPage: Int, // 27
        val totalResults: Int // 27
    )
}