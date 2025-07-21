package com.jws.resume.model

import com.jws.resume.data.entities.EducationEntry
import com.jws.resume.data.entities.HomeInfo
import com.jws.resume.data.entities.JobExperience
import com.jws.resume.data.entities.Reference
import com.jws.resume.data.entities.ResumeProfile
import com.jws.resume.data.entities.Skill

val mockResumeData = Resume(
    resume = ResumeProfile(
        homeInfo = HomeInfo(
            name = "Alex Doe",
            tagline = "Versatile Developer |\nCreative Solutions",
            location = "Anytown, USA",
            yearsExperience = 5,
            email = "alex.doe@example.com",
            phoneNumber = "(555) 123-4567",
            linkedInUrl = "https://linkedin.com",
            githubUrl = "https://github.com",
            profilePictureUrl = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fcartoon-profile-picture.jpg?alt=media&token=d0d33a27-5347-43df-918f-01a327c62e6d"
        ),
    ),
    experiences = listOf(
        JobExperience(
            resumeOwnerId = "",
            company = "Tech Solutions Inc.",
            title = "Software Engineer",
            startDate = "2021",
            endDate = "Present",
            responsibilities = listOf(
                "Developed and maintained web applications using modern frameworks.",
                "Collaborated with cross-functional teams to deliver high-quality software.",
                "Participated in code reviews and contributed to a positive team environment.",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit."
            ),
            technologies = listOf("Kotlin", "Java", "Spring Boot", "React", "Docker", "AWS")
        ),
        JobExperience(
            resumeOwnerId = "",
            company = "Innovatech Ltd.",
            title = "Junior Developer",
            startDate = "2019",
            endDate = "2021",
            responsibilities = listOf(
                "Assisted senior developers in various project tasks.",
                "Wrote unit tests and performed initial QA.",
                "Gained experience in agile development methodologies.",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem."
            ),
            technologies = listOf("Python", "Django", "JavaScript", "HTML", "CSS")
        )
    ),
    skills = listOf(
        Skill(resumeOwnerId = "", category = "Programming Languages", name = "Kotlin"),
        Skill(resumeOwnerId = "", category = "Programming Languages", name = "Java"),
        Skill(resumeOwnerId = "", category = "Programming Languages", name = "Python"),
        Skill(resumeOwnerId = "", category = "Web Development", name = "Spring Boot"),
        Skill(resumeOwnerId = "", category = "Web Development", name = "React"),
        Skill(resumeOwnerId = "", category = "Web Development", name = "HTML/CSS"),
        Skill(resumeOwnerId = "", category = "DevOps", name = "Docker"),
        Skill(resumeOwnerId = "", category = "Cloud", name = "AWS Basics")
    ),
    educationEntries = listOf(
        EducationEntry(
            resumeOwnerId = "",
            institution = "State University",
            degree = "B.S. Computer Science",
            yearsAttended = "2015 - 2019",
            description = "Graduated with honors. Relevant coursework in data structures, algorithms, and software design.",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fzhanhui-li-1iuxWsIZ6ko-unsplash.jpg?alt=media&token=c4a3d79a-254d-4e96-af27-069b908e483e",
        ),
        EducationEntry(
            resumeOwnerId = "",
            institution = "Online Coding Bootcamp",
            degree = "Full-Stack Web Development Certificate",
            yearsAttended = "2018",
            description = "Intensive program covering front-end and back-end technologies.",
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fgrowtika-YOEHA0Ou8ZY-unsplash.jpg?alt=media&token=73fb53da-c5eb-4c27-8854-2adb8c2b88c5",
        )
    ),
    references = listOf(
        Reference(
            resumeOwnerId = "",
            name = "Dr. Jane Smith",
            roleWhenWorked = "Professor / Project Advisor",
            description = "My favorite professor, I learned a lot",
            yearsWorkedTogether = "2 Years",
            linkedInProfileUrl = "https://linkedin.com/",
            profilePictureUrl = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fluthfi-alfarizi-yXAGGbVuhEY-unsplash.jpg?alt=media&token=fdfc780c-663c-4dd8-9805-a68946f00500 "
        ),
        Reference(
            resumeOwnerId = "",
            name = "Bob Johnson",
            roleWhenWorked = "Senior Manager at Tech Solutions Inc.",
            description = "The best manager I've ever worked with. He guided me through the entire development process and introduced me to new technologies. He encouraged me to constantly grow and to build networks.",
            yearsWorkedTogether = "3 Years",
            linkedInProfileUrl = "https://linkedin.com/",
            profilePictureUrl = "https://firebasestorage.googleapis.com/v0/b/resume-3e51d.firebasestorage.app/o/resume-images%2Fluthfi-alfarizi-jlJpDBK17Hw-unsplash.jpg?alt=media&token=40e6da85-cc95-4e66-8801-a513b1c68651"
        )
    )
)