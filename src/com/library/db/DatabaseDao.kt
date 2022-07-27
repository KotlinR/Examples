package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.db.entities.OwnershipEntity.Companion.assign
import com.library.utills.checkNotAMainThread
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import kotlin.concurrent.thread

class DatabaseDao(private val database: Database, private val executorService: ExecutorService) {

    fun getAllClients(): List<ClientEntity> {
        return executorService.submit(Callable { database.clients }).get()
    }

    fun getAllBooks(): List<BookEntity> {
        return executorService.submit(Callable { database.books }).get()
    }

    fun getOwnershipInfo(): List<OwnershipEntity> {
        return executorService.submit(Callable { database.ownership }).get()
    }

    fun addBookToClient(clientId: String, bookId: String) {
        executorService.execute {
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
        }
    }

    fun confiscateBooks(clientId: String) {
        executorService.execute {
            checkNotAMainThread()
            database.ownership.firstOrNull { it.client.id == clientId }?.let {
                database.ownership.remove(it)
            }
        }
    }
}
