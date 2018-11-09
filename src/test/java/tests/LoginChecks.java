package tests;

import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import requests.Invite;
import requests.Login;
import requests.Logout;
import utils.*;
import utils.data_generation.StudentCreator;
import utils.data_generation.TutorCreator;
import utils.data_generation.Any;
import utils.domain.Student;
import utils.domain.Tutor;
import utils.junit_extensions.PropertiesExtension;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PropertiesExtension.class)
public class LoginChecks {

    @Test
    public void Tutor_Logout_LogInAgain_Success(){
        Tutor tutor = new TutorCreator().create();
        JSONObject profile = tutor.profile();
        assertThat(profile.getJSONObject("data").getString("email")).isEqualTo(tutor.getEmail());

        tutor.logout();

        profile = tutor.profile();
        assertThat(profile.getJSONObject("data").get("email")).isEqualTo(null);

        assertTutorIsLoggedIn(tutor.login());
    }

    private void assertTutorIsLoggedIn(Tutor tutor) {
        assertThat(tutor.profile().getJSONObject("data").getString("email")).isEqualTo(tutor.getEmail());
    }

    @Test
    public void Student_Logout_LogInAgain_Success(){
        Student student = new StudentCreator().create();
        JSONObject profile = student.profile();
        assertThat(profile.getJSONObject("data").getString("email")).isEqualTo(student.getEmail());

        student.logout();

        profile = student.profile();
        assertThat(profile.isNull("data")).isTrue();

        assertStudentIsLoggedIn(student.login());
    }

    private void assertStudentIsLoggedIn(Student student) {
        assertThat(student.profile().getJSONObject("data").getString("email")).isEqualTo(student.getEmail());
    }

    @Test
    public void LoginStudent_InCorrectCredentials_UnableToLogin(){
        Student student = new StudentCreator().create();
        JSONObject profile = student.profile();
        assertThat(profile.getJSONObject("data").getString("email")).isEqualTo(student.getEmail());

        student.logout();

        Response response = new Login().failedLogin(student.getNickname(), "somewrongpassword");
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SC_UNAUTHORIZED);

        profile = student.profile();
        assertThat(profile.isNull("data")).isTrue();
    }

}
