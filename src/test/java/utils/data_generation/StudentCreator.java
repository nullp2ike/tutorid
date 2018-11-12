package utils.data_generation;

import io.restassured.response.Response;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import requests.Profile;
import requests.Signup;
import utils.domain.Student;

public class StudentCreator {

    final static Log logger = LogFactory.getLog(StudentCreator.class);

    private Student student;

    public StudentCreator(){
        this(Any.studentUsername());
    }

    public StudentCreator(String nickname){
        Signup signup = new Signup(nickname);
        signup.step1().then().statusCode(HttpStatus.SC_OK);
        signup.step2(false).then().statusCode(HttpStatus.SC_OK);
        signup.step3().then().statusCode(HttpStatus.SC_OK);
        signup.step4().then().statusCode(HttpStatus.SC_OK);
        signup.interests().then().statusCode(HttpStatus.SC_OK);
        signup.step5().then().statusCode(HttpStatus.SC_OK);

        Response profileResponse = new Profile().get(nickname, signup.getCookieFilter());
        profileResponse.then().statusCode(HttpStatus.SC_OK);
        this.student = new Student(profileResponse, signup.getCookieFilter());
        logger.info("Created student with nickname: " + student.getNickname());
    }

    public Student create(){
        return student;
    }

}
