package utils.data_generation;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.builders.BookingRequestBuilder;
import utils.domain.Student;
import utils.domain.Tutor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class DefaultBookingData {

    private JSONObject location;

    public BookingRequestBuilder tutorScheduled(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder tutorScheduledStudentLocation(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesStudentLocation(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder tutorScheduledTutorLocation(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesTutorLocation(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder tutorScheduledOnlineLocation(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesOnline(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }



    public BookingRequestBuilder tutorScheduledWithCard(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesWithCard(tutor);
        this.location.put("connectedCount", 1);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("nickname", student.getNickname()));
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlot(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValues(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlotStudentLocation(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesStudentLocation(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlotTutorLocation(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesTutorLocation(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    public BookingRequestBuilder bookedByStudentViaTimeSlotWithCard(final Tutor tutor, final Student student){
        final BookingRequestBuilder builder = setupDefaultValuesWithCard(tutor);
        builder.location(this.location);
        builder.userInfo(new JSONObject().put("email", student.getEmail())
                .put("firstName",student.getFirstName())
                .put("lastName",student.getLastName())
                .put("language","en")
                .put("nickname",student.getNickname())
        );
        return builder;
    }

    private BookingRequestBuilder setupDefaultValues(final Tutor tutor){
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("cash");
        setupDefaultPublicLocation(tutor);
        builder.locationId(String.valueOf(tutor.getPublicLocations().get(0).getId()));
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValuesStudentLocation(final Tutor tutor){
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("cash");
        setupDefaultStudentLocation(tutor);
        builder.locationId(String.valueOf(tutor.getPublicLocations().get(0).getId()));
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValuesTutorLocation(final Tutor tutor){
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("cash");
        setupDefaultTutorLocation(tutor);
        builder.locationId(String.valueOf(tutor.getPrivateLocations().get(0).getId()));
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValuesOnline(final Tutor tutor){
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("cash");
        setupOnlineLocation(tutor);
        builder.locationId(String.valueOf(tutor.getOnlineLocations().get(0).getId()));
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private BookingRequestBuilder setupDefaultValuesWithCard(final Tutor tutor){
        final BookingRequestBuilder builder = new BookingRequestBuilder();
        builder.additionalInfo("additional_info");
        builder.level("level1");
        builder.subjectId(6201);
        builder.zoneId("Europe/Tallinn");
        builder.recurrenceRule("");
        builder.price(20);
        builder.paymentMethod("creditCard");
        setupDefaultPublicLocation(tutor);
        builder.locationId(String.valueOf(tutor.getPublicLocations().get(0).getId()));
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        final String startDateTime = tomorrow.toString();
        final String endDateTime = tomorrow.plus(1, ChronoUnit.HOURS).toString();
        final JSONArray bookingEvents = new JSONArray().put(new JSONObject().put("startDateTime", startDateTime).put("endDateTime", endDateTime));
        builder.events(bookingEvents);

        return builder;
    }

    private void setupOnlineLocation(Tutor tutor) {
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type","onlineLocation")
                .put("label", "Skype")
                .put("name", "secretfish123")
                .put("discount", 0)
                .put("concealed", false);
    }

    private void setupDefaultPublicLocation(final Tutor tutor){
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type","publicLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("concealed", false);
    }

    private void setupDefaultStudentLocation(final Tutor tutor){
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type","studentLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("concealed", false);
    }

    private void setupDefaultTutorLocation(final Tutor tutor){
        this.location = new JSONObject()
                .put("id", tutor.getPublicLocations().get(0).getId())
                .put("type","tutorLocation")
                .put("address", "Pärnu maantee 27, 10141 Tallinn, Estonia")
                .put("latitude", 59.4307923)
                .put("longitude", 24.74567980000006)
                .put("label", "Cocoa")
                .put("discount", 0)
                .put("concealed", false);
    }
}
