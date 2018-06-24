package com.testkart.exam.edu.rest;

import com.testkart.exam.banking_digest.buy.model.MagazinesResponse;
import com.testkart.exam.banking_digest.my_magazines.model.MyMagazineResponse;
import com.testkart.exam.banking_digest.my_magazines.model.my_subscription.MySubscriptionResponse;
import com.testkart.exam.banking_digest.read.model.MagazineContentResponse;
import com.testkart.exam.edu.dashboard.MyDashboardResponse;
import com.testkart.exam.edu.exam.examlist.ExamListResponse;
import com.testkart.exam.edu.exam.examlist.examdetails.ExamDetailsResponse;
import com.testkart.exam.edu.exam.feedback.SubmitFeedbackResponse;
import com.testkart.exam.edu.exam.ibps.model.IbpsExamResponse;
import com.testkart.exam.edu.exam.model.CheckExamResponse;
import com.testkart.exam.edu.exam.model.ExamSubmitModel;
import com.testkart.exam.edu.help.HelpResponse;
import com.testkart.exam.edu.leaderboard.LeaderListResponse;
import com.testkart.exam.edu.login.model.LoginResponse;
import com.testkart.exam.edu.mailbox.MailCountResponse;
import com.testkart.exam.edu.mailbox.WalletResponse;
import com.testkart.exam.edu.myresult.MyResultResponse;
import com.testkart.exam.edu.profile.ProfileResponse;
import com.testkart.exam.edu.register.OTPModel;
import com.testkart.exam.edu.register.RegisterPostModel;
import com.testkart.exam.edu.register.RegisterResponse;
import com.testkart.exam.edu.register.ResendModel;
import com.testkart.exam.edu.transaction.TransationResponse;
import com.testkart.exam.packages.model.PackageResponse;
import com.testkart.exam.packages.model.coupon.CouponResponse;
import com.testkart.exam.packages.model.payu.PlaceOrderResponse;
import com.testkart.exam.packages.model.place_order.DataPlaceOrder;
import com.testkart.exam.payment.model.PaymentResponse;
import com.testkart.exam.testkart.contact_us.ServerResponse;
import com.testkart.exam.testkart.home.HomePageResponse;
import com.testkart.exam.testkart.my_result.score_card_model.ScoreCardResponse;
import com.testkart.exam.testkart.my_result.subject_report.SubjectReportResponse;
import com.testkart.exam.testkart.study_material.StudyMaterialResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by testkart on 12/4/17.
 */

public interface ApiInterface {


    @POST("rest/Users/login.json")
    @FormUrlEncoded
    Call<LoginResponse> getUserInfo(@Field("email") String email,
                                    @Field("password") String password,
                                    @Field("device") String device,
                                    @Field("manufacturer") String manufacturer,
                                    @Field("model") String model,
                                    @Field("imei_no") String imei_no);


    @POST("rest/Apis/sendmail.json")
    @FormUrlEncoded
    Call<ResendModel> sendNewDeviceLoginRequest(@Field("student_id") String student_id,
                                                @Field("message") String message,
                                    @Field("device") String device,
                                    @Field("manufacturer") String manufacturer,
                                    @Field("model") String model,
                                    @Field("imei_no") String imei_no);

    //student_id, imei_no, manufacturer, model, device


    @POST("rest/Forgots/password.json")
    @FormUrlEncoded
    Call<ResendModel> passwordResend(@Field("email") String email);

    @POST("rest/Forgots/reset_password.json")
    @FormUrlEncoded
    Call<ResendModel> resetPassword(@Field("verification_code") String code,
                                    @Field("password") String password,
                                    @Field("confirm_password ") String confirmPassword);

    @POST("rest/Emailverifications/emailVerify.json")
    @FormUrlEncoded
    Call<OTPModel> verifyOTP(@Field("verification_code") String otp);


    @POST("rest/Emailverifications/resendEmail.json")
    @FormUrlEncoded
    Call<ResendModel> reSendVerificationCode(@Field("email") String email);

    ///start
    @GET("rest/Exams/start.json")
    Call<IbpsExamResponse> getExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey, @Query("id") String id);

    @GET("1.json")
    Call<String> getExamScalar();

    @GET("rest/Registers/group.json")
    Call<String> getGroupScalar();

    @POST("rest/Registers/save.json")
   /* @FormUrlEncoded*/
    Call<RegisterResponse> registerUser(@Body RegisterPostModel user);


    @POST("rest/Exams/saveAll.json")
   /* @FormUrlEncoded*/
    Call<RegisterResponse> sendExamResponse(@Body ExamSubmitModel response);

    //@GET("rest/Leaderboards.json")
    @GET
    Call<LeaderListResponse> getLeaderList(@Url String fileUrl, @Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Helps.json")
    Call<HelpResponse> getHelpData(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Exams/index.json")
    Call<ExamListResponse> getTodayExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Exams/purchased.json")
    Call<ExamListResponse> getPurchaseExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    //www.testkart.com/rest/Exams/purchased.json?public_key=12345&private_key=67890

    @GET("rest/Exams/upcoming.json")
    Call<ExamListResponse> getUpcomingExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @GET("rest/Exams/expired.json")
    Call<ExamListResponse> getExpiredExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @POST("rest/Exams/feedbacks.json")
   /* @FormUrlEncoded*/
    Call<RegisterResponse> submitFeedback(@Body SubmitFeedbackResponse user);

    @POST("rest/Profiles/editProfile.json")
    @FormUrlEncoded
    Call<ResendModel> editProfile(@Field("enroll") String enroll,
                                  @Field("phone") String phone,
                                  @Field("guardian_phone") String guardianPhone,
                                  @Field("public_key") String publicKey,
                                  @Field("private_key") String privateKey);


    @POST("rest/Profiles/changePass.json")
    @FormUrlEncoded
    Call<ResendModel> passwordReset(@Field("oldPassword") String oldPassword,
                                    @Field("password") String password,
                                    @Field("con_password") String con_password,
                                    @Field("public_key") String publicKey,
                                    @Field("private_key") String privateKey);


    @GET("rest/Profiles/index.json")
    Call<ProfileResponse> getUserProfile(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @Multipart
    @POST("rest/Profiles/changePhoto.json")
   // @FormUrlEncoded
    Call<ResendModel> uploadImage(@Part MultipartBody.Part file/*@Part("data[photo]") RequestBody image*/, @Part("public_key") RequestBody publicKey,
                                  @Part("private_key") RequestBody privateKey);


    @GET("rest/Transactionhistorys/index.json")
    Call<TransationResponse> getTransactionData(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Results/index.json")
    Call<MyResultResponse> getMyResult(@Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @GET("rest/Dashboards/mail.json")
    Call<MailCountResponse> getMailCount(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET("rest/Dashboards/balance.json")
    Call<WalletResponse> getWalletCount(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);


    @GET("rest/Exams/view.json")
    Call<ExamDetailsResponse> examDetails(@Query("id") String id, @Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @GET("rest/Exams/instruction.json")
    Call<ExamDetailsResponse> getInstructions(@Query("id") String id, @Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @GET("rest/Dashboards/remainingExam.json")
    Call<CheckExamResponse> checkExam(@Query("public_key") String publicKey, @Query("private_key") String privateKey);


    @POST("rest/Exams/expiredFinish.json")
    @FormUrlEncoded
    Call<RegisterResponse> submitExpiredExam(@Field("exam_id") String exam_id,
                                             @Field("public_key") String publicKey,
                                             @Field("private_key") String privateKey);


    //get all packages
    @GET
    Call<PackageResponse> getAllPackages(@Url String url);

    //get all packages
    @GET
    Call<MagazinesResponse> getAllMagazines(@Url String url);


    //get all packages
    @GET
    Call<MySubscriptionResponse> getAllSubscriptions(@Url String url);

    //get all packages
    @GET("rest/Magazines/index.json")
    Call<MyMagazineResponse> getAllMyMagazines(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @POST("rest/packages/filter.json")
    @FormUrlEncoded
    Call<PackageResponse> getAllPackages(@Field("filter_type") String code,
                                     @Field("filter_value") String amount);

    //rest/Checkouts/coupon.json
    @POST("rest/Checkouts/coupon.json")
    @FormUrlEncoded
    Call<CouponResponse> checkCoupon(@Field("coupon_code") String code,
                                     @Field("total_amount") String amount,
                                     @Field("public_key") String publicKey,
                                     @Field("private_key") String privateKey);


    @POST
    @FormUrlEncoded
    Call<CouponResponse> checkCoupon1(@Url String fileUrl,
                                      @Field("coupon_code") String code,
                                      @Field("total_amount") String amount,
                                      @Field("public_key") String publicKey,
                                      @Field("private_key") String privateKey);


    @POST("rest/Checkouts/couponDelete.json")
    @FormUrlEncoded
    Call<CouponResponse> deleteCoupon(@Field("public_key") String publicKey,
                                     @Field("private_key") String privateKey);



    @POST
    @FormUrlEncoded
    Call<CouponResponse> deleteCoupon1(@Url String fileUrl,
                                       @Field("public_key") String publicKey,
                                       @Field("private_key") String privateKey);


    @POST("rest/Checkouts/placeOrder.json")
   /* @FormUrlEncoded*/
    Call<CouponResponse> placeOrder(@Body DataPlaceOrder user);

    @GET("rest/Payments/index.json")
    Call<PaymentResponse> getPaymentResponse(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    @GET
    Call<HomePageResponse> getHomePageResponse(@Url String fileUrl);

    @GET("rest/StudyMaterial/index.json")
    Call<StudyMaterialResponse> getStudyMaterial();

    @GET("rest/Dashboards/index.json")
    Call<MyDashboardResponse> getMyDashboardData(@Query("public_key") String publicKey, @Query("private_key") String privateKey);

    //update user token
    @GET("rest/Apis/updateusertoken.json")
    Call<StudyMaterialResponse> updateUserToken(@Query("id") String studentId, @Query("usertoken") String usertoken );


    @Multipart
    @POST("Contacts/submit")
    Call<ServerResponse> uploadDataWithAttachment(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("Contacts/submit")
    Call<ServerResponse> uploadDataWithoutAttachment(
            @PartMap() Map<String, RequestBody> partMap);

    @Multipart
    @POST("Contacts/submit")
    Call<String> uploadDataWithAttachmentStr(
            @PartMap() Map<String, RequestBody> partMap,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("Contacts/submit")
    Call<String> uploadDataWithOutAttachmentStr(
            @PartMap() Map<String, RequestBody> partMap);


    @GET("rest/Results/scorecard.json")
    Call<ScoreCardResponse> getScoreCard(@Query("result_id") String resultId, @Query("student_id") String studentId);

    @GET("rest/Results/subjectreport.json")
    Call<SubjectReportResponse> getSubjectResponse(@Query("result_id") String resultId, @Query("student_id") String studentId );


    @GET("rest/Apis/emailcapturing.json")
    Call<ResendModel> getOTP(@Query("capture_by") String captureBy, @Query("type") String type);


    @GET("Checkouts/placeOrder")
    Call<PlaceOrderResponse> getPlaceOrder(@Query("public_key") String publicKey,
                                           @Query("private_key") String privateKey,
                                           @Query("responses") String responses,
                                           @Query("couponCode") String couponCode,
                                           @Query("payment_gateway") String payment_gateway);

    @GET("rest/Magazines/expired.json")
    Call<MyMagazineResponse> getAllExpiredMagazines(@Query("public_key") String publicKey,
                                                    @Query("private_key") String privateKey);

    @GET("rest/Magazines/magazinepost.json")
    Call<MagazineContentResponse> getMagazineContent(@Query("public_key") String publicKey,
                                                     @Query("private_key") String privateKey,
                                                     @Query("id") String magId);
}
