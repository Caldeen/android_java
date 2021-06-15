package services;

import java.util.UUID;

import data.classes.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserService {
    @POST("/register")
    Call<User> register(@Body User user);
    @POST("/login")
    Call<User> login(@Body User user);
    @GET("/logout")
    Call<String> logout(@Header("SESSION") UUID token);

}
