package com.attrsense.android.api

import com.attrsense.android.baselibrary.base.open.BaseApi
import com.attrsense.android.baselibrary.base.open.model.BaseResponse
import com.attrsense.android.baselibrary.base.open.model.EmptyBean
import com.attrsense.android.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * author : zhangshuai@attrsense.com
 * date : 2022/10/8 10:36
 * mark :网络接口
 */
interface ApiService : BaseApi {

    @POST("v1/user/register")
    suspend fun register(@Body body: MutableMap<String, Any?>): BaseResponse<EmptyBean?>


    /**
     * 手机号+验证码登录
     */
    @POST("v1/user/login_mobile")
    suspend fun login(@Body body: MutableMap<String, Any?>): BaseResponse<LoginBean?>

    /**
     * 刷新token
     */
    @PUT("v1/user/mobile_auth")
    suspend fun refreshToken(@Header("Authorization") refreshToken: String?): BaseResponse<LoginBean?>

    /**
     * 退出登录
     */
    @DELETE("v1/user/logout")
    suspend fun logout(@Header("Authorization") token: String?): BaseResponse<EmptyBean?>

    /**
     * 用户信息
     */
    @GET("v1/user/user_info")
    suspend fun getUserInfo(@Header("Authorization") token: String?): BaseResponse<UserDataBean?>

    /**
     * 文件上传
     * @param Authorization token
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
        @Part imageFile: List<MultipartBody.Part>?
    ): BaseResponse<ImagesBean?>


    /**
     * 查询上传的文件
     * @param Authorization token
     * @param body: page 查询的页号, perPage 每页数目
     */
    @POST("v1/query_files")
    suspend fun getUploadFiles(
        @Header("Authorization") token: String?,
        @Body body: MutableMap<String, Any?>
    ): BaseResponse<ImagesBean?>


    /**
     * 删除文件
     * @param url
     */
    @HTTP(method = "DELETE", path = "v1/delete_file", hasBody = true)
    suspend fun deleteFile(
        @Header("Authorization") token: String?,
        @Body body: MutableMap<String, Any?>
    ): BaseResponse<EmptyBean?>


    /**
     * 体验反馈
     * @param feedback 用户填写的反馈信息.
     * @param additional 附加图片
     */
    @POST("v1/feedback/experience")
    @Multipart
    suspend fun feedback(
        @Header("Authorization") token: String?,
        @Part("feedback") feedback: RequestBody,
        @Part additional: List<MultipartBody.Part>?
    ): BaseResponse<EmptyBean?>


    /**
     * 申请内测
     */
    @POST("v1/feedback/applyfor")
    suspend fun applyTest(
        @Header("Authorization") token: String?,
        @Body body: MutableMap<String, Any?>
    ): BaseResponse<EmptyBean?>
}