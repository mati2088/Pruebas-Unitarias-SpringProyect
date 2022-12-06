package com.pruebasUnitariasMockito.mocks.controladores;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.ArgumentMatchers.any;
import  static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.core.JsonProcessingException;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pruebasUnitariasMockito.mocks.models.Empleado;
import com.pruebasUnitariasMockito.mocks.service.EmpleadoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
@WebMvcTest //sirve para probar controladores, autoconfig mock mvc para probar http al controlador
public class TestControllersEmpleado{


    @Autowired
    private MockMvc mockMvc;

    @MockBean //Se utiliza para agregar objetos simulados, el simulacro remplaza cualquier bean en la aplicacion
    private EmpleadoService empleadoService;

    @Autowired
    private ObjectMapper objectMapper;



    @DisplayName("guardar empleado")
    @Test
    void  testGuardarEmpleado() throws Exception {
        //given
         Empleado empleado =Empleado.builder()
                .id(1L)
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();
         given(empleadoService.saveEmpleado(any(Empleado.class)))
                 .willAnswer((invocation) ->invocation.getArgument(0));
        //when
        ResultActions response = mockMvc.perform(post("/api/empleados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleado)));
        //then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));
    }


    @DisplayName("Test para listar un empleado")
    @Test
    void testListarEmpleados()throws Exception{
        //given = configuracion and dado
        List<Empleado>listEmpleados= new ArrayList<>();
        listEmpleados.add(Empleado.builder().nombre("empleado1").apellido("wan").email("empleado1@gmail.com").build());
        listEmpleados.add(Empleado.builder().nombre("empleado2").apellido("chu").email("empleado2@gmail.com").build());
        listEmpleados.add(Empleado.builder().nombre("empleado3").apellido("tree").email("empleado3@gmail.com").build());
        listEmpleados.add(Empleado.builder().nombre("empleado4").apellido("forr").email("empleado4@gmail.com").build());
        listEmpleados.add(Empleado.builder().nombre("empleado5").apellido("siven").email("empleado5@gmail.com").build());
        given(empleadoService.getAllEmpleados()).willReturn(listEmpleados);

        //when = comportamiento and accion
        ResultActions response=mockMvc.perform(get("/api/empleados"));

        //then = resultados esperados
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",is(listEmpleados.size())));

    }

    @DisplayName("Test para obtener un empleado por id")
    @Test
    void testObtenerEmpleadoPorId()throws Exception{
        //given
        long empleadoId=1L;
        Empleado empleado =Empleado.builder()
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleado));

        //when
        ResultActions response= mockMvc.perform(get("/api/empleados/{id}",empleadoId));


        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleado.getEmail())));

    }

    @DisplayName("Test para obtener un empleado no encontrado")
    @Test
    void testObtenerEmpleadoNoEncontrado()throws Exception{
        //given
        long empleadoId=1L;
        Empleado empleado =Empleado.builder()
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();
        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());

        //when
        ResultActions response= mockMvc.perform(get("/api/empleados/{id}",empleadoId));


        //then
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    @Test
    void testActualizarEmpleado()throws Exception{
        //given
        long empleadoId=1L;
        Empleado empleadoGuardado =Empleado.builder()
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();

        Empleado empleadoActualizado =Empleado.builder()
                .nombre("actualizado")
                .apellido("actualizado")
                .email("actualizado@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.of(empleadoGuardado));
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        //when
        ResultActions response=mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.nombre",is(empleadoActualizado.getNombre())))
                .andExpect(jsonPath("$.apellido",is(empleadoActualizado.getApellido())))
                .andExpect(jsonPath("$.email",is(empleadoActualizado.getEmail())));

    }

    @Test

    void testActualizarEmpleadoNoEncontrado()throws Exception{
        //given
        long empleadoId=1L;
        Empleado empleadoGuardado =Empleado.builder()
                .nombre("lionel")
                .apellido("mesis")
                .email("lionelmesi@gmail.com")
                .build();

        Empleado empleadoActualizado =Empleado.builder()
                .nombre("actualizado")
                .apellido("actualizado")
                .email("actualizado@gmail.com")
                .build();

        given(empleadoService.getEmpleadoById(empleadoId)).willReturn(Optional.empty());
        given(empleadoService.updateEmpleado(any(Empleado.class)))
                .willAnswer(invocation -> invocation.getArgument(0));
        //when
        ResultActions response=mockMvc.perform(put("/api/empleados/{id}",empleadoId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(empleadoActualizado)));
        //then
        response.andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void testEliminarEmpleado()throws Exception{
        //given
        long empleadoId=1L;
        willDoNothing().given(empleadoService).deleteEmpleado(empleadoId);
        //when
        ResultActions response=mockMvc.perform(delete("/api/empleados/{id}",empleadoId));
        //then
        response.andExpect(status().isOk())
                .andDo(print());

    }









}
