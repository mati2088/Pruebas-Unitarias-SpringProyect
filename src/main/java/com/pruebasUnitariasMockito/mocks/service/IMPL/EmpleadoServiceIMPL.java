package com.pruebasUnitariasMockito.mocks.service.IMPL;

import com.pruebasUnitariasMockito.mocks.exception.ResourceNotFoundException;
import com.pruebasUnitariasMockito.mocks.models.Empleado;
import com.pruebasUnitariasMockito.mocks.repository.EmpleadoRepository;
import com.pruebasUnitariasMockito.mocks.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class EmpleadoServiceIMPL implements EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Override
    public Empleado saveEmpleado(Empleado empleado) {
        Optional<Empleado>empleadoGuardado=empleadoRepository.findByEmail(empleado.getEmail());

        if (empleadoGuardado.isPresent()){
            throw new ResourceNotFoundException("El empleado con ese email ya existe: "+empleado.getEmail());

        }
        return empleadoRepository.save(empleado);

    }

    @Override
    public List<Empleado> getAllEmpleados() {
        return empleadoRepository.findAll();
    }

    @Override
    public Optional<Empleado> getEmpleadoById(long id) {
        return empleadoRepository.findById(id);
    }

    @Override
    public Empleado updateEmpleado(Empleado empleadoActualizado) {
        return empleadoRepository.save(empleadoActualizado);
    }

    @Override
    public void deleteEmpleado(long id) {
        empleadoRepository.deleteById(id);
    }
}
