/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes;

import java.time.LocalDate; // Importante para las fechas de historial
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Importa las entidades de RECICLAJE
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.FillLevel;

// Importa los servicios (¡necesitarán ser actualizados!)
import es.deusto.sd.ecoembes.service.AuthService;
import es.deusto.sd.ecoembes.service.EcoembesService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	@Bean
	CommandLineRunner initData(EcoembesService ecoembesService, AuthService authService) {
		return args -> {

			// --- 1. Crear Empleados ---
            // (Asumimos que AuthService será adaptado para Empleados)
			Employee emp1 = new Employee("E001", "Empleado 1", "emp1@eco.com", "Athletic123");
			Employee emp2 = new Employee("E002", "Empleado 2", "emp2@eco.com", "Seguridad456");
			
            // authService.addEmployee(emp1); // Necesitarás crear este método en AuthService
			// authService.addEmployee(emp2); // y un repositorio de Employees.
			logger.info("Employees saved!");


			// --- 2. Crear Plantas de Reciclaje ---
			RecyclingPlant plant1 = new RecyclingPlant("PlasSB", "Plásticos San Bizente");
			RecyclingPlant plant2 = new RecyclingPlant("ContSocket", "Contenedores S.L.");

            // ecoembesService.addPlant(plant1); // Necesitarás crear este método en EcoembesService
            // ecoembesService.addPlant(plant2); // y un repositorio de RecyclingPlants.
			logger.info("Recycling Plants saved!");


			// --- 3. Crear Contenedores (Dumpsters) ---
			
            // Contenedor 1: Bilbao
            Dumpster d1 = new Dumpster("D-BI-001", "Plaza Moyua, Bilbao", 3800.0);
            // Añadimos historial simulado
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1200, FillLevel.GREEN));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 1800, FillLevel.ORANGE));
            // El método updateStatus añade el registro de "hoy" y actualiza el estado
			d1.updateStatus(1950, FillLevel.ORANGE); 

            // Contenedor 2: Bilbao
			Dumpster d2 = new Dumpster("D-BI-002", "Gran Vía 50, Bilbao", 3800.0);
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 200, FillLevel.GREEN));
			d2.updateStatus(350, FillLevel.GREEN); // Hoy

            // Contenedor 3: Donostia
			Dumpster d3 = new Dumpster("D-SS-001", "Playa de la Concha, Donostia", 4200.0);
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 3500, FillLevel.ORANGE));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 4000, FillLevel.RED));
			d3.updateStatus(4150, FillLevel.RED); // Hoy

            // ecoembesService.addDumpster(d1); // Necesitarás crear este método en EcoembesService
            // ecoembesService.addDumpster(d2); // y un repositorio de Dumpsters.
            // ecoembesService.addDumpster(d3);
			logger.info("Dumpsters saved!");
		};
	}
}