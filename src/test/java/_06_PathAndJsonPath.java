import Model.Location;
import Model.Places;
import Model.UserData;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _06_PathAndJsonPath {

    @Test
    public void extractingPath() // klasik yöntem
    {
        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("")     ,   as(Todo.Class)

        String postCode=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().path("'post code'")
                ;
        System.out.println("postCode = " + postCode);
        int postCodeInt= Integer.parseInt(postCode);
        System.out.println("postCodeInt = " + postCodeInt);
    }
    @Test
    public void extractingJosPath()
    {
        // gelen body de bilgiyi dışarı almanın 2 yöntemini gördük
        // .extract.path("")     ,   as(Todo.Class)

        int postCode=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        .log().body()
                        .extract().jsonPath().getInt("'post code'")
                // tip dönüşümü otomatik, uygun tip verilmeli
                ;
        System.out.println("postCode = " + postCode);
    }
    @Test
    public void getZipCode(){
        Response response=
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .log().body()
                        .extract().response()
                ;
        Location locationAsPath = response.as(Location.class); // Bütün class yapısını yazmak zorundayız
        System.out.println("locationAsPath.getPlaces() = " + locationAsPath.getPlaces());
        // bana sadece place dizisi lazım olsa bile, bütün diğer class ları yazmak zorundayım

        List<Places> places= response.jsonPath().getList("place", Places.class);
        // Sadece Place dizisi lazım ise diğerlerini yazmak zorunda değilsin.

        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }

    @Test
    public void users() {
    /*
    // https://gorest.co.in/public/v1/users  endpointte dönen Sadece Data Kısmını POJO
    // dönüşümü ile alarak yazdırınız.
     */
        Response response =
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")
                        .then()
                        //.log().body()
                        .extract().response();
        List<UserData> userDataList = response.jsonPath().getList("data", UserData.class);

        for (UserData userData : userDataList) {
            System.out.println(userData);
        }
    }
}
