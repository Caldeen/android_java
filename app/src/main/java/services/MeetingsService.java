package services;

import java.util.List;
import java.util.UUID;

import data.classes.Meeting;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface MeetingsService {
    @GET("/meetings")
    Call<List<Meeting>> getMeetings(@Header("SESSION") UUID token);
    @DELETE("/delete-meeting/{meetingId}")
    Call<String> deleteMeeting(@Header("SESSION") UUID token, @Path(value = "meetingId") int meetingId);
}
