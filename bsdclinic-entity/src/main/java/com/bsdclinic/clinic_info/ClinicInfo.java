package com.bsdclinic.clinic_info;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.bsdclinic.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "clinic_info")
public class ClinicInfo {
    @Id
    @Column(name = "clinic_info_id")
    private String clinicInfoId;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "working_hours", columnDefinition = "json")
    @Type(JsonType.class)
    private Map<String, List<TimeRange>> workingHours;

    @Column(name = "website")
    private String website;

    @Column(name = "introduction")
    private String introduction;

    @Column(name = "slogan")
    private String slogan;

    @Column(name = "description")
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "register_time_range")
    private Integer registerTimeRange;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "day_offs", columnDefinition = "json")
    @Type(JsonType.class)
    private List<LocalDate> dayOffs;

    @PrePersist
    public void prePersist() {
        if (clinicInfoId == null) {
            clinicInfoId = NanoIdUtils.randomNanoId();
        }
    }

    public record TimeRange(LocalTime start, LocalTime end) {
        public boolean overlaps(TimeRange other) {
            return !(this.end.isBefore(other.start) || this.start.isAfter(other.end));
        }

        public boolean contains(LocalTime time) {
            return !time.isBefore(start) && time.isBefore(end);
        }

        @Override
        public String toString() {
            return start + " - " + end;
        }
    }
}
