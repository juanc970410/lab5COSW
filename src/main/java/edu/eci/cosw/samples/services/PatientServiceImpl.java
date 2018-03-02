/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cosw.samples.services;

import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.repository.PatientsRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author 2103021
 */
@Service
public class PatientServiceImpl implements PatientServices{
    
    @Autowired
    PatientsRepository pr;
    
    @Override
    public Paciente getPatient(int id, String tipoid) throws ServicesException {
        PacienteId pid = new PacienteId(id, tipoid);
        Paciente p = pr.getOne(pid);
        if(p!=null){
            return p;
        }else{
            throw new ServicesException("Paciente inexistente");
        }
    }

    @Override
    public List<Paciente> topPatients(int n) throws ServicesException {
        return pr.topPatients(n);
    }
    
}
