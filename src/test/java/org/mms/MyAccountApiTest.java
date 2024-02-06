package org.mms;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MyAccountApiTest {

	String client_id = System.getProperty("client_id");
	String client_secret = System.getProperty("client_secret");

	@Test
	public void getCustomerByEmail() {

		String emailAddress = "testuser@nejmemail.com";
		String url = "https://api.nejm-dev.org/my-account/v1/api/customers?email=" + emailAddress;

		String body = RestAssured.given()
				.header("client_id", client_id)
				.header("client_secret", client_secret)
				.when()
				.get(url)
				.then()
				.statusCode(200)
				.and()
				.extract().body().asString();

		System.out.println(body);
	}

	@Test
	public void getCustomerByEmail2() {

		String emailAddress = "testuser@nejmemail.com";
		String url = "https://api.nejm-dev.org/my-account/v1/api/customers";

		RequestSpecification request = RestAssured.given();
		request.header("client_id", client_id);
		request.header("client_secret", client_secret);
		request.queryParam("email", emailAddress);

		Response response = request.get(url);
		int statusCode = response.getStatusCode();

		assertEquals(statusCode, 200);

		String body = response.getBody().asString();
		assertNotNull(body);

		System.out.println(body);
	}
}
