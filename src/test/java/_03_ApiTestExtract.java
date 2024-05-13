import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.*;

public class _03_ApiTestExtract {

    @Test
    public void extractingJsonPath() {
        String ulkeadi =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("country")// PATH i country olan değeri EXTRACT yap
                ;
        System.out.println("Ülke adı:" + ulkeadi);
        Assert.assertEquals(ulkeadi, "United States"); // alınan değer buna eşit mi

    }

    @Test
    public void extractingJsonPathSoru() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız

        String state =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].state");
        System.out.println(state);
        Assert.assertEquals(state, "California");

    }

    @Test
    public void extractingJsonPathSoru2() {
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu testNG Assertion ile doğrulayınız

        String placeName =
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .extract().path("places[0].'place name'");
        System.out.println(placeName);
        Assert.assertEquals(placeName, "Beverly Hills");

    }

    @Test
    public void extractingJsonPathSoru3() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // limit bilgisinin 10 olduğunu testNG ile doğrulayınız.

        Integer limit =
                given()

                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        //.log().body() // veriyi görmek için yazıldı
                        .extract().path("meta.pagination.limit") //.toString() stringe çevirebilirsin String alacaksan
                ;
        //System.out.println(limit);
        Assert.assertTrue(limit == 10);

    }

    @Test
    public void extractingJsonPath5() {

        List<Integer> idler=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        .log().body()
                        .extract().path("data.id")  // id lerin yer aldığı bir array
                ;

        System.out.println("idler = " + idler);
    }
    @Test
    public void extractingJsonPathSoru4() {
        // Soru : "https://gorest.co.in/public/v1/users"  endpoint in den dönen
        // bütün name leri yazdırınız.

        List<Integer> names=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .extract().path("data.name")
                ;

        System.out.println("names = " + names);
    }

    @Test
    public void extractingJsonPathResponsAll() {

        Response donenData=    // var donenData=pm.Response.Json()
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .extract().response()
                ;

        List<Integer> idler=donenData.path("data.id");  // var id=donenData.id;
        List<String> names=donenData.path("data.name");
        int limit=donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("names = " + names);
        System.out.println("limit = " + limit);

        Assert.assertTrue( idler.contains(6880125));
        Assert.assertTrue( names.contains("Karunanidhi Jain"));
        Assert.assertTrue( limit == 10);
    }

}
