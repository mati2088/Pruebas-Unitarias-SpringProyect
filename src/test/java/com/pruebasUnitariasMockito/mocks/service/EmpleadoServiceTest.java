package com.pruebasUnitariasMockito.mocks.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import  static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import com.pruebasUnitariasMockito.mocks.exception.ResourceNotFoundException;
import com.pruebasUnitariasMockito.mocks.models.Empleado;
import com.pruebasUnitariasMockito.mocks.repository.EmpleadoRepository;
import com.pruebasUnitariasMockito.mocks.service.IMPL.EmpleadoServiceIMPL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmpleadoServiceTest {

    private Empleado empleado;

    @BeforeEach
    void setup(){

        empleado=empleado.builder()
                .id(1L)
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();
    }
    @Mock //simulado simulacro
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoServiceIMPL empleadoServiceIMPL;

    @DisplayName("test para guardar un empleado")
    @Test
    void  testGuardarEmpleado(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.empty());
        given(empleadoRepository.save(empleado))
                .willReturn(empleado);
        //when
        Empleado empleadoGuardado=empleadoServiceIMPL.saveEmpleado(empleado);
        //then
        assertThat(empleadoGuardado).isNotNull();
    }
    @DisplayName("test para guardar un empleado con throw exceptions")
    @Test
    void  testGuardarEmpleadoConThrow(){
        //given
        given(empleadoRepository.findByEmail(empleado.getEmail()))
                .willReturn(Optional.of(empleado));

        //when
        assertThrows(ResourceNotFoundException.class,()->{
            empleadoServiceIMPL.saveEmpleado(empleado);

        });
        //then
        verify(empleadoRepository,never()).save(any(Empleado.class));
    }


    @DisplayName("test para listar los empleados")
    @Test
    void testListarEmpleados(){
        //given
        Empleado empleado1=empleado.builder()
                .id(1L)
                .nombre("julian")
                .apellido("romero")
                .email("julian@gmail.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(List.of(empleado,empleado1));

        //when
        List<Empleado>empleados=empleadoServiceIMPL.getAllEmpleados();
        //then
        assertThat(empleados).isNotNull();
        assertThat(empleados.size()).isEqualTo(2);
    }


    @DisplayName("test para retornar una lista vacia")
    @Test
    void testListarColeccionVacia(){
        //given
        Empleado empleado1=empleado.builder()
                .id(2L)
                .nombre("julian")
                .apellido("romero")
                .email("julian@gmail.com")
                .build();
        given(empleadoRepository.findAll()).willReturn(Collections.emptyList());
        //when
        List<Empleado>ListaEmpleados=empleadoServiceIMPL.getAllEmpleados();
        //then
        assertThat(ListaEmpleados).isEmpty();
        assertThat(ListaEmpleados.size()).isEqualTo(0);

    }

    @DisplayName("test para obtener un empleado por id")
    @Test
    void testObtenerEmpleadoPorId(){
        //given
        given(empleadoRepository.findById(1L)).willReturn(Optional.of(empleado));
        //when
        Empleado empleadoGuardado=empleadoServiceIMPL.getEmpleadoById(empleado.getId()).get();
        //then
        assertThat(empleadoGuardado).isNotNull();
    }


    @DisplayName("test para actualizar un empleado")
    @Test
    void  testActualizarEmpleado(){
        //given
        given(empleadoRepository.save(empleado)).willReturn(empleado);
        empleado.setEmail("empleadoActalizado@gmail.com");
        empleado.setNombre("actualizado");
        //when
        Empleado empleadoActualizado=empleadoServiceIMPL.updateEmpleado(empleado);
        //then
        assertThat(empleadoActualizado.getEmail()).isEqualTo("empleadoActalizado@gmail.com");
        assertThat(empleadoActualizado.getNombre()).isEqualTo("actualizado");

    }



    @DisplayName("test para eliminar un empleado")
    @Test
    void  testEliminarEmpleado(){
        //given
        long empleadoId=1L;
        willDoNothing().given(empleadoRepository).deleteById(empleadoId);
        //when
        empleadoServiceIMPL.deleteEmpleado(empleadoId);
        //then
        verify(empleadoRepository,times(1)).deleteById(empleadoId);

    }
}
