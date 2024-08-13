package com.teamChallenge.entity.order;

import com.teamChallenge.dto.response.figure.FigureInCartOrderResponseDto;
import com.teamChallenge.entity.user.address.AddressInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {

    @Id
    private String id;

    private AddressInfo addressInfo;

    @Column(nullable = false)
    private int totalPrice;

    @Enumerated
    private Statuses status;

    private List<FigureInCartOrderResponseDto> figures;

    @DBRef
    private String userId;

    private Date dateOfCompletion;

    public void setTotalPrice() {
        int totalPrice = 0;
        for (FigureInCartOrderResponseDto figure : figures) {
            totalPrice += figure.price()*figure.amount();
        }
        this.totalPrice = totalPrice;
    }

    public void setStatus(Statuses status) {
        if (status.equals(Statuses.COMPLETED)){
            this.dateOfCompletion = new Date();
        }

        this.status = status;
    }

    public OrderEntity(AddressInfo addressInfo, List<FigureInCartOrderResponseDto> figures, String userId) {
        this.addressInfo = addressInfo;
        status = Statuses.NEW;
        this.figures = figures;
        this.userId = userId;

        setTotalPrice();
    }
}
