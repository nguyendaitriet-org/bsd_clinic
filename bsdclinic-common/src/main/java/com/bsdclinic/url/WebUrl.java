package com.bsdclinic.url;

public class WebUrl {
    public static final String CLIENT_HOME = "/";
    public static final String ADMIN_HOME = "/admin";
    public static final String ADMIN_USERS_INDEX = "/admin/users";
    public static final String ADMIN_USERS_PROFILE = "/admin/users/profile";
    public static final String ADMIN_APPOINTMENT_INDEX = "/admin/appointments";
    public static final String ADMIN_APPOINTMENT_CREATE = "/admin/appointments/create";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    public static final String API_ADMIN_ENDPOINT = "/api/admin";
    public static final String API_CLIENT_ENDPOINT = "/api/client";

    // Admin - User API
    public static final String API_ADMIN_USER_ENDPOINT = API_ADMIN_ENDPOINT + "/users";
    public static final String API_ADMIN_USER_LIST = API_ADMIN_USER_ENDPOINT + "/list";
    public static final String API_ADMIN_USER_AVATAR = API_ADMIN_USER_ENDPOINT + "/avatar";
    public static final String API_ADMIN_USER_PROFILE = API_ADMIN_USER_ENDPOINT + "/profile";
    public static final String API_ADMIN_USER_CHANGE_PASSWORD = API_ADMIN_USER_ENDPOINT+ "/change-password";

    // Admin - Appointment API
    public static final String API_ADMIN_APPOINTMENT = API_ADMIN_ENDPOINT + "/appointments";
}
