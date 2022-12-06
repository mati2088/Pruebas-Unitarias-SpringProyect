package com.pruebasUnitariasMockito.mocks.repository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

import com.pruebasUnitariasMockito.mocks.models.Empleado;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest //con esta clases indicamos que solo vamos hacer test a las entidades y a los repositorios
public class EmpleadoRespositoryTest {

    @Autowired
    private EmpleadoRepository empleadoRepository;

    private Empleado empleado;

    @BeforeEach
    void setup(){

        empleado=empleado.builder()
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();
    }

    @DisplayName("test para guardar un empleado")
    @Test
    void testGuardarEmpleado(){
        //metodologia bdd
        //given - dado o condicion previa o configuracion
        Empleado empleado1=Empleado.builder()
                .nombre("Matias")
                .apellido("Arana")
                .email("matiasarana46@gmail.com")
                .build();
        //when - accion o el comportamiento que vamos a probar
        Empleado empleadoGuardado=empleadoRepository.save(empleado1);
        //then -verificar la salida
        assertThat(empleadoGuardado).isNotNull();
        assertThat(empleadoGuardado.getId()).isGreaterThan(0);

        //dado un empleado
        //cuando yo lo guarde
        //voy a esperar que no sea nulo
        //y que sea mayor a cero
    }

    @DisplayName("test para listar los empleados")
    @Test
    void testListarEmpleados(){

        //given
        Empleado empleado1=Empleado.builder()
                .nombre("Juan")
                .apellido("Arana")
                .email("juanarana46@gmail.com")
                .build();
        ;
        empleadoRepository.save(empleado1);
        empleadoRepository.save(empleado);

        //when
        List<Empleado>listaEmpleados=empleadoRepository.findAll();

        //then

        assertThat(listaEmpleados).isNotNull();
        assertThat(listaEmpleados.size()).isEqualTo(2);

    }

    @DisplayName("Test para obtener un empleado por id")
    @Test
    void testObtenerEmpleadoPorId(){
        empleadoRepository.save(empleado);
        //when- comportamiento o accion que vamos a probar

        Empleado empleadoDB= empleadoRepository.findById(empleado.getId()).get();
        //then
        assertThat(empleadoDB).isNotNull();
    }

    @DisplayName("test para actualizar un empleado")
    @Test
    void testActualizarEmpleado(){
        empleadoRepository.save(empleado);

        //when
        Empleado empleadoGuardado=empleadoRepository.findById(empleado.getId()).get();
        empleadoGuardado.setEmail("c2MODIFICADO@gmail.com");
        empleadoGuardado.setNombre("modificado");
        empleadoGuardado.setApellido("pa el test");

        Empleado empleadoActualizado=empleadoRepository.save(empleadoGuardado);


        //then
        assertThat(empleadoActualizado
                .getEmail())
                .isEqualTo("c2MODIFICADO@gmail.com");

        assertThat(empleadoActualizado.getNombre()).isEqualTo("modificado");
    }
    @DisplayName("test para eliminar un empleado")
    @Test
    void testEliminarEmpleado(){
        empleadoRepository.save(empleado);

        //when
        empleadoRepository.deleteById(empleado.getId());
        Optional<Empleado> empleadoOptional=empleadoRepository.findById(empleado.getId());

        //then

        assertThat(empleadoOptional).isEmpty();//is empty= esta vacio

    }




}
