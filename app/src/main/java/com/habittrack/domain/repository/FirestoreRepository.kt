package com.habittrack.domain.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.habittrack.data.local.HabitEntity
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun getUserId(): String? = auth.currentUser?.uid

    suspend fun syncHabitToCloud(habit: HabitEntity) {
        val uid = getUserId() ?: return
        val habitMap = mapOf(
            "id"             to habit.id,
            "name"           to habit.name,
            "category"       to habit.category,
            "color"          to habit.color,
            "frequencyDays"  to habit.frequencyDays,
            "createdAt"      to habit.createdAt,
            "isActive"       to habit.isActive
        )
        db.collection("users")
            .document(uid)
            .collection("habits")
            .document(habit.id.toString())
            .set(habitMap)
            .await()
    }

    suspend fun deleteHabitFromCloud(habitId: Int) {
        val uid = getUserId() ?: return
        db.collection("users")
            .document(uid)
            .collection("habits")
            .document(habitId.toString())
            .delete()
            .await()
    }

    suspend fun getHabitsFromCloud(): List<HabitEntity> {
        val uid = getUserId() ?: return emptyList()
        val snapshot = db.collection("users")
            .document(uid)
            .collection("habits")
            .get()
            .await()
        return snapshot.documents.mapNotNull { doc ->
            try {
                HabitEntity(
                    id            = (doc.getLong("id") ?: 0L).toInt(),
                    name          = doc.getString("name") ?: "",
                    category      = doc.getString("category") ?: "",
                    color         = doc.getString("color") ?: "#2E7D32",
                    frequencyDays = doc.getString("frequencyDays") ?: "[0,1,2,3,4]",
                    createdAt     = doc.getLong("createdAt") ?: 0L,
                    isActive      = doc.getBoolean("isActive") ?: true
                )
            } catch (e: Exception) {
                null
            }
        }
    }
}