package utils.domain;

import java.util.ArrayList;
import java.util.List;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import requests.Login;
import requests.Logout;
import requests.Profile;
import utils.Setup;

public class Tutor {

    private final CookieFilter cookieFilter;
    private final String nickname;
    private final String email;
    private final List publicLocations;

    public Tutor(final Response response, final CookieFilter cookieFilter){
        final JSONObject responseObject = new JSONObject(response.body().asString()).getJSONObject("data");
        this.nickname = responseObject.getString("tutorId");
        this.email = responseObject.getString("email");
        this.cookieFilter = cookieFilter;
        this.publicLocations = parseLocations(responseObject.getJSONArray("publicLocations"));
    }

    public CookieFilter getCookieFilter() {
        return this.cookieFilter;
    }

    public String getNickname() {
        return this.nickname;
    }

    public String getEmail() {
        return this.email;
    }

    public JSONObject profile(){
        return new JSONObject(new Profile().get(this.nickname, this.cookieFilter).getBody().asString());
    }

    public Tutor login(){
        return new Login().tutor(this.nickname, Setup.defaultPassword);
    }

    public Response logout(){
        return new Logout().logout(this.cookieFilter);
    }

    private List<Location> parseLocations(final JSONArray responseLocations){
        final List locations = new ArrayList<>();
        for (int i = 0; i <responseLocations.length(); i++) {
            final int id = responseLocations.getJSONObject(i).getInt("id");
            final String type = responseLocations.getJSONObject(i).getString("type");
            final Location location = new Location(id, type);
            locations.add(location);
        }
        return locations;
    }

    public List<Location> getPublicLocations() {
        return this.publicLocations;
    }
}
