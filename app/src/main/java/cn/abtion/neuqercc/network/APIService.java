package cn.abtion.neuqercc.network;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import cn.abtion.neuqercc.account.models.CheckTokenResponse;
import cn.abtion.neuqercc.account.models.LoginRequest;
import cn.abtion.neuqercc.account.models.RegisterRequest;
import cn.abtion.neuqercc.account.models.SmsRequest;
import cn.abtion.neuqercc.account.models.SmsResponse;
import cn.abtion.neuqercc.account.models.TokenResponse;
import cn.abtion.neuqercc.account.models.UpdatePasswordRequest;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewDataRequest;
import cn.abtion.neuqercc.home.models.InitContestRecylerViewItemRequest;
import cn.abtion.neuqercc.home.models.InitCrouselFigureRequest;
import cn.abtion.neuqercc.home.models.RaidersAndDetailsRequest;
import cn.abtion.neuqercc.home.models.SearchContestNameRequest;
import cn.abtion.neuqercc.message.models.AddFriendRequest;
import cn.abtion.neuqercc.message.models.FriendModel;
import cn.abtion.neuqercc.message.models.NoticeModel;
import cn.abtion.neuqercc.message.models.SearchUserModel;
import cn.abtion.neuqercc.mine.models.AddTeamMemberRequest;
import cn.abtion.neuqercc.mine.models.FeedBackRequest;
import cn.abtion.neuqercc.mine.models.GoodAtRequest;
import cn.abtion.neuqercc.mine.models.MineTeamInformationRequest;
import cn.abtion.neuqercc.mine.models.MineTeamListResponse;
import cn.abtion.neuqercc.mine.models.PersonInformationResponse;
import cn.abtion.neuqercc.mine.models.ShowHonorResponse;
import cn.abtion.neuqercc.mine.models.StudentGradeRequest;
import cn.abtion.neuqercc.mine.models.TeamMemberResponse;
import cn.abtion.neuqercc.mine.models.UpdateTeamInformationRequest;
import cn.abtion.neuqercc.team.models.EstablishTeamRequest;
import cn.abtion.neuqercc.team.models.InitAllTeamDataResponse;
import cn.abtion.neuqercc.team.models.InitAllTeamResponse;
import cn.abtion.neuqercc.team.models.InitRecommendTeamDataResponse;
import cn.abtion.neuqercc.team.models.InitRecommendTeamResponse;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * retrofit service interface.
 *
 * @author abtion.
 * @since 17/9/22 18:04.
 * email caiheng@hrsoft.net
 */

public interface APIService {


    /**
     * 检查token
     *
     * @param token
     * @return
     */
    @POST("check")
    Call<APIResponse<CheckTokenResponse>> check(@Query("token") String token);

    /**
     * 登录
     */
    @POST("login")
    Call<APIResponse<TokenResponse>> login(@Body LoginRequest loginRequest);

    /**
     * 验证码
     */

    @POST("sms")
    Call<APIResponse<SmsResponse>> captcha(@Body SmsRequest smsRequest);

    /**
     * 注册
     */
    @POST("register")
    Call<APIResponse> register(@Body RegisterRequest registerRequest);

    /**
     * 重新设置密码
     */
    @POST("forgot")
    Call<APIResponse> updatePassword(@Body UpdatePasswordRequest updatePasswordRequest);

    /**
     * 轮播图GET请求
     */
    @GET("carousel/show")
    Call<APIResponse<List<InitCrouselFigureRequest>>> initCrouselFigure();


    /**
     * 比赛详情列表GET请求
     */
    @GET("descs/show")
    Call<APIResponse<InitContestRecylerViewDataRequest<List<InitContestRecylerViewItemRequest>>>>
    initContestRecylerView(@Query("page") int page, @Query("size") int size);


    /**
     * 大神攻略和比赛详情内容GET
     */
    @GET("raider/show")
    Call<APIResponse<RaidersAndDetailsRequest>> getRaidersAndDetails(@Query("id") int contestItemId);

    /**
     * 搜索队伍POST请求
     */
    @POST("team/search")
    Call<APIResponse<List<InitAllTeamResponse>>> searchTeam(@Query("content") String seachTeamName);

    /**
     * 查询多支队伍GET请求
     */
    @GET("teams/show")
    Call<APIResponse<InitAllTeamDataResponse<List<InitAllTeamResponse>>>> initAllTeam(@Query("page") int page, @Query
            ("size") int teamSize);

    /**
     * 查询推荐队伍队伍GET请求
     */
    @GET("team/recommend")
    Call<APIResponse<InitRecommendTeamDataResponse<List<InitRecommendTeamResponse>>>> initRecommendTeam(@Query("phone") String contestName, @Query("page") int page, @Query("size") int teamSize);


    /**
     * 创建队伍POST请求
     */
    @POST("team/add")
    Call<APIResponse> establishTeam(@Body EstablishTeamRequest establishTeamRequest);


    /**
     * 搜索比赛POST请求
     */
    @POST("desc/search")
    Call<APIResponse<List<InitContestRecylerViewItemRequest>>> searchContest(@Body SearchContestNameRequest
                                                                                     searchContestNameRequest);


    /**
     * 获取个人资料
     */
    @GET("user/show")
    Call<APIResponse<PersonInformationResponse>> personalInformation(@Query("phone") String phone);


    /**
     * 提交修改资料(需要上传头像)
     *
     * @return
     */
    @Multipart
    @POST("user/edit")
    Call<APIResponse> uploadPersonInformation(@QueryMap Map<String, Object> map, @Part MultipartBody.Part pic);

    /**
     * 提交修改资料(不需要上传头像)
     *
     * @return
     */
    @POST("user/edit")
    Call<APIResponse> uploadPersonInformation(@QueryMap Map<String, Object> map);


    /**
     * 获取荣誉证书
     *
     * @param phone
     * @return
     */

    @GET("user/glory_show")
    Call<APIResponse<List<ShowHonorResponse>>> showHonorRequest(@Query("phone") String phone);


    /**
     * 添加证书
     *
     * @return
     */
    @Multipart
    @POST("user/glory_add")
    Call<APIResponse> addHonor(@Query("phone") String phone, @QueryMap Map<String, Object> map, @Part MultipartBody
            .Part glory_pic);


    /**
     * 编辑证书
     *
     * @return
     */
    @Multipart
    @POST("user/glory_edit")
    Call<APIResponse> uploadHonor(@Query("phone") String phone, @QueryMap Map<String, Object> map, @Part
            MultipartBody.Part glory_pic);

    /**
     * 编辑证书（不用照片）
     *
     * @return
     */
    @POST("user/glory_edit")
    Call<APIResponse> uploadHonor(@Query("phone") String phone, @QueryMap Map<String, Object> map);

    /**
     * 删除荣誉墙
     *
     * @param phone
     * @param order
     * @return
     */
    @DELETE("user/glory_del")
    Call<APIResponse> deleteHonor(@Query("phone") String phone, @Query("order") int order);


    /**
     * 提交意见反馈
     *
     * @return
     */
    @POST("feedback")
    Call<APIResponse> feedBack(@Body FeedBackRequest feedBackRequest);


    /**
     * 我的队伍列表
     *
     * @return
     */
    @GET("user/team_list")
    Call<APIResponse<List<MineTeamListResponse>>> mineTeamList(@Query("phone") String phone);

    /**
     * 我的队伍信息
     */
    @GET("team/show")
    Call<APIResponse<MineTeamInformationRequest>> mineTeamInformation(@Query("team_id") int team_id);

    /**
     * 我的队伍成员
     *
     * @return
     */
    @GET("team/member_show")
    Call<APIResponse<List<TeamMemberResponse>>> mineTeamMember(@Query("team_id") int team_id);


    /**
     * 添加队伍成员
     *
     * @return
     */
    @POST("team/member_add")
    Call<APIResponse> addTeamMember(@Body AddTeamMemberRequest request);


    /**
     * 修改我的队伍信息
     *
     * @return
     */
    @POST("team/edit")
    Call<APIResponse> updateTeamInformation(@Body UpdateTeamInformationRequest updateTeamInformationRequest);


    /**
     * 获取默认年级选项
     *
     * @return
     */
    @GET("other/year_show")
    Call<APIResponse<List<StudentGradeRequest>>> studentGradeRequest();


    /**
     * 获取默认擅长领域
     *
     * @return
     */
    @GET("other/field_show")
    Call<APIResponse<List<GoodAtRequest>>> goodAtRequest();


    /**
     * 加载用户好友列表
     *
     * @param phone
     * @return
     */
    @GET("friend/show")
    Call<APIResponse<List<FriendModel>>> loadFriendList(@Query("phone") String phone);


    /**
     * 添加好友接口
     *
     * @param request
     * @return
     */
    @POST("friend/add")
    Call<APIResponse> addFriend(@Body AddFriendRequest request);


    /**
     * 删除好友接口
     *
     * @param phone
     * @param friendPhone
     * @return
     */
    @DELETE("friend/del")
    Call<APIResponse> deleteFriend(@Query("phone") String phone, @Query("friend_phone") String friendPhone);


    /**
     * 搜索用户
     *
     * @param content
     * @return
     */
    @GET("chat/search")
    Call<APIResponse<List<SearchUserModel>>> searchUser(@Query("content") String content);


    /**
     * 加载通知列表
     *
     * @param phoneNumber
     * @return
     */
    @GET("notice/get")
    Call<APIResponse<List<NoticeModel>>> loadNotice(@Query("recvNum") String phoneNumber);

    /**
     * 添加通知
     *
     * @param noticeModel
     * @return
     */
    @POST("notice/add")
    Call<APIResponse> addNotice(@Body NoticeModel noticeModel);


    /**
     * 删除通知
     *
     * @param noticeId
     * @return
     */
    @DELETE("notice/del")
    Call<APIResponse> deleteNotice(@Query("noticeId") String noticeId);

}
