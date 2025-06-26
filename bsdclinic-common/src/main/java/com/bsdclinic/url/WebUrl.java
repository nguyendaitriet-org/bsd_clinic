package com.bsdclinic.url;

public class WebUrl {
    // Page
    public static final String ADMIN_HOME = "/admin";
    public static final String ADMIN_USERS_INDEX = "/admin/users";
    public static final String ADMIN_USERS_PROFILE = "/admin/users/profile";
    public static final String ADMIN_APPOINTMENT_INDEX = "/admin/appointments";
    public static final String ADMIN_APPOINTMENT_CREATE = "/admin/appointments/create";
    public static final String ADMIN_APPOINTMENT_FOR_DOCTOR = "/admin/appointments/doctors";
    public static final String ADMIN_CLINIC_INFO = "/admin/clinic-info";
    public static final String ADMIN_MEDICAL_RECORD_INDEX = "/admin/medical-records";
    public static final String ADMIN_MEDICAL_RECORD_DETAIL = "/admin/medical-records/{medicalRecordId}/appointments/{appointmentId}";
    public static final String ADMIN_MEDICAL_SERVICE_INDEX = "/admin/services";
    public static final String ADMIN_MEDICINE_INDEX = "/admin/medicines";

    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String ERROR = "/error";

    public static final String CLIENT_HOME = "/";
    public static final String CLIENT_APPOINTMENT_CREATE = "/client/appointments/create";

    // API base
    public static final String API_ADMIN_ENDPOINT = "/api/admin";
    public static final String API_CLIENT_ENDPOINT = "/api/client";

    // API Admin - Login
    public static final String API_ADMIN_LOGIN = API_ADMIN_ENDPOINT + LOGIN;

    // API Admin - User
    public static final String API_ADMIN_USER_ENDPOINT = API_ADMIN_ENDPOINT + "/users";
    public static final String API_ADMIN_USER_LIST = API_ADMIN_USER_ENDPOINT + "/list";
    public static final String API_ADMIN_USER_AVATAR = API_ADMIN_USER_ENDPOINT + "/avatar";
    public static final String API_ADMIN_USER_PROFILE = API_ADMIN_USER_ENDPOINT + "/profile";
    public static final String API_ADMIN_USER_CHANGE_PASSWORD = API_ADMIN_USER_ENDPOINT+ "/change-password";

    // API Admin - Appointment
    public static final String API_ADMIN_APPOINTMENT = API_ADMIN_ENDPOINT + "/appointments";
    public static final String API_ADMIN_APPOINTMENT_LIST = API_ADMIN_APPOINTMENT + "/list";
    public static final String API_ADMIN_APPOINTMENT_WITH_ID = API_ADMIN_APPOINTMENT + "/{appointmentId}";
    public static final String API_ADMIN_APPOINTMENT_FOR_DOCTOR = API_ADMIN_APPOINTMENT + "/doctors";
    public static final String API_ADMIN_APPOINTMENT_NEXT_STATUS = API_ADMIN_APPOINTMENT_WITH_ID + "/next-status";

    // API Admin - Subscriber
    public static final String API_ADMIN_SUBSCRIBER = API_ADMIN_ENDPOINT + "/subscribers";
    public static final String API_ADMIN_SUBSCRIBER_LIST = API_ADMIN_SUBSCRIBER + "/list";
    public static final String API_ADMIN_SUBSCRIBER_DETAIL = API_ADMIN_SUBSCRIBER + "/{subscriberId}";

    // API Admin - Clinic info
    public static final String API_ADMIN_CLINIC_INFO = API_ADMIN_ENDPOINT + "/clinic-info";
    public static final String API_ADMIN_UPDATE_CLINIC_INFO = API_ADMIN_CLINIC_INFO + "/{clinicInfoId}";

    // API Admin - Medical record
    public static final String API_ADMIN_MEDICAL_RECORD = API_ADMIN_ENDPOINT + "/medical-records";
    public static final String API_ADMIN_MEDICAL_RECORD_LIST = API_ADMIN_MEDICAL_RECORD + "/list";
    public static final String API_ADMIN_MEDICAL_RECORD_APPOINTMENT = API_ADMIN_MEDICAL_RECORD +"/{medicalRecordId}/appointments/{appointmentId}";

    // API Admin - Medical service
    public static final String API_ADMIN_MEDICAL_SERVICE = API_ADMIN_ENDPOINT + "/medical-services";
    public static final String API_ADMIN_MEDICAL_SERVICE_LIST = API_ADMIN_MEDICAL_SERVICE + "/list";
    public static final String API_ADMIN_MEDICAL_SERVICE_WITH_ID = API_ADMIN_MEDICAL_SERVICE + "/{medicalServiceId}";

    // API Admin - Medicine
    public static final String API_ADMIN_MEDICINE = API_ADMIN_ENDPOINT + "/medicines";
    public static final String API_ADMIN_MEDICINE_LIST = API_ADMIN_MEDICINE + "/list";
    public static final String API_ADMIN_MEDICINE_WITH_ID = API_ADMIN_MEDICINE + "/{medicineId}";

    // API Admin - prescription
    public static final String API_ADMIN_PRESCRIPTION = API_ADMIN_ENDPOINT + "/prescriptions";

    // API Admin - invoice
    public static final String API_ADMIN_INVOICE = API_ADMIN_ENDPOINT + "/invoices";


    // API Client - Appointment
    public static final String API_CLIENT_APPOINTMENT = API_CLIENT_ENDPOINT + "/appointments";
    public static final String API_CLIENT_APPOINTMENT_AVAILABLE_SLOTS = API_CLIENT_APPOINTMENT + "/available-slots";

    // API Client - Clinic info
    public static final String API_CLIENT_CLINIC_INFO = API_CLIENT_ENDPOINT + "/clinic-info";
}
