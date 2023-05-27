package ru.lazarenko.storemanagement.entity;

import lombok.*;
import ru.lazarenko.storemanagement.model.OrderStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<OrderRow> orderRows = new ArrayList<>();

    private BigDecimal amount = new BigDecimal(0);

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.NEW;

    public void setOrderRows(List<OrderRow> orderRows) {
        for (OrderRow orderRow : orderRows) {
            orderRow.setOrder(this);
        }
        this.orderRows = orderRows;
    }

    public void addOrderRow(OrderRow orderRow){
        orderRows.add(orderRow);
        orderRow.setOrder(this);
    }
}
