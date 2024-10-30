package org.communication.Database;


import javax.persistence.*;

@Entity
@Table(name = "World_Size")
public class World {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "top_left_x")
    private Integer topLeftX;

    @Column(name = "top_left_y")
    private Integer topLeftY;

    @Column(name = "bottom_right_x")
    private Integer bottomRightX;

    @Column(name = "bottom_right_y")
    private Integer bottomRightY;

    // Constructors
    public World() {
    }

    public World(String name, Integer topLeftX, Integer topLeftY, Integer bottomRightX, Integer bottomRightY) {
        this.name = name;
        this.topLeftX = topLeftX;
        this.topLeftY = topLeftY;
        this.bottomRightX = bottomRightX;
        this.bottomRightY = bottomRightY;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTopLeftX() {
        return topLeftX;
    }

    public void setTopLeftX(Integer topLeftX) {
        this.topLeftX = topLeftX;
    }

    public Integer getTopLeftY() {
        return topLeftY;
    }

    public void setTopLeftY(Integer topLeftY) {
        this.topLeftY = topLeftY;
    }

    public Integer getBottomRightX() {
        return bottomRightX;
    }

    public void setBottomRightX(Integer bottomRightX) {
        this.bottomRightX = bottomRightX;
    }

    public Integer getBottomRightY() {
        return bottomRightY;
    }

    public void setBottomRightY(Integer bottomRightY) {
        this.bottomRightY = bottomRightY;
    }

    @Override
    public String toString() {
        return "World{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", topLeftX=" + topLeftX +
                ", topLeftY=" + topLeftY +
                ", bottomRightX=" + bottomRightX +
                ", bottomRightY=" + bottomRightY +
                '}';
    }
}
