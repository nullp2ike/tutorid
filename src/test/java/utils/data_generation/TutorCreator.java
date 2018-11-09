package utils.data_generation;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import requests.Profile;
import requests.Signup;
import utils.domain.Tutor;

public class TutorCreator {

    private Tutor tutor;

    public TutorCreator(){
        this(Any.tutorUsername());
    }

    public TutorCreator(String nickname){
        Signup signup = new Signup(nickname);
        signup.step1().then().statusCode(HttpStatus.SC_OK);;
        signup.step2(true).then().statusCode(HttpStatus.SC_OK);;
        signup.step3().then().statusCode(HttpStatus.SC_OK);;
        signup.step4().then().statusCode(HttpStatus.SC_OK);;
        signup.subjects().then().statusCode(HttpStatus.SC_OK);;
        signup.step5().then().statusCode(HttpStatus.SC_OK);;
        signup.interests().then().statusCode(HttpStatus.SC_OK);;
        signup.locations1().then().statusCode(HttpStatus.SC_OK);;
        signup.locations2().then().statusCode(HttpStatus.SC_OK);;
        signup.locations3().then().statusCode(HttpStatus.SC_OK);;
        signup.step6().then().statusCode(HttpStatus.SC_OK);;
        Response profileResponse = new Profile().get(nickname, signup.getCookieFilter());
        profileResponse.then().statusCode(HttpStatus.SC_OK);
        this.tutor = new Tutor(profileResponse, signup.getCookieFilter());
    }

    public Tutor create(){
        return tutor;
    }

}
