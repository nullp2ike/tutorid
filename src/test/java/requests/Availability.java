package requests;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import groovy.json.internal.Chr;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.Setup;
import utils.domain.Tutor;

import static io.restassured.RestAssured.given;

public class Availability {

    private String startDateTime;
    private String endDateTime;
    private final JSONObject availability;

    public Availability(){
        this.availability = new JSONObject();
    }

    public Availability tomorrowFromNow1H(){
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(1, ChronoUnit.DAYS);
        this.availability.put("startDateTime", tomorrow.toString());
        this.availability.put("endDateTime", tomorrow.plus(1, ChronoUnit.HOURS).toString());
        return this;
    }

    public Availability tomorrowFromNow2H(){
        final Instant tomorrow = Instant.now().truncatedTo(ChronoUnit.HOURS).plus(2, ChronoUnit.DAYS);
        this.availability.put("startDateTime", tomorrow.toString());
        this.availability.put("endDateTime", tomorrow.plus(2, ChronoUnit.HOURS).toString());
        return this;
    }

    public Response add(final Tutor tutor){
        this.availability.put("discount", 0);
        this.availability.put("privateLocationIds",new JSONArray());
        this.availability.put("publicLocationIds", new JSONArray().put(tutor.getPublicLocations().get(0).getId()));
        this.availability.put("recurrenceRule","RRULE:FREQ=DAILY;COUNT=1");
        this.availability.put("studentsPlaceAllowed", false);
        this.availability.put("onlineLocationsAllowed", false);

        final Response response = given().filter(tutor.getCookieFilter())
                                         .contentType(ContentType.JSON)
                                         .body(this.availability.toString())
                                         .when()
                                         .post(Setup.usersUrl + "/" + tutor.getNickname() + "/calendar/availability/events");
        response.then().statusCode(HttpStatus.SC_OK);
        return response;
    }

}
