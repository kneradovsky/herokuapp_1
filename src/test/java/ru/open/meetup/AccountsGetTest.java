package ru.open.meetup;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayWithSize;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertTrue;

import io.restassured.RestAssured;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vk on 19.06.17.
 */
public class AccountsGetTest {
  private String baseUrl = hostUrl+"apitest";
  private static String hostUrl = System.getProperty("AppBaseURL","https://kn-ktapp.herokuapp.com/");
  RequestSpecification req = given()
      .filters(new ErrorLoggingFilter());

  @BeforeClass
  public static void beforeClass() {
    get(hostUrl+"kot/resetstorage")
        .then()
        .statusCode(200);
  }

  @Test
  public void testList() {
    JsonPath response = req.get(baseUrl+"/accounts")
        .then()
        .statusCode(200)
        .body("",hasSize(6))
        .extract().jsonPath();
    List<Map<String,Object>> elems = response.getList("");
    boolean elemsFound = elems.stream().map(m -> ((Integer)m.get("account_id"))).filter(i -> i==12345678 || i==12345679).findAny().isPresent();
    assertTrue("required elements not found",elemsFound);
  }
  @Test
  public void testAccount() {
    JsonPath response = req.get(baseUrl+"/accounts/12345678")
        .then()
        .statusCode(200)
        .extract().jsonPath();
    Map<String,Object> elems = response.getMap("");
    assertThat("element has wrong id",elems.get("account_id"),equalTo(12345678));
    assertThat("element has wrong title",elems.get("title"),equalTo("Master1"));
    assertThat("element has wrong currency",elems.get("currency"),equalTo("RUR"));
  }

  @Test
  public void testNotFoundAccount() {
    req.get(baseUrl+"/accounts/22345678")
        .then()
        .statusCode(404);
  }
  @Test
  public void testNotFoundPath() {
    req.get(baseUrl+"/accounts2/22345678")
        .then()
        .statusCode(404);
  }
}
