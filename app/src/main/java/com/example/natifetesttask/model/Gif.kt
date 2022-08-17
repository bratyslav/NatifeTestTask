package com.example.natifetesttask.model

import com.google.gson.annotations.SerializedName

class GifApiResponse {
    @SerializedName("data")
    var data: List<Gif>? = null
}

class Gif {

    @SerializedName("id")
    var id: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("images")
    var images: GifImages? = null

}

class GifImages {

    @SerializedName("original")
    var original: GifImage? = null

    @SerializedName("preview_gif")
    var previewGif: GifImage? = null

    @SerializedName("480w_still")
    var _480wStill: GifImage? = null

}

class GifImage {

    @SerializedName("height")
    var height: String? = null

    @SerializedName("width")
    var width: String? = null

    @SerializedName("size")
    var size: String? = null

    @SerializedName("url")
    var url: String? = null

}