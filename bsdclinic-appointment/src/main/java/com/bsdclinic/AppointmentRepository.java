package com.bsdclinic;

import com.bsdclinic.appointment.Appointment;
import com.bsdclinic.dto.response.IAppointmentStatusCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, String>, JpaSpecificationExecutor<Appointment> {
    @Query("SELECT a.registerTime FROM Appointment a WHERE a.registerDate = :registerDate")
    List<String> findRegisterTimesByRegisterDay(LocalDate registerDate);

    boolean existsByRegisterDateAndRegisterTime(LocalDate registerDate, String registerTime);

    boolean existsByAppointmentId(String appointmentId);

    @Query("SELECT " +
                "a.actionStatus AS actionStatus, " +
                "COUNT(a.actionStatus) AS statusCount " +
            "FROM Appointment a " +
            "GROUP BY a.actionStatus")
    List<IAppointmentStatusCount> getAppointmentStatusCount();

    @Modifying
    @Query("UPDATE Appointment a " +
            "SET a.actionStatus = :actionStatus " +
            "WHERE a.appointmentId = :appointmentId")
    void updateActionStatus(String appointmentId, String actionStatus);
}
