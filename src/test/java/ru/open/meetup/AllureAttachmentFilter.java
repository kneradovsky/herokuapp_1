package ru.open.meetup;


import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestLogSpecification;
import io.restassured.specification.RequestSpecification;
import ru.yandex.qatools.allure.annotations.Attachment;

/**
 * Created by vk on 20.06.17.
 */
public class AllureAttachmentFilter implements Filter {

  @Override
  public Response filter(FilterableRequestSpecification requestSpec,
      FilterableResponseSpecification responseSpec, FilterContext ctx) {
      attach_request(requestSpec);
      Response resp = ctx.next(requestSpec,responseSpec);
      attach_response(resp);
      return resp;
  }


  @Attachment("request")
  public byte[] attach_request(RequestSpecification reqSpec) {
    return reqSpec.toString().getBytes();
  }

  @Attachment("response")
  public byte[] attach_response(Response resp) {
    return resp.asByteArray();
  }

}
