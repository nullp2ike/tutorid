package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.Availability;
import requests.BookingRequest;
import utils.builders.BookingRequestBuilder;
import utils.data_generation.Any;
import utils.data_generation.DefaultBookingData;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.junit_extensions.PropertiesExtension;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(PropertiesExtension.class)
public class BookingConfirmationChecks {

    @Test
    public void Student_Booking_Request_To_Tutor_With_Confirmation_Success(){
        // Creating tutor and student
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        // Tutor creates availability
        final Response r = new Availability().tomorrowFromNow1H().add(tutor);
        r.then().body("description", equalTo("Availability created"));

        //Setup booking request data
        final BookingRequestBuilder builder = new DefaultBookingData().bookedByStudentViaTimeSlot(tutor, student);

        //Send booking request
        final Response response = new BookingRequest(builder).asStudent(tutor, student);
        //System.out.println(response.getBody().asString());
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        // Getting booking Id
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");

        // Confirming booking from tutor
        final Response confirmResponse = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        // Formating JSON
        JSONObject confirmResponseJson = new JSONObject(confirmResponse.getBody().asString());
        //System.out.println(confirmResponseJson);
        String instances = (String) new JSONObject(confirmResponseJson.getJSONObject("data")
                .toString())
                .getJSONArray("instances")
                .get(0)
                .toString();
        String bookingEventStatus = new JSONObject(instances).getString("bookingEventStatus");
        //System.out.println(bookingEventStatus);

        // Checking for success
        assertThat(bookingEventStatus.equals("confirmed"));
    }

    @Test
    public void Tutor_RequestsBookingOnBehalfOfStudent_StudentConfirms_Success(){
        final Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
        final Student student = new StudentCreator(Any.studentUsername()).create();

        final Response response = new BookingRequest(new DefaultBookingData().tutorScheduled(tutor, student))
                .asTutor(tutor, student);

        response.then()
                .statusCode(HttpStatus.SC_OK)
                .body("description", equalTo("Booking created"));
        final BookingRequestBuilder builder = new DefaultBookingData().tutorScheduled(tutor, student);
        JSONObject jsonObject = new JSONObject(response.getBody().asString());
        // Getting booking Id
        int bookingId = (int) new JSONObject(jsonObject.getJSONArray("data").get(0).toString()).get("id");

        // Confirming booking
        final Response confirmResponse = new BookingRequest(builder).confirm(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        // Formating JSON
        JSONObject confirmResponseJson = new JSONObject(confirmResponse.getBody().asString());

        String instances = (String) new JSONObject(confirmResponseJson.getJSONObject("data")
                .toString())
                .getJSONArray("instances")
                .get(0)
                .toString();
        String bookingEventStatus = new JSONObject(instances).getString("bookingEventStatus");

        // Checking for success
        assertThat(bookingEventStatus.equals("confirmed"));

    }




}
