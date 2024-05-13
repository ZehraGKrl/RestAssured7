package GoRest;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.*;

import java.beans.Transient;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class _07_GoRestUsersTest {
    // Token ı aldım ,
    // usersCreate için neler lazım, body(user bilgileri)
    // enpoint i aldım gidiş metodu
    // BeforeClass ın içinde yapılacaklar var mı? nelerdir ?  url set ve spec hazırlanmalı

    Faker randomUreteci=new Faker();
    RequestSpecification reqSpec;
    int userID=0;

    @BeforeClass
    public void Setup(){
        baseURI="https://gorest.co.in/public/v2/users";
        reqSpec= new RequestSpecBuilder()
                .addHeader("Authorization", "Bearer 25f6f12650de3a1c8d253277674e426ba80f3365e103328c8be0c2fab4f70362")
                .setContentType(ContentType.JSON)
                .build();
    }

    // Create User Testini yapınız (herkes kendi token ını kullanırsa iyi olur)

    @Test
    public void CreateUser(){

        String rndmFullName=randomUreteci.name().fullName();
        String rndmEmail=randomUreteci.internet().emailAddress();

        Map<String,String> newUser= new HashMap<>();
        newUser.put("name", rndmFullName);
        newUser.put("gender","female");
        newUser.put("email",rndmEmail);
        newUser.put("status","active");

        userID=
                given()
                        .spec(reqSpec)
                        .body(newUser)
                        .when()
                        .post("")// http ile başlamıyorsa BASEURI geçerli
                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().path("id")
                ;
    }

    // GetUserById testini yapınız
    @Test(dependsOnMethods ="CreateUser" )
    public void GetUserById()
    {

        given()
                .spec(reqSpec)

                .when()
                .get("/"+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
        ;
    }

    // UpdateUser testini yapınız
    @Test(dependsOnMethods = "GetUserById")
    public void UpdateUser()
    {
        String updName="Gul Koroglu";

        Map<String,String> updUser=new HashMap<>();
        updUser.put("name",updName);

        given()
                .spec(reqSpec)
                .body(updUser)

                .when()
                .put("/"+userID)

                .then()
                .log().body()
                .statusCode(200)
                .body("id", equalTo(userID))
                .body("name", equalTo(updName))
        ;
    }

    // DeleteUser testini yapınız
    @Test(dependsOnMethods = "UpdateUser")
    public void DeleteUser()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                .statusCode(204)
        ;
    }

    // DeleteUserNegative testini yapınız
    @Test(dependsOnMethods = "DeleteUser")
    public void DeleteUserNegative()
    {
        given()
                .spec(reqSpec)

                .when()
                .delete("/"+userID)

                .then()
                .statusCode(404)
        ;
    }

}
