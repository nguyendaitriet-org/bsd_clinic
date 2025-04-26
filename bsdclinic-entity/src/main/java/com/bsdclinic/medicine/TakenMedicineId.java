package com.bsdclinic.medicine;

import java.io.Serializable;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TakenMedicineId implements Serializable {
    private String prescriptionId;
    private String medicineId;
}
