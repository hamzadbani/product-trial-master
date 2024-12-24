package alten.shop.back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String image;
    private String category;
    private Double price;
    private Integer quantity;
    private String internalReference;
    private Long shellId;
    private String inventoryStatus;
    private Integer rating;
    private Long updatedAt;
}
