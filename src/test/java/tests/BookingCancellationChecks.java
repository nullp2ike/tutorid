package tests;

import io.restassured.response.Response;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@ExtendWith(PropertiesExtension.class)
public class BookingCancellationChecks {

    @Test
    public void Cancelling_Booking_By_Tutor_Success(){
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

        // Cancelling booking from tutor
        final Response declineResponse = new BookingRequest(builder).decline(tutor.getCookieFilter(),tutor.getNickname(),bookingId);

        // Formating JSON
        JSONObject declineResponseJson = new JSONObject(declineResponse.getBody().asString());
        //System.out.println(declineResponseJson);
        String instances = (String) new JSONObject(declineResponseJson.getJSONObject("data")
                .toString())
                .getJSONArray("instances")
                .get(0)
                .toString();
        String bookingEventStatus = new JSONObject(instances).getString("bookingEventStatus");
        //System.out.println(bookingEventStatus);

        // Checking for success
        assertThat(bookingEventStatus.equals("cancelledByTutorPending"));
    }

    @Test
    public void Cancelling_Booking_By_Student(){
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

        // Cancelling booking from student  (A little strange to me that in request we use tutor nickname)
        final Response declineResponse = new BookingRequest(builder).decline(student.getCookieFilter(),tutor.getNickname(),bookingId);

        // Formating JSON
        JSONObject declineResponseJson = new JSONObject(declineResponse.getBody().asString());
        //System.out.println(declineResponseJson);
        String instances = (String) new JSONObject(declineResponseJson.getJSONObject("data")
                .toString())
                .getJSONArray("instances")
                .get(0)
                .toString();
        String bookingEventStatus = new JSONObject(instances).getString("bookingEventStatus");
        //System.out.println(bookingEventStatus);

        // Checking for success
        assertThat(bookingEventStatus.equals("cancelledByStudentPending"));
    }
}
