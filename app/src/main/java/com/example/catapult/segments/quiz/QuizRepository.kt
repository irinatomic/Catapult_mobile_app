package com.example.catapult.segments.quiz

import kotlinx.coroutines.*
import kotlin.random.Random
import com.example.catapult.data.database.CatapultDatabase
import com.example.catapult.data.database.entities.ImageDbModel
import com.example.catapult.data.mapper.asImageDbModel
import com.example.catapult.networking.api.BreedsApi
import com.example.catapult.networking.retrofit
import com.example.catapult.segments.quiz.question_screen.Question

object QuizRepository {

    private val database by lazy { CatapultDatabase.database }
    private val breedsApi: BreedsApi = retrofit.create(BreedsApi::class.java)

    private var temperaments: List<String> = listOf()

    suspend fun generateQuestions(): List<Question> = coroutineScope {
        val questions = mutableListOf<Deferred<Question>>()
        repeat(5) {
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
     * Generates a question of type 1: "Which breed is this?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeOne(): Question {
        val breed = database.breedDao().getAll().random()

        val images = fetchImagesForBreed(breed.id)

        val imageUrl = images[0].url
        val answers = database.breedDao().getAll().shuffled().take(3).map { it.name.lowercase() } + breed.name.lowercase()
        return Question("Which breed is this?", imageUrl, answers.shuffled(), breed.name.lowercase())
    }

    /**
     * Generates a question of type 2: "Which temperament does not belong to this breed?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeTwo(): Question {
        val breed = database.breedDao().getAll().random()

        val images = fetchImagesForBreed(breed.id)

        val imageUrl = images[0].url
        val correctTemperament = breed.temperament.split(",").map{ it.lowercase() }.random().trim()
        val wrongTemperaments = fetchTemperaments().filter { it != correctTemperament }.shuffled().take(3)
        val answers = wrongTemperaments + correctTemperament
        return Question("Which temperament does not belong to this breed?", imageUrl, answers.shuffled(), correctTemperament)
    }

    /**
     * Generates a question of type 3: "Which temperament belongs to this breed?"
     * Shows an image of a random breed and 4 possible answers.
     */
    private suspend fun generateTypeThree(): Question {
        val breed = database.breedDao().getAll().random()

        val images = fetchImagesForBreed(breed.id)

        val imageUrl = images[0].url
        val correctTemperament = breed.temperament.split(",").map{ it.lowercase() }.random().trim()
        val wrongTemperaments = fetchTemperaments().filter { it != correctTemperament }.shuffled().take(3)
        val answers = wrongTemperaments + correctTemperament
        return Question("Which temperament belongs to this breed?", imageUrl, answers.shuffled(), correctTemperament)
    }

    /**
     * Fetches images for a breed from the API and saves them to the database.
     * If the images are already in the database, returns them from the database.
     */
    private suspend fun fetchImagesForBreed(breedId: String): List<ImageDbModel> {
        var images = database.imageDao().getAllForBreed(breedId)

        if (images.isEmpty()) {
            val imagesApi = breedsApi.getImagesForBreed(breedId)
            database.imageDao().insertAll(imagesApi.map { it.asImageDbModel(breedId) })

            images = database.imageDao().getAllForBreed(breedId)
        }

        return images
    }

    private suspend fun fetchTemperaments():List<String> {
        if (temperaments.isEmpty())
            temperaments = database.breedDao().getAllTemperaments()
        return temperaments
    }
}