package com.bsdclinic.client.constant;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ClinicSchedule {
    public static final Map<DayOfWeek, List<TimeRange>> CLINIC_SCHEDULE = Map.of(
            DayOfWeek.MONDAY, List.of(new TimeRange(LocalTime.of(18, 0), LocalTime.of(21, 30))),
            DayOfWeek.TUESDAY, List.of(new TimeRange(LocalTime.of(18, 0), LocalTime.of(21, 30))),
            DayOfWeek.WEDNESDAY, List.of(new TimeRange(LocalTime.of(18, 0), LocalTime.of(21, 30))),
            DayOfWeek.THURSDAY, List.of(new TimeRange(LocalTime.of(18, 0), LocalTime.of(21, 30))),
            DayOfWeek.FRIDAY, List.of(new TimeRange(LocalTime.of(18, 0), LocalTime.of(21, 30))),

            DayOfWeek.SATURDAY, List.of(
                    new TimeRange(LocalTime.of(8, 30), LocalTime.of(12, 0)),
                    new TimeRange(LocalTime.of(13, 30), LocalTime.of(20, 30))
            ),
            DayOfWeek.SUNDAY, List.of(
                    new TimeRange(LocalTime.of(8, 30), LocalTime.of(12, 0)),
                    new TimeRange(LocalTime.of(13, 30), LocalTime.of(20, 30))
            )
    );

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

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
