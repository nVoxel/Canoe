package com.voxeldev.canoe.utils.integration

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @author nvoxel
 */
abstract class BaseUseCase<in Params, out Type>(
    private val asyncDispatcher: CoroutineDispatcher = Dispatchers.IO,
) where Type : Any {

    abstract suspend fun run(params: Params): Result<Type>

    @OptIn(DelicateCoroutinesApi::class)
    operator fun invoke(
        params: Params,
        scope: CoroutineScope = GlobalScope,
        asyncDispatcher: CoroutineDispatcher = this.asyncDispatcher,
        onResult: suspend (Result<Type>) -> Unit = {},
    ) {
        scope.launch(Dispatchers.Main) {
            val deferred = async(asyncDispatcher) {
                run(params)
            }
            onResult(deferred.await())
        }
    }

    object NoParams
}
