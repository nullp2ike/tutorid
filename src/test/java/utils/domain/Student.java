package utils.domain;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.json.JSONObject;
import requests.Login;
import requests.Logout;
import requests.Profile;
import utils.Setup;

public class Student {

    private CookieFilter cookieFilter;
    private String nickname;
    private String email;
    private String firstName;
    private String lastName;

    public Student(Response response, CookieFilter cookieFilter){
        JSONObject responseObject = new JSONObject(response.body().asString()).getJSONObject("data");
        nickname = responseObject.getString("tutorId");
        email = responseObject.getString("email");
        firstName = responseObject.getString("firstName");
        lastName = responseObject.getString("lastName");
        this.cookieFilter = cookieFilter;
    }

    public CookieFilter getCookieFilter() {
        return cookieFilter;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public JSONObject profile(){
        return new JSONObject(new Profile().get(nickname, cookieFilter).getBody().asString());
    }

    public Student login(){
        return new Login().student(nickname, Setup.defaultPassword);
    }

    public Response logout(){
        return new Logout().logout(cookieFilter);
    }
}
