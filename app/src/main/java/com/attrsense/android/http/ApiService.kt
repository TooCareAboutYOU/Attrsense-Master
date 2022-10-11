package com.attrsense.android.http

import com.attrsense.android.baselibrary.base.BaseResponse
import com.attrsense.android.model.*
import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:36
 * mark :网络接口
 */
interface ApiService {

    //https://cn.bing.com/HPImageArchive.aspx?format=js&idx=1&n=1
    @GET("HPImageArchive.aspx")
    @Headers("Accept: application/json")
    suspend fun getHPIImage(
        @Query("format") format: String,
        @Query("idx") idx: Int,
        @Query("n") n: Int
    ): HPIImageBean


    /**
     * 手机号+验证码登录注册
     * @param mobile 手机号
     * @param code 验证码, 默认为"111111"
     */
    @POST("v1/user/mobile_auth")
    @Headers("Content-Type: application/json; charset=utf-8", "Accept: application/json")
    @FormUrlEncoded
    suspend fun login(
        @Field("mobile") mobile: String,
        @Field("code") code: String
    ): BaseResponse<LoginBean?>

    /**
     * 刷新token
     */
    @Headers("Authorization : Bearer refresh_token")
    @PUT("v1/user/mobile_auth")
    suspend fun refreshToken(): BaseResponse<String>


    /**
     * 文件上传
     * @param Authorization "Bearer " + token
     * @param rate 压缩参数1
     * @param roi_rate 压缩参数2
     * @param image_file 图片文件
     */
    @POST("v1/upload_file")
    @FormUrlEncoded
    @Headers("Content-Type: multipart/form-data")
    suspend fun uploadFile(
        @Header("Authorization") Authorization: String,
        @Field("rate") rate: String,
        @Field("roi_rate") roi_rate: String,
        @Field("image_file") image_file: String
    ): BaseResponse<ImageInfoBean>


    /**
     * 查询上传的文件
     * @param Authorization "Bearer " + token
     * @param page 查询的页号
     * @param per_page 每页数目
     */
    @POST("v1/query_files")

    @Headers("Content-Type : application/json")
    suspend fun queryUploadFile(
        @Header("Authorization") Authorization: String,
        @Field("page") page: Int,
        @Field("per_page") per_page: Int
    ): BaseResponse<ImagesBean>


    /**
     * 删除文件
     * @param url
     */
    @POST("v1/delete_file")
    @FormUrlEncoded
    @Headers("Content-Type : application/json")
    suspend fun deleteFile(@Field("file_id") file_id: String): BaseResponse<Any>
}