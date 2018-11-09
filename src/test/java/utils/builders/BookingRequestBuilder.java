package utils.builders;

import org.json.JSONArray;
import org.json.JSONObject;

public class BookingRequestBuilder {

    private JSONObject bookingRequest = new JSONObject();

    public BookingRequestBuilder additionalInfo(String info){
        bookingRequest.put("additionalInformation", info);
        return this;
    }

    public BookingRequestBuilder events(JSONArray events){
        bookingRequest.put("bookingEvents", events);
        return this;
    }

    public BookingRequestBuilder level(String level){
        bookingRequest.put("level", level);
        return this;
    }

    public BookingRequestBuilder location(JSONObject location){
        bookingRequest.put("location", location);
        return this;
    }

    public BookingRequestBuilder locationId(String locationId){
        bookingRequest.put("locationId", locationId);
        return this;
    }

    public BookingRequestBuilder price(int price){
        bookingRequest.put("price", price);
        return this;
    }

    public BookingRequestBuilder paymentMethod(String paymentMethod){
        bookingRequest.put("paymentMethod", paymentMethod);
        return this;
    }

    public BookingRequestBuilder recurrenceRule(String recurrenceRule){
        bookingRequest.put("recurrenceRule", recurrenceRule);
        return this;
    }

    public BookingRequestBuilder subjectId(int subjectId){
        bookingRequest.put("subjectId", subjectId);
        return this;
    }

    public BookingRequestBuilder zoneId(String zoneId){
        bookingRequest.put("zoneId", zoneId);
        return this;
    }

    public BookingRequestBuilder userInfo(JSONObject userInfo){
        bookingRequest.put("userInfo", userInfo);
        return this;
    }

    public String build(){
        return bookingRequest.toString();
    }
}
