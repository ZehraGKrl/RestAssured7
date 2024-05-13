import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class _01_ApiTest {
    @Test
    public void Test1(){

        given()

                // hazırlık kodları buraya yazılıyor

                .when()
                // endpoint(url), metoduyla birlikte istek gönderilme aşaması
                .then();
                // assertion, test data işlemleri

    }
    @Test
    public void statusCodeTest(){
        given()
                // gönderilecek bilgiler burada
                .when()
                .get("http://api.zippopotam.us/us/90210") // post, put, delete

                .then()
                .log().body() // gelen body kısmını gönder
                .statusCode(200) // test kısmı assertion, 200 mü diye sorar
        ;
    }

    @Test
    public void contentTypeTest(){
        given()
                // gönderilecek bilgiler burada
                .when()
                .get("http://api.zippopotam.us/us/90210") // post, put, delete

                .then()
                .log().body() // gelen body kısmını gönder
                .statusCode(200) // test kısmı assertion, 200 mü diye sorar
                .contentType(ContentType.JSON) // dönen datanın tipi JSON mı
        ;
    }

    @Test
    public void checkCountryInResponseBody(){
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("country", equalTo("United States")) // country yi dışarı almadan
                // bulundu yeri (path i) vererek içerde assertion hamcrest kütüphanesi yapıyor
                ;
    }
    @Test
    public void checkCountryInResponseBody2(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint indne dönen
        // place dizisinin ilk elemanının state değerinin  "California"
        // olduğunu doğrulayınız

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .statusCode(200)
                .body("places[0].state", equalTo("California"))
        ;


    }
    @Test
    public void checkHasItem(){
        // Soru : "http://api.zippopotam.us/tr/01000"  endpoint in dönen
        // place dizisinin herhangi bir elemanında  "Dörtağaç Köyü" değerinin
        // olduğunu doğrulayınız

        given()
                .when()
                .get("http://api.zippopotam.us/tr/01000")

                .then()
                .log().body()
                .body("places.'place name'", hasItem("Dörtağaç Köyü"))  //places place name kerin içinde  Dörtağaç Köyü var mı?
        ;
    }
    @Test
    public void bodyArrayHasSizeTest(){
        // Soru : "http://api.zippopotam.us/us/90210"  endpoint in dönen
        // place dizisinin dizi uzunluğunun 1 olduğunu doğrulayınız.

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .body("places", hasSize(1))  //places eleman uzunluğu 1 mi?
        ;
    }
    @Test
    public void combiningTest(){

        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .body("places", hasSize(1))  //places eleman uzunluğu 1 mi?
                .body("places[0].state", equalTo("California"))
                .body("places.'place name'", hasItem("Beverly Hills"))
        ;
    }
    @Test
    public void pathParamTest(){
        given() // gönderilecek hazırlıklar
                .pathParam("ulke","us")
                .pathParam("postaKodu", 90210)
                .log().uri() // request linkini göndermeden önce görebilirsin

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKodu}")

                .then()
                .log().body()
        ;
    }

    @Test
    public void queryParamTest(){
        given()

                .param("page",1)
                .log().uri()

                .when()
                .get("https://gorest.co.in/public/v1/users")// https://gorest.co.in/public/v1/users?page=1
                //.get("https://gorest.co.in/public/v1/users?page=3")

                .then()
                .log().body()
        ;
    }
    @Test
    public void queryParamTest2() {
        // https://gorest.co.in/public/v1/users?page=3
        // bu linkteki 1 den 10 kadar sayfaları çağırdığınızda response daki donen page degerlerinin
        // çağrılan page nosu ile aynı olup olmadığını kontrol ediniz.
        for (int i = 1; i <= 10; i++) {

            given()
                    .param("page",i)
                    .log().uri()

                    .when()
                    .get("https://gorest.co.in/public/v1/users")

                    .then()
                    .body("meta.pagination.page", equalTo(i))
            ;
        }
    }

}
