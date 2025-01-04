package com.bank.assignment.routing;

public class ApiRoutes {

  public static final String API = "/api";
  public static final String VERSION = "/v1";

  public static final String BASE_URL = API + VERSION + "/accounts";
  public static final String GET_ACCOUNTS = BASE_URL;
  public static final String PATCH_DESCRIPTION = BASE_URL + "/{accountNumber}/description";
}
