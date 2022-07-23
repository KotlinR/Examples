package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.db.entities.OwnershipEntity.Companion.assign
import com.library.utills.checkNotAMainThread
import kotlin.concurrent.thread

class DatabaseDao(private val database: Database) {

    fun getAllClients(): List<ClientEntity> {
        var clients = mutableListOf<ClientEntity>()
        thread {
            checkNotAMainThread()
            clients = database.clients
        }.join()
        return clients
    }

    fun getAllBooks(): List<BookEntity> {
        var books = mutableListOf<BookEntity>()
        thread {
            checkNotAMainThread()
            books = database.books
        }.join()
        return books
    }

    fun getOwnershipInfo(): List<OwnershipEntity> {
        var ownership = mutableListOf<OwnershipEntity>()
        thread {
            checkNotAMainThread()
            ownership = database.ownership
        }.join()
        return ownership
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
        thread {
            checkNotAMainThread()
            database.ownership.firstOrNull { it.client.id == clientId }?.let {
                database.ownership.remove(it)
            }
        }.join()
    }
}
