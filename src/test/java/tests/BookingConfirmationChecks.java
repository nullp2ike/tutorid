package tests;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.BookingRequest;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.data_generation.Any;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.junit_extensions.PropertiesExtension;

@ExtendWith(PropertiesExtension.class)
public class BookingConfirmationChecks {

//    @Test
//    public void BookingConfirmation(){
//        Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
//        Student student = new StudentCreator(Any.studentUsername()).create();
//
//        BookingRequest bookingRequest = new BookingRequest().request()
//
//        Response bookingResponse = bookingRequest.asTutor(tutor, student);
//        System.out.println(bookingResponse.getBody().asString());
//
//        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
//        Response bookingConfirmationResponse = bookingRequest.confirmByStudent(tutor, student, bookingId);
//        System.out.println(bookingConfirmationResponse.getBody().asString());
//    }
//
//    @Test
//    public void TestBookingConfirmation2(){
//        Tutor tutor = new TutorCreator(Any.tutorUsername()).create();
//        Student student = new StudentCreator(Any.studentUsername()).create();
//        BookingRequest bookingRequest = new BookingRequest();
//
//        Response bookingResponse = bookingRequest.asTutor(tutor, student);
//        System.out.println(bookingResponse.getBody().asString());
//
//        int bookingId = new JSONObject(bookingResponse.getBody().asString()).getJSONArray("data").getJSONObject(0).getInt("id");
//        Response bookingConfirmationResponse = bookingRequest.confirmByStudent(tutor, student, bookingId);
//        System.out.println(bookingConfirmationResponse.getBody().asString());
//    }

}
