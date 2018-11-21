package requests;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Setup;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.builders.BookingRequestBuilder;

import static io.restassured.RestAssured.given;

public class BookingRequest {

    public static final String CALENDAR_BOOKINGS_EVENTS = "/calendar/bookings/events";
    private String bookingRequestBody;

    public BookingRequest(){

    }

    public BookingRequest(BookingRequestBuilder builder){
        bookingRequestBody = builder.build();
    }

    public Response request(CookieFilter requesterCookies, String tutorNickname){
        Response response = given().filter(requesterCookies)
                .contentType(ContentType.JSON)
                .body(bookingRequestBody)
                .when()
                .post(Setup.usersUrl + "/" + tutorNickname + CALENDAR_BOOKINGS_EVENTS);
        return response;
    }

    public Response asTutor(Tutor requestingTutor, Student student){
        Response response = given().filter(requestingTutor.getCookieFilter())
                .contentType(ContentType.JSON)
                .body(bookingRequestBody)
                .when()
                .post(Setup.usersUrl + "/" + requestingTutor.getNickname() + CALENDAR_BOOKINGS_EVENTS);
        return response;
    }

    public Response asStudent(Tutor tutor, Student student){
        Response response = given().filter(student.getCookieFilter())
                .contentType(ContentType.JSON)
                .body(bookingRequestBody)
                .when()
                .post(Setup.usersUrl + "/" + tutor.getNickname() + CALENDAR_BOOKINGS_EVENTS);
        return response;

    }

    public Response decline(CookieFilter cookieFilter, String nickname, int eventId){
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .body("{\"cancelAll\":false,\"justification\":\"Booking decline justification\"}")
                .when()
                .post(Setup.usersUrl + "/" + nickname + "/calendar/bookings/events/" + eventId + "/decline");
        return response;
    }

    public Response confirmDecline(CookieFilter cookieFilter, String nickname, int eventId){
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .when()
                .post(Setup.usersUrl + "/" + nickname + "/calendar/bookings/events/" + eventId + "/confirmDeclination");
        return response;
    }

    public Response confirm(CookieFilter cookieFilter, String nickname, int bookingId){
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .when()
                .post(Setup.usersUrl + "/" + nickname + "/calendar/bookings/events/" + bookingId + "/confirm");
        return response;
    }

    public Response reschedule(CookieFilter cookieFilter, String nickname, int bookingId){
        Response response = given().filter(cookieFilter)
                .contentType(ContentType.JSON)
                .when()
                .post(Setup.usersUrl + "/" + nickname + "/calendar/bookings/events/" + bookingId + "/reschedule");
        return response;
    }

    //https://apis.tutor.id/users/jackjack/calendar/bookings/events/8016/reschedule
}
