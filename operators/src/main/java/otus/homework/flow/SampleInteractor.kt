package otus.homework.flow

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@ExperimentalCoroutinesApi
class SampleInteractor(
    private val sampleRepository: SampleRepository
) {

    /**
     * Реализуйте функцию task1 которая последовательно:
     * 1) возводит числа в 5ую степень
     * 2) убирает чила <= 20
     * 3) убирает четные числа
     * 4) добавляет постфикс "won"
     * 5) берет 3 первых числа
     * 6) возвращает результат
     */
    fun task1(): Flow<String> {
        return sampleRepository.produceNumbers()
            .transform<Int, Int> { number ->
                val result = number * 5
                emit(result.toInt())
            }
            .transform { number ->
                if (number >= 20) {
                    emit(number)
                }
            }
            .transform { number ->
                if (number % 2 != 0) {
                    emit(number)
                }
            }
            .transform { number ->
                emit("$number won")
            }
            .take(3)
    }

    /**
     * Классическая задача FizzBuzz с небольшим изменением.
     * Если входное число делится на 3 - эмитим само число и после него эмитим строку Fizz
     * Если входное число делится на 5 - эмитим само число и после него эмитим строку Buzz
     * Если входное число делится на 15 - эмитим само число и после него эмитим строку FizzBuzz
     * Если число не делится на 3,5,15 - эмитим само число
     */
    fun task2(): Flow<String> {
        return sampleRepository.produceNumbers()
            .transform { number ->
                if (number % 3 == 0 && number % 5 == 0) {
                    emit(number.toString())
                    emit("FizzBuzz")
                } else if (number % 5 == 0 || number % 3 == 0) {
                    when {
                        number % 3 == 0 -> {
                            emit(number.toString())
                            emit("Fizz")
                        }
                        number % 5 == 0 -> {
                            emit(number.toString())
                            emit("Buzz")
                        }
                        else -> {
                            emit(number.toString())
                        }
                    }
                } else {
                    emit(number.toString())
                }
            }
    }

    /**
     * Реализуйте функцию task3, которая объединяет эмиты из двух flow и возвращает кортеж Pair<String,String>(f1,f2),
     * где f1 айтем из первого флоу, f2 айтем из второго флоу.
     * Если айтемы в одно из флоу кончились то результирующий флоу также должен закончится
     */
    fun task3(): Flow<Pair<String, String>> {
        return sampleRepository.produceColors()
            .zip(sampleRepository.produceForms()) { first, second ->
                Pair(first, second)
            }
    }

    /**
     * Реализайте функцию task4, которая обрабатывает IllegalArgumentException и в качестве фоллбека
     * эмитит число -1.
     * Если тип эксепшена != IllegalArgumentException, пробросьте его дальше
     * При любом исходе, будь то выброс исключения или успешная отработка функции вызовите метод dotsRepository.completed()
     */
    fun task4(): Flow<Int> {
        return sampleRepository.produceNumbers()
            .catch { e ->
                if (e is IllegalArgumentException) {
                    emit(-1)
                } else {
                    throw e
                }
            }
            .onCompletion { sampleRepository.completed() }
    }
}