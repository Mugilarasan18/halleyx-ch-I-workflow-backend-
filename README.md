🚀 Dynamic Multi-Level Workflow Approval System

A full-stack enterprise application designed to handle dynamic, multi-tier approval workflows (such as Leave Requests, Expense Claims, etc.). The system features a robust Spring Boot rule engine that automatically routes user submissions to the appropriate authority (Admin/HR or CEO) based on custom-defined logic, alongside a fully responsive React.js frontend.

---

 🛠️ Tech Stack
* **Frontend:** React.js, Axios, React Router, CSS3
* **Backend:** Java 17+, Spring Boot 3.x, Spring Data JPA, Lombok
* **Database:** MySQL
* **Architecture:** RESTful APIs, Custom Rule-based Engine

---

 ✨ Key Features
1. **Role-Based Access Control (RBAC):** Secure login and routing for `USER`, `ADMIN`, and `CEO` roles.
2. **Dynamic Workflow Designer:** Admins can create workflows on the fly with custom rules (e.g., `#amount > 10000`).
3. **Smart Rule Engine:** Evaluates user inputs in real-time to determine if the workflow should stop at Level 1 or proceed to Level 2.
4. **Multi-Level Routing:** * **Level 1:** `ADMIN` (Initial Verification)
    * **Level 2:** `CEO` (Final Executive Approval)
5. **Context-Aware Dashboards:** Input labels automatically adapt based on the workflow context (e.g., automatically changing to "Duration (Days)" for Leave workflows, or "Amount (₹)" for Expense workflows).
6. **Real-time Analytics:** CEO and Admin dashboards auto-refresh every 10-15 seconds to show pending actions and complete system history without manual reloads.

---

 📂 Complete Project Structure

 Backend (Spring Boot)

src/main/java/com/halleyx/workflow/
├── controller/     # API Endpoints (ExecutionController, WorkflowController, AuthController)
├── entity/         # Database Models (Workflow, Step, Rule, Execution)
├── repository/     # JPA Repositories for Database Queries
├── service/        # Core Engine Logic (WorkflowExecutionService, RuleEngineService)
└── dto/            # Data Transfer Objects


Frontend (React.js)Plaintextsrc/
├── api/
│   └── axiosConfig.js      # Global API configuration with Base URL
├── components/
│   └── Navbar.jsx          # Contextual Navigation & Logout functionality
├── pages/
│   ├── Login.jsx           # Role-based authentication entry
│   ├── Register.jsx        # Account creation & Role assignment
│   ├── UserDashboard.jsx   # Employee submission portal & Status tracking
│   ├── AdminDashboard.jsx  # Level 1 Approval & Workflow Creator
│   └── CeoDashboard.jsx    # Level 2 Approval & Full System Analytics
├── App.jsx                 # Centralized Routing logic
├── main.jsx                # React Entry Point
└── index.css               # Global styles and UI formatting
Core Engine Flow (How it works)Trigger: A User selects a workflow (e.g., Sick Leave) and submits data. The execution status is immediately set to PENDING_ADMIN. The engine pauses.Level 1 (Admin Review): The Admin reviews the cleanly parsed key-value data on their dashboard. Upon clicking "Approve", the Java Engine resumes, evaluates the custom rules attached to that workflow, and identifies the next step.Level 2 (CEO Review): If the rule condition mandates executive review (e.g., the amount is too high, or days are too long), the status becomes PENDING_CEO. The engine pauses again.Completion: The CEO approves the request from their dashboard, and the workflow successfully concludes with a COMPLETED status. If rejected at any point, the status becomes REJECTED.📡 Essential API Endpoints1. AuthenticationMethodEndpointDescriptionPOST/api/auth/registerRegister a new User/Admin/CEO.POST/api/auth/loginAuthenticate and retrieve user role and ID.2. Workflow ManagementMethodEndpointDescriptionPOST/api/workflowsPublish a new workflow configuration.GET/api/workflowsFetch all active workflows for users to apply.3. Execution & ApprovalsMethodEndpointDescriptionPOST/api/executions/start/{id}Initiate a new workflow request (User).GET/api/executions/pending?role={ROLE}Fetch pending tasks for specific roles (Admin/CEO).POST/api/executions/{id}/approveProcess an approval or rejection with comments.GET/api/executions/all-historyRetrieve full system analytics and logs (CEO).⚙️ Setup & Installation Instructions1. Database SetupEnsure MySQL is running. Update your src/main/resources/application.properties in the Spring Boot project:Propertiesspring.datasource.url=jdbc:mysql://localhost:3306/workflow_db

spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

2. Running the Backend (Spring Boot)Open your terminal, navigate to the backend root directory, and run:Bash./mvnw clean install
   
./mvnw spring-boot:run

The backend will start on http://localhost:80803. Running the Frontend (React.js)Open a new terminal, navigate to the frontend root directory, and run:Bashnpm install

npm run dev

The frontend will start on your local Vite/React port (usually http://localhost:5173)📝 Usage & Design Guidelines for AdminsWhen designing a workflow via the Admin Dashboard, use the following strict conventions for the Java engine to route correctly:Admin Step Naming: Must contain the exact keyword ADMIN (e.g., ADMIN_REVIEW, VERIFY_ADMIN).CEO Step Naming: Must contain the exact keyword CEO (e.g., CEO_APPROVAL, FINAL_CEO).Rule Syntax: Follow standard SpEL or your custom evaluator syntax (e.g., #amount > 5000 or #leaveDays > 3).
Architected and Developed by Mugil
