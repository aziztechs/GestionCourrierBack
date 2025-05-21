package sn.coud.gestioncourrierback.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sn.coud.gestioncourrierback.dto.CourrierDTO;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.dto.UserDTO;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.service.CourrierService;
import sn.coud.gestioncourrierback.service.SuiviService;
import sn.coud.gestioncourrierback.service.UserService;

import java.time.LocalDate;

/**
 * Component to initialize the database with sample data.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final CourrierService courrierService;
    private final SuiviService suiviService;

    @Override
    public void run(String... args) throws Exception {
        // Initialize sample data only if the database is empty
        if (userService.getAllUsers().isEmpty()) {
            System.out.println("Initializing sample data...");
            initializeUsers();
            initializeCourriers();
            System.out.println("Sample data initialization completed.");
        } else {
            System.out.println("Database already contains data. Skipping initialization.");
        }
    }

    private void initializeUsers() {
        // Create admin user
        UserDTO admin = new UserDTO();
        admin.setNom("Admin");
        admin.setPrenom("System");
        admin.setUsername("admin");
        admin.setMatricule("ADM001");
        admin.setActive(true);
        admin.setRoleFonction("Administrateur");
        admin.setEmail("admin@coud.sn");
        admin.setPassword("admin123");
        admin.setTelephone("771234567");
        
        // Create regular users
        UserDTO user1 = new UserDTO();
        user1.setNom("Diop");
        user1.setPrenom("Mamadou");
        user1.setUsername("mdiop");
        user1.setMatricule("EMP001");
        user1.setActive(true);
        user1.setRoleFonction("Secrétaire");
        user1.setEmail("mdiop@coud.sn");
        user1.setPassword("password123");
        user1.setTelephone("772345678");
        
        UserDTO user2 = new UserDTO();
        user2.setNom("Ndiaye");
        user2.setPrenom("Fatou");
        user2.setUsername("fndiaye");
        user2.setMatricule("EMP002");
        user2.setActive(true);
        user2.setRoleFonction("Gestionnaire");
        user2.setEmail("fndiaye@coud.sn");
        user2.setPassword("password123");
        user2.setTelephone("773456789");
        
        UserDTO user3 = new UserDTO();
        user3.setNom("Sow");
        user3.setPrenom("Abdoulaye");
        user3.setUsername("asow");
        user3.setMatricule("EMP003");
        user3.setActive(true);
        user3.setRoleFonction("Directeur");
        user3.setEmail("asow@coud.sn");
        user3.setPassword("password123");
        user3.setTelephone("774567890");
        
        // Save users
        try {
            userService.createUser(admin);
            userService.createUser(user1);
            userService.createUser(user2);
            userService.createUser(user3);
            System.out.println("Users created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating users: " + e.getMessage());
        }
    }

    private void initializeCourriers() {
        // Create courriers
        CourrierDTO courrier1 = new CourrierDTO();
        courrier1.setNumCourrier("COUD-2023-001");
        courrier1.setObjet("Demande de fournitures");
        courrier1.setType(TypeCourrier.INTERNE);
        courrier1.setNature(NatureCourrier.DEPART);
        courrier1.setDestinataire("Service Logistique");
        courrier1.setExpediteur("Direction Générale");
        courrier1.setDate(LocalDate.now().minusDays(5));
        
        CourrierDTO courrier2 = new CourrierDTO();
        courrier2.setNumCourrier("COUD-2023-002");
        courrier2.setObjet("Invitation à la réunion annuelle");
        courrier2.setType(TypeCourrier.EXTERNE);
        courrier2.setNature(NatureCourrier.DEPART);
        courrier2.setDestinataire("Ministère de l'Éducation");
        courrier2.setExpediteur("Direction Générale");
        courrier2.setDate(LocalDate.now().minusDays(3));
        
        CourrierDTO courrier3 = new CourrierDTO();
        courrier3.setNumCourrier("COUD-2023-003");
        courrier3.setObjet("Rapport financier trimestriel");
        courrier3.setType(TypeCourrier.EXTERNE);
        courrier3.setNature(NatureCourrier.ARRIVE);
        courrier3.setDestinataire("Direction Générale");
        courrier3.setExpediteur("Ministère des Finances");
        courrier3.setDate(LocalDate.now().minusDays(1));
        
        CourrierDTO courrier4 = new CourrierDTO();
        courrier4.setNumCourrier("COUD-2023-004");
        courrier4.setObjet("Note de service - Horaires d'été");
        courrier4.setType(TypeCourrier.INTERNE);
        courrier4.setNature(NatureCourrier.DEPART);
        courrier4.setDestinataire("Tous les services");
        courrier4.setExpediteur("Ressources Humaines");
        courrier4.setDate(LocalDate.now());
        
        // Save courriers
        try {
            CourrierDTO savedCourrier1 = courrierService.createCourrier(courrier1);
            CourrierDTO savedCourrier2 = courrierService.createCourrier(courrier2);
            CourrierDTO savedCourrier3 = courrierService.createCourrier(courrier3);
            CourrierDTO savedCourrier4 = courrierService.createCourrier(courrier4);
            
            // Create suivis for courriers
            createSuivis(savedCourrier1.getId(), savedCourrier2.getId(), savedCourrier3.getId());
            
            System.out.println("Courriers created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating courriers: " + e.getMessage());
        }
    }
    
    private void createSuivis(Long courrierId1, Long courrierId2, Long courrierId3) {
        // Create suivis for courrier 1
        SuiviDTO suivi1 = new SuiviDTO();
        suivi1.setCourrierId(courrierId1);
        suivi1.setInstruction("À traiter en urgence");
        suivi1.setDescription("Besoin de fournitures pour le nouveau bureau");
        suivi1.setDate(LocalDate.now().minusDays(5));
        
        SuiviDTO suivi2 = new SuiviDTO();
        suivi2.setCourrierId(courrierId1);
        suivi2.setInstruction("Transmis au service concerné");
        suivi2.setDescription("En attente de validation");
        suivi2.setDate(LocalDate.now().minusDays(4));
        
        // Create suivis for courrier 2
        SuiviDTO suivi3 = new SuiviDTO();
        suivi3.setCourrierId(courrierId2);
        suivi3.setInstruction("Préparation des documents");
        suivi3.setDescription("Préparer les documents pour la réunion");
        suivi3.setDate(LocalDate.now().minusDays(3));
        
        // Create suivis for courrier 3
        SuiviDTO suivi4 = new SuiviDTO();
        suivi4.setCourrierId(courrierId3);
        suivi4.setInstruction("À analyser");
        suivi4.setDescription("Analyser le rapport et préparer une synthèse");
        suivi4.setDate(LocalDate.now().minusDays(1));
        
        // Save suivis
        try {
            suiviService.createSuivi(suivi1);
            suiviService.createSuivi(suivi2);
            suiviService.createSuivi(suivi3);
            suiviService.createSuivi(suivi4);
            System.out.println("Suivis created successfully.");
        } catch (Exception e) {
            System.err.println("Error creating suivis: " + e.getMessage());
        }
    }
}