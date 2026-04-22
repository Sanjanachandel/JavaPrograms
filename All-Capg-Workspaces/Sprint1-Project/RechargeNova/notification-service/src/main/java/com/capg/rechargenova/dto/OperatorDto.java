package com.capg.rechargenova.dto;

public class OperatorDto {
    private Long id;
    private String name;
    private String type;
    private String circle;

    public OperatorDto() {}

    public OperatorDto(Long id, String name, String type, String circle) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.circle = circle;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getCircle() { return circle; }
    public void setCircle(String circle) { this.circle = circle; }
}
