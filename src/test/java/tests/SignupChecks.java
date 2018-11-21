package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import utils.Setup;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.junit_extensions.PropertiesExtension;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PropertiesExtension.class)
public class SignupChecks {

    @Test
    public void Signup_Student_Success(){
        Student student = new StudentCreator().create();

        Response response = given().filter(student.getCookieFilter())
                .get(Setup.usersUrl + "/" + student.getNickname());
        response.then().statusCode(HttpStatus.SC_OK);

        JSONObject playerProfile = new JSONObject(response.body().asString()).getJSONObject("data");
        assertThat(playerProfile.getBoolean("isTutor")).isEqualTo(false);
        assertThat(playerProfile.getString("email")).isEqualTo("tutoridtest+" + student.getNickname() + "@gmail.com");
    }

    @Test
    public void Signup_Tutor_Success(){
        Tutor tutor = new TutorCreator().create();

        Response response = given().filter(tutor.getCookieFilter())
                .get(Setup.usersUrl + "/" + tutor.getNickname());

        response.then().statusCode(HttpStatus.SC_OK);
        JSONObject playerProfile = new JSONObject(response.body().asString()).getJSONObject("data");

        assertThat(playerProfile.getBoolean("isTutor")).isEqualTo(true);
        assertThat(playerProfile.getString("email")).isEqualTo("tutoridtest+" + tutor.getNickname() + "@gmail.com");
    }

}
