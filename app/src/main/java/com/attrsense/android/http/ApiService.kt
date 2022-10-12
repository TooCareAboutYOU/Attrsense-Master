package com.attrsense.android.http

import com.attrsense.android.baselibrary.base.BaseResponse
import com.attrsense.android.model.*
import com.attrsense.android.test.HPIImageBean
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
     */
    @POST("v1/user/mobile_auth")
    suspend fun login(@Body body: MutableMap<String, Any?>): BaseResponse<LoginBean?>

    /**
     * 刷新token
     */
    @PUT("v1/user/mobile_auth")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String?): BaseResponse<LoginBean?>


    /**
     * 文件上传
     * @param Authorization "Bearer " + token
     * @param rate 压缩参数1
     * @param roiRate 压缩参数2
     * @param imageFile 图片文件
     */
    @POST("v1/upload_file")
    @Multipart
    suspend fun uploadFile(
        @Header("Authorization") token: String?,
        @Part("rate") rate: RequestBody?,
        @Part("roiRate") roiRate: RequestBody?,
        @Part imageFile: MultipartBody.Part?
    ): BaseResponse<ImagesBean?>


    /**
     * 查询上传的文件
     * @param Authorization "Bearer " + token
     * @param body: page 查询的页号, perPage 每页数目
     */
    @POST("v1/query_files")
    suspend fun queryUploadFile(
        @Header("Authorization") token: String?,
        @Body body: MutableMap<String, Any?>
    ): BaseResponse<ImagesBean?>


    /**
     * 删除文件
     * @param url
     */
    @POST("v1/delete_file")
    suspend fun deleteFile(
        @Header("Authorization") token: String?,
        @Body body: MutableMap<String, Any?>
    ): BaseResponse<Any?>
}