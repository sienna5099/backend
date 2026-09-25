package com.aicareermentor.backend;

import com.aicareermentor.backend.entity.Career;
import com.aicareermentor.backend.entity.CareerSkill;
import com.aicareermentor.backend.entity.Skill;
import com.aicareermentor.backend.entity.SkillAssessmentQuestion;
import com.aicareermentor.backend.repository.CareerSkillRepository;
import com.aicareermentor.backend.repository.CareerRepository;
import com.aicareermentor.backend.repository.SkillRepository;
import com.aicareermentor.backend.repository.SkillAssessmentQuestionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    @Bean
        @Order(1)
    CommandLineRunner initializeCareers(CareerRepository careerRepository) {

        return args -> {

            if (careerRepository.count() > 0) {
                return;
            }

            Career dataAnalyst = new Career();
            dataAnalyst.setCareerId("data-analyst");
            dataAnalyst.setTitle("Data Analyst");
            dataAnalyst.setTagline("Turn data into meaningful insights.");
            dataAnalyst.setDescription(
                    "Analyze data, identify patterns and help organizations make better decisions."
            );
            dataAnalyst.setGrowth(
                    "Strong growth opportunities across technology, finance, healthcare and consulting."
            );
            dataAnalyst.setSalaryRange("₹4 LPA - ₹12 LPA");
            dataAnalyst.setSkills("SQL, Excel, Python, Data Visualization, Statistics");
            dataAnalyst.setResponsibilities(
                    "Collect and analyze data, create reports, identify trends, build dashboards"
            );
            dataAnalyst.setAvgSalary("₹7 LPA");
            dataAnalyst.setDemand("High");
            dataAnalyst.setTopLocation("Bengaluru");
            dataAnalyst.setGlobalOpportunities(
                    "Technology, finance, healthcare, consulting and e-commerce"
            );
            dataAnalyst.setAnalyticalScore(95);
            dataAnalyst.setTechnicalScore(70);
            dataAnalyst.setCommunicationScore(75);
            dataAnalyst.setLeadershipScore(55);
            dataAnalyst.setCreativeScore(65);

            careerRepository.save(dataAnalyst);


            Career softwareDeveloper = new Career();
            softwareDeveloper.setCareerId("software-developer");
            softwareDeveloper.setTitle("Software Developer");
            softwareDeveloper.setTagline("Build applications and solve technical problems.");
            softwareDeveloper.setDescription(
                    "Design, develop, test and maintain software applications."
            );
            softwareDeveloper.setGrowth(
                    "Excellent growth opportunities across technology companies and startups."
            );
            softwareDeveloper.setSalaryRange("₹4 LPA - ₹15 LPA");
            softwareDeveloper.setSkills(
                    "Java, Python, JavaScript, Spring Boot, React, SQL, Git"
            );
            softwareDeveloper.setResponsibilities(
                    "Design software, write code, test applications, fix bugs and maintain systems"
            );
            softwareDeveloper.setAvgSalary("₹8 LPA");
            softwareDeveloper.setDemand("Very High");
            softwareDeveloper.setTopLocation("Bengaluru");
            softwareDeveloper.setGlobalOpportunities(
                    "Software companies, startups, fintech, cloud computing and IT services"
            );
            softwareDeveloper.setAnalyticalScore(80);
            softwareDeveloper.setTechnicalScore(98);
            softwareDeveloper.setCommunicationScore(65);
            softwareDeveloper.setLeadershipScore(55);
            softwareDeveloper.setCreativeScore(75);

            careerRepository.save(softwareDeveloper);


            Career projectManager = new Career();
            projectManager.setCareerId("project-manager");
            projectManager.setTitle("Project Manager");
            projectManager.setTagline("Lead teams and deliver projects successfully.");
            projectManager.setDescription(
                    "Plan projects, coordinate teams, manage risks and ensure successful delivery."
            );
            projectManager.setGrowth(
                    "Strong opportunities in technology, consulting and business operations."
            );
            projectManager.setSalaryRange("₹6 LPA - ₹18 LPA");
            projectManager.setSkills(
                    "Project Planning, Communication, Leadership, Risk Management, Agile"
            );
            projectManager.setResponsibilities(
                    "Plan projects, coordinate teams, manage schedules, track risks and communicate progress"
            );
            projectManager.setAvgSalary("₹11 LPA");
            projectManager.setDemand("High");
            projectManager.setTopLocation("Mumbai");
            projectManager.setGlobalOpportunities(
                    "Technology, consulting, finance, construction and business operations"
            );
            projectManager.setAnalyticalScore(75);
            projectManager.setTechnicalScore(55);
            projectManager.setCommunicationScore(90);
            projectManager.setLeadershipScore(95);
            projectManager.setCreativeScore(70);

            careerRepository.save(projectManager);


            Career productManager = new Career();
            productManager.setCareerId("product-manager");
            productManager.setTitle("Product Manager");
            productManager.setTagline("Turn ideas into products people love.");
            productManager.setDescription(
                    "Define product strategy, understand users and work with teams to build useful products."
            );
            productManager.setGrowth(
                    "High-growth career path with opportunities in technology and startups."
            );
            productManager.setSalaryRange("₹8 LPA - ₹25 LPA");
            productManager.setSkills(
                    "Product Strategy, User Research, Communication, Analytics, Agile"
            );
            productManager.setResponsibilities(
                    "Define product strategy, research users, prioritize features and coordinate development teams"
            );
            productManager.setAvgSalary("₹14 LPA");
            productManager.setDemand("Very High");
            productManager.setTopLocation("Bengaluru");
            productManager.setGlobalOpportunities(
                    "Technology companies, startups, SaaS, fintech and e-commerce"
            );
            productManager.setAnalyticalScore(85);
            productManager.setTechnicalScore(65);
            productManager.setCommunicationScore(95);
            productManager.setLeadershipScore(90);
            productManager.setCreativeScore(90);

            careerRepository.save(productManager);


            Career hrSpecialist = new Career();
            hrSpecialist.setCareerId("hr-specialist");
            hrSpecialist.setTitle("HR Specialist");
            hrSpecialist.setTagline("Help people and organizations grow together.");
            hrSpecialist.setDescription(
                    "Support recruitment, employee development, workplace policies and people operations."
            );
            hrSpecialist.setGrowth(
                    "Opportunities across almost every industry and organization."
            );
            hrSpecialist.setSalaryRange("₹3.5 LPA - ₹10 LPA");
            hrSpecialist.setSkills(
                    "Communication, Recruitment, Employee Relations, Organization, Conflict Resolution"
            );
            hrSpecialist.setResponsibilities(
                    "Recruit employees, support employee development, manage workplace policies and assist people operations"
            );
            hrSpecialist.setAvgSalary("₹6 LPA");
            hrSpecialist.setDemand("High");
            hrSpecialist.setTopLocation("Mumbai");
            hrSpecialist.setGlobalOpportunities(
                    "Technology, healthcare, finance, consulting, education and retail"
            );
            hrSpecialist.setAnalyticalScore(60);
            hrSpecialist.setTechnicalScore(35);
            hrSpecialist.setCommunicationScore(95);
            hrSpecialist.setLeadershipScore(80);
            hrSpecialist.setCreativeScore(70);

            careerRepository.save(hrSpecialist);


            Career cybersecurityAnalyst = new Career();
            cybersecurityAnalyst.setCareerId("cybersecurity-analyst");
            cybersecurityAnalyst.setTitle("Cybersecurity Analyst");
            cybersecurityAnalyst.setTagline("Protect systems, data and organizations.");
            cybersecurityAnalyst.setDescription(
                    "Monitor security systems, investigate threats and help protect digital infrastructure."
            );
            cybersecurityAnalyst.setGrowth(
                    "Strong demand as organizations increasingly prioritize cybersecurity."
            );
            cybersecurityAnalyst.setSalaryRange("₹5 LPA - ₹18 LPA");
            cybersecurityAnalyst.setSkills(
                    "Network Security, Ethical Hacking, Linux, SIEM, Cryptography"
            );
            cybersecurityAnalyst.setResponsibilities(
                    "Monitor security systems, investigate threats, identify vulnerabilities and protect digital infrastructure"
            );
            cybersecurityAnalyst.setAvgSalary("₹9 LPA");
            cybersecurityAnalyst.setDemand("Very High");
            cybersecurityAnalyst.setTopLocation("Bengaluru");
            cybersecurityAnalyst.setGlobalOpportunities(
                    "Cybersecurity firms, banks, technology companies, government and consulting"
            );
            cybersecurityAnalyst.setAnalyticalScore(90);
            cybersecurityAnalyst.setTechnicalScore(95);
            cybersecurityAnalyst.setCommunicationScore(60);
            cybersecurityAnalyst.setLeadershipScore(65);
            cybersecurityAnalyst.setCreativeScore(70);

            careerRepository.save(cybersecurityAnalyst);


            System.out.println("Career data initialized successfully.");
        };
    }

    @Bean
    @Order(2)
    CommandLineRunner initializeCareerSkills(
            CareerRepository careerRepository,
            SkillRepository skillRepository,
            CareerSkillRepository careerSkillRepository) {

        return args -> {
            Map<String, List<String>> skillsByCareer = new LinkedHashMap<>();
            skillsByCareer.put("data-analyst", List.of(
                    "SQL", "Excel", "Python", "Data Visualization", "Statistics", "Power BI", "Git"));
            skillsByCareer.put("software-developer", List.of(
                    "Java", "Python", "JavaScript", "Spring Boot", "React", "SQL", "Git"));
            skillsByCareer.put("project-manager", List.of(
                    "Project Planning", "Communication", "Leadership", "Risk Management", "Agile"));
            skillsByCareer.put("product-manager", List.of(
                    "Product Strategy", "User Research", "Communication", "Analytics", "Agile"));
            skillsByCareer.put("hr-specialist", List.of(
                    "Communication", "Recruitment", "Employee Relations", "Organization", "Conflict Resolution"));
            skillsByCareer.put("cybersecurity-analyst", List.of(
                    "Network Security", "Ethical Hacking", "Linux", "SIEM", "Cryptography"));

            skillsByCareer.forEach((careerId, skillNames) -> {
                Career career = careerRepository.findByCareerId(careerId)
                        .orElseThrow(() -> new IllegalStateException(
                                "Career not found while seeding skills: " + careerId));

                for (int index = 0; index < skillNames.size(); index++) {
                    String skillName = skillNames.get(index);
                    Skill skill = skillRepository.findByName(skillName)
                            .orElseGet(() -> skillRepository.save(new Skill(skillName, null)));

                    if (!careerSkillRepository.existsByCareer_IdAndSkill_Id(career.getId(), skill.getId())) {
                        careerSkillRepository.save(new CareerSkill(career, skill, index + 1));
                    }
                }
            });
        };
    }

    @Bean
    @Order(3)
    CommandLineRunner initializeSkillAssessmentQuestions(
            CareerRepository careerRepository,
            SkillRepository skillRepository,
            SkillAssessmentQuestionRepository questionRepository) {

        return args -> {
            seedQuestions(careerRepository, skillRepository, questionRepository, "data-analyst", List.of(
                    new SkillQuestionSeed("SQL", "How comfortable are you writing SQL queries to retrieve and filter data?"),
                    new SkillQuestionSeed("Excel", "How comfortable are you using Excel formulas, sorting, filtering, and basic data analysis?"),
                    new SkillQuestionSeed("Python", "How comfortable are you using Python for basic data analysis?"),
                    new SkillQuestionSeed("Data Visualization", "How comfortable are you creating charts or dashboards to communicate data insights?"),
                    new SkillQuestionSeed("Statistics", "How comfortable are you with averages, medians, percentages, and correlation?"),
                    new SkillQuestionSeed("Power BI", "How comfortable are you creating basic reports or dashboards in Power BI?"),
                    new SkillQuestionSeed("Git", "How comfortable are you using Git or GitHub to manage project work?")));

            seedQuestions(careerRepository, skillRepository, questionRepository, "software-developer", List.of(
                    new SkillQuestionSeed("Java", "How comfortable are you writing and understanding basic Java programs?"),
                    new SkillQuestionSeed("Python", "How comfortable are you writing Python code to solve simple problems?"),
                    new SkillQuestionSeed("JavaScript", "How comfortable are you using JavaScript to add behavior to a web application?"),
                    new SkillQuestionSeed("Spring Boot", "How comfortable are you creating a basic REST endpoint with Spring Boot?"),
                    new SkillQuestionSeed("React", "How comfortable are you building a simple React component?"),
                    new SkillQuestionSeed("SQL", "How comfortable are you writing SQL queries to read and update data?"),
                    new SkillQuestionSeed("Git", "How comfortable are you using Git branches, commits, and pull requests?")));

            seedQuestions(careerRepository, skillRepository, questionRepository, "project-manager", List.of(
                    new SkillQuestionSeed("Project Planning", "How comfortable are you breaking a project into tasks, milestones, and timelines?"),
                    new SkillQuestionSeed("Communication", "How comfortable are you sharing clear updates with different stakeholders?"),
                    new SkillQuestionSeed("Leadership", "How comfortable are you coordinating a team toward a shared outcome?"),
                    new SkillQuestionSeed("Risk Management", "How comfortable are you identifying project risks and planning responses?"),
                    new SkillQuestionSeed("Agile", "How comfortable are you working with Agile practices such as sprints and retrospectives?")));

            seedQuestions(careerRepository, skillRepository, questionRepository, "product-manager", List.of(
                    new SkillQuestionSeed("Product Strategy", "How comfortable are you defining a product goal and prioritizing valuable outcomes?"),
                    new SkillQuestionSeed("User Research", "How comfortable are you gathering and interpreting feedback from users?"),
                    new SkillQuestionSeed("Communication", "How comfortable are you explaining product decisions to technical and non-technical teams?"),
                    new SkillQuestionSeed("Analytics", "How comfortable are you using product data to evaluate decisions?"),
                    new SkillQuestionSeed("Agile", "How comfortable are you collaborating with an Agile product development team?")));

            seedQuestions(careerRepository, skillRepository, questionRepository, "hr-specialist", List.of(
                    new SkillQuestionSeed("Communication", "How comfortable are you handling clear and respectful workplace conversations?"),
                    new SkillQuestionSeed("Recruitment", "How comfortable are you supporting candidate screening and interview coordination?"),
                    new SkillQuestionSeed("Employee Relations", "How comfortable are you listening to employee concerns and documenting follow-up?"),
                    new SkillQuestionSeed("Organization", "How comfortable are you maintaining accurate employee records and processes?"),
                    new SkillQuestionSeed("Conflict Resolution", "How comfortable are you helping people work through a disagreement fairly?")));

            seedQuestions(careerRepository, skillRepository, questionRepository, "cybersecurity-analyst", List.of(
                    new SkillQuestionSeed("Network Security", "How comfortable are you recognizing common network security risks?"),
                    new SkillQuestionSeed("Ethical Hacking", "How comfortable are you describing safe and authorized security testing practices?"),
                    new SkillQuestionSeed("Linux", "How comfortable are you using basic Linux commands and navigating a Linux system?"),
                    new SkillQuestionSeed("SIEM", "How comfortable are you reviewing security logs in a monitoring or SIEM tool?"),
                    new SkillQuestionSeed("Cryptography", "How comfortable are you explaining the purpose of encryption and hashing?")));
        };
    }

    private void seedQuestions(
            CareerRepository careerRepository,
            SkillRepository skillRepository,
            SkillAssessmentQuestionRepository questionRepository,
            String careerId,
            List<SkillQuestionSeed> seeds) {
        Career career = careerRepository.findByCareerId(careerId)
                .orElseThrow(() -> new IllegalStateException(
                        "Career not found while seeding questions: " + careerId));

        for (int index = 0; index < seeds.size(); index++) {
            SkillQuestionSeed seed = seeds.get(index);
                        int sequence = index + 1;
            Skill skill = skillRepository.findByName(seed.skillName())
                    .orElseThrow(() -> new IllegalStateException(
                            "Skill not found while seeding questions: " + seed.skillName()));
            questionRepository.findByCareer_CareerIdAndSkill_NameAndSequenceNumber(
                            career.getCareerId(), skill.getName(), sequence)
                    .orElseGet(() -> questionRepository.save(new SkillAssessmentQuestion(
                            career, skill, seed.question(), sequence, true)));
        }
    }

    private record SkillQuestionSeed(String skillName, String question) {
    }
}
