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
			logger.info("Creating employees...");
			Employee emp1 = new Employee("E001", "Jon Ander García", "jon.garcia@ecoembes.com", "password123");
			Employee emp2 = new Employee("E002", "Maialen Etxebarria", "maialen.etx@ecoembes.com", "password456");
			Employee emp3 = new Employee("E003", "Iker Mendoza", "iker.mendoza@ecoembes.com", "password789");
			Employee emp4 = new Employee("E004", "Nerea Agirre", "nerea.agirre@ecoembes.com", "password321");
			
			authService.addEmployee(emp1);
			authService.addEmployee(emp2);
			authService.addEmployee(emp3);
			authService.addEmployee(emp4);
			logger.info("✓ 4 Employees created successfully!");


			// --- 2. Crear Plantas de Reciclaje ---
			logger.info("Creating recycling plants...");
			RecyclingPlant plant1 = new RecyclingPlant("PLAST-BIO", "Plásticos Bizkaia S.A.");
			RecyclingPlant plant2 = new RecyclingPlant("CONT-GIP", "Contenedores Gipuzkoa");
			RecyclingPlant plant3 = new RecyclingPlant("ECO-ALAVA", "EcoReciclaje Álava");

			ecoembesService.addPlant(plant1);
			ecoembesService.addPlant(plant2);
			ecoembesService.addPlant(plant3);
			logger.info("✓ 3 Recycling Plants created successfully!");


			// --- 3. Crear Contenedores (Dumpsters) con historial completo ---
			logger.info("Creating dumpsters with historical data...");
			
			// ========== BILBAO (Código Postal 48001 - Centro) ==========
			Dumpster d1 = new Dumpster("D-BI-001", "Plaza Moyua, Bilbao", "48001", 3800.0);
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(7), 800, FillLevel.GREEN));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(6), 950, FillLevel.GREEN));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 1100, FillLevel.GREEN));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 1350, FillLevel.GREEN));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 1550, FillLevel.ORANGE));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1800, FillLevel.ORANGE));
			d1.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 2100, FillLevel.ORANGE));
			d1.updateStatus(2350, FillLevel.ORANGE);

			Dumpster d2 = new Dumpster("D-BI-002", "Calle Licenciado Poza, Bilbao", "48001", 3800.0);
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 500, FillLevel.GREEN));
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 720, FillLevel.GREEN));
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 890, FillLevel.GREEN));
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1050, FillLevel.GREEN));
			d2.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 1200, FillLevel.GREEN));
			d2.updateStatus(1380, FillLevel.GREEN);

			// ========== BILBAO (Código Postal 48011 - Indautxu) ==========
			Dumpster d3 = new Dumpster("D-BI-003", "Gran Vía 50, Bilbao", "48011", 4000.0);
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(6), 600, FillLevel.GREEN));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 850, FillLevel.GREEN));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 1100, FillLevel.GREEN));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 1400, FillLevel.GREEN));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1650, FillLevel.ORANGE));
			d3.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 1900, FillLevel.ORANGE));
			d3.updateStatus(2150, FillLevel.ORANGE);

			Dumpster d4 = new Dumpster("D-BI-004", "Calle Ercilla, Bilbao", "48011", 3500.0);
			d4.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 2800, FillLevel.RED));
			d4.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 3100, FillLevel.RED));
			d4.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 3300, FillLevel.RED));
			d4.updateStatus(3450, FillLevel.RED);

			// ========== DONOSTIA-SAN SEBASTIÁN (Código Postal 20001 - Centro) ==========
			Dumpster d5 = new Dumpster("D-SS-001", "Playa de la Concha, Donostia", "20001", 4200.0);
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(7), 2500, FillLevel.ORANGE));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(6), 2800, FillLevel.ORANGE));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 3100, FillLevel.ORANGE));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 3400, FillLevel.RED));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 3700, FillLevel.RED));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 3900, FillLevel.RED));
			d5.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 4050, FillLevel.RED));
			d5.updateStatus(4180, FillLevel.RED);

			Dumpster d6 = new Dumpster("D-SS-002", "Boulevard de Donostia", "20001", 3900.0);
			d6.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 1200, FillLevel.GREEN));
			d6.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 1500, FillLevel.GREEN));
			d6.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1800, FillLevel.ORANGE));
			d6.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 2100, FillLevel.ORANGE));
			d6.updateStatus(2400, FillLevel.ORANGE);

			// ========== DONOSTIA-SAN SEBASTIÁN (Código Postal 20004 - Gros) ==========
			Dumpster d7 = new Dumpster("D-SS-003", "Calle Zabaleta, Donostia", "20004", 3600.0);
			d7.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 800, FillLevel.GREEN));
			d7.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 1100, FillLevel.GREEN));
			d7.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 1400, FillLevel.GREEN));
			d7.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1700, FillLevel.ORANGE));
			d7.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 2000, FillLevel.ORANGE));
			d7.updateStatus(2300, FillLevel.ORANGE);

			// ========== VITORIA-GASTEIZ (Código Postal 01001 - Centro) ==========
			Dumpster d8 = new Dumpster("D-VG-001", "Plaza de la Virgen Blanca, Vitoria", "01001", 4100.0);
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(6), 1000, FillLevel.GREEN));
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(5), 1300, FillLevel.GREEN));
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 1600, FillLevel.GREEN));
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 1900, FillLevel.ORANGE));
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 2200, FillLevel.ORANGE));
			d8.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 2500, FillLevel.ORANGE));
			d8.updateStatus(2800, FillLevel.ORANGE);

			Dumpster d9 = new Dumpster("D-VG-002", "Calle Dato, Vitoria", "01001", 3700.0);
			d9.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(4), 500, FillLevel.GREEN));
			d9.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 750, FillLevel.GREEN));
			d9.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 1000, FillLevel.GREEN));
			d9.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 1250, FillLevel.GREEN));
			d9.updateStatus(1500, FillLevel.GREEN);

			// ========== VITORIA-GASTEIZ (Código Postal 01008 - Lakua) ==========
			Dumpster d10 = new Dumpster("D-VG-003", "Avenida Gasteiz, Vitoria", "01008", 3800.0);
			d10.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(3), 2900, FillLevel.RED));
			d10.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(2), 3200, FillLevel.RED));
			d10.getUsageHistory().add(new DumpsterUsageRecord(LocalDate.now().minusDays(1), 3500, FillLevel.RED));
			d10.updateStatus(3700, FillLevel.RED);

			// Añadir todos los contenedores al repositorio
			ecoembesService.addDumpster(d1);
			ecoembesService.addDumpster(d2);
			ecoembesService.addDumpster(d3);
			ecoembesService.addDumpster(d4);
			ecoembesService.addDumpster(d5);
			ecoembesService.addDumpster(d6);
			ecoembesService.addDumpster(d7);
			ecoembesService.addDumpster(d8);
			ecoembesService.addDumpster(d9);
			ecoembesService.addDumpster(d10);
			logger.info("✓ 10 Dumpsters created with complete historical data!");

			// --- 4. Asignar contenedores a plantas de reciclaje ---
			logger.info("Assigning dumpsters to recycling plants...");
			java.util.List<String> plant1Dumpsters = java.util.Arrays.asList("D-BI-001", "D-BI-002", "D-BI-003");
			java.util.List<String> plant2Dumpsters = java.util.Arrays.asList("D-SS-001", "D-SS-002", "D-SS-003");
			java.util.List<String> plant3Dumpsters = java.util.Arrays.asList("D-VG-001", "D-VG-002", "D-VG-003", "D-BI-004");

			ecoembesService.assignDumpstersToPlant("PLAST-BIO", plant1Dumpsters);
			ecoembesService.assignDumpstersToPlant("CONT-GIP", plant2Dumpsters);
			ecoembesService.assignDumpstersToPlant("ECO-ALAVA", plant3Dumpsters);
			logger.info("✓ Dumpsters assigned to plants successfully!");

			// Resumen final
			logger.info("\n========================================");
			logger.info("   DATA INITIALIZATION COMPLETED!");
			logger.info("========================================");
			logger.info("✓ Employees: 4");
			logger.info("✓ Recycling Plants: 3");
			logger.info("✓ Dumpsters: 10");
			logger.info("✓ Cities covered: Bilbao, Donostia, Vitoria");
			logger.info("✓ Postal codes: 48001, 48011, 20001, 20004, 01001, 01008");
			logger.info("========================================\n");
		};
	}
}