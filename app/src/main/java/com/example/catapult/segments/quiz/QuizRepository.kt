package com.example.catapult.segments.quiz

import com.example.catapult.data.database.AppDatabase
import com.example.catapult.data.database.entities.BreedDbModel
import com.example.catapult.data.database.entities.ResultDbModel
import com.example.catapult.data.datastore.UserStore
import kotlinx.coroutines.*
import kotlin.random.Random
import com.example.catapult.data.mapper.asImageDbModel
import com.example.catapult.networking.endpoints.BreedsApi
import com.example.catapult.segments.quiz.question_screen.QuizQuestionContract.*
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val breedsApi: BreedsApi,
    private val database: AppDatabase,
    private val store: UserStore
) {

    private var temperaments: List<String> = listOf()

    suspend fun generateQuestions(): List<Question> = coroutineScope {
        val questions = mutableListOf<Deferred<Question>>()
        repeat(20) {
            questions += async {
                withContext(Dispatchers.IO) {
                    when (Random.nextInt(3)) {
                        0 -> generateTypeOne()
                        1 -> generateTypeTwo()
                        else -> generateTypeThree()
                    }
                }
            }
        }
        questions.awaitAll()
    }

    /**
     * Fetches images for a breed from the API and saves them to the database.
     * If the images are already in the database, returns them from the database.
     */
     suspend fun fetchImagesForBreed(q: Question) {
         val breedId = q.breedId
         var images = database.imageDao().getAllForBreed(breedId)

         if (images.isEmpty()) {
             val imagesApi = breedsApi.getImagesForBreed(breedId)
             database.imageDao().insertAll(imagesApi.map { it.asImageDbModel(breedId) })

             images = database.imageDao().getAllForBreed(breedId)
         }

         q.breedImageUrl = images.random().url
    }

    suspend fun submitResultToDatabase(score: Double) {
        withContext(Dispatchers.IO) {
            database.resultDao().insert(
                ResultDbModel(
                    nickname = store.getUserData().nickname,
                    result = score,
                    createdAt = System.currentTimeMillis(),
                    published = false
                )
            )
        }
    }

    /**
     * Generates a question of type 1: "Which breed is this?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeOne(): Question {
        val breed = chooseRandomBreed()     //  correct answer

        val allBreeds = database.breedDao().getAll()
        val answers = allBreeds.filter { it != breed }.shuffled().take(3).map { it.name.lowercase() } + breed.name.lowercase()
        return Question("Which breed is this?", breed.id, "", answers, breed.name.lowercase())
    }

    /**
     * Generates a question of type 2: "Which temperament does not belong to this breed?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeTwo(): Question {
        val breed = chooseRandomBreed()       //  correct answer

        val correctTemperament = breed.temperament.split(",").map { it.lowercase() }.random().trim()
        val wrongTemperaments = fetchTemperaments().filter { it != correctTemperament }.shuffled().take(3)
        val falseTemperament = wrongTemperaments.random()
        val answers = wrongTemperaments + correctTemperament
        return Question("Which temperament does not belong to this breed?", breed.id, "", answers.shuffled(), falseTemperament)
    }

    /**
     * Generates a question of type 3: "Which temperament belongs to this breed?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeThree(): Question {
        val breed = chooseRandomBreed()       //  correct answer

        val correctTemperament = breed.temperament.split(",").map { it.lowercase() }.random().trim()
        val wrongTemperaments = fetchTemperaments().filter { it != correctTemperament }.shuffled().take(3)
        val answers = wrongTemperaments + correctTemperament
        return Question("Which temperament belongs to this breed?", breed.id, "", answers.shuffled(), correctTemperament)
    }

    private suspend fun chooseRandomBreed(): BreedDbModel {
        val excludedIds = listOf("mala")

        val breedsList = database.breedDao().getAll().filter { it.id !in excludedIds}
        return breedsList.random()
    }

    private suspend fun fetchTemperaments():List<String> {
        if (temperaments.isEmpty())
            temperaments = database.breedDao().getAllTemperaments()
        return temperaments
    }
}