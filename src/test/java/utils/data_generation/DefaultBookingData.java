package utils.data_generation;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.builders.BookingRequestBuilder;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DefaultBookingData {

    public static BookingRequestBuilder bookedByStudent(){
        BookingRequestBuilder builder = setupDefaultValues();
        return builder;
    }

    public static BookingRequestBuilder tutorScheduled(String studentNickname){
        BookingRequestBuilder builder = setupDefaultValues();
        Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1,ChronoUnit.DAYS);
        String startDateTime = tomorrow.toString();
        String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        JSONObject location = new JSONObject()
                .put("id", 7011)
                .put("type","publicLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("connectedCount", 1)
                .put("concealed", false);
        builder.location(location);
        builder.locationId("7011");
        builder.price(20);
        builder.paymentMethod("cash");
        builder.recurrenceRule("");
        builder.userInfo(new JSONObject().put("nickname", studentNickname));
        return builder;
    }

    public static BookingRequestBuilder studentViaTimeSlot(){
        BookingRequestBuilder builder = new BookingRequestBuilder();
        //TODO
        return builder;
    }

    private static BookingRequestBuilder setupDefaultValues(){
        BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        return builder;
    }
}
