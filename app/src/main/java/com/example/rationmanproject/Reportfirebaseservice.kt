package com.example.rationmanproject

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class Reportfirebaseservice @Inject constructor(private val instance : FirebaseFirestore) {

    suspend fun addReport(shopId: String, report: Report): Boolean {
        return try {
            instance.collection("shops")
                .document(shopId)
                .collection("reports")
                .add(report)
                .await()  // Use kotlinx-coroutines-play-services
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getreports(shopId: String): Flow<List<Report>> = callbackFlow {
        val listener = instance.collection("shops")
            .document(shopId)
            .collection("reports")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error) // stop flow with error
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val reports = snapshot.toObjects(Report::class.java)
                    trySend(reports) // emit into the flow
                }
            }

        // Close listener when flow is cancelled
        awaitClose { listener.remove() }
    }





}