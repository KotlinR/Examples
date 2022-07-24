package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.db.entities.OwnershipEntity.Companion.assign
import com.library.utills.checkNotAMainThread
import kotlin.concurrent.thread

class DatabaseDao(private val database: Database) {

    fun getAllClients(): List<ClientEntity> {
        return database.clients.getFromThread()
    }

    fun getAllBooks(): List<BookEntity> {
        return database.books.getFromThread()
    }

    fun getOwnershipInfo(): List<OwnershipEntity> {
        return database.ownership.getFromThread()
    }

    fun addBookToClient(clientId: String, bookId: String) {
        thread {
            checkNotAMainThread()
            var ownership: OwnershipEntity? = database.ownership
                .firstOrNull { it.client.id == clientId }
            val book = database.books.first { it.id == bookId }
            if (ownership != null) {
                ownership.booksInUse.add(book)
            } else {
                val client = database.clients.first { it.id == clientId }
                ownership = client assign book
            }
            database.ownership.add(ownership)
        }.join()
    }

    fun confiscateBooks(clientId: String) {
        checkNotAMainThread()
        database.ownership.getFromThread().firstOrNull { it.client.id == clientId }?.let {
            database.ownership.remove(it)
        }
    }

    private fun <T> MutableList<T>.getFromThread(): List<T> {
        var result = mutableListOf<T>()
        thread {
            result = this
        }
        return result
    }
}
