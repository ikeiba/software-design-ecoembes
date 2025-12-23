/**
 * This code is based on solutions provided by ChatGPT 4o and 
 * adapted using GitHub Copilot. It has been thoroughly reviewed 
 * and validated to ensure correctness and that it is free of errors.
 */
package es.deusto.sd.ecoembes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import es.deusto.sd.ecoembes.dao.AssignmentRepository;
import es.deusto.sd.ecoembes.dao.DumpsterRepository;
import es.deusto.sd.ecoembes.dao.EmployeeRepository;
import es.deusto.sd.ecoembes.dao.RecyclingPlantRepository;
import es.deusto.sd.ecoembes.entity.Assignment;
import es.deusto.sd.ecoembes.entity.Dumpster;
import es.deusto.sd.ecoembes.entity.DumpsterUsageRecord;
import es.deusto.sd.ecoembes.entity.Employee;
import es.deusto.sd.ecoembes.entity.FillLevel;
import es.deusto.sd.ecoembes.entity.RecyclingPlant;
import es.deusto.sd.ecoembes.service.EcoembesService;

@Configuration
public class DataInitializer {

	private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

		@Value("${data.init.force:false}")
		private boolean forceInit;

	@Bean
	@Transactional
	CommandLineRunner initData(EmployeeRepository employeeRepository, 
							RecyclingPlantRepository plantRepository,
							DumpsterRepository dumpsterRepository,
							AssignmentRepository assignmentRepository,
							EcoembesService ecoembesService) {
		return args -> {
			// Database guard with optional force flag
			if (!forceInit && employeeRepository.count() > 0) {
				logger.info("Database already initialized, skipping data creation (set data.init.force=true to repopulate).");
				return;
			}
			if (forceInit) {
				logger.warn("data.init.force=true -> cleaning and reinitializing seed data.");
				assignmentRepository.deleteAll();
				dumpsterRepository.deleteAll();
				plantRepository.deleteAll();
				employeeRepository.deleteAll();
				logger.info("Database cleaned. Proceeding with fresh seed data.");
			}

			// --- 1. Crear Empleados ---
			logger.info("Creating employees...");
			Employee emp1 = new Employee("Jon Ander Garcia", "jon.garcia@ecoembes.com", "password123");
			Employee emp2 = new Employee("Maialen Etxebarria", "maialen.etx@ecoembes.com", "password456");
			Employee emp3 = new Employee("Iker Ibarrola", "iker.mendoza@ecoembes.com", "password789");
			Employee emp4 = new Employee("Nerea Agirre", "nerea.agirre@ecoembes.com", "password321");
			Employee emp5 = new Employee("Bobi Gomez", "a", "a");
			Employee emp6 = new Employee("Ane Lasa", "ane.lasa@ecoembes.com", "password654");
			Employee emp7 = new Employee("Uxue Murua", "uxue.murua@ecoembes.com", "password987");
			Employee emp8 = new Employee("Martin Ruiz", "martin.ruiz@ecoembes.com", "password852");
			Employee emp9 = new Employee("David", "david.r@opendeusto.es", "a");
			
			List<Employee> employees = List.of(emp1, emp2, emp3, emp4, emp5, emp6, emp7, emp8, emp9);

			// Save employees - IDs will be auto-generated
			employeeRepository.saveAll(employees);
			logger.info("✓ {} Employees saved!", employees.size());


			// --- 2. Crear Plantas de Reciclaje ---
			logger.info("Creating recycling plants...");
			RecyclingPlant plant1 = new RecyclingPlant("PlasSB Ltd.");
			RecyclingPlant plant2 = new RecyclingPlant("ContSocket Ltd.");

			// Save plants - IDs will be auto-generated
			plantRepository.saveAll(List.of(plant1, plant2));
			logger.info("✓ 2 Recycling Plants saved!");


			// --- 3. Crear Contenedores (Dumpsters) con historial completo ---
			logger.info("Creating dumpsters with historical data...");

			// ===== BILBAO (48001 - Centro) =====
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

			// ===== BILBAO (48011 - Indautxu) =====
			Dumpster d3 = new Dumpster("Gran Via 50, Bilbao", "48011", 4000.0);
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

			// ===== DONOSTIA (20001 - Centro) =====
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

			// ===== DONOSTIA (20004 - Gros) =====
			Dumpster d7 = new Dumpster("Calle Zabaleta, Donostia", "20004", 3600.0);
			addUsageRecord(d7, LocalDate.now().minusDays(5), 800, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(4), 1100, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(3), 1400, FillLevel.GREEN);
			addUsageRecord(d7, LocalDate.now().minusDays(2), 1700, FillLevel.ORANGE);
			addUsageRecord(d7, LocalDate.now().minusDays(1), 2000, FillLevel.ORANGE);
			d7.updateStatus(2300, FillLevel.ORANGE);

			// ===== VITORIA (01001 - Centro) =====
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

			// ===== VITORIA (01008 - Lakua) =====
			Dumpster d10 = new Dumpster("Avenida Gasteiz, Vitoria", "01008", 3800.0);
			addUsageRecord(d10, LocalDate.now().minusDays(3), 2900, FillLevel.RED);
			addUsageRecord(d10, LocalDate.now().minusDays(2), 3200, FillLevel.RED);
			addUsageRecord(d10, LocalDate.now().minusDays(1), 3500, FillLevel.RED);
			d10.updateStatus(3700, FillLevel.RED);

			// ===== PAMPLONA =====
			Dumpster d11 = new Dumpster("Calle Estafeta, Pamplona", "31001", 3600.0);
			addUsageRecord(d11, LocalDate.now().minusDays(4), 1400, FillLevel.GREEN);
			addUsageRecord(d11, LocalDate.now().minusDays(3), 1800, FillLevel.ORANGE);
			addUsageRecord(d11, LocalDate.now().minusDays(2), 2100, FillLevel.ORANGE);
			addUsageRecord(d11, LocalDate.now().minusDays(1), 2400, FillLevel.ORANGE);
			d11.updateStatus(2650, FillLevel.ORANGE);

			Dumpster d12 = new Dumpster("Plaza del Castillo, Pamplona", "31001", 4000.0);
			addUsageRecord(d12, LocalDate.now().minusDays(5), 900, FillLevel.GREEN);
			addUsageRecord(d12, LocalDate.now().minusDays(4), 1200, FillLevel.GREEN);
			addUsageRecord(d12, LocalDate.now().minusDays(3), 1550, FillLevel.GREEN);
			addUsageRecord(d12, LocalDate.now().minusDays(2), 1900, FillLevel.ORANGE);
			addUsageRecord(d12, LocalDate.now().minusDays(1), 2250, FillLevel.ORANGE);
			d12.updateStatus(2500, FillLevel.ORANGE);

			// ===== SANTANDER =====
			Dumpster d13 = new Dumpster("Paseo de Pereda, Santander", "39001", 3700.0);
			addUsageRecord(d13, LocalDate.now().minusDays(6), 600, FillLevel.GREEN);
			addUsageRecord(d13, LocalDate.now().minusDays(5), 950, FillLevel.GREEN);
			addUsageRecord(d13, LocalDate.now().minusDays(4), 1300, FillLevel.GREEN);
			addUsageRecord(d13, LocalDate.now().minusDays(3), 1700, FillLevel.ORANGE);
			addUsageRecord(d13, LocalDate.now().minusDays(2), 2000, FillLevel.ORANGE);
			addUsageRecord(d13, LocalDate.now().minusDays(1), 2300, FillLevel.ORANGE);
			d13.updateStatus(2550, FillLevel.ORANGE);

			Dumpster d14 = new Dumpster("Barrio Pesquero, Santander", "39002", 3400.0);
			addUsageRecord(d14, LocalDate.now().minusDays(5), 2600, FillLevel.RED);
			addUsageRecord(d14, LocalDate.now().minusDays(4), 2800, FillLevel.RED);
			addUsageRecord(d14, LocalDate.now().minusDays(3), 3000, FillLevel.RED);
			addUsageRecord(d14, LocalDate.now().minusDays(2), 3200, FillLevel.RED);
			addUsageRecord(d14, LocalDate.now().minusDays(1), 3350, FillLevel.RED);
			d14.updateStatus(3400, FillLevel.RED);

			// ===== ZARAGOZA =====
			Dumpster d15 = new Dumpster("Plaza del Pilar, Zaragoza", "50001", 4200.0);
			addUsageRecord(d15, LocalDate.now().minusDays(4), 1600, FillLevel.GREEN);
			addUsageRecord(d15, LocalDate.now().minusDays(3), 1950, FillLevel.ORANGE);
			addUsageRecord(d15, LocalDate.now().minusDays(2), 2300, FillLevel.ORANGE);
			addUsageRecord(d15, LocalDate.now().minusDays(1), 2750, FillLevel.ORANGE);
			d15.updateStatus(3000, FillLevel.ORANGE);

			Dumpster d16 = new Dumpster("Avenida Goya, Zaragoza", "50005", 3600.0);
			addUsageRecord(d16, LocalDate.now().minusDays(6), 700, FillLevel.GREEN);
			addUsageRecord(d16, LocalDate.now().minusDays(5), 950, FillLevel.GREEN);
			addUsageRecord(d16, LocalDate.now().minusDays(4), 1250, FillLevel.GREEN);
			addUsageRecord(d16, LocalDate.now().minusDays(3), 1550, FillLevel.ORANGE);
			addUsageRecord(d16, LocalDate.now().minusDays(2), 1850, FillLevel.ORANGE);
			addUsageRecord(d16, LocalDate.now().minusDays(1), 2150, FillLevel.ORANGE);
			d16.updateStatus(2350, FillLevel.ORANGE);

			// ===== MADRID =====
			Dumpster d17 = new Dumpster("Puerta del Sol, Madrid", "28013", 5000.0);
			addUsageRecord(d17, LocalDate.now().minusDays(7), 2200, FillLevel.ORANGE);
			addUsageRecord(d17, LocalDate.now().minusDays(6), 2550, FillLevel.ORANGE);
			addUsageRecord(d17, LocalDate.now().minusDays(5), 2900, FillLevel.ORANGE);
			addUsageRecord(d17, LocalDate.now().minusDays(4), 3300, FillLevel.RED);
			addUsageRecord(d17, LocalDate.now().minusDays(3), 3600, FillLevel.RED);
			addUsageRecord(d17, LocalDate.now().minusDays(2), 3950, FillLevel.RED);
			addUsageRecord(d17, LocalDate.now().minusDays(1), 4300, FillLevel.RED);
			d17.updateStatus(4550, FillLevel.RED);

			Dumpster d18 = new Dumpster("Gran Via, Madrid", "28013", 4200.0);
			addUsageRecord(d18, LocalDate.now().minusDays(4), 900, FillLevel.GREEN);
			addUsageRecord(d18, LocalDate.now().minusDays(3), 1300, FillLevel.GREEN);
			addUsageRecord(d18, LocalDate.now().minusDays(2), 1650, FillLevel.ORANGE);
			addUsageRecord(d18, LocalDate.now().minusDays(1), 2050, FillLevel.ORANGE);
			d18.updateStatus(2300, FillLevel.ORANGE);

			// ===== BARCELONA =====
			Dumpster d19 = new Dumpster("Ramblas, Barcelona", "08002", 3800.0);
			addUsageRecord(d19, LocalDate.now().minusDays(6), 600, FillLevel.GREEN);
			addUsageRecord(d19, LocalDate.now().minusDays(5), 950, FillLevel.GREEN);
			addUsageRecord(d19, LocalDate.now().minusDays(4), 1400, FillLevel.ORANGE);
			addUsageRecord(d19, LocalDate.now().minusDays(3), 1750, FillLevel.ORANGE);
			addUsageRecord(d19, LocalDate.now().minusDays(2), 2100, FillLevel.ORANGE);
			addUsageRecord(d19, LocalDate.now().minusDays(1), 2500, FillLevel.ORANGE);
			d19.updateStatus(2700, FillLevel.ORANGE);

			Dumpster d20 = new Dumpster("Diagonal, Barcelona", "08028", 4100.0);
			addUsageRecord(d20, LocalDate.now().minusDays(5), 2600, FillLevel.ORANGE);
			addUsageRecord(d20, LocalDate.now().minusDays(4), 2950, FillLevel.ORANGE);
			addUsageRecord(d20, LocalDate.now().minusDays(3), 3300, FillLevel.RED);
			addUsageRecord(d20, LocalDate.now().minusDays(2), 3550, FillLevel.RED);
			addUsageRecord(d20, LocalDate.now().minusDays(1), 3750, FillLevel.RED);
			d20.updateStatus(3950, FillLevel.RED);

			List<Dumpster> dumpsters = List.of(
				d1, d2, d3, d4, d5, d6, d7, d8, d9, d10,
				d11, d12, d13, d14, d15, d16, d17, d18, d19, d20);

			// Save all dumpsters - usage history will be persisted due to CascadeType.ALL
			dumpsterRepository.saveAll(dumpsters);
			logger.info("✓ {} Dumpsters saved with complete historical data!", dumpsters.size());

			// --- 4. Asignar contenedores a plantas y notificar ---
			logger.info("Assigning dumpsters to recycling plants and notifying external systems...");
			LocalDate today = LocalDate.now();
			LocalDate yesterday = LocalDate.now().minusDays(1);
			LocalDate twoDaysAgo = LocalDate.now().minusDays(2);

			List<Assignment> assignments = new ArrayList<>();
			assignments.add(assignAndNotify(ecoembesService, today, d1, emp1, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d2, emp1, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d3, emp2, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d4, emp4, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d5, emp2, plant2));
			assignments.add(assignAndNotify(ecoembesService, today, d6, emp3, plant2));
			assignments.add(assignAndNotify(ecoembesService, today, d7, emp3, plant2));
			assignments.add(assignAndNotify(ecoembesService, today, d15, emp7, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d17, emp6, plant1));
			assignments.add(assignAndNotify(ecoembesService, today, d19, emp8, plant2));

			assignments.add(assignAndNotify(ecoembesService, yesterday, d1, emp2, plant1));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d2, emp1, plant1));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d5, emp3, plant2));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d8, emp4, plant1));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d10, emp5, plant1));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d11, emp6, plant2));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d12, emp7, plant2));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d13, emp8, plant1));
			assignments.add(assignAndNotify(ecoembesService, yesterday, d14, emp5, plant2));

			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d3, emp4, plant1));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d4, emp1, plant1));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d6, emp2, plant2));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d9, emp6, plant1));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d15, emp7, plant1));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d16, emp8, plant2));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d18, emp3, plant1));
			assignments.add(assignAndNotify(ecoembesService, twoDaysAgo, d20, emp2, plant2));

			logger.info("✓ {} Assignments sent and persisted!", assignments.size());

			// Resumen final
			logger.info("\n========================================");
			logger.info("   DATA INITIALIZATION COMPLETED!");
			logger.info("========================================");
			logger.info("✓ Employees: {}", employees.size());
			logger.info("✓ Recycling Plants: {}", 2);
			logger.info("✓ Dumpsters: {}", dumpsters.size());
			logger.info("✓ Assignments: {} (spread across today, yesterday and two days ago)", assignments.size());
			logger.info("✓ Cities covered: Bilbao, Donostia, Vitoria, Pamplona, Santander, Zaragoza, Madrid, Barcelona");
			logger.info("✓ Postal codes: {}", Set.of("48001", "48011", "20001", "20004", "01001", "01008", "31001", "39001", "39002", "50001", "50005", "28013", "08002", "08028"));
			logger.info("========================================\n");
		};
	}

	private static Assignment assignAndNotify(EcoembesService ecoembesService, LocalDate date, Dumpster dumpster, Employee employee, RecyclingPlant plant) {
		if (dumpster.getId() == null || employee.getId() == null || plant.getId() == null) {
			throw new IllegalStateException("Entities must be persisted before assignment");
		}
		return ecoembesService.assignDumpsterToPlant(date, dumpster.getId(), employee.getId(), plant.getId());
	}

	// Helper method to add usage records with proper bidirectional relationship
	private static void addUsageRecord(Dumpster dumpster, LocalDate date, int containers, FillLevel level) {
		DumpsterUsageRecord record = new DumpsterUsageRecord(date, containers, level);
		record.setDumpster(dumpster);
		dumpster.getUsageHistory().add(record);
	}
}