import Model.Todo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class _05_Tasks {
    /**
     * Task 1
     * create a request to https://jsonplaceholder.typicode.com/todos/2
     * expect status 200
     * expect content type JSON
     * expect title in response body to be "quis ut nam facilis et officia qui"
     */


    @Test
    public void Task1() {

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("title", equalTo("quis ut nam facilis et officia qui"))
        ;


    }

    @Test
    public void Task2a() {
        /*
        Task 2
        create a request to https://jsonplaceholder.typicode.com/todos/2
        expect status 200
        expect content type JSON
        *a) expect response completed status to be false(hamcrest)

        */

        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("completed", equalTo(false))
        ;

    }

    @Test
    public void Task2b() {
        /*
         *b) extract completed field and testNG assertion(testNG)
         */

        Boolean completedFalse =
                given()
                        .when()
                        .get("https://jsonplaceholder.typicode.com/todos/2")

                        .then()
                        .log().body()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract().path("completed");

        Assert.assertFalse(completedFalse, "false");
    }

    @Test
    public void Task3() {
        /** Task 3
         create a request to https://jsonplaceholder.typicode.com/todos/2
         expect status 200
         Converting Into POJO (Plain Old Java Object)
         */
        Todo todo =
        given()
                .when()
                .get("https://jsonplaceholder.typicode.com/todos/2")
                .then()
                .log().body()
                .statusCode(200)
                .extract().body().as(Todo.class);
                ;


        System.out.println("ID: " + todo.getId());
        System.out.println("Title: " + todo.getTitle());
        System.out.println("Completed: " + todo.isCompleted());

    }

}
