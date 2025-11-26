package ru.chichkov.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false, length = 5)
    private String number;
    private Integer price;
    @ManyToMany
    @JoinTable(
            name = "bus_city",
            joinColumns = @JoinColumn(name = "bus_id"),
            inverseJoinColumns = @JoinColumn(name = "city_id")
    )
    private List<City> cities;

    public Bus(Long id, String number, Integer price, List<City> cities) {
        this.id = id;
        this.number = number;
        this.price = price;
        this.cities = cities;
    }

    public Bus(Long id, String number, Integer price) {
        this.id = id;
        this.number = number;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", price=" + price +
                ", cities=" + cities +
                '}';
    }
}
