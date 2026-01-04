package es.deusto.sd.client.web;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.deusto.sd.client.data.Assignment;
import es.deusto.sd.client.data.DumpsterStatus;
import es.deusto.sd.client.data.Login;
import es.deusto.sd.client.data.NewDumpster;
import es.deusto.sd.client.data.PlantCapacity;
import es.deusto.sd.client.proxies.IEcoembesServiceProxy;

@Controller
public class WebClientController {

    @Autowired
    private IEcoembesServiceProxy ecoembesServiceProxy;

    private String token; // Token de sesión en memoria

    @ModelAttribute("token")
    public String getToken() {
        return token;
    }

    // ================== LOGIN / HOME ==================

    @GetMapping("/")
    public String home(Model model) {
        if (token != null) {
            return "redirect:/dashboard";
        }
        return "login"; // Si no hay token, mostramos login
    }

    @PostMapping("/login")
    public String login(@RequestParam("email") String email, 
                        @RequestParam("password") String password, 
                        Model model, 
                        RedirectAttributes redirectAttributes) {
        try {
            Login creds = new Login(email, password);
            this.token = ecoembesServiceProxy.login(creds);
            return "redirect:/dashboard";
        } catch (Exception e) {
            model.addAttribute("error", "Login error: " + e.getMessage());
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        try {
            if (token != null) {
                ecoembesServiceProxy.logout(token);
            }
            token = null;
            redirectAttributes.addFlashAttribute("message", "You have successfully logged out.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error logging out.");
        }
        return "redirect:/";
    }

    // ================== DASHBOARD ==================

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        if (token == null) return "redirect:/";
        return "dashboard"; // Renderiza dashboard.html (Menú principal)
    }

    // ================== GESTIÓN DE CONTENEDORES ==================

    @GetMapping("/dumpsters/new")
    public String showCreateDumpsterForm(Model model) {
        if (token == null) return "redirect:/";
        model.addAttribute("newDumpster", new NewDumpster());
        return "create-dumpster"; // Renderiza create-dumpster.html
    }

    @PostMapping("/dumpsters/new")
    public String createDumpster(@ModelAttribute NewDumpster newDumpster, 
                                 RedirectAttributes redirectAttributes) {
        if (token == null) return "redirect:/";
        try {
            ecoembesServiceProxy.createDumpster(newDumpster);
            redirectAttributes.addFlashAttribute("message", "Dumpster created successfully.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating dumpster: " + e.getMessage());
            return "redirect:/dumpsters/new";
        }
    }

    // ================== CONSULTA DE ESTADO (Por C.P. y Fecha) ==================

    @GetMapping("/dumpsters/status")
    public String showDumpsterStatus(@RequestParam(value = "postalCode", required = false) String postalCode,
                                     @RequestParam(value = "date", required = false) String dateStr,
                                     Model model) {
        if (token == null) return "redirect:/";

        if (postalCode != null && !postalCode.isBlank() && dateStr != null && !dateStr.isBlank()) {
            try {
                LocalDate date = LocalDate.parse(dateStr);
                List<DumpsterStatus> dumpsters = ecoembesServiceProxy.getDumpsterStatus(postalCode, date);
                model.addAttribute("dumpsters", dumpsters);
                model.addAttribute("selectedDate", dateStr);
                model.addAttribute("selectedZip", postalCode);
            } catch (DateTimeParseException e) {
                model.addAttribute("error", "Invalid date format.");
            } catch (Exception e) {
                model.addAttribute("error", "Error getting status: " + e.getMessage());
            }
        }
        return "dumpster-status"; // Renderiza dumpster-status.html
    }

    // ================== CAPACIDAD DE PLANTAS ==================

    @GetMapping("/plants/capacity")
    public String showPlantCapacity(@RequestParam(value = "date", required = false) String dateStr,
                                    Model model) {
        if (token == null) return "redirect:/";

        // Si no se pasa fecha, usamos la de hoy por defecto para mostrar algo
        LocalDate date = (dateStr != null && !dateStr.isBlank()) ? LocalDate.parse(dateStr) : LocalDate.now();
        
        try {
            List<PlantCapacity> capacities = ecoembesServiceProxy.getPlantsCapacity(date);
            model.addAttribute("capacities", capacities);
            model.addAttribute("selectedDate", date.toString());
        } catch (Exception e) {
            model.addAttribute("error", "Error getting capacities: " + e.getMessage());
        }

        return "plant-capacity"; // Renderiza plant-capacity.html
    }

    // ================== ASIGNACIÓN (DUMPSTER -> PLANTA) ==================

    @GetMapping("/assignments/new")
    public String showAssignmentForm(Model model) {
        if (token == null) return "redirect:/";
        
        // Inicializamos un  vacío para el formulario
        Assignment assignment = new Assignment();
        // Por defecto, ponemos la fecha de hoy para facilitar al usuario
        assignment.setDate(LocalDate.now()); 
        
        model.addAttribute("assignment", assignment);
        return "create-assignment"; // Renderiza create-assignment.html
    }

    @PostMapping("/assignments/new")
    public String createAssignment(@ModelAttribute Assignment assignment,
                                   RedirectAttributes redirectAttributes) {
        if (token == null) return "redirect:/";

        try {
            // Important: Inject the token into the assignment before sending
            // because the server expects it inside the JSON object.
            assignment.setToken(this.token);

            ecoembesServiceProxy.assignDumpster(assignment);
            
            redirectAttributes.addFlashAttribute("message", "Assignment completed successfully.");
            return "redirect:/dashboard";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Assignment failed: " + e.getMessage());
            return "redirect:/assignments/new";
        }
    }
}