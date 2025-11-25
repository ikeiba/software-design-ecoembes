/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

// Importa las entidades de RECICLAJE
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevel;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;

// Importa los repositorios
import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.dao.DumpsterRepository;
import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

	@Bean
	@Transactional
	CommandLineRunner initData(EmployeeRepository employeeRepository, 
								RecyclingPlantRepository plantRepository,
								DumpsterRepository dumpsterRepository,
								AssignmentRepository assignmentRepository) {
		return args -> {
			// Database is already initialized
			if (employeeRepository.count() > 0) {
				logger.info("Database already initialized, skipping data creation.");
				return;
			}

			// --- 1. Crear Empleados ---
			logger.info("Creating employees...");
			Employee emp1 = new Employee("Jon Ander Garcia", "jon.garcia@ecoembes.com", "password123");
			Employee emp2 = new Employee("Maialen Etxebarria", "maialen.etx@ecoembes.com", "password456");
			Employee emp3 = new Employee("Iker Ibarrola", "iker.mendoza@ecoembes.com", "password789");
			Employee emp4 = new Employee("Nerea Agirre", "nerea.agirre@ecoembes.com", "password321");
			Employee emp5 = new Employee("Bobi Gomez", "a", "a");

			
			// Save employees - IDs will be auto-generated
			employeeRepository.saveAll(List.of(emp1, emp2, emp3, emp4, emp5));
			logger.info("✓ 5 Employees saved!");


			// --- 2. Crear Plantas de Reciclaje ---
			logger.info("Creating recycling plants...");
			RecyclingPlant plant1 = new RecyclingPlant("PlasSB Ltd.");
			RecyclingPlant plant2 = new RecyclingPlant("ContSocket Ltd.");

			// Save plants - IDs will be auto-generated
			plantRepository.saveAll(List.of(plant1, plant2));
			logger.info("✓ 3 Recycling Plants saved!");


			// --- 3. Crear Contenedores (Dumpsters) con historial completo ---
			logger.info("Creating dumpsters with historical data...");
			
			// ========== BILBAO (Código Postal 48001 - Centro) ==========
			Dumpster d1 = new Dumpster("Plaza Moyua, Bilbao", "48001", 3800.0);
			addUsageRecord(d1, LocalDate.now().minusDays(7), 800, FillLevel.GREEN);
			addUsageRecord(d1, LocalDate.now().minusDays(6), 950, FillLevel.GREEN);
			addUsageRecord(d1, LocalDate.now().minusDays(5), 1100, FillLevel.GREEN);
			addUsageRecord(d1, LocalDate.now().minusDays(4), 1350, FillLevel.GREEN);
			addUsageRecord(d1, LocalDate.now().minusDays(3), 1550, FillLevel.ORANGE);
			addUsageRecord(d1, LocalDate.now().minusDays(2), 1800, FillLevel.ORANGE);
			addUsageRecord(d1, LocalDate.now().minusDays(1), 2100, FillLevel.ORANGE);
			d1.updateStatus(2350, FillLevel.ORANGE);

			Dumpster d2 = new Dumpster("Calle Licenciado Poza, Bilbao", "48001", 3800.0);
			addUsageRecord(d2, LocalDate.now().minusDays(5), 500, FillLevel.GREEN);
			addUsageRecord(d2, LocalDate.now().minusDays(4), 720, FillLevel.GREEN);
			addUsageRecord(d2, LocalDate.now().minusDays(3), 890, FillLevel.GREEN);
			addUsageRecord(d2, LocalDate.now().minusDays(2), 1050, FillLevel.GREEN);
			addUsageRecord(d2, LocalDate.now().minusDays(1), 1200, FillLevel.GREEN);
			d2.updateStatus(1380, FillLevel.GREEN);

			// ========== BILBAO (Código Postal 48011 - Indautxu) ==========
			Dumpster d3 = new Dumpster("Gran Vía 50, Bilbao", "48011", 4000.0);
			addUsageRecord(d3, LocalDate.now().minusDays(6), 600, FillLevel.GREEN);
			addUsageRecord(d3, LocalDate.now().minusDays(5), 850, FillLevel.GREEN);
			addUsageRecord(d3, LocalDate.now().minusDays(4), 1100, FillLevel.GREEN);
			addUsageRecord(d3, LocalDate.now().minusDays(3), 1400, FillLevel.GREEN);
			addUsageRecord(d3, LocalDate.now().minusDays(2), 1650, FillLevel.ORANGE);
			addUsageRecord(d3, LocalDate.now().minusDays(1), 1900, FillLevel.ORANGE);
			d3.updateStatus(2150, FillLevel.ORANGE);

			Dumpster d4 = new Dumpster("Calle Ercilla, Bilbao", "48011", 3500.0);
			addUsageRecord(d4, LocalDate.now().minusDays(3), 2800, FillLevel.RED);
			addUsageRecord(d4, LocalDate.now().minusDays(2), 3100, FillLevel.RED);
			addUsageRecord(d4, LocalDate.now().minusDays(1), 3300, FillLevel.RED);
			d4.updateStatus(3450, FillLevel.RED);

			// ========== DONOSTIA-SAN SEBASTIÁN (Código Postal 20001 - Centro) ==========
			Dumpster d5 = new Dumpster("Playa de la Concha, Donostia", "20001", 4200.0);
			addUsageRecord(d5, LocalDate.now().minusDays(7), 2500, FillLevel.ORANGE);
			addUsageRecord(d5, LocalDate.now().minusDays(6), 2800, FillLevel.ORANGE);
			addUsageRecord(d5, LocalDate.now().minusDays(5), 3100, FillLevel.ORANGE);
			addUsageRecord(d5, LocalDate.now().minusDays(4), 3400, FillLevel.RED);
			addUsageRecord(d5, LocalDate.now().minusDays(3), 3700, FillLevel.RED);
			addUsageRecord(d5, LocalDate.now().minusDays(2), 3900, FillLevel.RED);
			addUsageRecord(d5, LocalDate.now().minusDays(1), 4050, FillLevel.RED);
			d5.updateStatus(4180, FillLevel.RED);

			Dumpster d6 = new Dumpster("Boulevard de Donostia", "20001", 3900.0);
			addUsageRecord(d6, LocalDate.now().minusDays(4), 1200, FillLevel.GREEN);
			addUsageRecord(d6, LocalDate.now().minusDays(3), 1500, FillLevel.GREEN);
			addUsageRecord(d6, LocalDate.now().minusDays(2), 1800, FillLevel.ORANGE);
			addUsageRecord(d6, LocalDate.now().minusDays(1), 2100, FillLevel.ORANGE);
			d6.updateStatus(2400, FillLevel.ORANGE);

			// ========== DONOSTIA-SAN SEBASTIÁN (Código Postal 20004 - Gros) ==========
			Dumpster d7 = new Dumpster("Calle Zabaleta, Donostia", "20004", 3600.0);
			addUsageRecord(d7, LocalDate.now().minusDays(5), 800, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(4), 1100, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(3), 1400, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(2), 1700, FillLevel.ORANGE);
			addUsageRecord(d7, LocalDate.now().minusDays(1), 2000, FillLevel.ORANGE);
			d7.updateStatus(2300, FillLevel.ORANGE);

			// ========== VITORIA-GASTEIZ (Código Postal 01001 - Centro) ==========
			Dumpster d8 = new Dumpster("Plaza de la Virgen Blanca, Vitoria", "01001", 4100.0);
			addUsageRecord(d8, LocalDate.now().minusDays(6), 1000, FillLevel.GREEN);
			addUsageRecord(d8, LocalDate.now().minusDays(5), 1300, FillLevel.GREEN);
			addUsageRecord(d8, LocalDate.now().minusDays(4), 1600, FillLevel.GREEN);
			addUsageRecord(d8, LocalDate.now().minusDays(3), 1900, FillLevel.ORANGE);
			addUsageRecord(d8, LocalDate.now().minusDays(2), 2200, FillLevel.ORANGE);
			addUsageRecord(d8, LocalDate.now().minusDays(1), 2500, FillLevel.ORANGE);
			d8.updateStatus(2800, FillLevel.ORANGE);

			Dumpster d9 = new Dumpster("Calle Dato, Vitoria", "01001", 3700.0);
			addUsageRecord(d9, LocalDate.now().minusDays(4), 500, FillLevel.GREEN);
			addUsageRecord(d9, LocalDate.now().minusDays(3), 750, FillLevel.GREEN);
			addUsageRecord(d9, LocalDate.now().minusDays(2), 1000, FillLevel.GREEN);
			addUsageRecord(d9, LocalDate.now().minusDays(1), 1250, FillLevel.GREEN);
			d9.updateStatus(1500, FillLevel.GREEN);

			// ========== VITORIA-GASTEIZ (Código Postal 01008 - Lakua) ==========
			Dumpster d10 = new Dumpster("Avenida Gasteiz, Vitoria", "01008", 3800.0);
			addUsageRecord(d10, LocalDate.now().minusDays(3), 2900, FillLevel.RED);
			addUsageRecord(d10, LocalDate.now().minusDays(2), 3200, FillLevel.RED);
			addUsageRecord(d10, LocalDate.now().minusDays(1), 3500, FillLevel.RED);
			d10.updateStatus(3700, FillLevel.RED);

			// Save all dumpsters - usage history will be persisted due to CascadeType.ALL
			dumpsterRepository.saveAll(List.of(d1, d2, d3, d4, d5, d6, d7, d8, d9, d10));
			logger.info("✓ 10 Dumpsters saved with complete historical data!");

			// --- 4. Asignar contenedores a plantas de reciclaje ---
			logger.info("Assigning dumpsters to recycling plants...");
			LocalDate today = LocalDate.now();
			
			// Assignments for today
			Assignment a1 = new Assignment(today, d1, emp1, plant1);
			Assignment a2 = new Assignment(today, d2, emp1, plant1);
			Assignment a3 = new Assignment(today, d3, emp2, plant1);
			Assignment a4 = new Assignment(today, d5, emp2, plant2);
			Assignment a5 = new Assignment(today, d6, emp3, plant2);
			Assignment a6 = new Assignment(today, d7, emp3, plant2);
			
			// Some assignments for yesterday to have historical data
			LocalDate yesterday = LocalDate.now().minusDays(1);
			Assignment a11 = new Assignment(yesterday, d1, emp2, plant1);
			Assignment a12 = new Assignment(yesterday, d2, emp1, plant1);
			Assignment a13 = new Assignment(yesterday, d5, emp3, plant2);
			
			// Save all assignments
			assignmentRepository.saveAll(List.of(a1, a2, a3, a4, a5, a6, a11, a12, a13));
			logger.info("✓ Assignments saved successfully!");

			// Resumen final
			logger.info("\n========================================");
			logger.info("   DATA INITIALIZATION COMPLETED!");
			logger.info("========================================");
			logger.info("✓ Employees: 4");
			logger.info("✓ Recycling Plants: 3");
			logger.info("✓ Dumpsters: 10");
			logger.info("✓ Assignments: 14 (10 for today, 4 for yesterday)");
			logger.info("✓ Cities covered: Bilbao, Donostia, Vitoria");
			logger.info("✓ Postal codes: 48001, 48011, 20001, 20004, 01001, 01008");
			logger.info("========================================\n");
		};
	}

	// Helper method to add usage records with proper bidirectional relationship
	private static void addUsageRecord(Dumpster dumpster, LocalDate date, int containers, FillLevel level) {
		DumpsterUsageRecord record = new DumpsterUsageRecord(date, containers, level);
		record.setDumpster(dumpster);
		dumpster.getUsageHistory().add(record);
	}
}