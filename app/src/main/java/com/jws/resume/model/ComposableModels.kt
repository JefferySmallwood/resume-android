package com.jws.resume.model

import com.jws.resume.util.getInt
import com.jws.resume.util.getString
import com.jws.resume.util.getStringList
import com.jws.resume.util.mapToTypedList

data class HomeInfo(
    val name: String,
    val tagline: String,
    val location: String,
    val yearsExperience: Int,
    val email: String,
    val phoneNumber: String,
    val linkedInUrl: String,
    val githubUrl: String,
    val profilePictureUrl: String? = null
)

data class JobExperience(
    val company: String,
    val title: String,
    val startDate: String,
    val endDate: String?,
    val responsibilities: List<String>,
    val technologies: List<String>
)

data class Skill(
    val category: String,
    val name: String
)

data class EducationEntry(
    val institution: String,
    val degree: String,
    val yearsAttended: String,
    val description: String,
    val imageUrl: String? = null,
    val contentDescription: String? = null
)

data class Reference(
    val name: String,
    val roleWhenWorked: String,
    val description: String? = null,
    val yearsWorkedTogether: String,
    val linkedInProfileUrl: String,
    val profilePictureUrl: String? = null
)

data class Resume(
    val homeInfo: HomeInfo,
    val experience: List<JobExperience>,
    val skills: List<Skill>,
    val education: List<EducationEntry>,
    val references: List<Reference>
)

@Suppress("UNCHECKED_CAST")
fun mapFirestoreDataToResume(outerData: Map<String, Any>?): Resume? {
    if (outerData == null) {
        return null
    }

    val actualResumeData = outerData["data"] as? Map<String, Any>

    if (actualResumeData == null) {
        return null
    }

    val homeInfoMap = actualResumeData["homeInfo"] as? Map<String, Any>

    val homeInfo = homeInfoMap?.let {
        HomeInfo(
            name = it.getString("name") ?: "",
            tagline = it.getString("tagline") ?: "",
            location = it.getString("location") ?: "",
            yearsExperience = it.getInt("yearsExperience") ?: 0,
            email = it.getString("email") ?: "",
            phoneNumber = it.getString("phoneNumber") ?: "",
            linkedInUrl = it.getString("linkedInUrl") ?: "",
            githubUrl = it.getString("githubUrl") ?: "",
            profilePictureUrl = it.getString("profilePictureUrl")
        )
    } ?: HomeInfo(
        name = "",
        tagline = "",
        location = "",
        yearsExperience = 0,
        email = "",
        phoneNumber = "",
        linkedInUrl = "",
        githubUrl = "",
        profilePictureUrl = null
    )

    val experienceList = (actualResumeData["experience"] as? List<*>)?.mapToTypedList { expMap ->
        JobExperience(
            company = expMap.getString("company") ?: "",
            title = expMap.getString("title") ?: "",
            startDate = expMap.getString("startDate") ?: "",
            endDate = expMap.getString("endDate") ?: "",
            responsibilities = expMap.getStringList("responsibilities") ?: emptyList(),
            technologies = expMap.getStringList("technologies") ?: emptyList()
        )
    } ?: emptyList()

    val skillsList = (actualResumeData["skills"] as? List<*>)?.mapToTypedList { skillMap ->
        Skill(
            category = skillMap.getString("category") ?: "",
            name = skillMap.getString("name") ?: ""
        )
    } ?: emptyList()

    val educationList = (actualResumeData["education"] as? List<*>)?.mapToTypedList { eduMap ->
        EducationEntry(
            institution = eduMap.getString("institution") ?: "",
            degree = eduMap.getString("degree") ?: "",
            yearsAttended = eduMap.getString("yearsAttended") ?: "",
            description = eduMap.getString("description") ?: "",
            imageUrl = eduMap.getString("imageUrl") ?: ""
        )
    } ?: emptyList()

    val referencesList = (actualResumeData["references"] as? List<*>)?.mapToTypedList { refMap ->
        Reference(
            name = refMap.getString("name") ?: "",
            roleWhenWorked = refMap.getString("roleWhenWorked") ?: "",
            description = refMap.getString("description") ?: "",
            yearsWorkedTogether = refMap.getString("yearsWorkedTogether") ?: "", // Adjust if it's Int
            linkedInProfileUrl = refMap.getString("linkedInProfileUrl") ?: "",
            profilePictureUrl = refMap.getString("profilePictureUrl") ?: ""
        )
    } ?: emptyList()

    return Resume(
        homeInfo = homeInfo,
        experience = experienceList,
        skills = skillsList,
        education = educationList,
        references = referencesList
    )
}