package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;
import static org.hamcrest.Matchers.*;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class _06_CountryTest {
    RequestSpecification reqSpec;
    Faker randomUreteci=new Faker();
    String countryName = "";
    String countryCode = "";
    String countryID="";

    @BeforeClass
    public void LoginCampus() {
        baseURI = "https://test.mersys.io";
        //token cookies içinde geldi
        // cookies i alıcam
        // request spec hazırlayacağım
        // environment varible : baseURI
        //{"username": "turkeyts", "password": "TechnoStudy123","rememberMe": "true"}

        String userCredentials = "{\"username\": \"turkeyts\", \"password\": \"TechnoStudy123\",\"rememberMe\": \"true\"}";
        Map<String, String> userCredMap = new HashMap<>();
        userCredMap.put("username", "turkeyts");
        userCredMap.put("password", "TechnoStudy123");
        userCredMap.put("rememberMe", "true");

        Cookies gelenCookie =
                given()
                        //.body(userCredentials)
                        .body(userCredMap)
                        .contentType(ContentType.JSON)

                        .when()
                        .post("/auth/login")

                        .then()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();
        reqSpec = new RequestSpecBuilder()
                .addCookies(gelenCookie)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void CreateCountry() {
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım :spec

        countryName = randomUreteci.address().country() + randomUreteci.address().countryCode()
                + randomUreteci.address().latitude();
        countryCode = randomUreteci.address().countryCode();

        Map<String, String> newCountry = new HashMap<>();
        newCountry.put("name", countryName);
        newCountry.put("code", countryCode);

        //Not: Spec bilgileri givendan hemen sonra yazılmalı!
        countryID=
        given()
                .spec(reqSpec)  // gelen cookies, yeni istek için login olduğumun kanıtı olarak gönderildi.
                .body(newCountry)
                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;

    }

    @Test(dependsOnMethods = "CreateCountry")
    public void CreateCountryNegative() {
        // Yukarıda gönderilen name ve codu tekrar göndererek kayıt yapılamadığını doğrulayınız.
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım:spec

        Map<String , String > countryNegative= new HashMap<>();
        countryNegative.put("name",countryName);
        countryNegative.put("code",countryCode);

        given()
                .spec(reqSpec)
                .body(countryNegative)
                .when()
                .post("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
        ;
    }

    @Test
    public void UpdateCountry() {
        //burada gelen tooken ın yine cookies içinde geri gitmesi lazım:spec

        // yukarıda create edilen ülkenin adını bir başka ülke adı ile update ediniz.

        // benim id ye ihtiyacım var, id nerde oluşuyor
        String updCountryName="Zehra"+randomUreteci.address().country()+
                randomUreteci.address().latitude();

        Map<String, String> updCountry = new HashMap<>();
        updCountry.put("id", countryID);
        updCountry.put("name", updCountryName);
        updCountry.put("code", countryCode);

        given()
                .spec(reqSpec)
                .body(updCountry)
                .when()
                .put("/school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(updCountryName)) //gönderdiğiniz ülke adının , dönen body deki ülke adıyla aynı mı
                ;

    }

    // Delete Country i Testini yapınız.

    @Test(dependsOnMethods = "UpdateCountry")
    public  void  DeleteCountry()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .statusCode(200)
        ;

    }

    // Delete Country Negatif testini yapınız
    @Test(dependsOnMethods = "DeleteCountry")
    public  void  DeleteCountryNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/school-service/api/countries/"+countryID)

                .then()
                .log().all()
                .statusCode(400)
        ;

    }
    // TODO : CitizenShip in API testini yapınız
}
