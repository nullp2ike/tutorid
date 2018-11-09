package utils.domain;

import io.restassured.filter.cookie.CookieFilter;
import io.restassured.response.Response;
import org.json.JSONObject;
import requests.Login;
import requests.Logout;
import requests.Profile;
import utils.Setup;

public class Tutor {

    private CookieFilter cookieFilter;
    private String nickname;
    private String email;

    public Tutor(Response response, CookieFilter cookieFilter){
        JSONObject responseObject = new JSONObject(response.body().asString()).getJSONObject("data");
        nickname = responseObject.getString("tutorId");
        email = responseObject.getString("email");
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

    public JSONObject profile(){
        return new JSONObject(new Profile().get(nickname, cookieFilter).getBody().asString());
    }

    public Tutor login(){
        return new Login().tutor(nickname, Setup.defaultPassword);
    }

    public Response logout(){
        return new Logout().logout(cookieFilter);
    }
}
