package com.library.db

import com.library.db.entities.BookEntity
import com.library.db.entities.ClientEntity
import com.library.db.entities.OwnershipEntity
import com.library.db.entities.OwnershipEntity.Companion.assign
import com.library.utills.checkNotAMainThread
import java.io.*
import java.util.*
import kotlin.concurrent.thread
import kotlin.random.Random

class DatabaseManager(private val path: String = DB_PATH) {

    private companion object {
        const val DB_PATH = "src/com/library/LibraryDatabase.db"
    }

    fun getDatabase(): Database {
        var result: Database? = null
        thread {
            result = runCatching {
                if (isDatabaseExist()) {
                    readDatabaseFromFile()
                } else {
                    createNewDatabase()
                }
            }.getOrNull() ?: run {
                createNewDatabase()
            }
        }.join()
        return requireNotNull(result)
    }

    fun updateDatabase(database: Database) {
        thread {
            checkNotAMainThread()
            val file = File(path)
            ObjectOutputStream(BufferedOutputStream(FileOutputStream(file))).use {
                it.writeObject(database)
                it.flush()
            }
        }.join()
    }

    private fun isDatabaseExist(): Boolean {
        return File(path).exists()
    }

    private fun readDatabaseFromFile(): Database {
        return ObjectInputStream(BufferedInputStream(FileInputStream(File(path)))).use {
            it.readObject() as Database
        }
    }

    private fun createNewDatabase(): Database {
            checkNotAMainThread()
            val clients = Collections.synchronizedList(
                mutableListOf(
                    ClientEntity(
                        id = "LC-001",
                        name = "Вася",
                        surname = "Шлёпкин",
                    ),
                    ClientEntity(
                        id = "LC-002",
                        name = "Петя",
                        surname = "Лупа",
                    ),
                    ClientEntity(
                        id = "LC-003",
                        name = "Ваня",
                        surname = "Пупа",
                    ),
                    ClientEntity(
                        id = "LC-004",
                        name = "Вова",
                        surname = "Рубликов",
                    ),
                    ClientEntity(
                        id = "LC-005",
                        name = "Саша",
                        surname = "Зябликов",
                    ),
                    ClientEntity(
                        id = "LC-006",
                        name = "Маша",
                        surname = "Простоквашина",
                    ),
                    ClientEntity(
                        id = "LC-007",
                        name = "Наташа",
                        surname = "Хлебушкина",
                    ),
                    ClientEntity(
                        id = "LC-008",
                        name = "Даша",
                        surname = "Собачкина",
                    ),
                    ClientEntity(
                        id = "LC-009",
                        name = "Ксюша",
                        surname = "Магро",
                    ),
                    ClientEntity(
                        id = "LC-010",
                        name = "Максим",
                        surname = "Родин",
                    ),
                    ClientEntity(
                        id = "LC-011",
                        name = "Константин",
                        surname = "Евгеньев",
                    ),
                    ClientEntity(
                        id = "LC-012",
                        name = "Инокентий",
                        surname = "Попугаев",
                    ),
                    ClientEntity(
                        id = "LC-013",
                        name = "Эдуард",
                        surname = "Радзинский",
                    ),
                    ClientEntity(
                        id = "LC-014",
                        name = "Гена",
                        surname = "Крокодилов",
                    ),
                    ClientEntity(
                        id = "LC-015",
                        name = "Изя",
                        surname = "Ватман",
                    ),
                    ClientEntity(
                        id = "LC-016",
                        name = "Шлома",
                        surname = "Битонман",
                    ),
                    ClientEntity(
                        id = "LC-017",
                        name = "Абрам",
                        surname = "Голдман",
                    ),
                    ClientEntity(
                        id = "LC-018",
                        name = "Мойша",
                        surname = "Брумбович",
                    ),
                    ClientEntity(
                        id = "LC-019",
                        name = "Вацлав",
                        surname = "Цукерман",
                    ),
                    ClientEntity(
                        id = "LC-020",
                        name = "Коля",
                        surname = "Шниерсон",
                    ),
                )
            )

            val books = Collections.synchronizedList(
                mutableListOf(
                    BookEntity(id = "LB-001", title = "Книга-Нига", year = "2018"),
                    BookEntity(id = "LB-002", title = "Книга-Нига", year = "2018"),
                    BookEntity(id = "LB-003", title = "Книга-Нига", year = "2018"),
                    BookEntity(id = "LB-004", title = "Книга-Нига", year = "2018"),
                    BookEntity(id = "LB-005", title = "Ку-Клукс-Книга", year = "1488"),
                    BookEntity(id = "LB-006", title = "Ку-Клукс-Книга", year = "1488"),
                    BookEntity(id = "LB-007", title = "Ку-Клукс-Книга", year = "1488"),
                    BookEntity(id = "LB-008", title = "Ку-Клукс-Книга", year = "1488"),
                    BookEntity(id = "LB-009", title = "ЖИ есть или нет?", year = "2002"),
                    BookEntity(id = "LB-010", title = "ЖИ есть или нет?", year = "2002"),
                    BookEntity(id = "LB-011", title = "ЖИ есть или нет?", year = "2002"),
                    BookEntity(id = "LB-012", title = "ЖИ есть или нет?", year = "2002"),
                    BookEntity(
                        id = "LB-013",
                        title = "Срём не снимая штанов. Для чайниковю.",
                        year = "1999"
                    ),
                    BookEntity(
                        id = "LB-014",
                        title = "Срём не снимая штанов. Для чайниковю.",
                        year = "1999",
                    ),
                    BookEntity(
                        id = "LB-015",
                        title = "Срём не снимая штанов. Для чайниковю.",
                        year = "1999"
                    ),
                    BookEntity(
                        id = "LB-016",
                        title = "Срём не снимая штанов. Для чайниковю.",
                        year = "1999"
                    ),
                    BookEntity(
                        id = "LB-017",
                        title = "Программист за 3 месяца - ЛЕГКО",
                        year = "2022"
                    ),
                    BookEntity(
                        id = "LB-018",
                        title = "Программист за 3 месяца - ЛЕГКО",
                        year = "2022"
                    ),
                    BookEntity(
                        id = "LB-019",
                        title = "Программист за 3 месяца - ЛЕГКО",
                        year = "2022"
                    ),
                    BookEntity(
                        id = "LB-020",
                        title = "Программист за 3 месяца - ЛЕГКО",
                        year = "2022"
                    ),
                )
            )

            val booksForRandom = books.toMutableList()

            val ownership = Collections.synchronizedList(
                clients.mapNotNull {
                    if (Random.nextBoolean()) {
                        val bookInUse = booksForRandom.randomOrNull()
                        booksForRandom.remove(bookInUse)
                        if (bookInUse != null) it assign bookInUse else null
                    } else {
                        null
                    }
                }.toMutableList()
            )
        return Database(
            books = books,
            clients = clients,
            ownership = ownership,
        ).also {
            updateDatabase(it)
        }
    }
}
