package com.pruebasUnitariasMockito.mocks.models;


import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "empleados")
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Empleado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nombre",nullable = false)
    private String nombre;
    @Column(name = "apellido",nullable = false)

    private String apellido;
    @Column(name = "email",nullable = false)

    private String email;


}
