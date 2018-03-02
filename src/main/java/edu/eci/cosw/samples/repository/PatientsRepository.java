/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.cosw.samples.repository;

import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

/**
 *
 * @author 2103021
 */
@Service
public interface PatientsRepository extends JpaRepository<Paciente, PacienteId>{

    @Query("select p from Paciente p where size(p.consultas) > :n")
    public List<Paciente> topPatients(@Param("n") int n) throws ServicesException ;
}
