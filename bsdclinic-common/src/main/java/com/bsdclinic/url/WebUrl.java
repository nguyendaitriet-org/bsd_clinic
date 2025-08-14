package com.bsdclinic.url;

public class WebUrl {
    // Admin Page
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
    public static final String ADMIN_CATEGORY_INDEX = "/admin/categories";

    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String ERROR = "/error";

    // Client Page
    public static final String CLIENT_HOME = "/";
    public static final String CLIENT_APPOINTMENT_CREATE = "/client/appointments/create";
    public static final String CLIENT_APPOINTMENT_CREATE_SUCCESS = "/client/appointments/create/success";
    public static final String CLIENT_ZALO = "/client/zalo";
    public static final String CLIENT_VIDEOS = "/client/videos";

    public static final String ACNE_TREATMENT_BLOG = "/client/post/acne_treatment";
    public static final String SKIN_REJUVENATION_BLOG = "/client/post/skin_rejuvenation";
    public static final String LASER_BEAUTY_BLOG = "/client/post/laser_beauty";
    public static final String BOTOX_FILLER_BLOG = "/client/post/botox_filler";
    public static final String BIO_LIGHT_BLOG = "/client/post/bio_light";
    public static final String SKINCARE_BLOG = "/client/post/skincare";
    public static final String ACNE_DURING_PREGNANCY_BLOG = "/client/post/acne_during_pregnancy";
    public static final String CLINIC_INTRODUCTION_PAGE = "/client/post/clinic_introduction";

    // API Public
    public static final String API_PUBLIC_ENDPOINT = "/api/public";
    public static final String API_PUBLIC_IMAGE_BY_NAME = API_PUBLIC_ENDPOINT + "/images/{imageName}";

    // API Base
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

    // API Admin - Prescription
    public static final String API_ADMIN_PRESCRIPTION = API_ADMIN_ENDPOINT + "/prescriptions";
    public static final String API_ADMIN_PRESCRIPTION_WITH_ID = API_ADMIN_PRESCRIPTION + "/{prescriptionId}";

    // API Admin - Invoice
    public static final String API_ADMIN_INVOICE = API_ADMIN_ENDPOINT + "/invoices";
    public static final String API_ADMIN_INVOICE_WITH_ID = API_ADMIN_INVOICE + "/{invoiceId}";

    // API Admin - Category
    public static final String API_ADMIN_CATEGORY = API_ADMIN_ENDPOINT + "/categories";



    // API Client - Appointment
    public static final String API_CLIENT_APPOINTMENT = API_CLIENT_ENDPOINT + "/appointments";
    public static final String API_CLIENT_APPOINTMENT_AVAILABLE_SLOTS = API_CLIENT_APPOINTMENT + "/available-slots";

    // API Client - Clinic info
    public static final String API_CLIENT_CLINIC_INFO = API_CLIENT_ENDPOINT + "/clinic-info";
}
