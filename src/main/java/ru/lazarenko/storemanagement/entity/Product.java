package ru.lazarenko.storemanagement.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @Min(value = 100, message = "Минимальная цена должна быть не меньше 100")
    private BigDecimal price = new BigDecimal(0);

    private Integer count = 0;

    private String filename;

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartRow> cartRows = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderRow> orderRows = new ArrayList<>();

    public void setCartRows(List<CartRow> cartRows) {
        for (CartRow cartRow : cartRows) {
            cartRow.setProduct(this);
        }
        this.cartRows = cartRows;
    }

    public void setOrderRows(List<OrderRow> orderRows) {
        for (OrderRow orderRow : orderRows) {
            orderRow.setProduct(this);
        }
        this.orderRows = orderRows;
    }
}