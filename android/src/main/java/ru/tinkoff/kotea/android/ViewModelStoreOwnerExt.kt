package ru.tinkoff.kotea.android

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import ru.tinkoff.kotea.core.Store
import kotlin.coroutines.CoroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Lazily gets kotea [Store] from the [ViewModelStore].
 * Launches it in the [viewModelScope][ViewModel.viewModelScope] with the specified [dispatcher] on first access.
 *
 * By default uses a containing class and property names as a [ViewModel] key,
 * but you can share [Store] across a screens by specifying the [sharedViewModelKey].
 */
fun <T : Store<*, *, *>> storeViaViewModel(
    coroutineContext: CoroutineContext = Dispatchers.Default,
    sharedViewModelKey: String? = null,
    factory: () -> T
): ReadOnlyProperty<ViewModelStoreOwner, T> {
    return object : ReadOnlyProperty<ViewModelStoreOwner, T> {
        private var value: T? = null

        override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
            return value ?: run {
                val key = sharedViewModelKey ?: keyFromProperty(thisRef, property)
                val vm = thisRef.viewModelStore.get(key) { StoreViewModel(factory(), coroutineContext) }
                vm.store.also { value = it }
            }
        }

        private fun keyFromProperty(thisRef: ViewModelStoreOwner, property: KProperty<*>): String {
            return thisRef::class.java.canonicalName!! + "#" + property.name
        }
    }
}

private class StoreViewModel<T : Store<*, *, *>>(
    val store: T,
    coroutineContext: CoroutineContext
) : ViewModel() {
    init {
        store.launchIn(viewModelScope + coroutineContext)
    }
}

private inline fun <reified T : ViewModel> ViewModelStore.get(key: String, crossinline factory: () -> T): T {
    val viewModelFactory = object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <VM : ViewModel> create(modelClass: Class<VM>) = factory() as VM
    }
    return ViewModelProvider(this, viewModelFactory).get(key, T::class.java)
}
