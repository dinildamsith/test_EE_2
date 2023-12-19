package lk.ijse.test_ee_2.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString

public class ItemDTO implements Serializable {
    private String code;
    private String descr;
    private int qty;
    private double unitPrice;


}
