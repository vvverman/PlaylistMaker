package com.example.playlistmaker.ui.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Создает функцию дебаунса, которая откладывает выполнение указанной действия до тех пор,
 * пока не пройдет указанный задержка с момента последнего вызова дебаунсовой функции.
 *
 * @param delayMillis Задержка в миллисекундах перед выполнением действия.
 * @param coroutineScope Область видимости, в которой должен быть запущен дебаунс-задача.
 * @param useLastParam Флаг, указывающий, следует ли использовать последний переданный параметр.
 * Если false, действие будет выполнено только один раз после прохождения задержки, независимо от переданного параметра.
 * @param action Действие, которое должно быть выполнено после задержки.
 * @return Функция, принимающая параметр и выполняющая действие после указанной задержки.
 */

fun <T> debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    useLastParam: Boolean = true,
    action: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (useLastParam) {
            debounceJob?.cancel()
        }
        if (debounceJob?.isCompleted != false || useLastParam) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                action(param)
            }
        }
    }
}