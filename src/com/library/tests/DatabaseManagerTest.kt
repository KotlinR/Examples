package com.library.tests

import com.library.db.DatabaseDao
import com.library.db.DatabaseManager
import java.util.concurrent.Executors


object DatabaseManagerTest {

    @JvmStatic
    fun main(args: Array<String>) {
        `Given database When change ownership And update database Then assert old database ownership equals new database ownership`()
    }

    private fun `Given database When change ownership And update database Then assert old database ownership equals new database ownership`() {
        // SET UP
        val executorService = Executors.newSingleThreadExecutor()
        val path = "src/com/library/test.db"
        val databaseManager = DatabaseManager(path, executorService)
        val dbBeforeUpdate = databaseManager.getDatabase()
        val dao = DatabaseDao(dbBeforeUpdate, executorService)

        // TEST
        dao.addBookToClient(
            clientId = dao.getAllClients().random().id,
            bookId = dao.getAllBooks().random().id,
        )
        dao.confiscateBooks(dao.getOwnershipInfo().random().client.id)
        databaseManager.updateDatabase(dbBeforeUpdate)

        val dbAfterUpdate = databaseManager.getDatabase()
        require(dbAfterUpdate.ownership == dbBeforeUpdate.ownership) {
            "Different ownership"
        }
    }
}
