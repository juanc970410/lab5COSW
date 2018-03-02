package edu.eci.cosw.samples;


import edu.eci.cosw.jpa.sample.model.Consulta;
import edu.eci.cosw.jpa.sample.model.Paciente;
import edu.eci.cosw.jpa.sample.model.PacienteId;
import edu.eci.cosw.samples.repository.PatientsRepository;
import edu.eci.cosw.samples.services.PatientServices;
import edu.eci.cosw.samples.services.ServicesException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringDataRestApiApplication.class)
@WebAppConfiguration
@ActiveProfiles("test")
public class SpringDataRestApiApplicationTests {

        
        @Autowired
        PatientServices ps;
        @Autowired
        PatientsRepository pr;
        
        //Consulta a paciente que existe
        @Test
        public void getExistingPatient(){
            Set<Consulta> c = new HashSet<>();
            c.add(new Consulta(new Date(2018,02,28), "Control"));
            Paciente expected = new Paciente(new PacienteId(101912, "cc"),"Juan", new Date(1997,04,10), c);
            pr.save(expected);
            Paciente p = null;
            try {
                p = ps.getPatient(101912, "cc");
            } catch (ServicesException ex) {
                Assert.fail("Couldn't get the patient");
            }
            Assert.assertTrue("The expected patient and the consulted patient must be the same",p.equals(expected));
            pr.deleteAll();
        }
        
        //Consulta a paciente que no existe
        @Test
        public void getNonexistentPatient() {
            try{
                ps.getPatient(99999, "cc");
                Assert.fail("Test failed");            
            } catch (ServicesException ex) {
                Logger.getLogger(SpringDataRestApiApplicationTests.class.getName()).log(Level.SEVERE, null, ex);
            }
            pr.deleteAll();
        }
        
        //No existen pacientes con N o más consultas
        @Test 
        public void noPatientsWithNOrMore(){
            Set<Consulta> c = new HashSet<>();
            c.add(new Consulta(new Date(2018,02,28), "Control"));
            Paciente p = new Paciente(new PacienteId(101911, "cc"),"Diana", new Date(1995,12,29), c);
            pr.save(p);
            List<Paciente> lp = null;
             try {
                lp = ps.topPatients(2);
                System.err.println("empty list");
            } catch (ServicesException ex) {
                Assert.fail("Test failed");
            }
            Assert.assertTrue("The list is empty",lp.isEmpty());
            pr.deleteAll();
        }
        
        //Registrar 3 pacientes. Uno sin consultas, otro con una, y el último con dos consultas. Probar usando N=1  y esperar una lista con los dos pacientes correspondientes
        @Test
        public void registerThreePatients(){
            //primer paciente
            Set<Consulta> c0 = new HashSet<>();
            Paciente p0 = new Paciente(new PacienteId(101912, "cc"),"Juan", new Date(1997,04,10), c0);
            pr.save(p0);
            
            //segundo paciente
            Set<Consulta> c1 = new HashSet<>();
            c1.add(new Consulta(new Date(2018,02,28), "Control"));
            Paciente p1= new Paciente(new PacienteId(11111, "cc"),"Jhordy", new Date(1997,06,10), c1);
            pr.save(p1);
            
            //tercer paciente
             Set<Consulta> c2 = new HashSet<>();
            c2.add(new Consulta(new Date(2018,02,28), "Control for flu"));
            c2.add(new Consulta(new Date(2018,02,28), "Control for broken arm"));
            Paciente p2 = new Paciente(new PacienteId(409127, "cc"),"Carlos", new Date(1997,06,10), c2);
            pr.save(p2);
            
            List<Paciente> oldlist = null;
            try {
                oldlist = ps.topPatients(1);
            } catch (ServicesException ex) {
                Assert.fail("Test failed");
            }
            
            List<Paciente> newlist = new ArrayList<>();
            newlist.add(p1); 
            newlist.add(p2); 
            System.out.println(oldlist+ "old list");
            System.out.println(newlist+ "new list");
            Assert.assertTrue("the lists are equals",newlist.equals(oldlist));      

        }
}
